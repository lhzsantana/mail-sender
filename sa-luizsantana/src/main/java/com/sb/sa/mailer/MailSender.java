package com.sb.sa.mailer;

import com.sb.sa.model.ProcessingRequest;
import com.sb.sa.model.Subscriber;

public interface MailSender {

	public void send(ProcessingRequest processingRequest, Subscriber subscriber);

}
