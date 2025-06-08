package com.honda.galc.client.qi.base;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * <h3>AbstractQiProcessView description</h3> <h4>Description</h4>
 * <p>
 * <code>AbstractQiProcessView</code> is model for Defect Entry Screen
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
 * <TD>26/11/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public abstract class AbstractQiProcessView<M extends QiProcessModel,C extends AbstractQiProcessController<?,?>> extends AbstractProcessView<M,C>{
	private Stage stage;
	
	public AbstractQiProcessView(ViewId viewId, MainWindow window) {
		super(viewId, window);
		stage = ClientMainFx.getInstance().getStage(getApplicationId());
		getController().initializeListeners();
		addMaximizedListener();
	}
	
	private void addMaximizedListener()  {
		getStage().maximizedProperty().addListener(
				new ChangeListener<Boolean>() {
				    @Override
				    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				    	if(!t.equals(t1) && t1==true)  {
				            Rectangle2D screenBounds = getCurrentScreenBounds();
		                	Stage stage = getStage();
		               		stage.setMaxHeight(screenBounds.getHeight());
		            		stage.setMaxWidth(screenBounds.getWidth());
		            		stage.setHeight(screenBounds.getHeight());
		            		stage.setWidth(screenBounds.getWidth());
		            		stage.setMaximized(true);
				            getStage().centerOnScreen();
				    	}
				    }
				}
			);
	}
	
	/**
	 * This method is used to create Radio Button.
	 * @param title
	 * @param group
	 * @param isSelected
	 * @return
	 */
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = UiFactory.createRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler); 
		return radioButton;
	}
	
	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setOnAction(handler);
		btn.getStyleClass().add("station-btn");
		btn.setPrefWidth(320);
		btn.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.45 * btn.getWidth())));
		return btn;
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
		return chkBox;
	}
	
	/**
	 *  This is used to create the serial number dynamically on the TableView's '#' column
	 * @param rowIndex
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	/**
	 * This method is used to create ComboBox with PromptText
	 * @param id
	 * @param promptText
	 * @return
	 */
	public LoggedComboBox<String> createComboBox(String id, String promptText) {
		LoggedComboBox<String> comboBox = new LoggedComboBox<String>(id);
		comboBox.setPromptText(promptText);
		return comboBox;
	}
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		return comboBox;
	}
	/**
	 * This method is used to create Label
	 */
	public LoggedLabel createLabel(String id, String text) {
		LoggedLabel label = UiFactory.createLabel(id,text);
		label.getStyleClass().add("display-label-station");
		return label;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	/**
	 * This method is used to create Text Field.
	 * @return
	 */
	public LoggedTextField createTextField(String id, double width, double height, EventHandler<ActionEvent> handler) {
		LoggedTextField textField = (LoggedTextField)UiFactory.createTextField(id,Fonts.SS_DIALOG_PLAIN(22), TextFieldState.EDIT, width,height);
		textField.setOnAction(handler);
		return textField;
	}
	
	public Rectangle2D getCurrentScreenBounds()  {
        Point p = MouseInfo.getPointerInfo().getLocation();
        // Get list of available screens
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        List<Screen> screens = Screen.getScreens();
        if (p != null && screens != null && screens.size() > 1) {
            // Screen bounds as rectangle
            // Go through each screen to see if the mouse is currently on that screen
            for (Screen screen : screens) {
                // If the Point p is in the Bounds
                if (screen.getVisualBounds().contains(p.x, p.y)) {
                	screenBounds = screen.getVisualBounds();
                	break;
                }
            }
        }
        return screenBounds;
	}
	
	public String getUserId()  {
		//First try this application's userid, if blank get it from default app context, else get it from ApplicationContext instance
		String userId = getMainWindow().getApplicationContext().getUserId();
		if(StringUtils.isBlank(userId))  userId = ClientMainFx.getInstance().getApplicationContext().getUserId();
		if(StringUtils.isBlank(userId))  userId = getModel().getApplicationContext().getUserId();
		return userId;
	}

}
