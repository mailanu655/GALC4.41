package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.service.datacollection.DataCollectionServiceTest;
import com.honda.test.service.datacollection.GSmurfTest;

@RunWith(Suite.class)
@SuiteClasses(
	{
		GSmurfTest.class,
		DataCollectionServiceTest.class,
    }
)
public class DataCollectionTestSuite {

}
