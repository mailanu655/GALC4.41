package com.honda.galc.oif.task;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;

public class AutoHoldByProductDemandTypeTask extends OifTask<Object> implements IEventTaskExecutable {

	private static final String PLAN_CODE = "PLAN_CODE";
	private static final String PROCESS_LOCATION = "PROCESS_LOCATION";
	private static final String PRODUCT_TYPE = "PRODUCT_TYPE";
	private static final String HOLD_ACCESS_TYPE = "HOLD_ACCESS_TYPE";
	private static final String HOLD_REASON = "HOLD_REASON";
	private static final String HOLD_USER = "HOLD_USER";
	private static final String HOLD_PHONE = "HOLD_PHONE";
	
	protected OifErrorsCollector errorsCollector;
	
	public AutoHoldByProductDemandTypeTask(String name) {
		super(name);
		errorsCollector = new OifErrorsCollector(name);
	}

	public void execute(Object[] args) {
		try {
			String planCode = getProperty(PLAN_CODE, "");
			String processLocation = getProperty(PROCESS_LOCATION,"");
			String productType = getProperty(PRODUCT_TYPE,"");
			String holdAccessType = getProperty(HOLD_ACCESS_TYPE, "");
			String holdReason = getProperty(HOLD_REASON, "");
			String holdUser = getProperty(HOLD_USER, "");
			String holdPhone = getProperty(HOLD_PHONE, "");
			Timestamp now = super.getCurrentTime(true);
			
			getLogger().info("Started processing Auto Hold By Product Demand Type Task ");
			Timestamp lastRunTimestamp = getLastProcessTimestamp();
			
			//get All non Mass ProductionLots created after lastRunTimestamp
			List<PreProductionLot> nonMassProductionLots  = getProductionLotDao().findAllNonMassProductionLotsByPlanCodeAndCreateDate(planCode, lastRunTimestamp);
			String pattern = "yyyyMMdd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date(now.getTime()));
			String reason = holdReason + " on "+ date;
			
			Qsr qsr = null;
			if(nonMassProductionLots.size() > 0) {
				qsr = createQSR(processLocation, productType,reason,holdAccessType);
			}else {
				getLogger().info("No New Non Mass Production Lots found");
			}
			List<HoldResult> holdResults = new ArrayList<HoldResult>();
			for(PreProductionLot productionLot:nonMassProductionLots) {
				//find All vins for ProductionLot and place on hold
				getLogger().info("Creating Hold Result Records for ProductionLot: "+productionLot.getId() +" of Demand Type: "+productionLot.getDemandType() );
				List<Frame> products = getFrameDao().findAllByProductionLot(productionLot.getId());
				for(Frame product:products) {
					HoldResult holdResult = createHoldResult(product.getId(), HoldResultType.HOLD_AT_SHIPPING.getId(), reason,holdUser,holdPhone);
					getLogger().debug("hold Created :"+holdResult.toString());
					holdResults.add(holdResult);
				}
			}
			if(holdResults.size() > 0) {
				qsr = getQsrDao().holdProducts(ProductType.getType(productType), holdResults, qsr);
			}else {
				getLogger().info(" No Hold Result Records Created");
			}
			if(qsr != null) {
				getLogger().info(holdResults.size() +" Hold Result Records Created");
				getLogger().info("Qsr Created :"+qsr.toString());
			}
			if(errorsCollector.getErrorList().isEmpty()){	
				updateLastProcessTimestamp(now);
			}
			getLogger().info("Finished processing Auto Hold By Product Demand Type Task");
		} catch (Exception e) {
			getLogger()
					.info("Unexpected Exception Occurred  while running the Auto Hold By Product Demand Type Task :"
							+ e.getMessage());
			e.printStackTrace();
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}

	}
	
	private FrameDao getFrameDao() {
		return ServiceFactory.getDao(FrameDao.class);
	}

	private PreProductionLotDao getProductionLotDao() {
		return ServiceFactory.getDao(PreProductionLotDao.class);
	}

	private QsrDao getQsrDao() {
		return ServiceFactory.getDao(QsrDao.class);
	}
	
	private Qsr createQSR(String division, String productType,String holdReason,String holdAccessType) {
		Qsr qsr = new Qsr();
		
		qsr.setProcessLocation(division);
		qsr.setProductType(productType);
		qsr.setDescription(holdReason);
		qsr.setStatus(QsrStatus.ACTIVE.getIntValue());
		qsr.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		qsr.setHoldAccessType(holdAccessType);
				
		return qsr;
	}
	
	private HoldResult createHoldResult(String productId, int holdType, String holdReason,String holdUser,String phone) {
		HoldResult holdResult = new HoldResult();
		holdResult.setId(new HoldResultId(productId, holdType));
		holdResult.setHoldReason(holdReason);
		holdResult.setHoldAssociateNo(holdUser);
		holdResult.setHoldAssociateName(holdUser);
		holdResult.setHoldAssociatePhone(phone);
		holdResult.setReleasePermission(1);
		
		return holdResult;
	}
	private Logger getLogger() {
		return Logger.getLogger(componentId);
	}
	
	    
}