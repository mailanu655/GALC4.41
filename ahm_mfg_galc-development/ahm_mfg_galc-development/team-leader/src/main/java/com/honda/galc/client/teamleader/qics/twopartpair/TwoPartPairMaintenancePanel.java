package com.honda.galc.client.teamleader.qics.twopartpair;

import static com.honda.galc.service.ServiceFactory.getDao;



import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dao.qics.ImageSectionDao;
import com.honda.galc.dao.qics.InspectionPartDescriptionDao;
import com.honda.galc.dao.qics.InspectionTwoPartDescriptionDao;
import com.honda.galc.dao.qics.PartGroupDao;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.InspectionTwoPartDescription;
import com.honda.galc.entity.qics.InspectionTwoPartDescriptionId;
import com.honda.galc.entity.qics.PartGroup;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 19, 2015
 */
public class TwoPartPairMaintenancePanel extends QicsMaintenanceTabbedPanel implements  ListSelectionListener  {

	private static final long serialVersionUID = 1L;	
	private JButton deleteCombinationsBtn = null;
	private JButton saveUpdateCombinationsBtn = null;	
	private JLabel partGroupLabel = null;	
	private JScrollPane partScrollPane = null;	
	private JScrollPane selPartScrollPane = null;	
	private JScrollPane twoPartPairScrollPane = null;	
	private JScrollPane selTwoPartPairScrollPane = null;	
	private JComboBox partGroupComboBox = null;	
	private ObjectTablePane<InspectionPartDescription> partObjectPane = null;	
	private ObjectTablePane<InspectionPartDescription> selectedPartObjectPane = null;	
	private ObjectTablePane<InspectionPartDescription> twoPartPairObjectPane = null;	
	private ObjectTablePane<InspectionPartDescription> selecteTwoPartPairObjectPane = null;	
	private DrawnButton downButtonForPart = null;	
	private DrawnButton upButtonForPart = null;	
	private DrawnButton downButtonForTwoPartPair = null;	
	private DrawnButton upButtonForTwoPartPair = null;	
	private JLabel noticeLabel = null;

	public TwoPartPairMaintenancePanel(QicsMaintenanceFrame mainWindow) {
		super("Two Part Pair Maintenance",KeyEvent.VK_T);
		setMainWindow(mainWindow);
		initialize();
	}

	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			isInitialized = true;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getSaveUpdateCombinationsBtn()) 
			clickSaveUpdate();
		else if (e.getSource() == getDeleteCombinationsBtn()) 
			clickDelete();
		else if (e.getSource() == getDownBtnForPart()) 
			clickMoveDownPart();
		else if (e.getSource() == getUpBtnForPart()) 
			clickMoveUpPart();
		else if (e.getSource() == getDownBtnforTwoPartPair()) 
			clickMoveDownTwoPartPair();
		else if (e.getSource() == getUpBtnForTwoPartPair()) 
			clickMoveUpTwoPartPair();
		else if (e.getSource() == getPartGroupComboBox()) 
			selectPartGroup();
	}

	@Override
	public void deselected(ListSelectionModel model) {		
	}

	@Override
	public void selected(ListSelectionModel model) {
		if(model.equals(getPartObjectPane().getTable().getSelectionModel())) 
			selectUnselectPart();
		else if(model.equals(getTwoPartPairObjectPane().getTable().getSelectionModel())) 
			selectUnselectTwoPartPair();
		else if(model.equals(getSelPartObjectPane().getTable().getSelectionModel())) 
			selectUnselectSelPart();
		else if(model.equals(getSelTwoPartPairObjectPane().getTable().getSelectionModel())) 
			selectUnselectSelTwoPartPair();
	}



	class WindowIconifiedEventHandler extends WindowAdapter {																					
		public void windowIconified(WindowEvent e) {																				
			TwoPartPairMaintenancePanel.this.requestFocus();																			
		};																				
	}

	public void clearMessage() {
		setErrorMessage("");
	}

	public void clickDelete() {
		clearMessage();
		if (getSelTwoPartPairObjectPane().getItems().size()==0) 
			return;
		String		tmpPart			=	getSelPartObjectPane().getItems().get(0).getInspectionPartName();
		String		tmpPartLocation	=	getSelPartObjectPane().getItems().get(0).getInspectionPartLocationName();
		String[]	tmpTPP			=	null;
		String[]	tmpTPPL			=	null;
		List<InspectionTwoPartDescription> temp=getDao(InspectionTwoPartDescriptionDao.class).findByPartGroupInspLocPartName(getPartGroupComboBox().getSelectedItem().toString(), tmpPartLocation, tmpPart);
		tmpTPP = new String[temp.size()];
		tmpTPPL = new String[temp.size()];
		for (int i=0; i < temp.size(); i++) {
			InspectionTwoPartDescription temp1 = temp.get(i);
			tmpTPP[i]	=	temp1.getId().getTwoPartPairPart();
			tmpTPPL[i]	=	temp1.getId().getTwoPartPairLocation();
		}				
		String partGroupName = (String)getPartGroupComboBox().getSelectedItem();
		if(!deleteTwoPartPair(tmpPart,tmpPartLocation,tmpTPP,tmpTPPL, partGroupName)) 
		{			
			return;
		}else
		{			
			resetScreen();
		}		
	}

	private boolean deleteTwoPartPair(String tmpPart, String tmpPartLocation,String[] tmpTPP, String[] tmpTPPL, String partGroupName) {
		for(int iLoopCount = 0; iLoopCount < tmpTPP.length; iLoopCount++) {
			String twoPart = tmpTPP[iLoopCount];
			String twoPartPairLocation = tmpTPPL[iLoopCount];
			if(!verifyRelationships(tmpPart, tmpPartLocation,  twoPartPairLocation, partGroupName,  twoPart))
				return false;		
		}
		for (int i=0; i<tmpTPP.length; i++) {
			InspectionTwoPartDescription inspectionTwoPartDescription=new InspectionTwoPartDescription();
			InspectionTwoPartDescriptionId id=new InspectionTwoPartDescriptionId( tmpPart, tmpPartLocation,  tmpTPPL[i], partGroupName,  tmpTPP[i]);
			inspectionTwoPartDescription.setId(id);
			getDao(InspectionTwoPartDescriptionDao.class).remove(inspectionTwoPartDescription);
			logUserAction(REMOVED, inspectionTwoPartDescription);
		}	
		return true;
	}


	private boolean verifyRelationships(String tmpPart, String tmpPartLocation,String twoPartPairLocation, String partGroupName, String twoPart)
	{
		InspectionTwoPartDescription inspTwoPartDesc=getDao(InspectionTwoPartDescriptionDao.class).findByKey(new InspectionTwoPartDescriptionId( tmpPart, tmpPartLocation,  twoPartPairLocation, partGroupName,  twoPart));
		if(inspTwoPartDesc==null) {
			setErrorMessage("Selected two part pair has been already deleted.");
			return false ;
		}
		List<DefectDescription> defectDescList=getDao(DefectDescriptionDao.class).findAllByTwoPartPair(partGroupName, tmpPart, tmpPartLocation,twoPart,twoPartPairLocation);
		if(defectDescList.size() > 0) {
			setErrorMessage("Please remove relation with Part Defect Combination");
			return false;
		}
		List<ImageSection> imageSectionList=getDao(ImageSectionDao.class).findAllByDescriptionIdPartKindFlag(inspTwoPartDesc.getDescriptionId(),(short)1);
		if(imageSectionList.size() > 0) {				
			setErrorMessage("Remove relation with Image Section: ");
			return false;
		}
		return true;
	}
	
	public void clearTableSelections()
	{
		getSelPartObjectPane().clearSelection();
		getSelTwoPartPairObjectPane().clearSelection();
		getPartObjectPane().clearSelection();
		getTwoPartPairObjectPane().clearSelection();
	}

	public void clickMoveDownPart() {
		clearMessage();
		int selectedIndex = getPartObjectPane().getTable().getSelectedRow();
		InspectionPartDescription selItem=getPartObjectPane().getSelectedItem();
		if (getSelPartObjectPane().getItems().size()==0 && selectedIndex != -1) {
			getSelPartObjectPane().getItems().add(getPartObjectPane().getItems().get(selectedIndex));
			getSelPartObjectPane().reloadData(getSelPartObjectPane().getItems());
			getPartObjectPane().getItems().remove(selItem);
			getPartObjectPane().reloadData(getPartObjectPane().getItems());
			getTwoPartPairObjectPane().getItems().remove(selItem);
			getTwoPartPairObjectPane().reloadData(getTwoPartPairObjectPane().getItems());
			getDownBtnForPart().setEnabled(false);				
			List<InspectionTwoPartDescription> inspTwoPartdescList=getDao(InspectionTwoPartDescriptionDao.class).findByPartGroupInspLocPartName(getPartGroupComboBox().getSelectedItem().toString(), selItem.getId().getInspectionPartLocationName(), selItem.getId().getInspectionPartName());		
			for (InspectionTwoPartDescription inspTwoPartDesc:inspTwoPartdescList) {		
				String tempTwoPart =inspTwoPartDesc.getId().getTwoPartPairPart();
				String tempTwoPartLoc = inspTwoPartDesc.getId().getTwoPartPairLocation();	
				List<InspectionPartDescription> tempList=getTwoPartPairObjectPane().getItems();
				for (InspectionPartDescription inspPartDesc:tempList) {
					String tempPart = inspPartDesc.getInspectionPartName().trim();
					String tempLoc = inspPartDesc.getInspectionPartLocationName().trim();
					if ((tempTwoPart.equals(tempPart)) && (tempTwoPartLoc.equals(tempLoc))){
						getSelTwoPartPairObjectPane().getItems().add(inspPartDesc);
						getSelTwoPartPairObjectPane().reloadData(getSelTwoPartPairObjectPane().getItems());
						getTwoPartPairObjectPane().getItems().remove(inspPartDesc);
						getTwoPartPairObjectPane().reloadData(getTwoPartPairObjectPane().getItems());
						break;
					}
				}
			}					
		}
		if (getSelTwoPartPairObjectPane().getItems().size()>0 && selectedIndex != -1) {
			getSaveUpdateCombinationsBtn().setEnabled(true);
			getDeleteCombinationsBtn().setEnabled(true);
		}	
		clearTableSelections();
	}

	public void clickMoveDownTwoPartPair() {
		clearMessage();
		if (getSelPartObjectPane().getItems().size() == 0) {
			return;
		}
		int[] selectedIndex = getTwoPartPairObjectPane().getTable().getSelectedRows();
		for (int i = 0; i < selectedIndex.length; i++) {
			int num = getSelTwoPartPairObjectPane().getItems().size();
			InspectionPartDescription temp = getTwoPartPairObjectPane().getItems().get(selectedIndex[i]-i);
			int insertNum = 0;
			for (int iLoopCount = 0; iLoopCount < num; iLoopCount++) {
				InspectionPartDescription tableData = getSelTwoPartPairObjectPane().getItems().get(iLoopCount);
				insertNum = comparePartTwoPartData(temp, insertNum, iLoopCount,tableData);
			}
			getSelTwoPartPairObjectPane().getItems().add(insertNum, temp);
			getSelTwoPartPairObjectPane().reloadData(getSelTwoPartPairObjectPane().getItems());
			getTwoPartPairObjectPane().getItems().remove(temp);
			getTwoPartPairObjectPane().reloadData(getTwoPartPairObjectPane().getItems());
			if (getTwoPartPairObjectPane().getItems().size() == 0)
				getDownBtnforTwoPartPair().setEnabled(false);
			getSaveUpdateCombinationsBtn().setEnabled(true);
			getDeleteCombinationsBtn().setEnabled(true);
		}
		getDownBtnforTwoPartPair().setEnabled(false);
		clearTableSelections();
	}

	private int comparePartTwoPartData(InspectionPartDescription temp,int insertNum, int iLoopCount, InspectionPartDescription tableData) {
		String selectedData1 = temp.getInspectionPartName();
		String selectedData2 = temp.getInspectionPartLocationName();						
		String columnData1 = tableData.getInspectionPartName();
		String columnData2 = tableData.getInspectionPartLocationName();			
		if (columnData1.compareTo(selectedData1) < 0) {
			insertNum = iLoopCount;
			insertNum++;
		} else if(columnData1.compareTo(selectedData1) == 0) {
			if(columnData2.compareTo(selectedData2) < 0) {
				insertNum = iLoopCount;
				insertNum++;
			}
		}
		return insertNum;
	}

	public void clickMoveUpPart() {

		clearMessage();
		int[] selectedIndex = getSelPartObjectPane().getTable().getSelectedRows();
		InspectionPartDescription selItem=getSelPartObjectPane().getSelectedItem();
		if (selectedIndex.length!=0) {
			int num = getPartObjectPane().getItems().size();
			InspectionPartDescription temp = getSelPartObjectPane().getItems().get(selectedIndex[0]);
			int insertNum = 0;
			for (int iLoopCount = 0; iLoopCount < num; iLoopCount++) {
				InspectionPartDescription tableData =  getPartObjectPane().getItems().get(iLoopCount);
				insertNum = comparePartTwoPartData(temp, insertNum, iLoopCount,tableData);
			}
			getPartObjectPane().getItems().add(insertNum, temp);
			getPartObjectPane().reloadData(getPartObjectPane().getItems());
			getSelPartObjectPane().getItems().remove(selItem);
			getSelPartObjectPane().reloadData(getSelPartObjectPane().getItems());		
			List<InspectionPartDescription> inspectPartDescList=getDao(InspectionPartDescriptionDao.class).findAllByPartGroupName(getPartGroupComboBox().getSelectedItem().toString());
			getTwoPartPairObjectPane().reloadData(inspectPartDescList);		
			getSelTwoPartPairObjectPane().removeData();
			getUpBtnForPart().setEnabled(false);
			resetComponent();
		}
		clearTableSelections();
	}

	public void clickMoveUpTwoPartPair() {

		clearMessage();
		int[] selectedIndex = getSelTwoPartPairObjectPane().getTable().getSelectedRows();
		for (int i = 0; i < selectedIndex.length; i++) {
			int num = getTwoPartPairObjectPane().getItems().size();
			InspectionPartDescription temp = getSelTwoPartPairObjectPane().getItems().get(selectedIndex[i]-i);
			int insertNum = 0;
			for (int iLoopCount = 0; iLoopCount < num; iLoopCount++) {
				InspectionPartDescription tableData =  getTwoPartPairObjectPane().getItems().get(iLoopCount);
				insertNum = comparePartTwoPartData(temp, insertNum, iLoopCount,tableData);
			}
			getTwoPartPairObjectPane().getItems().add(insertNum,temp);
			getTwoPartPairObjectPane().reloadData(getTwoPartPairObjectPane().getItems());
			getSelTwoPartPairObjectPane().getItems().remove(temp);
			getSelTwoPartPairObjectPane().reloadData(getSelTwoPartPairObjectPane().getItems());
			if (getSelTwoPartPairObjectPane().getItems().size() == 0) {
				getUpBtnForTwoPartPair().setEnabled(false);
				getSaveUpdateCombinationsBtn().setEnabled(false);
				getDeleteCombinationsBtn().setEnabled(false);
			}
		}
		getUpBtnForTwoPartPair().setEnabled(false);
		clearTableSelections();
	}

	public void clickSaveUpdate() {
		clearMessage();	
		if (getSelTwoPartPairObjectPane().getItems().size()==0) return;
		String tmpPart = getSelPartObjectPane().getItems().get(0).getInspectionPartName();
		String tmpPartLocation = (String)getSelPartObjectPane().getItems().get(0).getInspectionPartLocationName();
		String[] tmpTPP = new String[getSelTwoPartPairObjectPane().getItems().size()];
		String[] tmpTPPL = new String[getSelTwoPartPairObjectPane().getItems().size()];
		for (int i=0; i<getSelTwoPartPairObjectPane().getItems().size(); i++) {
			tmpTPP[i] =  (String)getSelTwoPartPairObjectPane().getItems().get(i).getInspectionPartName();
			tmpTPPL[i] =  (String)getSelTwoPartPairObjectPane().getItems().get(i).getInspectionPartLocationName();
		}		
		if(!saveTwoPartPair(tmpPart,tmpPartLocation,tmpTPP,tmpTPPL)) 
		{			
			return;
		}else
		{
			resetScreen();			
		}		
	}


	public boolean saveTwoPartPair(String tmpPart, String tmpPartLocation,String[] tmpTPP, String[] tmpTPPL)
	{
		String partGroup = getPartGroupComboBox().getSelectedItem().toString();
		List<InspectionPartDescription> inspPartDescList=getDao(InspectionPartDescriptionDao.class).findAllByPart(partGroup, tmpPart, tmpPartLocation);
		if(inspPartDescList.size() <= 0) {
			setErrorMessage("Selected part and/or location has been deleted");
			return false ;
		}	
		for(int i = 0; i <tmpTPP.length; i++) {
			inspPartDescList.clear();
			inspPartDescList=getDao(InspectionPartDescriptionDao.class).findAllByPart(partGroup, tmpTPP[i], tmpTPPL[i]);
			if(inspPartDescList.size() <= 0) {
				setErrorMessage("Selected part and/or location has been deleted");
				return false ;
			}
		}
		List<InspectionTwoPartDescription> inspTwoPartDescList=getDao(InspectionTwoPartDescriptionDao.class).findByPartGroupInspLocPartName(partGroup, tmpPartLocation,tmpPart );
		List<InspectionTwoPartDescription> existingTwoPair = new ArrayList<InspectionTwoPartDescription>();
		List<InspectionPartDescription> twoPartPair = new ArrayList<InspectionPartDescription>();
		if(inspTwoPartDescList.size() > 0) {
			existingTwoPair.addAll(inspTwoPartDescList);
		}			
		for(InspectionTwoPartDescription inspTwoPartDesc:existingTwoPair)
		{
			inspPartDescList.clear();
			inspPartDescList=getDao(InspectionPartDescriptionDao.class).findAllByPart(partGroup, inspTwoPartDesc.getId().getTwoPartPairPart(), inspTwoPartDesc.getId().getTwoPartPairLocation());
			if (inspPartDescList.size()>0)
				twoPartPair.addAll(inspPartDescList); 
		}	
		String[]	statusEle		=	new String[tmpTPP.length];
		String[]	statusExistEle	=	new String[twoPartPair.size()];
		for (int i=0; i<tmpTPP.length; i++) {
			String	tpp		=	tmpTPP[i].trim();
			String	tppL	=	tmpTPPL[i].trim();
			statusEle[i]	=	"insert";
			for (int j=0; j<twoPartPair.size(); j++){
				InspectionPartDescription	tmp			=	twoPartPair.get(j);
				String	tppExist	=	tmp.getId().getInspectionPartName().trim();
				String	tppLExist	=	tmp.getId().getInspectionPartLocationName().trim();
				if((tppExist.equals(tpp))&&(tppLExist.equals(tppL))) {
					statusEle[i]	=	"no insert";
					break;
				}					
			}
		}	
		for (int j=0; j<twoPartPair.size(); j++){
			InspectionPartDescription	tmp			=	twoPartPair.get(j);
			String	tppExist	=	tmp.getId().getInspectionPartName().trim();
			String	tppLExist	=	tmp.getId().getInspectionPartLocationName().trim();
			statusExistEle[j]	=	"delete";
			for (int i=0; i<tmpTPP.length; i++) {
				String	tpp		=	tmpTPP[i].trim();
				String	tppL	=	tmpTPPL[i].trim();
				if((tppExist.equals(tpp))&&(tppLExist.equals(tppL))) {
					statusExistEle[j]	=	"no delete";
					break;
				}					
			}	
		}	
		for(int iLoopCount = 0; iLoopCount < twoPartPair.size(); iLoopCount++) {
			if(statusExistEle[iLoopCount].equals("delete")) {
				InspectionPartDescription	temp	=	twoPartPair.get(iLoopCount);
				String twoPart = temp.getId().getInspectionPartName();
				String twoPartPairLocation =  temp.getId().getInspectionPartLocationName();
				if(!verifyRelationships(tmpPart, tmpPartLocation,  twoPartPairLocation, partGroup,  twoPart))
					return false;
			}
		}
		for (int i=0; i<twoPartPair.size(); i++) {
			if(statusExistEle[i].equals("delete")){  
				InspectionPartDescription	tmp	=	twoPartPair.get(i);
				InspectionTwoPartDescriptionId key = new InspectionTwoPartDescriptionId(tmpPart, tmpPartLocation, tmp.getInspectionPartLocationName(), partGroup, tmp.getInspectionPartName());
				getDao(InspectionTwoPartDescriptionDao.class).removeByKey(key);
				logUserAction("removed InspectionTwoPartDescription by key: " + key.toString());
			}
		}
		for (int i=0; i<tmpTPP.length; i++) {
			if (statusEle[i].equals("insert")){  
				InspectionTwoPartDescription inspTwoPartDesc=getDao(InspectionTwoPartDescriptionDao.class).findByKey(new InspectionTwoPartDescriptionId( tmpPart, tmpPartLocation,  tmpTPPL[i], partGroup,  tmpTPP[i]));
				if(inspTwoPartDesc !=null){
					continue;
				}
				InspectionTwoPartDescription inspectionTwoPartDescription=new InspectionTwoPartDescription();
				InspectionTwoPartDescriptionId id=new InspectionTwoPartDescriptionId( tmpPart, tmpPartLocation,  tmpTPPL[i], partGroup,  tmpTPP[i]);
				inspectionTwoPartDescription.setId(id);
				getDao(InspectionTwoPartDescriptionDao.class).saveInspectionTwoPartDescription(inspectionTwoPartDescription);
				logUserAction(SAVED, inspectionTwoPartDescription);
			}	
		}	
		clearTableSelections();
		return true;
	}

	private javax.swing.JButton getDeleteCombinationsBtn() {
		if (deleteCombinationsBtn == null) {
			try {
				deleteCombinationsBtn = new javax.swing.JButton();
				deleteCombinationsBtn.setName("JButtonDeleteSelectedCombinations");
				deleteCombinationsBtn.setFont(new java.awt.Font("dialog", 0, 18));
				deleteCombinationsBtn.setText("Delete Selected Combinations");
				deleteCombinationsBtn.setBounds(609, 588, 300, 25);
				deleteCombinationsBtn.setEnabled(true);
			} catch (Exception e) {			
				handleException(e);
			}
		}
		return deleteCombinationsBtn;
	}

	private DrawnButton getDownBtnForPart() {
		if (downButtonForPart == null) {
			try {
				downButtonForPart = new DrawnButton();
				downButtonForPart.setName("downButtonForPart");
				downButtonForPart.setButtomMode(1);
				downButtonForPart.setText("DrawnButton1");
				downButtonForPart.setBounds(167, 283, 30, 40);
				downButtonForPart.setEnabled(false);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return downButtonForPart;
	}

	private DrawnButton getDownBtnforTwoPartPair() {
		if (downButtonForTwoPartPair == null) {
			try {
				downButtonForTwoPartPair = new DrawnButton();
				downButtonForTwoPartPair.setName("downButtonForTwoPartPair");
				downButtonForTwoPartPair.setButtomMode(1);
				downButtonForTwoPartPair.setText("DrawnButton1");
				downButtonForTwoPartPair.setBounds(655, 283, 30, 40);
				downButtonForTwoPartPair.setEnabled(false);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return downButtonForTwoPartPair;
	}

	private javax.swing.JButton getSaveUpdateCombinationsBtn() {
		if (saveUpdateCombinationsBtn == null) {
			try {
				saveUpdateCombinationsBtn = new javax.swing.JButton();
				saveUpdateCombinationsBtn.setName("saveUpdateCombinationsBtn");
				saveUpdateCombinationsBtn.setFont(new java.awt.Font("dialog", 0, 18));
				saveUpdateCombinationsBtn.setText("Save Update Combinations");
				saveUpdateCombinationsBtn.setBounds(114, 588, 300, 25);
				saveUpdateCombinationsBtn.setEnabled(true);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return saveUpdateCombinationsBtn;
	}

	private DrawnButton getUpBtnForPart() {
		if (upButtonForPart == null) {
			try {
				upButtonForPart = new DrawnButton();
				upButtonForPart.setName("upButtonForPart");
				upButtonForPart.setText("upButtonForPart");
				upButtonForPart.setBounds(340, 283, 30, 40);
				upButtonForPart.setEnabled(false);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return upButtonForPart;
	}

	private DrawnButton getUpBtnForTwoPartPair() {
		if (upButtonForTwoPartPair == null) {
			try {
				upButtonForTwoPartPair = new DrawnButton();
				upButtonForTwoPartPair.setName("upButtonForTwoPartPair");
				upButtonForTwoPartPair.setText("upButtonForTwoPartPair");
				upButtonForTwoPartPair.setBounds(829, 283, 30, 40);
				upButtonForTwoPartPair.setEnabled(false);			
			} catch (Exception e) {
				handleException(e);
			}
		}
		return upButtonForTwoPartPair;
	}

	private javax.swing.JLabel getPartGroupLabel() {
		if (partGroupLabel == null) {
			try {
				partGroupLabel = new javax.swing.JLabel();
				partGroupLabel.setName("partGroupLabel");
				partGroupLabel.setFont(new java.awt.Font("dialog", 0, 18));
				partGroupLabel.setText("Part Group");
				partGroupLabel.setBounds(85, 13, 106, 23);
				partGroupLabel.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return partGroupLabel;
	}

	private javax.swing.JLabel getJLabelNotice() {
		if (noticeLabel == null) {
			try {
				noticeLabel = new javax.swing.JLabel();
				noticeLabel.setName("noticeLabel");
				noticeLabel.setFont(new java.awt.Font("dialog", 1, 18));
				noticeLabel.setText("NOTE: If you close without saving, your work will be lost!");
				noticeLabel.setBounds(243, 627, 523, 24);
				noticeLabel.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return noticeLabel;
	}

	private javax.swing.JScrollPane getPartScrollPane() {
		if (partScrollPane == null) {
			try {
				partScrollPane = new javax.swing.JScrollPane();
				partScrollPane.setName("partScrollPane");
				partScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				partScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				partScrollPane.setBounds(30, 52, 468, 219);
				getPartScrollPane().setViewportView(getPartObjectPane());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return partScrollPane;
	}

	private javax.swing.JScrollPane getSelPartScrollPane() {
		if (selPartScrollPane == null) {
			try {
				selPartScrollPane = new javax.swing.JScrollPane();
				selPartScrollPane.setName("selPartScrollPane");
				selPartScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				selPartScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				selPartScrollPane.setBounds(30, 336, 468, 219);
				getSelPartScrollPane().setViewportView(getSelPartObjectPane());			
			} catch (Exception e) {


				handleException(e);
			}
		}
		return selPartScrollPane;
	}

	private javax.swing.JScrollPane getTwoPartPairScrollPane() {
		if (twoPartPairScrollPane == null) {
			try {
				twoPartPairScrollPane = new javax.swing.JScrollPane();
				twoPartPairScrollPane.setName("twoPartPairScrollPane");
				twoPartPairScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				twoPartPairScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				twoPartPairScrollPane.setBounds(525, 52, 468, 219);
				getTwoPartPairScrollPane().setViewportView(getTwoPartPairObjectPane());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return twoPartPairScrollPane;
	}

	private javax.swing.JScrollPane getSelTwoPartPairScrollPane() {
		if (selTwoPartPairScrollPane == null) {
			try {
				selTwoPartPairScrollPane = new javax.swing.JScrollPane();
				selTwoPartPairScrollPane.setName("selTwoPartPairScrollPane");
				selTwoPartPairScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				selTwoPartPairScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				selTwoPartPairScrollPane.setBounds(525, 336, 468, 219);
				getSelTwoPartPairScrollPane().setViewportView(getSelTwoPartPairObjectPane());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return selTwoPartPairScrollPane;
	}

	private javax.swing.JComboBox getPartGroupComboBox() {
		if (partGroupComboBox == null) {
			try {
				partGroupComboBox = new javax.swing.JComboBox();
				partGroupComboBox.setName("partGroupComboBox");
				partGroupComboBox.setFont(new java.awt.Font("dialog", 0, 14));
				partGroupComboBox.setBackground(java.awt.Color.white);
				partGroupComboBox.setBounds(191, 13, 228, 25);
				partGroupComboBox.setEnabled(true);
				partGroupComboBox.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return partGroupComboBox;
	}

	private ObjectTablePane<InspectionPartDescription> getPartObjectPane() {
		if(partObjectPane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Part 1","inspectionPartName").put("Part 1 Location", "inspectionPartLocationName"); 			
			partObjectPane = new ObjectTablePane<InspectionPartDescription>(columnMappings.get(),true);
			int height = 500;
			partObjectPane.setSize(400, height);
			partObjectPane.setLocation(getLeftMargin() + 5, getTopMargin() + 10);
			partObjectPane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			partObjectPane.setFont(new java.awt.Font("dialog", 0, 14));
			partObjectPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			partObjectPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}	
		return partObjectPane;
	}

	private ObjectTablePane<InspectionPartDescription> getSelPartObjectPane() {
		if(selectedPartObjectPane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Part 1","inspectionPartName").put("Part 1 Location", "inspectionPartLocationName"); 			
			selectedPartObjectPane = new ObjectTablePane<InspectionPartDescription>(columnMappings.get(),true);
			int height = 500;
			selectedPartObjectPane.setSize(400, height);
			selectedPartObjectPane.setLocation(getLeftMargin() + 5, getTopMargin() + 10);
			selectedPartObjectPane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			selectedPartObjectPane.setFont(new java.awt.Font("dialog", 0, 14));
			selectedPartObjectPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			selectedPartObjectPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}	
		return selectedPartObjectPane;
	}

	private ObjectTablePane<InspectionPartDescription> getSelTwoPartPairObjectPane() {
		if(selecteTwoPartPairObjectPane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Part 2","inspectionPartName").put("Part 2 Location", "inspectionPartLocationName"); 			
			selecteTwoPartPairObjectPane = new ObjectTablePane<InspectionPartDescription>(columnMappings.get(),true);
			int height = 500;
			selecteTwoPartPairObjectPane.setSize(400, height);
			selecteTwoPartPairObjectPane.setLocation(getLeftMargin() + 5, getTopMargin() + 10);
			selecteTwoPartPairObjectPane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			selecteTwoPartPairObjectPane.setFont(new java.awt.Font("dialog", 0, 14));
			selecteTwoPartPairObjectPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			selecteTwoPartPairObjectPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}	
		return selecteTwoPartPairObjectPane;
	}

	private ObjectTablePane<InspectionPartDescription> getTwoPartPairObjectPane() {
		if(twoPartPairObjectPane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Part 2","inspectionPartName").put("Part 2 Location", "inspectionPartLocationName"); 			
			twoPartPairObjectPane = new ObjectTablePane<InspectionPartDescription>(columnMappings.get(),true);
			int height = 500;
			twoPartPairObjectPane.setSize(400, height);
			twoPartPairObjectPane.setLocation(getLeftMargin() + 5, getTopMargin() + 10);
			twoPartPairObjectPane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			twoPartPairObjectPane.setFont(new java.awt.Font("dialog", 0, 14));
			twoPartPairObjectPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			twoPartPairObjectPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}	
		return twoPartPairObjectPane;
	}

	public void handleException(Exception e) {
		if(e == null) this.clearErrorMessage();
		else {
			getLogger().error(e, "unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			this.setErrorMessage(e.getMessage());	
		}
	}

	private void initConnections() throws java.lang.Exception {
		getSaveUpdateCombinationsBtn().addActionListener(this);
		getDeleteCombinationsBtn().addActionListener(this);
		getPartObjectPane().getTable().getSelectionModel().addListSelectionListener(this);
		getTwoPartPairObjectPane().getTable().getSelectionModel().addListSelectionListener(this);
		getSelPartObjectPane().getTable().getSelectionModel().addListSelectionListener(this);
		getSelTwoPartPairObjectPane().getTable().getSelectionModel().addListSelectionListener(this);
		getPartGroupComboBox().addActionListener(this);
		getDownBtnForPart().addActionListener(this);
		getUpBtnForPart().addActionListener(this);
		getDownBtnforTwoPartPair().addActionListener(this);
		getUpBtnForTwoPartPair().addActionListener(this);
	}

	private void initialize() {
		try {
			setName("TwoPartPairMaintenancePanel");
			setSize(1024, 768);
			setLayout(null);
			add(getPartGroupLabel(), getPartGroupLabel().getName());
			add(getPartGroupComboBox(), getPartGroupComboBox().getName());
			add(getSaveUpdateCombinationsBtn(), getSaveUpdateCombinationsBtn().getName());
			add(getDeleteCombinationsBtn(), getDeleteCombinationsBtn().getName());
			add(getPartScrollPane(), getPartScrollPane().getName());
			add(getSelPartScrollPane(), getSelPartScrollPane().getName());
			add(getTwoPartPairScrollPane(), getTwoPartPairScrollPane().getName());
			add(getSelTwoPartPairScrollPane(), getSelTwoPartPairScrollPane().getName());
			add(getDownBtnForPart(), getDownBtnForPart().getName());
			add(getUpBtnForPart(), getUpBtnForPart().getName());
			add(getDownBtnforTwoPartPair(), getDownBtnforTwoPartPair().getName());
			add(getUpBtnForTwoPartPair(), getUpBtnForTwoPartPair().getName());
			add(getJLabelNotice(), getJLabelNotice().getName());
			initConnections();
			startFrame();		
		} catch (Exception e) {
			handleException(e);
		}

	}


	public void resetComponent() {

		getDownBtnForPart().setEnabled(false);
		getUpBtnForPart().setEnabled(false);
		getDownBtnforTwoPartPair().setEnabled(false);
		getUpBtnForTwoPartPair().setEnabled(false);
		getSaveUpdateCombinationsBtn().setEnabled(false);
		getDeleteCombinationsBtn().setEnabled(false);

	}

	public void resetScreen() {

		getPartGroupComboBox().removeAllItems();
		List<PartGroup> partGroupList=getDao(PartGroupDao.class).findLikePartGroupName("%TWO PART PAIR-%");
		for(PartGroup partGroup:partGroupList)
		{
			getPartGroupComboBox().addItem(partGroup.getPartGroupName());
		}	
		resetComponent();
		getTwoPartPairObjectPane().removeData();
		getSelTwoPartPairObjectPane().removeData();	
		getPartObjectPane().removeData();
		getSelPartObjectPane().removeData();
		getPartGroupComboBox().setSelectedIndex(-1);
	}

	public void selectPartGroup() {

		String selectedItem = (String)getPartGroupComboBox().getSelectedItem();
		if(getPartGroupComboBox().getSelectedIndex() == -1 || selectedItem == null) {
			return;
		}
		setErrorMessage("");
		List<InspectionPartDescription> inspectPartDescList=getDao(InspectionPartDescriptionDao.class).findAllByPartGroupName(selectedItem);
		if (inspectPartDescList==null) return;	
		getPartObjectPane().removeData();
		getSelPartObjectPane().removeData();
		getTwoPartPairObjectPane().removeData();
		getSelTwoPartPairObjectPane().removeData();	
		getPartObjectPane().reloadData(inspectPartDescList);
		getTwoPartPairObjectPane().reloadData(inspectPartDescList);
		resetComponent();
	}

	public void selectUnselectPart() {
		clearMessage();
		if((getPartObjectPane().getItems().size() > 0) && (getSelPartObjectPane().getItems().size() <= 0)) { 
			getDownBtnForPart().setEnabled(true);
			getUpBtnForPart().setEnabled(false);
			getDownBtnforTwoPartPair().setEnabled(false);
			getUpBtnForTwoPartPair().setEnabled(false);
			getSelPartObjectPane().clearSelection();
			getTwoPartPairObjectPane().clearSelection();
			getSelTwoPartPairObjectPane().clearSelection();		
		} else {
			getDownBtnForPart().setEnabled(false);
			getPartObjectPane().clearSelection();
		}	
	}

	public void selectUnselectSelPart() {
		clearMessage();	
		if(getSelPartObjectPane().getSelectedItems().size() > 0) {
			getUpBtnForPart().setEnabled(true);
			getDownBtnForPart().setEnabled(false);
			getDownBtnforTwoPartPair().setEnabled(false);
			getUpBtnForTwoPartPair().setEnabled(false);	
			getPartObjectPane().clearSelection();
			getTwoPartPairObjectPane().clearSelection();
			getSelTwoPartPairObjectPane().clearSelection();
		} else {
			getUpBtnForPart().setEnabled(false);
			setErrorMessage("Please select Part 1 and Part 1 location before selecting Part 2 and Part 2 location");
		}	
	}

	public void selectUnselectSelTwoPartPair() {
		clearMessage();	
		if(getSelTwoPartPairObjectPane().getSelectedItems().size() > 0) {
			getUpBtnForTwoPartPair().setEnabled(true);
			getDownBtnForPart().setEnabled(false);
			getUpBtnForPart().setEnabled(false);
			getDownBtnforTwoPartPair().setEnabled(false);
			getPartObjectPane().clearSelection();
			getSelPartObjectPane().clearSelection();
			getTwoPartPairObjectPane().clearSelection();
		} else {
			getUpBtnForPart().setEnabled(false);
		}	
		return;
	}

	public void selectUnselectTwoPartPair() {

		clearMessage();
		if((getSelPartObjectPane().getItems().size() > 0) && getTwoPartPairObjectPane().getSelectedItems().size() > 0 ) {
			getDownBtnforTwoPartPair().setEnabled(true);
			getDownBtnForPart().setEnabled(false);
			getUpBtnForPart().setEnabled(false);
			getUpBtnForTwoPartPair().setEnabled(false);
			getPartObjectPane().clearSelection();
			getSelPartObjectPane().clearSelection();
			getSelTwoPartPairObjectPane().clearSelection();
		} else {
			getDownBtnforTwoPartPair().setEnabled(false);
			getTwoPartPairObjectPane().clearSelection();
		}
		return;
	}

	public void startFrame() {
		try {
			setErrorMessage("");
			resetScreen();		
		} catch (Exception e) {
			handleException(e);
		}
	}

}