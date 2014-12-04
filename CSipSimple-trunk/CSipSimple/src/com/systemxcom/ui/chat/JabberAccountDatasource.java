package com.systemxcom.ui.chat;

import java.util.ArrayList;

import com.systemxcom.models.JabberAccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class JabberAccountDatasource 
{
	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_ACCOUNT_NAME , 
	      MySQLiteHelper.COLUMN_USER_NAME , 
	      MySQLiteHelper.COLUMN_PASSWORD ,
	      MySQLiteHelper.COLUMN_DOMAIN };

	  public JabberAccountDatasource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public JabberAccount createAccount(JabberAccount jabberAccount) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_ACCOUNT_NAME, jabberAccount.getAccountName());
	    values.put(MySQLiteHelper.COLUMN_USER_NAME, jabberAccount.getUserName());
	    values.put(MySQLiteHelper.COLUMN_PASSWORD, jabberAccount.getPassword());
	    values.put(MySQLiteHelper.COLUMN_DOMAIN, jabberAccount.getDomain());
	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_JABBER_ACCOUNT, null,
	        values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_JABBER_ACCOUNT,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    JabberAccount account = cursorToAccount(cursor);
	    cursor.close();
	    return account;
	  }

	  public void deleteAccount(JabberAccount account) {
	    long id = account.getId();
	    System.out.println("Comment deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_JABBER_ACCOUNT, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public ArrayList<JabberAccount> getAllAccounts() {
		  ArrayList<JabberAccount> accounts = new ArrayList<JabberAccount>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_JABBER_ACCOUNT,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	JabberAccount account = cursorToAccount(cursor);
	    	accounts.add(account);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return accounts;
	  }

	  private JabberAccount cursorToAccount(Cursor cursor) {
		  JabberAccount account = new JabberAccount();
		  account.setId(cursor.getLong(0));
		  account.setAccountName(cursor.getString(1));
		  account.setUserName(cursor.getString(2));
		  account.setPassword(cursor.getString(3));
		  account.setDomain(cursor.getString(4));
	    
	    return account;
	  }

}
