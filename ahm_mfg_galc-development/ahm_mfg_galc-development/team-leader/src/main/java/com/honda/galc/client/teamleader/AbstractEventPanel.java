package com.honda.galc.client.teamleader;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;

public abstract class AbstractEventPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private Map<Object, IClientEventListener> listeners;
	
	
	
	public AbstractEventPanel() {
		super();
		initialize();
	}


	private void initialize() {
		listeners = new HashMap<Object, IClientEventListener>();
		
	}


	public void register(Object event, IClientEventListener listener){
		listeners.put(event, listener);
	}
	
	public void publish(Object event) {
		if(listeners != null && listeners.containsKey(event))
			listeners.get(event).processEvent(event);
		else
			EventBus.publish(event);
	}
}
