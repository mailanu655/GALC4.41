package com.honda.galc.client.teamlead;

import java.util.Map;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SpecCodeMaskUnitsTable extends BaseFXMLTable<SpecCodeMaskUnitsDTO> {
	
	MfgMaintFXMLPane controller;
	
	TableView<SpecCodeMaskUnitsDTO> tblView = null;
	
	public SpecCodeMaskUnitsTable(MfgMaintFXMLPane controller,
			TableView<SpecCodeMaskUnitsDTO> t,
			Map<String, TableColumn<SpecCodeMaskUnitsDTO, String>> columnMap) {
		super(t, columnMap, false, true);
		this.controller = controller;
		this.tblView = t;
		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
}
