package com.honda.galc.client.teamlead;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.dto.ChangeFormDTO;
import com.honda.galc.client.dto.MCPddaPlatformDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.RevisionType;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;

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

public class MCRevisionsTable extends BaseFXMLTable<MCRevisionDTO> {

	TableView<MCRevisionDTO> tblView = null;
	TableView<ChangeFormDTO> childTblView = null;
	private String userId;
	private MfgMaintFXMLPane controller;
	private TableView<MCPddaPlatformDTO> platformMMTblView=null;
	
	private MCViosMasterPlatform platform;
	public MCRevisionsTable() {
		super();

	}

	public MCRevisionsTable(MfgMaintFXMLPane controller,TableView<MCPddaPlatformDTO> apprMatrix,TableView<MCRevisionDTO> t,
			Map<String, TableColumn<MCRevisionDTO, String>> columnMap, String userId, boolean isOneClick) {
		super(t, columnMap, false);
		tblView = t;
		this.controller = controller;
		this.userId = userId;
		this.platformMMTblView=apprMatrix;
		if(!isOneClick) {
			setContextMenu();
			onRowSelection();
		} 
	}

	public void init(MCViosMasterPlatform platform, TableView<ChangeFormDTO> childTable, List... lists) {
		this.platform = platform;
		this.childTblView = childTable;
		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblView.setItems(FXCollections.observableList(lists[0]));
	}

	protected void setContextMenu() {

		tblView.setRowFactory(new Callback<TableView<MCRevisionDTO>, TableRow<MCRevisionDTO>>() {
			public TableRow<MCRevisionDTO> call(TableView<MCRevisionDTO> tableView) {
				final TableRow<MCRevisionDTO> row = new TableRow<MCRevisionDTO>() {
					@Override
					protected void updateItem(MCRevisionDTO item,
							boolean empty) {
						super.updateItem(item, empty);

						if(item != null) {
							
							Tooltip tooltip = new Tooltip();
		                    tooltip.setText("Right Click To Manage Approvals");
		                    setTooltip(tooltip);
						}
					}
				};
				rowMenu = new ContextMenu();

				MenuItem matchProcessItem = UiFactory.createMenuItem("Approve");

				matchProcessItem.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {
						// MCRevision rec = row.getItem();
						MCRevisionDTO rec = tblView.getSelectionModel()
								.getSelectedItem();
						String appId = controller.getMainWindow().getApplication().getApplicationId();
						new RevisionApprovalWizard(rec, platformMMTblView,userId, controller.getPropertyBean(), platform);

					}
				});

				rowMenu.getItems().add(matchProcessItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));

				return row;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRowSelection() {

		tblView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener() {

					public void changed(ObservableValue arg0, Object arg1,
							Object arg2) {

						MCRevisionDTO revision = tblView.getSelectionModel()
								.getSelectedItem();

						if(revision!=null && revision.getId()!=null) {
							List<ChangeForm> list =	null;
							if(revision.getType().equalsIgnoreCase(RevisionType.PLATFORM_CHG.getRevType())) {
								List<MCPddaPlatform> platforms = ServiceFactory.getDao(MCPddaPlatformDao.class).getPlatformsForRevision(revision.getId());
								//For Revision type 'Platform change', there will always be only one platform
								if(platforms!=null && platforms.size() > 0) {
									MCPddaPlatform platform = platforms.iterator().next();
									//Fetching change form
									ChangeForm chgFrm = ServiceFactory.getService(GenericPddaDaoService.class).getChangeFormForPddaPlatform(platform.getPddaPlatformId());
									list = new ArrayList<ChangeForm>();
									list.add(chgFrm);
								}
							}
							else if(revision.getType().equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType()) 
									|| revision.getType().equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())) {
								list =	ServiceFactory.getDao(ChangeFormDao.class)
										.getChangeFormsForRevId(revision.getId());
							}
							if(list == null) {
								list = new ArrayList<ChangeForm>();
							}
							childTblView.setItems(FXCollections.observableList(DTOConverter.convertChangeForm(list).getChangeForms()));
						} else {
							childTblView.getItems().clear();
						}
					}
				});
	}

}
