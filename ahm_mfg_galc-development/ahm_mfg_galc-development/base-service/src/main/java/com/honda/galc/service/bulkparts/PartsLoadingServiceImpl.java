/**
 * 
 */
package com.honda.galc.service.bulkparts;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.PartsLoadingHistoryDao;
import com.honda.galc.dao.product.PartsLoadingMaintenanceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartsLoadingHistory;
import com.honda.galc.entity.product.PartsLoadingMaintenance;
import com.honda.galc.service.PartsLoadingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.common.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * @author vf031824
 *
 */
public class PartsLoadingServiceImpl implements PartsLoadingService{

	protected String processPointId = null;
	protected String binName = null;
	protected String partNumber = null;
	protected String associateId = null;
	protected int status = 0;

	protected static final String NO_ERROR = "01";
	protected static final String NO_DC_PP = "02";
	protected static final String NO_BIN = "03";
	protected static final String NO_PART_SPEC = "04";
	protected static final String NO_BIN_PART_COMBO = "05";
	protected static final String DUPLICATE_RULE_FOUND = "06";
	protected static final String INVALID_PART_LOADED = "07";
	protected static final String DB_ERROR = "08";

	private String CLASS_NAME = "PartsLoadingServiceImpl";
	private String DC_PROCESS_POINT_ID = "DC_PROCESS_POINT_ID";
	private String COMPLETED_SUCCESSFULLY = "Parts Loading Service completed successfully";
	private String NO_DC_PROCESS_POINT_CONFIGURED = "No data collection process point is configured";
	private String NO_BIN_COMBINATION_FOUND =  "No bin and process point combination found";
	private String NO_PART_SPEC_FOUND = "No part spec found";
	private String DUPLICATE_RULE_FOUND_MSG = "Multiple part serial number masks found attached to process point";
	private String DB_ERROR_OCCURED = "A database error has occured in method: ";
	private String ASSOCIATE_NO = "ASSOCIATE_NO";

	protected HeadlessDataCollectionContext context = new HeadlessDataCollectionContext();
	private PartsLoadingMaintenance partMaintenance;
	private DataContainer dc = new  DefaultDataContainer();


	private void init(Device device){
		context.clear();

		context.setDevice(device);
		this.processPointId = StringUtils.trim(device.getIoProcessPointId());
		this.binName = (String) (device.getInputValue((TagNames.BIN_NAME.name()).toString()));
		this.partNumber = (String) (device.getInputValue((TagNames.PART_NUMBER.name()).toString()));
		this.associateId = (String) (device.getInputValue(TagNames.ASSOCIATE_ID.name().toString()));
		getLogger().info("collector received device data:", device.toInputString());

		if(context.containsKey(ASSOCIATE_NO)) 
			context.setAssociateNo(context.get(ASSOCIATE_NO).toString());
		else context.setAssociateNo("");

		context.put("processPointId", processPointId);
		context.put(TagNames.ERROR_CODE.name(), NO_ERROR);//set default error code
		context.put(TagNames.ERROR_MESSAGE.name(), COMPLETED_SUCCESSFULLY);
	}

	public DataContainer execute(DefaultDataContainer data) {
		DataContainer dt = data;
		return execute(dt);
	}

	public DataContainer execute(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		device.populate(data);
		return execute(device).toReplyDataContainer(true);
	}

	public Device execute(Device device) {
		init(device);

		getLogger().info("Begin PartsLoadingService");		

		context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);

		String dcProcessPoint = PropertyService.getProperty(processPointId, DC_PROCESS_POINT_ID);

		if(StringUtils.isEmpty(dcProcessPoint)) {
			errorFound(NO_DC_PROCESS_POINT_CONFIGURED, NO_DC_PP);
			context.prepareReply(device);
			return device;
		}

		partMaintenance = getPartsMaintByProcessPointAndBin(dcProcessPoint, binName);

		if(partMaintenance == null) {
			errorFound(NO_BIN_COMBINATION_FOUND, NO_BIN);
			context.prepareReply(device);
			return device;
		}

		PartSpec partSpec = getSpecByPartNameAndPartId(partMaintenance.getPartName(), partMaintenance.getPartSpecId());

		if(partSpec == null) {
			errorFound(NO_PART_SPEC_FOUND, NO_PART_SPEC);
			context.prepareReply(device);
			return device;
		}
		boolean status = CommonPartUtility.verification(partNumber, partSpec.getPartSerialNumberMask(),
				CommonPartUtility.PartMaskFormat.getFormat(PropertyService.getPartMaskWildcardFormat()).toString());

		context.put(TagNames.STATUS.name(),(status)?1:0);

		saveData();

		context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
		context.prepareReply(device);
		getLogger().info("End PartsLoadingService");
		return device;
	}

	public DataContainer getBinNameByPartNumber(DefaultDataContainer data) {

		boolean partFound = false;
		List<PartSpec> filteredPartSpec = new  ArrayList<PartSpec>();

		processPointId = data.getString(TagNames.PROCESS_POINT_ID.name());
		partNumber= data.getString(TagNames.PART_NUMBER.name());

		String dcProcessPoint = PropertyService.getProperty(processPointId, DC_PROCESS_POINT_ID);

		List<PartSpec> partSpec = ServiceFactory.getDao(PartSpecDao.class)
				.findAllByProcessPoint(dcProcessPoint);

		for(PartSpec spec : partSpec) {
			if(CommonPartUtility.verification(partNumber, spec.getPartSerialNumberMask(),
					CommonPartUtility.PartMaskFormat.getFormat(PropertyService.getPartMaskWildcardFormat()).toString())) {
				filteredPartSpec.add(spec);
			}
		}
		if(!filteredPartSpec.isEmpty()){
			PartsLoadingMaintenance partsLoadingMaintenance = new PartsLoadingMaintenance();
			for(PartSpec spec :filteredPartSpec){

				partsLoadingMaintenance = findByProcessPointPartNameAndPartId(spec, dcProcessPoint);

				if(!(partsLoadingMaintenance == null)) {
					if(partFound != true){
						partFound = true;
						partMaintenance = partsLoadingMaintenance;
					}else {
						dc.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
						dc.put(TagNames.ERROR_CODE.name(), DUPLICATE_RULE_FOUND);
						dc.put(TagNames.ERROR_MESSAGE.name(), DUPLICATE_RULE_FOUND_MSG);
						getLogger().error(DUPLICATE_RULE_FOUND_MSG);
						return dc;
					}
				}
			}
			if(partFound == false) {
				noPartFound();
				
			} else partFound();	
		}else noPartFound();
		
		return dc;
	}

	public void saveData() {
		PartsLoadingHistory partsLoadingHistory = new PartsLoadingHistory();
		partsLoadingHistory.setProcessPointId(processPointId);
		partsLoadingHistory.setPartName(partMaintenance.getPartName());
		partsLoadingHistory.setPartNumber(partNumber);
		partsLoadingHistory.setPartsContainerSerialNumber(binName);
		partsLoadingHistory.setAssociateId(associateId);
		partsLoadingHistory.setPartsLoadingStatus((Integer) context.get(TagNames.STATUS.name()));
		partsLoadingHistory.setActualTimestamp(new Timestamp(System.currentTimeMillis()));

		ServiceFactory.getDao(PartsLoadingHistoryDao.class).save(partsLoadingHistory);

		getLogger().info("data saved to DB");
	}

	public PartsLoadingMaintenance getPartsMaintByProcessPointAndBin(String dcProcessPointId, String binName) {
		PartsLoadingMaintenance partsLoadingMaintenance = null;
		try{
			partsLoadingMaintenance = ServiceFactory.getDao(PartsLoadingMaintenanceDao.class)
					.findByProcessPointIdAndBinName(dcProcessPointId, binName);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getPartsMaintByProcessPointAndBin";
			errorFound(msg, DB_ERROR);
			e.printStackTrace();
		}
		return partsLoadingMaintenance;
	}

	public PartSpec getSpecByPartNameAndPartId(String partName, String partId) {
		PartSpec partSpec = null;
		try {
			partSpec = ServiceFactory.getDao(PartSpecDao.class)
					.findValueWithPartNameAndPartID(partName, partId);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getSpecByPartNameAndPartId";
			errorFound(msg, DB_ERROR);
			e.printStackTrace();
		}
		return partSpec;
	}

	public PartsLoadingMaintenance findByProcessPointPartNameAndPartId(PartSpec spec, String processPointId){
		PartsLoadingMaintenance partsLoadingMaintenance = null;
		try {
			partsLoadingMaintenance = ServiceFactory.getDao(PartsLoadingMaintenanceDao.class)
					.findByProcessPointPartNameAndPartId(spec, processPointId);
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "findByProcessPointPartNameAndPartId";
			errorFound(msg, DB_ERROR);
			e.printStackTrace();
		}
		return partsLoadingMaintenance;
	}

	private void noPartFound() {
		dc.put(TagNames.ERROR_CODE.name(), NO_BIN_PART_COMBO );
		dc.put(TagNames.ERROR_MESSAGE.name(), NO_BIN_COMBINATION_FOUND );
		dc.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		getLogger().error(NO_BIN_COMBINATION_FOUND);
	}

	private void partFound() {
		dc.put(TagNames.PROCESS_POINT_ID.name(), processPointId);
		dc.put(TagNames.PART_NUMBER.name(), partNumber);
		dc.put(TagNames.PART_NAME.name(), partMaintenance.getPartName());
		dc.put(TagNames.BIN_NAME.name(), partMaintenance.getBinName());
		dc.put(TagNames.ERROR_CODE.name(), NO_ERROR);
		dc.put(TagNames.ERROR_MESSAGE.name(), COMPLETED_SUCCESSFULLY);
		dc.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
	}

	private void errorFound(String msg, String errorCode) {
		context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		context.put(TagNames.ERROR_CODE.name(), errorCode);
		context.put(TagNames.ERROR_MESSAGE.name(), msg);
		getLogger().error(msg);
	}

	public Logger getLogger() {
		if(StringUtils.isEmpty(processPointId)) 
			return Logger.getLogger(CLASS_NAME);
		else 
			return Logger.getLogger(processPointId);
	}
}