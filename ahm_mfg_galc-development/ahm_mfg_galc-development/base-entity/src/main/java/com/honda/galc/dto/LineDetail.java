package com.honda.galc.dto;

import java.io.Serializable;


/**
 * @author Subu Kathiresan
 * @Date Jun 7, 2012
 *
 */
public class LineDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String productId = "";
	private String nextProductId = "";
	private String productSpecCode = "";
	private String productionLotKd = "";
	private String nextProductionLotKd = "";
	private String processPointName = "";
	private String processPointId = "";
	private int processSequence = 0;
	private int sequenceNumber = 0;
	private int afOnSequenceNumber = 0;
	private int nextAfOnSequenceNumber = 0;
	private int nonRepairedDefects = 0;
	private int missingInstalledParts = 0;
	private int badInstalledParts = 0;
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getNextProductId() {
		return nextProductId;
	}

	public void setNextProductId(String nextProductId) {
		this.nextProductId = nextProductId;
	}
	
	public String getProductionLotKd() {
		return productionLotKd;
	}

	public void setProductionLotKd(String productionLotKd) {
		this.productionLotKd = productionLotKd;
	}
	
	public String getNextProductionLotKd() {
		return nextProductionLotKd;
	}

	public void setNextProductionLotKd(String nextProductionLotKd) {
		this.nextProductionLotKd = nextProductionLotKd;
	}
	
	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void setSequenceNumber(int seq) {
		this.sequenceNumber = seq;
	}
	
	public int getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}
	
	public void setAfOnSequenceNumber(int afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}
	
	public int getNextAfOnSequenceNumber() {
		return nextAfOnSequenceNumber;
	}
	
	public void setNextAfOnSequenceNumber(int nextAfOnSequenceNumber) {
		this.nextAfOnSequenceNumber = nextAfOnSequenceNumber;
	}
	
	public int getNonRepairedDefects() {
		return nonRepairedDefects;
	}
	
	public void setNonRepairedDefects(int nonRepairedDefects) {
		this.nonRepairedDefects = nonRepairedDefects;
	}
	
	public int getMissingInstalledParts() {
		return missingInstalledParts;
	}
	
	public void setMissingInstalledParts(int missingInstalledParts) {
		this.missingInstalledParts = missingInstalledParts;
	}
	
	public int getBadInstalledParts() {
		return badInstalledParts;
	}
	
	public void setBadInstalledParts(int badInstalledParts) {
		this.badInstalledParts = badInstalledParts;
	}
	
	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
	
	public int getProcessSequence() {
		return processSequence;
	}

	public void setProcessSequence(int processSequence) {
		this.processSequence = processSequence;
	}
	
	public String getProcessPointId() {
		return processPointId;
	}
	
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
}
