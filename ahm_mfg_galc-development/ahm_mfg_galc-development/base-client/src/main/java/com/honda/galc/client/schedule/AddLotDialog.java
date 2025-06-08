package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComponent;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.LabeledNumberSpinner;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.client.ui.component.MbpnColumns;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel.YmtocColumns;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.ProductionLotMbpnSequenceDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.ProductionLotMbpnSequence;
import com.honda.galc.entity.product.ProductionLotMbpnSequenceId;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.ProductStampingSeqGenService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.SortedArrayList;


/**
 * 
 * <h3>AddLotDialog Class description</h3>
 * <p> AddLotDialog description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 13, 2013
 *
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date March 8 , 2017
 * 
 */

public class AddLotDialog extends JDialog implements ActionListener,ListSelectionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static int STANDARD_KD_LOT_SIZE = 30; 
	private static String SERVICE_LOT_PREFIX = "INT 01";
	
	private JCheckBox currentKdLotCheckBox = new JCheckBox("Current Kd Lot");
	private LabeledTextField kdLotTextField = new LabeledTextField("KD LOT");
	private LabeledTextField prodLotTextField = new LabeledTextField("PROD LOT");
	private LabeledComponent<JDatePickerImpl> datePickerPanel = createDatePickerPanel();
	private LabeledTextField commentTextField =  new LabeledTextField("COMMENT");
	private LabeledTextField prodSpecTextField = new LabeledTextField("PROD SPEC CODE");
	private JCheckBox useAlternateColorCheckBox = new JCheckBox("Use Alternate Color");
	
	private LabeledNumberSpinner lotSizeSpinner = null;

	private String productName;
	private List<PreProductionLot> currentLots;
	private List<PreProductionLot> processedLots;
	private List<PreProductionLot> upcomingLots;
	private ScheduleClientController controller;
	private JButton removePartButton;
	private JButton addPartButton;
	List<String> attributeNames = new SortedArrayList<String>();
	List<String> selectedAttributeList = new SortedArrayList<String>();
	String attributeValue = new String();
	String selectedValue = new String();

	List<PreProductionLot> serviceLots = new ArrayList<PreProductionLot>();
	
	private JButton addLotButton= new JButton("ADD LOT");
	
	private JButton closeButton = new JButton("Close");
	
	private ProductSpecSelectionBase productSpecPanel;
	
	private ObjectTablePane<PreProductionLot> serviceLotPanel;
	private LabeledListBox attributePanel;
	private LabeledListBox selectedAttributePanel;

	private Component addMbpnPanel;
	private  String attrValueCode;
	private  String attrValueMbpn;
	
	private JDatePanelImpl datePanel;
	private JDatePickerImpl datePicker;
	
	public AddLotDialog(ScheduleClientController controller, HashMap<String, List<PreProductionLot>> lotsMap) {
		super();
		AnnotationProcessor.process(this);
	    
		setSize(560, 500);
		setTitle("Add Lot Dialog");
		setName("AddLotDialog");
		initDialog(controller, lotsMap);
		configComponents();
		initComponents(controller.getProductSpecData());
		pack();
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		mapActions();
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				loadData();
			}
		});
		Logger.getLogger().check("Add Lot Dialogbox opened");
		setVisible(true);
	}

	private void initDialog(ScheduleClientController controller, HashMap<String, List<PreProductionLot>> lotsMap) {
		this.controller = controller;
		this.productName = controller.getProductName();
		this.currentLots = lotsMap.get(ScheduleClientController.CURRENT);
		this.processedLots = lotsMap.get(ScheduleClientController.PROCESSED);
		this.upcomingLots = lotsMap.get(ScheduleClientController.UPCOMING);	
	}

	public void dispose() {
		setVisible(false);
	}
	
	private void initComponents(ProductSpecData productSpecData) {
		
		JPanel lotInputPanel = new JPanel();
		lotInputPanel.setLayout(new BoxLayout(lotInputPanel,BoxLayout.Y_AXIS));
		lotInputPanel.add(currentKdLotCheckBox);
		lotInputPanel.add(kdLotTextField);
		lotInputPanel.add(prodLotTextField);
		lotInputPanel.add(lotSizeSpinner);
		lotInputPanel.add(this.datePickerPanel);
		lotInputPanel.add(commentTextField);
		if(!isUpdateComment()) {
			commentTextField.setVisible(false);
		}
		
		if(!StringUtils.isBlank(getProperty().getAddLotDialogProductType())) {
			if(getProperty().getAddLotDialogProductType().equals(ProductType.MBPN.name()))
			{
				productSpecPanel = new MbpnSelectionPanel(ProductType.MBPN.name());
			}else
			{
				productSpecPanel = new ProductSpecSelectionPanel(ProductType.getType(getProperty().getAddLotDialogProductType()).getProductName(), productSpecData);
			}
			
		} else {
			productSpecPanel = new ProductSpecSelectionPanel(productName, productSpecData);
		}
		
		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		Box box1 = Box.createHorizontalBox();
		box1.setBorder(border);
		
		box1.add(productSpecPanel);
		box1.add(getAttrSelectionPanel());
		if(isAddMbpn()) {							
			getAttrSelectionPanel().setVisible(true);
		}
		
		ViewUtil.setPreferredWidth(lotInputPanel, 400);
		ViewUtil.setPreferredWidth(productSpecPanel, 500);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(lotInputPanel,BorderLayout.WEST);
		topPanel.add(box1,BorderLayout.CENTER);
		
		
		
		JPanel commandPanel = new JPanel(new GridBagLayout());
		commandPanel.add(addLotButton);
		
		GridBagConstraints gbConstCloseBtn = new GridBagConstraints();
		gbConstCloseBtn.insets = new Insets(0, 20, 0, 0);
		commandPanel.add(closeButton, gbConstCloseBtn);		
		
		JPanel upperPanel = new JPanel(new BorderLayout());
		upperPanel.setBorder(new TitledBorder("Lot Input Panel"));
		upperPanel.add(topPanel,BorderLayout.NORTH);
		upperPanel.add(commandPanel,BorderLayout.SOUTH);
		
		serviceLotPanel = createLotPanel();
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(upperPanel,BorderLayout.NORTH);
		mainPanel.add(serviceLotPanel,BorderLayout.CENTER);
		ViewUtil.setInsets(mainPanel, 10, 10, 10, 10);
		
		setContentPane(mainPanel);
		addListeners();
	}
	
	private Component getAttrSelectionPanel() {
		if(this.addMbpnPanel == null) 		
		{	
			JPanel attrPanel = new JPanel();
			attrPanel.setLayout(new BoxLayout(attrPanel,BoxLayout.Y_AXIS));
			
			Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
			
			Box box1 = Box.createHorizontalBox();
			box1.setBorder(border);
			box1.add(createPartSelectionPanel());
			box1.add(addRemoveButtonPanel());
			box1.add(createSelectedPartListPanel());
			
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
			useAlternateColorCheckBox.setFont(Fonts.DIALOG_PLAIN_22);
			panel.add(useAlternateColorCheckBox);
		
			attrPanel.add(box1);
			attrPanel.add(panel);
			attrPanel.setVisible(false);
			this.addMbpnPanel = attrPanel;
		}	
			return this.addMbpnPanel;
	}

	private void configComponents() {
		currentKdLotCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		lotSizeSpinner= new LabeledNumberSpinner("LOT SIZE",true,1,getProperty().getAddLotDialogMaxKdLotSize());
		lotSizeSpinner.getComponent().setValue(STANDARD_KD_LOT_SIZE);
		ViewUtil.setFont(Fonts.DIALOG_BOLD_20,kdLotTextField,prodLotTextField, prodSpecTextField,lotSizeSpinner,commentTextField, datePickerPanel);
		ViewUtil.setPreferredWidth(120, kdLotTextField.getLabel(),prodLotTextField.getLabel(), prodSpecTextField.getLabel(),lotSizeSpinner.getLabel(),commentTextField.getLabel(), datePickerPanel.getLabel());
		ViewUtil.setPreferredWidth(200, addLotButton);
		addLotButton.setFont(Fonts.DIALOG_BOLD_20);
		addLotButton.setName("addLotButton");
		ViewUtil.setPreferredWidth(200, closeButton);
		closeButton.setFont(Fonts.DIALOG_BOLD_20);
		closeButton.setName("closeButton");		
		kdLotTextField.getComponent().setEditable(false);
		prodLotTextField.getComponent().setEditable(false);
	}
	
	private void mapActions() {
		addLotButton.addActionListener(this);
		currentKdLotCheckBox.addActionListener(this);
		closeButton.addActionListener(this);
		removePartButton.addActionListener(this);
		addPartButton.addActionListener(this);
	}
	
	private void addListeners() {
		attributePanel.getComponent().addListSelectionListener(this);
		selectedAttributePanel.getComponent().addListSelectionListener(this);
		if(getProperty().getAddLotDialogProductType().equals(ProductType.MBPN.name())) {           
			for(MbpnColumns clm : MbpnColumns.values()){
				productSpecPanel.getPanel(clm.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		            public void valueChanged(ListSelectionEvent e) {
		                columnValueChanged(e);
		            }
		        });
			}
		}	
		else {
			productSpecPanel.getPanel(YmtocColumns.Year.name()).getComponent().addListSelectionListener(this);
			productSpecPanel.getPanel(YmtocColumns.Model.name()).getComponent().addListSelectionListener(this);
			productSpecPanel.getPanel(YmtocColumns.Model_Type.name()).getComponent().addListSelectionListener(this);
			productSpecPanel.getPanel(YmtocColumns.Ext_Color.name()).getComponent().addListSelectionListener(this);
			productSpecPanel.getPanel(YmtocColumns.Int_Color.name()).getComponent().addListSelectionListener(this);
		}	
	
	}
	
	protected void columnValueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;	
		sendProductSpecSelectingEvent();
	}
	
	public void sendProductSpecSelectingEvent() {
		getAttributes();
	}
	
	private ObjectTablePane<PreProductionLot> createLotPanel(){
		ColumnMappings columnMappings = 
			ColumnMappings.with("Production Lot", "productionLot")
						  .put("Kd Lot", "kdLot").put("Lot Size", "lotSize").put("Product Spec","productSpecCode");
		if(isUpdateComment()) {
			columnMappings.put("Comment","notes");
		}
		ObjectTablePane<PreProductionLot> panel = new ObjectTablePane<PreProductionLot>("Service Lot Panel",columnMappings.get());

		return panel;
	}
	
	private void loadData() {
		serviceLots = findAllServiceLots();
		PreProductionLot lot = findLatestServiceLot(serviceLots);
		
		boolean enabledCrrKdLotchkBox = enableCurrentKdLotCheckBox();
		currentKdLotCheckBox.setEnabled(enabledCrrKdLotchkBox);
		currentKdLotCheckBox.setSelected(enabledCrrKdLotchkBox);
		
		kdLotTextField.getComponent().setText(findNextKdLotNumber(lot, currentKdLotCheckBox.isEnabled() && currentKdLotCheckBox.isSelected()));
		prodLotTextField.getComponent().setText(findNextProdLotNumber(lot));
		
		serviceLotPanel.reloadData(serviceLots);
	}
	
	private  boolean enableCurrentKdLotCheckBox() {
		List<PreProductionLot> lastServiceLots = getLastServiceLots();
		return getTotalLotSize(lastServiceLots) > 0 && getTotalLotSize(lastServiceLots) < STANDARD_KD_LOT_SIZE;
	}
	
	private int getTotalLotSize(List<PreProductionLot> lots) {
		int size = 0;
		for(PreProductionLot prodLot :lots)	size += prodLot.getLotSize();
		
		return size;
	}
	
	/**
	 * if the last lot of the process location is the service lot, find the lots of the same kd lot
	 * @return
	 */
	private List<PreProductionLot> getLastServiceLots() {
		if(upcomingLots.isEmpty()) return serviceLots;
		PreProductionLot lastLot = upcomingLots.get(upcomingLots.size()-1);
		if(!isServiceLot(lastLot.getProductionLot())) return serviceLots;
		for(int i = upcomingLots.size() -1;i>=0;i--) {
			PreProductionLot preProdLot = upcomingLots.get(i);
			if(preProdLot.isSameKdLot(lastLot)&&!serviceLots.contains(preProdLot)) serviceLots.add(preProdLot);
			else break;
		}
		return serviceLots;
	}
	
	private String findNextProdLotNumber(PreProductionLot prodLot) {
		String date = (new SimpleDateFormat("yyyyMMdd")).format(new java.util.Date());
		boolean isSameDate = prodLot!= null && date.equalsIgnoreCase(prodLot.getProductionLot().substring(8,16));
		int number = 10;
		
		if(isSameDate){
			String sn = prodLot.getProductionLot().substring(16,20);
			number = Integer.parseInt(sn) + 10;
		}

		return getServiceLotPrefix()+ getProcessLocation() + date+StringUtils.leftPad(""+number,4,"0");

	}

	private String getProcessLocation() {
		if(StringUtils.isEmpty(getProperty().getPlanCode()))
			return getProperty().getProcessLocation();
		else
			return getProperty().getPlanCode().length()>=9?getProperty().getPlanCode().substring(7,9):getProperty().getProcessLocation();
	}

	private String getServiceLotPrefix() {
		 return (StringUtils.isEmpty(getProperty().getPlanCode())) ? SERVICE_LOT_PREFIX : 
			 SERVICE_LOT_PREFIX.substring(0,3) + " " + getProperty().getAssemblyLineId();
	}
	
	private String findNextKdLotNumber(PreProductionLot prodLot,boolean isSameKdLot) {
		String month = (new SimpleDateFormat("yyyyMM")).format(new java.util.Date());
		boolean isSameMonth = prodLot!= null && month.equalsIgnoreCase(prodLot.getProductionLot().substring(8,14));
		
		int number = 1001;
		if(isSameMonth) {
			int sn1 = Integer.parseInt(prodLot.getKdLot().substring(12,16));
			int sn2 = Integer.parseInt(prodLot.getKdLot().substring(16,18));
			number = isSameKdLot ? sn1*100 + sn2 + 1 : (sn1 + 1) * 100 + 1;
		}
		return SERVICE_LOT_PREFIX.substring(0,3) + " " + getProperty().getAssemblyLineId() + month + StringUtils.leftPad(""+number,6,"0");
	}
	
	private PreProductionLot findLatestServiceLot(List<PreProductionLot> prodLots) {
		PreProductionLot maxLot = null;
		for(PreProductionLot prodLot :prodLots) {
			if(!isServiceLot(prodLot.getProductionLot())) continue;
			if(maxLot == null || prodLot.getProductionLot().compareTo(maxLot.getProductionLot()) > 0) maxLot = prodLot;
		}
		return maxLot;
	}
	
	protected boolean isServiceLot(String prodLotNumber) {
		return !StringUtils.isEmpty(prodLotNumber) && prodLotNumber.startsWith(getServiceLotPrefix());
	}
	
	protected ScheduleClientProperty getProperty() {
		return getController().getProperties();
	}
		
	protected ScheduleClientController getController() {
		return controller;
	}
	
	protected List<PreProductionLot> findAllServiceLots(){
		serviceLots = ServiceFactory.getDao(PreProductionLotDao.class).findCurrentDayLastServiceLots(getServiceLotPrefix(), getProcessLocation());
		if(serviceLots.size() > 0) return serviceLots;
		
	    serviceLots = findServiceLots(upcomingLots);
		if(serviceLots.size() > 0) return serviceLots;
		
		serviceLots = findServiceLots(currentLots);
		if(serviceLots.size() > 0) return serviceLots;
		if(!getProperty().isProcessedProductOrLot()) {
			serviceLots = findServiceLots(processedLots);
			if(serviceLots.size() > 5) serviceLots = serviceLots.subList(serviceLots.size() -5, serviceLots.size());
		}
		return serviceLots;
	}
	
	private List<PreProductionLot> findServiceLots(List<PreProductionLot> lotGroup) {
		List<PreProductionLot> serviceLots = new ArrayList<PreProductionLot>();
		if(lotGroup == null) return serviceLots;
		
		for(PreProductionLot item : lotGroup) {
			if(item != null && isServiceLot(item.getProductionLot())) serviceLots.add(item);
		}
		
		return serviceLots;
	}

	public void actionPerformed(ActionEvent e) {
		
		try{
			if(e.getSource().equals(addLotButton)) createServiceLot();
			else if(e.getSource().equals(currentKdLotCheckBox)) kdLotCheckBoxChanged();
			else if (e.getSource().equals(closeButton)) closeDialogBox();		
			else if (e.getSource().equals(addPartButton)) assignPart();
    		else if (e.getSource().equals(removePartButton)) deassignPart();
		
		}catch(Exception ex) {
			MessageDialog.showError(this,"Exception occured. " + ex.getMessage());
			 ex.printStackTrace(new java.io.PrintStream(System.out));
			getController().playNGSound();
		}
	}
	
	public void createServiceLot() {
		Logger.getLogger().check("Add Lot button Clicked");
		if(!productSpecPanel.isProductSpecSelected()){
			MessageDialog.showError(this, "Please Select Product Spec Code");
			getController().playNGSound();
			return;
		}else {
			List<String> productSpecCodes = productSpecPanel.buildSelectedProductSpecCodes();
			if(productSpecCodes.size()!=1){
				MessageDialog.showError(this, "Please Select only one product spec code");
				getController().playNGSound();
				return;
			}
			String productSpecCode = productSpecCodes.get(0);
			if(!productSpecPanel.isUniqueFullSpecSelected()) {
				MessageDialog.showError(this, "Please Select full product spec code up to interior color code");
				getController().playNGSound();
				return;
			}
			
			if(selectedAttributeList.isEmpty()&& isAddMbpn()) {
				MessageDialog.showInfo(this,"Please assign part");
				getController().playNGSound();
				return;
			}
			
			if(isAddMbpn()) {
				BuildAttribute buildAttributeforColorCode = getDao(BuildAttributeDao.class).findById("COLOR_CODE",productSpecCode);
				if(buildAttributeforColorCode==null) {
					MessageDialog.showInfo(this, "Color Code not found for "+ productSpecCode +". Cannot create service lot ");
					Logger.getLogger().info( "Color Code not found. Cannot create service lot");
					getController().playNGSound();
					return;
				}
			}
			
			if(useAlternateColorCheckBox.isSelected()) {
				BuildAttribute alternateColorAttribute = getDao(BuildAttributeDao.class).findById("ALTERNATE_COLOR_SPEC", productSpecCode);
				if (alternateColorAttribute == null) {
					MessageDialog.showInfo(this, "No available Alternate Color");	
					Logger.getLogger().info( "No available Alternate Color");
					return;
			    }
				else {
					String alternateColorCode = StringUtils.rightPad(alternateColorAttribute.getAttributeValue().trim(),ProductSpecCodeDef.EXT_COLOR.getLength()," ");			
					productSpecCode = productSpecCode.replace(productSpecCode.substring(ProductSpecCodeDef.EXT_COLOR.getStartPosition(), ProductSpecCodeDef.EXT_COLOR.getStartPosition()+ProductSpecCodeDef.EXT_COLOR.getLength()),alternateColorCode) ;
				}
			}
			
			getController().playWarnSound();
			boolean isOK = MessageDialog.confirm(this, "Are you sure to create a service production lot with lot size " + lotSizeSpinner.getValue());
			if(!isOK) return;
			
			if(isAddMbpn()){
				mbpnCalc( attrValueMbpn, attrValueCode);		
			}
			
			createProductionLot(productSpecCode);	
			
		}
	}
	
	private void kdLotCheckBoxChanged() {
		List<PreProductionLot> lots = findAllServiceLots();
		PreProductionLot lot = findLatestServiceLot(lots);
		kdLotTextField.getComponent().setText(findNextKdLotNumber(lot, currentKdLotCheckBox.isEnabled() && currentKdLotCheckBox.isSelected()));
	}
	
	private void closeDialogBox(){
		Logger.getLogger().check("Close button Clicked");
		this.dispose();
	}
	
	private void createProductionLot(String productSpecCode) {
		final ProductionLot lot = new ProductionLot();
		lot.setProductionLot(prodLotTextField.getComponent().getText());
		lot.setKdLotNumber(kdLotTextField.getComponent().getText());
		lot.setLotSize(lotSizeSpinner.getValue());
		lot.setProductSpecCode(StringUtils.chomp(productSpecCode, "%"));
		java.util.Date prodDate = (java.util.Date) datePicker.getModel().getValue();
		lot.setProductionDate(new Date(prodDate.getTime()));
		lot.setPlanOffDate(lot.getProductionDate());
		lot.setProcessLocation(getProperty().getProcessLocation());
		lot.setPlanCode(getProperty().getPlanCode());
		lot.setPlantCode(getProperty().getSiteName());
		lot.setLineNo(getProperty().getAssemblyLineId());
		lot.setStartProductId(getStartProductId(lot.getProductSpecCode()));
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				saveProductionLot(lot);
				loadData();
			}
		});
		
	}
	
	 private void mbpnCalc(String attrValueMbpn,String attrValueCode) {
		 List<String> attrValueMbpns = Arrays.asList(attrValueMbpn.split(","));
			int counter = 1;
			// save in PRODUCT_LOT_MBPN_SEQUENCE_TBX
			for (String attrValueMbpn1 : attrValueMbpns) {
				ProductionLotMbpnSequenceId mbpnSeqId = new ProductionLotMbpnSequenceId();
				String prodLot = prodLotTextField.getComponent().getText();
				mbpnSeqId.setProductionLot(prodLot);
				mbpnSeqId.setSequence(counter);
				ProductionLotMbpnSequence mbpnSeq = new ProductionLotMbpnSequence();
				mbpnSeq.setId(mbpnSeqId);
				mbpnSeq.setMbpn(attrValueMbpn1);
				mbpnSeq.setCombinationCode(attrValueCode);

				if (getDao(ProductionLotMbpnSequenceDao.class).findByKey(mbpnSeqId) != null) {
					MessageDialog.showInfo(this, "Duplicate production lot Mbpn Sequence Record");
					Logger.getLogger().info( "Duplicate production lot Mbpn Sequence Record");					
					return;
				} else {
					getDao(ProductionLotMbpnSequenceDao.class).insert(mbpnSeq);	
				}
				counter++;
			}
	 }
	
	private String getStartProductId(String productSpecCode) {
		StringBuilder startProductId = new StringBuilder();
		//temporarily set the start product Id to <sub_id>*<sub_id>
		//this will be override if product will be created.
		BuildAttributeCache bcache = new BuildAttributeCache(BuildAttributeTag.SUB_IDS);
		String subIds = bcache.findAttributeValue(productSpecCode, BuildAttributeTag.SUB_IDS, getProductType());
		if(!StringUtils.isEmpty(subIds)){
			String[] split = subIds.split(Delimiter.COMMA);
			for(int i = 0; i < split.length; i++){
				if(startProductId.length() > 0) startProductId.append("*");
				startProductId.append(split[i]);
			}
		}
			
		return startProductId.toString();
	}
	
	public void createProductStampingSeq(ProductionLot prodLot,String ppId)
	{
		getDao(ProductionLotDao.class).save(prodLot);
		PreProductionLotDao preProductionLotDao = getDao(PreProductionLotDao.class);
		
		PreProductionLot preProdLot = preProductionLotDao.findByKey(prodLot.getProductionLot());
			
		if(preProdLot == null) {
			preProdLot = prodLot.derivePreProductionLot();
			double maxSeq = preProductionLotDao.findMaxSequence(preProdLot.getPlanCode());
			int interval = PropertyService.getPropertyBean(ProductOnHlPropertyBean.class).getSequenceInterval();
			preProdLot.setSequence(maxSeq + interval);
			
		}else {
			preProdLot.setStartProductId(prodLot.getStartProductId());
		}
		preProdLot.setNotes(commentTextField.getComponent().getText());
		preProductionLotDao.save(preProdLot);

		if(upcomingLots.size() > 0) {
			PreProductionLot lastLot = upcomingLots.get(upcomingLots.size() -1);
			lastLot.setNextProductionLot(preProdLot.getProductionLot());
			preProductionLotDao.save(lastLot);
		}
		
		List<ProductStampingSequence> prodLotProductStampList=ServiceFactory.getService(ProductStampingSeqGenService.class).createStampingSequenceList(prodLot.getProductSpecCode(), prodLot.getProcessLocation(), preProdLot,ppId);
		getDao(ProductStampingSequenceDao.class).saveAll(prodLotProductStampList);
		ServiceFactory.getService(ProductStampingSeqGenService.class).updateTargetPreProdLot(preProdLot, prodLotProductStampList.get(0).getId().getProductID(), prodLot.getProductSpecCode(), prodLot.getProcessLocation());

	}

	public void saveProductionLot(ProductionLot lot) {
		ProductDao<?> productDao = ProductTypeUtil.getProductDao(getProperty().getProductType());
		int count=0;
		if(getProductType()==ProductType.MBPN && getProperty().isProductStampingSeqGenerationEnabled())
		{
			createProductStampingSeq(lot, getController().scheduleMainPanel.getProcessPointId());
		}else
		{
			count = productDao.createProducts(lot, 
					getProperty().getProductType(), 
					getProperty().getMSLineId(), 
					getProperty().getMSProcessPointId());
			PreProductionLot preProductionLot =  ServiceFactory.getDao(PreProductionLotDao.class).findByKey(lot.getProductionLot());
			 if(preProductionLot!=null &&  isUpdateComment()) {
				 preProductionLot.setNotes(commentTextField.getComponent().getText());
				 ServiceFactory.getDao(PreProductionLotDao.class).update(preProductionLot);
			 }
		}
		
		if(!isMbpnProduct(getProperty().getProductType()) && !ProductTypeUtil.isDieCast(getProductType()) && count == 0) {
			if(ProductType.KNUCKLE.equals(getProperty().getProductType())) {
				MessageDialog.showError(this,"Could not create service lot. Please check PART NUMBER build attribute for selected product spec");
				getController().playNGSound();
			} else {
				MessageDialog.showError(this,"Could not create service lot.");
				getController().playNGSound();
			}
		} else {
			getController().playWarnSound();
			MessageDialog.showInfo(this, "Service lot is created successfully");
		}
	}

	protected boolean isMbpnProduct(String productType) {
		ProductType type = getProductType();
		return type == ProductType.MBPN || type == ProductType.PLASTICS || type == ProductType.WELD || type == ProductType.BUMPER;
	}
	
	public ProductType getProductType(){
		return ProductTypeCatalog.getProductType(getProperty().getProductType());
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
			addPartButton.setEnabled(true);
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
		JPanel partPanel = new JPanel(new GridLayout(1,1, 40, 10));
		partPanel.add(createSelectedAttributePanel());
		return partPanel;
	}

	private JPanel createPartSelectionPanel() {
		JPanel partPanel = new JPanel(new GridLayout(1,1, 40, 10));
		partPanel.add(createAttributePanel());
		return partPanel;
	}
	
	private Component createAttributePanel() {
		attributePanel = new LabeledListBox(" Available Parts    ", true);
		attributePanel.setModel(new ListModel<String>(getAttributes()),0);
		return attributePanel;
	}
	
	private Component createSelectedAttributePanel() {
		selectedAttributePanel = new LabeledListBox(" Assigned Parts ",true);	
		selectedAttributePanel.setModel(new ListModel<String>(getselectedAttributes()),0);		
		return selectedAttributePanel;
	}
	
	private List<String> getAttributes(){
		if(productSpecPanel.isProductSpecSelected()) {
			attributeNames.removeAll(attributeNames);
			List<String> specCodes = ProductSpec.trimWildcard(productSpecPanel.buildSelectedProductSpecCodes());
			
			try {
			    if(!specCodes.isEmpty()) {
				    String productSpecCode = specCodes.get(0);		    
			    	int combinations = getServiceLotCombinationNumbers();
					for(int i=1; i<= combinations; i++) {
					    String serviceLotCombination = getServiceLotCombinations() +"_"+i;  
						BuildAttribute  attribute = ServiceFactory.getDao(BuildAttributeDao.class).findById(serviceLotCombination, productSpecCode);
						if(attribute!=null)
						attributeNames.add(attribute.getAttributeValue());
						if(i==3 && attributeNames.isEmpty()) {
							break;
						}
					}
			    }
			}catch(Exception ex){
				MessageDialog.showError(this,"Exception occured. " + ex.getMessage());
				 ex.printStackTrace(new java.io.PrintStream(System.out));
				getController().playNGSound();
				
			}
		}	
		attributePanel.getComponent().clearSelection();
		return attributeNames;
	 }
	
	private List<String> getselectedAttributes(){
		List<String> selectedAttributeNames = new SortedArrayList<String>();
		return selectedAttributeNames;
	}
	
private void assignPart() {
		
		String attr = (String) attributePanel.getComponent().getSelectedValue();
		List<String> productSpecCodes = productSpecPanel.buildSelectedProductSpecCodes();
			if(productSpecCodes.size()!=1){
				MessageDialog.showError(this, "Please Select only one product spec code");
				getController().playNGSound();
				return;
			}
			String productSpecCode = productSpecCodes.get(0);		
			String attrCode = attr + "_CODE";
			String attrMbpn = attr + "_MBPNS";

			if (useAlternateColorCheckBox.isSelected()) {
				BuildAttribute alternateAttribute = getDao(BuildAttributeDao.class).findById("ALTERNATE_COLOR_SPEC", productSpecCode);
				if (alternateAttribute == null) {
						MessageDialog.showInfo(this, "No available Alternate Color");	
						Logger.getLogger().info( "No available Alternate Color");
						return;
				}
			}

			BuildAttribute buildAttributeforCode = getDao(BuildAttributeDao.class).findById(attrCode,productSpecCode);
			BuildAttribute buildAttributeforMbpn = getDao(BuildAttributeDao.class).findById(attrMbpn,productSpecCode);
			if (buildAttributeforCode == null ||buildAttributeforMbpn == null ) {
				MessageDialog.showInfo(this, "No Valid combinations found for "+ attr);
				Logger.getLogger().info("No Valid combinations found for "+ attr);
				return;
			}		
			 attrValueCode = buildAttributeforCode.getAttributeValue();
			if (attrValueCode == null) {
				MessageDialog.showInfo(this, "No Valid  attributeValue  found for Combination Code"+ attrCode );
				Logger.getLogger().info( "No Valid  attributeValue  found for Combination Code"+ attrCode);
				return;
			}				
			attrValueMbpn = buildAttributeforMbpn.getAttributeValue();
			if (attrValueMbpn == null) {
				MessageDialog.showInfo(this, "No Valid  attributeValue for Mbpn Code" + attrMbpn);
				Logger.getLogger().info("No Valid  attributeValue for Mbpn Code" + attrMbpn);
				return;
			}
	
		attributePanel.setModel(new ListModel<String>(getchangedAttributeList()), 0);
		selectedAttributePanel.setModel(new ListModel<String>(getchangedselectedAttributeList()), 0);
	}

	private void deassignPart() {
		selectedAttributePanel.setModel(new ListModel<String>(getSelectedDeassignedAttributeList()), 0);
		attributePanel.setModel(new ListModel<String>(getchangedDeassignedAttributeList()), 0);
	}

	private List<String> getchangedDeassignedAttributeList() {
		List<String> attributeList = attributeNames;
		attributeList.add(selectedValue);
		return attributeList;
	}

	private List<String> getSelectedDeassignedAttributeList() {
		selectedValue = (String) selectedAttributePanel.getComponent().getSelectedValue();
		selectedAttributeList.clear();
		return selectedAttributeList;
	}

	private List<String> getchangedselectedAttributeList() {
		selectedAttributeList.clear();
		selectedAttributeList.add(attributeValue);
		return selectedAttributeList;
	}

	private List<String> getchangedAttributeList() {
		attributeValue = (String) attributePanel.getComponent().getSelectedValue();
		List<String> attributeList = attributeNames;
		attributeList.remove(attributeValue);
		attributeList.addAll(selectedAttributeList);
		return attributeList;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
			if (e.getSource() == productSpecPanel.getPanel(YmtocColumns.Year.name()).getComponent()
					|| e.getSource() == productSpecPanel.getPanel(YmtocColumns.Model.name()).getComponent()
					|| e.getSource() == productSpecPanel.getPanel(YmtocColumns.Model_Type.name()).getComponent()
					|| e.getSource() == productSpecPanel.getPanel(YmtocColumns.Ext_Color.name()).getComponent()
					|| e.getSource() == productSpecPanel.getPanel(YmtocColumns.Int_Color.name()).getComponent()) {
				attributePanel.setModel(new ListModel<String>(getAttributes()), 0);
				selectedAttributePanel.setModel(new ListModel<String>(getselectedAttributes()), 0);
				selectedAttributeList.clear();
			}else if (e.getSource() == attributePanel.getComponent()) {
				enableButton();
			}
	}
	
	public String getServiceLotCombinations() {
		return getProperty().getServiceLotCombination().toString();
	}
	
	public int getServiceLotCombinationNumbers() {
		return getProperty().getServiceLotCombinationNumber();
	}
    
	private void enableButton() {
		if (attributePanel.getComponent() != null) {
			addPartButton.setEnabled(true);
		} else {
			addPartButton.setEnabled(false);
		}
		if (selectedAttributePanel.getComponent().getSelectedValues() != null) {
			removePartButton.setEnabled(true);
		} else {
			removePartButton.setEnabled(false);
		}
	}
	
	private boolean  isAddMbpn() {
		return getProperty().isAssignMbpnToProdLot();
	}
	
	public boolean isUpdateComment() {
	    return getController().updateComment();
	}
	
	private LabeledComponent<JDatePickerImpl> createDatePickerPanel() {
		UtilDateModel model = new UtilDateModel();
		Calendar today = Calendar.getInstance();
		model.setDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
		model.setSelected(true);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		this.datePanel = new JDatePanelImpl(model, p);
		this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		LabeledComponent<JDatePickerImpl> datePickerPanel = new LabeledComponent<JDatePickerImpl>("PROD DATE", this.datePicker);
		datePickerPanel.setInsets(10, 10, 10, 10);
		return datePickerPanel;
	}
	
	public class DateLabelFormatter extends AbstractFormatter {
		private static final long serialVersionUID = 1L;
		private String datePattern = "yyyy-MM-dd";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parseObject(text);
	    }

	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }

	}
    
}