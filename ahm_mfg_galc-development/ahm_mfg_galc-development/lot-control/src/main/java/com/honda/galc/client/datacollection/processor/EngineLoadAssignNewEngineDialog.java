package com.honda.galc.client.datacollection.processor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.util.EngineLoadUtility;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.EngineLoadPropertyBean;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameEngineModelMapDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import net.miginfocom.swing.MigLayout;

public class EngineLoadAssignNewEngineDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private Frame frame;
	private Engine engine;
	private String engineSn;
	private LabeledComboBox engYmtoComboBox;
	private JCheckBox engFiredCheckBox;
	private JButton cancelButton;
	private JButton submitButton;
	private ClientContext ownerContext;
	private String suffix;
	private EngineLoadUtility engineUtil;

	public EngineLoadAssignNewEngineDialog(Frame frame, String engineSn, String suffix, ClientContext ownerContext) {
		super(ownerContext.getFrame(), "ENGINE LOAD: Add New Engine");
		this.frame = frame;
		this.engineSn = engineSn;
		this.suffix = suffix;
		this.ownerContext = ownerContext;
		this.setSize(500, 400);
		this.initView();
		this.addListeners();
	}

	protected void initView() {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		
		this.setLayout(new MigLayout());
		this.add(this.getWarningLabel(),"spanx, center, wrap, gapbottom 10");
		this.add(this.getVinTextField(),"wrap, gapleft 76, al right");
		this.add(this.getEngSnTextField(),"wrap, gapleft 22, al right");
		this.add(this.getEngYmtoComboBox(),"wrap");
		this.add(this.getEngFiredPanel(),"wrap, gapleft 10, growx");
		this.add(this.getButtonsPanel(),"spanx, growx, center");

		this.getRootPane().setDefaultButton(getCancelButton());
	}
	
	protected JLabel getWarningLabel() {
		JLabel warningLabel = new JLabel("The following engine data will be created: " );
		warningLabel.setFont(this.getLabelFont());
		warningLabel.setForeground(Color.RED);
		return warningLabel;
	}
	
	protected LabeledTextField getVinTextField() {
		return this.createLabeledTextField("VIN: ", this.frame.getId());
	}
	
	protected LabeledTextField getEngSnTextField() {
		return this.createLabeledTextField("Engine SN: ", this.engineSn);
	}
	
	protected LabeledTextField createLabeledTextField(String title, String text) {
		LabeledTextField labeledTextField = new LabeledTextField(title);
		labeledTextField.setName(title + "LabeledTextField");
		labeledTextField.getLabel().setFont(this.getLabelFont());
		labeledTextField.getComponent().setText(text);
		labeledTextField.getComponent().setFont(this.getInputFont());
		labeledTextField.getComponent().setEditable(false);
		return labeledTextField;
	}
	
	protected LabeledComboBox getEngYmtoComboBox() {
		if (this.engYmtoComboBox == null) {
			this.engYmtoComboBox = new LabeledComboBox("Engine YMTO: ");
			FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(this.frame.getProductSpecCode());
			for (String engYmto : this.getEngYmtoList(frameSpec))
				this.engYmtoComboBox.getComponent().addItem(engYmto);
			this.engYmtoComboBox.getComponent().setSelectedItem(frameSpec.getEngineMto().trim());
			this.engYmtoComboBox.getComponent().setFont(this.getInputFont());
			this.engYmtoComboBox.getComponent().setBackground(Color.WHITE);
			this.engYmtoComboBox.getComponent().setName("EngineYmtoComboBox");
			this.engYmtoComboBox.getLabel().setFont(this.getLabelFont());
			this.engYmtoComboBox.getLabel().setMinimumSize(new Dimension(100,40));
		}
		return this.engYmtoComboBox;
	}
	
	protected JPanel getButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new MigLayout("center"));
		buttonsPanel.add(this.getSubmitButton());
		buttonsPanel.add(this.getCancelButton());
		return buttonsPanel;
	}
	
	protected JButton getSubmitButton() {
		if (this.submitButton == null)
			this.submitButton = this.createButton("Submit", true);
		return this.submitButton;
	}

	protected JButton getCancelButton() {
		if (this.cancelButton == null)
			this.cancelButton = createButton("Cancel", true);
		return this.cancelButton;
	}

	protected JButton createButton(String title, Boolean enabled) {
		JButton button = new JButton();
		button.setName(title + "Button");
		button.setFont(getButtontFont());
		button.setText(title);
		button.setEnabled(enabled);
		button.setMnemonic(KeyEvent.VK_S);
		return button;
	}
	
	protected JPanel getEngFiredPanel() {
		JPanel engFiredPanel = new JPanel(new MigLayout());
		JLabel label = new JLabel("Engine Fired: ");
		label.setFont(this.getLabelFont());
		label.setMinimumSize(new Dimension(100,40));
		engFiredPanel.add(label);
		engFiredPanel.add(this.getEngFiredCheckBox());
		return engFiredPanel;
	}
	
	protected JCheckBox getEngFiredCheckBox() {
		if (this.engFiredCheckBox == null) {
			this.engFiredCheckBox = new JCheckBox();
			this.engFiredCheckBox.setName("EngFiredCheckBox");
			this.engFiredCheckBox.setSize(10, 10);
		}
		return this.engFiredCheckBox;
	}
	
	protected TreeSet<String> getEngYmtoList(FrameSpec frame) {
		TreeSet<String> engYmtoSet = new TreeSet<String>();
		boolean useAltEngineMto=PropertyService.getPropertyBean(ProductCheckPropertyBean.class, this.ownerContext.getProcessPointId()).isUseAltEngineMto();
		if (frame.getEngineMto() != null)
			engYmtoSet.add(frame.getEngineMto().trim());
		if (useAltEngineMto && frame.getAltEngineMto() != null)
			engYmtoSet.add(frame.getAltEngineMto().trim());
		List<FrameEngineModelMap> altEngYmtoList = ServiceFactory.getDao(FrameEngineModelMapDao.class).findAllByFrameYmto(frame.getProductSpecCode());
		if (altEngYmtoList != null && !altEngYmtoList.isEmpty()) {
			for (FrameEngineModelMap altEngYmto : altEngYmtoList) {
				engYmtoSet.add(altEngYmto.getEngineYmto().trim());
			}
		}
		engYmtoSet.remove("");
		return engYmtoSet;
	}
	
	protected int getElementHeight() {
		return 25;
	}

	protected Font getLabelFont() {
		return Fonts.DIALOG_BOLD_16;
	}

	protected Font getInputFont() {
		return Fonts.DIALOG_PLAIN_30;
	}

	protected Font getButtontFont() {
		return Fonts.DIALOG_BOLD_16;
	}

	
	public Logger getLogger() {
		return Logger.getLogger(getOwner().getName());
	}
	
	private void addListeners() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				engCancelButtonAction();
			}
		});
		this.getEngYmtoComboBox().getComponent().addActionListener(this);	
		this.getEngFiredCheckBox().addActionListener(this);
		this.getSubmitButton().addActionListener(this);
		this.getCancelButton().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.getSubmitButton())) this.engSubmitButtonAction();
		else if (e.getSource().equals(this.getCancelButton())) this.engCancelButtonAction();
		
	}
	
	protected void engSubmitButtonAction() {
		String message = "Create a manifest for engine " + this.engineSn + "?";
		int confirmCreate = JOptionPane.showConfirmDialog(this, message, "Create manifest", JOptionPane.OK_CANCEL_OPTION);
		if (confirmCreate != JOptionPane.OK_OPTION) return;
		ServiceFactory.getDao(EngineDao.class).insert(this.createEngine());
		this.getLogger().info("Engine " + this.getEngSnTextField().getComponent().getText() + " created.");
		this.dispose();
	}
	
	protected Engine createEngine() {
		String engineSn = this.getEngSnTextField().getComponent().getText().trim();
		String selectedEngYmto = (String)this.getEngYmtoComboBox().getComponent().getSelectedItem();
		int holdStatus = this.getEngineLoadUtility().isEngineOnHold(engineSn) ? HoldStatus.ON_HOLD.getId() : HoldStatus.NOT_ON_HOLD.getId();
		this.engine = new Engine();
		this.engine.setProductId(engineSn);
		this.engine.setProductSpecCode(selectedEngYmto);
		this.engine.setEngineFiringFlag((short)(this.engFiredCheckBox.isSelected() ? 1 : 0));
		this.engine.setAutoHoldStatus((short)holdStatus);
		this.engine.setLastPassingProcessPointId(this.ownerContext.getProcessPointId() + suffix);
		String plantCode = PropertyService.getPropertyBean(EngineLoadPropertyBean.class, this.ownerContext.getProcessPointId()).getEngineSource();
		if (!StringUtils.isBlank(plantCode))
			this.engine.setPlantCode(plantCode.trim());
		return this.engine;
	}
	
	public Engine getEngine() {
		return this.engine;
	}

	protected void engCancelButtonAction() {
		this.engine = null;
		this.getLogger().info("Engine creation cancelled.");
		this.dispose();
	}
	
	public EngineLoadUtility getEngineLoadUtility() {
		if (this.engineUtil == null) this.engineUtil = new EngineLoadUtility();
		return this.engineUtil;
	}
}