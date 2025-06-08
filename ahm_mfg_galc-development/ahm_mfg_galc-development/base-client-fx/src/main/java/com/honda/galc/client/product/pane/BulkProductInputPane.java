package com.honda.galc.client.product.pane;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.AccessControlManager;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.ui.ElevatedLoginDialog;
import com.honda.galc.client.ui.ElevatedLoginResult;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Screen;

public class BulkProductInputPane extends AbstractProductInputPane  {
	private ProductScanPane productScanPane;
	private SearchByProcessPane searchByProcessPane;
	private SearchByDunnagePane searchByDunnagePane;
	private SearchByTransactionIdPane searchByTransactionIdPane;
	private SearchByRepairAreaPane searchByRepairAreaPane;
	private SearchByProductFilterPane searchByProductFilterPane;

	public double screenWidth;
	public double screenHeight;

	public BulkProductInputPane(BulkProductController productController) {
		super(productController);
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

		initView();
		mapActions();
		EventBusUtil.register(this);
	}

	private void mapActions() {
		if (getProductController().getModel().getProperty().isManualProductEntryEnabled()) {

			getProductScanPane().getProductIdButton().setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(final ActionEvent arg0) {
					getProductController().setKeyboardPopUpVisible(false);
					ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
							"Manual Product Entry Dialog", getProductController().getProductTypeData(),getProductController().getView().getMainWindow().getApplicationContext().getApplicationId());
					Logger.getLogger().check("Manual Product Entry Dialog Box populated");
					manualProductEntryDialog.showDialog();
					String productId = manualProductEntryDialog.getResultProductId();
					Logger.getLogger().check("Product Id : " + productId + " selected");
					if (!(productId == null || StringUtils.isEmpty(productId))) {
						getProductController().getView().getMainWindow().setWaitCursor();
						setProductId(productId);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (getProductController().getModel().getProperty().isAutoEnteredManualProductInput()) {
									getProductIdField().fireEvent(arg0);
									Logger.getLogger().check("Selected Product Id Auto Entered");
								}
								getProductController().getView().getMainWindow().setDefaultCursor();
							}
						});
					}
				}

			});
		}	
		
		getProductInputTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
				// Add the Elevated User Function to Screens
				Map<String, String> elevatedSecurityGroupMap = getProductController().getModel().getProperty().getElevatedSecurityGroupFor();
				if (elevatedSecurityGroupMap != null) {
					String elevatedSecurityGroup = elevatedSecurityGroupMap.get(getProductInputTabPane().getSelectionModel().getSelectedItem().getId());
					if (!StringUtil.isNullOrEmpty(elevatedSecurityGroup) && elevatedSecurityGroup != "" ) {
						boolean isPasswordRequired = PropertyService
								.getPropertyBean(ProductCheckPropertyBean.class, getProductController().getView()
										.getMainWindow().getApplicationContext().getApplicationId())
								.isElevatedUserPasswordRequired();
						String reason = "Current user don't have access to this tab. Elevated user sign-on required to continue processing";
	
						String currentUser = getProductController().getView().getMainWindow().getApplicationContext().getUserId();
						//check if current user has access to the selected pane- yes: display the tab / No: ask to login
						LoginStatus loginStatus = AccessControlManager.getInstance().verifyLDAPUser(currentUser, null,
								elevatedSecurityGroup);
						if (loginStatus != LoginStatus.OK) {
							ElevatedLoginResult elevatedLoginResult = ElevatedLoginDialog.login(
									ClientMainFx.getInstance()
											.getStage(getProductController().getView().getMainWindow()
													.getApplicationContext().getApplicationId()),
									reason, isPasswordRequired, elevatedSecurityGroup);
	
							if (elevatedLoginResult.isSuccessful()) {
								Logger.getLogger().info(
										"Elevated User (" + elevatedLoginResult.getUserId() + ") logged in successfully");
							} else {
								Logger.getLogger().info("Elevated User (" + elevatedLoginResult.getUserId()
										+ ") failed to log in due to " + elevatedLoginResult.getMessage());
								getProductInputTabPane().getSelectionModel().select(0);
							}
						} 
					} 
				}
			}
		});
	}

	private void initView() {		
		productInputTabPane = new TabPane();
		productInputTabPane.setPrefHeight(screenHeight * 0.115);
		getProductController().setInputPaneHeight((int) productInputTabPane.getPrefHeight());
		
		productScanPane = getProductScanPane();
		productScanPane.setStyle((int) productInputTabPane.getPrefHeight());
		searchByProcessPane = getSearchByProcessPane();
		searchByProductFilterPane = getSearchByProductFilterPane();
		searchByDunnagePane = getSearchByDunnagePane();
		searchByTransactionIdPane = getSearchByTransactionIdPane();
		searchByRepairAreaPane = getSearchByRepairAreaPane();
		String[] paneIds = getProductController().getModel().getProperty().getSearchByPanes();
		for(String paneId:paneIds) {
			if(StringUtils.equals(paneId,PaneId.PRODUCT_SCAN_PANE.toString())) productInputTabPane.getTabs().add(getProductScanTab());
			if(StringUtils.equals(paneId,PaneId.SEARCH_BY_PROCESS_PANE.toString())) productInputTabPane.getTabs().add(getSearchByProcessTab());
			if(StringUtils.equals(paneId,PaneId.SEARCH_BY_DUNNAGE_PANE.toString())) productInputTabPane.getTabs().add(getSearchByDunnageTab());
			if(StringUtils.equals(paneId,PaneId.SEARCH_BY_TRANSACTION_PANE.toString())) productInputTabPane.getTabs().add(getSearchByTransactionIdTab());
			if(StringUtils.equals(paneId,PaneId.SEARCH_BY_REPAIR_AREA_PANE.toString())) productInputTabPane.getTabs().add(getSearchByRepairAreaTab());
			if(StringUtils.equals(paneId,PaneId.SEARCH_BY_PRODUCT_FILTER_PANE.toString())) productInputTabPane.getTabs().add(getSearchByProductFilterTab());
		}
		
		productInputTabPane.setPrefHeight(screenHeight * 0.15);
		productInputTabPane.setStyle("-fx-border-color: white, grey; -fx-border-width: 2, 1;");

		this.add(productInputTabPane);
	}

	private Tab getProductScanTab() {
		Tab tab = new Tab();
		tab.setText(PaneId.PRODUCT_SCAN_PANE.getPaneLabel());
		tab.setStyle(getLabelStyleSmaller());
		tab.setId(PaneId.PRODUCT_SCAN_PANE.toString());
		tab.setClosable(false);
		tab.setContent(productScanPane);
		return tab;
	}

	private Tab getSearchByProcessTab() {
		Tab tab = new Tab();
		tab.setText(PaneId.SEARCH_BY_PROCESS_PANE.getPaneLabel());
		tab.setStyle(getLabelStyleSmaller());
		tab.setId(PaneId.SEARCH_BY_PROCESS_PANE.toString());
		tab.setClosable(false);
		tab.setContent(searchByProcessPane);
		return tab;
	}

	private Tab getSearchByDunnageTab() {
		Tab tab = new Tab();
		tab.setText(PaneId.SEARCH_BY_DUNNAGE_PANE.getPaneLabel());
		tab.setStyle(getLabelStyleSmaller());
		tab.setId(PaneId.SEARCH_BY_DUNNAGE_PANE.toString());
		tab.setClosable(false);
		tab.setContent(searchByDunnagePane);
		return tab;
	}

	private Tab getSearchByTransactionIdTab() {
		Tab tab = new Tab();
		tab.setText(PaneId.SEARCH_BY_TRANSACTION_PANE.getPaneLabel());
		tab.setStyle(getLabelStyleSmaller());
		tab.setId(PaneId.SEARCH_BY_TRANSACTION_PANE.toString());
		tab.setClosable(false);
		tab.setContent(searchByTransactionIdPane);
		return tab;
	}
	
	private Tab getSearchByRepairAreaTab() {
		Tab tab = new Tab();
		tab.setText(PaneId.SEARCH_BY_REPAIR_AREA_PANE.getPaneLabel());
		tab.setStyle(getLabelStyleSmaller());
		tab.setId(PaneId.SEARCH_BY_REPAIR_AREA_PANE.toString());
		tab.setClosable(false);
		tab.setContent(searchByRepairAreaPane);
		return tab;
	}

	private Tab getSearchByProductFilterTab() {
		Tab tab = new Tab();
		tab.setText(PaneId.SEARCH_BY_PRODUCT_FILTER_PANE.getPaneLabel());
		tab.setStyle(getLabelStyleSmaller());
		tab.setId(PaneId.SEARCH_BY_PRODUCT_FILTER_PANE.toString());
		tab.setClosable(false);
		tab.setContent(searchByProductFilterPane);
		return tab;
	}

	@Override
	public TextField getProductIdField() {
		return getProductScanPane().getProductIdField();
	}

	@Override
	public TextInputControl getExpectedProductIdField() {
		return null;
	}

	@Override
	public void setProductSequence() {
	}

	@Override
	public LoggedTextField getQuantityTextField() {
		return null;
	}
	


	public ProductScanPane getProductScanPane() {
		return productScanPane == null ? productScanPane = new ProductScanPane(getProductController()) : productScanPane;
	}

	public SearchByProcessPane getSearchByProcessPane() {
		return searchByProcessPane == null ? searchByProcessPane = new SearchByProcessPane(getProductController()) : searchByProcessPane;
	}

	public SearchByDunnagePane getSearchByDunnagePane() {
		return searchByDunnagePane == null ? searchByDunnagePane = new SearchByDunnagePane(getProductController()) : searchByDunnagePane;
	}

	public SearchByTransactionIdPane getSearchByTransactionIdPane() {
		return searchByTransactionIdPane == null ? searchByTransactionIdPane = new SearchByTransactionIdPane(getProductController()) : searchByTransactionIdPane;
	}
	
	public SearchByRepairAreaPane getSearchByRepairAreaPane() {
		return searchByRepairAreaPane == null ? searchByRepairAreaPane = new SearchByRepairAreaPane(getProductController()) : searchByRepairAreaPane;
	}

	public SearchByProductFilterPane getSearchByProductFilterPane() {
		return searchByProductFilterPane == null ? searchByProductFilterPane = new SearchByProductFilterPane(getProductController(), this) : searchByProductFilterPane;
	}

	public  String  getLabelStyleSmaller() {
		return String.format("-fx-font-weight: bold; -fx-font-size: %dpx;", (int)(0.007 * screenWidth));
	}

	protected BulkProductController getProductController() {
		return (BulkProductController) productController;
	}

}
