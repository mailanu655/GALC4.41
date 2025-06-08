package com.honda.galc.client.dc.control;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SearchCriteriaPanel extends FxDialog implements
EventHandler<javafx.event.ActionEvent>{
	private static SearchCriteriaPanel instance;
	private static String STYLESHEET = "resource/com/honda/galc/client/dc/view/UnitNavigatorWidget.css";
	private GridPane grid;
	private Group group;
	private Stage dialog;
	private Scene scene;
	private LoggedLabel errMsgLabel;
    private LoggedButton findButton;
    private LoggedButton closeButton;
    private String[]  filterCriterias;
    private DataCollectionPropertyBean dataCollectionPropertyBean;
    private DataCollectionController controller;
    
    private List<MCOperationRevision> allOperations;
    
	private SearchCriteriaPanel(DataCollectionController controller) {
		super("");
		initComponents(controller);
		init();
	}
	
	public static void prepare(DataCollectionController controller) {
		reset();
		instance = new SearchCriteriaPanel(controller);
	}
	
	public static SearchCriteriaPanel getInstance() {
		return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	public List<MCOperationRevision> getAllOperations() {
		return allOperations;
	}

	public void showPanel() {
		dialog.show();
	}
	
	private void initComponents(DataCollectionController controller) {
		this.controller = controller;
		this.allOperations = controller.getModel().getOperations();
	}
	
	private void init() {
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		dialog = new Stage();
		dialog.initStyle(StageStyle.UTILITY);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setResizable(false);
		group = new Group();
		Text scenetitle = new Text("Filter Criteria");
		scenetitle.setFont(Font.font("arial", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		group.getChildren().addAll(grid);
		dataCollectionPropertyBean = PropertyService.getPropertyBean(
				DataCollectionPropertyBean.class, controller.getProcessPointId());
		filterCriterias = dataCollectionPropertyBean.getFilterIds();

		int i = 1;

		for (String filter : filterCriterias) {
			String methodName = FilterCriteria.getComponentMethod(filter);
			String componentDescription = FilterCriteria
					.getComponentDescription(filter);
			String listValues = FilterCriteria.getListValues(filter);
			Method method;
			try {
				method = getClass().getDeclaredMethod(methodName, String.class,
						String.class, String.class, int.class, GridPane.class);
				method.invoke(this, filter, componentDescription, listValues,
						i, grid);
				i++;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		scene = new Scene(group);
		dialog.setScene(scene);
		errMsgLabel = UiFactory.createLabel("ERR_MSG");
		grid.add(errMsgLabel, 1, i++);
		HBox buttons = new HBox(10);
		findButton = UiFactory.createButton("Find");
		closeButton = UiFactory.createButton("Close");
		findButton.setId("nav-search-button");
		findButton.setStyle(STYLESHEET);
		closeButton.setId("nav-search-button");
		closeButton.setStyle(STYLESHEET);
		buttons.getChildren().add(findButton);
		buttons.getChildren().add(closeButton);
		grid.add(buttons, 1, i);
		dialog.sizeToScene();
		findButton.setOnAction(this);
		closeButton.setOnAction(this);
	}
	
	public void handle(ActionEvent event) {
		if (event.getSource() == findButton) {
			//controller.getView().getMainWindow().clearMessage();
			HashMap<String, String> hmap = new HashMap<String, String>();
			for (int i = 0; i < grid.getChildren().size(); i++) {
				if (grid.getChildren().get(i) instanceof LoggedTextField) {
					LoggedTextField t = (LoggedTextField) grid
							.getChildren().get(i);
					if (t.getText() != null
							&& StringUtils.isNotEmpty(t.getText())) {
						hmap.put(t.getId(), t.getText());
					}
				} else if (grid.getChildren().get(i) instanceof ComboBox) {
					ComboBox cb = (ComboBox) grid.getChildren().get(i);
					if (cb.getSelectionModel().getSelectedItem() != null
							&& StringUtils.isNotEmpty(cb
									.getSelectionModel().getSelectedItem()
									.toString())) {
						hmap.put(cb.getId(), cb.getSelectionModel()
								.getSelectedItem().toString());
					}
				}
	
			}
			List<MCOperationRevision> filteredList = filterCriteria(hmap);
	 	    int matchCount = (filteredList!=null)?filteredList.size():0;
			if (matchCount > 0) {
				errMsgLabel.setText(String.format("Found %d occurrences",matchCount));
				errMsgLabel.setTextFill(Color.GREEN);
				controller.getView().populateUnitNavigator(filteredList);
			} else {
				errMsgLabel.setText("No match found!");
				errMsgLabel.setTextFill(Color.RED);
			}
		}
		if (event.getSource() == closeButton) {
			dialog.hide();
		}
	}
	
	private List<MCOperationRevision>  filterCriteria(HashMap<String, String> hashMap){
	  	Set<String> keys = hashMap.keySet();
	  	List<MCOperationRevision> filteredList = new ArrayList<MCOperationRevision>(allOperations);
		for (String key : keys) {
			if (key.equals("OP_TYPE") && !hashMap.get(key).equals("NONE")) {
				List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
				for (MCOperationRevision operation: filteredList) {
					String operationType = operation.getType().toString().toUpperCase();
					if (operationType.equals(hashMap.get(key))) {
						list.add(operation);
					}
				}
				filteredList.clear();
				filteredList = list;

			} else if (key.equals("PART_MASK")) {
				List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
				for (MCOperationRevision operation: filteredList) {
					List<MCOperationPartRevision> partList = operation.getParts();
					for (MCOperationPartRevision part: partList) {
						String partMask = null;
						if (part.getPartMask() != null) {
							partMask = part.getPartMask().toUpperCase().toString();
						}
					if (partMask != null && (partMask.contains((CharSequence) hashMap.get(key).toString().toUpperCase()))) {
							list.add(operation);
						}
						
					}
				}
				filteredList.clear();
				filteredList = list;

			} else if (key.equals("INCOMPLETE_UNITS") && !hashMap.get(key).equals("NONE")) {
				List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
				for (MCOperationRevision operation: filteredList) {
					if(hashMap.get(key).equals("INCOMPLETE")) {
						if (!isComplete(operation)) {
							list.add(operation);
						}
					} else if(hashMap.get(key).equals("COMPLETE")) {
						if (isComplete(operation)) {
							list.add(operation);
						}
					}
					
				}
				filteredList.clear();
				filteredList = list;

			} else if(key.equals("PROCESS_POINT") && !hashMap.get(key).equals("NONE")) {
				List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
        		for (MCOperationRevision operation: filteredList) {
    					String operationType = operation.getStructure().getId().getProcessPointId();
    					if (operationType.contains((CharSequence) hashMap.get(key))) {
    						list.add(operation);
    					}
    	       	}
        		filteredList.clear();
				filteredList = list;
				
    		} else if(key.equals("PART_NAME")) {
    			List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
    			for (MCOperationRevision operation: filteredList) {
    	       		 List<MCOperationPartRevision> partList = operation.getParts();
    	       		 for(MCOperationPartRevision part: partList) {
    	       			String partName = part.getPartNo().toUpperCase();
    	       			 if(partName != null && (partName.contains((CharSequence) hashMap.get(key).toString().toUpperCase()))) {
    	       				 list.add(operation);
    	       			 }
    	       		 }
    	       	}
    			filteredList.clear();
				filteredList = list;
				
    		} else if(key.equals("SPECIALITY_SCREENS") && !hashMap.get(key).equals("NONE")) {
    			List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
    			for (MCOperationRevision operation: filteredList) {
    					String operationView = operation.getView();
    					String operationProcessor = operation.getProcessor();
    					String[] s1= hashMap.get(key).toString().split("/");
    					if (operationView.contains(s1[0]) && operationProcessor.contains(s1[1])) {
    						list.add(operation);
    					}
    	       	}
    			filteredList.clear();
				filteredList = list;
				
    		}else if(key.equals("TOOL_TYPE") && !hashMap.get(key).equals("NONE")) {
    			List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
    			for (MCOperationRevision operation: filteredList) {
    	       		 List<MCOperationPartRevision> partList = operation.getParts();
    	       		 for(MCOperationPartRevision part: partList) {
    	       			String toolType = null;
    	       			 if(part.getDeviceId() != null) {
    	       				toolType = part.getDeviceId().toString();
    	       			 }
    	       			if (toolType != null && toolType.contains((CharSequence) hashMap.get(key))) {
    						list.add(operation);
    					}
    	       		 }
    	       	}
    			filteredList.clear();
				filteredList = list;
				
    		}else if(key.equals("PSET_VALUE")) {
    			List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
    			for (MCOperationRevision operation: filteredList) {
    	       		 List<MCOperationPartRevision> partList = operation.getParts();
    	       		 for(MCOperationPartRevision part: partList) {
    	       			String pSetValue = null;
    	       			 if(part.getDeviceMsg() != null) {
    	       				pSetValue = part.getDeviceMsg().toUpperCase().toString();
    	       			 }
    	       			 
    	       			if (pSetValue != null && (pSetValue.contains((CharSequence) hashMap.get(key).toString().toUpperCase()))) {
    						list.add(operation);
    					}
    	       		 }
    	       	}
    			filteredList.clear();
				filteredList = list;
				
    		}else if(key.equals("ALL")) {
    			List<MCOperationRevision> list = new ArrayList<MCOperationRevision>();
    			for (MCOperationRevision operation: filteredList) {
    	       		List<MCOperationPartRevision> partList = operation.getParts();
             		 for(MCOperationPartRevision part: partList) {
             			String partNumber = part.getPartNo().toString();
             			String partName = part.getPartDesc().toString();
	   	       			 if(partNumber.equalsIgnoreCase((String) hashMap.get(key)) || partName.equalsIgnoreCase((String) hashMap.get(key))) {
	   	       				 list.add(operation);
	   	       			 }
             		 }
    	       		String operationName = operation.getId().getOperationName().toUpperCase();
    				String operationDescription = operation.getDescription().toUpperCase();
    				String operationType = operation.getType().toString().toUpperCase();
    				
    				if (operationName.contains((CharSequence) hashMap.get(key)) || operationDescription.contains((CharSequence) hashMap.get(key)) || operationType.contains((CharSequence) hashMap.get(key))) {
    					list.add(operation);
    				}
    	       	}
    			filteredList.clear();
				filteredList = list;
    		}
		}
	   	return filteredList;
	}
	
	
	private boolean isComplete(MCOperationRevision operation) {
		String operationName = operation.getId().getOperationName();
		
		if (operation.getSelectedPart() != null && DataCollectionModel.hasMeasurements(operation)) {
			int goodCount = controller.getModel().getGoodMeasurementsCount(operationName);
			int expectedCount=  operation.getSelectedPart().getMeasurementCount();
			if (goodCount != expectedCount) { 
				return false;
			}
		}

		return controller.getModel().isOperationComplete(operationName);
	}
	
	
	public void getTextBox(String filter, String componentDescription, String listValues, int i , GridPane grid){
		LoggedLabel label = UiFactory.createLabel(filter, componentDescription);
		label.setFont(Font.font("arial", FontWeight.NORMAL, 15));
		grid.add(label, 0, i);
		
		LoggedTextField textBox = UiFactory.createTextField(filter);
		textBox.setPrefWidth(200);
		textBox.setPrefHeight(10);
		grid.add(textBox, 1, i);
	}
	
	public void getComboBox(String filter, String componentDescription, String listValues,  int i , GridPane grid){
		ComboBox cb = null;
		if(filter.equals("PROCESS_POINT")) {
			String[] comboBoxValues = dataCollectionPropertyBean.getMultiProcessPoints();
			cb = new ComboBox(FXCollections.observableArrayList(comboBoxValues));
		} else if(filter.equals("OP_TYPE")) {
			cb = new ComboBox(FXCollections.observableArrayList(OperationType.values()));
		} else if (filter.equals("SPECIALITY_SCREENS")) {
			List<Object[]> viewProcessors = ServiceFactory.getService(MCOperationRevisionDao.class).findAllOperationViewAndProcessor();
			List<String> viewProcessorsList = new ArrayList<String>();
			for (Object[] opName: viewProcessors) {
				Pattern p = Pattern.compile("([^/.]+)*$");
				Matcher m1 = p.matcher(opName[0].toString().trim());
				Matcher m2 = p.matcher(opName[1].toString().trim());
				if (m1.find() && m2.find()) {
					viewProcessorsList.add(m1.group()+"/"+m2.group());
				}
			}
			cb = new ComboBox(FXCollections.observableArrayList(viewProcessorsList));
		} else if (filter.equals("TOOL_TYPE")) {
			List<Object[]> deviceIdData = ServiceFactory.getService(MCOperationMeasurementDao.class).findDistinctDeviceId();
			List<String> deviceId = new ArrayList<String>();
			for(int k=0; k<deviceIdData.size() ; k++) {
				if(deviceIdData.get(k)!=null) {
					Object obj = deviceIdData.get(k);
					deviceId.add(obj.toString().trim());	
				}
			}
			cb = new ComboBox(FXCollections.observableArrayList(deviceId));
		} else {
			List<String> comboBoxValues = Arrays.asList(listValues.split(","));
			cb = new ComboBox(FXCollections.observableArrayList(comboBoxValues));
		}
		LoggedLabel label = UiFactory.createLabel(filter, componentDescription);
		label.setFont(Font.font("arial", FontWeight.NORMAL, 15));
		grid.add(label, 0, i);
		cb.setId(filter);
		cb.setPrefWidth(200);
		cb.setPrefHeight(20);
		cb.getItems().add(0, "NONE");
		grid.add(cb, 1, i);
	}
	
}
