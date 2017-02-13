
package com.beem.project.beem.service.aidl;

import com.beem.project.beem.service.PresenceAdapter;

interface IBeemRosterListener {
    void onEntriesAdded(in List<String> addresses);
    void onEntriesUpdated(in List<String> addresses);
    void onEntriesDeleted(in List<String> addresses);
    void onPresenceChanged(in PresenceAdapter presence);
}
