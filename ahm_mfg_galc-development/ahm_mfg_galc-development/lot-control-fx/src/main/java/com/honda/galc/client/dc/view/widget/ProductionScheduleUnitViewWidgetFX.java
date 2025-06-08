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
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class ProductionScheduleUnitViewWidgetFX extends AbstractWidget {

	private static final long serialVersionUID = 1L;
	private int numberOfRows;

	public ProductionScheduleUnitViewWidgetFX(
			ProductController productController) {
		super(ViewId.PRODUCTION_SCHEDULE_UNITVIEWWIDGETFX, productController);
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
		// setExpectedProductId(productModel.getExpectedProductId());
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.setCenter(getunitViewList(getProductController().getModel()
				.getProductId()));
	}

	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	private TableView<UnitViewListData> getunitViewList(String ProductID) {
		// get parameter from property
		PDDAPropertyBean property = PropertyService
				.getPropertyBean(PDDAPropertyBean.class);
		String width = property.getProductionScheduleUnitWidth();
		String height = property.getProductionScheduleUnitViewWidgetHeight();
		String fontsize = property
				.getProductionScheduleUnitViewWidgetFontSize();
		numberOfRows = Integer.parseInt(property
				.getProductionScheduleUnitViewWidgetRows());

		TableView<UnitViewListData> unitViewList = UiFactory.createTableView(UnitViewListData.class);
		unitViewList
				.getStylesheets()
				.add("resource/com/honda/galc/client/dc/view/ProductionScheduleUnitViewWidgetFX.css");
		unitViewList.setEditable(false);
		unitViewList.setPrefWidth(Integer.parseInt(width));
		unitViewList.setMaxWidth(Integer.parseInt(width));
		unitViewList.setPrefHeight(Integer.parseInt(height));
		unitViewList.setMaxHeight(Integer.parseInt(height));
		unitViewList
				.setRowFactory(new Callback<TableView<UnitViewListData>, TableRow<UnitViewListData>>() {
					// @Override
					public TableRow<UnitViewListData> call(
							TableView<UnitViewListData> tableView) {
						final TableRow<UnitViewListData> row = new TableRow<UnitViewListData>() {
							@Override
							protected void updateItem(UnitViewListData item,
									boolean empty) {
								super.updateItem(item, empty);

								if (item != null
										&& item.getColor().equals("yellow")) {
									getStyleClass().add("currentvin");
								} else if (item != null
										&& item.getColor().equals("green")) {
									getStyleClass().add("mtocchange");

								}
							}
						};
						return row;
					}
				});

		// Frame and Engine display different content
		String productType = getProductController().getModel().getProductType();
		if ("FRAME".equals(productType))
			unitViewList.getColumns().addAll(
					makeStringColumn("MODEL TYPE", "model",
							(int) (Integer.parseInt(width) * 0.5), fontsize,
							null),
					makeStringColumn("SERIAL", "productid",
							(int) (Integer.parseInt(width) * 0.5), fontsize,
							null));
		if ("ENGINE".equals(productType))
			unitViewList.getColumns().addAll(
					makeStringColumn("MODEL TYPE", "model",
							(int) (Integer.parseInt(width) * 0.5), fontsize,
							null),
					makeStringColumn("SERIAL", "productid",
							(int) (Integer.parseInt(width) * 0.5), fontsize,
							null));

		unitViewList
				.setItems(getUnitViewListForProductID(ProductID, productType));

		return unitViewList;
	}

	private TableColumn<UnitViewListData, String> makeStringColumn(
			String columnName, String propertyName, int prefWidth,
			final String fontsize, final TextAlignment a) {
		TableColumn<UnitViewListData, String> column = UiFactory.createTableColumn(UnitViewListData.class, String.class, columnName);
		column.setCellValueFactory(new PropertyValueFactory<UnitViewListData, String>(
				propertyName));

		column.setCellFactory(new Callback<TableColumn<UnitViewListData, String>, TableCell<UnitViewListData, String>>() {
			// @Override
			public LoggedTableCell<UnitViewListData, String> call(
					TableColumn<UnitViewListData, String> param) {

				final LoggedTableCell<UnitViewListData, String> cell = new LoggedTableCell<UnitViewListData, String>() {
					private Text text;

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							setText(item.toString());
							setStyle("-fx-font-size: " + fontsize + ";");
						}
					}
				};
				if (null != a)
					cell.setAlignment(Pos.CENTER_RIGHT);
				return cell;
			}
		});
		if (prefWidth != 0)
			column.setPrefWidth(prefWidth);
		column.setSortable(false);
		return column;
	}

	private ObservableList<UnitViewListData> getUnitViewListForProductID(
			String productID, String productType) {

		ObservableList<UnitViewListData> tc = FXCollections
				.observableArrayList();
		


		List<Map<String, String>> unitViewListFromDb = ServiceFactory
				.getService(InProcessProductDao.class).getVinSequenceVIOS(
						productID, numberOfRows, productType);
		for (Map<String, String> UnitViewObj : unitViewListFromDb) {
			tc.add(new UnitViewListData(UnitViewObj.get("productid"),
					UnitViewObj.get("mto"), UnitViewObj.get("color")));
		}
		return tc;
	}

	public static class UnitViewListData {
		private SimpleStringProperty productid;
		private SimpleStringProperty model;
		private SimpleStringProperty color;

		public String getColor() {
			return color.get();
		}

		public void setColor(String color) {
			this.color = new SimpleStringProperty(color);
		}

		public String getModel() {
			return model.get();
		}

		public void setModel(String model) {
			this.model = new SimpleStringProperty(model);
		}

		public UnitViewListData(String productid, String model, String color) {
			this.productid = new SimpleStringProperty(productid);
			this.model = new SimpleStringProperty(model);
			this.color = new SimpleStringProperty(color);
		}

		/**
		 * @return the productid
		 */
		public String getProductid() {
			return productid.get();
		}

		/**
		 * @param the
		 *            productid to set
		 */
		public void setProductid(String productid) {
			this.productid = new SimpleStringProperty(productid);
		}

	}

}
