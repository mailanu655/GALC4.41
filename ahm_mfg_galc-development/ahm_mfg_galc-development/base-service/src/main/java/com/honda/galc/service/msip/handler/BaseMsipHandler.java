package com.honda.galc.service.msip.handler;

import static com.honda.galc.service.ServiceFactory.getService;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameMTOCMasterSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public class BaseMsipHandler <P extends BaseMsipPropertyBean> {
	
	private P propertyBean = null;
	private String componentId = null;
	public static final String LAST_PROCESS_TIMESTAMP = ApplicationConstants.LAST_PROCESS_TIMESTAMP;
	
	@SuppressWarnings("unchecked")
	public P getPropertyBean() {
		if (propertyBean == null) {
			ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		    Class<P> clazz = (Class<P>) genericSuperclass.getActualTypeArguments()[0];
			propertyBean = PropertyService.getPropertyBean(clazz);
		}
		return propertyBean;
	}
    
	@SuppressWarnings("unchecked")
	public String getComponentId() {
		if (componentId == null) {
			ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		    Class<P> clazz = (Class<P>) genericSuperclass.getActualTypeArguments()[0];
			componentId = PropertyService.getComponentByBeanName(clazz.getSimpleName());
		}
		return componentId;
	}
	
	public ComponentPropertyDao getComponentPropertyDao() {
    	return ServiceFactory.getDao(ComponentPropertyDao.class);
    }
	
	public ComponentStatusDao getComponentStatusDao() {
    	return ServiceFactory.getDao(ComponentStatusDao.class);
    }
	
    public FrameMTOCMasterSpecDao getFrameMtocMasterSpecDao() {
    	return ServiceFactory.getDao(FrameMTOCMasterSpecDao.class);
    }
    
    public ComponentStatusDao getCompStatusDao() {
    	return ServiceFactory.getDao(ComponentStatusDao.class);
    }
    
    public FrameDao getFrameDao() {
    	return ServiceFactory.getDao(FrameDao.class);
    }
    
    public FrameSpecDao getFrameSpecDao() {
    	return ServiceFactory.getDao(FrameSpecDao.class);
    }
    
    public PreProductionLotDao getPreProductionLotDao() {
    	return ServiceFactory.getDao(PreProductionLotDao.class);
    }
    
	public Logger getLogger(){
		return Logger.getLogger(this.getClass().getName());
	}
	
	public Timestamp getPropertyTimestamp(String propertyName) {
		Timestamp ts = null;
		String propValue = "";
			try {
				propValue = PropertyService.getProperty(getComponentId(), propertyName, "");
				if(!StringUtils.isEmpty(propValue))  {
					ts = Timestamp.valueOf(propValue.trim());
				}
			} catch (Exception e) {
				getLogger().error(e, "Error parsing timestamp" + propValue);
			}
		return ts;
	}
	
	/**
	 * @param compTs: ComponentStatus
	 * @return Timestamp
	 * given component status, convert the status value to a Timestamp
	 */
	protected Timestamp getComponentTimestamp(ComponentStatus compTs) {
		Timestamp ts = null;
		if (compTs != null && !StringUtils.isBlank(compTs.getStatusValue())) {
			try {
				String strTS = StringUtils.trim(compTs.getStatusValue());
				ts = Timestamp.valueOf(strTS);
			} catch (Exception e) {
				getLogger().error(e, "Error parsing timestamp" + compTs.getStatusValue());
			}
		}
		return ts;
	}

	/**
	 * @param key:String
	 * @return Timestamp
	 * find timestamp value given the status key, for the current component id
	 */
	protected Timestamp getComponentTimestamp(String key) {
		Timestamp ts = null;
		if(!StringUtils.isEmpty(key))  {
			ComponentStatus compTs = getComponentStatus(LAST_PROCESS_TIMESTAMP);
			if(compTs != null)  {
				ts = getComponentTimestamp(compTs);
			}
		}
		return ts;
	}
	
	protected ComponentStatus getComponentStatus(String key)  {
		ComponentStatusId id = new ComponentStatusId(getComponentId(), key);
		ComponentStatus cStat = getComponentStatusDao().findByKey(id);
		return cStat;
	}

	/**
	 * @return Timestamp
	 * get timestamp for key=LAST_PROCESS_TIMESTAMP
	 */
	protected Timestamp getLastProcessTimestamp() {
		return getComponentTimestamp(LAST_PROCESS_TIMESTAMP);
	}

	/**
	 * @param currentTimestamp
	 * update the timestamp for key=LAST_PROCESS_TIMESTAMP to the given Timestamp
	 */
	protected void updateLastProcessTimestamp(Timestamp currentTimestamp) {
		
		if(currentTimestamp == null)  return;
		
		ComponentStatus cStat = getComponentStatus(LAST_PROCESS_TIMESTAMP);
		if(cStat == null)  {
			cStat = new ComponentStatus(getComponentId(), LAST_PROCESS_TIMESTAMP, currentTimestamp.toString());			
		} else {
			cStat.setStatusValue(currentTimestamp.toString());
		}
		getComponentStatusDao().save(cStat);
	}

	/**
	 * @param String
	 * update the component value for key
	 */
	protected void updateComponentStatus(String key, String value) {
		
		String val = "";
		if(StringUtils.isBlank(key))  return;
		if(!StringUtils.isBlank(value))  {
			val = value;
		}
		ComponentStatus cStat = getComponentStatus(key);
		if(cStat == null)  {
			cStat = new ComponentStatus(getComponentId(), key, val);			
		} else {
			cStat.setStatusValue(val);
		}
		getComponentStatusDao().save(cStat);
	}

	/**
	 * @param isDBTimestamp: true=use database timestamp, else use system TS
	 * update the timestamp for key=LAST_PROCESS_TIMESTAMP to the CURRENT Timestamp
	 */
	protected void updateLastProcessTimestamp(boolean isDBTimestamp) {
		
		Timestamp ts = getCurrentTime(isDBTimestamp);
		
		updateLastProcessTimestamp(ts);
	}

	/**
	 * @deprecated
	 * @param: none
	 * update with current system timestamp
	 */
	protected void updateLastProcessTimestamp() {
		
		updateLastProcessTimestamp(false);
	}

	
	protected Timestamp getStartOfToday() {
		Timestamp startTimestamp = null;
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.HOUR, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.SECOND, 0); 
		startTimestamp = new Timestamp(cal.getTimeInMillis());

		return startTimestamp;
	}

	public Timestamp getStartTime()  {
		Timestamp startTs = null;
		//get the configured start timestamp, if not set/null then get the component last run timestamp
		startTs = getPropertyTimestamp("START_TIMESTAMP");
		if(startTs == null)  {
			startTs = getLastProcessTimestamp(); //component status
		}
		//if startTs still cannot be found, set it to start of current day/midnight
		if(startTs == null)  {
			startTs = getStartOfToday();
		}
		return startTs;
	}
	
	
	/**
	 * @return Timestamp
	 */
	public Timestamp getEndTimestamp()  {
		Timestamp endTs = getPropertyTimestamp("END_TIMESTAMP");
		return endTs;
	}
	
	/**
	 * @param isDBTimestamp: yes=default to database current TS, else default to system time
	 * @return
	 */
	public Timestamp getEndTime(boolean isDBTimestamp)  {
		return getEndTime(null, isDBTimestamp);
	}
	
	/**
	 * @deprecated
	 * @param isDBTimestamp: true=default to database current TS, else default to system time
	 * @return: configured end_time or current time or start_time provided if start > end
	 */
	public Timestamp getEndTime(Timestamp startTs)  {
		return getEndTime(startTs, false);
	}
	
	/**
	 * @param startTs: start timestamp to validate, if startTs provided is > configured endTs.
	 * then, set endTs = startTs
	 * @return
	 */
	public Timestamp getEndTime(Timestamp startTs, boolean isDBTimestamp)  {
		Timestamp nowTs = getCurrentTime(isDBTimestamp);
		Timestamp endTs = null;
		//get the configured start timestamp, if not-set/null then default to now
		endTs = getPropertyTimestamp("END_TIMESTAMP");
		if(endTs == null)  {
			endTs = nowTs;
		}
		//if start time was provided and if the start > end, then set end = start
		if(startTs != null && startTs.after(endTs))  {
			endTs = startTs;
		}
		return endTs;
	}
	
	public Timestamp getCurrentTime(boolean isDBTimestamp)  {
		GenericDaoService genericDao = getService(GenericDaoService.class);
		Calendar now = GregorianCalendar.getInstance();
		Timestamp nowTs = null;
		if(isDBTimestamp)  {
			nowTs = genericDao.getCurrentDBTime();
		}
		else  {
			nowTs = new Timestamp(now.getTimeInMillis());
		}
		
		return nowTs;
	}
}
