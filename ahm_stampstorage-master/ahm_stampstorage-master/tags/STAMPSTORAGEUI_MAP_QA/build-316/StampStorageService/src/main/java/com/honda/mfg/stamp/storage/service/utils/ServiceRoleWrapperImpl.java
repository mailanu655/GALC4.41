package com.honda.mfg.stamp.storage.service.utils;

import com.honda.mfg.connection.processor.messages.ConnectionExTrigger;
import com.honda.mfg.connection.processor.messages.MissedPingPassive;
import com.honda.mfg.connection.watchdog.WatchdogAdapterInterface;
import com.honda.mfg.stamp.conveyor.domain.AuditErrorLog;
import com.honda.mfg.stamp.conveyor.domain.ServiceRole;
import com.honda.mfg.stamp.conveyor.processor.ConnectionEventMessage;
import com.honda.mfg.stamp.storage.service.clientmgr.SocketConnectionConstantsInterface;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author VCC44349
 *
 */
public final class ServiceRoleWrapperImpl implements ServiceRoleWrapper, SocketConnectionConstantsInterface {

	@Override
	public void run() {
		LOG.info("ServiceRoleWrapperImpl#run: processing service roles");
		try {
			processServiceRoles();
		} catch (Exception e) {
			LOG.info("ServiceRoleWrapperImpl#run:" + e.getMessage());
			e.printStackTrace();
		}
		finally  {
			bExTrigger = false;
		}
	}

	@Transactional
	private void processServiceRoles()  {
		if(ipAll == null)  {return;}
		setRoleChangeReason(null);
		for (InetAddress thisInet : ipAll) {
			String thisIp = thisInet.getHostAddress();
			ServiceRole thisRole = null, activeRole = null;
			if(thisIp != null && !"".equals(thisIp.trim()))  {
				List<ServiceRole> thisRoleList = ServiceRole.findServiceRolesByIpEqualsAndPort(
							thisIp.trim(), port).getResultList();
				List<ServiceRole> activeRoleList = ServiceRole.findServiceRolesByCurrentActiveNot(false).getResultList();
				if(thisRoleList.size() > 0) {thisRole = (ServiceRole)thisRoleList.get(0);}
				if(thisRole == null)  {continue;}
				serviceRole = thisRole;
				if(activeRoleList.size() > 0) {activeRole = (ServiceRole)activeRoleList.get(0);}
				if(shouldIStart(thisRole, activeRole))  {
					if(isPassive())  {
						setPassive(false);
						latch.countDown();
						notifyAllRoleChange();
						persistRoles(thisRole, activeRoleList);
						LOG.info("ServiceRoleWrapperImpl#run: starting service");
						logAuditError("Service start: " + thisIp + "#" + thisRole.getServiceName());
						setMissedPing(false);
					}
					else  {
						LOG.info("ServiceRoleWrapperImpl#run: I was asked to start, but I'm already active");
					}
				}
				else if(shouldIStop(thisRole, activeRole))  {
					if(!isPassive())  {
						setPassive(true);
						latch = new CountDownLatch(1);
						notifyAllRoleChange();
						persistRoles(activeRole, thisRole);
						LOG.info("ServiceRoleWrapperImpl#run: stopping service");
						logAuditError("Service stop: " + thisIp + "#" + thisRole.getServiceName());
					}
					else  {
						LOG.info("ServiceRoleWrapperImpl#run: I was asked to stop, but I'm already passive");
					}
				}
				// 2013-11-12:VB The following is a work-around for the read errors in ConnectionResponseProcessor
				//In this case stop the current service and force start the next in fail-over order
				else if(shouldIStopAndStartNext())  {
					List<ServiceRole> all = ServiceRole.findAllServiceRolesOrderByFailoverOrder();
					ServiceRole nextRole = getNext(all, thisRole);
					setRoleChangeReason(RoleChangeReason.COMMUNICATION_ERRORS);
					if(!isPassive() && nextRole != null)  {
						setPassive(true);
						latch = new CountDownLatch(1);
						notifyAllRoleChange();
						persistRoles(nextRole, all);
						LOG.info("ServiceRoleWrapperImpl#run: stopping service, force-start next");
						String nextIp = nextRole.getIp();
						nextIp = nextIp.isEmpty() ? "" : nextIp.trim();
						StringBuilder auditMessage = 
								new StringBuilder("Service stop: " + thisIp + "#" + thisRole.getServiceName());
						auditMessage.append("\nStarting service: " + nextIp + "#" + nextRole.getServiceName());
						logAuditError(auditMessage.toString());
					}
					else  {
						LOG.info("ServiceRoleWrapperImpl#run: I was asked to stop, but I'm already passive");
					}
				}
				else  {
					LOG.info("ServiceRoleWrapperImpl#run: no action, exiting");
				}
			}
		}
	}
	
	@Transactional
	private void persistRoles(ServiceRole active, ServiceRole passive)  {
		if(active != null)  {
			active.setCurrentActive(true);
			active.merge();
		}
		if(passive != null && active != null && passive.getId() != active.getId())  {
			passive.setCurrentActive(false);
			passive.merge();
		}

	}

	/**
	 * set current instance as active in database, set all other roles that are currently active to passive.
	 * ordinarily, there should be only 1 currently active - but if there are multiple, set them all inactive
	 * @param active - current service instance
	 * @param passiveRoles - list of roles that are currently active
	 */
	@Transactional
	private void persistRoles(ServiceRole active, List<ServiceRole> passiveRoles)  {
		if(active != null)  {
			active.setCurrentActive(true);
			active.merge();
		}
		if(passiveRoles != null && active != null && passiveRoles.size() > 0)  {
			for (ServiceRole passive : passiveRoles)  {
				if(passive != null && passive.getId() != active.getId())  {
					passive.setCurrentActive(false);
					passive.merge();
				}					
			}
		}
	}

	private void logAuditError(String message) {
	   String roleChange = "";
       Calendar c = Calendar.getInstance();
       Timestamp timestamp = new Timestamp(c.getTimeInMillis());
       AuditErrorLog errorLog = new AuditErrorLog();
       errorLog.setLogTimestamp(timestamp);
       if(getRoleChangeReason() != null) {roleChange = getRoleChangeReason().getMessage();}
       errorLog.setMessageText(roleChange + ":" + message);
       errorLog.setNodeId(serviceRole.getServiceName());
       errorLog.setSeverity(1);
       errorLog.setSource(serviceRole.getServiceName() + "#logAuditError");

       errorLog.persist();

   }

	@Override
	public void init()  {
		try {
			String cHost = InetAddress.getLocalHost().getCanonicalHostName();
			port = Integer.valueOf(socketProperties.getProperty(SERVER_PORT_PROPERTY_KEY,
					String.valueOf(DEFAULT_PORT))).intValue();
			InetAddress[] all = InetAddress.getAllByName(cHost);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < all.length; i++) {
				ipAll.add(all[i]);
				String ip = all[i].getHostAddress();
				sb.append(ip).append(", ");

			}
			latch = new CountDownLatch(1);
			LOG.info("canonical host name:" + cHost);
			LOG.info("IP addresses found: " + sb.toString());
		} catch (Exception e) {
			LOG.error("ServiceRoleWrapperImpl#init()" + e.getMessage());
		}		
	}
	
	private boolean shouldIStart(ServiceRole thisRole, ServiceRole activeRole)  {
		boolean shouldI = false;
		List<ServiceRole> all = ServiceRole.findAllServiceRolesOrderByFailoverOrder();
		if (thisRole == null)  {shouldI = false;}
		else if(thisRole.getCurrentActive() && isRunningActive())  {shouldI = false;}
			//do nothing, already running as active
		else if(thisRole.getCurrentActive() && !isRunningActive()) {
			shouldI = true;
			setRoleChangeReason(RoleChangeReason.FORCE_START);
			//force start, admin is asking me to run in active mode
		}
		else if (bExTrigger)  {shouldI = false;}
		else if(!thisRole.getCurrentActive() && thisRole.getDesignatedPrimary()
				&& activeRole == null)  {
			shouldI = true;
			setRoleChangeReason(RoleChangeReason.DESIGNATED_PRI);
			//I am primary and not active and no other instance is active
		}
		else if(!thisRole.getCurrentActive() && !isRunningActive() && isMissedPing())  {
			shouldI = nextInLine(all, thisRole, activeRole);
			setRoleChangeReason(RoleChangeReason.MISSED_PING);
		}
		else {shouldI = false;}
		return shouldI;
	}
	
	protected boolean nextInLine(List<ServiceRole> allRoles, ServiceRole thisRole, ServiceRole activeRole)  {
		
		int thisPos = 0, thatPos = 0;
		boolean isNext = false;
		
		if(allRoles == null || allRoles.size() == 0)  {isNext = false;}
		else if(thisRole == null || activeRole == null) {isNext = false;}
		//if failover order is set to 0, don't participate in failover
		else if(thisRole.getFailoverOrder() <= 0) {isNext = false;}
		else if(thisRole.getId() == activeRole.getId())  {isNext = true;}
			//this service is the same as the one that is currently marked active
		else  { //current service is not the same as the one marked active
			int listSize = allRoles.size();
			for (int i = 0; i < listSize; i++) {
				ServiceRole r = allRoles.get(i);
				if(r == null) {continue;}
				if(thisRole.getId() == r.getId())  {
					thisPos = i + 1;
				}
				else if(activeRole.getId() == r.getId())  {
					thatPos = i + 1;
				}
			}
			if(thisPos - thatPos%listSize == 1)  { //operator precedence is: %, -, ==
				isNext = true;
				//set below at the end: serviceWatchdogAdapter.resetMaxWaitCount();
			}
			else  {
				//if wait count value was previously incremented, it is time for this instance to startup
				//else, it is the first missed ping, increase the wait count
				if(serviceWatchdogAdapter.isAdaptiveWaitCount())  {isNext = true;}
				else {
					int failoverQueuePos = 0;
					if(thisPos > thatPos)  {//failover_order of this instance higher than active instance
						failoverQueuePos = thisPos - thatPos;
					}
					else  {
						failoverQueuePos = thisPos - thatPos + listSize;						
					}
					int delta = serviceWatchdogAdapter.getPassiveWaitCount()
							* (int) Math.pow(2, (failoverQueuePos - 1));
					LOG.info("incrementing missed ping wait count by: " + delta);
					serviceWatchdogAdapter.incrementWaitCount(delta);
					setMissedPing(false);
					isNext = false;
				}
			}
		}
		if(isNext) {serviceWatchdogAdapter.resetMaxWaitCount();}
		return isNext;
	}
	
	private ServiceRole getNext(List<ServiceRole> allRoles, ServiceRole thisRole)  {
		
		int thisPos = 0, listSize = 0;
		boolean bFoundCurrent = false;
		
		//if something wrong with input, return the parameter itself
		if(thisRole == null || allRoles.isEmpty())  {
			return thisRole;
		}
		else  { //the list is not empty
			listSize = allRoles.size();
			//find "thisRole" in the list of sorted service roles
			for (int i = 0; i < listSize; i++) {
				ServiceRole r = allRoles.get(i);
				if(r == null) {continue;}
				if(thisRole.getId() == r.getId())  {
					thisPos = i + 1;
					bFoundCurrent = true;
					break;
				}
			}
			if(!bFoundCurrent)  {  //thisRole not found, something wrong, return null;
				return null;
			}
			else  {
				//modulo (i+1)%listSize gives 0-based index of next element.
				//if i is the matched index for thisRole, say 0, 1, 2, 3, 4
				//j will be 1, 2, 3, 4, 0
				int j = thisPos % listSize;
				return allRoles.get(j);
			}
		}
	}
	
	private boolean shouldIStop(ServiceRole thisRole, ServiceRole activeRole)  {
		boolean shouldI = false;
		if (thisRole == null)  {shouldI = false;}
		else if(!thisRole.getCurrentActive() && isRunningActive())  {
			shouldI = true;
			setRoleChangeReason(RoleChangeReason.FORCE_STOP);
			//force stop, admin is asking me to run in passive mode
		}
		else {shouldI = false;}
		return shouldI;
	}
	
	private boolean shouldIStopAndStartNext()  {
		return (isRunningActive() && bExTrigger);
	}

	@EventSubscriber(eventClass = ConnectionExTrigger.class, referenceStrength = ReferenceStrength.STRONG)
    public void setExTrigger(ConnectionExTrigger thisTrigger) {
        LOG.error("received ConnectionExTrigger event: exCount=" + thisTrigger.getExCount());
		if(thisTrigger != null)  {bExTrigger = true;}
	}

	@EventSubscriber(eventClass = MissedPingPassive.class, referenceStrength = ReferenceStrength.STRONG)
    public void passiveMissedPing(MissedPingPassive msg) {
        LOG.error("received MissedPingPassive: current_wait_count=" + msg.getCurrentWaitCount());
		setMissedPing(true);
	}

	@EventSubscriber(eventClass = ConnectionEventMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void resetConnectionState(ConnectionEventMessage msg) {
        LOG.error("received connection event: " + msg.isConnected());
        if(msg.isConnected())  {
        	setMissedPing(false);
        	serviceWatchdogAdapter.resetMaxWaitCount();
        }
        //else don't do anything --> missedPing=true will be set when MissedPingPassive is received.
	}

	private ServiceRoleWrapperImpl() {
        AnnotationProcessor.process(this);
	};
	
	public static ServiceRoleWrapper getInstance()  {
		if(instance == null)  {
			instance = new ServiceRoleWrapperImpl();
			instance.init();
		}
		return instance;
	}
	public static ServiceRoleWrapper getInstance(Properties props)  {
		socketProperties = props;
		if(instance == null)  {
			instance = new ServiceRoleWrapperImpl();
			instance.init();
		}
		return instance;
	}
	
	protected static ServiceRoleWrapperImpl getInstanceImpl(Properties props)  {
		socketProperties = props;
		if(instance == null)  {
			instance = new ServiceRoleWrapperImpl();
			instance.init();
		}
		return instance;
	}
	
	private ServiceRole serviceRole = null;
	
	/**
	 * is Service in passive mode
	 */
	private volatile boolean isPassive = true;
	@Override
	public boolean isPassive() {
		return isPassive;
	}

	@Override
	public void setPassive(boolean isPassive) {
		this.isPassive = isPassive;
	}
    private boolean isRunningActive() {
		return !isPassive;
	}

	// isPassive
	
    /**
     * boolean trigger indicates if ConnectionResponseReader has encountered too many exceptions
     * set by 
     */
    private volatile boolean bExTrigger = false;
	
	/**
	 * All ip addressess tied to this host
	 */
	private ArrayList<InetAddress> ipAll = new ArrayList<InetAddress>();
	private ArrayList<InetAddress> getIpAll() {
		return ipAll;
	}

	private void setIpAll(ArrayList<InetAddress> ipAll) {
		this.ipAll = ipAll;
	}
	// ipAll
	
	
	/**
	 * Port for accepting socket connections from clients
	 */
	private int port = 0;	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	// port
	
	/**
	 * did this service miss a ping from the QPC
	 */
	private boolean missedPing = false;
	protected boolean isMissedPing() {
		return missedPing;
	}

	protected void setMissedPing(boolean missedPing) {
		this.missedPing = missedPing;
	}
	// missedPing
	
	/**
	 * indicates why the service started or stopped
	 */
	private RoleChangeReason roleChangeReason;
	

	private RoleChangeReason getRoleChangeReason() {
		return roleChangeReason;
	}

	private void setRoleChangeReason(RoleChangeReason roleChangeReason) {
		this.roleChangeReason = roleChangeReason;
	}

	/**
	 * to make long-running threads go into inactive state; this does not apply
	 * to threads that are scheduled at fixed rate - they will simply check if
	 * state is active or passive.
	 * {@link com.honda.mfg.stamp.storage.service.clientmgr.StampServiceServerSocket}
	 */
	private volatile CountDownLatch latch = null;
	
	@Autowired
	private WatchdogAdapterInterface serviceWatchdogAdapter;

	protected WatchdogAdapterInterface getServiceWatchdogAdapter() {
		return serviceWatchdogAdapter;
	}

	protected void setServiceWatchdogAdapter(
			WatchdogAdapterInterface serviceWatchdogAdapter) {
		this.serviceWatchdogAdapter = serviceWatchdogAdapter;
	}

	@Override
	public CountDownLatch getLatch() {
		return latch;
	}

	private void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}
	
	private List<ServiceRoleObserver> serviceRoleObservers = new CopyOnWriteArrayList<ServiceRoleObserver>();
	@Override
	public void addObserver(ServiceRoleObserver newObserver)  {
		if(newObserver != null) {serviceRoleObservers.add(newObserver);}
	}
	@Override
	public boolean removeObserver(ServiceRoleObserver ob)  {
		return serviceRoleObservers.remove(ob);
	}
	private void notifyAllRoleChange()  {
		for (ServiceRoleObserver thisOb : serviceRoleObservers) {
			if(thisOb != null)  {thisOb.roleChange(isPassive());}
		}
		//2013-02-27:VB: think of a better way to do this
		if(serviceWatchdogAdapter != null)  {
			serviceWatchdogAdapter.roleChange(isPassive());
			if(serviceRole != null)  {
				serviceWatchdogAdapter.setOwnerId(serviceRole.getServiceName());
			}
		}
	}

	private static Properties socketProperties;

	public static Properties getSocketProperties() {
		return socketProperties;
	}

	@Override
	public void setSocketProperties(Properties socketProperties) {
		ServiceRoleWrapperImpl.socketProperties = socketProperties;
	}

	private static ServiceRoleWrapperImpl instance = null;
    private static final Logger LOG = LoggerFactory.getLogger(ServiceRoleWrapperImpl.class);
}
