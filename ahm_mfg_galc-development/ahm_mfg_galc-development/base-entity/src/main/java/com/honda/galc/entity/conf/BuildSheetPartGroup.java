package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.UserAuditEntry;

@Entity
@Table(name = "BUILD_SHEET_PART_ATTR_BY_MODEL_GRP_TBX")
public class BuildSheetPartGroup extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
    private BuildSheetPartGroupId id;

    @Column(name = "ROW")
    @Auditable(isPartOfPrimaryKey= false,sequence=2)
    private int row;
    
    @Column(name = "COL")
    @Auditable(isPartOfPrimaryKey= false,sequence=3)
    private int column;
    
    public BuildSheetPartGroup() {
        super();
    }

	public BuildSheetPartGroupId getId() {
		return this.id;
	}

	public void setId(BuildSheetPartGroupId id) {
		this.id = id;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
    
	public String getFormId() {
    	return getId() != null ? getId().getFormId() : null;
    }

    public void setFormId(String formId) {
		if( getId() != null )
			getId().setFormId(formId);
	}
    
	public String getAttribute() {
    	return getId() != null ? getId().getAttribute() : null;
    }

    public void setAttribute(String attribute) {
		if( getId() != null )
			getId().setAttribute(attribute);
	}
    
    public String getModelGroup() {
    	return getId() != null ? getId().getModelGroup() : null;
	}

	public void setModelGroup(String modelGroup) {
		if( getId() != null )
			getId().setModelGroup(modelGroup);
	}

    
	@Override
	public String toString() {
		return toString(id.getAttribute(), id.getFormId(), id.getModelGroup());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildSheetPartGroup other = (BuildSheetPartGroup) obj;
		if (column != other.column)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
}
