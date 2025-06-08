package com.honda.galc.client.dc.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.processor.CylinderShimInstallAbstractProcessor;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.ui.binding.AllTrueBinding;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiUtils;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 9, 2014
 */
public abstract class CylinderShimInstallAbstractBodyPane<V extends CylinderShimInstallAbstractView> extends MigPane {
	public static enum InputFieldType {
		BASE_SHIM_ID, ACT_SHIM_ID, BASE_GAP, NEED_SHIM_ID, FINAL_GAP
	}
	protected final String DONE = "Done";
	protected final String REJECT = "Reject";
	protected final Image IMG_DONE = new Image("resource/images/common/confirm.png");
	protected final Image IMG_REJECT = new Image("resource/images/common/reject.png");
	protected static final double NORMALIZED_IMAGE_SIZE = 30.0;

	protected int cylinderNum;
	protected int cylinderStartNo;
	protected TextField[][] baseShimIds;
	protected TextField[][] baseShims;
	protected LoggedTextField[][] baseGaps;
	protected TextField[][] totalGaps;
	protected TextField[][] targetGaps;
	protected TextField[][] needShimSzs;
	protected TextField[][] needShimIds;
	protected TextField[][] actShimIds;
	protected TextField[][] finalGaps;
	protected Button editBtn, doneBtn;
	protected List<TextField> focusableTextFields;
	protected V view;
	protected Map<TextField, TextFieldCommand> validators = new HashMap<TextField, TextFieldCommand>();
	protected Map<TextField, BooleanProperty> fieldCheckResults = new HashMap<TextField, BooleanProperty>();
	protected AllTrueBinding allFieldsOk;
	protected ObservableList<BooleanExpression> editableFieldList = FXCollections.observableArrayList();
	// Is this work unit finished? If the user re-enter a processed product ID, this value is true.
	protected BooleanProperty finishedProcessing = new SimpleBooleanProperty(false);
	//This Boolean Property is to disable 'Done' button for accidental hit after unit is completed
	protected BooleanProperty disableDoneBtn = new SimpleBooleanProperty(false);
	// The current focused component
	protected Control focusedComponent;

	public CylinderShimInstallAbstractBodyPane(int cylinderNum, int cylinderStartNo, V view) {
		super();
		this.cylinderNum = cylinderNum;
		this.cylinderStartNo = cylinderStartNo;
		this.view = view;
	}

	protected void initComponent() {
		drawPane();
		initFocusFields();
		initBindings();
		initValidators();
		mapActions();
	}

	protected abstract void drawPane();

	protected void initFocusFields() {}

	protected void initBindings() {}

	protected void initValidators() {}

	protected abstract void mapActions();

	public TextField getNextFocusableTextField(TextField currentFocused) {
		return UiUtils.getNextFocusableTextField(focusableTextFields, currentFocused);
	}
	
	public void init() {}

	/*
	 * Be careful that this method has two entries: one is from listeners, the other is from the processor. So just put
	 * the common code here.
	 */
	public void finishedInput(TextField field, InputFieldType type) {}

	public boolean isFinished() {
		return finishedProcessing.get();
	}

	public abstract void setFinished(boolean finished);

	public abstract int[] getTextFieldIndex(TextField field, InputFieldType type);

	public Map<TextField, BooleanProperty> getFieldCheckResults() {
		return fieldCheckResults;
	}
	
	public void setFieldCheckResult(TextField field, boolean checkResult) {
		if(fieldCheckResults.containsKey(field)) {
			fieldCheckResults.get(field).set(checkResult);
		}
	}

	public List<TextField> getFocusableTextFields() {
		return focusableTextFields;
	}

	public V getView() {
		return view;
	}

	public <P extends CylinderShimInstallAbstractProcessor> P getProcessor() {
		return (P)getView().getProcessor();
	}

	public DataCollectionController getController() {
		return getProcessor().getController();
	}

	public AllTrueBinding getAllFieldsOk() {
		return allFieldsOk;
	}

	public Button getDoneBtn() {
		return doneBtn;
	}

	public ObservableList<BooleanExpression> getEditableFieldList() {
		return editableFieldList;
	}

	public Control getFocusedComponent() {
		return focusedComponent;
	}

	public void setFocusedComponent(Control focusedComponent) {
		this.focusedComponent = focusedComponent;
	}

	public TextField[][] getBaseShimIds() {
		return baseShimIds;
	}

	public TextField[][] getBaseShims() {
		return baseShims;
	}

	public TextField[][] getBaseGaps() {
		return baseGaps;
	}

	public TextField[][] getTotalGaps() {
		return totalGaps;
	}

	public TextField[][] getTargetGaps() {
		return targetGaps;
	}

	public TextField[][] getNeedShimSzs() {
		return needShimSzs;
	}

	public TextField[][] getNeedShimIds() {
		return needShimIds;
	}

	public TextField[][] getActShimIds() {
		return actShimIds;
	}

	public TextField[][] getFinalGaps() {
		return finalGaps;
	}
	
	protected ImageView getImageView(Image img) {
	   return normalizeImage(new ImageView(img));
	}
	
	protected ImageView normalizeImage(ImageView imageView) {
		if (imageView.getImage() != null && ( imageView.getImage().getWidth() != NORMALIZED_IMAGE_SIZE || imageView.getImage().getWidth() != NORMALIZED_IMAGE_SIZE)) {
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(NORMALIZED_IMAGE_SIZE);
			imageView.setFitWidth(NORMALIZED_IMAGE_SIZE);
		}
		return imageView;
	}

	public void setTextFieldState(TextField[][] fields, TextFieldState textFieldState) {
		for (TextField[] cylinderFields: fields) {
		    for (TextField field: cylinderFields) {
		    	textFieldState.setState(field);
		    	if(textFieldState.equals(TextFieldState.EDIT)) {
		    		setFieldCheckResult(field, false);
		    		if(!editableFieldList.contains(field.editableProperty())) {
		    			editableFieldList.add(field.editableProperty());
		    		}
		    	}
		    }    
		}
	}
	
	public void requestFocusForFirstField() {
		if(focusableTextFields!=null && focusableTextFields.size()>0) {
			TextField focusableTextField = focusableTextFields.iterator().next();
			getController().setFocusComponent(focusableTextField);
			setFocusedComponent(focusableTextField);
		}
		getController().requestFocus();
	}
}
