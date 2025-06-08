package com.honda.galc.client.teamlead;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.dto.MCOperationRevisionDTO;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;

public class ProcessPointChooser {

	private String site;
	private String selectedPlant;
	private String selectedDivision;
	private ChoiceBox<String> plants;
	private ChoiceBox<String> divisions;
	private ChoiceBox<String> processPoints;
	private TableView<MCOperationRevisionDTO> tblView;
	private TableView<MCOperationMatrixDTO> opMatrixTable;
	private TableView<MCOperationPartRevisionDTO> partsTable;
	MfgMaintFXMLPane controller;
	MCOperationsTable table;
	MCOperationMatrix table2;

	public ProcessPointChooser(MfgMaintFXMLPane controller,
			ChoiceBox<String> plants, ChoiceBox<String> divisions,
			ChoiceBox<String> processPoints, String site,
			TableView<MCOperationRevisionDTO> tblView, MCOperationsTable table,
			TableView<MCOperationPartRevisionDTO> partsTable,
			TableView<MCOperationMatrixDTO> table2) {

		this.plants = plants;
		this.divisions = divisions;
		this.processPoints = processPoints;
		this.site = site;
		this.tblView = tblView;
		this.table = table;
		this.opMatrixTable = table2;
		this.partsTable = partsTable;
		this.controller = controller;
		populatePlants(site);
		handleSelections();

	}

	private void handleSelections() {
		plants.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String arg1, String arg2) {
						ProcessPointChooser.this.selectedPlant = arg2;

						populateDivisions(arg2);

					}
				});

		divisions.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String arg1, String arg2) {

						if (arg2 != null) {

							StringTokenizer tokens = new StringTokenizer(arg2, "  (");
							String div = tokens.nextToken();
							
							ProcessPointChooser.this.selectedDivision = arg2;
							populateProcessPoints(selectedPlant, div);
						}

					}
				});

		processPoints.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String arg1, String arg2) {

						if (arg2 != null) {

							StringTokenizer tokens = new StringTokenizer(arg2);

							String pp = tokens.nextToken();
							ProcessPointChooser.this.controller
									.setSelectedProcessPoint(pp);
							// ProcessPointChooser.this.controller.getOpPartAccordian().
							populateView(pp);
						}

					}
				});
	}

	protected void populateView(String selectedPP) {

		List<MCOperationRevision> opList = ServiceFactory.getDao(
				MCOperationRevisionDao.class)
				.findAllByProcessPointAndPlatform(selectedPP, controller.getPlantValue(), controller.getDepartmentValue(), 
						new BigDecimal(controller.getModelYearValue()), new BigDecimal(controller.getProductionRateValue()), 
						controller.getLineNoValue(), controller.getVmcValue());
		controller.getOpPartTblView().setItems(null);
		controller.getOpMatrixTblView().setItems(null);
		table.init(partsTable, opMatrixTable,
				FXCollections.observableList(DTOConverter
						.convertMcoperationRevision(opList)));
	}

	public void populatePlants(String site) {
		List<Plant> list = ServiceFactory.getDao(PlantDao.class).findAllBySite(
				site);
		List<String> plist = new ArrayList<String>();
		for (Plant p : list) {

			plist.add(p.getPlantName());

		}
		plants.setItems(FXCollections.observableList(plist));

	}

	public void populateDivisions(String plant) {

		List<Division> list = ServiceFactory.getDao(DivisionDao.class)
				.findById(site, plant);
		List<String> plist = new ArrayList<String>();
		for (Division p : list) {

			plist.add(p.getDivisionId() + "  (" + p.getDivisionName() + ")");

		}
		divisions.setItems(FXCollections.observableList(plist));

	}

	public void populateProcessPoints(String plant, String division) {

		Division div = new Division();
		div.setDivisionId(division);
		div.setPlantName(plant);
		div.setSiteName(site);

		// Plant p = new Plant();
		// PlantId id = new PlantId();
		// id.setPlantName(plant);
		// id.setSiteName(site);
		// p.setId(id);

		// div.setPlant(p);

		List<ProcessPoint> list = ServiceFactory.getDao(ProcessPointDao.class)
				.findAllByDivision(div);

		List<String> plist = new ArrayList<String>();
		for (ProcessPoint pp : list) {

			plist.add(pp.getProcessPointId() + "  "
					+ pp.getProcessPointDescription());

		}

		processPoints.setItems(FXCollections.observableList(plist));

	}

}
