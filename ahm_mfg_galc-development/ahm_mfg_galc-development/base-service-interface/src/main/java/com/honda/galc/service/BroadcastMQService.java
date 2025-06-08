package com.honda.galc.service;

import com.honda.galc.dto.BroadcastContext;

public interface BroadcastMQService extends IService {
	
	void broadcast(BroadcastContext context);
	
}
