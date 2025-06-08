package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.HoldReleasePropertyBean;
import com.honda.galc.client.ui.OptionDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;



public class HoldReleasePreProductionLotPanel extends TabbedPanel implements ActionListener, ItemListener
{
	private static final long serialVersionUID = 1L;
	private JPanel panel = null;
	private LabeledComboBox plantComboBox = null;
	private LabeledComboBox lineComboBox = null;
	private JLabel productionSeqLabel = null;	
	private JLabel lotsOnHoldLabel = null;
	private JTable productionSeqTable = null;
	private JTable lotsOnHoldTable = null;
	private JScrollPane productionSeqScrollPane=null;
	private JScrollPane lotsOnHoldScrollPane=null;
	private JButton holdLotsButton = null;
	private JButton releaseLotButton = null;
	private JButton resetButton = null;	
	private JCheckBox deptHoldCheckBox = null;
	private JCheckBox pcHoldCheckBox = null;
	private JCheckBox insertBeforeCheckBox = null;
	private JCheckBox insertAfterCheckBox = null;
	private String processLocation=null;
	private HoldReleasePropertyBean holdReleasePropertyBean;
	private Date lastUpdateTimeStamp;



	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == getPlantComboBox().getComponent())
			plantComboBoxActionPerformed(e);
		else if(e.getSource() == getLineComboBox().getComponent())
			loadProdSeqTableData();
		else if(e.getSource() ==getReleaseLotButton())
			releaseButtonActionPerformed(e);	
		else if(e.getSource()==getHoldLotsButton())
			holdLotsButtonActionPerformed(e);
		else if(e.getSource()==getResetButton())
			resetButtonActionPerformed(e);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == getDeptHoldCheckBox()||e.getSource() == getPcHoldCheckBox())
		{
			getLotsOnHoldTable().clearSelection();
		}
		
	}



	public HoldReleasePreProductionLotPanel(TabbedMainWindow mainWindow) {
		super("Hold Release PreProduction Lot", KeyEvent.VK_L,mainWindow);	
		initComponents();
		addListeners();

	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
	}


	private void initComponents() 
	{
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);
		holdReleasePropertyBean = PropertyService.getPropertyBean(HoldReleasePropertyBean.class, getApplicationId());
		processLocation=holdReleasePropertyBean.getProcessLocation();

		if(holdReleasePropertyBean.isSequenceBased()) {
			getPlantComboBox().setVisible(false);
			getLineComboBox().setVisible(false);
			loadProdSeqTableData();
		} else {
			getPlantCodes();
		}
	}

	public void getPlantCodes() {
		try{		
			getLineComboBox().getComponent().removeAllItems();
			getProductionSeqTable().setModel(new DefaultTableModel());
			getLotsOnHoldTable().setModel(new DefaultTableModel());
			getDeptHoldCheckBox().setEnabled(false);
			getPcHoldCheckBox().setEnabled(false);
			getInsertAfterCheckBox().setEnabled(false);
			getInsertBeforeCheckBox().setEnabled(false);
			getHoldLotsButton().setEnabled(false);
			getReleaseLotButton().setEnabled(false);

			List<Object[]> plantCodesList = getDao(PreProductionLotDao.class).findDistinctPlantCodes();

			if (plantCodesList.size() > 0) {
				getPlantComboBox().getComponent().addItem("");
				for (Object[] array : plantCodesList) {
					if(array[0]!=null)
					{
						getPlantComboBox().getComponent().addItem( (String) array[0]);
					}
				}
			}

		}
		catch(Exception e){
			handleException(e);
		}
	}
	
	private void addListeners() 
	{
		getPlantComboBox().getComponent().addActionListener(this);
		getLineComboBox().getComponent().addActionListener(this);
		getReleaseLotButton().addActionListener(this);
		getHoldLotsButton().addActionListener(this);
		getResetButton().addActionListener(this);
		getDeptHoldCheckBox().addItemListener(this);
		getPcHoldCheckBox().addItemListener(this);
	}


	private javax.swing.JPanel getMainPanel() {
		if (panel == null) {
			try {
				panel = new javax.swing.JPanel();
				panel.setName("MainPanel");
				panel.setLayout(null);
				panel.setBackground(new java.awt.Color(192,192,192));
				panel.setMinimumSize(new java.awt.Dimension(0, 0));
				getMainPanel().add(getPlantComboBox(), getPlantComboBox().getName());
				getMainPanel().add(getLineComboBox(), getLineComboBox().getName());
				getMainPanel().add(getProductionSeqPane(),getProductionSeqPane().getName());
				getMainPanel().add(getProductionSeqLabel(), getProductionSeqLabel().getName());
				getMainPanel().add(getHoldLotsButton(),getHoldLotsButton().getName());
				ButtonGroup checkboxGroup = new ButtonGroup();
				getMainPanel().add(getDeptHoldCheckBox(), getDeptHoldCheckBox().getName());
				checkboxGroup.add(getDeptHoldCheckBox());
				getMainPanel().add(getPcHoldCheckBox(), getPcHoldCheckBox().getName());
				checkboxGroup.add(getPcHoldCheckBox());
				getMainPanel().add(getLotsOnHoldPane(),getLotsOnHoldPane().getName());
				getMainPanel().add(getLotsOnHoldLabel(), getLotsOnHoldLabel().getName());
				getMainPanel().add(getReleaseLotButton(),getReleaseLotButton().getName());
				getMainPanel().add(getInsertBeforeCheckBox(), getInsertBeforeCheckBox().getName());
				checkboxGroup.add(getInsertBeforeCheckBox());
				getMainPanel().add(getInsertAfterCheckBox(), getInsertAfterCheckBox().getName());
				checkboxGroup.add(getInsertAfterCheckBox());
				getMainPanel().add(getResetButton(), getResetButton().getName());				
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return panel;
	}



	public LabeledComboBox getPlantComboBox() {
		if(plantComboBox == null){
			plantComboBox = new LabeledComboBox("Plant", true);				
			plantComboBox.setBackground(new java.awt.Color(192,192,192));
			plantComboBox.getLabel().setForeground(java.awt.Color.black);
			plantComboBox.getComponent().setBackground(java.awt.Color.white);
			plantComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			plantComboBox.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			plantComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			plantComboBox.setBounds(15, 20,165,50);
		}		
		return plantComboBox;
	}


	public LabeledComboBox getLineComboBox() {
		if(lineComboBox == null){
			lineComboBox = new LabeledComboBox("Line", true);				
			lineComboBox.setBackground(new java.awt.Color(192,192,192));
			lineComboBox.getLabel().setForeground(java.awt.Color.black);
			lineComboBox.getComponent().setBackground(java.awt.Color.white);
			lineComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			lineComboBox.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			lineComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lineComboBox.setBounds(200, 20,165,50);
		}		
		return lineComboBox;
	}


	private javax.swing.JScrollPane getProductionSeqPane() {
		if (productionSeqScrollPane == null) {
			try {
				productionSeqScrollPane = new javax.swing.JScrollPane(getProductionSeqTable());
				productionSeqScrollPane.setName("productionSeqScrollPane");
				productionSeqScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				productionSeqScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				productionSeqScrollPane.setFont(new java.awt.Font("dialog", 0, 14));
				productionSeqScrollPane.setBounds(15, 115, 750, 165);
				productionSeqScrollPane.setViewportView(getProductionSeqTable());

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return productionSeqScrollPane;
	}

	private JTable getProductionSeqTable() {
		if (productionSeqTable == null) {
			try {
				productionSeqTable = new JTable();
				productionSeqTable.setName("productionSeqTable");
				getProductionSeqPane().setColumnHeaderView(productionSeqTable.getTableHeader());
				productionSeqTable.setFont(new java.awt.Font("dialog", 0, 14));
				productionSeqTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				productionSeqTable.addMouseListener(new java.awt.event.MouseAdapter() { 
					public void mouseClicked(java.awt.event.MouseEvent e) {

						if(!HoldReleasePreProductionLotPanel.this.getInsertAfterCheckBox().isEnabled()==true&& 
								!HoldReleasePreProductionLotPanel.this.getInsertBeforeCheckBox().isEnabled()==true)
						{							
							HoldReleasePreProductionLotPanel.this.getLotsOnHoldTable().clearSelection();

						}
					}
				});

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return productionSeqTable;
	}



	private javax.swing.JLabel getProductionSeqLabel() {
		if (productionSeqLabel == null) {
			try {
				productionSeqLabel = new javax.swing.JLabel();
				productionSeqLabel.setName("JLabelReason");
				productionSeqLabel.setText("Pre-Production Sequence");
				productionSeqLabel.setMaximumSize(new java.awt.Dimension(201, 22));
				productionSeqLabel.setForeground(new java.awt.Color(0,0,0));
				productionSeqLabel.setFont(new java.awt.Font("dialog", 0, 14));
				productionSeqLabel.setBounds(17, 85, 165, 29);
				productionSeqLabel.setMinimumSize(new java.awt.Dimension(201, 22));

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return productionSeqLabel;
	}

	private javax.swing.JButton getHoldLotsButton() {
		if (holdLotsButton == null) {
			try {
				holdLotsButton = new javax.swing.JButton();
				holdLotsButton.setName("JButtonHoldLots");
				holdLotsButton.setText("Hold Lots");
				holdLotsButton.setMaximumSize(new java.awt.Dimension(45, 25));
				holdLotsButton.setActionCommand("OK");
				holdLotsButton.setFont(new java.awt.Font("dialog", 0, 14));
				holdLotsButton.setEnabled(false);
				holdLotsButton.setMinimumSize(new java.awt.Dimension(45, 25));
				holdLotsButton.setBounds(830, 200, 100, 25);

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return holdLotsButton;
	}

	private javax.swing.JCheckBox getDeptHoldCheckBox() {
		if (deptHoldCheckBox == null) {
			try {
				deptHoldCheckBox = new javax.swing.JCheckBox();
				deptHoldCheckBox.setName("JCheckBoxDeptHold");
				deptHoldCheckBox.setFont(new java.awt.Font("dialog", 0, 14));
				deptHoldCheckBox.setText("Dept. Hold");
				deptHoldCheckBox.setBounds(775, 150, 90, 22);
				deptHoldCheckBox.setEnabled(false);				

			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return deptHoldCheckBox;
	}

	private javax.swing.JCheckBox getPcHoldCheckBox() {
		if (pcHoldCheckBox == null) {
			try {
				pcHoldCheckBox = new javax.swing.JCheckBox();
				pcHoldCheckBox.setName("JCheckBoxPCHold");
				pcHoldCheckBox.setFont(new java.awt.Font("dialog", 0, 14));
				pcHoldCheckBox.setText("PC Hold");
				pcHoldCheckBox.setBounds(890, 150, 80, 22);
				pcHoldCheckBox.setEnabled(false);

			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return pcHoldCheckBox;
	}

	private javax.swing.JButton getResetButton() {
		if (resetButton == null) {
			try {
				resetButton = new javax.swing.JButton();
				resetButton.setName("JButtonResetScreen");
				resetButton.setText("Reset");
				resetButton.setMaximumSize(new java.awt.Dimension(45, 25));
				resetButton.setActionCommand("OK");
				resetButton.setFont(new java.awt.Font("dialog", 0, 14));
				resetButton.setEnabled(true);
				resetButton.setMinimumSize(new java.awt.Dimension(45, 25));
				resetButton.setBounds(450, 550, 100, 30);

			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return resetButton;
	}


	private javax.swing.JScrollPane getLotsOnHoldPane() {
		if (lotsOnHoldScrollPane == null) {
			try {
				lotsOnHoldScrollPane = new javax.swing.JScrollPane(getLotsOnHoldTable());
				lotsOnHoldScrollPane.setName("lotsOnHoldScrollPane");
				lotsOnHoldScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				lotsOnHoldScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				lotsOnHoldScrollPane.setFont(new java.awt.Font("dialog", 0, 14));
				lotsOnHoldScrollPane.setBounds(15, 330, 750, 165);
				lotsOnHoldScrollPane.setViewportView(getLotsOnHoldTable());

			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return lotsOnHoldScrollPane;
	}

	private JTable getLotsOnHoldTable() {
		if (lotsOnHoldTable == null) {
			try {
				lotsOnHoldTable = new JTable();
				lotsOnHoldTable.setName("lotsOnHoldTable");
				getLotsOnHoldPane().setColumnHeaderView(lotsOnHoldTable.getTableHeader());
				lotsOnHoldTable.setFont(new java.awt.Font("dialog", 0, 14));
				lotsOnHoldTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lotsOnHoldTable.addMouseListener(new java.awt.event.MouseAdapter() { 
					public void mouseClicked(java.awt.event.MouseEvent e) {
							HoldReleasePreProductionLotPanel.this.getProductionSeqTable().clearSelection();

					}
				});

				int[] width = {150,125,125,33,33,33,150 };
				String[] headers={"Lot Number","Starting Id","Product Spec Code","Lot Size","In PLC","Hold Status","Next Production Lot"};
				for (int i = 0; i < width.length; i++)
				{
					TableColumn column = new TableColumn();					
					column.setPreferredWidth(width[i]);
					column.setHeaderValue(headers[i]);
					lotsOnHoldTable.addColumn(column);					
				}
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return lotsOnHoldTable;
	}



	private javax.swing.JLabel getLotsOnHoldLabel() {
		if (lotsOnHoldLabel == null) {
			try {
				lotsOnHoldLabel = new javax.swing.JLabel();
				lotsOnHoldLabel.setName("lotsOnHoldLabel");
				lotsOnHoldLabel.setText("Lots On Hold");
				lotsOnHoldLabel.setMaximumSize(new java.awt.Dimension(201, 22));
				lotsOnHoldLabel.setForeground(new java.awt.Color(0,0,0));
				lotsOnHoldLabel.setFont(new java.awt.Font("dialog", 0, 14));
				lotsOnHoldLabel.setBounds(17, 300, 165, 29);
				lotsOnHoldLabel.setMinimumSize(new java.awt.Dimension(201, 22));

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return lotsOnHoldLabel;
	}

	private javax.swing.JButton getReleaseLotButton() {
		if (releaseLotButton == null) {
			try {
				releaseLotButton = new javax.swing.JButton();
				releaseLotButton.setName("JButtonReleaseLots");
				releaseLotButton.setText("Release Lot");
				releaseLotButton.setMaximumSize(new java.awt.Dimension(45, 25));
				releaseLotButton.setActionCommand("OK");
				releaseLotButton.setFont(new java.awt.Font("dialog", 0, 14));
				releaseLotButton.setEnabled(true);
				releaseLotButton.setMinimumSize(new java.awt.Dimension(45, 25));
				releaseLotButton.setBounds(830, 430, 125, 25);

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return releaseLotButton;
	}

	private javax.swing.JCheckBox getInsertBeforeCheckBox() {
		if (insertBeforeCheckBox == null) {
			try {
				insertBeforeCheckBox = new javax.swing.JCheckBox();
				insertBeforeCheckBox.setName("JCheckBoxDeptHold");
				insertBeforeCheckBox.setFont(new java.awt.Font("dialog", 0, 14));
				insertBeforeCheckBox.setText("Insert Before");
				insertBeforeCheckBox.setBounds(775, 380, 110, 22);
				insertBeforeCheckBox.setEnabled(true);				

			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return insertBeforeCheckBox;
	}

	private javax.swing.JCheckBox getInsertAfterCheckBox() {
		if (insertAfterCheckBox == null) {
			try {
				insertAfterCheckBox = new javax.swing.JCheckBox();
				insertAfterCheckBox.setName("JCheckBoxPCHold");
				insertAfterCheckBox.setFont(new java.awt.Font("dialog", 0, 14));
				insertAfterCheckBox.setText("Insert After");
				insertAfterCheckBox.setBounds(890, 380, 110, 22);
				insertAfterCheckBox.setEnabled(true);

			} catch (Exception ex) {

				handleException(ex);
			}
		}
		return insertAfterCheckBox;
	}


	public void plantComboBoxActionPerformed(ActionEvent event)
	{
		setErrorMessage(null);
		getLineComboBox().getComponent().removeAllItems();
		getProductionSeqTable().setModel(new DefaultTableModel());
		getLotsOnHoldTable().setModel(new DefaultTableModel());
		getDeptHoldCheckBox().setEnabled(false);
		getPcHoldCheckBox().setEnabled(false);
		getInsertAfterCheckBox().setEnabled(false);
		getInsertBeforeCheckBox().setEnabled(false);
		getHoldLotsButton().setEnabled(false);
		getReleaseLotButton().setEnabled(false);


		if(getPlantComboBox().getComponent().getSelectedItem()!=null&&getPlantComboBox().getComponent().getSelectedItem().toString().trim().length()>0)
		{	
			List<Object[]> linesList=getDao(PreProductionLotDao.class).findDistinctLines(getPlantComboBox().getComponent().getSelectedItem().toString().trim(),processLocation);
			if (linesList.size() > 0)
			{
				getLineComboBox().getComponent().addItem("");
				for (Object[] array : linesList) 
				{
					if(array[0]!=null)
					{
						getLineComboBox().getComponent().addItem( (String) array[0]);
					}
				}
			}
		}
	}

	public DefaultTableModel createModel()
	{
		DefaultTableModel model=new DefaultTableModel();
		String columnHeader = holdReleasePropertyBean.isSequenceBased() ? "Sequence" : "Next Production Lot";

		String[] headers={"Lot Number","Starting Id","Product Spec Code","Lot Size","In PLC","Hold Status",columnHeader};

		for (int i = 0; i < headers.length; i++)
		{
			model.addColumn(headers[i]);
		}
		return model;

	}

	public void loadProdSeqTableData()
	{
		setErrorMessage(null);

		if(holdReleasePropertyBean.isSequenceBased()) {
			loadPreProductionLotsBySequence();
		} else {
			Object selectedLine = getLineComboBox().getComponent().getSelectedItem();
			if(selectedLine == null || StringUtils.isEmpty(selectedLine.toString())) {
				return;
			}
			loadPreProductionLots();
			getLotsOnHoldData();
		}

		getDeptHoldCheckBox().setEnabled(true);
		getPcHoldCheckBox().setEnabled(true);
		getInsertAfterCheckBox().setEnabled(true);
		getInsertBeforeCheckBox().setEnabled(true);
		getHoldLotsButton().setEnabled(true);
		getReleaseLotButton().setEnabled(true);
		refreshLastUpdateTimestamp();
	}
	
	private void loadPreProductionLots() {
		List<PreProductionLot> preProductionLotList=getDao(PreProductionLotDao.class).findLotsWithNullNextProdLot(getPlantComboBox().getComponent().getSelectedItem().toString().trim(),getLineComboBox().getComponent().getSelectedItem().toString().trim(), processLocation);
		boolean LastDataFlag = true;
		DefaultTableModel prodSeqTableModel=createModel();
		while(preProductionLotList.size() > 0) 
		{
			PreProductionLot lot = preProductionLotList.get(0);
			Vector<String> lotData = createPreProductionLotData(lot);
			
			if (LastDataFlag)
			{
				prodSeqTableModel.addRow(lotData);
				LastDataFlag = false;
			}
			else 
			{				
				ProductionLot productionLot=getDao(ProductionLotDao.class).findByKey(lot.getProductionLot().toString());
				if(productionLot.getProdLotKd()!=null&& productionLot.getProdLotKd().equals(lot.getProductionLot().toString()))				
				{
					prodSeqTableModel.insertRow(0, lotData);
				}
			}
			preProductionLotList=getDao(PreProductionLotDao.class).findLotByNextProductionLot(lot.getProductionLot());
		}
		getProductionSeqTable().setModel(prodSeqTableModel);
	}

	private void loadPreProductionLotsBySequence() {

		List<PreProductionLot> preProductionLots = getDao(PreProductionLotDao.class).findAllByPlanCode(holdReleasePropertyBean.getPlanCode());
		
		List<PreProductionLot> upcomingLots = new SortedArrayList<PreProductionLot>("getSequence");
		List<PreProductionLot> onHoldLots = new SortedArrayList<PreProductionLot>("getProductionLot");
		for(PreProductionLot lot : preProductionLots) {

			if(lot.getHoldStatus() == 0) {
				onHoldLots.add(lot);
			} else if(lot.getSendStatus() == PreProductionLotSendStatus.WAITING) {
				upcomingLots.add(lot);
			}
		}
		
		DefaultTableModel prodSeqTableModel=createModel();

		for(PreProductionLot lot : upcomingLots) {
			prodSeqTableModel.addRow(createPreProductionLotData(lot));
		}
		getProductionSeqTable().setModel(prodSeqTableModel);
		
//		On hold lots
		DefaultTableModel lotsOnHoldTableModel=createModel();
		for(PreProductionLot holdLot : onHoldLots) {
			lotsOnHoldTableModel.addRow(createPreProductionLotData(holdLot));
		}
		getLotsOnHoldTable().setModel(lotsOnHoldTableModel);
	}

	private Vector<String> createPreProductionLotData(PreProductionLot lot) {
		Vector<String> lotData = new Vector<String>();
		lotData.addElement(lot.getProductionLot().toString());
		lotData.addElement(lot.getStartProductId().toString());
		lotData.addElement(lot.getProductSpecCode().toString());
		lotData.addElement(new Integer(lot.getLotSize()).toString());
		lotData.addElement(new Integer(lot.getSendStatusId()) > 0 ? "Yes" : "No");
		lotData.addElement(new Integer(lot.getHoldStatus()).toString());
		if(holdReleasePropertyBean.isSequenceBased()) {
			lotData.addElement(Double.toString(lot.getSequence()));
		} else {
			lotData.addElement(lot.getNextProductionLot() == null ? "" : lot.getNextProductionLot().toString());
		}

		return lotData;
	}

	private void getLotsOnHoldData() 
	{
		List<PreProductionLot> lotsOnHoldList=getDao(PreProductionLotDao.class).findLotsOnHold(getPlantComboBox().getComponent().getSelectedItem().toString().trim(),getLineComboBox().getComponent().getSelectedItem().toString().trim(),processLocation);
		DefaultTableModel lotsOnHoldTableModel=createModel();
		for(PreProductionLot holdLot:lotsOnHoldList)
		{
			ProductionLot productionLot=getDao(ProductionLotDao.class).findByKey(holdLot.getProductionLot());
			if(productionLot.getProdLotKd()!=null&& productionLot.getProdLotKd().equals(holdLot.getProductionLot()))				
			{
				lotsOnHoldTableModel.addRow(createPreProductionLotData(holdLot));
			}
		}
		getLotsOnHoldTable().setModel(lotsOnHoldTableModel);
	}

	private void saveData()
	{
		try {
			DefaultTableModel productSeqModel=(DefaultTableModel)getProductionSeqTable().getModel();
			List<PreProductionLot> lotsOnHoldList=getDao(PreProductionLotDao.class).findAllSentLots(processLocation);
			if (lotsOnHoldList.size() == 0) 
			{
				String lastLotNum=null;
				lastLotNum=getDao(PreProductionLotDao.class).findLastStampedLot(processLocation);


				String prodLotKd =getDao(PreProductionLotDao.class).findSplitLot(productSeqModel.getValueAt(0, 0).toString());
				if(prodLotKd!=null)
				{
					getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(prodLotKd, Integer.parseInt(productSeqModel.getValueAt(0, 5).toString()), lastLotNum);
					logUserAction("updated production lot: " + lastLotNum);
					getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(productSeqModel.getValueAt(0, 0).toString(), Integer.parseInt(productSeqModel.getValueAt(0, 5).toString()), prodLotKd);
					logUserAction("unpdted production lot: " + prodLotKd);
				}else
				{
					getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(productSeqModel.getValueAt(0, 0).toString(), Integer.parseInt(productSeqModel.getValueAt(0, 5).toString()), lastLotNum);
					logUserAction("updated production lot: " + lastLotNum);
				}			   
			}	

			for(int i = 0; i < productSeqModel.getRowCount(); i++)
			{
				PreProductionLot lot=getDao(PreProductionLotDao.class).findByKey(productSeqModel.getValueAt(0, 0).toString());
				if(lot==null)
				{
					setErrorMessage("Target ProductionLot:"+productSeqModel.getValueAt(0, 0).toString()+" does not exist in database");
				}

				Integer k=null;
				if((i!=(productSeqModel.getRowCount()-1))&&((!productSeqModel.getValueAt(i,6).equals(""))))
				{
					for (int j=i+1;j<productSeqModel.getRowCount();j++)
					{
						if(productSeqModel.getValueAt(j,5).toString().equals("1"))
						{
							k=j;
							break;
						}
					}

					String prodLotKd =getDao(PreProductionLotDao.class).findSplitLot(productSeqModel.getValueAt(k, 0).toString());
					if(prodLotKd!=null)
					{
						getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(prodLotKd, Integer.parseInt(productSeqModel.getValueAt(i, 5).toString()), productSeqModel.getValueAt(i, 0).toString());
						logUserAction("updated production lot: " + productSeqModel.getValueAt(i, 0).toString());
						getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(productSeqModel.getValueAt(k, 0).toString(), Integer.parseInt(productSeqModel.getValueAt(i, 5).toString()), prodLotKd);
						logUserAction("updated production lot: " + prodLotKd);
					}else
					{
						getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(productSeqModel.getValueAt(k, 0).toString(), Integer.parseInt(productSeqModel.getValueAt(i, 5).toString()), productSeqModel.getValueAt(i, 0).toString());
						logUserAction("updated production lot: " + productSeqModel.getValueAt(i, 0).toString());
					}
				}else
				{
					getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(null, Integer.parseInt(productSeqModel.getValueAt(i, 5).toString()), productSeqModel.getValueAt(i, 0).toString());
					logUserAction("updated production lot: " + productSeqModel.getValueAt(i, 0).toString());
					String prodLotKd =getDao(PreProductionLotDao.class).findSplitLot(productSeqModel.getValueAt(i, 0).toString());
					if(prodLotKd!=null)
					{
						getDao(PreProductionLotDao.class).updateNextLotAndHoldStatus(null, Integer.parseInt(productSeqModel.getValueAt(i, 5).toString()), prodLotKd);
						logUserAction("updated production lot: " + prodLotKd);
					}
				}
			}
		} catch (Exception e) {

			handleException(e);
		}
		refreshLastUpdateTimestamp();
	}

	public void releaseButtonActionPerformed(ActionEvent event)
	{
		if(checkProdLotSeqChanged()) return;
		setErrorMessage(null);
		if(getLotsOnHoldTable().getSelectedRow()==-1||getProductionSeqTable().getSelectedRow()==-1)
		{
			setErrorMessage("Please select both the rows in Pre-Production Sequence table and Lots on Hold table");
			return;
		}
		
		if(holdReleasePropertyBean.isSequenceBased()) {
			releaseBySequence();
		} else {
			releaseByNextProductionLot();
		}

	}
	
	private void releaseByNextProductionLot() {
		int selectedRow = getProductionSeqTable().getSelectedRow();
		String[] seqs = new String[7];
		DefaultTableModel productSeqModel=(DefaultTableModel)getProductionSeqTable().getModel();
		DefaultTableModel lotsOnHoldModel=(DefaultTableModel)getLotsOnHoldTable().getModel();

		String comboPPLot = lotsOnHoldModel.getValueAt(getLotsOnHoldTable().getSelectedRow(), 0).toString();
		String tablePPLot = "";

		if(getInsertBeforeCheckBox().isSelected()==true) {
			for (int i=0; i<lotsOnHoldModel.getRowCount(); i++)
			{
				if (comboPPLot.equals(lotsOnHoldModel.getValueAt(i, 0))) 
				{
					for (int j=0; j<5; j++) 
					{
						seqs[j] = (String) lotsOnHoldModel.getValueAt(i, j);
					}
					seqs[5] = "1";
					tablePPLot = (String) productSeqModel.getValueAt(selectedRow, 0);
					seqs[6] = tablePPLot;
					lotsOnHoldModel.removeRow(i);
				}
			}
			for (int i=0; i<productSeqModel.getRowCount(); i++) 
			{
				if (tablePPLot.equals(productSeqModel.getValueAt(i, 6))) 
				{
					productSeqModel.setValueAt(comboPPLot, i, 6);
				}
				if (tablePPLot.equals(productSeqModel.getValueAt(i, 0))) 
				{
					productSeqModel.insertRow(i, seqs);
					i++;
				}
			}
			saveData();
		} else if(getInsertAfterCheckBox().isSelected()==true) {

			String nextPPLot = "";
			for (int i=0; i<lotsOnHoldModel.getRowCount(); i++) 
			{
				if (comboPPLot.equals(lotsOnHoldModel.getValueAt(i, 0))) 
				{
					for (int j=0; j<5; j++) 
					{
						seqs[j] = (String) lotsOnHoldModel.getValueAt(i, j);
					}

					seqs[5] = "1";
					tablePPLot = (String) productSeqModel.getValueAt(selectedRow, 0);
					for (int j=0; j<productSeqModel.getRowCount(); j++)
					{
						if (tablePPLot.equals(productSeqModel.getValueAt(j, 0)))
						{
							nextPPLot = (String) productSeqModel.getValueAt(j, 6);
							productSeqModel.setValueAt(comboPPLot,j,6);
							break;
						}
					}
					seqs[6] = nextPPLot;
					lotsOnHoldModel.removeRow(i);
				}
			}

			int tableRows = productSeqModel.getRowCount();
			for (int i=0; i<tableRows; i++) 
			{
				if (tablePPLot.equals(productSeqModel.getValueAt(i, 0))) 
				{
					if (i == (tableRows-1))
					{
						productSeqModel.addRow(seqs);
					} else 
					{
						productSeqModel.insertRow(i+1, seqs);
					}
					break;
				}
			}
			saveData();
		}else
		{
			setErrorMessage("Please select either Insert Before or Insert After check box");
		}
	}
	
	private void releaseBySequence() {
		if(!getInsertBeforeCheckBox().isSelected() && !getInsertAfterCheckBox().isSelected()) {
			setErrorMessage("Please select either Insert Before or Insert After check box");
			return;
		}

		String releaseLotNumber = (String) getLotsOnHoldTable().getModel().getValueAt(getLotsOnHoldTable().getSelectedRow(), 0);
		PreProductionLot releaseLot = getDao(PreProductionLotDao.class).findByKey(releaseLotNumber);
		
		int sequenceInterval = PropertyService.getPropertyBean(ProductOnHlPropertyBean.class).getSequenceInterval();
		double maxSequence = getDao(PreProductionLotDao.class).findMaxSequence(holdReleasePropertyBean.getPlanCode()).doubleValue();
		releaseLot.setHoldStatus(1);
		releaseLot.setSequence(maxSequence + sequenceInterval);
		
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		changedLots.add(releaseLot);

		DefaultTableModel prodLotModel=(DefaultTableModel)getProductionSeqTable().getModel();
		int lastRow = prodLotModel.getRowCount() - 1;
		int selectedRow = getProductionSeqTable().getSelectedRow();

		PreProductionLot lot;
		double temp;
		for(int i = lastRow; i >= selectedRow; i--) {
			if(i > selectedRow || getInsertBeforeCheckBox().isSelected()) {
				lot = getDao(PreProductionLotDao.class).findByKey((String) prodLotModel.getValueAt(i, 0));
				temp = lot.getSequence();
				lot.setSequence(releaseLot.getSequence());
				releaseLot.setSequence(temp);
				changedLots.add(lot);
			}
		}

		getDao(PreProductionLotDao.class).saveAll(changedLots);
		logUserAction(SAVED, changedLots);
		loadProdSeqTableData();
	}

	public void resetButtonActionPerformed(ActionEvent ie)
	{
		setErrorMessage(null);
		getProductionSeqTable().setModel(new DefaultTableModel());
		getLotsOnHoldTable().setModel(new DefaultTableModel());
		getDeptHoldCheckBox().setEnabled(false);
		getPcHoldCheckBox().setEnabled(false);
		getInsertAfterCheckBox().setEnabled(false);
		getInsertBeforeCheckBox().setEnabled(false);
		getHoldLotsButton().setEnabled(false);
		getReleaseLotButton().setEnabled(false);
		if(holdReleasePropertyBean.isSequenceBased()) {
			loadProdSeqTableData();
		} else {
			getPlantComboBox().getComponent().setSelectedIndex(0);
			getLineComboBox().getComponent().removeAllItems();
		}
	}

	public void holdLotsButtonActionPerformed(ActionEvent ie) 
	{
		try {
			if(checkProdLotSeqChanged()) return;
			setErrorMessage(null);
			if(getLotsOnHoldTable().getSelectedRow()!=-1)
			{
				setErrorMessage("Please select only the row in Pre-Production Sequence table and not the Lots on Hold table.Click reset button to redo");
				return;
			}
			if (!getPcHoldCheckBox().isSelected()&&!getDeptHoldCheckBox().isSelected()) 
			{
				setErrorMessage("Please select either Dept. Hold or PC hold check box");
				return; 
			}

			int selIndex = getProductionSeqTable().getSelectedRow();
			String preProductionLot = "";
			if (selIndex != -1) {
				preProductionLot = (String) getProductionSeqTable().getValueAt(selIndex, 0);
			}else
			{
				setErrorMessage("Please select a Lot from Product Sequence table ");
				return; 
			}

			
			PreProductionLot lot = getDao(PreProductionLotDao.class).findByKey(preProductionLot);
			if(lot.getSendStatusId() > 0 ) {
				if(!OptionDialog.confirm(this.window, "The lot was sent to PLC. Do you really want to put it on hold?")) {
					return;
				}
			}

			if(holdReleasePropertyBean.isSequenceBased()) {
				holdBySequence(preProductionLot);
			} else {
				holdByNextProductionLot(preProductionLot, selIndex);
			}
			
		} catch (Exception e) {
			handleException(e);
		}
			
	}
	
	private void holdByNextProductionLot(String preProductionLot, int selIndex) {
		DefaultTableModel productSeqModel=(DefaultTableModel)getProductionSeqTable().getModel();
		DefaultTableModel lotsOnHoldModel=(DefaultTableModel)getLotsOnHoldTable().getModel();
		
		for (int i = 0; i < productSeqModel.getRowCount(); i++)
		{
			if (preProductionLot.equals(productSeqModel.getValueAt(i, 0))) 
			{
				String nextLot = (String) productSeqModel.getValueAt(i,6);
				if(getPcHoldCheckBox().isSelected())
				{
					productSeqModel.setValueAt("2", i, 5);
				}else if(getDeptHoldCheckBox().isSelected())
				{
					productSeqModel.setValueAt("0", i, 5);
				}
				productSeqModel.setValueAt("", i, 6);

				for (int j=0; j<productSeqModel.getRowCount(); j++) 
				{
					if (preProductionLot.equals((String) productSeqModel.getValueAt(j,6)))
					{
						productSeqModel.setValueAt(nextLot, j, 6);
					}
				}

			}
		}
		saveData();
		String[] seqs = new String[7];
		for (int j=0; j<7; j++) 
		{
			seqs[j] = (String) productSeqModel.getValueAt(selIndex, j);
		}
		productSeqModel.removeRow(selIndex);
		lotsOnHoldModel.addRow(seqs);
	}
	
	private void holdBySequence(String lotNumber) {
		PreProductionLot lot = getDao(PreProductionLotDao.class).findByKey(lotNumber);
		lot.setHoldStatus(0);
		getDao(PreProductionLotDao.class).save(lot);
		logUserAction(SAVED, lot);
		loadProdSeqTableData();
	}
	
	private boolean isProdLotSeqChanged(){
		Date lastUpdateTimeKN = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(processLocation);
		if(lastUpdateTimeKN == null) return false;
		if(lastUpdateTimeStamp == null) return true;
		return lastUpdateTimeKN.after(lastUpdateTimeStamp);
	}

	private boolean checkProdLotSeqChanged() {
		if(isProdLotSeqChanged()){ 
			loadProdSeqTableData();
			setErrorMessage("The Production Lot Sequence was refreshed because it was changed from other application.Please redo your operation");
			return true;
		}
		return false;

	}
	
	private void refreshLastUpdateTimestamp() {
		lastUpdateTimeStamp = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(processLocation);
	}

}