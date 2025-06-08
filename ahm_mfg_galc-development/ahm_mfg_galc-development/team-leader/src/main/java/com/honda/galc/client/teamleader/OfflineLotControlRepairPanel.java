package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.teamleader.model.RepairPart;
import com.honda.galc.client.teamleader.model.RepairPartTableModel;
import com.honda.galc.client.teamleader.model.RepairProcessPointTableModel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.event.UpdateEvent;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.RepairProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.RepairProcessPoint;
import com.honda.galc.entity.product.RepairProcessPointId;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;

public class OfflineLotControlRepairPanel extends TabbedPanel
		implements ListSelectionListener, ActionListener, TableModelListener {

	private static final long serialVersionUID = 1L;
	protected ProcessPointSelectiontPanel processPointPanel;
	protected PartNameSelectionPanel partNameSelectionPanel;
	protected TablePane assignedPartNameSelectionPanel;
	protected TablePane partSpecSelectionPanel;
	protected TablePane repairLotControlRulePanel;
	protected TablePane measurementListPanel;
	protected JButton refreshButton, copyToNewProcessPointButton, addPartButton, removePartButton, copyButton;
	private PartSpecTableModel partSpecTableModel;
	private RepairPartTableModel assignedPartNameTableModel;
	private MeasurementSpecListTableModel measurementListTableModel;
	private RepairProcessPointTableModel repairProcessPointTableModel;

	private int processPointPanelWidth = 1000;
	private int processPointPanelHeight = 50;

	private int refreshButtonWidth = 80;

	private int midPanelHeight = 250;
	private Dimension screenDimension;

	private boolean isHeadLess = false;
	private String currentProductType;
	private boolean errorDetected = false;
	private LabeledComboBox ppComboBox;
	private JDialog copyDialog;
	private List<PartName> partNames;
	
	
	public OfflineLotControlRepairPanel() {
		super("LotControl Offline Repair", KeyEvent.VK_L);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}

	public OfflineLotControlRepairPanel(TabbedMainWindow mainWindow) {
		super("LotControl Offline Repair", KeyEvent.VK_L, mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}

	protected void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);

		Box box1 = Box.createHorizontalBox();
		box1.setBorder(border);
		box1.add(createProcessPointSelectionPanel());
		box1.add(createRefreshButton());
		add(box1);

		Box box21 = Box.createVerticalBox();
		box21.setBorder(border);
		JPanel buttonPanel2 = new JPanel(new GridLayout(6,1, 10, 40));
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		buttonPanel2.add(createAddPartButton());
		buttonPanel2.add(createRemovePartButton());
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		box21.add(buttonPanel2);
		
		Box box22 = Box.createVerticalBox();
		box22.add(createRepairLotControlRulePanel());
		box22.add(createPartSpecSelectionPanel());
		box22.add(createMeasurementListPanel());
	
		Box box2 = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(createPartSelectionPanel());
		box2.add(box21);
		box2.add(createAssignedPartSelectionPanel());
		box2.add(box22);
		add(box2);
		
		Box box4 = Box.createHorizontalBox();
		box4.setBorder(border);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		buttonPanel.add(createCopyToNewProcessPointButton());
		box4.add(buttonPanel);

		add(box4);

	}

	private ProcessPointSelectiontPanel createProcessPointSelectionPanel() {
		String siteName = PropertyService.getSiteName();
		if (StringUtils.isEmpty(siteName)) {
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");
		}

		processPointPanel = new ProcessPointSelectiontPanel(siteName);
		processPointPanel.setSize(processPointPanelWidth, processPointPanelHeight);

		return processPointPanel;
	}

	private JButton createRefreshButton() {
		refreshButton = new JButton("Refresh");
		refreshButton.setToolTipText("Refresh Lot Control Rule on server cache for selected headless process point");
		refreshButton.setSize(refreshButtonWidth, processPointPanelHeight);
		refreshButton.setEnabled(false);
		return refreshButton;
	}
	
	private PartNameSelectionPanel createPartSelectionPanel() {
		partNameSelectionPanel = new PartNameSelectionPanel(processPointPanel.getWidth() / 6, midPanelHeight,
				new Dimension(screenDimension.width / 4, screenDimension.height / 2));
		
		return partNameSelectionPanel;
	}
	
	private TablePane createAssignedPartSelectionPanel() {
		assignedPartNameSelectionPanel = new TablePane("Assigned Parts"); 
		assignedPartNameSelectionPanel.setSize(processPointPanel.getWidth() / 6, midPanelHeight);
		assignedPartNameSelectionPanel.setPreferredSize(new Dimension(screenDimension.width / 4, screenDimension.height / 2));
		
		return assignedPartNameSelectionPanel ;
	}
	
	private TablePane createRepairLotControlRulePanel() {
		repairLotControlRulePanel = new TablePane("Repair Rule");
		return repairLotControlRulePanel;
	}

	private TablePane createPartSpecSelectionPanel() {
		partSpecSelectionPanel = new TablePane("Part Spec");
		return partSpecSelectionPanel;
	}

	private TablePane createMeasurementListPanel() {
		measurementListPanel = new TablePane("Measurement Spec");
		return measurementListPanel;
	}

	private JButton createCopyToNewProcessPointButton() {
		copyToNewProcessPointButton = new JButton("Copy to New Process Point");
		copyToNewProcessPointButton.setToolTipText("Copy to New Process Point");
		copyToNewProcessPointButton.setSize(refreshButtonWidth, processPointPanelHeight);
		copyToNewProcessPointButton.setEnabled(false);
		return copyToNewProcessPointButton;
	}
	
	private JButton createAddPartButton() {
		addPartButton = new JButton(" >> ");
		addPartButton.setToolTipText("add part");
		addPartButton.setSize(refreshButtonWidth, processPointPanelHeight);
		addPartButton.setEnabled(false);
		return addPartButton;
	}
	
	private JButton createRemovePartButton() {
		removePartButton = new JButton(" << ");
		removePartButton.setToolTipText("remove part");
		removePartButton.setSize(refreshButtonWidth, processPointPanelHeight);
		removePartButton.setEnabled(false);
		return removePartButton;
	}

	@Override
	public void onTabSelected() {
		if (isInitialized) {
			return;
		}
		initComponents();
		addListeners();
		isInitialized = true;
	}

	private void addListeners() {
		partNameSelectionPanel.getPartSelectionPanel().addListSelectionListener(this);
		assignedPartNameSelectionPanel.addListSelectionListener(this);
		processPointPanel.getProcessPointComboBox().getComponent().addActionListener(this);
		partSpecSelectionPanel.addListSelectionListener(this);
		repairLotControlRulePanel.addListSelectionListener(this);
		refreshButton.addActionListener(this);
		copyToNewProcessPointButton.addActionListener(this);
		addPartButton.addActionListener(this);
		removePartButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(processPointPanel.getProcessPointComboBox().getComponent()))
			processPointSelected();
		else if (e.getSource().equals(addPartButton))
			assignPart();
		else if (e.getSource().equals(removePartButton))
			deassignPart();
		else if (e.getSource().equals(copyToNewProcessPointButton))
			copyToNewProcessPoint();
		else if (e.getSource().equals(copyButton))
			copyData();
			
	}

	private void deassignPart() {
		RepairPart assignedPartName = assignedPartNameTableModel.getSelectedItem();
		List<RepairPart> assignedPartList = assignedPartNameTableModel.getItems();
		assignedPartList.remove(assignedPartName);
		sortAndrefreshAssignedPartNames(assignedPartList);
		
		List<PartName> partList = partNames!= null && partNames.size() > 0 ?partNames: partNameSelectionPanel.getPartNameTableModel().getItems();
		partList.add(assignedPartName.getPartName());
		partNameSelectionPanel.update(partList);
				
		removeRepairPart(assignedPartName);
		reset();
	}

	private void removeRepairPart(RepairPart assignedPartName) {
		RepairProcessPointDao repairProcessPointDao =  getDao(RepairProcessPointDao.class);
		ProcessPoint processPoint = processPointPanel.getCurrentProcessPoint();
		List<RepairProcessPoint> repairParts = findAllRepairPartsForSelectedAssignedPart(processPoint.getProcessPointId(), assignedPartName.getPartName());
		for(RepairProcessPoint part: repairParts){
			repairProcessPointDao.remove(part);
			logUserAction(REMOVED, part);
			AuditLoggerUtil.logAuditInfo( part, null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}
		
	}

	private void assignPart() {
		PartName partName = partNameSelectionPanel.getPartNameTableModel().getSelectedItem();
		List<PartName> partList = partNames!= null && partNames.size() > 0 ?partNames: partNameSelectionPanel.getPartNameTableModel().getItems();
		partList.remove(partName);
		partNameSelectionPanel.update(partList);
				
		List<RepairPart> assignedPartList = assignedPartNameTableModel.getItems();
		assignedPartList.add(createRepairPart(partName));
		sortAndrefreshAssignedPartNames(assignedPartList);
		
		reset();
	}

	private RepairPart createRepairPart(PartName partName) {
		List<PartSpec> partSpecs = partName.getAllPartSpecs();
		ProcessPoint processPoint = processPointPanel.getCurrentProcessPoint();
		RepairProcessPointDao repairProcessPointDao =  getDao(RepairProcessPointDao.class);
		for(PartSpec partSpec:partSpecs){
			RepairProcessPointId id = new RepairProcessPointId();
			id.setPartId(partSpec.getId().getPartId());
			id.setPartName(partSpec.getId().getPartName());
			id.setProcessPointId(processPoint.getProcessPointId());
			
			RepairProcessPoint repairProcessPoint = new RepairProcessPoint();
			repairProcessPoint.setId(id);
			repairProcessPoint.setSequenceNo(0);
			
			repairProcessPointDao.save(repairProcessPoint);
			logUserAction(SAVED, repairProcessPoint);
			AuditLoggerUtil.logAuditInfo( null, repairProcessPoint,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}
		RepairPart repairPart = new RepairPart();
		repairPart.setPartName(partName);
		
		return repairPart;
	}

	private void processPointSelected() {
		ProcessPoint processPoint = processPointPanel.getCurrentProcessPoint();
		if (processPoint == null ){
			copyToNewProcessPointButton.setEnabled(false);
		}else{
			initPartSelectionPanel();
			
		}
	}

	private void enableButtons() {
		if(processPointPanel.getCurrentProcessPointId() == null){
			copyToNewProcessPointButton.setEnabled(false);
			addPartButton.setEnabled(false);
			removePartButton.setEnabled(false);
		}else{
			copyToNewProcessPointButton.setEnabled(true);
			if(partNameSelectionPanel.getPartNameTableModel().getSelectedItem() != null) {
				addPartButton.setEnabled(true);
			}else{
				addPartButton.setEnabled(false);
			}
			if(assignedPartNameTableModel != null && assignedPartNameTableModel.getSelectedItem() != null){
				removePartButton.setEnabled(true);
			}else{
				removePartButton.setEnabled(false);
			}
		}
	}

	private void initPartSelectionPanel() {
		String productType = processPointPanel.selectedProductType();
		if (productType != null) {
			if (!productType.equalsIgnoreCase(currentProductType)) {
				currentProductType = productType;
				updatePartSelectionModel();
			}
		}
	}

	@EventSubscriber(eventClass = UpdateEvent.class)
	public void processUpdateEvent(UpdateEvent event) {
		if (event.isUpdatePartName()){
			updatePartSelectionModel();
			reset();
		}
	}

	private void updatePartSelectionModel() {
		if (partNameSelectionPanel == null)
			return;
		String productType = processPointPanel.selectedProductType();
		if (productType != null)
			partNames = loadPartNames(productType);

		partNameSelectionPanel.update(partNames);
	}

	private List<PartName> loadPartNames(String productType) {
		return getDao(PartNameDao.class).findAllByProductType(productType);
	}

	@EventSubscriber(eventClass = ProcessPointSelectionEvent.class)
	public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
		if (event.isEventFromSource(SelectionEvent.DEPARTMENT_SELECTED, processPointPanel)) {
			initPartSelectionPanel();
			filterProcessPoints();
		} else if (event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, processPointPanel)) {
			filterProcessPoints();
		} else if (event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, processPointPanel)) {
			if (processPointPanel.getCurrentProcessPoint() == null)
			{
				return;
			}
			isHeadLess = isHeadLess(processPointPanel.getCurrentProcessPoint());
			enableRefreshButton(isHeadLess);
			if(processPointPanel.getCurrentProcessPointId() == null){
				enableButtons();
			}else{
				enableButtons();
				List<RepairPart> assignedParts = getAllRepairParts(processPointPanel.getCurrentProcessPointId());
				if(assignedParts != null && assignedParts.size() > 0){
					assignedPartNameTableModel = new RepairPartTableModel(assignedPartNameSelectionPanel.getTable(),assignedParts,true);
					sortAndrefreshAssignedPartNames(assignedParts);
					List<PartName> partList = partNameSelectionPanel.getPartNameTableModel().getItems();
					List<PartName> assignedPartNames = new ArrayList<PartName>();
					for(RepairPart part:assignedParts){
						assignedPartNames.add(part.getPartName());
					}
					partList.removeAll(assignedPartNames);
					partNameSelectionPanel.update(partList);
					assignedPartNameTableModel.addTableModelListener(this);
					assignedPartNameTableModel.pack();
				}else{
					assignedPartNameTableModel = new RepairPartTableModel(assignedPartNameSelectionPanel.getTable(),new ArrayList<RepairPart>(),true);
					updatePartSelectionModel();
					assignedPartNameTableModel.addTableModelListener(this);
					assignedPartNameTableModel.pack();
				}
				
			}
		}
	}

	private void filterProcessPoints() {
		ComboBoxModel<ProcessPoint> model = (ComboBoxModel<ProcessPoint>)processPointPanel.getProcessPointComboBox().getComponent().getModel();
		ArrayList<ProcessPoint> processPoints = new ArrayList<ProcessPoint>();
		for(int i=0;i<model.getSize();i++){
			ProcessPoint processPoint = model.getElementAt(i);
			processPoints.add(processPoint);
		}
    	
    	List<ProcessPoint> filteredProcessPoints  = new ArrayList<ProcessPoint>();
	    for(ProcessPoint ppt: processPoints){
	    	if(ppt.getProcessPointId().equals(processPointPanel.DUMMY_PROCESS_POINT_ID)){
	    		filteredProcessPoints.add(ppt);
	    	}else if(ProcessPointType.Repair == ppt.getProcessPointType()){
	    				filteredProcessPoints.add(ppt);
	    	}
	    }
	    
	    if(filteredProcessPoints.size() > 0)
	    processPointPanel.getProcessPointComboBox().setModel(new ComboBoxModel<ProcessPoint>(filteredProcessPoints,"getDisplayName"), 0);
	}

	private List<RepairPart> getAllRepairParts(String processPointId) {
		List<RepairPart> repairPartList = new ArrayList<RepairPart>();
		List<Object[]> repairPartDataList = getDao(RepairProcessPointDao.class).findRepairPartData(processPointId);
		for (Object[] repairPartData: repairPartDataList) {
			Integer sequenceNo = (Integer) repairPartData[0];
			PartName partName = new PartName();
			partName.setPartName((String) repairPartData[1]);
			partName.setProductTypeName((String) repairPartData[2]);
			partName.setPartConfirmCheck(repairPartData[3] == null ? 0 : (Integer) repairPartData[3]);
			partName.setWindowLabel((String) repairPartData[4]);
			partName.setSubProductType((String) repairPartData[5]);
			partName.setPartVisible(repairPartData[6] == null ? 0 : (Integer) repairPartData[6]);
			partName.setRepairCheck(repairPartData[7] == null ? 0 : (Integer) repairPartData[7]);
			partName.setExternalRequired(repairPartData[8] == null ? 0 : (Integer) repairPartData[8]);
			partName.setLETCheckRequired(repairPartData[9] == null ? 0 : (Integer) repairPartData[9]);
			RepairPart repairPart = new RepairPart();
			repairPart.setSequenceNo(sequenceNo);
			repairPart.setPartName(partName);
			repairPartList.add(repairPart);
		}
		return repairPartList;
	}
	private List<RepairProcessPoint> findAllRepairPartsForProcessPoint(String currentProcessPointId) {
		return getDao(RepairProcessPointDao.class).findAllRepairPartsForProcessPoint(currentProcessPointId);
	}
	
	private List<RepairProcessPoint> findAllRepairPartsForSelectedAssignedPart(String currentProcessPointId, PartName partName) {
		return getDao(RepairProcessPointDao.class).findAllRepairPartsByProcessPointAndPartName(currentProcessPointId, partName.getPartName());
	}

	private void enableRefreshButton(boolean headLess) {
		refreshButton.setEnabled(headLess);
	}

	private boolean isHeadLess(ProcessPoint processPoint) {
		if (processPoint == null)
			return false;
		return (getDao(TerminalDao.class).findFirstByProcessPointId(processPoint.getProcessPointId()) == null);
	}

	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting())
			return;
		Exception exception = null;
		try {
			 if(e.getSource() == (assignedPartNameSelectionPanel.getTable().getSelectionModel())){
				RepairPart assignedPartName = assignedPartNameTableModel.getSelectedItem();
				
				if(assignedPartName != null){
					List<RepairProcessPoint> repairParts = findAllRepairPartsForSelectedAssignedPart(processPointPanel.getCurrentProcessPointId(), assignedPartName.getPartName());
					repairProcessPointTableModel = new RepairProcessPointTableModel(repairLotControlRulePanel.getTable(), repairParts);
					repairProcessPointTableModel.pack();
					repairProcessPointTableModel.addTableModelListener(this);
					partSpecTableModel= new PartSpecTableModel(partSpecSelectionPanel.getTable(),new ArrayList<PartSpec>());
					measurementListTableModel = new MeasurementSpecListTableModel(measurementListPanel.getTable(),new ArrayList<MeasurementSpec>());
				}
			}else if(e.getSource() == (repairLotControlRulePanel.getTable().getSelectionModel())){
				RepairProcessPoint repairPart = repairProcessPointTableModel.getSelectedItem();
				if(repairPart != null){
					PartSpec partSpec = getDao(PartSpecDao.class).findValueWithPartNameAndPartID(repairPart.getId().getPartName(), repairPart.getId().getPartId());
					if(partSpec != null){
						List<PartSpec> partSpecList = new ArrayList<PartSpec>();
						partSpecList.add(partSpec);
						partSpecTableModel= new PartSpecTableModel(partSpecSelectionPanel.getTable(),partSpecList);
						partSpecTableModel.setEditable(false);
						partSpecTableModel.pack();
						
						
						measurementListTableModel = new MeasurementSpecListTableModel(measurementListPanel.getTable(),
								partSpec == null ? null : partSpec.getMeasurementSpecs());
						measurementListTableModel.pack();
						
					}
				}
			}
		} catch (Exception ex) {
			exception = ex;
		}
		enableButtons();
		handleException(exception);
	}

	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof RepairProcessPointTableModel) {
			handleRepairProcessPointTableChanged(e, (RepairProcessPointTableModel)e.getSource());
		}else if(e.getSource() instanceof RepairPartTableModel){
			this.clearErrorMessage();
			handleRepairPartTableChanged(e, (RepairPartTableModel)e.getSource());
		}
	}

	private void handleRepairProcessPointTableChanged(TableModelEvent e, RepairProcessPointTableModel model) {
		try {
			final RepairProcessPoint repairPart = model.getSelectedItem();
			if(repairPart == null) return;
			if(e.getType()==TableModelEvent.UPDATE){
				RepairProcessPoint oldRepairPart = getDao(RepairProcessPointDao.class).findByKey(repairPart.getId());
				getDao(RepairProcessPointDao.class).update(repairPart);
				logUserAction(UPDATED, repairPart);
				RepairProcessPoint newRepairPart = getDao(RepairProcessPointDao.class).findByKey(repairPart.getId());
				AuditLoggerUtil.logAuditInfo( oldRepairPart, newRepairPart,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");		
				model.selectItem(repairPart);
			}
		} catch(Exception ex) {
			if(model!=null)
				model.rollback();
			handleException(ex);
		}
		return;
		
	}
	
	private void handleRepairPartTableChanged(TableModelEvent e, RepairPartTableModel model) {
		try {	
			
			RepairPart assignedPartName = assignedPartNameTableModel.getSelectedItem();
			if(assignedPartName == null) return;
			if(e.getType()==TableModelEvent.UPDATE ){
				
				RepairProcessPointDao repairProcessPointDao =  getDao(RepairProcessPointDao.class);
				validateRepairPart(model);
				int rows = repairProcessPointDao.updateSequenceNo(processPointPanel.getCurrentProcessPointId(), assignedPartName.getPartName().getPartName(), assignedPartName.getSequenceNo());
				if(rows > 0){
					List<RepairPart> repairParts = getAllRepairParts(processPointPanel.getCurrentProcessPointId());
					sortAndrefreshAssignedPartNames(repairParts);
				}
			}
		} catch(Exception ex) {
			if(model!=null)
				model.rollback();
			handleException(ex);
		}
		return;
	}

	@Override
	protected void handleException(Exception e) {
		if (e != null) {
			getLogger().error(e, "unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			this.setErrorMessage(e.getMessage());
		} else if (!errorDetected) {
			clearErrorMessage();
		}
	}
		
	private void reset(){
		if(repairProcessPointTableModel != null){
			RepairPart assignedPartName = assignedPartNameTableModel.getSelectedItem();
			if(assignedPartName != null){
				List<RepairProcessPoint> repairParts = findAllRepairPartsForSelectedAssignedPart(processPointPanel.getCurrentProcessPointId(), assignedPartName.getPartName());
				repairProcessPointTableModel.refresh(repairParts);
			}else{
				repairProcessPointTableModel.refresh(new ArrayList<RepairProcessPoint>());
			}
		}
		if(partSpecTableModel != null){
			RepairProcessPoint repairPart = repairProcessPointTableModel.getSelectedItem();
			if(repairPart != null){
				PartSpec partSpec = getDao(PartSpecDao.class).findValueWithPartNameAndPartID(repairPart.getId().getPartName(), repairPart.getId().getPartId());
				if(partSpec != null){
					List<PartSpec> partSpecList = new ArrayList<PartSpec>();
					partSpecList.add(partSpec);
				}
			}else{
				partSpecTableModel.refresh(new ArrayList<PartSpec>());
			}
		}
		if(measurementListTableModel != null){
			PartSpec partSpec = partSpecTableModel.getSelectedItem();
			if(partSpec != null){
				measurementListTableModel.refresh(partSpec.getMeasurementSpecs());
			}else{
				measurementListTableModel.refresh(new ArrayList<MeasurementSpec>());
			}
		}
		enableButtons();
	}
	
	private void copyToNewProcessPoint(){
		ComboBoxModel<ProcessPoint> model = (ComboBoxModel<ProcessPoint>)processPointPanel.getProcessPointComboBox().getComponent().getModel();
		ArrayList<ProcessPoint> ppt = new ArrayList<ProcessPoint>();
		for(int i=0;i<model.getSize();i++){
			ProcessPoint p = model.getElementAt(i);
			ppt.add(p);
		}
		
		 JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
		 
		 ppComboBox = new LabeledComboBox("Process Point");
         ppComboBox.getComponent().setPreferredSize(new Dimension(350,20)); 
         ppComboBox.setModel(new ComboBoxModel<ProcessPoint>(ppt,"getDisplayName"), 0);
         
         copyButton = new JButton(" Copy ");
         copyButton.addActionListener(this);
         comboPanel.add(ppComboBox);
         comboPanel.add(copyButton);
         
         copyDialog = new JDialog(this.getMainWindow());
         copyDialog.setContentPane(comboPanel);
         copyDialog.setSize(600, 300);
         copyDialog.setLocationRelativeTo(this.getMainWindow());
         copyDialog.setVisible(true);
       
	}
	
	private void copyData(){
		ProcessPoint selectedProcessPoint = ((ProcessPoint)ppComboBox.getComponent().getSelectedItem());
	
		if(selectedProcessPoint != null && !selectedProcessPoint.getId().equalsIgnoreCase(processPointPanel.getCurrentProcessPointId())){
			List<RepairProcessPoint> repairPartList = findAllRepairPartsForProcessPoint(processPointPanel.getCurrentProcessPointId());
			RepairProcessPointDao repairProcessPointDao =  getDao(RepairProcessPointDao.class);
			for(RepairProcessPoint repairProcessPoint: repairPartList){
					RepairProcessPointId id = new RepairProcessPointId();
					id.setPartId(repairProcessPoint.getId().getPartId());
					id.setPartName(repairProcessPoint.getId().getPartName());
					id.setProcessPointId(selectedProcessPoint.getId());
					
					RepairProcessPoint repairProcessPointCopy = new RepairProcessPoint();
					repairProcessPointCopy.setId(id);
					repairProcessPointCopy.setSequenceNo(repairProcessPoint.getSequenceNo());
					repairProcessPointCopy.setDeviceId(repairProcessPoint.getDeviceId());
					repairProcessPointCopy.setInstructionCode(repairProcessPoint.getInstructionCode());
					
					repairProcessPoint.getId().setProcessPointId(selectedProcessPoint.getId());
					RepairProcessPoint repairProcessPointNew =  repairProcessPoint;
					RepairProcessPoint repairProcessPointOld = repairProcessPointDao.findByKey(repairProcessPoint.getId());
					
					repairProcessPointDao.save(repairProcessPointCopy);
					logUserAction(SAVED, repairProcessPointCopy);
					if(repairProcessPointOld==null) {
						AuditLoggerUtil.logAuditInfo(null, repairProcessPointCopy,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
					}
					else {					
						if((!repairProcessPointOld.equals(repairProcessPointCopy)))		{
							AuditLoggerUtil.logAuditInfo(repairProcessPointOld, repairProcessPointNew,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
						}
				    }
			}
		}
		processPointPanel.getProcessPointComboBox().getComponent().setSelectedItem(selectedProcessPoint);
		copyDialog.dispose();
	}
	
	private void sortAndrefreshAssignedPartNames(List<RepairPart> repairParts){
		Collections.sort(repairParts, new Comparator<RepairPart>() {

	        public int compare(RepairPart part0, RepairPart part1) {
	            return part0.getSequenceNo() - part1.getSequenceNo();
	        }
	    });
		
		assignedPartNameTableModel.refresh(repairParts);
	}
	
	private void validateRepairPart(RepairPartTableModel model) {
		//validate Sequence Number
		RepairPart currentRepairPart = model.getSelectedItem();
		List<String> partNameList = new ArrayList<String>();
		List<RepairPart> repairPartList = getAllRepairParts(processPointPanel.getCurrentProcessPointId());
		for(RepairPart r :  repairPartList){
			if(currentRepairPart.getSequenceNo() == r.getSequenceNo() &&
					!r.getPartName().getPartName().trim().equalsIgnoreCase(currentRepairPart.getPartName().getPartName().trim()))
				partNameList.add(r.getPartName().getPartName());
		}
		
		if(partNameList.size() > 0) throw new TaskException("Invalid Sequence Number:" + currentRepairPart.getSequenceNo() + " already used by parts:" + partNameList);
		
	}

	
}
