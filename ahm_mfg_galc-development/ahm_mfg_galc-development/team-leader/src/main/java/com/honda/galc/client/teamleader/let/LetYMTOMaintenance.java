package com.honda.galc.client.teamleader.let;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.dao.product.LetPassCriteriaDao;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.product.LetPassCriteria;
import com.honda.galc.entity.product.LetPassCriteriaId;
import com.honda.galc.entity.product.LetPassCriteriaMto;
import com.honda.galc.entity.product.LetPassCriteriaMtoId;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date nov 26, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetYMTOMaintenance extends JDialog
{
	private static final long serialVersionUID = 1L;
	private int itMode = 0;
	private Vector pgmNameDatas = null;
	private Hashtable pgmTypeDatas = null;
	private Hashtable pgmAttrDatas = null;
	private Hashtable pgmColorDatas = null;
	private Vector colorExpList = null;
	private Vector vecChkPgmCategory=null;
	private Vector vecChkPgm=null;
	private ProgramCategoryColorExpanation programCategoryColorExpanation = null;
	private JPanel pnlBase = null;
	private JPanel pnlYMTO = null;
	private StatusMessagePanel errorMessageArea = null;
	private JLabel lblYear = null;
	private JLabel lblModel = null;
	private JLabel lblType = null;
	private JLabel lblOption = null;
	private JTextField txtYear = null;
	private JTextField txtModel = null;
	private JTextField txtType = null;
	private JTextField txtOption = null;
	private JLabel lblEffective = null;
	private JTextField txtEffDate = null;
	private JTextField txtEffTime = null;
	private JLabel lblExpiration = null;
	private JTextField txtExpDate = null;
	private JTextField txtExpTime = null;
	private JLabel lblCriteria = null;
	private JScrollPane spCriteria = null;
	private JTable tblCriteria = null;
	private JButton btnSave = null;
	private JButton btnCancel = null;
	private Vector vecPgmData;
	private LetShippingJudgementSettingPanel parentPanel;
	private JLabel lblEffExplainDate = null;
	private JLabel lblEffExplainTime = null;
	private JLabel lblExpExplainDate = null;
	private JLabel lblExpExplainTime = null;
	private JLabel lblEffExplain = null;
	private JLabel lblExpExplain = null;
	private Hashtable cloneHtData = null;
	private String gstrModelYearCode;
	private String gstrModelCode;
	private String gstrModelTypeCode;
	private String gstrModelOptionCode;
	private String gstrEffectiveTime;
	private LetUtil letUtil=null;
	private String strEff = "";
	private String strExp = "";
	private int itRslt01 = 0;
	private int itRslt02 = 0;
	private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private LetPropertyBean letPropertyBean;

	public LetYMTOMaintenance() {
		super();
		initialize(null);
	}

	public LetYMTOMaintenance(LetShippingJudgementSettingPanel jf) {
		super();
		parentPanel = (LetShippingJudgementSettingPanel) jf;
		initialize(null);
	}

	public LetYMTOMaintenance(LetShippingJudgementSettingPanel jf, Vector colorExpList) {
		super();
		parentPanel = (LetShippingJudgementSettingPanel) jf;
		this.colorExpList = colorExpList;
		initialize(null);
	}

	public LetYMTOMaintenance(LetShippingJudgementSettingPanel jf,  Hashtable htMto, Vector colorExpList) {
		super();
		parentPanel = (LetShippingJudgementSettingPanel) jf;
		this.colorExpList = colorExpList;
		initialize(htMto);
	}

	public void setInsertMode() {
		itMode = 0;
		setTitle(Message.get("LETPassCriteriaAdd"));
	}

	public void setCopyMode(Vector vecData){
		itMode=10;
		setTitle(Message.get("LETPassCriteriaCopy"));
		if (vecPgmData == null) {
			return;
		}
		for (int i = 0; i < vecPgmData.size(); i++) {
			Hashtable htAllData = (Hashtable) vecPgmData.get(i);
			for (int j = 0; j < vecData.size(); j++) {
				Hashtable htSelData = (Hashtable) vecData.get(j);
				String strAllData = String.valueOf(htAllData.get(LETDataTag.CRITERIA_PGM_ID));
				String strSelData = String.valueOf(htSelData.get(LETDataTag.CRITERIA_PGM_ID));
				if (strAllData.equals(strSelData)) {
					tblCriteria.setValueAt(Boolean.TRUE, i, 0);
				}
			}
		}
	}
	public void setUpdateMode(Hashtable htData, Vector vecData) 
	{
		setTitle(Message.get("LETPassCriteriaUpdate"));
		cloneHtData = (Hashtable) htData.clone();
		Calendar cal = Calendar.getInstance();
		String strEff = (String) htData.get(LETDataTag.EFFECTIVE_TIME);
		strEff = strEff.substring(0, 4)
		+ strEff.substring(5, 7)
		+ strEff.substring(8, 10)
		+ strEff.substring(11, 13)
		+ strEff.substring(14, 16)
		+ strEff.substring(17, 19);
		if (Long.parseLong(strEff) > Long.parseLong(sdf1.format(cal.getTime()))) {
			itMode = 2;
			getTxtYear().setEnabled(true);
			getTxtModel().setEnabled(true);
			getTxtType().setEnabled(true);
			getTxtOption().setEnabled(true);
			getTxtEffDate().setEnabled(true);
			getTxtEffTime().setEnabled(true);
		} else {
			itMode = 1;
			getTxtYear().setEnabled(false);
			getTxtModel().setEnabled(false);
			getTxtType().setEnabled(false);
			getTxtOption().setEnabled(false);
			getTxtEffDate().setEnabled(false);
			getTxtEffTime().setEnabled(false);
		}
		getTxtYear().setText((String) htData.get(LETDataTag.MODEL_YEAR_CODE));
		this.gstrModelYearCode = (String) htData.get(LETDataTag.MODEL_YEAR_CODE);
		getTxtModel().setText((String) htData.get(LETDataTag.MODEL_CODE));
		this.gstrModelCode = (String) htData.get(LETDataTag.MODEL_CODE);
		getTxtType().setText((String) htData.get(LETDataTag.MODEL_TYPE_CODE));
		this.gstrModelTypeCode = (String) htData.get(LETDataTag.MODEL_TYPE_CODE);
		getTxtOption().setText((String) htData.get(LETDataTag.MODEL_OPTION_CODE));
		this.gstrModelOptionCode = (String) htData.get(LETDataTag.MODEL_OPTION_CODE);
		String strEffective = (String) htData.get(LETDataTag.EFFECTIVE_TIME);
		this.gstrEffectiveTime = (String) htData.get(LETDataTag.EFFECTIVE_TIME);
		getTxtEffDate().setText(strEffective.substring(0, 4)+ strEffective.substring(5, 7)+ strEffective.substring(8, 10));
		getTxtEffTime().setText(strEffective.substring(11, 13) + strEffective.substring(14, 16)+ strEffective.substring(17, 19));
		String strExpiration = (String) htData.get(LETDataTag.EXPIRATION_TIME);
		if (!"".equals(strExpiration)) {
			getTxtExpDate().setText(strExpiration.substring(0, 4)+ strExpiration.substring(5, 7) + strExpiration.substring(8, 10));
			getTxtExpTime().setText(strExpiration.substring(11, 13)+ strExpiration.substring(14, 16)+ strExpiration.substring(17, 19));
		}
		if (vecPgmData == null) {
			return;
		}
		for (int i = 0; i < vecPgmData.size(); i++) {
			Hashtable htAllData = (Hashtable) vecPgmData.get(i);
			for (int j = 0; j < vecData.size(); j++) {
				Hashtable htSelData = (Hashtable) vecData.get(j);
				String strAllData = String.valueOf(htAllData.get(LETDataTag.CRITERIA_PGM_ID));
				String strSelData = String.valueOf(htSelData.get(LETDataTag.CRITERIA_PGM_ID));
				if (strAllData.equals(strSelData)) {
					tblCriteria.setValueAt(Boolean.TRUE, i, 0);
				}
			}
		}

	}

	private void initialize(Hashtable htMto) {
		try {
			this.setContentPane(getPnlBase());
			setName("LetYMTOMaintenance");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(898, 628);
			setTitle(Message.get("LETPassCriteriaAdd"));
			onInit(htMto);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter(){
				 public void windowClosing(WindowEvent e) {
		                onCancelCriteria();
		         }
			});
		} catch (java.lang.Exception e) {
			logException(e);
		}
	}

	private JPanel getPnlBase() {
		if (pnlBase == null) {
			pnlBase = new JPanel();
			pnlBase.setLayout(new BorderLayout());
			pnlBase.add(getPnlYMTO(), BorderLayout.CENTER);
			pnlBase.add(getErrorMessageArea(), BorderLayout.SOUTH);
		}
		return pnlBase;
	}

	private JPanel getPnlYMTO() 
	{
		if (pnlYMTO == null) {
			pnlYMTO = new JPanel();
			pnlYMTO.setName("JDialogYMTOPanel");
			pnlYMTO.setLayout(null);
			pnlYMTO.add(getLblYear(), null);
			pnlYMTO.add(getLblModel(), null);
			pnlYMTO.add(getLblType(), null);
			pnlYMTO.add(getLblOption(), null);
			pnlYMTO.add(getTxtYear(), null);
			pnlYMTO.add(getTxtModel(), null);
			pnlYMTO.add(getTxtType(), null);
			pnlYMTO.add(getTxtOption(), null);
			pnlYMTO.add(getLblEffective(), null);
			pnlYMTO.add(getTxtEffDate(), null);
			pnlYMTO.add(getTxtEffTime(), null);
			pnlYMTO.add(getLblExpiration(), null);
			pnlYMTO.add(getTxtExpDate(), null);
			pnlYMTO.add(getTxtExpTime(), null);
			pnlYMTO.add(getLblCriteria(), null);
			pnlYMTO.add(getSpCriteria(), null);
			pnlYMTO.add(getBtnSave(), null);
			pnlYMTO.add(getBtnCancel(), null);
			pnlYMTO.add(getLblEffExplainDate(), null);
			pnlYMTO.add(getLblEffExplainTime(), null);
			pnlYMTO.add(getLblExpExplainDate(), null);
			pnlYMTO.add(getLblExpExplainTime(), null);
			pnlYMTO.add(getLblEffExplain(), null);
			pnlYMTO.add(getLblExpExplain(), null);
			pnlYMTO.add(getProgramCategoryColorExpanation(), null);

		}
		return pnlYMTO;
	}

	private StatusMessagePanel getErrorMessageArea() {
		if (errorMessageArea == null) {
			errorMessageArea = new StatusMessagePanel();
			errorMessageArea.setFont(new Font("Dialog", java.awt.Font.BOLD, 16));
		}
		return errorMessageArea;
	}

	private JLabel getLblYear() {
		if (lblYear == null) {
			lblYear = new JLabel();
			lblYear.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 11));
			lblYear.setBounds(40, 40, 60, 30);
			lblYear.setText(Message.get("YEAR_CODE"));
		}
		return lblYear;
	}

	private JLabel getLblModel() {
		if (lblModel == null) {
			lblModel = new JLabel();
			lblModel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 11));
			lblModel.setBounds(40, 80, 60, 30);
			lblModel.setText(Message.get("MODEL"));
		}
		return lblModel;
	}

	private JLabel getLblType() {
		if (lblType == null) {
			lblType = new JLabel();
			lblType.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 11));
			lblType.setBounds(40, 120, 60, 30);
			lblType.setText(Message.get("TYPE"));
		}
		return lblType;
	}

	private JLabel getLblOption() {
		if (lblOption == null) {
			lblOption = new JLabel();
			lblOption.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 11));
			lblOption.setBounds(40, 160, 60, 36);
			lblOption.setText(Message.get("OPTION"));
		}
		return lblOption;
	}

	private JTextField getTxtYear() {
		if (txtYear == null) {
			txtYear = new JTextField();
			txtYear.setDocument(new AsciiDocument(1));
			txtYear.setBounds(100, 50, 40, 20);
		}
		return txtYear;
	}

	private JTextField getTxtModel() {
		if (txtModel == null) {
			txtModel = new JTextField();
			txtModel.setDocument(new AsciiDocument(3));
			txtModel.setBounds(100, 90, 40, 20);
		}
		return txtModel;
	}

	private JTextField getTxtType() {
		if (txtType == null) {
			txtType = new JTextField();
			txtType.setDocument(new AsciiDocument(3));
			txtType.setBounds(100, 130, 40, 20);
		}
		return txtType;
	}

	private JTextField getTxtOption() {
		if (txtOption == null) {
			txtOption = new JTextField();
			txtOption.setDocument(new AsciiDocument(3));
			txtOption.setBounds(100, 170, 40, 20);
		}
		return txtOption;
	}

	private JLabel getLblEffective() {
		if (lblEffective == null) {
			lblEffective = new JLabel();
			lblEffective.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 11));
			lblEffective.setBounds(40, 215, 110, 30);
			lblEffective.setText(Message.get("EFFECTIVE_DATE"));
		}
		return lblEffective;
	}

	private JTextField getTxtEffDate() {
		if (txtEffDate == null) {
			txtEffDate = new JTextField();
			txtEffDate.setDocument(new AsciiDocument(8));
			txtEffDate.setBounds(150, 225, 70, 20);
		}
		return txtEffDate;
	}

	private JLabel getLblEffExplainDate() {
		if (lblEffExplainDate == null) {
			lblEffExplainDate = new JLabel();
			lblEffExplainDate.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblEffExplainDate.setBounds(150, 210, 70, 15);
			lblEffExplainDate.setText("YYYYMMDD");
		}
		return lblEffExplainDate;
	}

	private JLabel getLblEffExplainTime() {
		if (lblEffExplainTime == null) {
			lblEffExplainTime = new JLabel();
			lblEffExplainTime.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblEffExplainTime.setBounds(222, 210, 70, 15);
			lblEffExplainTime.setText("HHMMSS");
		}
		return lblEffExplainTime;
	}

	private JLabel getLblExpExplainDate() {
		if (lblExpExplainDate == null) {
			lblExpExplainDate = new JLabel();
			lblExpExplainDate.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblExpExplainDate.setBounds(150, 315, 70, 15);
			lblExpExplainDate.setText("YYYYMMDD");
		}
		return lblExpExplainDate;
	}

	private JLabel getLblExpExplainTime() {
		if (lblExpExplainTime == null) {
			lblExpExplainTime = new JLabel();
			lblExpExplainTime.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblExpExplainTime.setBounds(222, 315, 70, 15);
			lblExpExplainTime.setText("HHMMSS");
		}
		return lblExpExplainTime;
	}


	private JLabel getLblEffExplain() {
		if (lblEffExplain == null) {
			lblEffExplain = new JLabel();
			lblEffExplain.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblEffExplain.setBounds(40, 248, 250, 40);
			lblEffExplain.setText(Message.get("ExplainEff"));
		}
		return lblEffExplain;
	}

	private JLabel getLblExpExplain() {
		if (lblExpExplain == null) {
			lblExpExplain = new JLabel();
			lblExpExplain.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblExpExplain.setBounds(40, 355, 250, 40);
			lblExpExplain.setText(Message.get("ExplainExp"));
		}
		return lblExpExplain;
	}

	private JTextField getTxtEffTime() {
		if (txtEffTime == null) {
			txtEffTime = new JTextField();
			txtEffTime.setDocument(new AsciiDocument(6));
			txtEffTime.setBounds(220, 225, 70, 20);
		}
		return txtEffTime;
	}

	private JLabel getLblExpiration() {
		if (lblExpiration == null) {
			lblExpiration = new JLabel();
			lblExpiration.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 11));
			lblExpiration.setBounds(40, 320, 110, 30);
			lblExpiration.setText(Message.get("EXPIRATION_DATE"));
		}
		return lblExpiration;
	}

	private JTextField getTxtExpDate() {
		if (txtExpDate == null) {
			txtExpDate = new JTextField();
			txtExpDate.setDocument(new AsciiDocument(8));
			txtExpDate.setBounds(150, 330, 70, 20);
		}
		return txtExpDate;
	}

	private JTextField getTxtExpTime() {
		if (txtExpTime == null) {
			txtExpTime = new JTextField();
			txtExpTime.setDocument(new AsciiDocument(6));
			txtExpTime.setBounds(220, 330, 70, 20);
		}
		return txtExpTime;
	}

	private JLabel getLblCriteria() {
		if (lblCriteria == null) {
			lblCriteria = new JLabel();
			lblCriteria.setBounds(305, 6, 321, 33);
			lblCriteria.setText(Message.get("PassCriteria"));
		}
		return lblCriteria;
	}

	private JScrollPane getSpCriteria() {
		if (spCriteria == null) {
			spCriteria = new JScrollPane();
			spCriteria.setViewportView(getTblCriteria());
			spCriteria.setSize(433, 345);
			spCriteria.setLocation(304, 39);
		}
		return spCriteria;
	}

	private JTable getTblCriteria() {
		if (tblCriteria == null) {
			tblCriteria = new JTable();
			tblCriteria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblCriteria.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			DefaultTableModel model = new DefaultTableModel() {
				public Class getColumnClass(int columnIndex) {
					if (columnIndex == 0) {
						return Boolean.class;
					}
					return super.getColumnClass(columnIndex);
				}
				public boolean isCellEditable(int row, int column) {
					if (column == 0) {
						return true;
					}
					if (column == 2) {
						JComboBox inspectionCombo = new JComboBox(pgmNameDatas);
						inspectionCombo.setBackground(Color.white);
						TableColumn sportColumn = tblCriteria.getColumnModel().getColumn(column);
						sportColumn.setCellEditor(new DefaultCellEditor(inspectionCombo));
						return true;
					}
					return false;
				}
			};
			model.addColumn(Message.get("Mandatory"));
			model.addColumn(Message.get("Program"));
			model.addColumn(Message.get("PGM_CATEGORY"));
			tblCriteria.setModel(model);
			TableColumnModel columnModel = tblCriteria.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(93);
			columnModel.getColumn(1).setPreferredWidth(200);
			columnModel.getColumn(2).setPreferredWidth(120);
			tblCriteria.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected,boolean hasFocus, int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
					if (column == 2&& table.getValueAt(row, 2) != null&& !("".equals(table.getValueAt(row, 2)))&& pgmColorDatas.size() > 0) {
						org.setBackground(Color.decode((String) pgmColorDatas.get((String) table.getValueAt(row, 2))));
					} else {
						org.setBackground(Color.WHITE);
					}
					return org;
				}
			});
		}
		return tblCriteria;
	}

	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton();
			btnSave.setBounds(356, 399, 120, 35);
			btnSave.setText(Message.get("SAVE"));
			btnSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onSaveCriteria();
				}
			});
		}
		return btnSave;
	}

	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setBounds(551, 399, 120, 35);
			btnCancel.setText(Message.get("CANCEL"));
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onCancelCriteria();
				}
			});
		}
		return btnCancel;
	}


	private void onInit(Hashtable htMto) 
	{
		letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, this.parentPanel.getApplicationId()); 
		letUtil=new LetUtil();
		try {
			if (htMto != null) {
				String strEffective = (String) htMto.get(LETDataTag.EFFECTIVE_TIME);
				letUtil.getProgram((String) htMto.get(LETDataTag.MODEL_YEAR_CODE), (String) htMto.get(LETDataTag.MODEL_CODE), (String) htMto.get(LETDataTag.MODEL_TYPE_CODE), (String) htMto.get(LETDataTag.MODEL_OPTION_CODE), strEffective.substring(0, 4)+ strEffective.substring(5, 7)+ strEffective.substring(8, 10), strEffective.substring(11, 13)+ strEffective.substring(14, 16)+ strEffective.substring(17, 19));
			} else {
				letUtil.getProgram("", "", "", "", "99991231", "235959");
			}
			htMto = null;
			vecPgmData = letUtil.getPgmData(); 
			pgmNameDatas = letUtil.getVecPgmComboDatas();
			pgmTypeDatas = letUtil.getPgmComboTypeData();
			pgmAttrDatas = letUtil.getPgmComboAttrData();
			pgmColorDatas = letUtil.getPgmComboColorData();
			DefaultTableModel criteriaModel = (DefaultTableModel) getTblCriteria().getModel();
			criteriaModel.setRowCount(0);
			for (int i = 0; i < vecPgmData.size(); i++) {
				Hashtable htPgm = (Hashtable) vecPgmData.get(i);
				criteriaModel.addRow( new Object[]{null, htPgm.get(LETDataTag.CRITERIA_PGM_NAME),htPgm.get(LETDataTag.PGM_CATEGORY)});
			}
		} catch (Exception e) {
			logException(e);
			}
	}

	private void clear() {
		strEff = "";
		strExp = "";
		itRslt01 = 0;
		itRslt02 = 0;
		errorMessageArea.setErrorMessageArea("");
		parentPanel.getMainWindow().setErrorMessage("");	
	}
 
	private void onSaveCriteria() 
	{
		clear();
			
		try {
			if(!validateMto())
				return;
			if(!validateEffExpDates())
				return;
			int itWeight = 0;
			if (!"*".equals(txtYear.getText().trim())) {
				itWeight += 4;
			}
			if (!"*".equals(txtModel.getText().trim())) {
				itWeight += 8;
			}
			if (!"*".equals(txtType.getText().trim())) {
				itWeight += 2;
			}
			if (!"*".equals(txtOption.getText().trim())) {
				itWeight += 1;
			}
			vecChkPgm = new Vector();
			vecChkPgmCategory = new Vector();
			for (int i = 0; i < tblCriteria.getRowCount(); i++) {
				if (tblCriteria.getValueAt(i, 0) == Boolean.TRUE) {
					if (tblCriteria.getValueAt(i, 2) == null || "".equals(tblCriteria.getValueAt(i, 2))) {
						logMessage("Program category is not selected.");
						return;
					}
					vecChkPgmCategory.add(tblCriteria.getValueAt(i, 2));
					vecChkPgm.add(vecPgmData.get(i));
				}
			}
			parentPanel.mode=false;		
			if(!executeAddUpdateDeleteMtoProcess(itWeight))
				return ;
			HashMap mapMsg = new HashMap();
			mapMsg.put("Msg01", String.valueOf(itRslt01));
			mapMsg.put("Msg02", String.valueOf(itRslt02));
			if (!strEff.equals("")) {
				mapMsg.put("EffDate", sdf2.format(letUtil.createCalendar(strEff).getTime()));
			} else {
				mapMsg.put("EffDate", "");
			}
			if (!strExp.equals("")) {
				mapMsg.put("ExpDate", sdf2.format(letUtil.createCalendar(strExp).getTime()));
			} else {
				mapMsg.put("ExpDate", "");
			}
			String[] strDialogMsg = null;
			if (((String) mapMsg.get("Msg01")).equals("1")) {
				strDialogMsg =  Message.getStrArrayMultipleLineMessage("CRITERIA_MSG8",null);             
				int itDialog = JOptionPane.showConfirmDialog(this,strDialogMsg,Message.get("SaveConfirmation"),JOptionPane.YES_NO_OPTION);
				if (itDialog != JOptionPane.YES_OPTION) {
					return;
				}
			}
			String strRslt = (String) mapMsg.get("Msg02");
			strDialogMsg=mapMessage(strRslt,mapMsg);			
			int itDialog = JOptionPane.showConfirmDialog(this,strDialogMsg, Message.get("SaveConfirmation"),JOptionPane.YES_NO_OPTION);
			if (itDialog != JOptionPane.YES_OPTION) {
				return;
			}
			parentPanel.mode=true;			
			if(!executeAddUpdateDeleteMtoProcess(itWeight))
				return ;
			this.dispose();
		} catch (Exception e) {
			logException(e);
			} 
	}

	private boolean executeAddUpdateDeleteMtoProcess(Integer itWeight) {
		if (itMode == 0 || itMode == 10) {
			if(!addMto(StringUtils.trim(txtYear.getText()),StringUtils.trim(txtModel.getText()),StringUtils.trim(txtType.getText()), StringUtils.trim(txtOption.getText()),StringUtils.trim(txtEffDate.getText()),StringUtils.trim(txtEffTime.getText()),StringUtils.trim(txtExpDate.getText()),StringUtils.trim(txtExpTime.getText()),false,itWeight))
				 return false;
		} else if (itMode == 1) {                                             
			if(!updateMtoApply(StringUtils.trim(txtYear.getText()),StringUtils.trim(txtModel.getText()),StringUtils.trim(txtType.getText()), StringUtils.trim(txtOption.getText()),StringUtils.trim(txtEffDate.getText()),StringUtils.trim(txtEffTime.getText()),StringUtils.trim(txtExpDate.getText()),StringUtils.trim(txtExpTime.getText()),false,itWeight))
				return false;
		} else if (itMode == 2) {
			if(!updateMto(StringUtils.trim(txtYear.getText()),StringUtils.trim(txtModel.getText()),StringUtils.trim(txtType.getText()), StringUtils.trim(txtOption.getText()),StringUtils.trim(txtEffDate.getText()),StringUtils.trim(txtEffTime.getText()),StringUtils.trim(txtExpDate.getText()),StringUtils.trim(txtExpTime.getText()),itWeight))
				return false;			
		}
		return true;
		
	}

	private boolean validateEffExpDates()
	{
		if ("".equals(txtEffDate.getText()) && !"".equals(txtEffTime.getText())) {
			logMessage("Effective date invalid.");
			return false;
		} else if (!"".equals(txtEffDate.getText()) && "".equals(txtEffTime.getText())) {
			if (!isValidDate(txtEffDate.getText())) {
				logMessage("Effective date invalid.");
				return false;
			}
		} else if ("".equals(txtEffDate.getText()) && "".equals(txtEffTime.getText())) {
		} else if (!"".equals(txtEffDate.getText()) && !"".equals(txtEffTime.getText())) {
			if (!isValidDate(txtEffDate.getText() + txtEffTime.getText())) {
				logMessage("Effective time invalid.");
				return false;
			}
		}
		if ("".equals(txtExpDate.getText()) && !"".equals(txtExpTime.getText())) {
			logMessage("Expiration date invalid.");
			return false;
		} else if ("".equals(txtExpDate.getText()) && "".equals(txtExpTime.getText())) {
		} else if (!"".equals(txtExpDate.getText()) && "".equals(txtExpTime.getText())) {
			if (!isValidDate(txtExpDate.getText())) {
				logMessage("Expiration date invalid.");
				return false;
			}
		} else if (!"".equals(txtExpDate.getText()) && !"".equals(txtExpTime.getText())) {
			if (!isValidDate(txtExpDate.getText() + txtExpTime.getText())) {
				logMessage("Expiration time invalid.");
				return false;
			}
		}
		return true;
	}

	private boolean validateMto() 
	{
		if ("".equals(txtYear.getText())) {
			logMessage("Enter year code.");
			return false;
		}
		if ("".equals(txtModel.getText())) {
			logMessage("Enter model code.");
			return false;
		}
		if ("".equals(txtType.getText())) {
			logMessage("Enter type code.");
			return false;
		}
		if ("".equals(txtOption.getText())) {
			logMessage("Enter option code.");
			return false;
		}
		return true;
	}

	private String[] mapMessage(String strRslt, HashMap mapMsg) 
	{
		String[] strDialogMsg = null;
		if (strRslt.equals("0")) {
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG0",null);
		} else if (strRslt.equals("1")) {
			String[] paramStringArray = new String[]{(String)mapMsg.get("EffDate")};
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG1",paramStringArray);
		} else if (strRslt.equals("2")) {
			String[] paramStringArray = new String[]{(String)mapMsg.get("ExpDate")};
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG2",paramStringArray);
		} else if (strRslt.equals("3")) {
			String[] paramStringArray = new String[]{(String)mapMsg.get("EffDate"),(String)mapMsg.get("ExpDate")};
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG3",paramStringArray);
		} else if (strRslt.equals("4")) {
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG4",null);
		} else if (strRslt.equals("5")) {
			String[] paramStringArray = new String[]{(String)mapMsg.get("EffDate"),(String)mapMsg.get("EffDate")};
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG5",paramStringArray);
		} else if (strRslt.equals("6")) {
			String[] paramStringArray = new String[]{(String)mapMsg.get("ExpDate")};
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG6",paramStringArray);
		} else if (strRslt.equals("7")) {
			String[] paramStringArray = new String[]{(String)mapMsg.get("EffDate"),(String)mapMsg.get("ExpDate"),(String)mapMsg.get("ExpDate")};
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG7",paramStringArray);
		}
		return strDialogMsg;
	}

	private void onCancelCriteria() {
		// SR08552 show confirm dialog if the pop up is for copying criteria 
		if (itMode == 10) {
			int confirm = JOptionPane.showConfirmDialog(this,
					Message.get("LETPassCriteriaCopyCancelConfirm"),
					Message.get("LETPassCriteriaCopyCancelTitle"), JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.NO_OPTION) {
				return;
			}
		}
		this.dispose();
	}

	static public boolean isValidDate(String date) 
	{
		String strFormat = new String();
		if (date == null) {
			return false;
		}
		if (date.length() == 8) {
			strFormat = "yyyyMMdd";
		} else if (date.length() == 14) {
			strFormat = "yyyyMMddHHmmss";
		} else {
			return false;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
		formatter.setLenient(false);
		ParsePosition pos = new ParsePosition(0);
		Date rtn = formatter.parse(date, pos);
		if (rtn == null) {
			return false;
		}
		return true;
	}

	private ProgramCategoryColorExpanation getProgramCategoryColorExpanation() 
	{
		if (programCategoryColorExpanation == null) {
			programCategoryColorExpanation = new ProgramCategoryColorExpanation(colorExpList);
			programCategoryColorExpanation.setBounds(751, 25, 100, 48);
		}
		return programCategoryColorExpanation;
	}

	private boolean addMto(String modelYear,String modelCode,String modelType,String modelOption,String strEffDate,String strEffTime,String strExpDate,String strExpTime,boolean updateMto, int weight)
	{
		try {
			if(!validateExpEffDateForMtoAddition( strEffDate, strEffTime, strExpDate, strExpTime)) 
				return false ;
			String effecDateStr=sdf2.format(sdf1.parse(strEff, new ParsePosition(0))) + ".000000";
			Timestamp effecDate=new Timestamp(sdf2.parse(effecDateStr).getTime());
			List<LetPassCriteriaMto> letPassCriteriaPgmList=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaGreaterEqualEffecTime(modelYear, modelCode, modelType, modelOption, effecDate);
			for (LetPassCriteriaMto letPassCriteriaPgm:letPassCriteriaPgmList) {
				if (updateMto&&(parentPanel.mode == false&&cloneHtData.get(LETDataTag.EFFECTIVE_TIME).equals(letPassCriteriaPgm.getId().getEffectiveTime()))){
					continue;
				}
				Timestamp getEff = letPassCriteriaPgm.getId().getEffectiveTime();
				Timestamp getExp = letPassCriteriaPgm.getEndTimestamp();
				if(itMode==10){
					//For copy criteria only
					//The new screen shall have business logic to ensure that no rules are copied that overlap existing rules for the given period. 
					//If the new rules overlap with old rules, the screen shall not allow the user to add the new rules, and prompt the user to modify the old rules instead.
					Timestamp expireDate = letUtil.createTimestamp(strExp, 999999000);
					
					if ((getEff.compareTo(effecDate) > 0 && getEff.compareTo(expireDate) < 0) 
							|| (getExp.compareTo(effecDate) >0 && getExp.compareTo(expireDate) < 0)
							|| (effecDate.compareTo(getEff) > 0 && effecDate.compareTo(getExp) < 0)
							|| (expireDate.compareTo(getEff) > 0 && expireDate.compareTo(getExp) < 0)) {
						//Time overlap detected
						JOptionPane.showMessageDialog(this, Message.getStrArrayMultipleLineMessage("CRITERIA_MSG9", sdf2.format(effecDate),  sdf2.format(expireDate))
								,Message.get("ERROR_MESSAGE"), JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
				else{
					if (getEff.compareTo(letUtil.createTimestamp(strEff, 000000000)) < 0 && getExp.compareTo(letUtil.createTimestamp(strExp, 999999000)) > 0) {
						logMessage("The enforcement period of the standard which it is going to register is contained during the enforcement of the already registered standard.");
						return false;
					} else if (getEff.compareTo(letUtil.createTimestamp(strEff, 000000000)) < 0&&getExp.compareTo(letUtil.createTimestamp(strEff, 000000000)) > 0) {
						Calendar calExp = letUtil.createCalendar(strEff);
						calExp.add(Calendar.SECOND, -1);
						Date date = new Date();
						updateExpiration(sdf2.format(calExp.getTime()) + ".999999",modelYear, modelCode, modelType, modelOption,getEff.toString(),getExp.toString());
						itRslt01 = 1;
					} else if (getEff.compareTo(letUtil.createTimestamp(strExp, 999999000)) < 0&&getExp.compareTo(letUtil.createTimestamp(strExp, 999999000)) > 0) {
						itRslt02 += 3;
						Calendar calEff = letUtil.createCalendar(strExp);
						calEff.add(Calendar.SECOND, +1);
						updateEffect(sdf2.format(calEff.getTime()) + ".000000",modelYear, modelCode, modelType, modelOption,getEff.toString(),getExp.toString());
						itRslt01 = 1;
					} else if (getEff.compareTo(letUtil.createTimestamp(strEff, 000000000)) >= 0&&getExp.compareTo(letUtil.createTimestamp(strExp, 999999000)) <= 0) {
						deletePassCriteria(modelYear, modelCode, modelType, modelOption,getEff.toString());
						itRslt01 = 1;
					}
				}
			}
			addPassCriteria( modelYear, modelCode, modelType, modelOption,sdf2.format(letUtil.createCalendar(strEff).getTime()) + ".000000",sdf2.format(letUtil.createCalendar(strExp).getTime()) + ".999999",weight);
		    return true;
		} catch (Exception e) {
			logException(e);
			return false;
		}
	}



	private boolean validateExpEffDateForMtoAddition(String strEffDate,String strEffTime,String strExpDate,String strExpTime) 
	{
		Calendar cal = Calendar.getInstance();
		if ("".equals(strEffDate) && "".equals(strEffTime)) {
			strEff = sdf1.format(cal.getTime());
			itRslt02 = 0;
		} else if (!"".equals(strEffDate) && "".equals(strEffTime)) {
			try {
				try {
					strEff =sdf1.format(sdf2.parse(letUtil.getStartTime(strEffDate,letPropertyBean.getProcessLocation())));
				} catch (Exception e) {
					logMessage("Schedule data not found");
					return false;
				}

				if (sdf1.parse(strEff).before(cal.getTime())) {
					logMessage("Effective time should not precede current time.");
					return false;
				}
				itRslt02 = 1;
			} catch (ParseException e) {
				logException(e);
				return false;
			}
		} else {
			try {
				strEff = strEffDate + strEffTime;
				if (sdf1.parse(strEff).before(cal.getTime())) {
					logMessage("Effective time should not precede current time.");
					return false;

				}
				itRslt02 = 1;
			} catch (ParseException e) {
				logException(e);
				return false;
			}
		}
		if ("".equals(strExpDate) && "".equals(strExpTime)) {
			strExp = "99991231235959";
		} else if (!"".equals(strExpDate) && "".equals(strExpTime)) {
			try {
				strExp =sdf1.format(sdf2.parse(letUtil.getEndTime(strExpDate,letPropertyBean.getProcessLocation())));
			} catch (ParseException e) {
				logMessage("Schedule data not found");
				e.printStackTrace();
				return false;
			}
			if (letUtil.createCalendar(strEff).after(letUtil.createCalendar(strExp))) {
				logMessage("Effective time should not exceed expiration time.");
				return false;
			} else if (strEff.equals(strExp)) {
				logMessage("Effective time should not exceed expiration time.");
				return false;
			}
			itRslt02 += 2;
		} else {
			strExp = strExpDate
			+ strExpTime;
			if (letUtil.createCalendar(strEff).after(letUtil.createCalendar(strExp))) {
				logMessage("Effective time should not exceed expiration time.");
				return false;
			} else if (strEff.equals(strExp)) {
				logMessage("Effective time should not exceed expiration time.");
				return false;
			}
			itRslt02 += 2;
		}
		return true;
	}

	private void updateEffect(String newEffecDate, String modelYear,String modelCode, String modelType, String modelOption,String oldEffecDate, String expTime)  
	{
		if (!parentPanel.mode) {
			return;
		}
		try {
			Timestamp newEffTimeStamp=new Timestamp(sdf2.parse(newEffecDate).getTime());
			Timestamp oldEffTimeStamp=new Timestamp(sdf2.parse(oldEffecDate).getTime());
			Timestamp expTimeStamp=new Timestamp(sdf2.parse(expTime).getTime());
			getDao(LetPassCriteriaMtoDao.class).updateLetPassCriteriaEffTime( newEffTimeStamp, modelYear, modelCode, modelType, modelOption, oldEffTimeStamp, expTimeStamp ) ;
			Timestamp oldEffTime=new Timestamp(sdf2.parse((String)cloneHtData.get(LETDataTag.EFFECTIVE_TIME)).getTime());
			getDao(LetPassCriteriaMtoDao.class).deleteLetPassCriteriaMto((String)cloneHtData.get(LETDataTag.MODEL_YEAR_CODE), (String)cloneHtData.get(LETDataTag.MODEL_CODE), (String)cloneHtData.get(LETDataTag.MODEL_TYPE_CODE), (String)cloneHtData.get(LETDataTag.MODEL_OPTION_CODE), oldEffTime);
			for (int i = 0; i < vecChkPgm.size(); i++) {
				Hashtable htPgm = (Hashtable) vecChkPgm.get(i);
				String pgmIdStr=String.valueOf(htPgm.get(LETDataTag.CRITERIA_PGM_ID));
				LetPassCriteriaId id=new LetPassCriteriaId(modelYear,modelCode,modelType,modelOption,oldEffTimeStamp,Integer.parseInt(pgmIdStr));
				LetPassCriteria letPassCriteria =new LetPassCriteria(id,(String)pgmTypeDatas.get(vecChkPgmCategory.get(i)),(String)pgmAttrDatas.get(vecChkPgmCategory.get(i)));
				getDao(LetPassCriteriaDao.class).save(letPassCriteria);
			}
		}  catch (Exception e) {
			logException(e);
			} 
	}


	public void updateExpiration(String newExpTime, String modelYear,String modelCode, String modelType, String modelOption,String effectiveTime, String oldExpTime)  
	{
		if (!parentPanel.mode) {
			return;
		}
		try {
			Timestamp newExpTimeStamp=new Timestamp(sdf2.parse(newExpTime).getTime());
			Timestamp effectiveTimeStamp=new Timestamp(sdf2.parse(effectiveTime).getTime());
			Timestamp oldExpTimeStamp=new Timestamp(sdf2.parse(oldExpTime).getTime());
			getDao(LetPassCriteriaMtoDao.class).updateLetPassCriteriaExpTime( newExpTimeStamp, modelYear, modelCode, modelType, modelOption, effectiveTimeStamp, oldExpTimeStamp ) ;
		} catch (Exception e) {
			logException(e);
			} 
	}

	public void deletePassCriteria(String modelYear,String modelCode, String modelType, String modelOption,String effecDate)  
	{
		if (!parentPanel.mode) {
			return;
		}
		try {
			Timestamp effTimeStamp=new Timestamp(sdf2.parse(effecDate).getTime());
			getDao(LetPassCriteriaMtoDao.class).deleteLetPassCriteriaMto( modelYear, modelCode,  modelType,  modelOption,effTimeStamp);
			getDao(LetPassCriteriaDao.class).deleteLetPassCriteria( modelYear, modelCode,  modelType,  modelOption,effTimeStamp);
		}catch (Exception e) {
			logException(e);
			} 
	}

	public void addPassCriteria(String modelYear, String modelCode,String modelType, String modelOption, String effDate,String expDate, int weight) 
	{
		if (!parentPanel.mode) {
			return;
		}
		try {
			Timestamp effTimeStamp=new Timestamp(sdf2.parse(effDate).getTime());
			Timestamp expTimeStamp=new Timestamp(sdf2.parse(expDate).getTime());
			LetPassCriteriaMtoId letPassCriteriaMtoId =new  LetPassCriteriaMtoId(modelYear,  modelCode, modelType,  modelOption,effTimeStamp); 			 
			LetPassCriteriaMto letPassCriteriaMto =new  LetPassCriteriaMto(letPassCriteriaMtoId,expTimeStamp,weight);
			getDao(LetPassCriteriaMtoDao.class).save(letPassCriteriaMto);
			for (int i = 0; i < vecChkPgm.size(); i++) {
				Hashtable htPgm = (Hashtable) vecChkPgm.get(i);
				String pgmIdStr=String.valueOf(htPgm.get(LETDataTag.CRITERIA_PGM_ID));
				LetPassCriteriaId letPassCriteriaId=new LetPassCriteriaId(modelYear,modelCode,modelType,modelOption,effTimeStamp,Integer.parseInt(pgmIdStr));
				LetPassCriteria letPassCriteria =new LetPassCriteria(letPassCriteriaId,(String)pgmTypeDatas.get(vecChkPgmCategory.get(i)),(String)pgmAttrDatas.get(vecChkPgmCategory.get(i)));
				getDao(LetPassCriteriaDao.class).save(letPassCriteria);
			}
		}  catch (Exception e) {
			logException(e);
			}
	}


	private boolean  updateMtoApply(String modelYear,String modelCode,String modelType,String modelOption,String strEffDate,String strEffTime,String strExpDate,String strExpTime,boolean updateMto, int weight)
	{
		try {
			Calendar cal = Calendar.getInstance();
			itRslt01 = 1;
			strEff = sdf1.format(cal.getTime());
			if ("".equals(strExpDate) && "".equals(strExpTime)) {
				strExp = "99991231235959";
				itRslt02 = 4;
			} else if (!"".equals(strExpDate) && "".equals(strExpTime)) {
				try {
					strExp =sdf1.format(sdf2.parse(letUtil.getEndTime(strExpDate,letPropertyBean.getProcessLocation())));
				} catch (ParseException e) {
					logMessage("Schedule data not found");
					return false;
				}
				if (letUtil.createCalendar(strEff).after(letUtil.createCalendar(strExp))) {
					logMessage("Effective time should not exceed expiration time.");
					return false;
				} else if (strEff.equals(strExp)) {
					logMessage("Effective time should not exceed expiration time.");
					return false;
				}
				itRslt02 = 6;
			} else {
				strExp = strExpDate
				+ strExpTime;
				if (letUtil.createCalendar(strEff).after(letUtil.createCalendar(strExp))) {
					logMessage("Effective time should not exceed expiration time.");
					return false;
				} else if (strEff.equals(strExp)) {
					logMessage("Effective time should not exceed expiration time.");
					return false;
				}
				if ("99991231235959".equals(strExp)) {
					itRslt02 = 4;
				} else {
					itRslt02 = 6;
				}
			}			
			String effecDateStr=sdf2.format(sdf1.parse(strEff, new ParsePosition(0)));
			String expDateStr=sdf2.format(sdf1.parse(strExp, new ParsePosition(0))) + ".999999";
			Timestamp effecDate=new Timestamp(sdf2.parse(effecDateStr).getTime());
			Timestamp expDate=new Timestamp(sdf2.parse(expDateStr).getTime());
			List<LetPassCriteriaMto> letPassCriteriaPgmList=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaGreaterEqualEffecTime(modelYear, modelCode, modelType, modelOption, effecDate);
			for (LetPassCriteriaMto letPassCriteriaPgm:letPassCriteriaPgmList) {
				Timestamp getEff = letPassCriteriaPgm.getId().getEffectiveTime();
				Timestamp getExp = letPassCriteriaPgm.getEndTimestamp();
				if (getEff.compareTo(letUtil.createTimestamp(strEff, 000000000)) < 0 && getExp.compareTo(letUtil.createTimestamp(strEff, 000000000)) > 0) {
					Calendar calExp = letUtil.createCalendar(strEff);
					calExp.add(Calendar.SECOND, -1);
					updateExpiration(sdf2.format(calExp.getTime()) + ".999999",modelYear, modelCode, modelType, modelOption,getEff.toString(),getExp.toString());
				} else if (getEff.compareTo(letUtil.createTimestamp(strExp, 999999000)) < 0 && getExp.compareTo(letUtil.createTimestamp(strExp, 999999000)) > 0) {
					itRslt02 += 1;
					Calendar calEff = letUtil.createCalendar(strExp);
					calEff.add(Calendar.SECOND, +1);
					updateEffect(sdf2.format(calEff.getTime()) + ".000000",modelYear, modelCode, modelType, modelOption,getEff.toString(),getExp.toString());
				} else if (getEff.compareTo(letUtil.createTimestamp(strEff, 000000000)) >= 0 && getExp.compareTo(letUtil.createTimestamp(strExp, 999999000)) <= 0) {
					deletePassCriteria(modelYear, modelCode, modelType, modelOption,getEff.toString());
				}
			}
			addPassCriteria( modelYear, modelCode, modelType, modelOption,sdf2.format(letUtil.createCalendar(strEff).getTime()) + ".000000",sdf2.format(letUtil.createCalendar(strExp).getTime()) + ".999999",weight);
		    return true;
		} catch (Exception e) {
			logException(e);
			return false;
			} 
	} 

	private boolean updateMto(String modelYear,String modelCode,String modelType,String modelOption,String strEffDate,String strEffTime,String strExpDate,String strExpTime, int weight)	
	{		
		if(!deleteMto(this.txtYear.getText(),this.txtModel.getText(),this.txtType.getText(), this.txtOption.getText(),this.txtEffDate.getText(),this.txtEffTime.getText(),this.txtExpDate.getText(),this.txtExpTime.getText(),weight))
			return false;
		if(!addMto(this.txtYear.getText(),this.txtModel.getText(),this.txtType.getText(), this.txtOption.getText(),this.txtEffDate.getText(),this.txtEffTime.getText(),this.txtExpDate.getText(),this.txtExpTime.getText(),true,weight))
			return false;
		return true;
	}

	private boolean deleteMto(String modelYear,String modelCode,String modelType,String modelOption,String strEffDate,String strEffTime,String strExpDate,String strExpTime, int weight)
	{
		try {
			Calendar cal = Calendar.getInstance();		
			if ((Timestamp.valueOf((String) cloneHtData.get(LETDataTag.EFFECTIVE_TIME))).before(cal.getTime())) {
				logMessage("Program used in effective criteria cannot be deleted.");
				return false;
			}
			getDao(LetPassCriteriaMtoDao.class).deleteLetPassCriteriaMto( (String)cloneHtData.get(LETDataTag.MODEL_YEAR_CODE), (String)cloneHtData.get(LETDataTag.MODEL_CODE),  (String)cloneHtData.get(LETDataTag.MODEL_TYPE_CODE),  (String)cloneHtData.get(LETDataTag.MODEL_OPTION_CODE),Timestamp.valueOf((String) cloneHtData.get(LETDataTag.EFFECTIVE_TIME)));
			getDao(LetPassCriteriaDao.class).deleteLetPassCriteria( (String)cloneHtData.get(LETDataTag.MODEL_YEAR_CODE), (String)cloneHtData.get(LETDataTag.MODEL_CODE),  (String)cloneHtData.get(LETDataTag.MODEL_TYPE_CODE), (String)cloneHtData.get(LETDataTag.MODEL_OPTION_CODE),Timestamp.valueOf((String) cloneHtData.get(LETDataTag.EFFECTIVE_TIME)));
            return true;
		} catch (Exception e) {
			logException(e);	
			return false;
		}
	}
	
	private void logMessage(String msg)
	{
		parentPanel.logMessage(msg,errorMessageArea,false);
	}
	
	private void logException(Exception e)
	{
		parentPanel.logException(e,errorMessageArea,false);		
	}
} 