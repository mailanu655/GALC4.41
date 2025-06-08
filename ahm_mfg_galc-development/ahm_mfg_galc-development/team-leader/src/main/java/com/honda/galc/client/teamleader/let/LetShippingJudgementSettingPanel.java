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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.table.TableColumnModel;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetPassCriteriaDao;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date nov 26, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetShippingJudgementSettingPanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel = null;
	private final int DISPLAY_CNT = 25;
	private HashMap bgColorDatas = null;    
	private Vector colorExpList = null;
	private Vector vecPgmData;
	private Vector vecMtoData;
	private int itCurrentPage;
	private String strSearchY = new String();
	private String strSearchM = new String();
	private String strSearchT = new String();
	private String strSearchO = new String();
	private String strSearchC = new String();
	private JLabel lblTitle = null;
	private JLabel lblYear = null;
	private JLabel lblModel = null;
	private JLabel lblType = null;
	private JLabel lblOption = null;
	private JTextField txtYear = null;
	private JTextField txtModel = null;
	private JTextField txtType = null;
	private JTextField txtOption = null;
	private JCheckBox chkPastCriterion = null;
	private JLabel lblPastCriterion = null;
	private JButton btnSearch = null;
	private JScrollPane spMto = null;
	private JTable tblMto = null;
	private JTable tblCriteria=null;
	private JButton btnPrevious = null;
	private JButton btnNext = null;
	private JLabel lblPage = null;
	private JButton btnMtoAdd = null;
	private JButton btnMtoUpdate = null;
	private JButton btnMtoDelete = null;
	private JButton btnMtoCopy = null;
	 
	private JLabel lblCriteria = null;
	private JScrollPane spCriteria = null;
	private JButton btnPgmMnt = null;
	private JButton displayConnectedPgmDialogBtn;
	private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
	private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private LetProgramCategoryUtility letProgramCategoryUtility=null;
	private LetPropertyBean letPropertyBean;
	private ProgramCategoryColorExpanation programCategoryColorExpanation = null;
	private LetUtil letUtil=null;
	private int itAllCnt=0;
	public boolean mode=false;


	public LetShippingJudgementSettingPanel(TabbedMainWindow mainWindow) {
		super("Let Shipping Judgement Setting Panel", KeyEvent.VK_P,mainWindow);	
		initialize();
	}

	public LetShippingJudgementSettingPanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);
		initialize();
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(getApplicationId()).info("LetShippingJudgementSettingPanel is selected");
	}

	private void initialize() {
		letUtil=new LetUtil();
		letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());  		
		letProgramCategoryUtility=new LetProgramCategoryUtility(getApplicationId());
		colorExpList=letProgramCategoryUtility.getColorExplanation(letPropertyBean.getLetPgmCategories());
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);
		getMainWindow().addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				getTxtYear().requestFocusInWindow();
			}
		});	
	}

	private javax.swing.JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new javax.swing.JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getLblTitle(), null);                
			mainPanel.add(getLblYear(), null);                
			mainPanel.add(getLblModel(), null);                
			mainPanel.add(getLblType(), null);               
			mainPanel.add(getLblOption(), null);                
			mainPanel.add(getTxtYear(), null);               
			mainPanel.add(getTxtModel(), null);                
			mainPanel.add(getTxtType(), null);                
			mainPanel.add(getTxtOption(), null);               
			mainPanel.add(getLblPastCriterion(), null);       
			mainPanel.add(getChkPastCriterion(), null);        
			mainPanel.add(getBtnSearch(), null);                
			mainPanel.add(getSpMto(), null);                   
			mainPanel.add(getBtnPrevious(), null);           
			mainPanel.add(getBtnNext(), null);                
			mainPanel.add(getLblPage(), null);               
			mainPanel.add(getBtnMtoAdd(), null);               
			mainPanel.add(getBtnMtoUpdate(), null);            
			mainPanel.add(getBtnMtoDelete(), null);
			mainPanel.add(getBtnMtoCopy(), null);    
			mainPanel.add(getLblCriteria(), null);            
			mainPanel.add(getSpCriteria(), null);            
			mainPanel.add(getBtnPgmMnt(), null);               
			mainPanel.add(getDisplayConnectedPgmDialogBtn(), null);
			mainPanel.add(getProgramCategoryColorExpanation(), null);
		}
		return mainPanel;
	}

	public void actionPerformed(ActionEvent arg) {
	}

	private JLabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new JLabel();
			lblTitle.setBounds(132, 13, 796, 29);
			lblTitle.setText(Message.get("LETPassCriteriaSetting"));
			lblTitle.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTitle;
	}

	private JLabel getLblYear() {
		if (lblYear == null) {
			lblYear = new JLabel();
			lblYear.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblYear.setBounds(113, 58, 60, 30);
			lblYear.setText(Message.get("YEAR_CODE"));
		}
		return lblYear;
	}

	private JLabel getLblModel() {
		if (lblModel == null) {
			lblModel = new JLabel();
			lblModel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblModel.setBounds(188, 58, 60, 30);
			lblModel.setText(Message.get("MODEL"));
		}
		return lblModel;
	}

	private JLabel getLblType() {
		if (lblType == null) {
			lblType = new JLabel();
			lblType.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblType.setBounds(263, 58, 60, 30);
			lblType.setText(Message.get("TYPE"));
		}
		return lblType;
	}

	private JLabel getLblOption() {
		if (lblOption == null) {
			lblOption = new JLabel();
			lblOption.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblOption.setText(Message.get("OPTION"));
			lblOption.setBounds(338, 58, 60, 30);
		}
		return lblOption;
	}

	private JTextField getTxtYear() {
		if (txtYear == null) {
			txtYear = new JTextField();
			txtYear.setDocument(new AsciiDocument(1));
			txtYear.setBounds(113, 88, 50, 20);
		}
		return txtYear;
	}

	private JTextField getTxtModel() {
		if (txtModel == null) {
			txtModel = new JTextField();
			txtModel.setDocument(new AsciiDocument(3));
			txtModel.setBounds(188, 88, 50, 20);
		}
		return txtModel;
	}

	private JTextField getTxtType() {
		if (txtType == null) {
			txtType = new JTextField();
			txtType.setDocument(new AsciiDocument(3));
			txtType.setBounds(263, 88, 50, 20);
		}
		return txtType;
	}

	private JTextField getTxtOption() {
		if (txtOption == null) {
			txtOption = new JTextField();
			txtOption.setDocument(new AsciiDocument(3));
			txtOption.setBounds(338, 88, 50, 20);
		}
		return txtOption;
	}

	private JCheckBox getChkPastCriterion() {
		if (chkPastCriterion == null) {
			chkPastCriterion = new JCheckBox();
			chkPastCriterion.setBounds(403, 71, 21, 24);
		}
		return chkPastCriterion;
	}

	private JLabel getLblPastCriterion() {
		if (lblPastCriterion == null) {
			lblPastCriterion = new JLabel();
			lblPastCriterion.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			lblPastCriterion.setBounds(428, 66, 130, 30);
			lblPastCriterion.setText(Message.get("INCLUDE_EXPIRED_CRITERIA"));
		}
		return lblPastCriterion;
	}

	private JButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new JButton(Message.get("Search"));
			btnSearch.setBounds(574, 68, 90, 34);
			btnSearch.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			btnSearch.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onSearch();
				}
			});
		}
		return btnSearch;
	}

	private JScrollPane getSpMto() {
		if (spMto == null) {
			spMto = new JScrollPane();
			spMto.setViewportView(getTblMto());
			spMto.setSize(550, 300);
			spMto.setLocation(127, 150);
		}
		return spMto;
	}

	private JTable getTblMto() {
		if (tblMto == null) {
			tblMto = new JTable();
			tblMto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblMto.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblMto.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						return;
					}
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (tblMto.getModel().getRowCount() == 0) {
						return;
					}
					if (lsm.isSelectionEmpty()) {
						disabledButton();
					} else {
						onSelectMto();
					}
				}
			});
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("YEAR_CODE"));
			model.addColumn(Message.get("MODEL"));
			model.addColumn(Message.get("TYPE"));
			model.addColumn(Message.get("OPTION"));
			model.addColumn(Message.get("WEIGHT"));
			model.addColumn(Message.get("EFFECTIVE_PERIOD"));
			tblMto.setModel(model);
			TableColumnModel columnModel = tblMto.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(63);
			columnModel.getColumn(1).setPreferredWidth(50);
			columnModel.getColumn(2).setPreferredWidth(60);
			columnModel.getColumn(3).setPreferredWidth(70);
			columnModel.getColumn(4).setPreferredWidth(50);
			columnModel.getColumn(5).setPreferredWidth(254);
		}
		return tblMto;
	}

	private JButton getBtnPrevious() {
		if (btnPrevious == null) {
			btnPrevious = new JButton(Message.get("PREVIOUS"));
			btnPrevious.setBounds(309, 460, 80, 30);
			btnPrevious.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			btnPrevious.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onPrevious();
				}
			});
		}
		return btnPrevious;
	}

	private JButton getBtnNext() {
		if (btnNext == null) {
			btnNext = new JButton(Message.get("NEXT"));
			btnNext.setSize(80, 30);
			btnNext.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			btnNext.setLocation(391, 460);
			btnNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onNext();
				}
			});

		}
		return btnNext;
	}

	private JLabel getLblPage() {
		if (lblPage == null) {
			lblPage = new JLabel();
			lblPage.setSize(160, 33);
			lblPage.setText("");
			lblPage.setLocation(501, 568);
		}
		return lblPage;
	}

	private JButton getBtnMtoAdd() {
		if (btnMtoAdd == null) {
			btnMtoAdd = new JButton();
			btnMtoAdd.setSize(90, 35);
			btnMtoAdd.setText(Message.get("ADD"));
			btnMtoAdd.setLocation(200, 525);
			btnMtoAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onAddMto();
				}
			});
		}
		return btnMtoAdd;
	}

	private JButton getBtnMtoUpdate() {
		if (btnMtoUpdate == null) {
			btnMtoUpdate = new JButton();
			btnMtoUpdate.setSize(90, 35);
			btnMtoUpdate.setText(Message.get("Modify"));
			btnMtoUpdate.setLocation(310, 525);
			btnMtoUpdate.setForeground(Color.GRAY);
			btnMtoUpdate.setEnabled(false);
			btnMtoUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onUpdateMto();
				}
			});
		}
		return btnMtoUpdate;
	}

	private JButton getBtnMtoDelete() {
		if (btnMtoDelete == null) {
			btnMtoDelete = new JButton();
			btnMtoDelete.setSize(90, 35);
			btnMtoDelete.setText(Message.get("DELETE"));
			btnMtoDelete.setLocation(420, 525);
			btnMtoDelete.setForeground(Color.GRAY);
			btnMtoDelete.setEnabled(false);
			btnMtoDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDeleteMto();
				}
			});
		}
		return btnMtoDelete;
	}
	
	private JButton getBtnMtoCopy() {
		if (btnMtoCopy == null) {
			btnMtoCopy = new JButton();
			btnMtoCopy.setSize(90, 35);
			btnMtoCopy.setText(Message.get("COPY"));
			btnMtoCopy.setLocation(530, 525);
			btnMtoCopy.setForeground(Color.GRAY);
			btnMtoCopy.setEnabled(false);
			btnMtoCopy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onCopyMto();
				}
			});
		}
		return btnMtoCopy;
	}

	private JLabel getLblCriteria() {
		if (lblCriteria == null) {
			lblCriteria = new JLabel();
			lblCriteria.setSize(220, 25);
			lblCriteria.setText(Message.get("PassCriteria"));
			lblCriteria.setLocation(732, 125);
		}
		return lblCriteria;
	}

	private JScrollPane getSpCriteria() {
		if (spCriteria == null) {
			spCriteria = new JScrollPane();
			spCriteria.setViewportView(getTblCriteria());
			spCriteria.setSize(218, 300);
			spCriteria.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			spCriteria.setLocation(734, 150);
		}
		return spCriteria;
	}

	private JTable getTblCriteria() 
	{
		if(tblCriteria==null)
		{
			tblCriteria = new JTable();
			tblCriteria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblCriteria.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("CRITERIA_PROGRAM"));
			tblCriteria.setModel(model);
			TableColumnModel columnModel = tblCriteria.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
			tblCriteria.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
					JLabel org =(JLabel) super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
					if (table.getValueAt(row, column) != null&& !("".equals(table.getValueAt(row, column)))&& bgColorDatas.size() > 0) {
						org.setBackground(Color.decode((String) bgColorDatas.get((String) table.getValueAt(row, column))));
					} else {
						org.setBackground(Color.WHITE);
					}
					return org;
				}
			});
		}
		return tblCriteria;
	}

	private JButton getBtnPgmMnt() {
		if (btnPgmMnt == null) {
			btnPgmMnt = new JButton();
			btnPgmMnt.setBounds(740, 525, 200, 35);
			btnPgmMnt.setText(Message.get("MAINTENANCE_PROGRAM"));
			btnPgmMnt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onProgramButton();
				}
			});
		}
		return btnPgmMnt;
	}
	
	protected JButton getDisplayConnectedPgmDialogBtn() {
		if (displayConnectedPgmDialogBtn == null) {
			JButton button = new JButton();
			button.setBounds(740, 485, 200, 35);
			button.setText(Message.get("CONNECTED_PROGRAM"));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onConnectedProgramButton();
				}
			});
			displayConnectedPgmDialogBtn = button;
		}
		return displayConnectedPgmDialogBtn;
	}
	
	private void onSearch() 
	{
		this.strSearchY = this.txtYear.getText();
		this.strSearchM = this.txtModel.getText();
		this.strSearchT = this.txtType.getText();
		this.strSearchO = this.txtOption.getText();
		if (chkPastCriterion.isSelected()) {
			this.strSearchC = "1";
		} else {
			this.strSearchC = "0";
		}
		onSearch(0);                       
	}

	private void onSearch(int itPage) 
	{
		setErrorMessage("");
		getBtnMtoUpdate().setForeground(Color.GRAY);
		getBtnMtoUpdate().setEnabled(false);
		getBtnMtoDelete().setForeground(Color.GRAY);
		getBtnMtoDelete().setEnabled(false);
		disableCopyButton();
		DefaultTableModel dtmYmto = (DefaultTableModel) getTblMto().getModel();
		DefaultTableModel dtmCriteria = (DefaultTableModel) getTblCriteria().getModel();
		itCurrentPage = itPage;
		int itStart = 0;
		int itEnd = 0;
		itStart = itCurrentPage * DISPLAY_CNT + 1;
		itEnd = itCurrentPage * DISPLAY_CNT + DISPLAY_CNT;
		dtmYmto.setRowCount(0);
		dtmCriteria.setRowCount(0);
		Hashtable htRslt;
		try {
			getYMTOData(this.strSearchY,this.strSearchM,this.strSearchT,this.strSearchO,this.strSearchC,String.valueOf(itStart), String.valueOf(itEnd));
			dtmYmto.setRowCount(0);
			for (int i = 0; i < vecMtoData.size(); i++) {
				htRslt = (Hashtable) vecMtoData.get(i);
				String strEff = new String();
				String strExp = new String();
				if (!"".equals((String) htRslt.get(LETDataTag.EFFECTIVE_TIME))) {
					strEff = (String) htRslt.get(LETDataTag.EFFECTIVE_TIME);
					strEff = sdf3.format(letUtil.createCalendarWithoutParsePostion(strEff).getTime());
				} else {
					strEff = "";
				}
				if (!"".equals((String) htRslt.get(LETDataTag.EXPIRATION_TIME))) {
					strExp = (String) htRslt.get(LETDataTag.EXPIRATION_TIME);
					strExp = sdf3.format(letUtil.createCalendarWithoutParsePostion(strExp).getTime());
				} else {
					strExp = "";
				}
				dtmYmto.addRow(new Object[]{
						htRslt.get(LETDataTag.MODEL_YEAR_CODE),
						htRslt.get(LETDataTag.MODEL_CODE),
						htRslt.get(LETDataTag.MODEL_TYPE_CODE),
						htRslt.get(LETDataTag.MODEL_OPTION_CODE),
						htRslt.get(LETDataTag.CONDITION_WEIGHT),
						strEff + " - " + strExp});
			}
			int AllPages = 0;
			if (itAllCnt % DISPLAY_CNT == 0) {
				AllPages = itAllCnt / DISPLAY_CNT;
			} else {
				AllPages = itAllCnt / DISPLAY_CNT + 1;
			}
			String strPage = new String();
			String[] paramStringArray = new String[]{String.valueOf(itCurrentPage + 1),String.valueOf(AllPages),String.valueOf(itAllCnt)};
			strPage = (String)Message.get("RECORDS", paramStringArray);
			Vector vecPage = new Vector();
			vecPage.add(String.valueOf(itCurrentPage + 1));
			vecPage.add(String.valueOf(AllPages));
			vecPage.add(String.valueOf(itAllCnt));
			getLblPage().setText(strPage);
			if (itCurrentPage == 0) {
				getBtnPrevious().setForeground(Color.GRAY);
				getBtnPrevious().setEnabled(false);
			} else {
				getBtnPrevious().setForeground(Color.BLACK);
				getBtnPrevious().setEnabled(true);
			}
			if (itAllCnt <= itEnd) {
				getBtnNext().setForeground(Color.GRAY);
				getBtnNext().setEnabled(false);
			} else {
				getBtnNext().setForeground(Color.BLACK);
				getBtnNext().setEnabled(true);
			}
		} catch (Exception e) {
			dtmYmto.setRowCount(0);
			dtmCriteria.setRowCount(0);
			getLblPage().setText("");
			logException(e,getMainWindow().getStatusMessagePanel(),true);
		} 
	}

	private void onSelectMto() 
	{
		Calendar cal = Calendar.getInstance();
		DefaultTableModel criteriaModel = (DefaultTableModel) getTblCriteria().getModel();
		Hashtable htMto = (Hashtable) this.vecMtoData.get(tblMto.getSelectedRow());		
		if (htMto.get(LETDataTag.EXPIRATION_TIME) == null || "".equals(htMto.get(LETDataTag.EXPIRATION_TIME))) {
	        
        } else {
            String strEffective = (String) htMto.get(LETDataTag.EFFECTIVE_TIME);
            strEffective = strEffective.substring(0, 4)
                    + strEffective.substring(5, 7)
                    + strEffective.substring(8, 10)
                    + strEffective.substring(11, 13)
                    + strEffective.substring(14, 16)
                    + strEffective.substring(17, 19);
            if (Long.parseLong(strEffective)
                    > Long.parseLong(sdf2.format(cal.getTime()))) {
                getBtnMtoDelete().setForeground(Color.BLACK);
                getBtnMtoDelete().setEnabled(true);
            } else {
                getBtnMtoDelete().setForeground(Color.GRAY);
                getBtnMtoDelete().setEnabled(false);
            }
        }
		if (htMto.get(LETDataTag.EXPIRATION_TIME) == null || "".equals(htMto.get(LETDataTag.EXPIRATION_TIME))) {
			getBtnMtoUpdate().setForeground(Color.BLACK);
			getBtnMtoUpdate().setEnabled(true);
		} else {
			String strExpration = (String) htMto.get(LETDataTag.EXPIRATION_TIME);
			strExpration = strExpration.substring(0, 4)
			+ strExpration.substring(5, 7)
			+ strExpration.substring(8, 10)
			+ strExpration.substring(11, 13)
			+ strExpration.substring(14, 16)
			+ strExpration.substring(17, 19);
			if (Long.parseLong(strExpration)> Long.parseLong(sdf2.format(cal.getTime()))) {
				getBtnMtoUpdate().setForeground(Color.BLACK);
				getBtnMtoUpdate().setEnabled(true);
			} else {
				getBtnMtoUpdate().setForeground(Color.GRAY);
				getBtnMtoUpdate().setEnabled(false);
			}
		}
		enableCopyButton();
		try {
			vecPgmData = getCriteriaPgm((String)htMto.get(LETDataTag.MODEL_YEAR_CODE),  (String)htMto.get(LETDataTag.MODEL_CODE), (String)htMto.get(LETDataTag.MODEL_TYPE_CODE), (String)htMto.get(LETDataTag.MODEL_OPTION_CODE), (String)htMto.get(LETDataTag.EFFECTIVE_TIME));
			bgColorDatas = new HashMap();
			criteriaModel.setRowCount(0);
			for (int i = 0; i < vecPgmData.size(); i++) {
				Hashtable htPgm = (Hashtable) vecPgmData.get(i);
				criteriaModel.addRow(new Object[]{htPgm.get(LETDataTag.CRITERIA_PGM_NAME)});
				bgColorDatas.put(htPgm.get(LETDataTag.CRITERIA_PGM_NAME), htPgm.get(LETDataTag.BG_COLOR));
			}
		} catch (Exception e) {
			criteriaModel.setRowCount(0);
		}
		TableColumnModel columnModel = getTblCriteria().getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(200);
	}

	private void onPrevious() {
		onSearch(itCurrentPage - 1);
	}

	private void onNext() {
		onSearch(itCurrentPage + 1);
	}

	private void onAddMto() 
	{
		LetYMTOMaintenance ymto = new LetYMTOMaintenance(this, colorExpList);
		ymto.setModal(true);
		ymto.setVisible(true);
		//the legacy code had this infinite while logic as long as ymto screen is visible.....
		while (ymto.isVisible()) {
		}
		this.onSearch();
	}

	private void onUpdateMto()
	{
		JTable table = getTblMto();
		Hashtable htMto = (Hashtable) vecMtoData.get(table.getSelectedRow());
		LetYMTOMaintenance ymto = new LetYMTOMaintenance(this, htMto, colorExpList);
		ymto.setUpdateMode(htMto, vecPgmData);
		ymto.setModal(true);
		ymto.setVisible(true);
	}

	private void onDeleteMto() {
		try {
			int result = JOptionPane.showConfirmDialog(this,Message.get("ReallyDelete?"),Message.get("DeleteConfirmation"),JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				DefaultTableModel criteriaModel = (DefaultTableModel) getTblCriteria().getModel();
				mode=true;
				Calendar cal = Calendar.getInstance();
				Vector vecParam = new Vector();
				Hashtable oldData = (Hashtable)vecMtoData.get(getTblMto().getSelectedRow());
				if ((Timestamp.valueOf((String) oldData.get(LETDataTag.EFFECTIVE_TIME))).before(cal.getTime())) {
					logMessage("Program used in effective criteria cannot be deleted.",getMainWindow().getStatusMessagePanel(),true);
					return;
				}
				getDao(LetPassCriteriaMtoDao.class).deleteLetPassCriteriaMto( (String)oldData.get(LETDataTag.MODEL_YEAR_CODE), (String)oldData.get(LETDataTag.MODEL_CODE),  (String)oldData.get(LETDataTag.MODEL_TYPE_CODE),  (String)oldData.get(LETDataTag.MODEL_OPTION_CODE),Timestamp.valueOf((String) oldData.get(LETDataTag.EFFECTIVE_TIME)));
				String info = (String)oldData.get(LETDataTag.MODEL_YEAR_CODE) + (String)oldData.get(LETDataTag.MODEL_CODE) + (String)oldData.get(LETDataTag.MODEL_TYPE_CODE) + (String)oldData.get(LETDataTag.MODEL_OPTION_CODE);
				logUserAction("deleted LetPassCriteriaMto " + info);
				getDao(LetPassCriteriaDao.class).deleteLetPassCriteria( (String)oldData.get(LETDataTag.MODEL_YEAR_CODE), (String)oldData.get(LETDataTag.MODEL_CODE),  (String)oldData.get(LETDataTag.MODEL_TYPE_CODE), (String)oldData.get(LETDataTag.MODEL_OPTION_CODE),Timestamp.valueOf((String) oldData.get(LETDataTag.EFFECTIVE_TIME)));
				logUserAction("deleted LetPassCriteria " + info);
				this.onSearch();
			}
		} catch (Exception e) {
			logException(e,getMainWindow().getStatusMessagePanel(),true);
		} 
	}
	
	private void onCopyMto() 
	{
		LetYMTOMaintenance ymto = new LetYMTOMaintenance(this, colorExpList);
		JTable table = getTblMto();
		ymto.setCopyMode(vecPgmData);
		ymto.setModal(true);
		ymto.setVisible(true);
		this.onSearch();
	}
	
	private void enableCopyButton(){
		getBtnMtoCopy().setEnabled(true);
		getBtnMtoCopy().setForeground(Color.BLACK);
	}
	
	private void disableCopyButton(){
		getBtnMtoCopy().setEnabled(false);
		getBtnMtoCopy().setForeground(Color.GRAY);
	}
	
	private void onProgramButton() {
		LetProgramMaintenance pm = new LetProgramMaintenance(this, colorExpList);
		pm.setModal(true);
		pm.setVisible(true);
	}
	
	protected void onConnectedProgramButton() {
		ConnectedProgramDialog dlg = new ConnectedProgramDialog();
		dlg.setLocationRelativeTo(getMainWindow());
		dlg.setModal(true);
		dlg.setVisible(true);
	}

	private void disabledButton() {
		getBtnMtoUpdate().setEnabled(false);
		getBtnMtoDelete().setEnabled(false);
		
		getBtnMtoUpdate().setForeground(Color.GRAY);
		getBtnMtoDelete().setForeground(Color.GRAY);
		disableCopyButton();
	}

	private ProgramCategoryColorExpanation getProgramCategoryColorExpanation() {
		if (programCategoryColorExpanation == null) {
			programCategoryColorExpanation = new ProgramCategoryColorExpanation(colorExpList);
			programCategoryColorExpanation.setBounds(795, 58, 100, 50);
		}
		return programCategoryColorExpanation;
	}

	private void getYMTOData(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,String checkStr,String startRow,String endRow)
	{
		vecMtoData = new Vector();
		itAllCnt=0;
		Hashtable htData;
		String strWork = new String();
		try {
			List<Object[]> letPassCriteriaList=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaBySearchParams(modelYearCode, modelCode, modelTypeCode, modelOptionCode, checkStr, startRow, endRow);
			if (letPassCriteriaList.size() == 0) {
				logMessage("Criteria not found.",getMainWindow().getStatusMessagePanel(),true);
				return ;		
			}
			for(Object[] letPassCriteria:letPassCriteriaList) {
				htData = new Hashtable();
				htData.put(LETDataTag.MODEL_YEAR_CODE, StringUtils.trimToEmpty((String)letPassCriteria[1]));
				htData.put(LETDataTag.MODEL_CODE, (strWork != null ? StringUtils.trimToEmpty((String)letPassCriteria[2]) : ""));
				htData.put(LETDataTag.MODEL_TYPE_CODE, StringUtils.trimToEmpty((String)letPassCriteria[3]));
				htData.put(LETDataTag.MODEL_OPTION_CODE, (strWork != null ?  StringUtils.trimToEmpty((String)letPassCriteria[4]) : ""));
				htData.put(LETDataTag.CONDITION_WEIGHT, letPassCriteria[7]==null?"":letPassCriteria[7].toString() );
				htData.put(LETDataTag.EFFECTIVE_TIME, (strWork == null ? "" : sdf1.format((Timestamp)letPassCriteria[5])));
				htData.put(LETDataTag.EXPIRATION_TIME, (strWork == null ? null : sdf1.format((Timestamp)letPassCriteria[6])));
				vecMtoData.add(htData);
			}		
			itAllCnt=getDao(LetPassCriteriaMtoDao.class).getYMTOSearchCount(modelYearCode, modelCode, modelTypeCode, modelOptionCode, checkStr);
		} catch (Exception e){
			logException(e,getMainWindow().getStatusMessagePanel(),true);	
		} 
	}

	private Vector getCriteriaPgm(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,String effectiveTime)
	{
		Vector vecData = new Vector();
		Hashtable htData;		
		try {
			List<Object[]> letPassCrPgmList=getDao(LetPassCriteriaMtoDao.class).findLetPassCritPgrm( modelYearCode, modelCode, modelTypeCode, modelOptionCode, new Timestamp(sdf1.parse(effectiveTime).getTime()));
			for (Object[] letCritPgm:letPassCrPgmList) {
				htData = new Hashtable();
				htData.put(LETDataTag.CRITERIA_PGM_ID,ObjectUtils.defaultIfNull(letCritPgm[0], ""));
				htData.put(LETDataTag.CRITERIA_PGM_NAME,ObjectUtils.defaultIfNull(letCritPgm[1], ""));
				htData.put(LETDataTag.BG_COLOR,ObjectUtils.defaultIfNull(letCritPgm[2], ""));
				vecData.add(htData);
			}			
		}  catch (Exception e) {
			logException(e,getMainWindow().getStatusMessagePanel(),true);
		} 
		return vecData;
	}
	
	public void logMessage(String msg,StatusMessagePanel statusMsgPanel,boolean isParentWindow)
	{
		Logger.getLogger(getApplicationId()).error(msg);
		if(isParentWindow)
		  getMainWindow().setErrorMessage(msg);
		if(statusMsgPanel!=null)
			statusMsgPanel.setErrorMessageArea(msg);		
	}
	
	public void logException(Exception e,StatusMessagePanel statusMsgPanel,boolean isParentWindow)
	{
		Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Let Shipping Judgement Setting Panel screen");
		e.printStackTrace();
		if(isParentWindow)
		  getMainWindow().setErrorMessage("An error Occurred while processing the Let Shipping Judgement Setting Panel screen");	
		if(statusMsgPanel!=null)
			statusMsgPanel.setErrorMessageArea("An error Occurred while processing the Let Shipping Judgement Setting Panel screen");
		
	}

}