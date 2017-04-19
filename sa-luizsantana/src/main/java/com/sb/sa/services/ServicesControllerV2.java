package com.sb.sa.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sb.sa.Application;
import com.sb.sa.mailer.MailerManager;
import com.sb.sa.model.ProcessedMessage;
import com.sb.sa.pushSubscribe.PushSubscribeManager;

@RestController
public class ServicesControllerV2 extends AbstractServicesController {

	private final static String VERSION = "/v2";

	private static Logger logger = LoggerFactory.getLogger(ServicesControllerV2.class);

	@Autowired
	protected PushSubscribeManager pushSubscribeManager;

	@Autowired
	protected MailerManager mailerManager;
	
	@RequestMapping(value = VERSION + "/subscriber/{email}/message/{timestamp}", method = RequestMethod.GET)
	public ResponseEntity<List<ProcessedMessage>> getMessagesPerEmail(@PathVariable String email,
			@PathVariable Long timestamp) {

		logger.info("Getting messages to subscriber {} after {}", email, timestamp);

		List<ProcessedMessage> filteredMessages = new ArrayList<ProcessedMessage>();
		
		for(ProcessedMessage processedMessage: pushSubscribeManager.getMessages(email)){
			if(processedMessage.getDate().after(new Date(timestamp))){
				filteredMessages.add(processedMessage);
			}
		}
		
		logger.info("Got {} messages to subscriber {}", filteredMessages.size(), email);

		return ResponseEntity.status(HttpStatus.OK).body(filteredMessages);
	}
	
	@RequestMapping(value = VERSION + "/utils/message", method = RequestMethod.GET)
	public int sentMessages() {

		logger.info("Total sent messages {}", Application.SENT_EMAIL);
		
		return Application.SENT_EMAIL;
	}
}