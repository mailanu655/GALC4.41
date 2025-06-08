package com.honda.galc.client.teamleader.mbpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.dao.product.ProductIdNumberDefDao;
import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.service.ServiceFactory;

import net.miginfocom.swing.MigLayout;

public class ProductNumberDefCreateDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private MainWindow window;
	
	private JPanel inputPanel;
	private LabeledTextField defIdField;
	private LabeledTextField lengthField;
	private LabeledTextField maskField;
	
	private JPanel buttonPanel;
	private JButton saveButton;
	private JButton cancelButton;
	
	private ProductIdNumberDef newDef;
	private Set<String> existingDefs;
	
	
	public ProductNumberDefCreateDialog(MainWindow window, Set<String> existingDefs) {
		super(window, "New Product Number Definition", true);
		this.window = window;
		this.existingDefs = existingDefs;
		this.setSize(730, 140);
		this.initView();
		this.mapActions();
	}
	
	public MainWindow getParentPanel() {
		return this.window;
	}
	
	protected void initView() {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.add(getInputPanel());
	}

	private JPanel getInputPanel() {
		if (this.inputPanel == null) {
			this.inputPanel = new JPanel(new MigLayout());
			this.inputPanel.add(this.getDefIdField());
			this.inputPanel.add(this.getLengthField());
			this.inputPanel.add(this.getMaskField(),"wrap");
			this.inputPanel.add(this.getButtonPanel(),"grow,spanx");
		}
		return this.inputPanel;
	}
	
	private LabeledTextField getDefIdField() {
		if (this.defIdField == null) {
			this.defIdField = new LabeledTextField("DEF_ID");
		}
		return this.defIdField;
	}
	
	private LabeledTextField getLengthField() {
		if (this.lengthField == null) {
			this.lengthField = new LabeledTextField("LENGTH");
		}
		return this.lengthField;
	}
	
	private LabeledTextField getMaskField() {
		if (this.maskField == null) {
			this.maskField = new LabeledTextField("MASK");
		}
		return this.maskField;
	}
	
	private JPanel getButtonPanel() {
		if (this.buttonPanel == null) {
			this.buttonPanel = new JPanel(new MigLayout("fillx"));
			this.buttonPanel.add(this.getSaveButton(),"al right");
			this.buttonPanel.add(this.getCancelButton());
		}
		return this.buttonPanel;
	}
	
	private JButton getSaveButton() {
		if (this.saveButton == null) {
			this.saveButton = new JButton("Save");
		}
		return this.saveButton;
	}
	
	private JButton getCancelButton() {
		if (this.cancelButton == null) {
			this.cancelButton = new JButton("Cancel");
		}
		return this.cancelButton;
	}
	
	public ProductIdNumberDef getNewDef() {
		return this.newDef;
	}
	
	private void mapActions() {
		this.getSaveButton().addActionListener(this);
		this.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.getSaveButton())) {
			this.saveProductNumberDef();
			if (this.newDef != null) this.dispose();
		}
		
	}
	
	private void saveProductNumberDef() {
		this.window.clearMessage();
		String defId = this.getDefIdField().getComponent().getText();
		if (StringUtils.isBlank(defId)) {
			this.window.setErrorMessage("DEF_ID can not be blank.");
			return;
		} else if (this.existingDefs.contains(defId)) {
			this.window.setErrorMessage("DEF_ID \"" + defId + "\" already exists.");
			return;
		}
		
		short length = 0;
		try {
			length = StringUtils.isBlank(this.getLengthField().getComponent().getText()) ? 0 : 
				Short.parseShort(this.getLengthField().getComponent().getText());
		} catch (NumberFormatException ex) {
			this.window.setErrorMessage("LENGTH value must be an integer.");
			return;
		}
		
		String mask = StringUtils.isBlank(this.getMaskField().getComponent().getText()) ? "" : this.getMaskField().getComponent().getText();
		
		this.newDef = new ProductIdNumberDef(defId, length, mask);
		ServiceFactory.getDao(ProductIdNumberDefDao.class).insert(this.newDef);
	}
}
