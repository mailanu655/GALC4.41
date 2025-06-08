package com.honda.galc.client.teamlead.mdrs;

import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.honda.galc.client.dto.GALCPeriodDTO;
import com.honda.galc.client.dto.MdrsQuarterDTO;
import com.honda.galc.client.teamlead.BaseFXMLTable;

public class GALCPeriodTable extends BaseFXMLTable {
	TableView<GALCPeriodDTO> tblView = null;
	
	public GALCPeriodTable(
			TableView<com.honda.galc.client.dto.GALCPeriodDTO> t,
			Map<String, TableColumn<GALCPeriodDTO, String>> columnMap) {

		super(t, columnMap, false);
		tblView = t;

	}
	
	public void init(List<GALCPeriodDTO> list) {
		tblView.setEditable(false);
		tblView.setItems(FXCollections.observableList(list));
	}

}
