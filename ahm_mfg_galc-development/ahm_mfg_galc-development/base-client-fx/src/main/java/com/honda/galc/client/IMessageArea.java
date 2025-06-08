package com.honda.galc.client;

import javafx.scene.paint.Color;





public interface IMessageArea {
	public void setErrorMessageArea(String errorMessage);
	public void setErrorMessageArea(String errorMessage, Color color); 
	public void setStatusMessage(String message);
	public boolean isError();
	public static String Size = "Size";
	public static String Big = "big";
}
