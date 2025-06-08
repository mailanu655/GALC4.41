package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

public class VehicleColorChangePanel extends TabbedPanel implements ActionListener
{
	protected static List <Frame> frames;
	private static final long serialVersionUID = 1L;
	private LabeledTextField numberInputField;
	private Frame enteredVin;
	private Frame selectedVin;
    private JPanel productionLotPanel;
    private JPanel lastProcessPointPanel;
    private JPanel comparisonPanel;
    private JPanel frameOnePanel;
    private JPanel frameTwoPanel;
    private ObjectTablePane<FrameSpec> colorCodesTable;
    private ObjectTablePane<PreProductionLot> matchingLotsTable;
    private ObjectTablePane<Frame> matchingVinsTable;
	private JButton switchButton;
	
	
	public VehicleColorChangePanel(TabbedMainWindow mainWindow){
		super("Vehicle Color Change", KeyEvent.VK_Z, mainWindow);
	}

	@Override
	public void onTabSelected() {
		try {
			if (isInitialized)	return;
			initComponents();
			addListeners();
			isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start Vehicle Color Change panel.");
			setErrorMessage("Exception to start Vehicle Color Change panel." + e.toString());
		}	
	}
	
	private void initComponents() {
		setLayout(new MigLayout("insets 1", "[grow,fill]"));
		add(this.createVinEntryPanel(),"grow");
		add(this.createLastProcessPointPanel(),"wrap, spany 2, width 360:360:360, center");
		add(this.createProductionLotPanel(),"wrap, grow");
		add(this.createVinSelectionPanel(),"span,wrap");
		add(this.createComparisonPanel(), "center, span 2");
	}
	
	protected JPanel createVinEntryPanel(){
		this.numberInputField = this.createTextField(" VIN: ", true, true);
		this.numberInputField.getComponent().setEditable(true);
		this.numberInputField.setEnabled(true);
		this.numberInputField.getComponent().setName("VinInputField");
		JPanel panel= new JPanel(new MigLayout("align 50% 50%"));
		panel.add(this.numberInputField, "gaptop 10");
		return panel;
	}
	
	protected JPanel createProductionLotPanel(){
		this.productionLotPanel = new JPanel(new MigLayout("align 50% 50%","[grow,fill]"));
		this.productionLotPanel.add(createTextField("PRODUCTION_LOT",false, true));
		this.productionLotPanel.add(createTextField("KD_LOT",false, true));
		this.productionLotPanel.add(createTextField("LOT_SIZE",false, true));
		this.productionLotPanel.add(createTextField("PRODUCT_SPEC_CODE",false, true));
		this.productionLotPanel.setBorder(new TitledBorder("Production Lot"));
		return this.productionLotPanel;
	}
	
    protected JPanel createLastProcessPointPanel(){
    	this.lastProcessPointPanel = new JPanel(new MigLayout("insets 0 0 0 0,gapy 0,fill"));
    	this.lastProcessPointPanel.add(createTextField("Proces Point: ",true, true),"wrap, al right");
    	this.lastProcessPointPanel.add(createTextField("Plant: ",true, true),"wrap, al right, gapy 0 0");
    	this.lastProcessPointPanel.add(createTextField("Division: ",true, true),"wrap, al right");
    	this.lastProcessPointPanel.add(createTextField("Line: ",true, true),"wrap, al right");
    	this.lastProcessPointPanel.add(createTextField("Timestamp: ",true, true),"wrap, al right");
    	this.lastProcessPointPanel.setBorder(new TitledBorder("Last Process Point"));
    	return this.lastProcessPointPanel;
	}
    
    protected JPanel createVinSelectionPanel(){
		JPanel panel= new JPanel(new MigLayout("ins 0, center"));
		panel.add(this.createColorCodesTable(),"width 50%");
		panel.add(this.createMatchingLotsTable(),"width 25%");
		panel.add(this.createMatchingVinsTable(),"width 25%");
		panel.setBorder(new TitledBorder(""));
		return panel;
    }
    
    protected ObjectTablePane<FrameSpec> createColorCodesTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Ext Color Code", "extColorCode");
		mapping.put("Ext Color Desc", "extColorDescription");
		mapping.put("Int Color Code", "intColorCode");
		mapping.put("Int Color Desc", "intColorDescription");
		this.colorCodesTable = new ObjectTablePane<FrameSpec>(mapping.get(), true, true);
		this.colorCodesTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.colorCodesTable.getTable().setName("colorCodesTable");
		this.colorCodesTable.setBorder(new TitledBorder("Color Codes"));
		return colorCodesTable;
	}
    
    protected ObjectTablePane<PreProductionLot> createMatchingLotsTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Production Lot", "productionLot");
		this.matchingLotsTable = new ObjectTablePane<PreProductionLot>(mapping.get(), true, true);
		this.matchingLotsTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.matchingLotsTable.getTable().setName("matchingLotsTable");
		this.matchingLotsTable.setBorder(new TitledBorder("Production Lots"));
		return matchingLotsTable;
	}
    
    protected ObjectTablePane<Frame> createMatchingVinsTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("VIN", "productId");
		this.matchingVinsTable = new ObjectTablePane<Frame>(mapping.get(), true, true);
		this.matchingVinsTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.matchingVinsTable.getTable().setName("matchingVinsTable");
		this.matchingVinsTable.setBorder(new TitledBorder("VINs"));
		return matchingVinsTable;
	}
    
    protected JPanel createComparisonPanel(){
    	this.comparisonPanel = new JPanel(new MigLayout("wrap2, center"));
    	this.comparisonPanel.add(this.createFrameOnePanel());
    	this.comparisonPanel.add(this.createSwitchButton(), "spany2, alignx center, gaptop 26, gapleft 30");
    	this.comparisonPanel.add(this.createFrameTwoPanel());
    	return this.comparisonPanel;
    }
    
    protected JPanel createFrameOnePanel() {
    	this.frameOnePanel = new JPanel(new MigLayout("insets 1, align 50% 50%"));
    	this.frameOnePanel.add(new JLabel("FRAME 1: "),"aligny bottom");
    	this.frameOnePanel.add(createTextField("Product ID",false, true),"width 100:215:215, growx");
    	this.frameOnePanel.add(createTextField("Production Lot",false, true),"width 100:215:215, growx");
    	this.frameOnePanel.add(createTextField("KD Lot",false, true),"width 100:215:215, growx");
    	this.frameOnePanel.add(createTextField("Product SpecCode",false, true),"width 100:215:215, growx");
    	this.frameOnePanel.add(createTextField("Last Process Point",false, true),"width 100:215:215, growx");
    	this.frameOnePanel.setBorder(null);
		return this.frameOnePanel;
	}
    
    protected JPanel createFrameTwoPanel() {
    	this.frameTwoPanel = new JPanel(new MigLayout("insets 1, align 50% 50%"));
    	this.frameTwoPanel.add(new JLabel("FRAME 2: "),"aligny top");
    	this.frameTwoPanel.add(createTextField("",false, false),"width 100:215:215, growx");
    	this.frameTwoPanel.add(createTextField("",false, false),"width 100:215:215, growx");
    	this.frameTwoPanel.add(createTextField("",false, false),"width 100:215:215, growx");
    	this.frameTwoPanel.add(createTextField("",false, false),"width 100:215:215, growx");
    	this.frameTwoPanel.add(createTextField("",false, false),"width 100:215:215, growx");
		return this.frameTwoPanel;
	}
    
    public JButton createSwitchButton(){
		if(this.switchButton == null){
			this.switchButton = new JButton("Switch Frames");
			this.switchButton.setName("SwitchFramesButton");
			this.switchButton.setEnabled(false);
		}
		return switchButton;
	}
    
    protected LabeledTextField createTextField(String title, Boolean horizontal, Boolean showLabel){
    	LabeledTextField textField = new LabeledTextField(title,horizontal);
    	textField.getComponent().setEditable(false);
    	textField.getComponent().setHorizontalAlignment(JTextField.CENTER);
    	textField.getLabel().setFont(new Font("Dialog", Font.BOLD, 12));
    	if (!horizontal) textField.getLabel().setAlignmentX(CENTER_ALIGNMENT);
    	textField.getLabel().setHorizontalAlignment(SwingConstants.CENTER);
    	if (!showLabel) textField.remove(textField.getLabel());
    	textField.setBorder(null);
    	((BorderLayout)textField.getLayout()).setVgap(0);
    	return textField;
    }
    
	private void addListeners() {
		this.numberInputField.getComponent().addActionListener(this);
		
		((TablePane)this.colorCodesTable).getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				if (e.getValueIsAdjusting()) return;
            	colorSelected();
            }
		});
		
		((TablePane)this.matchingLotsTable).getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
            	lotSelected();
            }
		});
		
		((TablePane)this.matchingVinsTable).getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				vinSelected();
            }
		});
		
		this.switchButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.numberInputField.getComponent())) numberEntered();
		else if(e.getSource().equals(this.switchButton)) buttonPushed();
	}
	
	public void numberEntered(){
		this.resetPanel();
		this.clearErrorMessage();
		String enteredNum = this.numberInputField.getComponent().getText().trim();
		Logger.getLogger().info("Entered VIN " + enteredNum);
		ProductTypeData productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey("FRAME");
		if(!productTypeData.isNumberValid(enteredNum)){
			this.getMainWindow().setErrorMessage("Invalid VIN format.");
			return;
		}
		if ((this.enteredVin = ServiceFactory.getDao(FrameDao.class).findBySn(enteredNum)) == null){
			this.getMainWindow().setErrorMessage("VIN "+ enteredNum +" does not exist.");
			return;
		}
		if (!this.validate(this.enteredVin)) return;
		List<PreProductionLot> prodLot = new ArrayList<PreProductionLot>();
		prodLot.add(ServiceFactory.getDao(PreProductionLotDao.class).findByKey(enteredVin.getProductionLot()));
		this.reloadProductionLotPanel();
		this.reloadLastProcessPointPanel();
		this.reloadColorCodesTable();
		this.reloadFrameOnePanel();
	}
	
	protected void reloadProductionLotPanel(){
		PreProductionLot prodLot;
		if ((prodLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(enteredVin.getProductionLot())) != null){
			((LabeledTextField) this.productionLotPanel.getComponent(0)).getComponent().setText(prodLot.getId());
			((LabeledTextField) this.productionLotPanel.getComponent(1)).getComponent().setText(prodLot.getKdLot());
			((LabeledTextField) this.productionLotPanel.getComponent(2)).getComponent().setText(String.valueOf(prodLot.getLotSize()));
			((LabeledTextField) this.productionLotPanel.getComponent(3)).getComponent().setText(prodLot.getProductSpecCode());
		} else {
			Logger.getLogger().error("Production lot missing for product " + this.enteredVin.getId());
		}
	}
	
	protected void reloadLastProcessPointPanel(){
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(enteredVin.getLastPassingProcessPointId());
		List <ProductResult> productResults;
		((LabeledTextField) this.lastProcessPointPanel.getComponent(0)).getComponent().setText(processPoint.getId());
		((LabeledTextField) this.lastProcessPointPanel.getComponent(1)).getComponent().setText(processPoint.getPlantName());
		((LabeledTextField) this.lastProcessPointPanel.getComponent(2)).getComponent().setText(processPoint.getDivisionName());
		((LabeledTextField) this.lastProcessPointPanel.getComponent(3)).getComponent().setText(processPoint.getLineName());
		
		productResults = ServiceFactory.getDao(ProductResultDao.class).findAllByProductAndProcessPoint(this.enteredVin.getId(), processPoint.getId());
		if (productResults.size() > 0){
			Timestamp timestamp = null;
			for (ProductResult productResult : productResults){
				timestamp = productResult.getActualTimestamp();
			}
			if (timestamp != null) ((LabeledTextField) this.lastProcessPointPanel.getComponent(4)).getComponent().setText(timestamp.toString());
		}else {
			this.setMessage("Product result record for VIN " + this.enteredVin.getId() + " at process point " + this.enteredVin.getLastPassingProcessPointId() + " is missing.");
		}
	}
	
	public void reloadColorCodesTable(){
		List<FrameSpec> frameSpecs = new SortedArrayList<FrameSpec>();
		frameSpecs.addAll(ServiceFactory.getDao(FrameSpecDao.class).findAllByPrefix(this.enteredVin.getYMTO()));
		frameSpecs.remove(ServiceFactory.getDao(FrameSpecDao.class).findByKey(this.enteredVin.getProductSpecCode()));
		this.colorCodesTable.reloadData(frameSpecs);
		this.matchingLotsTable.removeData();
		this.matchingVinsTable.removeData();
		
	}
	
	public void colorSelected(){
		FrameSpec frameSpec;
		if ((frameSpec = this.colorCodesTable.getSelectedItem()) != null){
			List<PreProductionLot> prodLots = new SortedArrayList<PreProductionLot>("prodLots"); 
			prodLots.addAll(ServiceFactory.getDao(PreProductionLotDao.class).findAllPreProductionLotsByProductSpecCode(frameSpec.getProductSpecCode()));
			for (PreProductionLot prodLot : prodLots){
				if (prodLot.getSendStatusId() != PreProductionLotSendStatus.WAITING.getId())
					prodLots.remove(prodLot);
			}
			this.matchingLotsTable.reloadData(prodLots);
			this.matchingVinsTable.removeData();
			
			((LabeledTextField) this.frameTwoPanel.getComponent(1)).getComponent().setText("");
			((LabeledTextField) this.frameTwoPanel.getComponent(2)).getComponent().setText("");
			((LabeledTextField) this.frameTwoPanel.getComponent(3)).getComponent().setText("");
			((LabeledTextField) this.frameTwoPanel.getComponent(4)).getComponent().setText("");
		}
	}
	
	public void lotSelected(){
		PreProductionLot lot = this.matchingLotsTable.getSelectedItem();
		if ((lot = this.matchingLotsTable.getSelectedItem()) != null){
			List<Frame> frames = new SortedArrayList<Frame>("getProductId"); 
			frames.addAll( ServiceFactory.getDao(FrameDao.class).findAllByProductionLot(lot.getId()));
			
			this.matchingVinsTable.reloadData(frames);
		}
	}
	
	public void vinSelected(){
		this.selectedVin = this.matchingVinsTable.getSelectedItem();
		this.clearErrorMessage();
		this.reloadFrameTwoPanel();
		if (this.selectedVin == null){
			this.switchButton.setEnabled(false);
		} else {
			this.switchButton.setEnabled(true);
		}
	}
	
	public void reloadFrameOnePanel(){
		((LabeledTextField) this.frameOnePanel.getComponent(1)).getComponent().setText(this.enteredVin.getId());
		((LabeledTextField) this.frameOnePanel.getComponent(2)).getComponent().setText(this.enteredVin.getProductionLot());
		((LabeledTextField) this.frameOnePanel.getComponent(3)).getComponent().setText(this.enteredVin.getKdLotNumber());
		((LabeledTextField) this.frameOnePanel.getComponent(4)).getComponent().setText(this.enteredVin.getProductSpecCode());
		((LabeledTextField) this.frameOnePanel.getComponent(5)).getComponent().setText(this.enteredVin.getLastPassingProcessPointId());
	}
	
	public void reloadFrameTwoPanel(){
		if (this.matchingVinsTable.getSelectedItem() != null){
			((LabeledTextField) this.frameTwoPanel.getComponent(1)).getComponent().setText(this.selectedVin.getId());
			((LabeledTextField) this.frameTwoPanel.getComponent(2)).getComponent().setText(this.selectedVin.getProductionLot());
			((LabeledTextField) this.frameTwoPanel.getComponent(3)).getComponent().setText(this.selectedVin.getKdLotNumber());
			((LabeledTextField) this.frameTwoPanel.getComponent(4)).getComponent().setText(this.selectedVin.getProductSpecCode());
			((LabeledTextField) this.frameTwoPanel.getComponent(5)).getComponent().setText(this.selectedVin.getLastPassingProcessPointId());
		}
	}
	
	public void buttonPushed(){
		List<Frame> frames = new ArrayList<Frame>();
		if (!this.validate(selectedVin)) return;
		String tmpProdLot = this.enteredVin.getProductionLot();
		String tmpProdSpecCode = this.enteredVin.getProductSpecCode();
		String tmpLastProcPoint = this.enteredVin.getLastPassingProcessPointId();
		this.enteredVin.setProductionLot(selectedVin.getProductionLot());
		selectedVin.setProductionLot(tmpProdLot);
		this.enteredVin.setProductSpecCode(selectedVin.getProductSpecCode());
		selectedVin.setProductSpecCode(tmpProdSpecCode);
		this.enteredVin.setLastPassingProcessPointId(selectedVin.getLastPassingProcessPointId());
		selectedVin.setLastPassingProcessPointId(tmpLastProcPoint);
		switchButton.setEnabled(false);
		frames.add(this.enteredVin);
		frames.add(selectedVin);
		ServiceFactory.getDao(FrameDao.class).saveAll(frames);
		logUserAction(SAVED, frames);
		String message = "VIN " + this.enteredVin.getId() + " moved to lot " + this.enteredVin.getProductionLot() + ".    \nVIN " + selectedVin.getId() + " moved to lot " + selectedVin.getProductionLot() + ".";
		this.resetPanel();
		this.setMessage(message);
	}
	
	public void resetPanel(){
		((LabeledTextField) this.productionLotPanel.getComponent(0)).getComponent().setText("");
		((LabeledTextField) this.productionLotPanel.getComponent(1)).getComponent().setText("");
		((LabeledTextField) this.productionLotPanel.getComponent(2)).getComponent().setText("");
		
		this.matchingVinsTable.removeData();
		this.matchingLotsTable.removeData();
		this.colorCodesTable.removeData();
		
		((LabeledTextField) this.frameTwoPanel.getComponent(1)).getComponent().setText("");
		((LabeledTextField) this.frameTwoPanel.getComponent(2)).getComponent().setText("");
		((LabeledTextField) this.frameTwoPanel.getComponent(3)).getComponent().setText("");
		((LabeledTextField) this.frameTwoPanel.getComponent(4)).getComponent().setText("");
		
		((LabeledTextField) this.frameOnePanel.getComponent(1)).getComponent().setText("");
		((LabeledTextField) this.frameOnePanel.getComponent(2)).getComponent().setText("");
		((LabeledTextField) this.frameOnePanel.getComponent(3)).getComponent().setText("");
		((LabeledTextField) this.frameOnePanel.getComponent(4)).getComponent().setText("");
		
		((LabeledTextField) this.lastProcessPointPanel.getComponent(0)).getComponent().setText("");
		((LabeledTextField) this.lastProcessPointPanel.getComponent(1)).getComponent().setText("");
		((LabeledTextField) this.lastProcessPointPanel.getComponent(2)).getComponent().setText("");
		((LabeledTextField) this.lastProcessPointPanel.getComponent(3)).getComponent().setText("");
		((LabeledTextField) this.lastProcessPointPanel.getComponent(4)).getComponent().setText("");	
	}
	
	public Boolean validate(Frame product){
		PreProductionLot prodLot;
		if (product.getLastPassingProcessPointId() == null){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Last passing process point value is NULL.");
			return false;
		}
		
		if ((ServiceFactory.getDao(ProcessPointDao.class).findById(product.getLastPassingProcessPointId())) == null){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Last passing process point " + product.getLastPassingProcessPointId() + " does not exist.");
			return false;
		}
		
		if (product == this.enteredVin && 
			ServiceFactory.getDao(ProductResultDao.class).findAllByProductAndProcessPoint(product.getProductId(), PropertyService.getPropertyBean(FrameLinePropertyBean.class).getAfOnProcessPointId()).size() > 0){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Vehicle "+ product.getProductId() +" is past AF_ON.");
			return false;
		}
		if (product == this.matchingVinsTable.getSelectedItem() && 
			ServiceFactory.getDao(ProductResultDao.class).findAllByProductAndProcessPoint(product.getProductId(), PropertyService.getPropertyBean(FrameLinePropertyBean.class).getPaintOffProcessPointId()).size() > 0){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Vehicle "+ product.getProductId() +" is already painted.");
			return false;
		}
		if (product.getProductionLot() == null){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Production lot value is NULL.");
			return false;
		}
		if ((prodLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(product.getProductionLot())) == null){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Production lot " + product.getProductionLot() + " does not exist.");
			return false;
		}
		if (product.getProductSpecCode() == null){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Product spec code value is NULL.");
			return false;
		}
		if ((ServiceFactory.getDao(FrameSpecDao.class).findByKey(product.getProductSpecCode())) == null){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Product spec code " + product.getProductSpecCode() + " does not exist.");
			return false;
		}
		if (!prodLot.getProductSpecCode().equalsIgnoreCase(product.getProductSpecCode())){
			this.getMainWindow().setErrorMessage("Cannot process VIN. Frame product spec code does not match that of production lot.");
			return false;
		}
		return true;
	}
}

