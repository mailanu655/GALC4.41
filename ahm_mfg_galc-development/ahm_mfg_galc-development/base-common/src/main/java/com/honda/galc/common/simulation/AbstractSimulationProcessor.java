package com.honda.galc.common.simulation;


import com.honda.galc.common.logging.LogSubscribers;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.SocketAppender;
import com.honda.galc.notification.service.INotificationService;

public abstract class AbstractSimulationProcessor implements ISimulationProcessor,INotificationService{
	
	protected RobotGuiEvent simEvent;
	
	/**
	 * subscribe log server for the GALC simulator to get all log infos
	 * @param hostname
	 * @param port
	 */
	public void subscribe(String hostname, int port){
		LogSubscribers subscribers = LogSubscribers.getInstance();
		String key = hostname + port;
		String val = key;
		
		if (!subscribers.isSubscriber(key)) {
			subscribers.add(key, val);
			SocketAppender sa = SocketAppender.newBuilder().setPrimaryHost(hostname).setPrimaryPort(port).build();
			Logger.addAppenders(sa);
			Logger.getLogger().info("Added Socket appender for GALC simulator at " + hostname + ":" + port );
		}
	}
	
	/**
	 * entrance point of all simulation requests
	 * @param eventVo
	 */
	public void execute(RobotGuiEvent event) {
		createRobot();
		
		this.simEvent = event;
		if(event.isPrintComponents()) printComponents();
		
		if(simEvent != null) 
			simEvent.execute(this);
	}
	
	protected abstract void printComponents(); 

	protected abstract void createRobot();
}
