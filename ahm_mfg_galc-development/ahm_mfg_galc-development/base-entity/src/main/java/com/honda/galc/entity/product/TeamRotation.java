package com.honda.galc.entity.product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the TEAM_ROTATION_TBX database table.
 * 
 */
@Entity
@Table(name="TEAM_ROTATION_TBX")
public class TeamRotation extends AuditEntry implements Cloneable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TeamRotationId id;

	public TeamRotation() {
	}

	public TeamRotationId getId() {
		return this.id;
	}

	public void setId(TeamRotationId id) {
		this.id = id;
	}
	
	public String toString() {
		return id == null ? "" : toString(id.getPlantCode(), id.getLineNo(),
				id.getProcessLocation(), id.getProductionDate(), id.getShift(),
				id.getTeam());
	}
	
	public String toDescription() {
		return " Shift: " + id.getShift() + " Team: " + id.getTeam() + " on " + getFormatProductionDate();
	}
	
	public String getFormatProductionDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(id.getProductionDate());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		TeamRotation cloned = (TeamRotation) super.clone();
		if(id!=null){
			cloned.id= (TeamRotationId) this.id.clone();
		}
		return cloned;
	}

}