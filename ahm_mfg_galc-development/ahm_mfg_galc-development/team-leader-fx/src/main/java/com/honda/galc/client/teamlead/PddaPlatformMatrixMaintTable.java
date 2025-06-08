package com.honda.galc.client.teamlead;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dto.MCPddaPlatformDTO;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCPddaUnitDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCPddaUnit;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class PddaPlatformMatrixMaintTable extends
BaseFXMLTable<MCPddaPlatformDTO> {
	private TableView<MCPddaPlatformDTO> tblView = null;
	private MfgMaintFXMLPane controller;
	private TableView<SpecCodeMaskUnitsDTO> childTblView = null;
	private List<MCOperationMatrix> availableMatrixInfo = null;
	private List<MCOperationPartMatrix> availablePartMatrixInfo = null;
	private List<MCOperationMatrix> assignedMatrixInfo = null;
	private List<MCOperationPartMatrix> assignedPartMatrixInfo = null;
	List<? extends BaseProductSpec> findAllMbpnSpecCodes = null;

	public PddaPlatformMatrixMaintTable(MfgMaintFXMLPane controller,
			TableView<MCPddaPlatformDTO> t,
			Map<String, TableColumn<MCPddaPlatformDTO, String>> columnMap) {
		super(t, columnMap, false);
		this.controller = controller;
		this.tblView = t;
		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	public void init(TableView<SpecCodeMaskUnitsDTO> childTable, PddaPlatformDetails pddaPlatformDetails) {
		this.childTblView = childTable;
		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblView.setItems(FXCollections.observableList(pddaPlatformDetails
				.getPddaPlatforms()));
		findAllMbpnSpecCodes = ProductTypeUtil.getProductSpecDao(ProductType.MBPN).findAll();
		onRowSelection();
		setContextMenu();

	}

	private void fetchAllAvailableModelModelTypes(int pddaPltformId) {
		availableMatrixInfo = ServiceFactory.getDao(MCOperationMatrixDao.class)
				.findAllMatrixByPDDAPlatformId(pddaPltformId);
		availablePartMatrixInfo = ServiceFactory.getDao(
				MCOperationPartMatrixDao.class).findAllMatrixByPDDAPlatformId(
						pddaPltformId);
	}

	public void onRowSelection() {
		tblView.getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<MCPddaPlatformDTO>() {

			public void changed(
					ObservableValue<? extends MCPddaPlatformDTO> arg0,
					MCPddaPlatformDTO arg1, MCPddaPlatformDTO arg2) {

				MCPddaPlatformDTO selected = tblView
						.getSelectionModel().getSelectedItem();
				if (selected == null)
					return;
				fetchAllAvailableModelModelTypes(selected
						.getPlatformId());
				separateSpecialSpecCodeMasks();
				List<SpecCodeMaskUnitsDTO> maskUnitMapping = displaySpecialSpecCodeMaskUnitMapping(selected.getPlatformId());
				childTblView.setItems(FXCollections.observableArrayList(maskUnitMapping));
				childTblView.getSelectionModel().clearSelection();
			}


		});
	}


	private List<SpecCodeMaskUnitsDTO> displaySpecialSpecCodeMaskUnitMapping(
			int pddaPlatformId) {
		Set<String> specialSpecCodeMasks = new TreeSet<String>();
		for (MCOperationMatrix matrix : assignedMatrixInfo) {
			specialSpecCodeMasks.add(matrix.getId().getSpecCodeMask());
		}
		List<SpecCodeMaskUnitsDTO> maskUnitsDTOs = new ArrayList<SpecCodeMaskUnitsDTO>();
		Iterator<String> iterator = specialSpecCodeMasks.iterator();
		while (iterator.hasNext()) {
			String specCodeMask = iterator.next();
			List<MCPddaUnit> list = ServiceFactory.getDao(MCPddaUnitDao.class)
					.findAllByPDDAPlatformIdSpecCodeMask(pddaPlatformId,
							specCodeMask);
			List<String> unitNos = new ArrayList<String>();
			for (MCPddaUnit unit : list) {
				unitNos.add(unit.getId().getUnitNo());
			}
			maskUnitsDTOs.add(new SpecCodeMaskUnitsDTO(specCodeMask,
					StringUtils.join(unitNos, ", ")));
		}

		return maskUnitsDTOs;
	}

	private void separateSpecialSpecCodeMasks() {
		assignedMatrixInfo = new ArrayList<MCOperationMatrix>();
		assignedPartMatrixInfo = new ArrayList<MCOperationPartMatrix>();
		Iterator<MCOperationMatrix> iter = availableMatrixInfo.iterator();
		Iterator<MCOperationPartMatrix> iter2 = availablePartMatrixInfo
				.iterator();
		boolean isSpecialSpecCodeMask = false;

		while (iter.hasNext()) {
			MCOperationMatrix next = iter.next();
			isSpecialSpecCodeMask = false;
			for (BaseProductSpec productSpec : findAllMbpnSpecCodes) {
				if (CommonPartUtility.verification(productSpec
						.getProductSpecCode(), next.getId().getSpecCodeMask(),
						PropertyService.getPartMaskWildcardFormat())) {
					isSpecialSpecCodeMask = true;
					break;
				}
			}
			if (isSpecialSpecCodeMask) {
				assignedMatrixInfo.add(next);
				iter.remove();
			}
		}

		isSpecialSpecCodeMask = false;

		while (iter2.hasNext()) {
			MCOperationPartMatrix next = iter2.next();
			isSpecialSpecCodeMask = false;
			for (BaseProductSpec productSpec : findAllMbpnSpecCodes) {
				if (CommonPartUtility.verification(productSpec
						.getProductSpecCode(), next.getId().getSpecCodeMask(),
						PropertyService.getPartMaskWildcardFormat())) {
					isSpecialSpecCodeMask = true;
					break;
				}
			}
			if (isSpecialSpecCodeMask) {
				assignedPartMatrixInfo.add(next);
				iter2.remove();
			}
		}

	}

	protected void setContextMenu() {

		tblView.setRowFactory(new Callback<TableView<MCPddaPlatformDTO>, TableRow<MCPddaPlatformDTO>>() {
			public TableRow<MCPddaPlatformDTO> call(
					TableView<MCPddaPlatformDTO> tableView) {
				final TableRow<MCPddaPlatformDTO> row = new TableRow<MCPddaPlatformDTO>() {
					@Override
					protected void updateItem(MCPddaPlatformDTO item,
							boolean empty) {
						super.updateItem(item, empty);

						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("RIGHT CLICK TO ADD MASK");
							setTooltip(tooltip);
						}
					}
				};

				rowMenu = new ContextMenu();

				MenuItem addRecordItem = new MenuItem("ADD");
				addRecordItem.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent arg0) {
						MCPddaPlatformDTO selectedItem = tblView
								.getSelectionModel().getSelectedItem();
						if (selectedItem == null) {
							MessageDialog.showError("Please Select a Process");
						}

						String planCode = controller.getPropertyBean().getPlanCodes();
						if(planCode == null || StringUtils.isEmpty(planCode)){
							MessageDialog.showError("Please Enter Application Property - PLAN_CODES");
							return;
						}

						AddEditSpecCodeMask mask = new AddEditSpecCodeMask(planCode,
								availableMatrixInfo, availablePartMatrixInfo, childTblView.getItems());
						mask.showDialog();
						tblView.getSelectionModel().clearSelection();
						tblView.getSelectionModel().select(selectedItem);
					}
				});

				rowMenu.getItems().add(addRecordItem);
				row.setContextMenu(rowMenu);
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu).otherwise((ContextMenu) null));
				return row;
			}
		});

		childTblView
		.setRowFactory(new Callback<TableView<SpecCodeMaskUnitsDTO>, TableRow<SpecCodeMaskUnitsDTO>>() {
			public TableRow<SpecCodeMaskUnitsDTO> call(
					TableView<SpecCodeMaskUnitsDTO> tableView) {
				final TableRow<SpecCodeMaskUnitsDTO> row = new TableRow<SpecCodeMaskUnitsDTO>() {
					@Override
					protected void updateItem(
							SpecCodeMaskUnitsDTO item,
							boolean empty) {
						super.updateItem(item, empty);

						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("RIGHT CLICK TO DELETE THIS RECORD");
							setTooltip(tooltip);
						}
					}
				};

				rowMenu = new ContextMenu();

				MenuItem deleteRecordItem = new MenuItem("DELETE");

				deleteRecordItem
				.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent arg0) {

						MCPddaPlatformDTO selectedItem = tblView
								.getSelectionModel()
								.getSelectedItem();
						SpecCodeMaskUnitsDTO selectedItem2 = childTblView
								.getSelectionModel()
								.getSelectedItem();
						if (selectedItem2 == null) {
							MessageDialog
							.showError("Please Select a Spec Code Mask");
							return;
						}
						List<MCOperationMatrix> deleteOperationMatrix = ServiceFactory
								.getDao(MCOperationMatrixDao.class)
								.findAllMatrixByPDDAPlatformIdAndSpecCodeMask(
										selectedItem
										.getPlatformId(),
										selectedItem2
										.getSpecCodeMask());
						if (deleteOperationMatrix != null)
							ServiceFactory
							.getDao(MCOperationMatrixDao.class)
							.removeAll(
									deleteOperationMatrix);

						List<MCOperationPartMatrix> deletePartMatrix = ServiceFactory
								.getDao(MCOperationPartMatrixDao.class)
								.findAllMatrixByPDDAPlatformIdAndSpecCodeMask(
										selectedItem
										.getPlatformId(),
										selectedItem2
										.getSpecCodeMask());

						if (deletePartMatrix != null)
							ServiceFactory
							.getDao(MCOperationPartMatrixDao.class)
							.removeAll(deletePartMatrix);
						childTblView.getItems().remove(selectedItem2);

					}
				});


				rowMenu.getItems().addAll(deleteRecordItem);
				row.setContextMenu(rowMenu);
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu).otherwise((ContextMenu) null));
				return row;
			}
		});

	}

}
