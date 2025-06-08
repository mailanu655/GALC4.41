package com.honda.galc.client.dc.view;

import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import net.miginfocom.layout.CC;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.processor.TransmissionInstallProcessor;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.MCOperationRevision;
/*   
 * @author Jiamei Li<br>
 * Aug 13, 2014
 *
 */
public class TransmissionInstallView extends OperationView {

	private MigPane contentPane;
	private TextField missionAndType;
	private TextField missionTorque;
	private Button doneButton;
	public TransmissionInstallView(OperationProcessor processor) {
		super(processor);
		EventBusUtil.register(this);
	}

	@Override
	public void initComponents(){
		getProcessor().setView(this);
		contentPane = new MigPane("insets 40 20 0 8", "", "");
		this.setCenter(contentPane);
		createTransmissionPane();
		//if isPreviousLineCheckEnabled is true and previous line is valid, disable the textFileds
		if(getProcessor().getPropertyBean().isPreviousLineCheckEnabled() 
				&& !getProcessor().isProductPreviousLineValid()){
			TextFieldState.READ_ONLY.setState(missionAndType);
			TextFieldState.READ_ONLY.setState(missionTorque);
			this.doneButton.setDisable(true);
		}
		setDefaultFocus();
		mapActions();
		Platform.runLater(new Runnable(){
			public void run(){
				contentPane.layout();
			}
		});

	}

	public void createTransmissionPane(){
		String layoutConstraints = "insets 40 20 0 0";
		String columnToken = "[grow,fill,center]";
		String columnConstraints = String.format("[230px!]"+ columnToken);
		MigPane mainPane = new MigPane(layoutConstraints, columnConstraints);

		String missionPart = getProcessor().getPropertyBean().getMissionAndTypeName();
		mainPane.add(UiFactory.getInfo().createLabel("missionPart", missionPart, TextAlignment.CENTER), new CC().cell(0,0).alignX("center"));

		String partMask = getProcessor().getController().getModel().getCurrentOperation().getSelectedPart().getPartMask();
		mainPane.add(UiFactory.getInfoSmall().createLabel("partMask", partMask, TextAlignment.CENTER), new CC().cell(0,1).alignX("center"));

		missionAndType = UiFactory.getInputBig().createTextField("missionAndType", 14, TextFieldState.EDIT, Pos.BASELINE_CENTER);
		setMissionAndType();//set missionType value and set the status
		mainPane.add(missionAndType, new CC().cell(1, 0).spanY(2).alignX("left"));

		mainPane.add(UiFactory.getInfo().createLabel("Measurement", "Measurement", TextAlignment.CENTER), new CC().cell(0,2).gapTop("50px!").alignX("center"));
		String minTorque = String.valueOf(getProcessor().getController().getModel().getCurrentMeasurement().getMinLimit());
		String maxTorque = String.valueOf(getProcessor().getController().getModel().getCurrentMeasurement().getMaxLimit());
		mainPane.add(UiFactory.getInfoSmall().createLabel("Min/Max:", "Min/Max: " + minTorque + "/" + maxTorque, TextAlignment.CENTER), new CC().cell(0,3).alignX("center"));

		missionTorque = UiFactory.getInputBig().createTextField("missionTorque", 14, TextFieldState.EDIT, Pos.BASELINE_CENTER);
		setMissionMeasurement();//set mission measurement value and status
		mainPane.add(missionTorque, new CC().cell(1, 2).spanY(2).gapTop("50px!"));

		this.doneButton = UiFactory.getInfo().createButton("Done", false);
		this.doneButton.defaultButtonProperty().bind(this.doneButton.focusedProperty());
		focusDoneButton();
		mainPane.add(this.getDoneButton(), new CC().cell(0,4).width("120px!").spanX(2).alignX("right").gapTop("30px!"));

		contentPane.add(mainPane);
	}

	/**
	 * set missionAndType value and set its status
	 */
	private void setMissionAndType(){
		String partSN = getProcessor().getInstalledPart() == null ? null : getProcessor().getInstalledPart().getPartSerialNumber();
		missionAndType.setText(partSN);
		if(partSN != null){
			TextFieldState.READ_ONLY.setState(missionAndType);
		}else{
			TextFieldState.EDIT.setState(missionAndType);
		}
	}

	/**
	 * set mission measurement value and status
	 */
	private void setMissionMeasurement(){
		String measurement = getProcessor().getMeasurement() == null ? null : String.valueOf(getProcessor().getMeasurement().getMeasurementValue());
		missionTorque.setText(measurement);
		if(measurement != null){
			TextFieldState.READ_ONLY.setState(missionTorque);
		}else{
			TextFieldState.EDIT.setState(missionTorque);
		}
	}

	/**
	 * set done button status and request focus
	 */
	private void focusDoneButton(){
		if(StringUtils.isNotBlank(this.missionAndType.getText()) && StringUtils.isNotBlank(this.missionTorque.getText())){
			this.doneButton.setDisable(false);
			getProcessor().getController().setFocusComponent(this.doneButton);
			getProcessor().getController().requestFocus();
		}else{
			this.doneButton.setDisable(true);
		}
	}

	public void refreshView(){
		//make contentPane to layout
		Platform.runLater(new Runnable(){
			public void run(){
				contentPane.layout();
			}
		});

		if(StringUtils.isBlank(missionAndType.getText())){
			getProcessor().getController().setFocusComponent(missionAndType);
			getProcessor().getController().requestFocus();
			return ;
		}
		if(StringUtils.isBlank(missionTorque.getText())){
			getProcessor().getController().setFocusComponent(missionTorque);
			getProcessor().getController().requestFocus();
			return ;
		}
		focusDoneButton();
	}

	public void resetView(){
		setMissionAndType();
		setMissionMeasurement();
		focusDoneButton();

	}
	protected void mapActions(){
		if(this.getMissionAndType() != null){
			this.missionAndType.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					getProcessor().processInputPart((TextField)e.getSource());
				}
			});
		}
		if(this.getMissionTorque() != null){
			this.missionTorque.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					getProcessor().collectTorque((TextField)e.getSource());
				}
			});
		}

		this.getDoneButton().setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				getProcessor().saveData();
			}
		}
				);
	}

	/**
	 * This method is used to set default focus to missionAndType 
	 */
	private void setDefaultFocus(){
		if(StringUtils.isBlank(this.missionAndType.getText()) && StringUtils.isBlank(this.missionTorque.getText())){
			getProcessor().getController().setFocusComponent(this.getMissionAndType());
			getProcessor().getController().requestFocus();
		}
	}

	public TransmissionInstallProcessor getProcessor(){
		return (TransmissionInstallProcessor)super.getProcessor();
	}

	public MigPane getContentPane() {
		return contentPane;
	}

	public TextField getMissionAndType() {
		return missionAndType;
	}

	public TextField getMissionTorque() {
		return missionTorque;
	}

	@Subscribe 
	public void handleUnitNavigatorEvent(UnitNavigatorEvent event){
		getLogger().debug("UnitNavigatorWidget.handleEvent recvd : " + event.toString());
		List<MCOperationRevision> structures = this.getProcessor().getController().getModel().getOperations();
		int index = structures.indexOf(this.getProcessor().getOperation());

		if(event.getType().equals(UnitNavigatorEventType.SELECTED) && index == event.getIndex()){
			refreshView();
		}
	}

	@Subscribe 
	public void handleProductStartedEvent(ProductStartedEvent event){ 
		resetView(); 
	} 

	public Button getDoneButton() {
		return doneButton;
	}
}
