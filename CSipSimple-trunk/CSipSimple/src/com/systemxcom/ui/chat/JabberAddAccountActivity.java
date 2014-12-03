package com.systemxcom.ui.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.csipsimple.R;
import com.systemxcom.models.JabberAccount;

public class JabberAddAccountActivity extends Activity
{
	private ListView mJabberAccountList;
	private ArrayList<JabberAccount> mJabberAccounts;
	private CustomAdapter mCustomAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jabberaccountlist);
		mJabberAccounts = new ArrayList<JabberAccount>();
		
		mJabberAccountList = (ListView)findViewById(R.id.account_list);
		mCustomAdapter = new CustomAdapter();
		mJabberAccountList.setAdapter(mCustomAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_jabber_account, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.menu_add:
	            // Red item was selected
	            return true;
	      
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
	public class CustomAdapter extends BaseAdapter   implements OnClickListener {
	          
	    
	         private  LayoutInflater inflater=null;
 
	         public CustomAdapter() 
	         {
	        	 /***********  Layout inflator to call external xml layout () ***********/
	             inflater = ( LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
	         }
	      
	         /******** What is the size of Passed Arraylist Size ************/
	         public int getCount() 
	         {
	             return mJabberAccounts.size();
	         }
	      
	         public Object getItem(int position) {
	             return position;
	         }
	      
	         public long getItemId(int position) {
	             return position;
	         }
	         /****** Depends upon data size called for each row , Create each ListView row *****/
	         public View getView(int position, View convertView, ViewGroup parent) {
	              
	             View vi = convertView;
	             ViewHolder holder;
	              
	             if(convertView==null)
	             {
	                 vi = inflater.inflate(R.layout.account_row, null);
	                 holder = new ViewHolder();
	                 holder.accountName = (TextView) vi.findViewById(R.id.text_account_name);
	                 
	                 vi.setTag( holder );
	             }
	             else 
	                 holder=(ViewHolder)vi.getTag();
	              
	             if(mJabberAccounts.size()<=0)
	             {
	                 holder.accountName.setText("No Account");
	                  
	             }
	             else
	             {
	                /* *//***** Get each Model object from Arraylist ********//*
	                 tempValues=null;
	                 tempValues = ( ListModel ) data.get( position );
	                  
	                 *//************  Set Model values in Holder elements ***********//*
	 
	                  holder.text.setText( tempValues.getCompanyName() );
	                  holder.text1.setText( tempValues.getUrl() );
	                   holder.image.setImageResource(
	                               res.getIdentifier(
	                               "com.androidexample.customlistview:drawable/"+tempValues.getImage()
	                               ,null,null));*/
	                   
	                  /******** Set Item Click Listner for LayoutInflater for each row *******/
	 
	                //  vi.setOnClickListener(new OnItemClickListener( position ));
	             }
	             return vi;
	         }
	          
	         @Override
	         public void onClick(View v) {
	                 Log.v("CustomAdapter", "=====Row button clicked=====");
	         }
	          
	         /********* Called when Item click in ListView ************/
	         private class OnItemClickListener  implements OnClickListener{           
	             private int mPosition;
	              
	             OnItemClickListener(int position){
	                  mPosition = position;
	             }
	              
	             @Override
	             public void onClick(View arg0) {
	 
	        
	            /*   CustomListViewAndroidExample sct = (CustomListViewAndroidExample)activity;*/
	 
	              /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
	 
	                 //sct.onItemClick(mPosition);
	             }               
	         }   
	     }
	
	 /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
         
        public TextView accountName;
        public ToggleButton isConnected;
       
    }
}
