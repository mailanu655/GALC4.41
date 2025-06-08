package com.honda.galc.client.product.pane;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.report.CrystalReportGenerator;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.dao.pdda.ProcessPpeImageDao;
import com.honda.galc.dao.pdda.UnitDao;
import com.honda.galc.dao.pdda.UnitPpeImageDao;
import com.honda.galc.dto.ProcessPpeImageDto;
import com.honda.galc.dto.UnitPpeImageDto;
import com.honda.galc.dto.UpdateTrainingData;
import com.honda.galc.net.HttpClient;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.vios.dto.ProcessChange;
import com.honda.galc.vios.dto.ProcessRequirement;
import com.honda.galc.vios.dto.SafetyErgos;
import com.honda.galc.vios.dto.UnitPpeRequirement;
import com.sun.glass.ui.Screen;

public class ProcessChangeAndPpe extends AbstractProcessInfoDialog {
	
	private static String PROCESS_REQUIREMENTS = "Process Requirements";
	private static String PROCESS_CHANGE = "Process Change";
	private static String UNIT_REQUIREMENTS = "Unit Requirements";
	private String processPoint = null;
	private boolean confirmStatus = false;
	TableView<ProcessChange> processChangeTbl = null;
	private ClientAudioManager audioManager;
	private ArrayList<ProcessChange> processChgLst = null;
	private List<String> trainedProcessList = null;
	private String reportUrl;
	ProductController controller;
	private static String MDRS_URL_PART = "UpdateAssociateTrainingData";
	public ProcessChangeAndPpe(ProductController controller) {
		super("Process Change and Personal Protection Equipments", controller);
		this.controller = controller;
		processPoint = controller.getModel().getProcessPoint().getProcessPointId();
		initComponents(controller.getModel().getApplicationContext());
	}
	
	/**
	 * com.honda.galc.client.product.pane.ProcessChangeAndPpe
	 */
	private void initComponents(ApplicationContext appContext) {
		
		int width = Screen.getMainScreen().getVisibleWidth();
		int height = Screen.getMainScreen().getVisibleHeight();

		setHeight(height);
		setWidth(width);
		VBox processChangeAndPpePane = new VBox();
		processChangeAndPpePane.setPadding(new Insets(25, 25, 25, 25));
		processChangeAndPpePane.setSpacing(5.0);
		
		// Need to show process change table only if there is a process change 
		VBox processPane = constuctProcessChangePane(appContext);
		if(processPane != null)
			processChangeAndPpePane.getChildren().add(processPane);
		
		VBox processPpePane = constructProcessRequirement();
		if(processPpePane != null)
			processChangeAndPpePane.getChildren().add(processPpePane);
		
		VBox unitSftyPpeReqPane = constructSftyErgoPane();
		
		if(unitSftyPpeReqPane != null)
			processChangeAndPpePane.getChildren().add(unitSftyPpeReqPane);
		
		if(processPane == null && processPpePane == null && unitSftyPpeReqPane == null){
			setShowDialogFlag(false);
			closeProcessChange();
		}else{
			
			getRootBorderPane().setCenter(processChangeAndPpePane);
			getRootBorderPane().setBottom(constructUsrControlPane());
			
			/*
			 * As per user request, alarm on PPE screen made as configurable.
			 * Component='System_Info' property='PPE_SCREEN_ALARM_ENABLED'
			 * */
			if(PropertyService.getPropertyBean(SystemPropertyBean.class).isPpeScreenAlarmEnabled()){
				/*
				 * User has to confirm the PCN/PPE Screen by pressing Confirm Button in the screen.
				 * This action has to be happened with in 6 seconds.. other wise No Good Audio clip
				 * will be played till user clicks the confirm button.
				 * */
				
				this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
				Runnable confirmation = new Runnable() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(6000);
							while (!confirmStatus){
								// trigger URGE Sound till user confirm PCN/PPE Screen
								getAudioManager().playUrgeSound();
								Thread.sleep(2000);
								
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				new Thread(confirmation).start();				
			}

		}
			
		Logger.getLogger().check("Process Change and PPE page loaded successfully");
		
	}
	
	private void closeProcessChange(){
		confirmStatus = true;
		updateTrainingStatusInMDRS();
		Logger.getLogger().check("Process Change and PPE page closed successfully");
		super.close();
	}
	
	private void updateTrainingStatusInMDRS() {
		Gson gson = new Gson();
		trainedProcessList = new ArrayList<String>();
		ApplicationContext applicationContext = ClientMainFx.getInstance().getApplicationContext();
		SystemPropertyBean systemPropertyBean =  PropertyService.getPropertyBean(SystemPropertyBean.class);
		if(null!=processChangeTbl && null!=processChangeTbl.getItems() && !(processChangeTbl.getItems().isEmpty())){
			for (ProcessChange processChange : processChangeTbl.getItems()){
				if(processChange.isProcess()){
					trainedProcessList.add(processChange.getProcessNo());
				}
			}
		}
		if(trainedProcessList.size()>0){
			
			String baseUrl = systemPropertyBean.getMdrsRestUrl();
			
			Object[] objectList = trainedProcessList.toArray();
			String[] stringArray =  Arrays.copyOf(objectList,objectList.length,String[].class);
			Logger.getLogger().check("updating training data "+stringArray + "for user "+applicationContext.getUserId());
			
			UpdateTrainingData data = new UpdateTrainingData(CommonUtil.removeChars(applicationContext.getUserId()),stringArray);
			String json = gson.toJson(data);
			
			String response = HttpClient.post(baseUrl+MDRS_URL_PART, json, HttpURLConnection.HTTP_OK);
			if (StringUtils.isBlank(response)) {
				Logger.getLogger().check("Failed to update MDRS training status");
			}else{
				Logger.getLogger().check("Training status updated in MDRS");
			}
			
		}

	}


	private VBox constructUsrControlPane() {
		
		VBox controlPane = null;
		
		try {
			String okImgUrl = "/resource/images/common/confirm.png";
			Image imageOk = new Image(okImgUrl);
			controlPane = new VBox();
			
			ImageView confirmImgView = new ImageView(imageOk);
			Button confirmBtn = UiFactory.createButton("Confirm", "confirmBtn");
			confirmBtn.setGraphic(confirmImgView);
			confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					closeProcessChange();
				}
			});
			confirmBtn.setFont(Fonts.DIALOG_BOLD_22);
			confirmBtn.setCursor(Cursor.HAND);
			controlPane.setAlignment(Pos.BOTTOM_CENTER);
			controlPane.getChildren().add(confirmBtn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controlPane;
	}

	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}
	
	private VBox constructSftyErgoPane(){
		
		VBox sftyErgosPane = null;
		
		TableView<SafetyErgos> sftyErgosTbl = constructSftyErgos();
		TableView<UnitPpeRequirement> unitPpeReqTbl = constructUnitPpeRequirement();
		
		if(sftyErgosTbl != null || unitPpeReqTbl != null){
			sftyErgosPane = new VBox();
			Label untRequirementLbl = UiFactory.createLabel("untRequirementLbl", UNIT_REQUIREMENTS, Fonts.SS_DIALOG_BOLD(20));
			sftyErgosPane.setPadding(new Insets(5, 5, 5, 5));
			sftyErgosPane.setSpacing(5);
			sftyErgosPane.getChildren().add(untRequirementLbl);
			
			if(sftyErgosTbl != null) {
				sftyErgosPane.getChildren().add(sftyErgosTbl);
			}
			if(unitPpeReqTbl != null) {
				sftyErgosPane.getChildren().add(unitPpeReqTbl);
			}	
		}
		

		return sftyErgosPane;
	}
	
	@SuppressWarnings("unchecked")
	private TableView<UnitPpeRequirement> constructUnitPpeRequirement() {
		TableView<UnitPpeRequirement> unitPpeReqTbl = null;
		ObservableList<UnitPpeRequirement> unitPpeImgLst = null;
		
		if((unitPpeImgLst = getUnitPpeRequirement()) != null){
			
			unitPpeReqTbl = UiFactory.createTableView(UnitPpeRequirement.class, "unitPpeReqTblView");
			unitPpeReqTbl.setEditable(false);
			unitPpeReqTbl.setPrefHeight(200.0);
			unitPpeReqTbl.setPrefWidth(300);
			
			TableColumn<UnitPpeRequirement, String> unitNo = UiFactory.createTableColumn(UnitPpeRequirement.class, String.class, "Unit No"); 
			unitNo.setMaxWidth(400);
			unitNo.setCellValueFactory(new PropertyValueFactory<UnitPpeRequirement, String>("unitNo"));
			
			TableColumn<UnitPpeRequirement, String> unitOfOperationName = UiFactory.createTableColumn(UnitPpeRequirement.class, String.class, "Unit Of Operation"); 
			unitOfOperationName.setMaxWidth(400);
			unitOfOperationName.setCellValueFactory(new PropertyValueFactory<UnitPpeRequirement, String>("unitOfOperationName"));

			
			TableColumn<UnitPpeRequirement, String> ppeRequired = UiFactory.createTableColumn(UnitPpeRequirement.class, String.class, "PPE Required");
			ppeRequired.setMaxWidth(400);
			
			ppeRequired.setCellValueFactory(new PropertyValueFactory<UnitPpeRequirement, String>("ppeRequired"));
			ppeRequired.setCellFactory(new Callback<TableColumn<UnitPpeRequirement,String>, TableCell<UnitPpeRequirement,String>>() {
				
				@Override
				public TableCell<UnitPpeRequirement, String> call(
						TableColumn<UnitPpeRequirement, String> arg0) {
					
					final TableCell<UnitPpeRequirement, String> cell = new LoggedTableCell<UnitPpeRequirement, String>() {
						private Text text;
						@Override
						protected void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if(!isEmpty()){
								text = UiFactory.createText(item.toString());
								text.setWrappingWidth(200);
								setGraphic(text);
							}
						}
					};
					return cell;
				}
			});
			
			TableColumn<UnitPpeRequirement, String> potHazard = UiFactory.createTableColumn(UnitPpeRequirement.class, String.class, "Potential Hazard");
			potHazard.setMaxWidth(400);
			potHazard.setCellValueFactory(new PropertyValueFactory<UnitPpeRequirement, String>("potentialHazard"));
			
			TableColumn<UnitPpeRequirement, String> howTo = UiFactory.createTableColumn(UnitPpeRequirement.class, String.class, "How To Use");
			howTo.setMaxWidth(400);
			howTo.setCellValueFactory(new PropertyValueFactory<UnitPpeRequirement, String>("howToUse"));
			
			howTo.setCellFactory(new Callback<TableColumn<UnitPpeRequirement,String>, TableCell<UnitPpeRequirement,String>>(){
				@Override
				public TableCell<UnitPpeRequirement, String> call( TableColumn<UnitPpeRequirement, String> param) {
					
					final TableCell<UnitPpeRequirement, String> cell = new LoggedTableCell<UnitPpeRequirement, String>() {
						private Text text;
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								text = UiFactory.createText(item.toString());
								text.setWrappingWidth(200); // Setting the wrapping width to the Text
								setGraphic(text);
							}
						}
					};
					return cell;
				}

			} );
			
			TableColumn<UnitPpeRequirement, ImageView> ppeImages = UiFactory.createTableColumn(UnitPpeRequirement.class, ImageView.class, "PPE Images");
			ppeImages.setMaxWidth(500);
			ppeImages.setCellValueFactory(new PropertyValueFactory<UnitPpeRequirement, ImageView>("ppeImages"));
			
			unitPpeReqTbl.getColumns().addAll(unitNo, unitOfOperationName, ppeRequired,potHazard, howTo, ppeImages);
			
			unitPpeReqTbl.setItems(unitPpeImgLst);
		}
		
		return unitPpeReqTbl;
	}

	private ObservableList<UnitPpeRequirement> getUnitPpeRequirement() {
		ArrayList<UnitPpeImageDto> unitPpeImgLst = null;
		ArrayList<UnitPpeRequirement> UnitPpeReqLst = null;
		
		unitPpeImgLst = (ArrayList<UnitPpeImageDto>) ServiceFactory.getDao(UnitPpeImageDao.class).findAllUnitPpeImgForProcessPoint(processPoint);
		
			for(UnitPpeImageDto unitPpeImage : unitPpeImgLst){
				
				if(UnitPpeReqLst == null)
					UnitPpeReqLst = new ArrayList<UnitPpeRequirement>();
				
				UnitPpeRequirement unitPpeRequirement = new UnitPpeRequirement();
				unitPpeRequirement.setUnitNo(unitPpeImage.getUnitNo());
				unitPpeRequirement.setUnitOfOperationName(unitPpeImage.getOperationName());				
				unitPpeRequirement.setPpeRequired(unitPpeImage.getPpeRequired());
				unitPpeRequirement.setPotentialHazard(unitPpeImage.getPotentialHazard());
				unitPpeRequirement.setHowToUse(unitPpeImage.getPpeUsage());
				
				if(unitPpeImage.getImage() != null)
					unitPpeRequirement.setPpeImages(buildImageView(unitPpeImage.getImage()));
				
				UnitPpeReqLst.add(unitPpeRequirement);
			}

		if(UnitPpeReqLst == null)
			return null;
		else
			return FXCollections.observableArrayList(UnitPpeReqLst);

	}

	@SuppressWarnings("unchecked")
	private TableView<SafetyErgos> constructSftyErgos() {
		
		TableView<SafetyErgos> sftyErgosTbl = null;
		ObservableList<SafetyErgos> sftyErgoLst = null;
		
		if((sftyErgoLst = getSafetyErgoForProcessPoint()) != null){
			
			sftyErgosTbl = UiFactory.createTableView(SafetyErgos.class, "safetyErgoTblView");
			sftyErgosTbl.setEditable(false);
			sftyErgosTbl.setPrefHeight(300.0);
			sftyErgosTbl.setPrefWidth(800);
			
			TableColumn<SafetyErgos, String> unitNo = UiFactory.createTableColumn(SafetyErgos.class, String.class, "Unit No"); 
			unitNo.setMaxWidth(400);
			unitNo.setCellValueFactory(new PropertyValueFactory<SafetyErgos, String>("unitNo"));
			
			TableColumn<SafetyErgos, String> unitOfOperationName = UiFactory.createTableColumn(SafetyErgos.class, String.class, "Unit Of Operation"); 
			unitOfOperationName.setMaxWidth(400);
			unitOfOperationName.setCellValueFactory(new PropertyValueFactory<SafetyErgos, String>("unitOfOperationName"));
			
			TableColumn<SafetyErgos, String> safetyErgoPt = UiFactory.createTableColumn(SafetyErgos.class, String.class, "Safety Ergo Point"); 
			safetyErgoPt.setMaxWidth(400);
			safetyErgoPt.setCellValueFactory(new PropertyValueFactory<SafetyErgos, String>("safetyErgoPt"));
			
			safetyErgoPt.setCellFactory(new Callback<TableColumn<SafetyErgos,String>, TableCell<SafetyErgos,String>>(){
				@Override
				public TableCell<SafetyErgos, String> call( TableColumn<SafetyErgos, String> param) {
					
					final TableCell<SafetyErgos, String> cell = new LoggedTableCell<SafetyErgos, String>() {
						private Text text;
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								item = (item!=null)?item:"";
								text = UiFactory.createText(item.toString());
								text.setWrappingWidth(300); // Setting the wrapping width to the Text
								setGraphic(text);
							}
						}
					};
					return cell;
				}

			} );
			
			TableColumn<SafetyErgos, String> safetyErgoInst = UiFactory.createTableColumn(SafetyErgos.class, String.class, "Safety Ergo Instruction"); 
			safetyErgoInst.setMaxWidth(500);
			safetyErgoInst.setCellValueFactory(new PropertyValueFactory<SafetyErgos, String>("safetyErgoInst"));
			safetyErgoInst.setCellFactory(new Callback<TableColumn<SafetyErgos,String>, TableCell<SafetyErgos,String>>(){
				@Override
				public TableCell<SafetyErgos, String> call( TableColumn<SafetyErgos, String> param) {
					
					final TableCell<SafetyErgos, String> cell = new LoggedTableCell<SafetyErgos, String>() {
						private Text text;
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								item = (item!=null)?item:"";
								text = UiFactory.createText(item.toString());
								text.setWrappingWidth(450); // Setting the wrapping width to the Text
								setGraphic(text);
							}
						}
					};
					return cell;
				}

			} );
			
			sftyErgosTbl.getColumns().addAll( unitNo,unitOfOperationName, safetyErgoPt, safetyErgoInst);
			
			sftyErgosTbl.setItems(sftyErgoLst);
			
		}
		
		return sftyErgosTbl;
	}

	private ObservableList<SafetyErgos> getSafetyErgoForProcessPoint() {
		
		List<Object[]> sftyErogsLstFromDb = ServiceFactory.getService(UnitDao.class).findAllSftyErgoForProcessPoint(processPoint);
		ArrayList<SafetyErgos> sftyErgosLst = null;
		
		for(Object[] safetyErgoObj : sftyErogsLstFromDb){
			
			if((safetyErgoObj[2] != null || safetyErgoObj[3] != null) && 
					(safetyErgoObj[2].toString().trim().length() > 0 || safetyErgoObj[3].toString().trim().length() > 0)){
				
				if(sftyErgosLst == null)
					sftyErgosLst = new ArrayList<SafetyErgos>();
				
				SafetyErgos safetyErgos = new SafetyErgos();
				safetyErgos.setUnitNo(safetyErgoObj[0].toString());
				safetyErgos.setUnitOfOperationName(safetyErgoObj[1].toString());
				
				if(safetyErgoObj[2] != null && safetyErgoObj[2].toString().trim().length() > 0)
					safetyErgos.setSafetyErgoPt(safetyErgoObj[2].toString());
				
				if(safetyErgoObj[3] != null && safetyErgoObj[3].toString().trim().length() > 0)
					safetyErgos.setSafetyErgoInst(safetyErgoObj[3].toString());
				
				sftyErgosLst.add(safetyErgos);
				
				Logger.getLogger().check(safetyErgos.getUnitNo() +"," + safetyErgos.getUnitOfOperationName()+","+ safetyErgos.getSafetyErgoPt()+","+safetyErgos.getSafetyErgoInst());
			}

		}
		
		if(sftyErgosLst == null)
			return null;
		else{
			return FXCollections.observableArrayList(sftyErgosLst);
		}
			
	}

	private VBox constuctProcessChangePane(ApplicationContext appContext){
		
		VBox processRequirementPane = null;
		
		processChangeTbl = constructProcessChange(appContext);
		if(processChangeTbl != null){
			processRequirementPane = new VBox(2);
			Label processChangeLbl = UiFactory.createLabel("processChangeLbl", PROCESS_CHANGE, Fonts.SS_DIALOG_BOLD(20));
			processRequirementPane.setPadding(new Insets(5, 5, 5, 5));
			processRequirementPane.setSpacing(5);
			processRequirementPane.getChildren().add(processChangeLbl);
			processRequirementPane.getChildren().add(processChangeTbl);
		}
		
		return processRequirementPane;
	}
	
	@SuppressWarnings("unchecked")
	private TableView<ProcessChange> constructProcessChange(ApplicationContext appContext){
		
		if((processChgLst = getProcessChangeData(appContext)) != null){
			
			processChangeTbl = UiFactory.createTableView(ProcessChange.class, "processChgTblView"); 
			processChangeTbl.setEditable(false);
			processChangeTbl.setPrefHeight(200.0);
			processChangeTbl.setPrefWidth(800);
			
			
			
			TableColumn<ProcessChange, String> unitNumber = UiFactory.createTableColumn(ProcessChange.class, String.class, "Unit Number"); 
			unitNumber.setPrefWidth(200);
			unitNumber.setMaxWidth(200);
			unitNumber.setCellValueFactory(new PropertyValueFactory<ProcessChange, String>("unitNo"));
			
			TableColumn<ProcessChange, String> changeDec = UiFactory.createTableColumn(ProcessChange.class, String.class, "Change Description"); 
			changeDec.setPrefWidth(400);
			changeDec.setMaxWidth(400);
			changeDec.setCellValueFactory(new PropertyValueFactory<ProcessChange, String>("changeDesc"));
			
			TableColumn<ProcessChange, String> dateModified = UiFactory.createTableColumn(ProcessChange.class, String.class, "Date Modified"); 
			dateModified.setPrefWidth(200);
			dateModified.setMinWidth(75);
			dateModified.setCellValueFactory(new PropertyValueFactory<ProcessChange, String>("date"));
			
			TableColumn<ProcessChange, String> processNumber = UiFactory.createTableColumn(ProcessChange.class, String.class, "Process Number"); 
			processNumber.setPrefWidth(300);
			processNumber.setMaxWidth(300);
			processNumber.setCellValueFactory(new PropertyValueFactory<ProcessChange, String>("processNo"));
			
			TableColumn<ProcessChange, String> associateNumber = UiFactory.createTableColumn(ProcessChange.class, String.class, "Associate No"); 
			associateNumber.setPrefWidth(300);
			associateNumber.setMaxWidth(300);
			associateNumber.setCellValueFactory(new PropertyValueFactory<ProcessChange, String>("associateNo"));
			
			TableColumn<ProcessChange, Hyperlink> trngPdfLinkColumn = new TableColumn<ProcessChange, Hyperlink>("Training Document");
			trngPdfLinkColumn.setCellValueFactory(new PropertyValueFactory<ProcessChange, Hyperlink>("hyperlink"));
			trngPdfLinkColumn.setPrefWidth(300);
			trngPdfLinkColumn.setMaxWidth(300);
			
			trngPdfLinkColumn.setCellFactory(new Callback<TableColumn<ProcessChange,Hyperlink>, TableCell<ProcessChange,Hyperlink>>() {
				@Override
				public TableCell<ProcessChange, Hyperlink> call(TableColumn<ProcessChange, Hyperlink> arg0) {
			        TableCell<ProcessChange, Hyperlink> cell = new TableCell<ProcessChange, Hyperlink>() {
			            @Override
			            protected void updateItem(Hyperlink item, boolean empty) {
			                setGraphic(item);
			            }
			        };
			        return cell;
			    }
			});
			
			processChangeTbl.setItems(FXCollections.observableArrayList(processChgLst));
			
			TableColumn<ProcessChange, Boolean> processCol = new TableColumn<ProcessChange, Boolean>("Process");
			processCol.setCellValueFactory(new PropertyValueFactory<ProcessChange, Boolean>("process"));
			processCol.setCellFactory(CheckBoxTableCell.forTableColumn(processCol));
			processCol.setEditable(true);
			processChangeTbl.setEditable(true);
		    
			processChangeTbl.getColumns().addAll(processCol,unitNumber, changeDec, dateModified, processNumber,associateNumber,trngPdfLinkColumn);
			processChangeTbl.setRowFactory(new Callback<TableView<ProcessChange>, TableRow<ProcessChange>>() {

				@Override
				public TableRow<ProcessChange> call(TableView<ProcessChange> arg0) {
					final TableRow<ProcessChange> row = new TableRow<ProcessChange>(){
						@Override
						public void updateIndex(int arg0) {
							super.updateIndex(arg0);
							if(arg0 == 0){
								/*       set row color and bolded font for the most recent change request.        */
								setStyle("-fx-background-color: dodgerblue  ; -fx-font-weight: bold;");
							}
						}
						
					};
				return row;	
				}
			});
		}
		
		return processChangeTbl;
	}
	

	
	@SuppressWarnings("unchecked")
	private VBox constructProcessRequirement(){
		
		VBox ppeRequirementPane = null;
		ObservableList<ProcessRequirement> processPpeObsLst = getProcessPpeRequirement();
		
		if(processPpeObsLst != null){
		
			ppeRequirementPane = new VBox(2);
			Label ppeRequirementLbl = UiFactory.createLabel("ppeRequirementLbl", PROCESS_REQUIREMENTS, Fonts.SS_DIALOG_BOLD(20));
			ppeRequirementPane.setPadding(new Insets(5, 5, 5, 5));
			ppeRequirementPane.setSpacing(5); 
			ppeRequirementPane.getChildren().add(ppeRequirementLbl);
			
			TableView<ProcessRequirement> processReqEquip = UiFactory.createTableView(ProcessRequirement.class, "procReqTblView");
			processReqEquip.setEditable(false);
			processReqEquip.setPrefHeight(200.0);
			processReqEquip.setPrefWidth(300);
			
			TableColumn<ProcessRequirement, String> ppeRequired = UiFactory.createTableColumn(ProcessRequirement.class, String.class, "PPE Required");
			ppeRequired.setMaxWidth(400);
			
			ppeRequired.setCellValueFactory(new PropertyValueFactory<ProcessRequirement, String>("ppeRequired"));
			ppeRequired.setCellFactory(new Callback<TableColumn<ProcessRequirement,String>, TableCell<ProcessRequirement,String>>() {
				
				@Override
				public TableCell<ProcessRequirement, String> call(
						TableColumn<ProcessRequirement, String> arg0) {
					
					final TableCell<ProcessRequirement, String> cell = new LoggedTableCell<ProcessRequirement, String>() {
						private Text text;
						@Override
						protected void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if(!isEmpty()){
								text = UiFactory.createText(item.toString());
								text.setWrappingWidth(200);
								setGraphic(text);
							}
						}
					};
					return cell;
				}
			});
			
			TableColumn<ProcessRequirement, String> potHazard = UiFactory.createTableColumn(ProcessRequirement.class, String.class, "Potential Hazard");
			potHazard.setMaxWidth(400);
			potHazard.setCellValueFactory(new PropertyValueFactory<ProcessRequirement, String>("potentialHazard"));
			
			TableColumn<ProcessRequirement, String> howTo = UiFactory.createTableColumn(ProcessRequirement.class, String.class, "How To Use");
			howTo.setMaxWidth(400);
			howTo.setCellValueFactory(new PropertyValueFactory<ProcessRequirement, String>("howToUse"));
			
			howTo.setCellFactory(new Callback<TableColumn<ProcessRequirement,String>, TableCell<ProcessRequirement,String>>(){
				@Override
				public TableCell<ProcessRequirement, String> call( TableColumn<ProcessRequirement, String> param) {
					
					final TableCell<ProcessRequirement, String> cell = new LoggedTableCell<ProcessRequirement, String>() {
						private Text text;
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								text = UiFactory.createText(item.toString());
								text.setWrappingWidth(200); // Setting the wrapping width to the Text
								setGraphic(text);
							}
						}
					};
					return cell;
				}
	
			} );
			
			TableColumn<ProcessRequirement, ImageView> ppeImages = UiFactory.createTableColumn(ProcessRequirement.class, ImageView.class, "PPE Images");
			ppeImages.setMaxWidth(500);
			ppeImages.setCellValueFactory(new PropertyValueFactory<ProcessRequirement, ImageView>("ppeImages"));
			
			processReqEquip.getColumns().addAll(ppeRequired,potHazard, howTo, ppeImages);
			
			processReqEquip.setItems(processPpeObsLst);
			ppeRequirementPane.getChildren().add(processReqEquip);
		
		} 
		return ppeRequirementPane;
	}
	
	private ArrayList<ProcessChange> getProcessChangeData(ApplicationContext appContext){
		
		ArrayList<Object[]> processCngLst = null;
		ArrayList<ProcessChange> changeLst = null;
		
		//Getting application bean to get process change properties
		ApplicationPropertyBean appBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, appContext.getApplicationId());
				

		processCngLst = (ArrayList<Object[]>) ServiceFactory.getService(ChangeFormDao.class)
				.getProcessChangeForProcessPoint(processPoint, appBean.getProcessChangeHistoryDays(), appBean.getProcessChangeDisplayRows());
	
		CrystalReportGenerator generator = new CrystalReportGenerator();
		
		for(Object[] processChangeObj : processCngLst){
			if(changeLst == null){
				changeLst = new ArrayList<ProcessChange>();
			}


			HashMap<String,String> params = new HashMap<String,String>();

			params.put("DEPT", processChangeObj[6].toString()); 
			params.put("MODELYEAR", processChangeObj[7].toString());
			params.put("PLANT", processChangeObj[8].toString());
			params.put("LINE", processChangeObj[9].toString());
			params.put("RATE", processChangeObj[10].toString());
			params.put("VMC", processChangeObj[11].toString());
			params.put("CONTROLNO", processChangeObj[12].toString());
			params.put("ADDITIONAL_SIGN_OFF", "N");
			

			try {
				reportUrl = generator.getInfoViewURL(params);
				Logger.getLogger().check("Report URL is :: " +reportUrl);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				Logger.getLogger().error("Error generating the Crystal report URL "+e.getMessage());
			}

			if(null!=reportUrl){
				ProcessChange processChange = new ProcessChange(reportUrl);

				processChange.setUnitNo(processChangeObj[0].toString());
				processChange.setChangeDesc(processChangeObj[1].toString().trim());
				processChange.setDate((null!=processChangeObj[2]) ? processChangeObj[2].toString(): "");
				processChange.setProcessNo(processChangeObj[3].toString());
				processChange.setAssociateNo(processChangeObj[4].toString());
				changeLst.add(processChange);
				Logger.getLogger().info("Process change:: " + processChange.getProcessNo() + "," + processChange.getAssociateNo()+","+ processChange.getDate()+","+processChange.getChangeDesc());
			}
			


			
			
		}
		
		if(changeLst == null)
			return null;
		else{
			return changeLst;
		}
			
	}
	
	private ObservableList<ProcessRequirement> getProcessPpeRequirement(){
		
		ArrayList<ProcessRequirement> constructedPpeTblLst = null;
		List<ProcessPpeImageDto> processImgLst = ServiceFactory.getDao(ProcessPpeImageDao.class).findAllPpeImageForProcessPoint(processPoint);
		
		for(ProcessPpeImageDto processPpeImage : processImgLst){
			
			if(constructedPpeTblLst == null)
				constructedPpeTblLst = new ArrayList<ProcessRequirement>();
			
			constructedPpeTblLst.add(constructPpeRequirementTable(processPpeImage));
		}
		ObservableList<ProcessRequirement> ppeRequirementLst = null;
		if(constructedPpeTblLst != null)
			ppeRequirementLst = FXCollections.observableArrayList(constructedPpeTblLst);
		
		return ppeRequirementLst;
		
	}
	
	private ProcessRequirement constructPpeRequirementTable(ProcessPpeImageDto processPpeImage){
		
		ProcessRequirement processRequirement = new ProcessRequirement();
		processRequirement.setPpeRequired(processPpeImage.getPpeRequired());
		processRequirement.setPotentialHazard(processPpeImage.getPotentialHazard());
		processRequirement.setHowToUse(processPpeImage.getPpeUsage());
		
		byte[] ppeImg = null;
		if((ppeImg = processPpeImage.getImage()) != null){
			processRequirement.setPpeImages(buildImageView(ppeImg));
		}

		return processRequirement;
		
	}
	
	
	private ImageView buildImageView(byte[] imageInBytes){
		ImageView imageView = new ImageView();
		Image image = null;
		try{
			image = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(imageInBytes)),null);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		imageView.setSmooth(true);
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);
		imageView.setPreserveRatio(true);
		imageView.setImage(image);
		
		return imageView;
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

}
