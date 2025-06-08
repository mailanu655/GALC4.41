package com.honda.galc.client.teamlead;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.dto.MCMeasurementDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.ui.component.MessageBox;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.service.ServiceFactory;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class MeasurementsTable extends BaseFXMLTable<com.honda.galc.client.dto.MCMeasurementDTO> {

	TableView<MCMeasurementDTO> tblView = null;
	MfgMaintFXMLPane controller;
	int numDuplicates;

	public MeasurementsTable() {
		super();
	}

	public MeasurementsTable(TableView<MCMeasurementDTO> t,
			Map<String, TableColumn<MCMeasurementDTO, String>> columnMap,
			MfgMaintFXMLPane controller) {
		super(t, columnMap, true);
		tblView = t;
		this.controller = controller;

		onRowSelection();
		setContextMenu();
		setAddAction();
	}

	public void init(List<MCMeasurementDTO> measList) {
		tblView.setEditable(false);
		tblView.setItems(FXCollections.observableList(measList));
	}

	@Override
	public void dropHandler() {
		List<MCMeasurementDTO> list = tblView.getItems();
		int i = 0;
		for (MCMeasurementDTO meas : list) {
			i++;
			meas.setSeqNumber(String.valueOf(i));
			MCOperationMeasurement entity = DTOConverter
					.convertMCMeasurementDTO(meas);
			ServiceFactory.getDao(MCOperationMeasurementDao.class).save(entity);
		}
	}

	protected void setAddAction() {
		controller.getAddMeasurementButton().setOnAction(
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						MCOperationPartRevisionDTO part = controller
								.getPartMaintTblView().getItems().iterator()
								.next();
						//Measurement can be added only for part type 'MFG'
						PartType partType = PartType.get(part.getPartType());
						if(partType.equals(PartType.MFG)) {
							new MeasurementWizard(part, controller, "Add");
						}
						else {
							new MessageBox(
									"Measurements can be added only for part type 'MFG'")
							.showMessage();
							return;
						}
					}
				});
	}

	protected void setContextMenu() {
		tblView.setRowFactory(new Callback<TableView<MCMeasurementDTO>, TableRow<MCMeasurementDTO>>() {
			public TableRow<MCMeasurementDTO> call(
					TableView<MCMeasurementDTO> tableView) {
				final TableRow<MCMeasurementDTO> row = new TableRow<MCMeasurementDTO>() {
					@Override
					protected void updateItem(MCMeasurementDTO item,
							boolean empty) {
						super.updateItem(item, empty);

						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("Right Click To Edit Measurement");
							setTooltip(tooltip);
						}
					}
				};

				rowMenu = new ContextMenu();

				MenuItem editMeasurementItem = UiFactory.createMenuItem("Edit Measurement");
				editMeasurementItem
				.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						MCMeasurementDTO rec = row.getItem();
						new MeasurementWizard(rec, controller,
								"Edit");
					}
				});

				MenuItem duplicateMeasurementItem = UiFactory.createMenuItem(
						"Duplicate Measurement");
				duplicateMeasurementItem
				.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {

						duplicatePopup();
						MCOperationPartRevisionDTO part = controller
								.getPartMaintTblView().getItems()
								.iterator().next();

						ObservableList<MCMeasurementDTO> list = tblView
								.getItems();
						Collections.sort(list);
						int maxSeqNumber = Integer.parseInt(list
								.get(list.size()-1).getSeqNumber());


						MCMeasurementDTO rec = row.getItem();

						MCOperationMeasurementDao measDao = ServiceFactory
								.getDao(MCOperationMeasurementDao.class);

						for (int i = 0; i < numDuplicates; i++) {
							++maxSeqNumber;
							MCMeasurementDTO newRec = (MCMeasurementDTO) rec.clone();
							newRec.setSeqNumber(String
									.valueOf(maxSeqNumber));

							MCOperationMeasurement measurement = DTOConverter
									.convertMCMeasurementDTO(newRec);
							measDao.save(measurement);
							list.add(newRec);

						}
						tblView.setItems(list);

						// Setting measurement count
						MCOperationPartRevision partRevEntity = DTOConverter
								.convertMcoperationPartRevisionDTO(part);
						
						MCOperationPartRevisionId recEntityId = new MCOperationPartRevisionId(part.getOperationName(), part.getPartId(), Integer.parseInt(part.getPartRev()));
						MCOperationPartRevision recEntity = ServiceFactory.getDao(MCOperationPartRevisionDao.class).findByKey(recEntityId);
						
						if(recEntity != null && recEntity.getApproved() != null) {
							partRevEntity.setApproved(new Timestamp(recEntity.getApproved().getTime()));
						}
						if(recEntity != null && recEntity.getDeprecated() != null) {
							partRevEntity.setDeprecated(new Timestamp(recEntity.getDeprecated().getTime()));
						}
						
						ServiceFactory.getDao(
								MCOperationPartRevisionDao.class)
						.save(partRevEntity);

					}
				});

				MenuItem deleteMeasurementItem = UiFactory.createMenuItem(
						"Delete Measurement");
				deleteMeasurementItem
				.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						MCOperationPartRevisionDTO part = controller
								.getPartMaintTblView().getItems()
								.iterator().next();
						ObservableList<MCMeasurementDTO> list = tblView
								.getItems();
						MCMeasurementDTO rec = row.getItem();
						list.remove(rec);
						MCOperationMeasurement entity = DTOConverter
								.convertMCMeasurementDTO(rec);
						MCOperationMeasurementDao measDao = ServiceFactory
								.getDao(MCOperationMeasurementDao.class);
						measDao.remove(entity);
						tblView.setItems(list);

						int i = 0;
						for (MCMeasurementDTO meas : list) {
							entity = DTOConverter
									.convertMCMeasurementDTO(meas);
							measDao.remove(entity);
							i++;
							meas.setSeqNumber(String.valueOf(i));
							entity = DTOConverter
									.convertMCMeasurementDTO(meas);
							measDao.save(entity);
						}

						// Setting measurement count
						MCOperationPartRevision partRevEntity = DTOConverter
								.convertMcoperationPartRevisionDTO(part);
						
						MCOperationPartRevisionId recEntityId = new MCOperationPartRevisionId(part.getOperationName(), part.getPartId(), Integer.parseInt(part.getPartRev()));
						MCOperationPartRevision recEntity = ServiceFactory.getDao(MCOperationPartRevisionDao.class).findByKey(recEntityId);
						
						if(recEntity != null && recEntity.getApproved() != null) {
							partRevEntity.setApproved(new Timestamp(recEntity.getApproved().getTime()));
						}
						if(recEntity != null && recEntity.getDeprecated() != null) {
							partRevEntity.setDeprecated(new Timestamp(recEntity.getDeprecated().getTime()));
						}
						
						ServiceFactory.getDao(
								MCOperationPartRevisionDao.class)
						.save(partRevEntity);
					}
				});

				rowMenu.getItems().addAll(editMeasurementItem,
						deleteMeasurementItem, duplicateMeasurementItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));
				return row;
			}
		});
	}
	
	private void duplicatePopup() {
		
		
		final TextField duplicateNumberTF = UiFactory.createTextField("duplicateNumberTF", "1");
		Label duplicateLabel = UiFactory.createLabel("duplicateLabel", "Enter Number of Duplicates:  ");
		Button okButton = UiFactory.createButton("OK");
		Button cancelButton = UiFactory.createButton("Cancel");
		final Stage dialog = new Stage();
		
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initStyle(StageStyle.DECORATED);
		dialog.setTitle("Add Measurements");
		

		dialog.setHeight(200);
		dialog.setWidth(400);
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				dialog.close();
			}
		});
		
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				numDuplicates =Integer.valueOf(duplicateNumberTF.getText());
				dialog.close();
			}
		});
		
		GridPane grid = new GridPane();
		
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));
		
		grid.add(duplicateLabel, 0, 1);
		grid.add(duplicateNumberTF, 1, 1);
		grid.add(okButton, 0, 2);
		grid.add(cancelButton, 1, 2);
		Scene scene = new Scene(grid);
	
		dialog.setScene(scene);
		
		dialog.centerOnScreen();

		dialog.toFront();
		dialog.showAndWait();
		
		
	}
}
