package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>DefectStatus Class description</h3>
 * <p> DefectStatus description </p>
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
 * @author Jeffray Huang<br>
 * Apr 26, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public enum DefectStatus implements IdEnum<DefectStatus> {
	
	ALL(-1, "All", false),
	OUTSTANDING(0, "Outstanding", true), 
	REPAIRED(1, "Repaired", false), 
	DIRECT_PASS(2, "Direct Pass", false), 
	SCRAP(3, "Scrap", false), 
	PREHEAT_SCRAP(4, "Preheat", false),
	NOT_REPAIRED(5, "Not Repaired", false),
	NOT_FIXED(6, "Not Fixed", true),
	FIXED(7, "Fixed", false),
	NON_REPAIRABLE(8, "Non Repairable", false),
	IPP_ENTRY_TAG(9, "IPP Entry Tag", false),
	NOT_FIXED_SCRAPPED(10,"NOT FIXED SCRAPPED",false);


    private final int id;
    private final String name;
    private boolean updatable;
    
    private DefectStatus(int intValue, String name, boolean updatable) {
		this.id = intValue;
		this.name = name;
		this.updatable = updatable;
	}

    public int getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public boolean isUpdatable() {
		return updatable;
	}
    
    public boolean isDirectPass() {
    	return this.equals(DIRECT_PASS);
    }
    
    public boolean isScrap() {
    	return this.equals(SCRAP);
    }
    
    public boolean isNonRepairable() {
    	return this.equals(NON_REPAIRABLE);
    }
    
    public boolean isNotFixedScrapped() {
    	return this.equals(NOT_FIXED_SCRAPPED);
    }
    
    public boolean isPreheatScrap() {
    	return this.equals(PREHEAT_SCRAP);
    }
    
    public boolean isOutstanding() {
    	return this.equals(OUTSTANDING);
    }
    
    public boolean isNotRepaired() {
    	return this.equals(NOT_REPAIRED);
    }
    
    public boolean isRepaired() {
    	return this.equals(REPAIRED);
    }
    
    public static DefectStatus getType(int id) {
        return EnumUtil.getType(DefectStatus.class, id);
    }
    
    public static boolean isOutstandingStatus(Integer status) {
    	
    	return OUTSTANDING.getId() == status;
    	
    }
    
    public static DefectStatus getType(String name) {
  		for(DefectStatus type : values()) {
  			if(type.getName().equalsIgnoreCase(name)) return type;
  		}
  		return null;
  	}
    
    
    //convert current defect status in NAQ to defect status in old QICS
    public static short getOldDefectStatus(short newCurrentDefectStatus) {
    	short oldDefectStatus = 0;
   		switch (newCurrentDefectStatus) {
	    	case 3: //Scrap
	    	case 4: //Preheat
	    	case 6: //Not Fixed
	    	case 8: //Non Repairable
	    	case 10: //Not Fixed Scrapped
	    		oldDefectStatus = 0;
	    		break;
	    	case 7: //Fixed
	    		oldDefectStatus = 1;
	    		break;
    		} 
    	return oldDefectStatus;
    }
    	
	//convert original defect status in NAQ to outstanding flag in old QICS
	public static short getOldOutstandingFlag(short newOriginalDefectStatus) {
		short oldOutstandingFlag = 1;
		switch (newOriginalDefectStatus) {
		case 1: //Repaired
			oldOutstandingFlag = 0;
			break;
		case 3: //Scrap	
		case 4: //Preheat				
		case 5: //Not Repaired
		case 8:	//Non Repairable
		case 10: //Not Fixed Scrapped
			oldOutstandingFlag = 1;
			break;
		}
		return oldOutstandingFlag;
	}
}
