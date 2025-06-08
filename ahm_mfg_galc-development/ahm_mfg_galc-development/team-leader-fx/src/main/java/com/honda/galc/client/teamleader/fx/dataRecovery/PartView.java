package com.honda.galc.client.teamleader.fx.dataRecovery;


import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import net.miginfocom.layout.AC;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.validator.RegExpValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.entity.product.ProductBuildResult;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartView</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 * @created July 14, 2017
 */
public class PartView extends MigPane  {

	private int statusFieldWidth;

	private boolean renderStatus;
	private boolean renderValue;
	private boolean editable;

	private HBox statusPanel;
	private UpperCaseFieldBean statusView;
	private LoggedComboBox<?> statusInput;
	private List<LoggedTextField> valueFields;

	private PartDefinition partDefinition;
	private ProductBuildResult buildResult;

	public PartView(PartDefinition partDefinition, boolean renderStatus, boolean renderValue) {
		this.partDefinition = partDefinition;
		this.renderStatus = renderStatus;
		this.renderValue = renderValue;
		this.valueFields = new ArrayList<LoggedTextField>();
		createComponents();
		this.setPadding(new Insets(2));
		initLayout(); 
		
	}

	public PartView(PartDefinition partDefinition, boolean renderStatus, boolean renderValue, int statusFieldWidth) {
		this.partDefinition = partDefinition;
		this.renderStatus = renderStatus;
		this.renderValue = renderValue;
		this.valueFields = new ArrayList<LoggedTextField>();
		this.statusFieldWidth = statusFieldWidth;
		createComponents();
		initLayout();
	}

	protected void createComponents() {

		statusPanel = new HBox();
		statusView = createStatusField();
		statusInput = createStatusComboBox();

		int tokenCount = getPartDefinition().getTokenCount() < 1 ? 1 : getPartDefinition().getTokenCount();
		for (int i = 0; i < tokenCount; i++) {
			LoggedTextField field = createValueField();
			getValueFields().add(field);
			List<Command> validators = new ArrayList<Command>();
			validators.add(new RequiredValidator());
			if (StringUtils.isNotEmpty(getPartDefinition().getValuePattern())) {
				validators.add(new RegExpValidator(StringUtils.trim(getPartDefinition().getValuePattern())));
			}
			String name = String.format("%s (%s)", StringUtils.trimToEmpty(getPartDefinition().getLabel()), getPartDefinition().getName());
			ChainCommand validator = ChainCommand.create(validators, name);
			validator.setShortCircuit(true);
			UiUtils.mapValidator(field, validator);
		}
	}

	protected void initLayout() {
		StackPane tp = new StackPane();
		tp.getChildren().add(getStatusView());
		tp.getChildren().add(getStatusInput());
		getStatusPanel().getChildren().add(tp);
		getStatusPanel().getChildren().clear();
		getStatusPanel().getChildren().add(getStatusInput());
		if (isRenderStatus()) {
			add(getStatusPanel(),"growx");
		}
		if (isRenderValue()) {
			for (LoggedTextField tf : getValueFields()) {
				add(tf,"growx");
			}
		}
		this.setColumnConstraints(new AC().align("left").gap("2"));
		this.setPrefWidth(Utils.getScreenWidth() * 0.09);
		this.setPrefHeight(Utils.getScreenHeight() * 0.05);
	}

	@SuppressWarnings("unchecked")
	protected LoggedComboBox<?> createStatusComboBox() {
		LoggedComboBox<?> comp = new LoggedComboBox<String>();
		comp.setItems(FXCollections.observableArrayList(BuildAttributeStatus.values()));
		comp.requestFocus();
		comp.setMaxWidth(Utils.getScreenWidth() * 0.09);
		return comp;
	}

	protected UpperCaseFieldBean createStatusField() {
		UpperCaseFieldBean comp = new UpperCaseFieldBean("");
		TextFieldState.DISABLED.setState(comp);
		comp.setAlignment(Pos.CENTER);
		comp.setPrefWidth(Utils.getScreenWidth() * 0.09);
		return comp;
	}

	protected UpperCaseFieldBean createValueField() {
		UpperCaseFieldBean textField = new UpperCaseFieldBean("");
		textField.setFocusTraversable(true);
		textField.requestFocus();
		if (getPartDefinition().getLength() == 1) {
			textField.setAlignment(Pos.CENTER);
		}
		addListeners(textField);
		TextFieldState.DISABLED.setState(textField);
		textField.setPrefWidth(Utils.getScreenWidth() * 0.09);
		return textField;
	}

	protected void addListeners(final UpperCaseFieldBean textField) {
		if (getPartDefinition().getLength() > 0) {
			textField.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(getPartDefinition().getLength()));
		} else {
			textField.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(30));
		}
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,Boolean oldValue, Boolean newValue) {
				if(newValue || oldValue){
					textField.selectAll();
				}
			}
		
		});
		textField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,String oldValue, String arg2) {
				if (TextFieldState.ERROR.isInState(textField)) {
					TextFieldState.EDIT.setState(textField);
				}
				if (getPartDefinition() != null && getPartDefinition().isValueDecimal()){

					if(QiCommonUtil.isDecimalField(textField)){
						textField.clear();
						return;
					}
				}
				else if (getPartDefinition() != null && getPartDefinition().isValueNumeric()){
					if(QiCommonUtil.isNumberTextMaximum(0,textField)){
						return;
					}
				}
				
				if (textField.getSkin() instanceof BehaviorSkinBase) {
					if (getPartDefinition() != null && getPartDefinition().getLength() == textField.getText().length()) {
						((BehaviorSkinBase) textField.getSkin()).getBehavior().traverseNext();
					}
				}
			}

		});
	}

	// === controlling api === //
	public void setIdleMode() {
		getStatusPanel().getChildren().clear();
		getStatusPanel().getChildren().add(getStatusView());
		getStatusView().setText("");
		TextFieldState.DISABLED.setState(getStatusView());
		getStatusInput().setDisable(true);
		UiUtils.setText(getValueFields(), "");
		UiUtils.setState(getValueFields(), TextFieldState.DISABLED);
	}

	@SuppressWarnings("unchecked")
	public void setReadOnlyMode() {
		getStatusPanel().getChildren().clear();
		getStatusPanel().getChildren().add(getStatusView());
		TextFieldState state = TextFieldState.ERROR_READ_ONLY;

		BuildAttributeStatus status = BuildAttributeStatus.FAILED;

		if (getBuildResult() != null && getBuildResult().isStatusOk()) {
			state = TextFieldState.GOOD_READ_ONLY;
			status = BuildAttributeStatus.OK;
		}
		if (getBuildResult() != null) {
			getStatusView().setText(status.getLabel());
		}
		getStatusInput().getSelectionModel().select(status);
		getStatusInput().setDisable(true);
		state.setState(getStatusView());
		UiUtils.setState(getValueFields(), state);

		if (getBuildResult() != null && getBuildResult().getResultValue() != null) {
			if (getValueFields().size() == 1) {
				getValueFields().get(0).setText(getBuildResult().getResultValue());
			} else if (getValueFields().size() > 1) {
				String[] values = getBuildResult().getResultValue().trim().split(getPartDefinition().getDelimiter());
				for (int i = 0; i < values.length && i < getValueFields().size(); i++) {
					getValueFields().get(i).setText(values[i].trim());
				}
			}
		}
	}

	public void setEditMode() {
		getStatusPanel().getChildren().clear();
		getStatusPanel().getChildren().add(getStatusInput());
		TextFieldState state = TextFieldState.EDIT;
		UiUtils.setState(getValueFields(), state);
		UiUtils.setText(getValueFields(), "");
		getStatusInput().setDisable(false);
	}

	protected Node getFirstFocusableComponent() {
		if (isRenderStatus() && !getStatusInput().isDisabled()) {
			return getStatusInput();
		}
		for (LoggedTextField tf : getValueFields()) {
			if (!tf.isDisabled() && tf.isEditable()) {
				return tf;
			}
		}
		return null;
	}

	protected LoggedTextField getFirstTextFieldInState(TextFieldState state) {
		for (LoggedTextField tf : getValueFields()) {
			if (state.isInState(tf)) {
				return tf;
			}
		}
		return null;
	}

	public String validateInput() {
		if (isRenderStatus() && BuildAttributeStatus.FAILED.equals(getStatusInput().getSelectionModel().getSelectedItem())) {
			return null;
		}
		if (isRenderValue()) {
			for (LoggedTextField tf : getValueFields()) {
				String str = tf.getText().trim();
				tf.setText(str);
			}
			for (LoggedTextField tf : getValueFields()) {
				List<String> msgs = UiUtils.validate(tf);
				if (msgs != null && !msgs.isEmpty()) {
					TextFieldState.ERROR.setState(tf);
					return StringUtils.join(msgs, "\n");
				}
			}
		}
		return null;
	}

	// === config api === //
	protected boolean isRenderStatus() {
		return renderStatus;
	}

	protected boolean isRenderStatusOnly() {
		return isRenderStatus() && !isRenderValue();
	}

	protected boolean isRenderValue() {
		return renderValue;
	}

	protected boolean isRenderValueOnly() {
		return isRenderValue() && !isRenderStatus();
	}

	// === get/set === //
	protected PartDefinition getPartDefinition() {
		return partDefinition;
	}

	protected List<LoggedTextField> getValueFields() {
		return valueFields;
	}

	protected HBox getStatusPanel() {
		return statusPanel;
	}

	protected UpperCaseFieldBean getStatusView() {
		return statusView;
	}

	protected LoggedComboBox<?> getStatusInput() {
		return statusInput;
	}

	public ProductBuildResult getBuildResult() {
		return buildResult;
	}

	public void setBuildResult(ProductBuildResult buildResult) {
		this.buildResult = buildResult;
	}

	public boolean isEditable() {
		return editable;
	}

	protected int getStatusFieldWidth() {
		return statusFieldWidth;
	}

	protected void setStatusFieldWidth(int statusFieldWidth) {
		this.statusFieldWidth = statusFieldWidth;
	}

}
