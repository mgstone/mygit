
package com.beem.project.beem.service.aidl;

import  com.beem.project.beem.service.Contact;
import  com.beem.project.beem.service.Message;
import  com.beem.project.beem.service.aidl.IMessageListener;

/**
 * An aidl interface for Chat session.
 */
interface IChat {

    	/**
    	 * Send a message.
    	 * @param message	the message to send
    	 */
	void sendMessage(in Message message);

	/**
	 * Get the participant of the chat
	 * @return the participant
	 */
	Contact getParticipant();

	/**
	 * Add a message listener.
	 * @param listener the listener to add.
	 */
	void addMessageListener(in IMessageListener listener);

	/**
	 * Remove a message listener.
	 * @param listener the listener to remove.
	 */
	void removeMessageListener(in IMessageListener listener);

	String getState();

	void setOpen(in boolean isOpen);

	boolean isOpen();

	void setState(in String state);

	List<Message> getMessages();
	
	/**
	 * Try to start an OTR session.
	 */
	void startOtrSession();
	
	/**
	 * Stop the OTR session.
	 */
	void endOtrSession();
	
	/**
	 * get local OTR key fingerprints.
	 */
	String getLocalOtrFingerprint();
	
	
	/**
	 * get remote OTR key fingerprints.
	 */
	String getRemoteOtrFingerprint();
	
	void verifyRemoteFingerprint(in boolean ok);
	
	
	/**
	 * get current OTR status.
	 */
	String getOtrStatus();
	

}
