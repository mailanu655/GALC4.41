package com.honda.galc.client.product.pane;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.util.ReflectionUtils;

/**
* 
 * 
 * <h3>ProductIdlePaneContainer Class description</h3>
* <p> ProductIdlePaneContainer description </p>
* 
 * <h4>Change History</h4>
* <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
* <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
* <TH>Update by</TH>
* <TH>Update date</TH>
* <TH>Version</TH>
* </TR>
*
* </TABLE>
*   
 * @author Rakesh<br>
* Oct 30, 2017
*
*
*/
public class ProductIdlePaneContainer extends BorderPane {

	private ProductController productController;
	private List<AbstractProductIdlePane> productIdlePanes = new ArrayList<AbstractProductIdlePane>();

	public ProductIdlePaneContainer(ProductController productController) {
		this.productController = productController;
		initComponents();
	}
	
	protected void initComponents() {
		String[] productIdlePanels = getProductController().getModel().getProperty().getProductIdlePane();
		if(productIdlePanels.length == 1) {
			AbstractProductIdlePane productIdlePane = createProductIdlePane(productIdlePanels[0]);
			productIdlePanes.add(productIdlePane);
			setCenter(productIdlePane);
		} else {
			createTabbedPane(productIdlePanels);
		}
	}
	
	protected void createTabbedPane(String[] productIdlePanels) {
		TabPane tabPane = createTabPane();
		if (productIdlePanels == null || productIdlePanels.length == 0)
			return;
		loadTabs(tabPane, productIdlePanels);
		setCenter(tabPane);
	}
	
	public void loadTabs(TabPane tabPane, String... args) {
		if (args == null)
			return;
		for (String name : args) {
			if (StringUtils.isEmpty(name))
				continue;
			AbstractProductIdlePane idlePane = createProductIdlePane(name);
			Tab tab = new Tab(idlePane.getName());
			productIdlePanes.add(idlePane);
			tab.setContent(idlePane);
			tabPane.getTabs().add(tab);
		}
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
	
	protected TabPane createTabPane() {
		TabPane pane = new TabPane();
		pane.setMinSize(200, 200);
		pane.requestFocus();
		return pane;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private AbstractProductIdlePane createProductIdlePane(String className) {
		Class clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			getProductController().getLogger().error("Class " + className + " not found");
			return null;
		}
		return (AbstractProductIdlePane)ReflectionUtils.createInstance(clazz,
				new Object[] { getProductController() });
	}


	public ProductController getProductController() {
		return productController;
	}

	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	public List<AbstractProductIdlePane> getProductIdlePanes() {
		return productIdlePanes;
	}

	public void setProductIdlePanes(List<AbstractProductIdlePane> productIdlePanes) {
		this.productIdlePanes = productIdlePanes;
	}
	
	public void toIdle() {
		for (AbstractProductIdlePane productIdlePane : productIdlePanes) {
			productIdlePane.toIdle();
		}
	}

}

