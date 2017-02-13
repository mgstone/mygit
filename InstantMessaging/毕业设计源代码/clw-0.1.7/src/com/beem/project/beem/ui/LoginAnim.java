
package com.beem.project.beem.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beem.project.beem.R;
import com.beem.project.beem.service.LoginAsyncTask;
import com.beem.project.beem.service.aidl.IXmppFacade;

import de.duenndns.ssl.MemorizingTrustManager;

/**
 * This class is an activity which display an animation during the connection with the server.
 * @author Da Risk <darisk972@gmail.com>
 */
public class LoginAnim extends Activity {

    private static final String TAG = "LoginAnim";
    private static final Intent SERVICE_INTENT = new Intent();
    private static final int RECEIVER_PRIORITY = 50;
    static {
	SERVICE_INTENT.setComponent(new ComponentName("com.beem.project.beem", "com.beem.project.beem.BeemService"));
    }
    private ImageView mLogo;
    private Animation mRotateAnim;
    private final ServiceConnection mServConn = new LoginServiceConnection();
    private IXmppFacade mXmppFacade;
    private AsyncTask<IXmppFacade, Integer, Boolean> mTask;
    private Button mCancelBt;
    private TextView mLoginState;
    private boolean mBinded;
    private BroadcastReceiver mSslReceiver;

    /**
     * Constructor.
     */
    public LoginAnim() {
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login_anim);
	mLoginState = (TextView) findViewById(R.id.loginanim_status_text);
	mLogo = (ImageView) findViewById(R.id.loginanim_logo_anim);
	mRotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_and_scale);
	mCancelBt = (Button) findViewById(R.id.loginanim_cancel_button);
	mCancelBt.setOnClickListener(new ClickListener());
	mSslReceiver = new BroadcastReceiver() {
	    public void onReceive(Context ctx, Intent i) {
		try {
		    Log.i(TAG, "Interception the SSL notification");
		    PendingIntent pi = i.getParcelableExtra(MemorizingTrustManager.INTERCEPT_DECISION_INTENT_LAUNCH);
		    pi.send();
		    abortBroadcast();
		} catch (PendingIntent.CanceledException e) {
		    Log.e(TAG, "Error while displaying the SSL dialog", e);
		}
	    }
	};
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
	super.onStart();
	mLogo.startAnimation(mRotateAnim);
	if (mTask == null)
	    mTask = new LoginTask();
	if (!mBinded)
	    mBinded = bindService(LoginAnim.SERVICE_INTENT, mServConn, BIND_AUTO_CREATE);
	IntentFilter filter = new IntentFilter(MemorizingTrustManager.INTERCEPT_DECISION_INTENT
		+ "/" + getPackageName());
	filter.setPriority(RECEIVER_PRIORITY);
	registerReceiver(mSslReceiver, filter);

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onStop() {
	super.onStop();
	if (mBinded && mTask.getStatus() != AsyncTask.Status.RUNNING) {
	    unbindService(mServConn);
	    mXmppFacade = null;
	    mBinded = false;
	}
	unregisterReceiver(mSslReceiver);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO use onBackPressed on Eclair (2.0)
	if (keyCode == KeyEvent.KEYCODE_BACK && mTask.getStatus() != AsyncTask.Status.FINISHED) {
	    if (!mTask.cancel(true)) {
		Log.d(TAG, "Can't interrupt the connection");
	    }
	    setResult(Activity.RESULT_CANCELED);
	}
	return super.onKeyDown(keyCode, event);
    }

    /**
     * Click event listener on cancel button.
     */
    private class ClickListener implements OnClickListener {

	/**
	 * Constructor.
	 */
	ClickListener() {
	}

	public void onClick(View v) {
	    if (v == mCancelBt) {
		if (!mTask.cancel(true)) {
		    Log.d(TAG, "Can't interrupt the connection");
		}
		setResult(Activity.RESULT_CANCELED);
		finish();
	    }
	}
    }

    /**
     * Asynchronous class for connection.
     */
    private class LoginTask extends LoginAsyncTask {

	/**
	 * Constructor.
	 */
	LoginTask() {
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Boolean result) {

	    if (result == null || !result) { // Task cancelled or exception
		if (!result) {
		    Intent i = new Intent();
		    i.putExtra("message", getErrorMessage());
		    LoginAnim.this.setResult(Activity.RESULT_CANCELED, i);
		} else
		    LoginAnim.this.setResult(Activity.RESULT_CANCELED);
		LoginAnim.this.finish();
	    } else {
		mCancelBt.setEnabled(false);
		LoginAnim.this.startService(LoginAnim.SERVICE_INTENT);
		LoginAnim.this.setResult(Activity.RESULT_OK);
		LoginAnim.this.finish();
	    }
	}

	@Override
	protected void onProgressUpdate(Integer ... values) {
	    mLoginState.setText(getResources().getStringArray(R.array.loganim_state)[values[0]]);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
	    super.onCancelled();
	    LoginAnim.this.stopService(LoginAnim.SERVICE_INTENT);
	}

    }

    /**
     * The service connection used to connect to the Beem service.
     */
    private class LoginServiceConnection implements ServiceConnection {

	/**
	 * Constructor.
	 */
	public LoginServiceConnection() {
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
	    mXmppFacade = IXmppFacade.Stub.asInterface(service);
	    if (mTask.getStatus() == AsyncTask.Status.PENDING)
		mTask = mTask.execute(mXmppFacade);
	}

	public void onServiceDisconnected(ComponentName name) {
	    mXmppFacade = null;
	}
    }
}
