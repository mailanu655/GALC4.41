package com.honda.galc.client.teamleader.qics.part;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import com.honda.galc.client.teamleader.qics.combination.PartDefectCombinationPanel;
import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.part.dialog.PartDataDialog;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.util.PropertyComparator;


/**
 * 
 * <h3>PartPanel Class description</h3>
 * <p> PartPanel description </p>
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
 * Oct 31, 2011
 *
 *
 */
public class PartPanel extends QicsMaintenanceTabbedPanel{

	private static final long serialVersionUID = 1L;
	
	private final static String CREATE_PART_GROUP="Create Part Group";
	private final static String DELETE_PART_GROUP="Delete Part Group";
	
	private final static String CREATE_PART="Create Part";
	private final static String DELETE_PART="Delete Part";
	
	private final static String CREATE_PART_LOCATION="Create Part Location";
	private final static String DELETE_PART_LOCATION="Delete Part Location";
	
	private final static String CREATE_PART_DESCRIPTION="Create Part Description";
	private final static String DELETE_PART_DESCRIPTION="Delete Part Description";
	
	private ObjectTablePane<PartGroup> partGroupPanel;
	private ObjectTablePane<InspectionPart> partPanel;
	private ObjectTablePane<InspectionPartLocation> partLocationPanel;

	private ObjectTablePane<InspectionPartDescription> partDescriptionPanel;

	public PartPanel(QicsMaintenanceFrame mainWindow) {
		super("Inspection Part",KeyEvent.VK_P);
		setMainWindow(mainWindow);
	}	
	
	protected void initComponents() {

		setLayout(new GridLayout(1, 1));
		// === create ui fragments === //

		partGroupPanel = createPartGroupPanel();
		partPanel = createPartPanel();
		partLocationPanel = createPartLocationPanel();
		partDescriptionPanel = createPartDescriptionPanel();

		// === add fragments == //
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 3));
		topPanel.add(getPartGroupPanel());
		topPanel.add(getPartPanel());
		topPanel.add(getPartLocationPanel());
		// add(topPanel);

		JPanel assignedPanel = new JPanel();
		assignedPanel.setLayout(new GridLayout(1, 1));
		assignedPanel.add(getPartDescriptionPanel());

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, assignedPanel);
		splitPane.setOneTouchExpandable(true);
		// splitPane.setDividerLocation(0.5);
		splitPane.setDividerLocation(300);
		add(splitPane);
		// == init data === //
		List<PartGroup> partGroups = getClientModel().getPartGroups();
		Collections.sort(partGroups, new PropertyComparator<PartGroup>(PartGroup.class, "partGroupName"));
		getPartGroupPanel().reloadData(partGroups);

		List<InspectionPart> parts = getClientModel().getParts();
		Collections.sort(parts, new PropertyComparator<InspectionPart>(InspectionPart.class, "inspectionPartName"));
		getPartPanel().reloadData(parts);

		List<InspectionPartLocation> partLocations = getClientModel().getPartLocations();
		Collections.sort(partLocations, new PropertyComparator<InspectionPartLocation>(InspectionPartLocation.class, "inspectionPartLocationName"));
		getPartLocationPanel().reloadData(partLocations);

		mapActions();
		mapHandlers();
	}

	protected void mapActions() {

	}

	protected void mapHandlers() {
		
		getPartGroupPanel().getTable().getSelectionModel().addListSelectionListener(this);
		getPartPanel().getTable().getSelectionModel().addListSelectionListener(this);
		getPartLocationPanel().getTable().getSelectionModel().addListSelectionListener(this);

		MouseListener partGroupMouseListener = createPartGroupMouseListener();
		getPartGroupPanel().addMouseListener(partGroupMouseListener);
		getPartGroupPanel().getTable().addMouseListener(partGroupMouseListener);

		MouseListener partMouseListener = createPartMouseListener();
		getPartPanel().addMouseListener(partMouseListener);
		getPartPanel().getTable().addMouseListener(partMouseListener);

		MouseListener partLocationMouseListener = createPartLocationMouseListener();
		getPartLocationPanel().addMouseListener(partLocationMouseListener);
		getPartLocationPanel().getTable().addMouseListener(partLocationMouseListener);

		getPartDescriptionPanel().getTable().addMouseListener(createPartDescriptionMouseListener()); 
		
	}
	
	private MouseListener createPartGroupMouseListener(){
		 return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showPartGroupPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showPartGroupPopupMenu(e);
			}			
		 };
	}
	
	private MouseListener createPartMouseListener(){
		 return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showPartPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showPartPopupMenu(e);
			}			
		 };
	}
	
	private MouseListener createPartLocationMouseListener(){
		 return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showPartLocationPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showPartLocationPopupMenu(e);
			}			
		 };
	}
	
	private MouseListener createPartDescriptionMouseListener(){
		 return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showPartDescriptionPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showPartDescriptionPopupMenu(e);
			}			
		 };
	}


	// === factory methods === //
	protected ObjectTablePane<PartGroup> createPartGroupPanel() {
		
		ColumnMappings columnMappings = ColumnMappings.with("#", "row").put("Part Group", "partGroupName");
		
		ObjectTablePane<PartGroup> pane = new ObjectTablePane<PartGroup>(columnMappings.get(),true);
//		pane.setColumnWidths(new int[] { 30, 300 });
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return pane;
	}

	protected ObjectTablePane<InspectionPart> createPartPanel() {

		ColumnMappings columnMappings = ColumnMappings.with("#", "row").put("Part", "inspectionPartName");
		
		ObjectTablePane<InspectionPart> pane = new ObjectTablePane<InspectionPart>(columnMappings.get(),true);
//		
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		pane.setColumnWidths(new int[] { 30, 300 });
		return pane;
	}

	protected ObjectTablePane<InspectionPartLocation> createPartLocationPanel() {
		
		ColumnMappings columnMappings = ColumnMappings.with("#", "row").put("Part Location", "inspectionPartLocationName");
		
		ObjectTablePane<InspectionPartLocation> pane = new ObjectTablePane<InspectionPartLocation>(columnMappings.get(),true);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		pane.setColumnWidths(new int[] { 30, 300 });
		return pane;

	}

	protected ObjectTablePane<InspectionPartDescription> createPartDescriptionPanel() {
		
		ColumnMappings columnMappings = 
			ColumnMappings.with("#", "row").put("Part Group", "partGroupName")
			              .put("Part", "inspectionPartName").put("Location", "inspectionPartLocationName");
		
		ObjectTablePane<InspectionPartDescription> pane = new ObjectTablePane<InspectionPartDescription>(columnMappings.get(),true,true);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		pane.setColumnWidths(new int[] { 30, 330, 330, 330 });
		
		return pane;

		
		
	}

	protected void showPartGroupPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_PART_GROUP,true));
		popupMenu.add(createMenuItem(DELETE_PART_GROUP,true));
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem(CREATE_PART_DESCRIPTION,!createNewPartDescription().isEmpty()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	protected void showPartPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_PART,true));
		popupMenu.add(createMenuItem(DELETE_PART,true));
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem(CREATE_PART_DESCRIPTION,!createNewPartDescription().isEmpty()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	protected void showPartLocationPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_PART_LOCATION,true));
		popupMenu.add(createMenuItem(DELETE_PART_LOCATION,true));
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem(CREATE_PART_DESCRIPTION,!createNewPartDescription().isEmpty()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	

	protected void showPartDescriptionPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		boolean selected = getPartDescriptionPanel().getTable().getSelectedRowCount() > 0;
		popupMenu.add(createMenuItem(DELETE_PART_DESCRIPTION,selected));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	// === get/set === //
	public ObjectTablePane<PartGroup> getPartGroupPanel() {
		return partGroupPanel;
	}

	public ObjectTablePane<InspectionPartLocation> getPartLocationPanel() {
		return partLocationPanel;
	}

	public ObjectTablePane<InspectionPart> getPartPanel() {
		return partPanel;
	}

	public ObjectTablePane<InspectionPartDescription> getPartDescriptionPanel() {
		return partDescriptionPanel;
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		initComponents();
		isInitialized = true;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem menuItem = (JMenuItem)e.getSource();
		Exception exception = null;
		try{
			if(menuItem.getName().equals(CREATE_PART_GROUP)) createPartGroup();
			else if(menuItem.getName().equals(DELETE_PART_GROUP)) deletePartGroup();
			else if(menuItem.getName().equals(CREATE_PART)) createPart();
			else if(menuItem.getName().equals(DELETE_PART)) deletePart();
			else if(menuItem.getName().equals(CREATE_PART_LOCATION)) createPartLocation();
			else if(menuItem.getName().equals(DELETE_PART_LOCATION)) deletePartLocation();
			else if(menuItem.getName().equals(CREATE_PART_DESCRIPTION)) createPartDescription();
			else if(menuItem.getName().equals(DELETE_PART_DESCRIPTION)) deletePartDescription();
		}catch(Exception ex){
			exception = ex;
		}
		handleException(exception);
		
	}
	
	private void createPartGroup() {
		List<String> modelCodes = getClientModel().getModels();
		PartDataDialog dialog = new PartDataDialog(getMainWindow(), "Part Group", modelCodes);
		if(!dialog.isDataCreated()) return;
		// Display confirmation popup if user creates Part Group with an existing Part Group Name
		boolean found = false;
		for (PartGroup partGroup : getClientModel().getPartGroups()) {
			if (partGroup.getPartGroupName().equals(
					dialog.getPartGroup().getPartGroupName())) {
				found = true;
				break;
			}
		}
		
		if (found) {
			if (MessageDialog
					.confirm(this,
							"The Entered Part Group Name already exits. Do you wish to update it?")) {
				getController().createPartGroup(dialog.getPartGroup());
			} else {
				return;
			}
		} else {
			getController().createPartGroup(dialog.getPartGroup());
		}

		List<PartGroup> partGroups = getController().selectPartGroups();
		getClientModel().setPartGroups(partGroups);
		Collections.sort(partGroups, new PropertyComparator<PartGroup>(PartGroup.class, "partGroupName"));
		getPartGroupPanel().reloadData(partGroups);
	}
	
	private void createPart() {
		PartDataDialog dialog = new PartDataDialog(getMainWindow(), "Inspection Part");
		if(!dialog.isDataCreated()) return;
		getController().createPart(dialog.getInspectionPart());

		List<InspectionPart> inpsectionParts = getController().selectParts();
		getClientModel().setParts(inpsectionParts);
		Collections.sort(inpsectionParts, new PropertyComparator<InspectionPart>(InspectionPart.class, "inspectionPartName"));
		getPartPanel().reloadData(inpsectionParts);
	}
	
	private void createPartLocation() {
		PartDataDialog dialog = new PartDataDialog(getMainWindow(), "Part Location");
		if(!dialog.isDataCreated()) return;
		getController().createPartLocation(dialog.getPartLocation());

		List<InspectionPartLocation> inpsectionPartLocations = getController().selectPartLocations();
		getClientModel().setPartLocations(inpsectionPartLocations);
		Collections.sort(inpsectionPartLocations, new PropertyComparator<InspectionPartLocation>(InspectionPartLocation.class, "inspectionPartLocationName"));
		getPartLocationPanel().reloadData(inpsectionPartLocations);
	}
	
	private void createPartDescription() {
		List<InspectionPartDescription> partDescriptions = createNewPartDescription();

		if (partDescriptions.isEmpty()) return;

		try{
			getController().createPartDescriptions(partDescriptions);
		}finally{
			partDataSelected();
		}

		PartDefectCombinationPanel combinationPanel = (PartDefectCombinationPanel)getMainWindow().getTabbedPanel(PartDefectCombinationPanel.class);
		if (combinationPanel != null)
			combinationPanel.getPartGroupPane().clearSelection();
	}
	
	private List<InspectionPartDescription> createNewPartDescription() {
		List<InspectionPartDescription> partDescriptions = new ArrayList<InspectionPartDescription>();

		List<PartGroup> partGroups = getPartGroupPanel().getSelectedItems();
		List<InspectionPart> parts = getPartPanel().getSelectedItems();
		List<InspectionPartLocation> partLocations = getPartLocationPanel().getSelectedItems();

		if ((partGroups == null || partGroups.isEmpty()) || (parts == null || parts.isEmpty()) || (partLocations == null || partLocations.isEmpty())) {
			return partDescriptions;
		}

		List<InspectionPartDescription> existingPartDescriptions = getPartDescriptionPanel().getItems();

		for (PartGroup partGroup : partGroups) {
			for (InspectionPart part : parts) {
				for (InspectionPartLocation partLocation : partLocations) {
					InspectionPartDescription description = new InspectionPartDescription();
					description.setPartGroupName(partGroup.getPartGroupName());
					description.setInspectionPartName(part.getInspectionPartName());
					description.setInspectionPartLocationName(partLocation.getInspectionPartLocationName());
					if (!existingPartDescriptions.contains(description))
						partDescriptions.add(description);
				}
			}
		}
		return partDescriptions;
	}
	
	private void deletePartGroup() {
		PartGroup partGroup = getPartGroupPanel().getSelectedItem();
		if (partGroup == null) return;

		if (getPartDescriptionPanel().getTable().getRowCount() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Part Group can not be deleted \n It is used in Part Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		InspectionPartDescription criteria = new InspectionPartDescription();
		criteria.setPartGroupName(partGroup.getPartGroupName());
		List<InspectionPartDescription> list = getController().selectInspectionPartDescriptions(criteria);

		if (list.size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Part Group can not be deleted \n It is used in Part Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Part Group", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) {
			return;
		}
		getController().deletePartGroup(partGroup);

		List<PartGroup> partGroups = getController().selectPartGroups();
		getClientModel().setPartGroups(partGroups);
		Collections.sort(partGroups, new PropertyComparator<PartGroup>(PartGroup.class, "partGroupName"));
		getPartGroupPanel().reloadData(partGroups);
	}
	
	private void deletePart(){
		InspectionPart part = getPartPanel().getSelectedItem();
		if (part == null)return;

		if (getPartDescriptionPanel().getTable().getRowCount() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Inspection Part can not be deleted \n It is used in Part Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		InspectionPartDescription criteria = new InspectionPartDescription();
		criteria.setInspectionPartName(part.getInspectionPartName());
		List<InspectionPartDescription> list = getController().selectInspectionPartDescriptions(criteria);

		if (list.size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Inspection Part can not be deleted \n It is used in Part Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Inspection Part", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) return;

		getController().deletePart(part);

		List<InspectionPart> parts = getController().selectParts();
		getClientModel().setParts(parts);
		Collections.sort(parts, new PropertyComparator<InspectionPart>(InspectionPart.class, "inspectionPartName"));
		getPartPanel().reloadData(parts);
	}
	
	private void deletePartLocation(){
		InspectionPartLocation partLocation = getPartLocationPanel().getSelectedItem();
		if (partLocation == null) return;

		if (getPartDescriptionPanel().getTable().getRowCount() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Inspection Part Location can not be deleted \n It is used in Part Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		InspectionPartDescription criteria = new InspectionPartDescription();
		criteria.setInspectionPartLocationName(partLocation.getInspectionPartLocationName());
		List<InspectionPartDescription> list = getController().selectInspectionPartDescriptions(criteria);

		if (list.size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Inspection Part Location can not be deleted \n It is used in Part Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Inspection Part Location", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) return;
		getController().deletePartLocation(partLocation);

		List<InspectionPartLocation> partLocations = getController().selectPartLocations();
		getClientModel().setPartLocations(partLocations);
		Collections.sort(partLocations, new PropertyComparator<InspectionPartLocation>(InspectionPartLocation.class, "name"));
		getPartLocationPanel().reloadData(partLocations);
	}
	
	private void deletePartDescription() {
		List<InspectionPartDescription> selectedValues = getPartDescriptionPanel().getSelectedItems();
		if (selectedValues == null || selectedValues.isEmpty()) return;

		List<DefectDescription> list = getController().selectDefectDescriptions(selectedValues);

		if (list != null && !list.isEmpty()) {

			List<InspectionPartDescription> pdList = new ArrayList<InspectionPartDescription>();
			for (DefectDescription dd : list) {
				InspectionPartDescription pd = new InspectionPartDescription();
				pd.setInspectionPartName(dd.getInspectionPartName());
				pd.setPartGroupName(dd.getPartGroupName());
				pd.setInspectionPartLocationName(dd.getId().getInspectionPartLocationName());
				if (!pdList.contains(pd)) {
					pdList.add(pd);
				}
			}

			String msg = "Some of Part Descriptions are associated with Defect Descriptions and can not be deleted";

			for (InspectionPartDescription pd : pdList) {
				msg = msg + "\n" + pd.getPartGroupName().trim() + ", " + pd.getInspectionPartName().trim() + ", " + pd.getInspectionPartLocationName().trim();
			}

			JOptionPane.showMessageDialog(getMainWindow(), msg, "Delete Part Description", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Part Description", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION != ret) return;

		try{
			getController().deletePartDescriptions(selectedValues);
		}finally {
			partDataSelected();
		}

	}
	
	@Override
	public void selected(ListSelectionModel model) {
		if(model.equals(getPartGroupPanel().getTable().getSelectionModel())) partDataSelected();
		else if(model.equals(getPartPanel().getTable().getSelectionModel())) partDataSelected();
		else if(model.equals(getPartLocationPanel().getTable().getSelectionModel())) partDataSelected();
	}
	
	@Override
	public void deselected(ListSelectionModel model) {
		if(model.equals(getPartGroupPanel().getTable().getSelectionModel())) partGroupDeselected();
		else if(model.equals(getPartPanel().getTable().getSelectionModel())) partDeselected();
		else if(model.equals(getPartLocationPanel().getTable().getSelectionModel())) partLocationDeselected();
	}
	
	private void partDataSelected() {
		PartGroup partGroup = getPartGroupPanel().getSelectedItem();
		List<InspectionPart> parts = getPartPanel().getSelectedItems();
		List<InspectionPartLocation> locations = getPartLocationPanel().getSelectedItems();

		getPartDescriptionPanel().removeData();
		if (partGroup == null && (parts == null || parts.isEmpty()) && (locations == null || locations.isEmpty())) {
			return;
		}
		if(parts.isEmpty()) parts.add(null);
		if(locations.isEmpty()) locations.add(null);
		
		List<InspectionPartDescription> list = new ArrayList<InspectionPartDescription>();

		for (InspectionPart part : parts) {
			for (InspectionPartLocation location : locations) {
				InspectionPartDescription criteria = new InspectionPartDescription();
				if (partGroup != null) 
					criteria.setPartGroupName(partGroup.getPartGroupName());
				if (part != null) 
					criteria.setInspectionPartName(part.getInspectionPartName());
				if (location != null) 
					criteria.setInspectionPartLocationName(location.getInspectionPartLocationName());
				List<InspectionPartDescription> items = getController().selectInspectionPartDescriptions(criteria);
				if (items == null) continue;
				else list.addAll(items);
			}
		}
		Collections.sort(list, new PropertyComparator<InspectionPartDescription>(InspectionPartDescription.class, "partGroupName", "inspectionPartName", "inspectionPartLocationName"));
		getPartDescriptionPanel().reloadData(list);
	}
	
	private void partGroupDeselected(){
		if (getPartPanel().getTable().getSelectedRowCount() > 0) {
			getPartPanel().clearSelection();
		} else if (getPartLocationPanel().getTable().getSelectedRowCount() > 0) {
			getPartLocationPanel().clearSelection();
		} else {
			getPartDescriptionPanel().removeData();
		}
	}
	
	private void partDeselected() {
		if (getPartLocationPanel().getTable().getSelectedRowCount() > 0) {
			getPartLocationPanel().clearSelection();
		} else {
			getPartDescriptionPanel().removeData();
			partDataSelected();
		}
	}
	
	private void partLocationDeselected() {
		getPartDescriptionPanel().removeData();
		partDataSelected();
	}
	
}
