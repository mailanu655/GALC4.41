package com.honda.galc.client.ui.event;

/**
 * @author Subu Kathiresan
 * @date April 21, 2014
 */
public enum UnitNavigatorEventType {
	SELECTED    		(0, "SELECTED"),
	MOVETO 				(1, "MOVETO"),
	NEXT				(2, "NEXT"),
	PREVIOUS			(3, "PREVIOUS"),
	PREPARE_FOR_MOVE 	(4, "PREPARE_FOR_MOVE"), 
	CANCEL_MOVE   		(5, "CANCEL_MOVE");

	private int id = 0;
	private String name = "";

	private UnitNavigatorEventType(int id, String name){
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}

