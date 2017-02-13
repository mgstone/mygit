
package com.beem.project.beem.service.aidl;

import com.beem.project.beem.service.Message;
import com.beem.project.beem.service.aidl.IChat;

interface IMessageListener {

	/**
	 * This method is executed when a chat receive a message.
	 * @param chat the chat receiving the message.
	 * @param msg the message received in the chat.
	 */
	void processMessage(in IChat chat, in Message msg);

	/**
	 * This method is executed when a new ChatState is received by the chat.
	 * You can use IChat.getState() in order to get the new state.
	 * @param chat the chat changed.
	 */
	void stateChanged(in IChat chat);
	/**
	 * This method is executed when the otr session status change.
	 * @param otrState the new state of otr session.
	 */
	void otrStateChanged(in String otrState);
}
