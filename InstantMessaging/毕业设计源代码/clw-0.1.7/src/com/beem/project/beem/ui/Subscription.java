
package com.beem.project.beem.ui;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

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
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.beem.BeemService;
import com.beem.project.beem.R;
import com.beem.project.beem.service.PresenceAdapter;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.beem.project.beem.service.Contact;
import com.beem.project.beem.utils.BeemBroadcastReceiver;

/**
 * This activity is used to accept a subscription request.
 * @author nikita
 */
public class Subscription extends Activity {

    private static final Intent SERVICE_INTENT = new Intent();
    private static final String TAG = Subscription.class.getSimpleName();
    private IXmppFacade mService;
    private String mContact;
    private ServiceConnection mServConn = new BeemServiceConnection();
    private final BeemBroadcastReceiver mReceiver = new BeemBroadcastReceiver();
    private MyOnClickListener mClickListener = new MyOnClickListener();

    static {
	SERVICE_INTENT.setComponent(new ComponentName("com.beem.project.beem", "com.beem.project.beem.BeemService"));
    }

    /**
     * Constructor.
     */
    public Subscription() {
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.subscription);
	findViewById(R.id.SubscriptionAccept).setOnClickListener(mClickListener);
	findViewById(R.id.SubscriptionRefuse).setOnClickListener(mClickListener);
	Contact c = new Contact(getIntent().getData());
	mContact = c.getJID();
	TextView tv = (TextView) findViewById(R.id.SubscriptionText);
	String str = String.format(getString(R.string.SubscriptText), mContact);
	tv.setText(str);
	this.registerReceiver(mReceiver, new IntentFilter(BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
	super.onResume();
	bindService(new Intent(this, BeemService.class), mServConn, BIND_AUTO_CREATE);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
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
     * Send the presence stanza.
     *
     * @param p presence stanza
     */
    private void sendPresence(Presence p) {
	PresenceAdapter preAdapt = new PresenceAdapter(p);
	try {
	    mService.sendPresencePacket(preAdapt);
	} catch (RemoteException e) {
	    Log.e(TAG, "Error while sending subscription response", e);
	}
    }

    /**
     * Event simple click on buttons.
     */
    private class MyOnClickListener implements OnClickListener {

	/**
	 * Constructor.
	 */
	public MyOnClickListener() {
	}

	public void onClick(View v) {
	    Presence presence = null;
	    switch (v.getId()) {
		case R.id.SubscriptionAccept:
		    presence = new Presence(Type.subscribed);
		    Toast.makeText(Subscription.this, getString(R.string.SubscriptAccept), Toast.LENGTH_SHORT)
			    .show();
		    break;
		case R.id.SubscriptionRefuse:
		    presence = new Presence(Type.unsubscribed);
		    Toast.makeText(Subscription.this, getString(R.string.SubscriptRefused), Toast.LENGTH_SHORT).show();
		    break;
		default:
		    Toast.makeText(Subscription.this, getString(R.string.SubscriptError), Toast.LENGTH_SHORT).show();
	    }
	    if (presence != null) {
		presence.setTo(mContact);
		sendPresence(presence);
	    }
	    finish();
	}
    };

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
	    mService = IXmppFacade.Stub.asInterface(service);
	}

	public void onServiceDisconnected(ComponentName name) {
	    mService = null;
	}
    }
}
