package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

privileged aspect Stop_Roo_JavaBean {

    public String Stop.getName() {
        return this.name;
    }

    public void Stop.setName(String name) {
        this.name = name;
    }
    public StopType Stop.getStopType() {
        return this.stopType;
    }

    public void Stop.setStopType(StopType stopType) {
        this.stopType = stopType;
    }
    public String Stop.getDescription() {
        return this.description;
    }

    public void Stop.setDescription(String description) {
        this.description = description;
    }

    public StopArea Stop.getStopArea() {
        return this.stopArea;
    }

    public void Stop.setStopArea(StopArea stopArea) {
        this.stopArea = stopArea;
    }

//
//    public Integer Stop.getCapacity() {
//        return this.capacity;
//    }
//
//    public void Stop.setCapacity(Integer capacity) {
//        this.capacity = capacity;
//    }
//
//    public StopAvailability Stop.getStopAvailability() {
//        return this.stopAvailability;
//    }
//
//    public void Stop.setStopAvailability(StopAvailability stopAvailability) {
//        this.stopAvailability = stopAvailability;
//    }
}
