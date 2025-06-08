package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.teamleader.model.ProductUpdateStatus;
import com.honda.galc.client.teamleader.model.ProductUpdateStatusTableModel;
import com.honda.galc.client.teamleader.model.StopShipProductTableModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.FilteredLabeledComboBox;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.SortedArrayList;


public class StopShipMassUpdatePanel extends TabbedPanel
implements ListSelectionListener, ActionListener, TableModelListener {

	public static final String STOP_SHIP_MASS_UPDATE = "Stop Ship Mass Update";
	private ProductType productType;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension screenDimension;
	protected JButton createInstalledPartsAndMeasuremntsButton;
	private PartNameSelectionPanel assignedPartNameSelectionPanel;
	private JButton removePartButton;
	private JButton addPartButton;
	private JButton startProductButton;
	private JButton endProductButton;
	private PartNameSelectionPanel partNameSelectionPanel;
	private TablePane productUpdatePanel;
	private JTextField startProductIdText, endProductIdText;
	private JLabel updateCountLabel;
	private List<PartName> partNames;
	private StopShipProductTableModel stopShipProductTableModel;
	private ProductUpdateStatusTableModel model;
	private TablePane productStatusUpdatePanel;
	private JButton cancelButton;
	private FilteredLabeledComboBox processPointComboBox;
	private boolean isContinue;
	private CommonTlPropertyBean commonTlPropertyBean;
	private ProductResultDao productResultDao;
	private PartNameDao partNameDao;
	private InstalledPartDao installedPartDao;
	private LotControlRuleDao lotControlRuleDao;
	private MeasurementDao measurementDao;
	private ProcessPointDao processPointDao;

	public StopShipMassUpdatePanel() {
		super(STOP_SHIP_MASS_UPDATE, KeyEvent.VK_L);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}


	public StopShipMassUpdatePanel(TabbedMainWindow mainWindow) {
		super(STOP_SHIP_MASS_UPDATE, KeyEvent.VK_L, mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		loadProperty(PropertyService.getPropertyBean(CommonTlPropertyBean.class, getApplicationId()));
		AnnotationProcessor.process(this);
	}
	
	protected void initComponents() {
	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		
		Box box = Box.createHorizontalBox();
		box.setBorder(border);
		
		Box box1 = Box.createHorizontalBox();
		box1.setBorder(border);
		box1.add(createPartSelectionPanel());
		box1.add(addRemoveButtonPanel());
		box1.add(createSelectedPartListPanel());
		box.add(box1);
		
		Box box2 = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(createProductSubSetSelectionPanel());
		box.add(box2);

		add(box);
		
		Box box3 = Box.createVerticalBox();
		box3.setBorder(border);
		box3.add(createUpdateProductCountPanel());
		box3.add(getProductUpdatePanel());
		add(box3);

		Box box4 = Box.createHorizontalBox();
		box4.setBorder(border);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		buttonPanel.add(getCreateInstalledPartsAndMeasuremntsButton());
		box4.add(buttonPanel);

		add(box4);

	}


	private Box addRemoveButtonPanel() {
		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		Box box21 = Box.createVerticalBox();
		box21.setBorder(border);
		JPanel buttonPanel2 = new JPanel(new GridLayout(6,1, 10, 40));
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		buttonPanel2.add(getAddPartButton());
		buttonPanel2.add(getRemovePartButton());
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		box21.add(buttonPanel2);
		
		return box21;
	}

	protected JButton getAddPartButton() {
		if(addPartButton == null){
			addPartButton = new JButton(" >> ");
			addPartButton.setToolTipText("add part");
			addPartButton.setName("JaddPartButton");
			addPartButton.setSize(80, 50);
			addPartButton.setEnabled(false);
		}
		return addPartButton;
	}
	
	protected JButton getRemovePartButton() {
		if(removePartButton == null){
			removePartButton = new JButton(" << ");
			removePartButton.setToolTipText("remove part");
			removePartButton.setName("JremovePartButton");
			removePartButton.setSize(80, 50);
			removePartButton.setEnabled(false);
		}
		return removePartButton;
	}

	private JPanel createSelectedPartListPanel() {
		JPanel partPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		partPanel.add(getAssignedPartNameSelectionPanel());
		return partPanel;
	}

	private JPanel createProductSubSetSelectionPanel() {
		
		JPanel productPanel = new JPanel();
		productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
	
		JPanel onProcessPointPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		onProcessPointPanel.add(getProcessPointComboBox());
		
		
		JPanel startProductPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		startProductPanel.add(getStartProductButton());
		startProductPanel.add(getStartProductIdText());
		
		
		JPanel endProductPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		endProductPanel.add(getEndProductButton());
		endProductPanel.add(getEndProductIdText());
	
		productPanel.add(onProcessPointPanel);
		productPanel.add(new Label());
		productPanel.add(startProductPanel);
		productPanel.add(endProductPanel);
		productPanel.add(new Label());
		
		return productPanel;
	}
	
	protected JTextField getStartProductIdText(){
		if(startProductIdText == null){
			startProductIdText  = UiFactory.createTextField(17, UiFactory.getInfo().getInputFont(), TextFieldState.EDIT,JTextField.CENTER);
			startProductIdText.setSize(450, 50);
			startProductIdText.setName("JstartProductIdText");
			startProductIdText.setPreferredSize(new Dimension(450,50));
		}
		
		return startProductIdText;
	}
	
	protected JTextField getEndProductIdText(){
		if(endProductIdText == null){
			endProductIdText  = UiFactory.createTextField(17, UiFactory.getInfo().getInputFont(), TextFieldState.EDIT,JTextField.CENTER);
			endProductIdText.setName("JendProductIdText");
			endProductIdText.setSize(450, 50);
			endProductIdText.setPreferredSize(new Dimension(450,50));
		}
		
		return endProductIdText;
	}
	
	private JPanel createUpdateProductCountPanel(){
		JPanel updateProductCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		
		JLabel updateListCountLabel = UiFactory.createLabel("Update List", UiFactory.getInfo().getLabelFont());
		updateProductCountPanel.add(updateListCountLabel);
		updateProductCountPanel.add(getUpdateCountLabel());
		
		return updateProductCountPanel;
	}

	protected JLabel getUpdateCountLabel(){
		if(updateCountLabel == null){
			updateCountLabel =  UiFactory.createLabel("Count (  )", UiFactory.getInfo().getLabelFont());
		}
		
		return updateCountLabel;
	}
	private JButton getStartProductButton() {
		if(startProductButton == null){
			startProductButton = new JButton(" Start Product Id ");
			startProductButton.setToolTipText("Start Product Id");
			startProductButton.setName("JstartProductButton");
			startProductButton.setSize(180, 60);
			startProductButton.setEnabled(true);
		}
		return startProductButton;
	}
	
	private JButton getEndProductButton() {
		if(endProductButton == null){
			endProductButton = new JButton(" End Product Id ");
			endProductButton.setToolTipText("End Product Id");
			endProductButton.setName("JendProductButton");
			endProductButton.setSize(180, 60);
			endProductButton.setEnabled(true);
		}
		return endProductButton;
	}
	
	protected JButton getCreateInstalledPartsAndMeasuremntsButton() {
		if(createInstalledPartsAndMeasuremntsButton == null){
			createInstalledPartsAndMeasuremntsButton = new JButton("Create Installed Part And Measurement");
			createInstalledPartsAndMeasuremntsButton.setToolTipText("createInstalledPartsAndMeasuremntsButton");
			createInstalledPartsAndMeasuremntsButton.setName("JcreateInstalledPartsAndMeasuremntsButton");
			createInstalledPartsAndMeasuremntsButton.setSize(80, 50);
			createInstalledPartsAndMeasuremntsButton.setEnabled(false);
		}
		return createInstalledPartsAndMeasuremntsButton;
	}

	private TablePane getProductUpdatePanel() {
		if(productUpdatePanel == null){
			productUpdatePanel = new TablePane("", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return productUpdatePanel;
	}

	private JPanel createPartSelectionPanel() {
		JPanel partPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		partPanel.add(getPartNameSelectionPanel());
		return partPanel;
	}
	
	protected PartNameSelectionPanel getPartNameSelectionPanel(){
		if(partNameSelectionPanel == null){
			partNameSelectionPanel = new PartNameSelectionPanel(1000 / 4, 150,
					new Dimension(screenDimension.width / 4, screenDimension.height / 3));
		}
		return partNameSelectionPanel;
	}
	
	protected PartNameSelectionPanel getAssignedPartNameSelectionPanel(){
		if(assignedPartNameSelectionPanel == null){
			assignedPartNameSelectionPanel = new PartNameSelectionPanel(1000 / 4, 150,
					new Dimension(screenDimension.width / 4, screenDimension.height / 3));
		}
		return assignedPartNameSelectionPanel;
	}
	
	
	public FilteredLabeledComboBox getProcessPointComboBox() {
        if (processPointComboBox == null) {
            processPointComboBox = new FilteredLabeledComboBox("Process Point");
            processPointComboBox.getComponent().setName("JProcessPointCombobox");
            processPointComboBox.getComponent().setPreferredSize(new Dimension(350,20));
            List<ProcessPoint> sortedList = getOnProcessPoints();
            ComboBoxModel<ProcessPoint> model = new ComboBoxModel<ProcessPoint>(sortedList,"getDisplayName");
            processPointComboBox.setModel(model);
            processPointComboBox.setSelectedIndex(0);
            
        }
        return processPointComboBox;
    }

	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPartButton))
			assignPart();
		else if (e.getSource().equals(removePartButton))
			deassignPart();
		else if (e.getSource().equals(startProductButton))
			selectStartProduct();
		else if (e.getSource().equals(endProductButton))
			selectEndProduct();
		else if (e.getSource().equals(startProductIdText))
			refreshProductUpdateList();
		else if (e.getSource().equals(endProductIdText))
			refreshProductUpdateList();
		else if(e.getSource().equals(createInstalledPartsAndMeasuremntsButton))
			createInstalledPartsAndMeasuremntsButtonClick();
		else if(e.getSource().equals(cancelButton))
			cancelProcess();
	}

	private void cancelProcess() {
		setContinue(false);
	}

	private void createInstalledPartsAndMeasuremntsButtonClick() {
		setContinue(true);
		
		setMessage(" Please wait while installed Part records are created .....");
		final JDialog dialog = getWaitDialog( "Update progress");
	
		Thread t = new Thread() {
			public void run() {
				
				try {
					createInstalledPartsAndMeasuremnts();
					dialog.dispose();
					clearErrorMessage();
					setMessage("parts and measurements Successfully Created");

				} catch (Exception e) {
					dialog.dispose();
					setErrorMessage(" unexpected Error occurred ");
					handleException(e);
				}
			}
		};
		t.start();
		dialog.setVisible(true);
	}
	
	protected void createInstalledPartsAndMeasuremnts(){
		List<BaseProduct> products = getStopShipProductTableModel().getItems();
		List<PartName> partNames =  getAssignedPartNameSelectionPanel().getPartNameTableModel().getItems();
		
		for(BaseProduct product:products){
			
			if(!isContinue()){
				Logger.getLogger().info("received cancel request, unable to create installed Part records for product - "+ product.getId());
				break;
			}
			for(PartName partName:partNames){
				List<LotControlRule> rules = getLotControlRuleDao().findAllByPartName(partName.getPartName().trim());
				LotControlRule matchedRule = ProductSpecUtil.getMatchedItem(product.getProductSpecCode(), rules, LotControlRule.class, ProductTypeUtil.isMbpnProduct(getProductType().name()));
				if(isCreatePartAndMeasurements(partName, product.getProductId())){
						if(matchedRule != null){
							createPartRecords(partName, matchedRule,product);
						}else{
							Logger.getLogger().error("No matching Lot ControlRule found for partName - "+partName.getPartName() + " for specCode - "+ product.getProductSpecCode());
						}

				}else{
					Logger.getLogger().error("Installed Parts exist for partName - "+partName.getPartName() + " for product - "+ product.getId());
				}
			}
		}
	}
	
	private void createPartRecords(PartName partName, LotControlRule matchedRule,BaseProduct product){
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		InstalledPartId installedPartId = new InstalledPartId();
		installedPartId.setPartName(partName.getPartName());
		installedPartId.setProductId(product.getProductId());
		
		InstalledPart installedPart = new InstalledPart();
		installedPart.setId(installedPartId);
		installedPart.setPartId(matchedRule.getPartByProductSpecs().get(0).getId().getPartId());
		if(matchedRule.isScan())installedPart.setPartSerialNumber("MASSUPDATE");
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(now);
		installedPart.setCreateTimestamp(now);
		installedPart.setInstalledPartReason("Repaired");
		
		getInstalledPartDao().save(installedPart);
		
		
		if(matchedRule.getParts().size() > 0 && matchedRule.getParts().get(0).getMeasurementCount() > 0){
			List<MeasurementSpec> meaSpecs = matchedRule.getParts().get(0).getMeasurementSpecs();
			for(MeasurementSpec spec:meaSpecs){
				MeasurementId measurementId = new MeasurementId();
				measurementId.setProductId(product.getProductId());
				measurementId.setPartName(partName.getPartName());
				measurementId.setMeasurementSequenceNumber(spec.getId().getMeasurementSeqNum());
				
				Measurement measurement = new Measurement();
				measurement.setId(measurementId);
				measurement.setPartSerialNumber("MASSUPDATE");
				measurement.setMeasurementValue(spec.getMinimumLimit());
				measurement.setMeasurementStatus(MeasurementStatus.OK);
				measurement.setActualTimestamp(now);
				measurement.setCreateTimestamp(now);
				
				getMeasurementDao().save(measurement);
			}
		}
		Logger.getLogger().info("installed parts created for partName - "+partName.getPartName() + " for product - "+ product.getId());
		refreshProductStatus(product.getProductId());
	}

	private boolean isCreatePartAndMeasurements(PartName partName, String productId) {
		 if(getInstalledPartDao().isPartInstalled(productId, partName.getPartName())){
			 return false;
		 }
		
		return true;
	}

	protected void refreshProductUpdateList() {
		String startProductId = this.getStartProductIdText().getText();
		String endProductId = this.getEndProductIdText().getText();
		String startTimeStamp = (new Timestamp(System.currentTimeMillis())).toString();
		String endTimeStamp = (new Timestamp(System.currentTimeMillis())).toString();
		String onPP = ((ProcessPoint)getProcessPointComboBox().getComponent().getSelectedItem()).getProcessPointId();
		
		if(StringUtils.isNotEmpty(startProductId) && StringUtils.isNotEmpty(onPP)){
			List<? extends BaseProduct> products = new ArrayList<BaseProduct>();
			if(StringUtils.isNotEmpty(startProductId)){
				ProductResult prodResult = new ProductResult();
				prodResult.setProductId(startProductId);
				prodResult.setProcessPointId(onPP);
				
				List<ProductResult> productResults = getProductResultDao().findByProductAndProcessPoint(prodResult);
				if(productResults!= null && productResults.size() > 0){
					startTimeStamp = productResults.get(0).getActualTimestamp().toString();
				
				}
			}
			
			if(StringUtils.isNotEmpty(endProductId)){
				ProductResult prodResult = new ProductResult();
				prodResult.setProductId(endProductId);
				prodResult.setProcessPointId(onPP);
				
				List<ProductResult> productResults = getProductResultDao().findByProductAndProcessPoint(prodResult);
				if(productResults != null && productResults.size() > 0){
					endTimeStamp = productResults.get(0).getActualTimestamp().toString();
				
				}
				products = ProductTypeUtil.getTypeUtil(getProductType()).getProductDao().findAllProcessedProductsForProcessPointForTimeRange(onPP, startTimeStamp, endTimeStamp,getShippedTrackingStatuses());
			}else{
				products = ProductTypeUtil.getTypeUtil(getProductType()).getProductDao().findAllProcessedProductsForProcessPointBeforeTime(onPP, startTimeStamp, getShippedTrackingStatuses());
			}
			
		
			stopShipProductTableModel = new StopShipProductTableModel(getProductUpdatePanel().getTable(), products, getProductType());
			
			String count = products!= null && products.size() > 0? String.valueOf(products.size()):"0";
			getUpdateCountLabel().setText(count);
			
			if(products!= null && products.size()> 0 && getAssignedPartNameSelectionPanel().getPartNameTableModel().getItems().size() > 0 ){
				getCreateInstalledPartsAndMeasuremntsButton().setEnabled(true);
			}else{
				getCreateInstalledPartsAndMeasuremntsButton().setEnabled(false);
			}
		}else{
			stopShipProductTableModel = new StopShipProductTableModel(getProductUpdatePanel().getTable(), new ArrayList<BaseProduct>(), getProductType());
			getUpdateCountLabel().setText("0");
			getCreateInstalledPartsAndMeasuremntsButton().setEnabled(false);
		}
		

	}

	private List<ProcessPoint> getOnProcessPoints() {
		String onPP =  commonTlPropertyBean.getOnPp();
		String[] onPPs = onPP.split(",");
		List<ProcessPoint> processPointList = new ArrayList<ProcessPoint>();
		for(String ppid:onPPs){
			processPointList.add(getProcessPointDao().findById(ppid));
		}
		return processPointList;
	}
	
	private List<String> getShippedTrackingStatuses() {
		String trackingStatus =  commonTlPropertyBean.getShippedTrackingStatuses();
		if(StringUtils.isNotBlank(trackingStatus)){
			String[] statuses = trackingStatus.split(",");
			
			return Arrays.asList(statuses);
		}
		
		return new ArrayList<String>();
	}
	
	private ProductResultDao getProductResultDao(){
		if(this.productResultDao == null){
			this.productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		}
		return this.productResultDao;
	}
	
	
	private PartNameDao getPartNameDao(){
		if(this.partNameDao ==  null){
			this.partNameDao = ServiceFactory.getDao(PartNameDao.class);
		}
		return this.partNameDao;
	}
	
	private LotControlRuleDao getLotControlRuleDao(){
		if(this.lotControlRuleDao == null){
			this.lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		}
		return this.lotControlRuleDao;
	}
	
	private InstalledPartDao getInstalledPartDao(){
		if(this.installedPartDao == null){
			this.installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		}
		return this.installedPartDao;
	}
	
	private ProcessPointDao getProcessPointDao(){
		if(this.processPointDao ==  null){
			this.processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return this.processPointDao;
	}

	private MeasurementDao getMeasurementDao() {
		if(this.measurementDao == null){
			this.measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		}
		return this.measurementDao;
	}
	
	protected void setPartNameDao(PartNameDao partNameDao) {
		this.partNameDao = partNameDao;
	}


	protected void setInstalledPartDao(InstalledPartDao installedPartDao) {
		this.installedPartDao = installedPartDao;
	}


	protected void setLotControlRuleDao(LotControlRuleDao lotControlRuleDao) {
		this.lotControlRuleDao = lotControlRuleDao;
	}


	protected void setMeasurementDao(MeasurementDao measurementDao) {
		this.measurementDao = measurementDao;
	}


	protected void setProductResultDao(ProductResultDao productResultDao) {
		this.productResultDao = productResultDao;
	}

	protected void setProcessPointDao(ProcessPointDao processPointDao) {
		this.processPointDao = processPointDao;
	}


	private void selectEndProduct() {
		ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(getMainWindow(), getProductType().name(),
				"Manual Product Entry Dialog");
		manualProductEntryDialog.setModal(true);
		manualProductEntryDialog.setVisible(true);
		String productId = manualProductEntryDialog.getResultProductId();
		this.getEndProductIdText().setText(productId);
		
	}

	private void selectStartProduct() {
		ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(getMainWindow(), getProductType().name(),
				"Manual Product Entry Dialog");
		manualProductEntryDialog.setModal(true);
		manualProductEntryDialog.setVisible(true);
		String productId = manualProductEntryDialog.getResultProductId();
		this.getStartProductIdText().setText(productId);
	}

	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == (getPartNameSelectionPanel().getPartSelectionPanel().getTable().getSelectionModel())) {
			enableButtons();
		}else if(e.getSource() == (getAssignedPartNameSelectionPanel().getPartSelectionPanel().getTable().getSelectionModel())) {
			enableButtons();
		}
		
	}

	@Override
	public void onTabSelected() {
		if (isInitialized) {
			return;
		}
		initComponents();
		addListeners();
		initPartSelectionPanel();
		isInitialized = true;
		
	}

	protected void addListeners() {
		getPartNameSelectionPanel().getPartSelectionPanel().addListSelectionListener(this);
		getAssignedPartNameSelectionPanel().getPartSelectionPanel().addListSelectionListener(this);
		getProductUpdatePanel().addListSelectionListener(this);
		getAddPartButton().addActionListener(this);
		getRemovePartButton().addActionListener(this);
		getStartProductButton().addActionListener(this);
		getEndProductButton().addActionListener(this);
		getCreateInstalledPartsAndMeasuremntsButton().addActionListener(this);
		getStartProductIdText().addActionListener(this);
		getEndProductIdText().addActionListener(this);
	}

	protected void initPartSelectionPanel() {
			updatePartSelectionModel();
	}

	private void updatePartSelectionModel() {
		if (getPartNameSelectionPanel() == null)
			return;
	
		partNames = loadPartNames();
		getPartNameSelectionPanel().update(partNames);
		getAssignedPartNameSelectionPanel().update(new ArrayList<PartName>());
	}
	
	private List<PartName> loadPartNames() {
		return getPartNameDao().findAllByProductType(getProductType().name());
	}

	private void deassignPart() {
		PartName partName = getAssignedPartNameSelectionPanel().getPartNameTableModel().getSelectedItem();
		getPartNameSelectionPanel().getPartNameTableModel().add(partName);
		getAssignedPartNameSelectionPanel().getPartNameTableModel().remove(partName);
	}
	
	private void assignPart() {
		PartName partName = getPartNameSelectionPanel().getPartNameTableModel().getSelectedItem();
		getAssignedPartNameSelectionPanel().getPartNameTableModel().add(partName);
		getPartNameSelectionPanel().getPartNameTableModel().remove(partName);
	}

	private void enableButtons() {
			if(getPartNameSelectionPanel().getPartNameTableModel().getSelectedItem() != null) {
				getAddPartButton().setEnabled(true);
			}else{
				getAddPartButton().setEnabled(false);
			}
			if(getAssignedPartNameSelectionPanel().getPartNameTableModel() != null && getAssignedPartNameSelectionPanel().getPartNameTableModel().getSelectedItem() != null){
				getRemovePartButton().setEnabled(true);
			}else{
				getRemovePartButton().setEnabled(false);
			}
	}
	
	private JDialog getWaitDialog(String title) {
		final JDialog dialog = new JDialog(this.getMainWindow(), title);
		dialog.setSize(400, 600);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.add(getProductStatusUpdatePanel());
		panel.add(getCancelButton());
		refreshProductStatus(null);
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		return dialog;
	}
	
	private List<ProductUpdateStatus> getProductList(List<BaseProduct> products){
		List<ProductUpdateStatus> productList = new ArrayList<ProductUpdateStatus>();
		for(BaseProduct product:products){
			ProductUpdateStatus productUpdateStatus = new ProductUpdateStatus(product.getProductId(), false, product.getProductSpecCode());
			productList.add(productUpdateStatus);
		}
		
		return productList;
	}
	
	protected void refreshProductStatus(String productId){
		if(StringUtils.isNotEmpty(productId)){
			List<ProductUpdateStatus> productList = getProductUpdateStatusTableModel().getItems();
			List<ProductUpdateStatus> updatedProductList = new ArrayList<ProductUpdateStatus>();
			for(ProductUpdateStatus product: productList){
				if(product.getProduct().equalsIgnoreCase(productId)){
					product.setCreated(true);
				}
				updatedProductList.add(product);
			}
			
			getProductUpdateStatusTableModel().refresh(updatedProductList);
		}else{
			List<BaseProduct> products = getStopShipProductTableModel().getItems();
			List<ProductUpdateStatus> productList = getProductList(products);
			getProductUpdateStatusTableModel().refresh(productList);
		}
	}
	
	protected void setContinue(boolean isContinue){
		this.isContinue=isContinue;
	}
	
	private boolean  isContinue(){
		return this.isContinue;
	}
	
	protected void  loadProperty(CommonTlPropertyBean bean) {
		this.commonTlPropertyBean =  bean; 
	}
	
	protected StopShipProductTableModel getStopShipProductTableModel() {
		return this.stopShipProductTableModel;
	}
	
	private ProductType getProductType(){
		if(this.productType == null){
			this.productType = ProductTypeCatalog.getProductType(getApplicationProductTypeName());
		}
		
		return this.productType;
	}
	
	protected ProductUpdateStatusTableModel getProductUpdateStatusTableModel(){
		if(model == null){
			model = new ProductUpdateStatusTableModel( getProductStatusUpdatePanel().getTable(),new ArrayList<ProductUpdateStatus>(),commonTlPropertyBean);
		}
		return model;
	}
	
	protected JButton getCancelButton(){
		if(cancelButton == null){
			cancelButton = new JButton(" Cancel ");
			cancelButton.setToolTipText("cancel");
			cancelButton.setSize(80, 50);
			cancelButton.setName("JcancelButton");
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}
	
	private TablePane getProductStatusUpdatePanel(){
		if(productStatusUpdatePanel == null){
			productStatusUpdatePanel = new TablePane("", ListSelectionModel.SINGLE_SELECTION);
			productStatusUpdatePanel.getTable().setRowHeight(40);
		}
		return productStatusUpdatePanel;
	}
}
