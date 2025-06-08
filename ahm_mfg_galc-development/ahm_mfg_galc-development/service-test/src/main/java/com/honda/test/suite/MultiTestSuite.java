package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
	{
		GtsTestSuite.class,
		DataCollectionTestSuite.class
    }
)
public class MultiTestSuite {

}
