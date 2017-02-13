
package com.beem.project.beem.service.aidl;

import com.beem.project.beem.service.aidl.IBeemRosterListener;
import com.beem.project.beem.service.Contact;
import com.beem.project.beem.service.PresenceAdapter;

interface IRoster {

    Contact addContact(in String user, in String name, in String[] groups);

    void deleteContact(in Contact contact);

    Contact getContact(in String jid);
    void setContactName(in String jid, in String name);

    void createGroup(in String groupname);

    void addContactToGroup(in String groupName, in String jid);

    void removeContactFromGroup(in String groupName, in String jid);

    List<Contact> getContactList();

    List<String> getGroupsNames();

    PresenceAdapter getPresence(in String jid);

    void addRosterListener(in IBeemRosterListener listen);
    void removeRosterListener(in IBeemRosterListener listen);

}
