package com.sb.sa.pushSubscribe;

import java.util.List;

import com.sb.sa.model.ProcessedMessage;
import com.sb.sa.model.ProcessingRequest;

public interface PushSubscribeManager {

	public void addMessage(ProcessingRequest processingRequest);

	public List<ProcessedMessage> getMessages(String email);
}
