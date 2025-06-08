package com.honda.galc.dto;

import java.io.Serializable;

public class ProductionLotBackout implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String PRODUCTION_LOT_COLUMN = "PRODUCTION_LOT";
	private final String table;
	private final String column;
	private final String lotMin;
	private final String lotMax;

	private int rows;
	private String lotRange;
	private String lotRangeStart;
	private String lotRangeEnd;

	public ProductionLotBackout(final String column, final String table, final String lotPrefix, final String lotDate) {
		this.table = table;
		this.column = column;
		final String lotBase = lotPrefix + lotDate;
		this.lotMin = lotBase + "0000";
		this.lotMax = lotBase + "9999";
	}
	
	public ProductionLotBackout(final String table, final String lotPrefix, final String lotDate) {
	
		this.table = table;
		this.column = PRODUCTION_LOT_COLUMN;
		final String lotBase = lotPrefix + lotDate;
		this.lotMin = lotBase + "0000";
		this.lotMax = lotBase + "9999";
	}

	public String getTable() {
		return this.table;
	}
	public String getLotMin() {
		return this.lotMin;
	}
	public String getLotMax() {
		return this.lotMax;
	}

	public int getRows() {
		return this.rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getLotRange() {
		return this.lotRange;
	}
	private void buildLotRange() {
		if (this.lotRangeStart == null && this.lotRangeEnd == null) {
			this.lotRange = "N/A";
			return;
		}
		StringBuilder lotRangeBuilder = new StringBuilder();
		if (this.lotRangeStart == null) {
			lotRangeBuilder.append(this.lotRangeEnd);
		}
		else if (this.lotRangeEnd == null) {
			lotRangeBuilder.append(this.lotRangeStart);
		}
		else {
			lotRangeBuilder.append(this.lotRangeStart);
			lotRangeBuilder.append(" - ");
			lotRangeBuilder.append(this.lotRangeEnd);
		}
		this.lotRange = lotRangeBuilder.toString();
	}

	public String getLotRangeStart() {
		return this.lotRangeStart;
	}
	public void setLotRangeStart(String lotRangeStart) {
		this.lotRangeStart = lotRangeStart;
		buildLotRange();
	}

	public String getLotRangeEnd() {
		return this.lotRangeEnd;
	}
	public void setLotRangeEnd(String lotRangeEnd) {
		this.lotRangeEnd = lotRangeEnd;
		buildLotRange();
	}

	public String getColumn() {
		return this.column;
	}

}