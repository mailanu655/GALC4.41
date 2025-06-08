package com.honda.galc.client.teamleader.qi.controller;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.ImportMtcModelModel;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryModelImportExportTableSummaryXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryModelTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryScreenDefectCombinationTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryScreenTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiLocalDefectCombinationTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiMtcModelImportExportConfigObjectFactory;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiTextEntryMenuTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QicsMtcModelConfigurationXmlDto;
import com.honda.galc.client.teamleader.qi.view.ImportMtcModelDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.sun.prism.paint.Color;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class ImportMtcModelController extends QiDialogController<ImportMtcModelModel,ImportMtcModelDialog> {
	
	public class MyEntryModel {
		private String plant = "";
		private String site = "";
		private String entryModel = "";
		private String entryModelDesc = "";
		public String getPlant() {
			return plant;
		}
		public String getEntryModel() {
			return entryModel;
		}
		public String getEntryModelDesc() {
			return entryModelDesc;
		}
		public String getSite() {
			return site;
		};
		public String toString()  {
			StringBuilder sb = new StringBuilder();
			sb.append(site).append("/")
			.append(plant).append("/")
			.append(entryModel);
			return sb.toString();
		}
	}
	MyEntryModel source;
	MyEntryModel dest;
	ExecutorService myExecutor = Executors.newSingleThreadExecutor();
	private volatile boolean isClose = false;
	private volatile boolean isRunning = false;
	
	public ImportMtcModelController(ImportMtcModelModel model,ImportMtcModelDialog dialog) {
		super();
		setModel(model);
		setDialog(dialog);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initListeners() {
		validationForTextfield();
		addPlantComboboxListener();
		
		getDialog().getPlantCombobox().valueProperty().addListener(updateEnablerForStringValueChange);
		
	}
	
	public void handleException(String loggerMsg, String errMsg,
			String parentScreen, Exception e) {
		getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.ERROR));
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();			
			if(QiConstant.IMPORT.equals(loggedButton.getText())) importBtnAction(actionEvent);
			else if(QiConstant.CANCEL.equals(loggedButton.getText())) cancelBtnAction(actionEvent);
		} 
	}

	
	private void cancelBtnAction(ActionEvent actionEvent) {
		try {
			Stage stage = (Stage) getDialog().getCancelBtn().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action",e);
		}
	}
	
	public boolean onClose()  {
		if(!isRunning())  return true;
		else  {
			StringBuilder sb = new StringBuilder();
			sb.append("If you cancel, the import will be partially complete.  Are you sure you wish to cancel?");
    		if (!MessageDialog.confirm(getDialog(), sb.toString())) {
    		   	return false;
    		}
    		else  {
    			setClose(true);
    			return true;
    		}
		}
	}
	
    private void selectImportFile(ActionEvent actionEvent) {
		
    	if(!validateDest())  return;
    	String toPlant = getDialog().getPlantCombobox().getSelectionModel().getSelectedItem().toString();
    	String toSite = getModel().getSiteName();
    	String toEntryModel = getDialog().getEntryModelText().getText();
    	String toEntryModelDesc = getDialog().getEntryModelDescText().getText();
		
		FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
 
    	final File importFile = fileChooser.showOpenDialog(ClientMainFx.getInstance().getStage());
    	if(importFile == null)  {
		   	getDialog().getStatusTextArea().appendText("User cancelled");  
		   	return;
    	}
		ObjectInputStream is =  null;
		try {
			is = getXsInputStream(importFile);
		} catch (IOException e1) {
			getLogger().error(e1);
    		closeFile(is);
    		setRunning(false);
		}

    	try {
    
			String fromEntrySite = (String)is.readObject();
			String fromEntryPlant = (String)is.readObject();
			String fromEntryModel = (String)is.readObject();
			if(!StringUtils.isBlank(fromEntryModel))  {
				StringBuilder sb = new StringBuilder();
				sb.append("Confirm import:");
				sb.append("\nFrom:\t").append(fromEntrySite).append("/")
				.append(fromEntryPlant).append("/")
				.append(fromEntryModel).append("\n")
				.append("\nTo:\t\t").append(toSite).append(toPlant).append(toEntryModel);
				
        		if (!MessageDialog.confirm(getDialog(), sb.toString())) {
        		   	getDialog().getStatusTextArea().appendText("User cancelled");
        		}
				else  {
					final ObjectInputStream isFinal = is;
					Runnable r = () -> {
						try {
							getDialog().getCancelBtn().setDisable(true);
							processXmlFile(isFinal, importFile, toPlant, toEntryModel, toEntryModelDesc);
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex);
						}
						finally  {
				    		closeFile(isFinal);
							getDialog().getCancelBtn().setDisable(false);
						}
					};
					setClose(false);
					getMyExecutor().submit(r);
				}
			}
		}
    	catch (Exception ex) {
			closeFile(is);
			getLogger().error(ex);
		}
	}
    
    
    private void closeFile(InputStream is)  {
		try {
			if(is != null)  {
				is.close();
			}
		} catch (Exception ex) {
			getLogger().error(ex);
			ex.printStackTrace();
		}    	
    }
    
    
    private int getTotalImportCount(List<QiEntryModelImportExportTableSummaryXmlDto> summaryList)  {
    	int count = 0;
    	if(summaryList == null || summaryList.isEmpty())  return 0;
    	else  {
    		for(QiEntryModelImportExportTableSummaryXmlDto dto : summaryList)  {
    			count += dto.getRecordCount();
    		}
    		return count + summaryList.size();
    	}
    }
    
    private void processXmlFile(ObjectInputStream is,File importFile,String plant, String entryModel, String entryModelDesc)  throws Exception {
    	int[] counts = {0,0,0,0};
    	int totalCount = 0, subCount = 0;
    	String currentTag = "";
		StringBuilder sb = new StringBuilder();
		List<QiEntryModelImportExportTableSummaryXmlDto> summaryList = new ArrayList<QiEntryModelImportExportTableSummaryXmlDto>();
	   	sb.append("\nStarting import... ");
	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
	   	Object lastProcessed = null;
	   	setRunning(true);
	   	long t1 = System.currentTimeMillis();
	   	try {
			while(!isClose())  {
				Object obj = null;
				if(totalCount > 4 && totalCount%100 == 0)  {
					
					sb.setLength(0);
					sb.append(String.format("Processing %s: %d", currentTag, subCount))
							.append(
									String.format("\nTotal tags processed: %d of %d", totalCount,
									getTotalImportCount(summaryList))
									);
		  		   	Platform.runLater(() -> getDialog().setMessageBig(
		  		   			String.format(sb.toString()),
		  		   			javafx.scene.paint.Color.YELLOWGREEN
		  		   			));						
				}
				try {
					obj = is.readObject();
				} catch (EOFException ex)  {
					break;
					
				} catch (Exception ex) {
					throw ex;
				}
				if(obj instanceof QiEntryModelImportExportTableSummaryXmlDto)  {
					summaryList.add((QiEntryModelImportExportTableSummaryXmlDto)obj);
				}
				else if(obj instanceof QiEntryScreenTbxXmlDto)  {
					if(counts[0] == 0)   {
						int nItems = 0;
						subCount = 0;
						currentTag = "QiEntryScreenTbxXmlDto";
						if(summaryList.get(0) != null)  {
							nItems = summaryList.get(0).getRecordCount();
						}
			  		   	sb.setLength(0);
			  		   	sb.append("\nImporting entry screens...").append(nItems);
			  		   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));						
					}
					processEntryScreen((QiEntryScreenTbxXmlDto)obj,plant,entryModel);
					counts[0]++;
				}
				else if(obj instanceof QiTextEntryMenuTbxXmlDto)  {
					if(counts[1] == 0)   {
						int nItems = 0;
						subCount = 0;
						currentTag = "QiTextEntryMenuTbxXmlDto";
						if(summaryList.get(1) != null)  {
							nItems = summaryList.get(1).getRecordCount();
						}
			  		   	sb.setLength(0);
			  		   	sb.append("\nImported entry screens: ").append(counts[0]);
			  		   	sb.append("\nImporting text entry menus...").append(nItems);
			  		   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
					}
				   	processTextEntryMenu((QiTextEntryMenuTbxXmlDto)obj,plant,entryModel);
					counts[1]++;
				}
				else if(obj instanceof QiEntryScreenDefectCombinationTbxXmlDto)  {
					if(counts[2] == 0)   {
						int nItems = 0;
						subCount = 0;
						currentTag = "QiEntryScreenDefectCombinationTbxXmlDto";
						if(summaryList.get(2) != null)  {
							nItems = summaryList.get(2).getRecordCount();
						}
						sb.setLength(0);
						sb.append("\nImported text entry menus: ").append(counts[1]);
			  		   	sb.append("\nImporting entry screen defect combinations...").append(nItems);
						Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
					}
					processESDC((QiEntryScreenDefectCombinationTbxXmlDto)obj,plant,entryModel);
					counts[2]++;
				}
				else if(obj instanceof QiLocalDefectCombinationTbxXmlDto)  {
				   	if (counts[3] == 0) {
						int nItems = 0;
						subCount = 0;
						currentTag = "QiLocalDefectCombinationTbxXmlDto";
						if(summaryList.get(3) != null)  {
							nItems = summaryList.get(3).getRecordCount();
						}
						sb.setLength(0);
						sb.append("\nImported entry screen defect combinations: ").append(counts[2]);
			  		   	sb.append("\nSkipping local defect combinations...").append(nItems);
						Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
					}
					counts[3]++;
				}
				totalCount++;
				subCount++;
				lastProcessed = obj;
			}
			//create entry model
			getModel().saveEntryModel(entryModel, entryModelDesc);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex);
			sb.setLength(0);
			sb.append("\nImport failed: last successful is\n").append(lastProcessed.toString());			
			Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
		   	setRunning(false);
			throw ex;
		}
	   	setRunning(false);
		sb.setLength(0);
		if(!isClose)  {
			sb.append("\nSkipped local defect combinations: ").append(counts[3]);
			sb.append("\nImport completed\n");
			getLogger().info(sb.toString());
			Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
			Platform.runLater(() -> getDialog().setMessageBig("Import completed", javafx.scene.paint.Color.YELLOWGREEN));
		}
		else  {
			sb.append("\nImport aborted: last successful is\n").append(lastProcessed.toString());			
			getLogger().info(sb.toString());
		   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));			
			Platform.runLater(() -> getDialog().setMessageBig("Import aborted", javafx.scene.paint.Color.YELLOWGREEN));
		}
		long tSec = (System.currentTimeMillis() - t1)/1000;					
	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(
	   			String.format("\nTotal time: %d min, %d sec", tSec/60, tSec%60)));					

    }
    
    
    private void processEntryScreen(QiEntryScreenTbxXmlDto dto,String plant, String entryModel)  {
    	if(dto == null)  return;
    	QiEntryScreen entity = QiMtcModelImportExportConfigObjectFactory.getQiEntryScreenEntity(dto);
    	entity.getId().setEntryModel(entryModel.toUpperCase());
    	entity.setUpdateUser(getUserId());
    	entity.setCreateUser(getUserId());
    	entity.setProductType(getModel().getProductType());
    	entity.setActive(true);
    	entity.getId().setIsUsed((short)0);
    	entity.setScreenIsUsed((short)0);
    	getModel().saveEntryScreen(entity);
    }
    
    private void processESDC(QiEntryScreenDefectCombinationTbxXmlDto dto,String plant, String entryModel)  {
    	if(dto == null)  return;
    	QiEntryScreenDefectCombination entity = QiMtcModelImportExportConfigObjectFactory.getQiEntryScreenDefectCombinationTbxXmlDtoEntity(dto);	
    	entity.getId().setEntryModel(entryModel.toUpperCase());
    	entity.getId().setIsUsed((short)0);
    	entity.setUpdateUser(getUserId());
    	entity.setCreateUser(getUserId());
    	getModel().saveEntryScreenDefectCombination(entity);
   }
    
    private void processTextEntryMenu(QiTextEntryMenuTbxXmlDto dto,String plant, String entryModel)  {
    	if(dto == null)  return;
    	QiTextEntryMenu entity = QiMtcModelImportExportConfigObjectFactory.getQiTextEntryMenuTbxXmlDtoEntity(dto);  
    	entity.getId().setEntryModel(entryModel.toUpperCase());
    	entity.setUpdateUser(getUserId());
    	entity.setCreateUser(getUserId());
    	entity.getId().setIsUsed((short)0);
    	getModel().saveTextEntryMenu(entity);
   }
    
    private void processLDC(QiLocalDefectCombinationTbxXmlDto dto,String plant, String entryModel)  {
    	if(dto == null)  return;
    	QiLocalDefectCombination entity = QiMtcModelImportExportConfigObjectFactory.getQiLocalDefectCombinationTbxXmlDtoEntity(dto);
    	entity.setEntryModel(entryModel.toUpperCase());
    	entity.setEntryPlantName(plant);
    	entity.setUpdateUser(getUserId());
    	entity.setCreateUser(getUserId());
    	entity.setResponsibleLevelId(0);
    	entity.setPddaResponsibilityId(0);
    	entity.setLocalTheme("");
    	entity.setLocalDefectCombinationId(null);
    	getModel().saveLocalDefectCombination(entity);
    }
    
	private void importBtnAction(ActionEvent actionEvent) {
		getDialog().clearStatusMessage();
		selectImportFile(actionEvent);
		
	}

	private boolean validateEntryModel(String myEntryModel, String myPlant)  {
		if(myEntryModel == null || StringUtils.isBlank(myEntryModel))  {
			getDialog().setErrorMessage("Please specify the new entry model");
			return false;
		}
		else if(getModel().hasEntryModel(myEntryModel, myPlant))  {
			getDialog().setErrorMessage("EntryModel exists");
			return false;
		}
		return true;
	}
	
	private boolean validateDest()  {
		LoggedComboBox<String> plantComboBox = getDialog().getPlantCombobox();
		if(plantComboBox.getSelectionModel().getSelectedItem() == null ||
				StringUtils.isBlank(plantComboBox.getSelectionModel().getSelectedItem().toString()))  {
			getDialog().setErrorMessage("Please select Plant");
			return false;
		}
		else if(!validateEntryModel(getDialog().getEntryModelText().getText(), plantComboBox.getSelectionModel().getSelectedItem().toString()))  {
			return false;
		}
		return true;
	}
	

	@SuppressWarnings("unchecked")
	private void addPlantComboboxListener() {
		getDialog().getPlantCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			}
		});		
	}
	
	private void validationForTextfield(){
		getDialog().getStatusTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));		
	}


	private ObjectInputStream getXsInputStream(File importFile) throws IOException {
		
		//import/export
	    XStream xs = getXStream();
	    BufferedInputStream exIn = null;
	    try {
	    	exIn = new BufferedInputStream(new FileInputStream(importFile));
	    	ObjectInputStream xsIn = xs.createObjectInputStream(new InputStreamReader(exIn));
	    	return xsIn;
	    
		} catch (IOException ex) {
			getLogger().error(ex);
			throw ex;
		}
	}


	private XStream getXStream()  {
	    XStream xstream = new XStream(new StaxDriver());
	    xstream.setClassLoader(this.getClass().getClassLoader());
	    xstream.alias("qiEntryModelConfigurationTbxXmlDto", QicsMtcModelConfigurationXmlDto.class);
	    xstream.alias("qiEntryModelImportExportTableSummaryXmlDto", QiEntryModelImportExportTableSummaryXmlDto.class);
	    xstream.alias("qiLocalDefectCombinationTbxXmlDto", QiLocalDefectCombinationTbxXmlDto.class);
	    xstream.alias("qiEntryScreenDefectCombinationTbxXmlDto", QiEntryScreenDefectCombinationTbxXmlDto.class);
	    xstream.alias("qiTextEntryMenuTbxXmlDto", QiTextEntryMenuTbxXmlDto.class);
	    xstream.alias("qiEntryModelTbxXmlDto", QiEntryModelTbxXmlDto.class);
	    xstream.alias("qiEntryScreenTbxXmlDto", QiEntryScreenTbxXmlDto.class);
	    xstream.addImplicitCollection(QicsMtcModelConfigurationXmlDto.class, "qiEntryModelImportExportTableSummaryXmlDto");
	    xstream.addImplicitCollection(QicsMtcModelConfigurationXmlDto.class, "qiLocalDefectCombinationTbxXmlDto");
	    xstream.addImplicitCollection(QicsMtcModelConfigurationXmlDto.class, "qiEntryScreenDefectCombinationTbxXmlDto");
	    xstream.addImplicitCollection(QicsMtcModelConfigurationXmlDto.class, "qiTextEntryMenuTbxXmlDto");
	    xstream.addImplicitCollection(QicsMtcModelConfigurationXmlDto.class, "qiEntryModelTbxXmlDto");
	    xstream.addImplicitCollection(QicsMtcModelConfigurationXmlDto.class, "qiEntryScreenTbxXmlDto");
		return xstream;
	}

	/**
	 * @return the myExecutor
	 */
	public ExecutorService getMyExecutor() {
		return myExecutor;
	}

	/**
	 * @return the isClose
	 */
	public boolean isClose() {
		return isClose;
	}

	/**
	 * @param isClose the isClose to set
	 */
	public void setClose(boolean isClose) {
		this.isClose = isClose;
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @param isRunning the isRunning to set
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}		
}
