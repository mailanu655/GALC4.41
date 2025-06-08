package com.honda.galc.entity.conf;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Site represents manufacturing site
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
@Table(name = "GAL117TBX")
public class Site extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "SITE_DESCRIPTION")
    private String siteDescription;
    
    private List<Plant> plants;

    
    public Site() {
        super();
    }

    public String getSiteName() {
        return StringUtils.trim(this.siteName);
    }
    
    public String getId() {
    	 return getSiteName();
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteDescription() {
        return StringUtils.trim(this.siteDescription);
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }
    
    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

	@Override
	public String toString() {
		return toString(getSiteName());
	}
}
