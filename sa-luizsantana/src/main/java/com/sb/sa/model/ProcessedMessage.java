package com.sb.sa.model;

import java.util.Date;

public class ProcessedMessage {
	
	public ProcessedMessage(Message message){
		this.message=message;
		this.date=new Date();
	}
	
	private Message message;
	
	private Date date;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}
}
