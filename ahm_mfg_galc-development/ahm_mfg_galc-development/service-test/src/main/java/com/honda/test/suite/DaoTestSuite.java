package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.dao.BuildAttributeCacheTest;

@RunWith(Suite.class)
@SuiteClasses(
  { BuildAttributeCacheTest.class
  })
public class DaoTestSuite {

}
