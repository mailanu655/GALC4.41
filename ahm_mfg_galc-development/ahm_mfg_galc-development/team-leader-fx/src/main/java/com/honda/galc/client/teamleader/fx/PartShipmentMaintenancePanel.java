package com.honda.galc.client.teamleader.fx;

import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.PartShipmentDao;
import com.honda.galc.dao.product.PartShipmentProductDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.BuildStatus;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartShipment;
import com.honda.galc.entity.product.PartShipmentProduct;
import com.honda.galc.entity.product.PartShipmentProductId;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.control.DatePicker;

public class PartShipmentMaintenancePanel extends TabbedPanel {

	private TabbedMainWindow mainWin;
	private ObjectTablePane<PartShipment> partShipmentPanel;
	private ObjectTablePane<MbpnProduct> shipmentProductPanel;
	public ContextMenu productRowMenu;
	public ContextMenu shipmentRowMenu;
	private Stage dialog;
	private ProductTypeData productTypeData;
	private String ALL_SITES = "ALL";
	private DatePicker datePicker;
	private ComboBox filterSiteComboBox; 
	
	public PartShipmentMaintenancePanel(TabbedMainWindow mainWin) {
		super("PartShipmentMaintenance", KeyEvent.VK_E, mainWin);
		this.mainWin = mainWin;
		init();
	}
	
	private void init() {
		onTabSelected();
	}

	@Override
	public void onTabSelected() {
		initComponents();
		
	}

	private void initComponents() {
		BorderPane pane = new BorderPane();
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 20, 10, 20));
		vbox.setSpacing(10);

		vbox.getChildren().add(createProductPanel());
		vbox.getChildren().add(createFilterPanel());
		vbox.getChildren().add(createPartShipmentPanel());
		vbox.getChildren().add(createPartShipmentProductPanel() );

		pane.setCenter(vbox);
		setCenter(pane);
		
		loadShipmentData(null,new Date(System.currentTimeMillis()));
		mapActions();
		setContextMenu();
	}

	private void loadShipmentData(String site,Date date) {
		List<PartShipment> partShipmentResult = ServiceFactory.getDao(PartShipmentDao.class).findByReceivingSiteAndShipmentDate(site,date);  
		partShipmentPanel.setData(partShipmentResult);
		mainWin.setErrorMessage("");
	}
	
	private void loadShipmentDataForProductId(String productId){
		PartShipmentProduct partShipmentProduct = ServiceFactory.getDao(PartShipmentProductDao.class).findByProductId(productId);
		if(partShipmentProduct != null){
			mainWin.clearMessage();
			PartShipment partShipment = ServiceFactory.getDao(PartShipmentDao.class).findByKey(partShipmentProduct.getId().getShipmentId());
			List<PartShipment> partShipmentResult = new ArrayList<PartShipment>();
			partShipmentResult.add(partShipment);
			partShipmentPanel.setData(partShipmentResult);
			shipmentProductPanel.setData(new ArrayList<MbpnProduct>());
		}else{
			mainWin.setErrorMessage(" No Shipment Record available for Scanned Product - "+productId);
		}
	}
	
	private Node createFilterPanel() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 10, 5, 10));
		
		HBox hbox1 = new HBox();
		hbox1.setPadding(new Insets(5, 10, 5, 10));
		Label siteLabel = UiFactory.createLabel("siteLabel", "Site ",20);
		filterSiteComboBox = new ComboBox<PartCheck>();
		List<String> sites = new ArrayList<String>();
		sites.add(ALL_SITES);
		sites.addAll(getReceivingSites());
		filterSiteComboBox.getItems().setAll(sites);
		filterSiteComboBox.getSelectionModel().selectFirst();
		
		hbox1.getChildren().add(siteLabel);
		hbox1.getChildren().add(filterSiteComboBox);
		
		HBox hbox2 = new HBox();
		hbox2.setPadding(new Insets(5, 10, 5, 10));
		
		datePicker = new DatePicker();
		Label dateLabel = UiFactory.createLabel("sentDateLabel", "Shipment Date ",20);
		datePicker.setValue(LocalDate.now());
		
		hbox2.getChildren().add(dateLabel);
		hbox2.getChildren().add(datePicker);
		
		Button filterButton = UiFactory.createButton("Filter Shipments");
		filterButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				filterShipments();
			}
		});		
		
		hbox.getChildren().add(hbox1);
		hbox.getChildren().add(hbox2);
		
		hbox.getChildren().add(filterButton);
			
		
		return hbox;
	}

	protected void filterShipments() {
		String selectedSite = (String)filterSiteComboBox.getSelectionModel().getSelectedItem();
		LocalDate ld = datePicker.getValue();
		Calendar c =  Calendar.getInstance();
		c.set(ld.getYear(), ld.getMonthValue()-1, ld.getDayOfMonth());
		Date selectedDate = c.getTime();
		String site = selectedSite == ALL_SITES?null:selectedSite;
		loadShipmentData(site, selectedDate);
		
	}

	private Node createProductPanel() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 10, 5, 10));
		
		final LoggedTextField productId = UiFactory.createTextField("productId");
		String label = getProductTypeData().getProductIdLabel();
		
		LoggedButton productIdButton = UiFactory.createButton(label, UiFactory.getIdle().getButtonFont(), true);
		productIdButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
						"Manual Product Entry Dialog",getProductTypeData(),getMainWindow().getApplicationContext().getApplicationId());
				manualProductEntryDialog.showDialog();
				String product = manualProductEntryDialog.getResultProductId();
				productId.requestFocus();
				productId.setText(product);
			}
		});
		hbox.getChildren().add(productIdButton);
	
		productId.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				String prodId = productId.getText();
				if(!StringUtils.isEmpty(prodId)){
					loadShipmentDataForProductId(productId.getText());
				}else{
					filterShipments();
					shipmentProductPanel.setData(new ArrayList<MbpnProduct>());
				}
			}
		});
	
		hbox.getChildren().add(productId);
		
		return hbox;
	}
	private Node createPartShipmentProductPanel() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 10, 5, 10));
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "productId")
				.put("Product Spec Code","currentProductSpecCode")
				.put("Order Number","currentOrderNo")
				.put("Tracking Status","trackingStatus");
			
		Double[] columnWidth = new Double[] {
				0.10,0.12,0.12,0.12
			};
		shipmentProductPanel = new ObjectTablePane<MbpnProduct>(columnMappingList,columnWidth);
		shipmentProductPanel.setMinWidth(250);
		shipmentProductPanel.setMinHeight(300);
		
		hbox.getChildren().add(shipmentProductPanel);
		
		return hbox;
	}

	private void mapActions() {
		// TODO Auto-generated method stub
		
	}

	

	private Node createPartShipmentPanel() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 10, 5, 10));
		String trailerNumberLabel = PropertyService.getProperty(getApplicationId(), "TRAILER_NUMBER_LABEL");
		ColumnMappingList columnMappingList = ColumnMappingList.with("Shipment Id", "shipmentId")
				.put("Receiving Site","receivingSite")
				.put(StringUtils.isBlank(trailerNumberLabel)?"Trailer Number":trailerNumberLabel,"trailerNumber")
				.put("Shipment Date","sentTimestamp")
				.put("Send Status","sendStatus");
				
			
		Double[] columnWidth = new Double[] {
				0.10,0.12,0.12,0.12,0.12
			};
		partShipmentPanel = new ObjectTablePane<PartShipment>(columnMappingList,columnWidth);
		partShipmentPanel.setMinWidth(250);
		partShipmentPanel.setMinHeight(300);
		
		partShipmentPanel.getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PartShipment>(){

			@Override
			public void changed(ObservableValue<? extends PartShipment> arg0, PartShipment arg1, PartShipment arg2) {
				PartShipment partShipment = partShipmentPanel.getTable().getSelectionModel().getSelectedItem();
				loadPartShipmentProducts(partShipment);
				
			}
			
		});

		hbox.getChildren().add(partShipmentPanel);
		
		Button generateShipmentButton = UiFactory.createButton("Change Receiving Site");
		generateShipmentButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				changeReceivingSiteShipmentDialog();
			}
		});
		hbox.getChildren().add(generateShipmentButton);
	
		return hbox;
	}

	protected void setContextMenu() {
		
		shipmentProductPanel.getTable().setRowFactory(new Callback<TableView<MbpnProduct>, TableRow<MbpnProduct>>() {
			public TableRow<MbpnProduct> call(
					TableView<MbpnProduct> tableView) {
				final TableRow<MbpnProduct> row = new TableRow<MbpnProduct>() {
					@Override
					protected void updateItem(MbpnProduct item,
							boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("Right Click To Remove Product");
							setTooltip(tooltip);
						}
					}
				};
				productRowMenu = new ContextMenu();

				MenuItem removePartItem = UiFactory.createMenuItem("Remove Product");
				removePartItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						MbpnProduct product = row.getItem();
						removeShipmentProduct(product);			
					}

				});
				
				MenuItem addPartItem = UiFactory.createMenuItem("Add Product");
				addPartItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						addShipmentProduct();			
					}

				});
			
				productRowMenu.getItems().addAll(removePartItem,addPartItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(productRowMenu).otherwise((ContextMenu) null));

				return row;
			}
		});
		
		partShipmentPanel.getTable().setRowFactory(new Callback<TableView<PartShipment>, TableRow<PartShipment>>() {
			public TableRow<PartShipment> call(
					TableView<PartShipment> tableView) {
				final TableRow<PartShipment> row = new TableRow<PartShipment>() {
					@Override
					protected void updateItem(PartShipment item,
							boolean empty) {
						super.updateItem(item, empty);
						if (item != null && item.getSendStatus() == 2) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("Right Click To mark Unsent");
							setTooltip(tooltip);
						}
					}
				};
				shipmentRowMenu = new ContextMenu();

				MenuItem markUnsent = UiFactory.createMenuItem("Mark Unsent");
				markUnsent.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						PartShipment shipment = row.getItem();
						if(shipment.getSendStatus() == 2){
							markShipmentUnsent(shipment);	
						}
					}
				});
				
				MenuItem createShipment = UiFactory.createMenuItem("Create");
				createShipment.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
							createShipment();
					}

					
				});
				
				shipmentRowMenu.getItems().add(markUnsent);
			
				shipmentRowMenu.getItems().add(createShipment);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(shipmentRowMenu).otherwise((ContextMenu) null));

				return row;
			}
		});
	}
	
	private void createShipment() {
		// Create the custom dialog.
					dialog = new Stage();
					dialog.setTitle("Shipment Dialog");
					dialog.initStyle(StageStyle.DECORATED);
					dialog.initModality(Modality.APPLICATION_MODAL);
				
					dialog.setWidth(400);
					Label siteLabel = UiFactory.createLabel("siteLabel", "Site ",20);
					final ComboBox siteComboBox = new ComboBox<PartCheck>();
					siteComboBox.getItems().setAll(getReceivingSites());
					siteComboBox.getSelectionModel().selectFirst();
					
					Label trailerLabel = UiFactory.createLabel("trailerLabel", "Trailer ",20);
					final LoggedTextField trailerTextField = UiFactory.createTextField("trailerText");
					
					LoggedButton productIdButton = UiFactory.createButton(ProductType.MBPN.name(), UiFactory.getIdle().getButtonFont(), true);
					final LoggedTextField productIdTextField = UiFactory.createTextField("productIdTextField", ProductNumberDef.BPN.getLength(),
							UiFactory.getIdle().getInputFont(), TextFieldState.EDIT, Pos.CENTER, true);
							
					LoggedButton okButton = UiFactory.createButton(" Ok ");
					LoggedButton cancelButton = UiFactory.createButton(" Cancel ");
					
					productIdButton.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
									"Manual Product Entry Dialog",getProductTypeData(),getMainWindow().getApplicationContext().getApplicationId());
							manualProductEntryDialog.showDialog();
							String productId = manualProductEntryDialog.getResultProductId();
							productIdTextField.setText(productId);
						}

						
					});
					
					cancelButton.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							closeMessage();
						}
					});
					
					okButton.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							String site = (String)siteComboBox.getSelectionModel().getSelectedItem();
							String trailerNo = trailerTextField.getText();
							String productId = productIdTextField.getText();
							createShipmentAndShipmentProduct(trailerNo,site, productId);				 
							 closeMessage();
						}

						
					});
					
					VBox vbox = new VBox();
					vbox.setPadding(new Insets(10, 20, 10, 20));
					vbox.setSpacing(10);
					
					HBox hbox = new HBox();
					hbox.setPadding(new Insets(5, 10, 5, 10));
					
					hbox.getChildren().add(siteLabel);
					hbox.getChildren().add(siteComboBox);
					
					HBox hbox1 = new HBox();
					hbox1.setPadding(new Insets(5, 10, 5, 10));
					
					hbox1.getChildren().add(trailerLabel);
					hbox1.getChildren().add(trailerTextField);
					
					HBox hbox2 = new HBox();
					hbox2.setPadding(new Insets(5, 10, 5, 10));
					
					hbox2.getChildren().add(productIdButton);
					hbox2.getChildren().add(productIdTextField);
					
					
					HBox hbox3 = new HBox();
					
					hbox3.setPadding(new Insets(25, 25, 5, 10));
					hbox3.getChildren().add(okButton);
					hbox3.getChildren().add(cancelButton);
					
					vbox.getChildren().add(hbox);
					vbox.getChildren().add(hbox1);
					vbox.getChildren().add(hbox2);
					vbox.getChildren().add(hbox3);
					

					Scene scene = new Scene(vbox);
					
					dialog.setScene(scene);
					dialog.centerOnScreen();
					dialog.sizeToScene();

					dialog.toFront();
					dialog.showAndWait();
		
	}
	
		private void removeShipmentProduct(MbpnProduct product) {
			PartShipment selectedPartShipment = partShipmentPanel.getTable().getSelectionModel().getSelectedItem();
			PartShipmentProduct partShipmentProduct = getPartShipmentProductDao().findByProductIdShipmentId(product.getProductId(), selectedPartShipment.getShipmentId() );
			Integer shipmentId = partShipmentProduct.getId().getShipmentId();
			partShipmentProduct.setBuildStatus(BuildStatus.REMOVE.getId());
			getPartShipmentProductDao().save(partShipmentProduct);
			
			PartShipment partShipment = getPartShipmentDao().findByKey(shipmentId);
			partShipment.setAssociateNo(getMainWindow().getApplicationContext().getUserId());
			partShipment.setSendStatus(0);
			ServiceFactory.getDao(PartShipmentDao.class).save(partShipment);
			
			loadPartShipmentProducts(partShipment);
			partShipmentPanel.getTable().getSelectionModel().select(partShipment);
		}
		
		private void addShipmentProduct() {
			ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
					"Manual Product Entry Dialog",getProductTypeData(),getMainWindow().getApplicationContext().getApplicationId());
			manualProductEntryDialog.showDialog();
			String productId = manualProductEntryDialog.getResultProductId();
			if (!StringUtils.isEmpty(productId)) {
				PartShipment partShipment = partShipmentPanel.getTable().getSelectionModel().getSelectedItem();
				if(isValid(productId,partShipment)){
					createShipmentProduct(productId,partShipment);
				}
				
			}
					
		}
		
		private void createShipmentAndShipmentProduct(String trailerNo,String site,String productId) {
			
			if (!StringUtils.isEmpty(productId)) {
				Timestamp now = new Timestamp(System.currentTimeMillis());
				PartShipment partShipment = new PartShipment();
				partShipment.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
				partShipment.setReceivingSite(site);
				partShipment.setSendStatus(0);
				partShipment.setTrailerNumber(trailerNo);
				partShipment.setSentTimestamp(now);
				partShipment.setCreateTimestamp(now);
				partShipment.setUpdateTimestamp(now);
				partShipment.setAssociateNo(getMainWindow().getApplicationContext().getUserId());
				if(isValid(productId,partShipment)){

					partShipment = ServiceFactory.getDao(PartShipmentDao.class).save(partShipment);
					
					PartShipmentProduct shipmentProduct = new PartShipmentProduct();
					PartShipmentProductId id = new PartShipmentProductId();
					id.setShipmentId(partShipment.getId());
					id.setProductId(productId);
					shipmentProduct.setId(id);
					shipmentProduct.setCreateTimestamp(now);
					shipmentProduct.setUpdateTimestamp(now);
					ServiceFactory.getDao(PartShipmentProductDao.class).save(shipmentProduct);
					datePicker.setValue(LocalDate.now());
					filterSiteComboBox.getSelectionModel().select(site);
					filterShipments();
					loadPartShipmentProducts(partShipment);
					partShipmentPanel.getTable().getSelectionModel().select(partShipment);
				}
				
			}
			
		}
			
		private boolean isValid(String productId, PartShipment partShipment) {
			BaseProduct product = checkProductOnServer(productId);
			if (product == null) {
				String msg = String.format("Product does not exist for: %s", productId);
				mainWin.setErrorMessage(msg);
				return false;
			}

			PartShipmentProduct partShipmentProduct = getPartShipmentProductDao().findByProductId(product.getProductId());
			if(partShipmentProduct != null) {
				String msg ="Part already shipped " + product.getProductId();
				mainWin.setErrorMessage(msg);
				return false;
			}
			 
			if(partShipmentProduct == null){
				boolean found= false;
				ObservableList<MbpnProduct> products = shipmentProductPanel.getTable().getItems();
				for(MbpnProduct mbpnProduct: products){
					if(mbpnProduct.getId().equalsIgnoreCase(productId)){
						found = true;
						break;
					}
				}
				
				if(found){
					String msg ="Part already Part of current shipment " + product.getProductId();
					mainWin.setErrorMessage(msg);
					return false;
				}
			}
			
			Map<String, Object> messages = validate(product);
			String msg = "FAILED PRODUCT CHECKS: -" ;
			if(!messages.isEmpty()){
				String key = messages.keySet().iterator().next();
				Object firstObject = messages.values().iterator().next();
				Object object = (firstObject instanceof List) ? ((List<?>) firstObject).get(0) : firstObject;
				mainWin.setErrorMessage(msg + key +"-"+(ObjectUtils.toString(object)));
				
				return false;
			}
			return true;
		}
		
		public BaseProduct checkProductOnServer(String vin) {
			try {
				
				return ProductTypeUtil.getTypeUtil(ProductType.MBPN).findProduct(vin);
			} catch (Exception e) {
				String msg = "failed to load " + ProductType.MBPN.name() + ": " + vin;
				Logger.getLogger().warn(e, msg);
				throw new TaskException(msg);
			}

		}
		
		public Map<String,Object> validate(BaseProduct product) {
			String[] checkTypes = getCheckTypes();
			String lastPassingProcessPointId = getProductCheckProcessPoint(product);
			ProcessPoint processPoint = null;
			if(StringUtils.isNotEmpty(lastPassingProcessPointId)){
				processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(lastPassingProcessPointId);
			}
			return ProductCheckUtil.check(product, processPoint, checkTypes);
		}
		
		private String[] getCheckTypes() {
			ProductCheckPropertyBean propertyBean= PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getMainWindow().getApplicationContext().getApplicationId());
			String[] productInputCheckTypes = propertyBean.getProductInputCheckTypes();
			List<String> types = new ArrayList<String>(Arrays.asList(productInputCheckTypes));
			
			return types.toArray(new String[types.size()]);
		}

		private void createShipmentProduct( String productId, PartShipment partShipment) {
			PartShipmentProductId id = new PartShipmentProductId();
			id.setProductId(productId);
			id.setShipmentId(partShipment.getId());
			PartShipmentProduct shipmentProduct  = getPartShipmentProductDao().findByKey(id);
			if(shipmentProduct == null){
				shipmentProduct = new PartShipmentProduct();
				shipmentProduct.setId(id);
				shipmentProduct.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				shipmentProduct.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			}else{
				shipmentProduct .setBuildStatus(null);
			}
			getPartShipmentProductDao().save(shipmentProduct);
			
			partShipment.setSendStatus(0);
			ServiceFactory.getDao(PartShipmentDao.class).save(partShipment);
			filterShipments();
			loadPartShipmentProducts(partShipment);
			partShipmentPanel.getTable().getSelectionModel().select(partShipment);
		}

		public ProductTypeData getProductTypeData() {
			if(this.productTypeData == null) {
				setProductTypeData(ProductType.MBPN.name());
			}
			return this.productTypeData;
		}
		
		public void setProductTypeData(String productType) {
			for (ProductTypeData type : getMainWindow().getApplicationContext().getProductTypeDataList()) {
				if (type.getProductTypeName().equals(productType)) {
					this.productTypeData = type;
					break;
				}
			}
		}

		private void loadPartShipmentProducts(PartShipment partShipment) {
			if(partShipment != null){
				List<MbpnProduct> products = getPartShipmentProductDao().findByShipmentId(partShipment.getShipmentId());
				shipmentProductPanel.setData(products);
			}
		}
		
		private PartShipmentProductDao getPartShipmentProductDao(){
			return ServiceFactory.getDao(PartShipmentProductDao.class);
		}
		
		private PartShipmentDao getPartShipmentDao(){
			return ServiceFactory.getDao(PartShipmentDao.class);
		}
		
		private void changeReceivingSiteShipmentDialog(){
			// Create the custom dialog.
			dialog = new Stage();
			dialog.setTitle("Shipment Dialog");
			dialog.initStyle(StageStyle.DECORATED);
			dialog.initModality(Modality.APPLICATION_MODAL);
		
			dialog.setWidth(400);
			Label siteLabel = UiFactory.createLabel("siteLabel", "Site ",20);
			final ComboBox siteComboBox = new ComboBox<PartCheck>();
			siteComboBox.getItems().setAll(getReceivingSites());
			siteComboBox.getSelectionModel().selectFirst();
			
			Label trailerLabel = UiFactory.createLabel("trailerLabel", "Trailer ",20);
			final LoggedTextField trailerTextField = UiFactory.createTextField("trailerText");
					
			LoggedButton okButton = UiFactory.createButton(" Ok ");
			LoggedButton cancelButton = UiFactory.createButton(" Cancel ");
			
			cancelButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					closeMessage();
				}
			});
			
			okButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					String site = (String)siteComboBox.getSelectionModel().getSelectedItem();
					String trailerNo = trailerTextField.getText();
					updatePartShipmentReceivingSite(site, trailerNo);				 
					 closeMessage();
				}

				
			});
			
			VBox vbox = new VBox();
			vbox.setPadding(new Insets(10, 20, 10, 20));
			vbox.setSpacing(10);
			
			HBox hbox = new HBox();
			hbox.setPadding(new Insets(5, 10, 5, 10));
			
			hbox.getChildren().add(siteLabel);
			hbox.getChildren().add(siteComboBox);
			
			HBox hbox1 = new HBox();
			hbox1.setPadding(new Insets(5, 10, 5, 10));
			
			hbox1.getChildren().add(trailerLabel);
			hbox1.getChildren().add(trailerTextField);
			
			
			HBox hbox2 = new HBox();
			
			hbox2.setPadding(new Insets(25, 10, 5, 10));
			hbox2.getChildren().add(okButton);
			hbox2.getChildren().add(cancelButton);
			
			vbox.getChildren().add(hbox);
			vbox.getChildren().add(hbox1);
			vbox.getChildren().add(hbox2);
			

			Scene scene = new Scene(vbox);
			
			dialog.setScene(scene);
			dialog.centerOnScreen();
			dialog.sizeToScene();

			dialog.toFront();
			dialog.showAndWait();
		}
		
		private List<String> getReceivingSites(){
			String receivingSite = PropertyService.getProperty(getApplicationId(), "SHIPPING_SITES");
			List<String> sites =  new ArrayList<String>();
			sites.addAll(Arrays.asList(receivingSite.split(",")));
			
			return sites;
		}
		
		public void closeMessage() {
			dialog.close();
		}
		
		private void updatePartShipmentReceivingSite(String site, String trailerNo) {
			PartShipment partShipment = partShipmentPanel.getTable().getSelectionModel().getSelectedItem();
			if(StringUtils.isNotEmpty(trailerNo))partShipment.setTrailerNumber(trailerNo);
			partShipment.setReceivingSite(site);
			partShipment.setAssociateNo(getMainWindow().getApplicationContext().getUserId());
			partShipment.setSendStatus(0);
			ServiceFactory.getDao(PartShipmentDao.class).save(partShipment);
			filterShipments();
			partShipmentPanel.getTable().getSelectionModel().select(partShipment);
			
		}
		
		private void markShipmentUnsent(PartShipment partShipment) {
			partShipment.setSendStatus(0);
			ServiceFactory.getDao(PartShipmentDao.class).save(partShipment);
			filterShipments();
			partShipmentPanel.getTable().getSelectionModel().select(partShipment);
		}
		
		private String getProductCheckProcessPoint(BaseProduct product){
			ProductPropertyBean productPropertyBean = PropertyService.getPropertyBean(ProductPropertyBean.class, getApplicationId());
			PartShipment partShipment = partShipmentPanel.getTable().getSelectionModel().getSelectedItem();
			String site = partShipment!= null?partShipment.getReceivingSite():null;
			Map<String,String> siteAllowPartialProductShipment = productPropertyBean.getAllowPartialBuild();
								 
			if(!StringUtils.isEmpty(site) && siteAllowPartialProductShipment != null){
				String val = siteAllowPartialProductShipment.get(site);
				if(StringUtils.isNotEmpty(val) && Boolean.parseBoolean(val.trim())){
					Map<String,String> partialCheckPP = productPropertyBean.getPartialCheckPp();
							
					if(partialCheckPP != null){
						return partialCheckPP.get(product.getLastPassingProcessPointId());
					}else{
						return null;
					}
				}else{
					return productPropertyBean.getFinalCheckPp();
							
				}
			}
				
			return null;
		}
}
