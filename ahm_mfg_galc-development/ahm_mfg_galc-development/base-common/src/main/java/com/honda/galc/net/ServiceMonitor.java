package com.honda.galc.net;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;



public class ServiceMonitor {
	
	private static ServiceMonitor instance = new ServiceMonitor();
	
	private Map<String,Endpoint> endpoints = new HashMap<String,Endpoint>();
	private Map<String,Set<ConnectionStatusListener>> listeners = new HashMap<String,Set<ConnectionStatusListener>>();
	private Map<String,Timer> timers = new HashMap<String,Timer>();
	public static ServiceMonitor getInstance() {
		return instance;
	}
	public ServiceMonitor() {
	}
	public void registerListener(String ip,int port,ConnectionStatusListener listener){
		registerListener(new TcpEndpoint(ip,port), listener);
	}
	
	public void registerHttpServiceListener(ConnectionStatusListener listener) {
		registerListener(new HttpEndpoint(), listener);
	}
	
	public static boolean isServerAvailable() {
		return getInstance().isServerConnected();
	}
	
	public boolean isServerConnected() {
		Endpoint newHttpEndpoint = new HttpEndpoint();
		Endpoint endpoint= endpoints.get(newHttpEndpoint.name());
		return endpoint == null ? newHttpEndpoint.connect():
								  endpoint.isConnected();
	}
	
	public void startMonitorHttpService() {
		Endpoint endpoint;
		Endpoint tmp = new HttpEndpoint();
		if(endpoints.containsKey(tmp.name())) {
		    endpoint = endpoints.get(tmp.name());
		}
	    else {
			endpoint = tmp;
			endpoints.put(tmp.name(), tmp);
			tmp.setConnected(tmp.connect());
			listeners.put(tmp.name(), new HashSet<ConnectionStatusListener>());
		}
		
		startTimer(endpoint);
	}
	
	public void registerListener(Endpoint endpoint, ConnectionStatusListener listener) {
		String name = endpoint.name();
		if(!endpoints.containsKey(name)) {
			endpoints.put(name,endpoint);
		};
		endpoint = endpoints.get(endpoint.name());
		if(listeners.containsKey(name)) {
			listeners.get(name).add(listener);
		}else {
			Set<ConnectionStatusListener> servicelisteners = new HashSet<ConnectionStatusListener>();
			if(listener != null) servicelisteners.add(listener);
			listeners.put(name, servicelisteners);
		};
		
		if(listener != null) listener.statusChanged(new ConnectionStatus(endpoint.name(),endpoint.isConnected()));
		startTimer(endpoint);
	}
	
	public void killTimer(Endpoint endpoint) {
		Timer timer = timers.get(endpoint.name());
		if(timer != null) {
			timers.remove(endpoint.name());
			timer.cancel();
		}
	}
	

	
	private void startTimer(Endpoint endpoint) {
		
		if(timers.containsKey(endpoint.name())) return;
		getLogger().info("starting to monitor the server at " + endpoint.name());
		Timer timer = new Timer(endpoint.name());
		timer.schedule(new ServiceTimerTask(endpoint),0, endpoint.heartbeatInterval());
		timers.put(endpoint.name(),timer);
		
	}
	
	private void serviceStatusChanged(Endpoint endpoint) {
		if(endpoint.isConnected()) getLogger().info("Server at " + endpoint.name() + " is connected");
		else getLogger().error("Server at " + endpoint.name() + " is disconnected");
		Set<ConnectionStatusListener> connectionStatusListeners = listeners.get(endpoint.name());
		for(ConnectionStatusListener listener : connectionStatusListeners) {
			listener.statusChanged(new ConnectionStatus(endpoint.name(),endpoint.isConnected()));
		}
	}
	
	private class ServiceTimerTask extends TimerTask {
		
		private Endpoint endpoint;
		public ServiceTimerTask(Endpoint endpoint) {
			this.endpoint = endpoint;
		}
		
		@Override
		public void run() {
			boolean isConnected = endpoint.connect();
			if(endpoint.isConnected() != isConnected) {
				endpoint.setConnected(isConnected);
				serviceStatusChanged(endpoint);
			}
		}
		
	}
	
}
