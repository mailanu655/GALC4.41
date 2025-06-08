package com.honda.galc.client.loader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementDao;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementId;
import com.honda.galc.service.ServiceFactory;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

public class MfgDataLoaderMeasurementController
		extends AbstractController<MfgDataLoaderMeasurementModel, MfgDataLoaderMeasurementPanel>
		implements EventHandler<ActionEvent> {
	private static final int MAX_ATTEMPTS = 3;
	private File template = null;
	private Map<String, String> errorMap = new LinkedHashMap<String, String>();
	private final Logger logger;
	private StringBuffer statusMessage;

	public MfgDataLoaderMeasurementController(MfgDataLoaderMeasurementModel model, MfgDataLoaderMeasurementPanel view) {
		super(model, view);
		this.logger = view.getLogger();
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if ("Select Template ".equals(loggedButton.getText()))
				selectFile(actionEvent);
			else if (loggedButton.getText().equals("Load Data "))
				loadFile(actionEvent);
			else if (loggedButton.getText().equals("Reset"))
				reset(actionEvent);
		}
	}

	private void selectFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		template = fileChooser.showOpenDialog(ClientMainFx.getInstance().getStage());
		if (template != null) {
			getView().getFileChooserButton().setVisible(false);
			getView().getLoadDataButton().setVisible(true);
			getView().getFilePath().setText(template.toString());
		}
	}

	private void loadFile(ActionEvent actionEvent) {

		if (template == null) {
			MessageDialog.showInfo(null, "Please Select Excel File");
			return;
		}
		getView().getLoadDataButton().setVisible(false);
		getView().getProgressBar().setVisible(true);
		Task<Void> mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				statusMessage = new StringBuffer();
				loadData();
				updateProgress(100, 100);
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent t) {
				getView().getProgressBar().setVisible(false);
				getView().getTemplateSelectedText().setVisible(true);
				getView().getResetButton().setVisible(true);
				getView().getTemplateSelectedText().setText(statusMessage.toString());

			}
		});
		getView().getProgressBar().progressProperty().bind(mainTask.progressProperty());
		new Thread(mainTask).start();
	}

	private void reset(ActionEvent actionEvent) {

		getView().getResetButton().setVisible(false);
		getView().getFileChooserButton().setVisible(true);
		getView().getFilePath().setText("");
		getView().getTemplateSelectedText().setText("");
		template = null;
	}

	public void loadData() {
		try {
			List<String> excelOperationList = new ArrayList<String>();
			InputStream filePath = new FileInputStream(template);
			HSSFWorkbook workbook = new HSSFWorkbook(filePath);
			HSSFSheet worksheet = null;
			int noOfSheets = workbook.getNumberOfSheets();
			for (int sheetIdx = 0; sheetIdx < noOfSheets; sheetIdx++) {
				worksheet = workbook.getSheetAt(sheetIdx);
				// Getting Platform details from sheet name
				String sheetName = StringUtils.trim(worksheet.getSheetName());

				int rowNum = worksheet.getLastRowNum() + 1;
				int colNum = worksheet.getRow(0).getLastCellNum();
				String[][] data = new String[rowNum][colNum];

				for (int i = 1; i < rowNum; i++) {
					HSSFRow row = worksheet.getRow(i);
					for (int j = 0; j < colNum; j++) {
						HSSFCell cell = row.getCell(j);
						String value = null;
						if (cell != null) {
							value = cell.toString().trim();
						}
						if (value == null || value.equals("")) {
							// Data is blank
							break;
						}
						data[i][j] = value;
					}
					// Data creation starts
					String unit_no = "";
					try {
						unit_no = StringUtils.trimToEmpty(data[i][0]);
						String qty = StringUtils.trimToEmpty(data[i][1]);
						String min_limit = StringUtils.trimToEmpty(data[i][2]);
						String max_limit = StringUtils.trimToEmpty(data[i][3]);
						String pset = StringUtils.trimToEmpty(data[i][4]);
						String tool = StringUtils.trimToEmpty(data[i][5]);

						String input = "Creating measurement: '" + unit_no + "_" + sheetName + ", " + qty + ", "
								+ min_limit + ", " + max_limit + ", " + pset + ", " + tool + "...";
						logger.info(input);
						statusMessage.append(input + "\n");
						if (StringUtils.isNotEmpty(sheetName) && StringUtils.isNotEmpty(unit_no)
								&& StringUtils.isNotEmpty(qty) && StringUtils.isNotEmpty(min_limit)
								&& StringUtils.isNotEmpty(max_limit)) {
							unit_no = unit_no + "_" + sheetName;
							try {
								pset = Integer.toString((int) Double.parseDouble(pset));
							} catch (NumberFormatException nfe) {
							}

							// Adding measurement into database
							addMeasurement(unit_no, (int) Double.parseDouble(qty), min_limit, max_limit, pset, tool);
							excelOperationList.add(unit_no);
						} else {
							statusMessage.append("Error! Row number " + (i + 1) + ": Skipped unit " + unit_no + "_"
									+ sheetName + ": BLANK" + "\n");
							errorMap.put(unit_no, "Data is blank in excel");
						}

					} catch (Exception e) {
						errorMap.put(unit_no, "Exception occurred! " + e.getMessage());
					}
				}
			}
			logger.info("-- Measurements added --");
			statusMessage.append("-- Measurements added --" + "\n");
			if (!errorMap.isEmpty()) {
				statusMessage.append("--  E R R O R S  --" + "\n");
				logger.info("--  E R R O R S  --");
				for (String operationName : errorMap.keySet()) {
					statusMessage
							.append("Operation: " + operationName + ", Error: " + errorMap.get(operationName) + "\n");
					logger.info("Operation: " + operationName + ", Error: " + errorMap.get(operationName));
				}
			}
			// Checking for any missed operations to enter measurement
			checkMissedOperations(excelOperationList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkMissedOperations(List<String> excelOperationList) throws Exception {
		try {
			for (McOperationDataDto opRevision : getModel().findAllApprovedRevision()) {
				String opName = opRevision.getOperationName();
				opName = StringUtils.isNotBlank(opName) ? opName.trim() : "";

				String opType = opRevision.getOperationType();
				opType = StringUtils.isNotBlank(opType) ? opType.trim() : "";
				if (!excelOperationList.contains(opName)) {
					statusMessage.append("Operation: " + opName + ", Op Type: " + opType
							+ ", Error: Missing Measurements for this operation. Please add it in Excel." + "\n");
					logger.info("Operation: " + opName + ", Op Type: " + opType
							+ ", Error: Missing Measurements for this operation. Please add it in Excel.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void addMeasurement(String opName, int qty, String minLimit, String maxLimit, String pset, String tool)
			throws Exception {
		try {
			boolean isEmpty = true;
			List<McOperationDataDto> partRevisions = getModel().findAllByOperationName(opName);
			for (McOperationDataDto partRevision : partRevisions) {
				isEmpty = false;
				String partId = StringUtils.trimToEmpty(partRevision.getPartId());
				int partRev = partRevision.getPartRevision();
				getModel().addMeasurement(opName, partId, partRev, qty, minLimit, maxLimit, MAX_ATTEMPTS, pset, tool);
				try {
					addMasterMeasurement(partRevision, opName, qty, minLimit, maxLimit, MAX_ATTEMPTS, pset, tool);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (isEmpty) {
				errorMap.put(opName, "Operation does not exist or not of measurement type");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void addMasterMeasurement(McOperationDataDto partRevision, String operationName, int qty, String minLimit,
			String maxLimit, int maxAttempt, String pset, String tool) {
		List<MCOperationPartRevision> partRevisionList = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
				.findAllActivePartsByOperationName(operationName);
		for (int i = 1; i <= qty; i++) {
			String operation = operationName;
			String[] operationArray = operation.split("_");
			String unitNo = StringUtils.trimToEmpty(operationArray[0]);
			String platformId = StringUtils.trimToEmpty(operationArray[1]);
			MCViosMasterOperationMeasurementId meansId = new MCViosMasterOperationMeasurementId();
			meansId.setViosPlatformId(platformId);
			meansId.setUnitNo(unitNo);
			meansId.setPartType(PartType.MFG);
			meansId.setMeasurementSeqNum(i);
			
			MCViosMasterOperationMeasurement meas = new MCViosMasterOperationMeasurement();
			meas.setId(meansId);
			meas.setMinLimit(Double.parseDouble(minLimit));
			meas.setMaxLimit(Double.parseDouble(maxLimit));
			meas.setMaxAttempts(maxAttempt);
			meas.setDeviceId(tool);
			meas.setDeviceMsg(pset);
			ServiceFactory.getDao(MCViosMasterOperationMeasurementDao.class).insert(meas);
		}
	}

	@Override
	public void initEventHandlers() {

	}

	public Logger getLogger() {
		return this.logger;
	}
}
