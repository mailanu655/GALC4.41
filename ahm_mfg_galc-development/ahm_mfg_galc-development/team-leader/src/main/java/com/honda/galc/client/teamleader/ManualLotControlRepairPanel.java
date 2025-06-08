package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.model.PartResultTableModel;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductPanel;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.dto.PartHistoryDto;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>ManualLotControlRepairPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairPanel description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Apr 29, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 29, 2011
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 03, 2016
 */

public class ManualLotControlRepairPanel extends ApplicationMainPanel {

	private static final long serialVersionUID = 1L;
	private ManualLotControlRepairController controller;
	protected ManualLotControlRepairPropertyBean property;
	private ProductPanel productIdPanel;
	private TablePane partStatusPanel;
	private JPanel buttonPanel;
	private JButton enterResultButton;
	private JButton enterMultiResultButton;
	private JButton removeResultButton;
	private JButton refreshButton;
	private JButton resetButton;
	private JButton historyButton;
	private ProductTypeData productTypeData;
	protected ProductType currentProductType;
	private LabeledComboBox subProductPartNameComboBox;
	private JPanel recursionProductIdPanel;
	private JLabel productIdLabel;
	private List<String> panelIds;
	protected JPanel filterPanel;
	private ObjectTablePane<PartHistoryDto> partHistoryPane;
	private List<PartHistoryDto> partHistoryData;
	private JLabel selectedDivisionLabel;
	private JLabel selectedLineLabel;
	private JLabel selectedZoneLabel;
	private JLabel selectedStatusLabel;
	private JLabel divisionLabel;
	private JLabel areaLabel;
	private JLabel zoneLabel;
	private JLabel statusLabel;
	private JButton filterButton;
	private JButton setResultButton;
	protected boolean subPanel = false;
	protected ManualLotControlRepairPanel parentView;

	public ManualLotControlRepairPanel(MainWindow mainWin) {
		super(mainWin);
		initialize();
		AnnotationProcessor.process(this);
	}

	public ManualLotControlRepairPanel(MainWindow mainWin,
			ProductType productType) {
		super(mainWin);
		currentProductType = productType;
		initializeRecursionPanel();
		AnnotationProcessor.process(this);
	}
	
	public ManualLotControlRepairPanel(MainWindow mainWin,
			ProductType productType, final ManualLotControlRepairPanel parentView) {
		super(mainWin);
		this.parentView = parentView;
		currentProductType = productType;
		subPanel = true;		
		initializeRecursionPanel();
		AnnotationProcessor.process(this);
	}

	private void initializeRecursionPanel() {
		property = PropertyService.getPropertyBean(
				ManualLotControlRepairPropertyBean.class, window
						.getApplication().getApplicationId());
		panelIds = getSubProductTypes();
		controller = createController();
		initComponentsRecursion();
		
	}

	private void initialize() {
		
		try {
			property = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class, 
					window.getApplication().getApplicationId());
			
		
			
			controller = createController();
			
			initComponents();
			
			window.addWindowListener(new WindowAdapter() {			
				public void windowOpened(WindowEvent e){				
					getProductIdField().requestFocus();						
				}
			});
			
			
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start ManualLotControlRepairView");
		}
	}


	@SuppressWarnings("unchecked")
	protected ManualLotControlRepairController createController() {
		if(ProductTypeUtil.isMbpnProduct(getProductType())){
			controller = new ManualLotControlRepairMbpnController(window, this,
					new ManualLtCtrResultEnterViewManager(window, property));
		}
		else{
			switch (getProductType()) {
			case ENGINE :
				controller = new ManualLotControlRepairEngineController(window,this, 
						new EngineManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case FRAME :
				controller = new ManualLotControlRepairFrameController(window,this, 
						new FrameManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case KNUCKLE :
				controller = new ManualLotControlRepairKnuckleController(window,this, 
						new KnuckleManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case HEAD :
				controller = new ManualLotControlRepairHeadController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case BLOCK :
				controller = new ManualLotControlRepairBlockController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case CONROD :
				controller = new ManualLotControlRepairConrodController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case CRANKSHAFT :
				controller = new ManualLotControlRepairCrankshaftController(window,this, 
						new DiecastManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case IPU :
				controller = new ManualLotControlRepairIpuController(window,this, 
						new IpuManualLtCtrResultEntereViewManager(window, property, currentProductType));
				break;
			case FIPUCASE :
				controller = new ManualLotControlRepairFrontIpuCaseController(window,this, 
						new FrontIpuCaseManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			case RIPUCASE :
				controller = new ManualLotControlRepairRearIpuCaseController(window,this, 
						new RearIpuCaseManualLtCtrResultEnterViewManager(window, property, currentProductType));
				break;
			default :
				controller = new ManualLotControlRepairBaseController(window,this, 
						new ManualLtCtrResultEnterViewManager(window, property));
			}
		}
		return controller;
	}

	public void initComponents() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(getProductIdPanel());
		add(getFilterPanel());
		int height = (window.getSize().height)-400  ;
		int width = (window.getSize().width)-50;
		getPartStatusPanel().setPreferredHeight(height);
		getPartStatusPanel().setPreferredWidth(width);
		add(getPartStatusPanel());
		add(getButtonPanel());
	}

	public void initComponentsRecursion() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (ProductTypeUtil.isInstanceOf(currentProductType, DieCast.class))
			add(getSubProductPartNameComboBox());
		
		add(getRecursionProductIdPanel());
		getPartStatusPanel().setPreferredHeight(350);
		add(getPartStatusPanel());
		add(getButtonPanel());

	}

	public JPanel getRecursionProductIdPanel() {
		if (recursionProductIdPanel == null) {
			recursionProductIdPanel = new JPanel();
			recursionProductIdPanel.setLayout(new BoxLayout(recursionProductIdPanel, BoxLayout.X_AXIS));
			recursionProductIdPanel.setBorder(new EmptyBorder(10, 0, 0, 0) );
			recursionProductIdPanel.add(getProductIdLabel());
			recursionProductIdPanel.add(Box.createRigidArea(new Dimension(20,0)));
			getProductIdField().setPreferredSize(new Dimension(300, 20));
			getProductIdField().setMaximumSize(new Dimension(350, 40));
			getProductIdField().setFont(new Font("dialog", 0, 18));
			recursionProductIdPanel.add(getProductIdField());
			recursionProductIdPanel.add(Box.createRigidArea(new Dimension(20,0)));
			getProductSpecLabel().setLabelFor(getProductSpecField());
			getProductSpecLabel().setPreferredSize(new Dimension(100, 20));
			recursionProductIdPanel.add(getProductSpecLabel());
			recursionProductIdPanel.add(Box.createRigidArea(new Dimension(10,0)));
			getProductSpecField().setPreferredSize(new Dimension(300, 20));
			getProductSpecField().setMaximumSize(new Dimension(400, 30));
			getProductSpecField().setFont(new Font("dialog", 0, 18));
			recursionProductIdPanel.add(getProductSpecField());
			recursionProductIdPanel.setPreferredSize(new Dimension(550, 35));
		}
		return recursionProductIdPanel;
	}

	public UpperCaseFieldBean getProductIdField() {
		return getProductIdPanel().getProductIdField();

	}

	public JLabel getProductIdLabel() {
		if (productIdLabel == null) {
			productIdLabel = new JLabel(currentProductType.name());
			productIdLabel.setPreferredSize(new Dimension(100, 20));
			productIdLabel.setFont(new Font("dialog", 0, 18));

		}
		return productIdLabel;
	}

	public UpperCaseFieldBean getProductSpecField() {
		return getProductIdPanel().getProductSpecField();
	}

	public JLabel getProductSpecLabel() {
		return getProductIdPanel().getProductSpecLabel();
	}
	
	public UpperCaseFieldBean getSeqField() {
		return getProductIdPanel().getSeqField();
	}

	public Component getButtonPanel() {
		if(buttonPanel == null){
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 5));
			buttonPanel.add(getEnterResultButton());
			buttonPanel.add(getMultiResultsButton());
			buttonPanel.add(getRemoveResultButton());
			buttonPanel.add(getSetResultNgButton());
			buttonPanel.add(getRefreshButton());
			buttonPanel.add(getHistoryButton());
			buttonPanel.add(getResetButton());
		}
		return buttonPanel;
	}

	public JButton getSetResultNgButton() {
		if(setResultButton == null){
			setResultButton = new JButton("Set Result NG");
			setResultButton.setName("Set Result NG");
			setResultButton.setEnabled(false);
		}
		return setResultButton;
	}

	public TablePane getPartStatusPanel() {
		if(partStatusPanel == null){
			partStatusPanel = new TablePane("Part Status Panel");
			new PartResultTableModel(null,property.getMlcrColumns(), partStatusPanel.getTable());
		}
		return partStatusPanel;
	}

	@SuppressWarnings("unchecked")
	protected void showPartHistoryDialog() {
		partHistoryData = controller.loadPartHistoryData();
		if (partHistoryData == null) MessageDialog.showError("No Part History");
		else {
			JDialog dialog = new JDialog(window, "Part History", true);
			JPanel panel = new JPanel();
			dialog.setSize(getSize());
			panel.setLayout(new BorderLayout());
			panel.add(getPartHistoryPane(), BorderLayout.CENTER);
			dialog.add(panel);
			dialog.setVisible(true);
		}
	}
	
	private ObjectTablePane<PartHistoryDto> getPartHistoryPane() {
		if (partHistoryPane == null) {
			ColumnMappings columnMappings = ColumnMappings.with("STATUS", "status").put("PRODUCT_ID", "productId")
					.put("PART_NAME", "partName").put("PART_SERIAL_NUMBER", "partSn").put("MEASUREMENTS", "measurements").put("PROCESS_POINT_ID", "processPointId")
					.put("TIMESTAMP", "timestamp").put("ASSOCIATE_NO", "associateNo");
					
			partHistoryPane = new ObjectTablePane<PartHistoryDto>(columnMappings.get(), false);
			partHistoryPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			partHistoryPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			partHistoryPane.getTable().setEnabled(false);
		}
		
		partHistoryPane.reloadData(partHistoryData);
		return partHistoryPane;
	}
	
	public ProductPanel getProductIdPanel() {
		if(productIdPanel == null){
			productIdPanel = new ProductPanel(window, getProductTypeData());
			
			if (isRemoveIEnabled() && currentProductType.equals(ProductType.FRAME)) {
				productIdPanel.getProductIdField().setMaximumLength(productIdPanel.getMaxProductIdLength()+1);
			}
			if(property.isProductIdCheckDisabled()) {
				productIdPanel.getProductIdField().setMaximumLength(property.getMaxProductSnLength());
			}
		}
		return productIdPanel;
		
	}

	public JTable getPartStatusTable(){
		return getPartStatusPanel().getTable();
	}
	
	
	public JButton getEnterResultButton() {
		if(enterResultButton == null){
			enterResultButton = new JButton("Enter Result");
			enterResultButton.setName("Enter Result");
		}
		return enterResultButton;
	}

	public JButton getMultiResultsButton() {
		if(enterMultiResultButton == null){
			enterMultiResultButton = new JButton("Enter Muitiple Result");
			enterMultiResultButton.setName("Enter Multiple Result");
		}
		return enterMultiResultButton;
	}

	public JButton getRemoveResultButton() {
		if(removeResultButton == null){
			removeResultButton = new JButton("Remove Result");
			removeResultButton.setName("Remove Result");
		}
		return removeResultButton;
	}

	public JButton getResetButton() {
		if(resetButton == null){
			resetButton = new JButton("Done");
			resetButton.setName("Done");
		}
		return resetButton;
	}
	
	public JButton getRefreshButton() {
		if(refreshButton == null){
			refreshButton = new JButton("Refresh");
			refreshButton.setName("Refresh");
		}
		return refreshButton;
	}
	
	public JButton getHistoryButton() {
		if(historyButton == null){
			historyButton = new JButton("Part History");
			historyButton.setName("Part History");
			historyButton.setEnabled(false);
		}
		return historyButton;
	}
	
	public void disableButtons() {
		getRemoveResultButton().setEnabled(false);
		getResetButton().setEnabled(false);
		getEnterResultButton().setEnabled(false);
		getRefreshButton().setEnabled(false);
		getSetResultNgButton().setEnabled(false);
		getHistoryButton().setEnabled(false);
	}

	//Getters & Setters
	@SuppressWarnings("unchecked")
	public ManualLotControlRepairController getController() {
		return controller;
	}

	@SuppressWarnings("unchecked")
	public void setController(ManualLotControlRepairController controller) {
		this.controller = controller;
	}

	public void loadProductId(String productId) {
		getProductIdField().setText(productId);
		getController().loadProductId(false);
	
	}
	
	public ProductTypeData getProductTypeData(){
		if(productTypeData == null){
			for(ProductTypeData type : window.getApplicationContext().getProductTypeDataList()){
				if(type.getProductTypeName().equals(property.getProductType())){
					productTypeData = type;
					break;
				}
			}
		}
		
		return productTypeData;
	}

	public ManualLotControlRepairPropertyBean getProperty() {
		return property;
	}
	
	

	public ProductType getCurrentProductType() {
		return currentProductType;
	}

	
	@EventSubscriber()
	public void onProductProcessEvent(ProductProcessEvent event) {
		if (event.getState().equals(ProductProcessEvent.State.LOAD)) {
			if (currentProductType.name().equals(event.getProductType().name())) {
				if (event.getProductId() != null) {
					publishReceivedEvent(event);
				} else {
					ComboBoxUtils.loadComboBox(getSubProductPartNameComboBox()
							.getComponent(), event.getProductBuildResult());
				}
			}
		} else if (event.isStateFromSource(ProductProcessEvent.State.COMPLETE, this))
			getController().reset();
		else if (event.isStateFromSource(ProductProcessEvent.State.ERROR, this)) {
			getProductIdField().setEditable(true);
			getProductIdField().setEnabled(true);
			getProductIdField().setSelectionStart(0);
			getProductIdField().setSelectionEnd(getProductIdField().getText().length());
		}
			
	}
	
	public LabeledComboBox getSubProductPartNameComboBox() {
		if (subProductPartNameComboBox == null) {
			subProductPartNameComboBox = new LabeledComboBox(
					"Select a Part Name");
			subProductPartNameComboBox.getComponent().setRenderer(
					new PropertyComboBoxRenderer<InstalledPart>(
							InstalledPart.class, "partName"));
			subProductPartNameComboBox.getComponent().setSelectedIndex(-1);
		}
		return subProductPartNameComboBox;
	}

	public List<String> getSubProductTypes() {
		String subProductTypes = PropertyService.getProperty(ApplicationContext
				.getInstance().getApplicationId(), "SUB_PRODUCT_TYPES", "");
		List<String> panelIds = new ArrayList<String>();
		for (String name : subProductTypes.split(Delimiter.COMMA)) {
			panelIds.add(name.trim());
		}
		return panelIds;
	}
	
	public ProductType getProductType() {
		if (currentProductType == null) {
			currentProductType = ProductTypeCatalog.getProductType(property.getProductType());
		}
		return currentProductType;
	}
	
	private void publishSubType(ProductType subType) {
		if (subType.equals(ProductType.ENGINE)) {
			EventBus.publish(new ProductProcessEvent(
					((Frame) getController().product).getEngineSerialNo(),
					subType, ProductProcessEvent.State.LOAD));
		} else {
			String subTypeName = fetchSubTypeNames(subType);
			if (StringUtils.isEmpty(subTypeName)) {
				InstalledPart installedPart = getSubProductPart(
						getController().product.getProductId(),
						subType.name().trim());
				if(installedPart != null)
				EventBus.publish(new ProductProcessEvent(installedPart.getPartSerialNumber(), subType,
						ProductProcessEvent.State.LOAD));
			} else {
				String[] subTypeNamesArray = subTypeName
						.split(Delimiter.COMMA);
				List<InstalledPart> productBuildResult = new ArrayList<InstalledPart>();
				for (String subTypeProductName : subTypeNamesArray) {
					InstalledPart installedPart = getSubProductPart(getController().product.getProductId(),subTypeProductName.trim());
					if(installedPart != null)
					productBuildResult.add(getSubProductPart(getController().product.getProductId(),subTypeProductName.trim()));
				}
				EventBus.publish(new ProductProcessEvent(subType,
						productBuildResult, ProductProcessEvent.State.LOAD));

			}
		}
	}
	
	private InstalledPart getSubProductPart(String productId,
			String subTypeProductName) {
		InstalledPart installedPart = (InstalledPart) ProductTypeUtil
				.getProductBuildResultDao(getProductType())
				.findById(
						getController().product
								.getProductId(),
								subTypeProductName.trim());
		return installedPart;
	}

	private List<ProductType> deduceSubProductTypes() {
		List<ProductType> list = new ArrayList<ProductType>();
		for(ProductTypeData data : window.getApplicationContext().getProductTypeDataList())
			if(data.getOwnerProductType() != null && data.getOwnerProductType().equals(currentProductType) && getSubProductTypes().contains(data.getProductType().name()))
				list.add(data.getProductType());
		
		return list;
	}
	
	private String fetchSubTypeNames(ProductType productType) {
		String partNamesList = PropertyService.getProperty(window
				.getApplication().getApplicationId(), "SUB_PRODUCT_PART_NAMES{"
				+ productType.name() + "}");

		return partNamesList;
	}
	
	private void publishReceivedEvent(ProductProcessEvent event) {
		getProductIdField().setText(event.getProductId());
		getProductIdField().postActionEvent();
		if(getSubProductPartNameComboBox().getComponent().getItemCount() == 0) {
			getSubProductPartNameComboBox().setEnabled(false);
		}
		
	}
	
	public void mclrSubProducts() {
		List<ProductType> subProductTypes = deduceSubProductTypes();
		for (ProductType subProductType : subProductTypes) {
			publishSubType(subProductType);
		}
	}
	
	@EventSubscriber()
	public void onShipScrapEvent(ShipScrapEvent event) {
			if (currentProductType.equals(event.getProductType())) {
				getController().ownerShipped = event.isShipped();
			}
	}
	
	public JPanel getFilterPanel() {
		if(filterPanel == null){
			filterPanel = new JPanel();
			filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 5));
			
			JPanel divisionPanel = new JPanel();
			divisionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
			divisionPanel.add(getDivisionLabel());
			divisionPanel.add(getSelectedDivisionLabel());
			
			JPanel areaPanel = new JPanel();
			areaPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
			areaPanel.add(getAreaLabel());
			areaPanel.add(getSelectedLineLabel());
			
			JPanel zonePanel = new JPanel();
			zonePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
			zonePanel.add(getZoneLabel());
			zonePanel.add(getSelectedZoneLabel());
			
			JPanel statusPanel = new JPanel();
			statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
			statusPanel.add(getStatusLabel());
			statusPanel.add(getSelectedStatusLabel());
			
			filterPanel.add(getFilterButton());
			filterPanel.add(divisionPanel);
			filterPanel.add(areaPanel);
			filterPanel.add(zonePanel);
			filterPanel.add(statusPanel);
			
			
		}
		return filterPanel;
	}
	
	public JButton getFilterButton() {
		if(filterButton == null){
			filterButton = new JButton("Filter");
			filterButton.setName("Filter");
		}
		return filterButton;
	}

	public JLabel getDivisionLabel() {
		if (divisionLabel == null) {
			divisionLabel = new JLabel("Department : ");
			divisionLabel.setPreferredSize(new Dimension(110, 20));
			divisionLabel.setFont(new Font("dialog", 0, 18));
		}
		return divisionLabel;
	}

	public JLabel getAreaLabel() {
		if (areaLabel == null) {
			areaLabel = new JLabel("Area : ");
			areaLabel.setPreferredSize(new Dimension(60, 20));
			areaLabel.setFont(new Font("dialog", 0, 18));
		}
		return areaLabel;
	}
	
	public JLabel getZoneLabel() {
		if (zoneLabel == null) {
			zoneLabel = new JLabel("Zone : ");
			zoneLabel.setPreferredSize(new Dimension(80, 20));
			zoneLabel.setFont(new Font("dialog", 0, 18));
		}
		return zoneLabel;
	}
	
	public JLabel getStatusLabel() {
		if (statusLabel == null) {
			statusLabel = new JLabel("Status : ");
			statusLabel.setPreferredSize(new Dimension(70, 20));
			statusLabel.setFont(new Font("dialog", 0, 18));
		}
		return statusLabel;
	}
	
	public JLabel getSelectedDivisionLabel() {
		if(selectedDivisionLabel == null){
			selectedDivisionLabel = new JLabel();
			selectedDivisionLabel.setName("division");
			selectedDivisionLabel.setPreferredSize(new Dimension(210, 20));
			selectedDivisionLabel.setFont(new Font("dialog", 0, 18));
		}
		
		return selectedDivisionLabel;
	}
	
	public JLabel getSelectedLineLabel() {
		if(selectedLineLabel == null ){
			selectedLineLabel = new JLabel();
			selectedLineLabel.setName("line");
			selectedLineLabel.setPreferredSize(new Dimension(240, 20));
			selectedLineLabel.setFont(new Font("dialog", 0, 18));
		}
		
		return selectedLineLabel ;
	}
	
	public JLabel getSelectedZoneLabel() {
		if(selectedZoneLabel == null ){
			selectedZoneLabel = new JLabel();
			selectedZoneLabel.setName("zone");
			selectedZoneLabel.setPreferredSize(new Dimension(130, 20));
			selectedZoneLabel.setFont(new Font("dialog", 0, 18));
		}
		
		return selectedZoneLabel ;
	}
	
	public JLabel getSelectedStatusLabel() {
		if(selectedStatusLabel == null ){
			selectedStatusLabel = new JLabel();
			selectedStatusLabel.setName("status");
			selectedStatusLabel.setPreferredSize(new Dimension(100, 20));
			selectedStatusLabel.setFont(new Font("dialog", 0, 18));
		}
		
		return selectedStatusLabel;
	}
	
	public boolean isRemoveIEnabled(){
		Boolean removeIEnabled  = PropertyService.getPropertyBoolean("DEFAULT_PRODUCT_CLIENT", "REMOVE_I_ENABLED", false);
		return removeIEnabled;
	}
	
}
