package com.honda.galc.client.ui.component;

import javafx.scene.Scene;

public interface ILoggedComponent {
	public StringBuilder getMessage();
	public Scene getScene();
	
	default public void setMessage(String str) {
		// Do nothing
	}
}
