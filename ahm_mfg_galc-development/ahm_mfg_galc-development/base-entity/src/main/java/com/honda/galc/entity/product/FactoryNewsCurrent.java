/**
 * 
 */
package com.honda.galc.entity.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;


/**
 * @author Subu Kathiresan
 * @Date Jun 7, 2012
 *
 */
public class FactoryNewsCurrent implements Serializable {

	private static final long serialVersionUID = -3792788815711563594L;
	
	private String lineName = "";
	private String lineId = "";
	private String divisionName = "";
	private String divisionId = "";
	private String nextLineName = "";
	private int currentInventory = 0;
	private int sequenceNumber = 0;
	private int plan = 0;
	private int target = 0;
	private int actual1st = 0;
	private int actual2nd = 0;
	private int actual3rd = 0;
	private int actualTotal = 0;
	private int difference = 0;
	
	@Transient
	private Date asOfDate = null;
	
	public String getLineName() {
		return lineName;
	}
	
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	public String getLineId() {
		return lineId;
	}
	
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	public String getDivisionName() {
		return divisionName;
	}
	
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	public String getDivisionId() {
		return divisionId;
	}
	
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	public String getNextLineName() {
		return nextLineName;
	}

	public void setNextLineName(String nextLineName) {
		this.nextLineName = nextLineName;
	}
	
	public int getCurrentInventory() {
		return currentInventory;
	}
	
	public void setCurrentInventory(int currentInventory) {
		this.currentInventory = currentInventory;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void setSequenceNumber(int seq) {
		this.sequenceNumber = seq;
	}
	
	public int getPlan() {
		return plan;
	}
	
	public void setPlan(int plan) {
		this.plan = plan;
	}
	
	public int getTarget() {
		return target;
	}
	
	public void setTarget(int target) {
		this.target = target;
	}
	
	public int getActual1st() {
		return actual1st;
	}
	
	public void setActual1st(int actual1st) {
		this.actual1st = actual1st;
	}
	
	public int getActual2nd() {
		return actual2nd;
	}
	
	public void setActual2nd(int actual2nd) {
		this.actual2nd = actual2nd;
	}
	
	public int getActual3rd() {
		return actual3rd;
	}
	
	public void setActual3rd(int actual3rd) {
		this.actual3rd = actual3rd;
	}
	
	public int getActualTotal() {
		return actualTotal;
	}
	
	public void setActualTotal(int actualTotal) {
		this.actualTotal = actualTotal;
	}
	
	public int getDifference() {
		return difference;
	}
	
	public void setDifference(int difference) {
		this.difference = difference;
	}
	
	public Date getAsOfDate() {
		return asOfDate;
	}
	
	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}
}
