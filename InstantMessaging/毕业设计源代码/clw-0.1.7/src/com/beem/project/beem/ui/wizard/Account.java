
package com.beem.project.beem.ui.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

import com.beem.project.beem.ui.CreateAccount;
import com.beem.project.beem.R;

/**
 * The first activity of an user friendly wizard to configure a XMPP account.
 *
 * @author Da Risk <darisk972@gmail.com>
 */
public class Account extends Activity implements OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup mConfigureGroup;
    private Button mNextButton;

    /**
     * Constructor.
     */
    public Account() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.wizard_account);
	mConfigureGroup = (RadioGroup) findViewById(R.id.configure_group);
	mConfigureGroup.setOnCheckedChangeListener(this);
	mNextButton = (Button) findViewById(R.id.next);
	mNextButton.setOnClickListener(this);
    }

    public void onClick(View v) {
	if (v == mNextButton) {
	    int selectedid = mConfigureGroup.getCheckedRadioButtonId();
	    Intent i = null;
	    if (selectedid == R.id.configure_account) {
		i = new Intent(this, AccountConfigure.class);
		finish();
	    } else if (selectedid == R.id.create_account) {
		i = new Intent(this, CreateAccount.class);
	    }
	    if (i != null) {
		startActivity(i);
	    }
	}
    }

    public void onCheckedChanged(RadioGroup  group, int checkedId) {
	if (checkedId == -1)
	    mNextButton.setEnabled(false);
	else
	    mNextButton.setEnabled(true);
    }
}
