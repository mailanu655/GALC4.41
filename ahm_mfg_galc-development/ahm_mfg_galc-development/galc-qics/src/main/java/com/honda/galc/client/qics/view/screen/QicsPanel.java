package com.honda.galc.client.qics.view.screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellRenderer;

import com.honda.galc.client.qics.config.QicsClientConfig;
import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.model.ClientModel;
import com.honda.galc.client.qics.model.ProductModel;
import com.honda.galc.client.qics.property.QicsPropertyBean;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.ActionButtonsPanel;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.util.DefectTableCellRenderer;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Abstract panel for qics screen.
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
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public abstract class QicsPanel extends ApplicationMainPanel {
	private static final long serialVersionUID = 1L;

	private Map<ActionId, Action> actions;

	public QicsPanel(QicsFrame frame) {
		super(frame);
		init();
	}

	private void init() {
		setActions(new HashMap<ActionId, Action>());
	}

	// === controlling api === //
	public void startCleint() {
	}

	public void startProduct() {
	}

	public abstract void startPanel();

	public void setButtonsState() {
	}

	public void requestFocus() {
		setFocus(getDefaultElement());
	}

	public void setFocus(final JComponent component) {
		if (component == null) {
			return;
		}
		component.requestFocusInWindow();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				component.requestFocusInWindow();
			}
		});
	}

	// == get/set === //
	public abstract QicsViewId getQicsViewId();

	public ProductModel getProductModel() {
		return getQicsController().getProductModel();
	}

	public ClientModel getClientModel() {
		return getQicsController().getClientModel();
	}

	public QicsController getQicsController() {
		return getQicsFrame().getQicsController();
	}
	
	public QicsPropertyBean getQicsPropertyBean() {
		return getQicsController().getQicsPropertyBean();
	}

	public QicsClientConfig getClientConfig() {
		return getQicsController().getClientConfig();
	}

	public QicsFrame getQicsFrame() {
		return (QicsFrame)getMainWindow();
	}

	protected Collection<ActionId> getActionButtonCodes() {
		return new ArrayList<ActionId>();
	}

	public JComponent getDefaultElement() {
		return null;
	}

	protected ActionButtonsPanel getActionButtonsPanel() {
		return null;
	}

	private Map<ActionId, Action> getActions() {
		return actions;
	}

	private void setActions(Map<ActionId, Action> actions) {
		this.actions = actions;
	}

	protected int getTabPaneWidth() {
		return 1000;
	}

	protected int getTabPaneHeight() {
		return 500;
	}

	// === factory methods ===//
	public TableCellRenderer createDefectTableCellRenderer() {
		return new DefectTableCellRenderer(getClientConfig());
	}

	// ============= event handlers mappings ======================//
	protected void mapEventHandlers() {
	}

	// ================= action mappings ========================//
	protected void mapActions() {
		if (getActionButtonsPanel() != null) {
			mapButtonActions(getActionButtonsPanel().getButtons());
		}
	}

	protected void mapButtonActions(Map<ActionId, JButton> buttons) {
		if (buttons == null || buttons.isEmpty()) {
			return;
		}
		for (Entry<ActionId, JButton> entry : buttons.entrySet()) {
			ActionId actionId = entry.getKey();
			JButton button = entry.getValue();
			if (button != null) {
				setButtonAction(actionId, button);
			}
		}
	}

	protected void setButtonAction(ActionId actionId, JButton button) {
		if (actionId == null || button == null) {
			return;
		}
		Action action = getAction(actionId);
		button.setAction(action);
		button.getInputMap().put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), actionId);
		button.getActionMap().put(actionId, action);
	}

	protected Action getAction(ActionId actionId) {
		Action action = getActions().get(actionId);
		if (action == null) {
			action = actionId.createAction((QicsPanel)this);
			Logger.getLogger().debug("create action : " + action.getClass().getSimpleName() );
			getActions().put(actionId, action);
		}
		return action;
	}
	
	/**
	 * this method handles general list selection events
	 * to use this, the sub class of this QcisPanel has to implements ListSelectionListener
	 * and register the listener to the corresponding list
	 * alse the sub class has to implement deselected and selected class
	 * @param e
	 */
	
	public void valueChanged(ListSelectionEvent e) {
		if (!getQicsController().isProductProcessable()) {
			return;
		}
		ListSelectionModel model = (ListSelectionModel) e.getSource();
		try {
			getQicsFrame().setWaitCursor();
			getQicsFrame().clearMessage();

			if (model.isSelectionEmpty()) {
				deselected(model);
			} else {
				if (e.getValueIsAdjusting()) {
					return;
				}
				selected(model);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			getQicsFrame().displayException(ex);
		} finally {
			getQicsFrame().setDefaultCursor();
		}
	}
	
	protected void deselected(ListSelectionModel model) {
		
	}
	
	protected void selected(ListSelectionModel model) {
		
	}
	
}
