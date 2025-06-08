package com.honda.galc.entity.qics;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * <h3>DefectTypeDescription Class description</h3>
 * <p> DefectTypeDescription description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 30, 2011
 *
 *
 */
@Entity
@Table(name = "GAL127TBX")
public class DefectTypeDescription extends AuditEntry  {
    @EmbeddedId
    private DefectTypeDescriptionId id;


    private static final long serialVersionUID = 1L;

    public DefectTypeDescription() {
        super();
    }

    public DefectTypeDescriptionId getId() {
        return this.id;
    }

    public void setId(DefectTypeDescriptionId id) {
        this.id = id;
    }
    
    public void setDefectTypeName(String defectTypeName) {
    	if(id == null) id = new DefectTypeDescriptionId();
    	id.setDefectTypeName(defectTypeName);
    }
    
    public String getDefectTypeName() {
    	
    	return id == null ? null : id.getDefectTypeName();
    	
    } 
    
    public void setDefectGroupName(String defectGroupName) {
    	if(id == null) id = new DefectTypeDescriptionId();
    	id.setDefectGroupName(defectGroupName);
    }
    
    public String getDefectGroupName() {
    	
    	return id == null ? null : id.getDefectGroupName();
    	
    } 
    
    public void setSecondaryPartName(String name) {
    	if(id == null) id = new DefectTypeDescriptionId();
    	id.setSecondaryPartName(name);
    }
    
    public String getSecondaryPartName() {
    	
    	return id == null ? null : id.getSecondaryPartName();
    	
    }

	@Override
	public String toString() {
		return toString(getDefectTypeName(),getDefectGroupName(),getSecondaryPartName());
	} 
    

}
