package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.service.tracking.FrameTrackingServiceTest;

@RunWith(Suite.class)
@SuiteClasses(
  { FrameTrackingServiceTest.class
  })
public class TrackingServiceTestSuite {

}
