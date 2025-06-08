
package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;


import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.property.LetPropertyBean;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.font.TextAttribute;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import com.honda.galc.message.Message;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetModifyHistoryDao;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.dao.product.LetProgramResultHistoryDao;
import com.honda.galc.dao.product.LetProgramValueHistoryDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.dao.product.LetResultHistoryDao;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.LetModifyHistory;
import com.honda.galc.entity.product.LetModifyHistoryId;
import com.honda.galc.entity.product.LetProgramResultHistory;
import com.honda.galc.entity.product.LetProgramResultHistoryId;
import com.honda.galc.entity.product.LetProgramValueHistory;
import com.honda.galc.entity.product.LetProgramValueHistoryId;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.entity.product.LetResultHistory;
import com.honda.galc.entity.product.LetResultHistoryId;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 21, 2013
 */

@SuppressWarnings(value = { "all" })
public class LetInspectionResultUpdatePanel extends TabbedPanel implements ActionListener {

    private static final String shortDescType = "3";
    private JPanel mainPanel = null;
    private JLabel titleLabel = null;
    private JLabel programLabel = null;
    private JScrollPane programScrollPane = null;
    private JTable programTable = null;
    private JLabel resultsLabel = null;
    private JScrollPane resultsScrollPane = null;
    private JTable resultsTable = null;
    private JButton updateButton = null;
    private JButton resetButton = null;
    private LetProductPanel productPanel = null;
    private String[][] strResultValues;
    private String[][] strEditResultValues;
    private int iSelectedProgram;
    private String[] resultStatus = null;
    private String[] resultProduction = null;
    private String[][] tableStatus = null;
    private String dbEncoding;
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
    private HashMap mapParamID=null;
    private HashMap mapUnit=null;
    private HashMap mapParamType=null;
    private SortedSet setParamName=null;
    private Vector firstTestSeqVec=null;
    private Vector vecTestSeq=null;
    private LetPropertyBean letPropertyBean=null;
    private DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
    private boolean initMessageDisplayed;

    public LetInspectionResultUpdatePanel(TabbedMainWindow mainWindow) {
        super("LET Inspection Result Update Panel", KeyEvent.VK_U,mainWindow);  
        initComponents();
    }

    public LetInspectionResultUpdatePanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
        super(screenName, keyEvent,mainWindow);
    }

    @Override
    public void onTabSelected() {
        setErrorMessage(null);
        Logger.getLogger(this.getApplicationId()).info("LET Inspection Result Update Panel is selected");
        if (!isInitMessageDisplayed()) {
            String msg = LetInspectionResultManualInputPanel.INIT_MSG;
            JOptionPane.showMessageDialog(this, msg, "Information Message", JOptionPane.INFORMATION_MESSAGE);
            setInitMessageDisplayed(true);
        }       
    }

    public void initComponents() 
    {       
        try {
            letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());  
            dbEncoding =letPropertyBean.getDbEncoding();        
            setLayout(new BorderLayout());
            add(getMainPanel(),BorderLayout.CENTER);
        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Update screen");
            e.printStackTrace();
            setErrorMessage("An error Occurred while processing the LET Inspection Result Update screen");
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
            mainPanel.add(getUpdateButton(), null);
            mainPanel.add(getResetButton(), null);
            mainPanel.add(getProductPanelUtilities(), null);
        }
        return mainPanel;
    }

    private JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel();
            titleLabel.setBounds(124, 11, 755, 29);
            titleLabel.setText(Message.get("LETInspectionResultUpdate"));
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
            programScrollPane.setSize(218, 323);
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
            programTable.setName("programTable");
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
            resultsScrollPane.setSize(743, 323);
            resultsScrollPane.setLocation(252, 159);
            resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return resultsScrollPane;
    }


    private JTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = new JTable() {
                public void setValueAt(Object value, int row, int column) {
                    if (row > 2 && column > 0 && value != null) {
                        int len = -1;
                        try {
                            len = ((String) value).getBytes(dbEncoding).length;
                        } catch (Exception e) {
                            Logger.getLogger().error(e,"An error Occurred while processing the LET Inspection Result Update screen");
                            e.printStackTrace();
                            setErrorMessage("An error Occurred while processing the LET Fault Result Update screen");
                            return;
                        }
                        if (row == 5 && column % 2 == 1 && value != null) {
                            if (len > 1 || !value.equals("1") && !value.equals("0")) {
                                setEditingRow(row);
                                setEditingColumn(column);
                                Logger.getLogger().error("Invalid value.");
                                setErrorMessage("Invalid value.");
                            }
                        }
                        if (len > 32) {
                            setEditingRow(row);
                            setEditingColumn(column);
                            Logger.getLogger().error("Invalid value.");
                            setErrorMessage("Invalid value.");
                        }
                    }
                    super.setValueAt(value, row, column);
                }
            };
            resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            resultsTable.getTableHeader().setReorderingAllowed(false);
            resultsTable.setName("resultsTable");
            DefaultTableModel model = setDefaultTableModel();
            model.addColumn(Message.get("ITEM"));
            resultsTable.setModel(model);
            TableColumnModel columnModel = resultsTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200);
            resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                    JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                    Map fontAttr = new HashMap();
                    if (column > 0 && table.getValueAt(2, column).equals(LetInspectionStatus.Fail.name())) {
                        org.setForeground(Color.RED);
                    } else {
                        org.setForeground(Color.BLACK);
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

    protected DefaultTableModel setDefaultTableModel() {
        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                if (row > 5) {
                    if ((column % 2) == 0) {
                        return false;
                    }
                    if (getValueAt(2, column) == null) {
                        return false;
                    } else if (getValueAt(2, column).toString().trim().equals("")) {
                        return false;
                    } else if (getValueAt(row, column) != null&& !shortDescType.equals(getValueAt(row, getColumnCount() - 1))) {
                        TableColumn sportColumn = resultsTable.getColumnModel().getColumn(column);
                        sportColumn.setCellEditor(new DefaultCellEditor(new JTextField()));
                        return true;
                    }
                } else if (row == 2 && (column % 2) == 1) {
                    if (getValueAt(row, column) == null) {
                        return false;
                    } else if (getValueAt(row, column).toString().trim().equals("")) {
                        return false;
                    } else if (getValueAt(row, column) != null) {
                        if (!getValueAt(row, column).equals("")) {
                            TableColumn sportColumn = resultsTable.getColumnModel().getColumn(column);
                            sportColumn.setCellEditor(new DefaultCellEditor(new JComboBox(getStatus())));
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else if (row == 5 && (column % 2) == 1) {
                    TableColumn sportColumn = resultsTable.getColumnModel().getColumn(column);
                    sportColumn.setCellEditor(new DefaultCellEditor(new JTextField()));
                    return true;
                }
                return false;
            }
        };
        return model;
    }

    private JButton getUpdateButton() {
        if (updateButton == null) {
            updateButton = new JButton();
            updateButton.setSize(110, 35);
            updateButton.setName("updateButton");
            updateButton.setText(Message.get("UPDATE"));
            updateButton.setLocation(763, 498);
            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onUpdate();
                }
            });
        }
        return updateButton;
    }

    private JButton getResetButton() {
        if (resetButton == null) {
            resetButton = new JButton();
            resetButton.setSize(110, 35);
            resetButton.setText(Message.get("RESET"));
            resetButton.setLocation(888, 498);
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onReset();
                }
            });
        }
        return resetButton;
    }

    private LetProductPanel getProductPanelUtilities() {
        if (productPanel == null) {
            productPanel = new LetProductPanel(this);
            productPanel.addListener(new LetProductPanelListener() {
                public void actionPerformed() {
                    clearProgramTable();
                    clearResultTable();
                    disabledButton();
                    try {
                        if(!productPanel.getData()) return;
                        fetchLetPrograms();
                        onEnterProductId();
                    }  catch (Exception e) {
                        Logger.getLogger().error(e,"An error Occurred while processing the LET Inspection Result Update screen");
                        e.printStackTrace();
                        setErrorMessage("An error Occurred while processing the LET Fault Result Update screen");

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


    private void onSelectProgram(int selectedRow) {
        try {
            disabledButton();
            iSelectedProgram = selectedRow;
            clearResultTable();
            DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
            DefaultTableModel Pmodel = (DefaultTableModel) getProgramTable().getModel();
            model.setColumnCount(1);
            model.setRowCount(0);
            getInspectionDetailResults((Integer)Pmodel.getValueAt(selectedRow, 1));
            firstTestSeqVec = (Vector) vecResults.get(0);
            model = addRow(model, firstTestSeqVec, vecResults);
            getResultsTable().removeColumn(getResultsTable().getColumnModel().getColumn(model.getColumnCount() - 1));
            getResultsTable().removeColumn(getResultsTable().getColumnModel().getColumn(model.getColumnCount() - 2));
            TableColumn rowHeader = getResultsTable().getColumnModel().getColumn(0);
            rowHeader.setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                    JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
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
            int cnt = 0;
            for (int i = 6; i < getResultsTable().getRowCount(); i++) {
                cnt = i - 6;
                for (int j = 0; j < getResultsTable().getColumnCount() - 1; j++) {
                    if (getResultsTable().getValueAt(i, j) == null) {
                        strEditResultValues[cnt][j] = "";
                    } else {
                        strEditResultValues[cnt][j] = getResultsTable().getValueAt(i, j).toString().trim();
                    }
                }
            }

        }  catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Update screen");
            e.printStackTrace();
            setErrorMessage("An error Occurred while processing the LET Fault Result Update screen");       } finally {
                getResultsTable().getColumnModel().getColumn(0).setPreferredWidth(200);
            }
    }

    public void processDetailResults()
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
        mapUnit = new HashMap();
        mapParamType = new HashMap();
        mapAllValues = new HashMap();
        String strTestSeq = null;
        for (Object[] result:results) {
            if (!vecTestSeq.contains(((Integer) result[0]).toString())) {
                if (mapValues != null && !mapValues.isEmpty()) {
                    mapAllValues.put(strTestSeq, mapValues);
                }
                mapValues = new HashMap();
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
                    vecStatus.add(LetInspectionStatus.getType(value).name());
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
        }
        mapAllValues.put(strTestSeq, mapValues);
    }

    public void getInspectionDetailResults(Integer programId){

        results=getDao(LetResultDao.class).getInspectionDetailResults(productPanel.getProductId().getText(),programId);
        processDetailResults();
        Iterator itrTestSeq = vecTestSeq.iterator();
        Iterator itrParamName = setParamName.iterator();
        int iColumn = 0;
        int iRow = 0;
        int iMaxColumn = vecTestSeq.size() * 2 + 2;
        int unitColumn = 0;
        String strParamName = "";
        HashMap mapValues = null;
        strResultValues = new String[setParamName.size()][iMaxColumn];
        while (itrTestSeq.hasNext()) {
            String strTestSeq = (String) itrTestSeq.next();
            if (mapAllValues.containsKey(strTestSeq)) {
                mapValues = (HashMap) mapAllValues.get(strTestSeq);
                while (itrParamName.hasNext()) {
                    unitColumn = iColumn + 1;
                    strParamName = (String) itrParamName.next();
                    if (mapValues.containsKey(strParamName)) {
                        strResultValues[iRow][iColumn] = (String) mapValues.get(strParamName);
                        strResultValues[iRow][unitColumn] = (String) mapUnit.get(strParamName);
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
            strResultValues[iColumn][0] = strParamName.substring(2);
            strResultValues[iColumn][iMaxColumn - 2] = String.valueOf((Integer) mapParamID.get(strParamName));
            strResultValues[iColumn++][iMaxColumn - 1] = String.valueOf((String) mapParamType.get(strParamName));
        }
    }

    protected DefaultTableModel addRow(DefaultTableModel model, Vector vecTestSeq,Vector vecResults) {
        resultStatus = new String[((Vector) vecResults.get(3)).size()];
        resultProduction = new String[((Vector) vecResults.get(6)).size()];
        for (int i = 1; i < vecTestSeq.size(); i++) {
            model.addColumn("Seq." + vecTestSeq.get(i));
            model.addColumn("");
        }
        model.addColumn("ParamId");
        model.addColumn("ParamType");
        int unit = vecResults.size();
        for (int i = 1; i < unit; i++) {
            model.addRow((Vector) vecResults.get(i));
            if (i == 3) {
                for (int j = 0; j < ((Vector) vecResults.get(i)).size(); j++) {
                    if (((Vector) vecResults.get(i)).get(j) != null) {
                        if (!((Vector) vecResults.get(i)).get(j).equals("")) {
                            if (j != 0) {
                                enabledButton();
                            }
                            resultStatus[j] = ((Vector) vecResults.get(i)).get(j).toString().trim();
                        }
                    }
                }
            } else if (i == 6) {
                for (int j = 0; j < ((Vector) vecResults.get(i)).size(); j++) {
                    if (((Vector) vecResults.get(i)).get(j) != null) {
                        if (!((Vector) vecResults.get(i)).get(j).equals("")) {
                            if (j != 0) {
                                enabledButton();
                            }
                            resultProduction[j] = ((Vector) vecResults.get(i)).get(j).toString().trim();
                        }
                    }
                }
            }
        }
        if (strResultValues.length > 0) {
            strEditResultValues = new String[strResultValues.length][strResultValues[0].length];
            for (int i = 0; i < strResultValues.length; i++) {
                for (int j = 0; j < strResultValues[i].length; j++) {
                    strEditResultValues[i][j] = strResultValues[i][j];
                }
            }
            enabledButton();
        }
        for (int i = 0; i < strResultValues.length; i++) {
            model.addRow(strResultValues[i]);
        }
        return model;
    }

    private void onInspectionResultValue(int row, int column) {
        if (row != -1 && column != -1) {
            String strValue = (String) getResultsTable().getValueAt(row, column);
            strEditResultValues[row - 3][column] = strValue;
            clearErrorMessageArea();
        }
    }

    private void onUpdate() {
        String strValue = null;
        String strEditValue = null;
        Vector vecModValues = new Vector();
        String[][] resultTableValues =new String[getResultsTable().getRowCount()][getResultsTable().getColumnCount()];
        int columnCnt = 1;
        int valueCntr = 5;
        int cntr = 0;
        for (int i = 6; i < getResultsTable().getRowCount(); i++) {
            for (int j = 0; j < getResultsTable().getColumnCount(); j++) {
                cntr = i - 6;
                if (getResultsTable().getValueAt(i, j) == null) {
                    resultTableValues[cntr][j] = "";
                } else {
                    resultTableValues[cntr][j] = getResultsTable().getValueAt(i, j).toString().trim();
                }
            }
        }
        try {
            judgementForUpdate(vecModValues, columnCnt, valueCntr, tableStatus, resultTableValues);
        }
        catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Update screen");
            e.printStackTrace();
            setErrorMessage("An error Occurred while processing the LET Fault Result Update screen");
            return;
        }
        if (vecModValues.isEmpty()) {
            Logger.getLogger(this.getApplicationId()).error("No value has been changed.");
            setErrorMessage("No value has been changed.");
            return;
        }
        int iValue = JOptionPane.showConfirmDialog(this,Message.get("ReallyUpdate?"),Message.get("UpdateConfirmation"),JOptionPane.YES_NO_OPTION);
        try {
            if (iValue == JOptionPane.YES_OPTION) {
                getModResultValues(vecModValues);
                onSelectProgram(iSelectedProgram);
            }
        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Update screen");
            e.printStackTrace();
            setErrorMessage("An error Occurred while processing the LET Fault Result Update screen");
        }
    }


    private void getModResultValues(Vector vecModValues) {

        Integer iHistSeq=getDao(LetModifyHistoryDao.class).getMaxHistorySeq(productPanel.getProductId().getText());
        
        DefaultTableModel Pmodel = (DefaultTableModel) getProgramTable().getModel();
        Integer inspectionPgmId = (Integer)Pmodel.getValueAt(iSelectedProgram, 1);
        saveLetModifyHistory(iHistSeq);
        for (Iterator itrModValues = vecModValues.iterator(); itrModValues.hasNext();) 
        {
            String[] strModValue = (String[]) itrModValues.next();
            String productId= productPanel.getProductId().getText();
            Integer testSeq= StringUtils.isEmpty(strModValue[0])?0:Integer.parseInt(strModValue[0]);
            Integer inspectionParamId= StringUtils.isEmpty(strModValue[1])?0:Integer.parseInt(strModValue[1]);
            String inspectionParamValue= strModValue[2];
            String inspectionParamType= strModValue[3];
            String processStatus= strModValue[4];
            String production= strModValue[5];
            if (strModValue[4].equals("") && strModValue[5].equals("")) 
            {
                Object[] programValueData=getDao(LetProgramValueHistoryDao.class).getProgramValueData( productId, testSeq, inspectionPgmId, inspectionParamId, inspectionParamType);
                saveLetProgramValueHistory(programValueData,iHistSeq);
                getDao(LetProgramValueHistoryDao.class).updateProgramValueData( productId , testSeq, inspectionPgmId, inspectionParamId, inspectionParamValue, inspectionParamType);
                logUserAction("updated LetProgramValueHistory for " + productId);
            } else if (!strModValue[5].equals("")) {
                LetResult letResult=getDao(LetResultDao.class).findLetResultByProductIdSeqNum(productPanel.getProductId().getText(),Integer.parseInt(strModValue[0]));
                if(letResult!=null)
                {
                    saveLetResultHistory(letResult,Integer.parseInt(strModValue[0]));
                }                   
                getDao(LetResultDao.class).updateLetResultProduction( productId , testSeq, production);    
                logUserAction("updated LetResult for " + productId);
            } else {

                Object[] letProgramResult=getDao(LetProgramResultHistoryDao.class).getProgramResultData( productId , testSeq, inspectionPgmId);
                saveLetProgramResultHistory(letProgramResult,iHistSeq);
                getDao(LetResultDao.class).updateProgramResultByEndTimestamp( productId , testSeq, inspectionPgmId, processStatus);
                logUserAction("updated LetResult for " + productId);
            }
        } 
    }

    private void saveLetModifyHistory(Integer historySeq) 
    {
        LetModifyHistoryId letModifyHistoryId=new LetModifyHistoryId(productPanel.getProductId().getText(),historySeq);
        LetModifyHistory letModifyHistory=new LetModifyHistory();
        letModifyHistory.setId(letModifyHistoryId);
        letModifyHistory.setUserId(getMainWindow().getUserId());
        letModifyHistory.setModifyTimestamp(new Timestamp(System.currentTimeMillis()));
        letModifyHistory.setModifyType("U");
        getDao(LetModifyHistoryDao.class).save(letModifyHistory);
        logUserAction(SAVED, letModifyHistory);
    }

    private void saveLetResultHistory(LetResult letResult,Integer testSeqNum) 
    {
        LetResultHistoryId letHistoryId=new LetResultHistoryId();
        letHistoryId.setProductId(letResult.getId().getProductId());
        letHistoryId.setHistorySeq(testSeqNum);
        letHistoryId.setTestSeq(letResult.getId().getTestSeq());
        LetResultHistory letResultHistory=new LetResultHistory();
        letResultHistory.setId(letHistoryId);
        letResultHistory.setAdditionalData(letResult.getAdditionalData());
        letResultHistory.setBaseRelease(letResult.getBaseRelease());
        letResultHistory.setBuildCode(letResult.getBuildCode());
        letResultHistory.setContStepFile(letResult.getContStepFile());
        letResultHistory.setEndTimestamp(letResult.getEndTimestamp());
        letResultHistory.setInspectionMto(letResult.getInspectionMto());
        letResultHistory.setLetLineNo(letResult.getLetLineNo());
        letResultHistory.setLetMfgAreaCode(letResult.getLetMfgAreaCode());
        letResultHistory.setLetMfgNo(letResult.getLetMfgNo());
        letResultHistory.setProduction(letResult.getProduction());
        letResultHistory.setSeqRange(letResult.getSeqRange());
        letResultHistory.setSeqStepFile(letResult.getSeqStepFile());
        letResultHistory.setStartTimestamp(letResult.getStartTimestamp());
        letResultHistory.setTotalResultStatus(letResult.getTotalResultStatus());
        letResultHistory.setTestId(letResult.getTestId());
        letResultHistory.setCreateTimestamp(letResult.getCreateTimestamp());
        letResultHistory.setUpdateTimestamp(letResult.getUpdateTimestamp());
        getDao(LetResultHistoryDao.class).save(letResultHistory);
        logUserAction(SAVED, letResultHistory);
    }

    private void saveLetProgramResultHistory(Object[] letResultData,Integer testSeqNum) 
    {
        LetProgramResultHistoryId  letProgramResultHistoryId=new LetProgramResultHistoryId();
        letProgramResultHistoryId.setProductId((String)letResultData[0]);
        letProgramResultHistoryId.setTestSeq((Integer)letResultData[1]);
        letProgramResultHistoryId.setInspectionPgmId((Integer)letResultData[2]);
        letProgramResultHistoryId.setHistorySeq(testSeqNum);
        LetProgramResultHistory letProgramResultHistory=new LetProgramResultHistory();
        letProgramResultHistory.setId(letProgramResultHistoryId);
        letProgramResultHistory.setInspectionPgmStatus((String)letResultData[3]);
        letProgramResultHistory.setProcessStep((String)letResultData[4]);
        letProgramResultHistory.setProcessStatus((String)letResultData[5]);
        letProgramResultHistory.setProcessStartTimestamp((Timestamp)letResultData[6]);            
        letProgramResultHistory.setProcessEndTimestamp((Timestamp)letResultData[7]);
        letProgramResultHistory.setLetTerminalId((String)letResultData[8]);
        letProgramResultHistory.setLetResultCal((String)letResultData[9]);
        letProgramResultHistory.setLetResultDcrev((String)letResultData[10]);
        letProgramResultHistory.setSoftwareVersion((String)letResultData[11]);
        letProgramResultHistory.setLetOperatorId((String)letResultData[12]);
        letProgramResultHistory.setInCycleRetestNum((Integer)letResultData[13]);
        letProgramResultHistory.setCreateTimestamp((Timestamp)letResultData[14]);
        letProgramResultHistory.setUpdateTimestamp((Timestamp)letResultData[15]);
        getDao(LetProgramResultHistoryDao.class).save(letProgramResultHistory);     
        logUserAction(SAVED, letProgramResultHistory);
    }

    private void saveLetProgramValueHistory(Object[] letResultValueData,Integer testSeqNum) 
    {   
        LetProgramValueHistoryId  letProgramValueHistoryId=new LetProgramValueHistoryId();
        letProgramValueHistoryId.setProductId((String)letResultValueData[0]);
        letProgramValueHistoryId.setTestSeq((Integer)letResultValueData[1]);
        letProgramValueHistoryId.setInspectionPgmId((Integer)letResultValueData[2]);
        letProgramValueHistoryId.setInspectionParamId((Integer)letResultValueData[3]);
        letProgramValueHistoryId.setHistorySeq(testSeqNum);
        letProgramValueHistoryId.setInspectionParamType((String)letResultValueData[4]);
        LetProgramValueHistory letProgramValueHistory=new LetProgramValueHistory();
        letProgramValueHistory.setId(letProgramValueHistoryId);
        letProgramValueHistory.setInspectionParamValue((String)letResultValueData[5]);
        letProgramValueHistory.setInspectionParamUnit((String)letResultValueData[6]); 
        letProgramValueHistory.setCreateTimestamp((Timestamp)letResultValueData[7]);
        letProgramValueHistory.setUpdateTimestamp((Timestamp)letResultValueData[8]);
        getDao(LetProgramValueHistoryDao.class).save(letProgramValueHistory);
        logUserAction(SAVED, letProgramValueHistory);
    }

    protected Vector judgementForUpdate(Vector vecModValues,int columnCnt,int valueCntr,String[][] tableStatus,String[][] resultTableValues)  
    {
        String strValue;
        String strEditValue;
        String resultTableValue;
        if (strEditResultValues != null) {
            for (int i = 0; i < strEditResultValues.length; i++) {
                boolean flg = true;
                boolean ProductionFlg = true;
                int seqcntr = 0;
                if (getResultsTable().getRowCount() > 6) {
                    for (int j = 0; j < strEditResultValues[i].length - 3; j++) {
                        strValue = strEditResultValues[i][j];
                        resultTableValue = resultTableValues[i][j];
                        if (j % 2 == 1) {
                            seqcntr++;
                        }
                        if (strValue != null && resultTableValue != null && !(strValue.equals(resultTableValue))) {
                            String[] strModValue = new String[]{(String) firstTestSeqVec.get(seqcntr),(String) strEditResultValues[i][strEditResultValues[0].length - 2],resultTableValue,(String) strEditResultValues[i][strEditResultValues[0].length - 1],"",""};
                            vecModValues.add(strModValue);
                        }
                    }
                }
            }
        }
        String beforeData = "";
        String updateData = "";
        int testSeq = 1;
        for (int i = 1; i < getResultsTable().getColumnCount(); i = i + 2) {
            beforeData = (String) resultStatus[i];
            updateData = (String) getResultsTable().getValueAt(2, i);
            if (beforeData != null && updateData != null) {
                if (!beforeData.equals(updateData)) {
                    String[] strModValue = new String[]{(String) firstTestSeqVec.get(testSeq),"","","",getStatus(updateData),""};
                    vecModValues.add(strModValue);
                }
            }

            beforeData = (String) resultProduction[i];
            updateData = (String) getResultsTable().getValueAt(5, i);
            if (!beforeData.equals(updateData)) {
                String[] strModValue = new String[]{(String) firstTestSeqVec.get(testSeq),"","","","",updateData};
                vecModValues.add(strModValue);
            }
            testSeq++;
        }
        return vecModValues;
    }

    private void onReset()
    {
        int iValue = JOptionPane.showConfirmDialog(this,Message.get("ReallyReset?"),Message.get("ResetConfirmation"),JOptionPane.YES_NO_OPTION);
        if (iValue == JOptionPane.YES_OPTION) {
            getResultsTable();
            onSelectProgram(iSelectedProgram);
        } else {
            getResultsTable();
        }
    }

    private void onUnselectProgram() 
    {
        clearResultTable();
        disabledButton();
    }

    private void clearProgramTable() 
    {
        getProgramTable().removeAll();
        DefaultTableModel model = (DefaultTableModel) getProgramTable().getModel();
        model.setColumnCount(2);
        model.setRowCount(0);
        getProgramTable().removeColumn(getProgramTable().getColumnModel().getColumn(1));
        TableColumnModel columnModel = getProgramTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
    }

    private void clearResultTable() 
    {
        clearErrorMessageArea();
        DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
        model.setColumnCount(1);
        model.setRowCount(0);
        getResultsTable().removeAll();
        TableColumnModel columnModel = getResultsTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
    }

    private void clearErrorMessageArea() 
    {
        setErrorMessage("");       
    }

    private void enabledButton() 
    {
        getUpdateButton().setEnabled(true);
        getResetButton().setEnabled(true);
        getUpdateButton().setForeground(Color.BLACK);
        getResetButton().setForeground(Color.BLACK);
    }

    private void disabledButton() 
    {
        getUpdateButton().setEnabled(false);
        getResetButton().setEnabled(false);
        getUpdateButton().setForeground(Color.GRAY);
        getResetButton().setForeground(Color.GRAY);
    }

    private Vector getStatus() 
    {
        Vector statusMap = new Vector();
        LetInspectionStatus[] statuses=LetInspectionStatus.values();
        for(int i=0;i<statuses.length;i++)
        {
            statusMap.add(i,statuses[i].name());
        }
        return statusMap;
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

    public void actionPerformed(ActionEvent arg0) {

    }

    protected boolean isInitMessageDisplayed() {
        return initMessageDisplayed;
    }

    protected void setInitMessageDisplayed(boolean initMessageDisplayed) {
        this.initMessageDisplayed = initMessageDisplayed;
    }
}
