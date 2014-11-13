package com.systemxcom.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.csipsimple.R;

public class ChatDestructionActivity extends SherlockFragmentActivity
{
	private SeekBar seekbar1;
	private TextView mTextViewTime;
	private int mTotalTime =86400;//6 days seconds
	private Button mSetButton;
	private EditText mDays,mHrs;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_destruction_activity);
		seekbar1 = (SeekBar) findViewById(R.id.seekbar1);
		mTextViewTime =(TextView)findViewById(R.id.text_destruction_time);
		mSetButton =(Button)findViewById(R.id.set_btn);
		mDays =(EditText)findViewById(R.id.edit_days);
		mHrs =(EditText)findViewById(R.id.edit_hrs);
		
		seekbar1.setOnSeekBarChangeListener(seekBarChange);
		
		mSetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String days = mDays.getEditableText().toString();
				String hrs = mHrs.getText().toString();
				
				if(days.equalsIgnoreCase("") && hrs.equalsIgnoreCase(""))
					return;
				else if(days.equalsIgnoreCase(""))
				{
					mTotalTime=(Integer.parseInt(hrs))*60*60;
				}
				else if(hrs.equalsIgnoreCase(""))
				{
					mTotalTime=(Integer.parseInt(days))*60*60*24;
				}
				else
				{
					mTotalTime = (Integer.parseInt(hrs))*60*60+(Integer.parseInt(days))*60*60*24;
				}
				
				seekbar1.setMax(mTotalTime);
					
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	 private OnSeekBarChangeListener seekBarChange = new OnSeekBarChangeListener() {
		  
		          @Override
		          public void onStopTrackingTouch(SeekBar seekBar) {
		              if (seekBar.getId() == R.id.seekbar1) {
		                  //textview1.setText("Seekbar1 stops dragging");
		              } else {
		                  //textview1.setText("Seekbar2 stops dragging");
		              }
		          }
		  
		          @Override
		          public void onStartTrackingTouch(SeekBar seekBar) {
		              if (seekBar.getId() == R.id.seekbar1) {
		                  //textview1.setText("Seekbar1 began to drag");
		              } else {
		                  //textview1.setText("Seekbar2 began to drag");
		              }
		          }
		  
		          @Override
		          public void onProgressChanged(SeekBar seekBar, int progress,
		                  boolean fromUser) {
		              if (seekBar.getId() == R.id.seekbar1) {
		            	  //mTextViewTime.setText(text);
		                  mTextViewTime.setText(" " + getTime(progress));
		              } else {
		            	  mTextViewTime.setText(" " + getTime(progress));
		              }
		  
		          }
		      };
		  
	public String getTime(int progress)
	{
		String time="";
		/*int temp = 518400*progress/100;
		int days =temp/(60*60*24);
		int hrs = (temp%(60*60*24))/(60*60);//no of hrs
		int min = ((temp%(60*60*24))*24);
		
	    time = days+" days "+hrs+" hrs ";//+min+" min ";
*/		
		if(progress<60)
			time ="0 days 00 hrs 00 min "+progress+" secs ";
		else if(progress>60 && progress<60*60)
			time ="0 days 00 hrs "+progress/60+" min "+"00 secs ";
		else if(progress>60*60 && progress<60*60*24)
			time ="0 days "+progress/(60*60)+" hrs "+"00 min "+"00 secs ";
		else
			time =progress/(60*60*24)+" days "+"00 hrs "+"00 min "+"00 secs ";
		
		return time;
		
	}
}
