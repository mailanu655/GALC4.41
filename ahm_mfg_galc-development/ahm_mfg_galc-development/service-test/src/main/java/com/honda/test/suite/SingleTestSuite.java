package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.dao.BuildAttributeCacheTest;
import com.honda.test.service.gts.EtsTest;
import com.honda.test.service.gts.P1PBSTest;
import com.honda.test.service.gts.P1WBSTest;

@RunWith(Suite.class)
@SuiteClasses(
  {BuildAttributeCacheTest.class})
public class SingleTestSuite {

}
