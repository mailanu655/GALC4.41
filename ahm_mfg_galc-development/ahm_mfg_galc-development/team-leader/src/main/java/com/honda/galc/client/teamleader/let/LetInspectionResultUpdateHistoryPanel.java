package com.honda.galc.client.teamleader.let;


import static com.honda.galc.service.ServiceFactory.getDao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetModifyHistoryDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.message.Message;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.honda.galc.entity.enumtype.LetInspectionStatus;



/**
 * 
 * @author Gangadhararao Gadde
 * @date Sept 16, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetInspectionResultUpdateHistoryPanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel = null;		
	private JLabel titleLabel = null;
	private JLabel programLabel = null;
	private JScrollPane programScrollPane = null;
	private JTable programTable = null;
	private JLabel resultsLabel = null;
	private JScrollPane resultsScrollPane = null;
	private JTable resultsTable = null;
	private LetProductPanel productPanel = null;
	private  Vector vecProgram= null;
	private Vector vecResults=null;
	private Vector vecResultValues=null;


	public LetInspectionResultUpdateHistoryPanel(TabbedMainWindow mainWindow) {
		super("LET Inspection Result Update History Panel", KeyEvent.VK_U,mainWindow);	
		initComponents();
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(this.getApplicationId()).info("LET Inspection Result Update History Panel is selected");
	}

	private void initComponents() 
	{
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);
		setSize(1024, 768);
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getTitleLabel(), null);
			mainPanel.add(getProgramLabel(), null);
			mainPanel.add(getProgramScrollPane(), null);
			mainPanel.add(getResultsLabel(), null);
			mainPanel.add(getResultsScrollPane(), null);
			mainPanel.add(getProductPanelUtilities(), null);
		}
		return mainPanel;
	}

	public void actionPerformed(ActionEvent arg0) {

	}

	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel();
			titleLabel.setBounds(124, 11, 755, 29);
			titleLabel.setText(Message.get("LETInspectionResultUpdateHistory"));
			titleLabel.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return titleLabel;
	}

	private JLabel getProgramLabel() {
		if (programLabel == null) {
			programLabel = new JLabel();
			programLabel.setSize(218, 33);
			programLabel.setText(Message.get("InspectionProgram"));
			programLabel.setLocation(19, 127);
		}
		return programLabel;
	}

	private JScrollPane getProgramScrollPane() {
		if (programScrollPane == null) {
			programScrollPane = new JScrollPane();
			programScrollPane.setViewportView(getProgramTable());
			programScrollPane.setSize(218, 372);
			programScrollPane.setLocation(19, 160);
			programScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return programScrollPane;
	}

	private JTable getProgramTable() {
		if (programTable == null) {
			programTable = new JTable();
			programTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			programTable.getTableHeader().setReorderingAllowed(false);
			programTable.setName("programTable");
			programTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						return;
					}
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (getProgramTable().getModel().getRowCount() == 0) {
						return;
					}
					if (lsm.isSelectionEmpty()) {
						onUnselectProgram();
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
			model.addColumn(Message.get("Program"));
			programTable.setModel(model);
			TableColumnModel columnModel = programTable.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
		}
		return programTable;
	}

	private JLabel getResultsLabel() {
		if (resultsLabel == null) {
			resultsLabel = new JLabel();
			resultsLabel.setSize(743, 33);
			resultsLabel.setText(Message.get("InspectionResultDetails"));
			resultsLabel.setLocation(258, 128);
		}
		return resultsLabel;
	}

	private JScrollPane getResultsScrollPane() {
		if (resultsScrollPane == null) {
			resultsScrollPane = new JScrollPane();
			resultsScrollPane.setViewportView(getResultsTable());
			resultsScrollPane.setSize(743, 372);
			resultsScrollPane.setLocation(252, 159);
			resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return resultsScrollPane;
	}

	private JTable getResultsTable() {
		if (resultsTable == null) {
			resultsTable = new JTable();
			resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("MAINTENANCE_COUNT"));
			resultsTable.getTableHeader().setReorderingAllowed(false);
			resultsTable.setModel(model);
			TableColumnModel columnModel = resultsTable.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
			resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row, column);
					Map fontAttr = new HashMap();

					if (table.getValueAt(4, column) != null) {
						if (column > 0 && table.getValueAt(4, column).equals(LetInspectionStatus.Fail.name())) {
							org.setForeground(Color.RED);
						} else {
							org.setForeground(Color.BLACK);
						}
					}
					if (row < 5) {
						org.setHorizontalAlignment(JLabel.CENTER);
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else {
						org.setHorizontalAlignment(JLabel.RIGHT);
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					}
					org.setFont(new Font(fontAttr));
					return org;
				}
			});

		}
		return resultsTable;
	}

	private LetProductPanel getProductPanelUtilities() {
		if (productPanel == null) {
			productPanel = new LetProductPanel(this);
			productPanel.addListener(new LetProductPanelListener() {
				public void actionPerformed() {
					clearProgramTable();
					clearResultTable();
					try {
						if(!productPanel.getData()) return;
						acquisitionOfProgram();										
						onEnterProductId();
					}  catch (Exception e) {
						Logger.getLogger().error(e,"An error Occurred while processing the LET Inspection Result Update History screen");
						e.printStackTrace();
						setErrorMessage("An error Occurred while processing the LET Inspection Result Update History screen");
					}
				}
			});
			productPanel.setBounds(6, 47, 1008, 119);
		}
		return productPanel;
	}

	private Vector acquisitionOfProgram() {

		List<Object[]> letProgramsList=getDao(LetModifyHistoryDao.class).findDistinctLetMdfyHstryPgmByProductId(productPanel.getProductId().getText());	
		vecProgram = new Vector();
		if (letProgramsList.size() > 0) {
			for (Object[] letProgram:letProgramsList)
			{
				if(letProgram[1]!=null)
				{
					Vector rowData = new Vector();
					rowData.addElement(letProgram[0]);
					rowData.addElement(letProgram[1]);
					vecProgram.add(rowData);
				}
			}
		} else {
			List<Object[]> letResultModifyHistoryList=getDao(LetModifyHistoryDao.class).findLetModifyResultHistoryByProductId(productPanel.getProductId().getText());
			if (letResultModifyHistoryList.size() > 0) {
				Vector noProgram = new Vector();
				noProgram.add(LETDataTag.NOT_APPLICATABLE);
				noProgram.add(new Integer(0));
				vecProgram.add(noProgram);
			} else {
				Logger.getLogger(this.getApplicationId()).error("Inspection program not found");
				setErrorMessage("Inspection program not found");            
			}
		}
		return vecProgram;
	}

	private void onEnterProductId() {
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

	private void onSelectProgram(int selectedRow) {
		try {
			clearResultTable();
			HashMap mapTestSeq = new HashMap();
			DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
			DefaultTableModel Pmodel = (DefaultTableModel) getProgramTable().getModel();
			model.setColumnCount(1);
			model.setRowCount(0);
			List<Object[]> letResultModifyHistoryList=getDao(LetModifyHistoryDao.class).getInpsectionDetails(productPanel.getProductId().getText(),(Integer)Pmodel.getValueAt(selectedRow, 1));
			if(letResultModifyHistoryList.size()==0)
			{				
				Logger.getLogger(this.getApplicationId()).error("Inspection details not found");
				setErrorMessage("Inspection details not found");  
				return;
			}
			storageOfCorrectionDetails(letResultModifyHistoryList);
			Vector vecHistorySeq = (Vector) vecResults.get(0);
			int cnt = 0;
			for (int i = 1; i < vecHistorySeq.size(); i++) {
				model.addColumn("No." + vecHistorySeq.get(i));
			}
			for (int i = 1; i < vecResults.size(); i++) {
				model.addRow((Vector) vecResults.get(i));
			}
			for (int i = 0; i < vecResultValues.size(); i++) {
				model.addRow((Vector) vecResultValues.get(i));
			}
			TableColumn rowHeader = getResultsTable().getColumnModel().getColumn(0);
			rowHeader.setCellRenderer(new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus, row,column);
					Map fontAttr = new HashMap();
					if (row < 5) {
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else {
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					}
					org.setFont(new Font(fontAttr));
					return org;
				}
			});
		}  catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Update History screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Update History screen");
		} finally {
			getResultsTable().getColumnModel().getColumn(0).setPreferredWidth(200);
		}
	}

	private void onUnselectProgram() {
		clearResultTable();
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

	private void storageOfCorrectionDetails( List<Object[]> results)
	{
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
		vecResults = new Vector();
		Vector vecTestSeq = new Vector();
		Vector vecUserid = new Vector();
		Vector vecDate = new Vector();
		Vector vecTime = new Vector();
		Vector vecHistorySeq = new Vector();
		Vector vecStatus = new Vector();
		Vector vecProduction = new Vector();
		Vector vecModifyType = new Vector();
		HashMap mapTestSeq = new HashMap();
		TreeSet seqKind = new TreeSet();
		TreeSet paramKind = new TreeSet();
		HashMap mapValue = new HashMap();
		vecResultValues = new Vector();
		vecHistorySeq.add("HistSeq");
		vecUserid.add("USER_ID");
		vecDate.add("Date");
		vecTime.add("Time");
		vecTestSeq.add("Test Count");
		vecModifyType.add("Delete or Update");
		vecStatus.add("Status");
		vecProduction.add("Production");
		vecResults.add(vecHistorySeq);
		vecResults.add(vecUserid);
		vecResults.add(vecDate);
		vecResults.add(vecTime);
		vecResults.add(vecTestSeq);
		vecResults.add(vecModifyType);
		vecResults.add(vecStatus);
		vecResults.add(vecProduction);
		for (Object[] result:results) {
			Vector row = new Vector(Arrays.asList(result));
			String testSeq = row.get(0).toString().trim();
			String historySeq = row.get(4).toString().trim();
			if (!seqKind.contains(historySeq + "_" + testSeq)) {
				seqKind.add(historySeq + "_" + testSeq);
				vecTestSeq.add(testSeq);
				vecHistorySeq.add(historySeq);
				vecUserid.add(row.get(1).toString().trim());
				Date dat = (Date) row.get(2);
				vecDate.add(dfDate.format(dat));
				vecTime.add(dfTime.format(dat));
				String status = row.get(3).toString();
				if (status.trim().equals("")) {
					vecStatus.add("-");
				} else {
					vecStatus.add(LetInspectionStatus.getType(Integer.parseInt(status)).name());
				}
				String production = row.get(10).toString();
				if (production .equals("")) {
					vecProduction.add("-");
				} else {
					vecProduction.add(production);
				}
				vecModifyType.add(getModType(row.get(11).toString()));
			}
			String paramName = (String) row.get(7);
			if (!paramName.equals("")) {
				paramKind.add(paramName);
			}
			String paramValue = (String) row.get(5);
			if (!paramValue.equals("")) {
				mapValue.put(historySeq + "_" + testSeq + "_" + paramName, paramValue);
			}
		}
		for (Iterator iteParam = paramKind.iterator(); iteParam.hasNext();) {
			Vector vecValues = new Vector();
			String paramName = (String) iteParam.next();
			vecValues.add(paramName);
			for (Iterator iteSeq = seqKind.iterator(); iteSeq.hasNext();) {
				String value = (String) mapValue.get(iteSeq.next() + "_" + paramName);
				if (value == null) {
					vecValues.add("-");
				} else {
					vecValues.add(value);
				}
			}
			vecResultValues.add(vecValues);
		}		
	}

	private String getModType(String modtype)  {
		String value = null;
		if (modtype.equals("U")) {
			return "Update";
		} else if (modtype.trim().equals("D")) {
			return "Delete";
		} else {
			return "Unknown";
		}
	}

}