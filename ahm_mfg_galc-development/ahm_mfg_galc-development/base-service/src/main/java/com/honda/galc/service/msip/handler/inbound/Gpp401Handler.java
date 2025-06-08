package com.honda.galc.service.msip.handler.inbound;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import com.honda.galc.dao.product.AnnualCalendarDao;
import com.honda.galc.entity.product.AnnualCalendar;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gpp401Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Anusha Gopalan
 * @date May 17, 2017
 */
public class Gpp401Handler extends PlanCodeBasedMsipHandler<BaseMsipPropertyBean, Gpp401Dto> {
	

	public Gpp401Handler() {}
	
	public boolean execute(List<Gpp401Dto> receivedRecords) {
		try {		
			processAnnualCalendar(receivedRecords);
		} catch(Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void processAnnualCalendar(List<Gpp401Dto> gpp401List) throws ParseException {
		getLogger().info("start to process AnnualCalendar");
			for(Gpp401Dto gpp401dto : gpp401List) {
				ServiceFactory.getDao(AnnualCalendarDao.class).save(deriveAnnualCalendar(gpp401dto));
			}
	}
	
	public AnnualCalendar deriveAnnualCalendar(Gpp401Dto gpp401dto) {
		Date sqlDate;
		AnnualCalendar annualCalendar = new AnnualCalendar();
		try {
			sqlDate = new java.sql.Date(MsipUtil.sdf1.parse(gpp401dto.getProductionDate()).getTime());
			annualCalendar.setDayOfWeek(gpp401dto.getDayOfWeek());
			annualCalendar.setProductionDate(sqlDate);
			annualCalendar.setWeekNo(gpp401dto.getWeekNo());
		} catch (ParseException e) {
			e.printStackTrace();
			getLogger().error("Unable to parse GPP401 request");
		} 
		return annualCalendar;
	}

}
