package com.honda.galc.client.datacollection.processor;


import java.awt.Color;
import java.util.Random;

import javax.swing.JOptionPane;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.entity.manager.DailyDepartmentScheduleUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.InspectionSamplingDao;
import com.honda.galc.dao.product.RuleDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.InspectionSamplingType;
import com.honda.galc.entity.enumtype.LotHoldStatus;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.InspectionSampling;
import com.honda.galc.entity.product.InspectionSamplingId;
import com.honda.galc.entity.product.Rule;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.UpdateEntity;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

/*
 * 
 * @author Gangadhararao Gadde 
 * @since Feb 06, 2014
 */
public class AFOffFrameVinProcessor extends FrameVinProcessor {

	private String vin = null;
	public String processPointId = null;
	private Frame frame = null;
	private FrameDao frameDao = null;
	private ProcessPointDao processPointDao = null;
	private FrameSpecDao frameSpecDao = null;
	private InspectionSamplingDao inspectionSamplingDao = null;
	private HoldResultDao holdResultDao = null;
	private RuleDao ruleDao = null;
	private ProductCheckUtil productCheckUtil = null;
	private boolean emissionSampledVin = false;
	private boolean dimensionSampledVin = false;
	private final static String AF_OFF_LABEL_HOLD = "AFOffLabelHold";
	private ProductBean productBean = null;
	private final static String ENTITIES_FOR = "AFOffFrameVinProcessor";
	protected EntityList<AbstractEntity> entityList = null;

	public AFOffFrameVinProcessor(ClientContext context) {
		super(context);
	}

	public void load() {
		processPointId = context.getProcessPointId().trim();
		processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		frameDao = ServiceFactory.getDao(FrameDao.class);
		frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		holdResultDao = ServiceFactory.getDao(HoldResultDao.class);
		inspectionSamplingDao = ServiceFactory.getDao(InspectionSamplingDao.class);
		ruleDao = ServiceFactory.getDao(RuleDao.class);
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
		try {
			load();
		
			boolean isValidProduct = (super.execute(productId));
			
			if (isValidProduct) {
				vin = productId.getProductId();
				entityList = new EntityList<AbstractEntity>(ENTITIES_FOR, vin, "");
				productBean = new ProductBean();
				productBean.setProductId(vin);
				frame = frameDao.findByKey(vin);
				
				productCheckUtil = new ProductCheckUtil();
				productCheckUtil.setProduct(frame);
				productCheckUtil.setProcessPoint(processPointDao.findByKey(processPointId));
				
				Boolean isInspectionSamplingEnabled = PropertyService.getPropertyBean(ProductPropertyBean.class, context.getProcessPointId().trim()).isInspectionSamplingEnabled();
				if (isInspectionSamplingEnabled && getInspectionSamplingStatus() > 0) {
					Logger.getLogger().info("This VIN[" + vin + "] is sampling vehicle");
				} else {
					if ((isLabelHoldSampling())&& productCheckUtil.checkDestinationChange()) {
						createShippingHold("Label Sampling Inspection Vehicle");
						Logger.getLogger().info("This VIN[" + vin + "] is label hold vehicle");
						MessageDialog.showInfo(context.getFrame(),"Label Inspection Sampling Vehicle:\nPlace a Hold Sheet in vehicle and move to QMA.","Dimension and Emission Sampling");
					}
				}

				doBroadcast();

				createActualOffDate();
				
				frame.setEngineStatus(true);
				
				entityList.addEntity(new UpdateEntity(frame, frame.toString(),frameDao));
				
				getController().getState().getProduct().setMasterEntityList(entityList);
				
				Logger.getLogger().info("AFOff Process Completed Successfully");
				
			}
			return isValidProduct;
		} catch (Exception e) {
			logException(e);
			return false;
		}
	}
	

	protected FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class);
	}

	protected ProductCheckPropertyBean getProductCheckPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class,processPointId);
	}

	public void logException(Exception e) {
		e.printStackTrace();
		getController().getFsm().productIdNg(productBean, MESSAGE_ID,"An Exception occured");
		Logger.getLogger().error(e.getMessage());
	}

	private InspectionSampling doEmissionSampling(InspectionSampling inspectionSampling) {
		boolean bEmissionSamplingThresholdReached = false;
		int iEmissionMolecule = inspectionSampling.getEmissionMolecule();
		int iEmissionDenominator = inspectionSampling.getEmissionDenominator();
		if (iEmissionDenominator > 0) {
			boolean vinSelectedForSampling = performInspectionSampling(iEmissionDenominator, iEmissionMolecule);
			iEmissionDenominator--;
			if (vinSelectedForSampling) {
				iEmissionMolecule--;
				emissionSampledVin = true;
				bEmissionSamplingThresholdReached = (iEmissionMolecule == 0);
				if (bEmissionSamplingThresholdReached) {
					iEmissionDenominator = 0;
					inspectionSampling.setEmissionDenominatorMaster(0);
					inspectionSampling.setEmissionMoleculeMaster(0);
				}
			} else {
				emissionSampledVin = false;
			}
		}
		inspectionSampling.setEmissionDenominator(iEmissionDenominator);
		inspectionSampling.setEmissionMolecule(iEmissionMolecule);
		return inspectionSampling;
	}

	private InspectionSampling doDimensionSampling(InspectionSampling inspectionSampling) {
		boolean bDimensionSamplingThresholdReached = false;
		int iDimensionMolecule = inspectionSampling.getDimensionMolecule();
		int iDimensionDenominator = inspectionSampling.getDimensionDenominator();
		if (iDimensionDenominator > 0) {
			boolean vinSelectedForSampling = performInspectionSampling(iDimensionDenominator, iDimensionMolecule);
			iDimensionDenominator--;
			if (vinSelectedForSampling) {
				iDimensionMolecule--;
				dimensionSampledVin = true;
				bDimensionSamplingThresholdReached = (iDimensionMolecule == 0);
				if (bDimensionSamplingThresholdReached) {
					iDimensionDenominator = 0;
					inspectionSampling.setDimensionDenominatorMaster(0);
					inspectionSampling.setDimensionMoleculeMaster(0);
				}
			} else {
				dimensionSampledVin = false;
			}
		}
		inspectionSampling.setDimensionDenominator(iDimensionDenominator);
		inspectionSampling.setDimensionMolecule(iDimensionMolecule);
		return inspectionSampling;
	}

	private int getInspectionSamplingStatus() {
		int samplingStatusValue = InspectionSamplingType.SAMPLING_NOTHING.getId();
		try {
			InspectionSampling inspectionSampling = null;
			FrameSpec frameSpec = frameSpecDao.findByKey(frame.getProductSpecCode());
			String strModelCode = frameSpec.getModelCode();
			String strModelTypeCode = frameSpec.getModelTypeCode();
			inspectionSampling = inspectionSamplingDao.findByKey(new InspectionSamplingId(strModelCode,strModelTypeCode));
			if (inspectionSampling == null)
				return samplingStatusValue;
			inspectionSampling = doEmissionSampling(inspectionSampling);
			inspectionSampling = doDimensionSampling(inspectionSampling);
			entityList.addEntity(new UpdateEntity(inspectionSampling,inspectionSampling.toString(), inspectionSamplingDao));
			if ((emissionSampledVin) && (dimensionSampledVin)) {
				createShippingHold("This vehicle is Emission and Dimension Sampling");
				samplingStatusValue = InspectionSamplingType.SAMPLING_BOTH.getId();
				emissionSampledVin = false;
				dimensionSampledVin = false;
				displaySamplingMsgDialog(samplingStatusValue,Color.LIGHT_GRAY);
			} else {
				if (emissionSampledVin) {
					createShippingHold("This vehicle is Emission  Sampling");
					samplingStatusValue = InspectionSamplingType.SAMPLING_EMISSION.getId();
					emissionSampledVin = false;
					displaySamplingMsgDialog(samplingStatusValue,Color.YELLOW);
				} else if (dimensionSampledVin) {
					createShippingHold("This vehicle is Dimension Sampling");
					samplingStatusValue = InspectionSamplingType.SAMPLING_DIMENSION.getId();
					dimensionSampledVin = false;
					displaySamplingMsgDialog(samplingStatusValue,Color.CYAN);
				}
			}
			
		} catch (Exception e) {
			logException(e);
		}
		return samplingStatusValue;
	}
	
	private void displaySamplingMsgDialog(int samplingStatusValue,Color color){
		LotControlAudioManager.getInstance().playWarnSound();
		Logger.getLogger().check("VIN : " +vin+ ", Assigned for " + InspectionSamplingType.getType(samplingStatusValue).getTypeString() );
		MessageDialog.showColoredMessageDialog(context.getFrame(), InspectionSamplingType.getType(samplingStatusValue).getTypeString() + " Vehicle:\nRoute car to "+InspectionSamplingType.getType(samplingStatusValue).getTypeString()+" area following appropriate procedure.","Sampling VIN",JOptionPane.INFORMATION_MESSAGE,color);
		Logger.getLogger().info(vin+ " " + InspectionSamplingType.getType(samplingStatusValue).getTypeString() + ": Please follow the appropriate procedure.");
	}

	private void createShippingHold(String aReason) {
		try {
			HoldResult holdResult = new HoldResult();
			holdResult.setId(new HoldResultId(vin,HoldResultType.HOLD_AT_SHIPPING.getId()));
			holdResult.setProductionLot(frame.getProductionLot());
			holdResult.setProductSpecCode(frame.getProductSpecCode());
			holdResult.setHoldReason(aReason);
			holdResult.setLotHoldStatus(LotHoldStatus.LOT_NOT_ON_HOLD.getId());
			entityList.addEntity(new UpdateEntity(holdResult, holdResult.toString(), holdResultDao));
		} catch (Exception e) {
			logException(e);
		}
	}

	private boolean isLabelHoldSampling() {
		try {
			Rule rule = ruleDao.findByKey(AF_OFF_LABEL_HOLD);
			if (rule != null) {
				if (rule.getActiveState() == 1)
					return true;
			}
			return false;
		} catch (Exception e) {
			logException(e);
			return false;
		}
	}

	private void createActualOffDate() {
		try {
			DailyDepartmentScheduleUtil dailyDepartmentScheduleUtil = new DailyDepartmentScheduleUtil(processPointDao.findByKey(processPointId));
			DailyDepartmentSchedule schedule = dailyDepartmentScheduleUtil.getCurrentScheduleByTimestamp();
			if (schedule != null)
				frame.setActualOffDate(schedule.getId().getProductionDate());
			else {
				logErrorMessage("Failed to obtain schedule for today.Set Current date as Actual OFF Date");
				frame.setActualOffDate(new java.util.Date(System.currentTimeMillis()));
			}

		} catch (Exception e) {
			logException(e);
		}
	}

	public void logErrorMessage(String message) {
		Logger.getLogger().error(message);
		getController().getFsm().error(new Message(message));
	}

	public boolean performInspectionSampling(int groupSize, int numToSample) {
		return (new Random().nextInt(groupSize) < numToSample);
	}

	private void doBroadcast() {
		Boolean useBroadcastCheckPoint = PropertyService.getPropertyBean(ApplicationPropertyBean.class, context.getProcessPointId().trim()).isUseBroadcastCheckPoint();
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, vin);
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		BroadcastService broadcastService =ServiceFactory.getService(BroadcastService.class);
		if(useBroadcastCheckPoint == true) {
			broadcastService.broadcast(processPointId, dc, CheckPoints.BEFORE_PRODUCT_PROCESSED);
		} else {
			broadcastService.broadcast(processPointId, dc);
		}
		
	}
}