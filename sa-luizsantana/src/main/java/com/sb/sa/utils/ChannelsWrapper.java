package com.sb.sa.utils;

import java.util.ArrayList;
import java.util.List;

import com.sb.sa.model.Channel;

public class ChannelsWrapper {

	private List<Channel> channels= new ArrayList<>();

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}
