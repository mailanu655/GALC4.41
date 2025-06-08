package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>DefectGroup Class description</h3>
 * <p> DefectGroup description </p>
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
@Table(name = "GAL123TBX")
public class DefectGroup extends AuditEntry  {
    @Id
    @Column(name = "DEFECT_GROUP_NAME")
    private String defectGroupName;

    @Column(name = "DEFECT_GROUP_DESCRIPTION_SHORT")
    private String defectGroupDescriptionShort;

    @Column(name = "DEFECT_GROUP_DESCRIPTION_LONG")
    private String defectGroupDescriptionLong;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "MODEL_CODE")
    private String modelCode;


    private static final long serialVersionUID = 1L;

    public DefectGroup() {
        super();
    }

    public String getDefectGroupName() {
        return StringUtils.trim(this.defectGroupName);
    }
    
    public String getId() {
    	return getDefectGroupName();
    }

    public void setDefectGroupName(String defectGroupName) {
        this.defectGroupName = defectGroupName;
    }

    public String getDefectGroupDescriptionShort() {
        return StringUtils.trim(this.defectGroupDescriptionShort);
    }

    public void setDefectGroupDescriptionShort(String defectGroupDescriptionShort) {
        this.defectGroupDescriptionShort = defectGroupDescriptionShort;
    }

    public String getDefectGroupDescriptionLong() {
        return StringUtils.trim(this.defectGroupDescriptionLong);
    }

    public void setDefectGroupDescriptionLong(String defectGroupDescriptionLong) {
        this.defectGroupDescriptionLong = defectGroupDescriptionLong;
    }

    public String getImageName() {
        return StringUtils.trim(this.imageName);
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getModelCode() {
        return StringUtils.trim(this.modelCode);
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

	@Override
	public String toString() {
		return toString(getDefectGroupName(),getImageName(),getModelCode());
	}

}
