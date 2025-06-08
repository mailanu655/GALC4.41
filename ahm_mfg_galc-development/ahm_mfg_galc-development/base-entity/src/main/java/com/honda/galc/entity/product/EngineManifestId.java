package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>EngineManifestId</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManifestId description </p>
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
@Embeddable
public class EngineManifestId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ENGINE_NO")
	private String engineNo;

	private String plant;

	public EngineManifestId() {
	}
	public String getEngineNo() {
		return StringUtils.trim(this.engineNo);
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getPlant() {
		return StringUtils.trim(this.plant);
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EngineManifestId)) {
			return false;
		}
		EngineManifestId castOther = (EngineManifestId)other;
		return 
			this.getEngineNo().equals(castOther.getEngineNo())
			&& this.getPlant().equals(castOther.getPlant());
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getEngineNo().hashCode();
		hash = hash * prime + this.getPlant().hashCode();
		
		return hash;
	}
	@Override
	public String toString() {
		return "EngineManifestId [engineNo=" + engineNo + ", plant=" + plant
				+ "]";
	}
	
	
}