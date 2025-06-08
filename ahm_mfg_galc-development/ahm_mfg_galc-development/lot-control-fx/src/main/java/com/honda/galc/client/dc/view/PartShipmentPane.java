package com.honda.galc.client.dc.view;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.pane.AbstractProductIdlePane;
import com.honda.galc.client.schedule.ProductEvent;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.dao.product.PartShipmentDao;
import com.honda.galc.dao.product.PartShipmentProductDao;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PartShipment;
import com.honda.galc.entity.product.PartShipmentProduct;
import com.honda.galc.entity.product.PartShipmentProductId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.CommonPartUtility.PartMaskFormat;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;


public class PartShipmentPane  extends AbstractProductIdlePane {
	private ObjectTablePane<BaseProduct> shipmentTrackingTablePane;
	List<BaseProduct> products;
	
	public ContextMenu rowMenu;
	private Stage dialog;
	private Button generateShipmentButton, resetButton;
	private Map<String, String> sitePartCountMap;
	private Map<String, String>  siteTrailerMaskMap;
	private Map<String, String> siteAllowPartialShipmentMap;
	private boolean isScanTrailerMask;
	private String trailerNumber, site, partCount;
	
	public PartShipmentPane(ProductController productController) {
		super(productController);
		products = new ArrayList<BaseProduct>();
		EventBusUtil.register(this);
	}
	
	@Override
	protected void initComponents() {
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
	
		VBox vBox = new VBox();
				
		shipmentTrackingTablePane = createShipmentTrackingTablePane(parentBounds);
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(25, 10, 5, 10));
		generateShipmentButton = UiFactory.createButton("Generate Shipment");
		generateShipmentButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						if(isScanTrailerMask){
							generateShipmentRecords(getSite(),getTrailerNumber(),getPartCount());
						}else{
							generateShipmentDialog();
						}
					}
				});
		generateShipmentButton.setDisable(true);
		resetButton = UiFactory.createButton("Reset");
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						reset();
					}
				});
		resetButton.setDisable(false);
		hbox.getChildren().addAll(generateShipmentButton, resetButton);
		vBox.getChildren().addAll(shipmentTrackingTablePane, hbox);
		vBox.setPadding(new Insets(50));
	
		setCenter(vBox);
		setContextMenu();
		loadProperties();
		
		ClientMainFx.getInstance().getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {

			private void closeOrConsume(ClientMainFx clientMainFx, WindowEvent windowEvent,
					boolean closeWithoutPrompt) {
				if (closeWithoutPrompt) {
					clientMainFx.exitApplication(0);
				}
				if (clientMainFx.isExitConfirmed()) {
					clientMainFx.exitApplication(0);
				} else {
					windowEvent.consume();
				}
			}

			@Override
			public void handle(WindowEvent windowEvent) {
				if (windowEvent.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
					ClientMainFx clientMainFx = ClientMainFx.getInstance();
					MainWindow mainWindow = clientMainFx.getLaunchedWindows().get(clientMainFx.getCurrentApplicationId());
					if (!generateShipmentButton.isDisable() && !products.isEmpty() && MessageDialog
							.confirm(ClientMainFx.getInstance().getStage(), "Unsaved Data.  Do you want to generate shipment?")) {
						generateShipmentRecords(getSite(),getTrailerNumber(),getPartCount());
						this.closeOrConsume(clientMainFx, windowEvent, true);
					} else {
						this.closeOrConsume(clientMainFx, windowEvent, false);
					}
				} else {
					windowEvent.consume();
				}
			}
		});

	}

	protected void reset() {
		((PartShipmentInputPane)getProductController().getView().getInputPane()).resetView();
		this.products.clear();
		shipmentTrackingTablePane.setData(this.products);
		
	}

	private void loadProperties() {
		isScanTrailerMask = getProductController().getModel().getProperty().isScanTrailerFirst();
		sitePartCountMap =   getProductController().getModel().getProperty().getSitePartCount();
		siteTrailerMaskMap =  getProductController().getModel().getProperty().getSiteTrailerMask();
		siteAllowPartialShipmentMap =  getProductController().getModel().getProperty().getSiteAllowPartialShipment();
	}

	@Override
	public void toIdle() {
		if(isScanTrailerMask){
			UiUtils.requestFocus(((PartShipmentInputPane)getProductController().getView().getInputPane()).getTrailerNoField());
			if(StringUtils.isEmpty(((PartShipmentInputPane)getProductController().getView().getInputPane()).getTrailerNoField().getText())){
				getProductController().getView().getInputPane().getProductIdField().setDisable(true);
			}
		}else{
			UiUtils.requestFocus(getProductController().getView().getInputPane().getProductIdField());
		}
	}
	
	
	private void loadPartShipmentResult() {
		shipmentTrackingTablePane.setData(products);
		if(isScanTrailerMask && this.products.size() > 0){
			 loadShipmentInfo();
			if(this.products.size() == Integer.parseInt(partCount)){
				generateShipmentRecords(this.site,this.trailerNumber, this.partCount);
			}
		}
		handleGenerateShipmentButton();
	}
	
	private ObjectTablePane<BaseProduct> createShipmentTrackingTablePane(Rectangle2D parentBounds) {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "productId")
				.put("Product Spec Code","productSpecCode");
						
		Double[] columnWidth = new Double[] {
				0.10,0.12,0.12
			};
		ObjectTablePane<BaseProduct> panel = new ObjectTablePane<BaseProduct>(columnMappingList,columnWidth);
		panel.setMinWidth(250);
		panel.setMinHeight(300);
		return panel;
	}
	
	@Override
	public String getName() {
		return "Part Shipment";
	}
	
	public List<BaseProduct> getProductList(){
		return this.products;
	}
	
	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if (null!=event && event.getEventType().equals(ProductEventType.PRODUCT_INPUT_OK)) {
			BaseProduct product = (BaseProduct)event.getTargetObject();
			if(product != null && this.products!= null){
				if(this.products.contains(product)){
					String msg = String.format("%s already scanned", product.getProductId());
					getProductController().getView().setErrorMessage(msg, getProductController().getView().getInputPane().getProductIdField());
					getProductController().getAudioManager().playNGSound();
				}else{
					this.products.add(product);
					getProductController().getAudioManager().playOKSound();
					loadPartShipmentResult();
					if (this.products.size() == 1) {
						ClientMainFx clientMainFx = ClientMainFx.getInstance();
						MainWindow mainw = clientMainFx.getLaunchedWindows().get(clientMainFx.currentApplicationId);
						MenuBar menuBar = (MenuBar) mainw.getTop();
						Menu systemMenu = menuBar.getMenus().get(0);
						systemMenu.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								String menuId = ((MenuItem) event.getTarget()).getId();
								if (menuId != null && (!menuId.equals("SwitchUserMenu"))) {
									if (!generateShipmentButton.isDisable() && !products.isEmpty() && MessageDialog
											.confirm(clientMainFx.getStage(), "Unsaved Data.  Do you want to generate shipment?")) {
										generateShipmentRecords(getSite(),getTrailerNumber(),getPartCount());
										clientMainFx.exitApplication(0);
									}
								}
							}
						});
					}
				}
			}
			
		}
	}
	

	protected void setContextMenu() {
		
		shipmentTrackingTablePane.getTable().setRowFactory(new Callback<TableView<BaseProduct>, TableRow<BaseProduct>>() {
			public TableRow<BaseProduct> call(
					TableView<BaseProduct> tableView) {
				final TableRow<BaseProduct> row = new TableRow<BaseProduct>() {
					@Override
					protected void updateItem(BaseProduct item,
							boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("Right Click To Remove Part");
							setTooltip(tooltip);
						}
					}
				};
				rowMenu = new ContextMenu();

				MenuItem removePartItem = UiFactory.createMenuItem("Remove Part");
				removePartItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						BaseProduct product = row.getItem();
						products.remove(product);
						loadPartShipmentResult();
					}
				});

				rowMenu.getItems().addAll(removePartItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));

				return row;
			}
		});
	}

	private void generateShipmentRecords(String site, String trailerNumber, String partCount) {
		if(products.size() < Integer.parseInt(partCount)){
			String allowPartial = siteAllowPartialShipmentMap != null? siteAllowPartialShipmentMap.get(site):"";
			
			if(!Boolean.parseBoolean(allowPartial)){
				MessageDialog.showError("Partial Shipment Not Allowed for Site.- "+site +" , shipment requires product count "+ partCount + " or greater");
			}else{
				createShipmentRecords(site, trailerNumber);
				getProductController().getView().setMessage("Shipment Records sucessfully created");
				if(isScanTrailerMask){
					((PartShipmentInputPane)getProductController().getView().getInputPane()).resetView();
				}else{
					UiUtils.requestFocus(getProductController().getView().getInputPane().getProductIdField());
				}
				this.products.clear();
				shipmentTrackingTablePane.setData(this.products);
			}
			
		}else{
			createShipmentRecords(site, trailerNumber);
			getProductController().getView().setMessage("Shipment Records sucessfully created");
			if(isScanTrailerMask){
				((PartShipmentInputPane)getProductController().getView().getInputPane()).resetView();
			}else{
				UiUtils.requestFocus(getProductController().getView().getInputPane().getProductIdField());
			}
			this.products.clear();
			shipmentTrackingTablePane.setData(this.products);
		}
			
	}
	
	private void createShipmentRecords(String site, String trailerNumber){
		PartShipment partShipment = new PartShipment();
		partShipment.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		partShipment.setReceivingSite(site);
		partShipment.setSendStatus(0);
		partShipment.setTrailerNumber(trailerNumber);
		partShipment.setSentTimestamp(new Timestamp(System.currentTimeMillis()));
		partShipment.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		partShipment.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		partShipment.setAssociateNo(getProductController().getModel().getApplicationContext().getUserId());
		partShipment = ServiceFactory.getDao(PartShipmentDao.class).save(partShipment);
			
		for(BaseProduct product:products){
			PartShipmentProduct shipmentProduct = new PartShipmentProduct();
			PartShipmentProductId id = new PartShipmentProductId();
			id.setShipmentId(partShipment.getId());
			id.setProductId(product.getProductId());
			shipmentProduct.setId(id);
			shipmentProduct.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
			shipmentProduct.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			ServiceFactory.getDao(PartShipmentProductDao.class).save(shipmentProduct);
		}
	}
	
	private void generateShipmentDialog(){
	
		// Create the custom dialog.
		dialog = new Stage();
		dialog.setTitle("Shipment Dialog");
		dialog.initStyle(StageStyle.DECORATED);
		dialog.initModality(Modality.APPLICATION_MODAL);
	
		Label siteLabel = UiFactory.createLabel("siteLabel", "Site");
		final ComboBox siteComboBox = new ComboBox<PartCheck>();
		siteComboBox.getItems().setAll(getReceivingSites());
		siteComboBox.getSelectionModel().selectFirst();
		
		final LoggedTextField trailerNo = UiFactory.createTextField("trailerNo");
		Label trailerNoLabel = UiFactory.createLabel("trailerNoLabel", "Trailer Number");
		trailerNo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				String receivingSite = getSite(trailerNo.getText());
				if(StringUtils.isNotEmpty(receivingSite)){
					siteComboBox.getSelectionModel().select(receivingSite);
					siteComboBox.setDisable(true);
				}
			}
		});
		
		LoggedButton okButton = UiFactory.createButton("Save");
		LoggedButton cancelButton = UiFactory.createButton("Cancel");
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				closeMessage();
			}
		});
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				String site = (String)siteComboBox.getSelectionModel().getSelectedItem();
				String trailerNumber = trailerNo.getText();
				String partCount = sitePartCountMap != null ?sitePartCountMap.get(site):"0";
				 generateShipmentRecords(site,trailerNumber,partCount);
				 
				 closeMessage();
			}
		});
		
		GridPane grid = new GridPane();
		GridPane.setColumnSpan(trailerNo, 3);
		GridPane.setColumnSpan(siteComboBox, 3);
		GridPane.setColumnSpan(okButton, 3);

		
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));

		grid.add(siteLabel, 0, 3);
		grid.add(siteComboBox, 1, 3);

		grid.add(trailerNoLabel, 0, 2);
		grid.add(trailerNo, 1, 2);

		grid.add(okButton, 0, 4);
		grid.add(cancelButton, 1, 4);

		Scene scene = new Scene(grid);
		
		dialog.setScene(scene);
		dialog.centerOnScreen();
		dialog.sizeToScene();

		dialog.toFront();
		dialog.showAndWait();
	}
	
	protected String getSite(String text) {
		String site = null;
		if(siteTrailerMaskMap != null){
			Collection<String> values = siteTrailerMaskMap.values();
			for(String key: siteTrailerMaskMap.keySet()){
				String value = siteTrailerMaskMap.get(key);
				String[] tempMasks = value.split(",");
				for(String temp:tempMasks){
					if(CommonPartUtility.verification(text,temp, PartMaskFormat.DEFAULT.name())){
						site = key;
						break;
					}
				}
				if(!StringUtils.isEmpty(site)){
					break;
				}
			}
		}
	
		return site;
	}

	private List<String> getReceivingSites(){
		String receivingSite = getProductController().getModel().getProperty().getShippingSites();
		List<String> sites =  Arrays.asList(receivingSite.split(","));
		return sites;
	}
	
	public void closeMessage() {
		dialog.close();
	}
	
	private void handleGenerateShipmentButton(){
		if(this.products!= null && this.products.size() > 0){
			generateShipmentButton.setDisable(false);
			
		}else{
			generateShipmentButton.setDisable(true);
		}
	}
	
	private void loadShipmentInfo(){
			this.trailerNumber = ((PartShipmentInputPane)getProductController().getView().getInputPane()).getTrailerNumber();
			this.site = ((PartShipmentInputPane)getProductController().getView().getInputPane()).getSite();
			this.partCount = sitePartCountMap != null ?sitePartCountMap.get(site):"0";
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public String getSite() {
		return site;
	}

	public String getPartCount() {
		return partCount;
	}

	
}
