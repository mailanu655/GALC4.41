package com.honda.galc.client.dc.control;

import java.io.IOException;

import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class UnitNavigatorRepairClientControl extends AbstractUnitNavigatorControl{

    @FXML
    private LoggedButton searchCriteriaButton;
    
    @FXML
    private LoggedLabel titleLabel;
  
    public UnitNavigatorRepairClientControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resource/com/honda/galc/client/dc/view/UnitNavigatorRepairClientControl.fxml"));
	    fxmlLoader.setRoot(this);
	    fxmlLoader.setController(this);
		this.getStylesheets().add(STYLESHEET);
		
	    try {
	        fxmlLoader.load();
	    } catch (IOException exception) {
	        throw new RuntimeException(exception);
	    }
	    
	    init();
	}

	@Override
	protected void initSearchPanel() {
       Image magnifier = new Image("/resource/com/honda/galc/client/images/filter.png");
       ImageView magnifierView = new ImageView(magnifier);
       magnifierView.setFitHeight(SMALL_ICON_SIZE);
       magnifierView.setFitWidth(SMALL_ICON_SIZE);
	   searchCriteriaButton.setId("nav-search-button");
       searchCriteriaButton.setText("");
       searchCriteriaButton.setGraphic(magnifierView);
       titleLabel.setTextFill(Color.LIGHTGREEN);
    }

	@Override
	protected void onSearchKeyReleased() {}
	
	@FXML
	void searchCriteriaButton(ActionEvent event) {
		 SearchCriteriaPanel.getInstance().showPanel();
	}
   
   @FXML
   void initialize() {
       assert naviListVBox != null : "fx:id=\"naviListVBox\" was not injected: check your FXML file 'UnitNavigatorRepairClientControl.fxml'.";
       assert scrollbar != null : "fx:id=\"scrollbar\" was not injected: check your FXML file 'UnitNavigatorRepairClientControl.fxml'.";
       assert searchCriteriaButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'UnitNavigatorRepairClientControl.fxml'.";
       assert fastTravelMap != null : "fx:id=\"fastTravelMap\" was not injected: check your FXML file 'UnitNavigatorRepairClientControl.fxml'.";
   }
}
