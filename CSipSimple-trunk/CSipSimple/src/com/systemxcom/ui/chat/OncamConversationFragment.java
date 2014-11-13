package com.systemxcom.ui.chat;

import java.util.ArrayList;
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

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.internal.widget.IcsAdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.view.Menu;
import com.csipsimple.R;
import com.systemxcom.models.ConversationMessage;

public class OncamConversationFragment extends ListFragment //implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener{
{	private static final String TAG = OncamConversationFragment.class.getSimpleName();

	private boolean mIsLoading = false;
	private int mPosition = 0;
	
	private OncamChatAdapter oncamChatAdapter=null;
	private EditText uiInput;
	
	//public static final String HOST = "talk.google.com";
	public static final String HOST = "172.31.24.80";//"talk.google.com";
	public static final int PORT = 5222;
	//public static final String SERVICE = "gmail.com";
	//public static final String USERNAME = "achelese@gmail.com";
	//public static final String USERNAME = "rohit";//"jitendra";
	//public static final String PASSWORD = "jitu2shalujitendra";
	//public static final String PASSWORD = "system123";//"transfer123";
	
	//public static final String USERNAME ="jitendra";
	//public static final String PASSWORD = "transfer123";

	private XMPPConnection connection;
	private ArrayList<String> messages = new ArrayList<String>();
	private ArrayList<ConversationMessage> chatMessages = new ArrayList<ConversationMessage>();
	private Handler mHandler = new Handler();

	//private ListView listview;
	private EditText recipient;
	

	public EditText getConversationEditText() {
		return uiInput;
	}

	class OncamChatAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return chatMessages.size();//OncamXmppConnection.getInstance().getConversationList().size();
		}

		@Override
		public Object getItem(int position) {
			return chatMessages.get(position);//OncamXmppConnection.getInstance().getConversationList().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			mPosition = position;
			if (null == convertView) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.v4_basic_chat_message_container, null);
			}
			TextView message = (TextView) convertView.findViewById(R.id.v4_chat_message_text);
			final View uiLayoutInner = convertView.findViewById(R.id.v4_basic_chat_message_inner);
			final ImageView uiAvatarMy = (ImageView) convertView.findViewById(R.id.v4_basic_chat_message_avatar_left);
            final ImageView uiAvatarOpponent = (ImageView) convertView.findViewById(R.id.v4_basic_chat_message_avatar_right);
			
			boolean _right = chatMessages.get(position).getUserId().equalsIgnoreCase(SystemXComXmppConnection.USERNAME);
			int resourceBubbleId = _right?R.drawable.right_bubble : R.drawable.left_bubble;
			
			int gravity = _right ? Gravity.RIGHT : Gravity.LEFT;
			
			/*if(_right)
				uiAvatarMy.setVisibility(View.VISIBLE);
			else
				uiAvatarOpponent.setVisibility(View.VISIBLE);*/
			
			((RelativeLayout)convertView.findViewById(R.id.content_view)).setGravity(gravity);
			
			uiLayoutInner.setBackgroundResource(resourceBubbleId);
			message.setText(chatMessages.get(position).getMessage());

			return convertView;
		}

	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (null == savedInstanceState)
			savedInstanceState = getArguments();
		if (null == savedInstanceState)
			savedInstanceState = getActivity().getIntent().getExtras();

		//getListView().setOnScrollListener(new StartlessScrollListener());
		oncamChatAdapter = new OncamChatAdapter();
		setListAdapter(oncamChatAdapter);
		//registerForContextMenu(getListView());  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.v4_oncam_conversation_fragment, null);
		
		ImageView sendImage = (ImageView)v.findViewById(R.id.v4_oncam_conversation_input_send);
		recipient = (EditText)v.findViewById(R.id.toET);
		uiInput = (EditText)v.findViewById(R.id.v4_oncam_conversation_input_text);
		//listview = (ListView)v.findViewById(R.id.)
		sendImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String to = recipient.getText().toString()+"@172.31.24.80";
				String text = uiInput.getText().toString();
				
				if(!text.equalsIgnoreCase(""))
				{
					ConversationMessage conversationMessage = new ConversationMessage();
					conversationMessage.setUserId(SystemXComXmppConnection.USERNAME);
					conversationMessage.setMessage(text);
					chatMessages.add(conversationMessage);
	
					Log.i("XMPPChatDemoActivity", "Sending text " + text + " to " + to);
					Message msg = new Message(to, Message.Type.chat);
					msg.setBody(text);				
					if (connection != null) {
						connection.sendPacket(msg);
						//messages.add(connection.getUser() + ":");
						messages.add(text);
						setListAdapter(oncamChatAdapter);
						uiInput.setText("");
					}
				}
			
			}
		});

		connect();
		return v;
	}

	/**
	 * Called by Settings dialog when a connection is establised with the XMPP
	 * server
	 * 
	 * @param connection
	 */
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
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
						chatMessages.add(conversationMessage);
						
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
								+ " from " + fromName );
						messages.add(fromName + ":");
						messages.add(message.getBody());
						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
								setListAdapter(oncamChatAdapter);
							}
						});
					}
				}
			}, filter);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if (connection != null)
				connection.disconnect();
		} catch (Exception e) {

		}
	}
	
	public void connect() {

		final ProgressDialog dialog = ProgressDialog.show(getActivity(),
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
				}
				try {
					// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
					connection.login(SystemXComXmppConnection.USERNAME, SystemXComXmppConnection.PASSWORD);
					Log.i("XMPPChatDemoActivity",
							"Logged in as " + connection.getUser());

					// Set the status to available
					Presence presence = new Presence(Presence.Type.available);
					connection.sendPacket(presence);
					setConnection(connection);

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
							+ SystemXComXmppConnection.USERNAME+"   password"+SystemXComXmppConnection.PASSWORD);
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
				}

				dialog.dismiss();
			}
		});
		t.start();
		dialog.show();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    String title = "options";
	    menu.setHeaderTitle(title);

	    //menu.add(Menu.NONE,Menu.NONE);
	}
	
	public void deleteMessage()
	{
		 new CountDownTimer(1800000, 1700000) {

		     public void onTick(long millisUntilFinished) {
		         //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		         //mTextField.setText("done!");
		    	 for(ConversationMessage message:chatMessages)
		    	 {
		    		 if(message.getUserId().contains(SystemXComXmppConnection.USERNAME))
		    		 {
		    			 chatMessages.remove(message);
		    		 }
		    	 }
		    	 setListAdapter(oncamChatAdapter);
		     }
		  }.start();
	}

}
