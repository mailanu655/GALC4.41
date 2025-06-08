package com.honda.galc.device.simulator.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimulatorSenderManager {
	private Map<String, IEiSender> _senders = Collections.synchronizedMap(new HashMap<String, IEiSender>());
	
	public void add(String key, IEiSender sender) {
		_senders.put(key, sender);
		
	}

	public IEiSender get(String key) {
		return _senders.get(key);
	}

	public Map<String, IEiSender> get_senders() {
		return _senders;
	}

	public void setSenders(Map<String, IEiSender> _senders) {
		this._senders = _senders;
	}
	
	public void cleanUp()
	{
		_senders.clear();
	}
	
	
}
