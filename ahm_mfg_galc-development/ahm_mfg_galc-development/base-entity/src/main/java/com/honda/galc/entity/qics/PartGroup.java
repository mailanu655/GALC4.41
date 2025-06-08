package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * <h3>PartGroup Class description</h3>
 * <p> PartGroup description </p>
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
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL208TBX")
public class PartGroup extends AuditEntry {
    @Id
    @Column(name = "PART_GROUP_NAME")
    private String partGroupName;

    @Column(name = "PART_GROUP_DESCRIPTION_SHORT")
    private String partGroupDescriptionShort;

    @Column(name = "PART_GROUP_DESCRIPTION_LONG")
    private String partGroupDescriptionLong;

    @Column(name = "MODEL_CODE")
    private String modelCode;


    private static final long serialVersionUID = 1L;

    public PartGroup() {
        super();
    }

    public String getPartGroupName() {
        return StringUtils.trim(this.partGroupName);
    }
    
    public String getId() {
    	return getPartGroupName();
    }

    public void setPartGroupName(String partGroupName) {
        this.partGroupName = partGroupName;
    }

    public String getPartGroupDescriptionShort() {
        return StringUtils.trim(this.partGroupDescriptionShort);
    }

    public void setPartGroupDescriptionShort(String partGroupDescriptionShort) {
        this.partGroupDescriptionShort = partGroupDescriptionShort;
    }

    public String getPartGroupDescriptionLong() {
        return StringUtils.trim(this.partGroupDescriptionLong);
    }

    public void setPartGroupDescriptionLong(String partGroupDescriptionLong) {
        this.partGroupDescriptionLong = partGroupDescriptionLong;
    }

    public String getModelCode() {
        return this.modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

	@Override
	public String toString() {
		return toString(getPartGroupName(),getModelCode());
	}
 
}
