package com.systemxcom.models;

public class JabberAccount 
{
	private long id;
	private String accountName;
	private String userName;
	private String domain;
	private String password;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAccountName() {
		return accountName;
	}
	public String getUserName() {
		return userName;
	}
	public String getDomain() {
		return domain;
	}
	public String getPassword() {
		return password;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
