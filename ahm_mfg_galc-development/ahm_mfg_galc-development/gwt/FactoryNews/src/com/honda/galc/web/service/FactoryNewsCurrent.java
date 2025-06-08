/**
 * 
 */
package com.honda.galc.web.service;

import java.io.Serializable;

/**
 * @author Subu Kathiresan
 * @date June 04, 2012
 *
 */
public class FactoryNewsCurrent implements Serializable {
	
	private static final long serialVersionUID = -119904633447935511L;
	
	public String divisionName = "";
	public String lineName = "";
	public int plan = -1;
	public int target = -1;
	public int actual1st = -1;
	public int actual2nd = -1;
	public int actual3rd = -1;
	public int actualTotal = -1;
	public int difference = -1;
	public int currentInventory = -1;
	public String nextLineName = "";
}
