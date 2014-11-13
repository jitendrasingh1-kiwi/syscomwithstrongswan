package com.systemxcom.ui.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.csipsimple.R;

public class JabberAccountActivity extends Activity
{
	private EditText mEditAccountName;
	private EditText mEditUserName;
	private EditText mEditPassword;
	private EditText mEditDomain;
	private Button mBttnConnect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jabber_account_xml);
		
		mEditAccountName = (EditText)findViewById(R.id.edit_account_name);
		mEditUserName = (EditText)findViewById(R.id.edit_user_name);
		mEditPassword = (EditText)findViewById(R.id.edit_password);
		mEditDomain = (EditText)findViewById(R.id.edit_domain);
		mBttnConnect = (Button)findViewById(R.id.bttn_connect);
		
		mBttnConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
