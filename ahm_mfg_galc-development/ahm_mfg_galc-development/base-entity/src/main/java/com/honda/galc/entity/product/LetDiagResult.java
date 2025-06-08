package com.honda.galc.entity.product;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 04, 2013
 */

@Entity
@Table(name="GAL726TBX")
public class LetDiagResult extends AuditEntry {
	@EmbeddedId
	private LetDiagResultId id;

	@Column(name="TOTAL_RESULT_STATUS")
	private String totalResultStatus;

	@Column(name="BASE_RELEASE")
	private String baseRelease;

	@Column(name="SOFTWARE_VERSION")
	private String softwareVersion;

	private static final long serialVersionUID = 1L;

	public String getTotalResultStatus() {
		return StringUtils.trim(totalResultStatus);
	}

	public void setTotalResultStatus(String totalResultStatus) {
		this.totalResultStatus = totalResultStatus;
	}

	public String getBaseRelease() {
		return StringUtils.trim(baseRelease);
	}

	public void setBaseRelease(String baseRelease) {
		this.baseRelease = baseRelease;
	}

	public String getSoftwareVersion() {
		return StringUtils.trim(softwareVersion);
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public LetDiagResult() {
		super();
	}

	public LetDiagResult(LetDiagResultId newId) {
		super();
		setId(newId);
	}

	public LetDiagResultId getId() {
		return this.id;
	}

	public void setId(LetDiagResultId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return toString(getId().getEndTimestamp().toString(),getId().getLetTerminalId());
	}

}
