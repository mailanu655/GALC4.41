package com.honda.galc.client.product.pane;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.PropertyDef;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.BeanUtils;
import com.honda.galc.util.ProductSpecUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import com.honda.galc.data.ProductType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public class BulkProductInfoPane extends ProductInfoPane {

	ObjectTablePane<BaseProduct> productTablePane;

	public BulkProductInfoPane(BulkProductController productController) {
		super(productController);
	}

	@Override
	public void initView(ProductTypeData productTypeData) {
		productTablePane = createProductTablePane();
		add(productTablePane, "dock west");
		addRowSelectionListner();
		super.initView(productTypeData);
	}

	private ObjectTablePane<BaseProduct> createProductTablePane() {
		String productId = "productId";
		if(ProductTypeUtil.isDieCast(getProductController().getProductTypeData().getProductType())) {
			boolean isDcStation = PropertyService.getPropertyBean(QiPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).isDcStation();
			if(isDcStation) {
				 productId = "dcSerialNumber";
			}else productId = "mcSerialNumber";
		}
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", productId);
		productTablePane = new ObjectTablePane<>(columnMappingList, new Double[] {0.123});

		if(getProductController() instanceof BulkProductController) {
			productTablePane.setPadding(new Insets(0,5,0,0));
			productTablePane.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.007 * Screen.getPrimary().getVisualBounds().getWidth())));
			productTablePane.setMaxWidth((int)(0.15 * Screen.getPrimary().getVisualBounds().getWidth()));
			productTablePane.setMaxHeight((int)(0.115 * Screen.getPrimary().getVisualBounds().getHeight()));

			LoggedTableColumn<BaseProduct, Integer> column = new LoggedTableColumn<BaseProduct, Integer>();
			createSerialNumber(column);
			productTablePane.getTable().getColumns().add(0, column);
			productTablePane.getTable().getColumns().get(0).setText("#");
			productTablePane.getTable().getColumns().get(0).setResizable(true);
			productTablePane.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * Screen.getPrimary().getVisualBounds().getWidth()));
			productTablePane.getTable().getColumns().get(0).setMinWidth(30);
			productTablePane.setEditable(false);
			productTablePane.setConstrainedResize(false);
			productTablePane.getTable().setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			productTablePane.setSelectionMode(SelectionMode.SINGLE);
		}		
		return productTablePane;
	}

	protected void addRowSelectionListner() {
		productTablePane.getTable().getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<BaseProduct>() {

			@Override
			public void changed(ObservableValue<? extends BaseProduct> observable,
					BaseProduct oldValue, BaseProduct newValue) {

				BaseProduct product = productTablePane.getTable().getSelectionModel().getSelectedItem();
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("product", product);
				setSelectedProduct(model);
				Tab currentTab = getProductController().getView().getProductProcessPane().getTabPane().getSelectionModel().getSelectedItem();
				if(currentTab != null && !(currentTab.getText().equals(ViewId.DEFECT_ENTRY.getViewLabel()) || currentTab.getText().equals(ViewId.REPAIR_ENTRY.getViewLabel())))  {
					getProductController().getModel().setProduct(product);
					getProductController().getView().getProductProcessPane().prepareProcessViews(product);
					getProductController().getView().getProductProcessPane().updateSelectedView();
				}

			}
		});
	}
	
	@Override
	public void setProductButtons(ProductActionId[] actionIds) {
		setProductButtons(actionIds, true);
	}
	
	@Override
	public void setInfo(Map<String, ?> model) {
		if(model != null ) {
			ObservableList<BaseProduct> products =   FXCollections.observableArrayList(getProductController().getModel().getProcessedProducts());

			productTablePane.getTable().setItems(products);
			if(!products.isEmpty()) {
				productTablePane.getTable().getSelectionModel().select(0);
			}
		}

		setSelectedProduct(model);
	}

	private void setSelectedProduct(Map<String, ?> model) {
		for (PropertyDef def : getInfoTextFields().keySet()) {
			Object value = BeanUtils.getNestedPropertyValue(model, def.getPropertyPath());
			String str = def.toString(value);
			TextField field = getInfoTextFields().get(def);
			if(StringUtils.equalsIgnoreCase(def.getHeader(), "YMTO")) {
				if (getProductController().getModel().getProductType().equals(ProductType.FRAME.toString()) || getProductController().getModel().getProductType().equals(ProductType.ENGINE.toString())) {
				str = ProductSpecUtil.extractModelYearCode(str)+ProductSpecUtil.extractModelCode(str)+ Delimiter.HYPHEN +ProductSpecUtil.extractModelTypeCode(str)+ Delimiter.HYPHEN +ProductSpecUtil.extractModelOptionCode(str)+ 
						Delimiter.HYPHEN +ProductSpecUtil.extractExtColorCode(str)+ Delimiter.HYPHEN +ProductSpecUtil.extractIntColorCode(str);
				}
			}
			field.setText(str);
			if (isShowSequenceNumber() && str != null && def.getHeader().equals("VIN")){  
				if (getProductController().getModel().getProductType().equals("FRAME")) {
					Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(str);
					Integer sequence = frame.getAfOnSequenceNumber();				   

					if (sequence != null) {
						getSequenceLabel().setText(sequence.toString());  
					}
					else {
						getSequenceLabel().setText("N/A");
					}
				}
			}
		}		
	}

	public Pane getProductTablePane() {
		return productTablePane;
	}
}