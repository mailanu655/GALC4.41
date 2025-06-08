/**
 * 
 */
package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;



/**
 * @author vf031824
 *
 */
@Entity
@Table(name = "PARTS_LOADING_MAINTENANCE_TBX")
public class PartsLoadingMaintenance extends AuditEntry {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="BIN_NAME")
	private String binName;

	@Column(name="PART_NAME")
	private String partName;

	@Column(name="PART_SPEC_ID")
	private String partSpecId;

	private static final long serialVersionUID = 1L;

	public PartsLoadingMaintenance() {
		super();
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getid() {
		return this.id;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointID) {
		this.processPointId = processPointID;
	}

	public String getBinName() {
		return StringUtils.trim(this.binName);
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartSpecId() {
		return StringUtils.trim(this.partSpecId);
	}

	public void setPartSpecId(String partSpecId) {
		this.partSpecId = partSpecId;
	}

	public String getDisplayName() {
		if(getBinName() == null) return getProcessPointId().trim();
		return getBinName().trim();
	}

	public Object getId() {
		return this.id;
	}
}