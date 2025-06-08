package com.honda.galc.entity.bearing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPart</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
@Entity
@Table(name = "GAL107TBX")
public class BearingPart extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BEARING_SERIAL_NO")
	private String id;

	@Column(name = "BEARING_COLOR")
	private String color;

	@Column(name = "BEARING_TYPE")
	private String type;

	public String getColor() {
		return StringUtils.trim(color);
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getType() {
		return StringUtils.trim(type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return StringUtils.trim(id);
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BearingPart{");
		sb.append("id:").append(getId());
		sb.append(",type:").append(getType());
		sb.append(",color:").append(getColor());
		sb.append("}");
		return sb.toString();
	}

}
