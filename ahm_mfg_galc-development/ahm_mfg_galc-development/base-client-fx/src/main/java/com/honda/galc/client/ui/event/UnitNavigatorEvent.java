package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.ProcessEvent;

/**
 * @author Suriya Sena
 * @date May 21, 2014
 */
public class UnitNavigatorEvent extends ProcessEvent   {
	
	private UnitNavigatorEventType type;
	private int index = -1;
	private boolean isLocateByIndex;
	private int opTime = 0;
	private int totalTimeForCompletedUnits = 0;
		
	public UnitNavigatorEvent(UnitNavigatorEventType type) {
		this.type = type;
	}
	
	public UnitNavigatorEvent(UnitNavigatorEventType type, int index) {
		this.type = type;
		this.index =  index;
        isLocateByIndex = (index >= 0 ? true : false ); 
	}
	
	
	public UnitNavigatorEvent(UnitNavigatorEventType type, int index, int opTime) {
		super();
		this.type = type;
		this.index = index;
		this.isLocateByIndex = (index >= 0 ? true : false ); 
		this.opTime = opTime;
	}
	
	

	public UnitNavigatorEvent(UnitNavigatorEventType type, int index,
			int opTime, int totalTimeForCompletedUnits) {
		super();
		this.type = type;
		this.index = index;
		this.isLocateByIndex = (index >= 0 ? true : false ); 
		this.opTime = opTime;
		this.totalTimeForCompletedUnits = totalTimeForCompletedUnits;
	}

	public int getTotalTimeForCompletedUnits() {
		return totalTimeForCompletedUnits;
	}

	public void setTotalTimeForCompletedUnits(int totalTimeForCompletedUnits) {
		this.totalTimeForCompletedUnits = totalTimeForCompletedUnits;
	}

	public UnitNavigatorEventType getType() {
		return type;
	}
	
	public int  getIndex() {
		return index;
	}

	public boolean isLocateByIndex() {
		return isLocateByIndex;
	}

	public int getOpTime() {
		return opTime;
	}

	public void setOpTime(int opTime) {
		this.opTime = opTime;
	}

	public String toString() {
		return String.format(" Type %s, index %d", type.toString(),index );
	}
}
