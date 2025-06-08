package com.honda.galc.client.teamleader.qi.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.DataValidationDto;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.QiDataValidationService;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DataValidationDialog</code> is the Dialog  class for validating regional screens against local.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>3/10/2017</TD>

 * </TABLE>
*/
 public class DataValidationDialog extends FxDialog {
	
	 private static final Logger logger = Logger.getLogger();
	private LoggedButton okButton;
	private ObjectTablePane<DataValidationDto> statusTablePane;
	List<Callable<String>> taskList = new ArrayList<Callable<String>>();
    List<Future<String>> resultList = new ArrayList<Future<String>>();
    private static double progressCounter = 0.0;
    Label updateLabel;
    ProgressBar progress;
    Task mainTask;
    boolean isLocalSiteImpact=true;
    String screenName;
	List<?> validateIdList;
	List<String> responseList= new ArrayList<String>();
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
    

	public DataValidationDialog(String title,Stage owner,String screenName , List<?> validateIdList) {
		super(title,owner);
		initComponents();
		this.validateIdList=validateIdList;
		this.screenName=screenName;
		runTask();
	}
	
	public DataValidationDialog(String title,String applicationId,String screenName,List<?> validateIdList) {
		super(title,applicationId);
		initComponents();
		this.validateIdList=validateIdList;
		this.screenName=screenName;
		runTask();
	}

	
	/**
	 * This method is used to create layout of Local Validation Dialog
	 */
	public void initComponents() {
		double screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane pane = new MigPane("insets 10 10 10 10", "[center,grow]", "");
		Label  label=UiFactory.createLabel("lbl", "List of local sites against which regional data is validated", Fonts.SS_DIALOG_BOLD(12));
		okButton = createBtn(QiConstant.OK, null);
        updateLabel = new Label("Calling Webservice...");
        progress = new ProgressBar();
        progress.setPrefWidth(screenWidth/4);
        okButton.setDisable(true);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				close();
			}
		});
		pane.add(updateLabel,"wrap");
		pane.add(progress,"wrap");
		pane.add(label,"wrap");
		pane.add(createStatusTable(),"wrap");
		pane.add(okButton,"wrap");
		pane.setPrefWidth(screenWidth/2);
		pane.setPrefHeight(screenHeight/1.7);
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}
	
	/**
	 *This method is used to create status table pane
	 *@return ObjectTablePane<LocalValidationDto>
	 */
	private ObjectTablePane<DataValidationDto> createStatusTable() {
		Double[] columnWidth = new Double[] { 0.05,0.12 ,0.08,0.20};
		ColumnMappingList columnMappingList = ColumnMappingList.with("Site", "SITENAME").put("Url","URL").put("Impact","STATUS").put("Comment","COMMENT");
		statusTablePane= new ObjectTablePane<DataValidationDto>(columnMappingList, columnWidth);
		statusTablePane.setPadding(new Insets(10));
		return statusTablePane;
	}
	
	@SuppressWarnings("unchecked")
	private void runTask() {
		final Map<String, String> allResults = getSitesForValidation();
		if(allResults==null || allResults.isEmpty()){
			progress.setVisible(false);
			isLocalSiteImpact=true;
			okButton.setDisable(false);
			updateLabel.setText("No local sites are configured.");
			return;
		}
		mainTask = new Task<Void>() {
            @Override
           protected Void call() throws Exception {
            	final int noOfThreads=allResults.size();
                ExecutorService pool = Executors.newFixedThreadPool(noOfThreads);
                Iterator entries = allResults.entrySet().iterator();
                while (entries.hasNext()) {
                	  Entry thisEntry = (Entry) entries.next();
                	  final String key = thisEntry.getKey().toString();
                	  final String value = thisEntry.getValue().toString();
                	  taskList.add(new Callable<String>() {
        		        public String call() throws Exception {
        		            // Web Service Call
        		        	String response=callWebService("http://"+value);
        		            progressCounter = progressCounter + 1;
        		            updateProgress(progressCounter, noOfThreads);
        		            updateMessage("Validation Progress : " + String.valueOf(new DecimalFormat("#.##").format((progressCounter/noOfThreads)*100)) + "% complete");
        		            updateStatusTablePane(key, "http://"+value,response);
        		            responseList.add(response);
        		            return response;
        		        }
        		    });
                }
                try {
                	resultList.clear();
                	resultList = pool.invokeAll(taskList);
        		} catch (InterruptedException e) {
        			logger.error("Validation against local sites failed due to :"+e.getMessage());
        		}
                progressCounter = 0.0;        	    
        	    pool.shutdown();
                return null;
            }
		};

        mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent t) {
            	for(String response:responseList){
            		if(response!=null && response.equalsIgnoreCase(""))
            			isLocalSiteImpact=false;
            		
            		else{
            			isLocalSiteImpact=true;
            			break;
            			}
            	}
            	//Enable OK Button For further operation
            	if(allResults.size()==statusTablePane.getRowCount())
            		okButton.setDisable(false);
            }
        });
	    progress.progressProperty().bind(mainTask.progressProperty());
	    updateLabel.textProperty().bind(mainTask.messageProperty());

	    new Thread(mainTask).start();
	    }

	/**
	 *This method is used to get site names for validation 
	 *@return Map<String, String>
	 */
	private Map<String, String> getSitesForValidation() {
		 Map<String,String> siteMap=PropertyService.getPropertyBean(QiPropertyBean.class).getSitesForValidation();
		return siteMap;
	}
	
	/**
	 *This method is used to update status table data 
	 */
	private void updateStatusTablePane(String siteName,String url,String response) {
		int nextIndex = statusTablePane.getTable().getSelectionModel().getSelectedIndex() + 1;
        if(response==null){
        	statusTablePane.getTable().getItems().add(nextIndex, new DataValidationDto(siteName,url, "Failed to connect or validation unavailable"," "));
    	}
        else{
        	statusTablePane.getTable().getItems().add(nextIndex, new DataValidationDto(siteName,url, response.equalsIgnoreCase("")?"No":"Yes",response.equalsIgnoreCase("false")?" ":response));
        }
	}
	
	@SuppressWarnings("unchecked")
	private String callWebService(String value){
		QiDataValidationService service=HttpServiceProvider.getService(value + HTTP_SERVICE_URL_PART,QiDataValidationService.class);
		try{
			switch(QiRegionalScreenName.getType(screenName)){
			case DEFECT_MAINTENANCE : case PART_DEFECT_COMBINATION :
				return service.validateByDefectId((List<Integer>) validateIdList);
			case PART_LOCATION_COMBINATION: case INSPECTION_PART: case INSPECTION_LOCATION:
				return service.validateByLocationId((List<Integer>) validateIdList);
			case REPAIR_METHOD:
				return service.validateByRepairMethodName((List<String>) validateIdList);
			case IMAGE_MAINTENANCE:
				return service.validateByImageName((List<String>) validateIdList);
			case RESPONSIBILITY_PLANT:
				return service.validateByPlantName((List<String>) validateIdList);
			case RESPONSIBILITY_SITE:
				return service.validateBySiteName((List<String>) validateIdList);
			case IMAGE_SECTION_MAINTENANCE:
				return service.validateByImageSection((List<String>) validateIdList);
			case PROCESS_POINT_GROUP:
				return service.validateByProcessPointGroup((List<RegionalProcessPointGroup>) validateIdList);
			}
		}
		catch(Exception e){
			logger.error("Calling webservice failed due to :"+e.getMessage());
		}
		return null;
	}
	 
	 
 	/**
	 * @return the statusTablePane
	 */
	public ObjectTablePane<DataValidationDto> getStatusTablePane() {
		return statusTablePane;
	}

	/**Sets statusTablePane
	 * @param statusTablePane
	 */
	public void setStatusTablePane(ObjectTablePane<DataValidationDto> statusTablePane) {
		this.statusTablePane = statusTablePane;
	}

	/**
	 * @return the okButton
	 */
	public LoggedButton getOkButton() {
		return okButton;
	}

	/** Set okButton
	 * @param okButton
	 */
	public void setOkButton(LoggedButton okButton) {
		this.okButton = okButton;
	}
	
	public boolean isLocalSiteImpact() {
		return isLocalSiteImpact;
	}

}