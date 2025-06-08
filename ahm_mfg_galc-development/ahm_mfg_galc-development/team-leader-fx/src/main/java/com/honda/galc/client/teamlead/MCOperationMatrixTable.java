package com.honda.galc.client.teamlead;

import java.util.List;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.service.ServiceFactory;

public class MCOperationMatrixTable extends BaseFXMLTable<MCOperationMatrixDTO> {

	TableView<MCOperationMatrixDTO> operationsMatrixTblView = null;
	TableView<?> childTblView = null;
	List childList1 = null;
	List childList2 = null;
	String EXCLUDE = "EXCLUDE";
	//
	public MCOperationMatrixTable() {
		super();

	}

	public MCOperationMatrixTable(
			TableView<com.honda.galc.client.dto.MCOperationMatrixDTO> t,
			Map<String, TableColumn<MCOperationMatrixDTO, String>> columnMap) {

		super(t, columnMap, false);
		operationsMatrixTblView = t;
		setContextMenu();
	}

	protected void setContextMenu() {
		// Will create right click menu on Operation Matrix Table
		
		operationsMatrixTblView.setRowFactory(new Callback<TableView<MCOperationMatrixDTO>, TableRow<MCOperationMatrixDTO>>() {

			public TableRow<MCOperationMatrixDTO> call(
					TableView<MCOperationMatrixDTO> arg0) {
				
				final TableRow<MCOperationMatrixDTO> row = new TableRow<MCOperationMatrixDTO>(){
					@Override
					protected void updateItem(MCOperationMatrixDTO item,
							boolean empty) {
						super.updateItem(item, empty);
						if(item != null){
							Tooltip tooltip = new Tooltip();
							tooltip.setText("RIGHT CLICK TO MANAGE EXCLUDE/INCLUDE OPERATION");
							
							setTooltip(tooltip);
							if(item.getSpecCodeType().trim().equalsIgnoreCase(EXCLUDE)){
								this.setStyle("-fx-font-weight: bold;-fx-text-fill: red;");
							}else{
								this.setStyle("-fx-font-weight: normal;-fx-text-fill: black;");
							}
						}
					}
				}; 
				

				
				rowMenu = new ContextMenu();
				
				MenuItem excludeOperationMenu = new MenuItem("Manage Exclude / Include Operation");

				excludeOperationMenu.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent ae) {
						boolean response =new ExcludeIncludeOperationDialog(row.getItem()).constructStage();
						if(response){
							operationsMatrixTblView.getItems().clear();
							List<MCOperationMatrix> updateOpMatrixLst = ServiceFactory.getDao(MCOperationMatrixDao.class).findAllMatrixForOperationAndRevId(row.getItem().getOperationName(), row.getItem().getOperationRev());
							operationsMatrixTblView.setItems(FXCollections
									.observableList(DTOConverter
											.convertMcoperationMatrix(updateOpMatrixLst)));
						}
					}

				});
				
				rowMenu.getItems().add(excludeOperationMenu);
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));
				return row;
			}
			
			
			
		});
		
		
	}
	
	public void init(TableView<?> childTable, List... lists) {
		this.childTblView = childTable;
		// childList1 = lists[1];
		// childList2 = lists[2];
		
		operationsMatrixTblView.setEditable(false);
		
		operationsMatrixTblView
				.setItems(FXCollections.observableList(lists[0]));
		
	}

	@Override
	public void onRowSelection() {

		operationsMatrixTblView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener() {

					public void changed(ObservableValue arg0, Object arg1,
							Object arg2) {

					

					}

				});

	}

}
