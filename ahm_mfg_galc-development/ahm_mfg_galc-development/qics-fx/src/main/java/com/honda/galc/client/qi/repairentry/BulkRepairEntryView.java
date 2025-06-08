package com.honda.galc.client.qi.repairentry;

import java.util.ArrayList;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiRepairResultDto;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;


public class BulkRepairEntryView extends AbstractRepairEntryView<RepairEntryModel, BulkRepairEntryController> {

	public BulkRepairEntryView(MainWindow window) {
		super(ViewId.REPAIR_ENTRY, window);
		EventBusUtil.register(this);
	}

	@Override
	public void reload() {
		if (isProductScraped)  {
			EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED, StatusMessageEventType.WARNING));
		}
	}

	@Override
	public void start() {		
	}

	@Override
	public void initView() {
		setParentCachedDefectList(new ArrayList<QiRepairResultDto>());
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);

		MigPane pane = new MigPane("insets 5 5 5 5 ", "[center,grow,fill]", "");
		pane.add(getMainTablePane(),"span,wrap");
		pane.add(getExistingProductAssignment(),"span,wrap");
		pane.add(createRepairOptionsPanel(),"span,wrap");
		this.setCenter(pane);
		reload();		
	}

	@Override
	protected TreeTableView<QiRepairResultDto> createTreeTablePane() {
		super.createTreeTablePane();
		// ********** Initialized tree table columns ****************//
		TreeTableColumn<QiRepairResultDto, String> groupTransId = new TreeTableColumn<QiRepairResultDto, String>("Trans Id/Qty");
		groupTransId.setPrefWidth(getScreenWidth() * 0.04);
		groupTransId.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						long id = param.getValue().getValue().getDefectTransactionGroupId();
						return new ReadOnlyObjectWrapper<String>(id == 0 ? "" : getTransactionColumnData(id));
					}

					private String getTransactionColumnData(long defectTransactionGroupId) {
						long count = ((BulkRepairEntryController) getController()).getDefectTransactionGroupCount(defectTransactionGroupId);
						return defectTransactionGroupId + "/" + count;
					}
				});

		treeTablePane.getColumns().add(0,groupTransId); 
		return treeTablePane;
	}
	
	@Override
	public void onTabSelected(){
		Logger.getLogger().check("Bulk Repair Entry panel Selected");
		getController().loadInitialData();
	}
}
