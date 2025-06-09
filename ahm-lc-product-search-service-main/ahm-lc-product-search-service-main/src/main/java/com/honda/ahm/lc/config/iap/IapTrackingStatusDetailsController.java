package com.honda.ahm.lc.config.iap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.honda.ahm.lc.vdb.rest.TrackingStatusDetailsController;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>Aap1DataController</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 12, 2022
 */

@Controller
@RequestMapping(path = "iap/trackingStatusDetails")
public class IapTrackingStatusDetailsController extends TrackingStatusDetailsController<IapDataService> {

	@Autowired
	public IapTrackingStatusDetailsController() {
		super();
	}

}
