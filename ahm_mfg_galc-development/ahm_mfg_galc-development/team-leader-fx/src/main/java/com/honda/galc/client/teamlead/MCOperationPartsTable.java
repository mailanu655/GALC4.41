package com.honda.galc.client.teamlead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.honda.galc.client.dto.MCMeasurementDTO;
import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.PartType;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.service.ServiceFactory;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class MCOperationPartsTable extends
		BaseFXMLTable<com.honda.galc.client.dto.MCOperationPartRevisionDTO> {

	TableView<MCOperationPartRevisionDTO> tblView = null;
	TableView<?> childTblView = null;
	MfgMaintFXMLPane controller;

	public MCOperationPartsTable() {
		super();

	}

	public MCOperationPartsTable(
			TableView<MCOperationPartRevisionDTO> t,
			Map<String, TableColumn<MCOperationPartRevisionDTO, String>> columnMap,
			MfgMaintFXMLPane controller) {

		super(t, columnMap, false);
		tblView = t;
		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.controller = controller;
		onRowSelection();
		setContextMenu();

	}

	public void init(TableView<?> childTable, List<MCOperationPartRevisionDTO> list) {
		this.childTblView = childTable;
		tblView.setItems(FXCollections.observableList(list));
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
							tooltip.setText("Right Click For Parts Maintenance");
							setTooltip(tooltip);
						}

					}
				};
				
				
				rowMenu = new ContextMenu();
				MenuItem matchProcessItem = UiFactory.createMenuItem("Do Parts Maintenance");
				matchProcessItem.setOnAction(new EventHandler<ActionEvent>() {
					@SuppressWarnings("rawtypes")
					public void handle(ActionEvent event) {
						MCOperationPartRevisionDTO rec = row.getItem();
						List<MCOperationPartRevisionDTO> pList = new ArrayList<MCOperationPartRevisionDTO>();
						pList.add(rec);
						
						//RGALCPROD-3093 FIF Part masking changes START
						int count=0;
						String partR="";
						String partRev="";
						String partMask="";
						String partItemNo="";
						String partRevItem="";
						rec.setPartMaskCheckbox(false);
						rec.setPartMaskTextboxVal("");
						HashMap<String, String> hmap= new HashMap<String, String>();
						List<MCOperationPartRevisionDTO> items = new ArrayList<MCOperationPartRevisionDTO>(tblView.getItems());
			        	
						for(MCOperationPartRevisionDTO partObj : items){
							if(partObj.getPartType().equalsIgnoreCase("MFG")){
								count = (partObj.getPartId()+"^"+partObj.getPartNo()).equals(rec.getPartId()+"^"+rec.getPartNo()) ? count+1 : count;
								partMask= partObj.getPartMask()==null || partObj.getPartMask().equals("") ? "#" : partObj.getPartMask();
								partR=(String) hmap.get(partObj.getPartId()+"^"+partObj.getPartNo());
								partRev=partObj.getPartRev();
								partItemNo=partObj.getPartItemNo();
								partRevItem = hmap.containsKey(partObj.getPartId()+"^"+partObj.getPartNo()) ?  partRev+":"+partItemNo+":"+partMask+"^"+partR : partRev+":"+partItemNo+":"+partMask;
								hmap.put(partObj.getPartId()+"^"+partObj.getPartNo(), partRevItem);
							}
						}
						
						
						if(hmap.isEmpty()==false)
						{
							Set set = hmap.entrySet();
						    Iterator iterator = set.iterator();
						      while(iterator.hasNext()) {
						         Map.Entry mentry = (Map.Entry)iterator.next();
						         if(mentry.getKey().equals(rec.getPartId()+"^"+rec.getPartNo()))
						         {
						        	 String j=mentry.getValue().toString();
						        	 String[] i=j.split("\\^");
						        	 if(i.length>1){
							        	 rec.setPartMaskTextboxVal(mentry.getValue().toString());
							    	 }
						         }
						      }
						}
						rec.setPartMaskCheckbox(count>1 ? true : false);
						SingleSelectionModel<Tab> selectionModel = controller
								.getMainTabPane().getSelectionModel();
						selectionModel.select(2);
						controller.getPartsMaintenanceTable().init(null, pList);
						String revStatus = ServiceFactory.getDao(
								MCOperationRevisionDao.class)
								.getRevStatusForOperationRev(
										Integer.valueOf(rec.getRevId()));
						
						//Measurement can be added only for part type 'MFG'
						PartType partType = PartType.get(rec.getPartType());
						if ((partType.equals(PartType.MFG))) {
							controller.getAddMeasurementButton().setDisable(false);
						}
						else {
							controller.getAddMeasurementButton().setDisable(true);
						}

						// fetch part matrix records
						List<MCOperationPartMatrix> mList = ServiceFactory
								.getDao(MCOperationPartMatrixDao.class)
								.findAllSpecCodeForOperationPartIdAndPartRev(
										rec.getOperationName(),
										rec.getPartId(),
										Integer.valueOf(rec.getPartRev()));
						
						ListIterator<MCOperationPartMatrix> litr = mList.listIterator();
						List<Object[]> mList3 = new ArrayList<Object[]>();
						while(litr.hasNext()) {
							MCOperationPartMatrix element = litr.next();
							List<Object[]> mList12 = ServiceFactory.getDao(BomDao.class).findEffectiveDate(element.getId().getSpecCodeMask(), rec.getOperationName(), rec.getPartNo());
							if(mList12 != null && mList12.size()>0){
								ListIterator<Object[]> litr1 = mList12.listIterator();
							 
								while(litr1.hasNext()){
									Object[] obj=litr1.next();
									mList3.add(obj);
								}
							}
						}
						
						List<MCOperationMatrixDTO> mList2 = DTOConverter
								.convertMcoperationPartMatrixEffDate(mList, mList3);
						
						controller
								.getOperationPartMatrixTable().init(
										null,
										FXCollections
												.observableArrayList(mList2));

						// fetch measurements records

						List<MCOperationMeasurement> measList = ServiceFactory
								.getDao(MCOperationMeasurementDao.class)
								.findAllMeasurementForOperationPartAndPartRevision(
										rec.getOperationName(),
										rec.getPartId(),
										Integer.valueOf(rec.getPartRev()));

						List<MCMeasurementDTO> measList2 = DTOConverter
								.convertMCOperationMeasurements(measList);
						

						controller
								.getMeasurementsTable()
								.init(FXCollections.observableArrayList(measList2));

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

}
