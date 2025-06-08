package com.honda.galc.client.qics.view.screen;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.ProductInfoPanel;
import com.honda.galc.client.qics.view.fragments.SubmitButtonsPanel;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>MainPanel</code> is ...
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
 * <TD>Mar 12, 2009</TD>
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
 /**
 * Added Repair In Panel
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public class MainPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;
	private ProductInfoPanel productInfoPanel;
	private JTabbedPane tabbedPane;
	private Map<QicsViewId, QicsPanel> inputPanels;
	public MainPanel(QicsFrame frame) {
		super(frame);
		inputPanels = new HashMap<QicsViewId, QicsPanel>();
		initialize();
	}

	protected void initialize() {
		setLayout(null);
		setSize(1025, 750);

		productInfoPanel =  new ProductInfoPanel(getQicsController());
		getProductInfoPanel().setLocation(10, 10);
		createInputPanels();

		tabbedPane = createTabbedPanel();
		addPanelsToTabbedPanel();

		add(getProductInfoPanel());
		add(getTabbedPane());
		
		mapSubmitActions();
		mapActions();
		mapEventHandlers();
	}

	// === controlling api === //
	// TODO consider creating startPanelOnNewClient, onNewProduct, onDisplay
	@Override
	public void startPanel() {
		getProductInfoPanel().resetPanel(getProductModel());
		if (getQicsController().isProductProcessable()) {
			if (getQicsController().isProductPreCheckResultsExist() || getQicsController().isProductWarnCheckResultsExist()) {
				QicsPanel panel = getInputPanels().get(QicsViewId.PRE_CHECK_RESULTS);
				if (getTabbedPane().indexOfComponent(panel) == -1) {
					addPanel(panel, 0);
				}
			}
		}
		setTabEnabled(getQicsController().isProductProcessable());
		startProduct();
		
		Component newSelectedComponent = null;
		
		for (int i = 0; i < getTabbedPane().getTabCount(); i++) {
			if (getTabbedPane().isEnabledAt(i)) {
				newSelectedComponent = getTabbedPane().getComponentAt(i);
				break;
			}
		}
		if (newSelectedComponent != null) {
			getTabbedPane().setSelectedComponent(newSelectedComponent);
		}
		setButtonsState();
	}
	
	@Override
	public void startProduct() {
		int count = getTabbedPane().getTabCount();
		for (int i = 0; i < count; i++) {
			QicsPanel panel = (QicsPanel) getTabbedPane().getComponentAt(i);
			panel.startProduct();
		}
	}	

	protected void startSelectedPanel() {
		setButtonsState();
		QicsPanel selectedPanel = getSelectedPanel();
		if (selectedPanel != null) {
			selectedPanel.startPanel();
			getLogger().info("Panel " + selectedPanel.getClass().getSimpleName() + " is selected");
		}
	}

	@Override
	public void setButtonsState() {
		ActionId submitActionId = getQicsController().getSubmitActionId(getSelectedPanel());
		setButtonAction(submitActionId, getSubmitButtonsPanel().getSubmitButton());

		if (getQicsController().isProductProcessable()) {
			getSubmitButtonsPanel().setButtonsEnabled(true);
			setScrapButtonState();
			setFocus(getSubmitButtonsPanel().getSubmitButton());
		} else {
			getSubmitButtonsPanel().setButtonsEnabled(false);
			getSubmitButtonsPanel().getCancelButton().setEnabled(true);
			setFocus(getSubmitButtonsPanel().getCancelButton());
		}
	}

	public void setScrapButtonState() {
		JButton scrapButton = getSubmitButtonsPanel().getButtons().get(ActionId.SCRAP);
		if (scrapButton == null) {
			return;
		}
		boolean defectSelected = false;
		if (getSelectedPanel() instanceof DefectRepairPanel) {
			defectSelected = ((DefectRepairPanel) getSelectedPanel()).isDefectSelected();
		} else if (getSelectedPanel() instanceof DefectPictureInputPanel) {
			defectSelected = ((DefectPictureInputPanel) getSelectedPanel()).isDefectSelected();
		} else if (getSelectedPanel() instanceof DefectTextInputPanel) {
			defectSelected = ((DefectTextInputPanel) getSelectedPanel()).isDefectSelected();
		}
		scrapButton.setEnabled(defectSelected);
	}

	protected void setTabEnabled(boolean enabled) {
		int count = getTabbedPane().getTabCount();
		for (int i = 0; i < count; i++) {
			if (enabled) {
				getTabbedPane().setEnabledAt(i, enabled);
			} else {
				Component component = getTabbedPane().getComponentAt(i);
				if (component instanceof DefectRepairPanel || component instanceof HistoryPanel) {
					getTabbedPane().setEnabledAt(i, true);
				} else {
					getTabbedPane().setEnabledAt(i, false);
				}
			}
		}
	}

	public void displayPreCheckResultsPanel() {
		QicsPanel panel = getInputPanels().get(QicsViewId.PRE_CHECK_RESULTS);
		if (getTabbedPane().indexOfComponent(panel) == -1) {
			addPanel(panel, 0);
		}
		getTabbedPane().setSelectedComponent(panel);
	}

	public void displayCheckResultsPanel() {
		removePanel(QicsViewId.DUNNAGE);
		QicsPanel panel = getInputPanels().get(QicsViewId.CHECK_RESULTS);
		if (getTabbedPane().indexOfComponent(panel) == -1) {
			addPanel(panel);
		}
		getTabbedPane().setSelectedComponent(panel);
		getQicsController().playWarnSound();
	}

	public void displayDunnagePanel() {
		removePanel(QicsViewId.CHECK_RESULTS);
		QicsPanel panel = getInputPanels().get(QicsViewId.DUNNAGE);
		if (getTabbedPane().indexOfComponent(panel) == -1) {
			addPanel(panel);
		}
		panel.putClientProperty("FROM_ACTION", true);
		getTabbedPane().setSelectedComponent(panel);
	}

	public void addPanel(QicsPanel panel) {
		addPanel(panel, -1);
	}

	public void addPanel(QicsPanel panel, int ix) {
		if (panel == null) {
			return;
		}
		if (ix < 0 || ix > getComponentCount()) {
			getTabbedPane().addTab(panel.getQicsViewId().getLabel(), panel);
		} else {
			getTabbedPane().insertTab(panel.getQicsViewId().getLabel(), null, panel, panel.getQicsViewId().getLabel(), ix);
		}
		getTabbedPane().setMnemonicAt(tabbedPane.indexOfComponent(panel), panel.getQicsViewId().getKeyEvent());
	}
	
	public void resetSelectedPanelIndex() {
		getTabbedPane().getModel().setSelectedIndex(-1);
	}

	public void removePanel(QicsViewId viewId) {
		if (viewId == null) {
			return;
		}
		QicsPanel panel = getInputPanels().get(viewId);
		removePanel(panel);
	}

	public void removePanel(QicsPanel panel) {
		if (panel == null) {
			return;
		}
		int ix = getTabbedPane().indexOfComponent(panel);
		if (ix > -1) {
			getTabbedPane().remove(ix);
		}
	}

	// === factory methods === //
	protected void createInputPanels() {

		List<QicsViewId> viewsIds = getClientConfig().getInputViewIds();
		QicsPanel panel = null;
		for(QicsViewId viewId : viewsIds) {
			panel = viewId.createQicsPanel(getQicsFrame());
			inputPanels.put(viewId, panel);
		}
	
		// === implicit panels === //
		inputPanels.put(QicsViewId.PRE_CHECK_RESULTS, QicsViewId.PRE_CHECK_RESULTS.createQicsPanel(getQicsFrame()));
		inputPanels.put(QicsViewId.CHECK_RESULTS, QicsViewId.CHECK_RESULTS.createQicsPanel(getQicsFrame()));
		if (getClientConfig().isDunnageRequired()) {
			inputPanels.put(QicsViewId.DUNNAGE, QicsViewId.DUNNAGE.createQicsPanel(getQicsFrame()));
		}
		inputPanels.put(QicsViewId.REPAIR_IN, QicsViewId.REPAIR_IN.createQicsPanel(getQicsFrame()));
		
	}

	protected JTabbedPane createTabbedPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setSize(1000, 535);
		tabbedPane.setLocation(getProductInfoPanel().getX(), getProductInfoPanel().getY() + getProductInfoPanel().getHeight() + 2);
		if (getClientConfig().isScreenTouch()) {
			tabbedPane.setFont(Fonts.DIALOG_BOLD_16);
		}
		return tabbedPane;
	}

	protected void addPanelsToTabbedPanel() {
		for (QicsViewId viewId : getClientConfig().getInputViewIds()) {
			QicsPanel inputPanel = getInputPanels().get(viewId);
			if (inputPanel != null) {
				addPanel(inputPanel);
			}
		}
	}

	// === get/set === //
	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.MAIN;
	}

	public SubmitButtonsPanel getSubmitButtonsPanel() {
		return getProductInfoPanel().getSubmitButtonsPanel();
	}

	public ProductInfoPanel getProductInfoPanel() {
		return productInfoPanel;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public QicsPanel getSelectedPanel() {
		Component component = getTabbedPane().getSelectedComponent();
		if (component instanceof QicsPanel) {
			QicsPanel selectedPanel = (QicsPanel) component;
			return selectedPanel;
		}
		return null;
	}

	protected Map<QicsViewId, QicsPanel> getInputPanels() {
		return inputPanels;
	}

	public QicsPanel getInputPanel(QicsViewId qicsViewId) {
		return getInputPanels().get(qicsViewId);
	}

	// === action/handler mapping === //
	@Override
	protected void mapEventHandlers() {
		super.mapEventHandlers();

		getTabbedPane().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				if (getQicsController().isProductProcessable()) {
					getQicsFrame().clearMessage();
				}		
				if(getQicsController().getQicsPropertyBean().isTimeoutInQicsProcess()) {
					getQicsFrame().setTimeout();
				}
				startSelectedPanel();
			}
		});
	}

	protected void mapSubmitActions() {
		if (getSubmitButtonsPanel() != null) {
			mapButtonActions(getSubmitButtonsPanel().getButtons());
		}
	}
	
	public void checkAlarmDialog() {
		if (getSelectedPanel() != null) {
			if ((getSelectedPanel() instanceof PreCheckResultsPanel) && getQicsController().getQicsPropertyBean().isProductCheckPlaySound()) {
				AudioPropertyBean property = PropertyService.getPropertyBean(AudioPropertyBean.class,getQicsController().getProcessPointId());
				final ClientAudioManager audioMgr =new ClientAudioManager(property);

				try {
  					Runnable audioReset = new Runnable() {
						public void run() {
							try {
								audioMgr.playRepeatedNgSound();
							} catch (Exception ex) {
								getLogger().info("playSound : An Exception occured spawning a thread to play"
										+ " audioManager clips! :: "
										+ " "
										+ ex.getMessage());
							}
						}
					};
					new Thread(audioReset).start();
				} catch (Exception ex) {
					getLogger().info("playSound : An Exception occured spawning a thread to play"
							+ " audioManager clips! :: "
							+ " "
							+ ex.getMessage());
				}
				JOptionPane
	            .showMessageDialog(
	                    getQicsFrame(),
	                    "Click OK to silence alarm",
	                    "Silence Alarm",
	                    JOptionPane.WARNING_MESSAGE);
				audioMgr.stopRepeatNgSound();
			}
		}
	}
	
	public void displayRepairInPanel() {
		QicsPanel panel = getInputPanels().get(QicsViewId.REPAIR_IN);
		if (getTabbedPane().indexOfComponent(panel) == -1) {
			addPanel(panel);
		}
		getTabbedPane().setSelectedComponent(panel);
	}
}
