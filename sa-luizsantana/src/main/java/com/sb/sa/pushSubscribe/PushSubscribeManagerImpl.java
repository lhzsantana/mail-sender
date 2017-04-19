package com.sb.sa.pushSubscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sb.sa.model.ProcessedMessage;
import com.sb.sa.model.ProcessingRequest;
import com.sb.sa.model.RequestStatus;
import com.sb.sa.model.Subscriber;

@Service
public class PushSubscribeManagerImpl implements PushSubscribeManager {

	private static Map<String, List<ProcessedMessage>> messagesPerSubscriber = new HashMap<String, List<ProcessedMessage>>();

	@Async
	@Override
	public void addMessage(ProcessingRequest processingRequest) {

		processingRequest.addStatus(RequestStatus.PROCESSING_PUSH_SUBSCRIBE);
		List<ProcessedMessage> messages = null;

		for (Subscriber subscriber : processingRequest.getRequest().getSubscribers()) {

			messages = messagesPerSubscriber.get(subscriber.getEmail());

			if (messages == null) {
				messages = new ArrayList<ProcessedMessage>();
			}
			
			messages.add(new ProcessedMessage(processingRequest.getRequest().getMessage()));
			messagesPerSubscriber.put(subscriber.getEmail(), messages);
		}
		
		processingRequest.addStatus(RequestStatus.COMPLETED_PUSH_SUBSCRIBE);
	}

	@Override
	public List<ProcessedMessage> getMessages(String email) {

		List<ProcessedMessage> messages = messagesPerSubscriber.get(email);

		if (messages == null) {
			return new ArrayList<ProcessedMessage>();
		}

		return messages;
	}

}
