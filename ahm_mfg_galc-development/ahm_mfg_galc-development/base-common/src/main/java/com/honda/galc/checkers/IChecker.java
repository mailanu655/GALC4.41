package com.honda.galc.checkers;

import java.util.List;

import com.honda.galc.device.dataformat.InputData;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public interface IChecker<T extends InputData> {
	 
	public String getName();
	
	public CheckerType getType();
	
	public int getSequence();
	
	public List<CheckResult> executeCheck(T inputData);
}
