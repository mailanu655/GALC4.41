package com.honda.galc.client.ui.menu;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.IAccessControlManager;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.pane.AbstractProcessInfoDialog;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>MainMenuDialog Class description</h3>
 * <p> MainMenuDialog description: VIOS Main Menu </p>
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
 * @author Alok Ghode<br>
 * May 08, 2014
 *
 *
 */
public class MainMenuDialog extends AbstractProcessInfoDialog {
	
	private static final String VIOS_MAIN_MENU = "VIOS_MAIN_MENU";
	private Map<String, String> menuDetailsMap = new LinkedHashMap<String, String>();
	private IAccessControlManager accessControlManager;
	private ApplicationContext defaultAppContext;
	private static final String MY_PROCESS = "My Process";
	boolean showMainMenu = false;
	String PDDALink="";
	String MDRSLink="";

	public MainMenuDialog(ProductController controller) {
		super("Main Menu", controller);
		initComponents();
	}
	
	private void initComponents() {
		accessControlManager = ClientMainFx.getInstance().getAccessControlManager();
		defaultAppContext = ClientMainFx.getInstance().getApplicationContext();
		setUserMenu();
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		setHeight(screenBounds.getHeight());
		setWidth(screenBounds.getWidth());
		setMaxHeight(screenBounds.getHeight());
		setMaxWidth(screenBounds.getWidth());
		if(menuDetailsMap!=null && menuDetailsMap.size() > 0) {
			showMainMenu = true;
		}
		else {
			showMainMenu = false;
		}
		if(showMainMenu) {
			getRootBorderPane().setCenter(getMainMenu(menuDetailsMap));
			Logger.getLogger().check("Main Menu page loaded successfully");
		}else{
			Logger.getLogger().check("Main Menu page will not be loaded");
		}
		
	}
	
	public boolean showMainMenu() {
		return this.showMainMenu;
	}
	
	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}
	
	private void setUserMenu() {
		//Fetching VIOS Main Menus from Database Properties i.e. GAL489TBX
		List<ComponentProperty> menuList = PropertyService.getComponentProperty(VIOS_MAIN_MENU);
		if(menuList!=null) {
			String applicationId;
			String menuName;
			for(ComponentProperty property : menuList) {
				if (property.getPropertyKey()!=null &&property.getPropertyKey().equals("PDDA_LINK")) 
					PDDALink=property.getPropertyValue();
				if (property.getPropertyKey()!=null &&property.getPropertyKey().equals("MDRS_LINK")) 
					MDRSLink=property.getPropertyValue();
				
				applicationId = property.getPropertyValue();
								
				if(!StringUtils.isEmpty(applicationId) && !StringUtils.startsWithIgnoreCase(applicationId, "http")) {
					//Check whether associate has access to application
					Application application = defaultAppContext.getApplication(applicationId.trim());
					if(application != null) {
						if(accessControlManager.isAccessPermitted(application.getScreenId())) {
							menuName = (property.getPropertyKey()!=null)?property.getPropertyKey().trim():"";
							menuDetailsMap.put(menuName, applicationId.trim());
						}
					}
				}
			}
		}
	}
	
	
	private Node getMainMenu(Map<String, String> menuDetails) {
		//Creation of Grid Pane for all menus
		GridPane gridPane = new GridPane();
		gridPane.setManaged(true);
		gridPane.setHgap(25);
		gridPane.setVgap(25);
		gridPane.setAlignment(Pos.CENTER);

		int noOfButtons = menuDetails.size();
		int noOfColumns = 2;
		int noOfRows = (noOfButtons % noOfColumns == 0) ? noOfButtons/noOfColumns : (noOfButtons/noOfColumns) + 1;
		noOfRows = (noOfRows < 2) ? 2 : noOfRows;
		int percentWidth = 75/(noOfColumns + 1);
		int percentHeight = 75/noOfRows;

		Iterator<String> menuItr;
		menuItr = menuDetails.keySet().iterator();
		String menuName = null;
		for(int iRow=0; iRow<noOfRows; iRow++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(percentHeight);
			rowConstraints.setVgrow(Priority.ALWAYS);
			gridPane.getRowConstraints().add(rowConstraints);

			for(int iCol=(noOfColumns-1); iCol>=0; iCol--) {
				if(iRow==0) {
					ColumnConstraints columnConstraints = new ColumnConstraints();
					columnConstraints.setPercentWidth(percentWidth);
					columnConstraints.setHgrow(Priority.ALWAYS);
					gridPane.getColumnConstraints().add(columnConstraints);
				}
				if(menuItr.hasNext()) {
					menuName = menuItr.next();
					gridPane.add(getButton(menuName), iCol, iRow); // Node, col, row

				}
			}
		}
		//Default My Process Button for everyone
		gridPane.add(getButton("My Process"), noOfColumns, 0, 1, noOfRows); // Node, col, row
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setPercentWidth(percentWidth);
		columnConstraints.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().add(columnConstraints);
		
		//Populating components in Main Menu Layout
		BorderPane mainMenu = new BorderPane();
        mainMenu.setCenter(gridPane);
        
        StackPane heading = new StackPane();
        Text headingText = UiFactory.createText("Main Menu");
        headingText.setStyle(" -fx-font-size: 2em; -fx-font-weight: bold; ");
  
        heading.setStyle("-fx-padding: 3em 0px 5px 0px;");
        heading.getChildren().add(headingText);
        mainMenu.setTop(heading);
	
		return mainMenu;
	}

	
	public void buttonClicked(String menuName, Scene scene) {
		Logger.getLogger().info("Menu Name Selected: "+menuName+"    Application Id: "+this.menuDetailsMap.get(menuName));
		if(menuName!=null && menuName.equalsIgnoreCase(MY_PROCESS)) {
			Logger.getLogger().check("My Process button clicked in Main Menu page");
			scene.getWindow().hide();
		}
		if(menuName!=null && menuName.equalsIgnoreCase("Training")) {
			Logger.getLogger().check("Training button clicked in Main Menu page");
			getProductController().getModel().setTrainingMode(true);
			scene.getWindow().hide();
		}

		if(menuName!=null && menuName.equalsIgnoreCase("PDDA")) {
			Logger.getLogger().check("PDDA button clicked in Main Menu page");
			getRootBorderPane().setCenter(getWebPage(PDDALink));
			scene.getWindow().centerOnScreen();
		}
		if(menuName!=null && menuName.equalsIgnoreCase("MDRS")) {
			Logger.getLogger().check("MDRS button clicked in Main Menu page");
			getRootBorderPane().setCenter(getWebPage(MDRSLink));
			scene.getWindow().centerOnScreen();
			
		}
	}
	
	
	public Button getButton(String buttonText) {
		Button btn = UiFactory.createButton(buttonText);
		btn.setWrapText(true);
        btn.setStyle(" -fx-font-size: 1.5em; -fx-font-weight: bold; "); 
		btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Scene scene = ((Button)event.getSource()).getScene();
				buttonClicked(((Button)event.getSource()).getText(), scene);
			}
		});

		return btn;
	}

	private VBox getWebPage(String url) {
		WebView webView = new WebView();
		webView.setId("webView");
        final WebEngine webEngine = webView.getEngine();
        webEngine.setUserAgent("Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; GTB7.4; InfoPath.2; SV1; .NET CLR 3.3.69573; WOW64; en-US)");
        webEngine.load(url);
        final TextField locationField = UiFactory.createTextField("locationField", url);
        locationField.setStyle("-fx-font-size: 20px");
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                locationField.setText(newValue);
            }
        });
        EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                webEngine.load(locationField.getText().startsWith("http://") 
                        ? locationField.getText() 
                        : "http://" + locationField.getText());
            }
        };

        locationField.setOnAction(goAction);
        Button goButton =  UiFactory.createButton ("Go");
        goButton.setDefaultButton(true);
        goButton.setOnAction(goAction);

        //toolbar
        HBox toolbar = new HBox();
        Button menuButton = UiFactory.createButton("Go to Menu");
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	getRootBorderPane().setCenter(getMainMenu(menuDetailsMap));
            	Logger.getLogger().check("Go to Menu button clicked");
            }
        });
        
    	Image backimage = new Image("resource/com/honda/galc/client/images/Back.png");
    	final ImageView back = new ImageView();
    	back.setFitWidth(50);
    	back.setPreserveRatio(true);
    	back.setSmooth(false);
    	back.setCache(false);
    	back.setImage(backimage);
    	back.setOnMouseClicked(new EventHandler<MouseEvent>() {
    	    @Override 
    	    public void handle(MouseEvent e) {
            	locationField.setText(goBack(webEngine));
    	    }
    	});
    	Image forwardimage = new Image("resource/com/honda/galc/client/images/Forward.png");
    	final ImageView forward = new ImageView();
    	forward.setFitWidth(50);
    	forward.setPreserveRatio(true);
    	forward.setSmooth(false);
    	forward.setCache(false);
    	forward.setImage(forwardimage);
    	forward.setOnMouseClicked(new EventHandler<MouseEvent>() {
    	    @Override 
    	    public void handle(MouseEvent e) {
            	locationField.setText(goForward(webEngine));
    	    }
    	});
    	
    	HBox imageBox = new HBox(5);
    	imageBox.setSpacing(50);
    	imageBox.getChildren().addAll(back,forward);
    	toolbar.setSpacing(500);
    	toolbar.getChildren().addAll(menuButton,imageBox);
    	toolbar.setAlignment(Pos.CENTER);

    	// Layout logic
        HBox hBox = new HBox(5);
        hBox.getChildren().setAll(locationField, goButton);
        HBox.setHgrow(locationField, Priority.ALWAYS);
        VBox vBox = new VBox(5);
        vBox.getChildren().setAll(hBox,toolbar, webView);
        VBox.setVgrow(webView, Priority.ALWAYS);

        return vBox;
	}
	
	
	private String goBack(WebEngine eng)
	  {    
	    final WebHistory history=eng.getHistory();
	    ObservableList<WebHistory.Entry> entryList=history.getEntries();
	    final int currentIndex=history.getCurrentIndex();
	    Platform.runLater(new Runnable() { public void run() {
	    	if (currentIndex>0) history.go(-1); 
	    } });
	    return entryList.get(currentIndex>0?currentIndex-1:currentIndex).getUrl();
	  }

	private String goForward(WebEngine eng)
	  {    
	    final WebHistory history=eng.getHistory();
	    final ObservableList<WebHistory.Entry> entryList=history.getEntries();
	    final int currentIndex=history.getCurrentIndex();
	    Platform.runLater(new Runnable() { public void run() { 
	    	if (currentIndex<entryList.size()-1)
	    	history.go(1); 
	    } });
	    return entryList.get(currentIndex<entryList.size()-1?currentIndex+1:currentIndex).getUrl();
	  }
	
}
