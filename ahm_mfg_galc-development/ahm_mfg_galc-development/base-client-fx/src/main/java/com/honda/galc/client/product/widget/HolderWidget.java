package com.honda.galc.client.product.widget;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.pane.AbstractWidget;

/**
 * 
 * 
 * <h3>HolderWidget Class description</h3>
 * <p> HolderWidget description </p>
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
 * Mar 28, 2014
 *
 *
 */
public class HolderWidget extends AbstractWidget{
	
	private List<AbstractWidget> subWidgets;
	
	public HolderWidget(ProductController productController, List<AbstractWidget> subWidgets) {
		super(null, productController);
		this.subWidgets = subWidgets;
		init();
	}
	
	//@Override
	protected void init() {
		HBox box = new HBox(10);
		box.setAlignment(Pos.CENTER);
		
		for(AbstractWidget widget : subWidgets) {
			box.getChildren().add(widget);
		}
		setCenter(box);
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
		
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
		
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		
	}

	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub
		
	}

}
