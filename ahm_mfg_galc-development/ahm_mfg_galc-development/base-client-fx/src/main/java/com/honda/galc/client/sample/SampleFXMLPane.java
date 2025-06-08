package com.honda.galc.client.sample;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.MainWindow;


public class SampleFXMLPane extends ApplicationMainPane  implements EventHandler<ActionEvent>{
    @FXML
    private Button myButton;

    
	public SampleFXMLPane(MainWindow window) {
		super(window,true);
	
	}
	


	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
