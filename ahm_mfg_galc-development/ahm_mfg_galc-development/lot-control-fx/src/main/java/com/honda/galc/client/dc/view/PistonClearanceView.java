package com.honda.galc.client.dc.view;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import net.miginfocom.layout.CC;

import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.processor.PistonClearanceProcessor;
import com.honda.galc.client.dc.validator.PistonClearanceValidator;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
/*   
* @author Jiamei Li<br>
* Jul 7, 2014
*
*/
public class PistonClearanceView extends OperationView {

	private Map<Integer, String> boreMeasurements;
	private Map<Integer, TextField> borMeasurementPanes;
	private Map<Integer, TextField> pistonMeasurements;
	private Map<Integer, TextField> clearanceMeasurements;
	private MigPane contentPane;
	private List<Measurement> boreMeasurementList;
	private boolean isPistonInstalled = false;
	private Button doneButton;
	private Button editButton;
	public PistonClearanceView(OperationProcessor processor) {
		super(processor);
		PistonClearanceValidator validator = new PistonClearanceValidator(this);
		getProcessor().setValidator(validator);
		EventBusUtil.register(this);
	}
	
	@Override
	public void initComponents(){
		getProcessor().setView(this);
		boreMeasurements = new HashMap<Integer, String>();
		borMeasurementPanes = new HashMap<Integer, TextField>();
		pistonMeasurements = new HashMap<Integer, TextField>();
		clearanceMeasurements = new HashMap<Integer, TextField>();
		contentPane = new MigPane("insets 40 20 0 8", "", "");
		if(getProcessor().isCylinderBoreInstalled()){
			this.boreMeasurementList = getProcessor().getInstalledMeasurements(getProcessor().getPropertyBean().getInstalledCylinderBoreName());//order by measurement sequence
			createMeasurements();
			setDefaultFocus(); // focus on the first piston measurement field
			mapActions();
		}else{
			//if cylinder bore has not been installed, show error message
			getProcessor().getController().displayErrorMessage("No cylinder bore installed.");
		}
		this.setCenter(contentPane);
		//make contentPane to layout
		Platform.runLater(new Runnable(){
			public void run(){
				refreshView();
				//contentPane.layout();
			}
		});
		
	}
	
	private void createMeasurements(){
		String layoutConstraints = "insets 30 10 0 0";
		String columnToken = "[grow,fill,center]";
		StringBuilder columns = new StringBuilder();
		for(int i =0; i < this.boreMeasurementList.size(); i ++){
			columns.append(columnToken);
		}
		String columnConstraints = String.format("[130px!]"+ columns.toString());
		int startColumn =1;
		int startRow = 0;
		
		MigPane mainPane = new MigPane(layoutConstraints, columnConstraints);
		createCylinderBorePanel(mainPane, startRow, startColumn);
		startRow = 2;//piston measurement labels and values shown start from the second row  
		createPistonPanel(mainPane, startRow, startColumn);
		startRow = 4;//clearance measurements shown start form the fourth row
		createClearancePanel(mainPane, startRow, startColumn);
		cteateButtonsPanel(mainPane);//clearance spec and buttons
		
		contentPane.add(mainPane);
	}
	
	private void createCylinderBorePanel(MigPane mainPane, int startRow, int startColumn){
		//create cylinder bore name cell
		mainPane.add(UiFactory.getInfo().createLabel("CylinderBore", "Cylinder Bore", TextAlignment.LEFT), new CC().cell(0,0));
		mainPane.add(UiFactory.getInfo().createLabel("Measurement", "Measurement", TextAlignment.LEFT), new CC().cell(0,1));
		//get installed cylinder bore measurement values and display them.
		int measCount = this.boreMeasurementList.size();
		for(int i =0; i < measCount; i ++){
			String measValue = String.valueOf(boreMeasurementList.get(i).getMeasurementValue());
			Label boreLabel = UiFactory.getInfo().createLabel("boreLabel", String.valueOf(i+1), TextAlignment.CENTER);
			TextField boreMeas = UiFactory.getInputBig().createTextField("boreMeas", measValue.length(), TextFieldState.READ_ONLY, Pos.BASELINE_CENTER);
			mainPane.add(boreLabel, new CC().cell(startColumn + i, startRow));
			mainPane.add(boreMeas, new CC().cell(startColumn + i, startRow +1));
			boreMeas.setText(measValue);
			boreMeasurements.put(Integer.valueOf(i), measValue);
			borMeasurementPanes.put(i, boreMeas);
		}

	}
	
	private void createPistonPanel(MigPane mainPane, int startRow, int startColumn){
		mainPane.add(UiFactory.getInfo().createLabel("Piston", "Piston", TextAlignment.LEFT), new CC().cell(0,startRow).gapTop("50px!"));
		mainPane.add(UiFactory.getInfo().createLabel("Measurement", "Measurement", TextAlignment.LEFT), new CC().cell(0,startRow + 1));
		
		List<Measurement> installedPistons = getProcessor().getInstalledMeasurements(getProcessor().getPropertyBean().getPistonName());
		if(installedPistons != null && installedPistons.size() > 0){
			isPistonInstalled = true;
		}
		for(int i =0; i < this.boreMeasurementList.size(); i ++){
			String measValue = String.valueOf(boreMeasurementList.get(i).getMeasurementValue());
			Label pistonLabel = UiFactory.getInfo().createLabel("pistonLabel", String.valueOf(i+1), TextAlignment.CENTER);
			TextField pistonMeas = UiFactory.getInputBig().createTextField("pistonMeas", measValue.length(), TextFieldState.EDIT, Pos.BASELINE_CENTER);
			mainPane.add(pistonLabel, new CC().cell(startColumn + i, startRow).gapTop("50px!"));
			mainPane.add(pistonMeas, new CC().cell(startColumn + i, startRow +1));
			pistonMeasurements.put(i, pistonMeas);
			//check whether piston already installed. if is installed, show them			
			if(installedPistons != null && installedPistons.size() > 0){
				pistonMeas.setText(String.valueOf(installedPistons.get(i).getMeasurementValue()));
				TextFieldState.READ_ONLY.setState(pistonMeas);
			}
		}
	}
	
	private void createClearancePanel(MigPane mainPane, int startRow, int startColumn){
		mainPane.add(UiFactory.getInfo().createLabel("Clearance", "Clearance", TextAlignment.LEFT), new CC().cell(0,startRow).spanY(2).alignY("bottom").gapTop("50px!"));
		
		List<Measurement> installedClearance = getProcessor().getInstalledMeasurements(getProcessor().getPropertyBean().getClearanceName());
		int measCount = this.boreMeasurementList.size();
		for(int i =0; i < measCount; i ++){
			String measValue = String.valueOf(boreMeasurementList.get(i).getMeasurementValue());
			Label clearanceLabel = UiFactory.getInfo().createLabel("clearanceLabel", String.valueOf(i+1), TextAlignment.CENTER);
			TextField clearanceMeas = UiFactory.getInputBig().createTextField("clearanceMeas", measValue.length(), TextFieldState.READ_ONLY, Pos.BASELINE_CENTER);
			mainPane.add(clearanceLabel, new CC().cell(startColumn + i, startRow).gapTop("50px!"));
			mainPane.add(clearanceMeas, new CC().cell(startColumn + i, startRow +1));
			clearanceMeasurements.put(i, clearanceMeas);
			//if clearance is already installed ,show installed value.
			if(isPistonInstalled && installedClearance != null && installedClearance.size() > 0){
				clearanceMeas.setText(String.valueOf(new BigDecimal(installedClearance.get(i).getMeasurementValue()).floatValue()));
			}
		}
		
	}
	/**
	 * this method is used to create edit and done button and notice of clearance range.
	 * @param mainPane
	 */
	private void cteateButtonsPanel(MigPane mainPane){
		Label specLabel =  UiFactory.getInfoSmall().createLabel("specLabel", "Clearance Spec: 0.150 +/- 0.025 (0.125 - 0.175)", TextAlignment.CENTER);
		mainPane.add(specLabel, new CC().cell(0, this.boreMeasurementList.size()).spanX(this.boreMeasurementList.size() + 1).alignX("center").gapTop("20px!"));
		
		if(!isPistonInstalled){
			this.editButton = UiFactory.getInfo().createButton("Edit", true);
			mainPane.add(this.getEditButton(), new CC().cell(0,this.boreMeasurementList.size() + 1).spanX(this.boreMeasurementList.size()).width("120px!").alignX("right").gapTop("15px!"));
			createDoneButton(mainPane, false);
		}else{
			createDoneButton(mainPane, true);
		}
	}
	
	private void createDoneButton(MigPane mainPane, boolean enabled){
		this.doneButton = UiFactory.getInfo().createButton("Done", enabled);
		this.doneButton.defaultButtonProperty().bind(this.doneButton.focusedProperty());
		mainPane.add(this.getDoneButton(), new CC().cell(this.boreMeasurementList.size(), this.boreMeasurementList.size() + 1).width("120px!").alignX("right").gapTop("15px!"));
	}
	
	protected void mapActions(){
		for(TextField measurementField : pistonMeasurements.values()){
			measurementField.setOnAction(new EventHandler<ActionEvent>(){
			   public void handle(ActionEvent e) {
				   getProcessor().processInputValue((TextField)e.getSource());
				}
			});
		}
		
		mapDoneButtonAction();
		
		if(this.getEditButton() != null){
			this.getEditButton().setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					getProcessor().changeToEditableState();
				}
			}
			);
		}
		
	}
	
	private void mapDoneButtonAction(){
		this.getDoneButton().setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				getProcessor().saveData();
			}
		}
		);
	}
	
	public void refreshView(){
		//make contentPane to layout
		Platform.runLater(new Runnable(){
			public void run(){
				contentPane.layout();
			}
		});
		if(!getProcessor().isCylinderBoreInstalled()){
			getProcessor().getController().displayErrorMessage("No cylinder bore installed.");
		}
		List<Measurement> installedPistons = getProcessor().getInstalledMeasurements(getProcessor().getPropertyBean().getPistonName());
		if(installedPistons == null || installedPistons.size() == 0){
			for(int i=0; i<this.boreMeasurementList.size(); i ++){
				if(this.pistonMeasurements.get(i) != null && (this.pistonMeasurements.get(i).getText().isEmpty())){
					getProcessor().getController().setFocusComponent(this.pistonMeasurements.get(i));
					getProcessor().getController().requestFocus();
					return ;
				}
			}
			
		}else{
			//if piston have already been installed, fill the textField with piston measurement values
			resetPistonAndClearance(installedPistons);
			if(this.getEditButton() != null) this.getEditButton().setDisable(true);
			this.getDoneButton().setDisable(false);
			getProcessor().getController().setFocusComponent(this.getDoneButton());
			getProcessor().getController().requestFocus();
		}
		
		
	}
	/**
	 * this method is used to reset data of new entered product id
	 * if cylinder bore is already installed, get bore measurements and show them.
	 * if piston is installed, get piston and clearance measurements and show them.
	 * if piston is not installed, prepare empty textFields of piston and clearance for data entry. 
	 */
	public void resetView(){
		if(getProcessor().isCylinderBoreInstalled()){
			resetCylinderBore();
			List<Measurement> installedPistons = getProcessor().getInstalledMeasurements(getProcessor().getPropertyBean().getPistonName());
			if(installedPistons != null && installedPistons.size() >0){//piston has already been installed
				resetPistonAndClearance(installedPistons);
				if(this.getEditButton() != null) this.getEditButton().setDisable(true);
				if(this.getDoneButton() != null) this.getDoneButton().setDisable(false);
				mapDoneButtonAction();
			}else{
				for(TextField tf : this.pistonMeasurements.values()){
					tf.setText("");
					TextFieldState.EDIT.setState(tf);
				}
				for(TextField tf : this.clearanceMeasurements.values()){
					tf.setText("");
				}
				getProcessor().getPistonMeasValues().values().clear();
				this.getDoneButton().setDisable(true);
				if(this.getEditButton() != null) this.getEditButton().setDisable(false);
				setDefaultFocus(); // focus on the first piston measurement field
			}
		}else{
			getProcessor().getController().displayErrorMessage("No cylinder bore installed.");
		}
	}
	
	/**
	 * reset cylinder bore measurement
	 */
	private void resetCylinderBore(){
		this.boreMeasurementList = getProcessor().getInstalledMeasurements(getProcessor().getPropertyBean().getInstalledCylinderBoreName());//order by measurement sequence
		for(int i=0; i< this.boreMeasurementList.size(); i ++){
			this.boreMeasurements.put(i, String.valueOf(this.boreMeasurementList.get(i).getMeasurementValue()));
			this.borMeasurementPanes.get(i).setText(String.valueOf(this.boreMeasurementList.get(i).getMeasurementValue()));
		}
	}
	
	/**
	 * reset piston and clearance measurement
	 */
	private void resetPistonAndClearance(List<Measurement> installedPistons){
		List<Measurement> installedClearance = getProcessor().getInstalledMeasurements(getProcessor().getPropertyBean().getClearanceName());
		for(int i=0; i< this.boreMeasurementList.size(); i ++){
			this.pistonMeasurements.get(i).setText(String.valueOf(installedPistons.get(i).getMeasurementValue()));
			this.clearanceMeasurements.get(i).setText(String.valueOf(new BigDecimal(installedClearance.get(i).getMeasurementValue()).floatValue()));
			TextFieldState.READ_ONLY.setState(this.pistonMeasurements.get(i));
		}
	}
	
	public Map<Integer, String> getBoreMeasurements() {
		return boreMeasurements;
	}

	public Map<Integer, TextField> getPistonMeasurements() {
		return pistonMeasurements;
	}

	public Map<Integer, TextField> getClearanceMeasurements() {
		return clearanceMeasurements;
	}

	public MigPane getContentPane() {
		return contentPane;
	}

	public PistonClearanceProcessor getProcessor(){
		return (PistonClearanceProcessor)super.getProcessor();
	}

	public List<Measurement> getBoreMeasurementList() {
		return boreMeasurementList;
	}

	public void setBoreMeasurementList(List<Measurement> boreMeasurementList) {
		this.boreMeasurementList = boreMeasurementList;
	}
	
	private void setDefaultFocus(){
		if(!isPistonInstalled){
			getProcessor().getController().setFocusComponent(pistonMeasurements.get(0));
			getProcessor().getController().requestFocus();
		}
	}

	public Button getDoneButton() {
		return doneButton;
	}

	public Button getEditButton() {
		return editButton;
	}

	public boolean isPistonInstalled() {
		return isPistonInstalled;
	}

	public Map<Integer, TextField> getBorMeasurementPanes() {
		return borMeasurementPanes;
	}

	@Subscribe 
	public void handleUnitNavigatorEvent(UnitNavigatorEvent event){
		getLogger().debug("UnitNavigatorWidget.handleEvent recvd : " + event.toString());
		List<MCOperationRevision> structures = this.getProcessor().getController().getModel().getOperations();
        int index = structures.indexOf(this.getProcessor().getOperation());

		if(event.getType().equals(UnitNavigatorEventType.SELECTED) && index == event.getIndex()){
			checkOperationStatus();
			refreshView();
			
		}
	}
	
	 @Subscribe
     public void handleProductStartedEvent(ProductStartedEvent event){ 
         resetView(); 
     } 
	
	 /**
	  * This method is used to check whether operation 'Piston Sub Assembly' is completed.
	  * if is completed, notify the data collection framework, the operation is completed.
	  * 'Piston Sub Assembly' has 2 parts and each part has 6 measurement values, 
	  * and 'Piston Sub Assembly' part has no measurement value. Here we use one of the installedParts(piston)
	  * to compare with mc_op_meas defined under 'Piston Sub Assembly' to do the completion check.
	  * 
	  */
	public void checkOperationStatus(){
		String checkPart = getProcessor().getPropertyBean().getPistonName();//get installed  piston name 
		InstalledPart part = getProcessor().getInstalledpart(getProcessor().getProductId(), checkPart);
		if(part != null){
			List<Measurement>  meas = getProcessor().getInstalledMeasurements(checkPart);
			part.setMeasurements(meas);
			if(getProcessor().getController().isDataCollectionComplete(part, getProcessor().getOperation()) 
					&& !getProcessor().getController().getModel().getCompletedOpsMap().containsKey(getProcessor().getOperation())){
				getProcessor().getController().getModel().getCompletedOpsMap().put(getProcessor().getOperation().getId().getOperationName(), true);
				EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_COMPLETED_FOR_PART,  getProcessor().getOperation()));
			}
		}
	}
}
