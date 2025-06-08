package com.honda.galc.client.ui.component;

import java.io.IOException;
import java.net.URL;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * 
 * 
 * <h3>FxDialog Class description</h3>
 * <p> FxDialog description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 17, 2014
 *
 *
 */
public class FxDialog extends Stage {
	
	public boolean showDialogFlag = true;

	private static final String resourcePath = "/resource/%s.fxml";
	
	public FxDialog(String title, String applicationId) {
		this(title, ClientMainFx.getInstance().getStage(applicationId));
	}
	public FxDialog(String title) {
		this(title, null,new BorderPane());
	}
	public FxDialog(String title,Stage owner) {
		this(title,owner,new BorderPane());
	}
	
	public FxDialog(String title,Stage owner,String terminalId) {
		this(title,owner,new BorderPane(),true,terminalId);
	}
	
	
	public FxDialog(String title,Stage owner, boolean isFXML) {
		this(title,owner,new BorderPane());
		if(isFXML)
			loadFXML();
	}
	
	
	public FxDialog(String title, Stage owner, Parent parent) {
		this(title,owner,parent,true);
	}
	
	
	public FxDialog(String title, Stage owner, Parent parent,boolean isModal) {
		setTitle(title);
		initStyle(StageStyle.UTILITY);
		initModality(isModal ?Modality.WINDOW_MODAL :Modality.NONE); // Fix: Modality APPLICATION_MODAL was blocking all opened windows instead of just parent window
		initOwner(owner);
		setResizable(false);
		setScene(new Scene(parent));
		getScene().getStylesheets().add(ClientMainFx.getInstance().getStylesheetPath());
		
		this.setOnCloseRequest( new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
			    close();	
			}
		});
	}
	public FxDialog(String title, Stage owner, Parent parent,boolean isModal,String terminalId) {
		setTitle(title);
		if(PropertyService.getPropertyBean((ApplicationPropertyBean.class), terminalId).isStageStyleDecorated())
			initStyle(StageStyle.DECORATED);
		else
			initStyle(StageStyle.UTILITY);
		initModality(isModal ?Modality.WINDOW_MODAL :Modality.NONE); // Fix: Modality APPLICATION_MODAL was blocking all opened windows instead of just parent window
		initOwner(owner);
		setResizable(false);
		setScene(new Scene(parent));
		getScene().getStylesheets().add(ClientMainFx.getInstance().getStylesheetPath());
		
		this.setOnCloseRequest( new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
			    close();	
			}
		});
	}		
	
	public void showDialog() {
		//Commenting this property as this does not override height and width explicit settings in java 8
		//sizeToScene();
		centerOnScreen();
		if(isShowDialogFlag())
			showAndWait();
		else
			close();
	}
	
	@Override
 	public void close() {
	   if (EventBusUtil.isRegistered(this)) {
 	     EventBusUtil.unregister(this);
	   }
 	   super.close();
	}

	
	public boolean isShowDialogFlag() {
		return showDialogFlag;
	}


	public void setShowDialogFlag(boolean showDialogFlag) {
		this.showDialogFlag = showDialogFlag;
	}
	
	public void loadFXML() {

		URL resource = getViewURL();

		assert resource != null;

		FXMLLoader loader = new FXMLLoader(resource);

		loader.setController(this);
		try {
			AnchorPane pane = (AnchorPane) loader.load();
			BorderPane borderPane = new BorderPane();
			borderPane.setCenter(pane);
			((BorderPane) this.getScene().getRoot()).setCenter(borderPane);
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
	}

	public String getViewPath() {
		String fxmlFilepath = String.format(resourcePath, this.getClass()
				.getName().replace(".", "/"));
		
		return fxmlFilepath;
	}

	private URL getViewURL() {
		return getClass().getResource(getViewPath());
	}
	
	/**
	 * This method is used to Create Button
	 * @param text
	 * @param handler
	 */
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.getStyleClass().add("popup-btn");
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		if(QiConstant.UPDATE.equals(text)){
			btn.setDisable(true);
		}
		return btn;
	}
	
	public void showDialog(double pointX, double pointY) {
		setX(pointX);
		setY(pointY);
		if(isShowDialogFlag())
			showAndWait();
		else
			close();
	}

}
