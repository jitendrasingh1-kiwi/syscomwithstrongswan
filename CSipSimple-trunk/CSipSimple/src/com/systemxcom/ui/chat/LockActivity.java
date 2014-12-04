package com.systemxcom.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.csipsimple.R;

public class LockActivity extends Activity
{
	public static final String LOCKPREFRENCES = "lockpreferences" ;
	SharedPreferences sharedpreferences;
	private EditText mEditLock , mConfirmLock;
	private Button mSetButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_account);
		
		sharedpreferences = getSharedPreferences(LOCKPREFRENCES, Context.MODE_PRIVATE);
		
		mEditLock = (EditText)findViewById(R.id.edit_password);
		mConfirmLock = (EditText)findViewById(R.id.edit_confirm);
		mSetButton = (Button)findViewById(R.id.button_set);
		
		mSetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String lock = mEditLock.getEditableText().toString();
				String confirmLock = mConfirmLock.getEditableText().toString();
				
				if(!lock.equalsIgnoreCase(confirmLock))
				{
					Toast.makeText(getApplicationContext(), "Please Enter correct confirm password", Toast.LENGTH_LONG).show();
				}
				else
				{
					//store in shared preferences
					 Editor editor = sharedpreferences.edit();
				     editor.putString("lock", lock);
				     
				     editor.commit();
				     
				     LockActivity.this.finish();
				     Toast.makeText(getApplicationContext(), "App lock set", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
}
