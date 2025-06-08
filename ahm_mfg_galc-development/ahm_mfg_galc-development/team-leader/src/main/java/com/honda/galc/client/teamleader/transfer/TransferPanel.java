package com.honda.galc.client.teamleader.transfer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

import net.miginfocom.swing.MigLayout;

public abstract class TransferPanel extends TabbedPanel{
	protected String siteName;
	protected List<Plant> plants = null;
	protected Map<String, ProductSpecData> productSpecCache;
	
	protected JPanel plDivProdPanel;
	protected LabeledComboBox plantComboBox;
	protected LabeledComboBox divisionComboBox;
	protected LabeledComboBox productComboBox;
	
	protected SortedArrayList<String> productTypes;
	protected Map<String, String> productTypeMap;
	protected String defaultProductType;
	
	protected HashMap<String, SortedArrayList<ProcessPoint>> processPoints;
	protected SortedArrayList<String> procPointTags;
	
	protected ProductSpecData productSpecData = null;
	
	protected JButton copyButton;
	
	protected AtomicInteger actionCounter = new AtomicInteger(0);
	
	private static final long serialVersionUID = 1L;
	
	public TransferPanel(String screenName, int keyEvent) {
		super(screenName, keyEvent);
	}

	public TransferPanel(String screenName, int keyEvent, MainWindow mainWindow) {
		super(screenName, keyEvent, mainWindow);
	}
	
	@Override
	public void onTabSelected() {
		if (isInitialized) return;
		if (StringUtils.isEmpty(this.siteName = PropertyService.getSiteName())) {
			MessageDialog.showError(this, "SITE_NAME property is not defined for System_Info component.");
			return;
		}
		this.plants = ServiceFactory.getDao(PlantDao.class).findAllBySite(this.siteName);
		this.productSpecCache = new HashMap<String, ProductSpecData>();
		this.initComponents();
		this.addListeners();
		this.buildProductTypeMap();
		this.isInitialized = true;
	}
	
	protected abstract void initComponents();
	protected abstract void addListeners();
	protected abstract void productTypeChanged();
	protected abstract Boolean isReadyToCopy();
	
	protected JPanel getplDivProdPanel() {
		if (this.plDivProdPanel == null) {
			this.plDivProdPanel = new JPanel(new MigLayout("align 50% 50%,inset 0 0 0 0"));
			this.plDivProdPanel.add(this.getPlantComboBox());
			this.plDivProdPanel.add(this.getDivisionComboBox());
			this.plDivProdPanel.add(this.getProductComboBox(),"span2, grow");
		}
		return this.plDivProdPanel;
	}
	
	protected LabeledComboBox getPlantComboBox() {
		if (this.plantComboBox == null) {
			this.plantComboBox = this.createComboBox("Plant", "PlantComboBox");
			ComboBoxModel<Plant> model = new ComboBoxModel<Plant>(this.plants, "getPlantName");
			this.plantComboBox.getComponent().setModel(model);
			this.plantComboBox.getComponent().setSelectedIndex(-1);
			this.plantComboBox.getComponent().setRenderer(model);
		}
		return this.plantComboBox;
	}
	
	protected LabeledComboBox getDivisionComboBox() {
		if (this.divisionComboBox == null)
			this.divisionComboBox = this.createComboBox("Division", "DivisionComboBox");
		return this.divisionComboBox;
	}
	
	protected LabeledComboBox getProductComboBox() {
		if (this.productComboBox == null)
			this.productComboBox = this.createComboBox("Product", "ProductComboBox");
		return this.productComboBox;
	}
	
	protected LabeledComboBox createComboBox(String label, String name) {
		Dimension dimension = new Dimension(100, 20);
		LabeledComboBox comboBox = new LabeledComboBox(label);
		comboBox.getComponent().setName(name);
		comboBox.getComponent().setBackground(Color.WHITE);
		comboBox.getComponent().setMinimumSize(new Dimension (50,20));
		comboBox.getComponent().setPreferredSize(dimension);
		return comboBox;
	}
	
	public void plantChanged() {
		Plant plant = (Plant) getPlantComboBox().getComponent().getSelectedItem();
		Logger.getLogger().info(plant.getId().getPlantName() + " is selected");
		SortedArrayList<Division> divisions = new SortedArrayList<Division>(plant.getDivisions(), "getDivisionName");
		ComboBoxModel<Division> model = new ComboBoxModel<Division>(divisions, "getDivisionName");
		getDivisionComboBox().getComponent().setModel(model);
		getDivisionComboBox().getComponent().setRenderer(model);
		getDivisionComboBox().getComponent().setSelectedIndex(-1);
	}
	
	public void divisionChanged() {
		Division division = (Division) getDivisionComboBox().getComponent().getSelectedItem();
		SortedArrayList<ProcessPoint> sortedList = new SortedArrayList<ProcessPoint>();
		if (division != null) {
			Logger.getLogger().info(division.getDivisionId() + " is selected");
			sortedList = this.getProcessPoints(division);
		}
		this.filterProcessPoints(sortedList);

		ComboBoxModel<String> model = new ComboBoxModel<String>(this.productTypes);
		JComboBox comboBox = getProductComboBox().getComponent();
		comboBox.setModel(model);
		comboBox.setRenderer(model);

		String prodType = PropertyService.getSystemPropertyBean().getProductType();
		if (division != null && this.productTypes.contains(prodType)) {
			comboBox.getModel().setSelectedItem(prodType);
			productTypeChanged();
		} else {
			comboBox.setSelectedIndex(-1);
		}
	}
	
	protected Plant getSelectedPlant() {
		return (Plant) getPlantComboBox().getComponent().getSelectedItem();
	}
	
	protected String getSelectedDivision() {
		Division division = (Division) getDivisionComboBox().getComponent().getSelectedItem();
		return division == null ? null : division.getDivisionId();
	}
	
	protected String getSelectedProductType() {
		return (String) getProductComboBox().getComponent().getSelectedItem();
	}

	protected SortedArrayList<ProcessPoint> getProcessPoints(Division division) {
		List<ProcessPoint> processPoints = ServiceFactory.getDao(
				ProcessPointDao.class).findAllByDivision(division);
		return new SortedArrayList<ProcessPoint>(processPoints,	getProcessPointSortingMethodName());
	}
	
	protected String getProcessPointSortingMethodName() {
		CommonTlPropertyBean bean = PropertyService.getPropertyBean(CommonTlPropertyBean.class);
		return bean.getProcessPointSortingMethodName();
	}
	
	public SortedArrayList<String> getProcPointTags(){
	return this.procPointTags;
}
	
	protected void buildProductTypeMap() {
		this.productTypeMap = new HashMap<String, String>();
		this.defaultProductType = PropertyService.getSystemPropertyBean().getProductType();
		List<ComponentProperty> properties = ServiceFactory.getDao(
				ComponentPropertyDao.class).findAllProductTypes();
		String type;
		for (ComponentProperty prop : properties) {
			type = prop.getPropertyValue();
			this.productTypeMap.put(prop.getId().getComponentId(),
					type == null ? defaultProductType : type);
		}
	}
	
	protected String getProductType(String id) {
		return this.productTypeMap.containsKey(id) ? this.productTypeMap.get(id) : this.defaultProductType;
	}
	
	protected void loadProcPointTags(ArrayList<ProcessPoint> procPoints){
		this.procPointTags = new SortedArrayList<String>();
		this.procPointTags.add(ProductSpec.WILDCARD);
		for (ProcessPoint procPoint : procPoints)
			this.procPointTags.add(procPoint.getId() + " - " + procPoint.getProcessPointName());
	}
	
	protected void filterProcessPoints(SortedArrayList<ProcessPoint> procPointsList) {
		this.processPoints = new HashMap<String, SortedArrayList<ProcessPoint>>();
		this.productTypes = new SortedArrayList<String>();
		SortedArrayList<ProcessPoint> subList;
		String prodType;
		ProcessPoint pp;
		Iterator<ProcessPoint> iterator = procPointsList.iterator();

		while (iterator.hasNext()) {
			pp = iterator.next();
			prodType = this.getProductType(pp.getId());
			subList = this.processPoints.get(prodType);
			if (subList == null) {
				subList = new SortedArrayList<ProcessPoint>();
				this.processPoints.put(prodType, subList);
				this.productTypes.add(prodType);
			}
			subList.add(pp);
		}
	}
	
	protected Map<String, ProductSpecData> getProductSpecCache() {
		return this.productSpecCache;
	}
	
	protected boolean isMbpnProduct() {
    	return super.isMbpnProduct();
    }
	
	protected AtomicInteger getActionCounter() {
		return this.actionCounter;
	}

	public void setActionCounter(int ctr) {
		getActionCounter().set(ctr);
	}
	
	@Override
    public void setCursor(Cursor cursor) {
		if (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(cursor)) {
			this.setWaitCursor();
		} else if (Cursor.getDefaultCursor().equals(cursor)) {
			this.setDefaultCursor();
		} else {
			super.setCursor(cursor);
		}
    }
	
	public void setWaitCursor() {
		getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getActionCounter().incrementAndGet();
	}

	public void setDefaultCursor() {
		getActionCounter().decrementAndGet();
		if (getActionCounter().get() < 1) {
			getRootPane().setCursor(Cursor.getDefaultCursor());
		}
	}
	
	protected JButton getCopyButton() {
		if (this.copyButton == null) {
			this.copyButton = new JButton("<html><b>&#9660 Transfer Selected Records to Destination &#9660</b></html>");
			this.copyButton.setName("CopyButton");
			this.copyButton.setPreferredSize(new Dimension(400, 55));
			this.copyButton.setEnabled(false);
		}
		return this.copyButton;
	}
	
	protected void enableCopyButton() {
		 copyButton.setEnabled(this.isReadyToCopy());
	}
	
	protected void handleException (Exception e) {
		super.handleException(e);
	}
    
    protected String getUserName() {
		return super.getUserName();
	}
}
