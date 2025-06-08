package com.honda.galc.client.qics.view.screen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicBorders;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.datacollection.view.action.ProductButtonAction;
import com.honda.galc.client.events.ProductSelectionEvent;
import com.honda.galc.client.qics.view.action.IdlePanelSubmitAction;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.ProductNumberPanel;
import com.honda.galc.client.qics.view.fragments.UnitInfoIdlePanel;
import com.honda.galc.client.qics.view.fragments.UnitInfoIdlePanel.UnitInfoConfig;
import com.honda.galc.client.qics.view.frame.QicsAutoProcessFrame;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.util.ReflectionUtils;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Idle screen for QICS application.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
@SuppressWarnings("serial")
public class IdlePanel extends QicsPanel implements DocumentListener, CaretListener, EventSubscriber<ProductSelectionEvent> {

	private static final long serialVersionUID = 1L;

	private static final int LABEL_WIDTH = 165;
	private static final int TEXT_FIELD_WIDTH = 200;
	private static final int LABEL_WIDTH_LONG = 365;
	private static final int TEXT_FIELD_WIDTH_LONG = 400;
	private static final int LABEL_WIDTH_SHORT = 340;
	private static final int TEXT_FIELD_WIDTH_SHORT = 140;
	private static final int ELEMENT_HEIGHT_SHORT = 40;

	private static final Map<String, String> panels = new HashMap<String, String>() {{
		put("Station Info", "com.honda.galc.client.qics.view.screen.QicsStationInfoPanel");
		put("Product List", "com.honda.galc.client.linesidemonitor.view.LineSideMonitorPanel");
	}};
	
	private ProductNumberPanel productNumberPanel;
	private JTabbedPane tabbedPane;
	
	public IdlePanel(QicsFrame frame) {
		super(frame);
		initializeGui();
		EventBus.subscribe(ProductSelectionEvent.class, this);
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.IDLE;
	}

	protected void initializeGui() {

		setLayout(new BorderLayout());
		setSize(1024, 620);

		setEnabled(true);

		productNumberPanel = createProductNumberPanel();
		tabbedPane = createTabbedPane();
		
		add(getProductNumberPanel(), BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);

		mapActions();
		mapEventHandlers();

	}

	private JTabbedPane createTabbedPane() {
		JTabbedPane pane = new JTabbedPane();
		String className;
		JPanel panel;
		for(String tab : getQicsController().getQicsPropertyBean().getIdlePanelTabs()) {
			className = panels.get(tab);
			panel = createPanel(className);
			if(panel != null) {
				pane.addTab(tab, panel);
			} else {
				pane.addTab(tab, new JLabel("Tab is not available: " + tab));
			}
		}
		return pane;
	}
	
	private JPanel createPanel(String className) {
		Class<?> panelClass;
		JPanel panel = null;
		if(className != null) {
			if(!StringUtils.isEmpty(className)) {
				try {
					panelClass = Class.forName(className);
					panel =  (JPanel) ReflectionUtils.createInstance(panelClass, new Class[] {MainWindow.class}, new Object[] {getMainWindow()});
				} catch (ClassNotFoundException e) {
				}
			}
		}
		return panel;
	}
	
	// === controlling api == //
	@Override
	public void startPanel() {
		setStationInfoValues();
		resetPanel();
	}

	protected String toString(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	protected String toString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	protected void setStationInfoValues() {
		QicsStationInfoPanel qicsStationInfoPanel = getQicsStationInfoPanel();
		if (qicsStationInfoPanel != null) {
			qicsStationInfoPanel.setStationInfoValues();
		}
	}
	
	public void resetPanel() {
		getProductNumberTextField().setText("");
		TextFieldState state = getQicsFrame().isUserLoggedIn() ? TextFieldState.EDIT : TextFieldState.DISABLED;
		state.setState(getProductNumberTextField());
		
		// if product queue is not empty pick up the next product
		if (getQicsFrame() instanceof QicsAutoProcessFrame){
			QicsAutoProcessFrame frame = (QicsAutoProcessFrame) getQicsFrame();
			if (frame.getProductQueue() != null && !frame.getProductQueue().isEmpty()) {
				getProductNumberTextField().setText(frame.getProductQueue().poll());
				getProductNumberTextField().getAction().actionPerformed(null);
			}
		}
		
		setFocus(getProductNumberTextField());
		getQicsFrame().clearMessage();
		getQicsFrame().clearStatusMessage();
	}

	// === get/set === //
	@Override
	public JComponent getDefaultElement() {
		return getProductNumberTextField();
	}

	public UpperCaseFieldBean getProductNumberTextField() {
		return getProductNumberPanel().getProductNumberTextField();
	}

	protected ProductNumberPanel getProductNumberPanel() {
		return productNumberPanel;
	}

	protected List<UnitInfoConfig> getIdlePanelCalculationConfigs() {
		return getClientConfig().getIdlePanelCalculationConfigs();
	}

	// === ui factory methods ===//
	protected ProductNumberPanel createProductNumberPanel() {
		ProductNumberPanel panel = new ProductNumberPanel(getQicsController());
//		panel.setLocation(20, 20);
		BasicBorders.MarginBorder border = new BasicBorders.MarginBorder();
		border.getBorderInsets(panel).set(20, 20, 20, 20);
		panel.setBorder(border);
		if(!getQicsController().getQicsPropertyBean().isProductButtonEnabled())
		{ 	
			panel.getProductNumberLabel().setFont(Fonts.DIALOG_PLAIN_36);
		}
		panel.getProductNumberTextField().setFont(Fonts.DIALOG_PLAIN_60);
		TextFieldState.EDIT.setState(panel.getProductNumberTextField());
		return panel;
	}

	protected UnitInfoIdlePanel createShortUnitInfoIdlePanel(UnitInfoConfig config, String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH_SHORT, TEXT_FIELD_WIDTH_SHORT, ELEMENT_HEIGHT_SHORT);
	}

	protected UnitInfoIdlePanel createShortUnitInfoIdlePanel(UnitInfoConfig config) {
		String labelText = getQicsController().getProductTypeData().getProductTypeLabel() + "s " + config.getName() + " ";
		UnitInfoIdlePanel panel = createShortUnitInfoIdlePanel(labelText);
		panel.setConfig(config);
		return panel;
	}

	protected UnitInfoIdlePanel createLongUnitInfoIdlePanel(String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH, TEXT_FIELD_WIDTH_LONG);
	}

	protected UnitInfoIdlePanel createUnitInfoIdlePanel(String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH_LONG, TEXT_FIELD_WIDTH);
	}

	protected UnitInfoIdlePanel createShortUnitInfoIdlePanel(String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH_SHORT, TEXT_FIELD_WIDTH_SHORT, ELEMENT_HEIGHT_SHORT);
	}

	protected int getAlignment(int elementWidth) {
		int width = this.getWidth();
		int x = 0;
		if (elementWidth > 0 && elementWidth < width) {
			x = x + (width - elementWidth) / 2;
		}
		return x;
	}

	protected JPanel createGroupPanel(JPanel groupPanel, List<UnitInfoIdlePanel> elements) {

		if (groupPanel == null || elements == null) {
			return groupPanel;
		}
		if (!elements.isEmpty()) {
			for (UnitInfoIdlePanel component : elements) {
				groupPanel.add(component);
			}

			UnitInfoIdlePanel element = elements.get(0);
			int w = element.getWidth();
			int h = element.getHeight();
			int count = groupPanel.getComponentCount();
			int maxNumOfRows = 7;
			if (count > maxNumOfRows) {
				w = w * 2;
				int height = (int) ((count * h) / 2);
				groupPanel.setSize(w, height);
				groupPanel.setLayout(new GridLayout(maxNumOfRows, 1, 0, 0));
			} else {
				groupPanel.setSize(w, count * h);
				groupPanel.setLayout(new GridLayout(count, 1, 0, 0));
			}
		}
		groupPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		return groupPanel;
	}

	
	@Override
	public void setButtonsState() {
	}
	
	// ================= event handlers mappings ========================//
	@Override
	protected void mapEventHandlers() {

		getProductNumberTextField().setAction(new IdlePanelSubmitAction(this));
		if(getQicsController().getQicsPropertyBean().isProductButtonEnabled())
		     getProductNumberButton().setAction(new ProductButtonAction(getMainWindow(), getApplicationProductTypeName(),getProductNumberButton().getName(),getProductNumberTextField()));       
		getProductNumberTextField().getDocument().addDocumentListener(this);
		getProductNumberTextField().addCaretListener(this);
	}
	
	public JButton getProductNumberButton()
    {
        return getProductNumberPanel().getProductNumberButton();
    }

	public void changedUpdate(DocumentEvent e) {
		
	}

	public void insertUpdate(DocumentEvent e) {
		processChange();
	}

	public void removeUpdate(DocumentEvent e) {
		processChange();
	}

	public void caretUpdate(CaretEvent e) {
		processChange();
	}
	
	protected void processChange() {
		if (getQicsFrame().getStatusMessagePanel().isError()) {
			TextFieldState.EDIT.setState(getProductNumberTextField());
			getQicsFrame().clearMessage();
		}
	}

	public static Map<String, String> getPanels() {
		return panels;
	}

	public void onEvent(ProductSelectionEvent event) {
		if(event.getProductType() == null || getQicsController().getProductType().equals(event.getProductType())) {
			productNumberPanel.getProductNumberTextField().setText(event.getProductId());
			productNumberPanel.getProductNumberTextField().grabFocus();
		}
	}

	protected JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	protected QicsStationInfoPanel getQicsStationInfoPanel() {
		for (Component comp : getTabbedPane().getComponents()) {
			if (comp instanceof QicsStationInfoPanel) {
				return (QicsStationInfoPanel) comp;
			}
		}
		return null;
	}	
}
