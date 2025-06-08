/**
 * 
 */
package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Jan 3, 2012
 */
@Entity
@Table(name = "HMIN_AFB_MTO_TBX")
public class AfbData extends AuditEntry {

	private static final long serialVersionUID = -1227287735300035685L;

	@EmbeddedId
	private AfbDataId id;

	@Column(name = "AFB_DATA_TEXT")
	private String afbDataText;

	public Object getId() {
		return this.id;
	}
	
	public void setId(AfbDataId id) {
		this.id = id;
	}
	
	public String getAfbDataText() {
		return afbDataText;
	}
	
	public void setAfbDataText(String afbDataText) {
		this.afbDataText = afbDataText;
	}
}

