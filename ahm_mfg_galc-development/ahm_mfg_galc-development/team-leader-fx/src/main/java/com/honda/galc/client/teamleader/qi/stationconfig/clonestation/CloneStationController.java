package com.honda.galc.client.teamleader.qi.stationconfig.clonestation;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigController;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationConfigObjectFactory;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationConfigurationXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationEntryDepartmentXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationEntryScreenXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationPreviousDefectXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationResponsibilityXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationUpcPartXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QiStationWriteUpDepartmentXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto.QicsEntryStationConfigurationXmlDto;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationResponsibility;
import com.honda.galc.entity.qi.QiStationUpcPart;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
/**
 * 
 * <h3>CloneStationController Class description</h3>
 * <p>
 * CloneStationController is used to handle actions on the Clone Station screen
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author<br>
 * 
 */
public class CloneStationController extends EntryStationConfigController {
	
	private CloneStationPanel myPanel = null;
	Station source;
	Station dest;
	String userId = "";

	public class Station {
		private String plant = "";
		private String division = "";
		private String stationId = "";
		private String site = "";
		public String getPlant() {
			return plant;
		}
		public String getDivision() {
			return division;
		}
		public String getStationId() {
			return stationId;
		}
		public String getSite() {
			return site;
		};
		public String toString()  {
			StringBuilder sb = new StringBuilder();
			sb.append(site).append("/")
			.append(plant).append("/")
			.append(division).append("/")
			.append(stationId);
			return sb.toString();
		}
	}

	private class CopyStatus  {
		List<String> good = new ArrayList<String>();
		List<String> bad = new ArrayList<String>();
	}
	
	public CloneStationController(EntryStationConfigModel model,
			EntryStationConfigPanel view) {
		super(model, view);
		myPanel = view.getCloneStationPanel();
		userId = getModel().getUserId();
	}

	public void initListeners() {
		addPlantComboBoxListener();
		addDivisionComboBoxListener();
		addQicsComboListener();
	}

	public void loadInitialData() {
		boolean b1 = validateStation(getSourceStation());
		boolean b2 = validateStation(getDestinationStation());
		if(b1)  {
			myPanel.disableExportButton(false);
		}
		else  {
			myPanel.disableExportButton(true);			
		}
		
		if(b1 && b2)  {
			myPanel.disableCopyButton(false);
		}
		else  {
			myPanel.disableCopyButton(true);			
		}
	}
	/**
	 * This method is used to perform action on 'Update', 'Reset' ,'shift' buttons for EntryDept , WriteupDept and EntryScreen table view
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			source = getSourceStation();
			dest = getDestinationStation();
			switch(StationConfigurationOperations.getType(loggedButton.getId())){
			case COPYSTN :
				executeCopy(actionEvent);
				break;
			case COPYSTN_RESET :
				myPanel.reset();
				break;
			case COPYSTN_SELECT_ALL :
				myPanel.selectAll();
				break;
			case COPYSTN_IMPORT :
				selectImportFile(actionEvent);
				break; 
			case COPYSTN_EXPORT :
				executeExport(actionEvent);
				break;
			default:
				break;
					
			}
		}
	}

	private void addStatusMessage(CopyStatus cpStatus)  {
		if(cpStatus != null && cpStatus.bad != null && !cpStatus.bad.isEmpty())  {
			StringBuilder sb = new StringBuilder();
			for(String error : cpStatus.bad)  {
				sb.append(error).append("\n");
			}
			
			myPanel.addStatusLine(sb.toString(), true);
		}
		if(cpStatus != null && cpStatus.good != null && !cpStatus.good.isEmpty())  {
			StringBuilder sb = new StringBuilder();
			for(String error : cpStatus.good)  {
				sb.append(error).append("\n");
			}
			
			myPanel.addStatusLine(sb.toString(), false);
		}		
	}

	/**
	 * This method is used to update status for selected Entry Station .
	 */
	private void executeCopy(ActionEvent actionEvent) {
		myPanel.clearStatus();
		CopyStatus cpStatus = copyQicsStation();	//copyStation();
		addStatusMessage(cpStatus);
	}
	
	/**
	 * This method is used to update status for selected Entry Station .
	 */
	private void executeExport(ActionEvent actionEvent) {
		myPanel.clearStatus();
		CopyStatus cpStatus = exportStation();
		addStatusMessage(cpStatus);
	}
	
	private Station getSourceStation()  {
		Station thisStation = new Station();
		EntryStationPanel entryPanel = getView().getEntryStationPanel();
		if (entryPanel.getQicsStationComboBoxSelectedId() != null) {
			thisStation.stationId = entryPanel.getQicsStationComboBoxSelectedId()
					.toString();
		}
		if (entryPanel.getDivisionComboBox().getSelectionModel().getSelectedItem() != null) {
			thisStation.division = entryPanel.getDivisionComboBoxSelectedId().toString();
		}
		if (entryPanel.getPlantComboBox().getSelectionModel().getSelectedItem() != null) {
			thisStation.plant = entryPanel.getPlantComboBox().getSelectionModel().getSelectedItem().toString();
		}
		thisStation.site = entryPanel.getSiteValueLabel().getText();
		return thisStation;
	}
	
	private Station getDestinationStation()  {
		Station thisStation = new Station();
		CloneStationPanel myPanel = getView().getCloneStationPanel();
		if (myPanel.getQicsStationComboBoxSelectedId() != null) {
			thisStation.stationId = myPanel.getQicsStationComboBoxSelectedId();
		}
		if (myPanel.getDivisionComboBoxSelectedId() != null) {
			thisStation.division = myPanel.getDivisionComboBoxSelectedId();
		}
		if (myPanel.getPlantComboBox().getSelectionModel().getSelectedItem() != null) {
			thisStation.plant = myPanel.getPlantComboBox().getSelectionModel().getSelectedItem().toString();
		}
		thisStation.site = getView().getEntryStationPanel().getSiteValueLabel().getText();

		return thisStation;
	}
	
	private CopyStatus exportStation() {
		
		CopyStatus cpStatus = new CopyStatus();
		if(!validateStation(source))  {
			cpStatus.bad.add("Source or desination station is null");
			return cpStatus;
		}
				
		List<CopySection> checkList = getCheckedSections();
		
		if(checkList == null || checkList.isEmpty())  {
			cpStatus.bad.add("Nothing to export, no sections checked");
			return cpStatus;
		}
		
		QicsEntryStationConfigurationXmlDto exportDto = createDto(checkList, source);
		DirectoryChooser dirChooser = new DirectoryChooser();
        File exportFile = dirChooser.showDialog(ClientMainFx.getInstance().getStage());
        StringBuilder sb = new StringBuilder();
        if (exportFile != null) {
        	try {
				//export	        	
	        	sb.append(exportFile.getAbsolutePath()).append("\\")
	        	.append(source.site).append("_").append(source.plant).append("_").append(source.division).append("_").append(source.stationId).append(".xml");
	        	File f = new File(sb.toString());
	        	if(f.exists())  {
	        		StringBuilder sb1 = new StringBuilder();
	        		sb1.append(sb.toString()).append(" exists.  Overwrite?");
	        		if (!MessageDialog.confirm(getView().getStage(), sb1.toString())) {
	        			cpStatus.bad.add("User cancelled export");
	        			return cpStatus;
	        		}        		
	        	}
	        	
				writeXml(exportDto, new File(sb.toString()));
				cpStatus.good.add("Export File successful: " + sb.toString());	            
			} catch (Exception e) {
				e.printStackTrace();
				cpStatus.bad.add("Export File failed: " + sb.toString());	            
				cpStatus.bad.add(getMessage());
			}
		}
		return cpStatus;
	}

	
	private QicsEntryStationConfigurationXmlDto createDto(List<CopySection> checkList, Station source)  {
		
		QicsEntryStationConfigurationXmlDto exportDto = new QicsEntryStationConfigurationXmlDto();
		String processPoint = source.stationId;
		String division = source.division;
		String site = source.site;
		String plant = source.plant;
		
		exportDto.setProcessPointId(processPoint);
		exportDto.setDivision(division);
		exportDto.setPlant(plant);
		exportDto.setSite(site);
		for(CopySection whichSection : checkList)  {
			if(whichSection == CopySection.ENTRY_DEPT)  {
				List<QiStationEntryDepartment> myList = getModel().findAllEntryDeptInfoByProcessPoint(processPoint);
				List<QiStationEntryDepartmentXmlDto> dtoList = new ArrayList<QiStationEntryDepartmentXmlDto>();
				if(myList != null && !myList.isEmpty())  {
					for(QiStationEntryDepartment listItem : myList)  {
						QiStationEntryDepartmentXmlDto thisDto = QiStationConfigObjectFactory.getQiStationEntryDepartmentXmlDtoInstance(listItem);
						dtoList.add(thisDto);
					}
					exportDto.setQiStationEntryDepartmentXmlDto(dtoList);
				}
			}
			else if(whichSection == CopySection.ENTRY_MODEL)  {
				List<QiStationEntryScreen> myList = getModel().findAllEntryScreenByProcessPointAndDivision(processPoint, division);
				List<QiStationEntryScreenXmlDto> dtoList = new ArrayList<QiStationEntryScreenXmlDto>();
				if(myList != null && !myList.isEmpty())  {
					for(QiStationEntryScreen listItem : myList)  {
						QiStationEntryScreenXmlDto thisDto = QiStationConfigObjectFactory.getQiStationEntryScreenXmlDtoInstance(listItem);
						dtoList.add(thisDto);
					}
					exportDto.setQiStationEntryScreenXmlDto(dtoList);
				}
			}
			else if(whichSection == CopySection.LIMIT_RESP)  {
				List<QiStationResponsibility> myList = getModel().findAllStationResponsibilityByProcessPoint(processPoint);
				List<QiStationResponsibilityXmlDto> dtoList = new ArrayList<QiStationResponsibilityXmlDto>();
				if(myList != null && !myList.isEmpty())  {
					for(QiStationResponsibility listItem : myList)  {
						QiStationResponsibilityXmlDto thisDto = QiStationConfigObjectFactory.getQiStationResponsibilityXmlDtoInstance(listItem);
						dtoList.add(thisDto);
					}
					exportDto.setQiStationResponsibilityXmlDto(dtoList);
				}
			}
			else if(whichSection == CopySection.PREV_DEFECT_VISIBLE)  {
				List<QiStationPreviousDefect> myList = getModel().findAllEntryDeptDefectInfoByProcessPoint(processPoint);
				List<QiStationPreviousDefectXmlDto> dtoList = new ArrayList<QiStationPreviousDefectXmlDto>();
				if(myList != null && !myList.isEmpty())  {
					for(QiStationPreviousDefect listItem : myList)  {
						QiStationPreviousDefectXmlDto thisDto = QiStationConfigObjectFactory.getQiStationPreviousDefectXmlDtoInstance(listItem);
						dtoList.add(thisDto);
					}
					exportDto.setQiStationPreviousDefectXmlDto(dtoList);
				}
			}
			else if(whichSection == CopySection.SETTINGS)  {
				List<QiStationConfiguration> myList = getModel().findAllByProcessPoint(processPoint);
				List<QiStationConfigurationXmlDto> dtoList = new ArrayList<QiStationConfigurationXmlDto>();
				if(myList != null && !myList.isEmpty())  {
					for(QiStationConfiguration listItem : myList)  {
						QiStationConfigurationXmlDto thisDto = QiStationConfigObjectFactory.getQiStationConfigurationXmlDtoInstance(listItem);
						dtoList.add(thisDto);
					}
					exportDto.setQiStationConfigurationXmlDto(dtoList);
				}
			}
			else if(whichSection == CopySection.WRITEUP_DEPT)  {
				List<QiStationWriteUpDepartment> myList = getModel().findAllWriteUpDepartmentByQicsStation(processPoint, site, plant);
				List<QiStationWriteUpDepartmentXmlDto> dtoList = new ArrayList<QiStationWriteUpDepartmentXmlDto>();
				if(myList != null && !myList.isEmpty())  {
					for(QiStationWriteUpDepartment listItem : myList)  {
						QiStationWriteUpDepartmentXmlDto thisDto = QiStationConfigObjectFactory.getQiStationWriteUpDepartmentXmlDtoInstance(listItem);
						dtoList.add(thisDto);
					}
					exportDto.setQiStationWriteUpDepartmentXmlDto(dtoList);
				}
			}
			else if(whichSection == CopySection.UPC)  {
				List<QiStationUpcPart> myList = getModel().findAllStationUpcPartByProcessPointId(processPoint);
				List<QiStationUpcPartXmlDto> dtoList = new ArrayList<QiStationUpcPartXmlDto>();
				if(myList != null && !myList.isEmpty())  {
					for(QiStationUpcPart listItem : myList)  {
						QiStationUpcPartXmlDto thisDto = QiStationConfigObjectFactory.getQiStationUpcPartXmlDtoInstance(listItem);
						dtoList.add(thisDto);
					}
					exportDto.setQiStationUpcPartXmlDto(dtoList);
				}
			}
		}
		return exportDto;
		
	}
        
	/**
	 * Old copy station, copies checked sections from source and creates destination entries
	 * 
	 */
	
	
	private boolean validateStation(Station myStation)  {
		if(myStation == null)  {
			return false;
		}
		
		else if (StringUtils.isBlank(myStation.stationId))  {
			return false;
		}
		return true;
	}
	
	private long countExisting(Station station, CopySection whichSection)  {
		long numRows = 0;
		if(station == null) return 0;
		if(whichSection == CopySection.ENTRY_DEPT)  {
			numRows = getModel().countEntryDeptInfoByProcessPoint(station.stationId.trim());			
			return numRows;
		}
		else if(whichSection == CopySection.WRITEUP_DEPT)  {
			numRows = getModel().countWriteUpDepartmentByQicsStation(station.stationId, station.site, station.plant);
			return numRows;
		}
		else if(whichSection == CopySection.ENTRY_MODEL)  {
			numRows = getModel().countEntryScreenByProcessPointAndDivision(station.stationId.trim(), station.division);
			
			return numRows;
		}
		else if(whichSection == CopySection.LIMIT_RESP)  {
			numRows = getModel().countAssignedRespByProcessPoint(station.stationId);
			
			return numRows;
		}
		else if(whichSection == CopySection.PREV_DEFECT_VISIBLE)  {
			numRows = getModel().countEntryDeptDefectInfoByProcessPoint(station.stationId);
			
			return numRows;
		}
		else if(whichSection == CopySection.SETTINGS)  {
			numRows = getModel().countStationConfigByProcessPoint(station.stationId);
			
			return numRows;
		}
		else if(whichSection == CopySection.UPC)  {
			numRows = getModel().countUpcPartByProcessPoint(station.stationId);
			
			return numRows;
		}
		
		return 0;
	}

	/**

	/**
	 * This method is event listener for plantComboBox
	 */

	ChangeListener<String> plantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			myPanel.clearDivisionComboBox();
			loadDivisionComboBox(new_val);
			myPanel.disableInButtons(true);
		}
	};
	
	@SuppressWarnings("unchecked")
	private void addPlantComboBoxListener() {
		myPanel.getPlantComboBox().getSelectionModel().selectedItemProperty()
		.addListener(plantComboBoxChangeListener);
	}

	/**
	 * This method is event listener for divisionComboBox
	 */
	ChangeListener<ComboBoxDisplayDto> divisionComboBoxChangeListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> ov,  ComboBoxDisplayDto old_val, ComboBoxDisplayDto new_val) {
			myPanel.clearQicsStationComboBox();;
			if(new_val != null)  {
			   loadQicsStationComboBox(new_val.getId());
			}
			myPanel.disableInButtons(true);
		} 
	};
	
	@SuppressWarnings("unchecked")
	private void addDivisionComboBoxListener(){
		myPanel.getDivisionComboBox().getSelectionModel().selectedItemProperty().addListener(divisionComboBoxChangeListener);
	}


	/**
	 * This method is event listener for divisionComboBox
	 */
	ChangeListener<ComboBoxDisplayDto> qicsComboBoxChangeListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> ov,  ComboBoxDisplayDto old_val, ComboBoxDisplayDto new_val) {
			if(new_val != null)  {
				myPanel.disableInButtons(false);
				if(!validateStation(getSourceStation()))  {
					//if source station is not specified, cannot copy
					myPanel.disableCopyButton(true);
				}
			}
			else  {
				myPanel.disableInButtons(true);
			}
		} 
	};
	
	@SuppressWarnings("unchecked")
	private void addQicsComboListener(){
		myPanel.getQicsStationComboBox().getSelectionModel().selectedItemProperty().addListener(qicsComboBoxChangeListener);
	}

	/**
	 * This method is used to load Division ComboBox based on Plant
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void loadDivisionComboBox(String plantName) {
		String siteName = getView().getEntryStationPanel().getSiteValueLabel().getText();
		List<String> divisionList= new ArrayList<String>();
		List<ComboBoxDisplayDto> dtoList = new ArrayList<ComboBoxDisplayDto>();
		for (Division divisionObj : getModel().findAllDivisionBySiteAndPlant(siteName, plantName)) {
			divisionList.add(divisionObj.getDivisionId());
			dtoList.add(ComboBoxDisplayDto.getInstance(divisionObj));
		}
		Collections.sort(dtoList);
		myPanel.getDivisionComboBox().setItems(FXCollections.observableArrayList(dtoList));
	}	

	/**
	 * This method is used to load Qics station list view based on division 
	 * @param divisionId
	 */
	@SuppressWarnings("unchecked")
	private void loadQicsStationComboBox(String divisionId){
		List<ProcessPoint> qicsStationList = getModel().findAllQicsStationByApplicationComponentDivision(divisionId);
		List<String> processPoints= new ArrayList<String>();
		List<ComboBoxDisplayDto> dtoList = new ArrayList<ComboBoxDisplayDto>();
		for (ProcessPoint processPoint : qicsStationList) {
			processPoints.add(processPoint.getProcessPointId());
			dtoList.add(ComboBoxDisplayDto.getInstance(processPoint));
		}
		Collections.sort(dtoList);
		myPanel.getQicsStationComboBox().setItems(FXCollections.observableArrayList(dtoList));
	}	
	
	/**
	 * Old copy station, copies checked sections from source and creates destination entries
	 * 
	 */
	
	private CopyStatus copyQicsStation()  {
		
		CopyStatus cpStatus = new CopyStatus();
		if(!validateStation(source) || !validateStation(dest))  {
			cpStatus.bad.add("Source or desination station is null");
			return cpStatus;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Confirm Copy:");
		sb.append("\nFrom:\t").append(source.toString());
		sb.append("\nTo:\t\t").append(dest.toString());
		if (!MessageDialog.confirm(getView().getStage(), sb.toString())) {
			cpStatus.bad.add("User cancelled copy");
			return cpStatus;
		}
		List<CopySection> checkList = getCheckedSections();
		QicsEntryStationConfigurationXmlDto dtoContainer = createDto(checkList, source);
        if (dtoContainer != null) {
        	try {
				//import/export
				String xml = getXml(dtoContainer);
		    	importFromXml(xml);
			} catch (Exception ex) {
				cpStatus.bad.add(getMessage());
				getLogger().error(ex);
			}
        }
        return cpStatus;
	}

        
    private void selectImportFile(ActionEvent actionEvent) {
		
		myPanel.clearStatus();
		CopyStatus cpStatus = new CopyStatus();
		if(!validateStation(dest))  {
			cpStatus.good.add("Source or desination station is null");
		}
		
		FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
    	File importFile = null;

    	importFile = fileChooser.showOpenDialog(ClientMainFx.getInstance().getStage());
    	try {
			BufferedReader bufRead = new BufferedReader(new FileReader(importFile));
			QicsEntryStationConfigurationXmlDto dtoContainer = importFromXml(bufRead);
			if(dtoContainer != null)  {
				StringBuilder sb = new StringBuilder();
				sb.append("Confirm import:");
				sb.append("\nFrom:\t").append(dtoContainer.getSite()).append("/")
				.append(dtoContainer.getPlant()).append("/")
				.append(dtoContainer.getDivision()).append("/")
				.append(dtoContainer.getProcessPointId());
				sb.append("\nTo:\t\t").append(dest.toString());
				
        		if (!MessageDialog.confirm(getView().getStage(), sb.toString())) {
					cpStatus.bad.add("User cancelled import");
        		}
				else  {
					cpStatus = saveStationConfiguration(dtoContainer);
					cpStatus.good.add("File imported successfully: " + importFile.getAbsolutePath());
				}
			}
			else {
				cpStatus.bad.add("Error(null dto) importing file: " + importFile.getAbsolutePath());
			}
		} catch (FileNotFoundException ex) {
			cpStatus.bad.add("File not found: " + importFile.getAbsolutePath());
			getLogger().error(ex);
		} catch (Exception ex) {
			cpStatus.bad.add("Exception importing file: " + importFile.getAbsolutePath());
			getLogger().error(ex);
		}
    	finally  {
    		addStatusMessage(cpStatus);
    	}
	}

	private QicsEntryStationConfigurationXmlDto importFromXml(BufferedReader bufRead)  throws Exception {
		
		CopyStatus cpStatus = new CopyStatus();
	    QicsEntryStationConfigurationXmlDto dtoContainer = null;
		try {
			if (bufRead != null) {
				//import/export
			    XStream xstream = getXStream();
			    Object myDto = xstream.fromXML(bufRead);
			    if(myDto instanceof QicsEntryStationConfigurationXmlDto)  {
			    	dtoContainer = (QicsEntryStationConfigurationXmlDto)myDto;
			    }
			}
		} catch (Exception ex) {
			cpStatus.bad.add("Import failed.  " + ex.getMessage());						
			getLogger().error(ex);
			addStatusMessage(cpStatus);
			throw ex;
		}   
		finally  {
	    	if(bufRead != null)  {
	        	try {
	        		bufRead.close();
				} catch (IOException ex1) {
					getLogger().error(ex1);
				}
	    	}			
		}
		return dtoContainer;
	}
	
	
	private void importFromXml(String xml) throws Exception {
		
		
		CopyStatus cpStatus = new CopyStatus();
		try {
			if (xml != null && !StringUtils.isEmpty(xml)) {
				//import/export
			    XStream xstream = getXStream();
			    Object myDto = xstream.fromXML(xml);
			    QicsEntryStationConfigurationXmlDto dtoContainer = null;
			    if(myDto instanceof QicsEntryStationConfigurationXmlDto)  {
			    	dtoContainer = (QicsEntryStationConfigurationXmlDto)myDto;
			    	cpStatus = saveStationConfiguration(dtoContainer);
			    }
			}
		} catch (Exception ex) {
			cpStatus.bad.add("Import failed.  " + ex.getMessage());						
			getLogger().error(ex);
			throw ex;
		}            
		finally  {
			addStatusMessage(cpStatus);
		}
	}
	
	
	private CopyStatus saveStationConfiguration(QicsEntryStationConfigurationXmlDto dto)  throws Exception {
		
		CopyStatus cpStatus = new CopyStatus();
		List<CopySection> checkList = getCheckedSections();
		
		for(CopySection whichSection : checkList)  {
			boolean isDelete = false, isCancelled = false;
			long numRows = countExisting(dest, whichSection);
			String sectionName = whichSection.getDisplayText();
			if(numRows > 0)  {
				StringBuilder sb = new StringBuilder();
				sb.append(sectionName).append(" already exists for this station.  Overwrite?");
				sb.append("\n").append(numRows).append(" rows exist and will be deleted");
				if (MessageDialog.confirm(getView().getStage(), sb.toString())) {
					isDelete = true;
				}
				else  {
					isCancelled = true;
					cpStatus.bad.add("User cancelled: " + sectionName);
				}
			}
			try {
				int numDeleted = 0;
				if(isDelete)  {
					numDeleted = getModel().deleteSectionByProcessPoint(dest.stationId, whichSection);
					if(numDeleted > 0)  {
						String changeReason = String.format("%d rows deleted from %s by %s", numDeleted, whichSection.name(), userId);
						getLogger().error(changeReason);
					}
				}
				if(!isCancelled)  {
					if(whichSection == CopySection.ENTRY_MODEL  && dto.getQiStationEntryScreenXmlDto() != null &&
							!dto.getQiStationEntryScreenXmlDto().isEmpty())  {
						List<QiStationEntryScreen> entityList = new ArrayList<QiStationEntryScreen>();
						List<QiStationEntryScreenXmlDto> dtoList = dto.getQiStationEntryScreenXmlDto();
						for(QiStationEntryScreenXmlDto thisDto : dtoList)  {
							QiStationEntryScreen entity = QiStationConfigObjectFactory.getQiStationEntryScreenEntity(thisDto, dest.stationId, userId);
							if(!getModel().isValidEntryModelScreen(entity.getEntryScreen(), entity.getId().getEntryModel())  )
							{
								String msg = String.format("%s: %s, %s not a valid combination", whichSection.name(), entity.getEntryScreen(), entity.getId().getEntryModel());
								cpStatus.bad.add(msg);
								getLogger().error(msg);
								continue;
							}
							else if(!getModel().isValidDepartment(entity.getId().getDivisionId()))  {
								String msg = String.format("%s: %s not a valid department", whichSection.name(), entity.getId().getDivisionId());
								cpStatus.bad.add(msg);
								getLogger().error(msg);
								continue;								
							}
							entityList.add(entity);
						}
						getModel().saveAllQiStationEntryScreen(entityList);
					}
					else if(whichSection == CopySection.ENTRY_DEPT  && dto.getQiStationEntryDepartmentXmlDto() != null &&
							!dto.getQiStationEntryDepartmentXmlDto().isEmpty())  {
						List<QiStationEntryDepartment> entityList = new ArrayList<QiStationEntryDepartment>();
						List<QiStationEntryDepartmentXmlDto> dtoList = dto.getQiStationEntryDepartmentXmlDto();
						for(QiStationEntryDepartmentXmlDto thisDto : dtoList)  {
							QiStationEntryDepartment entity = QiStationConfigObjectFactory.getQiStationEntryDepartmentEntity(thisDto, dest.stationId, userId);
							if(!getModel().isValidDepartment(entity.getId().getDivisionId()))  {
								String msg = String.format("%s: %s not a valid department", whichSection.name(), entity.getId().getDivisionId());
								cpStatus.bad.add(msg);
								getLogger().error(msg);
								continue;								
							}
							entityList.add(entity);
						}
						getModel().saveAllQiEntryDept(entityList);
					}
					else if(whichSection == CopySection.LIMIT_RESP  && dto.getQiStationResponsibilityXmlDto() != null &&
							!dto.getQiStationResponsibilityXmlDto().isEmpty())  {
						List<QiStationResponsibility> entityList = new ArrayList<QiStationResponsibility>();
						List<QiStationResponsibilityXmlDto> dtoList = dto.getQiStationResponsibilityXmlDto();
						for(QiStationResponsibilityXmlDto thisDto : dtoList)  {
							QiStationResponsibility entity = QiStationConfigObjectFactory.getQiStationResponsibilityEntity(thisDto, dest.stationId, userId);
							entityList.add(entity);
						}
						getModel().saveAllQiStationResponsibility(entityList);
					}
					else if(whichSection == CopySection.PREV_DEFECT_VISIBLE  && dto.getQiStationPreviousDefectXmlDto() != null &&
							!dto.getQiStationPreviousDefectXmlDto().isEmpty())  {
						List<QiStationPreviousDefect> entityList = new ArrayList<QiStationPreviousDefect>();
						List<QiStationPreviousDefectXmlDto> dtoList = dto.getQiStationPreviousDefectXmlDto();
						for(QiStationPreviousDefectXmlDto thisDto : dtoList)  {
							QiStationPreviousDefect entity = QiStationConfigObjectFactory.getQiStationPreviousDefectEntity(thisDto, dest.stationId, userId);
							if(!getModel().isValidDepartment(entity.getId().getEntryDivisionId()))  {
								String msg = String.format("%s: %s not a valid department", whichSection.name(), entity.getId().getEntryDivisionId());
								cpStatus.bad.add(msg);
								getLogger().error(msg);
								continue;								
							}
							entityList.add(entity);
						}
						getModel().saveAllQiStationPreviousDefect(entityList);
					}
					else if(whichSection == CopySection.SETTINGS  && dto.getQiStationConfigurationXmlDto() != null &&
							!dto.getQiStationConfigurationXmlDto().isEmpty())  {
						List<QiStationConfiguration> entityList = new ArrayList<QiStationConfiguration>();
						List<QiStationConfigurationXmlDto> dtoList = dto.getQiStationConfigurationXmlDto();
						for(QiStationConfigurationXmlDto thisDto : dtoList)  {
							QiStationConfiguration entity = QiStationConfigObjectFactory.getQiStationConfigurationEntity(thisDto, dest.stationId, userId);
							entityList.add(entity);
						}
						getModel().saveAllQiStationConfiguration(entityList);
					}
					else if(whichSection == CopySection.WRITEUP_DEPT  && dto.getQiStationWriteUpDepartmentXmlDto() != null &&
							!dto.getQiStationWriteUpDepartmentXmlDto().isEmpty())  {
						List<QiStationWriteUpDepartment> entityList = new ArrayList<QiStationWriteUpDepartment>();
						List<QiStationWriteUpDepartmentXmlDto> dtoList = dto.getQiStationWriteUpDepartmentXmlDto();
						for(QiStationWriteUpDepartmentXmlDto thisDto : dtoList)  {
							QiStationWriteUpDepartment entity = QiStationConfigObjectFactory.getQiStationWriteUpDepartmentEntity(thisDto, dest.stationId, dest.plant, dest.site, userId);
							if(!getModel().isValidWriteupDepartment(entity.getId().getDivisionId(), entity.getId().getSite(), entity.getId().getPlant()))  {
								String msg = String.format("%s: %s not a valid department", whichSection.name(), entity.getId().getDivisionId());
								cpStatus.bad.add(msg);
								getLogger().error(msg);
								continue;								
							}
							entityList.add(entity);
						}
						getModel().saveAllQiStationWriteUpDepartment(entityList);
					}
					else if(whichSection == CopySection.UPC  && dto.getQiStationUpcPartXmlDto() != null &&
							!dto.getQiStationUpcPartXmlDto().isEmpty())  {
						List<QiStationUpcPart> entityList = new ArrayList<QiStationUpcPart>();
						List<QiStationUpcPartXmlDto> dtoList = dto.getQiStationUpcPartXmlDto();
						for(QiStationUpcPartXmlDto thisDto : dtoList)  {
							QiStationUpcPart entity = QiStationConfigObjectFactory.getQiStationUpcPartEntity(thisDto, dest.stationId, userId);
							if(!getModel().isValidPart(entity.getId().getMainPartNo()))  {
								String msg = String.format("%s: %s not a valid part", whichSection.name(), entity.getId().getMainPartNo());
								cpStatus.bad.add(msg);
								getLogger().error(msg);
								continue;								
							}
							entityList.add(entity);
						}
						getModel().saveAllQiStationUpcPart(entityList);
					}
					StringBuilder sb = new StringBuilder();
					sb.append(sectionName + " completed");
					if(numDeleted > 0)  {
						String msg = String.format("; deleted (%d rows)", numDeleted);
						sb.append(msg);
					}
					cpStatus.good.add(sb.toString());
				}
			} 	
			catch (Exception ex) {
				cpStatus.bad.add(sectionName + " failed.  " + ex.getMessage());						
				getLogger().error(ex);
				throw ex;
			}
		}
		return cpStatus;
	}
	
	private void writeXml(QicsEntryStationConfigurationXmlDto dto, File exportFile) throws IOException {
		
			//import/export
        XStream xs = getXStream();
        BufferedOutputStream exOut = null;
        try {
        	char[] indent = new char[] { ' ', ' ', ' ', ' ' };
        	exOut = new BufferedOutputStream(new FileOutputStream(exportFile));
        	xs.marshal(dto, new PrettyPrintWriter(new OutputStreamWriter(exOut), indent));
        	//xs.toXML(dto,exOut);
        
		} catch (IOException ex) {
			getLogger().error(ex);
			throw ex;
		}
        finally  {
        	if(exOut != null)  {
        		exOut.close();
        	}
        }
	}
	

	private String getXml(QicsEntryStationConfigurationXmlDto dto) throws Exception {
		
		//generate xml string from dto
	    XStream xs = getXStream();
	    BufferedWriter bufWriter = null;
    	StringWriter strWriter = new StringWriter();
    	String xml = "";
	    try {
	    	bufWriter = new BufferedWriter(strWriter);
	    	xs.toXML(dto, bufWriter);
	    	//xs.toXML(dto,exOut);
	    	getLogger().info(strWriter.toString());
	    	xml = strWriter.toString();
	    
		} catch (Exception ex) {
			getLogger().error(ex);
			throw ex;
		}
	    finally  {
	    	if(bufWriter != null)  {
	        	try {
					bufWriter.close();
				} catch (IOException ex1) {
					getLogger().error(ex1);
				}
	    	}
	    }
    	return xml;
	}


	private XStream getXStream()  {
        XStream xstream = new XStream(new StaxDriver());
        xstream.setClassLoader(this.getClass().getClassLoader());
        xstream.alias("QicsEntryStationConfigurationXmlDto", QicsEntryStationConfigurationXmlDto.class);
        xstream.alias("qiStationConfigurationXmlDto", QiStationConfigurationXmlDto.class);
        xstream.alias("qiStationEntryDepartmentXmlDto", QiStationEntryDepartmentXmlDto.class);
        xstream.alias("qiStationEntryScreenXmlDto", QiStationEntryScreenXmlDto.class);
        xstream.alias("qiStationPreviousDefectXmlDto", QiStationPreviousDefectXmlDto.class);
        xstream.alias("qiStationResponsibilityXmlDto", QiStationResponsibilityXmlDto.class);
        xstream.alias("qiStationWriteUpDepartmentXmlDto", QiStationWriteUpDepartmentXmlDto.class);
        xstream.alias("qiStationUpcPartXmlDto", QiStationUpcPartXmlDto.class);
        xstream.addImplicitCollection(QicsEntryStationConfigurationXmlDto.class, "qiStationConfigurationXmlDto");
        xstream.addImplicitCollection(QicsEntryStationConfigurationXmlDto.class, "qiStationEntryDepartmentXmlDto");
        xstream.addImplicitCollection(QicsEntryStationConfigurationXmlDto.class, "qiStationEntryScreenXmlDto");
        xstream.addImplicitCollection(QicsEntryStationConfigurationXmlDto.class, "qiStationPreviousDefectXmlDto");
        xstream.addImplicitCollection(QicsEntryStationConfigurationXmlDto.class, "qiStationResponsibilityXmlDto");
        xstream.addImplicitCollection(QicsEntryStationConfigurationXmlDto.class, "qiStationWriteUpDepartmentXmlDto");
        xstream.addImplicitCollection(QicsEntryStationConfigurationXmlDto.class, "qiStationUpcPartXmlDto");
		return xstream;
	}
	
	
	private List<CopySection>  getCheckedSections()  {
		List<CopySection> checkList = new ArrayList<CopySection>();
		CloneStationPanel myPanel = getView().getCloneStationPanel();
		if(myPanel.getEntryDeptChkBox().isSelected())  {
			checkList.add(CopySection.ENTRY_DEPT);			
		}
		if(myPanel.getWriteUpDeptChkBox().isSelected()){
			checkList.add(CopySection.WRITEUP_DEPT);			
		}
		if(myPanel.getEntryModelScreenChkBox().isSelected()){
			checkList.add(CopySection.ENTRY_MODEL);			
		}
		if(myPanel.getPrevDefectVisibleChkBox().isSelected()){
			checkList.add(CopySection.PREV_DEFECT_VISIBLE);			
		}
		if(myPanel.getUpcChkBox().isSelected()){
			checkList.add(CopySection.UPC);			
		}
		if(myPanel.getLimitedRespChkBox().isSelected()){
			checkList.add(CopySection.LIMIT_RESP);			
		}
		if(myPanel.getSettingsChkBox().isSelected()){
			checkList.add(CopySection.SETTINGS);
		}
		return checkList;
	}
	
	
	public CloneStationPanel getMyPanel() {
		return myPanel;
	}

	public void setMyPanel(CloneStationPanel myPanel) {
		this.myPanel = myPanel;
	}
	
}
