package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


@Entity
@Table(name="PRODUCTION_LOT_MBPN_SEQUENCE_TBX ")
public class ProductionLotMbpnSequence extends AuditEntry {
	
	@EmbeddedId
	private ProductionLotMbpnSequenceId id;
	
	@Column( name = "MBPN" )
	private String mbpn;
	
	@Column( name = "COMBINATION_CODE" )
	private String combinationCode;

	public ProductionLotMbpnSequence() {
		super();
	}

	@Override
	public ProductionLotMbpnSequenceId getId() {
		return id;
	}

	public void setId(ProductionLotMbpnSequenceId id) {
		this.id = id;
	}

	public String getMbpn() {
		return StringUtils.trim(this.mbpn);
	}

	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
	}

	public String getCombinationCode() {
		return StringUtils.trim(this.combinationCode);
	}

	public void setCombinationCode(String combinationCode) {
		this.combinationCode = combinationCode;
	}

	@Override
	public String toString() {
		return "ProductionLotMbpnSequence [productionLotMbpnSequenceId=" + id.toString() + ", mbpn="
				+ mbpn + ", combinationCode=" + combinationCode + "]";
	}


	

}