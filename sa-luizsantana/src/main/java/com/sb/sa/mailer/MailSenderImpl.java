package com.sb.sa.mailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sb.sa.Application;
import com.sb.sa.model.ProcessingRequest;
import com.sb.sa.model.Subscriber;

@Service
public class MailSenderImpl implements MailSender {

	@Value("${email.from}")
	private String EMAIL_FROM;
	
	@Autowired
	private JavaMailSender mailSender;

	@Async
	@Override
	public void send(ProcessingRequest processingRequest, Subscriber subscriber) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();

			message.setTo(subscriber.getEmail());
			message.setSubject(processingRequest.getRequest().getMessage().getSubject());
			message.setText(processingRequest.getRequest().getMessage().getMessage());

			mailSender.send(message);
		} catch (Exception e) {
			processingRequest.addToFailures(subscriber);
		}

		Application.SENT_EMAIL++;
	}

}
