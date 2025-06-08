package com.honda.galc.client.product.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.product.controller.ProductController;
import com.honda.galc.client.product.controller.listener.ActionAdapter;
import com.honda.galc.client.product.controller.listener.CancelAction;
import com.honda.galc.client.product.controller.listener.DoneAction;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.controller.listener.InputNumberListener;
import com.honda.galc.client.product.process.view.ProcessView;
import com.honda.galc.client.product.process.view.ProcessViewFactory;
import com.honda.galc.client.product.view.fragments.InfoPanel;
import com.honda.galc.client.product.view.fragments.ProductInfoPanel;
import com.honda.galc.client.product.view.fragments.ProductInputPanel;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.entity.product.ProductTypeData;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductPanel</code> is ... .
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
public class ProductPanel extends ApplicationMainPanel {

	private static final long serialVersionUID = 1L;

	private ProductController controller;

	private JPanel togglePanel;
	private ProductInputPanel inputPanel;
	private ProductInfoPanel infoPanel;

	private JTabbedPane tabbedPanel;

	private ActionListener inputNumberListener;

	// === model === //
	public ProductPanel(MainWindow window) {
		super(window);
		this.controller = new ProductController(this);
		this.inputNumberListener = new InputNumberListener(this);
		initView();
		mapActions();
		AnnotationProcessor.process(getController());
	}

	// === life cycle ===//
	protected void initView() {
		ProductTypeData productTypeData = getProductTypeData();
		this.inputPanel = new ProductInputPanel(productTypeData);
		this.infoPanel = new ProductInfoPanel(productTypeData);
		this.togglePanel = createTogglePanel();
		this.tabbedPanel = createTabbedPanel();
		setLayout(new MigLayout("", "[grow,fill]", "[][grow,fill]"));
		setName("ProductPanel");
		add(getTogglePanel(), "wrap");
		add(getTabbedPanel());
	}

	protected void mapActions() {
		getInputPanel().getNumberTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getInputPanel().getNumberTextField()));
		getInputPanel().getNumberTextField().addActionListener(getInputNumberListener());
		getInfoPanel().getCancelButton().setAction(new ActionAdapter<ProductPanel>("Cancel", KeyEvent.VK_C, new CancelAction(this)));
		getInfoPanel().getDoneButton().setAction(new ActionAdapter<ProductPanel>("Next", KeyEvent.VK_N, new DoneAction(this)));

		getTabbedPanel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				Component component = getTabbedPanel().getSelectedComponent();
				if (component instanceof ProcessView) {
					((ProcessView) component).getController().update();
				}
			}
		});

		getInfoPanel().getDoneButton().getInputMap().put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "done");
		getInfoPanel().getDoneButton().getActionMap().put("done", getInfoPanel().getDoneButton().getAction());
		getInfoPanel().getCancelButton().getInputMap().put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "cancel");
		getInfoPanel().getCancelButton().getActionMap().put("cancel", getInfoPanel().getCancelButton().getAction());
	}

	// === factory === //
	protected JPanel createTogglePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new CardLayout());
		panel.add(getInputPanel(), "input");
		panel.add(getInfoPanel(), "info");
		return panel;
	}

	protected JTabbedPane createTabbedPanel() {
		JTabbedPane panel = new JTabbedPane();
		panel.setRequestFocusEnabled(true);
		return panel;
	}

	public void loadTabs(String... args) {
		if (args == null) {
			return;
		}
		for (String name : args) {
			if (name == null) {
				continue;
			}
			name = name.trim();
			Component comp = ProcessViewFactory.create(name, getMainWindow());
			if (comp instanceof ProcessView) {
				ProcessView pv = (ProcessView) comp;
				addTab(pv.getController().getProcessName(), comp, pv.getController().getMnemonicKey());
			} else {
				addTab(comp.getClass().getSimpleName(), comp, 0);
			}
		}
	}

	protected void loadTestTabs() {
		InfoPanel info = new InfoPanel();
		addTab("Info", info, KeyEvent.VK_I);
		addTab("Lot Control 1", new JPanel(), KeyEvent.VK_1);
		addTab("Lot Control 2", new JPanel(), KeyEvent.VK_2);
		addTab("Lot Control 3", new JPanel(), KeyEvent.VK_3);
	}

	protected void addTab(String title, Component tab, int mnemonic) {
		if (tab == null) {
			return;
		}
		if (title == null) {
			title = tab.getClass().getSimpleName();
		}
		getTabbedPanel().add(title, tab);
		getTabbedPanel().setMnemonicAt(getTabbedPanel().indexOfComponent(tab), mnemonic);
	}

	// === //
	@Override
	public void clearErrorMessage() {
		super.clearErrorMessage();
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}

	public void setErrorMessage(String errorMessage, JTextField textField) {
		setErrorMessage(errorMessage);
		TextFieldState.ERROR.setState(textField);
		textField.selectAll();
	}

	@Override
	public void setMessage(String message) {
		super.setMessage(message);
	}

	@Override
	public void setStatusMessage(String message) {
		super.setMessage(message);
	}

	public Component getSelectedTab() {
		return getTabbedPanel().getSelectedComponent();
	}

	// === get/set ===//
	public ProductInputPanel getInputPanel() {
		return inputPanel;
	}

	public JTabbedPane getTabbedPanel() {
		return tabbedPanel;
	}

	public ProductInfoPanel getInfoPanel() {
		return infoPanel;
	}

	public JPanel getTogglePanel() {
		return togglePanel;
	}

	public ProductController getController() {
		return controller;
	}

	public ProductTypeData getProductTypeData() {
		return getMainWindow().getApplicationContext().getProductTypeData();
	}

	public ActionListener getInputNumberListener() {
		return inputNumberListener;
	}
}
