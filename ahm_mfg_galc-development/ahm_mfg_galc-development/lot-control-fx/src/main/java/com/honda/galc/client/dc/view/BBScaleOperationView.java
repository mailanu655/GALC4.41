package com.honda.galc.client.dc.view;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.processor.BBScaleProcessor;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.constant.OperationType;
import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;


public class BBScaleOperationView extends OperationView {

	@FXML
	private LoggedLabel partMask;

	@FXML
	private Rectangle torqueRect;
	private	volatile ArrayList<TextField> inputFields = new ArrayList<TextField>();

	public BBScaleOperationView(OperationProcessor opProcessor) {
		super(opProcessor);
		EventBusUtil.register(this);
		init();
	}
	
	public BBScaleProcessor getProcessor() {
		return (BBScaleProcessor) super.getProcessor();
	}

	@FXML
	public void partScanInputAction(ActionEvent event) {
		getLogger().info("Scan received:" + ((TextField)event.getSource()).getText());
		PartSerialNumber partSn = new PartSerialNumber(((TextField)event.getSource()).getText());
		EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_RECEIVED, partSn));
	}

	@FXML
	public void partMeasurementInputAction(ActionEvent event) {
		String val = ((TextField)event.getSource()).getText();
		MeasurementValue measurementVal = new MeasurementValue();
		measurementVal.setMeasurementValue(Double.parseDouble(val));
		EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_RECEIVED, measurementVal));
	}

	@FXML
	public void skipButtonAction(ActionEvent event) {
		EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_SKIP, null));
	}
	
	@FXML
	public void rejectButtonAction(ActionEvent event) {
		EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_REJECT, null));
	}
	
	public void init(BorderPane datacollectionPane) {
		init();
	}
	
	public void init() {
		loadFXML();
		BBScaleProcessor.setViewInitialized(true);
	}
	
	protected URL getViewURL() {
		// return getClass().getResource(getView().getViewPath());
		return getClass().getResource(
				"/resource/com/honda/galc/client/dc/view/BBScalesFXMLPane.fxml");
	}

	public Node loadFXML() {

		URL resource = getViewURL();

		assert resource != null;

		FXMLLoader loader = new FXMLLoader(resource);
		Node screen = null;

		loader.setController(this);
		loader.setRoot(this);
		try {
			screen = (Node) loader.load();
		} catch (LoadException e) {
			if (e.getMessage().compareTo("Root value already specified.") == 0
					|| e.getMessage().compareTo(
							"Controller value already specified.") == 0) {
				String message = String
						.format("Error [%s] encountered when loading the FXML file [%s].\n\n"
								+ "The scene definition must be defined as follows :\n"
								+ "   MUST be contained within a root node\n"
								+ "   MUST NOT define a controller attribute in fx:root.\n\n"
								+ "For Example :\n\n"
								+ "<fx:root type=\"javafx.scene.layout.BorderPane\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n"
								+ "  <center>\n"
								+ "   content .... \n"
								+ "  </center>\n"
								+ "</fx:root>\n\n"
								+ "Please refer to http://docs.oracle.com/javafx/2/fxml_get_started/custom_control.htm for further details\n",
								e.getMessage(), resource);
				MessageDialog.showScrollingInfo(null, message, 10, 50);
			} else {
				MessageDialog.showScrollingInfo(null, e.getMessage(), 10, 50);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return screen;
	}

	public TextField getTextField(String id) {
		return (TextField) this.lookup("#" + id);
	}
	
	public Label getLabelField(String id) {
		return (Label) this.lookup("#" + id);
	}
	
	public Node getNode(String id) {
		return (Node) this.lookup("#" + id);
	}
	
	public ImageView getImageViewField(String id) {
		return (ImageView) this.lookup("#" + id);
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {
		TextField textNode = getTextField("vin");
		Node vinLbl = getNode("vin-label");
		Node carImg = getNode("cc_car_img");
		textNode.setManaged(true);
		textNode.setVisible(false);
		vinLbl.setManaged(true);
		vinLbl.setVisible(false);
		carImg.setManaged(true);
		
		GridPane bbGrid = (GridPane)getNode("bbgrid_01");
		bbGrid.setVgap(2);
		bbGrid.setHgap(2);
	     ColumnConstraints col = new ColumnConstraints();
	     ColumnConstraints colImg = new ColumnConstraints();
	     colImg.setHalignment(HPos.CENTER);

	     bbGrid.getColumnConstraints().addAll(col,col, col, colImg);


		textNode.textProperty().addListener(
				new ChangeListener() {
					public void changed(ObservableValue observableValue,
							Object oldValue, Object newValue) {
						if (newValue == null) return;
						String newVin = newValue.toString();
						String oldVin = "";
						if (oldValue != null) oldVin = oldValue.toString();
						
						if (!newVin.isEmpty() && !newVin.equalsIgnoreCase(oldVin)) {
							getProcessor().getDevice().requestWeights(newVin);
						}
					}
				});
		BBScaleProcessor.setViewInitialized(true);
		getProcessor().setView(this);
		getProcessor().updateVin();
	}
	
	@FXML
    public void handleButtonAction(ActionEvent event) {
        Button myButton = (Button) event.getSource();
        String bId = myButton.getId();
        if (bId.equals("cMode"))  {
        	String label = myButton.getText();
        	if(label.contains("Stop"))  {
            	getProcessor().getDevice().setContinousMode(false);
        		myButton.setText("Start cont. mode");
        		//myButton.setStyle("-fx-font: 12 arial; -fx-background-color: lightgray;");
        		ImageView iView = (ImageView)myButton.lookup("#cMode_icon");
        		if(iView != null)  {
        			iView.setImage(new Image("images/bbscales/Check-icon.png"));
        		}
        	}
        	else if(label.contains("Start"))  {
        		getProcessor().getDevice().setContinousMode(true);
        		myButton.setText("Stop cont. mode");
        		//myButton.setStyle("-fx-font: 12 arial; -fx-background-color: red;");
        		ImageView iView = (ImageView)myButton.lookup("#cMode_icon");
        		if(iView != null)  {
        			iView.setImage(new Image("images/bbscales/X-icon.png"));       		
        		}
        	}
        }else if (bId.equals("clear"))  {
        	getProcessor().stopClock();
        	getProcessor().resetValues();
            getProcessor().completeOperation();
            getProcessor().clearListeners();
        	this.getProcessor().getController().cancel();
        }
            
  	}

	public MCOperationPartRevision getPartSpec() {
		return getOperation().getSelectedPart();
	}
	
	public boolean isScanPart() {
		return (getOperation().getType().equals(OperationType.GALC_SCAN_WITH_MEAS) ||
		getOperation().getType().equals(OperationType.GALC_SCAN) || 
		getOperation().getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL));
	}
	
	private void fillTextField(int inputFieldIndex, String text) {
		if (inputFieldIndex < inputFields.size()) {
			TextField txtField = inputFields.get(inputFieldIndex);
			txtField.setEditable(false);
			txtField.setFocusTraversable(false);
			txtField.setText(text);
		}
	}
	
	public void populateCollectedData(InstalledPart installedPart) {
		if (isScanPart() && !StringUtils.trimToEmpty(installedPart.getPartSerialNumber()).equals("")) {
			fillTextField(0, installedPart.getPartSerialNumber());
		}
		for (Measurement measurement: installedPart.getMeasurements()) {
			int measurementSeq = measurement.getId().getMeasurementSequenceNumber();
			int index = isScanPart() ? measurementSeq : --measurementSeq;
			fillTextField(index, Double.toString(measurement.getMeasurementValue()));
		}
	}
}
