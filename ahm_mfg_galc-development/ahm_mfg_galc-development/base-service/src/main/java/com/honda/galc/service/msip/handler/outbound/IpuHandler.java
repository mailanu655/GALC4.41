package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.product.IpuDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.IpuDto;
import com.honda.galc.service.msip.property.outbound.IpuPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class IpuHandler extends BaseMsipOutboundHandler<IpuPropertyBean> {

	private IpuDao ipuDao = null;
	
	String exportFileNameQA = "";
	String exportFileNameLET = "";
	String exportLocalPath = "";
	String exportRemotePath = "";
	String fQALocal = "";
	String fQARemote = "";
	String fLETLocal = "";
	String fLETRemote = "";
	Timestamp startTs = null, endTs = null;
	protected ComponentPropertyDao propertyDao;	
    
	public static final String LAST_PROCESS_TIMESTAMP = "LAST_PROCESS_TIMESTAMP";
	
	
   /**
	 * Initialize runtime parameters.
	 * @return true if the initialization is successful. Otherwise, false
	 */
	public void initialize(Date startTimestamp, int duration) {		
		this.ipuDao = ServiceFactory.getDao(IpuDao.class);
		
		this.propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		
		getLogger().info("Loading Properties for IPU QA...");
		startTs = new Timestamp(startTimestamp.getTime());  
		endTs = getTimestamp(startTs, duration);
		Calendar now = GregorianCalendar.getInstance();
		if(endTs == null)  {
			endTs = new Timestamp(now.getTimeInMillis());
		}
		if(startTs == null)  {
			Calendar aWeekAgo = GregorianCalendar.getInstance();
			aWeekAgo.set(Calendar.DATE, now.get(Calendar.DATE) - 7);
			startTs = new Timestamp(aWeekAgo.getTimeInMillis());
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<IpuDto> fetchDetails(Date startTimestamp, int duration)  {
		getLogger().info("Inside  List<IpuDto> ");
		getLogger().info("Component Id ::  " + getComponentId());
		getLogger().info("IPU_PART_NAME ::  " + getPropertyBean().getIpuPartName());
		getLogger().info("BATTERY_PART_NAME::  " + getPropertyBean().getBatteryPartName());
		List<IpuDto> dtoList = new ArrayList<IpuDto>();
		try {
			initialize(startTimestamp, duration);
			List<Object[]> details =  ipuDao.getIpuQaData(
					startTs, endTs, getPropertyBean().getIpuPartName(), getPropertyBean().getBatteryPartName());
			getLogger().info("IPU details size" + details.size());
			if(getPropertyBean().isSendLet())  {  //if LET results are also to be sent
				List<Object[]> letDetails =  ipuDao.getIpuLetData(startTs, endTs, getPropertyBean().getProcessPoint(), 
						getPropertyBean().getIpuPartName(), getPropertyBean().getBatteryPartName());
				getLogger().info("IPU LET details size" + details.size());
				return convertToDto(letDetails);
			}
			updateLastProcessTimestamp(endTs);
			return convertToDto(details);
		} catch (TaskException e) {
			getLogger().error(e.getMessage());
			dtoList.clear();
			IpuDto dto = new IpuDto();
			e.printStackTrace();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		} catch (Exception e) {
			getLogger().error(e.getMessage());
			e.printStackTrace();
			dtoList.clear();
			IpuDto dto = new IpuDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		} 
	}
	
	private List<IpuDto> convertToDto(List<Object[]> results) {
		List<IpuDto> dtoList = new ArrayList<IpuDto>();
		for(Object[] objects : results) {
			IpuDto dto = new IpuDto();
			dto.setProductId(convertIfNull(objects[0],""));
			dto.setIpuSn(convertIfNull(objects[1],""));
			dto.setBattery(convertIfNull(objects[2],""));
			dto.setStartTimestamp(convertIfNull(objects[3],""));
			dto.setStartUtc(convertIfNull(objects[4],""));
			dto.setProcessEndTimestamp(convertIfNull(objects[5],""));
			dto.setTotalStatus(convertIfNull(objects[6],""));
			dto.setCellVoltageStatus(convertIfNull(objects[7],""));
			dto.setInspectionParamName(convertIfNull(objects[8],""));
			dto.setInspectionParamValue(convertIfNull(objects[9],""));
			dto.setVbc1(convertIfNull(objects[10],""));
			dto.setVbc10(convertIfNull(objects[11],""));
			dto.setVbc11(convertIfNull(objects[12],""));
			dto.setVbc12(convertIfNull(objects[13],""));
			dto.setVbc13(convertIfNull(objects[14],""));
			dto.setVbc14(convertIfNull(objects[15],""));
			dto.setVbc15(convertIfNull(objects[16],""));
			dto.setVbc16(convertIfNull(objects[17],""));
			dto.setVbc17(convertIfNull(objects[18],""));
			dto.setVbc18(convertIfNull(objects[19],""));
			dto.setVbc19(convertIfNull(objects[20],""));
			dto.setVbc2(convertIfNull(objects[21],""));
			dto.setVbc20(convertIfNull(objects[22],""));
			dto.setVbc21(convertIfNull(objects[23],""));
			dto.setVbc22(convertIfNull(objects[24],""));
			dto.setVbc23(convertIfNull(objects[25],""));
			dto.setVbc24(convertIfNull(objects[26],""));
			dto.setVbc25(convertIfNull(objects[27],""));
			dto.setVbc26(convertIfNull(objects[28],""));
			dto.setVbc27(convertIfNull(objects[29],""));
			dto.setVbc28(convertIfNull(objects[30],""));
			dto.setVbc29(convertIfNull(objects[31],""));
			dto.setVbc3(convertIfNull(objects[32],""));
			dto.setVbc30(convertIfNull(objects[33],""));
			dto.setVbc31(convertIfNull(objects[34],""));
			dto.setVbc32(convertIfNull(objects[35],""));
			dto.setVbc33(convertIfNull(objects[36],""));
			dto.setVbc34(convertIfNull(objects[37],""));
			dto.setVbc35(convertIfNull(objects[38],""));
			dto.setVbc36(convertIfNull(objects[39],""));
			dto.setVbc37(convertIfNull(objects[40],""));
			dto.setVbc38(convertIfNull(objects[41],""));
			dto.setVbc39(convertIfNull(objects[42],""));
			dto.setVbc4(convertIfNull(objects[43],""));
			dto.setVbc40(convertIfNull(objects[44],""));
			dto.setVbc41(convertIfNull(objects[45],""));
			dto.setVbc42(convertIfNull(objects[46],""));
			dto.setVbc43(convertIfNull(objects[47],""));
			dto.setVbc44(convertIfNull(objects[48],""));
			dto.setVbc45(convertIfNull(objects[49],""));
			dto.setVbc46(convertIfNull(objects[50],""));
			dto.setVbc47(convertIfNull(objects[51],""));
			dto.setVbc48(convertIfNull(objects[52],""));
			dto.setVbc49(convertIfNull(objects[53],""));
			dto.setVbc5(convertIfNull(objects[54],""));
			dto.setVbc50(convertIfNull(objects[55],""));
			dto.setVbc51(convertIfNull(objects[56],""));
			dto.setVbc52(convertIfNull(objects[57],""));
			dto.setVbc53(convertIfNull(objects[58],""));
			dto.setVbc54(convertIfNull(objects[59],""));
			dto.setVbc55(convertIfNull(objects[60],""));
			dto.setVbc56(convertIfNull(objects[61],""));
			dto.setVbc57(convertIfNull(objects[62],""));
			dto.setVbc58(convertIfNull(objects[63],""));
			dto.setVbc59(convertIfNull(objects[64],""));
			dto.setVbc6(convertIfNull(objects[65],""));
			dto.setVbc60(convertIfNull(objects[66],""));
			dto.setVbc61(convertIfNull(objects[67],""));
			dto.setVbc62(convertIfNull(objects[68],""));
			dto.setVbc63(convertIfNull(objects[69],""));
			dto.setVbc64(convertIfNull(objects[70],""));
			dto.setVbc65(convertIfNull(objects[71],""));
			dto.setVbc66(convertIfNull(objects[72],""));
			dto.setVbc67(convertIfNull(objects[73],""));
			dto.setVbc68(convertIfNull(objects[74],""));
			dto.setVbc69(convertIfNull(objects[75],""));
			dto.setVbc7(convertIfNull(objects[76],""));
			dto.setVbc70(convertIfNull(objects[77],""));
			dto.setVbc71(convertIfNull(objects[78],""));
			dto.setVbc72(convertIfNull(objects[79],""));
			dto.setVbc8(convertIfNull(objects[80],""));
			dto.setVbc9(convertIfNull(objects[81],""));//81
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	protected Timestamp getTimestamp(String tsVal) {
		Timestamp ts = null;
		if (!StringUtils.isBlank(tsVal)) {
			ts = Timestamp.valueOf(StringUtils.trim(tsVal));
		}
		return ts;
	}

	public void updateLastProcessTimestamp(Timestamp currentTimestamp) {
		ComponentPropertyId id = new ComponentPropertyId(getComponentId(), LAST_PROCESS_TIMESTAMP);
		ComponentProperty cProp = propertyDao.findByKey(id);
		if(cProp == null)  {
			cProp = new ComponentProperty(getComponentId(), LAST_PROCESS_TIMESTAMP, currentTimestamp.toString());			
		} else {
			cProp.setPropertyValue(currentTimestamp.toString());
		}
		propertyDao.save(cProp);
	}

	/**
	 * @return the propertyDao
	 */
	public ComponentPropertyDao getPropertyDao() {
		return propertyDao;
	}

	/**
	 * @param propertyDao
	 *            the propertyDao to set
	 */
	public void setPropertyDao(ComponentPropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}
}
