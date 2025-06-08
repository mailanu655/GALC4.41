package com.honda.galc.checkers;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public enum ReactionType {
	
	DISPLAY_WARNING_MSG,
	
	// Need authorization
	USER_CONFIRMATION,
	DISPLAY_MSG_WITH_CONFIRMATION,
	AUTHORIZE_BY_LOGIN,
	
	// Abort process
	DISPLAY_ERR_MSG,
	REDO_PROCESS,
	RETRY_PROCESS,
	GOTO_NEXT_PRODUCT,
	DISPLAY_ERR_WITH_CONFIRMATION, UNEXPECTED_PRODUCT;
	
	public static ReactionType getReactionType(String reactionId) {
		return ReactionType.valueOf(reactionId);
	}
}
