
package com.beem.project.beem.service;

import org.jivesoftware.smack.packet.Presence;

import android.net.Uri;
import android.os.RemoteException;

import com.beem.project.beem.service.aidl.IChatManager;
import com.beem.project.beem.service.aidl.IPrivacyListManager;
import com.beem.project.beem.service.aidl.IRoster;
import com.beem.project.beem.service.aidl.IXmppConnection;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.beem.project.beem.utils.PresenceType;

/**
 * This class is a facade for the Beem Service.
 * @author darisk
 */
public class XmppFacade extends IXmppFacade.Stub {

    private final XmppConnectionAdapter mConnexion;

    /**
     * Constructor for XMPPFacade.
     * @param connection the connection use by the facade
     */
    public XmppFacade(final XmppConnectionAdapter connection) {
	this.mConnexion = connection;
    }

    /**
     * {@inheritDoc}
     */
    public void changeStatus(int status, String msg) {
	mConnexion.changeStatus(status, msg);
    }

    /**
     * {@inheritDoc}
     */
    public void connectAsync() throws RemoteException {
	mConnexion.connectAsync();
    }

    /**
     * {@inheritDoc}
     */
    public void connectSync() throws RemoteException {
	mConnexion.connectSync();
    }

    /**
     * {@inheritDoc}
     */
    public IXmppConnection createConnection() throws RemoteException {
	return mConnexion;
    }

    /**
     * {@inheritDoc}
     */
    public void disconnect() throws RemoteException {
	mConnexion.disconnect();
    }

    /**
     * {@inheritDoc}
     */
    public IChatManager getChatManager() throws RemoteException {
	return mConnexion.getChatManager();
    }

    /**
     * {@inheritDoc}
     */
    public IRoster getRoster() throws RemoteException {
	return mConnexion.getRoster();
    }

    /**
     * {@inheritDoc}
     */
    public IPrivacyListManager getPrivacyListManager() {
	return mConnexion.getPrivacyListManager();
    }

    public void sendPresencePacket(PresenceAdapter presence) throws RemoteException {
	Presence presence2 = new Presence(PresenceType.getPresenceTypeFrom(presence.getType()));
	presence2.setTo(presence.getTo());
	mConnexion.getAdaptee().sendPacket(presence2);
    }

    /* (non-Javadoc)
     * @see com.beem.project.beem.service.aidl.IXmppFacade#call(java.lang.String)
     */
    public void call(String jid) throws RemoteException {
    }

    public boolean publishAvatar(Uri avatarUri) throws RemoteException {
	BeemAvatarManager mgr = mConnexion.getAvatarManager();
	if (mgr == null)
	    return false;

	return mgr.publishAvatar(avatarUri);
    }

    public void disableAvatarPublishing() throws RemoteException {
	BeemAvatarManager mgr = mConnexion.getAvatarManager();
	if (mgr != null)
	    mgr.disableAvatarPublishing();
    }

    public UserInfo getUserInfo() throws RemoteException {
	return mConnexion.getUserInfo();
    }
}
