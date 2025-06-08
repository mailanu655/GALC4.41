package com.honda.galc.client.utils;


import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Date;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DunnageUtils</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @author 
 */
public class DunnageUtils {

	public static final String DATE_PATTERN = "yyMMdd";
	public static final String SEQUENCE_PATTERN = "000";
	public static final String DUNNAGE_NUMBER_PATTERN = String.format("{0}{1,date,%s}{2,number,%s}", DATE_PATTERN, SEQUENCE_PATTERN);

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
	public static final MessageFormat FORMAT = new MessageFormat(DUNNAGE_NUMBER_PATTERN);


	public static String format(String machineId, Date productionDate, int sequence) {
		return  FORMAT.format(new Object[] { machineId, productionDate, sequence });
	}
	public static Integer parseSequence(String dunnage) {
		String str = getSequenceToken(dunnage);
		return (str==null ? 0 : Integer.valueOf(str));

	}

	protected static String getSequenceToken(String dunnage) {
		if(null!=dunnage){
			dunnage = dunnage.trim();
			if (dunnage.length() < SEQUENCE_PATTERN.length()) {
				return null;
			}
		}
		return StringUtils.right(dunnage, SEQUENCE_PATTERN.length());
	}
	
	/**
	 * This method is used to Print the dunnage Data
	 * @param dunnageNumber
	 * @param productTypeName
	 * @param products
	 * @param processPointId
	 * @param dunnagePrinters
	 */
	public static void print(String dunnageNumber, String productTypeName, List<Object> products, String processPointId, List<BroadcastDestination> dunnagePrinters){
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.DUNNAGE_NUMBER, dunnageNumber);
		dc.put(DataContainerTag.PRODUCT_TYPE_NAME, productTypeName);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, "*");
		dc.put("DATA_SOURCE", products);
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		for (BroadcastDestination bd : dunnagePrinters) {
			getService(BroadcastService.class).broadcast(bd.getId().getProcessPointId(), bd.getId().getSequenceNumber(), dc);
		}
	}
	
	/**
	 * This method is used to get the list of Broadcast Destinations
	 * @param dunnagePrinter
	 * @param dunnageForm
	 * @param applicationId
	 * @return
	 */
	
	public static List<BroadcastDestination> getPrinters(String dunnagePrinter, String dunnageForm, String applicationId) {
		List<BroadcastDestination> printers = new ArrayList<BroadcastDestination>();
		List<BroadcastDestination> broadcastDestinations = new ArrayList<BroadcastDestination>();
		if(!StringUtils.isBlank(dunnagePrinter) || !StringUtils.isBlank(dunnageForm)){
			broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(applicationId);
			if(null!= broadcastDestinations || !broadcastDestinations.isEmpty()){
				for (BroadcastDestination bd : broadcastDestinations) {
					if (bd.getDestinationId().equals(dunnagePrinter) && dunnageForm.equals(StringUtils.trim(bd.getRequestId()))) {
						printers.add(bd);
					}
				}
			}
		}
		return printers;
	}
}
