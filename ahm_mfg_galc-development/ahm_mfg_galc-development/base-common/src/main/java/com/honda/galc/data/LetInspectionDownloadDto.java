package com.honda.galc.data;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 25, 2013
 */

public class LetInspectionDownloadDto implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String useFrame;
	private String frameNo;
	private String productId;
	private String lineNo;
	private String modelYearCode;
	private String modelCode;
	private String modelOptionCode;
	private String modelTypeCode;
	private GregorianCalendar startDate;
	private GregorianCalendar endDate;
	private GregorianCalendar proStartDate;
	private GregorianCalendar proEndDate;
	private String production;
	private String programName;
	private Integer programId;
	private String endTimestampStartDate;
	private String endTimestampEndDate;
	
	
	public LetInspectionDownloadDto() {
		super();
	}
	
	public LetInspectionDownloadDto(String useFrame, String frameNo,
			String productId, String lineNo, String modelYearCode,
			String modelCode, String modelOptionCode, String modelTypeCode,
			GregorianCalendar startDate, GregorianCalendar endDate,
			GregorianCalendar proStartDate, GregorianCalendar proEndDate,
			String production, String programName, Integer programId,
			String endTimestampStartDate, String endTimestampEndDate) {
		super();
		this.useFrame = useFrame;
		this.frameNo = frameNo;
		this.productId = productId;
		this.lineNo = lineNo;
		this.modelYearCode = modelYearCode;
		this.modelCode = modelCode;
		this.modelOptionCode = modelOptionCode;
		this.modelTypeCode = modelTypeCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.proStartDate = proStartDate;
		this.proEndDate = proEndDate;
		this.production = production;
		this.programName = programName;
		this.programId = programId;
		this.endTimestampStartDate = endTimestampStartDate;
		this.endTimestampEndDate = endTimestampEndDate;
	}

	public String getUseFrame() {
		return useFrame;
	}
	public void setUseFrame(String useFrame) {
		this.useFrame = useFrame;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getModelYearCode() {
		return modelYearCode;
	}
	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelOptionCode() {
		return modelOptionCode;
	}
	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}
	public String getModelTypeCode() {
		return modelTypeCode;
	}
	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}
	public GregorianCalendar getStartDate() {
		return startDate;
	}
	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}
	public GregorianCalendar getProStartDate() {
		return proStartDate;
	}
	public void setProStartDate(GregorianCalendar proStartDate) {
		this.proStartDate = proStartDate;
	}
	public GregorianCalendar getProEndDate() {
		return proEndDate;
	}
	public void setProEndDate(GregorianCalendar proEndDate) {
		this.proEndDate = proEndDate;
	}
	public String getProduction() {
		return production;
	}
	public void setProduction(String production) {
		this.production = production;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public Integer getProgramId() {
		return programId;
	}
	public void setProgramId(Integer programId) {
		this.programId = programId;
	}
	public String getEndTimestampStartDate() {
		return endTimestampStartDate;
	}
	public void setEndTimestampStartDate(String endTimestampStartDate) {
		this.endTimestampStartDate = endTimestampStartDate;
	}
	public String getEndTimestampEndDate() {
		return endTimestampEndDate;
	}
	public void setEndTimestampEndDate(String endTimestampEndDate) {
		this.endTimestampEndDate = endTimestampEndDate;
	}
	
	
	public String toString() {
		return (getProductId());
	}
	
}