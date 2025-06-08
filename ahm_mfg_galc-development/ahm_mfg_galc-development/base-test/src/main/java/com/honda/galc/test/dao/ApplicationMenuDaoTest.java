package com.honda.galc.test.dao;


import org.junit.Test;

import com.honda.galc.dao.conf.ApplicationMenuDao;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.ApplicationMenuEntryId;

/**
 * 
 * <h3>ApplicationByTerminalDaoTest</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClientMain description </p>
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
 * @author Jeffray Huang
 * March 1, 2010
 *
 */
public class ApplicationMenuDaoTest extends BaseDaoTest<ApplicationMenuDao>{

	@Override
	ApplicationMenuEntry createEntity() {
		ApplicationMenuEntry applicationMenu = new ApplicationMenuEntry();
		ApplicationMenuEntryId id = new ApplicationMenuEntryId();
		id.setClientId("TestClient");
		id.setNodeNumber(10);
		
		applicationMenu.setId(id);
		applicationMenu.setNodeName("TestNodeName");
		applicationMenu.setParentNodeNumber(11);
		return applicationMenu;
	}

	@Override
	ApplicationMenuEntry updateEntity(Object entity) {
		ApplicationMenuEntry applicationMenu = (ApplicationMenuEntry)entity;
		applicationMenu.setNodeName("TestNodeName 1");
		return applicationMenu;
	}
	@Test()
	public void dummyTest() {
		
	}
}
