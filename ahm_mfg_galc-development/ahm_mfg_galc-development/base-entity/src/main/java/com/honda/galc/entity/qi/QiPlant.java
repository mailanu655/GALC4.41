package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
/**
 * 
 * <h3>QiPlant Class description</h3>
 * <p> QiPlant description </p>
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
 * @author LnTInfotech<br>
 *        Oct 06, 2016
 * 
 */
@Entity
@Table(name = "QI_PLANT_TBX")
public class QiPlant extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiPlantId id;
	
	@Column(name = "PLANT_DESCRIPTION")
	@Auditable
	private String plantDesc;
	
	@Column(name = "HAM_PLANT_CODE")
	@Auditable
	private String pddaPlantCode;
	
	@Column(name = "PRODUCT_KIND")
	@Auditable
	private String productKind;
	
	@Column(name = "ENTRY_SITE")
	@Auditable
	private String entrySite;
	
	@Column(name = "ENTRY_PLANT")
	@Auditable
	private String entryPlant;
	
	@Column(name = "PROD_LINE_NO")
	@Auditable
	private short productLineNo;
	
	@Column(name = "ACTIVE")
	@Auditable
	private int active;
	
	@Column(name = "PDDA_LINE")
	private String pddaLine;
	
	public QiPlant() {
	}

	public QiPlant(QiPlantId id, String plantDesc, String pddaPlantCode,String productKind, String entrySite, String entryPlant,
			short productLineNo, int active,String pddaLine) {
		super();
		this.id = id;
		this.plantDesc = plantDesc;
		this.pddaPlantCode = pddaPlantCode;
		this.productKind = productKind;
		this.entrySite = entrySite;
		this.entryPlant = entryPlant;
		this.productLineNo = productLineNo;
		this.active = active;
		this.pddaLine=pddaLine;
	}

	public QiPlantId getId() {
		return id;
	}

	public void setId(QiPlantId id) {
		this.id = id;
	}

	public String getPlantDesc() {
		return StringUtils.trimToEmpty(this.plantDesc);
	}

	public void setPlantDesc(String plantDesc) {
		this.plantDesc = plantDesc;
	}

	public String getPddaPlantCode() {
		return StringUtils.trimToEmpty(this.pddaPlantCode);
	}

	public void setPddaPlantCode(String pddaPlantCode) {
		this.pddaPlantCode = pddaPlantCode;
	}

	public String getProductKind() {
		return StringUtils.trimToEmpty(this.productKind);
	}

	public void setProductKind(String productKind) {
		this.productKind = StringUtils.trimToEmpty(productKind);
	}
	public String getEntrySite() {
		return StringUtils.trimToEmpty(this.entrySite);
	}

	public void setEntrySite(String entrySite) {
		this.entrySite = entrySite;
	}

	public String getEntryPlant() {
		return StringUtils.trimToEmpty(this.entryPlant);
	}

	public void setEntryPlant(String entryPlant) {
		this.entryPlant = entryPlant;
	}

	public short getProductLineNo() {
		return productLineNo;
	}

	public void setProductLineNo(short productLineNo) {
		this.productLineNo = productLineNo;
	}
	
	public String getPlant() {
		return getId().getPlant();
	}
	
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public boolean isActive() {
		return this.active == (short) 1;
	}

	public void setActive(boolean active) {
		this.active = (short) (active ? 1 : 0);
	}

	public String getPddaLine() {
		return StringUtils.trimToEmpty(pddaLine);
	}

	public void setPddaLine(String pddaLine) {
		this.pddaLine = pddaLine;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((entryPlant == null) ? 0 : entryPlant.hashCode());
		result = prime * result
				+ ((entrySite == null) ? 0 : entrySite.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((pddaLine == null) ? 0 : pddaLine.hashCode());
		result = prime * result
				+ ((pddaPlantCode == null) ? 0 : pddaPlantCode.hashCode());
		result = prime * result
				+ ((plantDesc == null) ? 0 : plantDesc.hashCode());
		result = prime * result
				+ ((productKind == null) ? 0 : productKind.hashCode());
		result = prime * result + productLineNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiPlant other = (QiPlant) obj;
		if (active != other.active)
			return false;
		if (entryPlant == null) {
			if (other.entryPlant != null)
				return false;
		} else if (!entryPlant.equals(other.entryPlant))
			return false;
		if (entrySite == null) {
			if (other.entrySite != null)
				return false;
		} else if (!entrySite.equals(other.entrySite))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pddaLine == null) {
			if (other.pddaLine != null)
				return false;
		} else if (!pddaLine.equals(other.pddaLine))
			return false;
		if (pddaPlantCode == null) {
			if (other.pddaPlantCode != null)
				return false;
		} else if (!pddaPlantCode.equals(other.pddaPlantCode))
			return false;
		if (plantDesc == null) {
			if (other.plantDesc != null)
				return false;
		} else if (!plantDesc.equals(other.plantDesc))
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		if (productLineNo != other.productLineNo)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "QiPlant [id=" + id + ", plantDesc=" + plantDesc
				+ ", pddaPlantCode=" + pddaPlantCode + ", productKind="
				+ productKind + ", entrySite=" + entrySite + ", entryPlant="
				+ entryPlant + ", productLineNo=" + productLineNo + ", active="
				+ active + ", pddaLine=" + pddaLine + "]";
	}
	
	
}
