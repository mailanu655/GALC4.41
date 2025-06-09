package com.ils_tech.dw.ei;

public class DWCustomType {
    private String name;
    private Object type = new String();
    private boolean hasDefaultValue = false;
    private int length = 0;
    private int count = 1;

    DWCustomType(String pName) {
        name = pName;
    }

    DWCustomType(String pName, Object pType) {
        this(pName);
        type = pType;
        if (type instanceof String) {
            length = 8;
        }
    }

    DWCustomType(String pName, Object pType, int pLength) {
        this(pName, pType);
        length = pLength;
    }

    DWCustomType(String pName, Object pType, int pLength, boolean useDefault) {
        this(pName, pType, pLength);
        hasDefaultValue = useDefault;
    }
}
