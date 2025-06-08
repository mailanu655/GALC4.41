package com.honda.galc.client.dc.control;

import java.io.IOException;

import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.entity.conf.MCOperationRevision;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


/**
 * 
 * @author Suriya Sena
 * @date Jan 23, 2015
 * 
 */

public class UnitNavigatorControl extends AbstractUnitNavigatorControl{
	@FXML
	protected LoggedButton searchButton;

	@FXML
	protected LoggedTextField searchTextField;

	public UnitNavigatorControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resource/com/honda/galc/client/dc/view/UnitNavigatorControl.fxml"));
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
		Image magnifier = new Image("resource/images/common/magnifier.png");
		ImageView magnifierView = new ImageView(magnifier);
		magnifierView.setFitHeight(SMALL_ICON_SIZE);
		magnifierView.setFitWidth(SMALL_ICON_SIZE);

		searchButton.setId("nav-search-button");
		searchButton.setText("");
		searchButton.setGraphic(magnifierView);
	}

	@Override
	protected void onSearchKeyReleased() {
		controller.getView().getMainWindow().clearMessage();
		searchButton.setDisable(searchTextField.getLength() <= 0);
	}

	/*
	 * The search function will scan forward from the current cursor position to the end of the list
	 * stopping on the first match. If no match is found it wraps around to the start of the list exiting once
	 * all items have been visited or a match is found.
	 */

	@FXML
	void searchButton(ActionEvent event) {
		controller.getView().getMainWindow().clearMessage();
		int matchIndex;
		int matchCount = findOperation(searchTextField.getText().toUpperCase(),false);

		if (matchCount > 0 ) {
			matchIndex = findOperation(searchTextField.getText().toUpperCase(),true);
			moveTo(matchIndex);
			controller.getView().getMainWindow().setMessage(String.format("Found %d occurrences", matchCount), Color.LIGHTGREEN);
		} else {
			controller.getView().getMainWindow().setMessage("No matching text found.", Color.CORAL);
		}
	}

	private int findOperation(String pattern, boolean isStopOnFirstMatch) {
		int itemsVisited = 0;
		int startIndex = (cursorIndex.get()+1) % items.size();
		int match=0;

		matchFound:
			while (itemsVisited != items.size()) {
				for (int i = startIndex; i < items.size() && itemsVisited != items.size(); i++) {
					itemsVisited++;

					MCOperationRevision operation = items.get(i);

					String operationName = operation.getId().getOperationName().toUpperCase();
					String operationDescription = operation.getDescription().toUpperCase();
					String operationType = operation.getType().toString().toUpperCase();

					if (operationName.contains(pattern) || operationDescription.contains(pattern) || operationType.contains(pattern)) {
						match++;
						if (isStopOnFirstMatch) {
							match= i;
							break matchFound;
						}
					}
				}
				startIndex = 0;
			}

		return match;
	}

	@FXML
	void initialize() {
		assert naviListVBox != null : "fx:id=\"naviListVBox\" was not injected: check your FXML file 'UnitNavigatorControl.fxml'.";
		assert scrollbar != null : "fx:id=\"scrollbar\" was not injected: check your FXML file 'UnitNavigatorControl.fxml'.";
		assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'UnitNavigatorControl.fxml'.";
		assert fastTravelMap != null : "fx:id=\"fastTravelMap\" was not injected: check your FXML file 'UnitNavigatorControl.fxml'.";
		assert searchTextField != null : "fx:id=\"searchTextField\" was not injected: check your FXML file 'UnitNavigatorControl.fxml'.";
	}
}