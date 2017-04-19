package com.sb.sa.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sb.sa.mailer.MailerManager;
import com.sb.sa.model.Channel;
import com.sb.sa.model.ProcessedMessage;
import com.sb.sa.model.ProcessingRequest;
import com.sb.sa.model.Request;
import com.sb.sa.model.RequestStatus;
import com.sb.sa.pushSubscribe.PushSubscribeManager;

@RestController
public class ServicesControllerV1 extends AbstractServicesController {

	private final static String VERSION = "/v1";

	private static Logger logger = LoggerFactory.getLogger(ServicesControllerV1.class);

	@Value("${service.bulk.size}")
	private int SERVICE_BULK_SIZE;

	@Autowired
	protected PushSubscribeManager pushSubscribeManager;

	@Autowired
	protected MailerManager mailerManager;

	/*
	 * @ApiOperation(value = "Adds a new message sender request.", notes =
	 * "A UUID will be returned so the caller will know the status of the request;"
	 * , response = Request.class)
	 * 
	 * @ApiImplicitParams({
	 * 
	 * @ApiImplicitParam(name = "message", value =
	 * "The textual message for the email", required = true, paramType =
	 * "query"),
	 * 
	 * @ApiImplicitParam(name = "subscribers", value = "The list of subscribers"
	 * , required = true, paramType = "query"),
	 * 
	 * @ApiImplicitParam(name = "channels", value = "The list of channels",
	 * required = true, paramType = "query")})
	 */
	@RequestMapping(value = VERSION + "/message", method = RequestMethod.POST)
	public ResponseEntity<ProcessingRequest> addMessage(@RequestBody Request request) {

		if (request.getSubscribers().size() > SERVICE_BULK_SIZE) {
			logger.error("The number of subscribers "+request.getSubscribers().size()+" is bigger than limit of " + SERVICE_BULK_SIZE);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		if (request.getChannels().isEmpty()) {
			logger.error("No channel");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		ProcessingRequest processingRequest = processRequest(request);

		return ResponseEntity.status(HttpStatus.OK).body(processingRequest);
	}

	@RequestMapping(value = VERSION + "/subscriber/{email}/message", method = RequestMethod.GET)
	public ResponseEntity<List<ProcessedMessage>> getMessagesPerEmail(@PathVariable String email) {

		logger.info("Getting messages to subscriber {}", email);

		List<ProcessedMessage> messages = pushSubscribeManager.getMessages(email);

		logger.info("Got {} messages to subscriber {}", messages.size(), email);

		return ResponseEntity.status(HttpStatus.OK).body(messages);
	}

	@RequestMapping(value = VERSION + "/message/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<ProcessingRequest> getRequest(@PathVariable String uuid) {

		ProcessingRequest processingRequest = allRequests.get(uuid);

		logger.info("Getting request {}", uuid);

		if (processingRequest == null) {
			ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(processingRequest);
	}

	private ProcessingRequest processRequest(Request request) {

		ProcessingRequest processingRequest = new ProcessingRequest(request);
		processingRequest.addStatus(RequestStatus.ADDED);
		
		allRequests.put(processingRequest.getUuid(), processingRequest);

		logger.info("Sending a new message {}", processingRequest.getUuid());

		if (processingRequest.getRequest().getChannels().contains(Channel.PUSH_SUBSCRIBE)) {

			logger.info("Adding {} to push subscribe", processingRequest.getUuid());
			pushSubscribeManager.addMessage(processingRequest);
		}

		if (processingRequest.getRequest().getChannels().contains(Channel.EMAIL)) {

			logger.info("Adding {} to email ", processingRequest.getUuid());
			mailerManager.sendEmails(processingRequest, processingRequest.getRequest().getSubscribers());
		}
		
		return processingRequest;

	}

}