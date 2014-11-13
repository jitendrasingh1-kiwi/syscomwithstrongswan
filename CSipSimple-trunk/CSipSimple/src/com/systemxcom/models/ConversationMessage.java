package com.systemxcom.models;

import java.util.Date;

public class ConversationMessage
{
	private String message;
	private int id;
	private String userId;
	private int userName;
	private Date date = new Date();
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getUserName() {
		return userName;
	}
	public void setUserName(int userName) {
		this.userName = userName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
