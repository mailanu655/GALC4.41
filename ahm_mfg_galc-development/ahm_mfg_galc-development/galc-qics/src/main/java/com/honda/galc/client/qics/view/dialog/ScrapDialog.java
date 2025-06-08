package com.honda.galc.client.qics.view.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.screen.DefectPictureInputPanel;
import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.qics.view.screen.DefectScanTextPanel;
import com.honda.galc.client.qics.view.screen.DefectTextInputPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.ListComboBoxModel;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.util.ValidatorUtils;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ScrapDialog</code> is ...
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
 * <TD>Feb 21, 2008</TD>
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
public class ScrapDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton okButton;
	private JButton cancelButton;

	private JPanel contentPanel;

	private String message;
	private JLabel messageLabel;

	private JLabel defectNameLabel;
	private JLabel locationLabel;
	private JLabel commentLabel;

	private JTextField defectNameInput;
	private JComboBox locationInput;
	private JTextField commentInput;

	private boolean editable;

	private boolean cancelled = true;
	private boolean errorOnInit = false;
	private ExceptionalOut returnValue;
	
	private String partName;
	private String otherPartName;
	private final static char COMMENT_SEPARATOR = ',';

	public ScrapDialog(JFrame frame, String title, boolean editable, String message) {
		super(frame, title, true);
		initialize();
		this.message = message;
		getMessageLabel().setText(this.message);
		setEditable(editable);
	}

	public ScrapDialog(JFrame frame, String title, boolean editable) {
		super(frame, title, editable);
		initialize();
	}

	protected void initialize() {
		
		setContentPane(getContentPanel());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);

		getRootPane().setDefaultButton(getCancelButton());

		setSize(500, 280);

		QicsFrame frame = (QicsFrame) getParent();
		QicsPanel currentPanel = frame.getMainPanel().getSelectedPanel();

		if (currentPanel instanceof DefectPictureInputPanel) {
			initInput((DefectPictureInputPanel) currentPanel);
		} else if (currentPanel instanceof DefectTextInputPanel) {
			initInput((DefectTextInputPanel) currentPanel);
		} else if (currentPanel instanceof DefectRepairPanel) {
			initInput((DefectRepairPanel) currentPanel);
		}else if (currentPanel instanceof DefectScanTextPanel) {
			initInput((DefectScanTextPanel) currentPanel);
		}

		mapActions();
	}

	protected void initInput(DefectPictureInputPanel qicsPanel) {

		DefectTypeDescription defectTypeDescription = qicsPanel.getDefectPane().getSelectedItem();
		Image image = qicsPanel.getQicsController().getClientModel().getImage(defectTypeDescription.getDefectGroupName());

		getDefectNameInput().setText(defectTypeDescription.getDefectTypeName());
		
		if (image==null){
			String msg = "Please configure a picture.";
			((QicsFrame) getParent()).setWarningMessage(msg);
			setErrorOnInit(true);
			return;
		}
		
		List<DefectDescriptionId> defectDescriptionIds =  new ArrayList<DefectDescriptionId>();
		for (ImageSection item : image.getSections()) {
			
			DefectDescription defectDescription = item.getDefectDescription();
			if(defectDescription != null && defectDescription.getDefectTypeName().equals(defectTypeDescription.getDefectTypeName())){
				defectDescriptionIds.add(defectDescription.getId());
			}
		}
		ComboBoxModel model = new ListComboBoxModel(defectDescriptionIds);
		getLocationInput().setModel(model);
		// set the Location as the showed value in combobox
		getLocationInput().setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		    	DefectDescriptionId defectDescriptionId = (DefectDescriptionId)value;
		        value = defectDescriptionId.getInspectionPartLocationName();
		        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		    }
		});
		if (getLocationInput().getComponentCount() > 0){
			if (getLocationInput().getItemCount()>0){
				getLocationInput().setSelectedIndex(0);
			}else{
				String msg = "Please configure previously the area on picture.";
				((QicsFrame) getParent()).setWarningMessage(msg);
				setErrorOnInit(true);
			}
		}

	}

	protected void initInput(DefectTextInputPanel qicsPanel) {
		String defectName = qicsPanel.getDefectPane().getSelectedItem();
		String location = qicsPanel.getLocationPane().getSelectedItem();
		partName = qicsPanel.getPartPane().getSelectedItem();
		otherPartName = qicsPanel.getOtherPartPane().getSelectedItem().getSecondaryPartName();
		getDefectNameInput().setText(defectName);

		List<String> locations = new ArrayList<String>();
		locations.add(location);
		ComboBoxModel model = null;
		model = new DefaultComboBoxModel(new Vector<String>(locations));

		getLocationInput().setModel(model);
		if (getLocationInput().getComponentCount() > 0) {
			getLocationInput().setSelectedIndex(0);
		}
	}

	protected void initInput(DefectRepairPanel qicsPanel) {
		DefectResult data = qicsPanel.getDefectPane().getSelectedItem();
		String defectName = data.getId().getDefectTypeName();
		String location = data.getId().getInspectionPartLocationName();
		partName = data.getId().getInspectionPartName();
		otherPartName = data.getId().getSecondaryPartName();

		getDefectNameInput().setText(defectName);

		List<String> locations = new ArrayList<String>();
		locations.add(location);
		ComboBoxModel model = null;
		model = new DefaultComboBoxModel(new Vector<String>(locations));

		getLocationInput().setModel(model);
		if (getLocationInput().getComponentCount() > 0) {
			getLocationInput().setSelectedIndex(0);
		}
	}
	
	protected void initInput(DefectScanTextPanel qicsPanel) {		
		getDefectNameInput().setEditable(true);
		getDefectNameLabel().setText("Scrap Reason :");
		getLocationLabel().setVisible(false);
		getLocationInput().setVisible(false);
		
		
	}

	protected JPanel getContentPanel() {
		if (contentPanel == null) {

			commentLabel = createCommentLabel();
			commentInput = createCommentInput();

			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());

			int row = 0;
			int col = 0;

			GridBagConstraints constraints = null;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getMargin(), getMargin(), getSpacing(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridwidth = 2;
			contentPanel.add(getMessageLabel(), constraints);

			// new row
			col = 0;
			row++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getSpacing(), getMargin(), getSpacing(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			contentPanel.add(getDefectNameLabel(), constraints);

			col++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weightx = 1;
			constraints.insets = new Insets(getMargin(), getSpacing(), getSpacing(), getMargin());
			contentPanel.add(getDefectNameInput(), constraints);

			// new row
			col = 0;
			row++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getSpacing(), getMargin(), getSpacing(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			contentPanel.add(getLocationLabel(), constraints);

			col++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.insets = new Insets(getSpacing(), getSpacing(), getSpacing(), getMargin());
			constraints.gridwidth = 3;
			contentPanel.add(getLocationInput(), constraints);

			// new row
			col = 0;
			row++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getSpacing(), getMargin(), getSpacing(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			contentPanel.add(getCommentLabel(), constraints);

			col++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.insets = new Insets(getSpacing(), getSpacing(), getSpacing(), getMargin());
			constraints.gridwidth = 3;
			contentPanel.add(getCommentInput(), constraints);

			// new row
			col = 0;
			row++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getSpacing(), getMargin(), getMargin(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			contentPanel.add(new JLabel(), constraints);

			JPanel p = new JPanel();
			p.setLayout(new GridLayout(1, 2, getSpacing(), getSpacing()));
			p.add(getCancelButton());
			p.add(getOkButton());

			col++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.insets = new Insets(getSpacing(), getSpacing(), getSpacing(), getMargin());
			contentPanel.add(p, constraints);
		}
		return contentPanel;
	}

	public JLabel getMessageLabel() {
		if (messageLabel == null) {
			messageLabel = new JLabel(message);
			messageLabel.setFont(getLabelFont());
		}
		return messageLabel;
	}

	public JLabel getDefectNameLabel() {
		if (defectNameLabel == null) {
			defectNameLabel = new JLabel("Defect: ");
			defectNameLabel.setFont(getLabelFont());
		}
		return defectNameLabel;
	}

	public JLabel getLocationLabel() {
		if (locationLabel == null) {
			locationLabel = new JLabel("Location: ");
			locationLabel.setFont(getLabelFont());
		}
		return locationLabel;
	}

	public JTextField getDefectNameInput() {
		if (defectNameInput == null) {
			defectNameInput = new JTextField();
			defectNameInput.setDocument(new LimitedLengthPlainDocument(250));
			defectNameInput.setFont(getInputFont());
			defectNameInput.setEditable(isEditable());
		}
		return defectNameInput;
	}

	public JComboBox getLocationInput() {
		if (locationInput == null) {
			locationInput = new JComboBox();
			JTextComponent editor = (JTextComponent) locationInput.getEditor().getEditorComponent();
			editor.setDocument(new LimitedLengthPlainDocument(64));
			locationInput.setFont(getInputFont());
		}
		return locationInput;
	}

	protected JLabel createCommentLabel() {
		JLabel label = new JLabel("Comment: ");
		label.setFont(getLabelFont());
		return label;
	}

	public JTextField createCommentInput() {
		JTextField element = new JTextField();
		element.setDocument(new LimitedLengthPlainDocument(250));
		element.setFont(getInputFont());
		return element;
	}

	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.setFont(Fonts.DIALOG_PLAIN_18);
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.setPreferredSize(new Dimension(90, 50));
			okButton.setSize(90, 50);
			okButton.setLocation(10, 10);
		}
		return okButton;
	}

	protected JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setPreferredSize(new Dimension(90, 50));
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.setFont(Fonts.DIALOG_PLAIN_18);
			cancelButton.setSize(90, 50);
			cancelButton.setLocation(getOkButton().getX() + getOkButton().getWidth() + 10, getOkButton().getY());
		}
		return cancelButton;
	}

	protected Font getLabelFont() {
		return Fonts.DIALOG_BOLD_16;
	}

	protected Font getInputFont() {
		return Fonts.DIALOG_PLAIN_18;
	}

	protected int getMargin() {
		return 15;
	}

	protected int getSpacing() {
		return 5;
	}

	public void setReturnValue() {
		ExceptionalOut scrap = new ExceptionalOut();

		String defectName = getDefectNameInput().getText();
		String location = "";
		if (getLocationInput().getSelectedItem() != null) {
			if (getLocationInput().getSelectedItem() instanceof String ){
				location = (String) getLocationInput().getSelectedItem();
			}else if (getLocationInput().getSelectedItem() instanceof DefectDescriptionId ){
				location = ((DefectDescriptionId) getLocationInput().getSelectedItem()).getInspectionPartLocationName();
				partName = ((DefectDescriptionId) getLocationInput().getSelectedItem()).getInspectionPartName();
				otherPartName = ((DefectDescriptionId) getLocationInput().getSelectedItem()).getSecondaryPartName();
			}
		}
		String comment = getCommentInput().getText().trim();
		
		if (!StringUtils.isBlank(partName) && !StringUtils.isBlank(otherPartName))
			comment = comment + COMMENT_SEPARATOR + partName + COMMENT_SEPARATOR + otherPartName;

		scrap.setExceptionalOutReasonString(defectName);
		scrap.setLocation(location);
		scrap.setExceptionalOutComment(comment);
		setReturnValue(scrap);
	}

	protected List<String> validate(Object value) {
		return new ArrayList<String>();
	}

	public void setReturnValue(ExceptionalOut returnValue) {
		this.returnValue = returnValue;
	}

	public ExceptionalOut getReturnValue() {
		return returnValue;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		ScrapDialog dialog = new ScrapDialog(frame, "Scrap Product", true, "Hello World");
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}

	protected boolean isEditable() {
		return editable;
	}

	protected void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void onOkButton() {
		setReturnValue();
		Object value = getReturnValue();
		List<String> messages = validate(value);
		if (messages.isEmpty()) {
			dispose();
			setCancelled(false);
		} else {
			JOptionPane.showMessageDialog(this, ValidatorUtils.formatMessages(messages));
		}
	}

	protected void onCancelButton() {
		setCancelled(true);
		setReturnValue(null);
		dispose();
	}

	protected void mapActions() {

		getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOkButton();
			}
		});

		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancelButton();
			}
		});
	}

	public JLabel getCommentLabel() {
		return commentLabel;
	}

	public JTextField getCommentInput() {
		return commentInput;
	}

	public boolean isErrorOnInit() {
		return errorOnInit;
	}

	public void setErrorOnInit(boolean errorOnInit) {
		this.errorOnInit = errorOnInit;
	}	
	
}
