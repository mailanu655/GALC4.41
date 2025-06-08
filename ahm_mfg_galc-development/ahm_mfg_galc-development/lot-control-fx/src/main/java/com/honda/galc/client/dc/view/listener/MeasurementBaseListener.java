package com.honda.galc.client.dc.view.listener;

import javafx.scene.control.TextField;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.view.OperationView;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.ui.listener.BaseListener;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 9, 2014
 */
public class MeasurementBaseListener<T extends OperationView> extends BaseListener<T> {
	protected TextField field;
	protected TextFieldCommand validator;

	public MeasurementBaseListener(T view) {
		this(view, null);
	}

	public MeasurementBaseListener(T view, TextFieldCommand validator) {
		this(view, null, validator);
	}

	public MeasurementBaseListener(T view, TextField field, TextFieldCommand validator) {
		super(view);
		this.field = field;
		this.validator = validator;
	}

	protected void preExecute(TextField currentField) {
		getController().clearMessages();
		if (null != validator) {
			if (null != currentField && !currentField.equals(validator.getTextField())) {
				validator.setTextField(currentField);
			}
			validator.execute();
		}
	}

	protected void preExecute() {
		preExecute(null);
	}

	protected void postExecute() {
		getController().processMessages();
		getController().requestFocus();
	}

	public OperationProcessor getProcessor() {
		return getView().getProcessor();
	}

	public DataCollectionController getController() {
		return getProcessor().getController();
	}

	public TextField getField() {
		return field;
	}

	public void setField(TextField field) {
		this.field = field;
	}

	public TextFieldCommand getValidator() {
		return validator;
	}

	public void setValidator(TextFieldCommand validator) {
		this.validator = validator;
	}

}
