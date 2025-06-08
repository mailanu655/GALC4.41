package com.honda.galc.checkers;

import java.util.List;

import com.honda.galc.device.dataformat.InputData;

/**
 * @author Subu Kathiresan
 * @date Oct 1, 2014
 */
public interface ICheckPoint<T extends InputData> {
	
	public boolean executeCheckers(T inputData);
	
	public boolean dispatchReactions(List<CheckResult> checkResults, T inputData);

	public String getCheckPointName();
}
