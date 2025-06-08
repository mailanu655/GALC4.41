package com.honda.galc.client.qi.repairentry;

import java.util.ArrayList;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiRepairResultDto;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>RepairEntryView</code> is the Panel class for repair entry screen with
 * product Id and home screen with UPC.
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
 * <TD>L&T Infotech</TD>
 * <TD>15/11/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class RepairEntryView extends AbstractRepairEntryView<RepairEntryModel, RepairEntryController> {

	public RepairEntryView(MainWindow window) {
		super(ViewId.REPAIR_ENTRY, window);
		EventBusUtil.register(this);
	}

	@Override
	public void reload() {
		if (isProductScraped)  {
			EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED, StatusMessageEventType.WARNING));
		}
		else  {
			onTabSelected();
		}
	}

	@Override
	public void start() {
	}

	@Override
	public void initView() {
		setParentCachedDefectList(new ArrayList<QiRepairResultDto>());
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		
		MigPane pane = new MigPane("insets 5 5 5 5 ", "[center,grow,fill]", "");
		pane.add(getMainTablePane(),"span,wrap");
		pane.add(getExistingProductAssignment(),"span,wrap");
		pane.add(createRepairOptionsPanel(),"span,wrap");
		this.setCenter(pane);
		reload();
	}
	
	@Override
	public void onTabSelected(){
		Logger.getLogger().check("Repair Entry panel Selected");
		getController().reload();
	}
}
