package com.honda.mfg.stamp.conveyor.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
@Configurable
public class ServiceRoleIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private ServiceRoleDataOnDemand dod;

	@Test
    public void testCountServiceRoles() {
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", dod.getRandomServiceRole());
        long count = com.honda.mfg.stamp.conveyor.domain.ServiceRole.countServiceRoles();
        org.junit.Assert.assertTrue("Counter for 'ServiceRole' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindServiceRole() {
        com.honda.mfg.stamp.conveyor.domain.ServiceRole obj = dod.getRandomServiceRole();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ServiceRole.findServiceRole(id);
        org.junit.Assert.assertNotNull("Find method for 'ServiceRole' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'ServiceRole' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllServiceRoles() {
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", dod.getRandomServiceRole());
        long count = com.honda.mfg.stamp.conveyor.domain.ServiceRole.countServiceRoles();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'ServiceRole', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.honda.mfg.stamp.conveyor.domain.ServiceRole> result = com.honda.mfg.stamp.conveyor.domain.ServiceRole.findAllServiceRoles();
        org.junit.Assert.assertNotNull("Find all method for 'ServiceRole' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'ServiceRole' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindServiceRoleEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", dod.getRandomServiceRole());
        long count = com.honda.mfg.stamp.conveyor.domain.ServiceRole.countServiceRoles();
        if (count > 20) count = 20;
        java.util.List<com.honda.mfg.stamp.conveyor.domain.ServiceRole> result = com.honda.mfg.stamp.conveyor.domain.ServiceRole.findServiceRoleEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'ServiceRole' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'ServiceRole' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        com.honda.mfg.stamp.conveyor.domain.ServiceRole obj = dod.getRandomServiceRole();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ServiceRole.findServiceRole(id);
        org.junit.Assert.assertNotNull("Find method for 'ServiceRole' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyServiceRole(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'ServiceRole' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        com.honda.mfg.stamp.conveyor.domain.ServiceRole obj = dod.getRandomServiceRole();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ServiceRole.findServiceRole(id);
        boolean modified =  dod.modifyServiceRole(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.honda.mfg.stamp.conveyor.domain.ServiceRole merged = (com.honda.mfg.stamp.conveyor.domain.ServiceRole) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'ServiceRole' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", dod.getRandomServiceRole());
        com.honda.mfg.stamp.conveyor.domain.ServiceRole obj = dod.getNewTransientServiceRole(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'ServiceRole' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'ServiceRole' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        com.honda.mfg.stamp.conveyor.domain.ServiceRole obj = dod.getRandomServiceRole();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ServiceRole' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ServiceRole.findServiceRole(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'ServiceRole' with identifier '" + id + "'", com.honda.mfg.stamp.conveyor.domain.ServiceRole.findServiceRole(id));
    }
}
