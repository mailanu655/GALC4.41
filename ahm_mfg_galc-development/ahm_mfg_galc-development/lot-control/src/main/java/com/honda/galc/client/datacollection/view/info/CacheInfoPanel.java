package com.honda.galc.client.datacollection.view.info;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.common.LotControlAccessControlManager;
import com.honda.galc.client.common.data.InstalledPartTableModel;
import com.honda.galc.client.common.data.MeasurementTableModel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.InstalledPartCache;
import com.honda.galc.client.datacollection.sync.DataCacheSyncManager;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.client.ui.component.SplitInfoPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.util.KeyValue;
/**
 * 
 * <h3>CacheInfoPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CacheInfoPanel description </p>
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
 * May 7, 2010
 *
 */
public class CacheInfoPanel extends JPanel 
implements ListSelectionListener, ActionListener, InformationPanel{
	private static final long serialVersionUID = 5724784636807460812L;

	private String title;
	private InstalledPartPanel installedPartPanel;
	private List<Integer> currentDisplayKeys = new ArrayList<Integer>();
	private int pageSize = 30;//default to 30
	private SplitInfoPanel dataPanel;
	private JPanel buttonPanel;

	private JButton refreshButton;
	private JButton nextButton;
	private JButton previousBtton;
	private JButton deleteButton;
	private ClientContext context;
	
	
	public CacheInfoPanel(ClientContext context) {
		super();
		this.context = context;
		initialize();
	}
	
	public CacheInfoPanel() {
		super();
		initialize();
	}
	
	
	private void initialize() {
		pageSize = context.getProperty().getCacheDisplayPageSize();
		setLayout(new BorderLayout());

		initComponents();	
		
		initConnections();
		
		initScreen();
	}

	private void initComponents() {
		add(getDataPanel(), BorderLayout.NORTH);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}


	private void initConnections() {
		getProductList().getComponent().addListSelectionListener(this);
		getRefreshButton().addActionListener(this);
		getPreviousBtton().addActionListener(this);
		getNextButton().addActionListener(this);
		getDeleteButton().addActionListener(this);
		
	}


	

	private TablePane getInstalledPartTablePanel() {
		return getInstalledPartPanel().getInstalledPartTablePanel();
	}

	private LabeledListBox getProductList() {
		return getDataPanel().getSelectionList();
	}

	public void initScreen(){
    	disableIterationButtons();
    	getInitalDisplayKeys();
    	
		setDataDisplay();
	}

	private void setDataDisplay() {
		title = "Cache Data:" + getCachedKeys().size();
		dataPanel.getSelectionList().getLabel().setText(title);
		new MeasurementTableModel(null, getMeasurementTablePanel().getTable());
		new InstalledPartTableModel(null, getInstalledPartTablePanel().getTable());
		ListModel<KeyValue<Integer, String>> listModel = new ListModel<KeyValue<Integer, String>>(getProductList(currentDisplayKeys), "getValue");
		getProductList().getComponent().setModel(listModel);
		getProductList().getComponent().setCellRenderer(listModel);
	}
	
	private void disableIterationButtons() {
		getPreviousBtton().setEnabled(false);
		getNextButton().setEnabled(false);
		getDeleteButton().setEnabled(false);
		
	}

	private void getInitalDisplayKeys() {
		List<Integer> keys = getCachedKeys();
    	
    	if(keys.size() > pageSize){
    		getNextButton().setEnabled(true);
    		currentDisplayKeys.addAll(keys.subList(0, pageSize));
    		
    	} else {
    		currentDisplayKeys.addAll(keys);
    	}
		
	}

	private List<Integer> getCachedKeys() {
		List <Integer> keys = getCache().getKeys();
		if (keys.size() > 0)
			Collections.sort(keys);
		return keys;
	}

	@SuppressWarnings("unchecked")
	private List<KeyValue<Integer, String>> getProductList(List<Integer> keys) {
		List<KeyValue<Integer, String>> list = new ArrayList<KeyValue<Integer, String>>();
		for(int i = 0; i < keys.size(); i++){
			List<InstalledPart> installedParts = getCache().get(keys.get(i), List.class);
			
			if(installedParts == null) continue;
			
			if(!isValidCacheData(installedParts)) {
				Logger.getLogger().warn("Invalid data found in Cache:" + keys.get(i));
				continue;
		    }
			
			list.add(new KeyValue<Integer, String>(keys.get(i), installedParts.get(0).getId().getProductId()));
		}
		
		return list;
	}

	private boolean isValidCacheData(List<InstalledPart> installedParts) {
		return installedParts != null && installedParts.size() > 0 && installedParts.get(0) != null && installedParts.get(0).getId() != null;
	}

	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(getProductList().getComponent())) {
			KeyValue<Integer, String> selectedValue = (KeyValue<Integer, String>)getProductList().getComponent().getSelectedValue();
			if(selectedValue == null){
				getDeleteButton().setEnabled(false);
				return;
			} else {
				showCollectedData(selectedValue);
				getDeleteButton().setEnabled(true);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	private void showCollectedData(KeyValue<Integer, String> selectedValue) {
		
		List<InstalledPart> installedParts = getCache().get(selectedValue.getKey(), List.class);
		if(installedParts == null || installedParts.size() == 0) return;
		
		//show installed parts
		new InstalledPartTableModel(installedParts, getInstalledPartTablePanel().getTable());
		
		//show measurements
		new MeasurementTableModel(getAllMeasurements(installedParts),getMeasurementTablePanel().getTable());
	}
	
	private TablePane getMeasurementTablePanel() {
		return getInstalledPartPanel().getMeasurementTablePanel();
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

	public InstalledPartCache getCache() {
		return InstalledPartCache.getInstance();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public SplitInfoPanel getDataPanel() {
		if(dataPanel == null){
			dataPanel = new SplitInfoPanel();
			dataPanel.setDetailsPanel(getInstalledPartPanel());
			dataPanel.getSelectionList().getLabel().setText(title);
			dataPanel.initialize();
		}
		return dataPanel;
	}

	private InstalledPartPanel getInstalledPartPanel() {
		if(installedPartPanel == null){
			installedPartPanel = new InstalledPartPanel();
		}
		return installedPartPanel;
	}


	public JPanel getButtonPanel() {
		if(buttonPanel == null){
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
			buttonPanel.add(getPreviousBtton());
			buttonPanel.add(getNextButton());
			buttonPanel.add(getRefreshButton());
			buttonPanel.add(Box.createRigidArea(new Dimension(50,0)));
			buttonPanel.add(getDeleteButton());
		}
		return buttonPanel;
	}

	private JButton getRefreshButton() {
		if(refreshButton == null){
			refreshButton = new JButton("Refesh");
		}
		return refreshButton;
	}

	private JButton getNextButton() {
		if(nextButton == null){
			nextButton = new JButton("Next >>");
			nextButton.setEnabled(false);
		}
		return nextButton;
	}

	private JButton getPreviousBtton() {
		if(previousBtton == null)
			previousBtton = new JButton("<< Previous");
		return previousBtton;
	}
	

	public JButton getDeleteButton() {
		if(deleteButton == null){
			deleteButton = new JButton("Delete");
		}
		return deleteButton;
	}

	public void setButtonPanel(JPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}


	public void actionPerformed(ActionEvent e) {
		if(getRefreshButton() == e.getSource()){
			refresh();
		} else if (getPreviousBtton() == e.getSource()){
			displayPreviousPage();
		} else if(getNextButton() == e.getSource()){
			displayNextPage();
		} else if(getDeleteButton() == e.getSource()){
		    if(context.getProperty().isNeedAuthorizedUserToDeleteCacheData() && 
		    		!LotControlAccessControlManager.getInstance().login())
		    	return;
		    	
			deleteSelectedFromCache();
		}
		
	}

	public void refresh() {
		currentDisplayKeys.clear();
		initScreen();
		
	}

	private void displayPreviousPage() {
		List<Integer> cachedKeys = getCachedKeys();
		
		Integer firstKeyInCurrentPage = currentDisplayKeys.get(0);
		int indexOfLastKey = cachedKeys.indexOf(firstKeyInCurrentPage);
		currentDisplayKeys.clear();
		
		int indexOfFirstKey = indexOfLastKey - pageSize;
		if(indexOfFirstKey <= 0){
			indexOfFirstKey = 0;
			getPreviousBtton().setEnabled(false);
		}
		currentDisplayKeys.addAll(cachedKeys.subList(indexOfFirstKey, indexOfLastKey));

		getNextButton().setEnabled(true);
		setDataDisplay();
	}

	private void displayNextPage() {
		List<Integer> cachedKeys = getCachedKeys();
		
		Integer lastKeyInCurrentPage = currentDisplayKeys.get(Integer.valueOf(currentDisplayKeys.size() -1));
		int indexOfFirstKey = cachedKeys.indexOf(lastKeyInCurrentPage) + 1;
		currentDisplayKeys.clear();
		
		int indexOfLastKey = indexOfFirstKey + pageSize;
		if(indexOfLastKey > cachedKeys.size())
		{
			indexOfLastKey = cachedKeys.size();
			getNextButton().setEnabled(false);
		}
		currentDisplayKeys.addAll(cachedKeys.subList(indexOfFirstKey, indexOfLastKey));
		
		setDataDisplay();
		
		//enable display previous page button
		getPreviousBtton().setEnabled(true);
		
	}
	
	@SuppressWarnings("unchecked")
	private void deleteSelectedFromCache() {
		
		try{
			//get selected product(s)
			Object[] selectedValues = getProductList().getComponent().getSelectedValues();
			for(int i = 0; i < selectedValues.length; i++){
				Logger.getLogger().info(((KeyValue< Integer, String>)selectedValues[i]).getKey() + " is removed from cache.");
				Logger.getLogger().info(DataCacheSyncManager.getInstalledPartsDetails(getCache().get(((KeyValue<Integer, String>)selectedValues[i]).getKey(), List.class)));

				getCache().remove(((KeyValue<Integer, String>)selectedValues[i]).getKey());
				getCache().flush();
			}

			refresh();
		} catch(Exception e){
			Logger.getLogger().debug(e, "Exception was thrown when remove selected item from cache.");
			MessageDialog.showError("Exception occurred to remove selected item. The data may not exist in cache.");
		}
		
		
	}

}
