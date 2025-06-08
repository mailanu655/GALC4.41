package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getService;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.IMsipOutboundDto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.service.property.PropertyService;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public abstract class BaseMsipOutboundHandler<P extends BaseMsipPropertyBean> implements IMsipOutboundHandler<IMsipOutboundDto> {
	
	public static final String JAPAN_VIN_LEFT_JUSTIFIED = "JAPAN_VIN_LEFT_JUSTIFIED";
	public static final String LAST_PROCESS_TIMESTAMP = ApplicationConstants.LAST_PROCESS_TIMESTAMP;	
	protected Logger logger;
	private P propertyBean = null;
	private String componentId = null;
	
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
	
	
	public Timestamp getDBTimestamp() {
		return getService(GenericDaoService.class).getCurrentDBTime();
	}
	
	public Timestamp getCurrentTime()  {
		return new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
	}
	
	public boolean isJapaneseVinLeftJustified() {
		return PropertyService.getPropertyBoolean(getComponentId(),JAPAN_VIN_LEFT_JUSTIFIED,true);
	}
	
	public Logger getLogger(){
		return Logger.getLogger(this.getClass().getName());
	}
	
	/**
	 * @param isDBTimestamp: true=use database timestamp, else use system TS
	 * update the timestamp for key=LAST_PROCESS_TIMESTAMP to the CURRENT Timestamp
	 */
	protected void updateLastProcessTimestamp(boolean isDBTimestamp) {
		
		Timestamp ts = getCurrentTime();
		
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
		Timestamp nowTs = getCurrentTime();
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
	
	protected ComponentStatus getComponentStatus(String key)  {
		ComponentStatusId id = new ComponentStatusId(getComponentId(), key);
		ComponentStatus cStat = getComponentStatusDao().findByKey(id);
		return cStat;
	}
	
	public ComponentStatusDao getComponentStatusDao() {
    	return ServiceFactory.getDao(ComponentStatusDao.class);
    }
	
	/**
	 * @return Timestamp
	 * get timestamp for key=LAST_PROCESS_TIMESTAMP
	 */
	protected Timestamp getLastProcessTimestamp() {
		return getComponentTimestamp(LAST_PROCESS_TIMESTAMP);
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
	
	@Override
	public <D extends IMsipOutboundDto> List<D> fetchDetails(
			Date startTimestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <D extends IMsipOutboundDto> List<D> fetchDetails(
			Date startTimestamp, int duration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <H extends IMsipOutboundDto> H fetchHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <F extends IMsipOutboundDto> List<F> fetchFooter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <D extends IMsipOutboundDto> List<D> fetchDetails() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Timestamp getTimestamp(Timestamp startTimestamp, int seconds) {
		Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTimestamp.getTime());
        cal.add(Calendar.SECOND, seconds);
        return new Timestamp(cal.getTime().getTime());
 	}
	

	public static String convertIfNull(Object sourceStr, Object toConvert){
	    return isNull(sourceStr) ? toConvert.toString() : sourceStr.toString();	
	}
	
	
	
	public static boolean isNull(Object obj){
        if(obj == null){	
            return true;	
        }else if(obj instanceof String){
            if(obj == null || ((String)obj).trim().length()==0  || ((String)obj).equals("null")){	
                return true;	
            }else{	
                return false;	
            }	
        }else{	
            return false;	
        }
	}
	
}
