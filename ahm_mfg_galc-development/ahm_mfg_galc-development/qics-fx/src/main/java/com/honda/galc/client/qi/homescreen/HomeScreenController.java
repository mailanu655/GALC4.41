package com.honda.galc.client.qi.homescreen;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.KeyboardEventType;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.repairentry.AbstractRepairEntryView;
import com.honda.galc.client.schedule.EntryDepartmentEvent;
import com.honda.galc.client.schedule.KeyboardEvent;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.schedule.TrainingModeEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.event.CacheEvent;
import com.honda.galc.client.ui.event.CacheEventType;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.StationUser;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.DailyDepartmentScheduleUtil;
import com.honda.galc.util.MultiLineHelper;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationController</code> is the controller class for Part Location Combination Panel.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class HomeScreenController extends AbstractQiProcessController<HomeScreenModel, HomeScreenView> implements EventHandler<ActionEvent> {
		
	private QiPropertyBean property;
	
	private static final int MAX_LIST_SZ = 30;
	
	private DailyDepartmentScheduleUtil schedule;
	private QiStationConfiguration qiEntryStationConfigManagement;
	private ApplicationPropertyBean appBean;
	
	private boolean isUpcStation, inTrainingMode = true;
	private static Node node;
	
	private boolean isKeepMeSignedInSelected = false;
	
	public HomeScreenController(HomeScreenModel model,HomeScreenView view) {
		super(model, view);
		EventBusUtil.register(this);
	}
	
	@Override
	public void initEventHandlers() {		
		isUpcStation = getModel().getProperty().isUpcStation();				
		addAssociateTableListners();
		addSignedInCheckBoxListner();
		if (!isUpcStation) {
			addEntryDeptComboboxListener();
			if (MultiLineHelper.getInstance(getProcessPointId()).isMultiLine()) {
				addEntryDept2ComboboxListener();
			}
		}
		addTabChangeListener();
		addProcessedProductTableListner();		
		if(isShowProductSequenceTab())  {
		getView().getProdSeqPanel().getController().initEventHandlers();
		}
		loadStationAndProductData();		
	}
	
	
	/**
	 * Method to load Station Counts, process products and associate info
	 */
	public void loadStationAndProductData() {
		schedule = ApplicationContext.getInstance().getDailyDepartmentScheduleUtil();
		loadDeptDropDown();
		loadDept2DropDown();
		addAssociateTableData();
		keepUserSignedIn();
		loadInitialData();	
		initTrainingModeButton();
		addProcessedProductData();	
		if (isShowProductSequenceTab()) {
			getView().getProdSeqPanel().getController().reload();
		}
		checkVqGdpProcessPoint();
	}

	private void initTrainingModeButton() {
		qiEntryStationConfigManagement = getModel().findPropertyKeyValueByProcessPoint(getModel().getProcessPointId(),
				QiEntryStationConfigurationSettings.TRAINING_MODE.getSettingsName());
		if (qiEntryStationConfigManagement != null) {
			if (qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.NO)) {
				inTrainingMode = false;
				getView().getEnterTrainingModeButton().setDisable(true);
				getView().getExitTrainingModeButton().setDisable(true);
				return;
			}
		}
		inTrainingMode = true;
		getView().getEnterTrainingModeButton().setDisable(false);
		getView().getExitTrainingModeButton().setDisable(false);
	}

	public void addTabChangeListener(){

		getView().getTabbedPane().getSelectionModel().selectedItemProperty().addListener(listener);
	}


	ChangeListener<Tab> listener= new ChangeListener<Tab>() {
		@SuppressWarnings("unchecked")
		@Override
		public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
			
			if(newValue != null && newValue.getId() != null){
				Logger.getLogger().check("ProductSequence/ProcessedProduct:: " + newValue.getText() + " selected");
			}
			
				if(newValue.getId().equals(getView().getProcessedTab().getId())){
					addProcessedProductData();
				}
				else if(newValue.getId().equals(getView().getProductSeqTab().getId())){
					getView().getProdSeqPanel().reload();
				}
			}
	};
	
	public boolean isShowProductSequenceTab()  {
		boolean isProductSequenceTab = PropertyService.getPropertyBoolean(getProcessPointId(), "SHOW_PRODUCT_SEQUENCE_TAB", false);
		boolean isProductSequence = PropertyService.getPropertyBoolean(getProcessPointId(), "IS_PRODUCT_SEQUENCE_STATION", false);
		return (isProductSequence && isProductSequenceTab);
	}
	
	private void addAssociateTableData() {
		String processPointId = getModel().getProcessPointId();
		this.appBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, getApplicationContext().getApplicationId());
		
		Timestamp oldTimestamp = CommonUtil.findDateRange(appBean.getDateRangeForRecentUsers());
		if (appBean.isClearRecentUser()) {
			
			if (schedule != null&&!StringUtils.isEmpty(schedule.getCurrentShift())) oldTimestamp = schedule.getShiftStartTimestamp(schedule.getCurrentShift());
		}

		getView().getAssociateTable().setData(getModel().findAllRecentUsersByHostName(processPointId,oldTimestamp,appBean.getMaxRecentUsers()));
		
		//Check configurations from station config screen to gather Associate Id
		qiEntryStationConfigManagement=getModel().findPropertyKeyValueByProcessPoint(processPointId,QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getSettingsName());
		if(qiEntryStationConfigManagement!=null ){
			if(qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.NO)){
				getView().getAssociateTable().setDisable(true);
				getView().getNewButton().setDisable(true);
				getView().getKeepSignedInChkBox().setDisable(true);
			}
		}else if(QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.NO)){
				getView().getAssociateTable().setDisable(true);
				getView().getKeepSignedInChkBox().setDisable(true);
				getView().getNewButton().setDisable(true);
		}

	}


	@SuppressWarnings("unchecked")
	private void addProcessedProductTableListner() {
		
		ObservableList<TableColumn<QiDefectResultDto, ?>> columns  = getView().getProcessedPrdTable().getTable().getColumns();
		TableColumn productIdColumn  = columns.get(1);
		
		productIdColumn.setCellFactory(new Callback<TableColumn<QiDefectResultDto, String>, TableCell<QiDefectResultDto, String>>() {
		    @Override
		    public TableCell<QiDefectResultDto, String> call(TableColumn<QiDefectResultDto, String> col) {
		        final TableCell<QiDefectResultDto, String> cell = new TableCell<QiDefectResultDto, String>() {
		            @Override
		            public void updateItem(String firstName, boolean empty) {
		                super.updateItem(firstName, empty);
		                if (empty) {
		                    setText(null);
		                } else {
		                    setText(firstName);
		                }
		            }
		         };
		         cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		             @Override
		             public void handle(MouseEvent event) {
		                 if (event.getClickCount() > 1) {
		                	 if(null!=getView().getEntryDeptComboBox().getSelectionModel().getSelectedItem()){
		                		 EventBusUtil.publish(new ProductEvent(getView().getProcessedPrdTable().getSelectedItem().getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
		                	 }else{
		                		 displayErrorMessage("Please select Entry Department");
		                	 }
		                    
		                 }
		             }
		         });
		         return cell ;
		    }
		});
		
	}

	

	private void loadDeptDropDown() {
		if(!isUpcStation){
				List<QiStationEntryDepartment> deptList = getModel().findAllEntryDeptByProcessPoint(getProcessPointId());
				if(deptList.size()>1){
					getView().getEntryDeptComboBox().setItems(FXCollections.observableArrayList(deptList));
					getView().getEntryDeptComboBox().setConverter(qiDivisionConverter());
				}

			QiStationEntryDepartment qiStationEntryDepartment = getModel().findDefaultEntryDeptByProcessPoint(getProcessPointId());
			if(qiStationEntryDepartment!=null){
					getView().getEntryDeptComboBox().getSelectionModel().select(qiStationEntryDepartment);
					getView().getEntryDeptComboBox().setConverter(qiDivisionConverter());
					getApplicationContext().setEntryDept(qiStationEntryDepartment.getId().getDivisionId());
			} else {
				getApplicationContext().setEntryDept(StringUtils.EMPTY);
			}
			EventBusUtil.publish(new EntryDepartmentEvent(getApplicationContext().getEntryDept(), QiConstant.ENTRY_DEPT_SELECTED));
		}
		
	}

	private void loadDept2DropDown() {
		if(!isUpcStation){
			MultiLineHelper multiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
			if(!multiLineHelper.isMultiLine() || StringUtils.isBlank(multiLineHelper.getFirstAlternateStation())) {
				return;
			}

			List<QiStationEntryDepartment> deptList = getModel().findAllEntryDeptByProcessPoint(multiLineHelper.getFirstAlternateStation());
			if(deptList.size()>1){
				getView().getEntryDeptComboBox2().setItems(FXCollections.observableArrayList(deptList));
				getView().getEntryDeptComboBox2().setConverter(qiDivisionConverter());
			}

			QiStationEntryDepartment qiStationEntryDepartment = getModel().findDefaultEntryDeptByProcessPoint(multiLineHelper.getFirstAlternateStation());
			if(qiStationEntryDepartment!=null){
					getView().getEntryDeptComboBox2().getSelectionModel().select(qiStationEntryDepartment);
					getView().getEntryDeptComboBox2().setConverter(qiDivisionConverter());
					getApplicationContext().setEntryDept2(qiStationEntryDepartment.getId().getDivisionId());
			} else {
				getApplicationContext().setEntryDept2(StringUtils.EMPTY);
			}
			EventBusUtil.publish(new EntryDepartmentEvent(getApplicationContext().getEntryDept2(), QiConstant.ENTRY_DEPT2_SELECTED));
		}
		
	}



	private void addSignedInCheckBoxListner() {
		getView().getKeepSignedInChkBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov,
	                Boolean old_val, Boolean new_val) {
	        	if(new_val){
	        		ClientMainFx.getInstance().stopActivityListener();
	        	}else{
	        		ClientMainFx.getInstance().startActivityListener();
	        	}
	            }
	        });
		
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addAssociateTableListners() {
		
		ObservableList<TableColumn<StationUser, ?>> columns  = getView().getAssociateTable().getTable().getColumns();
		TableColumn associateidColumn  = columns.get(1);
		
		associateidColumn.setCellFactory(new Callback<TableColumn<StationUser, String>, TableCell<StationUser, String>>() {
		    @Override
		    public TableCell<StationUser, String> call(TableColumn<StationUser, String> col) {
		        final TableCell<StationUser, String> cell = new TableCell<StationUser, String>() {
		            @Override
		            public void updateItem(String firstName, boolean empty) {
		                super.updateItem(firstName, empty);
		                if (empty) {
		                    setText(null);
		                } else {
		                    setText(firstName);
		                }
		            
		            }
		         };
		         cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		             @Override
		             public void handle(MouseEvent event) {
		                 if (event.getClickCount() >= 1) {
		                	 StationUser selectedUser = getView().getAssociateTable().getTable().getSelectionModel().getSelectedItem();
		                	 if(null != selectedUser) {
		                		String selectedUserId = selectedUser.getId().getUser();
		                		String lastUserId = getApplicationContext().getUserId();
		     					clearMessage();
		    					LoginStatus loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(selectedUserId);
		    					if(loginStatus == LoginStatus.OK){
		    						getApplicationContext().setUserId(selectedUserId);
		    						getView().getMainWindow().getStatusMessagePane().getStatusPane().setUser(selectedUserId);
		    						EventBusUtil.publish(new EntryDepartmentEvent(selectedUserId, QiConstant.ASSOCIATE_ID_SELECTED));
		    						if(!selectedUserId.equalsIgnoreCase(lastUserId))
		    							getModel().updateLastLogInUser(selectedUserId);
		    					}else{
		    						displayErrorMessage(selectedUserId.trim()+" does not exist");
		    						EventBusUtil.publish(new EntryDepartmentEvent(null, QiConstant.ASSOCIATE_ID_SELECTED));
		    						
		    					}
		    				}else{
		                		 displayErrorMessage("Please select Associate id");
		                	 }
		                    
		                 }
		             }
		         });
		         return cell ;
		    }
		});}

	
	private int getMaxListSize()  {
		
		String maxListSz = "";
		int maxSize = 0;
		QiStationConfiguration qiStationConfigEntry = getModel().findPropertyKeyValueByProcessPoint(
				getProcessPointId(), QiEntryStationConfigurationSettings.MAX_PROCESSED_LIST_SZ.getSettingsName());
		if(qiStationConfigEntry != null && qiStationConfigEntry.getPropertyValue() != null)  {
			maxListSz = qiStationConfigEntry.getPropertyValue();
		}
		if(StringUtils.isBlank(maxListSz))  {
			maxListSz = QiEntryStationConfigurationSettings.MAX_PROCESSED_LIST_SZ.getDefaultPropertyValue();				
		}
		try {
				maxSize = Integer.parseInt(maxListSz);
		} catch (NumberFormatException e) {
			maxSize = MAX_LIST_SZ;
		}

		return maxSize;
	}
	
	
	private void addProcessedProductData() {
		
		if(null!=schedule && null!=schedule.getCurrentSchedule()){
			Timestamp shiftStartTimestamp = schedule.getShiftStartTimestamp(schedule.getCurrentShift());
			if (shiftStartTimestamp == null) {
				return;
			}
			List<QiDefectResultDto> defectResultDtos = getModel().getProcessedProductData(getModel().getProcessPointId(), shiftStartTimestamp, getMaxListSize());

			if(null!=defectResultDtos && defectResultDtos.size() > 0) {
				String productType = getApplicationContext().getApplicationPropertyBean().getProductType();
				if(ProductTypeUtil.isDieCast(ProductTypeCatalog.getProductType(productType))) {
					List<QiDefectResultDto> results = new ArrayList<QiDefectResultDto>(defectResultDtos.size());
					for(QiDefectResultDto dto : defectResultDtos) {
						DieCast product = (DieCast) ProductTypeUtil.getProductDao(productType).findByKey(dto.getProductId());
						dto.setDcSerialNumber(product.getDcSerialNumber());
						dto.setMcSerialNumber(product.getMcSerialNumber());
						results.add(dto);
					}
					getView().getProcessedPrdTable().setData(results);
				}else
					getView().getProcessedPrdTable().setData(defectResultDtos);
			}
			
			getView().getProcessedPrdTable().getTable().setRowFactory(new Callback<TableView<QiDefectResultDto>, TableRow<QiDefectResultDto>>() {
	            @Override
	            public TableRow<QiDefectResultDto> call(TableView<QiDefectResultDto> paramP) {
	                return new TableRow<QiDefectResultDto>() {
	                    @Override
	                    protected void updateItem(QiDefectResultDto defectResultDto, boolean paramBoolean) {
	                    	if(null!=defectResultDto && null!=defectResultDto.getProductId()){
		                    	if(isProductScrapped(defectResultDto)){
		                    		String style = "-fx-text-background-color: red ;";
		                        	setStyle(style);
		                    	}else {
		                    		String style = "-fx-text-background-color: black ;";
		                        	setStyle(style);
		                    	}
	                    	}
	                    	
	                    	super.updateItem(defectResultDto, paramBoolean);
	                    	
	                    }
	                };
	            }
	        });
			
		}
		
	}
	
	private boolean isProductScrapped(QiDefectResultDto defectResultDto) {
		boolean isProductScraped = false;
		List<QiDefectResult>  defectResults =  getModel().getCurrentDefectStatus(defectResultDto.getProductId());
		for (QiDefectResult dto : defectResults) {
			if (DefectStatus.NON_REPAIRABLE.getId() == dto.getCurrentDefectStatus()) {
				isProductScraped = true;
				break;
			}
		}
		return isProductScraped;
	}

	private void loadInitialData() {
		WebStartClient webStart = getModel().getStationDetails(ApplicationContext.getInstance().getTerminalId());

		if(null!=schedule && null!=schedule.getCurrentSchedule()){
			getView().getShiftTextField().setText(schedule.getCurrentShift());
			StationResult stationResult = getModel().getInspectedDetails(getView().getStationNameTextField().getText(),getView().getShiftTextField().getText(),schedule.getCurrentSchedule().getId().getProductionDate());
			if(stationResult == null){
				stationResult = getModel().addStationResultForShiftAndProdDate(schedule.getCurrentSchedule().getId().getProductionDate(),schedule.getCurrentSchedule().getId().getShift(),getProcessPointId());
			}else{
				int productIdInspect = stationResult.getProductIdInspect();
				int scanCount = stationResult.getScanCount();
				int productIdDirectPassed = stationResult.getProductIdDirectPassed();
				int outstandingProductId = stationResult.getOutstandingProductId();
				int totalRejections = stationResult.getTotalRejections();
				int productIdWithDefects = stationResult.getProductIdWithDefects();
				int scrap = stationResult.getScrap();
				
				MultiLineHelper qiMultiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
				if (qiMultiLineHelper.isMultiLine()) {
					List<String> validProcessPoints = qiMultiLineHelper.getValidProcessPoints();
					String currentProcessPoint = getView().getStationNameTextField().getText();
					
					for (int i = 0; i < validProcessPoints.size(); i++) {
						if (!validProcessPoints.get(i).equals(currentProcessPoint)) {
							StationResult otherStationResult = getModel().getInspectedDetails(validProcessPoints.get(i),getView().getShiftTextField().getText(),schedule.getCurrentSchedule().getId().getProductionDate());
							if(otherStationResult != null){
								productIdInspect += otherStationResult.getProductIdInspect();
								scanCount += otherStationResult.getScanCount();
								productIdDirectPassed += otherStationResult.getProductIdDirectPassed();
								outstandingProductId += otherStationResult.getOutstandingProductId();
								totalRejections += otherStationResult.getTotalRejections();
								productIdWithDefects += otherStationResult.getProductIdWithDefects();
								scrap += otherStationResult.getScrap();
							}
						}
					}
				}
								
				getView().getInspectedTextField().setText(String.valueOf(productIdInspect));
				getView().getScanCountTextField().setText(String.valueOf(scanCount));
				getView().getDirectPassedTextField().setText(String.valueOf(productIdDirectPassed));
				getView().getNotRepairedRejectionsTextField().setText(String.valueOf(outstandingProductId));
				getView().getTotalRejectionsTextField().setText(String.valueOf(totalRejections));
				getView().getPrdsWithRejectionsTextField().setText(String.valueOf(productIdWithDefects));
				getView().getPrdsScrappedTextField().setText(String.valueOf(scrap));
			}
			TeamRotation teamRotationObj = getModel().getTeamDetails();
			if(null!=teamRotationObj)
				getView().getTeamTextField().setText(StringUtils.trimToEmpty(teamRotationObj.getId().getTeam()));
		}else{
			EventBusUtil.publish(new StatusMessageEvent("Production schedule data is not available. Please contact Administrator ", StatusMessageEventType.WARNING));
		}
		if(null!=webStart){
			getView().getExtentionTextField().setText(StringUtils.trimToEmpty(webStart.getPhoneExtension()));
			getView().getStationLocationTextField().setText(StringUtils.trimToEmpty(webStart.getColumnLocation()));
		}
	
		if (inTrainingMode && getView().getMainWindow().getStatusMessagePane().getBottom().toString().contains(QiConstant.TRAINING_MODE)){
			getView().getEnterTrainingModeButton().setVisible(false);
	        getView().getExitTrainingModeButton().setVisible(true);
	        if (AbstractRepairEntryView.getParentCachedDefectList() != null)
	        	AbstractRepairEntryView.getParentCachedDefectList().clear();
		}
		
		//check if allow defect entry for shipped VINs and tracking point flag is on
		
		if (getModel().getProperty().isAllowDefectForShippedVin() && getModel().isTrackingPoint()) {
			EventBusUtil.publish(new StatusMessageEvent("This station is for defect entry after shipping. Please contact IT to disable tracking point flag.", StatusMessageEventType.ERROR));
		}
	}

	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (QiConstant.ENTER_TRAINING_MODE.equalsIgnoreCase(loggedButton.getText()))setTrainingMode(actionEvent);
			else if (QiConstant.EXIT_TRAINING_MODE.equalsIgnoreCase(loggedButton.getText()))setExitTrainingMode(actionEvent);
			else if (QiConstant.COMMENT.equalsIgnoreCase(loggedButton.getId()))openCommentDialog(actionEvent);
			else if (QiConstant.REFRESH_CACHE.equalsIgnoreCase(loggedButton.getText())) openRefreshCacheDialog(actionEvent);
			else if (QiConstant.NEW.equalsIgnoreCase(loggedButton.getId()))openLoginPopUp(actionEvent);
			else if (QiConstant.LOGOUT.equalsIgnoreCase(loggedButton.getId())) openLogoutPopUp();
		}
	}


	private void openLoginPopUp(ActionEvent actionEvent) {
		EventBusUtil.publishAndWait(new KeyboardEvent(KeyboardEventType.HIDE_KEYBOARD));
		LoginStatus loginStatus = LoginDialog.login(ClientMainFx.getInstance().getStage(getModel().getProcessPointId()), true,true);
		if(loginStatus != LoginStatus.OK){
			EventBusUtil.publish(new EntryDepartmentEvent(null, QiConstant.ASSOCIATE_ID_SELECTED));
		} else {
			addAssociateTableData();
			getApplicationContext().setUserId(ClientMainFx.getInstance().getAccessControlManager().getUserName());
			getView().getMainWindow().getStatusMessagePane().getStatusPane().setUser(getApplicationContext().getUserId());
			EventBusUtil.publish(new EntryDepartmentEvent(ApplicationContext.getInstance().getUserId(), QiConstant.ASSOCIATE_ID_SELECTED));
		}
		
	}
	
	private void openLogoutPopUp() {
		getView().getMainWindow().logout();
		addAssociateTableData();
	}

	private void openCommentDialog(ActionEvent actionEvent) {
		EventBusUtil.publishAndWait(new KeyboardEvent(KeyboardEventType.HIDE_KEYBOARD));
    	CommentDialog dialog;
		dialog = new CommentDialog(QiConstant.CREATE,  new HomeScreenModel(),getApplicationId());
		dialog.showDialog();
	}

	private void openRefreshCacheDialog(ActionEvent actionEvent) {
		RefreshCacheDialog dialog = new RefreshCacheDialog(QiConstant.REFRESH_CACHE, new HomeScreenModel(), getApplicationId(), getApplicationContext().getApplicationPropertyBean().getProductType());
		dialog.showDialog();
		if (!dialog.isCancelled() && dialog.getEntryModel() != null) {
			EventBusUtil.publish(new CacheEvent(CacheEventType.REFRESH_PDC_CACHE, dialog.getEntryModel().getId().getEntryModel(), getApplicationId()));
		}
	}


	public QiPropertyBean getProperty() {
		if(property == null) {
			property= PropertyService.getPropertyBean(QiPropertyBean.class, getProcessPointId());
		}
		return property;
	}
	
	
	
	private void setTrainingMode(ActionEvent actionEvent) {
		LoggedLabel trainingModel = UiFactory.createLabel("trainingModel", "TRAINING MODE(Collected data won't be saved)");
		trainingModel.setMaxWidth(Double.MAX_VALUE);
		trainingModel.setStyle("-fx-font-size: 24; -fx-font-weight: bolder; -fx-text-fill: white; -fx-background-color:blue");
		trainingModel.setAlignment(Pos.CENTER);
		
		FadeTransition ft = new FadeTransition(Duration.millis(500), trainingModel);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(10);
        ft.setAutoReverse(true);
        
        ft.play();
        
        getView().getEnterTrainingModeButton().setVisible(false);
        getView().getExitTrainingModeButton().setVisible(true);
        node = getView().getMainWindow().getStatusMessagePane().getBottom();
        getView().getMainWindow().getStatusMessagePane().setBottom(trainingModel);
        getProductModel().setTrainingMode(true);        
        EventBusUtil.publish(new TrainingModeEvent(true, QiConstant.TRAINING_MODE_ON));
		
	}
	
	private void setExitTrainingMode(ActionEvent actionEvent){
		getView().getEnterTrainingModeButton().setVisible(true);
		getView().getExitTrainingModeButton().setVisible(false);
		getView().getMainWindow().getStatusMessagePane().setBottom(node);
		if(AbstractRepairEntryView.getParentCachedDefectList() != null)
			AbstractRepairEntryView.getParentCachedDefectList().clear();
		getProductModel().setTrainingMode(false);
		EventBusUtil.publish(new TrainingModeEvent(true, QiConstant.TRAINING_MODE_OFF));
	}


	private StringConverter<QiStationEntryDepartment> qiDivisionConverter() {
		return new StringConverter<QiStationEntryDepartment>() {

			@Override
			public QiStationEntryDepartment fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(QiStationEntryDepartment arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getId().getDivisionId() + "-" + arg0.getDivision().getDivisionName();
				}
			}

		};
	}
	
	
	/**
	 * This method is used to add Entry Dept Combobox Listener
	 */
	private void addEntryDept2ComboboxListener() {
		getView().getEntryDeptComboBox2().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiStationEntryDepartment>() {
			@Override
			public void changed(
					ObservableValue<? extends QiStationEntryDepartment> arg0,
					QiStationEntryDepartment oldValue, QiStationEntryDepartment newValue) {
				clearMessage();
				getApplicationContext().setEntryDept2(newValue.getId().getDivisionId());
				EventBusUtil.publish(new EntryDepartmentEvent(newValue.getId().getDivisionId(), QiConstant.ENTRY_DEPT2_SELECTED));
			}
		});
	}

	/**
	 * This method is used to add Entry Dept Combobox Listener
	 */
	private void addEntryDeptComboboxListener() {
		getView().getEntryDeptComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiStationEntryDepartment>() {
			@Override
			public void changed(
					ObservableValue<? extends QiStationEntryDepartment> arg0,
					QiStationEntryDepartment oldValue, QiStationEntryDepartment newValue) {
				clearMessage();
				if (newValue != null) {
					getApplicationContext().setEntryDept(newValue.getId().getDivisionId());
					EventBusUtil.publish(new EntryDepartmentEvent(newValue.getId().getDivisionId(), QiConstant.ENTRY_DEPT_SELECTED));
				}
			}
		});
	}


	@Override
	public void initializeListeners() {
		getView().getKeepSignedInChkBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				isKeepMeSignedInSelected = newValue;
			}
		});
	}
	
	/**
	 * This method is used to keep associate id selected when we return to home screen
	 */
	public void keepUserSignedIn() {
		if(isKeepMeSignedInSelected) {
			String userId = getView().getMainWindow().getUserId();
			getView().getKeepSignedInChkBox().setSelected(true);
			for(StationUser user : getView().getAssociateTable().getTable().getItems()) {
				if(user.getId().getUser().equalsIgnoreCase(userId)) {
					getView().getAssociateTable().getTable().getSelectionModel().select(user);
					userId = user.getId().getUser();
					break;
				}
			}
			EventBusUtil.publish(new EntryDepartmentEvent(userId, QiConstant.ASSOCIATE_ID_SELECTED));
		} else {
			EventBusUtil.publish(new EntryDepartmentEvent(null, QiConstant.ASSOCIATE_ID_SELECTED));
		}

	}

	//check if VQ GDP Process Point ID is set up for Frame plant
	public void checkVqGdpProcessPoint() {
		String productType = getApplicationContext().getApplicationPropertyBean().getProductType();
		if (ProductType.FRAME.name().equals(productType) 
				|| ProductType.FRAME_JPN.name().equals(productType)) { 
			if (getModel().getProperty().getVqGdpProcessPointId().length == 0) {
				EventBusUtil.publish(new StatusMessageEvent("Please contact IT to set up VQ GDP Process Point ID.", StatusMessageEventType.ERROR));
			}
		}
	}
}
