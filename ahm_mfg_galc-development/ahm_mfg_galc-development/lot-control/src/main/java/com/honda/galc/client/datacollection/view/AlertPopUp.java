/**
 * 
 */
package com.honda.galc.client.datacollection.view;

/**
 * @author Subu Kathiresan
 * @date Jan 16, 2012
 */
public enum AlertPopUp {
	
	notShown(1),
	shown(2),
	overridden(3);
	
	int _val = 1;
	
	private AlertPopUp(int val) {
		_val = val;
	}
	
	public int getValue() {
		return _val;
	}
}
