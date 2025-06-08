package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.engine.AEBlockLoadEngineNumberingTest;
import com.honda.test.engine.AEShippingTest;

@RunWith(Suite.class)
@SuiteClasses(
  { AEBlockLoadEngineNumberingTest.class, AEShippingTest.class
  })
public class EngineTestSuite {

}
