package com.honda.galc.client.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.loader.dto.MfgDataLoadDto;
import com.honda.galc.client.loader.dto.PlatformDto;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.constant.RevisionType;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.dao.pdda.ProcessDao;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.entity.pdda.ChangeFormUnit;
import com.honda.galc.entity.pdda.Process;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;
import com.honda.galc.service.property.PropertyService;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

public class MfgDataLoader extends TabbedPanel {
	
	private File template = null;
	private Button loadDataButton = UiFactory.createButton("LoadData");
	private Button fileChooserButton = UiFactory.createButton("Select Template");
	private List<ChangeForm> newchangeFormsList = null;
	private int successCount = 0;
	private int errorCount = 0;
	private Map<String, String> modelYrCodeMap;
	private String eol = System.getProperty("line.separator");
	Map<String, List<ChangeFormUnit>> unitMapMass;
	
	private TextArea statusMessageTextArea;
	private ProgressBar progressBar;
	private String statusMessage;
	private Button resetButton = UiFactory.createButton("Reset");;
	private Text filePath;
	
	public MfgDataLoader(TabbedMainWindow mainWindow) {
		super("Data Load Process", mainWindow);	
		init();
	}
	
	public void init() {
		
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		double width = parentBounds.getWidth() - 5;
		double height = parentBounds.getHeight() - 141;
		
		HBox buttonBox = new HBox();
		buttonBox.getChildren().addAll(loadDataButton, resetButton);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		
		VBox detailPanel = new VBox();
		Label loadPartMaskLbl =  UiFactory.createLabel("loadProcessLabel", "Please Select Spreadsheet To Load Process Data",Fonts.SS_DIALOG_BOLD(16),TextAlignment.CENTER);;
		
		detailPanel.setPrefSize(width, height);
		detailPanel.setAlignment(Pos.TOP_CENTER);
		detailPanel.getChildren().addAll(loadPartMaskLbl, createButtonContainer());
		
		VBox partMaskBox = new VBox();
		partMaskBox.getChildren().addAll(createTitiledPane("Mfg Maintainance Control Data Load", detailPanel, width, height/2-100, 12, false), 
				createProgressBox(width, height));
		
		this.setCenter(partMaskBox);
	}
	
	private VBox createProgressBox(double scrollPaneWidth, double scrollPaneHeight) {
		VBox progressBox = new VBox();
		progressBar = new ProgressBar();
        progressBar.setPrefWidth(scrollPaneWidth/4);
        progressBar.setVisible(false);
        statusMessageTextArea = new TextArea();
        statusMessageTextArea.setVisible(false);
        statusMessageTextArea.setEditable(false);
        statusMessageTextArea.setMaxHeight(scrollPaneHeight);
        statusMessageTextArea.setMinHeight(scrollPaneHeight/1.4);
        statusMessageTextArea.setMaxWidth(scrollPaneWidth);
        
        progressBox = new VBox();
        progressBox.getChildren().addAll(progressBar, statusMessageTextArea);
        progressBox.setAlignment(Pos.CENTER);
        return progressBox;
	}
	
	private VBox createButtonContainer() {
		
		filePath = new Text();
		
		final FileChooser fileChooser = new FileChooser();
		
		VBox btnBox = new VBox();
		HBox selectBox = new HBox();
		Label selectFileLbl =  UiFactory.createLabel("selectFileLbl", "Select File",Fonts.SS_DIALOG_BOLD(16),TextAlignment.LEFT);
		fileChooserButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls");
		        fileChooser.getExtensionFilters().add(extFilter);
				template = fileChooser.showOpenDialog(ClientMainFx.getInstance().getStage());
				if (template != null) {
					fileChooserButton.setVisible(false);
					loadDataButton.setVisible(true);
					filePath.setText(template.toString());
				}
			}
		});
		
		fileChooserButton.setPadding(new Insets(10));
		selectBox.getChildren().addAll(selectFileLbl, filePath, fileChooserButton);
		selectBox.setAlignment(Pos.CENTER);
		selectBox.setPadding(new Insets(10));
		selectBox.setSpacing(20);
		
		HBox loadBox = new HBox();
		resetButton.setVisible(false);
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetButton.setVisible(false);
				fileChooserButton.setVisible(true);
				filePath.setText("");
				statusMessageTextArea.setText("");
				template = null;
			}
		});
		resetButton.setVisible(false);
		
		loadDataButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				
				if(template == null) {
					MessageDialog.showInfo(null, "Please Select Excel File");
					return;
				}
				loadDataButton.setVisible(false);
				progressBar.setVisible(true);
				Task<Void> mainTask = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						statusMessage = loadData();
						updateProgress(100, 100);
						return null;
					}
				};

				mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					public void handle(WorkerStateEvent t) {
						progressBar.setVisible(false);
						statusMessageTextArea.setVisible(true);
						resetButton.setVisible(true);
						statusMessageTextArea.setText(statusMessage.toString());
					}
				});
				progressBar.progressProperty().bind(mainTask.progressProperty());
				new Thread(mainTask).start();
			}
		});
		
		loadBox.getChildren().addAll(loadDataButton, resetButton);
		loadDataButton.setVisible(false);
		loadBox.setAlignment(Pos.CENTER);
		loadBox.setPadding(new Insets(10));
		btnBox.getChildren().addAll(selectBox, loadBox);
		
		return btnBox;
	}
	
	private TitledPane createTitiledPane(String title,Node content, double width, double height, int font, boolean collapsible) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font("", FontWeight.BOLD, font));
		titledPane.setContent(content);
		titledPane.setPrefSize(width,height);
		titledPane.setCollapsible(collapsible);
		return titledPane;
	}
	
	public String loadData() {
		String statusMsg = "";
		try {
			//Get Model Year Code Map for populating matrix
			String productType = PropertyService.getPropertyBean(SystemPropertyBean.class).getProductType();
			modelYrCodeMap = ServiceFactory.getService(GenericPddaDaoService.class)
					.getYearDescriptionCodeMap(productType);
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(template));
			HSSFSheet worksheet = null;
			int noOfSheets = workbook.getNumberOfSheets();
			try {
				for (int sheetIdx = 0; sheetIdx<noOfSheets; sheetIdx++) {
					getLogger().info("Reading Sheet '"+sheetIdx+"'");
					worksheet = workbook.getSheetAt(sheetIdx);
					//Getting Platform details from sheet name
					String sheetName = StringUtils.trim(worksheet.getSheetName());
					if(StringUtils.isBlank(sheetName)) {
						throw new Exception("Platform details not found in sheet name");
					}
					String plant_loc_code = null;
					String dept_code = null;
					BigDecimal prod_sch_qty = null;
					String line_no = null;
					String vmc = null;
					BigDecimal model_yr = null;
					StringTokenizer t = new StringTokenizer(sheetName, "_");
					if(t.countTokens() == 6) {
						plant_loc_code = t.nextToken().trim().toUpperCase();
						dept_code = t.nextToken().trim().toUpperCase();
						prod_sch_qty = new BigDecimal(t.nextToken().trim());
						line_no = t.nextToken().trim().toUpperCase();
						vmc = t.nextToken().trim().toUpperCase();
						model_yr = new BigDecimal(t.nextToken().trim());
						
					}else {
						throw new Exception("All Platform details not found in sheet name");
					}
					
					if(StringUtils.isBlank(plant_loc_code)
							|| StringUtils.isBlank(dept_code)
							|| prod_sch_qty==null
							|| StringUtils.isBlank(line_no)
							|| StringUtils.isBlank(vmc)
							|| model_yr==null
							) {
						throw new Exception("All Platform details not found in sheet name");
					}
					
					//All pending change forms
					newchangeFormsList = ServiceFactory.getDao(ChangeFormDao.class)
							.getGetAllChangeForms();
					
					MfgDataLoadDto dataLoadDto = new MfgDataLoadDto();
					
					unitMapMass = new HashMap<String, List<ChangeFormUnit>>();
					
					int rowNum = worksheet.getLastRowNum() + 1;
			        int colNum = worksheet.getRow(0).getLastCellNum();
			        String [][] data = new String [rowNum] [colNum];
					
			        successCount = 0;
			        errorCount = 0;
			        
					for(int i = 2; i <rowNum; i++){
			        	HSSFRow row = worksheet.getRow(i);
			        	boolean breakFlg = false;
		                for (int j = 0; j < colNum; j++){
		                    HSSFCell cell = row.getCell(j);
		                    String value = null;
		                    if(cell != null) {
		                    	value = cell.toString().trim();
		                    }
		                    if(value == null || value.equals("")) {
		                    	//Data is blank
		                    	breakFlg = true;
		                    	break;
		                    }
		                    data[i][j] = value;
		                }
		                if(breakFlg) {
		                	getLogger().debug("Row number " + (i+1) +" is blank from sheet '"+sheetIdx+"', fetching next sheet.");
		                	//errorCount++;
		                	//continue;
		                	break;
		                }
		                // Data creation starts
		                String pdda_proc_no = "";
		                try {
		                	String procSeqNum = data[i][0];
		                	int proc_seq_num =  0;
		                	if(StringUtils.isNotEmpty(procSeqNum)) {
		                		try {
		                			proc_seq_num =  (int)Double.parseDouble(data[i][0]);
		                		}catch(NumberFormatException nfe) {
			                		//Skip This Exception
			                	}
		                	}
		                	pdda_proc_no = data[i][1];
		                	String proc_name = data[i][2];
		                	String process_pt_id = data[i][3];
		                	String input = "Creating data for Process No: '" + pdda_proc_no;
		                	input += "', Process Seq No: '" + proc_seq_num;
			                input += "', Process Name: '" + proc_name;
			                input += "', PP Id: '" + process_pt_id + "'...";
			                getLogger().info(input);
			                if(pdda_proc_no!=null && !pdda_proc_no.trim().equalsIgnoreCase("N/A") &&
			                		proc_name!=null && !proc_name.trim().equalsIgnoreCase("N/A") &&
			                		process_pt_id!=null && !proc_name.trim().equalsIgnoreCase("")) {
			                	try {
			                		int pdda_proc_num = (int)Double.parseDouble(pdda_proc_no);
			                		pdda_proc_no = Integer.toString(pdda_proc_num);
			                	}catch(NumberFormatException nfe) {
			                		//Skip This Exception
			                	}
			                	/**
			                	 * 
			                	 * POPULATIN DTO FOR DATA LOAD BEGINS HERE
			                	 * 
			                	 * plant_loc_code, dept_code, prod_sch_qty, line_no, vmc, model_yr
			                	 * proc_seq_num, pdda_proc_no.trim(), proc_name.trim(), process_pt_id.trim()
			                	 * 
			                	 */
			                	populateMfgDataDto(plant_loc_code, dept_code, prod_sch_qty, line_no, vmc, model_yr, proc_seq_num, pdda_proc_no.trim(), proc_name.trim(), process_pt_id.trim(), dataLoadDto);
								
			                }
			                else {
			                	getLogger().error("Row number " + (i+1) + ": Skipped process point "+process_pt_id+", null or N/A");
			                	errorCount++;
			                }
							
						} catch (Exception e) {
							errorCount++;
							getLogger().error("Row number " + (i+1) + ": pdda process number: " + pdda_proc_no+ ": "+ e);
							e.printStackTrace();
						}
			        }
					/**
                	 * 
                	 * DATA LOAD BEGINS HERE
                	 * 
                	 */
					populateMfgData(dataLoadDto);
					
					getLogger().info(sheetIdx + ": " + successCount + " record/s created successfully!");
					
					statusMsg += eol + "Plant: "+plant_loc_code+", Dept: "+dept_code+", vmc: "+vmc+", Model yr: "+model_yr+" --> " + successCount + " record/s created successfully!";
					
					if(errorCount > 0)
						getLogger().debug(sheetIdx + ": " + errorCount + " record/s failed!");
				}
			}
			catch (Exception e) {
				getLogger().error("Exception occurred:  "+ e);
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			getLogger().error(e);
			e.printStackTrace();
		} catch (IOException e) {
			getLogger().error(e);
			e.printStackTrace();
		} catch (Exception e) {
			getLogger().error(e);
			e.printStackTrace();
		}
		return statusMsg;
	}
	

	private void populateMfgData(MfgDataLoadDto dataLoadDto) {
		//Loading Mass approved data
		if(dataLoadDto.getMass_chg_frm_list()!=null && !dataLoadDto.getMass_chg_frm_list().isEmpty()) {
			//Creation of revision
			MCRevision massRev = ServiceFactory.getService(
					GenericPddaDaoService.class)
					.createRevisionForChangeForms(new ArrayList<Integer>(dataLoadDto.getMass_chg_frm_list()), getUserId(), 
							"Mass Aprvd Data Load", RevisionType.PDDA_MASS.getRevType());
			//Creating PDDA Platform
			for(Integer chgFrmId: dataLoadDto.getMass_chg_frm_list()) {
				List<PlatformDto> pltfrmDtoList = dataLoadDto.getChgFrm_platform_map().get(chgFrmId);
				for(PlatformDto pltfrmDto:pltfrmDtoList) {
					createPlatform(massRev.getId(), chgFrmId, 
							pltfrmDto.getAsm_proc_num(), pltfrmDto.getProcess_pt_id(), pltfrmDto.getProc_seq_num());
					
					List<ChangeFormUnit> unitList = unitMapMass.get(pltfrmDto.getAsm_proc_num());
					if(unitList!=null) {
						for(ChangeFormUnit unit: unitList) {
							//Adding all data
							ServiceFactory.getService(GenericPddaDaoService.class).addMCRecord(unit, massRev.getId(), modelYrCodeMap, getUserId());
							
						}
					}
					successCount++;
				}
			}
			ServiceFactory.getDao(MCRevisionDao.class).setRevisionStatus(massRev.getId(), RevisionStatus.DEVELOPING);
			ServiceFactory.getService(GenericPddaDaoService.class).performApproval(massRev.getId(), getUserId());
		}
	}

	private void populateMfgDataDto(String plant_loc_code, String dept_code,
			BigDecimal prod_sch_qty, String line_no, String vmc,
			BigDecimal model_yr, int proc_seq_num, String asm_proc_num, String proc_name,
			String process_pt_id, MfgDataLoadDto dataLoadDto) {
		if(newchangeFormsList!=null) {
			for(ChangeForm chgFrm: newchangeFormsList) {
				if(chgFrm.getPlantLocCode().equalsIgnoreCase(plant_loc_code) 
						&& chgFrm.getDeptCode().equalsIgnoreCase(dept_code)
						&& chgFrm.getProdSchQty().compareTo(prod_sch_qty) == 0
						&& chgFrm.getProdAsmLineNo().equalsIgnoreCase(line_no)
						&& chgFrm.getVehicleModelCode().equalsIgnoreCase(vmc) 
						&& chgFrm.getModelYearDate().compareTo(model_yr) == 0) {
					//Platform matches
					List<ChangeFormUnit> cfUnits =ServiceFactory.getDao(ChangeFormUnitDao.class)
							.findAllForChangeForm(chgFrm.getId(), asm_proc_num);
					//Revision Type for change form
					RevisionType rType = ServiceFactory.getService(
							GenericPddaDaoService.class)
							.getRevisionType(chgFrm.getId());
					if(cfUnits!=null && !cfUnits.isEmpty()) {
						//This change form has ASM process number
						if(rType.getRevType().equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType())) {
							if(unitMapMass.containsKey(asm_proc_num)) {
								List<ChangeFormUnit> cfUnitList = unitMapMass.get(asm_proc_num);
								cfUnitList.addAll(cfUnits);
								unitMapMass.put(asm_proc_num, cfUnitList);
							}
							else {
								unitMapMass.put(asm_proc_num, cfUnits);
							}
							dataLoadDto.addMassChangeFormId(chgFrm.getId(), new PlatformDto(asm_proc_num, process_pt_id, proc_seq_num));
						}
					}
				}
			}
		}
	}

	private MCPddaPlatform createPlatform(Long revId, Integer chgFrmId, String asm_proc_num,
			String process_pt_id, int proc_seq_num) {
		long time = System.currentTimeMillis();
		Date CurrentTime = new Date(time);
		List<Process> processes = ServiceFactory.getDao(ProcessDao.class)
				.getAllBy(revId, asm_proc_num);
		
		Process process = null;
		//deprecated process flag
		boolean isDeprecated = false;
		if(processes!=null) {
			for(Process p : processes) {
				process = p;
				if(p.getAction()!=null && p.getAction().trim().equalsIgnoreCase("D")) {
					isDeprecated = true;
				}
				else {
					isDeprecated = false;
				}
			}
		}
		if(process!=null) {
			MCPddaPlatform newPlatform = new MCPddaPlatform();
			newPlatform.setRevId(revId);
			newPlatform.setAsmProcessNo(process.getAsmProcNo());
			newPlatform.setDeptCode(process.getDeptCode());
			newPlatform.setModelYearDate(process.getModelYearDate());
			newPlatform.setPlantLocCode(process.getPlantLocCode());
			
			if(proc_seq_num == 0) {
				proc_seq_num = ServiceFactory.getService(
						GenericPddaDaoService.class).getSequenceNumberForOperation(
								process_pt_id);
			}
			
			MCViosMasterPlatform masterPlat = new MCViosMasterPlatform(process.getPlantLocCode(), process.getDeptCode(), 
					process.getModelYearDate(), process.getProdSchQty(), process.getProdAsmLineNo(), process.getVehicleModelCode());
			if(ServiceFactory.getDao(MCViosMasterPlatformDao.class).findByKey(masterPlat.getGeneratedId()) == null) {
				ServiceFactory.getDao(MCViosMasterPlatformDao.class).insert(masterPlat);
			} 
			
			MCViosMasterProcess masterProc = new MCViosMasterProcess(masterPlat.getGeneratedId(), process.getAsmProcNo(), 
					process_pt_id, proc_seq_num);
			ServiceFactory.getDao(MCViosMasterProcessDao.class).save(masterProc);
			
			newPlatform.setProductAsmLineNo(process.getProdAsmLineNo());
			newPlatform.setProductScheduleQty(process.getProdSchQty());
			newPlatform.setVehicleModelCode(process.getVehicleModelCode());
			newPlatform.setCreateTimestamp(new Timestamp(CurrentTime.getTime()));
			if(isDeprecated) {
				newPlatform.setDeprecatedRevId(revId);
				newPlatform.setDeprecated(new Timestamp(time));
				newPlatform.setDeprecaterNo(getUserId());
			}
			return ServiceFactory.getDao(MCPddaPlatformDao.class).insert(newPlatform);
		}
		return null;
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) {
			return;
		} 
	}
	
	private String getUserId() {
		return getMainWindow().getUserId();
	}
}

	
