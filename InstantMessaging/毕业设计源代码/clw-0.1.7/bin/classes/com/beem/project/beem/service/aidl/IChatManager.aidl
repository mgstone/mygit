
package com.beem.project.beem.service.aidl;

import  com.beem.project.beem.service.Contact;
import  com.beem.project.beem.service.aidl.IChat;
import  com.beem.project.beem.service.aidl.IMessageListener;
import  com.beem.project.beem.service.aidl.IChatManagerListener;

/**
 * Aidl interface for a chat manager.
 * The chat manager will manage all the chat sessions.
 */
interface IChatManager {

    	/**
    	 * Create a chat session with a contact.
    	 * @param contact	the contact to chat with
    	 * @param listener	the callback to call when a new message comes from this chat session
    	 * @return 		the chat session
    	 */
	IChat createChat(in Contact contact, in IMessageListener listener);

	/**
	 * Get an existing Chat session with a contact.
	 * @return null if the chat session does not exist.
	 */
	IChat getChat(in Contact contact);

	/**
    	 * Destroy a chat session with a contact.
    	 * @param chat	the chat session
    	 */
	void destroyChat(in IChat chat);

        /**
	 * @param chat the chat.
         */
	void deleteChatNotification(in IChat chat);

	/**
	 * Register a callback to call when a new chat session is created.
	 * @param listener	the callback to add
	 */
	void addChatCreationListener(in IChatManagerListener listener);

	/**
	 * Remove a callback for the creation of new chat session.
	 * @param listener	the callback to remove.
	 */
	void removeChatCreationListener(in IChatManagerListener listener);

	/**
	 * Get a list of contact which we are currently chatting.
	 * @return list of contact.
	 */
	List<Contact> getOpenedChatList();
}
