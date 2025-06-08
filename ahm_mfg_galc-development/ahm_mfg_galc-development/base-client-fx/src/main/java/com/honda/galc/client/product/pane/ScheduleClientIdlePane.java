package com.honda.galc.client.product.pane;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ScheduleMainPanel;
import com.honda.galc.client.schedule.SchedulingEvent;
import com.honda.galc.client.schedule.SchedulingEventType;
import com.honda.galc.client.ui.EventBusUtil;

/**
 * 
 * 
 * <h3>ScheduleClientIdlePane Class description</h3>
 * <p> ScheduleClientIdlePane description </p>
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
 *
 * </TABLE>
 *   
 * @author Shweta Kadav<br>
 * Oct 6 2016
 *
 *
 */
public class ScheduleClientIdlePane extends AbstractProductIdlePane{
	
	
	public ScheduleClientIdlePane(ProductController productController) {
		super(productController);
	}
	
	@Override
	protected void initComponents() {
		ScheduleMainPanel panel = new ScheduleMainPanel(getProductController().getView().getMainWindow());
		setCenter(panel);
	}

	// If schedule client is displayed, then refresh it
	@Override
	public void toIdle() {
		EventBusUtil.publish(new SchedulingEvent(null, SchedulingEventType.REFRESH_SCHEDULE_CLIENT));
		
	}

	@Override
	public String getName() {
		return "Schedule Client";
	}

	
}
