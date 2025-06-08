package com.honda.galc.client.teamleader.qi.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.ExportMtcModelModel;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryModelImportExportTableSummaryXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryModelTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryScreenDefectCombinationTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiEntryScreenTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiLocalDefectCombinationTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiMtcModelImportExportConfigObjectFactory;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QiTextEntryMenuTbxXmlDto;
import com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto.QicsMtcModelConfigurationXmlDto;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import com.honda.galc.client.teamleader.qi.view.ExportMtcModelDialog;

/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class ExportMtcModelController extends QiDialogController<ExportMtcModelModel,ExportMtcModelDialog> {
	
	public class MyEntryModel {
		private String plant = "";
		private String site = "";
		private String entryModel = "";
		public String getPlant() {
			return plant;
		}
		public String getEntryModel() {
			return entryModel;
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
	private volatile boolean isClose = false;
	private volatile boolean isRunning = false;
	ExecutorService myExecutor = Executors.newSingleThreadExecutor();
	
	public ExportMtcModelController(ExportMtcModelModel model, ExportMtcModelDialog dialog) {
		super();
		setModel(model);
		setDialog(dialog);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initListeners() {
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
			if(QiConstant.EXPORT.equals(loggedButton.getText())) exportBtnAction(actionEvent);
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
	

	private void exportBtnAction(ActionEvent actionEvent) {
						
				String entryModel = getModel().getEntryModel();
				String plant = (String)getDialog().getPlantCombobox().getValue();
				String site = getModel().getSiteName();
				if (StringUtils.isBlank(entryModel) || StringUtils.isBlank(plant) || StringUtils.isBlank(site)) {
					getDialog().setErrorMessage("Entry Model, plant and site required");
					return;
				}
				source = new MyEntryModel();
				source.entryModel = entryModel;
				source.plant = plant;
				source.site = site;
				exportEntryModel();
	}

	private boolean validateEntryModel(MyEntryModel myEntryModel)  {
		if(myEntryModel == null)  {
			return false;
		}
		
		else if (StringUtils.isBlank(myEntryModel.getEntryModel()))  {
			return false;
		}
		return true;
	}
	
	private void exportEntryModel() {
		
		if(!validateEntryModel(source))  {
			getDialog().setErrorMessage("Please select plant and entry model");
		}
					
		DirectoryChooser dirChooser = new DirectoryChooser();
        File exportFile = dirChooser.showDialog(ClientMainFx.getInstance().getStage());
        StringBuilder sb = new StringBuilder();
    	if(exportFile == null)  {
		   	getDialog().getStatusTextArea().appendText("\nUser cancelled");  
		   	return;
    	}
    	ObjectOutputStream xsOutStream = null;
    	try {
			//export	        	
        	sb.append(exportFile.getAbsolutePath()).append("\\")
        	.append(source.site.trim())
        	.append("_").append(source.plant.trim())
        	.append("_").append(source.entryModel.trim())
        	.append(".xml");
        	File f = new File(sb.toString());
        	if(f.exists())  {
        		StringBuilder sb1 = new StringBuilder();
        		sb1.append(sb.toString()).append(" exists.  Overwrite?");
        		if (!MessageDialog.confirm(getDialog(), sb1.toString())) {
        		   	getDialog().getStatusTextArea().appendText("\nUser cancelled");
        		}        		
        	}
        	
    	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
			xsOutStream = getXsOutputStream(f, "QiEntryModelConfigurationTbxXmlDto");
			final ObjectOutputStream osFinal = xsOutStream;
			long t1 = System.currentTimeMillis();
			Runnable r = () -> {
				try {
					setRunning(true);
					getDialog().getCancelBtn().setDisable(true);
					writeTableSummary(source, osFinal);
		        	writeEntryScreenDefectCombinations(source, osFinal);
		        	writeLocalDefectCombinations(source, 0, osFinal);
				} catch (Exception ex) {
					ex.printStackTrace();
					getLogger().error(ex);
				}
				finally  {
					long tSec = (System.currentTimeMillis() - t1)/1000;					
		    	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(
		    	   			String.format("\nTotal time: %d min, %d sec", tSec/60, tSec%60)));					
					setRunning(false);
		    		closeFile(osFinal);
					getDialog().getCancelBtn().setDisable(false);
				}
			};
			setClose(false);
			myExecutor.submit(r);
		} catch (Exception ex) {
			getLogger().error(ex);
    		closeFile(xsOutStream);
		}
	}

	private void writeTableSummary(MyEntryModel source, ObjectOutputStream xsOut) throws IOException {
		List<QiEntryModelImportExportTableSummaryXmlDto> tblSummary = new ArrayList<>();
		
		for(int i = 0; i < 4; i++)   {
			tblSummary.add(new QiEntryModelImportExportTableSummaryXmlDto());
		}
		tblSummary.get(0).setTableName("QiEntryScreen");
		tblSummary.get(0).setRecordCount(getModel().countEntryScreenByPlantAndEntryModel(source.plant, source.entryModel));
		
		tblSummary.get(1).setTableName("QiTextEntryMenu");
		tblSummary.get(1).setRecordCount(getModel().countTextEntryMenuByEntryModel(source.entryModel));

		List<QiEntryScreen> myEntryScreenList = getModel().findAllEntryScreenByPlantAndEntryModel(source.plant, source.entryModel);
		int count = 0;
		if(myEntryScreenList != null && !myEntryScreenList.isEmpty())  {
			for(QiEntryScreen es : myEntryScreenList)  {
				count += getModel().countESDCByEntryModelAndScreen(es.getId().getEntryScreen(), source.entryModel);
			}
		}
		tblSummary.get(2).setTableName("QiEntryScreenDefectCombination");
		tblSummary.get(2).setRecordCount(count);

		tblSummary.get(3).setTableName("QiLocalDefectCombination");
		tblSummary.get(3).setRecordCount(getModel().countLDCByEntryModelAndPlant(source.plant, source.entryModel));

		xsOut.writeObject(source.site);
		xsOut.writeObject(source.plant);
		xsOut.writeObject(source.entryModel);
        StringBuilder sb = new StringBuilder();
		for(QiEntryModelImportExportTableSummaryXmlDto summary : tblSummary)  {
			try  {
				xsOut.writeObject(summary);
				sb.append("\nExporting: ")
				.append(summary.getTableName())
				.append(" (").append(summary.getRecordCount()).append(")");				
	    	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.toString()));
			}
			catch (IOException ex) {
				getLogger().error(ex);
				throw ex;
			}			
		}
		
	}
	
	private void writeLocalDefectCombinations(MyEntryModel source, int startId, ObjectOutputStream xsOut) throws IOException {
		
		List<QiLocalDefectCombination> myLocalDefectCombinationList = getModel().findFirstXByPlantAndModel(source.entryModel, source.plant,startId);
		int count = 0;
		StringBuilder sb = new StringBuilder();
		while(myLocalDefectCombinationList != null && myLocalDefectCombinationList.size() > 0 && !isClose())  {
			int mySize = myLocalDefectCombinationList.size();
			int maxId = myLocalDefectCombinationList.get(mySize - 1).getLocalDefectCombinationId();
			for(QiLocalDefectCombination listItem : myLocalDefectCombinationList) {
				if(isClose())  break;
				QiLocalDefectCombinationTbxXmlDto thisDto = QiMtcModelImportExportConfigObjectFactory.QiLocalDefectCombinationTbxXmlDtoInstance(listItem);
				try  {
					xsOut.writeObject(thisDto);
					count++;
				}
				catch (IOException ex) {
					getLogger().error(ex);
					throw ex;
				}
			}
			myLocalDefectCombinationList.clear();
			sb.setLength(0);
			sb.append("Exported Local Defect Combinations: ").append(count);
			if(mySize < 1000)  {
				sb.append("\nExport Complete");
	  		   	Platform.runLater(() -> getDialog().setMessageBig(
	  		   			String.format(sb.toString()),
	  		   			javafx.scene.paint.Color.YELLOWGREEN
	  		   			));						
	    	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.insert(0, '\n').toString()));
				break;
			}
			else  {
	  		   	Platform.runLater(() -> getDialog().setMessageBig(
	  		   			String.format(sb.toString()),
	  		   			javafx.scene.paint.Color.YELLOWGREEN
	  		   			));						

			}
			myLocalDefectCombinationList = getModel().findFirstXByPlantAndModel(source.entryModel, source.plant,maxId);
			
		}
	}
	
	private void writeEntryScreenDefectCombinations(MyEntryModel source, ObjectOutputStream xsOut) throws IOException {
		
		List<QiEntryScreen> myEntryScreenList = getModel().findAllEntryScreenByPlantAndEntryModel(source.plant, source.entryModel);

		StringBuilder sb = new StringBuilder();
		int count = 0;
		if(myEntryScreenList != null && !myEntryScreenList.isEmpty() && !isClose())  {
			for(QiEntryScreen es : myEntryScreenList)  {
				QiEntryScreenTbxXmlDto thisDto = QiMtcModelImportExportConfigObjectFactory.QiEntryScreenTbxXmlDtoInstance(es);
				try  {
					xsOut.writeObject(thisDto);
				}
				catch (IOException ex) {
					getLogger().error(ex);
					throw ex;
				}
			}
			sb.append("Exported Entry Screens: ").append(count);
  		   	Platform.runLater(() -> getDialog().setMessageBig(
  		   			String.format(sb.toString()),
  		   			javafx.scene.paint.Color.YELLOWGREEN
  		   			));						
    	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.insert(0, '\n').toString()));
		}
		
		count = 0;
		sb.setLength(0);
		List<QiTextEntryMenu> myTextEntryMenuList = getModel().findAllTextEntryMenuByEntryModel(source.entryModel);
		if(myTextEntryMenuList != null && !myTextEntryMenuList.isEmpty())  {
			for(QiTextEntryMenu em : myTextEntryMenuList)  {
				QiTextEntryMenuTbxXmlDto thisDto = QiMtcModelImportExportConfigObjectFactory.QiTextEntryMenuTbxXmlDtoInstance(em);
				try  {
					xsOut.writeObject(thisDto);
				}
				catch (IOException ex) {
					getLogger().error(ex);
					throw ex;
				}
			}
			sb.append("Exported Text Entry Menus: ").append(count);
  		   	Platform.runLater(() -> getDialog().setMessageBig(
  		   			String.format(sb.toString()),
  		   			javafx.scene.paint.Color.YELLOWGREEN
  		   			));						
    	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.insert(0, '\n').toString()));
		}
		
		count = 0;
		sb.setLength(0);
		int subCount = 0;
		for(QiEntryScreen es : myEntryScreenList)  {
			subCount = 0;
			List<QiEntryScreenDefectCombination> myEntryScreenDefectCombinationList = getModel().findAllEntryScreenDefectCombinationByEntryModelAndScreen(source.entryModel,es.getId().getEntryScreen());
			if(myEntryScreenDefectCombinationList == null || myEntryScreenDefectCombinationList.isEmpty())  {
				continue;
			}
			for(QiEntryScreenDefectCombination listItem : myEntryScreenDefectCombinationList) {
				if(isClose())  break;
				QiEntryScreenDefectCombinationTbxXmlDto thisDto = QiMtcModelImportExportConfigObjectFactory.QiEntryScreenDefectCombinationTbxXmlDtoInstance(listItem);
				try  {
					xsOut.writeObject(thisDto);
					count++;
					subCount++;
				}
				catch (IOException ex) {
					getLogger().error(ex);
					throw ex;
				}
			}
			myEntryScreenDefectCombinationList.clear();
			sb.setLength(0);
			sb.append("Exported Entry Screen Defect Combinations: ")
			.append(es.getId().getEntryScreen())
			.append(" (").append(subCount).append(")");
  		   	Platform.runLater(() -> getDialog().setMessageBig(
  		   			String.format(sb.toString()),
  		   			javafx.scene.paint.Color.YELLOWGREEN
  		   			));						
    	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText(sb.insert(0, '\n').toString()));
		}
		sb.setLength(0);
		String msg = String.format("Exported Entry Screen Defect Combinations - Total: %d", count);
		   	Platform.runLater(() -> getDialog().setMessageBig(
		   			String.format(msg),
		   			javafx.scene.paint.Color.YELLOWGREEN
		   			));						
	   	Platform.runLater(() -> getDialog().getStatusTextArea().appendText("\n" + msg));
	}
	
	
	@SuppressWarnings("unchecked")
	private void addPlantComboboxListener() {
		getDialog().getPlantCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			}
		});		
	}
	
	private ObjectOutputStream getXsOutputStream(File exportFile, String rootNode) throws IOException {
		
			//import/export
	    XStream xs = getXStream();
	    BufferedOutputStream exOut = null;
	    try {
	    	char[] indent = new char[] { ' ', ' ', ' ', ' ' };
	    	exOut = new BufferedOutputStream(new FileOutputStream(exportFile));
	    	ObjectOutputStream xsOut = xs.createObjectOutputStream(new PrettyPrintWriter(new OutputStreamWriter(exOut), indent), rootNode);
	    	return xsOut;
	    
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
	
    private void closeFile(OutputStream os)  {
		try {
			if(os != null)  {
				os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
