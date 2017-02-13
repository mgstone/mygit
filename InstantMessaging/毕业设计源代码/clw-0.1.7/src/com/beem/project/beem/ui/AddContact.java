
package com.beem.project.beem.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beem.project.beem.BeemService;
import com.beem.project.beem.R;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.beem.project.beem.utils.BeemBroadcastReceiver;

/**
 * This activity is used to add a contact.
 * @author nikita
 */
public class AddContact extends Activity {

    private static final Intent SERVICE_INTENT = new Intent();
    private static final String TAG = "AddContact";
    private final List<String> mGroup = new ArrayList<String>();
    private IXmppFacade mXmppFacade;
    private final ServiceConnection mServConn = new BeemServiceConnection();
    private final BeemBroadcastReceiver mReceiver = new BeemBroadcastReceiver();
    private final OkListener mOkListener = new OkListener();

    static {
	SERVICE_INTENT.setComponent(new ComponentName("com.beem.project.beem", "com.beem.project.beem.BeemService"));
    }

    /**
     * Constructor.
     */
    public AddContact() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.addcontact);
	Button ok = (Button) findViewById(R.id.addc_ok);
	ok.setOnClickListener(mOkListener);
	this.registerReceiver(mReceiver, new IntentFilter(BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
	super.onStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
	super.onResume();
	bindService(new Intent(this, BeemService.class), mServConn, BIND_AUTO_CREATE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
	super.onPause();
	unbindService(mServConn);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
	super.onDestroy();
	this.unregisterReceiver(mReceiver);
    }

    /**
     * The ServiceConnection used to connect to the Beem service.
     */
    private class BeemServiceConnection implements ServiceConnection {

	/**
	 * Constructor.
	 */
	public BeemServiceConnection() {
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
	    mXmppFacade = IXmppFacade.Stub.asInterface(service);
	}

	public void onServiceDisconnected(ComponentName name) {
	    mXmppFacade = null;
	}
    }

    /**
     * Get the text of a widget.
     * @param id the id of the widget.
     * @return the text of the widget.
     */
    private String getWidgetText(int id) {
	EditText widget = (EditText) this.findViewById(id);
	return widget.getText().toString();
    }

    /**
     * Listener.
     */
    private class OkListener implements OnClickListener {

	/**
	 * Constructor.
	 */
	public OkListener() { }

	public void onClick(View v) {
	    String login;
	    login = getWidgetText(R.id.addc_login);
	    if (login.length() == 0) {
		Toast.makeText(AddContact.this, getString(R.string.AddCBadForm), Toast.LENGTH_SHORT).show();
		return;
	    }
	    boolean isEmail = Pattern.matches("[a-zA-Z0-9._%+-]+@(?:[a-zA-Z0-9-]+.)+[a-zA-Z]{2,4}", login);
	    if (!isEmail) {
		Toast.makeText(AddContact.this, getString(R.string.AddCContactAddedLoginError), Toast.LENGTH_SHORT)
		    .show();
		return;
	    }
	    String alias;
	    alias = getWidgetText(R.id.addc_alias);
	    if (getWidgetText(R.id.addc_group).length() != 0)
		mGroup.add(getWidgetText(R.id.addc_group));
	    try {
		if (mXmppFacade != null) {
		    if (mXmppFacade.getRoster().getContact(login) != null) {
			mGroup.addAll(mXmppFacade.getRoster().getContact(login).getGroups());
			Toast.makeText(AddContact.this, getString(R.string.AddCContactAlready), Toast.LENGTH_SHORT)
			    .show();
			return;
		    }
		    if (mXmppFacade.getRoster().addContact(login, alias, mGroup.toArray(new String[mGroup.size()])) == null) {
			Toast.makeText(AddContact.this, getString(R.string.AddCContactAddedError), Toast.LENGTH_SHORT)
			    .show();
			return;
		    } else {
			Toast.makeText(AddContact.this, getString(R.string.AddCContactAdded), Toast.LENGTH_SHORT)
			    .show();
			finish();
		    }
		}
	    } catch (RemoteException e) {
		Toast.makeText(AddContact.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		Log.e(TAG, "Problem adding contact", e);
	    }

	}
    };
}
