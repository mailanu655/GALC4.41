package com.honda.galc.client.product.pane;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dto.ProductHistoryDto;

/**
 * 
 * 
 * <h3>ProductHistoryIdlePane Class description</h3>
 * <p> ProductHistoryIdlePane description </p>
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
 * @author Rakesh<br>
 * Oct 24 2017
 *
 *
 */
public class ProductHistoryIdlePane extends AbstractProductIdlePane {
	private ObjectTablePane<ProductHistoryDto> trackingTablePane;
	
	public ProductHistoryIdlePane(ProductController productController) {
		super(productController);
	}
	
	@Override
	protected void initComponents() {
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		double scrollPaneWidth = parentBounds.getWidth();
		double scrollPaneHeight = parentBounds.getHeight();
		VBox vBox = new VBox();
		trackingTablePane = createTrackingTablePane(parentBounds);
		Label productLbl = UiFactory.createLabel("productLbl", "Product Scan History", Fonts.SS_DIALOG_BOLD(14), TextAlignment.CENTER);
		productLbl.setPadding(new Insets(10));
		vBox.getChildren().addAll(productLbl, trackingTablePane);
		vBox.setPadding(new Insets(5));
		vBox.setMinWidth(scrollPaneWidth - 50);
		vBox.setMinHeight((scrollPaneHeight/1.3) - 100);
		setCenter(vBox);
	}

	// If Tracking is displayed, then refresh it
	@Override
	public void toIdle() {
		loadProductResult();
	}
	
	private void loadProductResult() {
		String processPoints = "";
		if(getProductController().getModel().getProperty().getProductHistoryProcessPoint() != null &&
				StringUtils.isNotBlank(getProductController().getModel().getProperty().getProductHistoryProcessPoint()))
			processPoints = getProductController().getModel().getProperty().getProductHistoryProcessPoint();
		else
			processPoints = getProductController().getModel().getProcessPointId();
		List<ProductHistoryDto> productResult = getDao(ProductResultDao.class).findAllByProcessPoint(processPoints, getProductController().getModel().getProperty().getProductHistoryRowCount());
		trackingTablePane.setData(productResult);
	}
	
	private ObjectTablePane<ProductHistoryDto> createTrackingTablePane(Rectangle2D parentBounds) {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "productId")
				.put("Product Spec Code","productSpecCode")
				.put("Associate Number","associateNo")
				.put("Actual Time stamp", "actualTimestamp");
			
		Double[] columnWidth = new Double[] {
				0.10,0.12,0.12,0.15
			};
		ObjectTablePane<ProductHistoryDto> panel = new ObjectTablePane<ProductHistoryDto>(columnMappingList,columnWidth);
		panel.setMinWidth(parentBounds.getWidth() - 50);
		panel.setMinHeight((parentBounds.getHeight()/1.3) - 100);
		return panel;
	}
	
	@Override
	public String getName() {
		return "Product History";
	}
		
}
