package com.honda.galc.handheld.forms;

import org.apache.struts.action.ActionForm;

public class EnterUserIdForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private String userInput, division = "";
	
	public String getUserInput() {
		return userInput;
	}
	public void setUserInput(String userId) {
		this.userInput = userId;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
}
