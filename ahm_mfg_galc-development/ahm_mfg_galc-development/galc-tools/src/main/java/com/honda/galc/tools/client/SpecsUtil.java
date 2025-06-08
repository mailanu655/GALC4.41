package com.honda.galc.tools.client;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.service.ServiceFactory;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author vec15809
 *
 */
public class SpecsUtil {
	private static final String SPECS_XML = "_SPECS.xml";
	private XStream xStream;
	private String workingDirectory = ".";
	private	String[] overwriteOptions = {"Yes", "Yes To All", "Skip", "Skip All", "Cancel"};
	private int overwriteSelection = -1;
	private String filename = null;
	private GalcSpecConfiguration configuredSpecs;
	
	public SpecsUtil() {
		loadConfig();
		reset();
	}
	
	private void loadConfig() {
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
	}

	private void reset() {
		configuredSpecs = new GalcSpecConfiguration();
		xStream = new XStream();
	}
	
	public void backupSpecs(List<String> processPointIds) {
		overwriteSelection = -1;
		try {
			for(String processPointId : processPointIds) {
				filename = processPointId + SPECS_XML;
				saveSpecs(processPointId);
				if(overwriteSelection == 4) {
					System.out.println("Operation cancelled.");
					return;
				}
			}
		} catch (ServiceTimeoutException e) {
			JOptionPane.showMessageDialog(null, "Timeout. Please verify if the server is available.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
		}
		
	}

	private void saveSpecs(String processPointId) {
		if(FileHandler.isFileExist(workingDirectory, filename)) {
			if(overwriteSelection < 0) {
				overwriteSelection = JOptionPane.showOptionDialog(null, 
					filename + " exists in the directory. Do you want to overwrite it?", 
					"Please select a file overwrite option", 1,
					JOptionPane.DEFAULT_OPTION, null, overwriteOptions, null);
			}
			switch (overwriteSelection) {
			case 0:
				writeSpecsToFile(processPointId);
				overwriteSelection = -1;
				break;
			case 1:
				writeSpecsToFile(processPointId);
				break;
			case 2:
				System.out.println("Skipped backing up " + processPointId);
				overwriteSelection = -1;
				break;
			case 3:
				System.out.println("Skipped backing up " + processPointId);
			case 4:
			default :
					
			}
		} else {
			writeSpecsToFile(processPointId);
		}
		
	}

	private void writeSpecsToFile(String processPointId) {
		retrieveSpecs(processPointId);
		String xmlString = xStream.toXML(configuredSpecs);
		FileHandler handler = new FileHandler(workingDirectory, filename);
		handler.write(xmlString);
		
	}

	private void retrieveSpecs(String processPointId) {
		reset();
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		if(processPoint == null) {
			System.out.println("Process point " + processPointId + " does not exist - will not transfer Specs.");
			return;
		}

		configuredSpecs.setProcessPointId(processPointId);
		configuredSpecs.setLotControlRules(ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPoint(processPointId));
		configuredSpecs.setRequiredParts(ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPoint(processPointId));
		
	}

	public void restoreSpecsFromFiles(File[] files) {
		if(files != null) {
    		try {
            	for(File aFile : files) {
            		if(aFile.isFile() && aFile.canRead()) {
            			restoreSpecsFromFile(aFile.getAbsolutePath());       			
            		}
            	}
    		} catch (ServiceTimeoutException e) {
    			JOptionPane.showMessageDialog(null, "Timeout. Unable to restore.");
    		} catch (Exception ex) {
    			JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
    		}
    	}
		
	}

	private void restoreSpecsFromFile(String fileName) {

		reset();
		FileHandler fileHandler = new FileHandler(null, fileName);
		configuredSpecs = fileHandler.readSpecs();
		if(configuredSpecs == null) {
			JOptionPane.showMessageDialog(null, "Unable to read configuration from file: " + filename);
			return;
		}
		
		if(!configuredSpecs.isCurrentVersion()) {
			JOptionPane.showMessageDialog(null, configuredSpecs.wrongVersionMessage());
			return;
		}
		
		if(ServiceFactory.getDao(ProcessPointDao.class).findByKey(configuredSpecs.getProcessPointId()) == null) {
			JOptionPane.showMessageDialog(null, "Processs point " + configuredSpecs.getProcessPointId() + " does not exist.");
			return;
		}
		
		restoreLotControlRules();
		
		restoreRequiredParts();
	
		
	}

	private void restoreRequiredParts() {
		if(configuredSpecs.getRequiredParts() != null) {
			List<RequiredPart> existRequiredParts = ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPoint(configuredSpecs.getProcessPointId());
			if(existRequiredParts != null && existRequiredParts.size() > 0) {
				int result = showConfirmDialog(null, "Required Part for ", configuredSpecs.getProcessPointId(), 
						" already exists. It is recommended to remove it first.\n", "Do you want to remove it now?");
				switch(result) {
				case JOptionPane.YES_OPTION :
					removeRequiredParts(existRequiredParts);
					break;
				case JOptionPane.NO_OPTION :
					break;
				case JOptionPane.CANCEL_OPTION :
					System.out.println("Skipping Required Parts for process point: " + configuredSpecs.getProcessPointId());
					return;
				default :
				}
			}
			restoreRequiredParts(configuredSpecs.getRequiredParts());
		} else {
			System.out.println("No process point definition in " + filename);
		}
		
	}

	private void restoreRequiredParts(List<RequiredPart> requiredParts) {
		ServiceFactory.getDao(RequiredPartDao.class).saveAll(requiredParts);
		
	}

	private void removeRequiredParts(List<RequiredPart> existRequiredParts) {
		ServiceFactory.getDao(RequiredPartDao.class).removeAll(existRequiredParts);
		
	}

	private void restoreLotControlRules() {
		if(configuredSpecs.getLotControlRules() != null) {
			List<LotControlRule> existRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPoint(configuredSpecs.getProcessPointId());
			if(existRules != null && existRules.size() > 0) {
				int result = showConfirmDialog(null, "Lot Control Rules for ", configuredSpecs.getProcessPointId(), 
						" already exists. It is recommended to remove it first.\n", "Do you want to remove it now?");
				switch(result) {
				case JOptionPane.YES_OPTION :
					removeLotControlRules(existRules);
					break;
				case JOptionPane.NO_OPTION :
					break;
				case JOptionPane.CANCEL_OPTION :
					System.out.println("Skipping Lot Control Rules for process point: " + configuredSpecs.getProcessPointId());
					return;
				default :
				}
			}
			restoreLotControlRules(configuredSpecs);
		} else {
			System.out.println("No process point definition in " + filename);
		}
	}

	

	private void restoreLotControlRules(GalcSpecConfiguration configuredSpec) {
		List<MeasurementSpec> measurementSpecList = new ArrayList<MeasurementSpec>();
		List<PartSpec> partSpecList = new ArrayList<PartSpec>();
		List<PartName> partNameList = new ArrayList<PartName>();
		
		for(LotControlRule rule : configuredSpec.getLotControlRules()){
			if(rule.getPartName() != null){
				for(PartSpec partSpec : rule.getPartName().getAllPartSpecs())
					if(partSpec.getMeasurementSpecs().size() > 0 ) {
						measurementSpecList.addAll(partSpec.getMeasurementSpecs());
						partSpec.getMeasurementSpecs().clear();
					}
				
				
				partSpecList.addAll(rule.getPartName().getAllPartSpecs());
				rule.getPartName().getAllPartSpecs().clear();
				partNameList.add(rule.getPartName());
				rule.setPartName(null);
			}
		}
		ServiceFactory.getDao(PartNameDao.class).saveAll(partNameList);
		ServiceFactory.getDao(PartSpecDao.class).saveAll(partSpecList);
		ServiceFactory.getDao(MeasurementSpecDao.class).saveAll(measurementSpecList);
		ServiceFactory.getDao(LotControlRuleDao.class).saveAll(configuredSpec.getLotControlRules());
	}

	private void removeLotControlRules(List<LotControlRule> rules) {
		List<MeasurementSpec> measurementSpecList = new ArrayList<MeasurementSpec>();
		List<PartSpec> partSpecList = new ArrayList<PartSpec>();
		List<PartName> partNameList = new ArrayList<PartName>();
		
		for(LotControlRule rule : rules){
			if(rule.getPartName() != null){
				for(PartSpec partSpec : rule.getPartName().getAllPartSpecs())
					if(partSpec.getMeasurementSpecs().size() > 0 ) {
						measurementSpecList.addAll(partSpec.getMeasurementSpecs());
						partSpec.getMeasurementSpecs().clear();
					}
				
				
				partSpecList.addAll(rule.getPartName().getAllPartSpecs());
				rule.getPartName().getAllPartSpecs().clear();
				partNameList.add(rule.getPartName());
				rule.setPartName(null);
			}
			getDao(LotControlRuleDao.class).remove(rule);
		}
		
		ServiceFactory.getDao(MeasurementSpecDao.class).removeAll(measurementSpecList);
		ServiceFactory.getDao(PartSpecDao.class).removeAll(partSpecList);
		ServiceFactory.getDao(MeasurementSpecDao.class).removeAll(measurementSpecList);
		
	}

	private int showConfirmDialog(String...msg) {
		StringBuilder builder = new StringBuilder();
		for(String str : msg){
			builder.append(str);
		}
		
		int result = JOptionPane.showConfirmDialog(null, builder.toString());
		return result;
	}

	public void removeSpecs(List<String> processPointIds) {
		overwriteSelection = -1;
		try {
			for(String processPointId : processPointIds) {
				filename = processPointId + SPECS_XML;
				if(FileHandler.isFileExist(workingDirectory, filename)) {
					if(overwriteSelection < 0) {
						overwriteSelection = JOptionPane.showOptionDialog(null, 
							filename + " exists in the directory. Do you want to overwrite it?", 
							"Please select a file overwrite option", 1,
							JOptionPane.DEFAULT_OPTION, null, overwriteOptions, null);
					}
					switch (overwriteSelection) {
					case 0:
						writeSpecsToFile(processPointId);
						removeSpecs(processPointId);
						overwriteSelection = -1;
						break;
					case 1:
						writeSpecsToFile(processPointId);
						removeSpecs(processPointId);
						break;
					case 2:
						System.out.println("removeSpecs skipped " + processPointId);
						overwriteSelection = -1;
						break;
					case 3:
						System.out.println("removeSpecs skipped " + processPointId);
					case 4:
					default :
							
					}
				} else {
					writeSpecsToFile(processPointId);
					removeSpecs(processPointId);
				}
			}
		} catch (ServiceTimeoutException e) {
			JOptionPane.showMessageDialog(null, "Timeout. Unable to remove process points from server.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
		}
		
	}

	private void removeSpecs(String processPointId) {
		List<LotControlRule> existRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPoint(processPointId);
		ServiceFactory.getDao(LotControlRuleDao.class).removeAll(existRules);
		List<RequiredPart> existRequiredParts = ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPoint(processPointId);
		ServiceFactory.getDao(RequiredPartDao.class).removeAll(existRequiredParts);
		
	}

	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	public String getWorkingDirectory() {
		return workingDirectory;
	}
}
