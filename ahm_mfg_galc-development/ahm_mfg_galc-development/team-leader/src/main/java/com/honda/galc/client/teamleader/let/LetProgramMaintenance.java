
package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import com.honda.galc.client.teamleader.let.util.AsciiLimitedCharDocument;
import com.honda.galc.client.teamleader.let.util.LETIDCreator;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetPassCriteriaDao;
import com.honda.galc.dao.product.LetPassCriteriaProgramDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.product.LetPassCriteria;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.message.Message;

/**
 * 
 * @author Gangadhararao Gadde
 * @date nov 26, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetProgramMaintenance extends JDialog 
{
	private static final long serialVersionUID = 1L;
	private Vector pgmNameDatas = null;
	private Hashtable pgmTypeDatas = null;
	private Hashtable pgmAttrDatas = null;
	private Hashtable pgmColorDatas = null;
	private String pgmUpdateType = "";
	private JDialog addProgramDialog = null;
	private JPanel addProgramContentPane = null;
	private JLabel addProgramLabel1 = null;
	private JTextField addProgramText = null;
	private JLabel addProgramLabel2 = null;
	private JComboBox addProgramCombo = null;
	private javax.swing.JButton addProgramOkButton = null;
	private javax.swing.JButton addProgramCancelButton = null;
	private Vector colorExpList = null;
	private ProgramCategoryColorExpanation programCategoryColorExpanation = null;
	private JPanel pnlBase = null;  
	private JPanel pnlProgram = null;
	private JLabel lblCriteria = null;  
	private JScrollPane spCriteria = null;  
	private JTable tblCriteria = null;   
	private JButton btnProgramAdd = null;   
	private JButton btnProgramUpdate = null;  
	private JButton btnProgramDelete = null;   
	private JButton btnCancel = null;
	private Vector vecPgmData;
	private StatusMessagePanel errorMessageArea = null;   
	private LetShippingJudgementSettingPanel parentPanel=null;
	private LetUtil letUtil=null;

	public LetProgramMaintenance() {
		super();
		initialize();
	}

	public LetProgramMaintenance(LetShippingJudgementSettingPanel letShippingJudgementSettingPanel, Vector colorExpList) {
		super();
		this.parentPanel=letShippingJudgementSettingPanel;
		this.colorExpList = colorExpList;	
		initialize();
	}

	private void initialize() {
		try {
			this.setContentPane(getPnlBase());
			setName("ProgramMaintenance");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(640, 628);
			setTitle("ProgramMaintenance");
			onInit();
			disabledButton();
		} catch (Exception e) {
			logException(e);
		}
	}


	private JPanel getPnlBase() {
		if (pnlBase == null) {
			pnlBase = new JPanel();
			pnlBase.setLayout(new BorderLayout());
			pnlBase.add(getPnlProgram(), BorderLayout.CENTER);
			pnlBase.add(getErrorMessageArea(), BorderLayout.SOUTH);
		}
		return pnlBase;
	}

	private JPanel getPnlProgram() {
		if (pnlProgram == null) {
			pnlProgram = new JPanel();
			pnlProgram.setName("JDialogProgramPanel");
			pnlProgram.setLayout(null);
			pnlProgram.add(getLblCriteria(), null);
			pnlProgram.add(getSpCriteria(), null);
			pnlProgram.add(getBtnProgramAdd(), null);
			pnlProgram.add(getBtnProgramUpdate(), null);
			pnlProgram.add(getBtnProgramDelete(), null);
			pnlProgram.add(getBtnCancel(), null);
			pnlProgram.add(getProgramCategoryColorExpanation(), null);
		}
		return pnlProgram;
	}

	private StatusMessagePanel getErrorMessageArea() {
		if (errorMessageArea == null) {
			errorMessageArea = new StatusMessagePanel();
			errorMessageArea.setFont(new Font("Dialog", java.awt.Font.BOLD, 16));
		}
		return errorMessageArea;
	}


	private JLabel getLblCriteria() {
		if (lblCriteria == null) {
			lblCriteria = new JLabel();
			lblCriteria.setBounds(49, 8, 321, 33);
			lblCriteria.setText(Message.get("InspectionProgram"));
		}
		return lblCriteria;
	}

	private JScrollPane getSpCriteria() {
		if (spCriteria == null) {
			spCriteria = new JScrollPane();
			spCriteria.setViewportView(getTblCriteria());
			spCriteria.setSize(335, 383);
			spCriteria.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			spCriteria.setLocation(50, 40);
		}
		return spCriteria;
	}

	private JTable getTblCriteria() {
		if (tblCriteria == null) {
			tblCriteria = new JTable();
			tblCriteria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblCriteria.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblCriteria.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						return;
					}
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (tblCriteria.getModel().getRowCount() == 0) {
						return;
					}
					if (lsm.isSelectionEmpty()) {
						disabledButton();
					} else {
						enabledButton();
						onSelectProgram();
					}
				}
			});
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("Program"));
			model.addColumn(Message.get("PGM_CATEGORY"));
			tblCriteria.setModel(model);
			TableColumnModel columnModel = tblCriteria.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
			columnModel.getColumn(1).setPreferredWidth(115);
			tblCriteria.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected, boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value, isSelected,hasFocus,row,column);
					if (table.getValueAt(row, 0) != null && pgmColorDatas.size() > 0) {
						String color =(String) pgmColorDatas.get((String) table.getValueAt(row, 1));
						if (color != null && !"".equals(color)) {
							org.setBackground(Color.decode(color));
						} else {
							org.setBackground(Color.WHITE);
						}
					}
					return org;
				}
			});
		}
		return tblCriteria;
	}

	private JButton getBtnProgramAdd() {
		if (btnProgramAdd == null) {
			btnProgramAdd = new JButton();
			btnProgramAdd.setSize(90, 35);
			btnProgramAdd.setText(Message.get("ADD"));
			btnProgramAdd.setLocation(446, 177);
			btnProgramAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onAddProgram();
				}
			});
		}
		return btnProgramAdd;
	}

	private JButton getBtnProgramUpdate() {
		if (btnProgramUpdate == null) {
			btnProgramUpdate = new JButton();
			btnProgramUpdate.setSize(90, 35);
			btnProgramUpdate.setText(Message.get("Modify"));
			btnProgramUpdate.setLocation(446, 247);
			btnProgramUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onUpdateProgram();
				}
			});
		}
		return btnProgramUpdate;
	}

	private JButton getBtnProgramDelete() {
		if (btnProgramDelete == null) {
			btnProgramDelete = new JButton();
			btnProgramDelete.setSize(90, 35);
			btnProgramDelete.setText(Message.get("DELETE"));
			btnProgramDelete.setLocation(446, 317);
			btnProgramDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDeleteProgram();
				}
			});
		}
		return btnProgramDelete;
	}

	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new javax.swing.JButton();
			btnCancel.setBounds(480, 402, 120, 35);
			btnCancel.setText(Message.get("CLOSE"));
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onCancel();
				}
			});
		}
		return btnCancel;
	}


	private void onInit() {
		errorMessageArea.setErrorMessageArea("");
		letUtil=new LetUtil();
		letUtil.getProgram("", "", "", "", "99991231", "235959");
		try {
			vecPgmData = letUtil.getPgmData(); 
			pgmNameDatas = letUtil.getVecPgmComboDatas();
			pgmTypeDatas = letUtil.getPgmComboTypeData();
			pgmAttrDatas = letUtil.getPgmComboAttrData();
			pgmColorDatas = letUtil.getPgmComboColorData();
			DefaultTableModel criteriaModel = (DefaultTableModel) getTblCriteria().getModel();
			criteriaModel.setRowCount(0);
			for (int i = 0; i < vecPgmData.size(); i++) {
				Hashtable htPgm = (Hashtable) vecPgmData.get(i);
				criteriaModel.addRow(new Object[]{htPgm.get(LETDataTag.CRITERIA_PGM_NAME), htPgm.get(LETDataTag.PGM_CATEGORY)});
			}
			if (vecPgmData.size() == 0) {          	
				logMessage("Criteria program not found.");
			}
		} catch (Exception e) {
			logException(e);
		} 
	}

	private void onSelectProgram() {
		getBtnProgramUpdate().setEnabled(true);
		getBtnProgramDelete().setEnabled(true);
	}

	private void onAddProgram() {
		pgmUpdateType = LETDataTag.PGM_UPDATE_TYPE_ADD;
		errorMessageArea.setErrorMessageArea("");
		try {
			if (pgmNameDatas == null) {				
				letUtil.getProgramCategoryData(); 
				pgmNameDatas = letUtil.getVecPgmComboDatas();
				pgmTypeDatas = letUtil.getPgmComboTypeData();
				pgmAttrDatas = letUtil.getPgmComboAttrData();
				pgmColorDatas = letUtil.getPgmComboColorData();
			}
			if (pgmNameDatas != null && pgmNameDatas.size() > 0) {
				addProgramDialog = new JDialog(this, true);
				addProgramDialog.setContentPane(getAddProgramContentPane());
				addProgramDialog.setSize(290, 230);
				addProgramDialog.setTitle("");
				addProgramDialog.setLocation(200, 200);
				addProgramText.setText("");
				addProgramCombo.setSelectedIndex(0);
				addProgramDialog.show();
			}
			if (LETDataTag.SUCCESS.equals(pgmUpdateType)) {
				this.onInit();
				disabledButton();
			}
			pgmUpdateType = "";
		} catch (Exception e) {
			logException(e);
		} 
	}

	private void onUpdateProgram() 
	{
		pgmUpdateType = LETDataTag.PGM_UPDATE_TYPE_MOD;
		errorMessageArea.setErrorMessageArea("");
		try {
			if (pgmNameDatas == null) {
				letUtil.getProgramCategoryData();  
				pgmNameDatas = letUtil.getVecPgmComboDatas();
				pgmTypeDatas = letUtil.getPgmComboTypeData();
				pgmAttrDatas = letUtil.getPgmComboAttrData();
				pgmColorDatas = letUtil.getPgmComboColorData();
			}
			if (pgmNameDatas != null && pgmNameDatas.size() > 0) {
				addProgramDialog = new JDialog(this, true);
				addProgramDialog.setContentPane(getAddProgramContentPane());
				addProgramDialog.setSize(290, 230);
				addProgramDialog.setTitle("");
				addProgramDialog.setLocation(200, 200);
				addProgramText.setText((String) tblCriteria.getValueAt(tblCriteria.getSelectedRow(), 0));
				addProgramCombo.setSelectedItem((String) tblCriteria.getValueAt(tblCriteria.getSelectedRow(), 1));
				addProgramDialog.show();
			}
			if (LETDataTag.SUCCESS.equals(pgmUpdateType)) {
				this.onInit();
				disabledButton();
			}
			pgmUpdateType = "";
		} catch (Exception e) {
			logException(e);
		} 
	}

	private void onDeleteProgram() 
	{
		errorMessageArea.setErrorMessageArea("");
		int result =JOptionPane.showConfirmDialog(this, new String[]{ Message.get("ReallyDelete?"),Message.get("ProgramWillBeRemovedFromCriteria") },Message.get("DeleteConfirmation"),JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			Hashtable htPgmData = (Hashtable) vecPgmData.get(tblCriteria.getSelectedRow());
			String strProgram = (String) htPgmData.get(LETDataTag.CRITERIA_PGM_NAME);
			try {
				String pgmId=String.valueOf(htPgmData.get(LETDataTag.CRITERIA_PGM_ID));
				if(!deletePGM(Integer.parseInt(pgmId)))
					return;
				this.onInit();
				disabledButton();
			} catch (Exception e) {
				logException(e);
			} 
		}
	}

	private void onCancel() {
		this.dispose();
	}

	private void disabledButton() {
		getBtnProgramUpdate().setEnabled(false);
		getBtnProgramDelete().setEnabled(false);
		getBtnProgramUpdate().setForeground(Color.GRAY);
		getBtnProgramDelete().setForeground(Color.GRAY);
	}

	private void enabledButton() {
		getBtnProgramUpdate().setEnabled(true);
		getBtnProgramDelete().setEnabled(true);
		getBtnProgramUpdate().setForeground(Color.BLACK);
		getBtnProgramDelete().setForeground(Color.BLACK);
	}

	private JPanel getAddProgramContentPane() {
		if (addProgramContentPane == null) {
			addProgramContentPane = new JPanel();
			addProgramContentPane.setLayout(null);
			addProgramContentPane.add(getAddProgramLabel1(), null);
			addProgramContentPane.add(getAddProgramText(), null);
			addProgramContentPane.add(getAddProgramLabel2(), null);
			addProgramContentPane.add(getAddProgramCombo(), null);
			addProgramContentPane.add(getAddProgramOkButton(), null);
			addProgramContentPane.add(getAddProgramCancelButton(), null);
		}
		return addProgramContentPane;
	}

	private JLabel getAddProgramLabel1() {
		if (addProgramLabel1 == null) {
			addProgramLabel1 = new JLabel();
			addProgramLabel1.setSize(250, 30);
			addProgramLabel1.setText(Message.get("EnterProgramName"));
			addProgramLabel1.setLocation(20, 20);
		}
		return addProgramLabel1;
	}

	private JTextField getAddProgramText() {
		if (addProgramText == null) {
			addProgramText = new JTextField();
			addProgramText.setSize(250, 20);
			addProgramText.setLocation(20, 55);
			addProgramText.setDocument(new AsciiLimitedCharDocument(75));
		}
		return addProgramText;
	}

	private JLabel getAddProgramLabel2() {
		if (addProgramLabel2 == null) {
			addProgramLabel2 = new JLabel();
			addProgramLabel2.setSize(250, 30);
			addProgramLabel2.setText(Message.get("SelectProgramCategory"));
			addProgramLabel2.setLocation(20, 85);
		}
		return addProgramLabel2;
	}

	private JComboBox getAddProgramCombo() {
		if (addProgramCombo == null) {
			addProgramCombo = new JComboBox(pgmNameDatas);
			addProgramCombo.setSize(250, 20);
			addProgramCombo.setLocation(20, 120);
			addProgramCombo.setBackground(Color.white);
		}
		return addProgramCombo;
	}

	private javax.swing.JButton getAddProgramOkButton() {
		if (addProgramOkButton == null) {
			addProgramOkButton = new javax.swing.JButton();
			addProgramOkButton.setSize(110, 30);
			addProgramOkButton.setLocation(20, 160);
			addProgramOkButton.setText(Message.get("OK"));
			addProgramOkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						String enterPgmName = addProgramText.getText();
						String selectPgmCategory = (String) addProgramCombo.getSelectedItem();
						if (enterPgmName != null && enterPgmName.trim().length() > 0) {
							if (LETDataTag.PGM_UPDATE_TYPE_ADD.equals(pgmUpdateType)) {
								if(!addPGM(enterPgmName,(String)pgmTypeDatas.get(selectPgmCategory),(String)pgmAttrDatas.get(selectPgmCategory)))
									return;
							} else if (LETDataTag.PGM_UPDATE_TYPE_MOD.equals(pgmUpdateType)) {
								boolean pgrmExists=false;
								if (!enterPgmName.equals(tblCriteria.getValueAt(tblCriteria.getSelectedRow(), 0))) {
									pgrmExists=true;
								} else if (!selectPgmCategory.equals(tblCriteria.getValueAt(tblCriteria.getSelectedRow(), 1))) {
									pgrmExists=false;
								} else {
									return;
								}
								String pgmId=String.valueOf(((Hashtable) vecPgmData.get(tblCriteria.getSelectedRow())).get(LETDataTag.CRITERIA_PGM_ID));
								if(!updatePGM( enterPgmName, (String)pgmTypeDatas.get(selectPgmCategory),(String)pgmAttrDatas.get(selectPgmCategory), pgrmExists,Integer.parseInt(pgmId)))
									return;
							} else {
								return;
							}
							DefaultTableModel model =(DefaultTableModel) getTblCriteria().getModel();
							if (LETDataTag.PGM_UPDATE_TYPE_ADD.equals(pgmUpdateType)) {
								model.addRow(new Object[]{addProgramText.getText(), selectPgmCategory});
							}
							pgmUpdateType = LETDataTag.SUCCESS;
						}
					} catch (Exception ex) {
						logException(ex);
					}  finally {
						addProgramDialog.dispose();
					}
					return;
				}
			});
		}
		return addProgramOkButton;
	}

	private javax.swing.JButton getAddProgramCancelButton() {
		if (addProgramCancelButton == null) {
			addProgramCancelButton = new javax.swing.JButton();
			addProgramCancelButton.setSize(110, 30);
			addProgramCancelButton.setLocation(160, 160);
			addProgramCancelButton.setText(Message.get("CANCEL"));
			addProgramCancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addProgramDialog.dispose();
					return;
				}
			});
		}
		return addProgramCancelButton;
	}

	private ProgramCategoryColorExpanation getProgramCategoryColorExpanation() {
		if (programCategoryColorExpanation == null) {
			programCategoryColorExpanation = new ProgramCategoryColorExpanation(colorExpList);
			programCategoryColorExpanation.setBounds(447, 47, 100, 48);
		}
		return programCategoryColorExpanation;
	}

	public boolean addPGM(String enterPgmName, String pgmType, String pgmAttr)
	{
		try {
			List<LetPassCriteriaProgram> letPassCriteriaPgmList=getDao(LetPassCriteriaProgramDao.class).getLetPassCriteriaPgmByPgmName(enterPgmName);
			if (letPassCriteriaPgmList.size() >= 1) {			
				logMessage("Data already exists.");
				return false;
			}
			LETIDCreator idCreator = new LETIDCreator();
			int itId = idCreator.createID("GAL718TBX", "CRITERIA_PGM_ID");
			LetPassCriteriaProgram pgrm=new LetPassCriteriaProgram();
			pgrm.setCriteriaPgmId(itId);
			pgrm.setCriteriaPgmName(enterPgmName);
			pgrm.setInspectionDeviceType(pgmType);
			pgrm.setCriteriaPgmAttr(pgmAttr);
			getDao(LetPassCriteriaProgramDao.class).save(pgrm);
			return true;
		} catch (Exception e) {
			logException(e);
			return false;
		} 
	}

	private boolean updatePGM(String enterPgmName, String pgmType, String pgmAttr,boolean pgrmExists,Integer pgmId)
	{
		try {
			if (true==pgrmExists) {
				List<LetPassCriteriaProgram> letPassCriteriaPgmList=getDao(LetPassCriteriaProgramDao.class).getLetPassCriteriaPgmByPgmName(enterPgmName);
				if (letPassCriteriaPgmList.size() >= 1) {			
					logMessage("Already existing program!");
					return false;
				}			
			}
			getDao(LetPassCriteriaProgramDao.class).updateLetPassCriteriaPgm(enterPgmName, pgmType, pgmAttr, pgmId);
			return true;
		}   catch (Exception e) {
			logException(e);
			return false;
		} 
	}

	private boolean deletePGM(Integer pgmId )
	{
		try {
			LetPassCriteria letPassCriteria=getDao(LetPassCriteriaDao.class).getLetPassCriteriaByPgmId(pgmId);
			if(letPassCriteria!=null)
			{
				logMessage("Program used in effective criteria cannot be deleted.");
				return false;
			}
			getDao(LetPassCriteriaProgramDao.class).deleteLetPassCriteriaPgmByPgmId(pgmId);
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