package com.honda.galc.entity.enumtype;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>PartSerialNumberScanType</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartSerialNumberScanType description </p>
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
 * @author Paul Chou
 * Jan 30, 2012
 *
 */

public enum PartSerialNumberScanType implements IdEnum<PartSerialNumberScanType>{
    NONE(0),
    PART(1),
    PART_LOT(2),
    PART_MASK(3),
    PROD_LOT(4),
    KD_LOT(5),
    STATUS_ONLY(6), //for headless only
    DATE(7);
    
    private int id;
    
    private PartSerialNumberScanType(int id){
    	this.id = id;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
	public static PartSerialNumberScanType getType(int id) {
		return EnumUtil.getType(PartSerialNumberScanType.class, id);
	}

	public static List<Integer> getIdList(boolean isHeadless) {
		List<Integer> list = new ArrayList<Integer>();
		for(PartSerialNumberScanType t : values())
			if(!isHeadless && t == STATUS_ONLY ) continue;
			else list.add(t.getId());
		
		return list;
	}

	public static Object[] values(boolean isHeadless) {
		return isHeadless ? values() : headedValues();
	}

	private static Object[] headedValues() {
		List<PartSerialNumberScanType> list = new ArrayList<PartSerialNumberScanType>();
		for(PartSerialNumberScanType t : values())
			if(t == STATUS_ONLY ) continue;
			else list.add(t);
		
		return list.toArray();
	}
    
}
