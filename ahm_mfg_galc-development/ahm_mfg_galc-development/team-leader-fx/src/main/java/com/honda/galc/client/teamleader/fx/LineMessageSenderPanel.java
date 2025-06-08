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

import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/** * * 
* @author Fredrick Yessaian 
* @since Sep 03, 2014
*/

public abstract class LineMessageSenderPanel extends MassMessageSenderPanel {

	private static final long serialVersionUID = 1L;
	private static String MAM_LINE = "MAM_LINE";
	private Map<String, String> grpIdLine = new HashMap<String, String>();
	private HashSet<String> plantNames = new HashSet<String>();
	private MultiValueMap plantDivisionMap = new MultiValueMap();
	private MultiValueMap divisionLineMap = new MultiValueMap();
	ComboBox<String> plantNameCombo = new ComboBox<String>();
	ComboBox<String> divisionNameCombo = new ComboBox<String>();
	ComboBox<String> lineNameCombo = new ComboBox<String>();
	
	public LineMessageSenderPanel() {
		super("Line Level Message Sender", KeyEvent.VK_N);
		
	}
	
	protected abstract HBox populateFucntionSpecificLabel(); 

	public LineMessageSenderPanel(String string, int vkN) {
		super(string, KeyEvent.VK_N);
	}
	
	@Override
	protected HBox constructControlPanelFromGroupIds() {
		setAssociateNo(this.getMainWindow().getApplicationContext().getUserId());
		List<ComponentProperty> componentLst = PropertyService.getComponentProperty(MAM_LINE);

		if (componentLst == null || componentLst.size() == 0) {
			return createInfoLabel("Please contact support team, Line id & LDAP group id not configured in the system", true);
		} else {

			for (ComponentProperty property : componentLst) {
				grpIdLine.put(property.getPropertyKey(),
						property.getPropertyValue());
			}

			for (String userGroupId : getUserGroupLst(getAssociateNo())) {
				if (grpIdLine.containsKey(userGroupId.trim())) {
					
					String lineNameFromGroupId = (String) grpIdLine.get((userGroupId.trim()));
					
					Line line = ServiceFactory.getDao(LineDao.class).findByKey(lineNameFromGroupId.trim());
					
					if(line != null){
						
						plantNames.add(line.getPlantName().trim());
						
						if(!plantDivisionMap.containsValue(line.getPlantName().trim(), line.getDivisionId().trim()))
							plantDivisionMap.put(line.getPlantName().trim(), line.getDivisionId().trim());
						
						if(!divisionLineMap.containsValue(line.getDivisionId().trim(), line.getLineId().trim()))
							divisionLineMap.put(line.getDivisionId().trim(), line.getLineId().trim());
					}
				}
			}

			if (divisionLineMap.size() == 0) {
				return populateFucntionSpecificLabel();
			}else{
				return createComponents(plantNames);
			}
		}

	}
	
	private HBox createComponents(HashSet<String> plantNamesSet) {
		HBox selectionPane = new HBox(50);
		HBox plantHBox = new HBox(20);
		HBox divisionHBox = new HBox(20);
		HBox lineHBox = new HBox(20);
		
		plantNameCombo.setMinWidth(150);
		divisionNameCombo.setMinWidth(150);
		lineNameCombo.setMinWidth(150);
		
		for(String plantName : plantNamesSet){
			plantNameCombo.getItems().add(plantName);
		}
		
		
		plantNameCombo.valueProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String newPlantName) {
				divisionNameCombo.getItems().clear();
				loadDivisionCombo(newPlantName);
				
			}
		});
		
		plantNameCombo.setEditable(false);
		plantHBox.getChildren().add(buildInfoLabel("Plant Name : ", false));
		plantHBox.getChildren().add(plantNameCombo);
		
		divisionNameCombo.valueProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String newDivisionName) {
				lineNameCombo.getItems().clear();
				loadLineCombo(newDivisionName);
			}

		});
		
		lineNameCombo.valueProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String newLineId) {
				LoadDataFromDB(newLineId);
			}
		});
		
		divisionHBox.getChildren().add(buildInfoLabel("Division Name : ", false));
		divisionHBox.getChildren().add(divisionNameCombo);
		
		lineHBox.getChildren().add(buildInfoLabel("Line Name : ", false));
		lineHBox.getChildren().add(lineNameCombo);
		
		selectionPane.getChildren().add(plantHBox);
		selectionPane.getChildren().add(divisionHBox);
		selectionPane.getChildren().add(lineHBox);
		selectionPane.setPadding(new Insets(0,0,0,25));
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

	private void loadLineCombo(String newDivisionId) {
		setDivisionId(newDivisionId);
		setUserDataMode(UserDataMode.NONE);
		getMassMessageTxtArea().setText(WELCOME_MESSAGE);
		setStatusOfButtons();
		resetSeverityButton();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<String> divForPlantLst = (List)divisionLineMap.get(newDivisionId);
		
		for(String lineId : divForPlantLst){
			lineNameCombo.getItems().add(lineId);
		}
	}
	
	private void LoadDataFromDB(String lineId) {
		if(lineId != null){
			setUserDataMode(UserDataMode.NONE);
			setLineId(lineId);
			getMassMessageTxtArea().setText(getCurrentMessage());
			setSeverityButton();
		}else{
			setLineId(NONE);
		}
	}
}
