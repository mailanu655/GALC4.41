package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.service.datacollection.DataCollectionServiceTest;

@RunWith(Suite.class)
@SuiteClasses(
  {
	  DaoTestSuite.class,DataCollectionServiceTest.class, 
	  EngineTestSuite.class,TrackingServiceTestSuite.class,
	  OifTestSuite.class,GtsTestSuite.class
  })
public class AllTests {

}
