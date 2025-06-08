package com.honda.galc.client.dc.view;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.dc.enumtype.ShimInstallPartType;
import com.honda.galc.client.dc.processor.CylinderShimInstallProcessor;
import com.honda.galc.client.dc.validator.ShimBaseGapRangeValidator;
import com.honda.galc.client.dc.validator.ShimFinalGapRangeValidator;
import com.honda.galc.client.dc.validator.ShimIdExistValidator;
import com.honda.galc.client.dc.view.listener.CylinderShimActShimIdListener;
import com.honda.galc.client.dc.view.listener.CylinderShimBaseGapListener;
import com.honda.galc.client.dc.view.listener.CylinderShimBaseShimIdListener;
import com.honda.galc.client.dc.view.listener.CylinderShimFinalGapListener;
import com.honda.galc.client.dc.view.listener.CylinderShimMeasurementListener;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.product.validator.LengthValidator;
import com.honda.galc.client.product.validator.NumericValidator;
import com.honda.galc.client.product.validator.PositiveIntegerValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.ui.binding.AllTrueBinding;
import com.honda.galc.client.ui.binding.AnyFalseBinding;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 9, 2014
 */
public class CylinderShimInstallBodyPane extends CylinderShimInstallAbstractBodyPane<CylinderShimInstallView> {
	private final int FONT_SIZE = getView().getProcessor().getFontSize();
	private List<ShimInstallPartType> visibleShimInstallPartTypes = null;
	public CylinderShimInstallBodyPane(int cylinderNum, int cylinderStartNo, CylinderShimInstallView view) {
		super(cylinderNum, cylinderStartNo, view);
		this.cylinderNum = cylinderNum;
		this.cylinderStartNo = cylinderStartNo;
		this.view = view;
		//Getting list of install parts to be displayed
		this.visibleShimInstallPartTypes = getView().getProcessor().getVisibleShimInstallPartTypes();
		initComponent();
	}

	/**
	 * This method is called by the upper OperationView class to refresh the body pane when it comes to process next Product ID. 
	 */
	public void init() {
		// Clear the fields.
		for (int i = 0; i < cylinderNum; i++) {
			for (int j = 0; j < 2; j++) {
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_SHIM_ID)) {
					baseShimIds[i][j].setText("");
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_GAP)) {
					baseGaps[i][j].setText("");
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.ACTUAL_SHIM_ID)) {
					actShimIds[i][j].setText("");
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.FINAL_GAP)) {
					finalGaps[i][j].setText("");
				}
			}
		}
		// Initialize bindings.
		initBindings();
	}

	/*
	 * Be careful that this method has two entries: one is from listeners, the other is from the processor. So just put
	 * the common code here.
	 */
	public void finishedInput(TextField field, InputFieldType type) {
		if (null == field) {
			return;
		}
		int[] idx = getTextFieldIndex(field, type);
		if(idx!=null) {
			if(InputFieldType.BASE_SHIM_ID.equals(type)) {
				getBaseShims()[idx[0]][idx[1]].setText(getProcessor().findShimSizeById(field.getText()).toString());
			}
			else if (InputFieldType.BASE_GAP.equals(type)) {
				String baseShimId = getBaseShimIds()[idx[0]][idx[1]].getText();
				int baseShim = Integer.parseInt(getBaseShims()[idx[0]][idx[1]].getText());
				int baseGap = Integer.parseInt(field.getText());
				int totalGap = baseShim + baseGap;
				int targetGap = getProcessor().getTargetGap();
				int needShimSz = totalGap - targetGap;
				//Check baseGap value is within the limit
				double[] targetGapRange = getProcessor().getTargetGapRange();
				if(baseGap >= targetGapRange[0] && baseGap <= targetGapRange[1]) {
					//Highlight good data
					TextFieldState.GOOD_READ_ONLY.setState(field);
				}
				getTotalGaps()[idx[0]][idx[1]].setText(String.valueOf(totalGap));
				getTargetGaps()[idx[0]][idx[1]].setText(String.valueOf(targetGap));
				getNeedShimSzs()[idx[0]][idx[1]].setText(String.valueOf(needShimSz));
				getNeedShimIds()[idx[0]][idx[1]].setText(String.valueOf(getProcessor().findShimIdBySize(needShimSz, baseShim, baseShimId)).toUpperCase());
			}
		}
	}

	public int[] getTextFieldIndex(TextField field, InputFieldType type) {
		if (null == field || type == null) {
			return null;
		}
		switch (type) {
		case BASE_SHIM_ID:
			return lookup(baseShimIds, field);
		case BASE_GAP:
			return lookup(baseGaps, field);
		case ACT_SHIM_ID:
			return lookup(actShimIds, field);
		case FINAL_GAP:
			return lookup(finalGaps, field);
		default:
			break;
		}
		return null;
	}

	private int[] lookup(TextField[][] fields, TextField field) {
		int[] indices = new int[2];
		for (int i = 0; i < cylinderNum; i++) {
			for (int j = 0; j < 2; j++) {
				if (field.equals(fields[i][j])) {
					indices[0] = i;
					indices[1] = j;
				}
			}
		}
		return indices;
	}

	public void setFinished(boolean finished) {
		finishedProcessing.set(finished);
		if (finished) {
			doneBtn.setText(REJECT);
			doneBtn.setGraphic(getImageView(IMG_REJECT));
			getController().setFocusComponent(doneBtn);
			setFocusedComponent(doneBtn);
			getController().requestFocus();
		}
		else {
			doneBtn.setText(DONE);
			doneBtn.setGraphic(getImageView(IMG_DONE));
		}
	}
	
	protected void drawPane() {
		AC colConstraints = new AC();
		colConstraints.size("140!", 0);
		for (int i = 1; i < cylinderNum * 2; i++) {
			colConstraints.size("57::", i);
			colConstraints.fill(i);
			if (i % 2 == 0) {
				colConstraints.gap("6", i);
			} else {
				colConstraints.gap("2", i);
			}
		}
		// Since the insets won't take effect for the right side, we need to set the last column's gap instead.
		colConstraints.gap("10", cylinderNum * 2);
		setLayoutConstraints(new LC().insets("10").alignX("center"));
		setColumnConstraints(colConstraints);
		setRowConstraints(new AC().gap("15", 10));
		
		//Add Cylinder and Valve
		add(UiFactory.createLabel("Cylinder", "Cylinder", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 0");
		add(UiFactory.createLabel("Valve", "Valve", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 1");
		for (int i = 0; i < cylinderNum; i++) {
			TextField cylNum = UiFactory.createTextField("cylNum", Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER);
			TextField valveA = UiFactory.createTextField("valveA", Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER);
			TextField valveB = UiFactory.createTextField("valveB", Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER);
			cylNum.setText(String.valueOf(i + cylinderStartNo));
			valveA.setText("A");
			valveB.setText("B");
			add(cylNum, new CC().cell(1 + i * 2, 0, 2, 1));
			add(valveA, new CC().cell(1 + i * 2, 1));
			add(valveB, new CC().cell(2 + i * 2, 1));
		}
		// Add labels to be displayed 
		drawLabels();
		// Add fields to be displayed
		drawFields();
		
		editBtn = UiFactory.getInfo().createButton("Edit");
		add(editBtn, new CC().cell(0, 11, 1, 1).width("120!"));
		doneBtn = UiFactory.getInfo().createButton(DONE, getImageView(IMG_DONE));
		add(doneBtn, new CC().cell(0, 12, 1, 1).width("120!"));
	}
	
	protected void drawLabels() {
		if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_SHIM_ID)) {
			add(UiFactory.createLabel("BaseShimID", "Base Shim ID", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 2");
			add(UiFactory.createLabel("BaseShimSz", "Base Shim Sz", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 3");
			baseShimIds = new LoggedTextField[cylinderNum][2];
			baseShims = new LoggedTextField[cylinderNum][2];
		}
		if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_GAP)) {
			add(UiFactory.createLabel("Measure", "Measure", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 4");
			add(UiFactory.createLabel("TotalGap", "Total Gap", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 5");
			add(UiFactory.createLabel("TargetGap", "Target Gap", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 6");
			add(UiFactory.createLabel("NeedShimSz", "Need Shim Sz", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 7");
			add(UiFactory.createLabel("NeedShimID", "Need Shim ID", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 8");
			baseGaps = new LoggedTextField[cylinderNum][2];
			totalGaps = new LoggedTextField[cylinderNum][2];
			targetGaps = new LoggedTextField[cylinderNum][2];
			needShimSzs = new LoggedTextField[cylinderNum][2];
			needShimIds = new LoggedTextField[cylinderNum][2];
		}
		if(visibleShimInstallPartTypes.contains(ShimInstallPartType.ACTUAL_SHIM_ID)) {
			add(UiFactory.createLabel("Act. Shim ID", "Act. Shim ID", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 9");
			actShimIds = new LoggedTextField[cylinderNum][2];
		}
		if(visibleShimInstallPartTypes.contains(ShimInstallPartType.FINAL_GAP)) {
			add(UiFactory.createLabel("Final Measure", "Final Measure", Fonts.SS_DIALOG_PLAIN(FONT_SIZE)), "cell 0 10");
			finalGaps = new LoggedTextField[cylinderNum][2];
		}
	}

	protected void drawFields() {
		for (int i = 0; i < cylinderNum; i++) {
			for (int j = 0; j < 2; j++) {
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_SHIM_ID)) {
					baseShimIds[i][j] = UiFactory.createTextField("baseShimIds-" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					baseShims[i][j] = UiFactory.createTextField("baseShims" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					add(baseShimIds[i][j], new CC().cell(1 + i * 2 + j, 2));
					add(baseShims[i][j], new CC().cell(1 + i * 2 + j, 3));
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_GAP)) {
					baseGaps[i][j] = UiFactory.createTextField("baseGaps" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					totalGaps[i][j] = UiFactory.createTextField("totalGaps" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					targetGaps[i][j] = UiFactory.createTextField("targetGaps" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					needShimSzs[i][j] = UiFactory.createTextField("needShimSzs" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					needShimIds[i][j] = UiFactory.createTextField("needShimIds" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					add(baseGaps[i][j], new CC().cell(1 + i * 2 + j, 4));
					add(totalGaps[i][j], new CC().cell(1 + i * 2 + j, 5));
					add(targetGaps[i][j], new CC().cell(1 + i * 2 + j, 6));
					add(needShimSzs[i][j], new CC().cell(1 + i * 2 + j, 7));
					add(needShimIds[i][j], new CC().cell(1 + i * 2 + j, 8));
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.ACTUAL_SHIM_ID)) {
					actShimIds[i][j] = UiFactory.createTextField("actShimIds" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					add(actShimIds[i][j], new CC().cell(1 + i * 2 + j, 9));
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.FINAL_GAP)) {
					finalGaps[i][j] = UiFactory.createTextField("finalGaps" + Integer.toString(i) + "-" + Integer.toString(j), Fonts.SS_DIALOG_PLAIN(FONT_SIZE), TextFieldState.READ_ONLY, Pos.CENTER_LEFT);
					add(finalGaps[i][j], new CC().cell(1 + i * 2 + j, 10));
				}
			}
		}
	}
	
	protected void initFocusFields() {
		focusableTextFields = new ArrayList<TextField>();
		for (int i = 0; i < cylinderNum; i++) {
			for (int j = 0; j < 2; j++) {
				switch (getProcessor().getShimInstallPartType()) {
					case BASE_SHIM_ID:
						focusableTextFields.add(baseShimIds[i][j]);
						break;
					case BASE_GAP:
						focusableTextFields.add(baseGaps[i][j]);
						break;
					case ACTUAL_SHIM_ID:
						focusableTextFields.add(actShimIds[i][j]);
						break;
					case FINAL_GAP:
						focusableTextFields.add(finalGaps[i][j]);
						break;
					default:
						break;
				}
			}
		}
		requestFocusForFirstField();
	}
	
	protected void initBindings() {
		editableFieldList.clear();
		fieldCheckResults.clear();
		for (int i = 0; i < cylinderNum; i++) {
			for (int j = 0; j < 2; j++) {
				switch (getProcessor().getShimInstallPartType()) {
					case BASE_SHIM_ID:
						fieldCheckResults.put(baseShimIds[i][j], new SimpleBooleanProperty(false));
						break;
					case BASE_GAP:
						fieldCheckResults.put(baseGaps[i][j], new SimpleBooleanProperty(false));
						break;
					case ACTUAL_SHIM_ID:
						fieldCheckResults.put(actShimIds[i][j], new SimpleBooleanProperty(false));
						break;
					case FINAL_GAP:
						fieldCheckResults.put(finalGaps[i][j], new SimpleBooleanProperty(false));
						break;
					default:
						break;
				}
			}
		}
		//Setting default value on initialization
		disableDoneBtn.set(false);
		AnyFalseBinding anyEditableFieldsUneditabled = new AnyFalseBinding(editableFieldList);
		editBtn.disableProperty().bind(anyEditableFieldsUneditabled.not().or(finishedProcessing));
		allFieldsOk = new AllTrueBinding(FXCollections.observableArrayList(fieldCheckResults.values()));
		doneBtn.disableProperty().bind((allFieldsOk.not().and(finishedProcessing.not())).or(disableDoneBtn));
		doneBtn.defaultButtonProperty().bind(doneBtn.focusedProperty());
	}
	
	protected void initValidators() {
		List<Command> shimIdValidators = new ArrayList<Command>();
		shimIdValidators.add(new RequiredValidator());
		shimIdValidators.add(new NumericValidator());
		shimIdValidators.add(new LengthValidator(3));
		shimIdValidators.add(new ShimIdExistValidator(getView().getProcessor()));
		List<Command> finalGapSizeValidators = new ArrayList<Command>();
		finalGapSizeValidators.add(new RequiredValidator());
		finalGapSizeValidators.add(new PositiveIntegerValidator());
		finalGapSizeValidators.add(new ShimFinalGapRangeValidator(getView().getProcessor()));

		for (int i = 0; i < cylinderNum; i++) {
			for (int j = 0; j < 2; j++) {
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_SHIM_ID)) {
					validators.put(baseShimIds[i][j], TextFieldCommand.create(getController(), baseShimIds[i][j], shimIdValidators, "Base Shim ID"));
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_GAP)) {
					List<Command> baseGapSizeValidators = new ArrayList<Command>();
					baseGapSizeValidators.add(new RequiredValidator());
					baseGapSizeValidators.add(new PositiveIntegerValidator());
					baseGapSizeValidators.add(new ShimBaseGapRangeValidator(baseShimIds[i][j], getView().getProcessor()));
					validators.put(baseGaps[i][j], TextFieldCommand.create(getController(), baseGaps[i][j], baseGapSizeValidators, "Measure"));
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.ACTUAL_SHIM_ID)) {
					validators.put(actShimIds[i][j], TextFieldCommand.create(getController(), actShimIds[i][j], shimIdValidators, "Act. Shim ID"));
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.FINAL_GAP)) {
					validators.put(finalGaps[i][j], TextFieldCommand.create(getController(), finalGaps[i][j], finalGapSizeValidators, "Final Gap Size"));
				}
			}
		}
	}
	
	protected void mapActions() {
		CylinderShimMeasurementListener<CylinderShimInstallView, CylinderShimInstallBodyPane, CylinderShimInstallProcessor> listener = null;
		for (int i = 0; i < cylinderNum; i++) {
			for (int j = 0; j < 2; j++) {
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_SHIM_ID)) {
					listener = new CylinderShimBaseShimIdListener(getView(), baseShimIds[i][j], validators.get(baseShimIds[i][j]));
					baseShimIds[i][j].setOnAction(listener);
					baseShimIds[i][j].textProperty().addListener(listener);
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.BASE_GAP)) {
					listener = new CylinderShimBaseGapListener(getView(), baseGaps[i][j], validators.get(baseGaps[i][j]));
					baseGaps[i][j].setOnAction(listener);
					baseGaps[i][j].textProperty().addListener(listener);
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.ACTUAL_SHIM_ID)) {
					listener = new CylinderShimActShimIdListener(getView(), actShimIds[i][j], validators.get(actShimIds[i][j]));
					actShimIds[i][j].setOnAction(listener);
					actShimIds[i][j].textProperty().addListener(listener);
				}
				if(visibleShimInstallPartTypes.contains(ShimInstallPartType.FINAL_GAP)) {
					listener = new CylinderShimFinalGapListener(getView(), finalGaps[i][j], validators.get(finalGaps[i][j]));
					finalGaps[i][j].setOnAction(listener);
					finalGaps[i][j].textProperty().addListener(listener);
				}
			}
		}
		editBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				switch (getProcessor().getShimInstallPartType()) {
					case BASE_SHIM_ID:
						setTextFieldState(getBaseShimIds(), TextFieldState.EDIT);
						break;
					case BASE_GAP:
						setTextFieldState(getBaseGaps(), TextFieldState.EDIT);
						break;
					case ACTUAL_SHIM_ID:
						setTextFieldState(getActShimIds(), TextFieldState.EDIT);
						break;
					case FINAL_GAP:
						setTextFieldState(getFinalGaps(), TextFieldState.EDIT);
						break;
					default:
						break;
				}
				requestFocusForFirstField();
			}
		});
		doneBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (!isFinished()) {
					disableDoneBtn.set(true);
					getProcessor().saveData();
					getProcessor().getAudioManager().playOKSound();
					setFinished(true);
				} else {
					//Reject is clicked
					getProcessor().rejectData();
				}
			}
		});
	}
}
