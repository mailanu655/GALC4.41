package com.honda.galc.tools.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.honda.galc.entity.conf.ProcessPoint;

/**
 * <h3>Class description</h3>
 * This is the user interface for client utility tools.
 * .
 * <h4>Description</h4>
 * <p>
 *   GALC client configuration tool is used to backup,
 *   remove and restore client configurations.
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Aug 17, 2012</TD>
 * <TD>1.0</TD>
 * <TD>20120817</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */

public class ClientToolsWindow extends JFrame implements TableModelListener {
	private static final long serialVersionUID = 2728645680770659564L;

	protected Object[] headers = {"Name", "Description", "URL"};
	protected Object[][] servers = {{"Manual Entry", "Enter server URL in next column", "http://hostname:port/BaseWeb/HttpServiceHandler"}};
	private List<ProcessPoint> processPointList;
	
	protected JTable serverTable;
	private JTable processPointTable;
	private JPanel centerPanel;
	private JButton getProcessPointButton;
	private JButton backupButton;
	private JButton restoreButton;
	protected JButton removeButton;
	protected JTextField directoryTextField;
	private JButton chooseDirectoryButton;
	protected JButton cookbookButton;
	
	protected ClientConfigUtil clientUtil;
	protected String workingDirectory = ".";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientToolsWindow tools = new ClientToolsWindow();
		tools.openToolsWindow(args);
	}
	
	public void openToolsWindow(String[] args) {
		clientUtil = new ClientConfigUtil();
		if (args != null && args.length > 0) 
			this.loadServers(args[0]);
		initComponents();
		this.setSize(new Dimension(1000, 700));
		this.setLocation(50, 50);
		this.setVisible(true);
	    addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(java.awt.event.WindowEvent e) {
	            System.exit(0);
	        }
	    });
	}
	
	protected void initComponents() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(createServerList(), BorderLayout.NORTH);

		mainPanel.add(createProcessPointList(), BorderLayout.CENTER);
		mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
		this.setContentPane(mainPanel);
		setTitle();
	}

	protected void setTitle() {
		this.setTitle("GALC Client Configuration Tool Ver." + GalcClientConfiguration.CURRENT_VERSION);
	}

	private JPanel createServerList() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		serverTable = new JTable();
		serverTable.setCellSelectionEnabled(false);
		serverTable.setColumnSelectionAllowed(false);
		serverTable.setRowSelectionAllowed(true);
		serverTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				selectedServerChanged();
			}
			
		});
		
		ServerTableModel model = new ServerTableModel();
		model.setColumnCount(headers.length);
		model.setColumnIdentifiers(headers);
		model.setDataVector(servers, headers);
		model.addTableModelListener(this);
		serverTable.setModel(model);

		scrollPane.getViewport().setView(serverTable);
		scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		scrollPane.setPreferredSize(new Dimension(950,100));
		
		JPanel textPanel = new JPanel();
		textPanel.add(new JLabel("Servers"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(textPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		return panel;
	}
	
	private void loadServers(String filePath) {
		System.out.println(filePath);
		try (InputStream input = new FileInputStream(filePath)){
			Properties props = new Properties();
			props.load(input);
			if (props.isEmpty()) return;
			
			TreeMap<Object,Object> sortedProps = new TreeMap<Object,Object>();
			for (Map.Entry<Object,Object> prop : props.entrySet()) {
				sortedProps.put(prop.getKey(),prop.getValue());
			}
			
			ArrayList<Object[]> serverList = new ArrayList<Object[]>();
			serverList.add(this.servers[0]);
			for (Map.Entry<Object,Object> prop : sortedProps.entrySet()) {
				Object[] server = {"Server " + serverList.size(), prop.getKey(), prop.getValue()};
				serverList.add(server);
			}
			
			Object[][] objArray = new Object[serverList.size()][3];
			for (int i = 0; i < serverList.size(); i++) {
				objArray[i] = serverList.get(i);
			}
			this.servers = objArray;
		} catch (IOException ex) {
            ex.printStackTrace();
        }
		
	}
	
	private void selectedServerChanged() {
		getProcessPointButton.setEnabled(serverTable.getSelectedRow() >= 0);
		restoreButton.setEnabled(serverTable.getSelectedRow() >= 0);
		processPointList = new ArrayList<ProcessPoint>();
		new ProcessPointTableModel(processPointList, processPointTable);
		int rowIndex = serverTable.getSelectedRow();
		if(rowIndex >= 0) {
			clientUtil.setServerUrl(getServerUrl(rowIndex));
		}
	}

	protected String getServerUrl(int rowIndex) {
		return (String) servers[rowIndex][2];
	}
	
	protected void enabledButtonsAfterLoadProcessPoints() {
		boolean selected = processPointTable.getSelectedRowCount() > 0;
		backupButton.setEnabled(selected);
		removeButton.setEnabled(selected);
		cookbookButton.setEnabled(selected);
	}
	
	private JPanel createProcessPointList() {
		JScrollPane scrollPane = new JScrollPane();

		processPointTable = new JTable();
		processPointTable.setCellSelectionEnabled(false);
		processPointTable.setColumnSelectionAllowed(false);
		processPointTable.setRowSelectionAllowed(true);
		processPointTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		processPointTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				enabledButtonsAfterLoadProcessPoints();
			}

			
		});
		
		new ProcessPointTableModel(processPointList, processPointTable);
		
		this.setScrollPane(scrollPane, processPointTable);

		JPanel textPanel = new JPanel();
		JLabel processPointLabel = new JLabel("Process Point");
		textPanel.add(processPointLabel);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(textPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		return panel;
	}	

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		directoryTextField = new JTextField();
		directoryTextField.setEditable(false);
		directoryTextField.setColumns(30);
		directoryTextField.setText(workingDirectory);
		buttonPanel.add(directoryTextField);
		
		chooseDirectoryButton = new JButton("Choose Directory");
		chooseDirectoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chooseDirectoryButtonAction("Please select working directory");
			}
		});
//		buttonPanel.add(chooseDirectoryButton);
		
		getProcessPointButton = new JButton("Get Process Point");
		getProcessPointButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getProcessPointButtonAction();				
			}
		});
		getProcessPointButton.setEnabled(false);
		buttonPanel.add(getProcessPointButton);

		backupButton = new JButton("Backup");
		backupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooseDirectoryButtonAction("Please select backup directory")){
					doBackUp();
				}
			}
		});
		backupButton.setEnabled(false);
		buttonPanel.add(backupButton);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtonAction();
			}
		});
		removeButton.setEnabled(false);
		buttonPanel.add(removeButton);

		restoreButton = new JButton("Restore");
		restoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restoreButtonAction();
			}
		});
		restoreButton.setEnabled(false);
		buttonPanel.add(restoreButton);

		cookbookButton = new JButton("Cookbook");
		cookbookButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooseDirectoryButtonAction("Please select the directory for cookbooks")) {
					cookbookButtonAction();
				}
			}
		});
		cookbookButton.setEnabled(false);
		buttonPanel.add(cookbookButton);

		return buttonPanel;
	}
	
	
	
	private void removeButtonAction() {
		if(processPointTable.getSelectedRows().length <= 0) {
			return;
		}
		//get selected process points and create a JTable
		ArrayList<ProcessPoint> selectedProcessPoints = selectedProcessPoints();
		JTable selectedProcessPointTable = new JTable();
		selectedProcessPointTable.setCellSelectionEnabled(true);
		new RemoveProcessPointTableModel(selectedProcessPoints, selectedProcessPointTable);
		
		//set scroll pane
		JScrollPane scrollPane = new JScrollPane();
		this.setScrollPane(scrollPane, selectedProcessPointTable);
		
		JPanel textP = new JPanel();
		JLabel removeLabel = new JLabel("Clicking 'OK' will remove the following Process Points:");
		removeLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		textP.add(removeLabel);
		
		JPanel removeProcessPointsPanel = new JPanel();
		removeProcessPointsPanel.setLayout(new BorderLayout());
		removeProcessPointsPanel.add(textP, BorderLayout.NORTH);
		removeProcessPointsPanel.add(scrollPane, BorderLayout.CENTER);
		
		int answer = JOptionPane.showConfirmDialog(null, removeProcessPointsPanel, "Are you sure you want to delete these process points?", JOptionPane.OK_CANCEL_OPTION);
		
		if(answer == JOptionPane.OK_OPTION) {
			if(chooseDirectoryButtonAction("Please select a directory for backup first")) {
				doRemove();
				getProcessPointButtonAction();
			}
		}
	}

	

	private boolean chooseDirectoryButtonAction(String windowTitle) {
		JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setCurrentDirectory(new File(workingDirectory));
	    chooser.setDialogTitle(windowTitle);
	    int returnVal = chooser.showOpenDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
        	return false;
        }
		workingDirectory = chooser.getSelectedFile().getAbsolutePath();
		setSelectedWorkingDirectory();
		return true;
	}

	protected void setSelectedWorkingDirectory() {
		clientUtil.setWorkingDirectory(workingDirectory);
		directoryTextField.setText(workingDirectory);
	}

	protected ArrayList<String> selectedProcessPointIds() {
		ArrayList<String> ids = new ArrayList<String>();
		for(int index : processPointTable.getSelectedRows()) {
			ids.add((String) processPointList.get(index).getProcessPointId());
		}
		return ids;
	}
	
	protected ArrayList<ProcessPoint> selectedProcessPoints() {
		ArrayList<ProcessPoint> processPoints = new ArrayList<>();
		for(int index : processPointTable.getSelectedRows()) {
			processPoints.add(processPointList.get(index));
		}
		return processPoints;
	}
	
	protected void setScrollPane(JScrollPane scrollPane, JTable table) {
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView(table);
		scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		scrollPane.setPreferredSize(new Dimension(950,500));
	}
	
	private void restoreButtonAction() {
		int rowIndex = serverTable.getSelectedRow();
		if(rowIndex == 0) {
			stopEditing();
		}
		if(rowIndex < 0) {
			return;
		}
		clientUtil.setServerUrl(getServerUrl(rowIndex));
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
	    chooser.setCurrentDirectory(new File(workingDirectory));
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
        	return;
        }

        File[] files = chooser.getSelectedFiles();
        doRestore(files);
        getProcessPointButtonAction();
	}

	private void cookbookButtonAction() {
		clientUtil.createCookbook(selectedProcessPointIds());
	}
	
	protected void getProcessPointButtonAction() {
		int rowIndex = serverTable.getSelectedRow();
		if(rowIndex == 0) {
			stopEditing();
		}
		if(rowIndex >= 0) {
			clientUtil.setServerUrl(getServerUrl(rowIndex));
			populateProcessPoint();
		}
	}
	
	private void populateProcessPoint() {
		processPointList = clientUtil.findAllProcessPoint();
		new ProcessPointTableModel(processPointList, processPointTable);

	}

	protected void stopEditing() {
		TableCellEditor editor = serverTable.getCellEditor(0, 2);
		if(editor != null) {
			editor.stopCellEditing();
		}
	}
	
	private void displayInfo(final String string) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, string);
			}
		});
	}

	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public void setCenterPanel(JPanel centerPanel) {
		this.centerPanel = centerPanel;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getColumn() == 2 && e.getFirstRow() ==0) {
			ServerTableModel model = (ServerTableModel) e.getSource();
			servers[0][2] = model.getValueAt(0, 2);
		}
	}

/**
 * Table model for server table
 * @author is021426
 *
 */
	protected class ServerTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 2672041616329981323L;

		public boolean isCellEditable(int row, int col) {
			return row == 0 && col == 2;
		}
	}
	
	protected void doBackUp() {
		clientUtil.backupProcessPoints(selectedProcessPointIds());
	}
	
	protected void doRemove() {
		clientUtil.removeProcessPoints(selectedProcessPointIds());
	}
	
	protected void doRestore(File[] files) {
		clientUtil.restoreFromFiles(files);
	}
}
