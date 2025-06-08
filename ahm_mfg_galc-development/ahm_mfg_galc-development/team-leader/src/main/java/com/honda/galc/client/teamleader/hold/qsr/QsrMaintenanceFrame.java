package com.honda.galc.client.teamleader.hold.qsr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrMaintenanceFrame</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class QsrMaintenanceFrame extends TabbedMainWindow {

	private static final long serialVersionUID = 1L;

	public QsrMaintenanceFrame(ApplicationContext appContext, Application application) {
		super(appContext, application);
	}

	@Override
	protected void init() {
		loadIcon();
		super.init();
	}


	@Override
    protected List<String> loadPanelConfigs() {
    	List<String> panelIds = new ArrayList<String>();
    	QsrMaintenancePropertyBean property = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, application.getApplicationId());
    	panelIds = Arrays.asList(property.getPanels());
    	if(panelIds.isEmpty()) panelIds.add("BLANK");
    	return panelIds;
    }

	protected void loadIcon() {
		String iconUrl = "/resource/images/common/hcm.gif";
		try {
			ImageIcon frameIcon = new ImageIcon(getClass().getResource(iconUrl));
			setIconImage(frameIcon.getImage());
		} catch (Exception e) {
			System.err.println("Failed to load frame icon from url:'" + iconUrl + "'");
		}
	}
}
