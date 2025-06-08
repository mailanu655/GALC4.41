package com.honda.galc.client.dc.view;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.dc.processor.ImmobiRuleProcessor;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */

public class ImmobiView extends OperationView {

	protected GridPane contentPane;	
	protected TextField statusTxtField;
	protected Label  statusLabel;
	private ClientAudioManager audioManager;
	private String immobiMsg=null;
	
	
	public ImmobiView(ImmobiRuleProcessor processor) {
		super(processor);
		processor.setView(this);
		processor.process();
	}

	public ApplicationContext getApplicationContext() {
		return getProcessor().getController().getModel().getProductModel().getApplicationContext();
	}

	@Override
	public void initComponents() {
		try {
			setAudioManager(new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class, getApplicationContext().getProcessPointId())));			
			contentPane = new GridPane();
			contentPane.setHgap(10);
			contentPane.setVgap(10);
			contentPane.setPadding(new Insets(0, 10, 0, 10));
			this.setCenter(contentPane);
		    createStatusLabel();
			contentPane.add(getStatusLabel(), 1, 0); 	        
			createStatusTxtField();
			contentPane.add(getStatusTxtField(), 2,0);
			Text refreshMsg = UiFactory.createText("TO REFRESH PUSH \"COMPLETE\" BUTTON ON IMMOBI EQUIPMENT");
			refreshMsg.setFont(Font.font("Arial", FontWeight.BOLD, 20));
			contentPane.add(refreshMsg, 30, 10);		
			String immobiCompBtnImagePath = "/resource/images/immobi/immobi_complete_button_image.jpg";
			Image immobiCompBtnImage = new Image(immobiCompBtnImagePath);
			ImageView confirmImgView = new ImageView(immobiCompBtnImage);
			contentPane.add(confirmImgView,30,12);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createStatusTxtField() {
		statusTxtField = UiFactory.createTextField("statusTxtField", 20, UiFactory.getIdle().getInputFont(), TextFieldState.READ_ONLY);
		statusTxtField.setStyle("-fx-background-color: chartreuse ; -fx-font-size: 25;-fx-font-weight: bold;");
	}

	private void createStatusLabel() {
		statusLabel = UiFactory.getInfo().createLabel("immobiStatus", "Status:");
		statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
	}

	public void updateImmobilizeStatus(String msgType, String seq, String vin,String msg) {		
		Logger.getLogger().info("ThreadID = " + Thread.currentThread().getName()+ " Status from immobilizer: " + msg);
		if (this.getStatusTxtField().isVisible())			
		{			
			setImmobiMsg(msg);
	       Platform.runLater(new Runnable() {
			            public void run(){
                         ImmobiView.this.getStatusTxtField().setText(ImmobiView.this.getImmobiMsg());
							if(ImmobiView.this.getImmobiMsg().contains("REG_OK"))
							{
								ImmobiView.this.getProcessor().finish();
							}					
			            }
			          });  
		}
		else
			Logger.getLogger().info("Not expecting any messages.  Discarding message: " + msg);		
	}
	
	@Override
	public ImmobiRuleProcessor getProcessor() {
		return (ImmobiRuleProcessor) super.getProcessor();
	}

	public String getApplicationName() {
		return getProcessor().getController().getProcessPointId();
	}

	public String getImmobiMsg() {
		return immobiMsg;
	}

	public void setImmobiMsg(String immobiMsg) {
		this.immobiMsg = immobiMsg;
	}
	
	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	public void setAudioManager(ClientAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	public TextField getStatusTxtField() {
		return statusTxtField;
	}

	public void setStatusTxtField(TextField statusTxtField) {
		this.statusTxtField = statusTxtField;
	}
	
	public Label getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(Label statusLabel) {
		this.statusLabel = statusLabel;
	}
}
