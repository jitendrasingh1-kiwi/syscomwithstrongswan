package com.systemxcom.ui.chat;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class SystemXComXmppConnection 
{
	public static String USERNAME; //="jitendra";
	public static  String PASSWORD; //= "transfer123";
	public static XMPPConnection connection;
	 ProgressDialog dialog;
	
	public static final String HOST = "172.31.24.80";//"talk.google.com";
	public static final int PORT = 5222;
	
	 private static SystemXComXmppConnection instance = null;
	   protected SystemXComXmppConnection() {
	      
	   }
	   public static SystemXComXmppConnection getInstance() {
	      if(instance == null) {
	         instance = new SystemXComXmppConnection();
	      }
	      return instance;
	   }
	   
	   
	   public void connect(final Activity activity) {

		   
		   activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dialog = ProgressDialog.show(activity,
						"Connecting...", "Please wait...", false);
			}
		});
			/*final ProgressDialog dialog = ProgressDialog.show(context,
					"Connecting...", "Please wait...", false);*/

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// Create a connection
					/*ConnectionConfiguration connConfig = new ConnectionConfiguration(
							HOST, PORT, SERVICE);*/
					ConnectionConfiguration connConfig = new ConnectionConfiguration(
							HOST, PORT);
				    connection = new XMPPConnection(connConfig);

					try {
						connection.connect();
						Log.i("XMPPChatDemoActivity",
								"Connected to " + connection.getHost());
					} catch (XMPPException ex) {
						Log.e("XMPPChatDemoActivity", "Failed to connect to "
								+ connection.getHost());
						Log.e("XMPPChatDemoActivity", ex.toString());
						//setConnection(null);
					}
					try {
						// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
						connection.login(USERNAME, PASSWORD);
						Log.i("XMPPChatDemoActivity",
								"Logged in as " + connection.getUser());

						// Set the status to available
						Presence presence = new Presence(Presence.Type.available);
						connection.sendPacket(presence);
						//setConnection(connection);

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
								+ USERNAME);
						Log.e("XMPPChatDemoActivity", ex.toString());
						//setConnection(null);
					}
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					
				}
			});
			t.start();
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}
}
