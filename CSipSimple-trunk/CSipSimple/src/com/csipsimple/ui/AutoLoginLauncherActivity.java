package com.csipsimple.ui;


import com.csipsimple.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AutoLoginLauncherActivity extends Activity
{
	public static final String LOCKPREFRENCES = "lockpreferences" ;
	SharedPreferences sharedpreferences;
	EditText mPasswordText;
	private String lock;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Toast.makeText(getApplicationContext(), "Please Enter your password",Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_login_layout);
		
		sharedpreferences = getSharedPreferences(LOCKPREFRENCES, Context.MODE_PRIVATE);
		lock = sharedpreferences.getString("lock", "");
		
		mPasswordText = (EditText)findViewById(R.id.login_password);
		
		mPasswordText.setOnKeyListener(new OnKeyListener() {
		    @Override
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        if (event.getAction()!=KeyEvent.ACTION_DOWN)
		            return false;
		        if(keyCode == KeyEvent.KEYCODE_ENTER )
		        {
		        	if(lock.equalsIgnoreCase(mPasswordText.getEditableText().toString()))
		        		finish();
		        	else
		        		Toast.makeText(getApplicationContext(), "Please Enter correct lock", Toast.LENGTH_LONG).show();
		        	
		            return true;
		        }
		        return false;
		    }
		});
	}

	/*public void onToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	        // Enable vibrate
	    } else {
	        // Disable vibrate
	    }
	}*/
	
	
	
}
