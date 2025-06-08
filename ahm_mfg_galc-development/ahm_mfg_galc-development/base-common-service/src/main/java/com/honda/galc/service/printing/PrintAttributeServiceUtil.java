package com.honda.galc.service.printing;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BuildSheetPartGroupDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.product.CounterDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.PrintAttribute;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>PrintAttributeServiceUtil Class description</h3>
 * <p> PrintAttributeServiceUtil description </p>
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
 * @author
 *
 */

public class PrintAttributeServiceUtil {
	private Logger logger;
	private DataContainer dc;
	
	public static final String Y = "Y";
	public static final String N = "N";
	public static final String ZERO = "0";
	public static final String ONE = "1";
	public static final String TRAY_ONE = "TRAY_ONE";
	public static final String TRAY_TWO = "TRAY_TWO";
	public static final String STRAGGLER_TRAY = "STRAGGLER_TRAY";
	public static final String CURRENT_TRAY_VALUE = "CURRENT_TRAY_VALUE";
	public static final String CURRENT_SPEC_CODE = "CURRENT_SPEC_CODE";
	public static final String CURRENT_PROD_LOT = "CURRENT_PROD_LOT";
	public static final String STRAGGLER = "STRAGGLER";
	public static final String REBUILD = "REBUILD";
	public static final String PASS_QTY = "PASS_QTY";
	public static final String QTY = "QTY";
	
	private ComponentStatus currentTray =  new ComponentStatus();
	private ComponentStatus currentSpecCode =  new ComponentStatus();
	
	private ComponentStatusId cpTrayId = null;
	private ComponentStatusId cpSpecCodeId = null;
	
	private ComponentStatus currentProdLot =  new ComponentStatus();
	private ComponentStatusId cpProdLotId = null;
	
	public PrintAttributeServiceUtil(DataContainer dc, Logger logger) {
		this.dc = dc;
		this.logger = logger;
	}
	
	private void loadFormIdAndCurrentTrayValues() {
		currentTray =  ServiceFactory.getDao(ComponentStatusDao.class).findByKey(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_TRAY_VALUE);
		currentSpecCode =  ServiceFactory.getDao(ComponentStatusDao.class).findByKey(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_SPEC_CODE);
		
		cpTrayId = new ComponentStatusId(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_TRAY_VALUE);
		cpSpecCodeId = new ComponentStatusId(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_SPEC_CODE);
	}
	
	@PrintAttribute 
	public String getTrayValueForProdSpecChangeAltLot(){
		logger.info("Entering into getTrayValueForProdSpecChangeAltLot method of PrintAttributeServiceUtil");
		String isStraggler=verifyStraggler();
		return (isStraggler==null? getProdSpecChangeAltLot():isStraggler);
	}	
	
	@PrintAttribute 
	public String getTrayValueForProdSpecChangeFirst(){
		logger.info("Entering into getTrayValueForProdSpecChangeFirst method of PrintAttributeServiceUtil");
		String isStraggler=verifyStraggler();
		return (isStraggler==null? getFirstProdSpecChange():isStraggler);
	}
	
	@PrintAttribute 
	public String getTrayValueForAltLotChangeByKDLot(){
		logger.info("Entering into getTrayValueForAltLotChangeByKDLot method of PrintAttributeServiceUtil");
		String isStraggler=verifyStraggler();
		return (isStraggler==null? getAltLotChange():isStraggler);
	}
	
	@PrintAttribute 
	public String getTrayValueForConditionalChangeByKDLot(){
		logger.info("Entering into getTrayValueForConditionalChangeByKDLot method of PrintAttributeServiceUtil");
		String isStraggler=verifyStraggler();
		return (isStraggler==null? getConditionalChange():isStraggler);
	}
	
	public String verifyStraggler() {
		loadFormIdAndCurrentTrayValues();
		if(isStraggler()){
			return PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), STRAGGLER_TRAY, ONE);			
		}
		return null;
	}
	
	private boolean isStraggler(){
		String straggler = StringUtils.trimToEmpty(dc.getString(STRAGGLER));
		if(StringUtils.isBlank(straggler)){
			straggler = getStragglerByKDLot();
		}
		if(StringUtils.equalsIgnoreCase(straggler,Y)){
			return true;
		}
		return false;
	}
	
	private String getConditionalChange() {
		String rebuild = StringUtils.trimToEmpty(dc.getString(REBUILD));
		String passQty = StringUtils.trimToEmpty(dc.getString(PASS_QTY));
		if(StringUtils.isBlank(rebuild)){
			rebuild = getRebuildByKDLot();
		}
		if(StringUtils.isBlank(passQty)){
			passQty = getPassQtyByKDLot();
		}
		if(StringUtils.equalsIgnoreCase(rebuild,Y) || StringUtils.equalsIgnoreCase(passQty,ONE)){
			return PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_TWO, ONE);			
		} else {
			return PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO);
		}
	}

	@PrintAttribute
	public String getPassQtyByKDLot(){
		logger.info("Entering into getPassQtyByKDLot of PrintAttributeServiceUtil");
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		int passQty = ServiceFactory.getDao(FrameDao.class).getPassQuantity(productId, processPointId);
		return String.valueOf(passQty);
	}
	
	/**
	 * Get current production date's passing body count for a specific process point ID
	 * @return
	 */
	@PrintAttribute
	public String getCurrentProcessBodyCount(){
		logger.info("Entering into getCurrentProcessBodyCount of PrintAttributeServiceUtil");
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		int count = 
				ServiceFactory.getDao(CounterDao.class).getPassingBodyCountForCurrentDate(processPointId);
		return String.valueOf(count);
	}
	
	/**
	 * Determine if the product has been processed at this process point for the first time
	 * @return
	 */
	@PrintAttribute
	public String firstTimeInProcess(){
		logger.info("Entering into firstTimeInProcess of PrintAttributeServiceUtil");
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		int count = 
				ServiceFactory.getDao(ProductResultDao.class).firstTimeInProcess(productId, processPointId);
		if (count == 1) return Y;
		return N;
	}
	
	@PrintAttribute
	public String getScheduleQtyByKDLot(){
		logger.info("Entering into getScheduleQtyByKDLot of PrintAttributeServiceUtil");
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String planCode = PropertyService.getProperty(dc.getString(DataContainerTag.PROCESS_POINT_ID), TagNames.PLAN_CODE.name());
		int scheduleQty = ServiceFactory.getDao(FrameDao.class).getScheduleQuantity(productId, planCode);
		return String.valueOf(scheduleQty);
	}
	
	@PrintAttribute
	public String getStragglerByKDLot(){
		logger.info("Entering into getStragglerByKDLot of PrintAttributeServiceUtil");
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		List<Straggler> currentProductstragglerList = ServiceFactory.getDao(StragglerDao.class).findStragglerProductList(productId, processPointId);	
		if(currentProductstragglerList != null && currentProductstragglerList.size() > 0){
			if(PropertyService.getPropertyBoolean(processPointId,"STRAGGLER_GROUPING",false)) {
				for (Straggler straggler : currentProductstragglerList) {
					if(StringUtils.equals(StringUtils.trimToEmpty(straggler.getId().getStragglerType()), "MTOCI")) {
						return Y;
					}
				}
				return N;
			}
			return Y;
		}
		return N;		
	}
	
	@PrintAttribute
	public String getCutlotByKDLot(){
		logger.info("Entering into getCutlotByKDLot of PrintAttributeServiceUtil");
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		String sequencingPPId = PropertyService.getProperty(processPointId, "SEQUENCING_PROCESSPOINT", "TPA3OF1P00101");
		String rebuild = StringUtils.trimToEmpty(dc.getString(REBUILD));
		String straggler = StringUtils.trimToEmpty(dc.getString(STRAGGLER));
		String passQty = StringUtils.trimToEmpty(dc.getString(PASS_QTY));
		String qty = StringUtils.trimToEmpty(dc.getString(QTY));
		
		if(StringUtils.isBlank(rebuild)){
			rebuild = getRebuildByKDLot();
		}
		if(StringUtils.isBlank(straggler)){
			straggler = getStragglerByKDLot();
		}
		if(StringUtils.isBlank(passQty)){
			passQty = getPassQtyByKDLot();
		}
		if(StringUtils.isBlank(qty)){
			qty = getScheduleQtyByKDLot();
		}
		if(StringUtils.equalsIgnoreCase(rebuild,Y) || StringUtils.equalsIgnoreCase(straggler,Y)
				|| StringUtils.equalsIgnoreCase(passQty,ONE) || StringUtils.equalsIgnoreCase(qty,passQty) ){
			return N;			
		} else {
			int cutlotValue = ServiceFactory.getDao(FrameDao.class).getCutlot(productId, processPointId, sequencingPPId);
			if(cutlotValue == 0){
				return Y;
			}
		}
		return N;		
	}
	
	@PrintAttribute
	public String getRebuildByKDLot(){
		logger.info("Entering into getRebuildByKDLot of PrintAttributeServiceUtil");
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String planCode = PropertyService.getProperty(dc.getString(DataContainerTag.PROCESS_POINT_ID), TagNames.PLAN_CODE.name());
		int rebuild = ServiceFactory.getDao(FrameDao.class).getRebuild(productId, planCode);
		if(rebuild > 0){
			return Y;
		}
		return N;		
	}

	
	
	private String getFirstProdSpecChange() {
		logger.info("Entering into getFirstProdSpecChange method of PrintAttributeServiceUtil");
		String crtSpecCode = null;
		if(currentSpecCode != null){
			crtSpecCode = currentSpecCode.getStatusValue();
		}
		
		if(StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCT_SPEC_CODE)),crtSpecCode)){
			return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_TWO, ONE));
		} else {
			return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO));
		}
	}

	private String getProdSpecChangeAltLot() {
		logger.info("Entering into getProdSpecChangeAltLot method of PrintAttributeServiceUtil");
		String crtTrayVal = null;
		String crtSpecCode = null;
		if(currentTray != null && currentSpecCode != null){
			crtTrayVal = currentTray.getStatusValue();
			crtSpecCode = currentSpecCode.getStatusValue();
		}
		
		if(!StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCT_SPEC_CODE)),crtSpecCode)){
			if(StringUtils.equalsIgnoreCase(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO), crtTrayVal)){
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_TWO, ONE));
			} else {
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO));
			}
		} else {
			if(StringUtils.isBlank(crtTrayVal)){ 
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO));
			} else {
				return crtTrayVal;
			}
		}
	}
	
	private String getAltLotChange() {
		logger.info("Entering into getAltLotChange method of PrintAttributeServiceUtil");
		String crtTrayVal = null;
		String passQty = StringUtils.trimToEmpty(dc.getString(PASS_QTY));
		if(StringUtils.isBlank(passQty)){
			passQty = getPassQtyByKDLot();
		}
		if(currentTray != null){
			crtTrayVal = currentTray.getStatusValue();
		}
			
		if(StringUtils.equalsIgnoreCase(passQty, ONE)){
			if(StringUtils.equalsIgnoreCase(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO), crtTrayVal)){
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_TWO, ONE));
			} else {
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO));
			}
		} else {
			if(StringUtils.isBlank(crtTrayVal)){ 
				return PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO);
			} else {
				return crtTrayVal;
			}
		}
	}

	private String updateComponents(String tray) {
		updateComponentStatus(cpTrayId, tray);
		updateComponentStatus(cpSpecCodeId, StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCT_SPEC_CODE)));
		return tray;
    } 
	
	private void updateComponentStatus(ComponentStatusId id, String statusValue){
		ComponentStatus cpStatus = new ComponentStatus();
		cpStatus.setId(id);
		cpStatus.setStatusValue(statusValue);
		ServiceFactory.getDao(ComponentStatusDao.class).save(cpStatus);
	}
		
	private void updateData(DataContainer data, Object object){
		data.put(StringUtils.trimToEmpty(((Object[]) object)[0].toString())+"_"+((Object[]) object)[2]+"_"+((Object[]) object)[3]+"_PD", 
				StringUtils.trimToEmpty(((Object[]) object)[1].toString()));
		data.put(StringUtils.trimToEmpty(((Object[]) object)[0].toString())+"_"+((Object[]) object)[2]+"_"+((Object[]) object)[3]+"_PM", 
				StringUtils.trimToEmpty(((Object[]) object)[4].toString()));
	}
	
	private void errorData(DataContainer data, Object object){
		data.put(((Object[]) object)[0].toString().trim()+"_"+((Object[]) object)[2]+"_"+((Object[]) object)[3]+"_PD", "ERROR");
		data.put(((Object[]) object)[0].toString().trim()+"_"+((Object[]) object)[2]+"_"+((Object[]) object)[3]+"_PM", "??");
	}
			
	// Generated Part Matrix for Build Sheets will be in the form of Part Mark = FRONT_SUSP_1_1_PM(FORMID_ROW_COL_PM)
	// Part Desc = FRONT_SUSP_1_1_PD(FORMID_ROW_COL_PD) and same thing will mapped in jasper
	// If part mark and part desc is different for same row and column then display ERROR & ?? on build sheet
	@PrintAttribute
	public void generatePartMatrixByGroup(){
		ProductSpec productSpec = (ProductSpec) dc.get(DataContainerTag.PRODUCT_SPEC);
		List<Object[]> matrixList = ServiceFactory.getDao(BuildSheetPartGroupDao.class).generatePartMatrix(productSpec,dc.get(DataContainerTag.FORM_ID).toString());
		DataContainer data =  new DefaultDataContainer();
		String row = null, column = null, partDesc = null, partMark = null, modelGroup = null;
		for (Object object[] : matrixList) {
			if(object[4] == null){
				object[4] = StringUtils.EMPTY;
			}
			if(!data.isEmpty() && (StringUtils.equals(row, object[2].toString().trim())) 
				&& (StringUtils.equals(column, object[3].toString().trim()))) {
				if((StringUtils.equals(modelGroup, object[5].toString().trim())) &&
					(StringUtils.equals(partMark, object[4].toString().trim())) &&
					(StringUtils.equals(partDesc, object[1].toString().trim()))){
					updateData(data, object);
				} else {
					errorData(data, object);
				}
			} else {
				updateData(data, object);
			}
			partDesc = StringUtils.trimToEmpty(object[1].toString());
			row = StringUtils.trimToEmpty(object[2].toString());
			column = StringUtils.trimToEmpty(object[3].toString());
			partMark = StringUtils.trimToEmpty(object[4].toString());
			modelGroup = StringUtils.trimToEmpty(object[5].toString());
		}
		dc.putAll(data);
	}
	
	@PrintAttribute 
	public String getTrayValueForProdLotChangeAltLot(){
		logger.info("Entering into getTrayValueForProdLotChangeAltLot method of PrintAttributeServiceUtil");
		String crtTrayVal = null;
		String crtProdLot = null;
		loadCurrentTrayWithProdlot();
		if(currentTray != null && currentProdLot != null){
			crtTrayVal = currentTray.getStatusValue();
			crtProdLot = currentProdLot.getStatusValue();
		}
		
		if(!StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCTION_LOT)),crtProdLot)){
			if(StringUtils.equalsIgnoreCase(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO), crtTrayVal)){
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_TWO, ONE), StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCTION_LOT)));
			} else {
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO), StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCTION_LOT)));
			}
		} else {
			if(StringUtils.isBlank(crtTrayVal)){ 
				return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO), StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCTION_LOT)));
			} else {
				return crtTrayVal;
			}
		}
	}	
	
	@PrintAttribute 
	public String getTrayValueForProdLotChangeFirst(){
		logger.info("Entering into getTrayValueForProdLotChangeFirst method of PrintAttributeServiceUtil");
		String crtProdLot = null;
		loadCurrentTrayWithProdlot();
		if(currentProdLot != null){
			crtProdLot = currentProdLot.getStatusValue();
		}
		
		if(StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCTION_LOT)),crtProdLot)){
			return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_TWO, ONE), StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCTION_LOT)));
		} else {
			return updateComponents(PropertyService.getProperty(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID)), TRAY_ONE, ZERO), StringUtils.trimToEmpty(dc.getString(DataContainerTag.PRODUCTION_LOT)));
		}
	}
	
	private String updateComponents(String tray, String value) {
		updateComponentStatus(cpTrayId, tray);
		updateComponentStatus(cpProdLotId, value);
		return tray;
    }
	
	private void loadCurrentTrayWithProdlot(){
		currentProdLot =  ServiceFactory.getDao(ComponentStatusDao.class).findByKey(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_PROD_LOT);
		cpProdLotId = new ComponentStatusId(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_PROD_LOT);
		
		currentTray =  ServiceFactory.getDao(ComponentStatusDao.class).findByKey(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_TRAY_VALUE);
		cpTrayId = new ComponentStatusId(StringUtils.trimToEmpty(dc.getString(DataContainerTag.FORM_ID))+dc.getString(DataContainerTag.PROCESS_POINT_ID), CURRENT_TRAY_VALUE);
	}
	
}
