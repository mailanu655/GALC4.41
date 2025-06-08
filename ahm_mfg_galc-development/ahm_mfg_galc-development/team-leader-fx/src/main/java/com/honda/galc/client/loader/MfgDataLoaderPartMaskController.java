package com.honda.galc.client.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.honda.galc.dao.conf.MCViosMasterOperationPartDao;
import com.honda.galc.dto.PartDto;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartId;
import com.honda.galc.service.ServiceFactory;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

public class MfgDataLoaderPartMaskController
		extends AbstractController<MfgDataLoaderPartMaskModel, MfgDataLoaderPartMaskPanel>
		implements EventHandler<ActionEvent> {
	private File template = null;
	private Map<String, String> errorMap = new LinkedHashMap<String, String>();
	private final Logger logger;
	private StringBuffer statusMessage;

	public MfgDataLoaderPartMaskController(MfgDataLoaderPartMaskModel model, MfgDataLoaderPartMaskPanel view) {
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

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls");
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
			Map<String, List<PartDto>> unitPartsMap = new LinkedHashMap<String, List<PartDto>>();

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
						String partNo = StringUtils.trimToEmpty(data[i][1]);
						String partMask = StringUtils.trimToEmpty(data[i][2]);

						String input = "Reading part: '" + unit_no + "_" + sheetName + ", " + partMask + ", " + partNo
								+ "...";
						statusMessage.append(input + "\n");
						logger.info(input);
						if (StringUtils.isNotEmpty(sheetName) && StringUtils.isNotEmpty(unit_no)
								&& StringUtils.isNotEmpty(partMask) && StringUtils.isNotEmpty(partNo)) {
							unit_no = unit_no + "_" + sheetName;

							PartDto part = new PartDto(partNo, partMask);
							List<PartDto> unitPartList = null;
							if (unitPartsMap.containsKey(unit_no)) {
								unitPartList = unitPartsMap.get(unit_no);
								boolean isExist = false;
								for (PartDto p : unitPartList) {
									if (p.getPartNo().equals(partNo) && p.getPartMask().equals(partMask)) {
										isExist = true;
									}
								}
								if (!isExist) {
									unitPartList.add(part);
								}
							} else {
								unitPartList = new ArrayList<PartDto>();
								unitPartList.add(part);
							}
							unitPartsMap.put(unit_no, unitPartList);
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

			// Creating part Mask
			for (String operationName : unitPartsMap.keySet()) {
				statusMessage.append("Creating Part mask :: Operation: " + operationName + ", Parts: "
						+ unitPartsMap.get(operationName) + "\n");
				List<PartDto> parts = unitPartsMap.get(operationName);
				if (parts.size() > 0) {
					createMfgPart(operationName, parts);
				} else {
					errorMap.put(operationName, "No parts available");
				}
			}
			statusMessage.append("-- Part Masks --" + "\n");

			for (String operationName : unitPartsMap.keySet()) {
				statusMessage
						.append("Operation: " + operationName + ", Parts: " + unitPartsMap.get(operationName) + "\n");
				logger.info("Operation: " + operationName + ", Parts: " + unitPartsMap.get(operationName));

			}
			if (!errorMap.isEmpty()) {
				logger.info("--  E R R O R S  --");
				statusMessage.append("--  E R R O R S  --" + "\n");
				for (String operationName : errorMap.keySet()) {
					statusMessage
							.append("Operation: " + operationName + ", Error: " + errorMap.get(operationName) + "\n");
					logger.info("Operation: " + operationName + ", Error: " + errorMap.get(operationName));
				}
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void createMfgPart(String operationName, List<PartDto> parts) {
		Map<Integer, MCOperationPartRevision> mfgParts = new HashMap<Integer, MCOperationPartRevision>();
		Map<Integer, MCOperationPartRevision> refParts = new HashMap<Integer, MCOperationPartRevision>();
		List<Integer> partIdNumList = new ArrayList<Integer>();
		List<MCOperationPartRevision> partRevisions = getModel().findAllByOperationName(operationName);
		for (MCOperationPartRevision partRevision : partRevisions) {
			Integer partId = Integer
					.valueOf(partRevision.getId().getPartId().substring(1, partRevision.getId().getPartId().length()));
			partIdNumList.add(partId);
			if (partRevision.getPartType().toString().equals("MFG")) {
				mfgParts.put(partId, partRevision);
			} else {
				refParts.put(partId, partRevision);
			}
		}
		if (mfgParts.isEmpty()) {
			errorMap.put(operationName, "******* NO MFG PART : UNIT IS NOT OF SCAN/MEAS TYPE *******");
		} else {
			for (PartDto p : parts) {
				boolean isRefPartExists = false;
				for (MCOperationPartRevision refPart : refParts.values()) {
					if (refPart.getPartNo().equals(p.getPartNo())) {
						statusMessage.append("------------------------" + p + "------------------------" + "\n");
						logger.info("------------------------" + p + "------------------------");
						isRefPartExists = true;

						boolean ifMfgPartExists = false;
						for (MCOperationPartRevision mfgPart : mfgParts.values()) {
							if (StringUtils.equals(refPart.getPartNo(), mfgPart.getPartNo())
									&& StringUtils.equals(refPart.getPartSectionCode(), mfgPart.getPartSectionCode())
									&& StringUtils.equals(refPart.getPartItemNo(), mfgPart.getPartItemNo())) {
								// MFG Part Exists
								ifMfgPartExists = true;
								// Update part mask
								getModel().updatePartMask(p, mfgPart);
								//SAVE OR UPDATE PART_MASK VIOS MASTER PART TABLE - ref_part's part no, part item number, part section code
								try {
								updateMasterViosPart(operationName, p, mfgPart);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						if (!ifMfgPartExists) {
							// MFG Part Does Not Exist
							// Default MFG
							MCOperationPartRevision defaultMFgPart = mfgParts.get(Collections.min(partIdNumList));
							statusMessage.append("DEFAULT MFG: " + defaultMFgPart + "\n");
							logger.info("DEFAULT MFG: " + defaultMFgPart);
							// Create MFG Part
							getModel().createMfgPart(operationName, p, defaultMFgPart, refPart);
							//SAVE OR UPDATE PART_MASK VIOS MASTER PART TABLE - ref_part's part no, part item number, part section code
							try {
							updateMasterViosPart(operationName, p, defaultMFgPart);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}
				if (!isRefPartExists) {
					statusMessage.append(operationName + p + " Part Does not exist in VIOS");
					errorMap.put(operationName + p, "Part Does not exist in VIOS");
				}
			}
		}
	}

	@Override
	public void initEventHandlers() {

	}

	public Logger getLogger() {
		return this.logger;
	}
	
	public void updateMasterViosPart(String  operationName, PartDto p, MCOperationPartRevision mfgPart ) {
		MCViosMasterOperationPartId masterOpPartId = new MCViosMasterOperationPartId();
		String operation = operationName; 
		String [] operationArray = operation.split("_"); 
		String unitNo = StringUtils.trimToEmpty(operationArray[0]);
		String platformId = StringUtils.trimToEmpty(operationArray[1]);
		masterOpPartId.setViosPlatformId(platformId);
		masterOpPartId.setUnitNo(unitNo);
		masterOpPartId.setPartNo(p.getPartNo());
		masterOpPartId.setPartType(PartType.MFG);
		
		MCViosMasterOperationPart masterOpPart = new MCViosMasterOperationPart();
		masterOpPart.setId(masterOpPartId);
		masterOpPart.setPartMask(p.getPartMask());
		masterOpPart.setPartDesc(mfgPart.getPartDesc());
		masterOpPart.setPartView(mfgPart.getPartView());
		masterOpPart.setPartProcessor(mfgPart.getPartProcessor());
		masterOpPart.setDeviceId(mfgPart.getDeviceId());
		masterOpPart.setDeviceMsg(mfgPart.getDeviceMsg());
		masterOpPart.setPartMark(mfgPart.getPartMark());
		masterOpPart.setMeasCount(mfgPart.getMeasCount());
		masterOpPart.setPartCheck(mfgPart.getPartCheck());
		ServiceFactory.getDao(MCViosMasterOperationPartDao.class).save(masterOpPart);		
	}
	
}
