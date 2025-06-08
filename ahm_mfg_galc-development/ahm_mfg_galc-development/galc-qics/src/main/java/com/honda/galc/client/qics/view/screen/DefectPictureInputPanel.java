package com.honda.galc.client.qics.view.screen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.qics.util.QicsUtils;
import com.honda.galc.client.qics.view.QicsPicImage;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.dialog.SelectAssociateNumberDialog;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectRepairResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.ImageSection;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Defect graphic input screen for QICS.
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class DefectPictureInputPanel extends DefectInputPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private QicsPicImage defectImagePanel = null;
	private ObjectTablePane<DefectGroup> defectGroupPane = null;
	private ObjectTablePane<DefectTypeDescription> defectPane = null;
	private ObjectTablePane<Division> writeUpDeptPane = null;
	private ObjectTablePane<Division> respDeptPane = null;
	private ObjectTablePane<Line> respLinePane = null;
	private ObjectTablePane<Zone> respZonePane = null;


	public DefectPictureInputPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.PICTURE_INPUT;
	}

	@Override
	protected void initialize() {

		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			initComponentsWithRespDept();
			initComponentLocationsWithRespDept();
		}else {
			initComponents();
			initComponentLocations();
		}

		super.initialize();
		getAcceptButton().setVisible(false);
	}

	@Override
	public void startPanel() {

		getDefectGroupPane().removeData();
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			getWriteUpDeptPane().removeData();
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();

			List<Division> writeUpDepts = getClientModel().getDepartments();
			getWriteUpDeptPane().reloadData(writeUpDepts);

		}	
		getDefectGroupPane().reloadData(getProductModel().getDefectGroups());

		setButtonsState();

		getDefectImagePanel().setDefects(getQicsController().getProductModel().getDefects());
		getDefectImagePanel().clearPolygon();
		getDefectImagePanel().clearImage();
		getDefectImagePanel().setDispPolygonStatus(false);

		getDefectPane().clearSelection();
		getDefectPane().getTable().removeAll();

		setDefaultDefectStatusSelected();

		selectFirstDefectGroup();

		setFocus(getDefaultElement());
		
	}
	
	private void initComponents() {
		defectImagePanel = createDefectImagePanel();
		defectGroupPane = createDefectGroupPane();
		defectPane = createDefectPane();

		add(getDefectImagePanel());
		add(getDefectGroupPane());		
		add(getDefectPane());
	}

	private void initComponentsWithRespDept() {
		initComponents();
		writeUpDeptPane = createWriteUpDeptPane();
		respDeptPane = createRespDeptPane();
		respLinePane = createRespLinePane();
		respZonePane = createRespZonePane();
		add(getWriteUpDeptPane());		
		add(getRespDeptPane());
		add(getRespLinePane());
		add(getRespZonePane());
	}
	
	private void initComponentLocations(){
		defectImagePanel.setSize(500, 500);
		defectImagePanel.setLocation(0, 0);
		defectPane.setSize(265, 500);
		defectPane.setLocation(getDefectImagePanel().getX() + getDefectImagePanel().getWidth() + getSpacing(), getDefectImagePanel().getY());
		defectGroupPane.setSize(230, 350);
		defectGroupPane.setLocation(getDefectPane().getX() + getDefectPane().getWidth() + getSpacing(), getDefectPane().getY());

	}
	
	private void initComponentLocationsWithRespDept(){
		defectImagePanel.setSize(500, 500);
		defectImagePanel.setLocation(0, 0);

		defectGroupPane.setSize(265, 150);
		defectGroupPane.setLocation(getDefectImagePanel().getX() + getDefectImagePanel().getWidth() + getSpacing(), getDefectImagePanel().getY());

		defectPane.setSize(265, 350);
		defectPane.setLocation(getDefectGroupPane().getX(), getDefectGroupPane().getY() + getDefectGroupPane().getHeight() + getSpacing());

		writeUpDeptPane.setSize(230, 90);
		writeUpDeptPane.setLocation(getDefectGroupPane().getX() + getDefectGroupPane().getWidth() + getSpacing(), getDefectGroupPane().getY());

        respDeptPane.setSize(230, 90);
        respDeptPane.setLocation(getWriteUpDeptPane().getX(), getWriteUpDeptPane().getY() + getWriteUpDeptPane().getHeight() + getSpacing());

		respLinePane.setSize(230, 90);
		respLinePane.setLocation(getRespDeptPane().getX(), getRespDeptPane().getY() + getRespDeptPane().getHeight() + getSpacing());

		respZonePane.setSize(230, 90);
		respZonePane.setLocation(getRespLinePane().getX(), getRespLinePane().getY() + getRespLinePane().getHeight() + getSpacing());
	}


	public boolean isDefectSelected() {
		boolean rowSelected = getDefectPane().getTable().getSelectedRowCount() > 0;
		boolean imageConfigured = getDefectImagePanel().getNowImage()!=null;
		boolean defectMarked = isDefectMarkedOnImage();		
		
		return  rowSelected && imageConfigured && defectMarked;
	}	

	// === get/set === //
	public QicsPicImage getDefectImagePanel() {
		return defectImagePanel;
	}

	public ObjectTablePane<DefectGroup> getDefectGroupPane() {
		return defectGroupPane;
	}

	public ObjectTablePane<DefectTypeDescription> getDefectPane() {
		return defectPane;
	}

	protected int getSpacing() {
		return 0;
	}

	// === ui factory methods === //
	public QicsPicImage createDefectImagePanel() {
		QicsPicImage panel = new QicsPicImage();
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panel.setDispPolygonStatus(getQicsPropertyBean().isDisplayImageSection());
		return panel;
	}

	public ObjectTablePane<DefectTypeDescription> createDefectPane() {
		return createTablePane("Defect", ColumnMappings.with("Defect", "defectTypeName"), true);
	}

	public ObjectTablePane<DefectGroup> createDefectGroupPane() {
		return createTablePane("Defect Group", ColumnMappings.with("Defect Group", "defectGroupName"), true);
	}

	

	// ================= event handlers mappings ========================//
	@Override
	protected void mapEventHandlers() {
		getDefectImagePanel().addMouseListener(createDefectImageClickHandler());
		getDefectGroupPane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectPane().getTable().getSelectionModel().addListSelectionListener(this);
		// Table Header click listener, select by default the first element after sorting 
		getDefectGroupPane().getTable().getTableHeader().addMouseListener(new MouseSortListener());
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			getWriteUpDeptPane().getTable().getSelectionModel().addListSelectionListener(this);
			getRespDeptPane().getTable().getSelectionModel().addListSelectionListener(this);
			getRespLinePane().getTable().getSelectionModel().addListSelectionListener(this);
			getRespZonePane().getTable().getSelectionModel().addListSelectionListener(this);
		}
	}
	
	protected void deselected(ListSelectionModel model) {
		if(model.equals(getDefectGroupPane().getTable().getSelectionModel())) defectGroupDeselected();
		else if(model.equals(getDefectPane().getTable().getSelectionModel())) defectDeselected();
		
	}
	
	protected void selected(ListSelectionModel model) {
		if(model.equals(getDefectGroupPane().getTable().getSelectionModel())) defectGroupSelected();
		else if(model.equals(getDefectPane().getTable().getSelectionModel())) defectSelected();
		else if(model.equals(getWriteUpDeptPane().getTable().getSelectionModel())) writeUpDeptSelected();
		else if(model.equals(getRespDeptPane().getTable().getSelectionModel())) respDeptSelected();
		else if(model.equals(getRespLinePane().getTable().getSelectionModel())) respLineSelected();
		else if(model.equals(getRespZonePane().getTable().getSelectionModel())) respZoneSelected();
		
	}
	
	protected void defectGroupDeselected() {
		getDefectPane().removeData();
		getDefectImagePanel().clearImage();
		getDefectImagePanel().clearPolygon();
	}
	
	protected void defectGroupSelected() {
		DefectGroup defectGroup = getDefectGroupPane().getSelectedItem();

		getDefectPane().removeData();
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}
		getQicsController().selectDefectTypes(defectGroup.getDefectGroupName());
		getQicsController().selectImage(defectGroup);

		if (getQicsFrame().displayDelayedMessage()){
			
			getDefectImagePanel().clearImage();
			getDefectImagePanel().clearPolygon();

		} else {
			List<DefectTypeDescription> defectTypes = getQicsController().getProductModel().getDistinctDefectTypes();
			getDefectPane().reloadData(defectTypes);
			if (!isImageConfigured())
				return;
			Image image = getQicsController().getClientModel().getImage(defectGroup.getDefectGroupName());
			getDefectImagePanel().setImage(image);
			getDefectImagePanel().setNowImage(image.getImageName());

			if (getDefectPane().getTable().getRowCount() == 1) {
				getDefectPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
			}
			
			getDefectImagePanel().repaint();
			
			getLogger().info("Defect Group " + defectGroup.getDefectGroupName() + " is selected");
		}

	}
	protected void writeUpDeptSelected() {

		String WriteDeptName = null;
		WriteDeptName = getWriteUpDeptPane().getSelectedString();	
		List<DefectResult> newDefects=getQicsController().getProductModel().getNewDefects();
		if (!newDefects.isEmpty()){ 
			DefectResult lastDefect=newDefects.get(newDefects.size()-1); 
			lastDefect.setWriteUpDepartment(WriteDeptName);
		}

	}

	protected void respDeptSelected() {

		getRespLinePane().removeData();
		getRespZonePane().removeData();	
		String respDeptName = null;
		respDeptName = getRespDeptPane().getSelectedString();
		List<Line> lines = new ArrayList<Line>();
		List<Zone> zones = new ArrayList<Zone>();
		List<Division> list = getQicsController().getClientModel().getDepartments();
		String id = QicsUtils.getDepartmentId(list, respDeptName);		
		lines = getQicsController().selectLines(id);
		getRespLinePane().reloadData(lines);
		if (getRespLinePane().getTable().getRowCount() >= 1) 
			getRespLinePane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		zones = getQicsController().selectZones(id);
		getRespZonePane().reloadData(zones);
		if (getRespZonePane().getTable().getRowCount() >= 1)
			getRespZonePane().getTable().getSelectionModel().setSelectionInterval(0, 0);

		List<DefectResult> newDefects=getQicsController().getProductModel().getNewDefects();
		if (!newDefects.isEmpty()){ 
			DefectResult lastDefect=newDefects.get(newDefects.size()-1); 
			lastDefect.setResponsibleDept(respDeptName);
		} 

	}
	
	protected void respLineSelected() {
		String respLineName = null;
		respLineName = getRespLinePane().getSelectedString();	
        List<DefectResult> newDefects=getQicsController().getProductModel().getNewDefects();
        if (!newDefects.isEmpty()) { 
        	DefectResult lastDefect=newDefects.get(newDefects.size()-1); 
        	lastDefect.setResponsibleLine(respLineName);
 		}

	}
	
	protected void respZoneSelected() {
		
		String respZoneName = null;
		respZoneName = getRespZonePane().getSelectedString();		
		List<DefectResult> newDefects=getQicsController().getProductModel().getNewDefects();
		if (!newDefects.isEmpty()){ 
			DefectResult lastDefect=newDefects.get(newDefects.size()-1); 
			lastDefect.setResponsibleZone(respZoneName);
	   } 

	}

	
	protected void defectDeselected() {
		getDefectImagePanel().clearPolygon();
		setButtonsState();
	}
	
	protected void defectSelected() {
		DefectTypeDescription selectedItem = getDefectPane().getSelectedItem();

		List<DefectDescription> defectDescriptions = getQicsController().selectDefectDescriptions(selectedItem.getDefectTypeName());
		setButtonsState();
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}	
		getDefectImagePanel().clearPolygon();
		if (!isImageConfigured())
			return;
		
		getDefectImagePanel().setDispPolygonStatus(true);
		if (!getQicsFrame().displayDelayedMessage())
			getDefectImagePanel().setImageAndDefectTypeName(selectedItem.getDefectTypeName(),defectDescriptions);
		getLogger().info("Defectname " + this.getDefectPane().getName() + " is selected");
		getLogger().info("Defectgroupname " + this.getDefectGroupPane().getName() + " is selected");
		getLogger().info("Defect " + selectedItem.getDefectTypeName() + " is selected");
		
		if (!isImageSectionsConfigured()){
			getDefectImagePanel().clearPolygon();
			String msg = "Please configure previously the area on picture.";
			setWarningMessage(msg);
			setButtonsState();
		}
	}
	
	// =============== factory methods for event handlers
	protected MouseListener createDefectImageClickHandler() {
		MouseListener mouseListener = new MouseAdapter() {
	
			@Override
			public void mouseClicked(MouseEvent e) {
				defectImageClicked();
			}
		};
		return mouseListener;
	}
	
	private void defectImageClicked() {
		try {
			
			getLogger().info("Defect image clicked");
			getQicsFrame().setWaitCursor();
			getQicsFrame().clearMessage();

			DefectResult defectResult = getDefectImagePanel().getDefectResultData();
			String associateNumber = getQicsFrame().getUserId();

			if (defectResult != null) {
				defectResult = setDefectResultData(defectResult);

				getDefectImagePanel().setDefectStatus(defectResult.getDefectStatusValue());

				if(this.getProductModel().isDuplicatedPictureDefect(defectResult)){					
					if (!this.getQicsPropertyBean().isDuplicateDefectAllowed()){
						this.getDefectPane().clearSelection();
						this.defectDeselected();
						this.setErrorMessage("Duplicate defects not allowed.");
						return;
					} 
					
					if (this.getQicsPropertyBean().isDuplicateDefectCheckOn() && 
					!MessageDialog.confirm(this, "Defect already exists. Do you want to enter duplicate entry?")){
						getDefectPane().clearSelection();
						return;
					}
				}
				if (defectResult.isRepairedStatus()|| defectResult.isScrapStatus()) {
					if (getQicsPropertyBean().isOverrideRepairAssociateEnabled()) {

						SelectAssociateNumberDialog dialog = new SelectAssociateNumberDialog(getQicsFrame(), "Enter Repair Associate ID", true);
						dialog.setLocationRelativeTo(getQicsFrame());
						dialog.loadComboBox(getQicsController().getAssociateNumbers().toArray());
						dialog.setVisible(true);
						boolean cancelled = dialog.isCancelled();

						if (cancelled) {
							getDefectPane().clearSelection();
							return;
						} else {
							associateNumber = String.valueOf(dialog.getReturnValue());
							getQicsFrame().getQicsController().cacheAssociateNumber(associateNumber);
						}
					}
					
					DefectRepairResult defectRepairResult = defectResult.getDefectRepairResult();
					Timestamp now = new Timestamp(new Date().getTime());
					defectResult.setRepairTimestamp(now);
					defectResult.setRepairAssociateNo(associateNumber);
					defectRepairResult.setRepairAssociateNo(associateNumber);
					defectRepairResult.setActualTimestamp(now);
					defectRepairResult.setActualProblemName(defectResult.getDefectTypeName());
					defectRepairResult.setRepairDept(getQicsController().getClientModel().getProcessPoint().getDivisionId());
					defectRepairResult.setRepairProcessPointId(getQicsController().getProcessPointId());
					defectRepairResult.setComment("QUICK REPAIR");
				}

				getQicsController().getProductModel().addNewDefect(defectResult);
				if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
					String respDeptTemp=null;
					String respLineTemp=null;
					String respZoneTemp=null;      		   
	
					if (getWriteUpDeptPane().getTable().getRowCount() > 0) {
						if(!StringUtils.isEmpty(getQicsPropertyBean().getDefaultWriteUpDept().trim()))
						{
							getWriteUpDeptPane().setSelectedObjectDataRow(getQicsPropertyBean().getDefaultWriteUpDept().trim());
						}else
						{
							getWriteUpDeptPane().setSelectedObjectDataRow(getClientModel().getProcessPoint().getDivisionName());
						}
						
						int row=getWriteUpDeptPane().getTable().getSelectedRow();
						getWriteUpDeptPane().getTable().scrollRectToVisible(getWriteUpDeptPane().getTable().getCellRect(row, 0, true));	
					}
					if(defectResult.getResponsibleDept()!=null)
						respDeptTemp=defectResult.getResponsibleDept();
					getRespDeptPane().reloadData(getClientModel().getDepartments());
					if ((getRespDeptPane().getTable().getRowCount() > 0)&&(respDeptTemp!=null) ){
						getRespDeptPane().setSelectedObjectDataRow(respDeptTemp);
						int row=getRespDeptPane().getTable().getSelectedRow();
						getRespDeptPane().getTable().scrollRectToVisible(getRespDeptPane().getTable().getCellRect(row, 0, true));
					}
					if(defectResult.getResponsibleLine()!=null)
						respLineTemp=defectResult.getResponsibleLine();
					if ((getRespLinePane().getTable().getRowCount() > 0)&&(respLineTemp!=null)){
						getRespLinePane().setSelectedObjectDataRow(respLineTemp.trim());
						int row=getRespLinePane().getTable().getSelectedRow();
						getRespLinePane().getTable().scrollRectToVisible(getRespLinePane().getTable().getCellRect(row, 0, true));
					}
					if(defectResult.getResponsibleZone()!=null)
						respZoneTemp=defectResult.getResponsibleZone();
					if ((getRespZonePane().getTable().getRowCount() > 0)&&(respZoneTemp!=null) ){
						getRespZonePane().setSelectedObjectDataRow(respZoneTemp.trim());
						int row=getRespZonePane().getTable().getSelectedRow();
						getRespZonePane().getTable().scrollRectToVisible(getRespZonePane().getTable().getCellRect(row, 0, true));
					}
				}

			}

			setButtonsState();
			return;
		} finally {
			getQicsFrame().setDefaultCursor();
		}
	}
	
	private DefectResult setDefectResultData(DefectResult defectResult) {

		defectResult.getId().setProductId(getQicsController().getProductModel().getProduct().getProductId());
		defectResult.getId().setApplicationId(getQicsController().getProcessPointId());
		defectResult.setEntryStation(getClientModel().getProcessPoint().getProcessPointName());
		// Use Entry Department and Write Up Department Name instead of ID
		defectResult.setEntryDept(getClientModel().getProcessPoint().getDivisionName());
		defectResult.setWriteUpDepartment(getClientModel().getProcessPoint().getDivisionName());
		defectResult.setNewDefect(true);
		defectResult.setRepairAssociateNo(null);
		defectResult.setDefectStatus((short)getDefectStatusPanel().getSelectedStatus());
		defectResult.setAssociateNo(getQicsFrame().getUserId());

		if (defectResult.isOutstandingStatus()) {
			defectResult.setOutstandingFlag((short)1);
		} 

		if (getQicsController().getClientModel().getCurrentSchedule() != null) {
			defectResult.setShift(getQicsController().getClientModel().getCurrentSchedule().getId().getShift());
			defectResult.setDate(getQicsController().getClientModel().getCurrentSchedule().getId().getProductionDate());
		}
		defectResult.setActualTimestamp(new Timestamp(new Date().getTime()));
		defectResult.setPartGroupName(getDefectImagePanel().getDefectResultData().getPartGroupName());
		return defectResult;
	}
	
	public void selectFirstDefectGroup(){
		if (getDefectGroupPane().getTable().getRowCount() > 0) {
			getDefectGroupPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}
	
	public boolean isImageConfigured(){
		boolean isImageConfigured = false;
		Image image = getQicsController().getClientModel().getImage(getDefectGroupPane().getSelectedItem().getDefectGroupName());
		if (image==null){
			String msg = "Please configure a picture.";
			setWarningMessage(msg);
			getDefectImagePanel().clearImage();
			getDefectImagePanel().clearPolygon();
		}else{
			isImageConfigured = true;
		}
		return isImageConfigured;
	}
	
	public boolean isImageSectionsConfigured(){
		boolean isSectionsConfigured = false;
		DefectTypeDescription defectTypeDescription = getDefectPane().getSelectedItem();
		if (defectTypeDescription!=null){
			Image image = getQicsController().getClientModel().getImage(defectTypeDescription.getDefectGroupName());
			int locations = 0;
			
			for (ImageSection item : image.getSections()) {
				DefectDescription defectDescription = item.getDefectDescription();
				if(defectDescription != null && defectDescription.getDefectTypeName().equals(defectTypeDescription.getDefectTypeName()))
					locations++;
			}
			isSectionsConfigured = (locations > 0);
		}
		return isSectionsConfigured;
	}
	
	public boolean isDefectMarkedOnImage(){
		boolean isMarkedOnImage =  false;
		
		DefectResult defectResult = getDefectImagePanel().getDefectResultData();
		if (defectResult!=null && getQicsController().getProductModel().getDefects()!=null){
			isMarkedOnImage = this.getProductModel().isDuplicatedPictureDefect(defectResult);
		}
		
		return isMarkedOnImage;
	}
	
	private class MouseSortListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
        	getLogger().info("Selecting first Defect Group after sorting.");
    		selectFirstDefectGroup();
        }
	}
	
	public ObjectTablePane<Division> createWriteUpDeptPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Write Up Dept", "divisionName");
		ObjectTablePane<Division> panel = new ObjectTablePane<Division>(columnMappings.get());
		return panel;
	}
	
	public ObjectTablePane<Division> createRespDeptPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Responsible Dept", "divisionName");
		ObjectTablePane<Division> panel = new ObjectTablePane<Division>(columnMappings.get());
 		return panel;
	}
	
	public ObjectTablePane<Line> createRespLinePane() {
		ColumnMappings columnMappings = ColumnMappings.with("Responsible Line", "lineName");
		ObjectTablePane<Line> panel = new ObjectTablePane<Line>(columnMappings.get());
		return panel;
	}
	
	public ObjectTablePane<Zone> createRespZonePane() {
		ColumnMappings columnMappings = ColumnMappings.with("Responsible Zone", "zoneName");
		ObjectTablePane<Zone> panel = new ObjectTablePane<Zone>(columnMappings.get());
		return panel;
	}

	public ObjectTablePane<Division> getWriteUpDeptPane() {
		return writeUpDeptPane;
	}

	public ObjectTablePane<Division> getRespDeptPane() {
		return respDeptPane;
	}

	public ObjectTablePane<Line> getRespLinePane() {
		return respLinePane;
	}

	public ObjectTablePane<Zone> getRespZonePane() {
		return respZonePane;
	}
	
	

	
}
