package com.honda.galc.client.teamleader.qi.view;

import java.awt.MouseInfo;
import java.awt.Point;
import java.net.URL;
import java.util.List;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.enumtype.ScreenAccessLevel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

public abstract class QiAbstractTabbedView<M extends AbstractModel,C extends AbstractController<?,?>> extends AbstractTabbedView<M, C>{
	
	private LoggedRadioButton allRadioBtn;
	private LoggedRadioButton activeRadioBtn;
	private LoggedRadioButton inactiveRadioBtn;
	private Stage stage;
	
	public QiAbstractTabbedView(ViewId viewId, TabbedMainWindow mainWindow) {
		super(viewId, mainWindow);
		stage = ClientMainFx.getInstance().getStage(getApplicationId());
		getController().activate();
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

	public LoggedRadioButton getAllRadioBtn() {
		return allRadioBtn;
	}

	public void setAllRadioBtn(LoggedRadioButton allRadioBtn) {
		this.allRadioBtn = allRadioBtn;
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
	
	public HBox createFilterRadioButtons(EventHandler<ActionEvent> handler, double width) {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		allRadioBtn = createRadioButton(QiConstant.ALL, group, true, handler);
		activeRadioBtn = createRadioButton(QiConstant.ACTIVE, group, false, handler);
		inactiveRadioBtn = createRadioButton(QiConstant.INACTIVE, group, false, handler);
		
		radioBtnContainer.getChildren().addAll(allRadioBtn, activeRadioBtn, inactiveRadioBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(0, 0, 0, 10));
		radioBtnContainer.setPrefWidth(width);
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
	
	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("main-screen-btn");
		return btn;
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

	public boolean isFullAccess() {
		return getModel().getApplicationContext().getHighestAccessLevel() == ScreenAccessLevel.FULL_ACCESS;
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
	
	public LoggedTextField createLoggedFilterTextField(String id, int width, EventHandler<ActionEvent> handler) {
		LoggedTextField filterTextField = (LoggedTextField) UiFactory.createTextField(id, width, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		filterTextField.setOnAction(handler);
		return filterTextField;
	}
	
	/**
	 * This method is used to create button for Image and Image Section
	 * @param text
	 * @param handler
	 * @param style
	 * @return
	 */
	public LoggedButton createImageBtn(String text,EventHandler<ActionEvent> handler,String style)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setId(text);
		btn.setOnAction(handler);
		btn.setStyle(style);
		return btn;
	}
	
	/**
	 * @param imageName
	 * @param text
	 * @param id
	 * @param handler
	 * @param width
	 * @param height
	 * @param style
	 * @return
	 */
	public LoggedButton createBtn(String imageName, String text, String id,EventHandler<ActionEvent> handler,double width,double height,String style)
	{
		LoggedButton btn = UiFactory.createButton("");
		btn.setId(id);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		URL url = getClass().getResource("/resource/com/honda/galc/client/images/qi/"+imageName);
		ImageView imageView = new ImageView();
		Image image = new Image(url.toString());
		imageView.setImage(image);
		imageView.setBlendMode(BlendMode.MULTIPLY);
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		btn.setGraphic(imageView);
		Tooltip tooltip = new Tooltip();
		tooltip.setText(text);
		btn.setTooltip(tooltip);
		btn.getStyleClass().add(style);
		return btn;
	}
	
	/**
	 * This method is used to create checkbox
	 * @param text
	 * @param handler
	 * @param style
	 * @return
	 */
	public CheckBox createCheckBox(String text,EventHandler<ActionEvent> handler,String style)
	{
		CheckBox checkBox = new CheckBox(text);
		checkBox.setOnAction(handler);
		checkBox.setWrapText(true);
		checkBox.setPadding(new Insets(10));
		checkBox.getStyleClass().add(style);
		return checkBox;
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
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
}
