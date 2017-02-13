
package com.beem.project.beem.ui.dialogs.builders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.beem.project.beem.R;
import com.beem.project.beem.service.Contact;
import com.beem.project.beem.service.aidl.IRoster;

/**
 * Create dialog alias.
 */
public class Alias extends AlertDialog.Builder {

    private static final String TAG = "Dialogs.Builders > Alias";

    private IRoster mRoster;
    private Contact mContact;
    private EditText mEditTextAlias;

    /**
     * Constructor.
     * @param context context activity.
     * @param roster Beem roster.
     * @param contact the contact to modify.
     */
    public Alias(final Context context, final IRoster roster, final Contact contact) {
	super(context);

	mRoster = roster;
	mContact = contact;

	LayoutInflater factory = LayoutInflater.from(context);
	final View textEntryView = factory.inflate(
	    R.layout.contactdialogaliasdialog, null);
	setTitle(mContact.getJID());
	setView(textEntryView);
	mEditTextAlias = (EditText) textEntryView.findViewById(
	    R.id.CDAliasDialogName);
	mEditTextAlias.setText(mContact.getName());
	setPositiveButton(R.string.OkButton, new DialogClickListener());
	setNegativeButton(R.string.CancelButton, new DialogClickListener());
    }

    /**
     * Event click listener.
     */
    class DialogClickListener implements DialogInterface.OnClickListener {

	/**
	 * Constructor.
	 */
	public DialogClickListener() {
	}


	public void onClick(final DialogInterface dialog, final int which) {
	    if (which == DialogInterface.BUTTON_POSITIVE) {
		String name = mEditTextAlias.getText().toString();
		if (name.length() == 0) {
		    name = mContact.getJID();
		}
		try {
		    mRoster.setContactName(mContact.getJID(), name);
		} catch (RemoteException e) {
		    Log.e(TAG, e.getMessage());
		}
	    }
	}
    }
}
