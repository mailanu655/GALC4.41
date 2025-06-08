package com.honda.galc.client.teamleader.fx;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import org.apache.commons.collections.map.MultiValueMap;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.MassMessageType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/** * * 
* @author Fredrick Yessaian 
* @since Sep 03, 2014
*/

public class DivisionMessageSenderPanel extends MassMessageSenderPanel{

	private static final long serialVersionUID = 1L;
	private static String MAM_DIVISION = "MAM_DIVISION";
	private Map<String, String> grpIdDivision = new HashMap<String, String>();
	private HashSet<String> plantNames = new HashSet<String>();
	private MultiValueMap plantDivisionMap = new MultiValueMap();
	ComboBox<String> plantNameCombo = new ComboBox<String>();
	ComboBox<String> divisionNameCombo = new ComboBox<String>();
	
	public DivisionMessageSenderPanel() {
		super("Department Level Mass Message Sender", KeyEvent.VK_E);
	}

	@Override
	protected HBox constructControlPanelFromGroupIds() {
		setAssociateNo(this.getMainWindow().getApplicationContext().getUserId());
		List<ComponentProperty> componentLst = PropertyService.getComponentProperty(MAM_DIVISION);

		if (componentLst == null || componentLst.size() == 0) {
			return createInfoLabel("Please contact support team, Department id & LDAP group id not configured in the system", true);
		} else {

			for (ComponentProperty property : componentLst) {
				grpIdDivision.put(property.getPropertyKey(),
						property.getPropertyValue());
			}

			for (String userGroupId : getUserGroupLst(getAssociateNo())) {
				if (grpIdDivision.containsKey(userGroupId.trim())) {
					
					String divNameFromGroupId = (String) grpIdDivision.get((userGroupId.trim()));
					
					Division division = ServiceFactory.getDao(DivisionDao.class).findByDivisionId(divNameFromGroupId.trim());
					
					if(division != null){
						
						plantNames.add(division.getPlantName().trim());
						
						if(!plantDivisionMap.containsValue(division.getPlantName().trim(), division.getDivisionId().trim()))
							plantDivisionMap.put(division.getPlantName().trim(), division.getDivisionId().trim());
						
					}
				}
			}

			if (plantDivisionMap.size() == 0) {
				return createInfoLabel("You are not authorized to send Division Level Messages", true);
			}else{
				setMassMessageType(MassMessageType.DEPT);
				return createComponents(plantNames);
			}
		}

	}

	private HBox createComponents(HashSet<String> plantNamesSet) {
		// TODO Auto-generated method stub
		HBox selectionPane = new HBox(50);
		HBox plantHBox = new HBox(20);
		HBox divisionHBox = new HBox(20);
		plantNameCombo.setMinWidth(150);
		divisionNameCombo.setMinWidth(150);
		
		for(String plantName : plantNamesSet){
			plantNameCombo.getItems().add(plantName);
		}
		
		
		plantNameCombo.valueProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String plantName) {
				divisionNameCombo.getItems().clear();
				loadDivisionCombo(plantName);
				
			}
		});
		
		plantNameCombo.setEditable(false);
		plantHBox.getChildren().add(buildInfoLabel("Plant Name : ", false));
		plantHBox.getChildren().add(plantNameCombo);
		
		divisionHBox.getChildren().add(buildInfoLabel("Division Name : ", false));
		divisionHBox.getChildren().add(divisionNameCombo);
		divisionNameCombo.valueProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String newDivName) {
				LoadDataFromDB(newDivName);
				
			}
		});
		
		selectionPane.getChildren().add(plantHBox);
		selectionPane.getChildren().add(divisionHBox);
		selectionPane.setPadding(new Insets(0,0,0,250));
		return selectionPane;
	}

	private void loadDivisionCombo(String plantName) {
		setPlantName(plantName);
		setUserData(UserDataMode.NONE);
		getMassMessageTxtArea().setText(WELCOME_MESSAGE);
		setStatusOfButtons();
		resetSeverityButton();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<String> divForPlantLst = (List)plantDivisionMap.get(plantName);
		
		for(String divId : divForPlantLst){
			divisionNameCombo.getItems().add(divId);
		}
	}
	
	private void LoadDataFromDB(String divisionId) {
		if(divisionId != null){
			setUserDataMode(UserDataMode.NONE);
			setDivisionId(divisionId);
			getMassMessageTxtArea().setText(getCurrentMessage());
			setSeverityButton();			
		}else{
			setDivisionId(NONE);
		}
	}
}
