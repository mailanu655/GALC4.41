package com.honda.galc.client.product.view;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.conf.Application;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductFrame</code> is ... .
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
public class ProductFrame extends MainWindow {

	private static final long serialVersionUID = 1L;
	protected static final int STATUS_PANEL_HIGHT = 75;

	public ProductFrame(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		init();
	}

	protected void init() {
		setSize(1024, 768);
		setResizable(false);
		String iconUrl = "/resource/images/common/hcm.gif";
		try {
			ImageIcon frameIcon = new ImageIcon(getClass().getResource(iconUrl));
			setIconImage(frameIcon.getImage());
		} catch (Exception e) {
			System.err.println("Failed to load frame icon from url:'" + iconUrl + "'");
		}
		ProductPanel productPanel = createClientPanel();
		setClientPanel(productPanel);
		productPanel.loadTabs(getViewNames());
		productPanel.getController().toIdle();
	}

	protected String[] getViewNames() {
		String property = getApplicationProperty("PANELS");
		String[] panels = null;
		if (StringUtils.isEmpty(property)) {
			panels = new String[] {};
		} else {
			panels = property.split(Delimiter.COMMA);
		}
		return panels;
	}

	protected ProductPanel createClientPanel() {
		ProductPanel panel = new ProductPanel(this);
		return panel;
	}

	// === get/set === //
	protected ProductPanel getProductPanel() {
		return (ProductPanel) clientPanel;
	}
}
