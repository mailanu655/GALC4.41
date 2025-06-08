package com.honda.galc.client.dc.view.widget;

import java.util.List;
import java.util.Map;

import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class ProductionScheduleLotViewWidgetFX extends AbstractWidget {

	private static final long serialVersionUID = 1L;

	public ProductionScheduleLotViewWidgetFX(ProductController productController) {
		super(ViewId.PRODUCTION_SCHEDULE_LOTVIEWWIDGETFX, productController);
	}
	
	
	@Override
	protected void processProductCancelled(ProductModel productModel) {
		//setExpectedProductId(productModel.getExpectedProductId());
	}
	
	@Override
	protected void processProductFinished(ProductModel productModel) {
	}
	
	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.setCenter(getLotViewList(getProductController().getModel().getProductId()));
	}

	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("unchecked")
	private Node getLotViewList(String ProductID) {
		VBox outerBox = new VBox();
		//get parameter from property
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		String width = property.getProductionScheduleLotWidth();
		String height = property.getProductionScheduleLotHeight();
		String fontsize = property.getProductionScheduleLotFontSize();
		
		TableView<LotViewListData> LotViewList = UiFactory.createTableView(LotViewListData.class);
		LotViewList.getStylesheets().add("resource/com/honda/galc/client/dc/view/ProductionScheduleLotViewWidgetFX.css");
		LotViewList.setEditable(false);
		LotViewList.setPrefWidth(Integer.parseInt(width));
		LotViewList.setMaxWidth(Integer.parseInt(width));
	
		LotViewList.setRowFactory(new Callback<TableView<LotViewListData>, TableRow<LotViewListData>>() {
	        //@Override
	        public TableRow<LotViewListData> call(TableView<LotViewListData> tableView) {
	            final TableRow<LotViewListData> row = new TableRow<LotViewListData>() {
	                @Override
	                protected void updateItem(LotViewListData person, boolean empty){
	                    super.updateItem(person, empty);
	                    if (getIndex()==0) {
	                            getStyleClass().add("willhighlight");
	                    }
	                }
	            };
	            return row;
	        }
	    });
		
		//Frame and Engine display different content
		String productType = getProductController().getModel().getProductType();
		if ("FRAME".equals(productType))
			LotViewList.getColumns().addAll( 
					makeStringColumn("MTO","mto",(int) (Integer.parseInt(width)*0.25),fontsize,null),
					makeStringColumn("Starting Serial#","productid",(int) (Integer.parseInt(width)*0.2),fontsize,null),
					makeStringColumn("Qty","lotqty",(int) (Integer.parseInt(width)*0.05),fontsize,TextAlignment.RIGHT),
					//makeStringColumn("BOS","bos",(int) (Integer.parseInt(width)*0.05),fontsize,null),
					makeStringColumn("KD LOT","kdlot",(int) (Integer.parseInt(width)*0.25),fontsize,null), 
					makeStringColumn("EXT COLOR","extcolor",(int) (Integer.parseInt(width)*0.1),fontsize,null),
					makeStringColumn("INT COLOR","intcolor",(int) (Integer.parseInt(width)*0.15),fontsize,null));
		if ("ENGINE".equals(productType))
			LotViewList.getColumns().addAll( 
					makeStringColumn("MTO","mto",(int) (Integer.parseInt(width)*0.4),fontsize,null),
					makeStringColumn("Starting Serial#","productid",(int) (Integer.parseInt(width)*0.4),fontsize,null),
					makeStringColumn("Lot Qty","lotqty",(int) (Integer.parseInt(width)*0.2),fontsize,TextAlignment.LEFT));
		
		LotViewList.setItems(getLotViewListForProductID(ProductID,productType));
			
		Label title = UiFactory.createLabel("productionSchedule", "Production Schedule");
	
		outerBox.setAlignment(Pos.CENTER);
		outerBox.setTranslateX(10);
		outerBox.getChildren().addAll(title, LotViewList);
		outerBox.setPrefHeight(Integer.parseInt(height));
		outerBox.setMaxHeight(Integer.parseInt(height));
		return outerBox;
	}

	private TableColumn<LotViewListData, String> makeStringColumn(String columnName, String propertyName, int prefWidth,final String fontsize,final TextAlignment textAlignment) {
	    TableColumn<LotViewListData, String> column = UiFactory.createTableColumn(LotViewListData.class, String.class, columnName);
	    column.setCellValueFactory(new PropertyValueFactory<LotViewListData, String>(propertyName));
	    //TODO::change cell color for straggler EIN
		    column.setCellFactory(new Callback<TableColumn<LotViewListData,String>, TableCell<LotViewListData,String>>(){
				//@Override
				public LoggedTableCell<LotViewListData, String> call( TableColumn<LotViewListData, String> param) {
					
					final LoggedTableCell<LotViewListData, String> cell = new LoggedTableCell<LotViewListData, String>() {
						private Text text;
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								setText(item.toString());
								setStyle("-fx-font-size: "+fontsize+";");
							}
						}
					};
				    if (null != textAlignment) 
				    cell.setAlignment(Pos.CENTER);
				    
					return cell;
				}
			} );
	    if (prefWidth!=0) column.setPrefWidth(prefWidth);
	    column.setSortable(false);
	    return column;
	  }
	
	private ObservableList<LotViewListData> getLotViewListForProductID(String ProductID,String productType) {
		
		
		ObservableList<LotViewListData> tc= FXCollections.observableArrayList();
		
		
		List<Map<String, String>> LotViewListFromDb = ServiceFactory.getService(InProcessProductDao.class).getLotSequenceVIOS(ProductID,productType);
		for(Map<String, String> LotViewObj : LotViewListFromDb){
			tc.add(new LotViewListData(
					LotViewObj.get("productid"),
					LotViewObj.get("away"),
					LotViewObj.get("bos"),
					LotViewObj.get("kdlot"),
					LotViewObj.get("mto"),
					LotViewObj.get("extcolor"),
					LotViewObj.get("intcolor"),
					LotViewObj.get("lotqty")));
		}
		return tc;
	}

	
	public static class LotViewListData {  
	    private SimpleStringProperty productid;
	    private SimpleStringProperty away;
	    private SimpleStringProperty bos;
	    private SimpleStringProperty kdlot;
	    private SimpleStringProperty mto;
	    private SimpleStringProperty extcolor;
	    private SimpleStringProperty intcolor;
	    private SimpleStringProperty lotqty;
	    
	    
	    public LotViewListData(String productid, String away, String bos, String kdlot, String mto, String extcolor, String intcolor, String lotqty){  
	        this.productid=new SimpleStringProperty(productid);  
	        this.away=new SimpleStringProperty(away);  
	        this.bos=new SimpleStringProperty(bos);  
	        this.kdlot=new SimpleStringProperty(kdlot);  
	        this.mto=new SimpleStringProperty(mto);  
	        this.extcolor=new SimpleStringProperty(extcolor);  
	        this.intcolor=new SimpleStringProperty(intcolor);  
	        this.lotqty=new SimpleStringProperty(lotqty);  
	    }  
	      
	  
	    /** 
	     * @return the productid 
	     */  
	    public String getProductid() {  
	        return productid.get();  
	    }  
	  
	    /** 
	     * @param  the productid to set 
	     */  
	    public void setProductid(String productid) {  
	        this.productid=new SimpleStringProperty(productid);  
	    }  
	      
	    /** 
	     * @return the away 
	     */  
	    public String getAway() {  
	        return away.get();  
	    }  
	  
	    /** 
	     * @param  the away to set 
	     */  
	    public void setAway(String away) {  
	        this.away=new SimpleStringProperty(away);  
	    }  

	    /** 
	     * @return the bos 
	     */  
	    public String getBos() {  
	        return bos.get();  
	    }  
	  
	    /** 
	     * @param  the bos to set 
	     */  
	    public void setBos(String bos) {  
	        this.bos=new SimpleStringProperty(bos);  
	    }  
	
	    /** 
	     * @return the kdlot 
	     */  
	    public String getKdlot() {  
	        return kdlot.get();  
	    }  
	  
	    /** 
	     * @param  the kdlot to set 
	     */  
	    public void setKdlot(String kdlot) {  
	        this.kdlot=new SimpleStringProperty(kdlot);  
	    }  

	    /** 
	     * @return the mto 
	     */  
	    public String getMto() {  
	        return mto.get();  
	    }  
	  
	    /** 
	     * @param  the mto to set 
	     */  
	    public void setMto(String mto) {  
	        this.mto=new SimpleStringProperty(mto);  
	    }  

	    /** 
	     * @return the extcolor 
	     */  
	    public String getExtcolor() {  
	        return extcolor.get();  
	    }  
	  
	    /** 
	     * @param  the extcolor to set 
	     */  
	    public void setExtcolor(String extcolor) {  
	        this.extcolor=new SimpleStringProperty(extcolor);  
	    }  

	    /** 
	     * @return the intcolor 
	     */  
	    public String getIntcolor() {  
	        return intcolor.get();  
	    }  
	  
	    /** 
	     * @param  the intcolor to set 
	     */  
	    public void setIntcolor(String intcolor) {  
	        this.intcolor=new SimpleStringProperty(intcolor);  
	    }  

	    /** 
	     * @return the lotqty 
	     */  
	    public String getLotqty() {  
	        return lotqty.get();  
	    }  
	  
	    /** 
	     * @param  the lotqty to set 
	     */  
	    public void setLotqty(String lotqty) {  
	        this.lotqty=new SimpleStringProperty(lotqty);  
	    }  
}  
	

}
