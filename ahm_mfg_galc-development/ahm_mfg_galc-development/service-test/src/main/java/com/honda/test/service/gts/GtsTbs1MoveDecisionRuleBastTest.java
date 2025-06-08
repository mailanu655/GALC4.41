package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getService;

import com.honda.galc.service.gts.GtsTbs1TrackingService;

public abstract class GtsTbs1MoveDecisionRuleBastTest extends GtsMoveDecisionRuleBastTest {
	
	public static void loadTbsConfig() {
		initTbsArea();
		loadConfig("sql/service/gts/p1tbs.sql");
	}
	
	public static void initTbsArea() {
		TRACKING_AREA = "P1TBS";
		CARRIER_AREA= "PA_CARRIER";
		CLIENT_TERMINAL_NAME = "P1TBS_View";
		UNSOLICITED_DATA ="P1TBS.UNSOLICITED_DATA";
	}
	
	public GtsTbs1TrackingService getGtsService() {
		return getService(GtsTbs1TrackingService.class);
	}


}
