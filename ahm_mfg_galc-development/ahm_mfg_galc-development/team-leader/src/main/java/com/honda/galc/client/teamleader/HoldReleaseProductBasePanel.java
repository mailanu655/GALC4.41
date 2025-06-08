package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.honda.galc.client.datacollection.property.CommonPropertyBean;
import com.honda.galc.client.teamleader.property.HoldReleasePropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.data.HoldByNonProductType;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/** * * 
 * @version 0.1 
 * @author Gangadhararao Gadde 
 * @since Apr 30, 2013
 */

public class HoldReleaseProductBasePanel extends TabbedPanel  implements java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener, java.awt.event.KeyListener
{
	private static final long serialVersionUID = 1L;
	public JPanel panel = null;
	public JPanel holdReleaseInformationPanel = null;
	public JPanel currentAssociatePanel = null;
	public JLabel associateLabel = null;
	public JLabel arrowLabel = null;
	public JLabel holdReleaseInformationLabel = null;
	public JLabel startLabel = null;
	public JLabel stopLabel = null;
	public JLabel unitsHoldLabel = null;
	public LabeledTextField associateNoTextField = null;
	public LabeledTextField associateNameTextField = null;
	public LabeledTextField pagerTextField = null;
	public LabeledTextField phoneExtTextField = null;
	public LabeledTextField countTextField;
	public JButton holdReleaseButton = null;
	public JButton cancelButton = null;
	public JButton excludeButton = null;
	public JButton addButton = null;	
	public LabeledComboBox holdReleaseReasonComboBox = null;
	public LabeledComboBox holdReleaseByComboBox=null;
	public LabeledComboBox holdReasonSearchComboBox = null;
	public JCheckBox holdNowCheckBox = null;
	public JCheckBox holdShippingCheckBox = null;
	public SmallSNInputPanel snInput1Panel = null;
	public SmallSNInputPanel snInput2Panel = null;
	public JTable productListTable = null;
	public JScrollPane productListScrollPane=null;		
	public DefaultTableModel defaultTableModel = new DefaultTableModel() ;	
	public HoldReleasePropertyBean holdReleasePropertyBean;
	public String processLocation=null;
	public int countCheck = 0;
	public String NONE_LINE_ID = "NONE";
	private static final String NO_FRAME_HOLD_RELEASE_STATUSES = "NO_FRAME_HOLD_RELEASE_STATUSES";
	private String[] noFrameHoldReleaseStatuses;
	protected List<String> productIDsDisallowedShipped;
	
	public HoldReleaseProductBasePanel(String screenName, int keyEvent, MainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);	
		
	}
	
	@Override
	public void onTabSelected() {
		setErrorMessage(null);
	}
	
	public void initComponents()
	{
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);
		holdReleasePropertyBean = PropertyService.getPropertyBean(HoldReleasePropertyBean.class, getApplicationId());
		processLocation=holdReleasePropertyBean.getProcessLocation();
		getHoldReleaseByComboBox().getComponent().addItem("");
		String[] productTypes = holdReleasePropertyBean.getProductTypes();
		for(String productType : productTypes) 
		{
			ProductType type=ProductType.getType(productType);
			if (type==null)
			{
				
				HoldByNonProductType nonProductType=HoldByNonProductType.getType(productType);
				if(nonProductType!=null)
				{
					getHoldReleaseByComboBox().getComponent().addItem(nonProductType.getProductName());
				}else
				{
					setErrorMessage("Invalid Hold/Release type");
				}
				
			}else
			{
			    getHoldReleaseByComboBox().getComponent().addItem(ProductNumberDef.getProductNumberDef(ProductType.getType(productType)).get(0).getName());
			}
			
		}
		
		getHoldReleaseReasonComboBox().getComponent().setEnabled(false);
		getAssociateNoTextField().getComponent().setText(getMainWindow().getUserId());

			
		
	}
	
	public void addListeners() {

		getHoldReleaseButton().addActionListener(this);
		getCancelButton().addActionListener(this);
		getPhoneExtTextField().getComponent().addKeyListener(this);
		getPagerTextField().getComponent().addKeyListener(this);
		getAssociateNameTextField().getComponent().addKeyListener(this);
		getHoldReleaseByComboBox().getComponent().addActionListener(this);
		getAddButton().addActionListener(this);
		getExcludeButton().addActionListener(this);		
		getHoldReleaseByComboBox().getComponent().addFocusListener(this);
		getHoldReleaseByComboBox().getComponent().addItemListener(this);
		getHoldReleaseReasonComboBox().getComponent().addItemListener(this);
		getSnInput1Panel().getSNTextField().addFocusListener(this);
		getSnInput1Panel().getSNTextField().addActionListener(this);
		getSnInput2Panel().getSNTextField().addFocusListener(this);
		getSnInput2Panel().getSNTextField().addActionListener(this);	
		getHoldReleaseReasonComboBox().getComponent().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				getMainWindow().clearMessage();
			}
		});
    }

	public void actionPerformed(ActionEvent e) {
		
	}
	
	public javax.swing.JLabel getHoldReleaseInformationLabel() {
		if (holdReleaseInformationLabel == null) {
			holdReleaseInformationLabel = new javax.swing.JLabel();
			holdReleaseInformationLabel.setName("JLabelHoldReleaseInformation");
			holdReleaseInformationLabel.setText("Hold Information");
			holdReleaseInformationLabel.setMaximumSize(new java.awt.Dimension(201, 22));
			holdReleaseInformationLabel.setForeground(new java.awt.Color(0,0,0));
			holdReleaseInformationLabel.setFont(new java.awt.Font("dialog", 0, 14));
			holdReleaseInformationLabel.setBounds(17, 300, 133, 29);
			holdReleaseInformationLabel.setMinimumSize(new java.awt.Dimension(201, 22));
		}
		return holdReleaseInformationLabel;
	}
	
	public javax.swing.JPanel getHoldReleaseInformationPanel() {
		if (holdReleaseInformationPanel == null) {
			holdReleaseInformationPanel = new javax.swing.JPanel();
			holdReleaseInformationPanel.setName("JPanelHoldReleaseInfomation");
			holdReleaseInformationPanel.setBorder(new javax.swing.border.EtchedBorder());
			holdReleaseInformationPanel.setLayout(null);
			holdReleaseInformationPanel.setBounds(15, 325, 993, 100);
			holdReleaseInformationPanel.setMinimumSize(new java.awt.Dimension(993, 89));
			getHoldReleaseInformationPanel().add(getHoldReleaseReasonComboBox(), getHoldReleaseReasonComboBox().getName());	
			
		}
		return holdReleaseInformationPanel;
	}
	
	public LabeledComboBox getHoldReleaseReasonComboBox() {
		if(holdReleaseReasonComboBox == null){
			holdReleaseReasonComboBox = new LabeledComboBox("Reason", true);				
			holdReleaseReasonComboBox.getLabel().setForeground(java.awt.Color.black);
			holdReleaseReasonComboBox.getComponent().setBackground(java.awt.Color.white);
			holdReleaseReasonComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			holdReleaseReasonComboBox.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			holdReleaseReasonComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			holdReleaseReasonComboBox.setBounds(15, 15, 833, 45);
			holdReleaseReasonComboBox.getComponent().setEditable(true);

		}		
		return holdReleaseReasonComboBox;
	}
	
	public javax.swing.JButton getHoldReleaseButton() {
		if (holdReleaseButton == null) {
			holdReleaseButton = new javax.swing.JButton();
			holdReleaseButton.setName("JButtonHoldRelease");		
			holdReleaseButton.setMaximumSize(new java.awt.Dimension(61, 33));
			holdReleaseButton.setActionCommand("OK");
			holdReleaseButton.setFont(new java.awt.Font("dialog", 0, 14));
			holdReleaseButton.setEnabled(false);
			holdReleaseButton.setMinimumSize(new java.awt.Dimension(61, 33));
			holdReleaseButton.setBounds(300, 560, 115, 38);
		}
		return holdReleaseButton;
	}

	public javax.swing.JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new javax.swing.JButton();
			cancelButton.setName("JButtonCancel");
			cancelButton.setText("Cancel");
			cancelButton.setMaximumSize(new java.awt.Dimension(61, 33));
			cancelButton.setActionCommand("OK");
			cancelButton.setFont(new java.awt.Font("dialog", 0, 14));
			cancelButton.setEnabled(true);
			cancelButton.setMinimumSize(new java.awt.Dimension(61, 33));
			cancelButton.setBounds(554, 560, 115, 38);
		}
		return cancelButton;
	}
	
	public javax.swing.JLabel getAssociateLabel() {
		if (associateLabel == null) {
			try {
				associateLabel = new javax.swing.JLabel();
				associateLabel.setName("JLabelAssociate");
				associateLabel.setText("Associate");
				associateLabel.setMaximumSize(new java.awt.Dimension(77, 22));
				associateLabel.setForeground(new java.awt.Color(0,0,0));
				associateLabel.setFont(new java.awt.Font("dialog", 0, 14));
				associateLabel.setBounds(18, 450, 87, 29);
				associateLabel.setMinimumSize(new java.awt.Dimension(77, 22));

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return associateLabel;
	}

	public javax.swing.JPanel getCurrentAssociatePanel() {
		if (currentAssociatePanel == null) {
			try {
				currentAssociatePanel = new javax.swing.JPanel();
				currentAssociatePanel.setName("JPanelCurrentAssociate");
				currentAssociatePanel.setBorder(new javax.swing.border.EtchedBorder());
				currentAssociatePanel.setLayout(null);
				currentAssociatePanel.setBounds(16, 475, 993, 70);
				currentAssociatePanel.setMinimumSize(new java.awt.Dimension(997, 70));
				getCurrentAssociatePanel().add(getAssociateNoTextField(), getAssociateNoTextField().getName());
				getCurrentAssociatePanel().add(getPhoneExtTextField(), getPhoneExtTextField().getName());
				getCurrentAssociatePanel().add(getPagerTextField(), getPagerTextField().getName());
				getCurrentAssociatePanel().add(getAssociateNameTextField(), getAssociateNameTextField().getName());

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return currentAssociatePanel;
	}

	public LabeledTextField getAssociateNoTextField() {
		if(associateNoTextField == null){
			associateNoTextField = new LabeledTextField("Associate No", true);
			associateNoTextField.setUpperCaseField(true);
			associateNoTextField.getLabel().setForeground(java.awt.Color.black);
			associateNoTextField.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			associateNoTextField.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			associateNoTextField.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			associateNoTextField.getComponent().setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			associateNoTextField.getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
			associateNoTextField.getComponent().setDisabledTextColor(java.awt.Color.black);
			associateNoTextField.getComponent().setFont(new java.awt.Font("dialog", 0, 14));
			associateNoTextField.getComponent().setEditable(false);
			associateNoTextField.setBounds(10, 10, 210, 50);		
			associateNoTextField.getComponent().setEnabled(true);		
		}
		return associateNoTextField;
	}

	public LabeledTextField getPhoneExtTextField() {
		if(phoneExtTextField == null){
			phoneExtTextField = new LabeledTextField("Phone Extension", true);
			phoneExtTextField.setUpperCaseField(true);
			phoneExtTextField.getLabel().setForeground(java.awt.Color.black);
			phoneExtTextField.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			phoneExtTextField.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			phoneExtTextField.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			phoneExtTextField.getComponent().setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			phoneExtTextField.getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
			phoneExtTextField.getComponent().setDisabledTextColor(java.awt.Color.black);
			phoneExtTextField.getComponent().setFont(new java.awt.Font("dialog", 0, 14));
			phoneExtTextField.getComponent().setEditable(true);
			phoneExtTextField.setBounds(229, 10, 210, 50);		
			phoneExtTextField.getComponent().setEnabled(false);		
		}
		return phoneExtTextField;
	}

	public LabeledTextField getPagerTextField() {
		if(pagerTextField == null){
			pagerTextField = new LabeledTextField("Pager", true);
			pagerTextField.setUpperCaseField(true);
			pagerTextField.getLabel().setForeground(java.awt.Color.black);
			pagerTextField.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			pagerTextField.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			pagerTextField.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			pagerTextField.getComponent().setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			pagerTextField.getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
			pagerTextField.getComponent().setDisabledTextColor(java.awt.Color.black);
			pagerTextField.getComponent().setFont(new java.awt.Font("dialog", 0, 14));
			pagerTextField.getComponent().setEditable(true);
			pagerTextField.setBounds(475, 10, 210, 50);		
			pagerTextField.getComponent().setEnabled(false);		
		}
		return pagerTextField;
	}

	public LabeledTextField getAssociateNameTextField() {
		if(associateNameTextField == null){
			associateNameTextField = new LabeledTextField("Name", true);
			associateNameTextField.setUpperCaseField(true);
			associateNameTextField.getLabel().setForeground(java.awt.Color.black);
			associateNameTextField.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			associateNameTextField.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			associateNameTextField.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			associateNameTextField.getComponent().setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			associateNameTextField.getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
			associateNameTextField.getComponent().setDisabledTextColor(java.awt.Color.black);
			associateNameTextField.getComponent().setFont(new java.awt.Font("dialog", 0, 14));
			associateNameTextField.getComponent().setEditable(true);
			associateNameTextField.setBounds(700, 10, 250, 50);		
			associateNameTextField.getComponent().setEnabled(false);		
		}
		return associateNameTextField;
	}

	public SmallSNInputPanel getSnInput1Panel() {
		if (snInput1Panel == null) {
			snInput1Panel = new SmallSNInputPanel(getMainWindow());
			snInput1Panel.setName("SNInput");
			snInput1Panel.getSNTextField().setName("SNTextField1");
		}
		return snInput1Panel;
	}

	public SmallSNInputPanel getSnInput2Panel() {
		if (snInput2Panel == null) {
			snInput2Panel = new SmallSNInputPanel(getMainWindow());
			snInput2Panel.setName("SNInput2");
			snInput2Panel.getSNTextField().setName("SNTextField2");
		}
		return snInput2Panel;
	}
	
	public javax.swing.JScrollPane getProductsListScrollPane() {
		if (productListScrollPane == null) {
			productListScrollPane = new javax.swing.JScrollPane(getProductsListTable());
			productListScrollPane.setName("productListScrollPane");
			productListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			productListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			productListScrollPane.setFont(new java.awt.Font("dialog", 0, 14));
			productListScrollPane.setBounds(15, 150, 870, 125);
			productListScrollPane.setViewportView(getProductsListTable());

		}
		return productListScrollPane;
	}

	public JTable getProductsListTable() {
		if (productListTable == null) {
			try {
				productListTable = new JTable(){
					   private static final long serialVersionUID = 1L;

						@Override
				       public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				           Component component = super.prepareRenderer(renderer, row, column);
				           int rendererWidth = component.getPreferredSize().width;
				           TableColumn tableColumn = getColumnModel().getColumn(column);
				           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				           return component;
				        }
				    };
			    productListTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				productListTable.setName("productListTable");
				getProductsListScrollPane().setColumnHeaderView(productListTable.getTableHeader());
				productListTable.setFont(new java.awt.Font("dialog", 0, 14));
				productListTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return productListTable;
	}
	
	public javax.swing.JLabel getJLabelUnitsHold() {
		if (unitsHoldLabel == null) {
			unitsHoldLabel = new javax.swing.JLabel();
			unitsHoldLabel.setName("JLabelUnitsHold");
			unitsHoldLabel.setMaximumSize(new java.awt.Dimension(201, 22));
			unitsHoldLabel.setForeground(new java.awt.Color(0,0,0));
			unitsHoldLabel.setFont(new java.awt.Font("dialog", 0, 14));
			unitsHoldLabel.setBounds(17, 125, 133, 29);
			unitsHoldLabel.setMinimumSize(new java.awt.Dimension(201, 22));

		}
		return unitsHoldLabel;
	}

	public javax.swing.JButton getExcludeButton() {
		if (excludeButton == null) {
			excludeButton = new javax.swing.JButton();
			excludeButton.setName("JButtonExclude");
			excludeButton.setText("Exclude");
			excludeButton.setMaximumSize(new java.awt.Dimension(45, 25));
			excludeButton.setActionCommand("OK");
			excludeButton.setFont(new java.awt.Font("dialog", 0, 14));
			excludeButton.setEnabled(false);
			excludeButton.setMinimumSize(new java.awt.Dimension(45, 25));
			excludeButton.setBounds(900, 200, 90, 25);
		}
		return excludeButton;
	}

	public javax.swing.JButton getAddButton() {
		if (addButton == null) {
			addButton = new javax.swing.JButton();
			addButton.setName("JButtonAdd");
			addButton.setText("Add");
			addButton.setMaximumSize(new java.awt.Dimension(45, 25));
			addButton.setActionCommand("OK");
			addButton.setFont(new java.awt.Font("dialog", 0, 14));
			addButton.setEnabled(false);
			addButton.setMinimumSize(new java.awt.Dimension(45, 25));
			addButton.setBounds(940, 72, 60, 27);
		}
		return addButton;
	}
	
	public javax.swing.JLabel getArrowLabel() {
		if (arrowLabel == null) {
			try {
				arrowLabel = new javax.swing.JLabel();
				arrowLabel.setName("JLabelArrow");
				arrowLabel.setText(">>>");
				arrowLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				arrowLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return arrowLabel;
	}
	
	public javax.swing.JLabel getStartLabel() {
		if (startLabel == null) {
			startLabel = new javax.swing.JLabel();
			startLabel.setName("JLabelStart");
			startLabel.setText("Start");
			startLabel.setForeground(java.awt.Color.black);
			startLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			startLabel.setFont(new java.awt.Font("dialog", 0, 14));
			startLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		}
		return startLabel;
	}
	
	public javax.swing.JLabel getStopLabel() {
		if (stopLabel == null) {
			stopLabel = new javax.swing.JLabel();
			stopLabel.setName("JLabelStop");
			stopLabel.setText("Stop");
			stopLabel.setForeground(java.awt.Color.black);
			stopLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			stopLabel.setFont(new java.awt.Font("dialog", 0, 14));
			stopLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		}
		return stopLabel;
	}
	
	public LabeledComboBox getHoldReleaseByComboBox() {
		if(holdReleaseByComboBox == null){
			holdReleaseByComboBox = new LabeledComboBox("", false);				
			holdReleaseByComboBox.setBackground(new java.awt.Color(192,192,192));
			holdReleaseByComboBox.getLabel().setForeground(java.awt.Color.black);
			holdReleaseByComboBox.getComponent().setBackground(java.awt.Color.white);
			holdReleaseByComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			holdReleaseByComboBox.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			holdReleaseByComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			holdReleaseByComboBox.setBounds(15, 33,165,75);
		}		
		return holdReleaseByComboBox;
	}

	public javax.swing.JPanel getMainPanel() {
		if (panel == null) {
			try {
				panel = new javax.swing.JPanel();
				panel.setName("MainPanel");
				panel.setLayout(null);
				panel.setBackground(new java.awt.Color(192,192,192));
				panel.setMinimumSize(new java.awt.Dimension(0, 0));
				getMainPanel().add(getHoldReleaseInformationLabel(), getHoldReleaseInformationLabel().getName());
				getMainPanel().add(getHoldReleaseInformationPanel(), getHoldReleaseInformationPanel().getName());
				getMainPanel().add(getHoldReleaseButton(), getHoldReleaseButton().getName());
				getMainPanel().add(getCancelButton(), getCancelButton().getName());
				getMainPanel().add(getAssociateLabel(), getAssociateLabel().getName());
				getMainPanel().add(getCurrentAssociatePanel(), getCurrentAssociatePanel().getName());
				getMainPanel().add(getArrowLabel(), getArrowLabel().getName());
				getMainPanel().add(getHoldReleaseByComboBox(), getHoldReleaseByComboBox().getName());
				getMainPanel().add(getStartLabel(), getStartLabel().getName());
				getMainPanel().add(getStopLabel(), getStopLabel().getName());				
				getMainPanel().add(getSnInput1Panel(), getSnInput1Panel().getName());
				getMainPanel().add(getSnInput2Panel(), getSnInput2Panel().getName());
				getMainPanel().add(getProductsListScrollPane(),getProductsListScrollPane().getName());
				getMainPanel().add(getJLabelUnitsHold(), getJLabelUnitsHold().getName());
				getMainPanel().add(getExcludeButton(),getExcludeButton().getName());
				getMainPanel().add(getAddButton(),getAddButton().getName());

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return panel;
	}

	public void focusGained(FocusEvent e) {
			
	}

	public void focusLost(FocusEvent e) {
				
	}

	public void itemStateChanged(ItemEvent e) {
			
	}

	public void keyPressed(KeyEvent e) {
				
	}

	
	public void keyReleased(java.awt.event.KeyEvent e) {
		if (e.getSource() == getPhoneExtTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getPagerTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getAssociateNameTextField().getComponent()) 
			associateDataChanged(null);
	}

	public void keyTyped(java.awt.event.KeyEvent e) {
		if (e.getSource() == getPhoneExtTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getPagerTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getAssociateNameTextField().getComponent()) 
			associateDataChanged(null);

	}


	
	public void holdReleaseByComboBoxActionPerformed()
	{
		getSnInput1Panel().getSNButton().setText(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString());
		getSnInput1Panel().getTextFieldSN().setEnabled(false);
		getSnInput2Panel().getSNButton().setText(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString());
		getSnInput2Panel().getTextFieldSN().setEnabled(false);
		getSnInput1Panel().getSNButton().setVisible(true);
		getSnInput2Panel().getSNButton().setVisible(true);
		getSnInput1Panel().getSNTextField().setEditable(false);
		getSnInput2Panel().getSNTextField().setEditable(false);
		
		if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().trim().length()>0){
			getSnInput1Panel().getTextFieldSN().setEnabled(true);
			getSnInput2Panel().getTextFieldSN().setEnabled(true);
			getSnInput1Panel().getSNTextField().setEditable(true);
			getSnInput2Panel().getSNTextField().setEditable(true);
		}
		
		if(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().trim().length()>0&&!getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().trim().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))
		{
			ProductType selectedProductType=ProductNumberDef.getProductNumberDefs(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString()).get(0).getProductType();
			getSnInput1Panel().setProductType(selectedProductType);
			getSnInput2Panel().setProductType(selectedProductType);
		}else
		{
			getSnInput1Panel().getSNButton().setVisible(false);
			getSnInput2Panel().getSNButton().setVisible(false);
		}
		getSnInput1Panel().getTextFieldSN().setText("");
		getSnInput2Panel().getTextFieldSN().setText("");

		getHoldReleaseButton().setEnabled(checkText());
		defaultTableModel=new DefaultTableModel();
		getProductsListTable().setModel(defaultTableModel);
		getProductsListTable().repaint();
		getProductsListScrollPane().repaint();
		getHoldReleaseReasonComboBox().getComponent().setEnabled(false);
	}
	
	public void changeViewHoldReleaseDisabled()
	{
		getHoldReleaseReasonComboBox().getComponent().setSelectedItem("");
		getHoldReleaseReasonComboBox().getComponent().setEnabled(false);
		getHoldReleaseReasonComboBox().getComponent().setBackground(Color.lightGray);

		getPhoneExtTextField().getComponent().setText("");
		getPhoneExtTextField().getComponent().setEnabled(false);
		getPhoneExtTextField().getComponent().setBackground(Color.lightGray);

		getPagerTextField().getComponent().setText("");
		getPagerTextField().getComponent().setEnabled(false);
		getPagerTextField().getComponent().setBackground(Color.lightGray);

		getAssociateNameTextField().getComponent().setText("");
		getAssociateNameTextField().getComponent().setEnabled(false);
		getAssociateNameTextField().getComponent().setBackground(Color.lightGray);
		
		getExcludeButton().setEnabled(false);
	}
	
	public void changeViewHoldReleaseEnabled()
	{
		getHoldReleaseReasonComboBox().getComponent().setEnabled(true);
		getHoldReleaseReasonComboBox().getComponent().setBackground(Color.white);

		getPhoneExtTextField().getComponent().setEnabled(true);
		getPhoneExtTextField().getComponent().setBackground(Color.white);

		getPagerTextField().getComponent().setEnabled(true);
		getPagerTextField().getComponent().setBackground(Color.white);

		getAssociateNameTextField().getComponent().setEnabled(true);
		getAssociateNameTextField().getComponent().setBackground(Color.white);
		getExcludeButton().setEnabled(true);
	}
	
	public boolean checkText() {

		if (getPhoneExtTextField().getComponent().getText().equals("")) {
			return false;
		}	
		if (getPagerTextField().getComponent().getText().equals("")) {
			return false;
		}
		if (getAssociateNameTextField().getComponent().getText().equals("")) {
			return false;
		}	
		setErrorMessage("");
		return true;
	}

	public void FocusLost(java.awt.event.FocusEvent focusEvent) 
	{
		getHoldReleaseButton().setEnabled(checkText());
	}
	
	public void initializeView() 
	{
		getHoldReleaseReasonComboBox().getComponent().setSelectedItem("");
		getHoldReleaseReasonComboBox().getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
		getHoldReleaseReasonComboBox().getComponent().setEnabled(false);

		getHoldReleaseButton().setEnabled(false);

		getSnInput1Panel().getTextFieldSN().setText("");
		getSnInput2Panel().getTextFieldSN().setText("");
		    
		getPhoneExtTextField().getComponent().setText("");
		getPagerTextField().getComponent().setText("");
		getAssociateNameTextField().getComponent().setText("");

		getPhoneExtTextField().getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
		getPagerTextField().getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
		getAssociateNameTextField().getComponent().setBackground(java.awt.SystemColor.activeCaptionBorder);
		getPhoneExtTextField().getComponent().setEnabled(false);
		getPagerTextField().getComponent().setEnabled(false);
		getAssociateNameTextField().getComponent().setEnabled(false);
		getHoldReleaseByComboBox().getComponent().grabFocus();

	}
	
	public boolean checkYear() {

		int intStarChar = 0;
		int intStopChar = 17;


		if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.ENGINE).get(0).getName())) {
			intStarChar = 1;
			intStopChar = 7;
		}  else if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName())) 
		{
			intStarChar = 9;
			intStopChar = 10;
		} else if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName())) 
		{
			intStarChar = 1;
			intStopChar = 1;
		} 
		else {
			return false;
		}

		if (getSnInput2Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar)
				.equals(getSnInput1Panel().getTextFieldSN().getText().substring(intStarChar,intStopChar))) 
		{
			return true;
		} else 
		{
			return false;
		}

	}

	
	public void confirmSN(String strSN) {
		try {
			setErrorMessage(null);

			if (strSN == null || strSN.trim().length() != getCommonPropertyBean().getMaxProductSnLength()) 
			{
				setErrorMessage("Input error of Product ID");
				getSnInput1Panel().getTextFieldSN().setSelectionStart(0);
				getSnInput1Panel().getTextFieldSN().setSelectionEnd(getSnInput1Panel().getTextFieldSN().getMaximumLength());
			}

		} catch (Exception e) {
			handleException(e);
		}
	}

	public void associateDataChanged(java.awt.AWTEvent e) {
		getHoldReleaseButton().setEnabled(checkText());

	}
	
	public void excludeButtonActionPerformed(java.awt.event.ActionEvent actionEvent){	
		
		int[] rowNumList=getProductsListTable().getSelectedRows();
		setErrorMessage(null);
		if(rowNumList.length!=0)
		{
			DefaultTableModel model= ((DefaultTableModel)getProductsListTable().getModel());		
			for(int i=0;i<rowNumList.length;i++)
			{		
				model.removeRow(rowNumList[i]-i);					
			}
			getProductsListTable().setModel(model);
			getProductsListTable().repaint();
			getProductsListScrollPane().repaint();	
		}else
		{
			setErrorMessage("No rows selected");
		}
	}

	public void resetScreen()
	{
		getHoldReleaseByComboBox().getComponent().grabFocus();
		getHoldReleaseByComboBox().getComponent().setSelectedIndex(0);
		getSnInput1Panel().getTextFieldSN().setText("");
		getSnInput2Panel().getTextFieldSN().setText("");		
		getSnInput1Panel().getTextFieldSN().setEnabled(false);
		getSnInput2Panel().getTextFieldSN().setEnabled(false);
		defaultTableModel=new DefaultTableModel();
		getProductsListTable().setModel(defaultTableModel);
		getProductsListTable().repaint();
		getProductsListScrollPane().repaint();	
		getHoldReleaseReasonComboBox().getComponent().setSelectedItem("");
		getHoldReleaseReasonComboBox().getComponent().setBackground(Color.lightGray);
		getHoldReleaseReasonComboBox().getComponent().setEnabled(false);
		getHoldReleaseButton().setEnabled(false);		  
		getPhoneExtTextField().getComponent().setText("");
		getPagerTextField().getComponent().setText("");
		getAssociateNameTextField().getComponent().setText("");
		getPhoneExtTextField().getComponent().setBackground(Color.lightGray);
		getPagerTextField().getComponent().setBackground(Color.lightGray);
		getAssociateNameTextField().getComponent().setBackground(Color.lightGray);
		getPhoneExtTextField().getComponent().setEnabled(false);
		getPagerTextField().getComponent().setEnabled(false);
		getAssociateNameTextField().getComponent().setEnabled(false);
		getExcludeButton().setEnabled(false);
		getAddButton().setEnabled(false);
	    getMainWindow().clearMessage();
	}
	
	public boolean checkFrameProductsAllowed(){
		boolean productsAllowed;
		Vector data = defaultTableModel.getDataVector();
		productIDsDisallowedShipped = new ArrayList<String>();
		for (int i = 0; i < getProductsListTable().getRowCount(); i++) {
			Vector row = (Vector) data.elementAt(i);
			String productId =(String)row.get(0);
			if (!isFrameHoldReleaseAllowed(productId)){
				productIDsDisallowedShipped.add(productId);
			}
		}
		productsAllowed = productIDsDisallowedShipped.size()==0;
		return productsAllowed;
	}
	
	public boolean isFrameHoldReleaseAllowed(String productId){
		ShippingStatus shippingStatus = ServiceFactory.getDao(ShippingStatusDao.class).findByKey(productId);
		if(shippingStatus != null) {
			String statusName = ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus().intValue()).getName();
			for(int i = 0; i < getNoFrameHoldReleaseStatuses().length; i++) {
				if(statusName.equals(getNoFrameHoldReleaseStatuses()[i])) 
					return false;
			}
		}
		return true;
	}
	
	private String[] getNoFrameHoldReleaseStatuses() {
		if(noFrameHoldReleaseStatuses == null) {
			String property = getMainWindow().getApplicationProperty(NO_FRAME_HOLD_RELEASE_STATUSES);
			noFrameHoldReleaseStatuses = property != null ? property.split(",") : new String[]{};
		}
		return noFrameHoldReleaseStatuses;
	}

	protected CommonPropertyBean getCommonPropertyBean() {
		return PropertyService.getPropertyBean(CommonPropertyBean.class);
	}
	
	protected FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class);
	}
}