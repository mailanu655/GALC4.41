package com.honda.galc.client.qi.defectentry;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.util.KeyValue;
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class ResponsibleLevelPanel extends VBox {

	private static ResponsibleLevelPanel instance;
	private ResponsibleLevelController controller;
	private LabeledComboBox<String> siteComboBox;
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> departmentComboBox;
	private LabeledComboBox<KeyValue<String,Integer>> responsibleLevel1ComboBox;
	private LabeledComboBox<KeyValue<String,Integer>> responsibleLevel2ComboBox;
	private LabeledComboBox<KeyValue<String,Integer>> responsibleLevel3ComboBox;
	private LoggedButton clearResp;
	private LoggedButton resetResp;
	private double refWidth = 0.0D;
	private static Map<Integer,ResponsibleLevelPanel> panels = new HashMap<Integer,ResponsibleLevelPanel>();
	
	private ResponsibleLevelPanel() {}
	
	public static ResponsibleLevelPanel getInstance(Object view, double newWidth)
	{
		if(panels == null || panels.isEmpty())  {
			panels = new HashMap<>();
		}
		if(panels.get(view.hashCode()) == null)  {
			ResponsibleLevelPanel newPanel = new ResponsibleLevelPanel();
			newPanel.refWidth = newWidth;
			panels.put(view.hashCode(), newPanel);
		}
		return panels.get(view.hashCode());
	}

	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public LoggedButton createPlainBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setStyle(String.format("-fx-font-size: %dpx;-fx-font-weight: bold", 8));
		btn.setOnAction(handler);
		return btn;
	}
	
	/**
	 * This method is used to create Responsibility Pane for DefectEntry main panel
	 * @return TitledPane
	 */
	public TitledPane createEntryResponsibilityPanel() {
		VBox respBox = new VBox();
		VBox col2 = new VBox();
		VBox col1 = new VBox();
		HBox responsibilityLvlBox = new HBox();
		siteComboBox = createRespFilterComboBox("siteComboBox", "Site  ", true, true, true,refWidth);
		siteComboBox.getControl().getStyleClass().add("station-respons-combo");
		plantComboBox = createRespFilterComboBox("plantComboBox", "Plant", true, true, true,refWidth);
		plantComboBox.getControl().getStyleClass().add("station-respons-combo");
		departmentComboBox = createRespFilterComboBox("departmentComboBox", "Dept", true, true, true,refWidth);
		departmentComboBox.getControl().getStyleClass().add("station-respons-combo");
		col1.getChildren().addAll(siteComboBox,plantComboBox,departmentComboBox);
		responsibleLevel1ComboBox = createRespLevelComboBox("levelComboBox", "L1", true, true, true,refWidth);
		responsibleLevel1ComboBox.getControl().getStyleClass().add("station-respons-combo");
		responsibleLevel2ComboBox = createRespLevelComboBox("levelComboBox2", "L2", true, true, true,refWidth);
		responsibleLevel2ComboBox.getControl().getStyleClass().add("station-respons-combo");
		responsibleLevel3ComboBox = createRespLevelComboBox("levelComboBox3", "L3", true, true, true,refWidth);
		responsibleLevel3ComboBox.getControl().getStyleClass().add("station-respons-combo");
		HBox resetButtons = new HBox();
		clearResp = createPlainBtn("Clear", getController());
		resetResp = createPlainBtn("Reset", getController());
		clearResp.getStyleClass().add("small-btn");
		resetResp.getStyleClass().add("small-btn");
		resetButtons.getChildren().addAll(clearResp, resetResp);
		resetButtons.setPadding(new Insets(2, 20, 0, 0));
		resetButtons.setSpacing(10);
		col2.getChildren().addAll(responsibleLevel1ComboBox, responsibleLevel2ComboBox, responsibleLevel3ComboBox);
		col1.setAlignment(Pos.CENTER_LEFT);
		col1.setPadding(new Insets(0,5,0,0));
		col2.setAlignment(Pos.CENTER_LEFT);
		responsibilityLvlBox.getChildren().addAll(col1,col2);
		respBox.getChildren().addAll(responsibilityLvlBox,resetButtons);
		TitledPane responsibilityPane = new TitledPane("Responsibility", respBox);
		return responsibilityPane;
	}
	
	/**
	 * This method is used to create Responsibility Pane for DefectEntry main panel
	 * @return TitledPane
	 */
	public HBox createExistingResponsibilityPanel() {
		HBox myBox = new HBox();
		siteComboBox = createLabeledComboBox("siteComboBox", "Site", false, true, true);
		siteComboBox.getControl().getStyleClass().add("station-respons-combo");
		plantComboBox = createLabeledComboBox("plantComboBox", "Plant", false, true, true);
		plantComboBox.getControl().getStyleClass().add("station-respons-combo");
		departmentComboBox = createLabeledComboBox("departmentComboBox", "Dept", false, true, true);
		departmentComboBox.getControl().getStyleClass().add("station-respons-combo");
		responsibleLevel1ComboBox = ResponsibleLevelPanel.createLabeledKVComboBox("respLevel","Resp Level 1", false, true, true);
		responsibleLevel1ComboBox.getControl().getStyleClass().add("station-respons-combo");
		responsibleLevel2ComboBox = createLabeledKVComboBox("levelComboBox2", "Resp Level 2", false, true, true);
		responsibleLevel2ComboBox.getControl().getStyleClass().add("station-respons-combo");
		responsibleLevel3ComboBox = createLabeledKVComboBox("levelComboBox3", "Resp Level 3", false, true, true);
		responsibleLevel3ComboBox.getControl().getStyleClass().add("station-respons-combo");
		VBox resetButtons = new VBox();
		clearResp = createPlainBtn("Clear", getController());
		resetResp = createPlainBtn("Reset", getController());
		clearResp.getStyleClass().add("small-btn");
		resetResp.getStyleClass().add("small-btn");
		resetButtons.getChildren().addAll(clearResp, resetResp);
		
		resetButtons.setSpacing(10);
		myBox.getChildren().addAll(siteComboBox, plantComboBox, departmentComboBox, responsibleLevel1ComboBox,
				responsibleLevel2ComboBox, responsibleLevel3ComboBox, resetButtons);
		return myBox;
	}
	
	/**
	 * This method is used to create Labeled Combobox for Responsibility Panel
	 */
	public static LabeledComboBox<String> createRespFilterComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled,double refWidth) {
		LabeledComboBox<String> comboBox = createLabeledComboBox(id, labelName, isHorizontal, isMandatory, isDisabled);
		comboBox.setStyle(String.format("-fx-font-size: %dpx;", 10));
		return comboBox;
	}
	
	/**
	 * This method is used to create Labeled Combobox for Responsibility Panel
	 */
	public static LabeledComboBox<KeyValue<String,Integer>> createRespLevelComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled,double refWidth) {
		LabeledComboBox<KeyValue<String,Integer>> comboBox = createLabeledKVComboBox(id, labelName, isHorizontal, isMandatory, isDisabled);
		comboBox.setStyle(String.format("-fx-font-size: %dpx;", 10));
		return comboBox;
	}
	
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public static LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		return comboBox;
	}
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public static LabeledComboBox<KeyValue<String,Integer>> createLabeledKVComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<KeyValue<String,Integer>> comboBox = new LabeledComboBox<KeyValue<String,Integer>>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		return comboBox;
	}

	/**
	 * @return the siteComboBox
	 */
	public LabeledComboBox<String> getSiteComboBox() {
		return siteComboBox;
	}

	/**
	 * @param siteComboBox the siteComboBox to set
	 */
	public void setSiteComboBox(LabeledComboBox<String> siteComboBox) {
		this.siteComboBox = siteComboBox;
	}

	/**
	 * @return the plantComboBox
	 */
	public LabeledComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	/**
	 * @param plantComboBox the plantComboBox to set
	 */
	public void setPlantComboBox(LabeledComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}

	/**
	 * @return the departmentComboBox
	 */
	public LabeledComboBox<String> getDepartmentComboBox() {
		return departmentComboBox;
	}

	/**
	 * @param departmentComboBox the departmentComboBox to set
	 */
	public void setDepartmentComboBox(LabeledComboBox<String> departmentComboBox) {
		this.departmentComboBox = departmentComboBox;
	}

	/**
	 * @return the responsibleLevel1ComboBox
	 */
	public LabeledComboBox<KeyValue<String, Integer>> getResponsibleLevel1ComboBox() {
		return responsibleLevel1ComboBox;
	}

	/**
	 * @param responsibleLevel1ComboBox the responsibleLevel1ComboBox to set
	 */
	public void setResponsibleLevel1ComboBox(LabeledComboBox<KeyValue<String, Integer>> responsibleLevel1ComboBox) {
		this.responsibleLevel1ComboBox = responsibleLevel1ComboBox;
	}

	/**
	 * @return the responsibleLevel2ComboBox
	 */
	public LabeledComboBox<KeyValue<String, Integer>> getResponsibleLevel2ComboBox() {
		return responsibleLevel2ComboBox;
	}

	/**
	 * @param responsibleLevel2ComboBox the responsibleLevel2ComboBox to set
	 */
	public void setResponsibleLevel2ComboBox(LabeledComboBox<KeyValue<String, Integer>> responsibleLevel2ComboBox) {
		this.responsibleLevel2ComboBox = responsibleLevel2ComboBox;
	}

	/**
	 * @return the responsibleLevel3ComboBox
	 */
	public LabeledComboBox<KeyValue<String, Integer>> getResponsibleLevel3ComboBox() {
		return responsibleLevel3ComboBox;
	}

	/**
	 * @param responsibleLevel3ComboBox the responsibleLevel3ComboBox to set
	 */
	public void setResponsibleLevel3ComboBox(LabeledComboBox<KeyValue<String, Integer>> responsibleLevel3ComboBox) {
		this.responsibleLevel3ComboBox = responsibleLevel3ComboBox;
	}

	/**
	 * @return the clearResp
	 */
	public LoggedButton getClearResp() {
		return clearResp;
	}

	/**
	 * @return the resetResp
	 */
	public LoggedButton getResetResp() {
		return resetResp;
	}

	/**
	 * @return the controller
	 */
	public ResponsibleLevelController getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(ResponsibleLevelController controller) {
		this.controller = controller;
	}

}
