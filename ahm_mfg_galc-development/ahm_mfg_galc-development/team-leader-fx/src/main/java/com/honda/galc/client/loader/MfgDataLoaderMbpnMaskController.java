package com.honda.galc.client.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

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
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMatrixId;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrixId;

public class MfgDataLoaderMbpnMaskController extends AbstractController<MfgDataLoaderMbpnMaskModel, MfgDataLoaderMbpnMaskPanel> implements EventHandler<ActionEvent> {
	private File template = null;
	private final Logger logger;
	private StringBuffer statusMessage;
	
	public MfgDataLoaderMbpnMaskController(MfgDataLoaderMbpnMaskModel model, MfgDataLoaderMbpnMaskPanel view) {
		super(model, view);
		this.logger = view.getLogger();
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {
		
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			
			if("Select Template ".equals(loggedButton.getText())) selectFile(actionEvent);
			else if(loggedButton.getText().equals("Load Data ")) loadFile(actionEvent);
			else if(loggedButton.getText().equals("Reset")) reset(actionEvent);
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
		if(template == null) {
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
	
	private void reset(ActionEvent actionEvent){
		getView().getResetButton().setVisible(false);
		getView().getFileChooserButton().setVisible(true);
		getView().getFilePath().setText("");
		getView().getTemplateSelectedText().setText("");
		template = null;
	}
	
	public void loadData() {
		try {
			InputStream filePath = new FileInputStream(template); 
			HSSFWorkbook workbook = new HSSFWorkbook(filePath);
			HSSFSheet worksheet = null;
			int noOfSheets = workbook.getNumberOfSheets();
			for (int sheetIdx = 0; sheetIdx<noOfSheets; sheetIdx++) {
				worksheet = workbook.getSheetAt(sheetIdx);
				//Getting Platform details from sheet 
				
				int rowNum = worksheet.getLastRowNum() + 1;
		        int colNum = worksheet.getRow(0).getLastCellNum();
		        String [][] data = new String [rowNum] [colNum];
				Map<Integer, String> modelCodeMap = new HashMap<Integer, String>();
				
	        	HSSFRow columnsRow = worksheet.getRow(0);
                for (int i = 3; i < colNum; i++){
                	 HSSFCell cell = columnsRow.getCell(i);
                	if (cell != null) {
						modelCodeMap.put(i, cell.toString().trim());
					}
					if (cell == null) break;
				}
				

				for(int i = 1; i <rowNum; i++){
		        	HSSFRow row = worksheet.getRow(i);
	                for (int j = 0; j < colNum; j++){
	                    HSSFCell cell = row.getCell(j);
	                    String value = null;
	                    if(cell != null) {
	                    	value = cell.toString().trim();
	                    }
	                    if(value == null || value.equals("")) {
	                    	//Data is blank
	                    	break;
	                    }
	                    data[i][j] = value;
	                }
	                // Data creation starts
	                try {
	                	String dept_code = StringUtils.trimToEmpty(data[i][0]);
	                	String asm_proc_number = StringUtils.trimToEmpty(data[i][1]);
	                	String mbpn_mask = StringUtils.trimToEmpty(data[i][2]);
	                	for(int n = 3; n < colNum; n++) {
	                		String model_code = StringUtils.trimToEmpty(modelCodeMap.get(n));
	                		String model_types = StringUtils.trimToEmpty(data[i][n]);
	                		String input = "Creating mask: " + dept_code + " - " + asm_proc_number + " - " + mbpn_mask + " - (" + model_types + ")" + "...";
	                		statusMessage.append(input +"\n");
	                		logger.info(input);
			                if(StringUtils.isNotEmpty(dept_code) && StringUtils.isNotEmpty(asm_proc_number) 
			                		&& StringUtils.isNotEmpty(mbpn_mask) && StringUtils.isNotEmpty(model_types) && StringUtils.isNotEmpty(model_code)) {

			                	//Adding measurement into database
			                	loadMask(dept_code, asm_proc_number, mbpn_mask, model_types, model_code);
			                }
			                else {
			                	statusMessage.append("Error! Row number " + (i+1) + ": Skipped proc no - "+asm_proc_number+"_"+mbpn_mask+": BLANK");
			                }
	                	}
	                	
	                	
	                	
						
					} catch (Exception e) {
						logger.error("Exception occurred! "+e.getMessage());
					}
		        }
			}
			statusMessage.append("-- END --");
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMask(String dept_code, String asm_proc_number, String mbpn_mask, String model_types, String model_code) {
		//Creating model type to MBPN mapping
		Map<String, List<String>> modelTypeMbpnMap = new HashMap<String, List<String>>();
		List<String> mbpnMaskList = new ArrayList<String>();
		//Creating Model type - MBPN mask
		mbpnMaskList.add(mbpn_mask);
		String[] modelTypeArray = model_types.split(",");
		for(String modelType : modelTypeArray) {
			modelType =StringUtils.trimToEmpty(modelType);
			if(StringUtils.isNotEmpty(modelType)) {
				modelTypeMbpnMap.put(modelType, mbpnMaskList);
			}
		}

		Set<MCOperationMatrix> opMatrixSet = new LinkedHashSet<MCOperationMatrix>();
		List<McOperationDataDto> operationMatrixs = getModel().findAllOperationMatrixByDeptCodeAndAsmProc(dept_code, asm_proc_number, model_code);
		for (McOperationDataDto opMatrix : operationMatrixs) {
			String opName = StringUtils.isNotBlank(opMatrix.getOperationName()) ? opMatrix.getOperationName().trim() : "";
			int opRev = opMatrix.getOperationRevision();
			int pddaPltfrmId = opMatrix.getPddaPlatformId();
			String specCodeType = opMatrix.getSpecCodeType();
			specCodeType = StringUtils.isNotBlank(specCodeType)?specCodeType.trim():"";
			String specCodeMask = opMatrix.getSpecCodeMask();
			specCodeMask = StringUtils.isNotBlank(specCodeMask)?specCodeMask.trim():"";
			
			String modelType = StringUtils.substring(specCodeMask, 4, 7);
			List<String> mbpnMasks = modelTypeMbpnMap.get(modelType);
			if(mbpnMasks!=null && !mbpnMasks.isEmpty()) {
				for(String mbpnMask : mbpnMasks) {
					mbpnMask = StringUtils.trimToEmpty(mbpnMask);
						opMatrixSet.add(convertToOperationMatrixEntity(opName, opRev, pddaPltfrmId, specCodeType, mbpnMask));
					
				}
			}
		}
		
		Set<MCOperationPartMatrix> partMatrixSet = new LinkedHashSet<MCOperationPartMatrix>();
		List<McOperationDataDto> partMatrixs = getModel().findAllPartMatrixByDeptCodeAndAsmProc(dept_code, asm_proc_number, model_code);
		
		for (McOperationDataDto partMatrix : partMatrixs) {
			
			String opName = partMatrix.getOperationName();
			opName = StringUtils.isNotBlank(opName)?opName.trim():"";
			String partId = partMatrix.getPartId();
			partId = StringUtils.isNotBlank(partId)?partId.trim():"";
			int partRev = partMatrix.getPartRevision();
			String specCodeType = partMatrix.getSpecCodeType();
			specCodeType = StringUtils.isNotBlank(specCodeType)?specCodeType.trim():"";
			String specCodeMask = partMatrix.getSpecCodeMask();
			specCodeMask = StringUtils.isNotBlank(specCodeMask)?specCodeMask.trim():"";
			
			String modelType = StringUtils.substring(specCodeMask, 4, 7);
			List<String> mbpnMasks = modelTypeMbpnMap.get(modelType);
			if(mbpnMasks!=null && !mbpnMasks.isEmpty()) {
				for(String mbpnMask : mbpnMasks) {
					mbpnMask = StringUtils.trimToEmpty(mbpnMask);
						partMatrixSet.add(convertToPartMatrixEntity(opName, partId, partRev, specCodeType, mbpnMask));
				}
			}
		
		}
		getModel().saveMatrix(opMatrixSet, partMatrixSet);
		
	}
	
	private MCOperationMatrix convertToOperationMatrixEntity(String operationName, int opRev, int pddaPltformId, String specCodeType, String mbpnMask) {
		MCOperationMatrix matrix = new MCOperationMatrix();
		MCOperationMatrixId id = new MCOperationMatrixId();
		id.setOperationName(operationName);
		id.setOperationRevision(opRev);
		id.setPddaPlatformId(pddaPltformId);
		id.setSpecCodeMask(mbpnMask);
		id.setSpecCodeType(specCodeType);
		matrix.setId(id);
		
		return matrix;
	}
	
	private MCOperationPartMatrix convertToPartMatrixEntity(String operationName, String partId, int partRev, String specCodeType, String mbpnMask) {
		MCOperationPartMatrix matrix = new MCOperationPartMatrix();
		MCOperationPartMatrixId id = new MCOperationPartMatrixId();
		id.setOperationName(operationName);
		id.setPartId(partId);
		id.setPartRevision(partRev);
		id.setSpecCodeMask(mbpnMask);
		id.setSpecCodeType(specCodeType);
		matrix.setId(id);
		
		return matrix;
	}
	
	@Override
	public void initEventHandlers() {
	
	}
	
	public Logger getLogger() { return this.logger; }
}
