package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.utils.ProductTypeUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>DataRecoveryFrame</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * 
 * <pre>
 * Configuration: database properties:
 * 
 * 1. Add tab pane to the client
 * RECOVERY_CONFIG{CONFIG_ID}={configId:configId,title:"TITLE",productType:
 * PRODUCT_TYPE,keyEvent:KEY_EVENT,partDefinitions:[{P1},{P2},{P3}]} CONFIG_ID -
 * user assigned config name(id) or product model code for model base
 * configuration title - user assigned title to be displayed on tab keyEvent -
 * optional character value, shortcut key to swith to tab pane partDefinitions -
 * references to part to be included and that are defined under RECOVERY_PART
 * property
 * 
 * 2. Define parts
 * 
 * RECOVERY_PART{P1}={name:"NAME",label:"LABEL",elementType:VALUE,editable:true,
 * multiParts:[{P5},{P6}]}
 * 
 * P1 - key, that part can be refered by in configuration name: part name that
 * is stored in database label: label to be displayed - optional
 * multiParts:additional parts that can be included for complex layout (multiple
 * parts at one row) - optional. elementType:STATUS|VALUE|STATUS_VALUE
 * 
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>Jul 17, 2017</TD>
 * <TD>1.0.0</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class DataRecoveryView extends MainWindow {

	public DataRecoveryView(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		setCenter(createContentPanel());
	}

	protected void initialize() {
	}

	protected Node createContentPanel() {

		List<ProductRecoveryConfig> configs = ProductRecoveryConfigProvider.createProductRecoveryConfigs(getApplication().getApplicationId());

		if (configs == null) {
			configs = new ArrayList<ProductRecoveryConfig>();
		}

		Map<ProductType, List<String>> modelCodes = new HashMap<ProductType, List<String>>();
		List<ProductRecoveryConfig> singleConfigs = new ArrayList<ProductRecoveryConfig>();
		Map<ProductType, List<ProductRecoveryConfig>> modelBasedConfigs = new HashMap<ProductType, List<ProductRecoveryConfig>>();

		for (ProductRecoveryConfig prc : configs) {
			if (prc == null || prc.getProductType() == null) {
				continue;
			}
			ProductType productType = prc.getProductType();

			List<String> models = modelCodes.get(productType);
			if (models == null) {
				models = ProductTypeUtil.getProductSpecDao(productType).findAllModelCodes(productType.name());
				if (models == null) {
					models = new ArrayList<String>();
				}
				modelCodes.put(productType, models);
			}
			if ("*".equals(prc.getConfigId()) || models.contains(prc.getConfigId())) {
				List<ProductRecoveryConfig> list = modelBasedConfigs.get(productType);
				if (list == null) {
					list = new ArrayList<ProductRecoveryConfig>();
					modelBasedConfigs.put(productType, list);
				}
				list.add(prc);
			} else {
				singleConfigs.add(prc);
			}
		}

		List<ProductRecoveryPanel> screens = new ArrayList<ProductRecoveryPanel>();
		List<ProductRecoveryPanel> singleConfigScreens = getScreens(singleConfigs);
		List<ProductRecoveryPanel> modelBaseScreens = getScreens(modelBasedConfigs);
		if (singleConfigScreens != null) {
			screens.addAll(singleConfigScreens);
		}
		if (modelBaseScreens != null) {
			screens.addAll(modelBaseScreens);
		}

		Node panel = null;
		if (screens.isEmpty()) {
			return new BorderPane();
		} else if (screens.size() == 1) {
			panel = screens.get(0);
		} else {
			panel = createTabbedContentPanel(screens);
		}
		return panel;
	}

	protected Node createTabbedContentPanel(List<ProductRecoveryPanel> screens) {
		TabPane tabbedPane = new TabPane();
		for (ProductRecoveryPanel screen : screens) {
			Tab tab = new Tab(screen.getScreenName());
			tab.setContent(screen);
			tabbedPane.getTabs().add(tab);
		}
		tabbedPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		tabbedPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				clearMessage();
			}
		});

		return tabbedPane;
	}

	@SuppressWarnings("unused")
	protected List<ProductRecoveryPanel> getScreens(List<ProductRecoveryConfig> configs) {
		List<ProductRecoveryPanel> screens = new ArrayList<ProductRecoveryPanel>();
		if (configs == null) {
			return screens;
		}
		for (ProductRecoveryConfig config : configs) {
			if (config == null || config.getProductType() == null) {
				continue;
			}
			ProductRecoveryPanel screen = new ProductRecoveryPanel(this, config.getProductType(), config);
			if (screen == null) {
				continue;
			}
			screens.add(screen);
		}
		return screens;
	}

	@SuppressWarnings("unused")
	protected List<ProductRecoveryPanel> getScreens(Map<ProductType, List<ProductRecoveryConfig>> configMap) {
		List<ProductRecoveryPanel> screens = new ArrayList<ProductRecoveryPanel>();
		if (configMap == null || configMap.isEmpty()) {
			return screens;
		}
		for (ProductType productType : configMap.keySet()) {
			List<ProductRecoveryConfig> configs = configMap.get(productType);
			if (configs == null || configs.isEmpty()) {
				continue;
			}
			ProductRecoveryPanel screen = new ProductRecoveryPanel(this, productType, configs);
			if (screen == null) {
				continue;
			}
			screens.add(screen);
		}
		return screens;
	}
}
