package com.honda.galc.test.dao;


import org.junit.Test;

import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.conf.ApplicationTaskId;

/**
 * 
 * <h3>ApplicationTaskDaoTest</h3>
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
public class ApplicationTaskDaoTest extends BaseDaoTest<ApplicationTaskDao>{

	ApplicationTask createEntity() {
		ApplicationTask applicationTask = new ApplicationTask();
		ApplicationTaskId id = new ApplicationTaskId();
		id.setApplicationId("TestApp");
		id.setTaskName("TestTask");
		applicationTask.setId(id);
		applicationTask.setJndiName("Test JNDI Name");
		return applicationTask;
	}

	
	ApplicationTask updateEntity(Object entity) {
		ApplicationTask applicationTask = (ApplicationTask)entity;
		applicationTask.setJndiName("Test New JNDI Name");
		return applicationTask;
	}
	@Test()
	public void dummyTest() {
		
	}
}
