package com.honda.galc.client.datacollection.view.info;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.common.component.PropertyTableModel;
import com.honda.galc.client.common.data.InstalledPartTableModel;
import com.honda.galc.client.common.data.MeasurementTableModel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.client.ui.component.SplitInfoPanel;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>StateInfoManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> StateInfoManager description </p>
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
 * May 28, 2010
 *
 */
public class StateInfoManager implements ListSelectionListener{

	private LotControlRulePanel lotControlRulePanel;
	private SplitInfoPanel managerPanel;
	private InstalledPartPanel installedPartPanel;
	private LabeledTablePanel statePanel;
	private ClientContext context;
	enum StateItem {State, LotControlRule, Data}
	
	public StateInfoManager(ClientContext context) {
		super();
		this.context = context;
		
		initialize();
	}
	
	public StateInfoManager(ClientContext context, SplitInfoPanel managerPanel) {
		super();
		this.context = context;
		this.managerPanel = managerPanel;
		
		initialize();
	}


	private void initialize() {
		
		initComponents();
		
		initConnections();
		initScreen();
	}

    private void initComponents() {
    	getManagerPanel();
		getLotControlRulePanel();
		getStatePanel();
		getInstalledPartPanel();
	}

	private void initConnections() {
		getManagerPanel().getSelectionList().getComponent().addListSelectionListener(this);
		
	}

	//Getter & Setters
	public LotControlRulePanel getLotControlRulePanel() {
		if(lotControlRulePanel == null){
			lotControlRulePanel = new LotControlRulePanel();
		}
		return lotControlRulePanel;
	}

	public SplitInfoPanel getManagerPanel() {
		if(managerPanel == null){
			managerPanel = new SplitInfoPanel();
			managerPanel.initialize();
		}
		return managerPanel;
	}
	
	public void setManagerPanel(SplitInfoPanel managerPanel) {
		this.managerPanel = managerPanel;
	}
	

	public InstalledPartPanel getInstalledPartPanel() {
		if(installedPartPanel == null){
			installedPartPanel = new InstalledPartPanel();
		}
		return installedPartPanel;
	}

	public LabeledTablePanel getStatePanel() {
		if(statePanel == null){
			statePanel = new LabeledTablePanel("State Information:");
		}
		return statePanel;
	}

	private DataCollectionState getCurrentState() {
		return DataCollectionController.getInstance().getState();
	}
	
	public void refresh(){
		initScreen();
	}
	
	public void initScreen(){
		List<StateItem> selectionList = new ArrayList<StateItem>();
		String productSpec = getCurrentState().getProductSpecCode();
		getManagerPanel().getSelectionList().getLabel().setText(productSpec == null ? " " : productSpec);
		
		if(productSpec != null) selectionList.add(StateItem.State);
		if(getCurrentState().getLotControlRules() != null && getCurrentState().getLotControlRules().size() > 0)
			selectionList.add(StateItem.LotControlRule);
		if(getCurrentState().getProduct() != null) selectionList.add(StateItem.Data);
		
		ListModel<StateItem> listModel = new ListModel<StateItem>(selectionList, "toString");
		getManagerPanel().getSelectionList().getComponent().setModel(listModel);
		getManagerPanel().getSelectionList().getComponent().setCellRenderer(listModel);
		
		//reset table contents
		new PropertyTableModel(null , getStatePanel().getTablePanel().getTable());
		new InstalledPartTableModel(null, getInstalledPartPanel().getInstalledPartTablePanel().getTable());
		new MeasurementTableModel(null,getInstalledPartPanel().getMeasurementTablePanel().getTable());
		getLotControlRulePanel().refresh();
		
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(getManagerPanel().getSelectionList().getComponent())) showStateInfo();
		
	}

	private void showStateInfo() {
		StateItem selected = (StateItem)getManagerPanel().getSelectionList().getComponent().getSelectedValue();
		
		if(StateItem.State == selected) showState();
		else if(StateItem.LotControlRule == selected) showLotControlRules();
		else if(StateItem.Data == selected) showCollectedData();
		
	}

	private void showCollectedData() {
		new InstalledPartTableModel(getInstalledParts(), getInstalledPartPanel().getInstalledPartTablePanel().getTable());
		//show measurements
		new MeasurementTableModel(getAllMeasurements(getInstalledParts()),getInstalledPartPanel().getMeasurementTablePanel().getTable());
		
		setDetailPanel(getInstalledPartPanel());

	}

	private void setDetailPanel(JPanel panel) {
    	Component[] components = getManagerPanel().getDetailPanel().getComponents();
    	List<Component> list = Arrays.asList(components);
    	
    	if(!list.contains(panel)){
    		getManagerPanel().getDetailPanel().removeAll();
    		getManagerPanel().getDetailPanel().add(panel, BorderLayout.NORTH);
    	}

    	getManagerPanel().getDetailPanel().validate();
    	getManagerPanel().getDetailPanel().repaint();
		
	}

	private List<Measurement> getAllMeasurements(List<InstalledPart> installedParts) {
		List<Measurement> list = new ArrayList<Measurement>();
	    for(InstalledPart part : installedParts){
	    	if(part != null && part.getMeasurements() != null && part.getMeasurements().size() > 0){
	    		list.addAll(part.getMeasurements());
	    	}
	    }
		return list;
	}

	private List<InstalledPart> getInstalledParts() {
		return getCurrentState().getProduct().getPartList();
	}

	private void showLotControlRules() {
		getLotControlRulePanel().refresh();
		setDetailPanel(getLotControlRulePanel());
		
	}

	private void showState() {
		List<KeyValue<String, String>> items = getStateTableDataList(DataCollectionController.getInstance().getState());
		new PropertyTableModel(items , getStatePanel().getTablePanel().getTable());
		
		setDetailPanel(getStatePanel());
	}

	private List<KeyValue<String, String>> getStateTableDataList(
			DataCollectionState state) {
		List<KeyValue<String, String>> list = new ArrayList<KeyValue<String,String>>();
		if(state != null){
			list.add(new KeyValue<String, String>("PRODUCT_ID",state.getProductId()));
			list.add(new KeyValue<String, String>("PRODUCT_SPEC",state.getProductSpecCode()));
			if(context.isCheckExpectedProductId())
				list.add(new KeyValue<String, String>("EXPECTED_PRODUCT_ID",state.getExpectedProductId()));
			list.add(new KeyValue<String, String>("PART_NAME", state.getCurrentPartName()));
			list.add(new KeyValue<String, String>("PART_INDEX", String.valueOf(state.getCurrentPartIndex())));
			list.add(new KeyValue<String, String>("CURRENT_TORQUE_INDEX",String.valueOf(state.getCurrentTorqueIndex())));
			list.add(new KeyValue<String, String>("STATE",state.getClass().getSimpleName()));
		}
		return list;
	}
}
