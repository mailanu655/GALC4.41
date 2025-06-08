package com.honda.galc.client.dc.mvc;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import com.honda.galc.client.dc.control.SearchCriteriaPanel;
import com.honda.galc.client.dc.control.UnitNavigatorRepairClientControl;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.property.PropertyService;

public class DataCollectionRepairClientView extends AbstractDataCollectionView<DataCollectionRepairClientModel, DataCollectionController>{
	
	public DataCollectionRepairClientView(MainWindow window) {
		super(ViewId.DATA_COLLECTION_VIEW_REPAIR_CLIENT, window);
	}

	public DataCollectionRepairClientView(ViewId viewId, MainWindow window) {
		super(viewId, window);
	}
	
	@Override
	public void prepareUnitNavigator() {
		unitNavigator = new UnitNavigatorRepairClientControl();
		unitNavigator.init((DataCollectionController) getController(),getProcessPointId());
		unitNavigator.setItems(FXCollections.observableArrayList(getModel().getOperations()));
		unitNavigationPane = new BorderPane();
		unitNavigationPane.setTop(unitNavigator);		
		unitNavigationPane.autosize();	
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,getProcessPointId());
		unitNavigationPane.setPrefWidth(property.getUnitNavigatorWidth());		
		this.setLeft(unitNavigationPane);
		selectFirstOperation(getModel().getOperations());
		SearchCriteriaPanel.prepare(getController());
	}
	
	@Override
	public void populateUnitNavigator(List<MCOperationRevision> searchedList) {
		HBox hbox = (HBox) unitNavigator.getChildren().get(2);
		LoggedButton btn =  (LoggedButton) hbox.getChildren().get(0);
		btn.setId("nav-search-button-new");
		getProductModel().getProduct().setOperations(searchedList);
		unitNavigator.setItems(FXCollections.observableArrayList(searchedList));
		selectFirstOperation(searchedList);
	}
	
	@Override
	protected void selected(UnitNavigatorEvent event) {
		loadSelectedOperation(event.getIndex());
	}
}
