package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

public class LogLevelPanel extends TabbedPanel implements ListSelectionListener, TableModelListener{

	private static final long serialVersionUID = 1L;
	
	private TablePane logLevelPane;
	
	private LabeledListBox terminalNameListBox;
	
	private LogLevelTableModel logLevelTableModel;
	
	private ListModel<String> terminalListModels;
	
	private JPanel buttonPanel = new JPanel();
	
	private JButton addButton = new JButton("<=   Add   ");
	private JButton removeButton = new JButton("=> Remove");
	
	private LabeledTextField terminalField = new LabeledTextField("Application Name",false);
	
	
	private List<Terminal> allTerminals;
	
	private List<String> availableTerminalNames = new SortedArrayList<String>();
	
	public LogLevelPanel(){
		super("Log Level", KeyEvent.VK_L);
		initComponents();
		addListeners();
	}
	
	


	private void initComponents() {
		
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		add(Box.createHorizontalStrut(150));
		add(createLogLevelPane());
		add(Box.createHorizontalStrut(50));
		add(createButtonPanel());
		add(Box.createHorizontalStrut(50));
		add(createTerminalNameListBox());
		add(Box.createHorizontalStrut(150));
		
	}
	
	private void addListeners() {
		
		terminalNameListBox.getComponent().addListSelectionListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		
	}

	
	private TablePane createLogLevelPane() {
		logLevelPane = new TablePane("Log Level");
		int height = logLevelPane.getPreferredSize().height;
		logLevelPane.setPreferredSize(new Dimension(300,height));
		return logLevelPane;
	}
	
	private JPanel createButtonPanel() {
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(addButton);
		buttonPanel.add(Box.createVerticalStrut(20));
		buttonPanel.add(removeButton);
		buttonPanel.add(Box.createVerticalGlue());
		return buttonPanel;
	}
	
	private JPanel createTerminalNameListBox() {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
		terminalField.setPreferredHeight(30);
		terminalField.setMaxHight(30);
		terminalNameListBox = new LabeledListBox("Select From Terminal List");
		terminalNameListBox.getComponent().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rightPanel.add(terminalField);
		rightPanel.add(terminalNameListBox);
		return rightPanel;
	}
	
	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		
		List<ComponentProperty> logLevels = ServiceFactory.getDao(ComponentPropertyDao.class).findAllLogLevels();
		
		logLevelTableModel = new LogLevelTableModel(logLevels,logLevelPane.getTable());

		logLevelTableModel.addTableModelListener(this);
	
		allTerminals = ServiceFactory.getDao(TerminalDao.class).findAll();
		
		deriveAvailableTerminalNames();
		
		terminalListModels = new ListModel<String>(availableTerminalNames,"toString");
		terminalNameListBox.getComponent().setModel(terminalListModels);
		
		
		isInitialized = true;
	}
	
	

	private void deriveAvailableTerminalNames() {
		availableTerminalNames.clear();
		for(Terminal terminal : allTerminals) {
			availableTerminalNames.add(terminal.getHostName());
		}
		
		for(ComponentProperty logLevel : logLevelTableModel.getItems()) {
			availableTerminalNames.remove(logLevel.getId().getComponentId());
		}
		
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addButton)) addApplicationLogLevel();
		else if(e.getSource().equals(removeButton)) removeApplicationLogLevel();
	}
	

	private void addApplicationLogLevel() {
		String applicationName = terminalField.getComponent().getText();
		if(StringUtils.isEmpty(applicationName)) {
			MessageDialog.showError("Please input application name or select from terminal list");
		}else {
			try{
				ComponentProperty componentProperty = new ComponentProperty(applicationName, LogLevel.LOG_LEVEL,LogLevel.INFO.toString());
				componentProperty = ServiceFactory.getDao(ComponentPropertyDao.class).save(componentProperty);
				logUserAction(SAVED, componentProperty);
				logLevelTableModel.add(componentProperty);
				terminalListModels.remove(applicationName);
				terminalField.clear();
				
			}catch(Exception e) {
				MessageDialog.showError(this,"could not add log level setting due to " + e.getMessage());
			}
		}
		
	}

	private void removeApplicationLogLevel() {
		ComponentProperty item = logLevelTableModel.getSelectedItem();
		if(item == null) return;
		
		try{
			ServiceFactory.getDao(ComponentPropertyDao.class).remove(item);
			logUserAction(REMOVED, item);
			logLevelTableModel.remove(item);
		    terminalListModels.add(item.getId().getComponentId());
		}catch (Exception e){
			MessageDialog.showError(this,"could not delete log level setting due to " + e.getMessage());
		}
		
	}





	private class LogLevelTableModel extends BaseTableModel<ComponentProperty> {
		
		private static final long serialVersionUID = 1L;

		public LogLevelTableModel(List<ComponentProperty> items,JTable table) {
			super(items, new String[] {"Application","Log Level"},table);
			table.setRowHeight(25);
//			setColumnWidths(new int[]{100, 120});
			setComboBoxCell();
		}
		
		public boolean isCellEditable (int row, int column){
			return column >= 1;
	    }
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			ComponentProperty logLevel = getItem(rowIndex);
	        
	        switch(columnIndex) {
	            case 0: return logLevel.getId().getComponentId();
	            case 1: return LogLevel.getLogLevel(logLevel.getPropertyValue());
	        }
	        return null;
		}
		
		public void setValueAt(Object value, int row, int column) {
			if(row >= getRowCount() || column <=0) return;
			ComponentProperty logLevel = getItem(row);
			if(!MessageDialog.confirm(LogLevelPanel.this,"Are you sure that you want change the log level? ")) 
				return;
	        logLevel.setPropertyValue(value.toString());
	        
	        fireTableCellUpdated(row, column);
		}
		
		private void setComboBoxCell() {
			Object [] logLevels = LogLevel.values();
			TableColumn col = table.getColumnModel().getColumn(1);
			col.setCellEditor(new ComboBoxCellEditor(logLevels));
			col.setCellRenderer(new ComboBoxCellRender(logLevels));
			
		}
	}


	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		if(e.getFirstIndex() == -1) terminalField.getComponent().setText("");
		else terminalField.getComponent().setText((String)terminalNameListBox.getComponent().getSelectedValue());
		
	}




	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof LogLevelTableModel) {
			ComponentProperty item = logLevelTableModel.getSelectedItem();
			if(item == null) return;
			try{
				ServiceFactory.getDao(ComponentPropertyDao.class).update(item);
				logUserAction(UPDATED, item);
			}catch(Exception ex) {
				
				logLevelTableModel.rollback();
				MessageDialog.showError(this,"could not update log level setting due to " + ex.getMessage());

			}
			
			
		}
	}
	


}
