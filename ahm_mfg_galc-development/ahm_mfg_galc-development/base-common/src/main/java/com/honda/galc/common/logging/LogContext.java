package com.honda.galc.common.logging;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 
 * <h3>LogContext Class description</h3>
 * <p> LogContext description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Apr 28, 2010
 *
 */

public class LogContext implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private static LogContext logContext = new LogContext();
    private static ThreadLocal<LogContext> localLogContext = null;
    private String applicationName = "default1";
    private String clientName;
    private String subClientName;
    private String hostName;
    private String threadName;
    private boolean isApplicationInfoNeeded = false;
    private boolean isMultipleLine = true;
    private boolean isCenterLog = true;
    
    private LogLevel applicationLogLevel;
    

	public LogContext() {
        this.hostName = fetchHostName();
    }
    

    public LogContext(LogContext context) {
        if(context != null) copyContext(context);
        this.threadName = fetchThreadName();
    }
    
    public static LogContext getContext(){
        return logContext;
    }
    
    public static LogContext getThreadLocalContext() {
        if(localLogContext == null || localLogContext.get() == null || isNeedRefreshLocalLogContext()) createThreadLocalLogContext();
        return localLogContext.get();
    }
    
    private static boolean isNeedRefreshLocalLogContext() {
    	return localLogContext.get().getApplicationLogLevel() == null ? true :  localLogContext.get().getApplicationLogLevel() != logContext.getApplicationLogLevel();
	}


	private static void createThreadLocalLogContext(){
        localLogContext = new ThreadLocal<LogContext>();
        LogContext context = new LogContext(logContext);
        localLogContext.set(context);
    }
	


    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public String getSubClientName() {
		return subClientName;
	}


	public void setSubClientName(String subClientName) {
		this.subClientName = subClientName;
	}


	public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    
    private void copyContext(LogContext context){
        this.applicationName = context.getApplicationName();
        this.hostName = context.getHostName();
        this.clientName = context.getClientName();
        this.subClientName = context.getSubClientName();
        this.applicationLogLevel = context.getApplicationLogLevel();
    }
    
    public LogContext clone() {
    	
   		return new LogContext(this);
   		
    }
    
    private String fetchHostName(){
    	NetworkInterface iface = null;
		try{
			for(Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();ifaces.hasMoreElements();){
	    		iface = (NetworkInterface)ifaces.nextElement();
	    		InetAddress ia = null;
	    		for(Enumeration<InetAddress> ips = iface.getInetAddresses();ips.hasMoreElements();){
	    			ia = (InetAddress)ips.nextElement();
	    			if(!ia.isLoopbackAddress() && (!ia.isLinkLocalAddress()) && (ia instanceof Inet4Address)){
	    				return ia.getHostName();
	    			}
	    		}
	    	}
		}catch(Exception e){
			getLogger().error(e, "Unable to get Host Address. Please Contact IS");
		}
        return null;
    }
    
    private String fetchThreadName(){
        return Thread.currentThread().getName();
    }


    public LogLevel getApplicationLogLevel() {
		return applicationLogLevel;
	}


	public void setApplicationLogLevel(LogLevel applicationLogLevel) {
		this.applicationLogLevel = applicationLogLevel;
		
	}


	public boolean isApplicationInfoNeeded() {
		return isApplicationInfoNeeded;
	}


	public void setApplicationInfoNeeded(boolean isApplicationInfoNeeded) {
		this.isApplicationInfoNeeded = isApplicationInfoNeeded;
	}


	public boolean isCenterLog() {
		return isCenterLog;
	}


	public void setCenterLog(boolean isCenterLog) {
		this.isCenterLog = isCenterLog;
	}


	public boolean isMultipleLine() {
		return isMultipleLine;
	}


	public void setMultipleLine(boolean isMultipleLine) {
		this.isMultipleLine = isMultipleLine;
	}
	
	

    
}
