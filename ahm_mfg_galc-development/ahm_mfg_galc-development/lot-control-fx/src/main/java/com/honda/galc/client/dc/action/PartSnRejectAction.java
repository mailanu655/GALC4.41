package com.honda.galc.client.dc.action;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.checkers.AbstractBaseChecker;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.CheckerUtil;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.property.BearingPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.dao.conf.MCPartCheckerDao;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class PartSnRejectAction extends BaseDataCollectionAction<PartSerialScanData> {
	private static String checkPointName = CheckPoints.AFTER_PART_SN_TRASHED.toString();
	
	boolean isCompleted = true;
	
	public PartSnRejectAction(){
		CheckPointsRegistry.getInstance().register(this, checkPointName);
	}
	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		getLogger().check("Rejecting Part");
		if(isCompleted) {
			rejectPart(model.getInstalledPartsMap().get(getOperation().getId().getOperationName()));
			removeFromCompleteOpsMap(event);
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.REJECT_PART_SCAN_RECEIVED, event.getOperation(), event.getInputData()));
		} 
	}
	
	public String getCheckPointName() {
		return checkPointName;
	}
	
	public boolean executeCheckers(PartSerialScanData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>(); 
		List<MCPartChecker> partCheckers = ServiceFactory.getService(MCPartCheckerDao.class).findAllByDetails(getOperation().getId().getOperationName(), checkPointName, getOperation().getId().getOperationRevision());
		
		for (MCPartChecker mcPartChecker : partCheckers) {
			BearingPropertyBean bearingPropertyBean = PropertyService.getPropertyBean(
					BearingPropertyBean.class, getModel().getProductModel().getApplicationContext().getProcessPointId());
			AbstractBaseChecker<PartSerialScanData> checker = CheckerUtil.createChecker(mcPartChecker.getChecker(), PartSerialScanData.class);
			((PartSerialScanData)inputData).setBearingPickOperation(bearingPropertyBean.getBearingPickOperations());
			((PartSerialScanData)inputData).setStrucRev(getOperation().getStructure().getId().getRevision());
			((PartSerialScanData)inputData).setPartName(getOperation().getId().getOperationName());
			((PartSerialScanData)inputData).setProductId(getModel().getProductModel().getProductId());
			checkResults.addAll(checker.executeCheck(inputData));
		}
		if(checkResults!=null && checkResults.size()>0)  {
			isCompleted = false;
			notifyPartSnReject(null, getOperation(), checkResults.get(0).getCheckMessage());
		}

		return isCompleted;
	}


	public boolean dispatchReactions(List<CheckResult> checkResults, PartSerialScanData inputData) {
		return false;
	}
	
	public void notifyPartSnReject(InputData data, MCOperationRevision operation, String warningMessage) {
		DataCollectionResultEventType evType = DataCollectionResultEventType.INVALID_PART_SCAN_RECEIVED;
		evType.setMessage(warningMessage);
		EventBusUtil.publish(new DataCollectionResultEvent(evType, operation, data));
	}
}