/**
 * WheelAlignmentBE.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class WheelAlignmentBE  implements java.io.Serializable {
    private java.util.Calendar YMDHNS;
    private java.lang.String MACHINE_NUM;
    private java.lang.String CAR_IDNO;
    private java.lang.String BAR_CODE;
    private double PRST_CAM_LFRT;
    private double PRST_CAM_RFRT;
    private double PRST_CAM_LRR;
    private double PRST_CAM_RRR;
    private double PRST_CV_TOE_LFRT;
    private double PRST_CV_TOE_RFRT;
    private double PRST_TOE_LRR;
    private double PRST_TOE_RRR;
    private double PRST_ST_WHEEL_ANGLE;
    private double PRST_SYMMETRY;
    private boolean ACCEPTED_REJECTED;
    private double FNAL_CAM_LFRT;
    private double FNAL_CAM_RFRT;
    private double FNAL_CAM_LRR;
    private double FNAL_CAM_RRR;
    private double FNAL_CV_TOE_LFRT;
    private double FNAL_CV_TOE_RFRT;
    private double FNAL_TOE_LRR;
    private double FNAL_TOE_RRR;
    private double FNAL_ST_WHEEL_ANGLE;
    private double FNAL_SYMMETRY;
    private double LFRT_TORQUE;
    private double RFRT_TORQUE;
    private double LRR_TORQUE;
    private double RRR_TORQUE;
    private double FRT_ATA_TIME;
    private double RR_ATA_TIME;
    private double TOTAL_TIME;
    private double timeDriveOn;
    private double timeSetup;
    private double timeLeaving;
    private double timeEmpty;
    private double rearTries;
    private double frontTries;
    private java.lang.String locationID;
    private java.lang.String applicationID;
    private double PRST_WOCV_TOE_LFRT;
    private double PRST_WOCV_TOE_RFRT;
    private double FNAL_WOCV_TOE_LFRT;
    private double FNAL_WOCV_TOE_RFRT;
    private java.lang.String predictor_Mode;
    private java.lang.String frontAuto_ReCheck;

    public WheelAlignmentBE() {
    }

    public java.util.Calendar getYMDHNS() {
        return YMDHNS;
    }

    public void setYMDHNS(java.util.Calendar YMDHNS) {
        this.YMDHNS = YMDHNS;
    }

    public java.lang.String getMACHINE_NUM() {
        return MACHINE_NUM;
    }

    public void setMACHINE_NUM(java.lang.String MACHINE_NUM) {
        this.MACHINE_NUM = MACHINE_NUM;
    }

    public java.lang.String getCAR_IDNO() {
        return CAR_IDNO;
    }

    public void setCAR_IDNO(java.lang.String CAR_IDNO) {
        this.CAR_IDNO = CAR_IDNO;
    }

    public java.lang.String getBAR_CODE() {
        return BAR_CODE;
    }

    public void setBAR_CODE(java.lang.String BAR_CODE) {
        this.BAR_CODE = BAR_CODE;
    }

    public double getPRST_CAM_LFRT() {
        return PRST_CAM_LFRT;
    }

    public void setPRST_CAM_LFRT(double PRST_CAM_LFRT) {
        this.PRST_CAM_LFRT = PRST_CAM_LFRT;
    }

    public double getPRST_CAM_RFRT() {
        return PRST_CAM_RFRT;
    }

    public void setPRST_CAM_RFRT(double PRST_CAM_RFRT) {
        this.PRST_CAM_RFRT = PRST_CAM_RFRT;
    }

    public double getPRST_CAM_LRR() {
        return PRST_CAM_LRR;
    }

    public void setPRST_CAM_LRR(double PRST_CAM_LRR) {
        this.PRST_CAM_LRR = PRST_CAM_LRR;
    }

    public double getPRST_CAM_RRR() {
        return PRST_CAM_RRR;
    }

    public void setPRST_CAM_RRR(double PRST_CAM_RRR) {
        this.PRST_CAM_RRR = PRST_CAM_RRR;
    }

    public double getPRST_CV_TOE_LFRT() {
        return PRST_CV_TOE_LFRT;
    }

    public void setPRST_CV_TOE_LFRT(double PRST_CV_TOE_LFRT) {
        this.PRST_CV_TOE_LFRT = PRST_CV_TOE_LFRT;
    }

    public double getPRST_CV_TOE_RFRT() {
        return PRST_CV_TOE_RFRT;
    }

    public void setPRST_CV_TOE_RFRT(double PRST_CV_TOE_RFRT) {
        this.PRST_CV_TOE_RFRT = PRST_CV_TOE_RFRT;
    }

    public double getPRST_TOE_LRR() {
        return PRST_TOE_LRR;
    }

    public void setPRST_TOE_LRR(double PRST_TOE_LRR) {
        this.PRST_TOE_LRR = PRST_TOE_LRR;
    }

    public double getPRST_TOE_RRR() {
        return PRST_TOE_RRR;
    }

    public void setPRST_TOE_RRR(double PRST_TOE_RRR) {
        this.PRST_TOE_RRR = PRST_TOE_RRR;
    }

    public double getPRST_ST_WHEEL_ANGLE() {
        return PRST_ST_WHEEL_ANGLE;
    }

    public void setPRST_ST_WHEEL_ANGLE(double PRST_ST_WHEEL_ANGLE) {
        this.PRST_ST_WHEEL_ANGLE = PRST_ST_WHEEL_ANGLE;
    }

    public double getPRST_SYMMETRY() {
        return PRST_SYMMETRY;
    }

    public void setPRST_SYMMETRY(double PRST_SYMMETRY) {
        this.PRST_SYMMETRY = PRST_SYMMETRY;
    }

    public boolean isACCEPTED_REJECTED() {
        return ACCEPTED_REJECTED;
    }

    public void setACCEPTED_REJECTED(boolean ACCEPTED_REJECTED) {
        this.ACCEPTED_REJECTED = ACCEPTED_REJECTED;
    }

    public double getFNAL_CAM_LFRT() {
        return FNAL_CAM_LFRT;
    }

    public void setFNAL_CAM_LFRT(double FNAL_CAM_LFRT) {
        this.FNAL_CAM_LFRT = FNAL_CAM_LFRT;
    }

    public double getFNAL_CAM_RFRT() {
        return FNAL_CAM_RFRT;
    }

    public void setFNAL_CAM_RFRT(double FNAL_CAM_RFRT) {
        this.FNAL_CAM_RFRT = FNAL_CAM_RFRT;
    }

    public double getFNAL_CAM_LRR() {
        return FNAL_CAM_LRR;
    }

    public void setFNAL_CAM_LRR(double FNAL_CAM_LRR) {
        this.FNAL_CAM_LRR = FNAL_CAM_LRR;
    }

    public double getFNAL_CAM_RRR() {
        return FNAL_CAM_RRR;
    }

    public void setFNAL_CAM_RRR(double FNAL_CAM_RRR) {
        this.FNAL_CAM_RRR = FNAL_CAM_RRR;
    }

    public double getFNAL_CV_TOE_LFRT() {
        return FNAL_CV_TOE_LFRT;
    }

    public void setFNAL_CV_TOE_LFRT(double FNAL_CV_TOE_LFRT) {
        this.FNAL_CV_TOE_LFRT = FNAL_CV_TOE_LFRT;
    }

    public double getFNAL_CV_TOE_RFRT() {
        return FNAL_CV_TOE_RFRT;
    }

    public void setFNAL_CV_TOE_RFRT(double FNAL_CV_TOE_RFRT) {
        this.FNAL_CV_TOE_RFRT = FNAL_CV_TOE_RFRT;
    }

    public double getFNAL_TOE_LRR() {
        return FNAL_TOE_LRR;
    }

    public void setFNAL_TOE_LRR(double FNAL_TOE_LRR) {
        this.FNAL_TOE_LRR = FNAL_TOE_LRR;
    }

    public double getFNAL_TOE_RRR() {
        return FNAL_TOE_RRR;
    }

    public void setFNAL_TOE_RRR(double FNAL_TOE_RRR) {
        this.FNAL_TOE_RRR = FNAL_TOE_RRR;
    }

    public double getFNAL_ST_WHEEL_ANGLE() {
        return FNAL_ST_WHEEL_ANGLE;
    }

    public void setFNAL_ST_WHEEL_ANGLE(double FNAL_ST_WHEEL_ANGLE) {
        this.FNAL_ST_WHEEL_ANGLE = FNAL_ST_WHEEL_ANGLE;
    }

    public double getFNAL_SYMMETRY() {
        return FNAL_SYMMETRY;
    }

    public void setFNAL_SYMMETRY(double FNAL_SYMMETRY) {
        this.FNAL_SYMMETRY = FNAL_SYMMETRY;
    }

    public double getLFRT_TORQUE() {
        return LFRT_TORQUE;
    }

    public void setLFRT_TORQUE(double LFRT_TORQUE) {
        this.LFRT_TORQUE = LFRT_TORQUE;
    }

    public double getRFRT_TORQUE() {
        return RFRT_TORQUE;
    }

    public void setRFRT_TORQUE(double RFRT_TORQUE) {
        this.RFRT_TORQUE = RFRT_TORQUE;
    }

    public double getLRR_TORQUE() {
        return LRR_TORQUE;
    }

    public void setLRR_TORQUE(double LRR_TORQUE) {
        this.LRR_TORQUE = LRR_TORQUE;
    }

    public double getRRR_TORQUE() {
        return RRR_TORQUE;
    }

    public void setRRR_TORQUE(double RRR_TORQUE) {
        this.RRR_TORQUE = RRR_TORQUE;
    }

    public double getFRT_ATA_TIME() {
        return FRT_ATA_TIME;
    }

    public void setFRT_ATA_TIME(double FRT_ATA_TIME) {
        this.FRT_ATA_TIME = FRT_ATA_TIME;
    }

    public double getRR_ATA_TIME() {
        return RR_ATA_TIME;
    }

    public void setRR_ATA_TIME(double RR_ATA_TIME) {
        this.RR_ATA_TIME = RR_ATA_TIME;
    }

    public double getTOTAL_TIME() {
        return TOTAL_TIME;
    }

    public void setTOTAL_TIME(double TOTAL_TIME) {
        this.TOTAL_TIME = TOTAL_TIME;
    }

    public double getTimeDriveOn() {
        return timeDriveOn;
    }

    public void setTimeDriveOn(double timeDriveOn) {
        this.timeDriveOn = timeDriveOn;
    }

    public double getTimeSetup() {
        return timeSetup;
    }

    public void setTimeSetup(double timeSetup) {
        this.timeSetup = timeSetup;
    }

    public double getTimeLeaving() {
        return timeLeaving;
    }

    public void setTimeLeaving(double timeLeaving) {
        this.timeLeaving = timeLeaving;
    }

    public double getTimeEmpty() {
        return timeEmpty;
    }

    public void setTimeEmpty(double timeEmpty) {
        this.timeEmpty = timeEmpty;
    }

    public double getRearTries() {
        return rearTries;
    }

    public void setRearTries(double rearTries) {
        this.rearTries = rearTries;
    }

    public double getFrontTries() {
        return frontTries;
    }

    public void setFrontTries(double frontTries) {
        this.frontTries = frontTries;
    }

    public java.lang.String getLocationID() {
        return locationID;
    }

    public void setLocationID(java.lang.String locationID) {
        this.locationID = locationID;
    }

    public java.lang.String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(java.lang.String applicationID) {
        this.applicationID = applicationID;
    }

    public double getPRST_WOCV_TOE_LFRT() {
        return PRST_WOCV_TOE_LFRT;
    }

    public void setPRST_WOCV_TOE_LFRT(double PRST_WOCV_TOE_LFRT) {
        this.PRST_WOCV_TOE_LFRT = PRST_WOCV_TOE_LFRT;
    }

    public double getPRST_WOCV_TOE_RFRT() {
        return PRST_WOCV_TOE_RFRT;
    }

    public void setPRST_WOCV_TOE_RFRT(double PRST_WOCV_TOE_RFRT) {
        this.PRST_WOCV_TOE_RFRT = PRST_WOCV_TOE_RFRT;
    }

    public double getFNAL_WOCV_TOE_LFRT() {
        return FNAL_WOCV_TOE_LFRT;
    }

    public void setFNAL_WOCV_TOE_LFRT(double FNAL_WOCV_TOE_LFRT) {
        this.FNAL_WOCV_TOE_LFRT = FNAL_WOCV_TOE_LFRT;
    }

    public double getFNAL_WOCV_TOE_RFRT() {
        return FNAL_WOCV_TOE_RFRT;
    }

    public void setFNAL_WOCV_TOE_RFRT(double FNAL_WOCV_TOE_RFRT) {
        this.FNAL_WOCV_TOE_RFRT = FNAL_WOCV_TOE_RFRT;
    }

    public java.lang.String getPredictor_Mode() {
        return predictor_Mode;
    }

    public void setPredictor_Mode(java.lang.String predictor_Mode) {
        this.predictor_Mode = predictor_Mode;
    }

    public java.lang.String getFrontAuto_ReCheck() {
        return frontAuto_ReCheck;
    }

    public void setFrontAuto_ReCheck(java.lang.String frontAuto_ReCheck) {
        this.frontAuto_ReCheck = frontAuto_ReCheck;
    }

}
