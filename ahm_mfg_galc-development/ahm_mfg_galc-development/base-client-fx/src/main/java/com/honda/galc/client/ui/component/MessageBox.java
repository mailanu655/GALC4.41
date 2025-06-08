package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessageBox   {
	private Stage dialog = null;
	private Stage owner = null;
	private String message = null;
	
	public MessageBox(Stage owner, String message) {
		
   this.owner = owner;
   this.message = message;
   constructStage();
		
	}
	public MessageBox(String message) {
		this.message = message;
		constructStage();
	}
	
	public void closeMessage() {
		dialog.close();
	}

	
	public void constructStage() {
		
		 dialog = new Stage();
			dialog.initStyle(StageStyle.DECORATED);
			if(owner != null)
			dialog.initOwner(owner);
			dialog.initModality(Modality.APPLICATION_MODAL);
			Text t = UiFactory.createText();
			t.setFont(new Font(20));
			t.setText(message);
			t.setTextAlignment(TextAlignment.CENTER);
			
			
			HBox hbox = new HBox();
			
			hbox.getChildren().add(t);
			//Scene scene = new Scene(hbox);
			Scene scene = new Scene(VBoxBuilder.create().children(t).alignment(Pos.CENTER).build());
			
		
			dialog.setHeight(200);
			dialog.setWidth(400);
			
			
			dialog.setScene(scene);
			dialog.centerOnScreen();
			dialog.sizeToScene();
			
			dialog.toFront();
	
		
	}
	
	public void showMessage(){
		
					dialog.showAndWait();
	}
	

}
