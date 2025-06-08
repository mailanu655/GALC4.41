package com.honda.galc.client.teamlead.mdrs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

import com.honda.galc.client.dto.MCMeasurementDTO;
import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.dto.MdrsQuarterDTO;
import com.honda.galc.client.teamlead.BaseFXMLTable;
import com.honda.galc.client.teamlead.DTOConverter;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.service.ServiceFactory;

public class MdrsQuarterTable extends BaseFXMLTable {
	TableView<MdrsQuarterDTO> tblView = null;
	
	
	public MdrsQuarterTable(
			TableView<com.honda.galc.client.dto.MdrsQuarterDTO> t,
			Map<String, TableColumn<MdrsQuarterDTO, String>> columnMap) {

		super(t, columnMap, false);
		tblView = t;
		setContextMenu();

	}
	
	public void init(List<MdrsQuarterDTO> list) {
		tblView.setEditable(false);
		tblView.setItems(FXCollections.observableList(list));
	}
	
	
	protected void setContextMenu() {
		tblView.setRowFactory(new Callback<TableView<MdrsQuarterDTO>, TableRow<MdrsQuarterDTO>>() {
			public TableRow<MdrsQuarterDTO> call(
					TableView<MdrsQuarterDTO> tableView) {
				final TableRow<MdrsQuarterDTO> row = new TableRow<MdrsQuarterDTO>() {
					@Override
					protected void updateItem(MdrsQuarterDTO item,
							boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("Right Click For Mapping MDRS Quarters to GALC Periods");
							setTooltip(tooltip);
						}

					}
				};

				rowMenu = new ContextMenu();
				MenuItem mapQuarterItem = new MenuItem("Map Quarters");
				mapQuarterItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						MdrsQuarterDTO rec = row.getItem();
						

					
					}
				});

				rowMenu.getItems().add(mapQuarterItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));

				return row;
			}
		});

	}
	

}
