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
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import javax.swing.table.TableModel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.let.util.ExcelFileUtil;
import com.honda.galc.client.ui.FrozenFirstColumnPane;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.dao.product.LetProgramResultDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;




/**
 * 
 * @author Gangadhararao Gadde
 * @date Sept 23, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetInspectionResultPanel extends TabbedPanel implements ActionListener
{
	private JPanel basePanel = null;
	private JPanel mainPanel = null;
	private JLabel titleLabel = null;
	private JLabel vinLabel = null;
	private JLabel vin = null;
	private JLabel programLabel = null;
	private JScrollPane programScrollPane = null;
	private JTable programTable = null;
	private JLabel resultsLabel = null;
	private FrozenFirstColumnPane resultsScrollPane = null;
	private JTable resultsTable = null;
	private JButton updateButton = null;
	private JButton resetButton = null;
	private LetProductPanel productPanel = null;
	private Vector colorExpList = null;
	private ProgramCategoryColorExpanation programCategoryColorExpanation = null;
	private LetProgramCategoryUtility letProgramCategoryUtility=null;	
	private LetPropertyBean letPropertyBean=null;
	private DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
	private Vector vecResults = null;
	private Vector vecDate = null;
	private Vector vecTime = null;
	private Vector vecStatus = null;
	private Vector vecProcess = null;
	private Vector vecBRVersion = null;
	private Vector vecProduction = null;
	private Vector vecProgram=null;
	private List<Object[]> results=null;
	private HashMap mapAllValues=null;
	private HashMap mapAllUnits=null;
	private HashMap mapParamID=null;
	private HashMap mapUnit=null;
	private HashMap mapParamType=null;
	private SortedSet setParamName=null;
	private Vector vecTestSeq=null;
	private String[][] strResultValuesData=null;
	private Map<Integer, TableColumn> hiddenColumnMap;
	private final String HIDDEN_COLUMN = "HiddenColumn";
	private JButton showHideColumnBtn = null;
	private JButton exportBtn = null;
	private static final String HIDE_ALL_COLUMNS = "Hide All";
	private static final String SHOW_ALL_COLUMNS = "Show All";
	private static final String EXPORT = "Export";
	private static final String HIGH_LIMIT = "HiLimit";
	private static final String LOW_LIMIT = "LoLimit";
	private HashMap<String, String> lowLimitMap = null;
	private HashMap<String, String> highLimitMap = null;
	private Set<String> lowLimitSet = null;
	private Set<String> highLimitSet = null;

	public LetInspectionResultPanel(TabbedMainWindow mainWindow) {
		super("LET Inspection Result Panel", KeyEvent.VK_I,mainWindow);	
		initComponents();
	}

	public LetInspectionResultPanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(this.getApplicationId()).info("LET Inspection Result Panel is selected");
	}

	public void initComponents() 
	{		
		try {
			letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());  
			letProgramCategoryUtility=new LetProgramCategoryUtility(getApplicationId());
			colorExpList=letProgramCategoryUtility.getColorExplanation(letPropertyBean.getLetPgmCategories());
			setLayout(new BorderLayout());
			add(getMainPanel(),BorderLayout.CENTER);
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result screen");

		}
	}

	public JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getTitleLabel(), null);
			mainPanel.add(getProgramLabel(), null);
			mainPanel.add(getProgramScrollPane(), null);
			mainPanel.add(getResultsLabel(), null);
			mainPanel.add(getResultsScrollPane(), null);
			mainPanel.add(getProgramCategoryColorExpanation(), null);
			mainPanel.add(getShowHideColumnBtn(), null);
			mainPanel.add(getExportButton(), null);
			mainPanel.add(getProductPanelUtilities(), null);
		}
		return mainPanel;
	}

	public void actionPerformed(ActionEvent arg0) {

	}

	public JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel();
			titleLabel.setBounds(124, 11, 755, 29);
			titleLabel.setText(getTitleName());
			titleLabel.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return titleLabel;
	}

	public JLabel getProgramLabel() {
		if (programLabel == null) {
			programLabel = new JLabel();
			programLabel.setSize(218, 33);
			programLabel.setText(Message.get("InspectionProgram"));
			programLabel.setLocation(19, 127);
		}
		return programLabel;
	}

	public JScrollPane getProgramScrollPane() {
		if (programScrollPane == null) {
			programScrollPane = new JScrollPane();
			programScrollPane.setViewportView(getProgramTable());
			programScrollPane.setSize(218, 372);
			programScrollPane.setLocation(19, 160);
			programScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return programScrollPane;
	}

	public JTable getProgramTable() {
		if (programTable == null) {
			programTable = new JTable();
			programTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			programTable.setName("programTable");
			programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			programTable.getTableHeader().setReorderingAllowed(false);
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
			programTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row, column);
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					if (!isSelected) {
						if (!"".equals(model.getValueAt(row, 2)))
							org.setBackground(Color.decode((String) model.getValueAt(row, 2)));
						else
							org.setBackground(Color.WHITE);
					}
					return org;
				}
			});
			TableColumnModel columnModel = programTable.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
		}
		return programTable;
	}

	public JLabel getResultsLabel() {
		if (resultsLabel == null) {
			resultsLabel = new JLabel();
			resultsLabel.setSize(743, 33);
			resultsLabel.setText(Message.get("InspectionResultDetails"));
			resultsLabel.setLocation(258, 128);
		}
		return resultsLabel;
	}

	public FrozenFirstColumnPane getResultsScrollPane() {
		if (resultsScrollPane == null) {
			resultsScrollPane = new FrozenFirstColumnPane();
			resultsScrollPane.setViewportView(getResultsTable());
			resultsScrollPane.setSize(743, 372);
			resultsScrollPane.setLocation(252, 159);
			resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return resultsScrollPane;
	}

	public JTable getResultsTable() {
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
					JLabel org =(JLabel) super.getTableCellRendererComponent(table, value,isSelected, hasFocus,row, column);
					Map fontAttr = new HashMap();
					if (table.getValueAt(2, column) != null) {
						if (table.getValueAt(2, column).equals(LetInspectionStatus.Fail.name())) {
							org.setForeground(Color.RED);
						} else {
							org.setForeground(Color.BLACK);
						}
					}
					if (row > 5 && (column % 2 == 1) && (value != null && value.equals(""))) {
						org.setBackground(Color.PINK);
					} else {
						org.setBackground(Color.WHITE);
					}
					if (row < 6) {
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
	
	/**
	 * This method will be used to hide single column pair from result table.
	 * 
	 * @param columnIndex
	 *            modified column index based on new table column model
	 * @param actualColumnIndex
	 *            original column index
	 */
	private void hideInspectionResultColumnPair(int columnIndex, int actualColumnIndex) {
		TableColumn mainColumn = resultsTable.getColumnModel().getColumn(columnIndex);
		TableColumn unitColumn = resultsTable.getColumnModel().getColumn(columnIndex + 1);

		resultsTable.getColumnModel().removeColumn(mainColumn);
		resultsTable.getColumnModel().removeColumn(unitColumn);

		// Save the column so it can be redisplayed
		hiddenColumnMap = (LinkedHashMap<Integer, TableColumn>) resultsTable.getClientProperty(HIDDEN_COLUMN);
		if (hiddenColumnMap == null) {
			hiddenColumnMap = new LinkedHashMap<Integer, TableColumn>();
			resultsTable.putClientProperty(HIDDEN_COLUMN, hiddenColumnMap);
		}
		hiddenColumnMap.put(actualColumnIndex, mainColumn);
		hiddenColumnMap.put(actualColumnIndex + 1, unitColumn);
	}
	
	/**
	 * This method will be used to hide all the blank columns.
	 */
	private void hideAllBlankColumns() {
		clearErrorMessage();
		List<Integer> columnIndexList = new LinkedList<Integer>();

		for (int columnIndex = 0; columnIndex < resultsTable.getColumnCount(); columnIndex++) {
			TableColumn column = resultsTable.getColumnModel().getColumn(columnIndex);
			if (StringUtils.isNotBlank((String) column.getHeaderValue())) {
				String cellValue = (String) resultsTable.getValueAt(2, columnIndex);
				if (StringUtils.isBlank(cellValue)) {
					columnIndexList.add(columnIndex);
				}
			}
		}
		// call to remove column
		if (!columnIndexList.isEmpty()) {
			int counter = 0;
			for (Integer columnIndex : columnIndexList) {
				// column model will be reset after every removal hence need to
				// decrease the column index to remove the exact column
				hideInspectionResultColumnPair(columnIndex - counter, columnIndex);
				counter= counter + 2;
			}
		}
		// disable export button if all the columns are blank
		if (resultsTable.getColumnCount() == 0)
			exportBtn.setEnabled(false);
	}
	
	/**
	 * This method will be used to add all the hidden columns to the inspection
	 * result column model.
	 */
	private void showAllHiddenColumn() {
		clearErrorMessage();
		exportBtn.setEnabled(true);
		hiddenColumnMap = (LinkedHashMap<Integer, TableColumn>) resultsTable.getClientProperty(HIDDEN_COLUMN);

		if (hiddenColumnMap == null)
			return;

		for (Map.Entry<Integer, TableColumn> entry : hiddenColumnMap.entrySet()) {
			TableColumn column = entry.getValue();
			resultsTable.getColumnModel().addColumn(column);
			resultsTable.getColumnModel().moveColumn(resultsTable.getColumnCount() - 1, entry.getKey());
		}
		hiddenColumnMap.clear();
	}

	public LetProductPanel getProductPanelUtilities() {
		if (productPanel == null) {
			productPanel = new LetProductPanel(this);
			productPanel.addListener(new LetProductPanelListener() {
				public void actionPerformed() {
					clearProgramTable();
					clearResultTable();
					try {
						if(!productPanel.getData()) return;
						fetchLetPrograms();		
						onEnterProductId();
					}  catch (Exception e) {
						Logger.getLogger().error(e,"An error Occurred while processing the LET Inspection Result  screen");
						e.printStackTrace();
						setErrorMessage("An error Occurred while processing the LET Inspection Result  screen");
					}
				}
			});
			productPanel.setBounds(6, 47, 1008, 119);
		}
		return productPanel;
	}

	private void fetchLetPrograms()
	{		
		vecProgram = new Vector();
		Timestamp  finishTimestamp=getDao(LetResultDao.class).getMaxEndTimestamp(productPanel.getProductId().getText());
		if (finishTimestamp == null) {
			return  ;
		}
		boolean allFlg = false;
		String[] categories = letPropertyBean.getLetPgmCategories();
		if(categories.length==0)
		{
			allFlg=true;
		}
		List list = Arrays.asList(categories);
		Hashtable passCriteria = new Hashtable();
		Object[]  mtoData=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaForMto(productPanel.getModelYearCode(),productPanel.getModelCode(),productPanel.getModelTypeCode(),productPanel.getModelOptionCode(), finishTimestamp);
		if (mtoData!= null) {
			String criYearCode = (String)mtoData[0];
			String criModelCode = (String)mtoData[1];
			String criTypeCode = (String)mtoData[2];
			String criOptionCode = (String)mtoData[3];
			Timestamp efectiveTime = (Timestamp)mtoData[4];
			List<Object[]>  letPassCriteriaProgrmList=getDao(LetResultDao.class).getLetPassCriteriaProgramDataForMto(criYearCode, criModelCode, criTypeCode, criOptionCode, efectiveTime);
			for(Object[] passCriteriaProgram: letPassCriteriaProgrmList)
			{
				String criteriaProgramName = (String) passCriteriaProgram[2];
				passCriteria.put(criteriaProgramName, new Vector(Arrays.asList(passCriteriaProgram)));
			}
		}
		List<Object[]> distinctLetProgramsList=getDao(LetResultDao.class).findDistinctLetResultByProductIdSeq(productPanel.getProductId().getText());
		if(distinctLetProgramsList.size()==0)
		{			 
			return  ;
		}		
		
		for (Object[] program:distinctLetProgramsList) {
			if ((Integer)program[1] != null) {
				Vector value =  new Vector(Arrays.asList(program));
				String inspectionPgmName = (String)(program[0]) ;
				if(inspectionPgmName==null)
					continue;
				if (passCriteria.containsKey(inspectionPgmName)) {
					Vector v = (Vector) passCriteria.get(inspectionPgmName);
					String inspectionDeviceType = (String) v.get(0);
					String criteriaPgmAttribute = (String) v.get(1);
					String criteriaPgmName = (String) v.get(2);
					String bgColor = (String) v.get(3);
					if (allFlg || list.contains(inspectionDeviceType + criteriaPgmAttribute)) {
						value.add(bgColor);
						vecProgram.add(value);
					}
				} else {
					if (allFlg || list.contains("00")) {
						value.add("");
						vecProgram.add(value);
					}
				}
			}
		}	
		return ;   
	}

	public void onEnterProductId() {
		if (vecProgram.size()==0) {
			setErrorMessage("Inspection result not found.");
			Logger.getLogger(this.getApplicationId()).error("Inspection result not found.");
			return;
		}
		DefaultTableModel model = (DefaultTableModel) getProgramTable().getModel();
		if (vecProgram != null) {
			if (vecProgram.size() != 0) {
				for (int i = 0; i < vecProgram.size(); i++) {
					model.addRow((Vector) vecProgram.get(i));
				}
				getProgramTable().addRowSelectionInterval(0, 0);
			}
		}
	}

	public void onSelectProgram(int selectedRow) {
		try {
			clearResultTable();
			DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
			DefaultTableModel Pmodel = (DefaultTableModel) getProgramTable().getModel();
			model.setColumnCount(1);
			model.setRowCount(0);
			getInspectionDetailResults((Integer)Pmodel.getValueAt(selectedRow, 1));
			Vector firstTestSeq = (Vector) vecResults.get(0);
			addRow(model, vecResults, firstTestSeq);
			TableColumn rowHeader = getResultsTable().getColumnModel().getColumn(0);
			rowHeader.setCellRenderer(new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent( JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent( table, value,isSelected,hasFocus, row,column);
					Map fontAttr = new HashMap();
					if (row < 6) {
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
					} else {
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
					}
					org.setFont(new Font(fontAttr));
					return org;
				}
			});
			freezeFirstColumn();
			showHideColumnBtn.setEnabled(true);
			exportBtn.setEnabled(true);
		}  catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result screen");
		} 
	}
	
	/**
	 * Method will be used to freeze first column of result table.
	 */
	private void freezeFirstColumn() {
		resultsScrollPane.freezeFirstColumn("Item");
		resultsScrollPane.getFrozenTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				Map fontAttr = new HashMap();
				if (row <= 6) {
					fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
				} else if (LOW_LIMIT.equals(value) || HIGH_LIMIT.equals(value)) {
					fontAttr.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
				} else {
					fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
				}
				label.setBackground(new Color(182, 200, 252));
				label.setFont(new Font(fontAttr));
				return label;
			}
		});
	}

	protected DefaultTableModel addRow(DefaultTableModel model,Vector vecResults,Vector vecTestSeq) {
		for (int i = 1; i < vecTestSeq.size(); i++) {
			model.addColumn("Seq." + vecTestSeq.get(i));
			model.addColumn("");
		}
		int unit = vecResults.size();
		for (int i = 1; i < unit; i++) {
			model.addRow((Vector) vecResults.get(i));
		}
		for (String[] rowData : strResultValuesData) {
			model.addRow(rowData);

			// If any Test Parameter contains non zero high and low limit then we need to add row
			String paramName = rowData[0];
			if (lowLimitSet.contains(paramName)) {
				String[] lowLimitRow = new String[rowData.length];
				lowLimitRow[0] = LOW_LIMIT;
				int unitCounter = 0;
				for (int columnCounter = 1; columnCounter < vecTestSeq.size(); columnCounter++) {
					int finalCounter = columnCounter + unitCounter;
					lowLimitRow[finalCounter] = getLowLimitValue(columnCounter + "-" + paramName);
					lowLimitRow[finalCounter + 1] = null; // unit column needs to be null
					unitCounter = unitCounter + 1;
				}
				model.addRow(lowLimitRow);
			}
			if (highLimitSet.contains(paramName)) {
				String[] hiLimitRow = new String[rowData.length];
				hiLimitRow[0] = HIGH_LIMIT;
				int unitCounter = 0;
				for (int columnCounter = 1; columnCounter < vecTestSeq.size(); columnCounter++) {
					int finalCounter = columnCounter + unitCounter;
					hiLimitRow[finalCounter] = getHighLimitValue(columnCounter + "-" + paramName);
					hiLimitRow[finalCounter + 1] = null; // unit column needs to be null
					unitCounter = unitCounter + 1;
				}
				model.addRow(hiLimitRow);
			}
		}
		return model;
	}
	
	/**
	 * Method will be used to get low limit value of given parameter.
	 * 
	 * @param paramKey
	 * @return
	 */
	private String getLowLimitValue(String paramKey) {
		return lowLimitMap.get(paramKey);
	}

	/**
	 * Method will be used to get high limit value of given parameter.
	 * 
	 * @param parameterData
	 * @return
	 */
	private String getHighLimitValue(String paramKey) {
		return highLimitMap.get(paramKey);
	}

	public void onUnselectProgram() {
		clearResultTable();
	}

	public void clearProgramTable() {
		getProgramTable().removeAll();
		DefaultTableModel model = (DefaultTableModel) getProgramTable().getModel();
		model.setColumnCount(3);
		model.setRowCount(0);
		getProgramTable().removeColumn(getProgramTable().getColumnModel().getColumn(1));
		getProgramTable().removeColumn(getProgramTable().getColumnModel().getColumn(1));
		TableColumnModel columnModel = getProgramTable().getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(200);
	}

	public void clearResultTable() {
		setErrorMessage("");
		DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
		model.setColumnCount(1);
		model.setRowCount(0);
		getResultsTable().removeAll();
		TableColumnModel columnModel = getResultsTable().getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(200);
		getResultsTable().putClientProperty(HIDDEN_COLUMN, null);
		showHideColumnBtn.setText(HIDE_ALL_COLUMNS);
		showHideColumnBtn.setEnabled(false);
		exportBtn.setEnabled(false);
		// clear frozen table details
		JTable frozenTable = resultsScrollPane.getFrozenTable();
		if (frozenTable != null) {
			DefaultTableModel frozenModel = (DefaultTableModel) frozenTable.getModel();
			frozenModel.setColumnCount(0);
			frozenModel.setRowCount(0);
			frozenTable.removeAll();
			model.setColumnCount(0);
		}
	}

	public ProgramCategoryColorExpanation getProgramCategoryColorExpanation() {
		if (programCategoryColorExpanation == null) {
			programCategoryColorExpanation = new ProgramCategoryColorExpanation(colorExpList);
			programCategoryColorExpanation.setBounds(795, 58, 100, 50);			
		}
		return programCategoryColorExpanation;
	}
	
	private JButton getExportButton() {
		if (exportBtn == null) {
			exportBtn = new JButton();
			exportBtn.setEnabled(false);
			exportBtn.setText(EXPORT);
			exportBtn.setBounds(890, 125, 85, 30);

			exportBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onExportAction();
				}
			});
		}
		return exportBtn;
	}

	public JButton getShowHideColumnBtn() {
		if (showHideColumnBtn == null) {
			showHideColumnBtn = new JButton();
			showHideColumnBtn.setEnabled(false);
			showHideColumnBtn.setText(HIDE_ALL_COLUMNS);
			showHideColumnBtn.setBounds(795, 125, 85, 30);

			showHideColumnBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (showHideColumnBtn.getText().equals(HIDE_ALL_COLUMNS)) {
						hideAllBlankColumns();
						showHideColumnBtn.setText(SHOW_ALL_COLUMNS);
					} else {
						showAllHiddenColumn();
						showHideColumnBtn.setText(HIDE_ALL_COLUMNS);
					}
				}
			});
		}
		return showHideColumnBtn;
	}

	public String getTitleName() {
		return getScreenName();
	}
	public void processDetailResults(String productId, Integer programId)
	{
		vecResults = new Vector();
		vecTestSeq = new Vector();
	    vecDate = new Vector();
	    vecTime = new Vector();
	    vecStatus = new Vector();
	    vecProcess = new Vector();
	    vecBRVersion = new Vector();
	    vecProduction = new Vector();
		vecTestSeq.add("TestSeq");
		vecDate.add("Date");
		vecTime.add("Test End Time");
		vecStatus.add("Status");
		vecProcess.add("Process");
		vecBRVersion.add("Base Release Ver.");
		vecProduction.add("Production");
		vecResults.add(vecTestSeq);
		vecResults.add(vecDate);
		vecResults.add(vecTime);
		vecResults.add(vecStatus);
		vecResults.add(vecProcess);
		vecResults.add(vecBRVersion);
		vecResults.add(vecProduction);
		setParamName = Collections.synchronizedSortedSet(new TreeSet());
		mapParamID = new HashMap();
		HashMap mapParamUnit = new HashMap();
		HashMap mapValues = new HashMap();
		mapParamType = new HashMap();
		mapAllValues = new HashMap();
		mapAllUnits = new HashMap();
		lowLimitMap = new HashMap<String, String>();
		highLimitMap = new HashMap<String, String>();
		lowLimitSet = new HashSet<String>();
		highLimitSet = new HashSet<String>();
		String strTestSeq = null;
		Boolean passTerminated = null;
		for (Object[] result:results) {
			if (!vecTestSeq.contains(((Integer) result[0]).toString())) {
				if (mapValues != null && !mapValues.isEmpty()) {
					mapAllValues.put(strTestSeq, mapValues);
				}
				if (mapUnit != null && !mapUnit.isEmpty()) {
					mapAllUnits.put(strTestSeq, mapUnit);
				}
				mapValues = new HashMap();
				mapUnit = new HashMap();
				strTestSeq = ((Integer) result[0]).toString();
				vecTestSeq.add(strTestSeq);
				if (result[1] == null) {
					vecDate.add("");
					vecTime.add("");
				} else {
					Timestamp timestamp = (Timestamp) result[1];
					Date dat=new Date(timestamp.getTime());
					vecDate.add(dfDate.format(dat));
					vecTime.add(dfTime.format(dat));
				}
				if (result[2] == null) {
					vecStatus.add("");
				} else {
					Integer value =Integer.parseInt((String)result[2]) ;
					String statusName = "";
					LetInspectionStatus status = LetInspectionStatus.getType(value);
					if (status != null) {
						statusName = status.name();
					}
					if (LetInspectionStatus.Pass.equals(status)) {
						if (passTerminated == null) {
							LetProgramResultDao dao =  getDao(LetProgramResultDao.class); 
							passTerminated = dao.isProgramResultPassTerminated(productId, programId.intValue());
						}
						if (passTerminated) {
							statusName = LetInspectionStatus.PASS_TERMINATED.getLabel();
						}
					}					
					vecStatus.add(statusName);
				}
				if (result[3] == null) {
					vecProcess.add("");
				} else {
					if (!result[3].toString().trim().equals("")) {
						vecProcess.add((String) result[3]);
					}
				}
				if (result[9] == null) {
					vecBRVersion.add("");
				} else {
					if (!result[9].toString().trim().equals("")) {
						vecBRVersion.add((String)result[9]);
					}
				}
				if (result[10] == null) {
					vecProduction.add("");
				} else {
					if (!result[10].toString().trim().equals("")) {
						vecProduction.add((String) result[10]);
					}
				}
				vecDate.add("");
				vecTime.add("");
				vecStatus.add("");
				vecProcess.add("");
				vecBRVersion.add("");
				vecProduction.add("");
			}
			String paramKey = null;
			if (result[5] != null && result[4] != null) {
				paramKey = ((String) result[5]).toString() + "-" + ((String) result[4]).toString();
				
				lowLimitMap.put(strTestSeq + "-" + ((String) result[4]), StringUtils.defaultIfEmpty(((String) result[11]), ""));
				highLimitMap.put(strTestSeq + "-" + ((String) result[4]), StringUtils.defaultIfEmpty(((String) result[12]), ""));
			}
			if (result[4] != null && setParamName.add(paramKey)) {
				mapParamID.put(paramKey, (Integer) result[6]);
			}
			mapValues.put(paramKey, ObjectUtils.defaultIfNull((String) result[7],""));
			if (result[8] != null) {
				mapUnit.put(paramKey, result[8].toString().trim());
			}
			if (result[5] != null) {
				mapParamType.put(paramKey, (String) result[5]);
			}
			if (paramKey != null && StringUtils.isNotBlank((String) result[11])) {
				lowLimitSet.add(paramKey.substring(2));
			}
			if (paramKey != null && StringUtils.isNotBlank((String) result[12])) {
				highLimitSet.add(paramKey.substring(2));
			}
		}
		mapAllValues.put(strTestSeq, mapValues);
		mapAllUnits.put(strTestSeq, mapUnit);
	}

	public void getInspectionDetailResults(Integer programId){

		results=getDao(LetResultDao.class).getInspectionDetailResults(productPanel.getProductId().getText(),programId);
		processDetailResults(productPanel.getProductId().getText(), programId);
		Iterator itrTestSeq = vecTestSeq.iterator();
		Iterator itrParamName = setParamName.iterator();
		int iColumn = 0;
		int iRow = 0;
		int iMaxColumn = vecTestSeq.size() * 2 + 2;
		int unitColumn = 0;
		String strParamName = "";
		HashMap mapValues = null;
		strResultValuesData = new String[setParamName.size()][iMaxColumn];
		while (itrTestSeq.hasNext()) {
			String strTestSeq = (String) itrTestSeq.next();
			if (mapAllValues.containsKey(strTestSeq)) {
				mapValues = (HashMap) mapAllValues.get(strTestSeq);
				mapUnit = mapAllUnits.containsKey(strTestSeq) ? (HashMap) mapAllUnits.get(strTestSeq) : null;
				while (itrParamName.hasNext()) {
					unitColumn = iColumn + 1;
					strParamName = (String) itrParamName.next();
					if (mapValues.containsKey(strParamName)) {
						strResultValuesData[iRow][iColumn] = (String) mapValues.get(strParamName);
						strResultValuesData[iRow][unitColumn] = mapUnit == null ? null : (String) mapUnit.get(strParamName);
					}
					iRow++;
				}
				itrParamName = setParamName.iterator();
			}
			if (iColumn == 0) {
				iColumn++;
			} else {
				iColumn = iColumn + 2;
			}
			iRow = 0;
		}
		itrParamName = setParamName.iterator();
		iColumn = 0;
		while (itrParamName.hasNext()) {
			strParamName = (String) itrParamName.next();
			strResultValuesData[iColumn][0] = strParamName.substring(2);
			strResultValuesData[iColumn][iMaxColumn - 2] = String.valueOf((Integer) mapParamID.get(strParamName));
			strResultValuesData[iColumn++][iMaxColumn - 1] = String.valueOf((String) mapParamType.get(strParamName));
		}
	}
	
	/**
	 * This method will be used to export data in Excel format.
	 */
	private void onExportAction() {
		try {
			clearErrorMessage();
			boolean isExportSuccessful = ExcelFileUtil.writeDataToExcelFile(prepareResultTable(), "LetInspectionResult_");
			if (isExportSuccessful) {
				setMessage("File exported successfully...");
				Logger.getLogger(this.getApplicationId()).info("File exported successfully...");
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e, "An error Occurred while exporting LET Inspection Result details");
			setErrorMessage("An error Occurred while exporting LET Inspection Result details");
		}
	}
	
	private JTable prepareResultTable() {
		TableModel model = new DefaultTableModel(resultsTable.getRowCount(), resultsTable.getColumnCount() + 1);
		JTable table = new JTable(model);
		JTable frozenTable = resultsScrollPane.getFrozenTable();
		// set first column header
		table.getColumnModel().getColumn(0).setHeaderValue(frozenTable.getColumnModel().getColumn(0).getHeaderValue());

		for (int i = 0; i < resultsTable.getRowCount(); i++) {
			// populate first column
			String firstColumnValue = (String) frozenTable.getValueAt(i, 0);
			model.setValueAt(firstColumnValue, i, 0);
			for (int j = 0; j < resultsTable.getColumnCount(); j++) {
				// set column header
				table.getColumnModel().getColumn(j + 1).setHeaderValue(resultsTable.getColumnModel().getColumn(j).getHeaderValue());
				String value = (String) resultsTable.getValueAt(i, j);
				model.setValueAt(value, i, j + 1);
			}
		}
		return table;
	}
	
}