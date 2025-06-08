package com.honda.galc.entity.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * 
 * <h3>EngineManifestHis</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManifestHis description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 17, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 17, 2017
 */
@Entity
@Table(name="ENGINE_MANIFEST_HIST_TBX")
public class EngineManifestHis extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EngineManifestHisId id;

	@Column(name="ENGINE_FIRED_IND")
	private String engineFiredInd = "";

	@Column(name="ENGINE_SOURCE")
	private String engineSource ="";

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	private String vin ="";

	public EngineManifestHis() {
	}

	public EngineManifestHisId getId() {
		return this.id;
	}

	public void setId(EngineManifestHisId id) {
		this.id = id;
	}


	public String getEngineFiredInd() {
		return StringUtils.trim(this.engineFiredInd);
	}

	public void setEngineFiredInd(String engineFiredInd) {
		this.engineFiredInd = engineFiredInd;
	}

	public String getEngineSource() {
		return StringUtils.trim(this.engineSource);
	}

	public void setEngineSource(String engineSource) {
		this.engineSource = engineSource;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getVin() {
		return StringUtils.trim(this.vin);
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

}