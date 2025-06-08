package com.honda.galc.service.common;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.HoldParmDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.DeviceUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
/**
 * 
 * <h3>ProductHoldServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductHoldServiceImpl description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 21, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 21, 2017
 */
public class ProductHoldServiceImpl implements ProductHoldService{
	private final String SERVICE_NAME="ProductHoldService";
	private int holdSource;
	private String holdReason;
	private String associateNo;
	private ProcessPoint processPoint;
	private String productId;
	private BaseProduct product;
	private String processPointId;
	private String productionLot;
	
	private String productType;
	private SystemPropertyBean property;
	private HoldResultType holdResultType = HoldResultType.HOLD_NOW;//default
	private String holdAccessType;
	private boolean qsrHold = false;
	private HoldParmDao holdParmDao;
	
	@Autowired
	private HoldResultDao holdResultDao;
	
	@Autowired
	private QsrDao qsrDao;

	@Override
	public Device execute(Device device) {
		 DataContainer rdc = execute(device.toDataContainer());
		 device.populateReply(rdc);
		 return device;
	}

	@Override
	public DataContainer execute(DataContainer data) {
		this.productId = data.getString(TagNames.PRODUCT_ID.name());
		if(data.containsKey(TagNames.PROCESS_POINT.name()))
			this.processPoint = (ProcessPoint)data.get(TagNames.PROCESS_POINT.name());
		this.processPointId = data.getString(TagNames.PROCESS_POINT_ID.name());
		this.holdReason = data.getString(TagNames.HOLD_REASON.name());
		this.associateNo =data.getString(TagNames.ASSOCIATE_ID.name());
		String holdSrcStr = data.getString(TagNames.HOLD_SOURCE.name());		
		this.holdSource = holdSrcStr == null ? 0 : Integer.valueOf(holdSrcStr);
		if(data.containsKey(TagNames.PRODUCT.name()))
			this.product = getProduct(data);
		this.productionLot = data.getString(TagNames.PRODUCTION_LOT.name());
		if(data.containsKey(TagNames.QSR_HOLD.name()))
			this.qsrHold = DeviceUtil.convertToBoolean(data.getString(TagNames.QSR_HOLD.name()));
		if(data.containsKey(TagNames.HOLD_RESULT_TYPE.name()))
			this.holdResultType = HoldResultType.valueOf(data.getString(TagNames.HOLD_RESULT_TYPE.name()));
		if(data.containsKey(TagNames.HOLD_ACCESS_TYPE.name())) {
			this.holdAccessType = data.getString(TagNames.HOLD_ACCESS_TYPE.name());
		}
		data.put(SERVICE_NAME, createQCHold());
		
		return data;
	}
	
	private Boolean createQCHold() {
		 if((getProduct() == null || getProduct().getProductId() == null) && StringUtils.isEmpty(getProductId())) {
			 Logger.getLogger().warn("Failed to find product to create QC hold.");
			 return false;
		 }
		
		 try {
			saveHoldData();
			Logger.getLogger().info("Product placed on QC Hold:", getProduct().getProductId());
		} catch (Exception e) {
			Logger.getLogger().warn("Exception to put product:" + getProductId(), " On Hold:", e.getMessage());
			return false;
		}
		 
		 return true;
		
	}
	

	public void saveHoldData() {
		//create record in GAL147TBX with hold_source to equipment
		DailyDepartmentSchedule schedule = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).find(getProcessPoint().getDivisionId(), new Timestamp(System.currentTimeMillis()));
		Date productionDate = null;
		if (schedule != null) {
			productionDate = schedule.getId().getProductionDate();
		}
		HoldResult holdResult=new HoldResult();
		holdResult.setId(new HoldResultId(getProduct().getProductId(), holdResultType.getId()));
		holdResult.setHoldAssociateNo(getAssociateNo());
		holdResult.setProductSpecCode(getProduct().getProductSpecCode());	
		holdResult.setProductionLot(productionLot != null? productionLot : getProduct().getProductionLot());
		holdResult.setHoldProcessPoint(processPointId);

		holdResult.setEquipmentFlg(getHoldSource());
		holdResult.setHoldReason(getHoldReason());
		if (productionDate !=null)
			holdResult.setProductionDate(productionDate);
		
		//don't create duplicated hold record for the same reason
		if(!isHoldResultExist(holdResult)){
			createQsrHold(holdResult);
			Logger.getLogger().info(holdResult.getId().getProductId(), " was put on hold for hold reason:", holdResult.getHoldReason(), " by:", holdResult.getHoldAssociateNo());
		} else
			Logger.getLogger().info(holdResult.getId().getProductId(), " already on hold for hold reason:", holdResult.getHoldReason());
	}
	
	private boolean isHoldResultExist(HoldResult holdResult) {	
		HoldResultType holdResultType = HoldResultType.getType(holdResult.getId().getHoldType());
		List<HoldResult> allHolds = getHoldResultDao().findAllByProductHoldTypeAndHoldReason(holdResult.getId().getProductId(), holdResultType, holdResult.getHoldReason());
		return allHolds != null && allHolds.size() > 0;
	}
	
	private void createQsrHold(HoldResult holdResult) {

		if(!qsrHold) {
			ProductTypeUtil.getProductDao(getProduct().getProductType()).updateHoldStatus(getProduct().getProductId(),
					HoldStatus.ON_HOLD.getId());
			getHoldResultDao().save(holdResult);
		} else {
			List<HoldResult> holdResults = new ArrayList<HoldResult>();
			holdResults.add(holdResult);
			Qsr qsr = assembleQsr();
			getQsrDao().holdProducts(getProductType(), holdResults, qsr);
		} 

	}
	
	protected Qsr assembleQsr() {
		Qsr qsr = new Qsr();
		qsr.setProcessLocation(getProcessPoint().getDivisionId());
		qsr.setProductType(getProperty().getProductType());
		qsr.setDescription(this.holdReason);
		qsr.setStatus(QsrStatus.ACTIVE.getIntValue());
		qsr.setHoldAccessType(this.holdAccessType);
		qsr.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		return qsr;
	}

	@Override
	public void qsrRelease(DataContainer dc) { 
		if(dc.containsKey(TagNames.PRODUCT_TYPE.name()))
			this.productType = dc.getString(TagNames.PRODUCT_TYPE.name());
		this.productId = dc.getString(TagNames.PRODUCT_ID.name());
		this.product = getProduct(dc);
		if(dc.containsKey(TagNames.PRODUCTION_LOT.name()))
			this.productionLot = dc.getString(TagNames.PRODUCTION_LOT.name());
		this.processPointId = dc.getString(TagNames.PROCESS_POINT_ID.name());
		if(dc.containsKey(TagNames.HOLD_RESULT_TYPE.name()))
			holdResultType = HoldResultType.valueOf(dc.getString(TagNames.HOLD_RESULT_TYPE.name()));
		
		List<HoldResult> holdList = getHoldResultDao().findAllByProductHoldTypeAndHoldReason(productId, holdResultType, dc.getString(TagNames.HOLD_REASON.name()));
		int qsrId = 0;
		Qsr qsr = null;
		
		for(HoldResult result : holdList) {
			result.setReleaseAssociateNo(dc.getString(TagNames.ASSOCIATE_ID.name()));
			result.setReleaseAssociateName(dc.getString(TagNames.ASSOCIATE_ID.name()));
			result.setReleaseFlag((short)1);
			result.setReleaseReason(dc.getString(TagNames.RELEASE_REASON.name()));
			
			result.setReleaseTimestamp(new Timestamp(System.currentTimeMillis()));
			result.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			
			List<HoldResult> holdResultList = new ArrayList<HoldResult>();
			holdResultList.add(result);
			
			if(result.getQsrId() > 0) {
				qsrId = result.getQsrId();

				qsr = getQsrDao().findByKey(qsrId);
				qsr.setApproverName(processPointId);
				if(isAllHoldsOnQsrReleased(qsrId, holdResultList.size()) && !isActiveHoldParamExist(qsr))
					qsr.setStatus(QsrStatus.COMPLETED.getIntValue());
			}
			
			getQsrDao().updateHoldResults(ProductType.valueOf(productType), holdResultList, qsr);
			
			Logger.getLogger().info("release hold result:", result.toString());
		}
	}

	private boolean isAllHoldsOnQsrReleased(int qsrId, int releaseCount) {
		List<HoldResult> allByQsrId = getHoldResultDao().findAllByQsrId(qsrId);
		
		int holdCount = 0;
		for(HoldResult hr : allByQsrId) {
			if(hr.getReleaseFlag() != 1)
				holdCount++;
		}
		return holdCount == releaseCount;
	}


	@Override
	public void release(DataContainer dc) {
		this.product = (BaseProduct)dc.get(TagNames.PRODUCT.name());
		this.productionLot = dc.getString(TagNames.PRODUCTION_LOT.name());
		this.processPointId = dc.getString(TagNames.PROCESS_POINT_ID.name());
		List<HoldResult> holdList = getHoldResultDao().findAllHoldByProductIdAndHoldReason(dc.getString(TagNames.PRODUCT_ID.name()), dc.getString(TagNames.HOLD_REASON.name()));
		for(HoldResult result : holdList) {
			result.setReleaseAssociateNo(dc.getString(TagNames.ASSOCIATE_ID.name()));
			result.setReleaseFlag((short)1);
			result.setReleaseReason(dc.getString(TagNames.RELEASE_REASON.name()));
			result.setReleaseTimestamp(new Timestamp(System.currentTimeMillis()));
			result.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			getHoldResultDao().update(result);
			Logger.getLogger().info("release hold result:", result.toString());
		}
		
		List<HoldResult> holdsOnProduct = getHoldResultDao().findAllByProductAndReleaseFlag(dc.getString(TagNames.PRODUCT_ID.name()), false,HoldResultType.GENERIC_HOLD);
		if(holdsOnProduct == null || holdsOnProduct.isEmpty()) {
			ProductTypeUtil.getProductDao(getProduct().getProductType()).updateHoldStatus(getProduct().getProductId(),
					HoldStatus.NOT_ON_HOLD.getId());
		}
	}
	
	
	@Override
	public boolean isQsrHoldBySpecCheck(String productId, HoldResultType holdType, String holdReason) {
		 List<HoldResult> allSpecChekHolds = getHoldResultDao().findAllByProductHoldTypeAndHoldReason(productId, holdType, holdReason);
		return allSpecChekHolds != null && allSpecChekHolds.size() > 0;
	}
	
	protected boolean isActiveHoldParamExist(Qsr qsr) {
		List<HoldParm> holdParams = getHoldParamDao().findAllByQsrId(qsr.getId());
		if (holdParams != null && holdParams.size() > 0) {
			for (HoldParm hp : holdParams) {
				if (hp.getReleaseFlag() != 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	//----------getter & setters --------------
	public SystemPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(SystemPropertyBean.class, this.processPointId);
		
		return property;
	}
	
	public HoldParmDao getHoldParamDao() {
		if(holdParmDao == null)
			holdParmDao = ServiceFactory.getDao(HoldParmDao.class);
		
		return holdParmDao;
	}
	
	public HoldResultDao getHoldResultDao() {
		if(holdResultDao == null)
			holdResultDao = ServiceFactory.getDao(HoldResultDao.class);
		return holdResultDao;
	}
	

	public String getProductId() {
		return productId;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public String getHoldReason() {
		return holdReason;
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}
	
	public String getAssociateNo() {
		return associateNo;
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}
	
	private ProcessPoint getProcessPoint() {
		if(processPoint == null)
			processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(getProcessPointId());
			
		return processPoint;
	}
	
	public int getHoldSource() {
		return holdSource;
	}

	public void setHoldSource(int holdSource) {
		this.holdSource = holdSource;
	}
	

	private QsrDao getQsrDao() {
		if(qsrDao == null)
			qsrDao = ServiceFactory.getDao(QsrDao.class);
		return qsrDao;
	}

	private ProductType getProductType() {
		if(productType == null)
			productType = getProperty().getProductType();
		return ProductType.valueOf(productType);
	}
	
	private BaseProduct getProduct(DataContainer dc) {
		BaseProduct product = null;
		if(dc.containsKey(TagNames.PRODUCT.name()))
			product = (BaseProduct)dc.get(TagNames.PRODUCT.name());
		
		if(product != null) 
			return product;
		else 
			return ProductTypeUtil.getProductDao(getProductType()).findByKey(productId);
		
		  
	}

	private BaseProduct getProduct() {
		if(product == null){
			product = ProductTypeUtil.getProductDao(getProductType()).findByKey(getProductId());
		}
			
		return product;
	}
	
}
