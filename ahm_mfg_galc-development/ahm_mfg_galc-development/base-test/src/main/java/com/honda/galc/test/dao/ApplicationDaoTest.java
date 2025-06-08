package com.honda.galc.test.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ApplicationType;
import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * 
 * <h3>ApplicationDaoTest</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ApplicationDaoTest description </p>
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
 * Mar 3, 2010
 *
 */
public class ApplicationDaoTest extends BaseDaoTest<ApplicationDao>{

	@Override
	Object createEntity() {
		Application application = new Application();
		application.setApplicationId("TestApp");
		application.setApplicationDescription("TestApp");
		application.setApplicationName("TestAppName");
		application.setApplicationType(ApplicationType.PORD);
		application.setScreenClass("test.class");
		application.setScreenId("test screen id");
		return application;
	}

	@Override
	Object updateEntity(Object entity) {
		Application application = (Application)entity;
		application.setScreenClass("test.class.update");
		return application;
	}
	
	@Test
	public void findAllByApplicationTypeId(){
		List<Application> applications = daoImpl.findAllByApplicationTypeId(ApplicationType.PROD.getId());
		Assert.assertTrue(applications.size()>0);
	}

	@Test
	public void findAppByProcessPointId(){
		List<ProcessPoint> processPoints = getDao(ProcessPointDao.class).findAll();
		Application app = daoImpl.findAppByProcessPointId(processPoints.get(0).getProcessPointId());
		Assert.assertNotNull(app);
	}	
}
