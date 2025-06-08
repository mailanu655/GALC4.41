package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetLogDao;
import com.honda.galc.dao.product.LetLogHistoryDao;
import com.honda.galc.dao.product.LetModifyHistoryDao;
import com.honda.galc.dao.product.LetProgramResultHistoryDao;
import com.honda.galc.dao.product.LetProgramValueHistoryDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.dao.product.LetResultHistoryDao;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.LetLog;
import com.honda.galc.entity.product.LetLogHistory;
import com.honda.galc.entity.product.LetLogHistoryId;
import com.honda.galc.entity.product.LetModifyHistory;
import com.honda.galc.entity.product.LetModifyHistoryId;
import com.honda.galc.entity.product.LetProgramResultHistory;
import com.honda.galc.entity.product.LetProgramResultHistoryId;
import com.honda.galc.entity.product.LetProgramValueHistory;
import com.honda.galc.entity.product.LetProgramValueHistoryId;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.entity.product.LetResultHistory;
import com.honda.galc.entity.product.LetResultHistoryId;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Sept 02, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetInspectionResultDeletionPanel extends TabbedPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JPanel basePanel = null;
    private JPanel panel = null;
    private JPanel mainPanel = null;
    private JPanel msgPanel = null;
    private JLabel titleLabel = null;
    private JLabel resultsLabel = null;
    private JScrollPane resultScrollPane = null;
    private JTable resultTable = null;
    private JLabel faultLabel = null;
    private JScrollPane faultScrollPane = null;
    private JTable faultTable = null;
    private JButton deleteButton = null;
    private LetProductPanel productPanel = null;
    private boolean initMessageDisplayed;


    public LetInspectionResultDeletionPanel(TabbedMainWindow mainWindow) {
        super("LET Inspection Result Deletion Panel", KeyEvent.VK_D,mainWindow);    
        initComponents();
    }

    @Override
    public void onTabSelected() {
        setErrorMessage(null);
        Logger.getLogger(this.getApplicationId()).info("LET Inspection Result Deletion Panel is selected");
        if (!isInitMessageDisplayed()) {
            String msg = LetInspectionResultManualInputPanel.INIT_MSG;
            JOptionPane.showMessageDialog(this, msg, "Information Message", JOptionPane.INFORMATION_MESSAGE);
            setInitMessageDisplayed(true);
        }           
    }

    private void initComponents() 
    {
        setLayout(new BorderLayout());
        add(getMainPanel(),BorderLayout.CENTER);
        setSize(1024,1024);
        
    }

    private javax.swing.JPanel getMainPanel() {
        if (panel == null) {
            try {
                panel = new javax.swing.JPanel();
                panel.setName("MainPanel");
                panel.setLayout(null);
                panel.setBackground(new java.awt.Color(192,192,192));
                panel.setMinimumSize(new java.awt.Dimension(0, 0));
                panel = new JPanel();
                panel.setLayout(null);
                panel.add(getTitleLabel(), null);
                panel.add(getProductPanelUtilities(), null);
                panel.add(getResultsLabel(), null);
                panel.add(getResultsScrollPane(), null);
                panel.add(getFaultLabel(), null);
                panel.add(getDeleteButton(), null);
                panel.add(getFaultScrollPane(), null);

            } catch (Exception ex) {
                handleException(ex);
            }
        }
        return panel;
    }

    private JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel();
            titleLabel.setBounds(132, 13, 755, 29);
            titleLabel.setText(Message.get("LETInspectionResultDeletion"));
            titleLabel.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return titleLabel;
    }

    private JLabel getResultsLabel() {
        if (resultsLabel == null) {
            resultsLabel = new JLabel();
            resultsLabel.setSize(218, 33);
            resultsLabel.setText(Message.get("InspectionResults"));
            resultsLabel.setLocation(194, 174);
        }
        return resultsLabel;
    }

    private JScrollPane getResultsScrollPane() {
        if (resultScrollPane == null) {
            resultScrollPane = new JScrollPane();
            resultScrollPane.setViewportView(getResultsTable());
            resultScrollPane.setSize(218, 298);
            resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            resultScrollPane.setLocation(194, 205);
        }
        return resultScrollPane;
    }

    private JTable getResultsTable() {
        if (resultTable == null) {
            resultTable = new JTable();
            resultTable.setName("resultTable");
            resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            resultTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (getResultsTable().getModel().getRowCount() == 0) {
                        return;
                    }
                    if (lsm.isSelectionEmpty()) {
                        onUnselectResult();
                    } else {
                        onSelectResult(lsm.getMinSelectionIndex());
                    }
                }
            });
            resultTable.getTableHeader().setReorderingAllowed(false);
            DefaultTableModel model = new DefaultTableModel() {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.addColumn(Message.get("NUM_COUNT"));
            model.addColumn(Message.get("Timestamp"));
            resultTable.setModel(model);
            TableColumnModel columnModel = resultTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(150);
        }
        return resultTable;
    }

    private JLabel getFaultLabel() {
        if (faultLabel == null) {
            faultLabel = new JLabel();
            faultLabel.setBounds(606, 174, 218, 33);
            faultLabel.setText(Message.get("FaultList"));
        }
        return faultLabel;
    }

    private JScrollPane getFaultScrollPane() {
        if (faultScrollPane == null) {
            faultScrollPane = new JScrollPane();
            faultScrollPane.setViewportView(getFaultTable());
            faultScrollPane.setSize(218, 298);
            faultScrollPane.setLocation(606, 205);
            faultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return faultScrollPane;
    }

    private JTable getFaultTable() {
        if (faultTable == null) {
            faultTable = new JTable();
            faultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            faultTable.setEnabled(false);           
            faultTable.getTableHeader().setReorderingAllowed(false);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn(Message.get("Program"));
            faultTable.setModel(model);
            TableColumnModel columnModel = faultTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200);
        }
        return faultTable;
    }

    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new JButton();
            deleteButton.setSize(110, 35);
            deleteButton.setText(Message.get("DELETE"));
            deleteButton.setName("deleteBtn");
            deleteButton.setLocation(248, 525);
            deleteButton.setEnabled(false);
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onDelete();
                }
            });
        }
        return deleteButton;
    }

    private LetProductPanel getProductPanelUtilities() {
        if (productPanel == null) {
            productPanel = new LetProductPanel(this);
            productPanel.addListener(new LetProductPanelListener() {
                public void actionPerformed() {
                    if(!productPanel.getData()) return;
                    onEnterProductId();
                }
            });
            productPanel.setBounds(6, 47, 1008, 119);
        }
        return productPanel;
    }

    private void onEnterProductId() {

        clearResultsTable();
        clearFaultTable();
        disabledButton();
        try {
            List<Object[]> letResultDataList=getDao(LetResultDao.class).getLetResultByProductId(productPanel.getProductId().getText());
            if (letResultDataList.size()==0) {              
                setErrorMessage("Inspection result not found.");
                return;
            }
            Vector vecResults = new Vector();
            for (Object[] letResultData:letResultDataList)
            {
                Vector rowData = new Vector();
                rowData.addElement(letResultData[0]);
                rowData.addElement(letResultData[1]);
                vecResults.add(rowData);
            }           
            DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();     
            if (vecResults != null) {
                if (vecResults.size() != 0) {
                    for (int i = 0; i < vecResults.size(); i++) {
                        model.addRow((Vector) vecResults.get(i));
                    }
                    getResultsTable().setRowSelectionInterval(0, 0);
                    onSelectResult(getResultsTable().getSelectedRow());
                }
            }
        }  catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Deletion Panel");
            e.printStackTrace();        }
    }

    private void onSelectResult(int selectedRow) {
        enabledButton();
        clearFaultTable();
        try {

            DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
            DefaultTableModel fModel = (DefaultTableModel) getFaultTable().getModel();
            fModel.setColumnCount(2);
            fModel.setRowCount(0);
            List<Object[]> faultDataList=getDao(LetResultDao.class).getFaultDataByProductIdTestSeq(productPanel.getProductId().getText(),(Integer)model.getValueAt(selectedRow, 0));
            if (faultDataList.size()==0) {              
                setErrorMessage("Fault data not found.");
                return;
            }
            for (Object[] faultData:faultDataList)
            {
                Vector rowData = new Vector();
                rowData.addElement(faultData[0]);
                rowData.addElement(faultData[1]);
                fModel.addRow(rowData);
            }
            getFaultTable().removeColumn(getFaultTable().getColumnModel().getColumn(1));
            TableColumnModel columnModel = getFaultTable().getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200);
        }  catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Deletion Panel");
            e.printStackTrace();
        } finally {
            TableColumnModel columnModel = getResultsTable().getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(150);
        }
    }

    private void onUnselectResult() {
        clearFaultTable();
        disabledButton();
    }

    private void onDelete() {
        int result =JOptionPane.showConfirmDialog(this,Message.get("ReallyDelete?"),Message.get("DeleteConfirmation"),JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        try {       
            DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
            String productId=productPanel.getProductId().getText();
            Integer testSeqNum=(Integer)model.getValueAt(getResultsTable().getSelectedRow(), 0);
            List<LetResult> letResultdataList=getDao(LetResultDao.class).findLetResultByProductIdSeq(productId,testSeqNum);        
            if(letResultdataList.size()==0)
            {
                this.setErrorMessage("Inspection program already deleted");
                return;
            }
            Integer iHistSeq=getDao(LetModifyHistoryDao.class).getMaxHistorySeq(productPanel.getProductId().getText());        
            
            saveLetModifyHistory(testSeqNum);

            Object[] letResultData=getDao(LetResultDao.class).getLetProgramResultByProductIdSeqNum(productId,testSeqNum);    
            if(letResultData!=null)
                saveLetProgramResultHistory(letResultData,testSeqNum);

            int count; 
            count = getDao(LetResultDao.class).deleteLetProgramResultByProductIdSeqNum(productId,testSeqNum);
            getLogger().info("deleted " + count + " rows from GAL703TBX ");

            Object[] letResultValueData=getDao(LetResultDao.class).getLetProgramResultValueByProductIdSeqNum(productId,testSeqNum);   
            if(letResultValueData!=null)
                saveLetProgramValueHistory(letResultValueData,testSeqNum);

            count = getDao(LetResultDao.class).deleteLetProgramResultValueByProductIdSeqNum(productId,testSeqNum);
            getLogger().info("deleted " + count + " rows from GAL704TBX ");

            LetResult letResult=getDao(LetResultDao.class).findLetResultByProductIdSeqNum(productId,testSeqNum);
            if(letResult!=null)
                saveLetResultHistory(letResult,testSeqNum);

            count = getDao(LetResultDao.class).deleteLetResultByProductIdSeqNum(productId,testSeqNum);
            getLogger().info("deleted " + count + " rows from GAL701TBX ");

            LetLog letLog=getDao(LetLogDao.class).findLetLogByProductIdSeqNum(productId,testSeqNum);
            if(letLog!=null)
                saveLetLogHistory(letLog,testSeqNum);

            count = getDao(LetLogDao.class).deleteLetLogByProductIdSeqNum(productId,testSeqNum);
            getLogger().info("deleted " + count + " rows from GAL702TBX ");
            model.removeRow(getResultsTable().getSelectedRow());
            clearFaultTable();

        } catch (Exception e) {
            Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspect Panel");
            e.printStackTrace();
        } 
    }

    private void saveLetModifyHistory(Integer testSeqNum) 
    {
        LetModifyHistoryId letModifyHistoryId=new LetModifyHistoryId(productPanel.getProductId().getText(),testSeqNum);
        LetModifyHistory letModifyHistory=new LetModifyHistory();
        letModifyHistory.setId(letModifyHistoryId);
        letModifyHistory.setUserId(getMainWindow().getUserId());
        letModifyHistory.setModifyTimestamp(new Timestamp(System.currentTimeMillis()));
        letModifyHistory.setModifyType("D");
        getDao(LetModifyHistoryDao.class).save(letModifyHistory);
        logUserAction(SAVED, letModifyHistory);

    }

    private void saveLetLogHistory(LetLog letLog,Integer testSeqNum) 
    {
        LetLogHistoryId letLogHistoryId=new LetLogHistoryId();
        letLogHistoryId.setProductId(letLog.getId().getProductId());
        letLogHistoryId.setHistorySeq(testSeqNum);
        letLogHistoryId.setTestSeq(letLog.getId().getTestSeq()); 
        letLogHistoryId.setEcuName(letLog.getId().getEcuName());
        LetLogHistory letLogHistory=new LetLogHistory();            
        letLogHistory.setId(letLogHistoryId);
        letLogHistory.setEcuLog(letLog.getEcuLog());
        letLogHistory.setCreateTimestamp(letLog.getCreateTimestamp());
        letLogHistory.setUpdateTimestamp(letLog.getUpdateTimestamp());
        getDao(LetLogHistoryDao.class).save(letLogHistory); 
        logUserAction(SAVED, letLogHistory);
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

    private void clearFaultTable() {
        clearErrorMessageArea();
        getFaultTable().removeAll();
        DefaultTableModel model = (DefaultTableModel) getFaultTable().getModel();
        model.setColumnCount(2);
        model.setRowCount(0);
        getFaultTable().removeColumn(getFaultTable().getColumnModel().getColumn(1));
        TableColumnModel columnModel = getFaultTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
    }

    private void clearResultsTable() {
        clearErrorMessageArea();
        DefaultTableModel model = (DefaultTableModel) getResultsTable().getModel();
        model.setColumnCount(2);
        model.setRowCount(0);
        getResultsTable().removeAll();
        TableColumnModel columnModel = getResultsTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);
    }

    private void clearErrorMessageArea() {
        setErrorMessage("");
    }

    private void enabledButton() {
        getDeleteButton().setEnabled(true);
        getDeleteButton().setForeground(Color.BLACK);
    }

    private void disabledButton() {
        getDeleteButton().setEnabled(false);
        getDeleteButton().setForeground(Color.GRAY);
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