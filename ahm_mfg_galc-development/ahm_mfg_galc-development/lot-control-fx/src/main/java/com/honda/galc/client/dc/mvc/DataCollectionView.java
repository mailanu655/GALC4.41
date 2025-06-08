package com.honda.galc.client.dc.mvc;

import com.honda.galc.client.dc.control.UnitNavigatorControl;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.service.property.PropertyService;

import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;

/**
 * 
 * <h3>DataCollectionView Class description</h3>
 * <p> DataCollectionView description </p>
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
 * Feb 24, 2014
 *
 *
 */
public class DataCollectionView extends AbstractDataCollectionView<DataCollectionModel, DataCollectionController> {
	
	public DataCollectionView(MainWindow window) {
		super(ViewId.DATA_COLLECTION_VIEW, window);
	}

	public DataCollectionView(ViewId viewId, MainWindow window) {
		super(viewId, window);
	}
	
	protected void prepareUnitNavigator() {
		unitNavigator = new UnitNavigatorControl();
		unitNavigator.init(getController(),getProcessPointId());
		unitNavigator.setItems(FXCollections.observableArrayList(getModel().getOperations()));
		unitNavigationPane = new BorderPane();
		unitNavigationPane.setTop(unitNavigator);		
		unitNavigationPane.autosize();	
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,getProcessPointId());
		unitNavigationPane.setPrefWidth(property.getUnitNavigatorWidth());		
		this.setLeft(unitNavigationPane);
		selectFirstOperation(getModel().getOperations());
	}
}