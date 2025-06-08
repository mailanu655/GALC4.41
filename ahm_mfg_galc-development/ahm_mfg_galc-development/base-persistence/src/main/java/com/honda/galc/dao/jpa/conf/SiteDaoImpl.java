package com.honda.galc.dao.jpa.conf;


import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Site;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * <h3>SiteDaoImpl Class description</h3>
 * <p> SiteDaoImpl description </p>
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
 * Mar 29, 2010
 *
 */
public class SiteDaoImpl extends BaseDaoImpl<Site,String> implements SiteDao {
    
    @Autowired
    private PlantDao plantDao;

    public SiteDaoImpl() {
		super();
	}

    public List<Site> findAllWithChildren() {
       
        List<Site> sites= findAll();
        
        for(Site site : sites)
            site.setPlants(plantDao.findAllBySite(site.getSiteName()));
        
        return sites;
    }

  

}
