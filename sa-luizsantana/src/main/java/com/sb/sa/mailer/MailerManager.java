package com.sb.sa.mailer;

import java.util.List;

import com.sb.sa.model.ProcessingRequest;
import com.sb.sa.model.Subscriber;

public interface MailerManager {
	
	public void sendEmails(ProcessingRequest processingRequest, List<Subscriber> subscribers);

}
