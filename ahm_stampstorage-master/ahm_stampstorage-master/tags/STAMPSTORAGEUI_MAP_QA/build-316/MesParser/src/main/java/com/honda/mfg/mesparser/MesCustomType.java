package com.honda.mfg.mesparser;

public class MesCustomType {
    private String name;
    private Object type = new String();
    private boolean hasDefaultValue = false;
    private int length = 0;
    private int count = 1;

    MesCustomType(String pName) {
        name = pName;
    }

    MesCustomType(String pName, Object pType) {
        this(pName);
        type = pType;
        if (type instanceof String) {
            length = 8;
        }
    }

    MesCustomType(String pName, Object pType, int pLength) {
        this(pName, pType);
        length = pLength;
    }

    MesCustomType(String pName, Object pType, int pLength, boolean useDefault) {
        this(pName, pType, pLength);
        hasDefaultValue = useDefault;
    }
}
