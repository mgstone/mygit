
package com.beem.project.beem.service.aidl;

import  com.beem.project.beem.service.PrivacyListItem;

interface IPrivacyListListener {
	void updatedPrivacyList(in String listName);
	void setPrivacyList(in String listName, in List<PrivacyListItem> listItem);
}
