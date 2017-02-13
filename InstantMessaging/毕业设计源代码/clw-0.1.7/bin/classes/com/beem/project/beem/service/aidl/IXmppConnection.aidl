
package com.beem.project.beem.service.aidl;

import  com.beem.project.beem.service.aidl.IRoster;
import  com.beem.project.beem.service.aidl.IBeemConnectionListener;
import  com.beem.project.beem.service.aidl.IChatManager;
import  com.beem.project.beem.service.aidl.IPrivacyListManager;

interface IXmppConnection {

    boolean connect();

    boolean login();

    boolean connectSync();

    void connectAsync();

    boolean disconnect();

    IRoster getRoster();

    void addConnectionListener(in IBeemConnectionListener listen);
    void removeConnectionListener(in IBeemConnectionListener listen);

    boolean isAuthentificated();

    IChatManager getChatManager();

    void changeStatusAndPriority(in int status, in String msg, in int priority);

    void changeStatus(in int status, in String msg);

    IPrivacyListManager getPrivacyListManager();

    String getErrorMessage();
}
