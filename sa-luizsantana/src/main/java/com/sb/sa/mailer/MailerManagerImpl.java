package com.sb.sa.mailer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sb.sa.model.ProcessingRequest;
import com.sb.sa.model.RequestStatus;
import com.sb.sa.model.Subscriber;

@Service
public class MailerManagerImpl implements MailerManager {

	@Autowired
	private MailSender mailSender;

	@Async
	@Override
	public void sendEmails(ProcessingRequest processingRequest, List<Subscriber> subscribers) {

		processingRequest.addStatus(RequestStatus.PROCESSING_EMAIL);

		for (Subscriber subscriber : subscribers) {

			mailSender.send(processingRequest, subscriber);

		}

		processingRequest.addStatus(RequestStatus.COMPLETED_EMAIL);
	}

}
