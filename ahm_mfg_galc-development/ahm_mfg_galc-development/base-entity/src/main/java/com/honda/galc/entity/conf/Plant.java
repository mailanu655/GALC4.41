package com.honda.galc.entity.conf;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.*;
import java.util.List;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Plant represents manufacturing plant<br>
 * It belongs to Site and consists of Divisions
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Aug 31, 2009</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL211TBX")
public class Plant extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "siteName", column = @Column(name = "SITE_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 16)),
            @AttributeOverride(name = "plantName", column = @Column(name = "PLANT_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 16))})
    private PlantId id;

    @Column(name = "PLANT_LONG_DESCRIPTION")
    private String plantLongDescription;

    @OneToMany(targetEntity = Division.class, mappedBy = "plant", cascade = {}, fetch = FetchType.EAGER)
    private List<Division> divisions;
    
    @Transient
    private boolean isFullyLoaded = false;

    public Plant() {
        super();
    }

    public PlantId getId() {
        return this.id;
    }

    public String getPlantName() {
        return id.getPlantName();
    }
    
    public void setPlantName(String plantName) {
        id.setPlantName(plantName);
    }
    
    public String getSiteName() {
        return id.getSiteName();
    }

    public void setId(PlantId id) {
        this.id = id;
    }

    public String getPlantLongDescription() {
        return this.plantLongDescription;
    }

    public void setPlantLongDescription(String plantLongDescription) {
        this.plantLongDescription = plantLongDescription;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<Division> divisions) {
        this.divisions = divisions;
    }

	public boolean isFullyLoaded() {
		return isFullyLoaded;
	}

	public void setFullyLoaded(boolean isFullyLoaded) {
		this.isFullyLoaded = isFullyLoaded;
	}

	@Override
	public String toString() {
		return toString(id.getPlantName());
	}
}
