package com.systemxcom.ui.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	public static ArrayList<JabberAccount> mJabberAccounts;
	private CustomAdapter mCustomAdapter;
	private JabberAccountDatasource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jabberaccountlist);
		
		datasource = new JabberAccountDatasource(JabberAddAccountActivity.this);
		datasource.open();
		
		if(mJabberAccounts!=null)
			mJabberAccounts.clear();
		mJabberAccounts = datasource.getAllAccounts();
		
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
	        	///Toast.makeText(getApplicationContext(),"add an account", Toast.LENGTH_LONG).show();
	        	Intent intent = new Intent(JabberAddAccountActivity.this, JabberAccountActivity.class);
	        	intent.putExtra("position",-1);
	        	startActivity(intent);
	            return true;
	      
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	
	public class CustomAdapter extends BaseAdapter {
	          
	    
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
	         public View getView(final int position, View convertView, ViewGroup parent) {
	              
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
	            	 holder.accountName.setText(mJabberAccounts.get(position).getAccountName());
	             }
	             
	             vi.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(JabberAddAccountActivity.this, JabberAccountActivity.class);
						intent.putExtra("position",position);
			        	startActivity(intent);
					}
				});
	             
	             return vi;
	         }
	          
	        
	     }
	
	 /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
         
        public TextView accountName;
        public ToggleButton isConnected;
       
    }
    
    @Override
    protected void onResume() {
      datasource.open();
      mCustomAdapter.notifyDataSetChanged();
      super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }
}
