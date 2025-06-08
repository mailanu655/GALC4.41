package com.honda.galc.client.teamleader.fx;

import java.util.List;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManualLotControlViewMessageDialog extends FxDialog {

	private List<String> data;
	private boolean isProcessPoint;	
	
	public ManualLotControlViewMessageDialog(MainWindow mainWindow,String title, Stage owner,Boolean isProcessPoint, List<String> data) {
		super(title, owner);
		this.data = data;
		this.isProcessPoint = isProcessPoint;
		initComponents();
	}

	private void initComponents() {
		((BorderPane) this.getScene().getRoot()).setCenter(createMainContainer());
	}

	private Node createMainContainer() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setMaxSize(1000,450);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(20,20,20,20));
		
		VBox vBox1 = null;
		
		int  k = 1;
		for (int j = 0; j < data.size() ;j++) {
			
			String dataValue = data.get(j);
			if(isProcessPoint && dataValue!=null)
				dataValue = dataValue.substring(dataValue.indexOf("-")+1, dataValue.length());
				
			if(k==1) {
				vBox1 = new VBox();	
			} 
			TilePane pane = new TilePane();
			pane.setPrefSize(70, 20);
			pane.setPadding(new Insets(3));
			pane.setStyle("-fx-border-color: #808080; -fx-border-width: 1, 1; -fx-border-insets: 1, 1 1 1 1");
			Label lblName = UiFactory.createLabel(dataValue, dataValue, "15");
			pane.getChildren().addAll(lblName);
			vBox1.getChildren().add(pane);
			
			if(k == 10 || j == (data.size()-1)) {
				hBox.getChildren().add(vBox1);
				k =1;
			} else {
				k++;
			}
		}
		scrollPane.setContent(hBox);
		return scrollPane;
	}
}
