package com.honda.ahm.lc.model;

public class Frame extends Product {
    private static final long serialVersionUID = 1L;

    private String productId;

    private String engineSerialNo;

    private Double straightShipPercentage;

    private String keyNo;

    private String shortVin;

    private Integer engineStatus;

    private Integer afOnSequenceNumber;

    private String missionSerialNo;

    private String actualMissionType;

    private String purchaseContractNumber;

    public Frame() {
    }

    public Frame(String productId, String engineSerialNo, Double straightShipPercentage, String keyNo, String shortVin, Integer engineStatus, Integer afOnSequenceNumber, String missionSerialNo, String actualMissionType, String purchaseContractNumber) {
        this.productId = productId;
        this.engineSerialNo = engineSerialNo;
        this.straightShipPercentage = straightShipPercentage;
        this.keyNo = keyNo;
        this.shortVin = shortVin;
        this.engineStatus = engineStatus;
        this.afOnSequenceNumber = afOnSequenceNumber;
        this.missionSerialNo = missionSerialNo;
        this.actualMissionType = actualMissionType;
        this.purchaseContractNumber = purchaseContractNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getEngineSerialNo() {
        return engineSerialNo;
    }

    public void setEngineSerialNo(String engineSerialNo) {
        this.engineSerialNo = engineSerialNo;
    }

    public Double getStraightShipPercentage() {
        return straightShipPercentage;
    }

    public void setStraightShipPercentage(Double straightShipPercentage) {
        this.straightShipPercentage = straightShipPercentage;
    }

    public String getKeyNo() {
        return keyNo;
    }

    public void setKeyNo(String keyNo) {
        this.keyNo = keyNo;
    }

    public String getShortVin() {
        return shortVin;
    }

    public void setShortVin(String shortVin) {
        this.shortVin = shortVin;
    }

    public Integer getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(Integer engineStatus) {
        this.engineStatus = engineStatus;
    }

    public Integer getAfOnSequenceNumber() {
        return afOnSequenceNumber;
    }

    public void setAfOnSequenceNumber(Integer afOnSequenceNumber) {
        this.afOnSequenceNumber = afOnSequenceNumber;
    }

    public String getMissionSerialNo() {
        return missionSerialNo;
    }

    public void setMissionSerialNo(String missionSerialNo) {
        this.missionSerialNo = missionSerialNo;
    }

    public String getActualMissionType() {
        return actualMissionType;
    }

    public void setActualMissionType(String actualMissionType) {
        this.actualMissionType = actualMissionType;
    }

    public String getPurchaseContractNumber() {
        return purchaseContractNumber;
    }

    public void setPurchaseContractNumber(String purchaseContractNumber) {
        this.purchaseContractNumber = purchaseContractNumber;
    }
}
