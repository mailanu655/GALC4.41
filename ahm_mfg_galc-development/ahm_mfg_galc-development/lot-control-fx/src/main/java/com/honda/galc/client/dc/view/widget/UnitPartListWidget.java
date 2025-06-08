package com.honda.galc.client.dc.view.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.PartType;
import com.honda.galc.constant.PartValidity;
import com.honda.galc.dao.pdda.UnitPartDao;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.pdda.UnitPart;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.BomPartUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unused")
public class UnitPartListWidget extends AbstractWidget {

	private static final long serialVersionUID = 1L;

	public UnitPartListWidget(ProductController productController) {
		super(ViewId.UNIT_PARTLIST_WIDGET, productController);
	}
	
	@Override
	protected void processProductCancelled(ProductModel productModel) {
	}
	
	@Override
	protected void processProductFinished(ProductModel productModel) {
	}
	
	@Override
	protected void processProductStarted(ProductModel productModel) {
	}

	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub
	}
	
	@Subscribe
	public void received(UnitNavigatorEvent event) {
		getLogger().debug("UnitNavigatorWidget.handleEvent recvd : " + event.toString());
		if (event.getType().equals(UnitNavigatorEventType.SELECTED)) {
			this.setCenter(getPartList(event.getOperation()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private TableView<PartListData> getPartList(MCOperationRevision operation) {
		//get parameter from property
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		String width = property.getUnitPartListWidth();
		String height = property.getUnitPartListHeight();
		String fontsize = property.getUnitPartListFontSize();
		
		TableView<PartListData> partList = new TableView<PartListData>();
		partList.getStylesheets().add("resource/com/honda/galc/client/dc/view/UnitPartListWidget.css");
		partList.setEditable(false);
		partList.setPrefWidth(Integer.parseInt(width));
		partList.setMaxWidth(Integer.parseInt(width));
		partList.setPrefHeight(Integer.parseInt(height));
		partList.setMaxHeight(Integer.parseInt(height));
		
		partList.getColumns().addAll( 
				makeStringColumn("Part Number","partnumber",(int) (Integer.parseInt(width)*0.22),fontsize),
				makeStringColumn("Part Name","partname",(int) (Integer.parseInt(width)*0.33),fontsize),
				makeStringColumn("FIF Flag","fifflag",(int) (Integer.parseInt(width)*0.10),fontsize),
				makeStringColumn("Part Mark","partmark",(int) (Integer.parseInt(width)*0.10),fontsize),
				makeStringColumn("Delivery Location","deliveryLoc",(int) (Integer.parseInt(width)*0.15),fontsize),
				makeStringColumn("Quantity","partqty",(int) (Integer.parseInt(width)*0.10),fontsize));	
					//TODO:: add more column here base on FIF needs
		partList.setItems(getPartListFormOperation(operation));
		return partList;
	}
	
	@SuppressWarnings("unchecked")
	private TableView<PartListData> getPartList(int MaintID) {
		//get parameter from property
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		String width = property.getUnitPartListWidth();
		String height = property.getUnitPartListHeight();
		String fontsize = property.getUnitPartListFontSize();
		
		TableView<PartListData> partList = new TableView<PartListData>();
		partList.getStylesheets().add("resource/com/honda/galc/client/dc/view/UnitPartListWidget.css");
		partList.setEditable(false);
		partList.setPrefWidth(Integer.parseInt(width));
		partList.setMaxWidth(Integer.parseInt(width));
		partList.setPrefHeight(Integer.parseInt(height));
		partList.setMaxHeight(Integer.parseInt(height));
		
		partList.getColumns().addAll( 
					makeStringColumn("Part Number","partnumber",(int) (Integer.parseInt(width)*0.3),fontsize),
					makeStringColumn("Part Name","partname",(int) (Integer.parseInt(width)*0.4),fontsize),
					makeStringColumn("Part Mark","partmark",(int) (Integer.parseInt(width)*0.15),fontsize),
					makeStringColumn("Quantity","partqty",(int) (Integer.parseInt(width)*0.15),fontsize));
					//TODO:: add more column here base on FIF needs
		partList.setItems(getPartListFormMaintID(MaintID));
		return partList;
	}

	private TableColumn<PartListData, String> makeStringColumn(String columnName, String propertyName, int prefWidth,final String fontsize) {
	    TableColumn<PartListData, String> column = UiFactory.createTableColumn(PartListData.class, String.class, columnName);
	    column.setCellValueFactory(new PropertyValueFactory<PartListData, String>(propertyName));
		    column.setCellFactory(new Callback<TableColumn<PartListData,String>, TableCell<PartListData,String>>(){
				//@Override
				public LoggedTableCell<PartListData, String> call( TableColumn<PartListData, String> param) {
					final LoggedTableCell<PartListData, String> cell = new LoggedTableCell<PartListData, String>() {
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								setText(item.toString());
								setStyle("-fx-font-size: "+fontsize+";");
								PartListData partData = getTableView().getItems().get(getTableRow().getIndex());
								if(partData != null && partData.getPartValidity()!=null) {
									clearPartValidityStyle();
									switch(partData.getPartValidity()) {
									case EXPIRED:
										getStyleClass().add("expired-part");
										break;
									case FUTUREDATED:
										getStyleClass().add("future-dated-part");
										break;
									case INVALID:
										getStyleClass().add("invalid-part");
										break;
									case UNDEFINED:
									case VALID:
									default:
										break;
									
									}
				                }
							}
						}
						
						private void clearPartValidityStyle(){
				            ObservableList<String> styleClasses = getStyleClass();
				            styleClasses.remove("expired-part");
				            styleClasses.remove("future-dated-part");
				            styleClasses.remove("invalid-part");
				        }
					};
					return cell;
				}
			} );
	    if (prefWidth!=0) column.setPrefWidth(prefWidth);
	    column.setSortable(false);
	    return column;
	  }
	

	private ObservableList<PartListData> getPartListFormOperation(MCOperationRevision operation) {
		ObservableList<PartListData> tc= FXCollections.observableArrayList();
		//Quick Fix for not showing parts widget in AEP L5 : START
		/*String ymto = ((Product) getProductController().getModel().getProduct()).getYMTO();*/
		ProductModel productModel = getProductController().getModel();
		String ymto = productModel.getProduct().getProductSpecCode();
		//Quick Fix for not showing parts widget in AEP L5 : END
		List<MCOperationPartRevision> partList = operation.getParts();
		if(partList.size() > 1) {
			Collections.sort(partList,new Comparator<MCOperationPartRevision>() {
			    public int compare(MCOperationPartRevision a, MCOperationPartRevision b) {
			     return a.getId().getPartId().compareTo(b.getId().getPartId());
			    }
			 });
		}
		//Creating lists to arrange parts with respect to their validity
		List<PartListData> validPartList = new ArrayList<PartListData>();
		List<PartListData> expiredPartList = new ArrayList<PartListData>();
		List<PartListData> futureDatedPartList = new ArrayList<PartListData>();
		List<PartListData> invalidPartList = new ArrayList<PartListData>();
		for(MCOperationPartRevision partObj : partList){
			if(partObj.getPartType().equals(PartType.REFERENCE)) {
				// Checking parts validity
				if(getProductController().getModel().getProperty().isUseEffectiveDateForParts()){
					if(partObj.getPartValidity() == null) {
						//Fetching BOM data only if part validity is not set in operation part
						PartValidity partValidity = BomPartUtil.findPartValidity(productModel.getProductType(), 
								productModel.getProductSpec(), partObj);
						partObj.setPartValidity(partValidity);
					}
				}
				//delivery location on the plant floor
				String deliveryLocation = getDeliveryLoc(ymto,operation.getId().getOperationRevision(),partObj);
				
				String partBlockCode= getPartBlockCode(ymto,partObj.getId().getOperationName(),partObj.getId().getPartId(),
						partObj.getId().getPartRevision(),partObj.getPartNo());
				//Quick Fix for not showing parts widget in AEP L5 : START
				String fifFlag = "N";
				if(partObj.getPartItemNo()!=null && partObj.getPartItemNo().length()>=7 
						&& partObj.getPartItemNo().substring(5, 6).matches("[A-Z]") 
						&& partObj.getPartItemNo().substring(6, 7).matches("[A-Z]") 
						&& partBlockCode.equalsIgnoreCase("F")) {
					fifFlag = "Y";
				}
				//Quick Fix for not showing parts widget in AEP L5 : END
				PartListData partListData = new PartListData(
						partObj.getPartNo(),
						partObj.getPartDesc(),
						//Quick Fix for not showing parts widget in AEP L5 : START
						fifFlag,
						//Quick Fix for not showing parts widget in AEP L5 : END
						partObj.getPartMark(),
						Integer.toString(partObj.getMeasCount()),
						partObj.getPartValidity(),
						deliveryLocation
						);
				if(partObj.getPartValidity() == null) {
					validPartList.add(partListData);
				}
				else {
					switch(partObj.getPartValidity()) {
						case EXPIRED:
							expiredPartList.add(partListData);
							break;
						case FUTUREDATED:
							futureDatedPartList.add(partListData);
							break;
						case INVALID:
							invalidPartList.add(partListData);
							break;
						case UNDEFINED:
						case VALID:
							validPartList.add(partListData);
							break;
						default:
							validPartList.add(partListData);
							break;
					}
				}
			}
			Logger.getLogger().check("Operation Part :: " + partObj.getPartNo()+","+ partObj.getPartDesc() +","+ partObj.getPartMark()+","+partObj.getMeasurementCount());
		}
		//Creating observable part list data
		tc.addAll(validPartList);
		tc.addAll(expiredPartList);
		tc.addAll(futureDatedPartList);
		tc.addAll(invalidPartList);
		return tc;
	}
	
	private ObservableList<PartListData> getPartListFormMaintID(int MaintID) {
		ObservableList<PartListData> tc= FXCollections.observableArrayList();
		String modelCode = getProductController().getModel().getProduct().getModelCode();
		String modelTypeCode = ((Product) getProductController().getModel().getProduct()).getModelTypeCode();
		List<UnitPart> partListFromDb = ServiceFactory.getService(UnitPartDao.class).findAllByMaintenanceIdAndMTOC(MaintID,modelCode,modelTypeCode);
		for(UnitPart partObj : partListFromDb){
			tc.add(new PartListData(
					partObj.getId().getPartNo(),
					partObj.getId().getPartName(),
					partObj.getId().getPartMarkingNo(),
					Short.toString(partObj.getId().getPartQty()),
					partObj.getId().getDeliveryLocation()
					));
		}
		return tc;
	}
	
	private String getPartBlockCode(String ymto, String opName, String partId, int partRev, String partNo){
		String partBlockCd = ServiceFactory.getService(UnitPartDao.class).findPartBlockCode(ymto, opName, partId, partRev, partNo);
		String partBlockCode = partBlockCd!=null && !partBlockCd.isEmpty() ? partBlockCd : "N";
		return partBlockCode;
	}
	private String getDeliveryLoc(String ymto, int operationRev, MCOperationPartRevision part) {
		String deliveryLoc = ServiceFactory.getService(UnitPartDao.class).findDeliveryLocation
																	(ymto.substring(1, 4),  ymto.substring(4, 7),
																	 part.getId().getOperationName(), operationRev, 
																	 part.getPartNo(),part.getPartItemNo(), part.getPartSectionCode());
		
		
		return StringUtils.isNotEmpty(deliveryLoc)?deliveryLoc:" ";
	}

	
	public static class PartListData {  
	    private SimpleStringProperty partnumber;
	    private SimpleStringProperty partname;
	    private SimpleStringProperty fifflag;
	    private SimpleStringProperty partmark;
	    private SimpleStringProperty partqty;
	    private PartValidity partValidity;
	    private SimpleStringProperty deliveryLoc;
	    
	    public PartListData(String partnumber, String partname, String fifflag, String partmark, String partqty, PartValidity partValidity, String deliveryLoc){
	    	this(partnumber, partname, fifflag, partmark, partqty, deliveryLoc);
	    	this.partValidity=partValidity;
	    }
	    
	    public PartListData(String partnumber, String partname, String fifflag, String partmark, String partqty, String deliveryLoc){  
	        this(partnumber, partname, partmark, partqty, deliveryLoc);
	        this.fifflag=new SimpleStringProperty(fifflag);
	    }  
	  
	    public PartListData(String partnumber, String partname, String partmark, String partqty, String deliveryLoc){  
	        this.partnumber=new SimpleStringProperty(partnumber);
	        this.partname=new SimpleStringProperty(partname);
	        this.partmark=new SimpleStringProperty(partmark);
	        this.partqty=new SimpleStringProperty(partqty);  
	        this.deliveryLoc =new SimpleStringProperty(deliveryLoc);
	    }  

	    /** 
	     * @return the partnumber 
	     */  
	    public String getPartnumber() {  
	        return partnumber.get();  
	    }  
	  
	    /** 
	     * @param  the partnumber to set 
	     */  
	    public void setPartnumber(String partnumber) {  
	        this.partnumber=new SimpleStringProperty(partnumber);  
	    }  
	      
	    /** 
	     * @return the partname 
	     */  
	    public String getPartname() {  
	        return partname.get();  
	    }  
	  
	    /** 
	     * @param  the partname to set 
	     */  
	    public void setPartname(String partname) {  
	        this.partname=new SimpleStringProperty(partname);  
	    }  

	    /** 
	     * @return the fifflag 
	     */  
	    public String getFifflag() {  
	        return fifflag.get();  
	    }  
	  
	    /** 
	     * @param  the fifflag to set 
	     */  
	    public void setFifflag(String fifflag) {  
	        this.fifflag=new SimpleStringProperty(fifflag);  
	    }  

	    /** 
	     * @return the partmark 
	     */  
	    public String getPartmark() {  
	        return partmark.get();  
	    }  
	  
	    /** 
	     * @param  the partmark to set 
	     */  
	    public void setPartmark(String partmark) {  
	        this.partmark=new SimpleStringProperty(partmark);  
	    }  

	    /** 
	     * @return the deliveryLoc 
	     */  
	    public String getDeliveryLoc() {  
	        return deliveryLoc.get();  
	    }  
	  
	    /** 
	     * @param  the deliveryLoc to set 
	     */  
	    public void setDeliveryLoc(String deliveryLoc) {  
	        this.deliveryLoc=new SimpleStringProperty(deliveryLoc);  
	    }
	    
	    /** 
	     * @return the partqty 
	     */  
	    public String getPartqty() {  
	        return partqty.get();  
	    }  
	  
	    /** 
	     * @param  the partqty to set 
	     */  
	    public void setPartqty(String partqty) {  
	        this.partqty=new SimpleStringProperty(partqty);  
	    }

	    /** 
	     * @return the PartValidity 
	     */  
		public PartValidity getPartValidity() {
			return partValidity;
		}

		/** 
	     * @param  the PartValidity to set 
	     */  
		public void setPartValidity(PartValidity partValidity) {
			this.partValidity = partValidity;
		}  
	}  
}
