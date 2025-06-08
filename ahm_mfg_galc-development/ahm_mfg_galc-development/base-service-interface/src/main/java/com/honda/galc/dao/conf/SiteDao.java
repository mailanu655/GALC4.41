package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.Site;
import com.honda.galc.service.IDaoService;


public interface SiteDao extends IDaoService<Site, String> {
     
    // get all sites 
    public List<Site> findAllWithChildren();

}
