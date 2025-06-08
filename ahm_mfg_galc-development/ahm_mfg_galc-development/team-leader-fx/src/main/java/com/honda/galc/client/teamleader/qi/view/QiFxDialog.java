package com.honda.galc.client.teamleader.qi.view;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.StatusMessagePane;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationDialogController</code> is the parent class for QI Dialog.
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
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public class QiFxDialog<M extends QiModel> extends FxDialog {
	
	private LoggedRadioButton activeRadioBtn;
	private LoggedRadioButton inactiveRadioBtn;
	private RadioButton primaryRadioBtn;
	private RadioButton secondaryRadioBtn;
	protected LoggedButton updateButton;
    private String screenName; 
	
	M model;
	private StatusMessagePane statusMessagePane;
	
	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public StatusMessagePane getStatusMessagePane() {
		return statusMessagePane;
	}

	public void setStatusMessagePane(StatusMessagePane statusMessagePane) {
		this.statusMessagePane = statusMessagePane;
	}

	public QiFxDialog(String title, Stage owner, M model) {
		super(title, owner, model.getApplicationContext().getTerminalId());
		this.model = model;
		EventBusUtil.register(this);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());
		
		this.setOnCloseRequest( new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
			    close();	
			}
		});
	}
	
	public QiFxDialog(String title, String applicationId, M model) {
		this(title, ClientMainFx.getInstance().getStage(applicationId),model);
	}
	
	@Override
 	public void close() {
 	   EventBusUtil.unregister(this);
 	   super.close();
	}


	protected Pane initStatusMessagePane() {
		statusMessagePane = new StatusMessagePane(true);
		return statusMessagePane;
	}
	
	@Subscribe
	public void processEvent(StatusMessageEvent event) {
		String applicationId = event.getApplicationId();
		if (applicationId == null || this.model.getApplicationId().equals(applicationId)) {
			switch(event.getEventType()) {
				case DIALOG_INFO:
				setMessage(event.getMessage(),Color.YELLOWGREEN);
				break;
				case INFO:
					break;
				case WARNING:
				case DIALOG_ERROR:
					setErrorMessage(event.getMessage());
					break;
				case CLEAR:
					clearStatusMessage();
					break;
				default:
			}
		}
	}

	public void setErrorMessage(String errorMessage) {
		this.getStatusMessagePane().setErrorMessageArea(errorMessage);
	}
	
	public void setMessage(String message, Color yellowgreen) {
		this.getStatusMessagePane().setMessage(message, yellowgreen);
	}
	
	public void setMessageBig(String message, Color colorBg) {
		this.getStatusMessagePane().setMessageBig(message, colorBg);
	}
	
	public void clearStatusMessage() {
		this.getStatusMessagePane().setStatusMessage(null);
		this.getStatusMessagePane().clearErrorMessageArea();
	}

	/**
	 * This method is used to Create HBox which consists of active/inactive radio buttons
	 * @param handler
	 * @return radioBtnContainer
	 */
	public HBox createStatusRadioButtons(EventHandler<ActionEvent> handler) {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		activeRadioBtn = createRadioButton(QiConstant.ACTIVE, group, true, handler);
		inactiveRadioBtn = createRadioButton(QiConstant.INACTIVE, group, false, handler);
		
		radioBtnContainer.getChildren().addAll(activeRadioBtn, inactiveRadioBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(10, 0, 0, 10));
		return radioBtnContainer;
	}
	
	public HBox createPositionRadioButtons(EventHandler<ActionEvent> handler) {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		primaryRadioBtn = createRadioButton(QiConstant.PRIMARY, group, true, handler);
		secondaryRadioBtn = createRadioButton(QiConstant.SECONDARY, group, false, handler);
		
		radioBtnContainer.getChildren().addAll(primaryRadioBtn, secondaryRadioBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_RIGHT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(5, 0, 0, 10));
		return radioBtnContainer;
	}
	
	/**
	 * This method is used to create Radio Button.
	 * @param title
	 * @param group
	 * @param isSelected
	 * @return
	 */
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler);
		return radioButton;
	}

	public LoggedRadioButton getActiveRadioBtn() {
		return activeRadioBtn;
	}

	public void setActiveRadioBtn(LoggedRadioButton activeRadioBtn) {
		this.activeRadioBtn = activeRadioBtn;
	}

	public LoggedRadioButton getInactiveRadioBtn() {
		return inactiveRadioBtn;
	}

	public void setInactiveRadioBtn(LoggedRadioButton inactiveRadioBtn) {
		this.inactiveRadioBtn = inactiveRadioBtn;
	}

	public RadioButton getPrimaryRadioBtn() {
		return primaryRadioBtn;
	}

	public void setPrimaryRadioBtn(RadioButton primaryRadioBtn) {
		this.primaryRadioBtn = primaryRadioBtn;
	}

	public RadioButton getSecondaryRadioBtn() {
		return secondaryRadioBtn;
	}

	public void setSecondaryRadioBtn(RadioButton secondaryRadioBtn) {
		this.secondaryRadioBtn = secondaryRadioBtn;
	}
	/**
	 * This method is used to create Check Box.
	 * @param text
	 * @param handler
	 * @return
	 */
	public CheckBox createCheckBox(String text,EventHandler<ActionEvent> handler)
	{
		CheckBox chkBox=new CheckBox();
		chkBox.setOnAction(handler);
		chkBox.setText(text);
		chkBox.getStyleClass().add("radio-btn");
		return chkBox;
	}
	
	/**
	 * This method is used to add validation message on popup screen.
	 * @param messageLabel
	 * @param message
	 * @param styleClass
	 * @return
	 */
	public void displayValidationMessage(LoggedLabel messageLabel ,String message, String styleClass){
		messageLabel.setText(message);
		Tooltip tooltip = new Tooltip(message);
		tooltip.getStyleClass().add("display-label");
		messageLabel.setTooltip(tooltip);
		messageLabel.getStyleClass().add(styleClass);
	}
	
	/**
	 *  This is used to create the serial number dynamically on the TableView's '#' column
	 * @param rowIndex
	 */
	public void createSerialNumber(LoggedTableColumn rowIndex){
		rowIndex.setCellFactory( new Callback<LoggedTableColumn, LoggedTableCell>()
				{
			public LoggedTableCell call(LoggedTableColumn p)
			{
				return new LoggedTableCell()
				{
					@Override
					public void updateItem( Object item, boolean empty )
					{
						super.updateItem( item, empty );
						setText( empty ? null : getIndex() + 1 + "" );
					}
				};
			}
				});
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	/**
	 * This method is used to create Filter Text Field.
	 * @return
	 */
	public UpperCaseFieldBean createFilterTextField(String id, int width, EventHandler<ActionEvent> handler) {
		UpperCaseFieldBean filterTextField = (UpperCaseFieldBean) UiFactory.createUpperCaseFieldBean(id, width, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		filterTextField.setOnAction(handler);
		return filterTextField;
	}
	

	public void setUpdateButton(LoggedButton updateButton) {
		this.updateButton = updateButton;
	}

	public LoggedButton getUpdateButton() {
		return updateButton;
	}
	
	/**
	 * This method is used to get the Column info of the TableView 
	 * @param tableView
	 * @param name
	 * @return
	 */
	protected <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
	    for (TableColumn<T, ?> col : tableView.getColumns())
	        if (col.getText().equals(name)) return col ;
	    return null ;
	} 

}
