package com.honda.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.honda.test.service.gts.EtsTest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule01ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule02ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule04ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule05ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule08ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule09ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule10ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule11ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule12ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule13ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule14ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule15ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule16ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule17ATest;
import com.honda.test.service.gts.GtsPbs1MoveDecisionRule18ATest;
import com.honda.test.service.gts.GtsTbs1MoveDecisionRule01ATest;
import com.honda.test.service.gts.P1PBSTest;
import com.honda.test.service.gts.P1TBSTest;
import com.honda.test.service.gts.P1WBSTest;

@RunWith(Suite.class)
@SuiteClasses(
  { EtsTest.class,
	P1WBSTest.class,
	P1PBSTest.class,
	P1TBSTest.class,
	GtsPbs1MoveDecisionRule01ATest.class,
	GtsPbs1MoveDecisionRule02ATest.class,
	GtsPbs1MoveDecisionRule04ATest.class,
	GtsPbs1MoveDecisionRule05ATest.class,
	GtsPbs1MoveDecisionRule08ATest.class,
	GtsPbs1MoveDecisionRule09ATest.class,
	GtsPbs1MoveDecisionRule10ATest.class,
	GtsPbs1MoveDecisionRule11ATest.class,
	GtsPbs1MoveDecisionRule12ATest.class,
	GtsPbs1MoveDecisionRule13ATest.class,
	GtsPbs1MoveDecisionRule14ATest.class,
	GtsPbs1MoveDecisionRule15ATest.class,
	GtsPbs1MoveDecisionRule16ATest.class,
	GtsPbs1MoveDecisionRule17ATest.class,
	GtsPbs1MoveDecisionRule18ATest.class,
	GtsTbs1MoveDecisionRule01ATest.class})
public class GtsTestSuite {

}
