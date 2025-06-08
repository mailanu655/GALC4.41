package com.honda.galc.client.teamlead;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

public class AddEditSpecCodeMask extends FxDialog implements
		EventHandler<javafx.event.ActionEvent> {

	@FXML
	private Button applyButton;

	@FXML
	private Button selectAll;

	@FXML
	private Button deselectAll;

	@FXML
	private ListView<String> specCodeMasksListView;

	@FXML
	private ChoiceBox<String> modelsComboBox;

	@FXML
	private TextField specCodeTextField;

	@FXML
	private ScrollPane gridScrollPane;

	private List<MCOperationMatrix> availableMatrixInfo = null;

	private Map<String, List<MCOperationMatrix>> availableMatrixOperationsMapping = null;

	private List<CheckBox> checkBoxesList = null;

	private Map<String, Set<String>> mapModelModelTypes = null;

	private List<MCOperationPartMatrix> availablePartMatrixInfo;

	private Map<String, List<MCOperationPartMatrix>> availablePartMatrixOperationsMapping = null;

	private String planCode;

	private ObservableList<SpecCodeMaskUnitsDTO> assignedList = null;

	public AddEditSpecCodeMask(String planCode,
			List<MCOperationMatrix> availableMatrixInfo,
			List<MCOperationPartMatrix> availablePartMatrixInfo, ObservableList<SpecCodeMaskUnitsDTO> observableList) {
		super("Add Edit Spec Code Mask", ClientMainFx.getInstance().getStage(),
				true);
		this.planCode = planCode;
		this.availableMatrixInfo = availableMatrixInfo;
		this.availablePartMatrixInfo = availablePartMatrixInfo;
		this.assignedList  = observableList;
		fetchAllDistinctSpecCodeMasks();
		initComponents();
	}

	private void initComponents() {
		loadModelModelTypes();
		loadAvailableMatrixOperationsMapping();
		mapActions();
	}

	private void mapActions() {
		applyButton.setOnAction(this);

		selectAll.setOnAction(this);

		deselectAll.setOnAction(this);

		modelsComboBox.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String oldModel, String newModel) {
						GridPane gridPane = loadModelTypes(newModel);
						gridScrollPane.setContent(gridPane);
					}

				});

	}

	private GridPane loadModelTypes(String newModel) {

		GridPane modelTypesGridPane = new GridPane();
		int columns = (int) (gridScrollPane.getPrefWidth() / 70);
		for (int i = 0; i < columns; i++) {
			modelTypesGridPane.addColumn(i);
		}
		modelTypesGridPane.setVgap(20);
		modelTypesGridPane.setHgap(10);
		Set<String> modelTypesSet = mapModelModelTypes.get(newModel);
		checkBoxesList = new ArrayList<CheckBox>();
		Iterator<String> itr = modelTypesSet.iterator();
		int i = 0;
		int j = -1;
		while (itr.hasNext()) {
			if (i % columns == 0) {
				j++;
				modelTypesGridPane.addRow(j);
			}
			CheckBox box = new CheckBox(itr.next());
			checkBoxesList.add(box);
			modelTypesGridPane.add(box, i % columns, j);
			i++;
		}

		return modelTypesGridPane;
	}

	private void selectDeslect(Boolean selected) {
		for (CheckBox box : checkBoxesList)
			box.setSelected(selected);
	}

	private void loadAvailableMatrixOperationsMapping() {
		availableMatrixOperationsMapping = DTOConverter
				.convertMatrixInfoToSpecOperationMapping(availableMatrixInfo);
		availablePartMatrixOperationsMapping = DTOConverter
				.convertPartMatrixInfoToSpecOperationMapping(availablePartMatrixInfo);

	}

	private void loadModelModelTypes() {
		mapModelModelTypes = DTOConverter
				.convetMatrixInfoToModelModelTypesMapping(availableMatrixInfo);
		modelsComboBox.getItems().addAll(mapModelModelTypes.keySet());

	}

	private void fetchAllDistinctSpecCodeMasks() {

		List<String> specCodesList = getDao(PreProductionLotDao.class)
				.findDistinctSpecCodeByPlanCode(planCode);
		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		final FilteredList<String> filteredData = new FilteredList<String>(
				FXCollections.observableArrayList(specCodesList));

		// 2. Set the filter Predicate whenever the filter changes.
		specCodeTextField.textProperty().addListener(
				new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String arg1, final String arg2) {
						filteredData.setPredicate(new Predicate<String>() {
							public boolean test(String arg0) {
								if (arg2 == null || arg2.isEmpty()) {
									return true;
								}
								
								if (CommonPartUtility.verification(arg0,
										arg2,
										PropertyService.getPartMaskWildcardFormat())) {
									return true;
								}
								return false;
							}
						});
					}
					
					
				});
		specCodeTextField.setText(null);
		Platform.runLater(new Runnable() {
		     public void run() {
		    	 specCodeTextField.requestFocus();
		    	 specCodeMasksListView.getSelectionModel().selectedItemProperty()
		  		.addListener(new ChangeListener<String>() {
		  			public void changed(ObservableValue<? extends String> arg0,
		  					String oldVal, final String newVal) {
		  					if(StringUtils.isNotBlank(newVal)) {
		  						Platform.runLater(new Runnable() {
		  						     public void run() {
		  						    	specCodeTextField.setText(StringUtils.trimToEmpty(newVal)+"*");
		  						     }
		  						});
		  					}
		  			}
		  		});
		     }
		});
		// 3. Wrap the FilteredList in a SortedList.
		SortedList<String> sortedData = new SortedList<String>(filteredData);
		specCodeMasksListView.setItems(sortedData);

	}

	public void handle(ActionEvent event) {
		if (event.getSource() == applyButton) {

			String specCodeText = StringUtils.trim(specCodeTextField.getText());
			if (specCodeText == null || StringUtils.isEmpty(specCodeText)) {
				MessageDialog.showError("Please Enter Spec Code Mask");
				return;
			}
			
			if(specCodeMasksListView.getItems().size() == 0) {
				MessageDialog.showError("Please Enter a Valid Spec Code Mask");
				return;
			}

			String selectedModel = modelsComboBox.getSelectionModel()
					.getSelectedItem();

			if (selectedModel == null || StringUtils.isEmpty(selectedModel)) {
				MessageDialog.showError("Please Select Model");
				return;
			}

			List<String> selectedSpecCodeMasks = new ArrayList<String>();
			for (CheckBox box : checkBoxesList) {
				if (box.isSelected())
					selectedSpecCodeMasks.add(selectedModel + box.getText()
							+ "*");
			}

			if (selectedSpecCodeMasks.size() == 0) {
				MessageDialog.showError("Please Select Model Types");
				return;
			}
			
			for(SpecCodeMaskUnitsDTO dto : assignedList) {
				if(dto.getSpecCodeMask().equals(specCodeText)) {
					MessageDialog.showError("Process Already Mapped to the Entered Spec Code Mask");
					return;
				}
			}
			
			saveMatrixInfo(selectedSpecCodeMasks);

			this.close();

		} else if (event.getSource() == selectAll) {
			String selectedModel = modelsComboBox.getSelectionModel()
					.getSelectedItem();

			if (selectedModel == null || StringUtils.isEmpty(selectedModel)) {
				MessageDialog.showError("Please Select Model");
				return;
			}
			selectDeslect(true);
		} else if (event.getSource() == deselectAll) {
			String selectedModel = modelsComboBox.getSelectionModel()
					.getSelectedItem();

			if (selectedModel == null || StringUtils.isEmpty(selectedModel)) {
				MessageDialog.showError("Please Select Model");
				return;
			}
			selectDeslect(false);
		}

	}
	
	private void saveMatrixInfo(List<String> selectedSpecCodeMasks) {
		Set<MCOperationMatrix> addedMatrixSet = new HashSet<MCOperationMatrix>();
		Iterator<Entry<String, List<MCOperationMatrix>>> itr = availableMatrixOperationsMapping
				.entrySet().iterator();

		Set<MCOperationPartMatrix> addedPartMatrixSet = new HashSet<MCOperationPartMatrix>();
		Iterator<Entry<String, List<MCOperationPartMatrix>>> itr2 = availablePartMatrixOperationsMapping
				.entrySet().iterator();

		for (String selectedSpecCodeMask : selectedSpecCodeMasks) {

			while (itr.hasNext()) {
				Map.Entry<String, List<MCOperationMatrix>> pair = (Map.Entry<String, List<MCOperationMatrix>>) itr
						.next();
				String specCodeMask = pair.getKey().trim();
				String key = ProductSpec.extractModelYearCode(specCodeMask)
						+ ProductSpec.extractModelCode(specCodeMask)
						+ ProductSpec.extractModelTypeCode(specCodeMask) + "*";
				if (selectedSpecCodeMask.equals(key)) {
					for (MCOperationMatrix matrix : pair.getValue()) {
						matrix.getId().setSpecCodeMask(
								specCodeTextField.getText());
						addedMatrixSet.add(matrix);
					}
				}
			}

			while (itr2.hasNext()) {
				Map.Entry<String, List<MCOperationPartMatrix>> pair = (Map.Entry<String, List<MCOperationPartMatrix>>) itr2
						.next();
				String specCodeMask = pair.getKey().trim();
				String key = ProductSpec.extractModelYearCode(specCodeMask)
						+ ProductSpec.extractModelCode(specCodeMask)
						+ ProductSpec.extractModelTypeCode(specCodeMask) + "*";
				if (selectedSpecCodeMask.equals(key)) {
					for (MCOperationPartMatrix matrix : pair.getValue()) {
						matrix.getId().setSpecCodeMask(
								specCodeTextField.getText());
						addedPartMatrixSet.add(matrix);
					}
				}
			}

		}

		List<MCOperationMatrix> addedMatrixList = new ArrayList<MCOperationMatrix>(
				addedMatrixSet);
		ServiceFactory.getDao(MCOperationMatrixDao.class).saveAll(
				addedMatrixList);

		List<MCOperationPartMatrix> addedPartMatrixList = new ArrayList<MCOperationPartMatrix>(
				addedPartMatrixSet);
		ServiceFactory.getDao(MCOperationPartMatrixDao.class).saveAll(
				addedPartMatrixList);
	}

}
