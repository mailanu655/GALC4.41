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
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import com.honda.galc.client.teamleader.let.util.LETInspectionProgramData;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.dao.product.LetProgramResultDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.LetXmlService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.enumtype.LetProgramResultValueEnum;
import com.honda.galc.entity.product.LetProgramResult;
import com.honda.galc.entity.product.LetProgramResultId;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.entity.product.LetResultId;
import com.honda.galc.entity.enumtype.LetProgramResultEnum;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Sept 02, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetInspectionResultManualInputPanel extends TabbedPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    public static final String INIT_MSG = "Changes on this screen will not be reflected in all screens or reports which reference XML contents within GALC. \n" +
            "Please proceed with caution. \n" +
            "If you have questions on the impact of this process you are about to perform please contact your local GALC support member for details.";
    
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
    private JButton addResultButton = null;
    private JButton addPGMButton = null;
    private LetProductPanel productPanel = null;
    private String[][] strResultValues;
    private String[][] strEditResultValues;
    private Vector firstTestSeqVec = null;
    private int iSelectedProgram;
    private JDialog addProgramDialog = null;
    private JPanel addProgramContentPane = null;
    private JLabel addProgramLabel = null;
    private JComboBox addProgramComboBox = null;
    private JButton addProgramButton = null;
    private JButton canselProgramButton = null;
    private Vector addProgramData = null;
    private String updateType = "";
    private String nowDate = "";
    private String nowTime = "";
    private int testSeq = 0;
    private String dbEncoding;
    private Vector programsVector=null;
    private List<Object[]> results=null;
    private HashMap mapAllValues=null;
    private HashMap mapParamID=null;
    private HashMap mapParamType=null;
    private SortedSet setParamName=null;
    private Vector vecTestSeq=null;
    private Vector vecResults=null;
    private boolean initMessageDisplayed;

    public LetInspectionResultManualInputPanel(TabbedMainWindow mainWindow) {
        super("LET Inspection Result Manual Input Panel", KeyEvent.VK_I,mainWindow);    
        initComponents();
    }

    @Override
    public void onTabSelected() {
        setErrorMessage(null);
        Logger.getLogger(this.getApplicationId()).info("LET Inspection Result Manual Input Panel is selected");
        if (!isInitMessageDisplayed()) {
            String msg = INIT_MSG;
            JOptionPane.showMessageDialog(this, msg, "Information Message", JOptionPane.INFORMATION_MESSAGE);
            setInitMessageDisplayed(true);
        }
    }


    private void initComponents() 
    {
        setLayout(new BorderLayout());
        add(getMainPanel(),BorderLayout.CENTER);
        setSize(1024, 768);
        disabledButton();
        this.disabledResultButton();
        this.disabledPGMButton();
        LetPropertyBean letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());  
        dbEncoding =letPropertyBean.getDbEncoding();
        this.getMainWindow().addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                getProductPanelUtilities().getInitialFocus();
            }
        });
    }


    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(null);
            mainPanel.add(getTitleLabel(), null);
            mainPanel.add(getProgramLabel(), null);
            mainPanel.add(getProgramScrollPane(), null);
            mainPanel.add(getResultsLabel(), null);
            mainPanel.add(getAddResultButton(), null);
            mainPanel.add(getResultsScrollPane(), null);
            mainPanel.add(getUpdateButton(), null);
            mainPanel.add(getResetButton(), null);
            mainPanel.add(getProductPanelUtilities(), null);
            mainPanel.add(getAddPGMButton(), null);
        }
        return mainPanel;
    }

    private JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel();
            titleLabel.setBounds(124, 11, 755, 29);
            titleLabel.setText(Message.get("LET_ADD_INSPECTION_RESULT"));
            titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
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
            programTable.setName("programTable");
            programTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            programTable.getTableHeader().setReorderingAllowed(false);
            programTable
            .getSelectionModel()
            .addListSelectionListener(new ListSelectionListener() {
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
            resultsLabel.setSize(737, 40);
            resultsLabel.setText(("InspectionResultDetails"));
            resultsLabel.setLocation(258, 121);
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
                        } catch (UnsupportedEncodingException e) {
                            setErrorMessage("Exception occurred in Client program.");
                            return;
                        }
                        if (len > 32) {
                            setEditingRow(row);
                            setEditingColumn(column);
                            setErrorMessage("Invalid value.");
                            throw new IllegalArgumentException();
                        }
                    }
                    super.setValueAt(value, row, column);
                }
            };
            resultsTable.setName("resultsTable");
            resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            resultsTable.getTableHeader().setReorderingAllowed(false);
            DefaultTableModel model = new DefaultTableModel() {
                public boolean isCellEditable(int row, int column) {
                    if (row < 5&& column == firstTestSeqVec.size() + 2&& column != 0) {
                        if (row != 2) {
                            TableColumn sportColumn =resultsTable.getColumnModel().getColumn(firstTestSeqVec.size());
                            sportColumn.setCellEditor(new DefaultCellEditor(new JTextField()));
                        } else {
                            JComboBox inspectionCombo =new JComboBox(getStatus());
                            inspectionCombo.setBackground(Color.white);
                            TableColumn sportColumn =resultsTable.getColumnModel().getColumn(firstTestSeqVec.size());
                            sportColumn.setCellEditor(new DefaultCellEditor(inspectionCombo));
                        }
                        return true;
                    }
                    return false;
                }
            };
            model.addColumn(Message.get("ITEM"));
            resultsTable.setModel(model);
            TableColumnModel columnModel = resultsTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200);
            resultsTable.setDefaultRenderer(Object.class,new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                    JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                    Map fontAttr = new HashMap();
                    if (column > 0 && LetInspectionStatus.Fail.name().equals(table.getValueAt(2, column))) {
                        org.setForeground(Color.RED);
                    } else {
                        org.setForeground(Color.BLACK);
                    }
                    org.setBackground(Color.WHITE);
                    if (row < 5) {
                        org.setHorizontalAlignment(JLabel.CENTER);
                        fontAttr.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_BOLD);
                    } else {
                        org.setHorizontalAlignment(JLabel.RIGHT);
                        fontAttr.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_REGULAR);
                    }
                    if (column == 0) {
                        setHorizontalAlignment(this.LEFT);
                    }
                    org.setFont(new Font(fontAttr));
                    return org;
                }
            });
        }
        return resultsTable;
    }

    private Vector getStatus() {
        Vector statusMap = new Vector();
        LetInspectionStatus[] statuses=LetInspectionStatus.values();
        for(int i=0;i<statuses.length;i++)
        {
            statusMap.add(i,statuses[i].name());
        }
        return statusMap;
    }

    private JButton getUpdateButton() {
        if (updateButton == null) {
            updateButton = new JButton();
            updateButton.setName("updateButton");
            updateButton.setSize(110, 35);
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
            resetButton.setName("resetButton");
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

    private JButton getAddResultButton() {
        if (addResultButton == null) {
            addResultButton = new JButton();
            addResultButton.setName("addResultButton");
            addResultButton.setSize(163, 35);
            addResultButton.setText(Message.get("ADD_INSPECTION_RESULT"));
            addResultButton.setLocation(831, 122);
            addResultButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onAddResult();
                }
            });
        }
        return addResultButton;
    }

    private JButton getAddPGMButton() {
        if (addPGMButton == null) {
            addPGMButton = new JButton();
            addPGMButton.setName("addPGMButton");
            addPGMButton.setBounds(72, 501, 110, 35);
            addPGMButton.setText(Message.get("ADD_PGM"));
            addPGMButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onAddPGM();
                }
            });
        }
        return addPGMButton;
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
                        programsVector= fetchLetPrograms();                                         
                        if (programsVector.size()==0){              
                            setErrorMessage("Inspection result not found");
                            disabledResultButton();
                            disabledPGMButton();
                            return;
                        }else
                        {
                            onEnterProductId();
                            enabledResultButton();
                            enabledPGMButton();
                        }               
                    }  catch (Exception e) {
                        disabledResultButton();
                        disabledPGMButton();
                        e.printStackTrace();
                        setErrorMessage("An error Occurred while processing the LET Inspection Manual Input Panel");
                    }
                }
            });
            productPanel.setBounds(6, 47, 1008, 119);
        }
        return productPanel;
    }

    private Vector fetchLetPrograms()
    {
        programsVector = new Vector();
        List<Object[]> letProgramsList=getDao(LetResultDao.class).findDistinctLetResultByProductIdSeq(productPanel.getProductId().getText());
        for (Object[] letProgram:letProgramsList)
        {
            Vector rowData = new Vector();
            if ((Integer) letProgram[1] != null) {
                rowData.addElement(letProgram[0]);
            }
            programsVector.add(rowData);
        }

        return programsVector;
    }

    private void onEnterProductId() {
        updateType = "";
        DefaultTableModel model =(DefaultTableModel) getProgramTable().getModel();
        if (programsVector != null) {
            if (programsVector.size() != 0) {
                for (int i = 0; i < programsVector.size(); i++) {
                    model.addRow((Vector) programsVector.get(i));
                }
                getProgramTable().addRowSelectionInterval(0, 0);
            }
        }
    }

    private void onSelectProgram(int selectedRow) {

        try {
            disabledButton();
            enabledResultButton();
            enabledPGMButton();
            iSelectedProgram = selectedRow;
            clearResultTable();
            DefaultTableModel model =(DefaultTableModel) getResultsTable().getModel();
            DefaultTableModel Pmodel =(DefaultTableModel) getProgramTable().getModel();
            if (updateType.equals("PROGRAM")) {
                Pmodel.removeRow(Pmodel.getRowCount() - 1);
                updateType = "";
            }
            model.setColumnCount(1);
            model.setRowCount(0);
            strResultValues=fetchResults(Pmodel.getValueAt(selectedRow, 0).toString());
            firstTestSeqVec = (Vector) vecResults.get(0);
            int maxTestSeq = 0;
            testSeq = 0;
            for (int i = 1; i < firstTestSeqVec.size(); i++) {
                model.addColumn("No." + firstTestSeqVec.get(i));
                maxTestSeq = Integer.parseInt((String) firstTestSeqVec.get(i));
                if (maxTestSeq > testSeq) {
                    testSeq = maxTestSeq;
                }
            }
            testSeq++;
            model.addColumn("ParamId");
            model.addColumn("ParamType");
            model.addRow((Vector) vecResults.get(1));
            model.addRow((Vector) vecResults.get(2));
            model.addRow((Vector) vecResults.get(3));
            model.addRow((Vector) vecResults.get(4));
            model.addRow((Vector) vecResults.get(5));
            if (strResultValues!=null&&strResultValues.length > 0) {
                strEditResultValues =new String[strResultValues.length][strResultValues[0].length];
                for (int i = 0; i < strResultValues.length; i++) {
                    for (int j = 0; j < strResultValues[i].length; j++) {
                        strEditResultValues[i][j] = strResultValues[i][j];
                    }
                }
            }
            for (int i = 0; i < strResultValues.length; i++) {
                model.addRow(strResultValues[i]);
            }
            getResultsTable().removeColumn(getResultsTable().getColumnModel().getColumn(firstTestSeqVec.size() + 1));
            getResultsTable().removeColumn(getResultsTable().getColumnModel().getColumn(firstTestSeqVec.size()));
            TableColumn rowHeader =
                getResultsTable().getColumnModel().getColumn(0);
            rowHeader.setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(
                        JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                    JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                    Map fontAttr = new HashMap();
                    if (row < 5) {
                        fontAttr.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_BOLD);
                    } else {
                        fontAttr.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_REGULAR);
                    }
                    org.setFont(new Font(fontAttr));
                    return org;
                }
            });

        }  catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
        } finally {
            getResultsTable().getColumnModel().getColumn(0).setPreferredWidth(200);
        }
    }

    private void onUpdate() {

        int iValue =JOptionPane.showConfirmDialog(this,Message.get("ReallyUpdate?"),Message.get("UpdateConfirmation"),JOptionPane.YES_NO_OPTION);
        if (iValue != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            DefaultTableModel programModel =(DefaultTableModel) getProgramTable().getModel();
            DefaultTableModel resultModel =(DefaultTableModel) getResultsTable().getModel();
            String date =resultModel.getValueAt(0, firstTestSeqVec.size() + 2).toString();
            String time =resultModel.getValueAt(1, firstTestSeqVec.size() + 2).toString();
            String pgmStatus =(String) resultModel.getValueAt(2, firstTestSeqVec.size() + 2);
            String process =(String) resultModel.getValueAt(3, firstTestSeqVec.size() + 2);
            String production =(String) resultModel.getValueAt(4, firstTestSeqVec.size() + 2);
            if (date == null || date.equals("")|| time == null|| time.equals("")|| pgmStatus == null|| pgmStatus.equals("")) {
                setErrorMessage("Either Date, Time or Status are not inputted.");                  
                return;
            }
            if (process == null || process.equals("")) {
                setErrorMessage("Process is an indispensable input.");  
                return;
            }
            if (production == null || production.equals("")) {
                setErrorMessage("Production is an indispensable input.");   
                return;
            }
            if (!this.checkPattern(date, "yyyy-MM-dd", "Invalid Date (yyyy-mm-dd)")|| !this.checkPattern(time, "HH:mm:ss", "Invalid Time (HH:mm:ss)")) {
                return;
            }
            String dateTime = date + time;
            for (int i = 1; i < firstTestSeqVec.size(); i++) {
                if (dateTime.equals(resultModel.getValueAt(0, i).toString()+ resultModel.getValueAt(1, i).toString())) {
                    setErrorMessage(" The Timestamp overlaps.");    
                    return;
                }
            }
            if (dateTime.compareToIgnoreCase(nowDate + nowTime) > 0) {
                setErrorMessage("Timestamp is future days");
                return;
            }
            if (process.length() < 2 || process.length() > 9) {
                setErrorMessage("The number of characters of Process is from 2 to 9 digits.");
                return;
            }
            String fstCharProcess = process.substring(0, 1);
            if (fstCharProcess.equals("S")) {

            } else if (fstCharProcess.equals("C")) {
                int processIndex = process.indexOf("-");
                if (processIndex == -1) {
                    setErrorMessage("- is necessary on the way when the first character of Process starts by C");
                    return;
                } else {
                    if (process.length() < 3|| !process.substring(processIndex + 1,processIndex + 2).equals("C")) {
                        setErrorMessage("The character after - is only C when the first character of Process starts by C.");
                        return;
                    }
                }
            } else {
                setErrorMessage("The first character of Process is only 'S' or 'C'.");                  
                return;
            }
            if (!production.equals("0") && !production.equals("1")) {
                setErrorMessage("Please input either of 0 or 1 to Production.");         
                return;
            }
            insertInspectionProgramResult(productPanel.getProductId().getText(),new Integer(testSeq).toString(),date + " " + time,programModel.getValueAt(iSelectedProgram, 0).toString(),pgmStatus,getMainWindow().getUserId(),process,production);
            fetchLetPrograms();
            updateType = "";
            onSelectProgram(iSelectedProgram);
        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
        }
    }

    private boolean checkPattern(String target, String pattern, String msg) {

        SimpleDateFormat dateFormat =(SimpleDateFormat) DateFormat.getInstance();
        dateFormat.setLenient(false);
        try {
            dateFormat.applyPattern(pattern);
            Date dateTarget;
            dateTarget = dateFormat.parse(target);
            String targetStringAfter = dateFormat.format(dateTarget);
            if (!target.equals(targetStringAfter)) {
                setErrorMessage(msg);
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void onReset() {
        int iValue =JOptionPane.showConfirmDialog(this,Message.get("MSG_INITIAL_STATE"),Message.get("ResetConfirmation"),JOptionPane.YES_NO_OPTION);
        if (iValue == JOptionPane.YES_OPTION) {
            if (updateType.equals("PROGRAM")) {
                onSelectProgram(0);
                getProgramTable().setRowSelectionInterval(0, 0);
                getProgramScrollPane().getVerticalScrollBar().setValue(getProgramScrollPane().getVerticalScrollBar().getMinimum());
            } else {
                onSelectProgram(iSelectedProgram);
            }
        }
        updateType = "";
    }

    private void onAddResult() {
        DefaultTableModel model =(DefaultTableModel) getResultsTable().getModel();
        model.addColumn("No." + testSeq);
        Date date = new Date();
        SimpleDateFormat dataFormat =new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        String dateTime[] = dataFormat.format(date).split(",");
        nowDate = dateTime[0];
        nowTime = dateTime[1];
        model.setValueAt(nowDate, 0, model.getColumnCount() - 1);
        model.setValueAt(nowTime, 1, model.getColumnCount() - 1);
        for (int i = model.getColumnCount(); i > 0; i--) {
            if (model.getColumnName(i).equals("ParamId")
                    || model.getColumnName(i).equals("ParamType")) {
                getResultsTable().removeColumn(
                        getResultsTable().getColumnModel().getColumn(i));
            }
        }
        TableColumnModel columnModel = resultsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        enabledButton();
        disabledResultButton();
        disabledPGMButton();
        updateType = "RESULT";
    }

    private void onAddPGM() {
        try {                   
            addProgramData=getAddProgram();
            if (addProgramData.size() > 0) {
                addProgramDialog = new JDialog(this.getMainWindow(), true);
                JDialog x=new JDialog();
                addProgramDialog.setContentPane(getAddProgramContentPane());
                addProgramDialog.setSize(434, 184);
                addProgramDialog.setTitle(Message.get("UpdateConfirmation"));
                addProgramDialog.setLocation(200, 200);
                addProgramDialog.show();
            } else {
                setErrorMessage("The Inspection program already has results.");
            }
        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
        }

    }


    private javax.swing.JPanel getAddProgramContentPane() {
        addProgramContentPane = new JPanel();
        addProgramContentPane.setLayout(null);
        addProgramContentPane.add(getAddProgramLabel(), null);
        addProgramContentPane.add(getAddProgramComboBox(), null);
        addProgramContentPane.add(getAddProgramButton(), null);
        addProgramContentPane.add(getCanselProgramButton(), null);
        return addProgramContentPane;
    }


    private JLabel getAddProgramLabel() {
        if (addProgramLabel == null) {
            addProgramLabel = new JLabel();
            addProgramLabel.setBounds(8, 9, 161, 36);
            addProgramLabel.setText(Message.get("Program"));
        }
        return addProgramLabel;
    }


    private javax.swing.JComboBox getAddProgramComboBox() {
        addProgramComboBox = new JComboBox();
        addProgramComboBox.setBounds(50, 52, 318, 35);
        addProgramComboBox.setBackground(Color.white);
        for (int i = 0; i < addProgramData.size(); i++) {
            String program = addProgramData.get(i).toString();
            program =program.substring(program.indexOf("[") + 1,program.indexOf("]"));
            addProgramComboBox.addItem(program);
        }
        return addProgramComboBox;
    }


    private javax.swing.JButton getAddProgramButton() {
        if (addProgramButton == null) {
            addProgramButton = new JButton();
            addProgramButton.setBounds(67, 96, 123, 38);
            addProgramButton.setText(Message.get("OK"));
            addProgramButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultTableModel model =(DefaultTableModel) getProgramTable().getModel();
                    model.addRow((Vector) addProgramData.get(addProgramComboBox.getSelectedIndex()));
                    programTable.setRowSelectionInterval(model.getRowCount() - 2,model.getRowCount() - 1);
                    addProgramDialog.dispose();
                    onAddResult();
                    getProgramScrollPane().validate();
                    getProgramScrollPane().getVerticalScrollBar().setValue(getProgramScrollPane().getVerticalScrollBar().getMaximum());
                    updateType = "PROGRAM";
                    return;
                }
            });
        }
        return addProgramButton;
    }


    private JButton getCanselProgramButton() {
        if (canselProgramButton == null) {
            canselProgramButton = new JButton();
            canselProgramButton.setSize(123, 38);
            canselProgramButton.setLocation(230, 96);
            canselProgramButton.setText(Message.get("CANCEL"));
            canselProgramButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateType = "";
                    enabledResultButton();
                    enabledPGMButton();
                    disabledButton();
                    addProgramDialog.dispose();
                    return;
                }
            });
        }
        return canselProgramButton;
    }

    private void onUnselectProgram() {
        clearResultTable();
        disabledButton();
    }

    private void clearProgramTable() {
        getProgramTable().removeAll();
        DefaultTableModel model =(DefaultTableModel) getProgramTable().getModel();
        model.setColumnCount(2);
        model.setRowCount(0);
        getProgramTable().removeColumn(getProgramTable().getColumnModel().getColumn(1));
        TableColumnModel columnModel = getProgramTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
    }

    private void clearResultTable() {
        clearErrorMessageArea();
        DefaultTableModel model =(DefaultTableModel) getResultsTable().getModel();
        model.setColumnCount(1);
        model.setRowCount(0);
        getResultsTable().removeAll();
        TableColumnModel columnModel = getResultsTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
    }

    private void clearErrorMessageArea() {
        setErrorMessage("");
    }

    private void enabledButton() {
        getUpdateButton().setEnabled(true);
        getResetButton().setEnabled(true);
        getUpdateButton().setForeground(Color.BLACK);
        getResetButton().setForeground(Color.BLACK);
    }

    private void disabledButton() {
        getUpdateButton().setEnabled(false);
        getResetButton().setEnabled(false);
        getUpdateButton().setForeground(Color.GRAY);
        getResetButton().setForeground(Color.GRAY);
    }

    private void enabledResultButton() {
        getAddResultButton().setEnabled(true);
        getAddResultButton().setForeground(Color.BLACK);
    }

    private void enabledPGMButton() {
        getAddPGMButton().setEnabled(true);
        getAddPGMButton().setForeground(Color.BLACK);
    }

    private void disabledResultButton() {
        getAddResultButton().setEnabled(false);
        getAddResultButton().setForeground(Color.GRAY);
    }

    private void disabledPGMButton() {
        getAddPGMButton().setEnabled(false);
        getAddPGMButton().setForeground(Color.GRAY);
    }


    public void actionPerformed(ActionEvent arg0) {
    }

    private void insertInspectionProgramResult(String productId,String testSeq,String endTime,String pgmIdStr,String status,String addtionalData,String process,String production)
    {       
        LETInspectionProgramData iPgm = null;
        try {
            iPgm = LETInspectionProgramData.getInstance();
            String pgmId = iPgm.getInspectionPgmId(pgmIdStr);
            String pgmStatus = null;
            if (status != null && !status.equals("")) {
                pgmStatus = String.valueOf(LetInspectionStatus.valueOf(status).getId());
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date parsedDate = dateFormat.parse(endTime);
            java.sql.Timestamp endTimestamp = new java.sql.Timestamp(parsedDate.getTime());
            createLetResult(productId,addtionalData,endTimestamp,production,testSeq);
            createLetProgramResult(endTimestamp,productId,pgmId,pgmStatus,process, testSeq);
        } catch (Exception e) {
            discard(iPgm);
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
        } 
    }

    public void createLetResult(String productId, String addtionalData, Timestamp endTimestamp, String production, String testSeqValue)
    {
        try {
            LetResult letResult=new LetResult();
            LetResultId letResultId=new LetResultId();
            letResultId.setProductId(productId);
            letResultId.setTestSeq(Integer.parseInt(testSeqValue));
            letResult.setId(letResultId);
            letResult.setAdditionalData(addtionalData);
            letResult.setEndTimestamp(endTimestamp);
            letResult.setProduction(production);        
            getDao(LetResultDao.class).save(letResult);
            logUserAction(SAVED, letResult);
        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
        } 
    }
    public void createLetProgramResult(Timestamp endTimestamp, String productId, String pgmId, String pgmStatus, String process,String testSeqValue)
    {
        try {
            Calendar cal = GregorianCalendar.getInstance();
            Timestamp currentTimestamp = new Timestamp(cal.getTimeInMillis());          
            String letPgmResultTableName = ServiceFactory.getService(LetXmlService.class).getPhysicalTableName(currentTimestamp, LetProgramResult.class);
            LetProgramResult letProgramResult = Enum.valueOf(LetProgramResultEnum.class, letPgmResultTableName).getLetPgmResultClass().newInstance();
            LetProgramResultId letProgramResultId = new LetProgramResultId();
            letProgramResult.setId(letProgramResultId);
            letProgramResult.getId().setEndTimestamp(endTimestamp);
            letProgramResult.getId().setProductId(productId);
            letProgramResult.getId().setTestSeq(Integer.parseInt(testSeqValue));
            letProgramResult.getId().setInspectionPgmId(Integer.parseInt(pgmId));
            letProgramResult.setInspectionPgmStatus(pgmStatus);
            letProgramResult.setProcessStep(process);
            letProgramResult.setProcessStartTimestamp(endTimestamp);
            letProgramResult.setProcessEndTimestamp(endTimestamp);
            getDao(LetProgramResultDao.class).save(letProgramResult);
            logUserAction(SAVED, letProgramResult);
        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
        }

    }

    private void discard(LETInspectionProgramData iPgm) {
        if (iPgm != null) {
            iPgm.discard();
        }
    }

    private Vector getAddProgram()  {
        Vector addProgram = new Vector();
        try {       
            Calendar cal = GregorianCalendar.getInstance();
            Timestamp currentTimestamp = new Timestamp( cal.getTimeInMillis());
            Object[] letPassCriteria=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaForMto(productPanel.getModelYearCode(),productPanel.getModelCode(),productPanel.getModelTypeCode(),productPanel.getModelOptionCode(),currentTimestamp);
            if (letPassCriteria ==null) {
                Logger.getLogger(this.getApplicationId()).error("Inspection program not found");
                setErrorMessage("Inspection program not found");
                return addProgram;
            }
            List<Object[]> letPassCriteriaProgramList=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaProgramData((String) letPassCriteria[0],(String) letPassCriteria[1],(String) letPassCriteria[2],(String) letPassCriteria[3],(Timestamp)letPassCriteria[4]);
            if (letPassCriteriaProgramList.size() == 0) {
                Logger.getLogger(this.getApplicationId()).error("Inspection program not found");    
                setErrorMessage("Inspection program not found");
                return addProgram;
            }
            Vector programsVector = new Vector();
            for (Object[] letPassCriteriaProgram:letPassCriteriaProgramList)
            {
                Vector rowData = new Vector();
                if ( letPassCriteriaProgram[0] != null) {
                    rowData.addElement(letPassCriteriaProgram[0]);
                }
                programsVector.add(rowData);
            }       
            for (int i = 0; i < letPassCriteriaProgramList.size(); i++) {
                boolean addFlg = true;
                for (int j = 0; j < programsVector.size(); j++) {
                    if (programsVector.get(i).toString().equals(programsVector.get(j).toString())) {
                        addFlg = false;
                        break;
                    }
                }
                if (addFlg) {
                    addProgram.add(programsVector.get(i));
                }
            }

        }  catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Manual Input Panel");
            e.printStackTrace();
        } 
        return addProgram;
    }

    private String[][] fetchResults(String selectedRow)
    {
        LETInspectionProgramData iPgm = null;
        iPgm = LETInspectionProgramData.getInstance(); 
        Integer pgmId=Integer.parseInt(iPgm.getInspectionPgmId(selectedRow));
        results = getDao(LetResultDao.class).getLetManualInputResultData(productPanel.getProductId().getText(),pgmId);
        if(results==null||results.size()==0)
        {
            Logger.getLogger(this.getApplicationId()).error("Detail results not found");
            setErrorMessage("Detail results not found");
            return null ;
        }
        processResults();
        Iterator itrTestSeq = vecTestSeq.iterator();
        Iterator itrParamName = setParamName.iterator();
        int iColumn = 0;
        int iRow = 0;
        int iMaxColumn = vecTestSeq.size() + 2;
        String strParamName = "";
        HashMap mapValues = null;

        String[][] strResultValues =new String[setParamName.size()][iMaxColumn];
        while (itrTestSeq.hasNext()) {
            String strTestSeq = (String) itrTestSeq.next();
            if (mapAllValues.containsKey(strTestSeq)) {
                mapValues = (HashMap) mapAllValues.get(strTestSeq);
                while (itrParamName.hasNext()) {
                    strParamName = (String) itrParamName.next();
                    if (mapValues.containsKey(strParamName)) {
                        strResultValues[iRow][iColumn] =(String) mapValues.get(strParamName);
                    }
                    iRow++;
                }
                itrParamName = setParamName.iterator();
            }
            iColumn++;
            iRow = 0;
        }
        itrParamName = setParamName.iterator();
        iColumn = 0;
        while (itrParamName.hasNext()) {
            strParamName = (String) itrParamName.next();
            strResultValues[iColumn][0] = strParamName.substring(2);
            strResultValues[iColumn][iMaxColumn - 2] =String.valueOf((Integer) mapParamID.get(strParamName));
            strResultValues[iColumn++][iMaxColumn - 1] = String.valueOf((String) mapParamType.get(strParamName));
        }
        return strResultValues;     
    }

    public void processResults()
    {
        vecResults = new Vector();
        vecTestSeq = new Vector();
        Vector vecDate = new Vector();
        Vector vecTime = new Vector();
        Vector vecStatus = new Vector();
        Vector vecProcess = new Vector();
        Vector vecProduction = new Vector();
        vecTestSeq.add("TestSeq");
        vecDate.add("Date");
        vecTime.add("Time");
        vecStatus.add("Status");
        vecProcess.add("Process");
        vecProduction.add("Production");
        vecResults.add(vecTestSeq);
        vecResults.add(vecDate);
        vecResults.add(vecTime);
        vecResults.add(vecStatus);
        vecResults.add(vecProcess);
        vecResults.add(vecProduction);
        setParamName =Collections.synchronizedSortedSet(new TreeSet());
        mapParamID = new HashMap();
        mapParamType = new HashMap();
        HashMap mapValues = null;
        mapAllValues = new HashMap();
        DateFormat DF_DATE =new SimpleDateFormat("yyyy-MM-dd");
        DateFormat DF_TIME = new SimpleDateFormat("HH:mm:ss");
        String strTestSeq = null;
        for (Object[] result:results) {
            if (!vecTestSeq.contains(((Integer) result[0]).toString())) {
                if (mapValues != null && !mapValues.isEmpty()) {
                    mapAllValues.put(strTestSeq, mapValues);
                }
                mapValues = new HashMap();
                strTestSeq =((Integer) result[0]).toString();
                vecTestSeq.add(strTestSeq);
                if (result[1] == null || "".equals(result[1])) {
                    vecDate.add("");
                    vecTime.add("");
                } else {
                    Date dat = (Date) result[1];
                    vecDate.add(DF_DATE.format(dat));
                    vecTime.add(DF_TIME.format(dat));
                }
                if (result[2] != null) {
                    Integer value =Integer.parseInt((String)result[2]) ;
                    vecStatus.add(LetInspectionStatus.getType(value).name());
                } else {
                    vecStatus.add("");
                }
                if (result[7] == null) {
                    vecProcess.add("");
                } else {
                    if (!result[7] .toString().trim().equals("")) {
                        vecProcess.add((String) result[7]);
                    }
                }
                if (result[8] == null) {
                    vecProduction.add("");
                } else {
                    if (!result[8].toString().trim().equals("")) {
                        vecProduction.add((String) result[8]);
                    }
                }
            }
            String paramKey = null;
            if (result[3] != null&& result[6] != null) {
                paramKey =((String)result[6] ).toString() + "-"+ ((String)result[3]) .toString();
            }
            if (result[3] != null&& setParamName.add(paramKey)) {
                mapParamID.put( paramKey,(Integer) result[4]);
            }
            if (result[5] != null) {
                mapValues.put( paramKey,(String) result[5]);
            }
            if (result[6] != null) {
                mapParamType.put(paramKey,(String)result[6]);
            }
        }
        mapAllValues.put(strTestSeq, mapValues);

    }

    protected boolean isInitMessageDisplayed() {
        return initMessageDisplayed;
    }

    protected void setInitMessageDisplayed(boolean initMessageDisplayed) {
        this.initMessageDisplayed = initMessageDisplayed;
    }
}