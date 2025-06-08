package com.honda.galc.checkers;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public class CheckResult {

	 private boolean result;
	 private ReactionType reactionType;
	 private String checkMessage;
	 private int checkSequence;
	 private String checkName;

	 public CheckResult() {}
	 

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}


	public ReactionType getReactionType() {
		return reactionType;
	}

	public void setReactionType(ReactionType reactionType) {
		this.reactionType = reactionType;
	}

	public String getCheckMessage() {
		return checkMessage;
	}

	public void setCheckMessage(String checkMessage) {
		this.checkMessage = checkMessage;
	}

	public int getCheckSequence() {
		return checkSequence;
	}

	public void setCheckSequence(int checkSequence) {
		this.checkSequence = checkSequence;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
}
