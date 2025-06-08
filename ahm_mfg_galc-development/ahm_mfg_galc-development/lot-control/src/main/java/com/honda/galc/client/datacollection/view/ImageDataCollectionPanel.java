package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.datacollection.property.ImageViewPropertyBean;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.util.CommonPartUtility;

import net.miginfocom.swing.MigLayout;

/**
 * <h3>Class description</h3>
 * Main panel for image data collection
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 12, 2017</TD>
 * <TD>1.0</TD>
 * <TD>20170912</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Mar. 20, 2018</TD>
 * <TD>1.1</TD>
 * <TD>20180320</TD>
 * <TD>Modified for multiple parts</TD>
 * </TR>
 * </TABLE>
 */
public class ImageDataCollectionPanel extends DataCollectionPanelBase {
	private static final long serialVersionUID = 1L;
	
	protected SingleColumnTablePanel skippedProductPanel = null;
	protected ImageViewPropertyBean property;
	private ImagePartViewPanel partViewPanel = null;
	
	private List<JPanel> snPanels = new ArrayList<JPanel>();
	private List<JPanel> torquePanels = new ArrayList<JPanel>();
	private Logger logger = Logger.getLogger();
	
	public ImageDataCollectionPanel(ImageViewPropertyBean property, int winWidth, int winHeight) {
		super(property, winWidth, winHeight);
		this.property = property;
		init();
	}

	@Override
	protected void init() {
		AnnotationProcessor.process(this);
		super.init();
	}

	protected void initPanel() {
		setName("ImageDataCollectionView");
		setLayout(new MigLayout());
		if(viewProperty.isMonitorSkippedProduct()) {
			this.add(getSkipedProductPanel(),"width 200:200:200");
			this.addSkippedProductListeners();
		}
		this.add(createProductIdPanel(),"wrap, grow, left, pushx, width " + this.getMainWindowWidth() + "");
		this.add(getPartViewPanel(),"span, push, grow, wrap");
		this.add(createBottomPanel(),"spanx, grow, dock south");
	}
	

	protected JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.add(getLabelLastPid(), null);
		panel.add(getTextFieldLastPid(), null);

		if (this.property.isUseDefaultViewLook()) {
			for(int i = 0; i < getButtonList().size(); i++){
				if (i == 1)
					panel.add(getButton(2));
				else if (i == 2)
					panel.add(getButton(1));
				else
					panel.add(getButton(i));
			}
		} else { 
			for(int i = 0; i < getButtonList().size(); i++){
				panel.add(getButton(i));
			}
		}
		
		if(getClientContext().getProperty().isShowTestTorque() && hasTorqueDevices()) {
			panel.add(getTestTorqueButton(), null);
		}
			
		return panel;
	}

	public javax.swing.JTextField getTextFieldLastPid() {
		if(textFieldLastPid == null) {
			textFieldLastPid = new javax.swing.JTextField();
			textFieldLastPid.setPreferredSize(new Dimension(viewProperty.getLastPidTextFieldWidth(), viewProperty.getLastPidTextFieldHeight()));
			textFieldLastPid.setName("JTextLastPID");
			textFieldLastPid.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 30));
			textFieldLastPid.setEnabled(true);
			if(viewProperty.isLastPidTextFieldUnfocusable()) {
				textFieldLastPid.setFocusable(false);
			}
			textFieldLastPid.setEditable(false);
			textFieldLastPid.setBackground(Color.white);
		}
		return textFieldLastPid;
	}

	protected JPanel createProductIdPanel() {
		JPanel productIdPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		productIdPanel.setLayout(layout);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 5, 0, 5);

		// Product ID label
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		if(viewProperty.isProductIdButton()) {
			addGridBagComponent(productIdPanel, getJButtonProductId(), layout, c);
		} else {
			addGridBagComponent(productIdPanel, getLabelProdId(), layout, c);
		}
	
		// Product spec label
		c.gridy = 1;
		addGridBagComponent(productIdPanel, getLabelExpPIDOrProdSpec(), layout, c);
		
		// AF On sequence number
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		addGridBagComponent(productIdPanel, getLabelAfOnSeqNum(), layout, c);
		
		// AF On seq number value
		c.gridy = 1;
		addGridBagComponent(productIdPanel, getLabelAfOnSeqNumValue(), layout, c);

		// Product ID textfield
		c.gridx = 1;
		c.gridy = 0;
		addGridBagComponent(productIdPanel, getTextFieldProdId(), layout, c);

		// Product spec textfield
		c.gridy = 1;
		addGridBagComponent(productIdPanel, getTextFieldExpPidOrProdSpec(), layout, c);
		
		// Product Count number
		c.gridx = 3;
		c.gridy = 0;
		addGridBagComponent(productIdPanel, getLabelProductCount(), layout, c);
		
		// Product Count number value
		c.gridy = 1;
		addGridBagComponent(productIdPanel, getLabelProductCountValue(), layout, c);
		return productIdPanel;
	}

	protected void addGridBagComponent(JPanel panel, JComponent comp, GridBagLayout layout, GridBagConstraints c) {
		JComponent component = comp == null ? new JLabel("") : comp;
		layout.setConstraints(component, c);
		panel.add(component);
	}
	
	public JButton getJButtonProductId() {
        if (ivjJButtonProductId == null) {
            ivjJButtonProductId = new javax.swing.JButton();
            ivjJButtonProductId.setName("JButtonProductId");
            ivjJButtonProductId.setText(viewProperty.getProductIdLabel());
            ivjJButtonProductId.setFont(new java.awt.Font("dialog", 0, 18));
            ivjJButtonProductId.setIconTextGap(40);
       
            ivjJButtonProductId.setPreferredSize(new Dimension(viewProperty.getProductIdButtonWidth(), viewProperty.getProductIdButtonHeight()));
            ivjJButtonProductId.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
            ivjJButtonProductId.setForeground(java.awt.Color.black);
        }
        return ivjJButtonProductId;
    }
	
	public JLabel getLabelProdId() {
		if (prodIdLabel == null) {
			prodIdLabel = new JLabel();
			prodIdLabel.setName("JLabelEngineSN");
			prodIdLabel.setFont(new java.awt.Font("dialog", 0, 18));
			prodIdLabel.setIconTextGap(40);
			prodIdLabel.setText(viewProperty.getProductIdLabel());
			prodIdLabel.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			prodIdLabel.setForeground(java.awt.Color.black);
		}
		return prodIdLabel;
	}

	private Dimension getProductFieldPerferredSize() { 
		return new Dimension(isAfOnSeqNumExist() ? 450 : 623, 45);
	}
	
	public UpperCaseFieldBean getTextFieldProdId() {
		if (textFieldProdId == null) {
			textFieldProdId = new UpperCaseFieldBean();
			textFieldProdId.setName("JTextFieldProductID");
			textFieldProdId.setFont(new java.awt.Font("dialog", 0, 36));
			textFieldProdId.setText("");
			textFieldProdId.setFixedLength(true);
			
			int maxLength = viewProperty.getMaxProductSnLength();
			if (getClientContext().isRemoveIEnabled() && getClientContext().getProperty().getProductType().equalsIgnoreCase(ProductType.FRAME.toString())) {
				maxLength = viewProperty.getMaxProductSnLength() + 1;
			}						
			textFieldProdId.setMaximumLength(maxLength);
			textFieldProdId.setColumns(maxLength);
			textFieldProdId.setPreferredSize(getProductFieldPerferredSize());
			textFieldProdId.setSelectionColor(new Color(204, 204, 255));
			textFieldProdId.setColor(Color.blue);
			textFieldProdId.setBackground(Color.blue);
		}
		return textFieldProdId;
	}

	public JLabel getLabelExpPIDOrProdSpec() {
		if (labelExpPidOrProdSpec == null) {
			labelExpPidOrProdSpec = new javax.swing.JLabel();
			labelExpPidOrProdSpec.setName("LabelExpProdSpec");
			labelExpPidOrProdSpec.setFont(new java.awt.Font("dialog", 0, 18));
			labelExpPidOrProdSpec.setText("");
			labelExpPidOrProdSpec.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			labelExpPidOrProdSpec.setForeground(java.awt.Color.black);
		}
		return labelExpPidOrProdSpec;
	}
	
	public javax.swing.JTextField getTextFieldExpPidOrProdSpec() {
		if (textFieldExpPIDOrProdSpec == null) {
			textFieldExpPIDOrProdSpec = new javax.swing.JTextField();
			textFieldExpPIDOrProdSpec.setName("TextFieldExpPIDOrProdSpec");
			textFieldExpPIDOrProdSpec.setFont(new java.awt.Font("dialog", 0, 36));
			textFieldExpPIDOrProdSpec.setText("WWWWWWWWWWWWWWWWW");
			textFieldExpPIDOrProdSpec.setPreferredSize(getProductFieldPerferredSize());
		}
		return textFieldExpPIDOrProdSpec;
	}
	
	public JLabel getLabelAfOnSeqNum() {
		if(labelAfOnSeqNum == null) {
			labelAfOnSeqNum = new javax.swing.JLabel();
			labelAfOnSeqNum.setName("LabelAfOnSeq");
			labelAfOnSeqNum.setFont(new java.awt.Font("dialog", 0, viewProperty.getAfOnSeqNumLabelFontSize()));		
			labelAfOnSeqNum.setText("");
			labelAfOnSeqNum.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			labelAfOnSeqNum.setForeground(java.awt.Color.black);
			labelAfOnSeqNum.setVisible(false);
		}
		return labelAfOnSeqNum;
	}
	
	public javax.swing.JLabel getLabelAfOnSeqNumValue() {
		if (labelAfOnSeqNumValue == null) {
			labelAfOnSeqNumValue = new javax.swing.JLabel();
			labelAfOnSeqNumValue.setName("TextFieldAfOnSeqNo");
			labelAfOnSeqNumValue.setFont(new java.awt.Font("dialog", 0, viewProperty.getAfOnSeqNumFontSize()));
			labelAfOnSeqNumValue.setText("-----");
			labelAfOnSeqNumValue.setVisible(false);
		}
		return labelAfOnSeqNumValue;
	}
	
	
	public JLabel getLabelProductCount() {
		if(labelProductCount == null) {
			labelProductCount = new javax.swing.JLabel();
			labelProductCount.setName("LabelProductCount");
			labelProductCount.setFont(new java.awt.Font("dialog", 0, viewProperty.getProductCountLabelFontSize()));		
			labelProductCount.setText("");
			labelProductCount.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			labelProductCount.setForeground(java.awt.Color.black);
			labelProductCount.setVisible(false);
		}
		return labelProductCount;
	}
	
	public javax.swing.JLabel getLabelProductCountValue() {
		if (labelProductCountValue == null) {
			labelProductCountValue = new javax.swing.JLabel();
			labelProductCountValue.setName("TextFieldCountValue");
			labelProductCountValue.setFont(new java.awt.Font("dialog", 0, viewProperty.getProductCountValueFontSize()));
			labelProductCountValue.setText("--/--");
			labelProductCountValue.setVisible(false);
			labelProductCountValue.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return labelProductCountValue;
	}

	protected void createView(List<LotControlRule> lotControlRules) {
		try {
			if (lotControlRules == null || lotControlRules.size() == 0) {
				return;
			}

			getTextFieldExpPidOrProdSpec().setVisible(true);
			int torqueCount = 0;
			String defaultTorque = viewProperty.getDefaultTorqueValue();
			LotControlRule rule;
			for (int i = 0;((i < property.getMaxNumberOfPart()) && (i < lotControlRules.size())); i++) {
				rule = lotControlRules.get(i);
				if (rule != null && (rule.getSerialNumberScanFlag() == 1 || rule.isDateScan())) {
					setupSNFields(rule, i);
				}

				for (int j = 0; j < rule.getParts().get(0).getMeasurementCount(); j++) {
					// set Torque Point
					JTextField textField = getTorqueValueTextField(torqueCount++);
					textField.setBackground(Color.white);
					textField.setText(defaultTorque);
					textField.setVisible(true);
					textField.setEditable(false);
				}
			}

			getPartViewPanel().configureDcPanel(lotControlRules);
		} catch (Exception e) {
			logger.error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}
	
	protected void setupSNFields(LotControlRule rule, int index) {
		PartName partName = rule.getPartName();
		String partMask = CommonPartUtility.parsePartMaskDisplay(rule.getPartMasks());
		partMask = partMask.contains("<<") ? partMask.replace("<", "&lt;") : partMask;
		partMask = partMask.contains(">>") ? partMask.replace(">", "&gt;") : partMask;
		String label = "<HTML><p align='right'>" + partName.getWindowLabel() + ":" + "<br>" + partMask + "</p></HTML>";
		getPartLabel(index).setText(label);
		getPartLabel(index).setFont(new java.awt.Font("dialog", 0, 20));
		getPartLabel(index).setVisible(true);
		getPartSerialNumber(index).setVisible(true);
	}
	
	@Override
	protected void initComponents() {
		initPartLabelList();
		initPartSerialNumberList();
		initTorqueValueTextFildList();
		initButtonList();
	}

	@Override
	public void repositionPartLabel(JLabel partLabel, int position, boolean isAfOnSeqNoExist) {
	}

	@Override
	public void repositionSerialNumber(UpperCaseFieldBean partSN, int position, boolean isAfOnSeqNoExist) {
	}

	protected void initPartLabelList() {
		JLabel partLabel;

		for (int i = 0; i < maxNumOfPart; i++) {
			partLabel = new javax.swing.JLabel();
			partLabel.setName("JLabelPart0" + i);
			partLabel.setFont(new java.awt.Font("dialog", 0, viewProperty.getPartLabelFontSize()));
			partLabel.setText("Part0: " + i);
			partLabel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			partLabel.setForeground(java.awt.Color.black);
			partLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			partLabelList.add(partLabel);
		}		
	}
	
	public void initPartSerialNumberList() {
		for (int i = 0; i < maxNumOfPart; i++) {
			UpperCaseFieldBean partSN;
			partSN = new UpperCaseFieldBean();
			partSN.setName("UppPartSN0" + (i + 1));
			partSN.setFont(new java.awt.Font("dialog", 0, viewProperty.getPartSnFontSize()));
			partSN.setColor(ViewControlUtil.VIEW_COLOR_CURRENT);
			partSN.setPreferredSize(new Dimension(550, 45));
			partSNList.add(partSN);
			
			JPanel panel = new JPanel();
			GridBagLayout layout = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			panel.setLayout(layout);
			
			c.fill = GridBagConstraints.NONE;
			layout.setConstraints(partLabelList.get(i), c);
			panel.add(partLabelList.get(i));

			c.gridx = 1;
			c.insets = new Insets(0, 10, 0, 0);
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			layout.setConstraints(partSN, c);
			panel.add(partSN);
			snPanels.add(panel);
		}
	}

	protected void initButtonList() {
		super.initButtonList();
		int x = viewProperty.getButtonPositionX();
		int y = viewProperty.getButtonPositionY();
		if(y > 20) {
			y = 10;
		}
		
		int gap  = viewProperty.getButtonGap();
		int width = viewProperty.getButtonWidth();
		int height = viewProperty.getButtonHeight();
		int fontSize = viewProperty.getButtonFontSize();
		JButton button = null;
		for(int i = 0; i < TOTAL_BUTTON; i++) {
			button = new JButton();
			button.setName("Button0" + (i+1));
			button.setBounds(x + i * (width + gap), y, width, height);
			button.setFont(new Font("dialog", 0, fontSize));
			
			button.setVisible(false); 
			buttonList.add(button);
		}
	}
	
	public void initTorqueValueTextFildList() {
		int width = viewProperty.getTorqueFieldWidth();
		int height = viewProperty.getTorqueFieldHeight();
		
		String defaultTorque = viewProperty.getDefaultTorqueValue();
		JTextField textFieldTorque = null;

		for(int i = 0; i < maxNumOfTorque; i++) {
			textFieldTorque = new javax.swing.JTextField();
			textFieldTorque.setName("JTextFieldTorque0" + i);
			textFieldTorque.setText(defaultTorque);
			textFieldTorque.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			textFieldTorque.setFont(new java.awt.Font("dialog", 0, viewProperty.getTorqueFontSize()));
			textFieldTorque.setPreferredSize(new Dimension(width, height));
			textFieldTorque.setEditable(true);
			textFieldTorque.setEnabled(true);
			textFieldTorque.setVisible(false);
			textFieldTorque.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(FocusEvent e) {
					if (!e.isTemporary()) {
						logger.check(e.getComponent().getName() + " has gained focus");
					}
				}
			});
			torqueValueList.add(textFieldTorque);
			torquePanels.add(createTorquePanel(torqueValueList.get(i), i + 1));
		}	
	}
	
	public void showCurrentFocusField(JTextField textField) {
		if(textField != null) {
			partViewPanel.showPanel((JPanel) textField.getParent());
		}
	}
	
	// This method is required by the super class. The value is not used in this class.
	@Override
	protected int getTorquePositionY(int row, int height, int gap) {
		return 0;
	}
	
	public void reset() {
		getPartViewPanel().reset();
	}

	public void showImage(int imageId) {
		getPartViewPanel().showImage(imageId);
	}
	
	private JPanel createTorquePanel(JTextField valueField, int sequence) {
		int gap = viewProperty.getTorqueFieldGap();
		JLabel seqLabel;
		JPanel panel = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(layout);
			
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, gap, 0, 2);
		
		if (!this.property.isUseDefaultViewLook()) {
			seqLabel = new JLabel(String.valueOf(sequence));
			seqLabel.setFont(new java.awt.Font("dialog", 0, 50));
			layout.setConstraints(seqLabel, c);
			panel.add(seqLabel);
		}

		c.gridx = 1;
		c.insets = new Insets(0, 0, 0, 0);
		layout.setConstraints(valueField, c);
		panel.add(valueField);
		
		return panel;
	}
	
	public ImagePartViewPanel getPartViewPanel() {
		if(partViewPanel == null) {
			partViewPanel = new ImagePartViewPanel(property, snPanels, torquePanels);
		}
		return partViewPanel;
	}
	
	public JComponent getSkipedProductPanel() {
		if(this.skippedProductPanel == null)
		{
			try {
				this.skippedProductPanel = new SingleColumnTablePanel("Skipped Product");
				this.skippedProductPanel.initialize(viewProperty);

			} catch (Exception e) {
				handleException(e);
			}			
		}

		return skippedProductPanel;

	}
	
	private void addSkippedProductListeners(){
		this.skippedProductPanel.getTable().addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if(e.getClickCount() == 2){
					getCurrentField().requestFocus();
				}
			}
		});
	}
}
