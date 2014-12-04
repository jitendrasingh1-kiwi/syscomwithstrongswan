package com.systemxcom.ui.chat;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.csipsimple.R;
import com.systemxcom.models.ConversationMessage;
import com.systemxcom.models.JabberAccount;

/**
 * This class sets up an xmpp account and user can chat with its buddies
 * @author kiwitech
 *
 */
public class JabberAccountActivity extends Activity
{
	SharedPreferences sharedpreferences;
	public static final String XMPPPREFERENCES = "xmpppreferences" ;
	private EditText mEditAccountName;
	private EditText mEditUserName;
	private EditText mEditPassword;
	private EditText mEditDomain;
	private Button mBttnConnect;
	private int position;
	
	private String mUserName , mPassword, mAccountName;
	private String HOST;
	public static final int PORT = 5222;
	//private MessageListener messageListener; 
	//private XMPPConnection connection;
	
	private Handler mHandler = new Handler();
	
	private JabberAccountDatasource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jabber_account_xml);
		
		Bundle extra = getIntent().getExtras();
		
		position = extra.getInt("position");
		
		sharedpreferences = getSharedPreferences(XMPPPREFERENCES, Context.MODE_PRIVATE);
		datasource = new JabberAccountDatasource(JabberAccountActivity.this);
		datasource.open();
		
		mEditAccountName = (EditText)findViewById(R.id.edit_account_name);
		mEditUserName = (EditText)findViewById(R.id.edit_user_name);
		mEditPassword = (EditText)findViewById(R.id.edit_password);
		mEditDomain = (EditText)findViewById(R.id.edit_domain);
		mBttnConnect = (Button)findViewById(R.id.bttn_connect);
		if(position>=0)
		{
			if (sharedpreferences.contains("account_name"))
		      {
				//mEditAccountName.setText(sharedpreferences.getString("account_name", ""));
				mEditAccountName.setText(JabberAddAccountActivity.mJabberAccounts.get(position).getAccountName());
	
		      }
		      if (sharedpreferences.contains("username"))
		      {
		    	  //mEditUserName.setText(sharedpreferences.getString("username", ""));
		    	  mEditUserName.setText(JabberAddAccountActivity.mJabberAccounts.get(position).getUserName());
	
		      }
		      if (sharedpreferences.contains("password"))
		      {
		    	  //mEditPassword.setText(sharedpreferences.getString("password", ""));
		    	  mEditPassword.setText(JabberAddAccountActivity.mJabberAccounts.get(position).getPassword());
	
		      }
		      if (sharedpreferences.contains("domain"))
		      {
		    	  //mEditDomain.setText(sharedpreferences.getString("domain", ""));
		    	  mEditDomain.setText(JabberAddAccountActivity.mJabberAccounts.get(position).getDomain());
		      }
		}
	     
		mBttnConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(TextUtils.isEmpty(mEditAccountName.getEditableText().toString()) ||
						TextUtils.isEmpty(mEditUserName.getEditableText().toString()) ||
						TextUtils.isEmpty(mEditPassword.getEditableText().toString()) ||
						TextUtils.isEmpty(mEditDomain.getEditableText().toString()) ||
						TextUtils.isEmpty(mBttnConnect.getEditableText().toString()))
				{
					Toast.makeText(getApplicationContext(),"Please Enter all fields", Toast.LENGTH_SHORT).show();
				}
				else
				{*/
					HOST= mEditDomain.getEditableText().toString();
					mUserName = mEditUserName.getEditableText().toString();
					mPassword = mEditPassword.getEditableText().toString();
					mAccountName = mEditAccountName.getEditableText().toString();
					
					JabberAccount jabberAccount = new JabberAccount();
					jabberAccount.setAccountName(mAccountName);
					jabberAccount.setDomain(HOST);
					jabberAccount.setPassword(mPassword);
					jabberAccount.setUserName(mUserName);
					
					if(position<0)
						datasource.createAccount(jabberAccount);
					
					 Editor editor = sharedpreferences.edit();
				     editor.putString("account_name", mAccountName);
				     editor.putString("username", mUserName);
				     editor.putString("password", mPassword);
				     editor.putString("domain", HOST);
				     
				     editor.commit(); 		
				     if(SystemXComXmppConnection.connection!=null)
				    	 SystemXComXmppConnection.connection.disconnect();
					connect();
				//}
			}
		});
	}
	
	
	public void connect() {

		final ProgressDialog dialog = ProgressDialog.show(JabberAccountActivity.this,
				"Connecting...", "Please wait...", false);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create a connection
				/*ConnectionConfiguration connConfig = new ConnectionConfiguration(
						HOST, PORT, SERVICE);*/
				ConnectionConfiguration connConfig = new ConnectionConfiguration(
						HOST, PORT);
				XMPPConnection connection = new XMPPConnection(connConfig);

				try {
					connection.connect();
					Log.i("XMPPChatDemoActivity",
							"Connected to " + connection.getHost());
				} catch (XMPPException ex) {
					Log.e("XMPPChatDemoActivity", "Failed to connect to "
							+ connection.getHost());
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
					//messageListener = new MyMessageListener();
				}
				try {
					// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
					//connection.login(SystemXComXmppConnection.USERNAME, SystemXComXmppConnection.PASSWORD);
					connection.login(mUserName, mPassword);
					Log.i("XMPPChatDemoActivity",
							"Logged in as " + connection.getUser());

					// Set the status to available
					Presence presence = new Presence(Presence.Type.available);
					connection.sendPacket(presence);
					setConnection(connection);
					//messageListener = new MyMessageListener();

					Roster roster = connection.getRoster();
					Collection<RosterEntry> entries = roster.getEntries();
					for (RosterEntry entry : entries) {
						Log.d("XMPPChatDemoActivity",
								"--------------------------------------");
						Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
						Log.d("XMPPChatDemoActivity",
								"User: " + entry.getUser());
						Log.d("XMPPChatDemoActivity",
								"Name: " + entry.getName());
						Log.d("XMPPChatDemoActivity",
								"Status: " + entry.getStatus());
						Log.d("XMPPChatDemoActivity",
								"Type: " + entry.getType());
						Presence entryPresence = roster.getPresence(entry
								.getUser());

						Log.d("XMPPChatDemoActivity", "Presence Status: "
								+ entryPresence.getStatus());
						Log.d("XMPPChatDemoActivity", "Presence Type: "
								+ entryPresence.getType());
						Presence.Type type = entryPresence.getType();
						if (type == Presence.Type.available)
							Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
						Log.d("XMPPChatDemoActivity", "Presence : "
								+ entryPresence);

					}
				} catch (XMPPException ex) {
					Log.e("XMPPChatDemoActivity", "Failed to log in as "
							+ mUserName+"   password"+mPassword);
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
					//messageListener = new MyMessageListener();
				}

				dialog.dismiss();
			}
		});
		t.start();
		dialog.show();
	}
	
	/**
	 * Called by Settings dialog when a connection is establised with the XMPP
	 * server
	 * 
	 * @param connection
	 */
	public void setConnection(XMPPConnection connection) {
		//this.connection = connection;
		SystemXComXmppConnection.connection = connection;
		if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						
						ConversationMessage conversationMessage = new ConversationMessage();
						conversationMessage.setUserId(message.getFrom());
						conversationMessage.setMessage(message.getBody());
						SystemXComXmppConnection.chatMessages.add(conversationMessage);
						
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
								+ " from " + fromName );
						SystemXComXmppConnection.messages.add(fromName + ":");
						SystemXComXmppConnection.messages.add(message.getBody());
						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
							//	setListAdapter(oncamChatAdapter);
								 broadcastIntent();
							}
						});
					}
				}
			}, filter);
		}
	}
	
	 /*  private class MyMessageListener implements MessageListener {  
		   
	        @Override  
	        public void processMessage(Chat chat, Message message) {  
	            String from = message.getFrom();  
	            String body = message.getBody();  
	            System.out.println(String.format("Received message " + body + " from " + from));  
	        }  
	  
	    }  
	  */
	
	// broadcast a custom intent. 
	   public void broadcastIntent()
	   {
	      Intent intent = new Intent();
	      intent.setAction("com.tutorialspoint.refreshchat");
	      sendBroadcast(intent);
	   }
	   
	   @Override
	    protected void onResume() {
	      datasource.open();
	      super.onResume();
	    }

	    @Override
	    protected void onPause() {
	      datasource.close();
	      super.onPause();
	    }
	    
	    @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.menu_delete_jabber_account, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
		        case R.id.menu_delete:
		        	datasource.deleteAccount(JabberAddAccountActivity.mJabberAccounts.get(position));
		        	JabberAddAccountActivity.mJabberAccounts.remove(position);
		        	finish();
		            return true;
		      
		        default:
		            return super.onOptionsItemSelected(item);
			}
		}
}
