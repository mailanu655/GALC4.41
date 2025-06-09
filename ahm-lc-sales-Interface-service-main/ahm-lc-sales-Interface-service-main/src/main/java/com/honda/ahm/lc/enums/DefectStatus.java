package com.honda.ahm.lc.enums;

public enum DefectStatus {

    NOT_FIXED(6, "Not Fixed", true);

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

}
