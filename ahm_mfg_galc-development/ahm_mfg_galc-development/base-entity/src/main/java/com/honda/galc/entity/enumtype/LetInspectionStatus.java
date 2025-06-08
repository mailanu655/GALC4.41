package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public enum LetInspectionStatus implements IdEnum<LetInspectionStatus> {

    Fail(0, "Fail", "Failed"), Pass(1, "Pass", "Pass"), Abort(2, "Abort", "Aborted"), Terminate(3, "Terminate", "Terminated"), Untested(4, "Untested", "Not Run"), PASS_TERMINATED(9, "Pass Terminated", "Pass Terminated");

    private int id;
    private String stringValue;
    private String label;

    private LetInspectionStatus(int id, String stringValue, String label) {
        this.id = id;
        this.stringValue = stringValue;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public static LetInspectionStatus getType(int id) {
        return EnumUtil.getType(LetInspectionStatus.class, id);
    }

    public String getLabel() {
        return label;
    }

    public String getStringValue() {
        return stringValue;
    }
}
