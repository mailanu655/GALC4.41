package com.honda.galc.client.teamlead;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.ui.component.MessageBox;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCOperationPartDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.entity.conf.MCOperationPart;
import com.honda.galc.entity.conf.MCOperationPartId;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.service.ServiceFactory;

import javafx.beans.binding.Bindings;
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

public class PartsMaintenanceTable extends
		BaseFXMLTable<com.honda.galc.client.dto.MCOperationPartRevisionDTO> {

	TableView<MCOperationPartRevisionDTO> tblView = null;
	MfgMaintFXMLPane controller;
	List<MCOperationPartRevision> pList = new ArrayList<MCOperationPartRevision>();

	public PartsMaintenanceTable() {
		super();
	}

	public PartsMaintenanceTable(
			TableView<MCOperationPartRevisionDTO> t,
			Map<String, TableColumn<MCOperationPartRevisionDTO, String>> columnMap,
			MfgMaintFXMLPane controller) {

		super(t, columnMap, false);
		tblView = t;
		this.controller = controller;
		onRowSelection();
		setContextMenu();
	}

	public void init(TableView<?> childTable,
			List<MCOperationPartRevisionDTO> list) {
		tblView.setEditable(false);
		tblView.setItems(FXCollections.observableList(list));
		pList = new ArrayList<MCOperationPartRevision>();
	}

	protected void setContextMenu() {

		tblView.setRowFactory(new Callback<TableView<MCOperationPartRevisionDTO>, TableRow<MCOperationPartRevisionDTO>>() {
			public TableRow<MCOperationPartRevisionDTO> call(
					TableView<MCOperationPartRevisionDTO> tableView) {
				final TableRow<MCOperationPartRevisionDTO> row = new TableRow<MCOperationPartRevisionDTO>() {
					@Override
					protected void updateItem(MCOperationPartRevisionDTO item,
							boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("Right Click To Edit Part");
							setTooltip(tooltip);
						}
					}
				};
				rowMenu = new ContextMenu();

				MenuItem editPartItem = UiFactory.createMenuItem("Edit Part");
				editPartItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						MCOperationPartRevisionDTO rec = row.getItem();
						new PartRevEditDialog(tblView, rec);
					}
				});

				rowMenu.getItems().addAll(editPartItem);

				MenuItem createMgfPart = UiFactory
						.createMenuItem("Create Mfg Part");
				rowMenu.getItems().addAll(createMgfPart);

				createMgfPart.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {

						MCOperationPartRevisionDTO rec = row.getItem();
						MCOperationPartRevisionId recEntityId = new MCOperationPartRevisionId(rec.getOperationName(), rec.getPartId(), Integer.parseInt(rec.getPartRev()));
						MCOperationPartRevision recEntity = ServiceFactory.getDao(MCOperationPartRevisionDao.class).findByKey(recEntityId);
						
						if (!rec.getPartType()
								.equals(PartType.REFERENCE.name())) {
							new MessageBox(
									"Can not create mfg part for this record")
							.showMessage();
							return;
						}

						// Featch max part id
						String maxPartId = ServiceFactory.getDao(
								MCOperationPartRevisionDao.class)
								.findMaxPartId(rec.getOperationName(), Integer.parseInt(rec.getPartRev()));
						String minPartId = ServiceFactory.getDao(
								MCOperationPartRevisionDao.class)
								.findMinPartId(rec.getOperationName(), Integer.parseInt(rec.getPartRev()));
						String partId = StringUtils.substring(maxPartId, 0,
								1)
								+ String.format("%04d", (Integer.parseInt(StringUtils.substring(maxPartId, 1)) + 1));
						boolean partType = ServiceFactory.getDao(
								MCOperationPartRevisionDao.class)
								.getMFGPartType(rec.getOperationName(), minPartId);

						// Create new part revision
						MCOperationPartRevision partRevEntity = DTOConverter.convertMcoperationPartRevisionDTO(rec);
						if (pList.size() == 0) {
							pList.add(DTOConverter
									.convertMcoperationPartRevisionDTO(rec));
						}
						partRevEntity.getId().setPartId(
								StringUtils.trim(partId));
						partRevEntity.setPartType(PartType.MFG);
						if(recEntity != null && recEntity.getApproved() != null) {
							partRevEntity.setApproved(new Timestamp(recEntity.getApproved().getTime()));
						}
						if(recEntity != null && recEntity.getDeprecated() != null) {
							partRevEntity.setDeprecated(new Timestamp(recEntity.getDeprecated().getTime()));
						}
						ServiceFactory.getDao(
								MCOperationPartRevisionDao.class).save(partRevEntity);
						pList.add(partRevEntity);

						// Create new operation part
						MCOperationPart operationPart = new MCOperationPart();
						MCOperationPartId id = new MCOperationPartId();
						operationPart.setId(id);
						operationPart.getId().setOperationName(
								partRevEntity.getId().getOperationName());
						operationPart.getId().setPartId(partId);
						operationPart.setPartRev(0);
						operationPart.setCreateTimestamp(new Timestamp(System
								.currentTimeMillis()));
						operationPart.setUpdateTimestamp(new Timestamp(System
								.currentTimeMillis()));
						ServiceFactory.getDao(MCOperationPartDao.class)
						.save(operationPart);

						// insert into part matrix
						List<MCOperationPartMatrix> lstPartMatrix = ServiceFactory
								.getDao(MCOperationPartMatrixDao.class)
								.findAllSpecCodeForOperationPartIdAndPartRev(
										rec.getOperationName(),
										rec.getPartId(),
										Integer.parseInt(rec.getPartRev()));
						for (MCOperationPartMatrix mcOperationPartMatrix : lstPartMatrix) {
							mcOperationPartMatrix.getId().setPartId(
									StringUtils.trim(partId));
							ServiceFactory.getDao(
									MCOperationPartMatrixDao.class).save(
											mcOperationPartMatrix);

							// delete mfg part matrix operationName,
							// maxPartId, partRevision, specCodeMask)
							if (partType) {
								ServiceFactory.getDao(
										MCOperationPartMatrixDao.class)
								.deletePartMatrix(
										rec.getOperationName(),
										minPartId,
										Integer.parseInt(rec
												.getPartRev()),
										mcOperationPartMatrix
										.getId()
										.getSpecCodeMask());
							}
						}

						tblView.setItems(FXCollections.observableList(DTOConverter
								.convertMcoperationPartRevision(pList)));
					}
				});
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));

				return row;
			}
		});
	}
}
