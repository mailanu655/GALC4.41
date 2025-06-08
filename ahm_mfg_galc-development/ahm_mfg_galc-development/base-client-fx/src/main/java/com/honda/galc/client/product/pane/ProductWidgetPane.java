package com.honda.galc.client.product.pane;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.widget.HolderWidget;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * 
 * <h3>ProductWidgetPane Class description</h3>
 * <p> ProductWidgetPane description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 7, 2014
 *
 *
 */
public class ProductWidgetPane extends ApplicationMainPane{
	
	private ProductController productController;
	
	private TabPane tabPane;
    
	public ProductWidgetPane(ProductController productController) {
		super(productController.getView().getMainWindow());
		this.productController = productController;
		initComponents();
	}
	
	
	public ProductController getProductController() {
		return productController;
	}


	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	private TabPane getTabPane() {
		return tabPane;
	}
	
	private void initComponents() {
		
		tabPane = createTabPane();
		
		loadWidgets(productController.getModel().getProperty().getWidgets());
		
	}
		
		public void loadWidgets(String... args) {
			if (args == null) return;
			
			List<AbstractWidget> widgets = new ArrayList<AbstractWidget> ();
			for (String name : args) {
				if (StringUtils.isEmpty(name)) continue;
				AbstractWidget view = null;
				String tabName ="";
				
				String subNames[] = StringUtils.split(name, "+");
				if(subNames.length == 1){
					view = createWidget(name);
					if(view != null) {
						tabName = view.getViewLabel();
					}
				}else {
					List<AbstractWidget> subWidgets = new ArrayList<AbstractWidget> ();
					for(String subName : subNames) {
						if (StringUtils.isEmpty(subName)) continue;
						AbstractWidget widget = createWidget(subName);
						if(widget == null) continue;
						tabName += " " + widget.getViewId().getViewLabel();
						subWidgets.add(widget);
					}
					if(!subWidgets.isEmpty()){
						view = new HolderWidget(getProductController(),subWidgets);
					}
				}
				if(view != null) {
					widgets.add(view);
					Tab tab = new Tab(StringUtils.trim(tabName));
					tab.setContent(view);
					getTabPane().getTabs().add(tab);
				}
			}
			
			if(widgets.size() == 1) setCenter(widgets.get(0));
			else setCenter(getTabPane());
		}

		
		protected TabPane createTabPane() {
			TabPane pane = new TabPane();
			pane.setMinSize(200, 200);
			pane.requestFocus();
			return pane;
		}
		
		public AbstractWidget createWidget(String widgetName) {
			widgetName = widgetName.trim();
			Class<?> viewClass = ViewId.getViewClass(widgetName);
			if(viewClass == null){
				setErrorMessage("Widget class does not exist : " + widgetName);
				return null;
			}else {
				if(AbstractWidget.class.isAssignableFrom(viewClass))
					return (AbstractWidget)ReflectionUtils
						.createInstance(viewClass,new Object[] {getProductController()});
				else {
					setErrorMessage("Widget class cannot be created : " + widgetName);
				}
			}
			
			return null;
		}
		
		public boolean hasWidgets() {
			return !tabPane.getTabs().isEmpty();
		}
}
