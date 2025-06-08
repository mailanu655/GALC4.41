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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

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

import org.apache.commons.lang.ObjectUtils;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.dao.product.LetProgramResultDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetFaultResultPanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel panel = null;
	private JLabel lblTitle = null;
	private LetProductPanel productPanel = null;
	private JLabel lblFaultList = null;
	private JScrollPane scrollFaultList = null;
	private JTable tblFaultList = null;
	private JLabel lblInspection = null;
	private JScrollPane scrollInspection = null;
	private JTable tblInspection = null;
	private Vector vecFaultRslt;
	private LetPropertyBean letPropertyBean;
	private Vector colorExpList = null;
	private ProgramCategoryColorExpanation programCategoryColorExpanation = null;
	private LetProgramCategoryUtility letProgramCategoryUtility=null;
	private String modelYearCode=null;
	private String modelCode=null;
	private String modelTypeCode=null;
	private String modelOptionCode=null;
	private String productId=null;
	private List<Object[]>  letResultProgramList=null;
	private TreeMap mapCriteriaData=null;
	private Vector vecRslt=null;
	private List lPgmCategories=null;


	public LetFaultResultPanel(TabbedMainWindow mainWindow) {
		super("LET Fault Result Panel", KeyEvent.VK_F,mainWindow);	
		initComponents();
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(this.getApplicationId()).info("LET Fault Result Panel is selected");
	}


	private void initComponents() 
	{
		letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());  
		letProgramCategoryUtility=new LetProgramCategoryUtility(getApplicationId());
		colorExpList=letProgramCategoryUtility.getColorExplanation(letPropertyBean.getLetPgmCategories());
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);

	}


	private javax.swing.JPanel getMainPanel() {
		if (panel == null) {
			try {
				panel = new JPanel();
				panel.setLayout(null);
				panel.add(getProgramCategoryColorExpanation(), null);
				panel.add(getLblTitle(), null);
				panel.add(getProductPanel(), null);
				panel.add(getLblFaultList(), null);
				panel.add(getScrollFaultList(), null);
				panel.add(getLblInspection(), null);
				panel.add(getScrollInspection(), null);
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return panel;
	}

	private ProgramCategoryColorExpanation getProgramCategoryColorExpanation() {
		if (programCategoryColorExpanation == null) {
			programCategoryColorExpanation = new ProgramCategoryColorExpanation(colorExpList);
			programCategoryColorExpanation.setBounds(795, 58, 100, 50);
		}
		return programCategoryColorExpanation;
	}

	private JLabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new JLabel();
			lblTitle.setBounds(132, 13, 755, 29);
			lblTitle.setText(getTitleName());
			lblTitle.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTitle;
	}

	private LetProductPanel getProductPanel() {
		if (productPanel == null) {
			productPanel = new LetProductPanel( this);
			productPanel.addListener(new LetProductPanelListener() {
				public void actionPerformed() {
					if(!productPanel.getData()) return;
					clearProgramTable();
					clearResultTable();
					onEnterProductIdforVin();					
				}
			});
			productPanel.setBounds(6, 47, 808, 80);
		}
		return productPanel;
	}

	private JLabel getLblFaultList() {
		if (lblFaultList == null) {
			lblFaultList = new JLabel();
			lblFaultList.setSize(518, 31);
			lblFaultList.setText(Message.get("FaultList"));
			lblFaultList.setLocation(41, 128);
		}
		return lblFaultList;
	}

	private JScrollPane getScrollFaultList() {
		if (scrollFaultList == null) {
			scrollFaultList = new JScrollPane();
			scrollFaultList.setViewportView(getTblFaultList());
			scrollFaultList.setSize(668, 364);
			scrollFaultList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollFaultList.setLocation(21, 158);
		}
		return scrollFaultList;
	}

	private JLabel getLblInspection() {
		if (lblInspection == null) {
			lblInspection = new JLabel();
			lblInspection.setSize(293, 31);
			lblInspection.setText(Message.get("InspectionResultDetails"));
			lblInspection.setLocation(705, 128);
		}
		return lblInspection;
	}

	private JScrollPane getScrollInspection() {
		if (scrollInspection == null) {
			scrollInspection = new JScrollPane();
			scrollInspection.setViewportView(getTblInspection());
			scrollInspection.setSize(293, 364);
			scrollInspection.setLocation(705, 158);
			scrollInspection.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return scrollInspection;
	}

	private JTable getTblInspection() {
		if (tblInspection == null) {
			tblInspection = new JTable();
			tblInspection.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblInspection.setEnabled(false);

			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("ITEM"));
			model.addColumn(Message.get("VALUE"));
			model.addColumn(Message.get("UNIT"));
			tblInspection.setModel(model);
			TableColumnModel columnModel = tblInspection.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(125);
			columnModel.getColumn(1).setPreferredWidth(75);
			columnModel.getColumn(2).setPreferredWidth(75);
			tblInspection.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
					org.setBackground(Color.WHITE);
					org.setHorizontalAlignment(JLabel.RIGHT);
					return org;
				}
			});
			TableColumn rowHeader = tblInspection.getColumnModel().getColumn(0);
			rowHeader.setCellRenderer(new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
					return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
				}
			});
		}
		return tblInspection;
	}


	private void clearProgramTable() {
		getTblFaultList().removeAll();
		DefaultTableModel model = (DefaultTableModel) getTblFaultList().getModel();
		model.setRowCount(0);
		model.setColumnCount(0);
		model.addColumn(Message.get("PROCESS"));
		model.addColumn(Message.get("Program"));
		model.addColumn(Message.get("NUM_COUNT"));
		model.addColumn(Message.get("Timestamp"));
		model.addColumn(Message.get("BaseReleaseVer."));
		model.addColumn("BgColor");
		TableColumnModel columnModel = tblFaultList.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(100);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(2).setPreferredWidth(50);
		columnModel.getColumn(3).setPreferredWidth(150);
		columnModel.getColumn(4).setPreferredWidth(150);
		columnModel.getColumn(5).setPreferredWidth(150);
	}

	private void clearResultTable() {
		getTblInspection().removeAll();
		DefaultTableModel model = (DefaultTableModel) getTblInspection().getModel();
		model.setRowCount(0);
		model.setColumnCount(0);
		model.addColumn(Message.get("ITEM"));
		model.addColumn(Message.get("VALUE"));
		model.addColumn(Message.get("UNIT"));
		TableColumnModel columnModel = tblInspection.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(125);
		columnModel.getColumn(1).setPreferredWidth(75);
		columnModel.getColumn(2).setPreferredWidth(75);
	}


	private JTable getTblFaultList() {
		if (tblFaultList == null) {
			tblFaultList = new JTable();
			tblFaultList.setName("tblFaultList");
			tblFaultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblFaultList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblFaultList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (tblFaultList.getSelectedRow() != -1) {
						onSelectFaultList();
					}
				}
			});

			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("PROCESS"));
			model.addColumn(Message.get("Program"));
			model.addColumn(Message.get("NUM_COUNT"));
			model.addColumn(Message.get("Timestamp"));
			model.addColumn(Message.get("BaseReleaseVer."));
			model.addColumn("BgColor");
			tblFaultList.setModel(model);
			TableColumnModel columnModel = tblFaultList.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(100);
			columnModel.getColumn(1).setPreferredWidth(200);
			columnModel.getColumn(2).setPreferredWidth(50);
			columnModel.getColumn(3).setPreferredWidth(150);
			columnModel.getColumn(4).setPreferredWidth(150);
			columnModel.getColumn(5).setPreferredWidth(150);
			tblFaultList.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					if (!tableModel.getValueAt(row, 5).equals("")) {
						org.setBackground(Color.decode((String) tableModel.getValueAt(row, 5)));
					} else {
						org.setBackground(Color.white);
					}
					if (column == 1 && tableModel.getValueAt(row, 2).equals("")) {
						Map fontAttr = new HashMap();
						fontAttr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
						org.setFont(new Font(fontAttr));
					}
					return org;
				}
			});

			tblFaultList.removeColumn(tblFaultList.getColumnModel().getColumn(model.getColumnCount() - 1));
		}
		return tblFaultList;
	}


	private void onEnterProductIdforVin() {
		productPanel.getProductId();
		DefaultTableModel dtmFault = (DefaultTableModel) getTblFaultList().getModel();
		DefaultTableModel dtmInspection = (DefaultTableModel) getTblInspection().getModel();
		clearProgramTable();
		clearResultTable();
		try {
			tblFaultList.removeColumn(tblFaultList.getColumnModel().getColumn(dtmFault.getColumnCount() - 1));
			vecFaultRslt =getFaultList();
			if (vecFaultRslt==null||vecFaultRslt.size()==0) {
				Logger.getLogger(this.getApplicationId()).info("Fault Result data not found");
				setErrorMessage("Fault Result data not found");
				return;
			}
			dtmFault.setRowCount(0);			
			if (vecFaultRslt.size() > 0) {
				for (int i = 0; i < vecFaultRslt.size(); i++) {
					Hashtable htData = (Hashtable) vecFaultRslt.get(i);
					dtmFault.addRow(new Object[]{
							htData.get(LETDataTag.PROCESS_STEP),
							htData.get(LETDataTag.INSPECTION_PGM_NAME),
							htData.get(LETDataTag.TEST_SEQ),
							htData.get(LETDataTag.END_TIMESTAMP),
							htData.get(LETDataTag.BASE_RELEASE),
							htData.get(LETDataTag.BG_COLOR)});
				}
				tblFaultList.setRowSelectionInterval(0, 0);
				onSelectFaultList();
			}

		} catch (Exception e) {
			dtmFault.setRowCount(0);
			dtmInspection.setRowCount(0);
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET fault result Panel");
			setErrorMessage("An error Occurred while processing the LET Fault Result screen");
			e.printStackTrace();
		}
	}

	private Vector  getFaultList()
	{
		 vecRslt = new Vector();
		try {
			String[] categories = letPropertyBean.getLetPgmCategories();
			if (categories != null ) {			
				lPgmCategories = Arrays.asList(categories);
			}
			mapCriteriaData = new TreeMap();
			String strProductId = productPanel.getProductId().getText();
			Timestamp maxEndTimestamp=getDao(LetResultDao.class).getMaxEndTimestamp(productPanel.getProductId().getText());
			if(maxEndTimestamp==null)
			{
				return vecRslt ;
			}
			Object[]  mtoData=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaForMto(productPanel.getModelYearCode(), productPanel.getModelCode(), productPanel.getModelTypeCode(), productPanel.getModelOptionCode(), maxEndTimestamp);
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
					mapCriteriaData.put(criteriaProgramName, new Vector(Arrays.asList(passCriteriaProgram)));
				}
			}
			letResultProgramList=getDao(LetProgramResultDao.class).getLetResultProgramDataForProductId(strProductId);					
			parseLetProgramListData();				   
			Iterator iteCriteriaPgmName = mapCriteriaData.keySet().iterator();
			while (iteCriteriaPgmName.hasNext()) {
				Hashtable htResult = new Hashtable();
				Vector vecCriteriaData = (Vector) mapCriteriaData.get(iteCriteriaPgmName.next());
				String deviceType = (String) vecCriteriaData.get(0);
				String pgmAttr = (String) vecCriteriaData.get(1);
				if (lPgmCategories.size()==0 || lPgmCategories.contains(deviceType + pgmAttr)) {
					htResult.put(LETDataTag.PROCESS_STEP, "");
					htResult.put(LETDataTag.TEST_SEQ, "");
					htResult.put(LETDataTag.END_TIMESTAMP, "");
					htResult.put(LETDataTag.INSPECTION_PGM_ID, "");
					htResult.put(LETDataTag.INSPECTION_PGM_NAME, ObjectUtils.defaultIfNull(vecCriteriaData.get(2),""));				
					htResult.put(LETDataTag.BASE_RELEASE, "");
					htResult.put(LETDataTag.BG_COLOR,ObjectUtils.defaultIfNull(vecCriteriaData.get(3),""));					
					vecRslt.add(htResult);
				}
			}					
		}  catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET fault result Panel");
			setErrorMessage("An error Occurred while processing the LET Fault Result screen");
			e.printStackTrace();
		} 
		return vecRslt;
	}

	private void parseLetProgramListData( )
	{		
		for (Object[] letResultProgramData:letResultProgramList) {
			Hashtable htResult = new Hashtable();
			if (letResultProgramData[4] != null && letResultProgramData[5] != null) {
				if (Integer.parseInt(letResultProgramData[5].toString())==LetInspectionStatus.Pass.getId()) {
					mapCriteriaData.remove((String) letResultProgramData[4]);
				} else {
					if (mapCriteriaData.containsKey(letResultProgramData[4])) {
						Vector vecCriteriaData = (Vector) mapCriteriaData.get((String) letResultProgramData[4]);
						String deviceType = (String) vecCriteriaData.get(0);
						String pgmAttr = (String) vecCriteriaData.get(1);
						if (lPgmCategories.size()==0 || lPgmCategories.contains(deviceType + pgmAttr)) {
							htResult.put(LETDataTag.PROCESS_STEP, ObjectUtils.defaultIfNull(letResultProgramData[0],""));						
							htResult.put(LETDataTag.TEST_SEQ, letResultProgramData[1]);
							htResult.put(LETDataTag.END_TIMESTAMP, ObjectUtils.defaultIfNull((Timestamp) letResultProgramData[2],""));							
							htResult.put(LETDataTag.INSPECTION_PGM_ID, ObjectUtils.defaultIfNull(letResultProgramData[3],""));							
							htResult.put(LETDataTag.INSPECTION_PGM_NAME, ObjectUtils.defaultIfNull(letResultProgramData[4],""));						
							htResult.put(LETDataTag.BASE_RELEASE, ObjectUtils.defaultIfNull(letResultProgramData[6],""));
							htResult.put(LETDataTag.BG_COLOR, ObjectUtils.defaultIfNull(vecCriteriaData.get(3),""));							
							vecRslt.add(htResult);
							mapCriteriaData.remove((String) letResultProgramData[4]);
						}
					} else {
						if (lPgmCategories.size()==0 || lPgmCategories.contains("00")) {
							htResult.put(LETDataTag.PROCESS_STEP, ObjectUtils.defaultIfNull(letResultProgramData[0],""));							
							htResult.put(LETDataTag.TEST_SEQ, letResultProgramData[1]);
							htResult.put(LETDataTag.END_TIMESTAMP, ObjectUtils.defaultIfNull((Timestamp) letResultProgramData[2],""));
							htResult.put(LETDataTag.INSPECTION_PGM_ID, ObjectUtils.defaultIfNull(letResultProgramData[3],""));							
							htResult.put(LETDataTag.INSPECTION_PGM_NAME, ObjectUtils.defaultIfNull(letResultProgramData[4],""));							
							htResult.put(LETDataTag.BASE_RELEASE, ObjectUtils.defaultIfNull(letResultProgramData[6],""));						
							htResult.put(LETDataTag.BG_COLOR, "");
							vecRslt.add(htResult);
						}
					}
				}
			}
		}		
	}

	private Vector getInspectionResult(Hashtable htFault)
	{
		Hashtable htRslt;
		Vector vecRslt = new Vector();
		try {
			String strProductId = getProductPanel().getProductId().getText();
			String strTestSeq = String.valueOf(htFault.get(LETDataTag.TEST_SEQ));
			String strInspectionPgmId = String.valueOf(htFault.get(LETDataTag.INSPECTION_PGM_ID));
			if (!strTestSeq.equals("") && !strInspectionPgmId.equals("")) {
				List<Object[]>  letInspectionList=getDao(LetResultDao.class).getLetInspectionResults(strProductId,strTestSeq,strInspectionPgmId);
				for (Object[] letInspection:letInspectionList)  {
					htRslt = new Hashtable();
					htRslt.put(LETDataTag.INSPECTION_PARAM_NAME, null == letInspection[0]? "" : letInspection[0]);
					htRslt.put(LETDataTag.INSPECTION_PARAM_VALUE, null == letInspection[1]? "" : letInspection[1]);
					htRslt.put(LETDataTag.INSPECTION_PARAM_UNIT, null == letInspection[2]? "" : letInspection[2]);
					vecRslt.add(htRslt);
				}
			}

		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET fault result Panel");
			setErrorMessage("An error Occurred while processing the LET Fault Result screen");
			e.printStackTrace();
		} 
		return vecRslt;
	}


	private void onSelectFaultList() {
		DefaultTableModel model = (DefaultTableModel) getTblInspection().getModel();
		Hashtable htFault = (Hashtable) vecFaultRslt.get(tblFaultList.getSelectedRow());
		setErrorMessage("");
		try {
			Vector vecInspectionList = getInspectionResult(htFault);
			model.setRowCount(0);
			if (vecInspectionList.size() > 0) {
				for (int i = 0; i < vecInspectionList.size(); i++) {
					Hashtable htData = (Hashtable) vecInspectionList.get(i);
					model.addRow(new Object[]{
							htData.get(LETDataTag.INSPECTION_PARAM_NAME),
							htData.get(LETDataTag.INSPECTION_PARAM_VALUE),
							htData.get(LETDataTag.INSPECTION_PARAM_UNIT)});
				}
			}
			TableColumnModel columnModel = getTblInspection().getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(125);
			columnModel.getColumn(1).setPreferredWidth(75);
			columnModel.getColumn(2).setPreferredWidth(75);

		} catch (Exception e) {
			model.setRowCount(0);
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET fault result Panel");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result screen");
		} 
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	private String getTitleName() {
		return this.getScreenName();
	}




}