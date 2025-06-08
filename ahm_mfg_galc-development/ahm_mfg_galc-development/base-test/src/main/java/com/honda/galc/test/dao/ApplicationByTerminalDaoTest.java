package com.honda.galc.test.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationByTerminalId;

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
public class ApplicationByTerminalDaoTest extends BaseDaoTest<ApplicationByTerminalDao>{

	@Override
	ApplicationByTerminal createEntity() {
		ApplicationByTerminal application = new ApplicationByTerminal();
		ApplicationByTerminalId id = new ApplicationByTerminalId();
		id.setApplicationId("TestApp");
		id.setHostName("TestHostName");
		application.setId(id);
		application.setDefaultApplicationFlag((short)1);
		return application;
	}

	@Override
	ApplicationByTerminal updateEntity(Object entity) {
		ApplicationByTerminal application = (ApplicationByTerminal)entity;
		application.setDefaultApplicationFlag((short)0);
		return application;
	}
	
	@Test
	public void findById(){
		List<ApplicationByTerminal> applications = daoImpl.findAll();
		ApplicationByTerminal application = applications.get(0);
		daoImpl.findById(application.getId().getApplicationId(), application.getId().getHostName());
	}
	
	@Test
	public void findDefaultApplication(){
		List<ApplicationByTerminal> applications = daoImpl.findAll();
		ApplicationByTerminal application = applications.get(0);
		daoImpl.findDefaultApplication(application.getId().getHostName());
	}
	
	@Test
	public void removeById() {
		ApplicationByTerminal app = createEntity();
		daoImpl.save(app);
		ApplicationByTerminal app1 = daoImpl.findByKey(app.getId());
		Assert.assertNotNull(app1);
		daoImpl.removeById(app.getId().getApplicationId(), app.getId().getHostName());
		ApplicationByTerminal app2 = daoImpl.findByKey(app.getId());
		Assert.assertNull(app2);
		
	}
	

	
}
