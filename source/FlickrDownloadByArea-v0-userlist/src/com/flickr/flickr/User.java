package com.flickr.flickr;

public class User {
	private String userId;
	private String userName;
	private int bboxId;
	
	public User(String userId, String userName, int bboxId){
		this.userId=userId;
		this.userName=userName;
		this.bboxId=bboxId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getBboxId() {
		return bboxId;
	}

	public void setBboxId(int bboxId) {
		this.bboxId = bboxId;
	}

	
}
