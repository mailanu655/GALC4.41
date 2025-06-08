package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.dao.oif.MaterialServiceDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.oif.MaterialService;
import com.honda.galc.oif.dto.MaterialServiceDTO;
import com.honda.galc.property.MaterialServicePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MaterialServiceTask</code> is ... .
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
 * @ver 0.1
 * @author Karol Wozniak
 * @created Mar 4, 2015
 */
public class MaterialServiceTask extends OifTask<Object> implements IEventTaskExecutable {

	private MaterialServicePropertyBean property;
	private DateFormat dateFormat;
	private DateFormat timeFormat;
	private DateFormat dateTimeFormat;
	private MaterialServiceDao dao;
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, false);

	public MaterialServiceTask(String name) {
		super(name);
		this.property = PropertyService.getPropertyBean(MaterialServicePropertyBean.class, getComponentId());
		this.dao = ServiceFactory.getDao(MaterialServiceDao.class);
		this.dateFormat = new SimpleDateFormat(getProperty().getDateFormatPattern());
		this.timeFormat = new SimpleDateFormat(getProperty().getTimeFormatPattern());
		this.dateTimeFormat = new SimpleDateFormat(getProperty().getDateTimeFormatPattern());
	}

	public void execute(Object[] args) {

		Timestamp deleteBeforeTimestamp = getDeleteBeforeTimestamp();
		Timestamp processFromTimestamp = getProcessFromTimestamp();
		String[] processPointIds = getProperty().getProcessPointIds();
		String[] processLocations = getProperty().getProcessLocations();

		deleteProcessed(deleteBeforeTimestamp);
		loadToProcess(processPointIds, processFromTimestamp, processLocations);
		process(processFromTimestamp);
	}

	// === workflow === //
	protected void deleteProcessed(Timestamp deleteBeforeTimestamp) {
		try {
			int deletedCount = getDao().deleteProcessed(deleteBeforeTimestamp);
			String msg = String.format("Deleted %s records (Material Service), completed before %s.", deletedCount, deleteBeforeTimestamp);
			logger.info(msg);
		} catch (Exception e) {
			logger.error(e, "Failed to delete completed lots");
			errorsCollector.error("Failed to delete completed lots");
		}
	}

	protected void loadToProcess(String[] processPointIds, Timestamp processFromTimestamp, String[] processLocations) {

		if (processPointIds == null || processPointIds.length == 0) {
			logger.warn("No process points defined for Material Service.");
			return;
		}
		for (String ppId : processPointIds) {
			if (StringUtils.isBlank(ppId)) {
				continue;
			}
			try {
				int insertedCount = getDao().insertToProcess(ppId, processFromTimestamp, processLocations);
				String msg = String.format("Insert MS Progress, recordCount:%s, processPointId %s, startTimestamp:%s.", insertedCount, ppId, processFromTimestamp);
				logger.info(msg);
			} catch (Exception e) {
				logger.error(e, String.format("Failed to insert Material Service progress for processPointId:%s", ppId));
				errorsCollector.error(String.format("Failed to insert Material Service progress for processPointId:%s", ppId));
			}
		}
	}

	protected void process(Timestamp processFromTimestamp) {
		List<MaterialService> list = getDao().findAllToProcess(processFromTimestamp);
		if (list == null || list.isEmpty()) {
			logger.info("Material Service - nothing to processs.");
			return;
		}
		Map<String,String> processPointMap = PropertyService.getPropertyMap(componentId, OIFConstants.PROCESS_POINT_MAP);
		for (MaterialService ms : list) {
			if (ms == null) {
				continue;
			}
			process(ms, processPointMap);
		}

	}

	protected void process(MaterialService ms, Map<String,String> processPointMap) {
		logger.info(String.format("Start Processing productId:%s, processPointId:%s.", ms.getId().getProductId(), ms.getId().getProcessPointId()));
		MaterialServiceDTO dto = assemble(ms, processPointMap);
		List<MaterialServiceDTO> data = new ArrayList<MaterialServiceDTO>();
		data.add(dto);
		try {
			sendData(MaterialServiceDTO.class, data, null, null);
			String tm = getDateTimeFormat().format(new Timestamp(System.currentTimeMillis()));
			ms.setSentFlag('Y');
			ms.setCurrentTimestamp(tm);
			MaterialService saved = getDao().save(ms);
			String msg = String.format("Processed productId: %s, processPointId: %s, sentFlag:%s.", saved.getId().getProductId(), saved.getId().getProcessPointId(), saved.getSentFlag());
			logger.info(msg);
		} catch (Exception e) {
			logger.error(e, "Exception occured when processing productId:", dto.getProductId(), ", processPointId:", dto.getProcessPointId());
		}
	}

	// === utility === //
	protected MaterialServiceDTO assemble(MaterialService ms, Map<String,String> processPointMap) {
		MaterialServiceDTO dto = new MaterialServiceDTO();
		dto.setProductId(ProductNumberDef.justifyJapaneseVIN(ms.getId().getProductId(), JapanVINLeftJustified.booleanValue()));
		
		String pp = getTrimmedProcessPoint(ms.getId().getProcessPointId(), processPointMap);
		dto.setProcessPointId(pp);

		dto.setPlanCode(ms.getPlanCode());
		dto.setLineNo(ms.getLineNo());

		dto.setProductionDate(formatDate(ms.getActualTimestamp()));
		dto.setActualTimestamp(formatTime(ms.getActualTimestamp()));
		dto.setProductSpecCode(ms.getProductSpecCode());

		String lotSize = ms.getLotSize() == null ? "00000" : StringUtil.padLeft(ms.getLotSize().toString(), 5, '0');
		dto.setLotSize(lotSize);
		String onSeqNo = ms.getOnSeqNo() == null ? "000" : StringUtil.padLeft(ms.getOnSeqNo().toString(), 3, '0');
		dto.setOnSeqNo(onSeqNo);

		dto.setProductionLot(ms.getProductionLot());
		dto.setKdLotNumber(ms.getKdLotNumber());
		dto.setPlanOffDate(formatDate(ms.getProductionDate()));
		dto.setCurrentTimestamp(getDateTimeFormat().format(new Timestamp(System.currentTimeMillis())));
		return dto;
	}

	protected String getTrimmedProcessPoint(String realPP, Map<String,String> processPointMap)  {
		String justify = getProperty("PP_JUSTIFY", "L_TRIM");
		String overridePP = getProperty("PP_OVERRIDE", null);
		String pp = "";
		if(overridePP != null && !"".equals(overridePP.trim()))  {
			pp = overridePP;
		}
		else if(realPP != null)  {
			pp = realPP.trim();
			String mapPP = processPointMap.get(pp);
			if (mapPP != null)
				pp = mapPP;
		}
		OutputFormatHelper<MaterialServiceDTO> ofHelper = getOutputFormatHelper(MaterialServiceDTO.class);
		int defSize = ofHelper.getDataLength("PROCESS_POINT_ID");
		int ppLen = pp.length();
		String ppTrim = "";
		if(ppLen <= defSize)  { //process point length is < length specified in format
			ppTrim = pp;
		}
		//if L_TRIM, trim the left side and use the right most <defSize> characters
		else if("L_TRIM".equalsIgnoreCase(justify))  {
			ppTrim = pp.substring(ppLen - defSize);
		}
		//if R_TRIM, trim the right side and use the left most <defSize> characters
		else if("R_TRIM".equalsIgnoreCase(justify))  {
			ppTrim = pp.substring(0, defSize);
		}
		return ppTrim;
	}
	
	protected Timestamp getDeleteBeforeTimestamp() {
		int numberOfDays = getProperty().getNumberOfDaysToKeep();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1 * numberOfDays);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp deleteBeforeTimestamp = new Timestamp(cal.getTimeInMillis());
		return deleteBeforeTimestamp;
	}

	protected Timestamp getProcessFromTimestamp() {
		int numberOfDays = getProperty().getNumberOfDaysToSelect();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1 * numberOfDays);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp startTimestamp = new Timestamp(cal.getTimeInMillis());
		return startTimestamp;
	}

	protected String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return getDateFormat().format(date);
	}

	protected String formatTime(Date date) {
		if (date == null) {
			return null;
		}
		return getTimeFormat().format(date);
	}

	// === get/set === //
	protected MaterialServicePropertyBean getProperty() {
		return property;
	}

	protected MaterialServiceDao getDao() {
		return dao;
	}

	protected DateFormat getDateFormat() {
		return dateFormat;
	}

	protected DateFormat getTimeFormat() {
		return timeFormat;
	}

	protected DateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}
}
