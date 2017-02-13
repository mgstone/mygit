
package com.beem.project.beem.otr;

import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import net.java.otr4j.OtrEngine;
import net.java.otr4j.OtrEngineHost;
import net.java.otr4j.OtrEngineImpl;
import net.java.otr4j.OtrEngineListener;
import net.java.otr4j.OtrException;
import net.java.otr4j.OtrKeyManagerImpl;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;
import android.util.Log;

import com.beem.project.beem.service.ChatAdapter;

/**
 * BeemOtrManager.
 */
public class BeemOtrManager implements OtrEngineHost {

    private static final String TAG = "BeemOtrEngineHostImpl";
    private static BeemOtrManager INSTANCE;
    //We will have a global policy for Beem as long as we won't need to modify the policy per chat.
    private static final OtrPolicy mGlobalPolicy = new OtrPolicyImpl(OtrPolicy.ALLOW_V2 | OtrPolicy.ERROR_START_AKE);

    private OtrEngine mOtrEngine;
    private OtrKeyManagerImpl mOtrKeyManager;

    //Map of chat, needed because of the message injection
    private final Map<SessionID, ChatAdapter> mChats = new HashMap<SessionID, ChatAdapter>();

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private BeemOtrManager() {
	mOtrEngine = new OtrEngineImpl(this);
	mOtrEngine.addOtrEngineListener(new BeemOtrListener());
	try {
	    mOtrKeyManager = new OtrKeyManagerImpl("/sdcard/beem.keystore");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * getOtrManager.
     * @return OtrEngine
     */
    public OtrEngine getOtrManager() {
	return mOtrEngine;
    }

    /**
     * BeemOtrManager.getInstance.
     * @return BeemOtrManager
     */
    public static BeemOtrManager getInstance() {
	if (INSTANCE == null)
	    INSTANCE = new BeemOtrManager();
	return INSTANCE;
    }

    /**
     * We must call addChat before stating a new otr session because we will need the chat instance for message
     * injection.
     * @param sessionID the otr sessionID.
     * @param chat instance.
     */
    public void addChat(final SessionID sessionID, final ChatAdapter chat) {
	mChats.put(sessionID, chat);
	Log.d(TAG, "adding new OTR session " + sessionID);
    }

    /**
     * We must remove the chat from the map after we ended the corresponding otr session.
     * @param sessionID the otr sessionID to remove.
     */
    public void removeChat(final SessionID sessionID) {
	mChats.remove(sessionID);
    }

    /**
     * get the fingerprint of the remote part.
     * @param sessionID the otr session
     * @return a string containing the fingerprint
     */
    public String getRemoteFingerprint(final SessionID sessionID) {
	return mOtrKeyManager.getRemoteFingerprint(sessionID);
    }

    /**
     * set the remote fingerprint as verified.
     * @param sessionId the current otr session
     */
    public void verifyRemoteFingerprint(final SessionID sessionId) {
	mOtrKeyManager.verify(sessionId);
    }

    /**
     * unsetthe remote fingerprint as verified.
     * @param sessionId the current otr session
     */
    public void unverifyRemoteFingerprint(final SessionID sessionId) {
	mOtrKeyManager.unverify(sessionId);
    }

    /**
     * get the local fingerprint.
     * @param sessionID the otr session
     * @return a string containing the fingerprint
     */
    public String getLocalFingerprint(final SessionID sessionID) {
	return mOtrKeyManager.getLocalFingerprint(sessionID);
    }

    public void injectMessage(SessionID sessionID, String msg) {
	ChatAdapter chat = mChats.get(sessionID);
	chat.injectMessage(msg);
    }

    public void showWarning(SessionID sessionID, String warning) {
	Log.d(TAG, "Warning for " + sessionID + " : " + warning);
    }

    public void showError(SessionID sessionID, String error) {
	Log.d(TAG, "Error for " + sessionID + " : " + error);
    }

    public OtrPolicy getSessionPolicy(SessionID sessionID) {
	return mGlobalPolicy;
    }

    public KeyPair getKeyPair(SessionID sessionID) {
	KeyPair kp = mOtrKeyManager.loadLocalKeyPair(sessionID);

	if (kp != null)
	    return kp;

	mOtrKeyManager.generateLocalKeyPair(sessionID);
	return mOtrKeyManager.loadLocalKeyPair(sessionID);
    }

    /**
     * BeemOtrListener.
     */
    private class BeemOtrListener implements OtrEngineListener {

	public void sessionStatusChanged(final SessionID sessionID) {
	    Log.d(TAG, "OTR Status changed for " + sessionID + " : " + mOtrEngine.getSessionStatus(sessionID));
	    if (mOtrKeyManager.loadRemotePublicKey(sessionID) == null) {
		mOtrKeyManager.savePublicKey(sessionID, mOtrEngine.getRemotePublicKey(sessionID));
	    }

	    SessionStatus status = mOtrEngine.getSessionStatus(sessionID);

	    if (status.equals(SessionStatus.ENCRYPTED) && mOtrKeyManager.isVerified(sessionID)) {
		mChats.get(sessionID).otrStateChanged("AUTHENTICATED");
	    } else {
		if (status.equals(SessionStatus.FINISHED)) {
		    try {
			mChats.get(sessionID).localEndOtrSession();
		    } catch (OtrException e) {
			Log.w(TAG, "error when closing local otr session", e);
		    }
		}
		else {
		    mChats.get(sessionID).otrStateChanged(status.toString());
		}
	    }
	}
    }
}
