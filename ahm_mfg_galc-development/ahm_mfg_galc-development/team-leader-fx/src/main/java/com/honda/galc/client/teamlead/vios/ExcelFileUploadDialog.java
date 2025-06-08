package com.honda.galc.client.teamlead.vios;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.ExcelFileReader;
import com.honda.galc.client.utils.ExcelFilesDownloader;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.InvalidExcelFileException;
import com.honda.galc.common.exception.InvalidSheetException;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.ServiceFactory;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * <h3>ExcelFileUploadDialog Class description</h3>
 * <p>
 * ExcelFileUploadDialog is a dialog class to read and upload Excel Sheet. This
 * dialog can be used to upload any entity specific excel sheet by passing the
 * class in constructor argument.
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
 *         March 28, 2019
 */
public class ExcelFileUploadDialog extends AbstractViosDialog {

	private LoggedLabel fileNameLabel;
	private LoggedButton selectButton;
	private LoggedButton uploadButton;
	private LoggedButton closeButton;
	private LoggedButton downloadButton;
	private File file;
	private ProgressBar progressBar;
	private TextArea logMsgTextArea;
	private StringBuffer statusMsg;

	@SuppressWarnings("rawtypes")
	private Class clazz;
	private IExcelUploader excelUploader;
	//private String viosPlatform;
	//boolean isRevisionUpdate = false;

	public ExcelFileUploadDialog(Stage stage, @SuppressWarnings("rawtypes") Class clazz, IExcelUploader excelUploader) {
		super("Upload Excel", stage);
		this.clazz = clazz;
		this.excelUploader = excelUploader;
	}
	

	@Override
	public Node getMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setPadding(new Insets(10));
		mainBox.setSpacing(10);

		LoggedLabel msgLabel = UiFactory.createLabel("msgLabel", "Please select Excel File to Upload");
		msgLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		HBox selectionBox = new HBox();
		selectionBox.setAlignment(Pos.CENTER);
		selectionBox.setPadding(new Insets(12));
		selectionBox.setSpacing(8);

		fileNameLabel = UiFactory.createLabel("fileNameLabel");
		fileNameLabel.setStyle("-fx-font-size: 12px;");

		selectButton = new LoggedButton(ViosConstants.SELECT, "selectButton");
		selectButton.setStyle("-fx-font-size: 14px;");
		selectionBox.getChildren().addAll(fileNameLabel, selectButton);



		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(10));
		buttonBox.setSpacing(10);

		uploadButton = createBtn(ViosConstants.UPLOAD);
		uploadButton.setStyle("-fx-font-size: 14px;");
		uploadButton.setDisable(true);
		closeButton = createBtn(ViosConstants.CLOSE);
		closeButton.setStyle("-fx-font-size: 14px;");
		downloadButton = createBtn(ViosConstants.DOWNLOAD_TEMPLATE);
		downloadButton.setStyle("-fx-font-size: 14px;");
		buttonBox.getChildren().addAll(uploadButton, closeButton, downloadButton);

		progressBar = new ProgressBar();
		progressBar.setPrefWidth(150);
		progressBar.setVisible(false);

		logMsgTextArea = new TextArea();
		logMsgTextArea.setVisible(false);
		logMsgTextArea.setEditable(false);

		mainBox.getChildren().addAll(msgLabel, selectionBox,  buttonBox, progressBar, logMsgTextArea);
		return mainBox;
	}

	@SuppressWarnings("rawtypes")
	public Class getClazz() {
		return this.clazz;
	}

	public void setClazz(@SuppressWarnings("rawtypes") Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public void initHandler() {
		selectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xls");
				fileChooser.getExtensionFilters().add(extFilter);
				file = fileChooser.showOpenDialog(getStage());
				fileNameLabel.setText(file.getAbsoluteFile().toString());
				uploadButton.setDisable(false);

				downloadButton.setDisable(true);
			}
		});

		uploadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				progressBar.setVisible(true);
				Task<Void> mainTask = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						statusMsg = new StringBuffer();
						loadExcelData();
						updateProgress(100, 100);
						return null;
					}
				};

				mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					public void handle(WorkerStateEvent t) {
						progressBar.setVisible(false);
						logMsgTextArea.setText(statusMsg.toString());
						uploadButton.setDisable(true);
					}
				});
				progressBar.progressProperty().bind(mainTask.progressProperty());
				new Thread(mainTask).start();
			}
		});

		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) closeButton.getScene().getWindow();
				stage.close();
			}
		});

		downloadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File", "*.xls",
						"*.xlsx");
				fileChooser.getExtensionFilters().add(extFilter);
				fileChooser.setInitialFileName("Template_"+clazz.getSimpleName());
				File file = fileChooser.showSaveDialog((Stage) downloadButton.getScene().getWindow());
				if (file != null) {
					ExcelFilesDownloader downloader = new ExcelFilesDownloader(file);
					try {
						downloader.downloadExcelFile(file, clazz);
					} catch (Exception e) {
						Logger.getLogger().error(e, new LogRecord("Excel Sheet download failed."));
					}
				}
			}
		});
	}

	@Override
	public void loadData() {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadExcelData() {
		try {
			logMsgTextArea.setVisible(true);
			clearAndAppendText("Uploading Data...\n\n");
			if (file != null) {
				ExcelFileReader reader = new ExcelFileReader(file);
				List entityList = reader.getExcelContent(clazz);
				String status = excelUploader.doUpload(entityList);
				clearAndAppendText(status);
				setInfoMessage("Excel Sheet uploaded successfully!!");
			}
		} catch (Exception e) {
			String logMsg = null;
			if (e instanceof InvalidExcelFileException) {
				logMsg = e.getMessage();
			} else if (e instanceof InvalidSheetException) {
				logMsg = "The Excel Sheet data seems to be invalid.";
			} else {
				logMsg = "Something went wrong while uploading Excel Sheet.";
			}
			Logger.getLogger().error(e, new LogRecord(logMsg));
			setErrorMessage(logMsg);
		}
	}

	public  void clearAndAppendText(String text) {
		if (statusMsg != null && logMsgTextArea != null) {
			statusMsg.append(text);
			logMsgTextArea.clear();
			logMsgTextArea.setText(statusMsg.toString());
		}
	}

}
