package com.honda.galc.client.datacollection.view.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>LogInfoManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LogInfoManager description </p>
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
 * May 18, 2010
 *
 */
public class LogInfoManager implements ListSelectionListener{
	private static volatile LogInfoManager INSTANCE;
	public static final String CURRENT_LOG_PRODUCT="CURRENT_LOG_PRODUCT:";
	private String lineSeparator = System.getProperty("line.separator");
	private List<List<LogRecord>> logList = Collections.synchronizedList(new ArrayList<List<LogRecord>>());
	private LogInfoPanel logPanel;
	private LotControlSystemInfo sysInfo;
	private String selectedItem;
	private String logLevel;
	private int maxLogEntries;
	private Map<String, String> filters;
	

	private LogInfoManager() 
	{
		super();
		init();
	}

	public static LogInfoManager getInstance() {
		synchronized(LogInfoManager.class) {
			if (INSTANCE == null)
				INSTANCE = new LogInfoManager();
		}
		return INSTANCE;
	}

	private void init() {
		AnnotationProcessor.process(this);

		logList.add(Collections.synchronizedList(new ArrayList<LogRecord>()));
	}

	public void setConnections(LogInfoPanel logPanel, LotControlSystemInfo sysInfoPanel){
		this.logPanel = logPanel;
		this.sysInfo = sysInfoPanel;

		initConnections();
	}

	private void initConnections() {
		logLevel = sysInfo.getContext().getProperty().getClientInfoLogLevel();
		filters = sysInfo.getContext().getProperty().getClientLogFilter();
		logPanel.getLogItemList().getComponent().addListSelectionListener(this);
		maxLogEntries =  sysInfo.getContext().getProperty().getClientInfoLogMaxEntries();

	}

	@EventSubscriber(eventClass = LogRecord.class)
	public void append(LogRecord event){
		//add LogRecord to the current list
		if(!isFilterOut(event)) {
			getCurrentLog().add(event);

			if(isLogPanelCurrentPanel() && needLog(event))
				appendToDetailView(event);

			//Keep the log size under a safe level
			if(getCurrentLog().size() > maxLogEntries){
				getCurrentLog().remove(0);
			}
		}
	}

	private List<LogRecord> getCurrentLog() {
		return logList.get(logList.size() -1);
	}

	private boolean isLogPanelCurrentPanel() {

		return sysInfo != null && logPanel != null && logPanel == sysInfo.getCurrentPanel();
	}

	public List<List<LogRecord>> getLogList() {
		return logList;
	}

	public void setLogList(List<List<LogRecord>> logList) {
		this.logList = logList;
	}

	public void finishProduct(){
		logList.add(Collections.synchronizedList(new ArrayList<LogRecord>()));

		while(logList.size() > sysInfo.getContext().getProperty().getClientInfoLogSize()){
			logList.remove(0);
		}
	}

	public void newProduct(String productId){
		getCurrentLog().add(0, new LogRecord(CURRENT_LOG_PRODUCT + productId));
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(logPanel.getLogItemList().getComponent())) logSelected();

	}

	private void logSelected() {
		KeyValue<String, Integer> selected = getSelectedItem();

		if(selected == null){ 
			logPanel.getInformationTextArea().setText("");
			return;
		}

		selectedItem = selected.getKey();
		showLogDetails(selected.getValue());

	}

	@SuppressWarnings("unchecked")
	private KeyValue<String, Integer> getSelectedItem() {
		KeyValue<String, Integer> selected = ((KeyValue<String, Integer>)logPanel.getLogItemList().getComponent().getSelectedValue());
		return selected;
	}

	private void showLogDetails(int index){
		StringBuilder msg = new StringBuilder();
		for(LogRecord event : logList.get(index)){

			if(needLog(event)){
				if(event.getMessage().contains(CURRENT_LOG_PRODUCT)) continue;
				msg.append(event.getSingleLineMessage()).append(lineSeparator);
			}
		}

		logPanel.setText(msg.toString());
	}

	private boolean isFilterOut(LogRecord event) {
	
		if(filters == null) return false;
		
		for(String attribute : filters.keySet()){
			//Only two attribute supported now - so no reflections 
			String[] filterAttrs = filters.get(attribute).split(";");
			if(filterAttrs.length == 0) return false;
			
			if(attribute.equalsIgnoreCase("ThreadName")){
				return isFilterOut(event.getLogContext().getThreadName(), filterAttrs);
			} else if(attribute.equalsIgnoreCase("ApplicationName")){
				return isFilterOut(event.getLogContext().getApplicationName(), filterAttrs);
			}
		}
			
		return false;
	}

	private boolean isFilterOut(String attribute, String[] filterAttrs) {
		if(attribute == null) return false;
				
		for(int i = 0; i < filterAttrs.length; i++){
			if(attribute.equals(filterAttrs.clone()[i].trim()))
				return true;
		}
		return false;
	}

	private boolean needLog(LogRecord event) {
		return event.getLogLevel().isHigher(LogLevel.getLogLevel(logLevel)); 
	}

	private void initScreen(){
		List<KeyValue<String, Integer>> list = getLogProductList();

		ListModel<KeyValue<String, Integer>> listModel = new ListModel<KeyValue<String, Integer>>(list, "getKey");
		//DefaultListModel listModel = new DefaultListModel();
		logPanel.getLogItemList().getComponent().setModel(listModel);
		logPanel.getLogItemList().getComponent().setCellRenderer(listModel);
	}

	private List<KeyValue<String, Integer>> getLogProductList() {
		List<KeyValue<String, Integer>> list = new ArrayList<KeyValue<String, Integer>>();

		int i = 0;
		for(List<LogRecord> records: logList){
			list.add(new KeyValue<String, Integer>(findProductId(records, "Wait for Product"), i));
			i++;
		}
		return list;
	}

	private String findProductId(List<LogRecord> records, String defaultId) {
		if(records.size() == 0) return defaultId;
		if(records.get(0).getMessage().contains(CURRENT_LOG_PRODUCT)){
			String[] tokens = records.get(0).getMessage().split(":");
			if(tokens.length >= 2) return tokens[1];
		}

		return defaultId;
	}

	public void refresh() {
		initScreen();
	}
	
	public void appendToDetailView(final LogRecord event){
		Runnable r = new Runnable() {
			public void run()
			{
				updateDetailView(event);
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	@SuppressWarnings("unchecked")
	private void updateDetailView(LogRecord event) {
		if(logPanel.getLogItemList().getComponent().getModel().getSize() == logList.size()){
			javax.swing.ListModel model = logPanel.getLogItemList().getComponent().getModel();
			Object lastElement = model.getElementAt(model.getSize() -1);
			if(lastElement != null){
				if(((KeyValue<String, Integer>)lastElement).getKey().equals(selectedItem))
					logPanel.apend(event);
			}
		}
		
	}


}
