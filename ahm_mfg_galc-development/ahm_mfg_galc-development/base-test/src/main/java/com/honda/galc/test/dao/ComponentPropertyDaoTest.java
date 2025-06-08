package com.honda.galc.test.dao;


import java.sql.Timestamp;

import org.junit.Test;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.entity.conf.ComponentProperty;

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
public class ComponentPropertyDaoTest extends BaseDaoTest<ComponentPropertyDao>{

	@Override
	ComponentProperty createEntity() {
		ComponentProperty componentProperty = new ComponentProperty("TestComponentId", "TestPropertyKey", "testValue");
		componentProperty.setDescription("test Description");
		componentProperty.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		return componentProperty;
	}

	@Override
	ComponentProperty updateEntity(Object entity) {
		ComponentProperty componentProperty = (ComponentProperty)entity;
		componentProperty.setPropertyValue("New Test Value");
		return componentProperty;
	}
	@Test()
	public void dummyTest() {
		
	}
}
