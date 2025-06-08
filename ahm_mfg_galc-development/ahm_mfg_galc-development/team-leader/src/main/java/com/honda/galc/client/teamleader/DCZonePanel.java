package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.CheckBoxCellEditor;
import com.honda.galc.client.ui.component.CheckBoxCellRenderer;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.SortableObjectTableModel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DCZoneDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.dto.DCZoneDto;
import com.honda.galc.entity.conf.DCZone;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

import net.miginfocom.swing.MigLayout;

public class DCZonePanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JPanel divisionSelectPanel;
	private List<Plant> plants = null;
	private LabeledComboBox plantComboBox;
    private LabeledComboBox divisionComboBox;
    
    private ObjectTablePane<Zone> zoneTable;
    private ObjectTablePane<DCZoneDto> dcZoneTable;
    private ObjectTablePane<ProcessPoint> processPointTable;
    
    private JPanel zoneMgrButtonPanel;
	private JButton addToDCZoneButton;
	private JButton removeFromDCZoneButton;
	
	private List<ProcessPoint> inZone = new ArrayList<ProcessPoint>();
	private List<ProcessPoint> notInZone = new ArrayList<ProcessPoint>();
    
	public DCZonePanel(TabbedMainWindow mainWindow){
		super("DC Zones", KeyEvent.VK_Z, mainWindow);
	}

	@Override
	public void onTabSelected() {
		try {
			if (isInitialized)	return;
			initComponents();
			addListeners();
			isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start Zones.");
			setErrorMessage("Exception to start Zones panel." + e.toString());
		}	
	}
	
	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Border border = BorderFactory.createEtchedBorder();
		Box box1 = Box.createVerticalBox();
		box1.setBorder(border);
		box1.add(createDivisionSelectPanel());
		add(box1);
		
		Box box2 = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(this.zoneTable);
		add(box2);
		
		Box box3 = Box.createHorizontalBox();
		box3.setBorder(border);
		box3.add(this.dcZoneTable);
		box3.add(createZoneMgrButtonPanel());
		box3.add(this.processPointTable);
		add(box3);
	}
	
	private JPanel createDivisionSelectPanel(){
		if(StringUtils.isEmpty(PropertyService.getSiteName())){
			MessageDialog.showError(this, "Property SITE_NAME is not defined for System_Info component.");
			return this.divisionSelectPanel;
		}
		try{
            this.plants = ServiceFactory.getDao(PlantDao.class).findAllBySite(PropertyService.getSiteName());
        }catch(Exception e){
        	Logger.getLogger().error(e.getMessage());
        }
		
		this.divisionSelectPanel = new JPanel();
		this.divisionSelectPanel.setLayout(new FlowLayout());
		this.divisionSelectPanel.add(createPlantComboBox());
		this.divisionSelectPanel.add(createDivisionComboBox());
		dcZoneTable = createProcessPointZonePanel("Process Points Within Zone");
        processPointTable = createProcessPointPanel("Process Points Outside Zone");
		zoneTable = createZonesPanel();
        return divisionSelectPanel;
	}
	
    @SuppressWarnings("unchecked")
	public LabeledComboBox createPlantComboBox() {
        if (plantComboBox == null) {
            plantComboBox = new LabeledComboBox("Plant");
            plantComboBox.getComponent().setName("JPlantCombobox");
            plantComboBox.getComponent().setPreferredSize(new Dimension(70,20));
            
            ComboBoxModel<Plant> model = new ComboBoxModel<Plant>(plants,"getPlantName");
            plantComboBox.getComponent().setModel(model);
            plantComboBox.getComponent().setSelectedIndex(-1);
            plantComboBox.getComponent().setRenderer(model);  
        }
        return plantComboBox;
    }
    
    public LabeledComboBox createDivisionComboBox() {
    	if (divisionComboBox == null) {
            divisionComboBox = new LabeledComboBox("Division");
            divisionComboBox.getComponent().setName("JDivisionCombobox");
            divisionComboBox.getComponent().setPreferredSize(new Dimension(70,20));
        }
        return divisionComboBox;
    }
    
	private Component createZoneMgrButtonPanel() {
		if(zoneMgrButtonPanel == null){
			zoneMgrButtonPanel = new JPanel();
			zoneMgrButtonPanel.setLayout(new MigLayout("center, center, wrap"));
			zoneMgrButtonPanel.add(createAddToZoneButton());
			zoneMgrButtonPanel.add(createRemoveFromZoneButton());
		}
		return zoneMgrButtonPanel;
	}
	
	public JButton createAddToZoneButton() {
		if(addToDCZoneButton == null){
			addToDCZoneButton = new JButton("<<");
		}
		return addToDCZoneButton;
	}

	
	public JButton createRemoveFromZoneButton() {
		if(removeFromDCZoneButton == null){
			removeFromDCZoneButton = new JButton(">>");
		}
		return removeFromDCZoneButton;
	}

	private void addListeners() {
		this.plantComboBox.getComponent().addActionListener(this);
        this.divisionComboBox.getComponent().addActionListener(this);
        ((TablePane)this.zoneTable).getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        	public void valueChanged(ListSelectionEvent e){
            	updateDCZones();
            }
        });
		this.addToDCZoneButton.addActionListener(this);
		this.removeFromDCZoneButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.plantComboBox.getComponent())) plantChanged();
        if(e.getSource().equals(this.divisionComboBox.getComponent())) divisionChanged();
		if(e.getSource().equals(this.addToDCZoneButton)){
			List<DCZoneDto> zoneDtos = convertToDCZoneDtos(this.processPointTable.getSelectedItems());
			List<DCZone> zones = new ArrayList<DCZone>();
			for (DCZoneDto zoneDto : zoneDtos)
				zones.add(zoneDto.getZone());
			getDao(DCZoneDao.class).insertAll(zones);
			logUserAction(INSERTED, zones);
			updateDCZones();
		} 
		if(e.getSource().equals(this.removeFromDCZoneButton)){
			List<DCZoneDto> zoneDtos = this.dcZoneTable.getSelectedItems();
			List<DCZone> zones = new ArrayList<DCZone>();
			for (DCZoneDto zoneDto : zoneDtos)
				zones.add(zoneDto.getZone());
			getDao(DCZoneDao.class).removeAll(zones);
			logUserAction(REMOVED, zones);
			updateDCZones();
		}
	}
	
	public List<DCZoneDto> convertToDCZoneDtos(List<ProcessPoint> selectedItems){
		List<DCZoneDto> dcZones = new ArrayList<DCZoneDto>();
		for (ProcessPoint processPoint : selectedItems){
			dcZones.add(new DCZoneDto(new DCZone(this.zoneTable.getSelectedItem().getId(), processPoint.getProcessPointId(), false), processPoint.getProcessPointName()));	
		}
		return dcZones;
	}
	
	@SuppressWarnings("unchecked")
	public void plantChanged() {		
        Plant plant = (Plant) this.plantComboBox.getComponent().getSelectedItem();
        Logger.getLogger().info(plant.getId().getPlantName()+" is selected");
        SortedArrayList<Division> divisions = new SortedArrayList<Division>(plant.getDivisions(),"getDivisionName");
        ComboBoxModel<Division> model = new  ComboBoxModel<Division>(divisions,"getDivisionName");
        this.divisionComboBox.getComponent().setModel(model);
        this.divisionComboBox.getComponent().setRenderer(model);
        this.divisionComboBox.getComponent().setSelectedIndex(-1);
	}
    
    public void divisionChanged() {
    	List<Zone> zones = new SortedArrayList<Zone>();
    	Division division = (Division) this.divisionComboBox.getComponent().getSelectedItem();
        if(division!=null){
        	Logger.getLogger().info(division.getDivisionId()+" is selected");
        	zones.addAll(ServiceFactory.getDao(ZoneDao.class).findAllByDivisionId(division.getDivisionId()));
        } 
        this.zoneTable.reloadData(zones);
        this.dcZoneTable.removeData();
        this.processPointTable.removeData();
    }
    
    public void updateDCZones(){	
    	if (this.zoneTable.getSelectedItem() == null){
    		inZone.clear();
    		notInZone.clear();
    	} else {
    		processPointSort(this.zoneTable.getSelectedItem().getId());
    		this.dcZoneTable.reloadData(loadDCZoneDtos(this.inZone));
    		this.dcZoneTable.clearSelection();
        	this.processPointTable.reloadData(this.notInZone);
        	this.processPointTable.clearSelection();
        }
    }
    
    private List<DCZoneDto> loadDCZoneDtos(List<ProcessPoint> processPoints) {
    	List<DCZoneDto> DCZoneDtos = new ArrayList<DCZoneDto>();
    	for (ProcessPoint processPoint : processPoints) {
    		List<DCZone> zones = ServiceFactory.getDao(DCZoneDao.class).findAllByProcessPointId(processPoint.getProcessPointId());
    		for (DCZone zone : zones)
    			if (this.zoneTable.getSelectedItem().getZoneId().trim().equals(zone.getZoneId().trim()))
    				DCZoneDtos.add(new DCZoneDto(zone, processPoint.getProcessPointName()));
    	}
    	return DCZoneDtos;
    }
    
    private void processPointSort(String zoneId){
    	List<DCZone> dcZones = ServiceFactory.getDao(DCZoneDao.class).findAllByZoneId(zoneId);
    	this.inZone = new ArrayList<ProcessPoint>();
    	this.notInZone = ServiceFactory.getDao(ProcessPointDao.class).
				findAllByDivision((Division)this.divisionComboBox.getComponent().getSelectedItem());
    	for (DCZone dcZone : dcZones){
    		for (int i = 0; i < this.notInZone.size(); i++){
    			if (this.notInZone.get(i).getId().equals(dcZone.getProcessPointId())){
    				this.inZone.add(this.notInZone.get(i));
    	    		this.notInZone.remove(i);
    			}
    		}
    	}
    }
    
    protected ObjectTablePane<Zone> createZonesPanel() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Zone ID", "zoneId");
		mapping.put("Zone Name", "zoneName");
		mapping.put("Zone Description", "zoneDescription");
		this.zoneTable = new ObjectTablePane<Zone>(mapping.get(), true, true);
		this.zoneTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.zoneTable.getTable().setName("zonesPanel");
		this.zoneTable.setBorder(new TitledBorder("Zones"));
		return zoneTable;
	}
    
    protected ObjectTablePane<DCZoneDto> createProcessPointZonePanel(String title) {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Process Point ID", "processPointId");
		mapping.put("Process Point Name", "processPointName");
		mapping.put("HandHeld", "repairable");
		mapping.get().get(3).setEditable(true);
		this.dcZoneTable = new ObjectTablePane<DCZoneDto>(mapping.get(), true, true);
		this.dcZoneTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.dcZoneTable.getTable().setName("processPointPanel");
		this.dcZoneTable.setBorder(new TitledBorder(title));
		this.dcZoneTable.setTableModel(new SortableObjectTableModel<DCZoneDto>(this.dcZoneTable.getTable(),null,mapping.get()) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable (int row, int column){
				return column == 3;
		    }
			
		});
		this.dcZoneTable.getTable().getColumnModel().getColumn(3).setCellEditor(new CheckBoxCellEditor());
		this.dcZoneTable.getTable().getColumnModel().getColumn(3).setCellRenderer(new CheckBoxCellRenderer());
		
		return dcZoneTable;
	}
    
    protected ObjectTablePane<ProcessPoint> createProcessPointPanel(String title) {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Process Point ID", "processPointId");
		mapping.put("Process Point Name", "processPointName");
		this.processPointTable = new ObjectTablePane<ProcessPoint>(mapping.get(), true, true);
		this.processPointTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.processPointTable.getTable().setName("processPointPanel");
		this.processPointTable.setBorder(new TitledBorder(title));
		return processPointTable;
	}
}
