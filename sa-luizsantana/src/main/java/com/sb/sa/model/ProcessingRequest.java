package com.sb.sa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProcessingRequest {

	private Request request;
	
	private List<RequestStatus> history = new ArrayList<RequestStatus>();

	private String uuid;

	private Date start;
	
	private List<Subscriber> failures = new ArrayList<Subscriber>();

	public ProcessingRequest(Request request) {
		this.request=request;
		this.uuid = UUID.randomUUID().toString();
		this.start = new Date();
	}
	
	public List<RequestStatus> getHistory() {
		return history;
	}

	public String getUuid() {
		return uuid;
	}

	public List<Subscriber> getFailures() {
		return failures;
	}

	public void addToFailures(Subscriber failure) {
		this.failures.add(failure);
	}
	public void addStatus(RequestStatus status) {
		history.add(status);
	}

	public Date getStart() {
		return start;
	}
	
	public Request getRequest() {
		return request;
	}
}
