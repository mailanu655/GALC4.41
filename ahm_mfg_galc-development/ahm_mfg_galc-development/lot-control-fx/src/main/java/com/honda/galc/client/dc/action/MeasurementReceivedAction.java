package com.	honda.galc.client.dc.action;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.checkers.AbstractBaseChecker;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.CheckResultsEvaluator;
import com.honda.galc.checkers.CheckerUtil;
import com.honda.galc.checkers.MeasurementSequenceNumberChecker;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.checkers.TorqueMeasurementChecker;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class MeasurementReceivedAction extends BaseDataCollectionAction<MeasurementInputData> {

	private static String checkPointName = CheckPoints.AFTER_MEASUREMENT_INPUT.toString();
	
	public MeasurementReceivedAction() {
		CheckPointsRegistry.getInstance().register(this, checkPointName);
	}
	
	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		
		if (model.getGoodMeasurementsCount(event.getOperation().getId().getOperationName()) 
				== event.getOperation().getSelectedPart().getMeasurements().size()) {
			getLogger().warn("Unexpected Measurement received: " + event.getInputData().toString());
			return;
		}
		
		removeFromSkippedMeasurementsList(model, event);
		processMeasurement(event);
		model.getOpProcessors().get(event.getOperation().getId().getOperationName()).execute(event.getInputData());
	}

	public void removeFromSkippedMeasurementsList(DataCollectionModel model, DataCollectionEvent event) {
		MeasurementInputData measurement = (MeasurementInputData) event.getInputData();
		ArrayList<Integer> skippedMeasurements = model.getSkippedMeasurementsMap().get(event.getOperation().getId().getOperationName());
		if (skippedMeasurements != null && skippedMeasurements.contains(measurement.getMeasurementIndex())) {
			skippedMeasurements.remove(skippedMeasurements.indexOf(measurement.getMeasurementIndex()));
		}
	}
	
	public String getCheckPointName() {
		return checkPointName;
	}

	public boolean executeCheckers(MeasurementInputData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>(); 
		SortedArrayList<MCMeasurementChecker> measCheckers = CheckerUtil.getMeasurementCheckers(getOperation(), getCheckPointName());
		if (measCheckers.size() == 0) {
			addDefaultMeasurementCheckers(measCheckers);
		}

		for (MCMeasurementChecker measChecker: measCheckers) {
			getLogger().info("Executing checker: " + measChecker.getCheckName());
			AbstractBaseChecker<MeasurementInputData> checker = CheckerUtil.createChecker(measChecker.getChecker(), MeasurementInputData.class);
			checker.setReactionType(measChecker.getReactionType());
			checker.setOperation(getOperation());
			checker.setMeasurement(getModel().getCurrentMeasurement(inputData.getMeasurementIndex()-1));
			List<CheckResult> ckResults = checker.executeCheck(inputData);
			checkResults.addAll(ckResults);
			dispatchReactions(ckResults, inputData);
		}

		getLogger().info("Check Results size: " + checkResults.size());
		return CheckResultsEvaluator.evaluate(checkResults);
	}
	
	private void addMeasurementSequenceNumberChecker(SortedArrayList<MCMeasurementChecker> measCheckers) {
		//Adding measurement sequence number checker
		MCMeasurementCheckerId seqChkId = new MCMeasurementCheckerId();
		seqChkId.setCheckSeq(measCheckers.size());
		seqChkId.setCheckName("MEASUREMENT_SEQ_NO_CHECK");
		MCMeasurementChecker sequenceNumberChecker = new MCMeasurementChecker();
		sequenceNumberChecker.setId(seqChkId);
		sequenceNumberChecker.setChecker(MeasurementSequenceNumberChecker.class.getCanonicalName());
		measCheckers.add(sequenceNumberChecker);
	}

	private void addDefaultMeasurementCheckers(SortedArrayList<MCMeasurementChecker> measCheckers) {
		//Measurement sequence number checker
		addMeasurementSequenceNumberChecker(measCheckers);
		
		//Measurement limit checker
		MCMeasurementCheckerId id = new MCMeasurementCheckerId();
		id.setCheckSeq(measCheckers.size());
		id.setCheckName("MEASUREMENT_CHECK_LIMIT");
		MCMeasurementChecker torqueLimitChecker = new MCMeasurementChecker();
		torqueLimitChecker.setId(id);
		torqueLimitChecker.setChecker(TorqueMeasurementChecker.class.getCanonicalName());
		measCheckers.add(torqueLimitChecker);
	}
	
	public boolean dispatchReactions(List<CheckResult> checkResults, MeasurementInputData measurementData) {
		
		for (CheckResult checkResult: checkResults) {
			if (checkResult.getReactionType().equals(ReactionType.DISPLAY_ERR_MSG)) {

				
				notifyMeasurementNg(measurementData, getOperation(), checkResult.getCheckMessage());
				EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_NG, getOperation(), measurementData));

			}
		}
		return true;
	}
	
	public void processMeasurement(DataCollectionEvent event) {
		EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_OK, event.getOperation(), event.getInputData()));
	}
}
