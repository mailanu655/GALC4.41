package com.honda.galc.client.dc.view;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.ProcessInstructionModel;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.pdda.UnitDao;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class OperationListWidgetFX extends ProcessInstructionWidget<ProcessInstructionModel> {

	String asmProcessName = "";
	String mode = null;

	public OperationListWidgetFX(ProcessInstructionModel model) {
		super(model);
	}

	public void initComponents() {
		this.setMode(PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString()));
		this.setCenter(getOperationList());
		this.setId("operLstWidgetFxId");
		Logger.getLogger().check("OperationListWidgetFX has been loaded");
	}
		
	@SuppressWarnings("unchecked")
	private VBox getOperationList() {
		final String ProcessPointID = getModel().getProductModel().getProcessPoint().getProcessPointId();
		final String ProductID = getModel().getProductModel().getProductId();
		final boolean isTrainingMode = getModel().getProductModel().isTrainingMode();
		

		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,ProcessPointID);
		final String fontsize = property.getOperationListFontSize();
		String backgroundcolor = property.getOperationListBackGroundColor();
		
		String cssStyle = "-fx-font-size:"+fontsize+";";
		if (StringUtils.isNotEmpty(backgroundcolor))  cssStyle = cssStyle + "-fx-background-color:"+backgroundcolor+";";

		final String Style = cssStyle;
		
		final VBox vbox =  new VBox();
		vbox.setPadding(new Insets(5));
		
		final TableView<OperationListData> operationList = UiFactory.createTableView(OperationListData.class, "operationLstTbl");
		operationList.getStylesheets().add("resource/com/honda/galc/client/dc/view/OperationListWidget.css");
		
		operationList.setEditable(false);
		VBox.setVgrow(operationList,Priority.ALWAYS);
		
		
		//Add change listener
		operationList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OperationListData>() {
	        //@Override
	        public void changed(ObservableValue<? extends OperationListData> ov, OperationListData old, OperationListData newM) {
	        	
	        	if(operationList.getSelectionModel().getSelectedItem() != null) 
	            {    
	                TableViewSelectionModel<OperationListData> selectionModel = operationList.getSelectionModel();
	                List<OperationListData> selected = selectionModel.getSelectedItems();
	                String maintID = selected.get(0).getPddaMaintenanceId();
	                Logger.getLogger().check("Unit no " +selected.get(0).getUnitNo() + " is selected");
		        	vbox.getChildren().remove(operationList);
		        	Image image = new Image("resource/images/common/back.png");
		        	final ImageView back = new ImageView();
		        	back.setId("operBckBtn");
		        	back.setFitWidth(50);
		        	back.setPreserveRatio(true);
		        	back.setSmooth(false);
		        	back.setCache(false);
		        	back.setImage(image);
		        	
		        	final OperationInfoFXBase operationInfo = new OperationInfoFXBase( operationList.widthProperty().intValue(), Integer.parseInt(maintID),ProcessPointID,ProductID,isTrainingMode);
		        	
		        	back.setOnMouseClicked(new EventHandler<MouseEvent>() {
		        	    //@Override 
		        	    public void handle(MouseEvent e) {
		        	    	Logger.getLogger().check("Back button clicked");
		        	    	vbox.getChildren().add(operationList);
		        	    	vbox.getChildren().remove(back);
		        	    	vbox.getChildren().remove(operationInfo);
		        	    }
		        	});
		        	vbox.getChildren().add(back);
		        	vbox.getChildren().add(operationInfo);
	             }
	        }
	    });
			
		TableColumn<OperationListData, Integer> unitSeqNo = UiFactory.createTableColumn(OperationListData.class, Integer.class, "Seq."); 
		unitSeqNo.prefWidthProperty().bind(operationList.widthProperty().multiply(0.05));
		unitSeqNo.setCellValueFactory(new PropertyValueFactory<OperationListData, Integer>("unitSeqNo"));
		unitSeqNo.setCellFactory(new Callback<TableColumn<OperationListData,Integer>, TableCell<OperationListData,Integer>>(){
			//@Override
			public LoggedTableCell<OperationListData, Integer> call( TableColumn<OperationListData, Integer> param) {
				final LoggedTableCell<OperationListData, Integer> cell = new LoggedTableCell<OperationListData, Integer>() {
					private Text text;
					@Override
					public void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = UiFactory.createText(item.toString());
							setStyle(Style);
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		} );
			
		TableColumn<OperationListData, String> unitNo = UiFactory.createTableColumn(OperationListData.class, String.class, "Unit No"); 
		unitNo.setId("unitNoTblClmn");
		unitNo.prefWidthProperty().bind(operationList.widthProperty().multiply(0.15));
		unitNo.setCellValueFactory(new PropertyValueFactory<OperationListData, String>("unitNo"));
		unitNo.setCellFactory(new Callback<TableColumn<OperationListData,String>, TableCell<OperationListData,String>>(){
			//@Override
			public LoggedTableCell<OperationListData, String> call( TableColumn<OperationListData, String> param) {
				
				final LoggedTableCell<OperationListData, String> cell = new LoggedTableCell<OperationListData, String>() {
					private Label lbl;
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							lbl = UiFactory.createLabel("cell:" + item.toString().trim(), item.toString().trim());
							setStyle(Style);
							setGraphic(lbl);
						}
					}
				};
				return cell;
			}
		} );

		final TableColumn<OperationListData, String>  unitOperationDesc = UiFactory.createTableColumn(OperationListData.class, String.class, "Unit of Operation"); 
		unitOperationDesc.prefWidthProperty().bind(operationList.widthProperty().multiply(0.65));
		unitOperationDesc.setCellValueFactory(new PropertyValueFactory<OperationListData, String>("unitOperationDesc"));
		unitOperationDesc.setCellFactory(new Callback<TableColumn<OperationListData,String>, TableCell<OperationListData,String>>(){
			//@Override
			public LoggedTableCell<OperationListData, String> call( TableColumn<OperationListData, String> param) {
				final LoggedTableCell<OperationListData, String> cell = new LoggedTableCell<OperationListData, String>() {
					private Text text;
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = UiFactory.createText(item.toString().trim());
							text.setWrappingWidth(unitOperationDesc.getWidth()); // Setting the wrapping width to the Text
							setStyle(Style);
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		} );

		TableColumn<OperationListData, String> unitBasePartNo = UiFactory.createTableColumn(OperationListData.class, String.class, "Base No."); 
		unitBasePartNo.prefWidthProperty().bind(operationList.widthProperty().multiply(0.15));
		unitBasePartNo.setCellValueFactory(new PropertyValueFactory<OperationListData, String>("unitBasePartNo"));
		unitBasePartNo.setCellFactory(new Callback<TableColumn<OperationListData,String>, TableCell<OperationListData,String>>(){
			//@Override
			public LoggedTableCell<OperationListData, String> call( TableColumn<OperationListData, String> param) {
				final LoggedTableCell<OperationListData, String> cell = new LoggedTableCell<OperationListData, String>() {
					private Text text;
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = UiFactory.createText(item.toString());
							setStyle(Style);
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		} );
			
		operationList.getColumns().addAll( unitSeqNo,unitNo,unitOperationDesc, unitBasePartNo);
		operationList.setItems(getOperationListForProcessPoint(ProductID,ProcessPointID));
		unitOperationDesc.setText(asmProcessName+" Unit of Operation");
		vbox.getChildren().addAll(operationList);
		return vbox;
	}

	private ObservableList<OperationListData> getOperationListForProcessPoint(String ProductID,String ProcessPointID) {
		ObservableList<OperationListData> tc= FXCollections.observableArrayList();
		List<UnitOfOperation> OperationListFromDb = ServiceFactory.getService(UnitDao.class).getAllOperationsForProcessPoint(ProductID,ProcessPointID, getMode());
		if (OperationListFromDb == null) return null;
		for(UnitOfOperation OperationObj : OperationListFromDb){
			this.asmProcessName =  OperationObj.getAsmProcessName().trim();
			tc.add(new OperationListData(OperationObj.getUnitSeqNo(),OperationObj.getUnitNo(),OperationObj.getPartFlg(),OperationObj.getUnitOperationDesc(),OperationObj.getUnitBasePartNo(),OperationObj.getPddaMaintenanceId()));
		}
		return tc;
	}
	
	public static class OperationListData {  
	    private SimpleIntegerProperty unitSeqNo;  
	    private SimpleStringProperty unitNo;
	    private SimpleStringProperty partFlg;
	    private SimpleStringProperty unitOperationDesc;
	    private SimpleStringProperty unitBasePartNo;
	    private SimpleStringProperty pddaMaintenanceId;

	    public OperationListData(int unitSeqNo, String unitNo, String partFlg, String unitOperationDesc, String unitBasePartNo,String pddaMaintenanceId){  
	        this.unitSeqNo=new SimpleIntegerProperty(unitSeqNo);  
	        this.unitNo=new SimpleStringProperty(unitNo);  
	        this.partFlg=new SimpleStringProperty(partFlg);  
	        this.unitOperationDesc=new SimpleStringProperty(unitOperationDesc);  
	        this.unitBasePartNo=new SimpleStringProperty(unitBasePartNo);  
	        this.pddaMaintenanceId=new SimpleStringProperty(pddaMaintenanceId);
	    }  
	      
	    public int getUnitSeqNo() {  
	        return unitSeqNo.get();  
	    }  
	  
	    public void setUnitSeqNo(int unitSeqNo) {  
	       this.unitSeqNo=new SimpleIntegerProperty(unitSeqNo);  
	    }  
	  
	    /** 
	     * @return the unitNo 
	     */  
	    public String getUnitNo() {
	        String unitNoPartFlg = unitNo.get()+" "+partFlg.get();
	        return unitNoPartFlg.trim();
	    }  
	  
	    /** 
	     * @param  the unitNo to set 
	     */  
	    public void setUnitNo(String unitNo) {  
	        this.unitNo=new SimpleStringProperty(unitNo);  
	    }  
	      
	    /** 
	     * @return the partFlg 
	     */  
	    public String getPartFlg() {  
	        return partFlg.get();  
	    }  
	  
	    /** 
	     * @param  the partFlg to set 
	     */  
	    public void setPartFlg(String partFlg) {  
	        this.partFlg=new SimpleStringProperty(partFlg);  
	    }  

	    /** 
	     * @return the unitOperationDesc 
	     */  
	    public String getUnitOperationDesc() {  
	        return unitOperationDesc.get().trim();  
	    }  
	  
	    /** 
	     * @param  the unitOperationDesc to set 
	     */  
	    public void setUnitOperationDesc(String unitOperationDesc) {  
	        this.unitOperationDesc=new SimpleStringProperty(unitOperationDesc);  
	    }  
	
	    /** 
	     * @return the unitBasePartNo 
	     */  
	    public String getUnitBasePartNo() {  
	        return unitBasePartNo.get();  
	    }  
	  
	    /** 
	     * @param  the unitBasePartNo to set 
	     */  
	    public void setUnitBasePartNo(String unitBasePartNo) {  
	        this.unitBasePartNo=new SimpleStringProperty(unitBasePartNo);  
	    }  
	    
	    /** 
	     * @return the pddaMaintenanceId 
	     */  
	    public String getPddaMaintenanceId() {  
	        return pddaMaintenanceId.get();  
	    }  
	  
	    /** 
	     * @param  the pddaMaintenanceId to set 
	     */  
	    public void setPddaMaintenanceId(String pddaMaintenanceId) {  
	        this.pddaMaintenanceId=new SimpleStringProperty(pddaMaintenanceId);  
	    }  
	}

	private String getMode() {
		return mode;
	}

	private void setMode(String mode) {
		this.mode = mode;
	}  

}