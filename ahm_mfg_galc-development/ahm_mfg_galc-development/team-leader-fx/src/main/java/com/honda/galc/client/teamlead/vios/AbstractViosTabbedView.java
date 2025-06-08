package com.honda.galc.client.teamlead.vios;

import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * <h3>AbstractViosTabbedView Class description</h3>
 * <p>
 * Abstract class for Vios Tabbed View
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
public abstract class AbstractViosTabbedView<M extends AbstractViosModel, C extends AbstractViosController<?,?>> 
			extends AbstractTabbedView<M, C> implements IViosPanel {
	
	public ProgressIndicator loadingIndicator;
	
	public BorderPane viosBorderPane;

	public AbstractViosTabbedView(ViewId viewId, TabbedMainWindow mainWindow) {
		super(viewId, mainWindow);
		getController().activate();
	}
	
	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(ViosConstants.CSS_PATH);
		
		loadingIndicator = new ProgressIndicator();
		loadingIndicator.setMaxSize(100, 100);
		
		StackPane mainStackPane = new StackPane();
		mainStackPane.setAlignment(Pos.CENTER);
		
		viosBorderPane = new BorderPane();
		viosBorderPane.setTop(getTopBody());
		viosBorderPane.setCenter(getCenterBody());
		
		mainStackPane.getChildren().addAll(viosBorderPane, loadingIndicator);
		this.setPadding(new Insets(10));
		this.setCenter(mainStackPane);
	}
	
	public BorderPane getViosBorderPane() {
		return viosBorderPane;
	}

	@Override
	public void onTabSelected() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					loadingIndicator.setVisible(true);
					Thread.sleep(500);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							onScreenSelected();
						}
					});
				} catch (Exception e) {
					setErrorMessage("Something went wrong while loading "+getScreenName());
					Logger.getLogger().error(new LogRecord("An exception occured while loading "+getScreenName()));
				} finally {
					loadingIndicator.setVisible(false);
				}
			}
		}).start();
	}
	
	@Override
	public Stage getStage() {
		return getMainWindow().getStage();
	}
	
	@Override
	public void setErrorMessage(String msg) {
		super.setErrorMessage(msg);
	}
	
	@Override
	public void clearErrorMessage() {
		super.clearErrorMessage();
	}
	
	@Override
	public void setInfoMessage(String msg) {
		super.setMessage(msg, Color.YELLOW);
	}
	
	public abstract Node getTopBody();
	
	public abstract Node getCenterBody();
	
	public abstract void onScreenSelected();
	
}
