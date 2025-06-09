package com.honda.mfg.stamp.conveyor.domain.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/5/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnumsTest {


    @Test
    public void successfullyTestAlarmTypes() {
        assertEquals(AlarmTypes.MES_CONNECTION_UNHEALTHY, AlarmTypes.findByType(701));
        assertEquals(701, AlarmTypes.MES_CONNECTION_UNHEALTHY.type());

        assertEquals(AlarmTypes.DB2_CONNECTION_UNHEALTHY, AlarmTypes.findByType(702));
        assertEquals(702, AlarmTypes.DB2_CONNECTION_UNHEALTHY.type());

        assertEquals(AlarmTypes.INSPECTION_REQUIRED_ALARM, AlarmTypes.findByType(703));
        assertEquals(703, AlarmTypes.INSPECTION_REQUIRED_ALARM.type());

        assertEquals(AlarmTypes.INVALID_DIE_ALARM, AlarmTypes.findByType(704));
        assertEquals(704, AlarmTypes.INVALID_DIE_ALARM.type());

        assertEquals(AlarmTypes.INVALID_STOP_ALARM, AlarmTypes.findByType(705));
        assertEquals(705, AlarmTypes.INVALID_STOP_ALARM.type());

        assertEquals(AlarmTypes.BACK_ORDER_ALARM, AlarmTypes.findByType(706));
        assertEquals(706, AlarmTypes.BACK_ORDER_ALARM.type());

        assertEquals(AlarmTypes.UNDEFINED, AlarmTypes.findByType(0));
        assertEquals(0, AlarmTypes.UNDEFINED.type());

        assertEquals(AlarmTypes.UNDEFINED, AlarmTypes.findByType(100));


    }

    @Test
    public void successfullyTestAlarmNotificationCategory() {
        assertEquals(AlarmNotificationCategory.MECHANICAL, AlarmNotificationCategory.findByType(0));
        assertEquals(0, AlarmNotificationCategory.MECHANICAL.type());

        assertEquals(AlarmNotificationCategory.TRACKING, AlarmNotificationCategory.findByType(1));
        assertEquals(1, AlarmNotificationCategory.TRACKING.type());

        assertEquals(AlarmNotificationCategory.INVENTORY, AlarmNotificationCategory.findByType(2));
        assertEquals(2, AlarmNotificationCategory.INVENTORY.type());

        assertEquals(AlarmNotificationCategory.ATTENTION, AlarmNotificationCategory.findByType(3));
        assertEquals(3, AlarmNotificationCategory.ATTENTION.type());

        assertEquals(AlarmNotificationCategory.INFORMATION, AlarmNotificationCategory.findByType(4));
        assertEquals(4, AlarmNotificationCategory.INFORMATION.type());

        assertEquals(AlarmNotificationCategory.INFORMATION, AlarmNotificationCategory.findByType(5));
    }

    @Test
    public void successfullyTestCarrierFulfillmentStatus() {

        assertEquals(CarrierFulfillmentStatus.SELECTED, CarrierFulfillmentStatus.findByType(0));
        assertEquals(0, CarrierFulfillmentStatus.SELECTED.status());

        assertEquals(CarrierFulfillmentStatus.RETRIEVED, CarrierFulfillmentStatus.findByType(1));
        assertEquals(1, CarrierFulfillmentStatus.RETRIEVED.status());

        assertEquals(CarrierFulfillmentStatus.RELEASED, CarrierFulfillmentStatus.findByType(2));
        assertEquals(2, CarrierFulfillmentStatus.RELEASED.status());

        assertEquals(CarrierFulfillmentStatus.DELIVERED, CarrierFulfillmentStatus.findByType(3));
        assertEquals(3, CarrierFulfillmentStatus.DELIVERED.status());

        assertEquals(CarrierFulfillmentStatus.CONSUMED, CarrierFulfillmentStatus.findByType(4));
        assertEquals(4, CarrierFulfillmentStatus.CONSUMED.status());

        assertEquals(CarrierFulfillmentStatus.QUEUED, CarrierFulfillmentStatus.findByType(5));
        assertEquals(5, CarrierFulfillmentStatus.QUEUED.status());

        assertEquals(CarrierFulfillmentStatus.SELECTED_TO_DELIVER, CarrierFulfillmentStatus.findByType(6));
        assertEquals(6, CarrierFulfillmentStatus.SELECTED_TO_DELIVER.status());

        assertEquals(CarrierFulfillmentStatus.READY_TO_DELIVER, CarrierFulfillmentStatus.findByType(7));
        assertEquals(7, CarrierFulfillmentStatus.READY_TO_DELIVER.status());

        assertEquals(CarrierFulfillmentStatus.SELECTED, CarrierFulfillmentStatus.findByType(8));
    }

    @Test
    public void successfullyTestCarrierStatus() {
        assertEquals(CarrierStatus.SHIPPABLE, CarrierStatus.findByType(0));
        assertEquals(0, CarrierStatus.SHIPPABLE.type());

        assertEquals(CarrierStatus.ON_HOLD, CarrierStatus.findByType(1));
        assertEquals(1, CarrierStatus.ON_HOLD.type());

        assertEquals(CarrierStatus.INSPECTION_REQUIRED, CarrierStatus.findByType(2));
        assertEquals(2, CarrierStatus.INSPECTION_REQUIRED.type());

//        assertEquals(CarrierStatus.MAINTENANCE_REQUIRED, CarrierStatus.findByType(3));
//        assertEquals(3, CarrierStatus.MAINTENANCE_REQUIRED.type());

        assertEquals(CarrierStatus.SHIPPABLE, CarrierStatus.findByType(4));
    }


    @Test
    public void successfullyTestLaneCapacity() {
        assertEquals(LaneVolumeStorage.HIGH_VOLUME_STORAGE, LaneVolumeStorage.valueOf("HIGH_VOLUME_STORAGE"));
        assertEquals(0, LaneVolumeStorage.HIGH_VOLUME_STORAGE.type());
        assertEquals(LaneVolumeStorage.MEDIUM_VOLUME_STORAGE, LaneVolumeStorage.valueOf("MEDIUM_VOLUME_STORAGE"));
        assertEquals(1, LaneVolumeStorage.MEDIUM_VOLUME_STORAGE.type());
        assertEquals(LaneVolumeStorage.LOW_VOLUME_STORAGE, LaneVolumeStorage.valueOf("LOW_VOLUME_STORAGE"));
        assertEquals(2, LaneVolumeStorage.LOW_VOLUME_STORAGE.type());
        assertEquals(LaneVolumeStorage.ROW_35_HIGH_VOLUME_STORAGE, LaneVolumeStorage.valueOf("ROW_35_HIGH_VOLUME_STORAGE"));
        assertEquals(3, LaneVolumeStorage.ROW_35_HIGH_VOLUME_STORAGE.type());
    }

    @Test
    public void successfullyTestLaneCondition() {
        assertEquals(LaneCondition.VACANT, LaneCondition.valueOf("VACANT"));
        assertEquals(LaneCondition.EMPTY, LaneCondition.valueOf("EMPTY"));
        assertEquals(LaneCondition.PARTIAL, LaneCondition.valueOf("PARTIAL"));
        assertEquals(LaneCondition.MIXED_BACK, LaneCondition.valueOf("MIXED_BACK"));
        assertEquals(LaneCondition.MIXED_FRONT, LaneCondition.valueOf("MIXED_FRONT"));
        assertEquals(LaneCondition.MIXED_BLOCK, LaneCondition.valueOf("MIXED_BLOCK"));
        assertEquals(LaneCondition.MIXED, LaneCondition.valueOf("MIXED"));
        assertEquals(LaneCondition.FULL, LaneCondition.valueOf("FULL"));
    }

    @Test
    public void successfullyTestOrderStatus() {

        assertEquals(OrderStatus.Queued, OrderStatus.findByType(0));
        assertEquals(0, OrderStatus.Queued.status());

        assertEquals(OrderStatus.InProcess, OrderStatus.findByType(1));
        assertEquals(1, OrderStatus.InProcess.status());

        assertEquals(OrderStatus.Delivered, OrderStatus.findByType(2));
        assertEquals(2, OrderStatus.Delivered.status());

        assertEquals(OrderStatus.ManuallyCompleted, OrderStatus.findByType(3));
        assertEquals(3, OrderStatus.ManuallyCompleted.status());

        assertEquals(OrderStatus.Cancelled, OrderStatus.findByType(4));
        assertEquals(4, OrderStatus.Cancelled.status());

        assertEquals(OrderStatus.AutoCompleted, OrderStatus.findByType(5));
        assertEquals(5, OrderStatus.AutoCompleted.status());

        assertEquals(OrderStatus.OnHold, OrderStatus.findByType(6));
        assertEquals(6, OrderStatus.OnHold.status());

        assertEquals(OrderStatus.RetrievingCarriers, OrderStatus.findByType(7));
        assertEquals(7, OrderStatus.RetrievingCarriers.status());

        assertEquals(OrderStatus.SelectingCarriers, OrderStatus.findByType(9));
        assertEquals(9, OrderStatus.SelectingCarriers.status());

        assertEquals(OrderStatus.DeliveringCarriers, OrderStatus.findByType(8));
        assertEquals(8, OrderStatus.DeliveringCarriers.status());

        assertEquals(OrderStatus.Initialized, OrderStatus.findByType(10));
        assertEquals(10, OrderStatus.Initialized.status());

        assertEquals(OrderStatus.Initialized, OrderStatus.findByType(11));

    }

    @Test
    public void successfullyTestPartProductionVolume() {

        assertEquals(PartProductionVolume.HIGH_VOLUME, PartProductionVolume.valueOf("HIGH_VOLUME"));
        assertEquals(PartProductionVolume.MEDIUM_VOLUME, PartProductionVolume.valueOf("MEDIUM_VOLUME"));
        assertEquals(PartProductionVolume.LOW_VOLUME, PartProductionVolume.valueOf("LOW_VOLUME"));
        assertEquals(PartProductionVolume.EMPTY, PartProductionVolume.valueOf("EMPTY"));
    }

    @Test
    public void successfullyTestPress() {

        assertEquals(Press.REWORK_C_LINE, Press.findByType(405));
        assertEquals(405, Press.REWORK_C_LINE.type());

        assertEquals(Press.PRESS_C_ROBOT_1, Press.findByType(502));
        assertEquals(502, Press.PRESS_C_ROBOT_1.type());

        assertEquals(Press.PRESS_C_ROBOT_2, Press.findByType(506));
        assertEquals(506, Press.PRESS_C_ROBOT_2.type());

        assertEquals(Press.PRESS_B_ROBOT_1, Press.findByType(2011));
        assertEquals(2011, Press.PRESS_B_ROBOT_1.type());

        assertEquals(Press.PRESS_B_ROBOT_2, Press.findByType(2007));
        assertEquals(2007, Press.PRESS_B_ROBOT_2.type());

        assertEquals(Press.KD_AREA, Press.findByType(800));
        assertEquals(800, Press.KD_AREA.type());

        assertEquals(Press.WELD_LINE1_ROBOT_1, Press.findByType(3700));
        assertEquals(3700, Press.WELD_LINE1_ROBOT_1.type());

        assertEquals(Press.WELD_LINE1_ROBOT_2, Press.findByType(1800));
        assertEquals(1800, Press.WELD_LINE1_ROBOT_2.type());

        assertEquals(Press.WELD_LINE2_ROBOT_1, Press.findByType(10900));
        assertEquals(10900, Press.WELD_LINE2_ROBOT_1.type());

        assertEquals(Press.WELD_LINE2_ROBOT_2, Press.findByType(11900));
        assertEquals(11900, Press.WELD_LINE2_ROBOT_2.type());

        assertEquals(Press.REWORK_C_LINE, Press.findByType(123));
    }


    @Test
    public void successfullyTestDefectType() {

        assertEquals(DEFECT_TYPE.BAD_HANDWORK, DEFECT_TYPE.findByType(0));
        assertEquals(0, DEFECT_TYPE.BAD_HANDWORK.type());

        assertEquals(DEFECT_TYPE.BURR, DEFECT_TYPE.findByType(1));
        assertEquals(1, DEFECT_TYPE.BURR.type());

        assertEquals(DEFECT_TYPE.DIE_CRACK, DEFECT_TYPE.findByType(2));
        assertEquals(2, DEFECT_TYPE.DIE_CRACK.type());

        assertEquals(DEFECT_TYPE.DOUBLE_HIGH_LINE, DEFECT_TYPE.findByType(3));
        assertEquals(3, DEFECT_TYPE.DOUBLE_HIGH_LINE.type());

        assertEquals(DEFECT_TYPE.LASER_BLOWOUT, DEFECT_TYPE.findByType(4));
        assertEquals(4, DEFECT_TYPE.LASER_BLOWOUT.type());

        assertEquals(DEFECT_TYPE.LASER_CRACK, DEFECT_TYPE.findByType(5));
        assertEquals(5, DEFECT_TYPE.LASER_CRACK.type());

        assertEquals(DEFECT_TYPE.LASER_PINHOLE, DEFECT_TYPE.findByType(6));
        assertEquals(6, DEFECT_TYPE.LASER_PINHOLE.type());

        assertEquals(DEFECT_TYPE.MATERIAL, DEFECT_TYPE.findByType(7));
        assertEquals(7, DEFECT_TYPE.MATERIAL.type());

        assertEquals(DEFECT_TYPE.MATERIAL_CRACK, DEFECT_TYPE.findByType(8));
        assertEquals(8, DEFECT_TYPE.MATERIAL_CRACK.type());

        assertEquals(DEFECT_TYPE.MINUS_DEFORM, DEFECT_TYPE.findByType(9));
        assertEquals(9, DEFECT_TYPE.MINUS_DEFORM.type());

        assertEquals(DEFECT_TYPE.OIL_LINES, DEFECT_TYPE.findByType(10));
        assertEquals(10, DEFECT_TYPE.OIL_LINES.type());

        assertEquals(DEFECT_TYPE.OVERLAP, DEFECT_TYPE.findByType(11));
        assertEquals(11, DEFECT_TYPE.OVERLAP.type());

        assertEquals(DEFECT_TYPE.PLUS_DEFORM, DEFECT_TYPE.findByType(12));
        assertEquals(12, DEFECT_TYPE.PLUS_DEFORM.type());

        assertEquals(DEFECT_TYPE.SCRAP_IN_DIE, DEFECT_TYPE.findByType(13));
        assertEquals(13, DEFECT_TYPE.SCRAP_IN_DIE.type());

        assertEquals(DEFECT_TYPE.SCRATCH, DEFECT_TYPE.findByType(14));
        assertEquals(14, DEFECT_TYPE.SCRATCH.type());

        assertEquals(DEFECT_TYPE.TWIST, DEFECT_TYPE.findByType(15));
        assertEquals(15, DEFECT_TYPE.TWIST.type());

        assertEquals(DEFECT_TYPE.WAVES, DEFECT_TYPE.findByType(16));
        assertEquals(16, DEFECT_TYPE.WAVES.type());

        assertEquals(DEFECT_TYPE.WRINKLES, DEFECT_TYPE.findByType(17));
        assertEquals(17, DEFECT_TYPE.WRINKLES.type());

        assertEquals(DEFECT_TYPE.OTHER, DEFECT_TYPE.findByType(18));
        assertEquals(18, DEFECT_TYPE.OTHER.type());

        assertEquals(DEFECT_TYPE.BAD_HANDWORK, DEFECT_TYPE.findByType(19));
    }

    @Test
    public void successfullyTestReworkMethod() {

        assertEquals(REWORK_METHOD.DA, REWORK_METHOD.findByMethod(0));
        assertEquals(0, REWORK_METHOD.DA.method());

        assertEquals(REWORK_METHOD.FILE_AND_DA, REWORK_METHOD.findByMethod(1));
        assertEquals(1, REWORK_METHOD.FILE_AND_DA.method());

        assertEquals(REWORK_METHOD.STRAIGHTEN_WITH_PLIERS, REWORK_METHOD.findByMethod(2));
        assertEquals(2, REWORK_METHOD.STRAIGHTEN_WITH_PLIERS.method());

        assertEquals(REWORK_METHOD.CANNOT_REPAIR_SCRAP, REWORK_METHOD.findByMethod(3));
        assertEquals(3, REWORK_METHOD.CANNOT_REPAIR_SCRAP.method());

        assertEquals(REWORK_METHOD.BRAIZE, REWORK_METHOD.findByMethod(4));
        assertEquals(4, REWORK_METHOD.BRAIZE.method());

        assertEquals(REWORK_METHOD.MIG_WELD, REWORK_METHOD.findByMethod(5));
        assertEquals(5, REWORK_METHOD.MIG_WELD.method());

        assertEquals(REWORK_METHOD.SCOTCH_BRIGHT, REWORK_METHOD.findByMethod(6));
        assertEquals(6, REWORK_METHOD.SCOTCH_BRIGHT.method());

        assertEquals(REWORK_METHOD.UNDETERMINED, REWORK_METHOD.findByMethod(7));
        assertEquals(7, REWORK_METHOD.UNDETERMINED.method());

        assertEquals(REWORK_METHOD.DEBURR, REWORK_METHOD.findByMethod(8));
        assertEquals(8, REWORK_METHOD.DEBURR.method());

        assertEquals(REWORK_METHOD.DA, REWORK_METHOD.findByMethod(9));
    }

    @Test
    public void successfullyTestSeverity() {

        assertEquals(SEVERITY.ONE, SEVERITY.findByType(1));
        assertEquals(1, SEVERITY.ONE.type());

        assertEquals(SEVERITY.TWO, SEVERITY.findByType(2));
        assertEquals(2, SEVERITY.TWO.type());

        assertEquals(SEVERITY.THREE, SEVERITY.findByType(3));
        assertEquals(3, SEVERITY.THREE.type());

        assertEquals(SEVERITY.FOUR, SEVERITY.findByType(4));
        assertEquals(4, SEVERITY.FOUR.type());

        assertEquals(SEVERITY.NONE, SEVERITY.findByType(0));
        assertEquals(0, SEVERITY.NONE.type());

        assertEquals(SEVERITY.FOUR, SEVERITY.findByType(5));
    }

    @Test
    public void successfullyTestStopArea() {

        assertEquals(StopArea.UNDEFINED, StopArea.findByType(0));
        assertEquals(0, StopArea.UNDEFINED.type());

        assertEquals(StopArea.STORE_IN_ROUTE, StopArea.findByType(1));
        assertEquals(1, StopArea.STORE_IN_ROUTE.type());

        assertEquals(StopArea.ROW, StopArea.findByType(2));
        assertEquals(2, StopArea.ROW.type());

        assertEquals(StopArea.STORE_OUT_ROUTE, StopArea.findByType(3));
        assertEquals(3, StopArea.STORE_OUT_ROUTE.type());

        assertEquals(StopArea.WELD_LINE_1, StopArea.findByType(4));
        assertEquals(4, StopArea.WELD_LINE_1.type());

        assertEquals(StopArea.WELD_LINE_2, StopArea.findByType(5));
        assertEquals(5, StopArea.WELD_LINE_2.type());

        assertEquals(StopArea.KD_LINE, StopArea.findByType(6));
        assertEquals(6, StopArea.KD_LINE.type());

        assertEquals(StopArea.OLD_WELD_LINE, StopArea.findByType(7));
        assertEquals(7, StopArea.OLD_WELD_LINE.type());

        assertEquals(StopArea.EMPTY_AREA, StopArea.findByType(8));
        assertEquals(8, StopArea.EMPTY_AREA.type());

        assertEquals(StopArea.B_PRESS, StopArea.findByType(9));
        assertEquals(9, StopArea.B_PRESS.type());

        assertEquals(StopArea.C_PRESS, StopArea.findByType(10));
        assertEquals(10, StopArea.C_PRESS.type());

        assertEquals(StopArea.B_AREA, StopArea.findByType(11));
        assertEquals(11, StopArea.B_AREA.type());

        assertEquals(StopArea.Q_WELD_LINE_1, StopArea.findByType(12));
        assertEquals(12, StopArea.Q_WELD_LINE_1.type());

        assertEquals(StopArea.Q_WELD_LINE_2, StopArea.findByType(13));
        assertEquals(13, StopArea.Q_WELD_LINE_2.type());

        assertEquals(StopArea.UNDEFINED, StopArea.findByType(14));
    }

    @Test
    public void successfullyTestStopAvailability() {

        assertEquals(StopAvailability.AVAILABLE, StopAvailability.findByType(0));
        assertEquals(0, StopAvailability.AVAILABLE.type());

        assertEquals(StopAvailability.BLOCKED, StopAvailability.findByType(1));
        assertEquals(1, StopAvailability.BLOCKED.type());

        assertEquals(StopAvailability.AVAILABLE, StopAvailability.findByType(2));
    }

    @Test
    public void successfullyTestStopType() {

        assertEquals(StopType.STORE_IN_ALL_LANES, StopType.findByType(0));
        assertEquals(0, StopType.STORE_IN_ALL_LANES.type());

        assertEquals(StopType.STORE_IN_C_LOW_LANES, StopType.findByType(1));
        assertEquals(1, StopType.STORE_IN_C_LOW_LANES.type());

        assertEquals(StopType.STORE_IN_C_HIGH_LANES, StopType.findByType(2));
        assertEquals(2, StopType.STORE_IN_C_HIGH_LANES.type());

        assertEquals(StopType.RECIRC_TO_ALL_ROWS, StopType.findByType(3));
        assertEquals(3, StopType.RECIRC_TO_ALL_ROWS.type());

        assertEquals(StopType.NO_ACTION, StopType.findByType(4));
        assertEquals(4, StopType.NO_ACTION.type());

        assertEquals(StopType.DELIVERY_INSPECTION, StopType.findByType(5));
        assertEquals(5, StopType.DELIVERY_INSPECTION.type());

        assertEquals(StopType.PRESS_INSPECTION, StopType.findByType(6));
        assertEquals(6, StopType.PRESS_INSPECTION.type());

        assertEquals(StopType.REWORK, StopType.findByType(7));
        assertEquals(7, StopType.REWORK.type());

        assertEquals(StopType.FULL_CARRIER_DELIVERY, StopType.findByType(8));
        assertEquals(8, StopType.FULL_CARRIER_DELIVERY.type());

        assertEquals(StopType.FULL_CARRIER_CONSUMPTION, StopType.findByType(9));
        assertEquals(9, StopType.FULL_CARRIER_CONSUMPTION.type());

        assertEquals(StopType.RELEASE_CHECK, StopType.findByType(10));
        assertEquals(10, StopType.RELEASE_CHECK.type());

        assertEquals(StopType.LEFT_CONSUMED_CARRIER_EXIT, StopType.findByType(11));
        assertEquals(11, StopType.LEFT_CONSUMED_CARRIER_EXIT.type());

        assertEquals(StopType.RIGHT_CONSUMED_CARRIER_EXIT, StopType.findByType(12));
        assertEquals(12, StopType.RIGHT_CONSUMED_CARRIER_EXIT.type());

        assertEquals(StopType.MAINTENANCE, StopType.findByType(13));
        assertEquals(13, StopType.MAINTENANCE.type());

        assertEquals(StopType.EMPTY_CARRIER_DELIVERY, StopType.findByType(14));
        assertEquals(14, StopType.EMPTY_CARRIER_DELIVERY.type());

        assertEquals(StopType.NO_ACTION, StopType.findByType(15));

    }

    @Test
    public void successfullyTestStorageArea() {
        assertEquals(StorageArea.C_HIGH, StorageArea.valueOf("C_HIGH"));
        assertEquals(StorageArea.C_LOW, StorageArea.valueOf("C_LOW"));
        assertEquals(StorageArea.A_AREA, StorageArea.valueOf("A_AREA"));
        assertEquals(StorageArea.S_AREA, StorageArea.valueOf("S_AREA"));
        assertEquals(StorageArea.B_AREA, StorageArea.valueOf("B_AREA"));
    }

    @Test
    public void successfullyTestContactType() {

        assertEquals(ContactType.EMAIL, ContactType.findByType(0));
        assertEquals(0, ContactType.EMAIL.type());

        assertEquals(ContactType.PAGER, ContactType.findByType(1));
        assertEquals(1, ContactType.PAGER.type());

        assertEquals(ContactType.BOTH, ContactType.findByType(2));
        assertEquals(2, ContactType.BOTH.type());

        assertEquals(ContactType.EMAIL, ContactType.findByType(3));
    }

    @Test
    public void successfullyTestAutoArchiveTime() {
        assertEquals(AutoArchiveTime.NEVER, AutoArchiveTime.findByTime(0));
        assertEquals(0, AutoArchiveTime.NEVER.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.SECONDS_30, AutoArchiveTime.findByTime(0.5));
        assertEquals(0.5, AutoArchiveTime.SECONDS_30.getTimeInMin(), 1);

        assertEquals(AutoArchiveTime.MINUTES_1, AutoArchiveTime.findByTime(1));
        assertEquals(1, AutoArchiveTime.MINUTES_1.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.MINUTES_5, AutoArchiveTime.findByTime(5));
        assertEquals(5, AutoArchiveTime.MINUTES_5.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.MINUTES_10, AutoArchiveTime.findByTime(10));
        assertEquals(10, AutoArchiveTime.MINUTES_10.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.MINUTES_30, AutoArchiveTime.findByTime(30));
        assertEquals(30, AutoArchiveTime.MINUTES_30.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.HOURS_1, AutoArchiveTime.findByTime(60));
        assertEquals(60, AutoArchiveTime.HOURS_1.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.HOURS_4, AutoArchiveTime.findByTime(240));
        assertEquals(240, AutoArchiveTime.HOURS_4.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.HOURS_8, AutoArchiveTime.findByTime(480));
        assertEquals(480, AutoArchiveTime.HOURS_8.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.HOURS_12, AutoArchiveTime.findByTime(720));
        assertEquals(720, AutoArchiveTime.HOURS_12.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.HOURS_24, AutoArchiveTime.findByTime(1440));
        assertEquals(1440, AutoArchiveTime.HOURS_24.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.HOURS_48, AutoArchiveTime.findByTime(2880));
        assertEquals(2880, AutoArchiveTime.HOURS_48.getTimeInMin(), 0);

        assertEquals(AutoArchiveTime.NEVER, AutoArchiveTime.findByTime(3));
    }

    @Test
    public void successfullyTestStoragePriority() {
        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.EMPTY, StoragePriority.Priority.ONE), StorageArea.A_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.EMPTY, StorageArea.A_AREA), StoragePriority.Priority.ONE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.EMPTY, StoragePriority.Priority.TWO), StorageArea.C_LOW);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.EMPTY, StorageArea.C_LOW), StoragePriority.Priority.TWO);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.EMPTY, StoragePriority.Priority.THREE), StorageArea.B_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.EMPTY, StorageArea.B_AREA), StoragePriority.Priority.THREE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.EMPTY, StoragePriority.Priority.FOUR), StorageArea.C_HIGH);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.EMPTY, StorageArea.C_HIGH), StoragePriority.Priority.FOUR);


        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.HIGH_VOLUME, StoragePriority.Priority.ONE), StorageArea.S_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.HIGH_VOLUME, StorageArea.S_AREA), StoragePriority.Priority.ONE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.HIGH_VOLUME, StoragePriority.Priority.TWO), StorageArea.B_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.HIGH_VOLUME, StorageArea.B_AREA), StoragePriority.Priority.TWO);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.HIGH_VOLUME, StoragePriority.Priority.THREE), StorageArea.C_LOW);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.HIGH_VOLUME, StorageArea.C_LOW), StoragePriority.Priority.THREE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.HIGH_VOLUME, StoragePriority.Priority.FOUR), StorageArea.C_HIGH);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.HIGH_VOLUME, StorageArea.C_HIGH), StoragePriority.Priority.FOUR);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.HIGH_VOLUME, StoragePriority.Priority.FIVE), StorageArea.A_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.HIGH_VOLUME, StorageArea.A_AREA), StoragePriority.Priority.FIVE);


        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.MEDIUM_VOLUME, StoragePriority.Priority.ONE), StorageArea.C_LOW);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.MEDIUM_VOLUME, StorageArea.C_LOW), StoragePriority.Priority.ONE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.MEDIUM_VOLUME, StoragePriority.Priority.TWO), StorageArea.B_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.MEDIUM_VOLUME, StorageArea.B_AREA), StoragePriority.Priority.TWO);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.MEDIUM_VOLUME, StoragePriority.Priority.THREE), StorageArea.C_HIGH);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.MEDIUM_VOLUME, StorageArea.C_HIGH), StoragePriority.Priority.THREE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.MEDIUM_VOLUME, StoragePriority.Priority.FOUR), StorageArea.A_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.MEDIUM_VOLUME, StorageArea.A_AREA), StoragePriority.Priority.FOUR);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.MEDIUM_VOLUME, StoragePriority.Priority.FIVE), StorageArea.S_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.MEDIUM_VOLUME, StorageArea.S_AREA), StoragePriority.Priority.FIVE);


         assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.LOW_VOLUME, StoragePriority.Priority.ONE), StorageArea.C_HIGH);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.LOW_VOLUME, StorageArea.C_HIGH), StoragePriority.Priority.ONE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.LOW_VOLUME, StoragePriority.Priority.TWO), StorageArea.C_LOW);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.LOW_VOLUME, StorageArea.C_LOW), StoragePriority.Priority.TWO);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.LOW_VOLUME, StoragePriority.Priority.THREE), StorageArea.B_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.LOW_VOLUME, StorageArea.B_AREA), StoragePriority.Priority.THREE);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.LOW_VOLUME, StoragePriority.Priority.FOUR), StorageArea.A_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.LOW_VOLUME, StorageArea.A_AREA), StoragePriority.Priority.FOUR);

        assertEquals(StoragePriority.getStorageAreaByPriorityForVolume(PartProductionVolume.LOW_VOLUME, StoragePriority.Priority.FIVE), StorageArea.S_AREA);
        assertEquals(StoragePriority.getStoragePriorityByArea(PartProductionVolume.LOW_VOLUME, StorageArea.S_AREA), StoragePriority.Priority.FIVE);

    }

}
