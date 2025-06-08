package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.property.PlanCodeLotSeqMaintPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date April 01, 2015
 */
public class PlanCodeLotSequenceMaintenancePanel extends TabbedPanel implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel panel = null;
	private LabeledComboBox fromPlanCodeComboBox = null;
	private LabeledComboBox toPlanCodeComboBox = null;
	private JButton moveToBtn =null;
	private ObjectTablePane<PreProductionLot> fromOrderListPane = null;
	private ObjectTablePane<PreProductionLot> toOrderListPane = null;
	private JLabel fromOrderListLabel=null;
	private JLabel toOrderListLabel=null;
	private PlanCodeLotSeqMaintPropertyBean propertyBean=null;

	public PlanCodeLotSequenceMaintenancePanel(TabbedMainWindow mainWindow) {
		super("PlanCodeLotSequenceMaintenancePanel", KeyEvent.VK_L,mainWindow);	
		initComponents();
		addListeners();
		initData();
	}

	private void initData() {
		List<String> planCodeList=new ArrayList<String>();
		planCodeList.add("Select");
		propertyBean = PropertyService.getPropertyBean(PlanCodeLotSeqMaintPropertyBean.class, getApplicationId());		
		planCodeList.addAll(Arrays.asList(propertyBean.getPlanCodes()));
		getFromPlanCodeComboBox().getComponent().setModel(new ComboBoxModel<String>(planCodeList));
		getToPlanCodeComboBox().getComponent().setModel(new ComboBoxModel<String>(planCodeList));	
	}

	private void addListeners() 
	{
		getMoveToButton().addActionListener(this);
		getFromPlanCodeComboBox().getComponent().addActionListener(this);
		getToPlanCodeComboBox().getComponent().addActionListener(this);
		getFromOrderListPane().getTable().getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void onTabSelected() {
		setErrorMessage("");
	}

	private void initComponents() 
	{
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);		
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == getFromPlanCodeComboBox().getComponent())
			planCodeComboBoxSelectionActionPerformed(getFromOrderListPane(),getFromPlanCodeComboBox());
		else if (e.getSource() == getToPlanCodeComboBox().getComponent())
			planCodeComboBoxSelectionActionPerformed(getToOrderListPane(),getToPlanCodeComboBox());
		else if(e.getSource() == this.getMoveToButton())
			moveToBtnActionPerformed();
	}

	private boolean verifySelection()
	{
		if(getFromPlanCodeComboBox().getComponent().getSelectedItem().toString().equals("Select"))
		{
			setErrorMessage("Please select the PlanCode from the 'From Plan Code Combobox'");
			return false;
		}
		else if (getToPlanCodeComboBox().getComponent().getSelectedItem().toString().equals("Select"))
		{
			setErrorMessage("Please select the PlanCode from the 'To Plan Code Combobox'");
			return false;
		}
		else if(getFromPlanCodeComboBox().getComponent().getSelectedItem().equals(getToPlanCodeComboBox().getComponent().getSelectedItem().toString()))
		{
			setErrorMessage("Both 'From Plan Code Combobox' and 'To Plan Code Combobox' selections are the same");
			return false;
		}
		return true;
	}

	private void moveToBtnActionPerformed() {
		try {
			setErrorMessage("");
			if (!verifySelection())
				return;
			PreProductionLot selectedFromOrder = getFromOrderListPane().getSelectedItem();
			String toPlanCode = (String) getToPlanCodeComboBox().getComponent().getSelectedItem();
			List<PreProductionLot> toPlanCodeOrderList = ServiceFactory.getDao(PreProductionLotDao.class).findAllByPlanCodeOrderBySeqNum((String) getToPlanCodeComboBox().getComponent().getSelectedItem());
			PreProductionLot lastToOrder = toPlanCodeOrderList.get(toPlanCodeOrderList.size() - 1);
			selectedFromOrder.setSequence(lastToOrder.getSequence() + 1);
			selectedFromOrder.setPlanCode(toPlanCode);
			ServiceFactory.getDao(PreProductionLotDao.class).save(selectedFromOrder);
			logUserAction(SAVED, selectedFromOrder);
			resetScreen();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void valueChanged(ListSelectionEvent e) {				 
		if (e.getSource().equals(getFromOrderListPane().getTable().getSelectionModel())) 
			enableDisableMoveToBtn();
	};

	private void enableDisableMoveToBtn() {
		if(getFromOrderListPane().getTable().getSelectedRowCount()> 0 && getToOrderListPane().getTable().getRowCount()> 0)
		{
			getMoveToButton().setEnabled(true);
		}else
		{
			getMoveToButton().setEnabled(false);
		}
	}

	private void planCodeComboBoxSelectionActionPerformed(ObjectTablePane<PreProductionLot> tablePane, LabeledComboBox labeledComboBox) {
		setErrorMessage("");
		tablePane.removeData();
		if(labeledComboBox.getComponent().getSelectedItem().toString().equals("Select"))
		{
			setErrorMessage("Please select the PlanCode from the "+labeledComboBox.getLabel().getText()+" Combobox");
			enableDisableMoveToBtn();
			return ;
		}
		List<PreProductionLot> list=ServiceFactory.getDao(PreProductionLotDao.class).findAllByPlanCodeOrderBySeqNum((String)labeledComboBox.getComponent().getSelectedItem());
		tablePane.reloadData(list);	
		enableDisableMoveToBtn();
	}

	private javax.swing.JPanel getMainPanel() {
		if (panel == null) {
			try {
				panel = new javax.swing.JPanel();
				panel.setName("MainPanel");
				panel.setLayout(null);
				panel.setMinimumSize(new java.awt.Dimension(0, 0));
				panel.add(getFromPlanCodeComboBox(), getFromPlanCodeComboBox().getName());
				panel.add(getToPlanCodeComboBox(), getToPlanCodeComboBox().getName());
				panel.add(getMoveToButton(),getMoveToButton().getName());
				panel.add(getFromOrderListPane(),getFromOrderListPane().getName());
				panel.add(getToOrderListPane(),getToOrderListPane().getName());
				panel.add(getFromOrderListLabel(),getFromOrderListLabel().getName());
				panel.add(getToOrderListLabel(),getToOrderListLabel().getName());
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return panel;
	}

	public LabeledComboBox getFromPlanCodeComboBox() {
		if(fromPlanCodeComboBox == null){
			fromPlanCodeComboBox = new LabeledComboBox("From Plan Code", true);				
			fromPlanCodeComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			fromPlanCodeComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			fromPlanCodeComboBox.setBounds(225, 20,400,50);
			fromPlanCodeComboBox.getLabel().setFont(new java.awt.Font("dialog", Font.BOLD, 16));
		}		
		return fromPlanCodeComboBox;
	}

	public LabeledComboBox getToPlanCodeComboBox() {
		if(toPlanCodeComboBox == null){
			toPlanCodeComboBox = new LabeledComboBox("To Plan Code", true);				
			toPlanCodeComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			toPlanCodeComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			toPlanCodeComboBox.setBounds(225, 90,400,50);
			toPlanCodeComboBox.getLabel().setFont(new java.awt.Font("dialog", Font.BOLD, 16));
		}		
		return toPlanCodeComboBox;
	}

	private javax.swing.JButton getMoveToButton() {
		if (moveToBtn == null) {
			try {
				moveToBtn = new javax.swing.JButton();
				moveToBtn.setName("JButtonMoveTo");
				moveToBtn.setText("Move To");
				moveToBtn.setMaximumSize(new java.awt.Dimension(45, 25));
				moveToBtn.setActionCommand("OK");
				moveToBtn.setFont(new java.awt.Font("dialog", 0, 14));
				moveToBtn.setEnabled(false);
				moveToBtn.setMinimumSize(new java.awt.Dimension(45, 25));
				moveToBtn.setBounds(425, 160, 125, 25);
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return moveToBtn;
	}

	protected ObjectTablePane<PreProductionLot> getFromOrderListPane() {
		if(fromOrderListPane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Order","productionLot").put("SEQUENCE","sequence").put("ORDER SIZE", "lotSize").put("STAMPED COUNT", "stampedCount").put("PLAN CODE", "planCode").put("PRODUCT SPEC CODE","productSpecCode");
			fromOrderListPane = new ObjectTablePane<PreProductionLot>(columnMappings.get(),true);
			fromOrderListPane.setLocation(30, 230);
			fromOrderListPane.setSize(450, 450);
			fromOrderListPane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			fromOrderListPane.setFont(new java.awt.Font("dialog", 0, 14));
			fromOrderListPane.getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer()	{
				private static final long serialVersionUID = 1L;
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if(isSelected)
					{
						component.setBackground(Color.YELLOW);						
					}else
					{
						component.setBackground(Color.WHITE);						
					}
					return component;
				}
			});
		}
		return fromOrderListPane;
	}

	protected ObjectTablePane<PreProductionLot> getToOrderListPane() {
		if(toOrderListPane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Order","productionLot").put("SEQUENCE","sequence").put("ORDER SIZE", "lotSize").put("STAMPED COUNT", "stampedCount").put("PLAN CODE", "planCode").put("PRODUCT SPEC CODE","productSpecCode");
			toOrderListPane = new ObjectTablePane<PreProductionLot>(columnMappings.get(),true);
			toOrderListPane.setLocation(500, 230);
			toOrderListPane.setSize(450, 450);
			toOrderListPane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			toOrderListPane.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return toOrderListPane;
	}
	
	private javax.swing.JLabel getFromOrderListLabel() {
		if (fromOrderListLabel == null) {
			try {
				fromOrderListLabel = new javax.swing.JLabel();
				fromOrderListLabel.setName("fromOrderListLabel");
				fromOrderListLabel.setText("From Plan Code Order List");
				fromOrderListLabel.setBounds(30, 200, 300, 23);
				fromOrderListLabel.setFont(new java.awt.Font("dialog", Font.BOLD, 16));
				fromOrderListLabel.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return fromOrderListLabel;
	}
	
	private javax.swing.JLabel getToOrderListLabel() {
		if (toOrderListLabel == null) {
			try {
				toOrderListLabel = new javax.swing.JLabel();
				toOrderListLabel.setName("toOrderListLabel");
				toOrderListLabel.setText("To Plan Code Order List");
				toOrderListLabel.setBounds(500, 200, 300, 23);
				toOrderListLabel.setFont(new java.awt.Font("dialog", Font.BOLD, 16));
				toOrderListLabel.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return toOrderListLabel;
	}

	public void handleException(Exception e) {
		if(e == null) this.clearErrorMessage();
		else {
			getLogger().error(e, "unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			e.printStackTrace();
			setErrorMessage("unexpected exception occured: " + e.getMessage());	
		}
	}

	public void resetScreen(){
		getFromPlanCodeComboBox().getComponent().setSelectedIndex(0);
		getToPlanCodeComboBox().getComponent().setSelectedIndex(0);
		getMoveToButton().setEnabled(false);
		getFromOrderListPane().removeData();
		getToOrderListPane().removeData();
		setErrorMessage("");
	}
}