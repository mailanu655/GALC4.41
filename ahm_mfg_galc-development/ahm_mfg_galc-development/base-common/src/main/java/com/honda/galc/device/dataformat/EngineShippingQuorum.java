package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

/**
 * 
 * 
 * <h3>ShippingQuorum Class description</h3>
 * <p> ShippingQuorum description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 15, 2014
 *
 *
 */
public class EngineShippingQuorum implements IDeviceData, Serializable{
private static final long serialVersionUID = 1L;
	
	@Tag(name="PALLET_TYPE")
	private String palletType;
	
	@Tag(name="QUORUM_SIZE")
	private int quorumSize;

	@Tag(name="QUORUM_TYPE")
	private String quorumType;
	
	@Tag(name="ROW_NUMBER")
	private int rowNumber;

	@Tag(name="TRAILER_NUMBER")
	private String trailerNumber;

	public EngineShippingQuorum(){
	}

	public String getPalletType() {
		return palletType;
	}

	public void setPalletType(String palletType) {
		this.palletType = palletType;
	}

	public int getQuorumSize() {
		return quorumSize;
	}

	public void setQuorumSize(int quorumSize) {
		this.quorumSize = quorumSize;
	}

	public String getQuorumType() {
		return quorumType;
	}

	public void setQuorumType(String quorumType) {
		this.quorumType = quorumType;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}
	
}
