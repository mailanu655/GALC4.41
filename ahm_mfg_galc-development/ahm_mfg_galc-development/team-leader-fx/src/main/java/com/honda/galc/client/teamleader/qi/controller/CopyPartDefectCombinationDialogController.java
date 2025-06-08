package com.honda.galc.client.teamleader.qi.controller;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.PdcToEntryScreenAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.CopyPartDefectCombinationDialog;
import com.honda.galc.client.teamleader.qi.view.CopyPartDefectCombinationDialog.CopyMode;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiRegionalPartDefectLocationDto;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombinationId;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * 
 * <h3>CopyPartDefectCombinationDialogController Class description</h3>
 * <p>
 * CopyPartDefectCombinationDialogController description
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author L&T Infotech<br>
 *         Apr 13, 2017
 *
 *
 */

public class CopyPartDefectCombinationDialogController extends QiDialogController<PdcToEntryScreenAssignmentModel, CopyPartDefectCombinationDialog> {
	
	private List<QiTextEntryMenu> textEntryMenuList;
	private QiEntryScreenDto entryScreenDto;
	private List<QiTextEntryMenu> sourceMenuList;
	Map<String, List<QiRegionalPartDefectLocationDto>> textEntryMenuMap;
	private boolean isPddaConfirmed=false;
	private boolean copyPDDAInfo = false;
	private CopyPartDefectCombinationDialog.CopyMode copyType;
	private List<Integer> validPDCList = null;
	private class MyDto {
		private QiLocalDefectCombination ldc = null;
		private List<QiExternalSystemDefectMap> qiMap = null;
		public QiLocalDefectCombination getLdc() {
			return ldc;
		}
		public void setLdc(QiLocalDefectCombination ldc) {
			this.ldc = ldc;
		}
		public List<QiExternalSystemDefectMap> getQiMap() {
			return qiMap;
		}
		public void setQiMap(List<QiExternalSystemDefectMap> qiMap) {
			this.qiMap = qiMap;
		}
		
	}

	private ChangeListener<QiEntryScreenDto> entryScreenTableChangeListener = new ChangeListener<QiEntryScreenDto>() {
		public void changed(ObservableValue<? extends QiEntryScreenDto> arg0, QiEntryScreenDto arg1,QiEntryScreenDto arg2){
			clearDisplayMessage();
			if (getDialog().getScreenType().equals(CopyPartDefectCombinationDialog.CopyMode.COPY_PDC)
					|| getDialog().getScreenType().equals(CopyPartDefectCombinationDialog.CopyMode.MOVE_PDC)) {
				List<QiTextEntryMenu> menuList = getModel().getMenuDetailsByEntryScreen(getDialog().getEntryScreenPanel().getSelectedItem());
				//if source entry screen = destination entry screen, filter out source menu from list of available menus for target
				String plantName = StringUtils.trimToEmpty(getDialog().getPlantComboBox().getSelectionModel().getSelectedItem());
				String sourcePlant = StringUtils.trimToEmpty(getEntryScreenDto().getPlantName());
				if(plantName.equalsIgnoreCase(sourcePlant))  {
					menuList.remove(getDialog().getSourceMenuList().get(0));
				}
				getDialog().getTextEntryMenuPane().setData(menuList);
			}
		}

	};

	
	public CopyPartDefectCombinationDialogController(PdcToEntryScreenAssignmentModel model,
			CopyPartDefectCombinationDialog dialog, QiEntryScreenDto qiEntryScreenDto, CopyPartDefectCombinationDialog.CopyMode newCopyType) {
		this(model, dialog, newCopyType);
	}

	
	
	public CopyPartDefectCombinationDialogController(PdcToEntryScreenAssignmentModel model,
			CopyPartDefectCombinationDialog dialog, CopyPartDefectCombinationDialog.CopyMode newCopyType) {
		super();
		setModel(model);
		setDialog(dialog);
		copyType = newCopyType;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();

			if (actionEvent.getSource().equals(getDialog().getCancelButton()))
				cancelBtnAction();
			else
				okBtnAction(actionEvent, loggedButton);
		}
	}

	/** This method will be called on click of OK button.
	 *  This will copy PDCs according to its source to destination selected.
	 * 
	 * @param actionEvent
	 * @param button
	 */
	public void okBtnAction(ActionEvent actionEvent, LoggedButton button) {

		if (null != getDialog().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem()) {
			
			if (getDialog().getScreenType().equals(CopyPartDefectCombinationDialog.CopyMode.COPY_MENU)) {
				copyEntryMenu();
			}

			else if (getDialog().getScreenType().equals(CopyPartDefectCombinationDialog.CopyMode.COPY_SCREEN)) {
				copyPDCsFromImageTypeEntryScreen();
			}

			else if(getDialog().getScreenType().equals(CopyPartDefectCombinationDialog.CopyMode.COPY_PDC)) {
					int countExisting = howManyAlreadyExistInTarget();
					QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
					if(countExisting > 0)  {
						String confirmMessage = String.format("%d PDCs exist in destination entry screen and will be skipped. Continue?",
								countExisting, toEntryScreenDto.getEntryScreen());
						if (!MessageDialog.confirm(getDialog(), confirmMessage, false)) {
							cancelBtnAction();
							return;
						}
				}
				try {
					copyPDCFromEntryMenu();
					cancelBtnAction();
					
				} catch (Exception e) {
					getLogger().error(e.getMessage());
					displayErrorMessage("Problem occured while copying PDCs.", e.getMessage());
				}
			}
			else if(getDialog().getScreenType().equals(CopyPartDefectCombinationDialog.CopyMode.MOVE_PDC)) {
				movePDCFromEntryMenu();
				cancelBtnAction();
			}
			
		} else {
			displayErrorMessage("Please select Entry Screen.", "Please select Entry Screen.");
		}
	}

	/** This method will copy the selected PDCs to the entry screen and entry menu selected.
	 *  If PDC not already assigned to some other entry menu associated with selected entry screen.
	 * 
	 */
	private void copyPDCFromEntryMenu() throws Exception {
		QiTextEntryMenu selectedMenu = getDialog().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();
		QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
		if(toEntryScreenDto == null || selectedMenu == null)  {
			displayErrorMessage("Please select Entry Screen Menu.", "Please select Entry Screen Menu.");
			return;
		}
		//pass false for isSameScreen, cannot copy within the same entry screen.
		List<QiRegionalPartDefectLocationDto> dtoListToCopy = getDtoList(false);
		List<QiEntryScreenDefectCombination> pdcList = createPdcList(dtoListToCopy, toEntryScreenDto,
				selectedMenu.getId().getTextEntryMenu());
		try {
			if (!isListEmpty(pdcList)) {	
				getModel().saveAllAssignedPDCs(pdcList);
			}
			//else - would mean that the selected PDCs already exist in the target menu
			//in either case, copy-append, do not copy modified local attributes
			copyLocalAttributes(
					selectedMenu.getId().getTextEntryMenu(), getRegionalIdsList());
		} catch (Exception e) {
			getLogger().error(e.getMessage());
			displayErrorMessage("Problem occured while copying PDCs.", e.getMessage());
			throw e;
		}
	}		

	private int howManyAlreadyExistInTarget()  {
		QiTextEntryMenu selectedMenu = getDialog().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();
		List<QiRegionalPartDefectLocationDto> selectedPDCList = getDialog().getAssignedPartdefectPanel().getTable().getItems();
		int count = 0;
		if(isListEmpty(selectedPDCList) || selectedMenu == null)  {
			return count;
		}
		QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
		//get list of PDCs already assigned to target entry screen
		List<QiRegionalPartDefectLocationDto> existingPDCsForEntryScreen = getModel().getAssignedPartDefectDetails(StringUtils.EMPTY, getModel().getProductKind(),
				toEntryScreenDto.getEntryScreen(), "", toEntryScreenDto.getEntryModel(), toEntryScreenDto.getIsUsedVersion());

		for (QiRegionalPartDefectLocationDto dto : selectedPDCList) {
			if (null != dto && existingPDCsForEntryScreen.contains(dto)) {
				count++;
			}
		}
		return count;
	}
	
	private List<QiRegionalPartDefectLocationDto> getDtoList( boolean isSameScreen)  {
		
		List<QiRegionalPartDefectLocationDto> newPDCsForEntryScreen = null;
		//target menu
		QiTextEntryMenu selectedMenu = getDialog().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();
		//target entry screen
		QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
		List<QiRegionalPartDefectLocationDto> selectedPDCList = getDialog().getAssignedPartdefectPanel().getTable().getItems();
		if(isListEmpty(selectedPDCList) || selectedMenu == null || toEntryScreenDto == null)  {
			return newPDCsForEntryScreen;
		}
		//target entry screen
		//get list of PDCs already assigned to entry screen
		List<QiRegionalPartDefectLocationDto> existingPDCsForEntryScreen = getModel().getAssignedPartDefectDetails(StringUtils.EMPTY, getModel().getProductKind(),
				toEntryScreenDto.getEntryScreen(), "", toEntryScreenDto.getEntryModel(), toEntryScreenDto.getIsUsedVersion());

		newPDCsForEntryScreen = new ArrayList<QiRegionalPartDefectLocationDto>();
		if (!isListEmpty(selectedPDCList)) {
			for (QiRegionalPartDefectLocationDto dto : selectedPDCList) {
				
				if (null != dto)  {
						if(isSameScreen || !existingPDCsForEntryScreen.contains(dto)) {
						newPDCsForEntryScreen.add(dto);
					}
				}
			}
		}
		return newPDCsForEntryScreen;
	}

	private List<QiEntryScreenDefectCombination> createPdcList(List<QiRegionalPartDefectLocationDto> dtoList,
			QiEntryScreenDto entryScreen, String menu)  {
		List<QiEntryScreenDefectCombination> pdcList = new ArrayList<QiEntryScreenDefectCombination>();  
		for (QiRegionalPartDefectLocationDto dto : dtoList) {
			//find any existing entry screen combination - in case of move within same entry screen
			QiEntryScreenDefectCombination entryScreenDefectCombination =
				getModel().findEntryScreenByKey(entryScreen.getEntryScreen(), entryScreen.getEntryModel(),
					dto.getRegionalDefectCombinationId(), entryScreen.getIsUsedVersion());
			if(entryScreenDefectCombination == null)  {
				entryScreenDefectCombination = new QiEntryScreenDefectCombination();
				QiEntryScreenDefectCombinationId newId = new QiEntryScreenDefectCombinationId(
						entryScreen.getEntryScreen(), dto.getRegionalDefectCombinationId(),
						entryScreen.getEntryModel(), entryScreen.getIsUsedVersion());
				entryScreenDefectCombination.setId(newId);
				entryScreenDefectCombination.setCreateUser(getUserId());
				entryScreenDefectCombination.setTextEntryMenu(menu);
			}
			//if found and not destruct mode, then this must be move within same entry screen
			else  {
				entryScreenDefectCombination.setUpdateUser(getUserId());
				entryScreenDefectCombination.setTextEntryMenu(menu);				
			}
			pdcList.add(entryScreenDefectCombination);
		}
		return pdcList;
	}
	
	private List<QiEntryScreenDefectCombination> getPdcList(List<QiRegionalPartDefectLocationDto> dtoList, QiEntryScreenDto entryScreen)  {
		List<QiEntryScreenDefectCombination> pdcList = new ArrayList<QiEntryScreenDefectCombination>();  
		for (QiRegionalPartDefectLocationDto dto : dtoList) {
			//find any existing entry screen combination - in case of move within same entry screen
			QiEntryScreenDefectCombination entryScreenDefectCombination =
				getModel().findEntryScreenByKey(entryScreen.getEntryScreen(), entryScreen.getEntryModel(),
					dto.getRegionalDefectCombinationId(), entryScreen.getIsUsedVersion());
			if(entryScreenDefectCombination != null)  {
				pdcList.add(entryScreenDefectCombination);
			}
		}
		return pdcList;
	}
	

	private void movePDCAndHeadlessMappings(List<QiLocalDefectCombination> sourceLdcList, List<QiExternalSystemDefectMap> sourceHeadlessList ) throws Exception {
		QiTextEntryMenu selectedMenu = getDialog().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();
		QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
		if(toEntryScreenDto == null || selectedMenu == null)  {
			displayErrorMessage("Please select Entry Screen Menu.", "Please select Entry Screen Menu.");
			return;
		}
		//pass false for isSameScreen, cannot copy within the same entry screen.
		List<QiRegionalPartDefectLocationDto> dtoListToCopy = getDtoList(false);
		List<QiEntryScreenDefectCombination> pdcList = createPdcList(dtoListToCopy, toEntryScreenDto,
				selectedMenu.getId().getTextEntryMenu());
		try {
			if (!isListEmpty(pdcList)) {	
				getModel().saveAllAssignedPDCs(pdcList);
			}
			//else - would mean that the selected PDCs already exist in the target menu
			//in either case, copy-append, do not copy modified local attributes
			List<MyDto> targetList = getTargetList(
					selectedMenu.getId().getTextEntryMenu(), getRegionalIdsList());
			if(targetList != null && !targetList.isEmpty())  {
				for(MyDto dto : targetList)  {
					if(dto.getLdc() != null)  {
						try {
							QiLocalDefectCombination newLdc = getModel().copyLocalDefectCombination(dto.getLdc());
							if(newLdc != null && dto.getQiMap() != null && !dto.getQiMap().isEmpty())  {
								List<QiExternalSystemDefectMap> hlMappings = dto.getQiMap();
								for(QiExternalSystemDefectMap thisMap : hlMappings)  {
									thisMap.setLocalDefectCombinationId(newLdc.getLocalDefectCombinationId());
								}
							}
						} catch (Exception e) {
							displayErrorMessage("Problem occurred while copying local attribute data.", "Problem occurred while creating LDC");
							throw e;
						}
					}
					
				}
			}
			List<QiExternalSystemDefectMap> targetHLMappings = new ArrayList<QiExternalSystemDefectMap>();
			for(MyDto dto : targetList)  {
				if(dto != null)  {
					if(dto.getQiMap() != null && !dto.getQiMap().isEmpty())  {
						targetHLMappings.addAll(dto.getQiMap());
					}
				}
			}
			getModel().deleteLDCList(sourceLdcList);
			getModel().moveHeadlessMappings(targetHLMappings, sourceHeadlessList);
		} catch (Exception e) {
			getLogger().error(e.getMessage());
			displayErrorMessage("Problem occured while copying PDCs.", e.getMessage());
			throw e;
		}
	}		

	private List<MyDto> getTargetList(String textEntryMenu, List<Integer> regionalIdList) throws Exception {
		List<QiLocalDefectCombination> defectCombinationList = new ArrayList<QiLocalDefectCombination>();
		List<MyDto> qiLdcMapList = new ArrayList<MyDto>();
		
		QiEntryScreenId fromEntryScreenId = new QiEntryScreenId();
		QiEntryScreenId toEntryScreenId = new QiEntryScreenId();
		
		fromEntryScreenId.setEntryModel(entryScreenDto.getEntryModel());
		fromEntryScreenId.setEntryScreen(entryScreenDto.getEntryScreen());
		fromEntryScreenId.setIsUsed(entryScreenDto.getIsUsedVersion());
		
		QiEntryScreenDto toQiEntryScreenDto = getDialog().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();
		toEntryScreenId.setEntryModel(toQiEntryScreenDto.getEntryModel());
		toEntryScreenId.setEntryScreen(toQiEntryScreenDto.getEntryScreen());
		toEntryScreenId.setIsUsed(toQiEntryScreenDto.getIsUsedVersion());
		
		try {
			defectCombinationList = getModel().findAllLocalDefectCombinationsByLocalDefectId(fromEntryScreenId, toEntryScreenId,
					regionalIdList);
		
			if(!isListEmpty(defectCombinationList) && !isPddaConfirmed){
				copyPDDAInfo = MessageDialog.confirm(getDialog(), "Are you sure you want to copy PDDA Info?");
				isPddaConfirmed=true;
			}
			for (QiLocalDefectCombination localDefect : defectCombinationList) {
				
				QiLocalDefectCombination qiLocalDefectCombination = new QiLocalDefectCombination();
				qiLocalDefectCombination.setRegionalDefectCombinationId((Integer)localDefect.getRegionalDefectCombinationId());
				qiLocalDefectCombination.setLocalTheme(localDefect.getLocalTheme());
				qiLocalDefectCombination.setEngineFiringFlag(localDefect.getEngineFiringFlag());
				qiLocalDefectCombination.setRepairAreaName(localDefect.getRepairAreaName());
				qiLocalDefectCombination.setRepairMethod(localDefect.getRepairMethod());
				qiLocalDefectCombination.setRepairMethodTime(localDefect.getRepairMethodTime());
				qiLocalDefectCombination.setEstimatedTimeToFix(localDefect.getEstimatedTimeToFix());
				qiLocalDefectCombination.setEntrySiteName(localDefect.getEntrySiteName());
				qiLocalDefectCombination.setEntryPlantName(toQiEntryScreenDto.getPlantName());
				qiLocalDefectCombination.setReportable(localDefect.getReportable());
				qiLocalDefectCombination.setEntryScreen(toQiEntryScreenDto.getEntryScreen());
				qiLocalDefectCombination.setEntryModel(toQiEntryScreenDto.getEntryModel());
				qiLocalDefectCombination.setIsUsed(toQiEntryScreenDto.getIsUsedVersion());
				qiLocalDefectCombination.setTextEntryMenu(StringUtils.trimToEmpty(textEntryMenu));
				qiLocalDefectCombination.setDefectCategoryName(localDefect.getDefectCategoryName());
				qiLocalDefectCombination.setResponsibleLevelId(localDefect.getResponsibleLevelId());
				if(copyPDDAInfo)
					qiLocalDefectCombination.setPddaResponsibilityId(localDefect.getPddaResponsibilityId());
				else
					qiLocalDefectCombination.setPddaResponsibilityId(null);
				qiLocalDefectCombination.setCreateUser(getUserId());
				qiLocalDefectCombination.setUpdateTimestamp(null);
				qiLocalDefectCombination.setUpdateUser(null);

				MyDto dto = new MyDto();
				dto.setLdc(qiLocalDefectCombination);

				List<QiExternalSystemDefectMap> externalSystemDefectMapList = getModel().findExtSystemByLocalDefectCombId(localDefect.getLocalDefectCombinationId());
				if(externalSystemDefectMapList == null || externalSystemDefectMapList.isEmpty())  {
					qiLdcMapList.add(dto);
					continue;
				}
				List<QiExternalSystemDefectMap> headlessMappings = new ArrayList<>();
				for(QiExternalSystemDefectMap qiExternalSystemDefectMap : externalSystemDefectMapList){
					QiExternalSystemDefectMap qiExternalSystemDefectMapNew = new QiExternalSystemDefectMap();
					qiExternalSystemDefectMapNew.setExternalSystemName(qiExternalSystemDefectMap.getExternalSystemName());
					qiExternalSystemDefectMapNew.setExternalPartCode(qiExternalSystemDefectMap.getExternalPartCode());
					qiExternalSystemDefectMapNew.setExternalDefectCode(qiExternalSystemDefectMap.getExternalDefectCode());
					qiExternalSystemDefectMapNew.setEntryModel(qiExternalSystemDefectMap.getEntryModel());
					//LDC id will be set later
					qiExternalSystemDefectMapNew.setCreateUser(getUserId());
					qiExternalSystemDefectMapNew.setUpdateTimestamp(null);
					qiExternalSystemDefectMapNew.setUpdateUser(null);
					headlessMappings.add(qiExternalSystemDefectMapNew);
				}
				if(headlessMappings != null && !headlessMappings.isEmpty())  {
					dto.setQiMap(headlessMappings);
				}
				qiLdcMapList.add(dto);
			}
			return qiLdcMapList;
		} catch (Exception e) {
			displayErrorMessage("Problem occurred while copying PDCs.", "Problem occurred while copying local attributes.");
			throw e;
		}					
	}


	private void movePDCFromEntryMenu() {
		
		QiTextEntryMenu fromMenu = getTextEntryMenuList().get(0); // from menu
		//To menu
		QiTextEntryMenu selectedMenu = getDialog().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();
		List<QiRegionalPartDefectLocationDto> selectedPDCList = getDialog().getAssignedPartdefectPanel().getTable().getItems();
		if (null != selectedMenu) {
			QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
			boolean isSameScreen = entryScreenDto.isSameEntryScreen(toEntryScreenDto);
			String textEntryMenu = selectedMenu.getId().getTextEntryMenu();
			//removed destruct check
			String message = String.format("Are you sure you want to move %d PDCs from %s-%s to %s-%s", selectedPDCList.size(),
					entryScreenDto.getEntryScreen(), fromMenu.getId().getTextEntryMenu(),
					toEntryScreenDto.getEntryScreen(), selectedMenu.getId().getTextEntryMenu());
			if (!MessageDialog.confirm(getDialog(), message, false)) {
				return;
			}
			//create source list of pdcs and attributes
			//for move, isDestruct is not an option, pass false
			List<QiRegionalPartDefectLocationDto> dtoList = getDtoList(isSameScreen);
			List<QiEntryScreenDefectCombination> pdcList = createPdcList(dtoList, toEntryScreenDto, textEntryMenu);
			List<QiLocalDefectCombination> allLdcList = new ArrayList<QiLocalDefectCombination>();
			List<QiExternalSystemDefectMap> allExtSysList = new ArrayList<QiExternalSystemDefectMap>();
			for(QiEntryScreenDefectCombination pdc : pdcList)  {
				List<QiLocalDefectCombination> ldcList = getModel().findAllLocalDefectCombByPlantEntryScreenModelMenuAndRegionalId(pdc.getId().getRegionalDefectCombinationId(), entryScreenDto, fromMenu.getId().getTextEntryMenu());
				if(ldcList != null && !ldcList.isEmpty())  {
					allLdcList.addAll(ldcList);
				}
			}
			for(QiLocalDefectCombination ldc : allLdcList)  {
				List<QiExternalSystemDefectMap> extList = getModel().findExtSystemByLocalDefectCombId(ldc.getLocalDefectCombinationId());
				if(extList != null && !extList.isEmpty())  {
					allExtSysList.addAll(extList);
				}
			}
			//if copying to another menu in the same entry screen,
			//simply update the menu entry for the entry-screen-defect-combination
			if(isSameScreen)  {
				for(QiEntryScreenDefectCombination pdc : pdcList)  {
					pdc.setTextEntryMenu(textEntryMenu);
				}
				for(QiLocalDefectCombination ldc : allLdcList)  {
					ldc.setTextEntryMenu(textEntryMenu);
				}
				getModel().updateAllEntryScreenDefectCombination(pdcList);
				getModel().updateAttributes(allLdcList);
				cancelBtnAction();
				if(getDialog().getMainPanel() != null)  {
					getDialog().getMainPanel().getController().refreshBtnAction();
				}
				return;
			}
			//else - moving to a different entry screen
			//moving to a different entry screen, copy and delete
			//if any of the items already exist, cannot copy
			int countExisting = howManyAlreadyExistInTarget();
			if(countExisting > 0)  {
				String confirmMessage = String.format("%d PDCs exist in destination entry screen and will not be moved.  Continue?", countExisting, toEntryScreenDto.getEntryScreen());
				if (!MessageDialog.confirm(getDialog(), confirmMessage, false)) {
					cancelBtnAction();
					return;
				}
			}
			try {
				movePDCAndHeadlessMappings(allLdcList, allExtSysList);
				List<QiEntryScreenDefectCombination> sourcePdcList = getPdcList(dtoList, entryScreenDto);
				getModel().removeDeAssignedEntryScreenPdcList(sourcePdcList);
				cancelBtnAction();
				if(getDialog().getMainPanel() != null)  {
					getDialog().getMainPanel().getController().refreshBtnAction();
				}
			} catch (Exception e) {
				getLogger().error(e.getMessage());
				displayErrorMessage("Error moving PDcs", e.getMessage());
			}
		}
		
		else{
			displayErrorMessage("Please select Entry Screen Menu.", "Please select Entry Screen Menu.");
		}
	}
	
	/** This method will copy all the PDCs associated with entry screen of image type selected on PDCToEntryScreen
	 * 	to the Image type entry screen selected on dialog if all PDCs have valid sections on it.
	 * 
	 */
	private void copyPDCsFromImageTypeEntryScreen() {
		
		QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
		if(null != getDialog().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem()){
			
			List<QiRegionalPartDefectLocationDto> sourceAssignedList = getDialog().getAssignedPartdefectPanel().getTable().getItems();
			if(isListEmpty(sourceAssignedList))  {
				displayErrorMessage("No PDCs were selected in source screen, nothing to copy",
						"No PDCs were selected in source screen");
				return;				
			}
			List<QiRegionalPartDefectLocationDto> existingPDCsForEntryScreen = getModel().getAssignedPartDefectDetails(StringUtils.EMPTY, getModel().getProductKind(), toEntryScreenDto.getEntryScreen(),"", toEntryScreenDto.getEntryModel(), toEntryScreenDto.getIsUsedVersion());
			//now, validate the defect combinations being copied against the image sections for the target entry screen.
			List<Integer> partLocationIDsWithValidSections = getModel().findAllPartLocationIdsWithValidSectionByImageName(entryScreenDto.getEntryScreen(),
					toEntryScreenDto.getImageName());


			List<QiRegionalPartDefectLocationDto> newPDCsForEntryScreen = new ArrayList<QiRegionalPartDefectLocationDto>();
			
			if(!isListEmpty(partLocationIDsWithValidSections)){
				int invalidCount = 0;
				for(QiRegionalPartDefectLocationDto dto :  sourceAssignedList ){
					if(null != dto){
						if(partLocationIDsWithValidSections.contains(dto.getRegionalDefectCombinationId())){
								if(!existingPDCsForEntryScreen.contains(dto)){
								newPDCsForEntryScreen.add(dto);
							}
						}
						else{
							invalidCount++;
						}
					}
				}
				if (invalidCount > 0)  {
					String confirmMessage = String.format("There are %d PDCs without valid sections, Proceed?", invalidCount);
					if(!MessageDialog.confirm(getDialog(), confirmMessage, false)) {
						return;
					}
				}
			}
			else{
				displayErrorMessage("There are no PDCs withe valid sections.",
						"There are no PDCs withe valid sections.");
				return;
			}

			List<QiEntryScreenDefectCombination> list = new ArrayList<QiEntryScreenDefectCombination>();
			List<Integer> regionalIdList = new ArrayList<Integer>();
			if(!isListEmpty(newPDCsForEntryScreen)){
				for(QiRegionalPartDefectLocationDto dto : newPDCsForEntryScreen){
					regionalIdList.add(dto.getRegionalDefectCombinationId());
					QiEntryScreenDefectCombination entryScreenDefectCombination = new QiEntryScreenDefectCombination();
					entryScreenDefectCombination.setId(new QiEntryScreenDefectCombinationId(toEntryScreenDto.getEntryScreen(),dto.getRegionalDefectCombinationId(), toEntryScreenDto.getEntryModel(), toEntryScreenDto.getIsUsedVersion()));
					entryScreenDefectCombination.setCreateUser(getUserId());
					list.add(entryScreenDefectCombination);
				}

				try {
					getModel().saveAllAssignedPDCs(list);
					//copy-append, do not copy modified local attributes
					copyLocalAttributes(null, regionalIdList);
					cancelBtnAction();
				} catch (Exception e) {
					getLogger().error(e.getMessage());
					displayErrorMessage("Problem occured while assigning PDCs.", e.getMessage());
				}

			}
			else{
				//copy-append, do not copy modified local attributes
				//actually this block is a no-op, I have left it to illustrate a particular scenario
				//Previously, if there were no new PDCs, but local defect combination were modified, then this block would
				//update the local attributes even though there were no new PDCs.
				//Now, the user has to choose copy-destruct for this scenario.  Only copy destruct will update existing
				try {
					copyLocalAttributes(null, getRegionalIdsList());
					cancelBtnAction();
				} catch (Exception e) {
					getLogger().error(e.getMessage());
					displayErrorMessage("Problem occured while assigning PDCs.", e.getMessage());
				}
			}
		}
	}

	/** This method will copy the selected entry menu and PDCs associated with that entry menu 
	 * 	to the entry screen selected. If PDC not already assigned to some other entry menu 
	 * 	associated with selected entry screen.
	 * 
	 */
	private void copyEntryMenu() {
		
		QiEntryScreenDto toEntryScreenDto = getDialog().getEntryScreenPanel().getSelectedItem();
		if (null != getDialog().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem()) {

			for (QiTextEntryMenu textEntryMenu : textEntryMenuList) {
				List<QiTextEntryMenu> existingMenus = getModel()
						.getMenuDetailsByEntryScreen(toEntryScreenDto);

				textEntryMenu.getId().setEntryScreen(toEntryScreenDto.getEntryScreen());
				textEntryMenu.getId().setEntryModel(toEntryScreenDto.getEntryModel());
				textEntryMenu.getId().setIsUsed(toEntryScreenDto.getIsUsedVersion());
				textEntryMenu.setCreateUser(getUserId());

				List<String> menuNames = new ArrayList<String>();

				if (!isListEmpty(existingMenus)) {
					for (QiTextEntryMenu entryMenu : existingMenus) {
						menuNames.add(entryMenu.getId().getTextEntryMenu());
					}

					if (!menuNames.contains(textEntryMenu.getId().getTextEntryMenu())) {
						getModel().createTextEntryMenu(textEntryMenu);
					}
				}

				if (isListEmpty(existingMenus)) {
					getModel().createTextEntryMenu(textEntryMenu);
				}

				{
					List<QiEntryScreenDefectCombination> list = new ArrayList<QiEntryScreenDefectCombination>();

					List<QiRegionalPartDefectLocationDto> availablePDCs = getModel().getPartDefectDetails("",
							getModel().getProductKind(), getDialog().getEntryScreenPanel().getSelectedItem());
					if (!isListEmpty(availablePDCs)) {
						for (QiRegionalPartDefectLocationDto dto : textEntryMenuMap
								.get(textEntryMenu.getId().getTextEntryMenu())) {
							for (QiRegionalPartDefectLocationDto qiRegionalPartDefectLocationDto : availablePDCs) {
								if (dto.getRegionalDefectCombinationId().equals(qiRegionalPartDefectLocationDto.getRegionalDefectCombinationId())) {
									QiEntryScreenDefectCombination entryScreenDefectCombination = new QiEntryScreenDefectCombination();
									entryScreenDefectCombination.setId(new QiEntryScreenDefectCombinationId(
											toEntryScreenDto.getEntryScreen(), dto.getRegionalDefectCombinationId(),
											toEntryScreenDto.getEntryModel(), toEntryScreenDto.getIsUsedVersion()));
									entryScreenDefectCombination.setCreateUser(getUserId());
									entryScreenDefectCombination.setTextEntryMenu(textEntryMenu.getId().getTextEntryMenu());
									list.add(entryScreenDefectCombination);
									break;
								}
							}
						}
					}

					try {
						String textMenu = textEntryMenu.getId().getTextEntryMenu();
						if (!isListEmpty(list)) {
							getModel().saveAllAssignedPDCs(list);
						}
						//copy-append, do not copy modified local attributes
						copyLocalAttributes(textMenu, getRegionalIdsListForMenu(textMenu));
						cancelBtnAction();
					} catch (Exception e) {
						displayErrorMessage("Problem occured while assigning PDCs.", "Problem occured while assigning PDCs.");
						cancelBtnAction();
					}
				}
			}
		}
	}

	/** When user clicks on cancel button in the popup screen cancelDefect method
	 *  gets called.
	 *  
	 */
	public void cancelBtnAction() {
			Stage stage = (Stage)  getDialog().getCancelButton().getScene().getWindow();
			stage.close();
	}

	
	@Override
	public void initListeners() {
		addDropDownListner();
		addEntryScreenImageTableListner();

		getDialog().getAssignedPartdefectPanel().getTable().getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<QiRegionalPartDefectLocationDto>() {
			public void changed(ObservableValue<? extends QiRegionalPartDefectLocationDto> observable,
					QiRegionalPartDefectLocationDto oldValue, QiRegionalPartDefectLocationDto newValue) {
				clearDisplayMessage();
			}

		}); 

		if (getDialog().getScreenType().equals(CopyMode.COPY_PDC)) {
			getDialog().getTextEntryMenuPane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiTextEntryMenu>() {
				public void changed(ObservableValue<? extends QiTextEntryMenu> observable, QiTextEntryMenu oldValue, QiTextEntryMenu newValue) {
					clearDisplayMessage();
				}
			});
		}
	}

	/** This method will add change listener listener to Entry Screen table.
	 * 
	 */
	private void addEntryScreenImageTableListner() {
		getDialog().getEntryScreenPanel().getTable().getSelectionModel().selectedItemProperty().addListener(entryScreenTableChangeListener);
	}

	/** This method will be used to check whether list contains any items or not.
	 * 	 
	 * 
	 * @param list
	 * @return true if list is empty
	 */
	private boolean isListEmpty(List<?> list) {
		return null != list ? list.isEmpty() : true;
	}

	public List<QiTextEntryMenu> getTextEntryMenuList() {
		return textEntryMenuList;
	}



	/**  This method will be used to set entry name. 
	 * 	 If Dialog is created for Text type Entry Screen for copying Entry Menu.
	 * 
	 * @param textEntryMenu the textEntryMenu to set
	 */
	public void setTextEntryMenuList(List<QiTextEntryMenu> textEntryMenuList) {
		this.textEntryMenuList = textEntryMenuList;
	}

	

	/** This method will be used to set entry screen dto. If Dialog is created for Image type Entry Screen.
	 * 
	 * @param entryScreenDto the entryScreenDto to set
	 */

	public void setEntryScreenDto(QiEntryScreenDto entryScreenDto) {
		this.entryScreenDto = entryScreenDto;
	}
	
	public void setTextEntryMenuMap(Map<String, List<QiRegionalPartDefectLocationDto>> textEntryMenuMap) {
		this.textEntryMenuMap = textEntryMenuMap;
	}
	
	/**
	 * @return
	 */
	private List<Integer> getRegionalIdsList() {
		List<Integer> regionalDefectCombList = new ArrayList<Integer>();
		for(QiRegionalPartDefectLocationDto dto : getDialog().getAssignedPartdefectPanel().getTable().getItems()){
			regionalDefectCombList.add(dto.getRegionalDefectCombinationId());
		}
		return regionalDefectCombList;
	}
	
	
	/**
	 * @return
	 */
	private List<Integer> getRegionalIdsListForMenu(String textMenu) {
		List<Integer> regionalDefectCombList = new ArrayList<Integer>();
		if(StringUtils.isBlank(textMenu))  {
			regionalDefectCombList = getRegionalIdsList();
		}
		else  {
			for(QiRegionalPartDefectLocationDto dto : textEntryMenuMap.get(textMenu)){
				regionalDefectCombList.add(dto.getRegionalDefectCombinationId());
			}
		}
		return regionalDefectCombList;
	}
	
	
	/** This method will copy local attribute and headless codes associated with pdcs being copied
	 * @param textEntryMenu
	 */
	private void copyLocalAttributes(String textEntryMenu, List<Integer> regionalIdList) throws Exception {
		List<QiExternalSystemDefectMap> headlessCodes = new ArrayList<QiExternalSystemDefectMap>();
		List<QiLocalDefectCombination> defectCombinationList = new ArrayList<QiLocalDefectCombination>();
		
		QiEntryScreenId fromEntryScreenId = new QiEntryScreenId();
		QiEntryScreenId toEntryScreenId = new QiEntryScreenId();
		
		fromEntryScreenId.setEntryModel(entryScreenDto.getEntryModel());
		fromEntryScreenId.setEntryScreen(entryScreenDto.getEntryScreen());
		fromEntryScreenId.setIsUsed(entryScreenDto.getIsUsedVersion());
		
		QiEntryScreenDto toQiEntryScreenDto = getDialog().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();
		toEntryScreenId.setEntryModel(toQiEntryScreenDto.getEntryModel());
		toEntryScreenId.setEntryScreen(toQiEntryScreenDto.getEntryScreen());
		toEntryScreenId.setIsUsed(toQiEntryScreenDto.getIsUsedVersion());
		
		try {
			defectCombinationList = getModel().findAllLocalDefectCombinationsByLocalDefectId(fromEntryScreenId, toEntryScreenId,
					regionalIdList);
		
			if(!isListEmpty(defectCombinationList) && !isPddaConfirmed){
				copyPDDAInfo = MessageDialog.confirm(getDialog(), "Are you sure you want to copy PDDA Info?");
				isPddaConfirmed=true;
			}
			for (QiLocalDefectCombination localDefect : defectCombinationList) {
				
				QiLocalDefectCombination qiLocalDefectCombination = new QiLocalDefectCombination();
				qiLocalDefectCombination.setRegionalDefectCombinationId((Integer)localDefect.getRegionalDefectCombinationId());
				qiLocalDefectCombination.setLocalTheme(localDefect.getLocalTheme());
				qiLocalDefectCombination.setEngineFiringFlag(localDefect.getEngineFiringFlag());
				qiLocalDefectCombination.setRepairAreaName(localDefect.getRepairAreaName());
				qiLocalDefectCombination.setRepairMethod(localDefect.getRepairMethod());
				qiLocalDefectCombination.setRepairMethodTime(localDefect.getRepairMethodTime());
				qiLocalDefectCombination.setEstimatedTimeToFix(localDefect.getEstimatedTimeToFix());
				qiLocalDefectCombination.setEntrySiteName(localDefect.getEntrySiteName());
				qiLocalDefectCombination.setEntryPlantName(toQiEntryScreenDto.getPlantName());
				qiLocalDefectCombination.setReportable(localDefect.getReportable());
				qiLocalDefectCombination.setEntryScreen(toQiEntryScreenDto.getEntryScreen());
				qiLocalDefectCombination.setEntryModel(toQiEntryScreenDto.getEntryModel());
				qiLocalDefectCombination.setIsUsed(toQiEntryScreenDto.getIsUsedVersion());
				qiLocalDefectCombination.setTextEntryMenu(StringUtils.trimToEmpty(textEntryMenu));
				qiLocalDefectCombination.setDefectCategoryName(localDefect.getDefectCategoryName());
				qiLocalDefectCombination.setResponsibleLevelId(localDefect.getResponsibleLevelId());
				if(copyPDDAInfo)
					qiLocalDefectCombination.setPddaResponsibilityId(localDefect.getPddaResponsibilityId());
				else
					qiLocalDefectCombination.setPddaResponsibilityId(null);
				qiLocalDefectCombination.setCreateUser(getUserId());
				qiLocalDefectCombination.setUpdateTimestamp(null);
				qiLocalDefectCombination.setUpdateUser(null);

				QiLocalDefectCombination newLocalDefectCombination = new QiLocalDefectCombination();
				try {
					newLocalDefectCombination = getModel().copyLocalDefectCombination(qiLocalDefectCombination);
				} catch (Exception e) {
					displayErrorMessage("Problem occurred while copying local attribute data.", "Problem occurred while creating LDC");
					throw e;
				}

				//only if copying to a different entry model, copy the headless codes
				//if same entry model, headless codes should not be duplicated
				if(!fromEntryScreenId.getEntryModel().equalsIgnoreCase(toEntryScreenId.getEntryModel()))  {
					List<QiExternalSystemDefectMap> externalSystemDefectMapList = getModel().findExtSystemByLocalDefectCombId(localDefect.getLocalDefectCombinationId());
					if(externalSystemDefectMapList != null && !externalSystemDefectMapList.isEmpty())  {
						for(QiExternalSystemDefectMap srcMapEntry : externalSystemDefectMapList){
							//look for existing map for part/defect/entry-model/ext system
							//if map exists, do not copy
							QiExternalSystemDefectMap existingMap = getModel().findByPartAndDefectCodeExternalSystemAndEntryModel(
									srcMapEntry.getExternalPartCode(),
									srcMapEntry.getExternalDefectCode(),
									srcMapEntry.getExternalSystemName(),
									toEntryScreenId.getEntryModel()
							);
							if(existingMap != null)  {
								continue;
							}
							headlessCodes.clear();
							QiExternalSystemDefectMap newMap = new QiExternalSystemDefectMap();
							newMap.setExternalSystemName(srcMapEntry.getExternalSystemName());
							newMap.setExternalPartCode(srcMapEntry.getExternalPartCode());
							newMap.setExternalDefectCode(srcMapEntry.getExternalDefectCode());
							newMap.setEntryModel(toEntryScreenId.getEntryModel());
							newMap.setLocalDefectCombinationId(newLocalDefectCombination.getLocalDefectCombinationId());
							newMap.setCreateUser(getUserId());
							newMap.setUpdateTimestamp(null);
							newMap.setUpdateUser(null);
							headlessCodes.add(newMap);
						}
						try {
							if(!isListEmpty(headlessCodes))  {
								getModel().copyHeadlessCodes(headlessCodes);
							}
						} catch (Exception e) {
							displayErrorMessage("Problem occurred while copying headless codes.", "Problem occurred while saving headless codes.");
							throw e;
						}
					}
				}
			}
		
		} catch (Exception e) {
			displayErrorMessage("Problem occurred while copying PDCs.", "Problem occurred while copying attributes/headless codes.");
			throw e;
		}					
	}

	
	private final ChangeListener<? super String> plantChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue ov, String t, String newValue) {
			if(null!=getDialog().getEntryModelComboBox().getItems()){
				populateEntryModelComboBox();
			}
		}
	};

	/**
	 * populates Entry Model combobox
	 */
	public void populateEntryModelComboBox() {
		disableAndResetAll(false);
		getDialog().getEntryModelComboBox().getItems().clear();
		getDialog().getEntryModelComboBox().getItems().addAll(FXCollections.observableArrayList(getModel().getEntryModelForProductType(StringUtils.trimToEmpty(getDialog().getProductTypeComboBox().getValue()))));
	}
	
	private final ChangeListener<String> productTypeChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue ov, String t, String newValue) {
			getDialog().getEntryScreenPanel().getTable().getItems().removeAll(getDialog().getEntryScreenPanel().getTable().getItems());
			populateEntryModelComboBox();
			Logger.getLogger().check("Product Type : " + newValue.trim() + " selected");
		}
	};

	private final ChangeListener<String> entryModelChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue ov, String t, String t1) {
			disableAndResetAll(true);
			QiEntryScreenDto sourceDto = getEntryScreenDto();
			String plantName = StringUtils.trimToEmpty(getDialog().getPlantComboBox().getSelectionModel().getSelectedItem());
			String entryModel = getDialog().getEntryModelComboBox().getSelectionModel().getSelectedItem();
			List<QiEntryScreenDto> entryScreenList = getModel().getEntryScreenDataByImage(entryModel, plantName, sourceDto.isImage());
			if(!getDialog().getScreenType().equals(CopyPartDefectCombinationDialog.CopyMode.MOVE_PDC) && sourceDto != null)  {
				entryScreenList.remove(sourceDto);
			}
			getDialog().getEntryScreenPanel().setData(entryScreenList);
			Logger.getLogger().check("Entry Model : " + t1 + " selected");
		}		
	};

	private void addDropDownListner() {
		getDialog().getPlantComboBox().valueProperty().addListener(plantChangeListener);
		getDialog().getProductTypeComboBox().valueProperty().addListener(productTypeChangeListener);
		getDialog().getEntryModelComboBox().valueProperty().addListener(entryModelChangeListener);
	}
	/** This method will disable and reset components.
	 *  
	 */
	private void disableAndResetAll(boolean isEnable) {
		clearDisplayMessage();
		getDialog().getAssignedPartdefectPanel().setDisable(!isEnable);

		getDialog().getOkButton().setDisable(!isEnable);
	}
	
	public QiEntryScreenDto getEntryScreenDto() {
		return entryScreenDto;
	}



	public List<QiTextEntryMenu> getSourceMenuList() {
		return sourceMenuList;
	}



	public void setSourceMenuList(List<QiTextEntryMenu> sourceMenuList) {
		this.sourceMenuList = sourceMenuList;
	}



	public List<Integer> getValidPDCList() {
		return validPDCList;
	}



	public void setValidPDCList(List<Integer> validPDCList) {
		this.validPDCList = validPDCList;
	}

}
