package com.honda.galc.service.printing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;

public class SRSDefectFormatter implements PrintAttributeFormatter {
	
	public static final String PRINT_LINES = "_LINES";
	public static final String PRINT_COLUMNS = "_COLUMNS";
	public static final String PRINT_ITEM_L_NO = "L";
	public static final String PRINT_ITEM_F_NO = "F";

    private final static java.lang.String RESULT_PASS = "Pass";
	private final static java.lang.String RESULT_REJECT = "Reject";
	private final static int FIELD_COUNT = 5;
	private final static java.lang.String F_PART_TIMESTAMP = "1";
	private final static java.lang.String F_PART_DESC = "2";
	private final static java.lang.String F_PART_CHECK = "3";
	
	private LinkedHashMap <String, String> hashMap = new LinkedHashMap <String, String>();
	public static final char STX = 0x02;
	public static final char ETX = 0x03;
		
	public String execute(DataContainer dc) {
		
		int iLineNo = 1;
		String strHead = null;
		String strPartRes = null;
		String strTorqRes = null;
		InstalledPart installedPart = null;
		String measurement = "";
		String defects = "";
		List<PartSpec> partSpecList = new ArrayList<PartSpec>();
		List<InstalledPart> installedPartsByPartName = new ArrayList<InstalledPart>();
		partSpecList =  ServiceFactory.getDao(PartSpecDao.class).findProdSpecs(dc.get(DataContainerTag.PRODUCT_ID).toString(), dc.get(DataContainerTag.PROCESS_POINT_ID).toString());
		
		if(partSpecList!= null && partSpecList.size()>0){
		Set<PartSpec> uniquePartSpec = new HashSet<PartSpec>(partSpecList);
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		List<Measurement> measurements = new ArrayList<Measurement>();
		installedParts  = ServiceFactory.getDao(InstalledPartDao.class).findAllByProductId(dc.get(DataContainerTag.PRODUCT_ID).toString());
		measurements = ServiceFactory.getDao(MeasurementDao.class).findAllByProductId(dc.get(DataContainerTag.PRODUCT_ID).toString());
			for (PartSpec partSpec : uniquePartSpec) {
				String partName = partSpec.getId().getPartName();
				if (!StringUtils.isBlank(partName)) {
					installedPartsByPartName.clear();
					installedPart = null;
					strPartRes = "";
					for (InstalledPart instPart: installedParts) {
						if (instPart.getPartName() != null) {
							if (instPart.getPartName().trim().equals(
									partSpec.getId().getPartName().trim())) {
								
								installedPartsByPartName.add(instPart);
							}
						}
					}
				} else {
					partName = partSpec.getId().getPartName();
				}
				if (!installedPartsByPartName.isEmpty()) {
					installedPart = installedPartsByPartName.get(0);
				}

				if (installedPart != null && installedPart.getInstalledPartStatus()!= null && 
						installedPart.getInstalledPartStatus() !=InstalledPartStatus.REMOVED) {
					
				
				iLineNo++;
				strHead = PRINT_ITEM_L_NO + String.valueOf(iLineNo - 1)
						+ PRINT_ITEM_F_NO;
				if (installedPart.getInstalledPartStatus().equals(
						InstalledPartStatus.OK)
						|| installedPart.getInstalledPartStatus().equals(
								InstalledPartStatus.ACCEPT)) {
					// This installed part was installed correctly
					strPartRes = RESULT_PASS;
				} else if (installedPart.getInstalledPartStatus().equals(
						InstalledPartStatus.BLANK)) {
					// This installed part has a blank status
					strPartRes = "";
					
				} else {
					// This installed part was not installed correctly
					
					strPartRes = RESULT_REJECT;
					
				}
			
				Integer measurementCount = partSpec.getMeasurementCount();
				if (measurementCount.equals(0)) {
					strTorqRes = RESULT_PASS;
				} 
								
				if (RESULT_PASS.equals(strPartRes)
						&& RESULT_PASS.equals(strTorqRes)) {
					iLineNo--;
				} else {
					
					hashMap.put(strHead + F_PART_TIMESTAMP,
							installedPart.getActualTimestamp().toString());
					hashMap.put(strHead + F_PART_DESC, 
							installedPart.getPartName() + "" + ""+strPartRes + ","+ measurement+ ""+""+ strTorqRes);
					hashMap.put(strHead + F_PART_CHECK,
							"X");
					
				}
			} else {
				// Installed Part not installed
				
				iLineNo++;
				strHead = PRINT_ITEM_L_NO + String.valueOf(iLineNo-1)
						+ PRINT_ITEM_F_NO;
				strPartRes = "Missing";
				hashMap.put(strHead + F_PART_TIMESTAMP, new Timestamp(new java.util.Date().getTime()).toString());
				hashMap.put(strHead + F_PART_DESC,
						partName + "" + ""+ strPartRes);
				hashMap.put(strHead + F_PART_CHECK,
				"X");
				
			}
		}// End loop of lot control rules
		// set Line & Column
			if((iLineNo-1)>0){
				hashMap.put(PRINT_LINES, String.valueOf(iLineNo-1));
				hashMap
					.put(PRINT_COLUMNS, String.valueOf(FIELD_COUNT));
			}
			for(Map.Entry<String,String> entry: hashMap.entrySet()){
				
				defects = defects+ entry.getKey()+ getSTX()+ entry.getValue()+ getETX();
			}
			
		}
		
		return defects;
	} 
	
	public String getETX()
	{
		return Character.toString(ETX);
	}
	
	public String getSTX()
	{
		return Character.toString(STX);
	}
	
}
