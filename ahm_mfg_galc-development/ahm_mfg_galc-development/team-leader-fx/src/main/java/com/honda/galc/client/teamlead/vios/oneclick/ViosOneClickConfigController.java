package com.honda.galc.client.teamlead.vios.oneclick;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosController;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.process.ViosProcessMaintDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.task.AsyncTaskExecutorService;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViosOneClickConfigController extends AbstractViosController<ViosOneClickConfigModel, ViosOneClickConfigView> implements EventHandler<ActionEvent> {

	Timer timer = null;
	
	TimerTask mainTask = null;
	private boolean hasStarted = false;
	
	public ViosOneClickConfigController(ViosOneClickConfigModel model, ViosOneClickConfigView view) {
		super(model, view);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() instanceof LoggedButton){
			LoggedButton loggedButton = (LoggedButton) event.getSource();	
			if (ViosConstants.MAKE_ACTIVE.equals(loggedButton.getText())) 
				makeActivateAction();
		}
	}
	
	
	private void makeActivateAction() {
		MCViosMasterPlatform platform = getView().getPlatform();
		
		List<MCViosMasterProcessDto> unmappedProcesses = checkForUnMappedProcesses(platform);
		if(unmappedProcesses.size() > 0) {
			// Unmapped processes.
			mapUnmappedProcesses(platform);
		} else {
			runMFGJobApprovalJob();
		}
	}

	private void runMFGJobApprovalJob() {
		MCViosMasterPlatform platform = getView().getPlatform();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("plantCode", platform.getPlantLocCode());
		paramMap.put("deptCode", platform.getDeptCode());
		paramMap.put("modelYear", String.valueOf(platform.getModelYearDate()));
		paramMap.put("productionRate", String.valueOf(platform.getProdSchQty()));
		paramMap.put("line", platform.getProdAsmLineNo());
		paramMap.put("vmc", platform.getVehicleModelCode());
		paramMap.put("userId", platform.getUserId());
		ServiceFactory.getService(AsyncTaskExecutorService.class).execute("OIF_MFG_CTRL_APPROVAL", paramMap, null, "");
		
		getView().getMakeActiveButton().setDisable(true);
		try {
			Thread.sleep(500);
			mainTask = new TimerTask() {
				@Override
				public void run(){
					setHasStarted(true);
					getView().reload(StringUtils.EMPTY);
				}
			};
			timer = new Timer(); 
			timer.schedule(mainTask, 0, 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private List<MCViosMasterProcessDto> checkForUnMappedProcesses(MCViosMasterPlatform platform) {
		return ServiceFactory
				.getDao(ChangeFormUnitDao.class).findAllUnmappedProcessByPlatform(platform);
	}
	
	
	private void mapUnmappedProcesses(MCViosMasterPlatform platform) {
		Stage dialog = new Stage();
		GridPane grid = new GridPane();
		dialog.setTitle("Unmapped Processes");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		Label userMessage = UiFactory.createLabel("userMessage");
		userMessage.setTextFill(Color.RED);
		Hyperlink hyperLink = new Hyperlink("here");
		hyperLink.setStyle("-fx-font-size: 10pt;-fx-font-weight: bold ;");
		TextFlow linkFlow = new TextFlow(new Text("Please click "), hyperLink , new Text(" to map all unmapped processes"));
		linkFlow.setStyle("-fx-font-size: 10pt; -fx-font-family: arial;");
		linkFlow.setVisible(true);
		hyperLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	ViosProcessMaintDialog viosProcessMaintDialog = new ViosProcessMaintDialog(dialog, 0, platform);
            	viosProcessMaintDialog.showDialog();
            	dialog.close();
            	getView().getMakeActiveButton().setDisable(false);
            }
        });
		grid.add(linkFlow, 0, 4);
		userMessage.setText("There are Unmapped Processes for this Change Form");
		grid.add(userMessage, 0, 3);
		Button cancelButton = UiFactory.createButton("Cancel");
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {dialog.close();getView().getMakeActiveButton().setDisable(false);}
		});
		
		HBox hButtonBox = new HBox(20);
		hButtonBox.setPadding(new Insets(0, 0, 0, 10));
		hButtonBox.getChildren().addAll(cancelButton);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(grid, hButtonBox);
		
		Scene scene = new Scene(vbox);
		dialog.initStyle(StageStyle.DECORATED);
		dialog.initModality(Modality.APPLICATION_MODAL);
		
		dialog.setHeight(200);
		dialog.setWidth(500);

		dialog.setScene(scene);
		dialog.centerOnScreen();

		dialog.toFront();
		dialog.showAndWait();
}
	@Override
	public void initEventHandlers() {
		//TODO
	}

	public boolean hasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	public Timer getTimer() {
		return timer;
	}

	public TimerTask getMainTask() {
		return mainTask;
	}

	public void startTimer() {
		ComponentStatusId cpIdKd = new ComponentStatusId("OIF_MFG_CTRL_APPROVAL", "RUNNING_STATUS{"+getView().getPlatform().getGeneratedId()+"}");
		ComponentStatus cpKdLot = getDao(ComponentStatusDao.class).findByKey(cpIdKd);
		
		if(cpKdLot!=null && cpKdLot.getStatusValue().equalsIgnoreCase("RUNNING")) {
			mainTask = new TimerTask() {
				@Override
				public void run(){
					hasStarted = true;
					getView().reload(StringUtils.EMPTY);
				}
			};
			timer = new Timer(); 
			timer.schedule(mainTask, 0, 5000);
		}
		
	}

}
