package com.honda.ahm.lc.enums;

public enum InstalledPartStatus {
    BLANK(-1);

    private final int id;

    private InstalledPartStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
