package com.honda.galc.system.oif.svc.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.OifServiceException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.oif.RunHistoryDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.oif.RunHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;


public class OifServiceFactory {
	protected static final String MSG_OIF_CREATE_FAILURE = "Cannot create OIF service ";
	public static Logger logger = Logger.getLogger("OIF_EVENTS");

	public static final String OIF_SERVICES_COMPONENT_ID = "OIF_SERVICES";

	private static final String OIF_SERVICES_DEFAULT_PACKAGE = "DEF_PACKAGE";

	/**
	 * Constructor signature for printer senders
	 */
	private static final Class[] CONSTRUCTOR_ARG_TYPE = new Class[] {String.class};
	
	/**
	 * Class name cache
	 */
	private static final String LOG_CLASS_NAME = OifServiceFactory.class.getName();
	

	/**
	 * Factory instance
	 */
	private static OifServiceFactory instance = null;
	
	/**
	 * Return OIF service for given:<ul>
	 * <li>OIF service name</li>
	 * </ul>
	 * @param oifService - given 
	 * @param userName - user under which it will run
	 * @return
	 * @throws OifServiceException
	 */ 
	public static IOifRunHistory createEventTask(String oifService, String userName){
		
		final String logMethod = "createOifService()";
		
		IOifRunHistory oifInstance = null;
		
		Class<? extends IOifRunHistory> executableClass = getInstance().getOifServiceClass(oifService);
		
		Constructor<? extends IOifRunHistory> constructor;
		try {
			constructor = executableClass.getConstructor(CONSTRUCTOR_ARG_TYPE);
		} catch (Exception e) {
			String message = "Cannot find Constructor(String) for OIF service: " + oifService;
			
			logError(MSG_OIF_CREATE_FAILURE, logMethod, message, e);
			throw new OifServiceException(MSG_OIF_CREATE_FAILURE, e);			
		} 
		
		try {
			oifInstance = (IOifRunHistory) constructor.newInstance(new Object[] { oifService });
		} catch (InvocationTargetException e) {
			String message = "Cannot instantiate class of OIF Service: " + oifService;
			
			logError(MSG_OIF_CREATE_FAILURE, logMethod, message, e.getTargetException());
			throw new OifServiceException(MSG_OIF_CREATE_FAILURE, e.getTargetException());			
        } catch (Exception e) {
            String message = "Cannot instantiate class of OIF Service: " + oifService;
            
            logError(MSG_OIF_CREATE_FAILURE, logMethod, message, e);
            throw new OifServiceException(MSG_OIF_CREATE_FAILURE, e);          
        }
        
        if(userName != null && userName.length() > 0) {
        	// Find "setUserName(String)" method and execute it
        	try {
        		
				Method setUserNameMethod = executableClass.getMethod("setUserName", CONSTRUCTOR_ARG_TYPE);
				setUserNameMethod.invoke(oifInstance, new Object[] { userName });
				
			} catch (NoSuchMethodException e) {
				// It is a normal execution - we do nothing if there is no such method
				
			} catch (Exception e) {
	            String message = "Cannot set user name of OIF Service: " + oifService;
	            
	            logError(MSG_OIF_CREATE_FAILURE, logMethod, message, e);
	            throw new OifServiceException(MSG_OIF_CREATE_FAILURE, e);          
			} 
        }

        return oifInstance;
	}
	
	public static void executeTask(String taskName, String userName, Object[] args)  {
		
		IOifRunHistory executable;
		
		executable = createEventTask(taskName, userName);
	
		logger.info("Start OIF task - " + taskName);
	
		//set start timestamp
		Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());
		
		executable.execute(args);
		
		
		boolean defaultLevelSaveRunHistory=PropertyService.getPropertyBoolean(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SAVE_RUN_HISTORY, false);
		boolean taskLevelSaveRunHistory = PropertyService.getPropertyBoolean(taskName, OIFConstants.SAVE_RUN_HISTORY, true);
		if(defaultLevelSaveRunHistory && taskLevelSaveRunHistory)
		{
			
			OifErrorsCollector errorsCollector = executable.getOifErrorsCollector();
			if(errorsCollector != null && errorsCollector.getRunHistory() != null)
			{
				RunHistory runHistory = errorsCollector.getRunHistory();
				runHistory.setJobStartTimestamp(startTimestamp);
				if(StringUtils.isNotEmpty(userName))	runHistory.setUserName(userName);
				else runHistory.setUserName(OIFConstants.SCHEDULER_USERNAME);
				runHistory.setJobName(taskName);
				runHistory.setInterfaceId(PropertyService.getProperty(taskName, OIFConstants.INTERFACE_ID,null));
				runHistory.setJobEndTimestamp(new Timestamp(System.currentTimeMillis()));
				//set status if not exists already
				if(runHistory.getStatus() == null) {
					if(errorsCollector.isErrorExists())	runHistory.setStatus(OifRunStatus.FAILURE);
					else runHistory.setStatus(OifRunStatus.SUCCESS);
				}
				RunHistoryDao runHistoryDao = ServiceFactory.getDao(RunHistoryDao.class);			
				runHistoryDao.save(runHistory);
			} else {
				logger.info(" No History to save");
			}
			
		}
		logger.info("End OIF task  - " + taskName);
	
	}
	
	/**
	
	/**
	 * Returns printer sender class based on OIF service
	 * 
	 * @param oifService
	 * @return
	 * @throws OifServiceException 
	 */
	private Class<? extends IOifRunHistory> getOifServiceClass(String oifService) throws OifServiceException {
		final String logMethod = "getOifServiceClass()";
		
		PropertyService.refreshComponentProperties(OIF_SERVICES_COMPONENT_ID);
		String packageName = PropertyService.getProperty(OIF_SERVICES_COMPONENT_ID,OIF_SERVICES_DEFAULT_PACKAGE);
		
		// Use current package if default package property is not defined
		if(packageName == null || packageName.length() <= 0) {
			packageName = this.getClass().getPackage().getName();
		}
		
		String oifClassName = PropertyService.getProperty(OIF_SERVICES_COMPONENT_ID, oifService);
		
		if(oifClassName == null) {
			String message = "Cannot find class name property for OIF service: " + oifService;
			
			logError(MSG_OIF_CREATE_FAILURE, logMethod, message, null);
			throw new OifServiceException(MSG_OIF_CREATE_FAILURE, null);			
		}
		
		String fullOifClassName = oifClassName;
		
		// Apply default package if short class name is used
		if (fullOifClassName.indexOf(".") <= 0) {
			fullOifClassName = new StringBuilder(packageName).append(".").append(oifClassName)
					.toString();
		}		
		Class<? extends IOifRunHistory> oifClass;
		
		try {
			
			oifClass = getClassForName(fullOifClassName);
			
		} catch (ClassNotFoundException e) {
			final String message = "Cannot find class " + fullOifClassName + " for OIF Service: " + oifService;
			
			logError(MSG_OIF_CREATE_FAILURE, logMethod, message, null);
			throw new OifServiceException(MSG_OIF_CREATE_FAILURE, null);			
		}
		
		return oifClass;
	}

	/**
	 * Isolated class cast warnings
	 * 
	 * @param fulOifClassName
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends IOifRunHistory> getClassForName(String fulOifClassName) throws ClassNotFoundException {
		return (Class<? extends IOifRunHistory>) Class.forName(fulOifClassName);
	}

	/**
	 * Singleton with a private default constructor
	 * @throws OifServiceException 
	 */
	private OifServiceFactory() {

	}

	/**
	 * Helper method for logging events
	 * @param messageID - message ID
	 * @param methodName - method name
	 * @param userMessage - user message
	 * @param exception - reported exception
	 */
	private static void logError(String messageID, String methodName, String userMessage, Throwable exception)
	{
		if(exception != null)
			logger.error(exception,messageID,methodName,userMessage);
		else 	
		    logger.error(messageID,methodName,userMessage);
		
	}

	/**
	 * Returns factory instance
	 * 
	 * @return factory instance
	 * @throws OifServiceException 
	 */
	private static OifServiceFactory getInstance() throws OifServiceException {
		if(instance == null) {
			initInstance();
		}
		return instance;
	}

	/**
	 * Synchronized helper method to optimize write-once/read-many access
	 * @throws OifServiceException 
	 */
	private static synchronized void initInstance() throws OifServiceException {
		if(instance == null) {
			instance = new OifServiceFactory();
		}
	}
	

}
