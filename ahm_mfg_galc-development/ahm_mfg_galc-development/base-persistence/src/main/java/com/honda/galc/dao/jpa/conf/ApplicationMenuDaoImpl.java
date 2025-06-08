package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;



import com.honda.galc.dao.conf.ApplicationMenuDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.ApplicationMenuEntryId;
import com.honda.galc.service.Parameters;


/**
 * 
 * <h3>ApplicationMenuDaoImpl Class description</h3>
 * <p> ApplicationMenuDaoImpl description </p>
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

public class ApplicationMenuDaoImpl extends BaseDaoImpl<ApplicationMenuEntry,ApplicationMenuEntryId> implements ApplicationMenuDao {
    private static final long serialVersionUID = 1L;

    public ApplicationMenuEntry findById(String clientId, Integer nodeNumber) {

    	return findByKey(new ApplicationMenuEntryId(clientId, nodeNumber));
    	
    }

    /**
     * get all application menu entries of a terminal. 
     * @return  the list is sorted by parent node number and current node number
     */
    
    public List<ApplicationMenuEntry> findAllByClientId(String clientId) {

    	return findAll(Parameters.with("id.clientId", clientId),new String[] {"parentNodeNumber","id.nodeNumber"},true);
    	
    }
    
    @Transactional
    public void removeById(String clientId, Integer nodeNumber) {

    	remove(findById(clientId,nodeNumber));
    	
    }
    
    @Transactional
    public ApplicationMenuEntry createMenuAfter(ApplicationMenuEntry currentMenu, String applicationId) {
    	List<ApplicationMenuEntry> menus = findAllByClientId(currentMenu.getId().getClientId());
    	
    	ApplicationMenuEntry newMenu = currentMenu.clone();
    	return newMenu;
    	
    }
    
    public int createNextNodeNumber(List<ApplicationMenuEntry> menus,ApplicationMenuEntry currentMenu) {
    	for(ApplicationMenuEntry menu : menus) {
			if(menu.getParentNodeNumber() == currentMenu.getParentNodeNumber() && 
			   menu.getId().getNodeNumber() > currentMenu.getId().getNodeNumber()) {
				return (currentMenu.getId().getNodeNumber() + menu.getId().getNodeNumber())/2;
			}
		}
		return currentMenu.getId().getNodeNumber() + 1000;
    }

}
