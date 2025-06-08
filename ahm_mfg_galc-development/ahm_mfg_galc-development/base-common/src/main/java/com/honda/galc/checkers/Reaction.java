package com.honda.galc.checkers;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public class Reaction {

	private String name;
	
	private ReactionDispatcher dispatcher;
	
	private ReactionType type;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReactionDispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(ReactionDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public ReactionType getType() {
		return type;
	}

	public void setType(ReactionType type) {
		this.type = type;
	}
}
