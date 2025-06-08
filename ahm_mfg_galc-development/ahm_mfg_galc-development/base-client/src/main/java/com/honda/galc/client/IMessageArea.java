package com.honda.galc.client;

import java.awt.Color;

public interface IMessageArea {
	
	public void setErrorMessageArea(String errorMessage);
	public void setErrorMessageArea(String errorMessage, Color color); 
	public void setStatusMessage(String message);
	public boolean isError();
	
}
