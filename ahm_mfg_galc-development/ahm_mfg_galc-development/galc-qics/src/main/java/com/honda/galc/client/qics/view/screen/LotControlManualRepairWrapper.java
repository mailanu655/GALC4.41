package com.honda.galc.client.qics.view.screen;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.teamleader.ManualLotControlRepairPanel;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>LotControlManualRepairWrapper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlManualRepairWrapper description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Apr 15, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 15, 2011
 */

public class LotControlManualRepairWrapper extends QicsPanel {
	private static final long serialVersionUID = 1L;
	private ManualLotControlRepairPanel panel;

	public LotControlManualRepairWrapper(QicsFrame frame) {
		super(frame);
		initialize();
	}

	protected void initialize() {
		try {
			setLayout(new BorderLayout());
			setSize(getTabPaneWidth(), getTabPaneHeight());
			panel = createLotControlRepairPanel();
			add(panel);
		} catch (Exception e) {
			Logger.getLogger().error(e, "exception init recovery panel.");
		}
	}

	private ManualLotControlRepairPanel createLotControlRepairPanel() {
		return new ManualLotControlRepairPanel(getQicsFrame()){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void initComponents() {
				
				setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
				getProductIdPanel();
				getResetButton().setVisible(false);
				add(getPartStatusPanel());
				add(getButtonPanel());
			}
			
		};
		
	}

	@Override
	public void startPanel() {
	    panel.loadProductId(getProductModel().getInputNumber());
		setButtonsState();
	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		getQicsFrame().getMainPanel().setButtonsState();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.RECOVERY;
	}
	
}
