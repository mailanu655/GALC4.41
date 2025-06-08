package com.honda.galc.client.qi.defectentry;

import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>DefectEntrySelection Class description</h3>
 * <p> DefectEntrySelection description </p>
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
 * @author L&T Infotech<br>
 * June 14, 2017
 *
 *
 */

public enum DefectEntrySelection implements IdEnum<DefectEntrySelection>  {

	DEFECT1(0, "defect1"), 
	DEFECT2(1, "defect2"), 
	DEFECT1_SET_ITEMS(2, "defect1_setItems"), 
	DEFECT2_SET_ITEMS(3, "defect2_setItems"), 
	PART1(4, "part1"),
	PART2(5, "part2"),
	DEFECT(6, "defect"),
	PART1_SET_ITEMS(7, "part1_setItems"),
	PART2_SET_ITEMS(8, "part2_setItems"),
	DEFECT_SET_ITEMS(9, "defect_setItems"),
	PART_DEFECT_SET_ITEMS(10, "part_defect_setItems"),
	DEFECT1_CLEAR_ITEMS(11, "defect1_clearItems"),
	DEFECT2_CLEAR_ITEMS(12, "defect2_clearItems"),
	SHOW_CONFIRM_POPUP(13, "show_confirm_popup"),
	LOC(14,"loc"),
	LOC_SET_ITEMS(15,"loc_setItems");

    private final int id;
    private final String name;
    
    private DefectEntrySelection(int intValue, String name) {
		this.id = intValue;
		this.name = name;
	}

    public int getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }
    
    public static DefectEntrySelection getType(String name) {
		for(DefectEntrySelection type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
}
