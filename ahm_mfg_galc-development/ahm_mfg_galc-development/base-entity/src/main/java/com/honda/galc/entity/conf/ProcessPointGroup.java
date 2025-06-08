package com.honda.galc.entity.conf;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.StringUtil;
/**
 * <h3>Class description</h3>
 * Process point groups
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jul. 10, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180710</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */

@Entity
@Table(name = "PROCESS_POINT_GROUP_TBX")
public class ProcessPointGroup extends CreateUserAuditEntry {

	private static final long serialVersionUID = 7319394887578382630L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true)
	private ProcessPointGroupId id;

	public ProcessPointGroup() {
        super();
    }
	
	public ProcessPointGroup(ProcessPointGroupId id) {
        super();
		this.id = id;
    }

	public ProcessPointGroupId getId() {
		return id;
	}

    public void setId(ProcessPointGroupId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		ProcessPointGroup other = (ProcessPointGroup) obj;

		return id!= null && id.equals(other.getId());
	}

	public String toString() {
        return id == null ? this.getClass().getSimpleName() 
        		: StringUtil.toString(this.getClass().getSimpleName(), id.getCategoryCode(), id.getSite(), id.getProcessPointGroupName(), id.getProcessPointId());

    }
}
