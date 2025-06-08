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

import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.enumtype.MassMessageType;
import com.honda.galc.service.property.PropertyService;

/** * * 
* @author Fredrick Yessaian 
* @since Sep 03, 2014
*/

public class PlantMessageSenderPanel extends MassMessageSenderPanel {

	private static String MAM_PLANT = "MAM_PLANT";
	private HashSet<String> plantNames = new HashSet<String>();
	private Map<String, String> grpIdPlant = new HashMap<String, String>();
	
	
	public PlantMessageSenderPanel() {
		super("Plant Level Mass Message Sender", KeyEvent.VK_E);
		
	}

	protected HBox constructControlPanelFromGroupIds() {
		setAssociateNo(this.getMainWindow().getApplicationContext().getUserId());
		List<ComponentProperty> componentLst = PropertyService.getComponentProperty(MAM_PLANT);

		if (componentLst == null || componentLst.size() == 0) {
			return createInfoLabel("Please contact support team, Plant name & LDAP group id not configured in the system", true);
		} else {

			for (ComponentProperty property : componentLst) {
				grpIdPlant.put(property.getPropertyKey(),
						property.getPropertyValue());
			}

			for (String userGroupId : getUserGroupLst(getAssociateNo())) {
				if (grpIdPlant.containsKey(userGroupId.trim())) {
					plantNames
							.add((String) grpIdPlant.get((userGroupId.trim())));
				}
			}

			if (plantNames.size() == 0) {
				return createInfoLabel("You are not authorized to send Plant Level Messages", true);
			}else{
				setMassMessageType(MassMessageType.PLANT);
				return createComponents(plantNames);
			}
		}

	}


	
	private HBox createComponents(HashSet<String> plantNameSet) {
		// TODO Auto-generated method stub
		HBox selectionPane = new HBox();
		HBox plantHBox = new HBox(50);
		//selectionPane.prefWidth(arg0)
		ComboBox<String> plantNameCombo = new ComboBox<String>();
		plantNameCombo.setMinWidth(150);
		for(String plantName : plantNameSet){
			plantNameCombo.getItems().add(plantName);
		}
		
		plantNameCombo.valueProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String newPlantName) {
				LoadDataFromDB(newPlantName);
			}
		});
		
		plantNameCombo.setEditable(false);
		plantHBox.getChildren().add(buildInfoLabel("Plant Name : ", false));
		plantHBox.getChildren().add(plantNameCombo);
		selectionPane.setPadding(new Insets(0,0,0,350));
		selectionPane.getChildren().add(plantHBox);
		return selectionPane;
		
	}
	
	private void LoadDataFromDB(String newPlantName) {
		setUserDataMode(UserDataMode.NONE);
		setPlantName(newPlantName);
		getMassMessageTxtArea().setText(getCurrentMessage());
		setSeverityButton();
	}


}
