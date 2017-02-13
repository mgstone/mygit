
package com.beem.project.beem.service.aidl;

import com.beem.project.beem.service.aidl.IXmppConnection;
import com.beem.project.beem.service.aidl.IRoster;
import com.beem.project.beem.service.aidl.IChatManager;
import com.beem.project.beem.service.aidl.IPrivacyListManager;
import com.beem.project.beem.service.PresenceAdapter;
import com.beem.project.beem.service.UserInfo;

import android.net.Uri;

interface IXmppFacade {

    /**
     * Get the XmppConnection of the facade.
     */
    IXmppConnection createConnection();

    /**
     * Get the roster of the user
     */
    IRoster getRoster();

    /**
     * Connect and login synchronously on the server.
     */
    void connectSync();

    /**
     * Connect and login asynchronously on the server.
     */
    void connectAsync();

    /**
     * Disconnect from the server
     */
    void disconnect();

    /**
     * Get the chat manager.
     */
    IChatManager getChatManager();

    /**
     * Change the status of the user.
     * @param status the status to set
     * @param msg the message state to set
     */
    void changeStatus(in int status, in String msg);

    void sendPresencePacket(in PresenceAdapter presence);

    /**
     * make a jingle audio call
     * @param jid the receiver id
     */
     void call(in String jid);

    boolean publishAvatar(in Uri avatarUri);

    void disableAvatarPublishing();

    /**
     * Get the user informations.
     * @return null if not connected
     */
    UserInfo getUserInfo();

     IPrivacyListManager getPrivacyListManager();
}
