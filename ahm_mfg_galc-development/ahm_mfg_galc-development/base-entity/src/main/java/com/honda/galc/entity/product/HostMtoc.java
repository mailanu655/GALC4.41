package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>HostMtoc Class description</h3>
 * <p> HostMtoc description </p>
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
 * Dec 1, 2010
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL158TBX")
public class HostMtoc extends AuditEntry {
	@EmbeddedId
	private HostMtocId id;

	@Column(name="MODEL_YEAR_DESCRIPTION")
	private String modelYearDescription;

	@Column(name="EXT_COLOR_DESCRIPTION")
	private String extColorDescription;

	@Column(name="INT_COLOR_DESCRIPTION")
	private String intColorDescription;

	@Column(name="FRAME_NO_PREFIX")
	private String frameNoPrefix;

	@Column(name="SERIES_CODE")
	private String seriesCode;

	@Column(name="SERIES_DESCRIPTION")
	private String seriesDescription;

	@Column(name="GRADE_CODE")
	private String gradeCode;

	@Column(name="BODY_AND_TRANS_TYPE_CODE")
	private String bodyAndTransTypeCode;

	@Column(name="BODY_AND_TRANS_TYPE_DESC")
	private String bodyAndTransTypeDesc;

	@Column(name="SALES_MODEL_CODE")
	private String salesModelCode;

	@Column(name="SALES_MODEL_TYPE_CODE")
	private String salesModelTypeCode;

	@Column(name="SALES_MODEL_OPTION_CODE")
	private String salesModelOptionCode;

	@Column(name="SALES_EXT_COLOR_CODE")
	private String salesExtColorCode;

	@Column(name="SALES_INT_COLOR_CODE")
	private String salesIntColorCode;

	@Column(name="PROTOTYPE_CODE")
	private String prototypeCode;

	@Column(name="F_E")
	private String fE;

	@Column(name="PLANT_CODE_ENGINE")
	private String plantCodeEngine;

	@Column(name="ENGINE_MODEL_YEAR_CODE")
	private String engineModelYearCode;

	@Column(name="ENGINE_MODEL_CODE")
	private String engineModelCode;

	@Column(name="ENGINE_MODEL_TYPE_CODE")
	private String engineModelTypeCode;

	@Column(name="ENGINE_OPTION_CODE")
	private String engineOptionCode;

	private static final long serialVersionUID = 1L;

	public HostMtoc() {
		super();
	}

	public HostMtocId getId() {
		return this.id;
	}

	public void setId(HostMtocId id) {
		this.id = id;
	}

	public String getModelYearDescription() {
		return this.modelYearDescription;
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}

	public String getExtColorDescription() {
		return this.extColorDescription;
	}

	public void setExtColorDescription(String extColorDescription) {
		this.extColorDescription = extColorDescription;
	}

	public String getIntColorDescription() {
		return this.intColorDescription;
	}

	public void setIntColorDescription(String intColorDescription) {
		this.intColorDescription = intColorDescription;
	}

	public String getFrameNoPrefix() {
		return this.frameNoPrefix;
	}

	public void setFrameNoPrefix(String frameNoPrefix) {
		this.frameNoPrefix = frameNoPrefix;
	}

	public String getSeriesCode() {
		return this.seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public String getSeriesDescription() {
		return this.seriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public String getGradeCode() {
		return this.gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getBodyAndTransTypeCode() {
		return this.bodyAndTransTypeCode;
	}

	public void setBodyAndTransTypeCode(String bodyAndTransTypeCode) {
		this.bodyAndTransTypeCode = bodyAndTransTypeCode;
	}

	public String getBodyAndTransTypeDesc() {
		return this.bodyAndTransTypeDesc;
	}

	public void setBodyAndTransTypeDesc(String bodyAndTransTypeDesc) {
		this.bodyAndTransTypeDesc = bodyAndTransTypeDesc;
	}

	public String getSalesModelCode() {
		return this.salesModelCode;
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelTypeCode() {
		return this.salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	public String getSalesModelOptionCode() {
		return this.salesModelOptionCode;
	}

	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}

	public String getSalesExtColorCode() {
		return this.salesExtColorCode;
	}

	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	public String getSalesIntColorCode() {
		return this.salesIntColorCode;
	}

	public void setSalesIntColorCode(String salesIntColorCode) {
		this.salesIntColorCode = salesIntColorCode;
	}

	public String getPrototypeCode() {
		return this.prototypeCode;
	}

	public void setPrototypeCode(String prototypeCode) {
		this.prototypeCode = prototypeCode;
	}

	public String getFE() {
		return this.fE;
	}

	public void setFE(String fE) {
		this.fE = fE;
	}

	public String getPlantCodeEngine() {
		return this.plantCodeEngine;
	}

	public void setPlantCodeEngine(String plantCodeEngine) {
		this.plantCodeEngine = plantCodeEngine;
	}

	public String getEngineModelYearCode() {
		return this.engineModelYearCode;
	}

	public void setEngineModelYearCode(String engineModelYearCode) {
		this.engineModelYearCode = engineModelYearCode;
	}

	public String getEngineModelCode() {
		return this.engineModelCode;
	}

	public void setEngineModelCode(String engineModelCode) {
		this.engineModelCode = engineModelCode;
	}

	public String getEngineModelTypeCode() {
		return this.engineModelTypeCode;
	}

	public void setEngineModelTypeCode(String engineModelTypeCode) {
		this.engineModelTypeCode = engineModelTypeCode;
	}

	public String getEngineOptionCode() {
		return this.engineOptionCode;
	}

	public void setEngineOptionCode(String engineOptionCode) {
		this.engineOptionCode = engineOptionCode;
	}
	
	public String getEngineMto() {
		return engineModelYearCode + engineModelCode + engineModelTypeCode + engineOptionCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HostMtoc [id=" + id + ", modelYearDescription=" + modelYearDescription + ", extColorDescription="
				+ extColorDescription + ", intColorDescription=" + intColorDescription + ", frameNoPrefix="
				+ frameNoPrefix + ", seriesCode=" + seriesCode + ", seriesDescription=" + seriesDescription
				+ ", gradeCode=" + gradeCode + ", bodyAndTransTypeCode=" + bodyAndTransTypeCode
				+ ", bodyAndTransTypeDesc=" + bodyAndTransTypeDesc + ", salesModelCode=" + salesModelCode
				+ ", salesModelTypeCode=" + salesModelTypeCode + ", salesModelOptionCode=" + salesModelOptionCode
				+ ", salesExtColorCode=" + salesExtColorCode + ", salesIntColorCode=" + salesIntColorCode
				+ ", prototypeCode=" + prototypeCode + ", fE=" + fE + ", plantCodeEngine=" + plantCodeEngine
				+ ", engineModelYearCode=" + engineModelYearCode + ", engineModelCode=" + engineModelCode
				+ ", engineModelTypeCode=" + engineModelTypeCode + ", engineOptionCode=" + engineOptionCode + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bodyAndTransTypeCode == null) ? 0 : bodyAndTransTypeCode.hashCode());
		result = prime * result + ((bodyAndTransTypeDesc == null) ? 0 : bodyAndTransTypeDesc.hashCode());
		result = prime * result + ((engineModelCode == null) ? 0 : engineModelCode.hashCode());
		result = prime * result + ((engineModelTypeCode == null) ? 0 : engineModelTypeCode.hashCode());
		result = prime * result + ((engineModelYearCode == null) ? 0 : engineModelYearCode.hashCode());
		result = prime * result + ((engineOptionCode == null) ? 0 : engineOptionCode.hashCode());
		result = prime * result + ((fE == null) ? 0 : fE.hashCode());
		result = prime * result + ((frameNoPrefix == null) ? 0 : frameNoPrefix.hashCode());
		result = prime * result + ((gradeCode == null) ? 0 : gradeCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((plantCodeEngine == null) ? 0 : plantCodeEngine.hashCode());
		result = prime * result + ((prototypeCode == null) ? 0 : prototypeCode.hashCode());
		result = prime * result + ((salesExtColorCode == null) ? 0 : salesExtColorCode.hashCode());
		result = prime * result + ((salesIntColorCode == null) ? 0 : salesIntColorCode.hashCode());
		result = prime * result + ((salesModelCode == null) ? 0 : salesModelCode.hashCode());
		result = prime * result + ((salesModelOptionCode == null) ? 0 : salesModelOptionCode.hashCode());
		result = prime * result + ((salesModelTypeCode == null) ? 0 : salesModelTypeCode.hashCode());
		result = prime * result + ((seriesCode == null) ? 0 : seriesCode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HostMtoc other = (HostMtoc) obj;
		if (bodyAndTransTypeCode == null) {
			if (other.bodyAndTransTypeCode != null)
				return false;
		} else if (!bodyAndTransTypeCode.equals(other.bodyAndTransTypeCode))
			return false;
		if (bodyAndTransTypeDesc == null) {
			if (other.bodyAndTransTypeDesc != null)
				return false;
		} else if (!bodyAndTransTypeDesc.equals(other.bodyAndTransTypeDesc))
			return false;
		if (engineModelCode == null) {
			if (other.engineModelCode != null)
				return false;
		} else if (!engineModelCode.equals(other.engineModelCode))
			return false;
		if (engineModelTypeCode == null) {
			if (other.engineModelTypeCode != null)
				return false;
		} else if (!engineModelTypeCode.equals(other.engineModelTypeCode))
			return false;
		if (engineModelYearCode == null) {
			if (other.engineModelYearCode != null)
				return false;
		} else if (!engineModelYearCode.equals(other.engineModelYearCode))
			return false;
		if (engineOptionCode == null) {
			if (other.engineOptionCode != null)
				return false;
		} else if (!engineOptionCode.equals(other.engineOptionCode))
			return false;
		if (fE == null) {
			if (other.fE != null)
				return false;
		} else if (!fE.equals(other.fE))
			return false;
		if (frameNoPrefix == null) {
			if (other.frameNoPrefix != null)
				return false;
		} else if (!frameNoPrefix.equals(other.frameNoPrefix))
			return false;
		if (gradeCode == null) {
			if (other.gradeCode != null)
				return false;
		} else if (!gradeCode.equals(other.gradeCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (plantCodeEngine == null) {
			if (other.plantCodeEngine != null)
				return false;
		} else if (!plantCodeEngine.equals(other.plantCodeEngine))
			return false;
		if (prototypeCode == null) {
			if (other.prototypeCode != null)
				return false;
		} else if (!prototypeCode.equals(other.prototypeCode))
			return false;
		if (salesExtColorCode == null) {
			if (other.salesExtColorCode != null)
				return false;
		} else if (!salesExtColorCode.equals(other.salesExtColorCode))
			return false;
		if (salesIntColorCode == null) {
			if (other.salesIntColorCode != null)
				return false;
		} else if (!salesIntColorCode.equals(other.salesIntColorCode))
			return false;
		if (salesModelCode == null) {
			if (other.salesModelCode != null)
				return false;
		} else if (!salesModelCode.equals(other.salesModelCode))
			return false;
		if (salesModelOptionCode == null) {
			if (other.salesModelOptionCode != null)
				return false;
		} else if (!salesModelOptionCode.equals(other.salesModelOptionCode))
			return false;
		if (salesModelTypeCode == null) {
			if (other.salesModelTypeCode != null)
				return false;
		} else if (!salesModelTypeCode.equals(other.salesModelTypeCode))
			return false;
		if (seriesCode == null) {
			if (other.seriesCode != null)
				return false;
		} else if (!seriesCode.equals(other.seriesCode))
			return false;
		return true;
	}
	
}
