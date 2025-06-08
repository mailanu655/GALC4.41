package com.honda.galc.common.logging;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.net.Protocol;

public class LogServerNotification implements ILogServerSubscriptionService {
	
	public static ConcurrentHashMap<String, SocketAppender> socketAppenderMap = new ConcurrentHashMap<String, SocketAppender>();
	
	/* (non-Javadoc)
	 * @see com.honda.galc.common.logging.ILogServerSubscriptionService#subscribe(java.lang.String, int)
	 */
	public void subscribe(String hostname, int port) {

		String key = hostname + port;
		
		unSubscribe(hostname, port);
		
		SocketAppender sa = SocketAppender.newBuilder()
                .withAdvertise(false)
                .setConfiguration(null)
                .withConnectTimeoutMillis(30000)
                .withFilter(null)
                .withHost(hostname)
                .withIgnoreExceptions(true)
                .withImmediateFail(true)
                .withLayout(null)
                .withName(key)
                .withPort(port)
                .withProtocol(Protocol.TCP)
                .withReconnectDelayMillis(10000)
                .withSslConfiguration(null)
                .build();
		
		socketAppenderMap.putIfAbsent(key, sa);
		Logger.addAppenders(socketAppenderMap.get(key));
		
	}

	public boolean isAlreadySubscribed(String hostname, int port) {
		String key = hostname + port;
		if (socketAppenderMap.containsKey(key)){
			return Logger.isAppenderSubscribed(socketAppenderMap.get(key));
		}
		return false;
	}
	
	public void unSubscribe(String hostname, int port) {
		
		String key = hostname + port;
		if(isAlreadySubscribed(hostname,port)){
			Logger.removeAppender(socketAppenderMap.get(key));
			socketAppenderMap.remove(key);
		}
		
	}
	
	public void unSubscribeAll(){
		for(String key: socketAppenderMap.keySet()){
			Logger.removeAppender(socketAppenderMap.get(key));
		}
		socketAppenderMap.clear();
	}
	
}
