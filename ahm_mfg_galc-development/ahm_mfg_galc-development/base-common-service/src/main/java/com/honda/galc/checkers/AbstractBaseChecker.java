package com.honda.galc.checkers;

import java.util.List;

import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * @author Subu Kathiresan
 * @date Dec 9, 2014
 */
public abstract class AbstractBaseChecker<T extends InputData> implements IChecker<T> {

	private MCOperationRevision operation;
	private MCOperationPartRevision part;
	private MCOperationMeasurement measurement;
	private ReactionType reactionType;
	protected T inputData;
	
	public abstract String getName();
	public abstract CheckerType getType();
	public abstract int getSequence();
	public abstract List<CheckResult> executeCheck(T inputData);
	
	public MCOperationRevision getOperation() {
		return operation;
	}
	
	public void setOperation(MCOperationRevision operation) {
		this.operation = operation;
	}
	
	public ReactionType getReactionType() {
		if (reactionType == null) {
			reactionType = ReactionType.DISPLAY_ERR_MSG;
		}
		return reactionType;
	}

	public void setReactionType(ReactionType reactionType) {
		this.reactionType = reactionType;
	}

	public MCOperationPartRevision getPart() {
		return part;
	}

	public void setPart(MCOperationPartRevision part) {
		this.part = part;
	}

	public MCOperationMeasurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(MCOperationMeasurement measurement) {
		this.measurement = measurement;
	}
	
	public CheckResult createCheckResult(String message) {
		CheckResult checkResult = new CheckResult();
		checkResult.setCheckMessage(message);
		checkResult.setReactionType(getReactionType());		
		return checkResult;
	}
}
