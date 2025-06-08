package com.honda.galc.entity.bearing;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixCellId</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
@Embeddable
public class BearingMatrixCellId extends BearingMatrixId {

	private static final long serialVersionUID = 1L;

	@Column(name = "MODEL_TYPE_CODE")
	private String modelTypeCode;
	
	@Column(name = "JOURNAL_POSITION")
	private String journalPosition;
	
	@Column(name = "BEARING_TYPE")
	private String bearingType;

	@Column(name = "COLUMN_MEASUREMENT")
	private String columnMeasurement;

	@Column(name = "ROW_MEASUREMENT")
	private String rowMeasurement;

	public String getBearingType() {
		return StringUtils.trim(bearingType);
	}

	public void setBearingType(String bearingType) {
		this.bearingType = bearingType;
	}

	public String getColumnMeasurement() {
		return StringUtils.trim(columnMeasurement);
	}

	public void setColumnMeasurement(String columnMeasurement) {
		this.columnMeasurement = columnMeasurement;
	}

	public String getRowMeasurement() {
		return StringUtils.trim(rowMeasurement);
	}

	public void setRowMeasurement(String rowMeasurement) {
		this.rowMeasurement = rowMeasurement;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setJournalPosition(String journalPosition) {
		this.journalPosition = journalPosition;
	}

	public String getJournalPosition() {
		return journalPosition;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BearingMatrixCellId)) {
			return false;
		}
		BearingMatrixCellId other = (BearingMatrixCellId) o;
		boolean myc = equals(getModelYearCode(), other.getModelYearCode());
		boolean mc = equals(getModelCode(), other.getModelCode());
		boolean mtc = equals(getModelTypeCode(), other.getModelTypeCode());
		boolean jp = equals(getJournalPosition(), other.getJournalPosition());
		boolean bt = equals(getBearingType(), other.getBearingType());
		boolean cm = equals(getColumnMeasurement(), other.getColumnMeasurement());
		boolean rm = equals(getRowMeasurement(), other.getRowMeasurement());
		return myc && mc && mtc && jp && bt && cm && rm;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 37 * hash + (getBearingType() == null ? 0 : getBearingType().hashCode());
		hash = 37 * hash + (getColumnMeasurement() == null ? 0 : getColumnMeasurement().hashCode());
		hash = 37 * hash + (getRowMeasurement() == null ? 0 : getRowMeasurement().hashCode());
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("{");
		sb.append(toStringProperties());
		sb.append("}");
		return sb.toString();
	}

	public String toStringProperties() {
		StringBuilder sb = new StringBuilder();
		sb.append("modelYearCode:").append(getModelYearCode());
		sb.append(",modelCode:").append(getModelCode());
		sb.append(",modelTypeCode:").append(getModelTypeCode());
		sb.append(",journalPosition:").append(getJournalPosition());
		sb.append(",bearingType:").append(getBearingType());
		sb.append(",columnMeasurement:").append(getColumnMeasurement());
		sb.append(",rowMeasurement:").append(getRowMeasurement());
		return sb.toString();
	}
}
