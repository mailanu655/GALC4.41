package com.honda.ahm.lc.vdb.dto;

public class ChartDto {
	
	private String divisionName;
	private int seqNo;
	private String lineName;
	private long count;
	
	public ChartDto(String divisionName, int seqNo, long count) {
		super();
		this.divisionName = divisionName;
		this.seqNo = seqNo;
		this.count = count;
	}
	
	public ChartDto(String divisionName, String lineName, long count) {
		super();
		this.divisionName = divisionName;
		this.lineName = lineName;
		this.count = count;
	}

	public ChartDto() {
		super();
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
