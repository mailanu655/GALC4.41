package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getService;

import com.honda.galc.service.gts.GtsPbs1TrackingService;

public abstract class GtsPbs1MoveDecisionRuleBastTest extends GtsMoveDecisionRuleBastTest {
	
	public static void loadPbsConfig() {
		initPbsArea();
		loadConfig("sql/service/gts/p1pbs_move_decision.sql");
	}
	
	public static void initPbsArea() {
		TRACKING_AREA = "P1PBS";
		CARRIER_AREA= "PA_CARRIER";
		CLIENT_TERMINAL_NAME = "P1PBS_View";
		UNSOLICITED_DATA ="P1PBS.UNSOLICITED_DATA";
	}
	
	public GtsPbs1TrackingService getGtsService() {
		return getService(GtsPbs1TrackingService.class);
	}


}
