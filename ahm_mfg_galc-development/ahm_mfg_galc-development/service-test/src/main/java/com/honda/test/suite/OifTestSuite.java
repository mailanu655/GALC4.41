package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.oif.priorityplan.EnginePriorityPlanTest;

@RunWith(Suite.class)
@SuiteClasses(
  { EnginePriorityPlanTest.class
  })
public class OifTestSuite {

}
