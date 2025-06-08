package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetDiagResultDao;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.LetDiagResult;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 14, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetPreoperationalCheckResultPanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private javax.swing.JLabel titleLabel = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField startdateField = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField enddateField = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JTextField linenoField = null;
	private javax.swing.JButton searchButton = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JLabel jLabel11 = null;
	private JTable programTable = null;
	private JTable resultsTable = null;
	private JScrollPane programScrollPane = null;
	private javax.swing.JPanel mainPanel = null;
	private String terminalID = "";
	private HashMap allTestNameMap = null;
	private Vector  vecResults=null;
	private String[][] strResultValues =null;
	private int iDateRange = -1;
	private String letFailDisplay=null;					
	private String letFailSign=null;						
	private String letFaultCode=null;
	private LetPropertyBean letPropertyBean=null;
	private Vector vecProgram=null;
	private LetUtil letUtil=null;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public LetPreoperationalCheckResultPanel(TabbedMainWindow mainWindow) {
		super("Let Preoperational Check Result Panel", KeyEvent.VK_P,mainWindow);	
		initialize();
	}

	public LetPreoperationalCheckResultPanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(getApplicationId()).info("Let Preoperational Check Result Panel is selected");
	}

	private void initialize() {
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);
		getMainWindow().addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				getStartDate().requestFocusInWindow();
			}
		});
		try {
			letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());		
			letFailDisplay=letPropertyBean.getLetFailDisplay();					
			letFailSign=letPropertyBean.getLetFailSign();						
			letFaultCode=letPropertyBean.getLetFaultCode();
			letUtil=new LetUtil();
			try {
				iDateRange = Integer.valueOf(letPropertyBean.getPreoperationalDateRange()).intValue();
			} catch (Exception e) {
				Logger.getLogger(getApplicationId()).error("Invalid Date range.");
				setErrorMessage("Invalid Date range.");
				return;
			}
			String[] paramStringArray = new String[]{Integer.toString(iDateRange)};
			getWithinLabel().setText((String)Message.get("WITHIN_N_DAYS", paramStringArray));

		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");        }
	}

	private javax.swing.JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new javax.swing.JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getTitleLabel(), null);
			mainPanel.add(getStartDateDateLabel(), null);
			mainPanel.add(getStartDate(), null);
			mainPanel.add(getEndDateDateLabel(), null);
			mainPanel.add(getEndDate(), null);
			mainPanel.add(getTerminalIDLabel(), null);
			mainPanel.add(getTerminalID(), null);
			mainPanel.add(getSearchButton(), null);
			mainPanel.add(getJScrollPane(), null);
			mainPanel.add(getYmdLabel(), null);
			mainPanel.add(getWithinLabel(), null);
			mainPanel.add(getProgramScrollPane(), null);
		}
		return mainPanel;
	}


	public void actionPerformed(ActionEvent arg) {
	}

	private javax.swing.JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new javax.swing.JLabel();
			titleLabel.setBounds(238, 8, 568, 60);
			titleLabel.setText( Message.get("LETPreoperationalCheckResult"));
			titleLabel.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return titleLabel;
	}

	private javax.swing.JLabel getStartDateDateLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setBounds(218, 108, 60, 44);
			jLabel.setText(Message.get("StartDate"));
		}
		return jLabel;
	}

	private javax.swing.JTextField getStartDate() {
		if (startdateField == null) {
			startdateField = new javax.swing.JTextField();
			startdateField.setName("startdateField");
			startdateField.setBounds(288, 114, 152, 27);
			startdateField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
			startdateField.setDocument(new AsciiDocument(8));
		}
		return startdateField;
	}

	private javax.swing.JLabel getEndDateDateLabel() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setBounds(470, 108, 60, 44);
			jLabel1.setText(Message.get("EndDate"));
		}
		return jLabel1;
	}

	private javax.swing.JTextField getEndDate() {
		if (enddateField == null) {
			enddateField = new javax.swing.JTextField();
			enddateField.setBounds(527, 114, 152, 27);
			enddateField.setName("enddateField");
			enddateField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
			enddateField.setDocument(new AsciiDocument(8));
		}
		return enddateField;
	}

	private javax.swing.JLabel getTerminalIDLabel() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setBounds(218, 180, 100, 39);
			jLabel2.setText(Message.get("TERMINAL_ID"));
		}
		return jLabel2;
	}

	private javax.swing.JTextField getTerminalID() {
		if (linenoField == null) {
			linenoField = new javax.swing.JTextField();
			linenoField.setBounds(288, 184, 50, 27);
			linenoField.setName("linenoField");
			linenoField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
			linenoField.setDocument(new AsciiDocument(3));
		}
		return linenoField;
	}

	private javax.swing.JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new javax.swing.JButton();
			searchButton.setBounds(777, 176, 110, 44);
			searchButton.setName("searchButton");
			searchButton.setText(Message.get("Search"));
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onSearchButton();
				}
			});
		}
		return searchButton;
	}

	private javax.swing.JLabel getYmdLabel() {
		if (jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setBounds(680, 116, 87, 23);
			jLabel8.setText("(YYYYMMDD)");
		}
		return jLabel8;
	}

	private javax.swing.JLabel getWithinLabel() {
		if (jLabel11 == null) {
			jLabel11 = new javax.swing.JLabel();
			jLabel11.setBounds(777, 109, 160, 43);
			jLabel11.setText("");
		}
		return jLabel11;
	}

	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getSearchResultTable());
			jScrollPane.setBounds(218, 237, 753, 300);
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jScrollPane;
	}

	private JScrollPane getProgramScrollPane() {
		if (programScrollPane == null) {
			programScrollPane = new JScrollPane();
			programScrollPane.setViewportView(getProgramTable());
			programScrollPane.setSize(150, 300);
			programScrollPane.setLocation(59, 237);
			programScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return programScrollPane;
	}

	private javax.swing.JTable getSearchResultTable() {
		if (resultsTable == null) {
			resultsTable = new JTable();
			resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("ITEM"));
			resultsTable.getTableHeader().setReorderingAllowed(false);
			resultsTable.setModel(model);
			TableColumnModel columnModel = resultsTable.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
			resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row, int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
					Map fontAttr = new HashMap();
					if (column > 0 && table.getValueAt(0, column).equals(letFailSign)) {
						if (row < 5) {
							org.setForeground(Color.RED);
						} else if (table.getValueAt(row, column) == null) {
						} else if (row >= 5 && table.getValueAt(row, column).equals(letFailSign)) {
							org.setForeground(Color.RED);
						} else if (row >= 5 && table.getValueAt(row, column).equals(letFailDisplay)) {
							org.setForeground(Color.RED);
						} else {
							org.setForeground(Color.BLACK);
						}
					} else {
						org.setForeground(Color.BLACK);
					}
					org.setBackground(Color.WHITE);
					if (column == 1&& allTestNameMap.containsKey(table.getValueAt(row, column))) {
						org.setForeground(Color.RED);
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else if (row < 5) {
						org.setHorizontalAlignment(JLabel.CENTER);
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else {
						org.setHorizontalAlignment(JLabel.CENTER);
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					}
					org.setFont(new Font(fontAttr));
					return org;
				}
			});

		}
		return resultsTable;
	}

	private void onSearchButton() {
		clearProgramTable();
		clearResultTable();
		try {
			if (validateInputData()) {
				String startDate=null;
				try {
					startDate = letUtil.getStartTime(getStartDate().getText(),letPropertyBean.getProcessLocation());
				} catch (Exception e) {
					Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
					e.printStackTrace();
					setErrorMessage("Schedule data not found"); 
					return;
				}
				String endDate=null;
				try {
					endDate = letUtil.getStartTime(getEndDate().getText(),letPropertyBean.getProcessLocation());
				} catch (Exception e) {
					Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
					e.printStackTrace();
					setErrorMessage("Schedule data not found"); 
					return;
				}
				List<Object[]> letProgramList= getDao(LetDiagResultDao.class).getLetDiagResultTerminalIdList(startDate, endDate, terminalID);
				if (letProgramList.size()==0) {
					Logger.getLogger(getApplicationId()).error(" Preoperational check result not found.");
					setErrorMessage(" Preoperational check result not found.");
					return;
				}   
				vecProgram = new Vector();
				for (Object[] letProgram:letProgramList)
				{
					Vector rowData = new Vector();
					if (letProgram[0] != null) {
						rowData.addElement(letProgram[0]);
					}
					vecProgram.add(rowData);
				}      		
				getTerminalData();         
			}
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");        } 
	}


	private boolean validateInputData() 
	{
		terminalID = getTerminalID().getText();
		if (getStartDate().getText() == null || getStartDate().getText().equals("")) {
			Logger.getLogger(getApplicationId()).error("Enter start date.");
			setErrorMessage("Enter start date.");
			return false;
		} 
		if (getEndDate().getText() == null || getEndDate().getText().equals("")) {
			Logger.getLogger(getApplicationId()).error("Enter end date.");
			setErrorMessage("Enter end date.");
			return false;
		} 
		int startDate = 0;
		int endDate = 0;
		try {
			startDate = Integer.parseInt(getStartDate().getText());
		} catch (NumberFormatException e1) {
			Logger.getLogger(getApplicationId()).error("Invalid start date.");
			setErrorMessage("Invalid start date.");           
			return false;
		}
		try {
			endDate = Integer.parseInt(getEndDate().getText());
		} catch (NumberFormatException e1) {
			Logger.getLogger(getApplicationId()).error("Invalid end date.");
			setErrorMessage("Invalid end date.");
			return false;
		}
		if (startDate > endDate) {
			Logger.getLogger(getApplicationId()).error("Start date should not exceed end date.");
			setErrorMessage("Start date should not exceed end date.");
			return false;
		} 
		GregorianCalendar gcStartDate = null;
		GregorianCalendar gcEndDate = null;
		if (!getStartDate().getText().equals("")) {
			try {
				gcStartDate = new GregorianCalendar();
				gcStartDate.setLenient(false);
				gcStartDate.set(Integer.parseInt(getStartDate().getText().substring(0, 4)),Integer.parseInt(getStartDate().getText().substring(4, 6)) - 1,Integer.parseInt(getStartDate().getText().substring(6, 8)));
				gcStartDate.getTime();
				if (Integer.parseInt(getStartDate().getText().substring(4, 6)) - 1 > 12) {
					Logger.getLogger(getApplicationId()).error("Invalid start date.");
					setErrorMessage("Invalid start date.");           
					return false;
				}
				if (Integer.parseInt(getStartDate().getText().substring(6, 8)) > 31) {
					Logger.getLogger(getApplicationId()).error("Invalid start date.");
					setErrorMessage("Invalid start date.");           
					return false;
				}
			} catch (Exception e) {
				Logger.getLogger(getApplicationId()).error("Invalid start date.");
				setErrorMessage("Invalid start date.");           
				return false;
			}
		}
		if (!getEndDate().getText().equals("")) {
			try {
				gcEndDate = new GregorianCalendar();
				gcEndDate.setLenient(false);
				gcEndDate.set(Integer.parseInt(getEndDate().getText().substring(0, 4)),Integer.parseInt(getEndDate().getText().substring(4, 6)) - 1,Integer.parseInt(getEndDate().getText().substring(6, 8)));
				gcEndDate.getTime();

				if (Integer.parseInt(getEndDate().getText().substring(4, 6)) - 1 > 12) {
					Logger.getLogger(getApplicationId()).error("Invalid end date.");
					setErrorMessage("Invalid end date.");
					return false;
				}
				if (Integer.parseInt(getStartDate().getText().substring(6, 8)) > 31) {
					Logger.getLogger(getApplicationId()).error("Invalid end date.");
					setErrorMessage("Invalid end date.");
					return false;
				}
			} catch (Exception e) {
				Logger.getLogger(getApplicationId()).error("Invalid end date.");
				setErrorMessage("Invalid end date.");
				return false;
			}
		}
		if (gcStartDate != null && gcEndDate != null) {
			GregorianCalendar gcCompStartDate = (GregorianCalendar) gcStartDate.clone();
			gcCompStartDate.add(Calendar.DATE, iDateRange);
			if (gcCompStartDate.before(gcEndDate)) {
				Logger.getLogger(getApplicationId()).error("Date range exceeds limit.");
				setErrorMessage("Date range exceeds limit.");
				return false;
			}
		}
		if (terminalID == null || terminalID.equals("") || terminalID.length() != 3)  {
			Logger.getLogger(getApplicationId()).error("Terminal ID must be 3 figures.");
			setErrorMessage("Terminal ID must be 3 figures.");
			return false;
		}
		return true;
	}

	private JTable getProgramTable() 
	{
		if (programTable == null) {
			programTable = new JTable();
			programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			programTable.getTableHeader().setReorderingAllowed(false);
			programTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
			{
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						return;
					}
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (getProgramTable().getModel().getRowCount() == 0) {
						return;
					}
					if (lsm.isSelectionEmpty()) {
						clearResultTable();
					} else {
						onSelectProgram(lsm.getMinSelectionIndex());
					}
				}
			});
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("TERMINAL_ID"));
			programTable.setModel(model);
			TableColumnModel columnModel = programTable.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(132);
		}
		return programTable;
	}

	private JTable getResultsTable() 
	{
		if (resultsTable == null) {
			resultsTable = new JTable();
			resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("ITEM"));
			resultsTable.getTableHeader().setReorderingAllowed(false);
			resultsTable.setModel(model);
			TableColumnModel columnModel = resultsTable.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
			resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
					Map fontAttr = new HashMap();
					if (row > 4) {
						if (value == null) {
							org.setBackground(Color.PINK);
						} else if (value.equals("")) {
							org.setBackground(Color.PINK);
						} else {
							org.setBackground(Color.WHITE);
						}
					} else {
						org.setBackground(Color.WHITE);
					}
					if (row < 6) {
						org.setHorizontalAlignment(JLabel.CENTER);
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else {
						org.setHorizontalAlignment(JLabel.CENTER);
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					}
					org.setFont(new Font(fontAttr));
					return org;
				}
			});
		}
		return resultsTable;
	}


	private void onSelectProgram(int selectedRow) {
		try {
			clearResultTable();
			DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
			DefaultTableModel Pmodel = (DefaultTableModel) getProgramTable().getModel();
			model.setColumnCount(1);
			model.setRowCount(0);
			String startDate=null;
			try {
				startDate = letUtil.getStartTime(getStartDate().getText(),letPropertyBean.getProcessLocation());
			} catch (Exception e) {
				Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
				e.printStackTrace();
				setErrorMessage("Schedule data not found"); 
				return;
			}
			String endDate=null;
			try {
				endDate = letUtil.getEndTime(getEndDate().getText(),letPropertyBean.getProcessLocation());
			} catch (Exception e) {
				Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
				e.printStackTrace();
				setErrorMessage("Schedule data not found"); 
				return;
			}
			fetchResults(startDate,endDate,(String)Pmodel.getValueAt(selectedRow, 0));
			Vector vecSeqcounter = (Vector) vecResults.get(0);
			addRow(model, vecResults, vecSeqcounter);
			if(strResultValues!=null)
			{
				for (int i = 0; i < strResultValues.length; i++) {
					if (strResultValues[i][0] == null) {
					} else if (strResultValues[i][0].equals("")) {
						boolean blankFlg = false;
						for (int j = 1; j < strResultValues[i].length; j++) {
							if (strResultValues[i][j] != null) {
								blankFlg = true;
								break;
							}
						}
						if (blankFlg) {
							model.addRow(strResultValues[i]);
						}
					} else {
						model.addRow(strResultValues[i]);
					}
				}
			}
			TableColumnModel columnModel = resultsTable.getColumnModel();
			for (int i = 1; i < getResultsTable().getColumnCount(); i++) {
				columnModel.getColumn(i).setPreferredWidth(150);
			}
			TableColumn rowHeader = getResultsTable().getColumnModel().getColumn(0);
			rowHeader.setCellRenderer(new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
					if (table.getValueAt(2, column) != null) {
						if (column > 0 && row > 0 && table.getValueAt(row, column).equals(letFailSign)) {
							org.setForeground(Color.RED);
						} else if (table.getValueAt(row, column).equals(letFailSign)) {
							org.setForeground(Color.RED);
						} else if (table.getValueAt(row, column).equals(letFailDisplay)) {
							org.setForeground(Color.RED);
						} else {
							org.setForeground(Color.BLACK);
						}
					}
					Map fontAttr = new HashMap();
					if (column == 0 && row > 4 && allTestNameMap.containsKey(table.getValueAt(row, column))) {
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else if (row < 20) {
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else {
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					}
					org.setFont(new Font(fontAttr));
					return org;
				}
			});

		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}  finally {
			getResultsTable().getColumnModel().getColumn(0).setPreferredWidth(200);
		}
	}

	private DefaultTableModel addRow(DefaultTableModel model, Vector vecResults, Vector vecSeqcounter) {
		for (int i = 1; i < vecSeqcounter.size(); i++) {
			model.addColumn("No." + vecSeqcounter.get(i));
		}
		for (int i = 1; i < vecResults.size(); i++) {
			model.addRow((Vector) vecResults.get(i));
		}
		return model;
	}


	private void clearProgramTable() {
		getProgramTable().removeAll();
		DefaultTableModel model = (DefaultTableModel) getProgramTable().getModel();
		model.setColumnCount(2);
		model.setRowCount(0);
		getProgramTable().removeColumn(getProgramTable().getColumnModel().getColumn(1));
		TableColumnModel columnModel = getProgramTable().getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(200);
	}

	private void clearResultTable() {
		setErrorMessage("");
		DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
		model.setColumnCount(1);
		model.setRowCount(0);
		getResultsTable().removeAll();
		TableColumnModel columnModel = getResultsTable().getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(200);
	}

	private void getTerminalData() {
		DefaultTableModel model = (DefaultTableModel) getProgramTable().getModel();
		if (vecProgram != null) {
			if (vecProgram.size() != 0) {
				for (int i = 0; i < vecProgram.size(); i++) {
					model.addRow((Vector) vecProgram.get(i));
				}
				getProgramTable().addRowSelectionInterval(0, 0);
				onSelectProgram(0);
			}
		}
	}

	private void fetchResults(String startDate, String endDate, String terminalId)
	{
		try {
			allTestNameMap = new HashMap();
			List<LetDiagResult> selfDiagResultList =getSelfDiagnosisResult(startDate,  endDate,  terminalId);
			if(selfDiagResultList==null || selfDiagResultList.size()==0)
			{
				Logger.getLogger(getApplicationId()).error("Self Diag Results not found");
				setErrorMessage("Self Diag Results not found");
				return;
			}
			
			vecResults = storageOfSelfDiagnosisResult(selfDiagResultList, getDiagColumnsDetail(startDate, endDate, terminalId));
			List<Object[]> selfDiagResultDetailsList =getDao(LetDiagResultDao.class).getLetDiagResultNameData(startDate, endDate, terminalId);
			if(selfDiagResultDetailsList==null || selfDiagResultDetailsList.size()==0)
			{
				Logger.getLogger(getApplicationId()).info("Self Diag Detail Results not found");
				return;
			}

			storageOfSelfDiagnosisDetail(selfDiagResultDetailsList,(Vector) vecResults.get(2),allTestNameMap);
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}
	}
	
	private List<Object[]> getDiagColumnsDetail(String startDate, String endDate, String terminalId) throws ParseException {
		java.util.Date parsedStartDate = dateFormat.parse(startDate);
		java.sql.Timestamp startTimestamp = new java.sql.Timestamp(parsedStartDate.getTime());
		java.util.Date parsedEndDate = dateFormat.parse(endDate);
		java.sql.Timestamp endTimestamp = new java.sql.Timestamp(parsedEndDate.getTime());
		return getDao(LetDiagResultDao.class).getLetDiagColumns(startTimestamp, endTimestamp, terminalId);
	}

	private void storageOfSelfDiagnosisDetail(List<Object[]> selfDiagResultDetailsList,Vector vecFinishTime,HashMap allTestNameMap)
	{
		try {
			Map testidMap = new HashMap();
			Map faultCodeMap = new HashMap();
			Map shortDescMap = new HashMap();
			ArrayList faultList = new ArrayList();
			ArrayList storageList = new ArrayList();
			ArrayList cloneList = new ArrayList();
			strResultValues = new String[selfDiagResultDetailsList.size()* 3][vecFinishTime.size()];
			int cntr = 0;
			int rowCntr = 0;
			int testIDcntr = 0;
			int testStatuscntr = 1;
			int labelcntr = 0;
			String testid = null;
			String finishtime = null;
			String testStatus = null;
			String faultCode = null;
			String shortDesc = null;
			boolean idflg = false;
			boolean faultflg = false;
			boolean shortflg = false;
			boolean testflg = false;
			boolean flg = false;
			Vector labelflg = new Vector();
			int resultsCntr = 0;
			testid =storageInitialData(selfDiagResultDetailsList,testidMap,faultCodeMap,shortDescMap, testid,faultCode);
			for (Object[] detail:selfDiagResultDetailsList) {
				testid = StringUtils.trimToEmpty((String)detail[2]);
				if (!storageList.contains(testidMap.get(testid))) {
					storageList.add(testidMap.get(testid));
					cloneList.add(testidMap.get(testid));
				}
			}
			int checkCntr1 = 2;
			int checkCntr2 = 3;
			String checkid = "";
			makeListHeader(selfDiagResultDetailsList,storageList,cloneList,testStatus,checkCntr1,checkCntr2,checkid, vecFinishTime,shortDescMap,allTestNameMap);
			for (int i = 0; i < storageList.size(); i++) {
				strResultValues[i][0] = (String) storageList.get(i);
			}
			for (int i = 0; i < strResultValues.length; i++) {
				strResultValues[i][0]=StringUtils.trimToEmpty(strResultValues[i][0]);
			}
			cntr = 1;
			String workTestid = null;
			storageOfTableData(selfDiagResultDetailsList,vecFinishTime,cntr, rowCntr,finishtime, workTestid, cloneList);			
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}
	}


	private String storageInitialData( List<Object[]> selfDiagResultDetailsList,Map testidMap, Map faultCodeMap,Map shortDescMap,String testid,String faultCode) {
		try {
			String shortDesc;
			for (Object[] detail:selfDiagResultDetailsList) {
				testid =  StringUtils.trimToEmpty((String)detail[2]);
				faultCode =  StringUtils.trimToEmpty((String)detail[3]);
				shortDesc =  StringUtils.trimToEmpty((String)detail[4]);
				if (!testidMap.containsKey(testid)) {
					testidMap.put(testid, testid);
				}
				if (!faultCodeMap.containsKey(testid)) {
					faultCodeMap.put(testid + faultCode, faultCode);
				}
				if (!shortDescMap.containsKey(testid + faultCode)) {
					shortDescMap.put(testid + faultCode + shortDesc, shortDesc);
				}
			}
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}
		return testid;
	}

	private void makeListHeader(List<Object[]> selfDiagResultDetailsList,ArrayList storageList,ArrayList cloneList,String testStatus,int checkCntr1,int checkCntr2,String checkid,Vector vecFinishTime, Map shortDescMap,HashMap allTestNameMap) 
	{
		try {
			int cntr = 1;
			int timeCntr = 1;
			String finishtime = null;
			String testid = null;
			String faultCode = null;
			String shortDesc = null;
			Map checkMap = new HashMap();
			Map checkResultMap = new HashMap();
			ArrayList allTestList = null;
			HashMap allTestMap = new HashMap();
			int i=0;
			for (Object[] detail:selfDiagResultDetailsList) {      
				if (detail[1] == null) {
					finishtime = "";
				} else {
					finishtime =dateFormat.format(detail[0]);
				}
				testStatus =  StringUtils.trimToEmpty((String)detail[1]);
				testid =  StringUtils.trimToEmpty((String)detail[2]);
				faultCode =  StringUtils.trimToEmpty((String)detail[3]);
				shortDesc =  StringUtils.trimToEmpty((String)detail[4]);
				ArrayList testList = new ArrayList();
				if (i == 0) {
					testList.add(testid);
					allTestNameMap.put(testid, "");
				}
				if (allTestNameMap.containsKey(testid)) {
					testList.add(faultCode);
					testList.add(shortDesc);
				} else {
					testList.add(testid);
					testList.add(faultCode);
					testList.add(shortDesc);
					allTestNameMap.put(testid, "");
				}
				if (allTestMap.containsKey(testid)) {
					ArrayList list = (ArrayList) allTestMap.get(testid);
					boolean isDuplicate = false;
					for (int j = 1; j < list.size(); j = j + 2) {
						if (list.get(j).equals(faultCode)&& list.get(j + 1).equals(shortDesc)) {
							isDuplicate = true;
							break;
						}
					}
					if (!isDuplicate) {
						list.addAll(testList);
						allTestMap.put(testid, list);
					}
				} else {
					allTestMap.put(testid, testList);
				}
				i=i+1;
			}
			allTestList = new ArrayList();
			storageList.clear();
			Iterator it = allTestMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				storageList.addAll((ArrayList) allTestMap.get(key));
				allTestList.addAll((ArrayList) allTestMap.get(key));
			}
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}
	}

	private void storageOfTableData(List<Object[]> sqlResponsedetail,Vector vecFinishTime, int cntr,int rowCntr,String finishtime, String workTestid,ArrayList cloneList) 
	{
		try {
			String testid = null;
			String testStatus = null;
			String faultCode = null;
			String shortDesc = null;
			int checkCntr1 = 1;
			int checkCntr2 = 2;
			int shortCnt = 0;
			String testidFlg = null;
			Vector vecClone = new Vector();
			int i=0;
			for (Object[] detail:sqlResponsedetail) {

				if (detail[0] == null) {
					finishtime = "";
				} else {
					finishtime =dateFormat.format(detail[0]);
				}
				testStatus =  StringUtils.trimToEmpty((String)detail[1]);
				testid =  StringUtils.trimToEmpty((String)detail[2]);
				faultCode =  StringUtils.trimToEmpty((String)detail[3]);
				shortDesc =  StringUtils.trimToEmpty((String)detail[4]);
				if (vecFinishTime.get(cntr).toString().equals(finishtime)) {
					if (strResultValues[rowCntr][0] == null) {
					} else {
						if (testStatus.equals(LetInspectionStatus.Pass.name())) {
							for (int j = 0; j < strResultValues.length; j++) {
								if (strResultValues[j][0] == null) {
								} else {
									if (strResultValues[j][0].equals(testid)) {
										strResultValues[j][cntr] = testStatus;
									}
								}
							}
						} else {
							for (int j = 0; j < strResultValues.length; j++) {
								if (i < 1) {
									if (strResultValues[j][0] == null) {
									} else {
										if (cloneList.contains(testid)) {
											if (strResultValues[j][0].equals(testid)) {
												testidFlg = testid;
												if (strResultValues[j+ checkCntr1][0].equals(faultCode)) {
													if (strResultValues[j+ checkCntr2][0].equals(shortDesc)) {
														strResultValues[j][cntr] =testStatus;
														strResultValues[j+ checkCntr1][cntr] = letFaultCode;
														strResultValues[j+ checkCntr2][cntr] =letFaultCode;
														checkCntr1 += 2;
														checkCntr2 += 2;
														j =strResultValues.length+ 1;
													}
												}
											}
										}
									}
								} else {
									if (strResultValues[j][0] == null) {
									} else {
										if (testidFlg == null) {
											testidFlg = testid;
											if (strResultValues[j][0].equals(testid)) {
												if (strResultValues[j + checkCntr1][0].equals(faultCode)) {
													if (strResultValues[j+ checkCntr2][0].equals(shortDesc)) {
														strResultValues[j][cntr] = testStatus;
														strResultValues[j+ checkCntr1][cntr] = letFaultCode;
														strResultValues[j+ checkCntr2][cntr] = letFaultCode;
														checkCntr1 = 1;
														checkCntr2 = 2;
														j =strResultValues.length+ 1;
													} else {
														checkCntr1 += 2;
														checkCntr2 += 2;
														j = j - 1;
													}
												} else {
													checkCntr1 += 2;
													checkCntr2 += 2;
													j = j - 1;
												}
											} else {
												if (cloneList.contains(testid)) {
													if (strResultValues[j][0].equals(testid)) {
														testidFlg = testid;
														checkCntr1 = 1;
														checkCntr2 = 2;
														if (strResultValues[j + checkCntr1][0].equals(faultCode)) {
															if (strResultValues[j+ checkCntr2][0] .equals( shortDesc)) {
																strResultValues[j][cntr] =testStatus;
																strResultValues[j+ checkCntr1][cntr] = letFaultCode;
																strResultValues[j+ checkCntr2][cntr] =letFaultCode;
																checkCntr1 = 1;
																checkCntr2 = 2;
																j = strResultValues.length + 1;
															} else {
																checkCntr1 += 2;
																checkCntr2 += 2;
																j = j - 1;
															}
														} else {
															checkCntr1 += 2;
															checkCntr2 += 2;
															j = j - 1;
														}
													}
												}
											}
										} else {
											shortCnt = j + checkCntr2;
											if (shortCnt < strResultValues.length) {
												if (strResultValues[j + checkCntr1][0] == null) {
												} else if ( strResultValues[j+ checkCntr2][0]== null) {
												} else {
													if (testidFlg.equals(testid)&& strResultValues[j][0].equals(testid)) {
														if (strResultValues[j + checkCntr1][0].equals(faultCode)) {
															if (strResultValues[j + checkCntr2][0] .equals(shortDesc)) {
																strResultValues[j][cntr] = testStatus;
																strResultValues[j + checkCntr1][cntr] =letFaultCode;
																strResultValues[j + checkCntr2][cntr] = letFaultCode;
																checkCntr1 = 1;
																checkCntr2 = 2;
																j =strResultValues.length+ 1;
															} else {
																checkCntr1 += 2;
																checkCntr2 += 2;
																j = j - 1;
															}
														} else {
															checkCntr1 += 2;
															checkCntr2 += 2;
															j = j - 1;
														}
													} else {
														if (cloneList.contains(testid)) {
															if (strResultValues[j][0].equals(testid)) {
																testidFlg = testid;
																checkCntr1 = 1;
																checkCntr2 = 2;
																if (strResultValues[j+ checkCntr1][0].equals(faultCode)) {
																	if (strResultValues[j+ checkCntr2][0].equals( shortDesc)) {
																		strResultValues[j][cntr] =testStatus;
																		strResultValues[j + checkCntr1][cntr] =letFaultCode;
																		strResultValues[j+ checkCntr2][cntr] = letFaultCode;
																		checkCntr1 = 1;
																		checkCntr2 = 2;
																		j =strResultValues.length+ 1;
																	} else {
																		checkCntr1+= 2;
																		checkCntr2+= 2;
																		j = j - 1;
																	}
																} else {
																	checkCntr1 += 2;
																	checkCntr2 += 2;
																	j = j - 1;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				} else {
					cntr++;
					i = i - 1;
					checkCntr1 = 1;
					checkCntr2 = 2;
					shortCnt = 0;
				}
				i=i+1;
			}
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}
	}


	private Vector storageOfSelfDiagnosisResult( List<LetDiagResult> letDiagResultList, List<Object[]> letDiagColumn)
	{
		Vector vecResults = new Vector();
		try {

			Vector vecSeqcounter = new Vector();
			Vector vecTotalStatus = new Vector();
			Vector vecFinishTime = new Vector();
			Vector vecCell = new Vector();
			Vector vecDiagCal = new Vector();
			Vector vecSwVer = new Vector();
			Vector colHeader = new Vector();
			
			vecSeqcounter.add("No.");
			for (int i = 0; i < letDiagResultList.size(); i++) {
				int cntr = i + 1;
				vecSeqcounter.add(String.valueOf(cntr));
			}
			vecTotalStatus.add("Total Status");
			vecFinishTime.add("Finish Time");
			vecCell.add("Cell");
			vecDiagCal.add("Cal");
			vecSwVer.add("SWVer");
			vecResults.add(vecSeqcounter);
			vecResults.add(vecTotalStatus);
			vecResults.add(vecFinishTime);
			vecResults.add(vecCell);
			vecResults.add(vecDiagCal);
			vecResults.add(vecSwVer);
			for (Object[] columnHeader : letDiagColumn) {
				colHeader = new Vector();
				colHeader.add(((String)columnHeader[0]).trim());
				vecResults.add(colHeader);
			}
			
			for (LetDiagResult letDiagResult:letDiagResultList ) 
			{
				Map<String, Integer> letDiagDetailMap = new HashMap<String, Integer>();
				List<Object[]> letDiagResultListView = getDao(LetDiagResultDao.class).getLetDiagRsltView(letDiagResult.getId().getEndTimestamp(), letDiagResult.getId().getLetTerminalId());
				for (Object[] ob : letDiagResultListView) {
					letDiagDetailMap.put(((String)ob[0]).trim(), Integer.valueOf((String) ob[2]));
				}
				if (letDiagResult.getTotalResultStatus() == null) {
					Logger.getLogger(getApplicationId()).error("TOTAL_STATUS is null.");
					setErrorMessage("TOTAL_STATUS is null.");
				} else {
					if (!letDiagResult.getTotalResultStatus().trim() .equals("")) {
						Integer value =Integer.parseInt(letDiagResult.getTotalResultStatus()) ;
						vecTotalStatus.add(LetInspectionStatus.getType(value).name());
					}
				}
				if (letDiagResult.getId().getEndTimestamp() == null) {
					vecFinishTime.add("");
				} else {             
					vecFinishTime.add(dateFormat.format(letDiagResult.getId().getEndTimestamp()));                
				}
				if (letDiagResult.getId().getLetTerminalId() == null) {
					vecCell.add("");
				} else {
					if (!letDiagResult.getId().getLetTerminalId() .trim().equals("")) {
						vecCell.add((String) letDiagResult.getId().getLetTerminalId());
					}
				}
				if (letDiagResult.getBaseRelease() == null) {
					vecDiagCal.add("");
				} else {
					if (!letDiagResult.getBaseRelease().trim().equals("")) {
						vecDiagCal.add((String) letDiagResult.getBaseRelease());
					}
				}
				if (letDiagResult.getSoftwareVersion() == null) {
					vecSwVer.add("");
				} else {
					if (!letDiagResult.getSoftwareVersion() .toString().trim().equals("")) {
						vecSwVer.add( letDiagResult.getSoftwareVersion());
					}
				} 
				
				for(int numberOfColumns = 6; numberOfColumns < vecResults.size(); numberOfColumns++) {
					String columnName = vecResults.get(numberOfColumns).toString().trim().replace("[", "").replace("]", "");
					if(columnName.contains(","))
						columnName = columnName.substring(0, columnName.indexOf(","));
					Vector addVecValue = (Vector)vecResults.get(numberOfColumns); 
					if (letDiagDetailMap.containsKey(columnName)){
						Integer value = letDiagDetailMap.get(columnName) ;
						addVecValue.add(LetInspectionStatus.getType(value).name());
					} else {
						addVecValue.add(StringUtils.trimToEmpty(""));
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}
		return vecResults;
	}

	private List<LetDiagResult> getSelfDiagnosisResult(String startDate, String endDate, String terminalId)
	{
		try {
			java.util.Date parsedStartDate = dateFormat.parse(startDate);
			java.sql.Timestamp startTimestamp = new java.sql.Timestamp(parsedStartDate.getTime());
			java.util.Date parsedEndDate = dateFormat.parse(endDate);
			java.sql.Timestamp endTimestamp = new java.sql.Timestamp(parsedEndDate.getTime());
			return getDao(LetDiagResultDao.class).getLetDiagRsltBtwStartEndTmp(startTimestamp, endTimestamp, terminalId);
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Preoperational Check Result Panel screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the Let Preoperational Check Result Panel screen");
		}
		return null;
	}



	private String getStatus(String comboValue) 
	{
		String statusResult = null;
		LetInspectionStatus[] statuses=LetInspectionStatus.values();
		for(int i=0;i<statuses.length;i++)
		{
			if (comboValue.equals(statuses[i].name())) {
				statusResult = String.valueOf(i);
			}
		}
		return statusResult;
	}

}