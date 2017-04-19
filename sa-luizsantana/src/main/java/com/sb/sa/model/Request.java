package com.sb.sa.model;

import java.util.ArrayList;
import java.util.List;

public class Request {

	private List<Channel> channels = new ArrayList<Channel>();

	private List<Subscriber> subscribers = new ArrayList<Subscriber>();

	private Message message;

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public List<Subscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
