package com.honda.galc.client.checkers.reactions;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.common.exception.LoginException;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LDAPService;

public class UserAuthorizationCheckerReaction {
	
	private final static String DEFAULT_LOT_CONTROL = "Default_LotControl";
	private final static String LDAP_SECURITY_GROUP_LOT_CTRL_OVERRIDE = "LDAP_SECURITY_GROUP_LOT_CTRL_OVERRIDE";
	
	
	/**
	 * Displays pop up for user authorization by login
	 * @param isError
	 * @param msg
	 * @param checkerName
	 */
	public static String displayPopUp(boolean isError, String msg, String checkerName) {
		
		final FxDialog dialogStage = new FxDialog("", ClientMainFx.getInstance().getStage());
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		GridPane pane = new GridPane();
		Label header = new Label();
		header.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		GridPane.setHalignment(header, HPos.CENTER);
		header.setPadding(new Insets(0, 0, 10, 0));
		header.setText("WARNING : "+msg);
		pane.setStyle("-fx-background-color: YELLOW;");
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);// padding
		Scene scene = new Scene(pane, 800, 300);
		
		dialogStage.setScene(scene);
		pane.add(header, 0, 1);
		pane.autosize();
		
		//Add text box for username and password
		Label label1 = new Label("Quality Expert's User name:");
		final TextField username = new TextField();
		Label label2 = new Label("Password:");
		final PasswordField  passwordField = new PasswordField();
		
		final Label authFailLabel = new Label();
		
		HBox inputBox =new HBox();
		inputBox.getChildren().addAll(label1, username,label2,passwordField);
		inputBox.setSpacing(10);
		pane.add(inputBox, 0, 3);

		Button cancelButton = new Button("CANCEL");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					authFailLabel.setText("");
					dialogStage.close();
	
				}
		});
		
		
		authFailLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		authFailLabel.setTextFill(Color.web("#ff0000"));
		GridPane.setHalignment(authFailLabel, HPos.CENTER);
		authFailLabel.setPadding(new Insets(0, 0, 10, 0));
		pane.add(authFailLabel, 0, 8);
		
		//ADD authorize button
		Button okButton = new Button("AUTHORIZE");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				authFailLabel.setText("");
				if(StringUtils.isEmpty(username.getText()) || StringUtils.isEmpty(passwordField.getText())){
					authFailLabel.setText("Please enter quality expert's username and password to allow expired product. ");
				}else{
					try{
						LDAPService.getInstance().authenticate(username.getText(), passwordField.getText());
						}catch(LoginException le){
							if(le.getMessage().equalsIgnoreCase(LoginStatus.USER_NOT_EXIST.toString())){
								authFailLabel.setText(LoginStatus.USER_NOT_EXIST.getMessage());
							}else if(le.getMessage().equalsIgnoreCase(LoginStatus.PASSWORD_EXPIRED.toString())){
								authFailLabel.setText(LoginStatus.PASSWORD_EXPIRED.getMessage());
							}else if(le.getMessage().equalsIgnoreCase(LoginStatus.AUTHENTICATION_ERROR.toString())){
								authFailLabel.setText(LoginStatus.AUTHENTICATION_ERROR.getMessage());
							}else if(le.getMessage().equalsIgnoreCase(LoginStatus.PASSWORD_INCORRECT.toString())){
								authFailLabel.setText(LoginStatus.PASSWORD_INCORRECT.getMessage());
							}
						}
				//Check if the user is in correct LDAP group
				if(authFailLabel.getText().isEmpty())
					{
						String ldapSecurityGroup = PropertyService.getProperty(DEFAULT_LOT_CONTROL, LDAP_SECURITY_GROUP_LOT_CTRL_OVERRIDE,"");
						if(StringUtils.isEmpty(ldapSecurityGroup))
						{
							authFailLabel.setText("No LDAP group defined in the properties ");
						}else{
							List<String> securityGroups =  LDAPService.getInstance().getMemberList(username.getText());
							if(securityGroups != null && securityGroups.size() >0 ) {
								
								for(String group: securityGroups){
									if(group.equalsIgnoreCase(ldapSecurityGroup)){
										authFailLabel.setText("");
										dialogStage.close();
										
									}
								}
								authFailLabel.setText(username.getText()+" is not in the LDAP group: "+ldapSecurityGroup);
							}else{
								authFailLabel.setText("No LDAP groups for the user :"+username.getText());
							}
					  }
					}
				}
			}
		});
		
		HBox buttons = new HBox();
		buttons.getChildren().add(okButton);
		buttons.getChildren().add(cancelButton);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.BOTTOM_CENTER);
		pane.add(buttons, 0, 6);
		pane.setHalignment(cancelButton, HPos.CENTER);	
		
		
		dialogStage.sizeToScene();
		dialogStage.showDialog();
		
		if(authFailLabel.getText().isEmpty()){
			return null;
		}else{
			return username.getText();
		}
		
		
	}

}
