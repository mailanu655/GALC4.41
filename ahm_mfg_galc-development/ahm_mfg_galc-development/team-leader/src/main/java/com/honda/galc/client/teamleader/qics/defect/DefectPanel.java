package com.honda.galc.client.teamleader.qics.defect;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qics.combination.PartDefectCombinationPanel;
import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.image.ImageSectionPanel;
import com.honda.galc.client.teamleader.qics.part.dialog.PartDataDialog;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.SecondaryPart;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>DefectPanel</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jul 28, 2009</TD>
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
public class DefectPanel extends QicsMaintenanceTabbedPanel {

	private static final long serialVersionUID = 1L;
	
	private final static String CREATE_DEFECT_GROUP="Create Defect Group";
	private final static String DELETE_DEFECT_GROUP="Delete Defect Group";
	
	private final static String CREATE_DEFECT="Create Defect";
	private final static String DELETE_DEFECT="Delete Defect";
	
	private final static String CREATE_SECONDARY_PART="Create Secondary Part";
	private final static String DELETE_SECONDARY_PART="Delete Secondary Part";
	
	private final static String CREATE_DEFECT_DESCRIPTION="Create Defect Description";
	private final static String DELETE_DEFECT_DESCRIPTION="Delete Defect Description";
	

	private long imagesSelectedTime;

	private JLabel imagesLabel;
	private JComboBox images;
	private ObjectTablePane<DefectGroup> defectGroupPanel;
	private ObjectTablePane<DefectType> defectPanel;
	private ObjectTablePane<SecondaryPart> secondaryPartPanel;

	private ObjectTablePane<DefectTypeDescription> defectDescriptionPanel;

	public DefectPanel(QicsMaintenanceFrame mainWindow) {
		super("Defect",KeyEvent.VK_D);
		setMainWindow(mainWindow);
	}

	protected void initComponents() {

		setLayout(new GridLayout(1, 1));
		// === create ui fragments === //

		imagesLabel = createImagesLabel();
		images = createImages();

		defectGroupPanel = createDefectGroupPanel();
		defectPanel = createDefectPanel();
		secondaryPartPanel = createSecondaryPartPanel();
		defectDescriptionPanel = createDefectDescriptionPanel();

		// === add fragments == //
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 3));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;

		panel.add(getImagesLabel(), c);
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.8;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 0, 5, 0);
		panel.add(getImages(), c);
		c = new GridBagConstraints();
		c.gridheight = 5;
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		panel.add(getDefectGroupPanel(), c);

		topPanel.add(panel);

		topPanel.add(getDefectPanel());
		topPanel.add(getSecondaryPartPanel());

		JPanel assignedPanel = new JPanel();
		assignedPanel.setLayout(new GridLayout(1, 1));
		assignedPanel.add(getDefectDescriptionPanel());

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, assignedPanel);
		splitPane.setOneTouchExpandable(true);

		splitPane.setDividerLocation(300);
		add(splitPane);
		// == init data === //

		List<Image> images = getClientModel().getImages();
		setImagesSelectedTime(getClientModel().getImagesUpdateTime());
		Collections.sort(images, new PropertyComparator<Image>(Image.class, "imageName"));
		images.add(0, null);
		ComboBoxUtils.loadComboBox(getImages(), images);

		List<DefectGroup> defectGroups = getClientModel().getDefectGroups();
		Collections.sort(defectGroups, new PropertyComparator<DefectGroup>(DefectGroup.class, "defectGroupName"));
		getDefectGroupPanel().reloadData(defectGroups);

		List<DefectType> defects = getClientModel().getDefectTypes();
		Collections.sort(defects, new PropertyComparator<DefectType>(DefectType.class, "defectTypeName"));
		getDefectPanel().reloadData(defects);

		List<SecondaryPart> secondaryParts = getClientModel().getSecondaryParts();
		Collections.sort(secondaryParts, new PropertyComparator<SecondaryPart>(SecondaryPart.class, "secondaryPartName"));
		getSecondaryPartPanel().reloadData(secondaryParts);

		mapActions();
		mapHandlers();
	}

	@Override
	public void onTabSelected() {
				
		if(isInitialized) 
		{	//  Image drop down is not getting refreshed 
			if (getClientModel().isImagesUpdated(getImagesSelectedTime())) {
				List<Image> images = new ArrayList<Image>(getClientModel().getImages());
				setImagesSelectedTime(getClientModel().getImagesUpdateTime());
				Collections.sort(images, new PropertyComparator<Image>(Image.class, "imageName"));
				images.add(0, null);
				ComboBoxUtils.loadComboBox(getImages(), images);
			}
			return;
		}
		initComponents();
		
		isInitialized = true;
	}

	protected void mapActions() {

	}

	protected void mapHandlers() {

		getImages().addActionListener(this);
		getDefectGroupPanel().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectPanel().getTable().getSelectionModel().addListSelectionListener(this);
		getSecondaryPartPanel().getTable().getSelectionModel().addListSelectionListener(this);

		MouseListener defectGroupMouseListener = createDefectGroupMouseListener();
		getDefectGroupPanel().addMouseListener(defectGroupMouseListener);
		getDefectGroupPanel().getTable().addMouseListener(defectGroupMouseListener);
		
		MouseListener defectMouseListener = createDefectMouseListener();
		getDefectPanel().addMouseListener(defectMouseListener);
		getDefectPanel().getTable().addMouseListener(defectMouseListener);
		
		MouseListener secondaryPartMouseListener = createSecondaryPartMouseListener();
		getSecondaryPartPanel().addMouseListener(secondaryPartMouseListener);
		getSecondaryPartPanel().getTable().addMouseListener(secondaryPartMouseListener);

		getDefectDescriptionPanel().getTable().addMouseListener(createDefectDescriptionMouseListener());

	}
	
	private MouseListener createDefectGroupMouseListener(){
		 return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showDefectGroupPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showDefectGroupPopupMenu(e);
			}
		 };
	}

	private MouseListener createDefectMouseListener(){
		 return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showDefectPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showDefectPopupMenu(e);
			}			
		 };
	}

	private MouseListener createSecondaryPartMouseListener(){
		 return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showSecondaryPartPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showSecondaryPartPopupMenu(e);
			}			
		 };
	}
	
	private MouseListener createDefectDescriptionMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showDefectDescriptionPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showDefectDescriptionPopupMenu(e);
			}			
		 };
	}
	
	private void showDefectGroupPopupMenu(MouseEvent e) {
		boolean imageSelected = getImages().getSelectedItem() != null;
		boolean partGroupSingleSelected = getDefectGroupPanel().getTable().getSelectedRowCount() == 1;
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_DEFECT_GROUP,imageSelected));
		popupMenu.add(createMenuItem(DELETE_DEFECT_GROUP,partGroupSingleSelected));
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem(CREATE_DEFECT_DESCRIPTION,isCreateDefectTypeDescription()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void showDefectPopupMenu(MouseEvent e) {
		boolean partSingleSelected = getDefectPanel().getTable().getSelectedRowCount() == 1;
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_DEFECT,true));
		popupMenu.add(createMenuItem(DELETE_DEFECT,partSingleSelected));
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem(CREATE_DEFECT_DESCRIPTION,isCreateDefectTypeDescription()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void showSecondaryPartPopupMenu(MouseEvent e) {
		boolean partLocationSingleSelected = getSecondaryPartPanel().getTable().getSelectedRowCount() == 1;
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_SECONDARY_PART,true));
		popupMenu.add(createMenuItem(DELETE_SECONDARY_PART,partLocationSingleSelected));
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem(CREATE_DEFECT_DESCRIPTION,isCreateDefectTypeDescription()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private void showDefectDescriptionPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		boolean selected = getDefectDescriptionPanel().getTable().getSelectedRowCount() > 0;
		popupMenu.add(createMenuItem(DELETE_DEFECT_DESCRIPTION,selected));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	public boolean isCreateDefectTypeDescription() {
		boolean partGroupSelected = getDefectGroupPanel().getTable().getSelectedRowCount() > 0;
		boolean partSelected = getDefectPanel().getTable().getSelectedRowCount() > 0;
		boolean partLocationSelected = getSecondaryPartPanel().getTable().getSelectedRowCount() > 0;
		boolean createPartDescription = !createNewDefectTypeDescriptions().isEmpty();
		return partGroupSelected && partSelected && partLocationSelected && createPartDescription;
	}

	// === factory methods === //
	protected JLabel createImagesLabel() {
		JLabel component = createLabel("Image");
		return component;
	}

	protected JLabel createLabel(String text) {
		JLabel component = new JLabel(text, JLabel.CENTER);
		component.setFont(Fonts.DIALOG_BOLD_12);
		return component;
	}

	protected JComboBox createImages() {
		JComboBox comboBox = new JComboBox();
		ListCellRenderer tableCellRenderer = new PropertyComboBoxRenderer<Image>(Image.class, "imageName");
		((JLabel) tableCellRenderer).setHorizontalAlignment(SwingConstants.CENTER);
		comboBox.setRenderer(tableCellRenderer);
		comboBox.setFont(Fonts.DIALOG_BOLD_12);
		return comboBox;
	}

	protected ObjectTablePane<DefectGroup> createDefectGroupPanel() {
		
		ColumnMappings clumnMappings = ColumnMappings.with("#", "row").put("Defect Group", "defectGroupName");
		
		ObjectTablePane<DefectGroup> pane = new ObjectTablePane<DefectGroup>(clumnMappings.get(),true);
	//	pane.setColumnWidths(new int[] { 30, 300 });
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return pane;
	}

	protected ObjectTablePane<DefectType> createDefectPanel() {
		
		ColumnMappings clumnMappings = ColumnMappings.with("#", "row").put("Defect", "defectTypeName");
		
		ObjectTablePane<DefectType> pane = new ObjectTablePane<DefectType>(clumnMappings.get(),true);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		pane.setColumnWidths(new int[] { 30, 300 });
		return pane;
	}

	protected ObjectTablePane<SecondaryPart> createSecondaryPartPanel() {
		
		ColumnMappings clumnMappings = ColumnMappings.with("#", "row").put("Secondary Part", "secondaryPartName");
		
		ObjectTablePane<SecondaryPart> pane = new ObjectTablePane<SecondaryPart>(clumnMappings.get(),true);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		pane.setColumnWidths(new int[] { 30, 300 });
		return pane;
	}

	protected ObjectTablePane<DefectTypeDescription> createDefectDescriptionPanel() {
		
		ColumnMappings clumnMappings = 
			ColumnMappings.with("#", "row").put("Defect Group", "defectGroupName")
						  .put("Defect", "defectTypeName").put("Secondary Part", "secondaryPartName");
		ObjectTablePane<DefectTypeDescription> pane = new ObjectTablePane<DefectTypeDescription>(clumnMappings.get(),true,true);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		pane.setColumnWidths(new int[] { 30, 330, 330, 330 });
		return pane;
	}

	// === get/set === //
	public ObjectTablePane<DefectGroup> getDefectGroupPanel() {
		return defectGroupPanel;
	}

	public ObjectTablePane<SecondaryPart> getSecondaryPartPanel() {
		return secondaryPartPanel;
	}

	public ObjectTablePane<DefectType> getDefectPanel() {
		return defectPanel;
	}

	public ObjectTablePane<DefectTypeDescription> getDefectDescriptionPanel() {
		return defectDescriptionPanel;
	}

	public JComboBox getImages() {
		return images;
	}

	public JLabel getImagesLabel() {
		return imagesLabel;
	}

	public long getImagesSelectedTime() {
		return imagesSelectedTime;
	}

	public void setImagesSelectedTime(long imagesSelectedTime) {
		this.imagesSelectedTime = imagesSelectedTime;
	}

	@Override
	public void deselected(ListSelectionModel model) {
		if(model.equals(getDefectGroupPanel().getTable().getSelectionModel())) defectGroupDeselected();
		else if(model.equals(getDefectPanel().getTable().getSelectionModel())) defectDeselected();
		else if(model.equals(getSecondaryPartPanel().getTable().getSelectionModel())) secondaryPartDeselected();
	}

	@Override
	public void selected(ListSelectionModel model) {
		if(model.equals(getDefectGroupPanel().getTable().getSelectionModel())) defectDataSelected();
		else if(model.equals(getDefectPanel().getTable().getSelectionModel())) defectDataSelected();
		else if(model.equals(getSecondaryPartPanel().getTable().getSelectionModel())) defectDataSelected();
	}
	
	private void defectDataSelected() {
		
		getDefectDescriptionPanel().removeData();

		List<DefectTypeDescription> defectTypeItems = fetchDefectTypeDescriptions();
		if(defectTypeItems.isEmpty()) return;

		DefectGroup defectGroup = getDefectGroupPanel().getSelectedItem();
		List<DefectType> defects = getDefectPanel().getSelectedItems();
		List<SecondaryPart> secondaryParts = getSecondaryPartPanel().getSelectedItems();

		List<DefectTypeDescription> list = new ArrayList<DefectTypeDescription>();

		for(DefectTypeDescription item : defectTypeItems) {
			if((defectGroup == null || defectGroup.getDefectGroupName().equals(item.getDefectGroupName()))
				&&(defects.isEmpty() || containsDefect(defects,item.getDefectTypeName())) 
				&& (secondaryParts.isEmpty() || containsSecondaryPart(secondaryParts,item.getSecondaryPartName())))
				list.add(item);
		}
		Collections.sort(list, new PropertyComparator<DefectTypeDescription>(DefectTypeDescription.class, "defectGroupName", "defectTypeName", "secondaryPartName"));
		getDefectDescriptionPanel().reloadData(list);
	}
	
	private List<DefectTypeDescription> fetchDefectTypeDescriptions() {
		DefectGroup defectGroup = getDefectGroupPanel().getSelectedItem();
		List<DefectType> defects = getDefectPanel().getSelectedItems();
		List<SecondaryPart> secondaryParts = getSecondaryPartPanel().getSelectedItems();

		List<DefectTypeDescription> defectTypeDescriptions = new ArrayList<DefectTypeDescription>();
		
		DefectTypeDescription criteria = new DefectTypeDescription();
		if(defectGroup != null) {
			criteria.setDefectGroupName(defectGroup.getDefectGroupName());
			return getController().selectDefectTypeDescriptions(criteria);
		}else if(!defects.isEmpty()) {
			for(DefectType item : defects) {
				criteria.setDefectTypeName(item.getDefectTypeName());
				defectTypeDescriptions.addAll(getController().selectDefectTypeDescriptions(criteria));
			}
		}else if(!secondaryParts.isEmpty()) {
			for(SecondaryPart item : secondaryParts) {
				criteria.setSecondaryPartName(item.getSecondaryPartName());
				defectTypeDescriptions.addAll(getController().selectDefectTypeDescriptions(criteria));
			}
		}
		return defectTypeDescriptions;
	
	}
	
	private boolean containsDefect(List<DefectType> defectTypes, String defectType) {
		for(DefectType item : defectTypes) {
			if(item.getDefectTypeName().equals(defectType)) return true;
		}
		return false;
	}
	
	private boolean containsSecondaryPart(List<SecondaryPart> secondaryParts, String secondaryPart) {
		for(SecondaryPart item : secondaryParts) {
			if(item.getSecondaryPartName().equals(secondaryPart)) return true;
		}
		return false;
	}
	
	private void defectGroupDeselected() {
		if (getDefectPanel().getTable().getSelectedRowCount() > 0) 
			getDefectPanel().clearSelection();
		else if (getSecondaryPartPanel().getTable().getSelectedRowCount() > 0) 
			getSecondaryPartPanel().clearSelection();
		else 
			getDefectDescriptionPanel().removeData();
	}
	
	private void defectDeselected() {
		if (getSecondaryPartPanel().getTable().getSelectedRowCount() > 0) 
			getSecondaryPartPanel().clearSelection();
		else{
			getDefectDescriptionPanel().removeData();
			defectDataSelected();
		}
	}	

	private void secondaryPartDeselected() {
		getDefectDescriptionPanel().removeData();
		defectDataSelected();
	}
	
	public void actionPerformed(ActionEvent e) {
		Exception exception = null;

		try{
			if(e.getSource()== getImages()){
				imageSelected();
				return;
			}
			JMenuItem menuItem = (JMenuItem)e.getSource();
			if(menuItem != null && !StringUtils.isEmpty(menuItem.getName())) {
				logUserAction("selected menu item: " + menuItem.getName());
			}
			if(menuItem.getName().equals(CREATE_DEFECT_GROUP)) createDefectGroup();
			else if(menuItem.getName().equals(DELETE_DEFECT_GROUP)) deleteDefectGroup();
			else if(menuItem.getName().equals(CREATE_DEFECT)) createDefect();
			else if(menuItem.getName().equals(DELETE_DEFECT)) deleteDefect();
			else if(menuItem.getName().equals(CREATE_SECONDARY_PART)) createSecondaryPart();
			else if(menuItem.getName().equals(DELETE_SECONDARY_PART)) deleteSecondaryPart();
			else if(menuItem.getName().equals(CREATE_DEFECT_DESCRIPTION)) createDefectDescription();
			else if(menuItem.getName().equals(DELETE_DEFECT_DESCRIPTION)) deleteDefectDescription();
		}catch(Exception ex){
			exception = ex;
		}
		handleException(exception);
	}
	
	
	private void imageSelected() {
		List<DefectGroup> defectGroups = null;

		Object o = getImages().getSelectedItem();

		if (o == null)
			defectGroups = getClientModel().getDefectGroups();
		else {
			defectGroups = new ArrayList<DefectGroup>();
			Image image = (Image) o;
			logUserAction("selected", o.toString());
			for (DefectGroup dg : getClientModel().getDefectGroups()) {
				if (dg == null) continue;
				if (image.getImageName().trim().equals(dg.getImageName().trim())) {
					defectGroups.add(dg);
				}
			}
		}

		Collections.sort(defectGroups, new PropertyComparator<DefectGroup>(DefectGroup.class, "defectGroupName"));
		getDefectGroupPanel().reloadData(defectGroups);
	}
	

	private void createDefectGroup() {
		List<String> modelCodes = getClientModel().getModels();
		DefectGroup defectGroup = defectGroupPanel.getSelectedItem();
		PartDataDialog dialog = new PartDataDialog(getMainWindow(), "Defect Group",
				defectGroup == null? null: defectGroup.getDefectGroupName(),
				defectGroup == null? null: defectGroup.getDefectGroupDescriptionShort(),
				defectGroup == null? null: defectGroup.getDefectGroupDescriptionLong(),
				defectGroup == null? null: defectGroup.getModelCode(),modelCodes);
		if(!dialog.isDataCreated()) return;
		Image image = (Image) getImages().getSelectedItem();
		String imageName = image == null ? "" : image.getImageName();

		// Display confirmation popup if user creates Defect Group with an existing Defect Group Name
		boolean found = false;
		for (DefectGroup defectGrp : getClientModel().getDefectGroups()) {
			if (defectGrp.getDefectGroupName().equals(
					dialog.getDefectGroup(imageName).getDefectGroupName())) {
				found = true;
				break;
			}
		}
		
		if (found) {
			if (MessageDialog
					.confirm(this,
							"The Entered Defect Group Name already exits. Do you wish to update it?")) {
				getController().createDefectGroup(dialog.getDefectGroup(imageName));
			} else {
				return;
			}
		
		}else {
			getController().createDefectGroup(dialog.getDefectGroup(imageName));
		}
		
		
		List<DefectGroup> defectGroups = getController().selectDefectGroups();
		getClientModel().setDefectGroups(defectGroups);
		
		ImageSectionPanel imageSectionPanel = (ImageSectionPanel)getMainWindow().getTabbedPanel(ImageSectionPanel.class);
		if (imageSectionPanel != null) imageSectionPanel.imageSelected();
		
		imageSelected();

	}

	private void deleteDefectGroup() {
		DefectGroup defectGroup = getDefectGroupPanel().getSelectedItem();
		if (defectGroup == null) return;

		if (getDefectDescriptionPanel().getTable().getRowCount() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Defect Group can not be deleted \n It is used in Defect Type Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefectTypeDescription criteria = new DefectTypeDescription();
		criteria.setDefectGroupName(defectGroup.getDefectGroupName());
		List<DefectTypeDescription> list = getController().selectDefectTypeDescriptions(criteria);

		if (list.size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Defect Group can not be deleted \n It is used in Defect Type Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Defect Group", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) return;

		getController().deleteDefectGroup(defectGroup);

		List<DefectGroup> defectGroups = getController().selectDefectGroups();
		getClientModel().setDefectGroups(defectGroups);

		ImageSectionPanel imageSectionPanel = (ImageSectionPanel)getMainWindow().getTabbedPanel(ImageSectionPanel.class);
		if (imageSectionPanel != null) imageSectionPanel.imageSelected();
		
		imageSelected();

	}

	
	private void createDefect() {
		DefectType defectType = defectPanel.getSelectedItem();
		PartDataDialog dialog = new PartDataDialog(getMainWindow(), "Defect Type",
				defectType == null? null: defectType.getDefectTypeName(),
				defectType == null? null: defectType.getDefectTypeDescriptionShort(),
				defectType == null? null: defectType.getDefectTypeDescriptionLong());
		if(!dialog.isDataCreated()) return;
		
		getController().createDefectType(dialog.getDefectType());

		List<DefectType> defectTypes = getController().selectDefectTypes();
		getClientModel().setDefectTypes(defectTypes);
		Collections.sort(defectTypes, new PropertyComparator<DefectType>(DefectType.class, "defectTypeName"));
		getDefectPanel().reloadData(defectTypes);
	}

	private void deleteDefect() {
		DefectType defectType = getDefectPanel().getSelectedItem();
		if (defectType == null) return;

		if (getDefectDescriptionPanel().getTable().getRowCount() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Defect Type can not be deleted \n It is used in Defect Type Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefectTypeDescription criteria = new DefectTypeDescription();
		criteria.setDefectTypeName(defectType.getDefectTypeName());
		List<DefectTypeDescription> list = getController().selectDefectTypeDescriptions(criteria);

		if (list.size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Defect Type can not be deleted \n It is used in Defect Type Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Defect Type", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) return;

		getController().deleteDefectType(defectType);
		
		List<DefectType> defectTypes = getController().selectDefectTypes();
		getClientModel().setDefectTypes(defectTypes);
		Collections.sort(defectTypes, new PropertyComparator<DefectType>(DefectType.class, "defectTypeName"));
		getDefectPanel().reloadData(defectTypes);
	}

	private void createSecondaryPart() {
		
		SecondaryPart secondaryPart = secondaryPartPanel.getSelectedItem();
		PartDataDialog dialog = new PartDataDialog(getMainWindow(), "Secondary Part",
				secondaryPart == null? null: secondaryPart.getSecondaryPartName(),
						secondaryPart == null? null: secondaryPart.getSecondaryPartDescShort(),
								secondaryPart == null? null: secondaryPart.getSecondaryPartDescLong());
		if(!dialog.isDataCreated()) return;
		
		getController().createSecondaryPart(dialog.getSecondaryPart());

		List<SecondaryPart> parts = getController().selectSecondaryParts();
		getClientModel().setSecondaryParts(parts);
		//  Secondary Parts List Sorting
		Collections.sort(parts, new PropertyComparator<SecondaryPart>(SecondaryPart.class, "secondaryPartName"));
		getSecondaryPartPanel().reloadData(parts);
	}

	private void deleteSecondaryPart() {
		SecondaryPart secondaryPart = getSecondaryPartPanel().getSelectedItem();
		if (secondaryPart == null) return;

		if (getDefectDescriptionPanel().getTable().getRowCount() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Secondary Part can not be deleted \n It is used in Defect Type Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefectTypeDescription criteria = new DefectTypeDescription();
		criteria.setSecondaryPartName(secondaryPart.getSecondaryPartName());
		List<DefectTypeDescription> list = getController().selectDefectTypeDescriptions(criteria);

		if (list.size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Secondary Part can not be deleted \n It is used in Defect Type Description", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Secondary Part", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) return;
		getController().deleteSecondaryPart(secondaryPart);

		List<SecondaryPart> secondaryParts = getController().selectSecondaryParts();
		getController().getClientModel().setSecondaryParts(secondaryParts);
		// Secondary Parts List Sorting
		Collections.sort(secondaryParts, new PropertyComparator<SecondaryPart>(SecondaryPart.class, "secondaryPartName"));
		getSecondaryPartPanel().reloadData(secondaryParts);

	}

	private void createDefectDescription() {
		List<DefectTypeDescription> defectTypeDescriptions = createNewDefectTypeDescriptions();
		if (defectTypeDescriptions.isEmpty()) return;

		try{
			getController().createDefectTypeDescriptions(defectTypeDescriptions);
		}catch(BaseException ex) {
		   throw ex;
		}finally{
			defectDataSelected();
		}
		PartDefectCombinationPanel combinationPanel = (PartDefectCombinationPanel)getMainWindow().getTabbedPanel(PartDefectCombinationPanel.class);
		if (combinationPanel != null) 
			combinationPanel.getDefectGroupPane().clearSelection();
		
	}
	
	private List<DefectTypeDescription> createNewDefectTypeDescriptions() {
		List<DefectTypeDescription> defectTypeDescriptions = new ArrayList<DefectTypeDescription>();

		List<DefectGroup> defectGroups = getDefectGroupPanel().getSelectedItems();
		List<DefectType> defectTypes = getDefectPanel().getSelectedItems();
		List<SecondaryPart> secondaryParts = getSecondaryPartPanel().getSelectedItems();

		if ((defectGroups == null || defectGroups.isEmpty()) || (defectTypes == null || defectTypes.isEmpty()) || (secondaryParts == null || secondaryParts.isEmpty())) 
			return defectTypeDescriptions;
		
		List<DefectTypeDescription> existingDefectTypeDescriptions = getDefectDescriptionPanel().getItems();

		for (DefectGroup defectGroup : defectGroups) {
			for (DefectType defectType : defectTypes) {
				for (SecondaryPart secondaryPart : secondaryParts) {
					DefectTypeDescription description = new DefectTypeDescription();
					description.setDefectGroupName(defectGroup.getDefectGroupName());
					description.setDefectTypeName(defectType.getDefectTypeName());
					description.setSecondaryPartName(secondaryPart.getSecondaryPartName());
					if (!existingDefectTypeDescriptions.contains(description))
						defectTypeDescriptions.add(description);
				}
			}
		}
		return defectTypeDescriptions;
	}

	private void deleteDefectDescription() {
		List<DefectTypeDescription> selectedDefects = getDefectDescriptionPanel().getSelectedItems();
		if (selectedDefects == null || selectedDefects.isEmpty()) return;

		
		List<String> defectNames = canDelete(selectedDefects);
		if(!defectNames.isEmpty()){
		
			String msg = "Some of Defect Type Descriptions are associated with Defect Descriptions and can not be deleted\n";

			for (String str : defectNames) msg = msg + "\n" + str;

			JOptionPane.showMessageDialog(getMainWindow(), msg, "Delete Part Description", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int ret = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure ?", "Delete Defect Type Description", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION != ret) return; 
		try{	
			getController().deleteDefectTypeDescriptions(selectedDefects);
		}catch(BaseException ex) {
			throw ex;
		}finally{
			defectDataSelected();
		}

		PartDefectCombinationPanel combinationPanel = (PartDefectCombinationPanel)getMainWindow().getTabbedPanel(PartDefectCombinationPanel.class);
		if (combinationPanel != null) 
			combinationPanel.getDefectGroupPane().clearSelection();

	}
	
	private List<String> canDelete(List<DefectTypeDescription> selectedDefects){
		
		List<String> defectNames = new ArrayList<String>();
		
		List<DefectDescription> partDefects = getController().selectDefectDescriptionsByDefectTypeDescriptions(selectedDefects);

		Map<String,Integer> defectCountMap = new HashMap<String,Integer>();
		for(DefectTypeDescription item : selectedDefects) {
			Integer count = defectCountMap.get(item.getDefectTypeName());
			defectCountMap.put(item.getDefectTypeName(), count == null? 1 : count + 1);
		}
		
		for(Map.Entry<String, Integer> entry : defectCountMap.entrySet()) {
			DefectTypeDescription criteria = new DefectTypeDescription();
			criteria.setDefectTypeName(entry.getKey());
			List<DefectTypeDescription> list = getController().selectDefectTypeDescriptions(criteria);
			if(entry.getValue() >=list.size()){
				if(contains(partDefects,entry.getKey())) defectNames.add(entry.getKey());
			}
		}
		
		return defectNames;
	}
	
	private boolean contains(List<DefectDescription> partDefects, String defectTypeName) {
		for(DefectDescription partDefect : partDefects) {
			if(partDefect.getDefectTypeName().equals(defectTypeName)) return true;
		}
		return false;
	}
}
