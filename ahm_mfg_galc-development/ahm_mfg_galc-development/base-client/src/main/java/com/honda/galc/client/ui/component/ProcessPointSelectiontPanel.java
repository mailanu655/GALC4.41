package com.honda.galc.client.ui.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;
/**
 * 
 * <h3>ProcessPoinSelectiontPanel Class description</h3>
 * <p> ProcessPoinSelectiontPanel description </p>
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
 * Mar 29, 2012
 *
 *
 */
public class ProcessPointSelectiontPanel extends JPanel implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    public static final String DUMMY_PROCESS_POINT_ID = "No Selection";
    private String siteName;
    private LabeledComboBox plantComboBox;
    private LabeledComboBox departmentComboBox;
    private LabeledComboBox productComboBox;
    private FilteredLabeledComboBox processPointComboBox;
    protected String processPointSortingMethod = "getDivisionName";
    protected String sortByProcessPointName = "getDisplayName";
    protected HashMap<String, SortedArrayList<ProcessPoint>> processPoints;
    protected SortedArrayList<String> productTypes;
    protected Map<String, String> productTypeMap;
    protected String defaultProductType;

    private List<Plant> plants = null;
    
    public ProcessPointSelectiontPanel(String siteName, String processPointSortingMethod) {
    	this(siteName);
    	this.processPointSortingMethod = processPointSortingMethod;
    }
    
    public ProcessPointSelectiontPanel(String siteName) {
        super();
        this.siteName = siteName;
        try{
            plants = ServiceFactory.getDao(PlantDao.class).findAllBySite(this.siteName);
        }catch(Exception e){
            
        }
        buildProductTypeMap();
        initComponent();
        addActionListeners();
    }
    
    public void initComponent() {
   //     setSize(1000, 50);
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(getPlantComboBox(), c);
        c.gridx = 1;
        add(getDepartmentComboBox(), c);
        c.gridx = 2;
        add(getProductComboBox(), c);
        c.weightx = 0.7;
        c.gridx = 3;
        c.gridwidth = 2;
        add(getProcessPointComboBox(), c);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }
    
    public LabeledComboBox getPlantComboBox() {
        if (plantComboBox == null) {
            plantComboBox = new LabeledComboBox("Plant");
            plantComboBox.getComponent().setName("JPlantCombobox");
            ComboBoxModel<Plant> model = new ComboBoxModel<Plant>(plants,"getPlantName");
            plantComboBox.getComponent().setModel(model);
            plantComboBox.getComponent().setSelectedIndex(-1);
            plantComboBox.getComponent().setRenderer(model);
            
        }
        return plantComboBox;
    }
    
    public LabeledComboBox getDepartmentComboBox() {
        if (departmentComboBox == null) {
            departmentComboBox = new LabeledComboBox("Department");
            departmentComboBox.getComponent().setName("JDepartmentCombobox");
            departmentComboBox.getComponent().setPreferredSize(new Dimension(70,20));

        }
        return departmentComboBox;
    }
    
    public LabeledComboBox getProductComboBox() {
        if (productComboBox == null) {
        	productComboBox = new LabeledComboBox("Product");
        	productComboBox.getComponent().setName("JProductCombobox");
        	productComboBox.getComponent().setPreferredSize(new Dimension(80,20));
    
        }
        return productComboBox;
    }
    
    public FilteredLabeledComboBox getProcessPointComboBox() {
        if (processPointComboBox == null) {
            processPointComboBox = new FilteredLabeledComboBox("Process Point");
            processPointComboBox.getComponent().setName("JProcessPointCombobox");
            processPointComboBox.getComponent().setPreferredSize(new Dimension(350,20));
        }
        return processPointComboBox;
    }
    
    public void addActionListeners() {
        getPlantComboBox().getComponent().addActionListener(this);
        getDepartmentComboBox().getComponent().addActionListener(this);
        getProductComboBox().getComponent().addActionListener(this);
        getProcessPointComboBox().getComponent().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(getPlantComboBox().getComponent())) plantChanged();
        else if(e.getSource().equals(getDepartmentComboBox().getComponent())) departmentChanged();
        else if(e.getSource().equals(getProductComboBox().getComponent())) productTypeChanged();
        else if(e.getSource().equals(getProcessPointComboBox().getComponent())) processPointChanged();
        
    }
    
    private void processPointChanged() {
    	if(getProcessPointComboBox().getComponent().getSelectedItem()!=null)
    		Logger.getLogger().info(getProcessPointComboBox().getComponent().getSelectedItem().toString()+" is selected");
    	EventBus.publish(new ProcessPointSelectionEvent(this,SelectionEvent.PROCESSPOINT_SELECTED));
    	
	}

	public void plantChanged() {		
        Plant plant = (Plant) getPlantComboBox().getComponent().getSelectedItem();
        Logger.getLogger().info(plant.getId().getPlantName()+" is selected");
        SortedArrayList<Division> divisions = new SortedArrayList<Division>(plant.getDivisions(),"getDivisionName");
        ComboBoxModel<Division> model = new  ComboBoxModel<Division>(divisions,"getDivisionName");
        getDepartmentComboBox().getComponent().setModel(model);
        getDepartmentComboBox().getComponent().setRenderer(model);
        getDepartmentComboBox().getComponent().setSelectedIndex(-1);
        
        EventBus.publish(new ProcessPointSelectionEvent(this,SelectionEvent.PLANT_SELECTED));;
    }
    
	
	public Plant getSelectedPlant() {
		
		return (Plant) getPlantComboBox().getComponent().getSelectedItem();
        
	}
    public void departmentChanged() {
        EventBus.publish(new ProcessPointSelectionEvent(this,SelectionEvent.DEPARTMENT_SELECTED));

        Division division = (Division) getDepartmentComboBox().getComponent().getSelectedItem();
        if(division!=null)
         Logger.getLogger().info(division.getDivisionId()+" is selected");
        SortedArrayList<ProcessPoint> sortedList;
        if(division == null) {
            sortedList = new SortedArrayList<ProcessPoint>();
        } else {
            sortedList = getProcessPoints(division);
        }
        filterProcessPoints(sortedList);

        ComboBoxModel<String> model = new ComboBoxModel<String>(productTypes);
        JComboBox comboBox = getProductComboBox().getComponent();
        comboBox.setModel(model);
        comboBox.setRenderer(model);
        comboBox.setSelectedIndex(-1);
        
        
        String prodType = PropertyService.getSystemPropertyBean().getProductType();
        if(division != null && productTypes.contains(prodType)) {
        	comboBox.getModel().setSelectedItem(prodType);
            productTypeChanged();
        }
    }
    
    public void filterProcessPoints(SortedArrayList<ProcessPoint> list) {
    	processPoints = new HashMap<String, SortedArrayList<ProcessPoint>>();
    	productTypes = new SortedArrayList<String>();
    	SortedArrayList<ProcessPoint> subList;
    	String prodType;
    	ProcessPoint pp;
    	Iterator<ProcessPoint> iterator = list.iterator();
    	
        while(iterator.hasNext()) {
        	pp = iterator.next();
    		prodType = getProductType(pp.getId());
    		subList = processPoints.get(prodType);
    		if(subList == null) {
    			subList = new SortedArrayList<ProcessPoint>();
    			subList.add(createDummyProcessPoint());
    			processPoints.put(prodType, subList);
    			productTypes.add(prodType);
    		}
    		subList.add(pp);
        }
    }
    
    protected void buildProductTypeMap() {
    	productTypeMap = new HashMap<String, String>();
    	defaultProductType = PropertyService.getSystemPropertyBean().getProductType();
    	List<ComponentProperty> properties = findAllProductTypes();
    	String type;
    	for(ComponentProperty prop : properties) {
    		type = prop.getPropertyValue();
    		productTypeMap.put(prop.getId().getComponentId(), type == null ? defaultProductType : type);
    	}
    }
    
    protected List<ComponentProperty> findAllProductTypes() {
    	List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
    	ComponentPropertyDao componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
    	List<ComponentProperty> productTypes = componentPropertyDao.findAllProductTypes();
    	List<ComponentProperty> subAssyProductTypes = componentPropertyDao.findAllSubAssyProductTypes();
    	if (productTypes != null && !productTypes.isEmpty()) {
    		properties.addAll(productTypes);
    	}
    	if (subAssyProductTypes != null && !subAssyProductTypes.isEmpty()) {
    		properties.addAll(subAssyProductTypes);
    	}
    	return properties;
    }
    
    protected String getProductType(String id) {
    	return productTypeMap.containsKey(id) ? productTypeMap.get(id) : defaultProductType;
    }
    
    protected ProcessPoint createDummyProcessPoint() {
        ProcessPoint dummy = new ProcessPoint();
        dummy.setProcessPointId(DUMMY_PROCESS_POINT_ID);
        return dummy;
    }

    public void productTypeChanged() {
        SortedArrayList<ProcessPoint> sortedList = processPoints.get(selectedProductType());
        ComboBoxModel<ProcessPoint> model = new ComboBoxModel<ProcessPoint>(sortedList,"getDisplayName");
        getProcessPointComboBox().setModel(model);
        getProcessPointComboBox().setSelectedIndex(-1);
        
        Logger.getLogger().info(selectedProductType()+" is selected");               
        EventBus.publish(new ProcessPointSelectionEvent(this,SelectionEvent.PRODUCT_TYPE_SELECTED));
    }
        
    public String selectedProductType() {
    	return (String) getProductComboBox().getComponent().getSelectedItem();
    }
        
    public String selectedDivision() {
    	Division division = (Division) getDepartmentComboBox().getComponent().getSelectedItem();
    	return division == null ? null : division.getDivisionId();
    }

    protected SortedArrayList<ProcessPoint> getProcessPoints(Division division) {
    	List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAllByDivision(division);
    	return new SortedArrayList<ProcessPoint>(processPoints, getProcessPointSortingMethodName());
    }

	private String getProcessPointSortingMethodName() {
		CommonTlPropertyBean bean = PropertyService.getPropertyBean(CommonTlPropertyBean.class);
		return bean.getProcessPointSortingMethodName();
	}

	public void addProcessPointActionListener(ActionListener listener) {
 //   	getDepartmentComboBox().getComponent().addActionListener(listener);
 //       getProcessPointComboBox().getComponent().addActionListener(listener);
    }
    
	public List<ProcessPoint> getApplicableProcessPoints() {
		List<ProcessPoint> list = new ArrayList<ProcessPoint>();
		if(selectedProductType() != null) {
			ProcessPoint pp = getCurrentProcessPoint();
			if(pp == null || DUMMY_PROCESS_POINT_ID.equals(pp.getProcessPointId())) {
				SortedArrayList<ProcessPoint> fullList = processPoints.get(selectedProductType());
				for(int i = 1; i < fullList.size(); i++) {
					list.add(fullList.get(i));
				}
			} else {
				list.add(pp);
			}
		} 
		return list;
	}
	
    public ProcessPoint getCurrentProcessPoint() {
        return (ProcessPoint)getProcessPointComboBox().getComponent().getSelectedItem();
    }
    
    public String getCurrentProcessPointId() {
    	ProcessPoint processPoint = getCurrentProcessPoint();
    	if(processPoint == null || processPoint.getProcessPointId().equals(DUMMY_PROCESS_POINT_ID)) return null;
    	return processPoint.getProcessPointId();
    }
    
    public boolean isProcessPointSelected() {
    	return getCurrentProcessPointId() != null;
    }
    
    public SortedArrayList<ProcessPoint> getAllProcessPoints() {
    	return processPoints.get(selectedProductType());
    }
    
    
}
