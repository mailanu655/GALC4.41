package com.honda.galc.client.dc.action;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.AbstractBaseChecker;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.CheckResultsEvaluator;
import com.honda.galc.checkers.CheckerUtil;
import com.honda.galc.checkers.PartMaskChecker;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.client.checkers.reactions.UserAuthorizationCheckerReaction;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.view.LotControlOperationView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.OperationType;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class PartSnReceivedAction extends BaseDataCollectionAction<PartSerialScanData> {
	
	private static String checkPointName = CheckPoints.AFTER_PART_SCAN.toString();

	private boolean partSnOk = true;
	private List<String> warningMessage = new ArrayList<String>();
	
	public PartSnReceivedAction() {
		CheckPointsRegistry.getInstance().register(this, checkPointName);
	}

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		savePart(event);
		model.getOpProcessors().get(event.getOperation().getId().getOperationName()).execute(event.getInputData());
	}
	
	public String getCheckPointName() {
		return checkPointName;
	}

	public boolean executeCheckers(PartSerialScanData inputData) {
		warningMessage.clear();
		List<CheckResult> checkResults = new ArrayList<CheckResult>(); 
		List partMskLst=new ArrayList<String>();
		
		for (MCOperationPartRevision part : getOperation().getManufacturingMFGPartList()) {
			if(part.getPartMask()!=null){
			
			partMskLst.add(part.getPartMask());
			SortedArrayList<MCPartChecker> partCheckers = CheckerUtil.getPartCheckers(getOperation(), getCheckPointName(),part.getId().getPartId());
		
		if (part.getPartCheck().equals(PartCheck.DEFAULT)) {
			addDefaultPartCheckers(partCheckers);
		}
		
		for (MCPartChecker partChecker: partCheckers) {
			getLogger().info("Executing checker: " + partChecker.getCheckName());
			AbstractBaseChecker<PartSerialScanData> checker = CheckerUtil.createChecker(partChecker.getChecker(), PartSerialScanData.class);
			checker.setOperation(getOperation());
			checker.setReactionType(partChecker.getReactionType());
			((PartSerialScanData)inputData).setPartName(part.getId().getOperationName());
			((PartSerialScanData)inputData).setMask(part.getPartMask());
			((PartSerialScanData)inputData).setModelCode(getModel().getProductModel().getProduct().getModelCode());
			((PartSerialScanData)inputData).setProductSpecCode(getModel().getProductModel().getProduct().getProductSpecCode());
			List<CheckResult> ckResults = checker.executeCheck(inputData);
			checkResults.addAll(ckResults);
			dispatchReactions(ckResults, inputData);
		}
		if(warningMessage.size()>0 && getOperation().getManufacturingMFGPartList().size()>1){
			warningMessage.clear();
			
		}
		else if(warningMessage.size()<=0){
			if(getOperation().getType().equals(OperationType.GALC_SCAN)){
				getOperation().setSelectedPart(part);
				partSnOk=true;
				checkResults.clear();
				
				break;	
			}else if((getOperation().getType().equals(OperationType.GALC_SCAN_WITH_MEAS))
					||(getOperation().getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL))){
				getOperation().setSelectedPart(part);
				partSnOk=true;
				checkResults.clear();
				if(getOperation().getManufacturingMFGPartList().size()>=1){
				LotControlOperationView lv=(LotControlOperationView) getView();
				BorderPane bp=(BorderPane) lv.getCenter();
				VBox vb=(VBox) bp.getTop();
				for(int i=2;i<vb.getChildren().size();i++){
					if(vb.getChildren().get(i) instanceof HBox){
						vb.getChildren().remove(i);
					}
				}
				int inputchild=lv.getInputFields().size();
				for(int i=1;i<inputchild;i++){
					lv.getInputFields().remove(1);
				}
				lv.addMeasurementBoxes(vb);
				lv.setFocusToExpectedInputField();
				}
				break;
				
			}
			
			
			else{
			if(part.getMeasurements().size()>0){
				getOperation().setSelectedPart(part);
				partSnOk=true;
				checkResults.clear();
				
				break;	
			}else{
				String wMessage = "No measurements set for the selected part";
				
				notifyMeasurementNg(null, getOperation(), wMessage);
				return false;
			}
		}
			
			
		}
		}
	}
		
		if (partSnOk==false) {
			if(getOperation().getManufacturingMFGPartList().size()==1){
				String wMessage = StringUtils.join(warningMessage, ", ");
				notifyPartSnNg((PartSerialScanData) inputData, getOperation(), wMessage);
			}else{
				String wMessage = "No Parts Matched";
				
				notifyPartSnNg((PartSerialScanData) inputData, getOperation(), wMessage);
				
			}
			
		}
		else if((warningMessage.size() == 0 )&& (partMskLst.size()==0)){
			String wMessage = "No Parts Matched";
			notifyPartSnNg(null, getOperation(), wMessage);
			return false;
		}
		
		
		
		getLogger().info("Check Results size: " + checkResults.size());
		return CheckResultsEvaluator.evaluate(checkResults);
	}
	
	
	private void addDefaultPartCheckers(SortedArrayList<MCPartChecker> partCheckers) {
		
		//TODO refactor to load defualt checkers from the db?
		MCPartCheckerId id = new MCPartCheckerId();
		id.setCheckSeq(partCheckers.size());
		MCPartChecker partMaskChecker = new MCPartChecker();
		partMaskChecker.setId(id);
		partMaskChecker.setCheckName("PART_MASK_CHECK");
		partMaskChecker.setChecker(PartMaskChecker.class.getCanonicalName());
		partCheckers.add(partMaskChecker);
	}

	
	public boolean dispatchReactions(List<CheckResult> checkResults, PartSerialScanData inputData) {

		for (CheckResult checkResult: checkResults) {
			if (checkResult.getReactionType().equals(ReactionType.DISPLAY_ERR_MSG)) {
				warningMessage.add(checkResult.getCheckMessage());
				partSnOk = false;
			}else if(checkResult.getReactionType().equals(ReactionType.USER_CONFIRMATION)){
				boolean isFirstError = warningMessage.isEmpty();
				String msg = checkResult.getCheckMessage();
				Logger.getLogger().error(msg);
				warningMessage.add(msg);
				partSnOk = false;
				if (isFirstError) {
					String overrideUser = UserAuthorizationCheckerReaction.displayPopUp(false, msg,
							checkResults.get(0).getCheckName());
					if (StringUtils.isNotEmpty(overrideUser)) {
						//set override user name to save it to DB
						((PartSerialScanData) inputData).setOverrrideUser(overrideUser);
						partSnOk = true;
						warningMessage.clear();
					} 
				}
				
				
			}
		}
		return true;
	}
	
	
	public void savePart(DataCollectionEvent event) {
		if (partSnOk) {
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_OK, event.getOperation(), event.getInputData()));
		} else {
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_NG, event.getOperation(), event.getInputData()));
		}
	}

	

	
}