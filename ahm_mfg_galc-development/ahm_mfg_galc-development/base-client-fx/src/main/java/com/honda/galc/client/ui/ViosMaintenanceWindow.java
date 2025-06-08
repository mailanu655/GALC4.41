package com.honda.galc.client.ui;

import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
/**
 * <h3>ViosPlatformMaintDialog Class description</h3>
 * <p>
 * Dialog for Vios Platform Maintenance
 * </p>
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public class ViosMaintenanceWindow extends TabbedMainWindow {
	
	public ViosMaintenanceWindow(ApplicationContext appContext,Application application) {
		super(appContext,application);
	}

	@Override
	protected List<String> loadPanelConfigs() {
		return ViosPanels.getPanels();
	}

	@Override
	protected TabPane createTabPane() {
		TabPane tabPane = new TabPane();
		tabPane.setSide(Side.LEFT);
		tabPane.setStyle("-fx-tab-min-width: 50px; -fx-tab-max-width: 50px; -fx-tab-min-height: 100px; -fx-tab-max-height: 100px; -fx-border-color: silver");
		return tabPane;
	}

	@Override
	protected Tab createTab(ApplicationMainPane panel, ITabbedPanel iPanel) {
		Tab tab = new Tab();
		tab.setClosable(false);
		tab.setContent(panel);
		LoggedLabel label = UiFactory.createLabel("tabText", iPanel.getScreenName());
		label.setWrapText(true);
		label.setMinSize(100, 50);
		label.setMaxSize(100, 50);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setRotate(90);
		label.setStyle("-fx-font-weight: bold;");
		tab.setGraphic(label);
		return tab;
	}
	
	@Override
	protected Node getTopPanel() {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_LEFT);
		
		Hyperlink hyperLink = new Hyperlink("<< Select Platform >>");
		hyperLink.setStyle("-fx-font-size: 8pt; -fx-font-weight: bold;");
		hyperLink.setBorder(Border.EMPTY);
		hyperLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	getTabPane().getSelectionModel().selectFirst();
            }
        });
		//Adding the menubar from main window
		Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
       
		hBox.getChildren().addAll(this.getTop(), region, hyperLink);
		return hBox;
	}
}
