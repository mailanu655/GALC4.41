package com.honda.galc.entity.bearing;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrix</code> is ... .
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
@Entity
@Table(name = "GAL106TBX")
public class BearingMatrix extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BearingMatrixId id;

	@Column(name = "NUMBER_OF_MAINS")
	private int numberOfMainBearings;

	@Column(name = "NUMBER_OF_CONS")
	private int numberOfConrods;
	
	@Column(name = "CONROD_WEIGHT_INDEX")
	private String conrodWeightIndex;
	
	@Column(name = "CONROD_RANK_INDEX")
	private String conrodRankIndex;
	
	@Column(name = "CRANK_CON_INDEX")
	private String crankConIndex;
	
	@Column(name = "CRANK_MAIN_INDEX")
	private String crankMainIndex;

	public BearingMatrixId getId() {
		return id;
	}

	public void setId(BearingMatrixId id) {
		this.id = id;
	}

	public int getNumberOfMainBearings() {
		return numberOfMainBearings;
	}

	public void setNumberOfMainBearings(int numberOfMainBearings) {
		this.numberOfMainBearings = numberOfMainBearings;
	}

	public int getNumberOfConrods() {
		return numberOfConrods;
	}

	public void setNumberOfConrods(int numberOfConrods) {
		this.numberOfConrods = numberOfConrods;
	}

	public String getConrodWeightIndex() {
		return StringUtils.trim(conrodWeightIndex);
	}

	public void setConrodWeightIndex(String conrodWeightIndex) {
		this.conrodWeightIndex = conrodWeightIndex;
	}

	public String getConrodRankIndex() {
		return StringUtils.trim(conrodRankIndex);
	}

	public void setConrodRankIndex(String conrodRankIndex) {
		this.conrodRankIndex = conrodRankIndex;
	}

	public String getCrankConIndex() {
		return StringUtils.trim(crankConIndex);
	}

	public void setCrankConIndex(String crankConIndex) {
		this.crankConIndex = crankConIndex;
	}

	public String getCrankMainIndex() {
		return StringUtils.trim(crankMainIndex);
	}

	public void setCrankMainIndex(String crankMainIndex) {
		this.crankMainIndex = crankMainIndex;
	}
	
}