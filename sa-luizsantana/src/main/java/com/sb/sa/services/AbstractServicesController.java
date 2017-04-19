package com.sb.sa.services;

import java.util.HashMap;
import java.util.Map;

import com.sb.sa.model.ProcessingRequest;

public abstract class AbstractServicesController {

	protected static Map<String, ProcessingRequest> allRequests = new HashMap<String, ProcessingRequest>();

}
