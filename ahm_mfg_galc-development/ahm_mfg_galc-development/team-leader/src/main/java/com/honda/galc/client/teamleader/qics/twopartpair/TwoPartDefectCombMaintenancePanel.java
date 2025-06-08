package com.honda.galc.client.teamleader.qics.twopartpair;

import static com.honda.galc.service.ServiceFactory.getDao;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dao.qics.DefectTypeDescriptionDao;
import com.honda.galc.dao.qics.InspectionTwoPartDescriptionDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.InspectionTwoPartDescription;
import com.honda.galc.entity.qics.Iqs;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.entity.qics.Regression;
import com.honda.galc.util.PropertyComparator;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 19, 2015
 */
public class TwoPartDefectCombMaintenancePanel extends QicsMaintenanceTabbedPanel  implements ListSelectionListener{

	private static final long serialVersionUID = 1L;
	private JLabel partGroupLabel = null;
	private JLabel defectGroupLabel = null;
	private JLabel deptLabel = null;
	private JLabel lineLabel = null;
	private JLabel zoneLabel = null;
	private JLabel iqsCategortItemLabel = null;
	private JLabel regressionCodeLabel = null;
	private JLabel lockModeLabel = null;
	private JLabel invRepairTimeLabel = null;
	private JLabel onlineRepairTimeLabel = null;
	private JLabel firingFlagLabel = null;
	private JLabel noticeLabel = null;
	private JComboBox partGroupComboBox = null;
	private JComboBox defectGroupComboBox = null;
	private JComboBox deptComboBox = null;
	private JComboBox lineComboBox = null;
	private JComboBox zoneComboBox = null;
	private JComboBox iqsCategoryItemComboBox = null;
	private JComboBox regressionCodeComboBox = null;
	private JCheckBox firingFlagCheckBox;
	private JTextField lockModeTextField = null;
	private JTextField invRepairTimeTextField = null;
	private JTextField onlineRepairTimeTextField = null;
	private JButton assignButton = null;
	private JButton saveCombButton = null;
	private JButton deleteCombButton = null;
	private DrawnButton downDrawButton = null;
	private DrawnButton upDrawButton = null;
	private JScrollPane partLocationScrollPane = null;
	private JScrollPane twoPartLocationScrollPane = null;
	private JScrollPane defectSecPartScrollPane = null;
	private JScrollPane combinationScrollPane = null;
	private ObjectTablePane<DefectDescription> combinationObjectTablePane = null;
	private ObjectTablePane<DefectTypeDescription> defectSecPartObjectTablePane = null;
	private ObjectTablePane<InspectionTwoPartDescription>  partLocationObjectTablePane = null;
	private ObjectTablePane<InspectionTwoPartDescription>  twoPartLocationObjectTablePane = null;
	private JPanel defectDescSidePanel = null;
	private static final String SELECT_DEPARTMENT = "Select Department";
	private static final String SELECT_LINE = "Select Line";
	private static final String SELECT_ZONE = "Select Zone";
	private static final String SELECT_REGRESSION = "Select Regression";
	private static final String SELECT_IQS = "Select IQS";
	private String dept = null;
	private String line = null;
	private String zone = null;
	private String regressionCode = null;
	private String iqsCategory = null;
	private String iqsItemName = null;
	private short lockMode = -99; 
	private int invRepairTime = -99; 
	private int onlineRepairTime = -99; 
	private boolean firingFlag = false;
	private static final String TWO_PART_PAIR = "TWO PART PAIR";


	public TwoPartDefectCombMaintenancePanel(QicsMaintenanceFrame mainWindow) {
		super("Two Part Defect Comb Maintenance",KeyEvent.VK_T);
		setMainWindow(mainWindow);
		initialize();
	}

	@Override
	public void deselected(ListSelectionModel model) {
	}

	@Override
	public void selected(ListSelectionModel model) {
	}

	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			isInitialized = true;
		}
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == getPartGroupComboBox())
			selectPartGroup();
		else if (e.getSource() == getDefectGroupComboBox())
			selectDefectGroup();
		else if (e.getSource() == getDownDrawButton())
			clickMoveDownCombination();
		else if (e.getSource() == getUpDrawButton())
			clickMoveUpCombination();
		else if (e.getSource() == getAssignButton())
			clickAssignButton();
		else if (e.getSource() == getSaveCombinationButton())
			clickSaveCombinationButton();
		else if (e.getSource() == getDeleteCombinationButton())
			clickDeleteCombinationButton();		
		else if (e.getSource() == getDeptComboBox())
			selectDept();
	};

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource()== getPartLocationObjectTablePane().getTable().getSelectionModel()) {
			clickPartLocationTable();
			enableDisableDownButton();
		}
		else if (e.getSource() == getTwoPartLocObjectTablePane().getTable().getSelectionModel())
			enableDisableDownButton();
		else if (e.getSource() == getDefectSecPartObjectTablePane().getTable().getSelectionModel())
			enableDisableDownButton();
		else if (e.getSource() == getCombinationObjectTablePane().getTable().getSelectionModel())
			enableDisableUpButton();
	};

	public void clickDeleteCombinationButton() {
		setErrorMessage("");
		if (getCombinationObjectTablePane().getSelectedItems().size() <= 0) {
			setErrorMessage("No valid combinations selected for deletion");
			return;
		}
		int confirm =JOptionPane.showConfirmDialog(this,"All the selected combinations in the table will be deleted from database. Are you sure?","Confirmation",JOptionPane.YES_NO_OPTION);
		if (confirm != 0) { 
			return;
		}
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		getDao(DefectDescriptionDao.class).removeAll(getCombinationObjectTablePane().getSelectedItems());
		getCombinationObjectTablePane().removeData();
		getUpDrawButton().setEnabled(false);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void clickMoveDownCombination() {
		String selectedPartGroupName = ((String)getPartGroupComboBox().getSelectedItem()).trim();		
		List<InspectionTwoPartDescription> selectedPartLocations =  getPartLocationObjectTablePane().getSelectedItems();
		List<InspectionTwoPartDescription> selectedTwoPartLocations = getTwoPartLocObjectTablePane().getSelectedItems();
		List<DefectTypeDescription> selectedDefectSecParts = getDefectSecPartObjectTablePane().getSelectedItems();
		List <DefectDescription> defectDescList=new ArrayList<DefectDescription>();
		setErrorMessage("");
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		List<String> selParts=new ArrayList<String>();
		List<String> selPartLocations=new ArrayList<String>();		
		for(InspectionTwoPartDescription twoPartDesc:selectedPartLocations)
		{
			selParts.add(twoPartDesc.getId().getInspectionPartName());
			selPartLocations.add(twoPartDesc.getId().getInspectionPartLocationName());
		}
		List<String> selTwoParts=new ArrayList<String>();
		List<String> selTwoPartLocations=new ArrayList<String>();		
		for(InspectionTwoPartDescription twoPartDesc:selectedTwoPartLocations)
		{
			selTwoParts.add(twoPartDesc.getId().getTwoPartPairPart());
			selTwoPartLocations.add(twoPartDesc.getId().getTwoPartPairLocation());
		}
		List<String> selDefectTypes=new ArrayList<String>();
		List<String> selSecDefects=new ArrayList<String>();		
		for(DefectTypeDescription defectType:selectedDefectSecParts)
		{
			selDefectTypes.add(defectType.getDefectTypeName());
			selSecDefects.add(defectType.getSecondaryPartName());
		}
		defectDescList=getDao(DefectDescriptionDao.class).findAllDefectDescByInClause( selectedPartGroupName,selParts,selPartLocations, selDefectTypes, selSecDefects, selTwoParts, selTwoPartLocations);
		defectDescList=createDefectDescriptionBasedOnSelection(defectDescList);
		getCombinationObjectTablePane().reloadData(defectDescList);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		
	}

	private List<DefectDescription> createDefectDescriptionBasedOnSelection(List<DefectDescription> existingDefectDescriptions) {
		List<InspectionTwoPartDescription> parts = getPartLocationObjectTablePane().getSelectedItems();
		if (parts == null || parts.isEmpty()) return existingDefectDescriptions;
		List<DefectTypeDescription> defectTypes = getDefectSecPartObjectTablePane().getSelectedItems();
		List<InspectionTwoPartDescription> twoParts = getTwoPartLocObjectTablePane().getSelectedItems();
		if (defectTypes == null || defectTypes.isEmpty())return existingDefectDescriptions;
		fetchDefectDescSidePaneData();
		List<DefectDescription> newDefectDescList=new ArrayList<DefectDescription>();
		for (InspectionTwoPartDescription part : parts) {
			for (InspectionTwoPartDescription twoPart : twoParts)
			{
				for (DefectTypeDescription defectType : defectTypes) {
					DefectDescription defect = constructDefectDescription(part, defectType,twoPart);
					if(existingDefectDescriptions.size()==0)
					{
						newDefectDescList.add(defect);
					}else
					{
						for(DefectDescription existingDefectDesc:existingDefectDescriptions)
						{
							if(!(existingDefectDesc.getInspectionPartName().equals(defect.getInspectionPartName())
									&& existingDefectDesc.getInspectionPartLocationName().equals(defect.getInspectionPartLocationName())
									&& existingDefectDesc.getDefectTypeName().equals(defect.getDefectTypeName())
									&& existingDefectDesc.getTwoPartPairPart().equals(defect.getTwoPartPairPart())
									&& existingDefectDesc.getTwoPartPairLocation().equals(defect.getTwoPartPairLocation())
									&& existingDefectDesc.getSecondaryPartName().equals(defect.getSecondaryPartName())
									&& existingDefectDesc.getPartGroupName().equals(defect.getPartGroupName())))
							{
								newDefectDescList.add(defect);
							}
						}
					}
				}
			}
		}
		if(newDefectDescList.size()>0)
			existingDefectDescriptions.addAll(newDefectDescList);
		return existingDefectDescriptions;		
	}

	protected DefectDescription constructDefectDescription(InspectionTwoPartDescription part, DefectTypeDescription defectType, InspectionTwoPartDescription twoPart) {
		DefectDescription defect = new DefectDescription();
		defect.getId().setDefectTypeName(defectType.getDefectTypeName().trim());
		defect.getId().setInspectionPartLocationName(part.getInspectionPartLocationName().trim());
		defect.getId().setInspectionPartName(part.getInspectionPartName().trim());
		defect.getId().setTwoPartPairPart(twoPart.getTwoPartPairPart().trim());
		defect.getId().setTwoPartPairLocation(twoPart.getTwoPartPairLocation().trim());
		defect.getId().setPartGroupName(part.getPartGroupName().trim());
		defect.getId().setSecondaryPartName(defectType.getSecondaryPartName().trim());
		defect.setTwoPartDefectFlag(true);
		defect.setResponsibleDept(dept);
		defect.setResponsibleLine(line);
		defect.setResponsibleZone(zone);
		if(iqsCategory!=null)
			defect.setIqsCategoryName(iqsCategory);
		if(iqsItemName!=null)
			defect.setIqsItemName(iqsItemName);
		if (regressionCode != null)
			defect.setRegressionCode(regressionCode);
		defect.setEngineFiringFlag(firingFlag);
		if (lockMode != -99) {
			defect.setLockMode(lockMode);
		}
		if (invRepairTime != -99) {
			defect.setOnlineRepairTime(invRepairTime);
		}
		if (onlineRepairTime != -99) {
			defect.setOnlineRepairTime(onlineRepairTime);
		}
		return defect;
	}

	public void clickMoveUpCombination() {

		setErrorMessage("");
		getUpDrawButton().setEnabled(false);
		int[] selectedCombination = getCombinationObjectTablePane().getTable().getSelectedRows();
		int j = 0;
		for (int i = selectedCombination.length - 1; i >= 0; i--) {
			j = selectedCombination[i];
			getCombinationObjectTablePane().getItems().remove(j);
		}
		getCombinationObjectTablePane().reloadData(getCombinationObjectTablePane().getItems());		
		getCombinationObjectTablePane().clearSelection();
	}

	public void clickSaveCombinationButton() {
		setErrorMessage("");
		if (getCombinationObjectTablePane().getSelectedItems().size() <= 0) {
			setErrorMessage("Some combinations have invalid data!");
			return;
		}
		List<DefectDescription> defectDescList =getCombinationObjectTablePane().getSelectedItems();
		List<String> errorMsgs = new ArrayList<String>();
		for (int i = 0; i < defectDescList.size(); i++) {
			DefectDescription defectDesc = (DefectDescription) defectDescList.get(i);
			if (StringUtils.isEmpty(defectDesc.getResponsibleDept())) {
				errorMsgs.add("Responsible Department is required.");
			}
			if (StringUtils.isEmpty(defectDesc.getResponsibleLine())) {
				errorMsgs.add("Responsible Line is required.");
			}			
			if (StringUtils.isEmpty(defectDesc.getResponsibleZone())) {
				errorMsgs.add("Responsible Zone is required.");
			}
			if (StringUtils.isEmpty(defectDesc.getIqsCategoryName())) {
				errorMsgs.add("IQS Category is required.");
			}
			if (StringUtils.isEmpty(defectDesc.getIqsItemName())) {
				errorMsgs.add("IQS Item is required.");
			}
			if (StringUtils.isEmpty(defectDesc.getRegressionCode())) {
				errorMsgs.add("Regression Code is required.");
			}
			if (!errorMsgs.isEmpty()) {
				getCombinationObjectTablePane().getTable().setRowSelectionInterval(i,i);
				break;
			}
		}
		if (errorMsgs.size() > 0) {
			StringBuilder builder = new StringBuilder();
			for (String str : errorMsgs) {
				if (builder.length() > 0) {
					builder.append("\n");
				}
				builder.append(str);
			}
			String msg = errorMsgs.toString();
			JOptionPane.showMessageDialog(this, builder.toString(), "Missing Required Data:"+msg, JOptionPane.ERROR_MESSAGE);
			return;
		}		
		int confirm =JOptionPane.showConfirmDialog(this,"All selected combinations in the table will be inserted/updated to database. Are you sure?","Confirmation",JOptionPane.YES_NO_OPTION);
		if (confirm != 0) { 
			return;
		}
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		getDao(DefectDescriptionDao.class).saveAll(defectDescList);
		getCombinationObjectTablePane().removeData();
		getUpDrawButton().setEnabled(false);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	private DrawnButton getDownDrawButton() {
		if (downDrawButton == null) {
			downDrawButton = new DrawnButton();
			downDrawButton.setButtomMode(1);
			downDrawButton.setBounds(321, 248, 49, 50);
			downDrawButton.setEnabled(false);
		}
		return downDrawButton;
	}

	private DrawnButton getUpDrawButton() {
		if (upDrawButton == null) {
			upDrawButton = new DrawnButton();
			upDrawButton.setBounds(383, 248, 49, 50);
			upDrawButton.setEnabled(false);
		}
		return upDrawButton;
	}

	private JButton getDeleteCombinationButton() {
		if (deleteCombButton == null) {
			deleteCombButton = new JButton();
			deleteCombButton.setFont(new java.awt.Font("dialog", 0, 18));
			deleteCombButton.setText("Delete Combinations");
			deleteCombButton.setBounds(459, 574, 218, 25);
		}
		return deleteCombButton;
	}

	private JButton getSaveCombinationButton() {
		if (saveCombButton == null) {
			saveCombButton = new JButton();
			saveCombButton.setFont(new java.awt.Font("dialog", 0, 18));
			saveCombButton.setText("Save Combinations");
			saveCombButton.setBounds(194, 575, 232, 25);
		}
		return saveCombButton;
	}

	private JComboBox getIqsCategoryItemComboBox() {
		if (iqsCategoryItemComboBox == null) {
			iqsCategoryItemComboBox = new JComboBox();
			iqsCategoryItemComboBox.setBounds(170, 70, 120, 25);
			iqsCategoryItemComboBox.setFont(new java.awt.Font("dialog", 0, 14));
			iqsCategoryItemComboBox.setBackground(java.awt.Color.white);
			iqsCategoryItemComboBox.setForeground(java.awt.Color.black);
		}
		return iqsCategoryItemComboBox;
	}

	private JComboBox getDeptComboBox() {
		if (deptComboBox == null) {
			deptComboBox = new JComboBox();
			deptComboBox.setFont(new java.awt.Font("dialog", 0, 14));
			deptComboBox.setBackground(java.awt.Color.white);
			deptComboBox.setBounds(170, 10, 120, 25);
			deptComboBox.setForeground(java.awt.Color.black);
			deptComboBox.setRenderer(new PropertyComboBoxRenderer<Division>(Division.class, "divisionName"));
		}
		return deptComboBox;
	}

	private JComboBox getLineComboBox() {
		if (lineComboBox == null) {
			lineComboBox = new JComboBox();
			lineComboBox.setFont(new java.awt.Font("dialog", 0, 14));
			lineComboBox.setBackground(java.awt.Color.white);
			lineComboBox.setSize(100, 25);
			lineComboBox.setForeground(java.awt.Color.black);
			lineComboBox.setRenderer(new PropertyComboBoxRenderer<Line>(Line.class, "lineName"));
		}
		return lineComboBox;
	}		

	private JComboBox getZoneComboBox() {
		if (zoneComboBox == null) {
			zoneComboBox = new JComboBox();
			zoneComboBox.setFont(new java.awt.Font("dialog", 0, 14));
			zoneComboBox.setBackground(java.awt.Color.white);
			zoneComboBox.setBounds(170, 40, 120, 25);
			zoneComboBox.setForeground(java.awt.Color.black);
			zoneComboBox.setRenderer(new PropertyComboBoxRenderer<Zone>(Zone.class, "zoneName"));
		}
		return zoneComboBox;
	}	

	private JComboBox getPartGroupComboBox() {
		if (partGroupComboBox == null) {
			partGroupComboBox = new JComboBox();
			partGroupComboBox.setFont(new java.awt.Font("dialog", 0, 14));
			partGroupComboBox.setBackground(java.awt.Color.white);
			partGroupComboBox.setBounds(130, 10, 180, 25);
			partGroupComboBox.setForeground(java.awt.Color.black);
		}
		return partGroupComboBox;
	}

	private JLabel getPartGroupLabel() {
		if (partGroupLabel == null) {
			partGroupLabel = new JLabel();
			partGroupLabel.setFont(new java.awt.Font("dialog", 0, 18));
			partGroupLabel.setText("Part Group");
			partGroupLabel.setBounds(22, 10, 104, 20);
		}
		return partGroupLabel;
	}

	private JLabel getJLabelCategoryItem() {
		if (iqsCategortItemLabel == null) {
			iqsCategortItemLabel = new JLabel();
			iqsCategortItemLabel.setBounds(1, 70, 140, 20);
			iqsCategortItemLabel.setFont(new java.awt.Font("dialog", 0, 18));
			iqsCategortItemLabel.setText("IQS Cat. / Item");
		}
		return iqsCategortItemLabel;
	}

	private JLabel getJLabelDept() {
		if (deptLabel == null) {
			deptLabel = new JLabel();
			deptLabel.setFont(new java.awt.Font("dialog", 0, 18));
			deptLabel.setText("Resp Dept");
			deptLabel.setBounds(1, 10, 140, 20);
		}
		return deptLabel;
	}

	private JLabel getLineLabel() {
		if (lineLabel == null) {
			lineLabel = new JLabel();
			lineLabel.setFont(new java.awt.Font("dialog", Font.BOLD, 16));
			lineLabel.setText("Resp Line");
			lineLabel.setSize(115, 20);
		}
		return lineLabel;
	}		

	private JLabel getNoticeLabel() {
		if (noticeLabel == null) {
			noticeLabel = new JLabel();
			noticeLabel.setFont(new java.awt.Font("dialog", 1, 18));
			noticeLabel.setText("");
			noticeLabel.setBounds(20, 624, 890, 24);
			noticeLabel.setForeground(java.awt.Color.blue);
		}
		return noticeLabel;
	}

	private JLabel getJLabelRegressionCode() {
		if (regressionCodeLabel == null) {
			regressionCodeLabel = new JLabel();
			regressionCodeLabel.setBounds(1, 100, 140, 20);
			regressionCodeLabel.setFont(new java.awt.Font("dialog", 0, 18));
			regressionCodeLabel.setText("Regression Code");
		}
		return regressionCodeLabel;
	}

	private JLabel getJLabelZone() {
		if (zoneLabel == null) {
			zoneLabel = new JLabel();
			zoneLabel.setFont(new java.awt.Font("dialog", 0, 18));
			zoneLabel.setText("Resp Zone");
			zoneLabel.setBounds(1, 40, 140, 20);
		}
		return zoneLabel;
	}

	private JPanel getDefectDescSidePanel() {
		if (defectDescSidePanel == null) {

			defectDescSidePanel = new JPanel();
			defectDescSidePanel.setBorder(new CompoundBorder());
			defectDescSidePanel.setLayout(null);
			defectDescSidePanel.setSize(310, 260);;
			defectDescSidePanel.setLocation(700, 40);
			JPanel labelPanel = new JPanel();
			JPanel comboBoxPanel = new JPanel();
			labelPanel.setLayout(new GridLayout(10, 1, 3, 3));
			comboBoxPanel.setLayout(new GridLayout(10, 1, 3, 3));
			defectDescSidePanel.add(labelPanel);
			defectDescSidePanel.add(comboBoxPanel);
			labelPanel.setSize(160, 260);
			labelPanel.setLocation(0, 0);
			comboBoxPanel.setSize(135, 260);
			comboBoxPanel.setLocation(165, 0);
			labelPanel.add(getJLabelDept(), new GridBagConstraints());
			comboBoxPanel.add(getDeptComboBox());
			labelPanel.add(getLineLabel());
			comboBoxPanel.add(getLineComboBox());
			labelPanel.add(getJLabelZone());
			comboBoxPanel.add(getZoneComboBox());
			labelPanel.add(getJLabelCategoryItem());
			comboBoxPanel.add(getIqsCategoryItemComboBox());
			labelPanel.add(getJLabelRegressionCode());
			comboBoxPanel.add(getRegressionComboBox());
			labelPanel.add(getJLabelLockMode(), getJLabelLockMode());
			comboBoxPanel.add(getJTextFieldLockMode());
			labelPanel.add(getJLabelInvRepairTime());
			comboBoxPanel.add(getJTextFieldInvRepairTime());
			labelPanel.add(getJLabelOnlineRepairTime());
			comboBoxPanel.add(getJTextFieldOnlineRepairTime());
			labelPanel.add(getFiringFlagLabel());
			comboBoxPanel.add(getFiringFlagCheckBox());
			labelPanel.add(new JLabel(""));
			comboBoxPanel.add(getAssignButton());
		}
		return defectDescSidePanel;
	}

	private JScrollPane getCombinationScrollPane() {
		if (combinationScrollPane == null) {
			combinationScrollPane = new JScrollPane();
			combinationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			combinationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			combinationScrollPane.setBounds(10, 306, 996, 257);
			combinationScrollPane.setViewportView(getCombinationObjectTablePane());
		}
		return combinationScrollPane;
	}

	private JScrollPane getDefectSecPartScrollPane() {
		if (defectSecPartScrollPane == null) {
			defectSecPartScrollPane = new JScrollPane();
			defectSecPartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			defectSecPartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			defectSecPartScrollPane.setBounds(494, 41, 190, 200);
			defectSecPartScrollPane.setViewportView(getDefectSecPartObjectTablePane());
		}
		return defectSecPartScrollPane;
	}

	private JScrollPane getPartLocationScrollPane() {
		if (partLocationScrollPane == null) {
			partLocationScrollPane = new JScrollPane();
			partLocationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			partLocationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			partLocationScrollPane.setBounds(10, 41, 240, 200);
			partLocationScrollPane.setViewportView(getPartLocationObjectTablePane());
		}
		return partLocationScrollPane;
	}

	private JScrollPane getTwoPartLocationScrollPane() {
		if (twoPartLocationScrollPane == null) {
			twoPartLocationScrollPane = new JScrollPane();
			twoPartLocationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			twoPartLocationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			twoPartLocationScrollPane.setBounds(252, 41, 240, 200);
			twoPartLocationScrollPane.setViewportView(getTwoPartLocObjectTablePane());
		}
		return twoPartLocationScrollPane;
	}

	protected ObjectTablePane<InspectionTwoPartDescription> getPartLocationObjectTablePane() {
		if(partLocationObjectTablePane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Part", "inspectionPartName").put("Location", "inspectionPartLocationName");		
			partLocationObjectTablePane = new ObjectTablePane<InspectionTwoPartDescription>(columnMappings.get(),true);
			partLocationObjectTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			partLocationObjectTablePane.getTable().getColumnModel().getColumn(0).setMaxWidth(110);
			partLocationObjectTablePane.getTable().getColumnModel().getColumn(1).setMaxWidth(110);
		}
		return partLocationObjectTablePane;
	}

	protected ObjectTablePane<InspectionTwoPartDescription> getTwoPartLocObjectTablePane() {
		if(twoPartLocationObjectTablePane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("TwoPairPart", "twoPartPairPart").put("TwoPartLoc", "twoPartPairLocation");	
			twoPartLocationObjectTablePane = new ObjectTablePane<InspectionTwoPartDescription>(columnMappings.get(),true);
			twoPartLocationObjectTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			twoPartLocationObjectTablePane.getTable().getColumnModel().getColumn(0).setMaxWidth(110);
			twoPartLocationObjectTablePane.getTable().getColumnModel().getColumn(1).setMaxWidth(110);		
		}
		return twoPartLocationObjectTablePane;
	}

	protected ObjectTablePane<DefectTypeDescription> getDefectSecPartObjectTablePane() {
		if(defectSecPartObjectTablePane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("#", "row").put("Defect", "defectTypeName").put("2nd Part", "secondaryPartName");
			defectSecPartObjectTablePane = new ObjectTablePane<DefectTypeDescription>(columnMappings.get(),true);
			defectSecPartObjectTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			defectSecPartObjectTablePane.getTable().getColumnModel().getColumn(0).setMaxWidth(30);
			defectSecPartObjectTablePane.getTable().getColumnModel().getColumn(1).setMaxWidth(80);
			defectSecPartObjectTablePane.getTable().getColumnModel().getColumn(2).setMaxWidth(80);			
		}
		return defectSecPartObjectTablePane;
	}

	protected ObjectTablePane<DefectDescription> getCombinationObjectTablePane() {
		if(combinationObjectTablePane==null)
		{
			int width = getDefectSecPartObjectTablePane().getX() + getDefectSecPartObjectTablePane().getWidth() - getPartLocationObjectTablePane().getX();
			ColumnMappings columnMappings = ColumnMappings.with("#", "row")
					.put("Part Group", "partGroupName")
					.put("Part", "inspectionPartName")
					.put("Part Location", "inspectionPartLocationName")
					.put("Two Pair Part", "twoPartPairPart")
					.put("Two Part Loc", "twoPartPairLocation")
					.put("Defect", "defectTypeName")
					.put("2nd Part", "secondaryPartName")
					.put("Dept", "responsibleDept")
					.put("Line", "responsibleLine")
					.put("Zone", "responsibleZone")
					.put("Category", "iqsCategoryName")
					.put("Item","iqsItemName")
					.put("Regression", "regressionCode")
					.put("Firing Flag", "engineFiringFlag")
					.put("Lock Mode", "lockMode")
			        .put("Inv Repair time", "inventoryRepairTime")
			        .put("Online Repair time", "onlineRepairTime");;
			combinationObjectTablePane = new ObjectTablePane<DefectDescription>(columnMappings.get(),true);
			combinationObjectTablePane.setSize(width, 320);
			combinationObjectTablePane.setBounds(0, 0, 1027, 158);
			combinationObjectTablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			combinationObjectTablePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			combinationObjectTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return combinationObjectTablePane;
	}


	private void addListeners()  {
		getPartGroupComboBox().addActionListener(this);
		getDefectGroupComboBox().addActionListener(this);
		getDeptComboBox().addActionListener(this);
		getZoneComboBox().addActionListener(this);
		getIqsCategoryItemComboBox().addActionListener(this);
		getRegressionComboBox().addActionListener(this);
		getDownDrawButton().addActionListener(this);
		getUpDrawButton().addActionListener(this);
		getAssignButton().addActionListener(this);
		getSaveCombinationButton().addActionListener(this);
		getDeleteCombinationButton().addActionListener(this);
		getDeptComboBox().addActionListener(this);
		getLineComboBox().addActionListener(this);
		getPartLocationObjectTablePane().getTable().getSelectionModel().addListSelectionListener(this);
		getTwoPartLocObjectTablePane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectSecPartObjectTablePane().getTable().getSelectionModel().addListSelectionListener(this);
		getCombinationObjectTablePane().getTable().getSelectionModel().addListSelectionListener(this);		
	}


	private void initialize() {
		try {
			setName("PartDefectCombinationFrame");
			setSize(1024, 768);
			setLayout(null);
			add(getPartGroupLabel());
			add(getPartGroupComboBox());
			add(getDefectGroupLabel());
			add(getDefectGroupComboBox());
			add(getDefectDescSidePanel());
			add(getSaveCombinationButton());
			add(getDeleteCombinationButton());
			add(getDefectSecPartScrollPane());
			add(getPartLocationScrollPane());
			add(getTwoPartLocationScrollPane());
			add(getCombinationScrollPane());
			add(getDownDrawButton());
			add(getUpDrawButton());
			add(getNoticeLabel());
			addListeners();
			startFrame();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void resetComponent() {
		getUpDrawButton().setEnabled(false);
		getDownDrawButton().setEnabled(false);
		setErrorMessage("");
	}

	public void clearMessage() {
		setErrorMessage("");
	}

	public void selectPartGroup() {
		try {
			setErrorMessage("");
			getDownDrawButton().setEnabled(false);
			if (getPartGroupComboBox().getSelectedIndex() == -1)
				return;
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			getPartLocationObjectTablePane().removeData();
			getTwoPartLocObjectTablePane().removeData();
			getCombinationObjectTablePane().removeData();           
			List<InspectionTwoPartDescription> inspTwoPartDescList=getDao(InspectionTwoPartDescriptionDao.class).findByPartGroupName(((String)getPartGroupComboBox().getSelectedItem()).trim());
			getPartLocationObjectTablePane().reloadData(inspTwoPartDescList);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception e) {
			handleException(e);
		}
	}


	public void selectDefectGroup() {
		try {
			setErrorMessage("");
			getDownDrawButton().setEnabled(false);
			getDefectSecPartObjectTablePane().removeData();
			if (getDefectGroupComboBox().getSelectedIndex() == -1)
				return;
			setErrorMessage("");
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			DefectTypeDescription criteria = new DefectTypeDescription();
			criteria.setDefectGroupName(getDefectGroupComboBox().getSelectedItem().toString().trim());
			List<DefectTypeDescription> defectTypeDescList = getController().selectDefectTypeDescriptions(criteria);		
			Collections.sort(defectTypeDescList, new PropertyComparator<DefectTypeDescription>(DefectTypeDescription.class, "defectTypeName", "secondaryPartName"));
			getDefectSecPartObjectTablePane().reloadData(defectTypeDescList);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception e) {
			handleException(e);
		}
	}


	public void clickAssignButton() {
		try {
			setErrorMessage("");
			if (getCombinationObjectTablePane().getSelectedItems().size() <= 0) {
				setErrorMessage("No valid combinations selected");
				return;
			}
			fetchDefectDescSidePaneData();
			for (DefectDescription defectDesc:getCombinationObjectTablePane().getSelectedItems()) {
				defectDesc.setResponsibleDept(dept);
				defectDesc.setResponsibleLine(line);
				defectDesc.setResponsibleZone(zone);
				if (iqsCategory != null) {
					defectDesc.setIqsCategoryName(iqsCategory);
				}
				if (iqsItemName != null) {
					defectDesc.setIqsItemName(iqsItemName);
				}
				if (regressionCode != null) {
					defectDesc.setRegressionCode(regressionCode);
				}
				if (lockMode != -99) {
					defectDesc.setLockMode(lockMode);
				}
				if (invRepairTime != -99) {
					defectDesc.setInventoryRepairTime(invRepairTime);
				}
				if (onlineRepairTime != -99) {
					defectDesc.setOnlineRepairTime(onlineRepairTime);
				}
				defectDesc.setEngineFiringFlag(firingFlag);
			}
			getCombinationObjectTablePane().reloadData(getCombinationObjectTablePane().getItems());	
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void fetchDefectDescSidePaneData() {
		dept = null;
		line = null;
		zone = null;
		regressionCode = null;
		iqsCategory = null;
		iqsItemName = null;
		lockMode = -99; 
		invRepairTime = -99; 
		onlineRepairTime = -99; 
		firingFlag = false;
		if(getDeptComboBox().getSelectedIndex()>0)
			dept = getDeptComboBox().getSelectedItem()==null?"":((Division)getDeptComboBox().getSelectedItem()).getDivisionName();
		if(getZoneComboBox().getSelectedIndex()>0)
			zone = getZoneComboBox().getSelectedItem()==null?"":((Zone)getZoneComboBox().getSelectedItem()).getZoneName();
		if(getLineComboBox().getSelectedIndex()>0)
			line = getLineComboBox().getSelectedItem()==null?"":((Line) getLineComboBox().getSelectedItem()).getLineName();
		if (getIqsCategoryItemComboBox().getSelectedIndex() != 0) {
			String categoryItem = (String)getIqsCategoryItemComboBox().getSelectedItem();
			int index = categoryItem.indexOf("|");
			iqsCategory = categoryItem.substring(0, index - 1);
			iqsItemName = categoryItem.substring(index + 2);
		}
		if (getRegressionComboBox().getSelectedIndex() != 0) {
			regressionCode = ((Regression)getRegressionComboBox().getSelectedItem()).getRegressionCode();
		}			
		String lockModeString = getJTextFieldLockMode().getText();
		if (lockModeString != null&& ((lockModeString.trim()).length() > 0 && (lockModeString.trim()).length() < 3)) {
			lockMode = new Short(lockModeString.trim()).shortValue();
			if (lockMode < 0) {
				lockMode = -99; 
			}
		}
		String invRepairTimeString = getJTextFieldInvRepairTime().getText();
		if (invRepairTimeString != null && (invRepairTimeString.trim()).length() > 0) {
			invRepairTime = new Integer(invRepairTimeString.trim()).intValue();
			if (invRepairTime < 0) {
				invRepairTime = -99; 
			}
		}
		String onlineRepairTimeString = getJTextFieldOnlineRepairTime().getText();
		if (onlineRepairTimeString != null && (onlineRepairTimeString.trim()).length() > 0) {
			onlineRepairTime = new Integer(onlineRepairTimeString.trim()).intValue();
			if (onlineRepairTime < 0) {
				onlineRepairTime = -99; 
			}
		}
		firingFlag = getFiringFlagCheckBox().isSelected();
	}


	public void startFrame() {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			loadData();
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception e) {
			resetComponent();
			handleException(e);
		}
	}

	private JLabel getDefectGroupLabel() {
		if (defectGroupLabel == null) {
			defectGroupLabel = new JLabel();
			defectGroupLabel.setFont(new java.awt.Font("dialog", 0, 18));
			defectGroupLabel.setText("Defect Group");
			defectGroupLabel.setBounds(380, 10, 120, 20);
			partGroupLabel.setForeground(java.awt.Color.black);
		}
		return defectGroupLabel;
	}

	private JComboBox getDefectGroupComboBox() {
		if (defectGroupComboBox == null) {
			defectGroupComboBox = new JComboBox();
			defectGroupComboBox.setBounds(500, 10, 180, 25);
			defectGroupComboBox.setFont(new java.awt.Font("dialog", 0, 14));
			defectGroupComboBox.setBackground(java.awt.Color.white);
			defectGroupComboBox.setForeground(java.awt.Color.black);
		}
		return defectGroupComboBox;
	}

	private JComboBox getRegressionComboBox() {
		if (regressionCodeComboBox == null) {
			regressionCodeComboBox = new JComboBox();
			regressionCodeComboBox.setBounds(170, 100, 120, 25);
			regressionCodeComboBox.setFont(new java.awt.Font("dialog", 0, 14));
			regressionCodeComboBox.setBackground(java.awt.Color.white);
			regressionCodeComboBox.setForeground(java.awt.Color.black);
			regressionCodeComboBox.setRenderer(new PropertyComboBoxRenderer<Regression>(Regression.class, "regressionCode"));
		}
		return regressionCodeComboBox;
	}

	private JLabel getJLabelLockMode() {
		if (lockModeLabel == null) {
			lockModeLabel = new JLabel();
			lockModeLabel.setBounds(1, 130, 140, 20);
			lockModeLabel.setText("Lock Mode");
			lockModeLabel.setFont(new java.awt.Font("dialog", 0, 18));
		}
		return lockModeLabel;
	}

	private JLabel getJLabelInvRepairTime() {
		if (invRepairTimeLabel == null) {
			invRepairTimeLabel = new JLabel();
			invRepairTimeLabel.setBounds(1, 160, 140, 20);
			invRepairTimeLabel.setText("Inv Repair Time");
			invRepairTimeLabel.setFont(new java.awt.Font("dialog", 0, 18));
		}
		return invRepairTimeLabel;
	}

	private JLabel getJLabelOnlineRepairTime() {
		if (onlineRepairTimeLabel == null) {
			onlineRepairTimeLabel = new JLabel();
			onlineRepairTimeLabel.setBounds(1, 190, 160, 20);
			onlineRepairTimeLabel.setText("Online Repair Time");
			onlineRepairTimeLabel.setFont(new java.awt.Font("dialog", 0, 18));
		}
		return onlineRepairTimeLabel;
	}

	private JLabel getFiringFlagLabel() {
		if (firingFlagLabel == null) {
			firingFlagLabel = new JLabel();
			firingFlagLabel.setBounds(1, 190, 115, 20);
			firingFlagLabel.setText("Firing Flag");
			firingFlagLabel.setFont(new java.awt.Font("dialog", Font.BOLD, 16));
		}
		return firingFlagLabel;
	}

	private JCheckBox getFiringFlagCheckBox() {
		if (firingFlagCheckBox == null) {
			firingFlagCheckBox = new JCheckBox();
			firingFlagCheckBox.setSelected(false);
		}
		return firingFlagCheckBox;
	}	

	private JTextField getJTextFieldLockMode() {
		if (lockModeTextField == null) {
			lockModeTextField = new JTextField();
			lockModeTextField.setBounds(170, 130, 120, 25);
			lockModeTextField.setText("0");
		}
		return lockModeTextField;
	}

	private JTextField getJTextFieldInvRepairTime() {
		if (invRepairTimeTextField == null) {
			invRepairTimeTextField = new JTextField();
			invRepairTimeTextField.setBounds(170, 160, 120, 25);
			invRepairTimeTextField.setText("0");
		}
		return invRepairTimeTextField;
	}

	private JTextField getJTextFieldOnlineRepairTime() {
		if (onlineRepairTimeTextField == null) {
			onlineRepairTimeTextField = new JTextField();
			onlineRepairTimeTextField.setBounds(170, 190, 120, 25);
			onlineRepairTimeTextField.setText("0");
		}
		return onlineRepairTimeTextField;
	}

	private JButton getAssignButton() {
		if (assignButton == null) {
			try {
				assignButton = new JButton();
				assignButton.setFont(new java.awt.Font("dialog", 0, 18));
				assignButton.setText("Assign");
				assignButton.setBounds(130, 228, 100, 25);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return assignButton;
	}

	private void enableDisableDownButton() {
		if (getPartLocationObjectTablePane().getSelectedItems().size() > 0
				&& getTwoPartLocObjectTablePane().getSelectedItems().size() > 0
				&& getDefectSecPartObjectTablePane().getSelectedItems().size() > 0) {
			getDownDrawButton().setEnabled(true);
		} else {
			getDownDrawButton().setEnabled(false);
		}
	}

	private void enableDisableUpButton() {
		if (getCombinationObjectTablePane().getSelectedItems().size() > 0) {
			getUpDrawButton().setEnabled(true);
		} else {
			getUpDrawButton().setEnabled(false);
		}
	}

	private void clickPartLocationTable() {
		if (getPartLocationObjectTablePane().getItems().size() > 0) {
			InspectionTwoPartDescription selectedRow = getPartLocationObjectTablePane().getSelectedItem();
			if(selectedRow==null)
				return;
			setErrorMessage("");
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			List<InspectionTwoPartDescription> inspTwoPartDescList=getDao(InspectionTwoPartDescriptionDao.class).findByPartGroupInspLocPartName(((String)getPartGroupComboBox().getSelectedItem()).trim(), selectedRow.getId().getInspectionPartLocationName(), selectedRow.getId().getInspectionPartName());
			getTwoPartLocObjectTablePane().reloadData(inspTwoPartDescList);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void selectDept() {		

		Division division = (Division)getDeptComboBox().getSelectedItem();
		if(division == null) return;
		List<Line> lines = null;
		List<Zone> zones = null;
		if (division.getDivisionName().trim().equals(SELECT_DEPARTMENT)) {
			lines = getController().selectLines();
			zones = getController().selectZones();
		} else {
			lines = getController().selectLines(division);
			zones = getController().selectZones(division.getDivisionId());
		}
		Collections.sort(lines, new PropertyComparator<Line>(Line.class, "lineName"));
		Collections.sort(zones, new PropertyComparator<Zone>(Zone.class, "zoneName"));
		lines.add(0, createDummyLine());
		ComboBoxUtils.loadComboBox(getLineComboBox(), lines);
		zones.add(0, createDummyZone());
		ComboBoxUtils.loadComboBox(getZoneComboBox(), zones);
	}

	private void loadData() {
		getPartGroupComboBox().removeAllItems();		
		List<PartGroup> partGroupList=getClientModel().getPartGroups();
		Collections.sort(partGroupList, new PropertyComparator<PartGroup>(PartGroup.class, "partGroupName"));
		for(PartGroup partGroup:partGroupList)
		{
			if (!(partGroup.getPartGroupName().indexOf(TWO_PART_PAIR) == -1))
			    getPartGroupComboBox().addItem(partGroup.getPartGroupName());
		}
		getPartGroupComboBox().setSelectedIndex(-1);

		getDefectGroupComboBox().removeAllItems();

		List<Object[]> defectGroupList=getDao(DefectTypeDescriptionDao.class).findAllTwoPartPairDefectGroups();
		for (Object[] defectGroup:defectGroupList)
		{
			getDefectGroupComboBox().addItem(defectGroup[0].toString());	
		}
		getDefectGroupComboBox().setSelectedIndex(-1);		
		getDeptComboBox().removeAllItems();
		List<Division> departments=getClientModel().getDepartments();
		Collections.sort(departments, new PropertyComparator<Division>(Division.class, "divisionName"));
		departments.add(0, createDummyDivision());
		ComboBoxUtils.loadComboBox(getDeptComboBox(), departments);
		if (departments != null && departments.size() > 0) {
			getDeptComboBox().setSelectedIndex(0);
		}
		getLineComboBox().removeAllItems();
		List<Line> lines = getController().selectLines();
		Collections.sort(lines, new PropertyComparator<Line>(Line.class, "lineName"));
		lines.add(0, createDummyLine());
		ComboBoxUtils.loadComboBox(getLineComboBox(), lines);
		if (lines != null && lines.size() > 0) {
			getLineComboBox().setSelectedIndex(0);
		}
		getRegressionComboBox().removeAllItems();
		List<Regression> regression = getController().selectRegression();
		regression.add(0, createDummyRegression());
		ComboBoxUtils.loadComboBox(getRegressionComboBox(), regression);
		if (regression != null && regression.size() > 0) {
			getRegressionComboBox().setSelectedIndex(0);
		}
		List<Zone> zones = getController().selectZones();
		Collections.sort(zones, new PropertyComparator<Zone>(Zone.class, "zoneName"));
		zones.add(0, createDummyZone());
		ComboBoxUtils.loadComboBox(getZoneComboBox(), zones);
		if (zones != null && zones.size() > 0) {
			getZoneComboBox().setSelectedIndex(0);
		}     
		getIqsCategoryItemComboBox().removeAllItems();
		getIqsCategoryItemComboBox().addItem(SELECT_IQS);
		List<Iqs> iqsList = getController().selectDistinctIqs();
		for (Iqs iqs:iqsList) {
			String categoryItemString = iqs.getIqsCategoryName() + " | " + iqs.getIqsItemName();
			getIqsCategoryItemComboBox().addItem(categoryItemString);
		}
		if (iqsList != null && iqsList.size() > 0) {
			getIqsCategoryItemComboBox().setSelectedIndex(0);
		}
		resetComponent();
		getPartLocationObjectTablePane().removeData();
		getTwoPartLocObjectTablePane().removeData();
		getDefectSecPartObjectTablePane().removeData();
		getCombinationObjectTablePane().removeData();
	}

	protected Line createDummyLine() {
		Line dummy = new Line();
		dummy.setLineName(SELECT_LINE);
		return dummy;
	}

	protected Zone createDummyZone() {
		Zone dummy = new Zone();
		dummy.setZoneName(SELECT_ZONE);
		return dummy;
	}

	protected Division createDummyDivision() {
		Division dummy = new Division();
		dummy.setDivisionName(SELECT_DEPARTMENT);
		return dummy;
	}

	protected Regression createDummyRegression() {
		Regression dummy = new Regression();
		dummy.setRegressionCode(SELECT_REGRESSION);
		return dummy;
	}

}
