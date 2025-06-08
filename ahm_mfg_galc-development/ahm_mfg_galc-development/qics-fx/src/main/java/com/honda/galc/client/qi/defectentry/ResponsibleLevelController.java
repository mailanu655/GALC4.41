package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author vcc44349
 * This is a class to encapsulate behaviour of responsibility combo box selection
 * It needs to be initialized with three combo box for L1, L2, L3
 * The helper will provide listeners and other utility methods to filter the combo box values
 */
public class ResponsibleLevelController  implements EventHandler<ActionEvent>{
	
	private String site = "";
	private String plant = "";
	private String dept = "";
	private volatile int comboLvlPrimary = 0;
	private volatile int comboLvlSecondary = 0;
	private boolean isMaint = false;
	private static Map<Integer,ResponsibleLevelController> controllers = new HashMap<Integer,ResponsibleLevelController>();
	private ResponsibleLevelPanel responsiblePanel = null;
	private DefectEntryModel model;
	private QiResponsibleLevel originalResponsibleLevel = null;
	private List<QiStationResponsibilityDto> assignedStationResponsibilities;
	private String processPointId = "";
	private boolean isShowL2L3 = false;
	private boolean isResponsibilityAccessible = false;
	private ResponsibleLevelController()
	{
	}
	
	public static ResponsibleLevelController getInstance(DefectEntryModel m, Object view, String ppId)
	{
		if(controllers == null || controllers.isEmpty())  {
			controllers = new HashMap<>();
		}
		if(controllers.get(view.hashCode()) == null)  {
			ResponsibleLevelController newController = new ResponsibleLevelController();
			newController.setModel(m);
			newController.setProcessPointId(ppId);
			String qiSettingVal = getQiConfigSetting(ppId, QiEntryStationConfigurationSettings.SHOW_L2_L3);
			if(!StringUtils.isBlank(qiSettingVal) && qiSettingVal.trim().equalsIgnoreCase("Yes"))  {
				newController.isShowL2L3 = true;
			}
			qiSettingVal = getQiConfigSetting(ppId, QiEntryStationConfigurationSettings.RESPONSIBILITY);
			if(!StringUtils.isBlank(qiSettingVal) && qiSettingVal.trim().equalsIgnoreCase("Yes"))  {
				newController.setResponsibilityAccessible(true);
			}
			controllers.put(view.hashCode(), newController);
		}
		return controllers.get(view.hashCode());
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(responsiblePanel.getClearResp()))  {
			comboLvlPrimary = 0;
			comboLvlSecondary = 0;
			String dept = responsiblePanel.getDepartmentComboBox().getControl().getValue();
			if(!StringUtils.isBlank(dept))  {
				loadRespComboBoxesWithAllValues(responsiblePanel.getDepartmentComboBox().getControl().getValue().toString());
			}
		}
		else if(actionEvent.getSource().equals(responsiblePanel.getResetResp()))  {
			comboLvlPrimary = 1;
			comboLvlSecondary = 2;
			if(originalResponsibleLevel!=null) {
				responsiblePanel.getSiteComboBox().getControl().getSelectionModel().select(originalResponsibleLevel.getSite());
				responsiblePanel.getPlantComboBox().getControl().getSelectionModel().select(originalResponsibleLevel.getPlant());
				responsiblePanel.getDepartmentComboBox().getControl().getSelectionModel().select(originalResponsibleLevel.getDepartment());
				responsiblePanel.getResponsibleLevel1ComboBox().getControl().getSelectionModel().select(ResponsibleLevelController.getKeyValue(originalResponsibleLevel.getResponsibleLevelName()));
				if(isShowL2L3)  {
					selectParentLevels(getOriginalResponsibleLevel());
				}
			}
		}
	}

	/**
	 * Adds the listener.
	 */
	public void addListener() {
		addResponsibleLevel1ComboBoxListener();
		addResponsibleLevel2ComboBoxListener();
		addResponsibleLevel3ComboBoxListener();
	}
	
	
	/**
	 * Adds the responsible level1 combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addResponsibleLevel1ComboBoxListener() {
		responsiblePanel.getResponsibleLevel1ComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<String,Integer>>() {
			 public void changed(ObservableValue<? extends KeyValue<String,Integer>> ov,  KeyValue<String,Integer> old_val, KeyValue<String,Integer> new_val) { 
				 loadWhenLevel1Changed(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the responsible level2 combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addResponsibleLevel2ComboBoxListener() {
		responsiblePanel.getResponsibleLevel2ComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<String,Integer>>() {
			 public void changed(ObservableValue<? extends KeyValue<String,Integer>> ov,  KeyValue<String,Integer> old_val, KeyValue<String,Integer> new_val) { 
				 loadWhenLevel2Changed(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the responsible level2 combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addResponsibleLevel3ComboBoxListener() {
		responsiblePanel.getResponsibleLevel3ComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<String,Integer>>() {
			 public void changed(ObservableValue<? extends KeyValue<String,Integer>> ov,  KeyValue<String,Integer> old_val, KeyValue<String,Integer> new_val) { 
				 loadWhenLevel3Changed(new_val);
			 } 
		});
	}
	
	/**
	 * Load responsible level3 combo box.
	 *
	 * @param department the department
	 */
	@SuppressWarnings("unchecked")
	public void loadWhenLevel3Changed(KeyValue<String,Integer> new_val) {
		if(new_val == null)  return;
		
		String site = responsiblePanel.getSiteComboBox().getControl().getValue().toString();
		String plant = responsiblePanel.getPlantComboBox().getControl().getValue().toString();
		String department = responsiblePanel.getDepartmentComboBox().getControl().getValue().toString();
		LabeledComboBox<KeyValue<String,Integer>> l2ComboBox = responsiblePanel.getResponsibleLevel2ComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l1ComboBox = responsiblePanel.getResponsibleLevel1ComboBox();
		
		if(comboLvlPrimary == 0)  { //primary is open, set level 3 as primary
			comboLvlPrimary = 3;
		}
		else if(comboLvlPrimary == 3)  { //level 3 is already the primary, clear secondary
			comboLvlSecondary = 0;
		}
		else if(comboLvlSecondary == 0)  comboLvlSecondary = 3;//some other level is primary, but secondary is open
		if(comboLvlPrimary != 3 && comboLvlSecondary != 3)  return;
		
		if((comboLvlPrimary == 3 || comboLvlPrimary == 2))  {
			KeyValue<String, Integer> currentKV = l1ComboBox.getControl().getSelectionModel().getSelectedItem();
			clearComboBox(l1ComboBox);
			List<QiResponsibleLevel> responsibleLevel1List = null;
			List<KeyValue<String,Integer>> listOfL1 = null;
			if(assignedStationResponsibilities != null && !assignedStationResponsibilities.isEmpty()) {
				responsibleLevel1List = getModel().findAllAssignedLevel1HavingSameLevel3(site, plant, department, new_val.getKey(), getProcessPointId());
				listOfL1 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
			} else {
				responsibleLevel1List = getModel().findAllLevel1HavingSameLevel3(site, plant, department, new_val.getKey());
				listOfL1 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
			}
			if(listOfL1 != null && !listOfL1.isEmpty())  {
				l1ComboBox.getControl().getItems().addAll(listOfL1);
				if(l1ComboBox.getControl().getItems().size() == 1)  {
					l1ComboBox.getControl().getSelectionModel().select(0);
				}
				else if(currentKV != null && listOfL1.contains(currentKV))  {
					l1ComboBox.getControl().getSelectionModel().select(currentKV);					
				}
				else  {
					enableMultiSelectComboBox(l1ComboBox);
				}
			}
		}
		if((comboLvlPrimary == 3 || comboLvlPrimary == 1))  {
			clearComboBox(l2ComboBox);
			List<QiResponsibleLevel> listOfL2=getModel().findAllLevel2HavingSameLevel3(site, plant, department, new_val.getKey());
			if(listOfL2 != null && !listOfL2.isEmpty())  {
				l2ComboBox.getControl().getItems().addAll(getUniqueListOfResponsibleLevelNames(listOfL2));
				if(l2ComboBox.getControl().getItems().size() == 1)  {
					l2ComboBox.getControl().getSelectionModel().select(0);
				}
				else  {
					enableMultiSelectComboBox(l2ComboBox);
				}
			}
		}
	}
	
	/**
	 * Load responsible level3 combo box.
	 *
	 * @param department the department
	 */
	@SuppressWarnings("unchecked")
	public void loadWhenLevel2Changed(KeyValue<String,Integer> new_val) {
				
		if(new_val == null)  {
			return;
		}
		LabeledComboBox<KeyValue<String,Integer>> l3ComboBox = responsiblePanel.getResponsibleLevel3ComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l1ComboBox = responsiblePanel.getResponsibleLevel1ComboBox();
		String site = responsiblePanel.getSiteComboBox().getControl().getValue().toString();
		String plant = responsiblePanel.getPlantComboBox().getControl().getValue().toString();
		String department = responsiblePanel.getDepartmentComboBox().getControl().getValue().toString();
		if(comboLvlPrimary == 0)  { //primary is open, set level 2 as primary
			comboLvlPrimary = 2;
		}
		else if(comboLvlPrimary == 2)  { //level 2 is already the primary, clear secondary
			comboLvlSecondary = 0;
		}
		else if(comboLvlSecondary == 0)  comboLvlSecondary = 2;//some other level is primary, but secondary is open
		
		if(comboLvlPrimary != 2 && comboLvlSecondary != 2)  return;
		
		//enable L3 only when L1 is primary and L2 is selected, or L2 is primary and L1 is selected
		if(comboLvlPrimary == 1 || comboLvlPrimary == 2)  {  
			clearComboBox(l3ComboBox);
			List<QiResponsibleLevel> listOfL3=getModel().findAllLevel3HavingSameLevel2(site, plant, department, new_val.getKey());
			if(listOfL3 != null && !listOfL3.isEmpty())  {
				l3ComboBox.getControl().getItems().addAll(getUniqueListOfResponsibleLevelNames(listOfL3));
				if(l3ComboBox.getControl().getItems().size() == 1)  {
					l3ComboBox.getControl().getSelectionModel().select(0);
				}
				else  {
					enableMultiSelectComboBox(l3ComboBox);
				}
			}
			else  {
				l3ComboBox.setEnable(false);
			}
		}
		else  {
			l3ComboBox.setEnable(false);
		}
		
		if((comboLvlPrimary == 2 || comboLvlPrimary == 3))  {
			KeyValue<String, Integer> currentKV = l1ComboBox.getControl().getSelectionModel().getSelectedItem();
			clearComboBox(l1ComboBox);
			List<QiResponsibleLevel> responsibleLevel1List = null;
			List<KeyValue<String,Integer>> listOfL1 = null;
			if(assignedStationResponsibilities != null && !assignedStationResponsibilities.isEmpty()) {
				responsibleLevel1List = getModel().findAllAssignedLevel1HavingSameLevel2(site, plant, department, new_val.getKey(), getProcessPointId());
				listOfL1 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
			} else {
				responsibleLevel1List = getModel().findAllLevel1HavingSameLevel2(site, plant, department, new_val.getKey());
				listOfL1 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
			}
			if(listOfL1 != null && !listOfL1.isEmpty())  {
				l1ComboBox.getControl().getItems().addAll(listOfL1);
				if(l1ComboBox.getControl().getItems().size() == 1)  {
					l1ComboBox.getControl().getSelectionModel().select(0);
				}
				else if(currentKV != null && listOfL1.contains(currentKV))  {
					l1ComboBox.getControl().getSelectionModel().select(currentKV);
					
				}
				else  {
					enableMultiSelectComboBox(l1ComboBox);
				}
			}
		}
	}
	
	
	/**
	 * Load responsible level2 and level2 combo box based on plant site and dept.
	 *
	 * @param new_val the new_val
	 */
	@SuppressWarnings("unchecked")
	public void loadWhenLevel1Changed(KeyValue<String, Integer> new_val) {
		if(new_val == null)  {
			return;
		}
		LabeledComboBox<KeyValue<String,Integer>> l2ComboBox = responsiblePanel.getResponsibleLevel2ComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l3ComboBox = responsiblePanel.getResponsibleLevel3ComboBox();
		String site = responsiblePanel.getSiteComboBox().getControl().getValue().toString();
		String plant = responsiblePanel.getPlantComboBox().getControl().getValue().toString();
		String department = responsiblePanel.getDepartmentComboBox().getControl().getValue().toString();
		
		if(comboLvlPrimary == 0)  { //primary is open, set level 1 as primary
			comboLvlPrimary = 1;
		}
		else if(comboLvlPrimary == 1)  { //level 1 is already the primary, clear secondary
			comboLvlSecondary = 0;
		}
		else if(comboLvlSecondary == 0)  comboLvlSecondary = 1;//some other level is primary, but secondary is open
		if(comboLvlPrimary != 1 && comboLvlSecondary != 1)  return;
		
		if((comboLvlPrimary == 1 || comboLvlPrimary == 3))  {
			KeyValue<String, Integer> currentKV = l2ComboBox.getControl().getSelectionModel().getSelectedItem();
			clearComboBox(l2ComboBox);
			List<KeyValue<String,Integer>> listOfL2 = null;
			List<QiResponsibleLevel> responsibleLevel2List = null;
			if(assignedStationResponsibilities != null && !assignedStationResponsibilities.isEmpty()) {
				responsibleLevel2List = getModel().findAllAssignedLevel2HavingSameLevel1(site, plant, department, new_val.getKey(), getProcessPointId());
				listOfL2 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel2List);
			} else {
				responsibleLevel2List = getModel().findAllLevel2HavingSameLevel1(site, plant, department, new_val.getKey());
				listOfL2 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel2List);
			}

			
			if(listOfL2 != null && !listOfL2.isEmpty())  {
				l2ComboBox.getControl().getItems().addAll(listOfL2);
				if(l2ComboBox.getControl().getItems().size() == 1)  {
					l2ComboBox.getControl().getSelectionModel().select(0);
				}
				else if(currentKV != null && listOfL2.contains(currentKV))  {
					l2ComboBox.getControl().getSelectionModel().select(currentKV);					
				}
				else  {
					enableMultiSelectComboBox(l2ComboBox);
				}
			}
			else  {
				l2ComboBox.setEnable(false);
			}
		}
		
		//enable L3 only when L1 is primary and L2 is selected, or L2 is primary and L1 is selected
		if(comboLvlPrimary == 1 || comboLvlPrimary == 2)  {
			clearComboBox(l3ComboBox);
			KeyValue<String,Integer> kv = l2ComboBox.getControl().getValue();
			List<QiResponsibleLevel> listOfL3 = null;
			if(kv != null)  {
				listOfL3 = getModel().findAllLevel3HavingSameLevel2(site, plant, department, kv.getKey());
			}
			if(listOfL3 != null && !listOfL3.isEmpty())  {
				l3ComboBox.getControl().getItems().addAll(getUniqueListOfResponsibleLevelNames(listOfL3));
				if(l3ComboBox.getControl().getItems().size() == 1)  {
					l3ComboBox.getControl().getSelectionModel().select(0);
				}
				else  {
					enableMultiSelectComboBox(l3ComboBox);
				}
			}
			else  {
				l3ComboBox.setEnable(false);
			}
		}
		else  {
			l3ComboBox.setEnable(false);
		}
	}
	
	private void enableMultiSelectComboBox(LabeledComboBox<KeyValue<String,Integer>> combo)  {
		if(combo != null)  {
			combo.setEnable(true);
			combo.getControl().getSelectionModel().select(null);				
			combo.getControl().setPromptText("Select");
		}		
	}
	/**
	 * Load updated responsibility data.
	 *
	 * @param responsibleLevel the responsible level
	 */
	@SuppressWarnings("unchecked")
	public void selectParentLevels(QiResponsibleLevel l1) {
		QiResponsibleLevel nextLevel = null;
		QiResponsibleLevel l3 = null;
		
		if(l1 == null)  return;
		
		nextLevel = getModel().findResponsibleLevelById(l1.getUpperResponsibleLevelId());
		if(nextLevel == null)  {
			clearComboBox(responsiblePanel.getResponsibleLevel2ComboBox());
			clearComboBox(responsiblePanel.getResponsibleLevel3ComboBox());
			return;  //nothing to do
		}
		if(nextLevel.getLevel() == (short)2)  { // level 2
			//select level2, this will trigger filtering of level 3 list
			responsiblePanel.getResponsibleLevel2ComboBox().getControl().getSelectionModel().select(getKeyValue(nextLevel.getResponsibleLevelName()));
			if(nextLevel.getUpperResponsibleLevelId() != 0) {  //level 2 has a level 3
				l3 = getModel().findResponsibleLevelById(nextLevel.getUpperResponsibleLevelId()); 
				responsiblePanel.getResponsibleLevel3ComboBox().getControl().getSelectionModel().select(getKeyValue(l3.getResponsibleLevelName()));
			}
		}
		else if(nextLevel.getLevel() == (short)3) { //level 1 has direct level 3 parent
			responsiblePanel.getResponsibleLevel3ComboBox().getControl().getSelectionModel().select(getKeyValue(nextLevel.getResponsibleLevelName()));
		}
		
	}
	

	/**
	 * This method is used to Enable/Disable Responsibility Combobox
	 */
	public void disableComboBoxes(boolean isDisabled) {
			responsiblePanel.getResponsibleLevel1ComboBox().getControl().setDisable(isDisabled);
			responsiblePanel.getResponsibleLevel2ComboBox().getControl().setDisable(isDisabled);
			responsiblePanel.getResponsibleLevel3ComboBox().getControl().setDisable(isDisabled);
			responsiblePanel.getClearResp().setDisable(isDisabled);
			responsiblePanel.getResetResp().setDisable(isDisabled);
	}

	@SuppressWarnings("unchecked")
	public void loadRespComboBoxesWithAllValues(String department)
	{
		dept = department;
		clearAll();		
		if(StringUtils.isBlank(department))  return;
		
		List<QiResponsibleLevel> responsibleLevel1List = null;
		List<KeyValue<String,Integer>> listOfL1 = null;
		String site = responsiblePanel.getSiteComboBox().getControl().getSelectionModel().getSelectedItem();
		String plant = responsiblePanel.getPlantComboBox().getControl().getSelectionModel().getSelectedItem();
		responsiblePanel.getResponsibleLevel1ComboBox().getControl().getSelectionModel().select(null);
		responsiblePanel.getResponsibleLevel1ComboBox().getControl().getItems().clear();
		List<String> lvlNames = null;
		if(!isListNullOrEmpty(getAssignedStationResponsibilities())) {
			lvlNames = getModel().getDefectEntryCacheUtil().getLevel1ListFromResponsibilities(getAssignedStationResponsibilities(), site, plant, department, false);
			listOfL1 = getUniqueListOfNames(lvlNames);
		} else {
			responsibleLevel1List = model.findAllBySitePlantDepartmentLevel(site, plant, department,(short)1);
			listOfL1 = getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
		}
		if(listOfL1 != null && !listOfL1.isEmpty())  {
			Collections.sort(listOfL1,ResponsibleLevelController.getKVComparator());
			responsiblePanel.getResponsibleLevel1ComboBox().getControl().getItems().addAll(listOfL1);
			getResponsiblePanel().getResponsibleLevel1ComboBox().getControl().setPromptText("Select");
			if(isResponsibilityAccessible() && isShowL2L3())  {
				enableMultiSelectComboBox(getResponsiblePanel().getResponsibleLevel1ComboBox());
			}
		}
		
		if(!isResponsibilityAccessible() || !isShowL2L3())  return;
	
		List<QiResponsibleLevel> responsibleLevel2List = null;
		List<KeyValue<String,Integer>> listOfL2 = null;
		if(!isListNullOrEmpty(getAssignedStationResponsibilities())) {
			responsibleLevel2List = model.findAllLevel2HavingAssignedLevel1(site, plant, department,getProcessPointId());
			listOfL2 = getUniqueListOfResponsibleLevelNames(responsibleLevel2List);
		}
		else {
			responsibleLevel2List = model.findAllBySitePlantDepartmentLevel(site, plant, department,(short)2);
			listOfL2 = getUniqueListOfResponsibleLevelNames(responsibleLevel2List);			
		}
		if(listOfL2 != null && !listOfL2.isEmpty())  {
			Collections.sort(listOfL2,ResponsibleLevelController.getKVComparator());
			getResponsiblePanel().getResponsibleLevel2ComboBox().getControl().getItems().addAll(listOfL2);		
			enableMultiSelectComboBox(getResponsiblePanel().getResponsibleLevel2ComboBox());
		}
		
		List<QiResponsibleLevel> responsibleLevel3List = null;
		List<KeyValue<String,Integer>> listOfL3 = null;
		if(!isListNullOrEmpty(getAssignedStationResponsibilities())) {
			responsibleLevel3List = model.findAllLevel3HavingAssignedLevel1(site, plant, department,getProcessPointId());
			listOfL3 = getUniqueListOfResponsibleLevelNames(responsibleLevel3List);
		}
		else {
			responsibleLevel3List = model.findAllBySitePlantDepartmentLevel(site, plant, department,(short)3);
			listOfL3 = getUniqueListOfResponsibleLevelNames(responsibleLevel3List);			
		}
		if(listOfL3 != null && !listOfL3.isEmpty())  {
			Collections.sort(listOfL3,ResponsibleLevelController.getKVComparator());
			getResponsiblePanel().getResponsibleLevel3ComboBox().getControl().getItems().addAll(listOfL3);		
			getResponsiblePanel().getResponsibleLevel3ComboBox().getControl().getSelectionModel().select(null);				
			getResponsiblePanel().getResponsibleLevel3ComboBox().getControl().setPromptText("Select");
		}
		getResponsiblePanel().getResponsibleLevel3ComboBox().setEnable(false);
		
	}
	
	public static String getQiConfigSetting(String processPointId, QiEntryStationConfigurationSettings qiSetting)  {
		if(StringUtils.isBlank(processPointId) || qiSetting == null)  return null;
		QiStationConfiguration qiEntryStnSetting = ServiceFactory.getDao(QiStationConfigurationDao.class)
				.findValueByProcessPointAndPropKey(processPointId, qiSetting.getSettingsName());
		String cfgValue = qiSetting.getDefaultPropertyValue();
        if(null != qiEntryStnSetting && !StringUtils.isBlank(qiEntryStnSetting.getPropertyValue())) {
        	cfgValue = qiEntryStnSetting.getPropertyValue();
		}
        return cfgValue;
	}

	public static List<KeyValue<String,Integer>> getUniqueListOfResponsibleLevelNames(List<QiResponsibleLevel> listOfLevels)  {
		List<String> lvlNames = null;
		if(listOfLevels == null || listOfLevels.isEmpty())  return null;
		lvlNames = new ArrayList<String>();
		for(QiResponsibleLevel qiResp : listOfLevels)  {
			lvlNames.add(qiResp.getResponsibleLevelName());
		}
		Set<String> newSet = new LinkedHashSet<String>();
		newSet.addAll(lvlNames);
		lvlNames.clear();
		lvlNames.addAll(newSet);
		return getKeyValueList(lvlNames.listIterator());
	}
	
	public static List<KeyValue<String,Integer>> getUniqueListOfNames(List<String> listOfLevels)  {
		if(listOfLevels == null || listOfLevels.isEmpty())  return null;
		HashMap<String,Integer> levelNamesMap = new HashMap<>();
		for(String lvlName : listOfLevels)  {
			if(lvlName == null) continue;
			String key = lvlName;
			if(levelNamesMap.get(key) == null)  {
				levelNamesMap.put(key, 1);
			}
			else  {
				int n = levelNamesMap.get(key);
				levelNamesMap.put(key, n+1);				
			}
		}
		ArrayList<KeyValue<String,Integer>> levelNamesList = new ArrayList<>();
		
		for (String key : levelNamesMap.keySet())  {
			KeyValue<String,Integer> keyVal = getKeyValue(key,levelNamesMap.get(key));
			levelNamesList.add(keyVal);
		}
		Collections.sort(levelNamesList,
				new Comparator<KeyValue<String,Integer>>()  {
					public int compare(KeyValue<String,Integer> kv1, KeyValue<String,Integer> kv2)  {
						if(kv1 == null && kv2 == null)  return 0;
						else if(kv1.getKey() == null && kv2.getKey() == null)  return 0;
						else if(kv1 == null || kv1.getKey() == null)  return -1;
						else if(kv2 == null || kv2.getKey() == null)  return 1;
						else return kv1.getKey().compareTo(kv2.getKey());
					}
		});
		return levelNamesList;
	}
	
	/**
	 * Gets the key value.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the key value
	 */
	public static KeyValue<String, Integer> getKeyValue(final String key, Integer value){
		KeyValue<String, Integer> kv = new KeyValue<String, Integer>(key, value){
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return key;
			}
		};
		return kv;
	}
	
	public static KeyValue<String, Integer> getKeyValue(final String key){
		return getKeyValue(key, 1);
	}
	
	public static List<KeyValue<String, Integer>> getKeyValueList(final Iterator<String> kList){
		if(kList == null || !kList.hasNext())  return null;
		List<KeyValue<String, Integer>> kvList = new ArrayList<KeyValue<String, Integer>>();
		while(kList.hasNext())  {
			kvList.add(getKeyValue(kList.next()));
		}
		return kvList;
	}
	
	public static Comparator<KeyValue<String,Integer>> getKVComparator()  {
		return new Comparator<KeyValue<String,Integer>>()  {
			public int compare(KeyValue<String,Integer> kv1, KeyValue<String,Integer> kv2)  {
				if(kv1 == null && kv2 == null)  return 0;
				else if(kv1.getKey() == null && kv2.getKey() == null)  return 0;
				else if(kv1 == null || kv1.getKey() == null)  return -1;
				else if(kv2 == null || kv2.getKey() == null)  return 1;
				else return kv1.getKey().compareTo(kv2.getKey());
			}};
	}
	/**
	 * Clear combo box.
	 *
	 * @param comboBox the combo box
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public void clearAll() {
		clearComboBox(getResponsiblePanel().getResponsibleLevel1ComboBox());
		clearComboBox(getResponsiblePanel().getResponsibleLevel2ComboBox());
		clearComboBox(getResponsiblePanel().getResponsibleLevel3ComboBox());
		comboLvlPrimary = 0;
		comboLvlSecondary = 0;
	}
	
	/**
	 * Clear combo box.
	 *
	 * @param comboBox the combo box
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private static void clearComboBox(LabeledComboBox<KeyValue<String, Integer>> comboBox ) {
		comboBox.getControl().getItems().clear();
		comboBox.getControl().setValue(null);
		comboBox.getControl().getSelectionModel().clearSelection();
	}
	
	/**
	 * Checks if is validate responsible level1.
	 *
	 * @return true, if is validate responsible level1
	 */
	public boolean isValidateResponsibleLevel() {
		
		LabeledComboBox<KeyValue<String,Integer>> l1ComboBox = responsiblePanel.getResponsibleLevel1ComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l2ComboBox = responsiblePanel.getResponsibleLevel2ComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l3ComboBox = responsiblePanel.getResponsibleLevel3ComboBox();
		
		boolean r1Pass = l1ComboBox.getControl().getSelectionModel().selectedIndexProperty().get() >= 0;
		boolean r2Pass = isListNullOrEmpty(l2ComboBox.getControl().getItems()) || l2ComboBox.getControl().getSelectionModel().selectedIndexProperty().get() >= 0;
		boolean r3Pass = isListNullOrEmpty(l3ComboBox.getControl().getItems()) || l3ComboBox.getControl().getSelectionModel().selectedIndexProperty().get() >= 0;
		
		if(r1Pass && r2Pass && r3Pass)  return true;
		else  return false;
	}
	

	public QiResponsibleLevel findResponsibleLevel1()  {
		
		QiResponsibleLevel l1 = null;
		LabeledComboBox<String> siteComboBox = responsiblePanel.getSiteComboBox();
		LabeledComboBox<String> plantComboBox = responsiblePanel.getPlantComboBox();
		LabeledComboBox<String> departmentComboBox = responsiblePanel.getDepartmentComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l1ComboBox = responsiblePanel.getResponsibleLevel1ComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l2ComboBox = responsiblePanel.getResponsibleLevel2ComboBox();
		LabeledComboBox<KeyValue<String,Integer>> l3ComboBox = responsiblePanel.getResponsibleLevel3ComboBox();
		
		KeyValue<String,Integer> r1 = (KeyValue<String,Integer>)l1ComboBox.getControl().getValue();
		KeyValue<String,Integer> r2 = (KeyValue<String,Integer>)l2ComboBox.getControl().getValue();
		KeyValue<String,Integer> r3 = (KeyValue<String,Integer>)l3ComboBox.getControl().getValue();
		String site = siteComboBox.getControl().getSelectionModel().getSelectedItem().toString();
		String plant = plantComboBox.getControl().getSelectionModel().getSelectedItem().toString();
		String dept = departmentComboBox.getControl().getSelectionModel().getSelectedItem().toString();
		
		l1 = findResponsibleLevel1ByLevelNames(site, plant, dept, r1, r2, r3);
		
		return l1;
	}
	
	public QiResponsibleLevel findResponsibleLevel1ByLevelNames(String site, String plant, String dept, KeyValue<String, Integer> r1, KeyValue<String, Integer> r2, KeyValue<String, Integer> r3)  {
		
		boolean siteSelected = !StringUtils.isBlank(site);
		boolean plantSelected = !StringUtils.isBlank(plant);
		boolean deptSelected = !StringUtils.isBlank(dept);
		boolean r1Selected = r1 != null && !StringUtils.isBlank(r1.getKey());
		boolean r2Selected = r2 != null && !StringUtils.isBlank(r2.getKey());
		boolean r3Selected = r3 != null && !StringUtils.isBlank(r3.getKey());
		
		if(!siteSelected || !plantSelected || !deptSelected || !r1Selected)  return null;
		
		QiResponsibleLevel l1 = null;
		if(!r2Selected && !r3Selected)  {  //only L1
			l1 = getModel().findLevel1BySitePlantDepartmentLevelNameAndLevel(site, plant, dept, r1.getKey());
		}
		else if(r2Selected && !r3Selected)  { //L1 and L2
			l1 = getModel().findBySitePlantDeptLvlNameUpperLvlNameAndLvl(site, plant, dept,  r1.getKey(),  r2.getKey(), 1, 2);
		}
		else if(r2Selected && r3Selected)  { //L2, L2 and L3
			l1 = getModel().findResponsibleLevel1BySitePlantDeptL1L2L3Names(site, plant, dept, r1.getKey(),  r2.getKey(), r3.getKey());
		}
		else if(r1Selected && r3Selected)  { //L1 and L3
			l1 = getModel().findBySitePlantDeptLvlNameUpperLvlNameAndLvl(site, plant, dept,  r1.getKey(),  r3.getKey(), 1, 3);
		}
		
		return l1;
	}
	
	/**
	 * This method is used to clear the exception display message.
	 */
	public static void clearDisplayMessage() {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
	}
	
	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return the plant
	 */
	public String getPlant() {
		return plant;
	}

	/**
	 * @param plant the plant to set
	 */
	public void setPlant(String plant) {
		this.plant = plant;
	}

	/**
	 * @return the dept
	 */
	public String getDept() {
		return dept;
	}

	/**
	 * @param dept the dept to set
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}

	/**
	 * @return the responsiblePanel
	 */
	public ResponsibleLevelPanel getResponsiblePanel() {
		return responsiblePanel;
	}

	/**
	 * @param responsiblePanel the responsiblePanel to set
	 */
	public void setResponsiblePanel(ResponsibleLevelPanel responsiblePanel) {
		this.responsiblePanel = responsiblePanel;
	}

	/**
	 * @return the isMaint
	 */
	public boolean isMaint() {
		return isMaint;
	}

	/**
	 * @param isMaint the isMaint to set
	 */
	public void setMaint(boolean isMaint) {
		this.isMaint = isMaint;
	}

	/**
	 * @return the model
	 */
	public DefectEntryModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(DefectEntryModel model) {
		this.model = model;
	}

	/**
	 * @return the originalResponsibleLevel
	 */
	public QiResponsibleLevel getOriginalResponsibleLevel() {
		return originalResponsibleLevel;
	}

	/**
	 * @param originalResponsibleLevel the originalResponsibleLevel to set
	 */
	public void setOriginalResponsibleLevel(QiResponsibleLevel originalResponsibleLevel) {
		this.originalResponsibleLevel = originalResponsibleLevel;
	}

	/**
	 * @return the assignedStationResponsibilities
	 */
	public List<QiStationResponsibilityDto> getAssignedStationResponsibilities() {
		return assignedStationResponsibilities;
	}

	/**
	 * @param assignedStationResponsibilities the assignedStationResponsibilities to set
	 */
	public void setAssignedStationResponsibilities(List<QiStationResponsibilityDto> assignedStationResponsibilities) {
		this.assignedStationResponsibilities = assignedStationResponsibilities;
	}

	/**
	 * @return the processPointId
	 */
	public String getProcessPointId() {
		return processPointId;
	}

	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	/**
	 * @return the isResponsibilityAccessible
	 */
	public boolean isResponsibilityAccessible() {
		return isResponsibilityAccessible;
	}

	/**
	 * @param isResponsibilityAccessible the isResponsibilityAccessible to set
	 */
	public void setResponsibilityAccessible(boolean isResponsibilityAccessible) {
		this.isResponsibilityAccessible = isResponsibilityAccessible;
	}

	/**
	 * @return the isShowL2L3
	 */
	public boolean isShowL2L3() {
		return isShowL2L3;
	}

	/**
	 * @param isShowL2L3 the isShowL2L3 to set
	 */
	public void setShowL2L3(boolean isShowL2L3) {
		this.isShowL2L3 = isShowL2L3;
	}
	
	boolean isListNullOrEmpty(List newList)  {
		return newList == null || newList.isEmpty();
	}


}
