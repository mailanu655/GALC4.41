package com.honda.galc.client.datacollection.view.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.DeviceTableModel;
import com.honda.galc.client.common.component.PropertyTableModel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.property.ClassicViewPropertyBean;
import com.honda.galc.client.datacollection.property.CommonViewPropertyBean;
import com.honda.galc.client.datacollection.property.DataSyncProperty;
import com.honda.galc.client.datacollection.property.DefaultViewPropertyBean;
import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.property.ProductSequencePropertyBean;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.property.ViewManagerPropertyBean;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.device.IDevice;
import com.honda.galc.property.IProperty;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
/**
 * 
 * <h3>PropertyInfoManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PropertyInfoManager description </p>
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
 * @author Paul Chou
 * Jun 1, 2010
 *
 */
public class PropertyInfoManager implements ListSelectionListener{
	private static String PROPERTY_PACKAGE_PREFIX="com.honda.galc.client.datacollection.property.";
	private ClientContext context;
	private String fsmType;
	private PropertyPanel panel;
	private Object viewPropertyBean;
	
	public PropertyInfoManager(ClientContext context, PropertyPanel panel) {
		this.context = context;
		this.panel = panel;
		
		initialize();
	}

	private void initialize() {
		fsmType = context.getProperty().getFsmType();
		
		initConnections();
		
		initScreen();
	}
	
	private void initConnections() {
		getPanel().getSelectionList().getComponent().addListSelectionListener(this);
		
	}

	private void initScreen() {
		ListModel<KeyValue<String, List<Class<?>>>> listModel = getPropertyListModel();
		panel.getSelectionList().getLabel().setText("FSM: " + fsmType);
		panel.getSelectionList().getComponent().setModel(listModel);
		panel.getSelectionList().getComponent().setCellRenderer(listModel);
		
	}

	private ListModel<KeyValue<String, List<Class<?>>>> getPropertyListModel() {
		List<KeyValue<String, List<Class<?>>>> list = getPropertyList();
		return new ListModel<KeyValue<String, List<Class<?>>>>(list, "getKey");
	}

	private List<KeyValue<String, List<Class<?>>>> getPropertyList() {
		List<KeyValue<String, List<Class<?>>>> list = new ArrayList<KeyValue<String, List<Class<?>>>>();
		addToList(list, "Lot Control Properties", LotControlPropertyBean.class);
		addToList(list, "Data Sync Properties", DataSyncProperty.class);
		addToList(list, "Audio Properties", AudioPropertyBean.class);
		
		Hashtable<String, IDevice> devices = DeviceManager.getInstance().getDevices();
		if(devices != null){
			for(String deviceKey: devices.keySet()){
				addToList(list, deviceKey, IDevice.class);
			}
		}
	
		//Add client view properties
		addToList(list, "View Controller", ViewManagerPropertyBean.class);
		if(fsmType.equalsIgnoreCase(FsmType.DEFAULT.toString())){
			addToList(list, "View Properties", DefaultViewPropertyBean.class);
			addToList(list, "Product Sequence", ProductSequencePropertyBean.class);
			
		} else {
			addToList(list, "View Properties", ClassicViewPropertyBean.class);
		}
		
		addProductSpedificProperties(list);
		
			
		return list;
	}


	private void addProductSpedificProperties(List<KeyValue<String, List<Class<?>>>> list) {
		String productName = StringUtils.capitalize(context.getProperty().getProductType().toLowerCase());
		String propertyName = productName + " Properties";
		Class<?> prdClz = null;
		try {
			prdClz = Class.forName(PROPERTY_PACKAGE_PREFIX + productName + "PropertyBean");
		} catch (Exception e) {
			//OK may not defined 
			return;
		}
		
		if(prdClz != null)
			addToList(list, propertyName, prdClz);
		
	}

	@SuppressWarnings("unchecked")
	private void addToList(List<KeyValue<String, List<Class<?>>>> list, String key, Class<?> clzz) {
		//append if exists
		for(KeyValue<String, List<Class<?>>> keyValue: list){
			if(keyValue.getKey().equals(key))
			{
				keyValue.getValue().add(clzz);
				return;
			} 
		}

		//add new
		List<Class<?>> classList = new ArrayList<Class<?>>();
		classList.add(clzz);
		list.add(new KeyValue(key, classList));
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(panel.getSelectionList().getComponent())) showProperties();
	}
	
	private void showProperties() {
		KeyValue<String, List<Class<?>>> selected = getSelectedItem();

		showSelected(selected);
		
	}
	
	private void showSelected(KeyValue<String, List<Class<?>>> selected) {
		if(selected == null){ 
			getPanel().getPropertyLabel().setText("");
			new PropertyTableModel(null, getPanel().getLabeledTablePanel().getTablePanel().getTable());
			return;
		}
		
		getPanel().getPropertyLabel().setText(selected.getKey());
		createPropertyTableModel(selected);
		
	}

	public PropertyPanel getPanel() {
		return panel;
	}

	@SuppressWarnings("unchecked")
	private TableModel createPropertyTableModel(KeyValue<String, List<Class<?>>> selected) {
		List<Class<?>> clzList = selected.getValue();
		TableModel model = null;
		List<KeyValue<String, String>> propertyList = new ArrayList<KeyValue<String,String>>();
		for(Class<?> clz : clzList){
			
			List<Class> intferfaces = findAllInterfaces(clz);
			if(intferfaces.contains(IProperty.class)){
				propertyList.addAll(PropertyTableModel.getPropertyList((Class<? extends IProperty>)clz, getBeanInstance(intferfaces)));
				
			} else if(intferfaces.contains(IDevice.class)){
				model = new DeviceTableModel((Class<IDevice>)clz, getDeviceBeanInstance(selected.getKey()), getPanel().getLabeledTablePanel().getTablePanel().getTable());
			}
		}
		
		if(model == null)
			model = new PropertyTableModel(propertyList,  getPanel().getLabeledTablePanel().getTablePanel().getTable());
		
		return model;
	}
	

	private Object getDeviceBeanInstance(String key) {
		
		return DeviceManager.getInstance().getDevices().get(key);
	}

	@SuppressWarnings("unchecked")
	private Object getBeanInstance(List<Class> interfaces) {
		if(interfaces.contains(CommonViewPropertyBean.class))
			return PropertyService.getPropertyBean(getViewPropertyBeanClass(), context.getProcessPointId());
		else if(interfaces.contains(ProductSequencePropertyBean.class))
			return PropertyService.getPropertyBean(ProductSequencePropertyBean.class, context.getProcessPointId());
		else if(interfaces.contains(TerminalPropertyBean.class))
			return context.getProperty();
		else
			return PropertyService.getPropertyBean(interfaces.get(0), context.getProcessPointId());
	}

	@SuppressWarnings("unchecked")
	private List<Class> findAllInterfaces(Class<?> clz) {
		List<Class> interfaces = new ArrayList<Class>();
		findAllInterfaces(clz, interfaces);
		return interfaces;
	}


	@SuppressWarnings("unchecked")
	private KeyValue<String, List<Class<?>>> getSelectedItem() {
		KeyValue<String, List<Class<?>>> selected = ((KeyValue<String, List<Class<?>>>)panel.getSelectionList().getComponent().getSelectedValue());
		return selected;
	}

	public Object getViewPropertyBean() {
		if(viewPropertyBean == null){
			viewPropertyBean = PropertyService.getPropertyBean(getViewPropertyBeanClass(), context.getProcessPointId());
		}
		return viewPropertyBean;
	}

	private Class<? extends IProperty> getViewPropertyBeanClass() {
		if(context.getProperty().getFsmType().equalsIgnoreCase(FsmType.DEFAULT.toString())){
			return DefaultViewPropertyBean.class;
		} else {
			return ClassicViewPropertyBean.class;
		}
	}
	
	
	@SuppressWarnings({"unchecked" })
	private void findAllInterfaces(Class c, List<Class> list) {
		if(c.isInterface())
			list.add(c);
		Class[] directInterfaces = c.getInterfaces();
		list.addAll(Arrays.asList(directInterfaces));
		
		for(int i = 0; i < directInterfaces.length; i++){
			findAllInterfaces(directInterfaces[i], list);
		}
	}
}
