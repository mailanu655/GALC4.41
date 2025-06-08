package com.honda.galc.entity.qics;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>InspectionModel Class description</h3>
 * <p> InspectionModel description </p>
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
 * Jun 30, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL178TBX")
public class InspectionModel extends AuditEntry  {
    @EmbeddedId
    private InspectionModelId id;


    private static final long serialVersionUID = 1L;

    @OneToOne(targetEntity = DefectGroup.class,fetch = FetchType.EAGER)
    @JoinColumn(name="DEFECT_GROUP_NAME",referencedColumnName="DEFECT_GROUP_NAME",updatable=false,insertable=false)
    private DefectGroup defectGroup;
    
    @OneToOne(targetEntity = PartGroup.class,fetch = FetchType.EAGER)
    @JoinColumn(name="PART_GROUP_NAME",referencedColumnName="PART_GROUP_NAME",updatable=false,insertable=false)
    private PartGroup partGroup;
    
    public InspectionModel() {
        super();
    }

    public InspectionModelId getId() {
        return this.id;
    }

    public void setId(InspectionModelId id) {
        this.id = id;
    }
    
    public void setApplicationId(String applicationId) {
    	if (id == null) id = new InspectionModelId();
    	id.setApplicationId(applicationId);
    }
    
    public String getApplicationId() {
    	return id.getApplicationId();
    }

    public DefectGroup getDefectGroup() {
    	return defectGroup;
    }
    
    public PartGroup getPartGroup() {
    	return partGroup;
    }
    
    public String getModelCode() {
    	return id.getModelCode();
    }
    
    public String getPartGroupName() {
    	return id.getPartGroupName();
    }
    
    public String getDefectGroupName() {
    	return id.getDefectGroupName();
    }

	@Override
	public String toString() {
		return toString(id.getModelCode(),id.getDefectGroupName(),
				id.getPartGroupName(),getApplicationId());
	}


}
