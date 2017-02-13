
package com.beem.project.beem.ui.dialogs.builders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;

import com.beem.project.beem.R;
import com.beem.project.beem.service.aidl.IChat;

/**
 * Use this builder to build a dialog which allows you to display otr fingerprints.
 * @author nikita
 */
public class DisplayOtrFingerprint extends AlertDialog.Builder {

    private static final String TAG = "DisplayOtrFingerprint";
    private IChat mChat;

    /**
     * Constructor.
     * @param context context activity.
     * @param chat the current chat.
     */
    public DisplayOtrFingerprint(final Context context, final IChat chat) {
	super(context);

	mChat = chat;
	try {
	    setMessage(context.getString(R.string.chat_otr_verify_key, chat.getLocalOtrFingerprint(),
		chat.getRemoteOtrFingerprint()));
	} catch (RemoteException e) {
	    e.printStackTrace();
	}
	DialogClickListener dl = new DialogClickListener();
	setPositiveButton(R.string.userinfo_yes, dl);
	setNegativeButton(R.string.userinfo_no, dl);
    }

    /**
     * Event click listener.
     */
    private class DialogClickListener implements DialogInterface.OnClickListener {

	/**
	 * Constructor.
	 */
	public DialogClickListener() {
	}

	public void onClick(final DialogInterface dialog, final int which) {
	    if (which == DialogInterface.BUTTON_POSITIVE) {
		try {
		    mChat.verifyRemoteFingerprint(true);
		} catch (RemoteException e) {
		    e.printStackTrace();
		}
	    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
		try {
		    mChat.verifyRemoteFingerprint(false);
		} catch (RemoteException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
