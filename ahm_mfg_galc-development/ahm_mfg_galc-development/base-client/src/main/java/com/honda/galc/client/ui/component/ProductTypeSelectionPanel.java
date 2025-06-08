/**
 * 
 */
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

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.event.ProductTypeSelectionEvent;
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
 * @author VF031824
 *
 */
public class ProductTypeSelectionPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static final String DUMMY_PROCESS_POINT_ID = "No Selection";
	private String siteName;
	private LabeledComboBox plantComboBox;
	private LabeledComboBox departmentComboBox;
	private LabeledComboBox productComboBox;
	protected String processPointSortingMethod = "getDivisionName";
	protected String sortByProcessPointName = "getDisplayName";
	protected HashMap<String, SortedArrayList<ProcessPoint>> processPoints;
	protected SortedArrayList<String> productTypes;
	protected Map<String, String> productTypeMap;
	protected String defaultProductType;

	private List<Plant> plants = null;

	public ProductTypeSelectionPanel(String siteName) {
		super();
		this.siteName = siteName;
		try{
			plants = ServiceFactory.getDao(PlantDao.class).findAllBySite(this.siteName);
		}catch(Exception e){
			Logger.getLogger().error(e, "error getting plants: " + e.getMessage() + " stack trace:" + e.getStackTrace());
		}
		buildProductTypeMap();
		initComponent();
		addActionListeners();
	}

	public void initComponent() {
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

	public void addActionListeners() {
		getPlantComboBox().getComponent().addActionListener(this);
		getDepartmentComboBox().getComponent().addActionListener(this);
		getProductComboBox().getComponent().addActionListener(this);
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

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getPlantComboBox().getComponent())) plantChanged();
		else if(e.getSource().equals(getDepartmentComboBox().getComponent())) departmentChanged();
		else if(e.getSource().equals(getProductComboBox().getComponent())) productTypeChanged();
	}

	public void plantChanged() {		
		Plant plant = (Plant) getPlantComboBox().getComponent().getSelectedItem();
		Logger.getLogger().info(plant.getId().getPlantName()+" is selected");
		SortedArrayList<Division> divisions = new SortedArrayList<Division>(plant.getDivisions(),"getDivisionName");
		ComboBoxModel<Division> model = new  ComboBoxModel<Division>(divisions,"getDivisionName");
		getDepartmentComboBox().getComponent().setModel(model);
		getDepartmentComboBox().getComponent().setRenderer(model);
		getDepartmentComboBox().getComponent().setSelectedIndex(-1);

		EventBus.publish(new ProductTypeSelectionEvent(this,SelectionEvent.PLANT_SELECTED));;
	}

	public Plant getSelectedPlant() {

		return (Plant) getPlantComboBox().getComponent().getSelectedItem();
	}

	public void departmentChanged() {
		EventBus.publish(new ProductTypeSelectionEvent(this,SelectionEvent.DEPARTMENT_SELECTED));

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

	protected String getProductType(String id) {
		return productTypeMap.containsKey(id) ? productTypeMap.get(id) : defaultProductType;
	}

	protected SortedArrayList<ProcessPoint> getProcessPoints(Division division) {
		List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAllByDivision(division);
		return new SortedArrayList<ProcessPoint>(processPoints, getProcessPointSortingMethodName());
	}

	protected ProcessPoint createDummyProcessPoint() {
		ProcessPoint dummy = new ProcessPoint();
		dummy.setProcessPointId(DUMMY_PROCESS_POINT_ID);
		return dummy;
	}

	private String getProcessPointSortingMethodName() {
		CommonTlPropertyBean bean = PropertyService.getPropertyBean(CommonTlPropertyBean.class);
		return bean.getProcessPointSortingMethodName();
	}

	public void productTypeChanged() {

		Logger.getLogger().info(selectedProductType()+" is selected");               
		EventBus.publish(new ProductTypeSelectionEvent(this,SelectionEvent.PRODUCT_TYPE_SELECTED));
	}

	public String selectedProductType() {
		return (String) getProductComboBox().getComponent().getSelectedItem();
	}

	public String selectedDivision() {
		Division division = (Division) getDepartmentComboBox().getComponent().getSelectedItem();
		return division == null ? null : division.getDivisionId();
	}

	public boolean isProductTypeSelected() {
		return (getProductComboBox().getComponent().getSelectedItem() != null);
	}
}