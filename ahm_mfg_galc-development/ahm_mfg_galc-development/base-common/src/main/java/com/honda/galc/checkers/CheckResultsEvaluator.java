package com.honda.galc.checkers;

import java.util.List;

/**
 * @author Subu Kathiresan
 * @date Oct 2, 2014
 */
public class CheckResultsEvaluator {

	public static boolean evaluate(List<CheckResult> checkResults) {
		for (CheckResult checkResult: checkResults) {
			if (!checkResult.isResult())
				return false;
		}
		
		return true;
	}
}
