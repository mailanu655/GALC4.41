package com.honda.galc.client.qics.view.screen;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>LotControlPanel</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Apr 12, 2010</TD>
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
public class LotControlPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;

	public LotControlPanel(QicsFrame frame) {
		super(frame);
	}

	@Override
	public void startPanel() {

	}

	protected void setLotControlEnabled(boolean enabled) {
		int count = getQicsFrame().getMainPanel().getTabbedPane().getTabCount();
		for (int i = 0; i < count; i++) {
			Component component = getQicsFrame().getMainPanel().getTabbedPane().getComponentAt(i);
			if (component instanceof LotControlPanel) {
				getQicsFrame().getMainPanel().getTabbedPane().setSelectedComponent(component);
				getQicsFrame().getMainPanel().getTabbedPane().setEnabledAt(i, true);
			} else {
				getQicsFrame().getMainPanel().getTabbedPane().setEnabledAt(i, !enabled);
			}
		}

		if (enabled) {
			// do lot control
			for (JButton button : getQicsFrame().getMainPanel().getSubmitButtonsPanel().getButtons().values()) {
				button.setEnabled(false);
			}
		} else {
			// do qics
			for (int i = 0; i < count; i++) {
				Component component = getQicsFrame().getMainPanel().getTabbedPane().getComponentAt(i);
				if (!(component instanceof LotControlPanel)) {
					if (getQicsFrame().getMainPanel().getTabbedPane().isEnabledAt(i)) {
						getQicsFrame().getMainPanel().getTabbedPane().setSelectedComponent(component);
						break;
					}
				}
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setButtonsState();
				}
			});
		}
	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		getQicsFrame().getMainPanel().setButtonsState();
		if (getQicsFrame().getMainPanel().getSelectedPanel() == this) {

		}
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.LOT_CONTROL;
	}
	
	
}
