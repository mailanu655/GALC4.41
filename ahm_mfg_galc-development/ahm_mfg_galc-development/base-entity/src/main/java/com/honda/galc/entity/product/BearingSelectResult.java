package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>BearingSelectResult Class description</h3>
 * <p> BearingSelectResult description </p>
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
 * Jun 3, 2012
 *
 *
 */
@Entity
@Table(name="GAL108TBX")
public class BearingSelectResult extends AuditEntry {
	@EmbeddedId
	private BearingSelectResultId id;

	@Column(name="JOURNAL_BLOCK_MEASUREMENTS")
	private String journalBlockMeasurements;

	@Column(name="JOURNAL_CRANK_MEASUREMENTS")
	private String journalCrankMeasurements;

	@Column(name="CONROD_CRANK_MEASUREMENTS")
	private String conrodCrankMeasurements;

	@Column(name="CONROD_CONS_MEASUREMENTS")
	private String conrodConsMeasurements;

	@Column(name="MODEL_YEAR_CODE")
	private String modelYearCode;

	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="JOURNAL_UPPER_BEARING01")
	private String journalUpperBearing01;

	@Column(name="JOURNAL_LOWER_BEARING01")
	private String journalLowerBearing01;

	@Column(name="JOURNAL_UPPER_BEARING02")
	private String journalUpperBearing02;

	@Column(name="JOURNAL_LOWER_BEARING02")
	private String journalLowerBearing02;

	@Column(name="JOURNAL_UPPER_BEARING03")
	private String journalUpperBearing03;

	@Column(name="JOURNAL_LOWER_BEARING03")
	private String journalLowerBearing03;

	@Column(name="JOURNAL_UPPER_BEARING04")
	private String journalUpperBearing04;

	@Column(name="JOURNAL_LOWER_BEARING04")
	private String journalLowerBearing04;

	@Column(name="JOURNAL_UPPER_BEARING05")
	private String journalUpperBearing05;

	@Column(name="JOURNAL_LOWER_BEARING05")
	private String journalLowerBearing05;

	@Column(name="JOURNAL_UPPER_BEARING06")
	private String journalUpperBearing06;

	@Column(name="JOURNAL_LOWER_BEARING06")
	private String journalLowerBearing06;

	@Column(name="CONROD_UPPER_BEARING01")
	private String conrodUpperBearing01;

	@Column(name="CONROD_LOWER_BEARING01")
	private String conrodLowerBearing01;

	@Column(name="CONROD_UPPER_BEARING02")
	private String conrodUpperBearing02;

	@Column(name="CONROD_LOWER_BEARING02")
	private String conrodLowerBearing02;

	@Column(name="CONROD_UPPER_BEARING03")
	private String conrodUpperBearing03;

	@Column(name="CONROD_LOWER_BEARING03")
	private String conrodLowerBearing03;

	@Column(name="CONROD_UPPER_BEARING04")
	private String conrodUpperBearing04;

	@Column(name="CONROD_LOWER_BEARING04")
	private String conrodLowerBearing04;

	@Column(name="CONROD_UPPER_BEARING05")
	private String conrodUpperBearing05;

	@Column(name="CONROD_LOWER_BEARING05")
	private String conrodLowerBearing05;

	@Column(name="CONROD_UPPER_BEARING06")
	private String conrodUpperBearing06;

	@Column(name="CONROD_LOWER_BEARING06")
	private String conrodLowerBearing06;

	private static final long serialVersionUID = 1L;

	public BearingSelectResult() {
		super();
	}

	public BearingSelectResultId getId() {
		return this.id;
	}

	public void setId(BearingSelectResultId id) {
		this.id = id;
	}

	public String getJournalBlockMeasurements() {
		return StringUtils.trim(this.journalBlockMeasurements);
	}

	public void setJournalBlockMeasurements(String journalBlockMeasurements) {
		this.journalBlockMeasurements = journalBlockMeasurements;
	}

	public String getJournalCrankMeasurements() {
		return StringUtils.trim(this.journalCrankMeasurements);
	}

	public void setJournalCrankMeasurements(String journalCrankMeasurements) {
		this.journalCrankMeasurements = journalCrankMeasurements;
	}

	public String getConrodCrankMeasurements() {
		return StringUtils.trim(this.conrodCrankMeasurements);
	}

	public void setConrodCrankMeasurements(String conrodCrankMeasurements) {
		this.conrodCrankMeasurements = conrodCrankMeasurements;
	}

	public String getConrodConsMeasurements() {
		return StringUtils.trim(this.conrodConsMeasurements);
	}

	public void setConrodConsMeasurements(String conrodConsMeasurements) {
		this.conrodConsMeasurements = conrodConsMeasurements;
	}

	public String getModelYearCode() {
		return StringUtils.trim(this.modelYearCode);
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return StringUtils.trim(this.modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getJournalUpperBearing01() {
		return StringUtils.trim(this.journalUpperBearing01);
	}

	public void setJournalUpperBearing01(String journalUpperBearing01) {
		this.journalUpperBearing01 = journalUpperBearing01;
	}

	public String getJournalLowerBearing01() {
		return StringUtils.trim(this.journalLowerBearing01);
	}

	public void setJournalLowerBearing01(String journalLowerBearing01) {
		this.journalLowerBearing01 = journalLowerBearing01;
	}

	public String getJournalUpperBearing02() {
		return StringUtils.trim(this.journalUpperBearing02);
	}

	public void setJournalUpperBearing02(String journalUpperBearing02) {
		this.journalUpperBearing02 = journalUpperBearing02;
	}

	public String getJournalLowerBearing02() {
		return StringUtils.trim(this.journalLowerBearing02);
	}

	public void setJournalLowerBearing02(String journalLowerBearing02) {
		this.journalLowerBearing02 = journalLowerBearing02;
	}

	public String getJournalUpperBearing03() {
		return StringUtils.trim(this.journalUpperBearing03);
	}

	public void setJournalUpperBearing03(String journalUpperBearing03) {
		this.journalUpperBearing03 = journalUpperBearing03;
	}

	public String getJournalLowerBearing03() {
		return StringUtils.trim(this.journalLowerBearing03);
	}

	public void setJournalLowerBearing03(String journalLowerBearing03) {
		this.journalLowerBearing03 = journalLowerBearing03;
	}

	public String getJournalUpperBearing04() {
		return StringUtils.trim(this.journalUpperBearing04);
	}

	public void setJournalUpperBearing04(String journalUpperBearing04) {
		this.journalUpperBearing04 = journalUpperBearing04;
	}

	public String getJournalLowerBearing04() {
		return StringUtils.trim(this.journalLowerBearing04);
	}

	public void setJournalLowerBearing04(String journalLowerBearing04) {
		this.journalLowerBearing04 = journalLowerBearing04;
	}

	public String getJournalUpperBearing05() {
		return StringUtils.trim(this.journalUpperBearing05);
	}

	public void setJournalUpperBearing05(String journalUpperBearing05) {
		this.journalUpperBearing05 = journalUpperBearing05;
	}

	public String getJournalLowerBearing05() {
		return StringUtils.trim(this.journalLowerBearing05);
	}

	public void setJournalLowerBearing05(String journalLowerBearing05) {
		this.journalLowerBearing05 = journalLowerBearing05;
	}

	public String getJournalUpperBearing06() {
		return StringUtils.trim(this.journalUpperBearing06);
	}

	public void setJournalUpperBearing06(String journalUpperBearing06) {
		this.journalUpperBearing06 = journalUpperBearing06;
	}

	public String getJournalLowerBearing06() {
		return StringUtils.trim(this.journalLowerBearing06);
	}

	public void setJournalLowerBearing06(String journalLowerBearing06) {
		this.journalLowerBearing06 = journalLowerBearing06;
	}

	public String getConrodUpperBearing01() {
		return StringUtils.trim(this.conrodUpperBearing01);
	}

	public void setConrodUpperBearing01(String conrodUpperBearing01) {
		this.conrodUpperBearing01 = conrodUpperBearing01;
	}

	public String getConrodLowerBearing01() {
		return StringUtils.trim(this.conrodLowerBearing01);
	}

	public void setConrodLowerBearing01(String conrodLowerBearing01) {
		this.conrodLowerBearing01 = conrodLowerBearing01;
	}

	public String getConrodUpperBearing02() {
		return StringUtils.trim(this.conrodUpperBearing02);
	}

	public void setConrodUpperBearing02(String conrodUpperBearing02) {
		this.conrodUpperBearing02 = conrodUpperBearing02;
	}

	public String getConrodLowerBearing02() {
		return StringUtils.trim(this.conrodLowerBearing02);
	}

	public void setConrodLowerBearing02(String conrodLowerBearing02) {
		this.conrodLowerBearing02 = conrodLowerBearing02;
	}

	public String getConrodUpperBearing03() {
		return StringUtils.trim(this.conrodUpperBearing03);
	}

	public void setConrodUpperBearing03(String conrodUpperBearing03) {
		this.conrodUpperBearing03 = conrodUpperBearing03;
	}

	public String getConrodLowerBearing03() {
		return StringUtils.trim(this.conrodLowerBearing03);
	}

	public void setConrodLowerBearing03(String conrodLowerBearing03) {
		this.conrodLowerBearing03 = conrodLowerBearing03;
	}

	public String getConrodUpperBearing04() {
		return StringUtils.trim(this.conrodUpperBearing04);
	}

	public void setConrodUpperBearing04(String conrodUpperBearing04) {
		this.conrodUpperBearing04 = conrodUpperBearing04;
	}

	public String getConrodLowerBearing04() {
		return StringUtils.trim(this.conrodLowerBearing04);
	}

	public void setConrodLowerBearing04(String conrodLowerBearing04) {
		this.conrodLowerBearing04 = conrodLowerBearing04;
	}

	public String getConrodUpperBearing05() {
		return StringUtils.trim(this.conrodUpperBearing05);
	}

	public void setConrodUpperBearing05(String conrodUpperBearing05) {
		this.conrodUpperBearing05 = conrodUpperBearing05;
	}

	public String getConrodLowerBearing05() {
		return StringUtils.trim(this.conrodLowerBearing05);
	}

	public void setConrodLowerBearing05(String conrodLowerBearing05) {
		this.conrodLowerBearing05 = conrodLowerBearing05;
	}

	public String getConrodUpperBearing06() {
		return StringUtils.trim(this.conrodUpperBearing06);
	}

	public void setConrodUpperBearing06(String conrodUpperBearing06) {
		this.conrodUpperBearing06 = conrodUpperBearing06;
	}

	public String getConrodLowerBearing06() {
		return StringUtils.trim(this.conrodLowerBearing06);
	}

	public void setConrodLowerBearing06(String conrodLowerBearing06) {
		this.conrodLowerBearing06 = conrodLowerBearing06;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BearingSelectResult{");
		sb.append("id:").append(getId() == null ? null : getId().getProductId());
		sb.append(",actualTimestamp:").append(getId() == null ? null : getId().getActualTimestamp());
		sb.append(",myc:").append(getModelYearCode());
		sb.append(",mc:").append(getModelCode());
		sb.append(",bm:").append(getJournalBlockMeasurements());
		sb.append(",cmm:").append(getJournalCrankMeasurements());
		sb.append(",ccm:").append(getConrodCrankMeasurements());
		sb.append(",cm:").append(getConrodConsMeasurements());

		sb.append(",mub1:").append(getJournalUpperBearing01());
		sb.append(",mlb1:").append(getJournalLowerBearing01());
		sb.append(",mub2:").append(getJournalUpperBearing02());
		sb.append(",mlb2:").append(getJournalLowerBearing02());
		sb.append(",mub3:").append(getJournalUpperBearing03());
		sb.append(",mlb3:").append(getJournalLowerBearing03());
		sb.append(",mub4:").append(getJournalUpperBearing04());
		sb.append(",mlb4:").append(getJournalLowerBearing04());
		sb.append(",mub5:").append(getJournalUpperBearing05());
		sb.append(",mlb5:").append(getJournalLowerBearing05());
		sb.append(",mub6:").append(getJournalUpperBearing06());
		sb.append(",mlb6:").append(getJournalLowerBearing06());

		sb.append(",cub1:").append(getConrodUpperBearing01());
		sb.append(",clb1:").append(getConrodLowerBearing01());
		sb.append(",cub2:").append(getConrodUpperBearing02());
		sb.append(",clb2:").append(getConrodLowerBearing02());
		sb.append(",cub3:").append(getConrodUpperBearing03());
		sb.append(",clb3:").append(getConrodLowerBearing03());
		sb.append(",cub4:").append(getConrodUpperBearing04());
		sb.append(",clb4:").append(getConrodLowerBearing04());
		sb.append(",cub5:").append(getConrodUpperBearing05());
		sb.append(",clb5:").append(getConrodLowerBearing05());
		sb.append(",cub6:").append(getConrodUpperBearing06());
		sb.append(",clb6:").append(getConrodLowerBearing06());

		sb.append("}");
		return sb.toString();
	}
}
