package com.honda.galc.service.datacollection.work;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>Persistence</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Persistence description </p>
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
 * <TD>Mar 12, 2014</TD>
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
 * @since Mar 12, 2014
 */
public class PersistenceWork  extends CollectorWork{

	public PersistenceWork(HeadlessDataCollectionContext context, ProductDataCollectorBase collector) {
		super(context, collector);
	}

	@Override
	void doWork() throws Exception {
		
		if(getProperty().isAutoSaveBuildResult())
			collector.saveBuildResults();
		
		if(getProperty().isAutoUpdateMeasurements())
			collector.updateMeasurements();
		
		if(getProperty().isAutoUpdateQics())
			collector.updateQics();
		
		doTracking();
		
		updateProductSeqence();
			
		if(getProperty().isSaveLastProduct()) saveLastProcessProduct();
		
		if (collector.isQCHold())
			createQCHold();
	}

	protected void doTracking() {
		if(getProperty().isAutoTracking())
			collector.track();
	}

	private void saveLastProcessProduct() {

		ExpectedProduct expectedProduct = new ExpectedProduct(context.getProductId(), context.getProcessPointId());
		ServiceFactory.getDao(ExpectedProductDao.class).save(expectedProduct);
		getLogger().info("Updated ExpectedProduct:", expectedProduct.toString());
		
	}

	private void updateProductSeqence() {
		//update product sequence if required
		if(context.getProcessPointId().equals(getProperty().getInProductSequenceId()))
			addProductSequence();
		else if(context.getProcessPointId().equals(getProperty().getOutProductSequenceId()))
			removeProductSequence();
	}

	private ProductSequence getProductSequence(String ppid) {
		ProductSequence sequence = new ProductSequence();
		ProductSequenceId sequenceId = new ProductSequenceId(context.getProduct().getProductId(),ppid);
		sequence.setId(sequenceId);
		return sequence;
	}
	  
	private void removeProductSequence() {
		 if(context.getProduct() == null || context.getProduct().getProductId() == null) return;
    	 ProductSequenceDao dao = getDao(ProductSequenceDao.class);
    	 ProductSequence sequence = getProductSequence(getProperty().getInProductSequenceId());

    	 List<ProductSequence> removedList = dao.removeProductSequence(sequence);
    	 getLogger().info("removed Product Sequence:", removedList.toString());
	}

	private void addProductSequence() {
		 if(context.getProduct() == null || context.getProduct().getProductId() == null) return;
		 ProductSequenceDao dao = getDao(ProductSequenceDao.class);
		 ProductSequence sequence = getProductSequence(context.getProcessPointId());
		 sequence.setReferenceTimestamp(new Timestamp(System.currentTimeMillis()));
		 dao.save(sequence);
		 getLogger().info("added Product Sequence:", sequence.toString());
		
	}
	
	private void createQCHold() {
		 if(context.getProduct() == null || context.getProduct().getProductId() == null) return;
		
		 //update hold status in product table
		 ProductTypeUtil.getProductDao(context.getProductType()).updateHoldStatus(context.getProduct().getProductId(), HoldStatus.ON_HOLD.getId());
	     context.setHoldSource(1); //Equipment 
	     
		 saveHoldData();
		 
		 getLogger().info("Product placed on QC Hold:", context.getProduct().getProductId());
		
	}

	public void saveHoldData() {
		
		//create record in GAL147TBX with hold_source to equipment
		DailyDepartmentSchedule schedule = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).find(context.getProcessPoint().getDivisionId(), new Timestamp(System.currentTimeMillis()));
		Date productionDate = null;
		if (schedule != null) {
			productionDate = schedule.getId().getProductionDate();
		}
		HoldResult holdResult=new HoldResult();
		holdResult.setId(new HoldResultId(context.getProduct().getProductId(), HoldResultType.HOLD_NOW.getId()));
		holdResult.setHoldAssociateNo(context.getAssociateNo());
		holdResult.setProductSpecCode(context.getProductSpecCode());	
		holdResult.setEquipmentFlg(context.getHoldSource());
		holdResult.setHoldProcessPoint(context.getProcessPointId());
		holdResult.setHoldReason("Device");
		if (productionDate !=null)
			holdResult.setProductionDate(productionDate);
		getDao(HoldResultDao.class).save(holdResult);
	}
	
}
