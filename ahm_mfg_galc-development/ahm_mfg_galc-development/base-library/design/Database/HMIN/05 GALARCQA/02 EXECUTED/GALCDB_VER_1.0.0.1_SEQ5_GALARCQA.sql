--Update the comments for Regional Manufacturing Team DB Objects for GALCDB 1.0

COMMENT ON TABLE GALADM.GAL105TBX IS
   'RMT AccessControlManager for user security';
COMMENT ON TABLE GALADM.GAL108TBX IS
   'RMT BearingSelectResult for tracking bearing used in a specific engine';
COMMENT ON TABLE GALADM.GAL111TBX IS
   'RMT BroadcastDestination for setting up printing on a process point';
COMMENT ON TABLE GALADM.GAL117TBX IS 'RMT Site';
COMMENT ON TABLE GALADM.GAL118TBX IS
   'RMT CounterByModelGroup for summarizing number of models built by process point, production date, shift, period';
COMMENT ON TABLE GALADM.GAL119TBX IS
   'RMT CounterByProductSpec for summarizing number of product specs built by process point, production date, shift, period';
COMMENT ON TABLE GALADM.GAL120TBX IS
   'RMT CounterByProductionLot for summarizing number of production lots built by process point, production date, shift, period';
COMMENT ON TABLE GALADM.GAL123TBX IS 'RMT DefectGroup for QICS';
COMMENT ON TABLE GALADM.GAL125TBX IS
   'RMT DefectResult for QICS recording defects to product';
COMMENT ON TABLE GALADM.GAL126TBX IS
   'RMT DefectType for QICS to define defect type name';
COMMENT ON TABLE GALADM.GAL127TBX IS 'RMT DefectTypeDescription for QICS';
COMMENT ON TABLE GALADM.GAL128TBX IS 'RMT Division';
COMMENT ON TABLE GALADM.GAL131TBX IS 'RMT Engine';

COMMENT ON TABLE GALADM.GAL133TBX IS 'RMT EngineSpec';
COMMENT ON TABLE GALADM.GAL135TBX IS 'RMT ExpectedProduct for LOT CONTROL';
COMMENT ON TABLE GALADM.GAL136TBX IS 'RMT ExceptionalOut';

COMMENT ON TABLE GALADM.GAL141TBX IS 'RMT EngineFiringResult';
COMMENT ON TABLE GALADM.GAL143TBX IS 'RMT Frame';
COMMENT ON TABLE GALADM.GAL144TBX IS 'RMT FrameSpec';
COMMENT ON TABLE GALADM.GAL147TBX IS 'RMT HoldResult';

COMMENT ON TABLE GALADM.GAL158TBX IS
   'RMT HostMtoc for OIF Frame MTO Master Specification';

COMMENT ON TABLE GALADM.GAL173TBX IS 'RMT Image for QICS';
COMMENT ON TABLE GALADM.GAL174TBX IS 'RMT ImageSection for QICS';
COMMENT ON TABLE GALADM.GAL175TBX IS 'RMT ImageSectionPoint for QICS';
COMMENT ON TABLE GALADM.GAL176TBX IS 'RMT InProcessProduct for LINE CONTROL';
COMMENT ON TABLE GALADM.GAL177TBX IS 'RMT InRepairArea for QICS';

COMMENT ON TABLE GALADM.GAL178TBX IS 'RMT InspectionModel for QICS';
COMMENT ON TABLE GALADM.GAL179TBX IS 'RMT InspectionPart for QICS';
COMMENT ON TABLE GALADM.GAL180TBX IS 'RMT InspectionPartDescription for QICS';
COMMENT ON TABLE GALADM.GAL181TBX IS 'RMT InspectionPartLocation for QICS';

COMMENT ON TABLE GALADM.GAL185_HIST_TBX IS
   'RMT InstalledPartHistory for LOT CONTROL to keep history of Installed Parts per Product';

COMMENT ON TABLE GALADM.GAL185TBX IS 'RMT InstalledPart for LOT CONTROL';
COMMENT ON TABLE GALADM.GAL191TBX IS
   'RMT IPPTag for QICS to track Initial Production Part Tag';
COMMENT ON TABLE GALADM.GAL192TBX IS 'RMT IQS';
COMMENT ON TABLE GALADM.GAL195TBX IS 'RMT Line';
COMMENT ON TABLE GALADM.GAL198TBX IS 'RMT Measurement';

COMMENT ON TABLE GALADM.GAL205TBX IS 'RMT ParkingAddress';
COMMENT ON TABLE GALADM.GAL206TBX IS
   'RMT Part for LOT CONTROL - Replaced by PART_SPEC_TBX and MEASUREMENT_SPEC_TBX';

COMMENT ON TABLE GALADM.GAL208TBX IS 'RMT PartGroup for QICS';
COMMENT ON TABLE GALADM.GAL211TBX IS 'RMT Plant';
COMMENT ON TABLE GALADM.GAL212TBX IS 'RMT PreProductionLot for LINE CONTROL';
COMMENT ON TABLE GALADM.GAL214TBX IS 'RMT ProcessPoint';
COMMENT ON TABLE GALADM.GAL215TBX IS
   'RMT ProductResult for LINE CONTROL to record history of Product through Process Points';

COMMENT ON TABLE GALADM.GAL216TBX IS 'RMT ProductStampingSequence';
COMMENT ON TABLE GALADM.GAL217TBX IS 'RMT ProductionLot';
COMMENT ON TABLE GALADM.GAL219TBX IS 'RMT Regression';
COMMENT ON TABLE GALADM.GAL222TBX IS
   'RMT DefectRepairResult for QICS to record repairs for each product defect';

COMMENT ON TABLE GALADM.GAL224TBX IS 'RMT ReuseProductResult';
COMMENT ON TABLE GALADM.GAL226TBX IS
   'RMT DailyDepartmentSchedule for LINE CONTROL to define production working periods per GPCS Plan Code';
COMMENT ON TABLE GALADM.GAL227TBX IS 'RMT SecondaryPart for QICS';
COMMENT ON TABLE GALADM.GAL234TBX IS 'RMT Terminal';
COMMENT ON TABLE GALADM.GAL235TBX IS 'RMT Zone';
COMMENT ON TABLE GALADM.GAL236TBX IS 'RMT PreviousLine';
COMMENT ON TABLE GALADM.GAL237TBX IS 'RMT GpcsPlant';
COMMENT ON TABLE GALADM.GAL238TBX IS 'RMT GpcsDivision';
COMMENT ON TABLE GALADM.GAL240TBX IS 'RMT Counter';
COMMENT ON TABLE GALADM.GAL241TBX IS 'RMT Application';
COMMENT ON TABLE GALADM.GAL242TBX IS 'RMT ApplicationByTerminal';
COMMENT ON TABLE GALADM.GAL243TBX IS 'RMT ApplicationTask';
COMMENT ON TABLE GALADM.GAL244TBX IS 'RMT TaskSpec';
COMMENT ON TABLE GALADM.GAL245TBX IS
   'RMT PartByProductSpecCode for LOT CONTROL to define Installed Part IDs per Product Spec Code';
COMMENT ON TABLE GALADM.GAL246TBX IS
   'RMT LotControlRule for LOT CONTROL to define Installed Part rules per Process Point and Product Spec Code';

COMMENT ON TABLE GALADM.GAL253TBX IS 'RMT Device for LOT CONTROL';
COMMENT ON TABLE GALADM.GAL257TBX IS
   'RMT DeviceFormat for LOT CONTROL to configure Device by Client ID';
COMMENT ON TABLE GALADM.GAL258TBX IS
   'RMT PrintAttributeFormat for LOT CONTROL by Form';
COMMENT ON TABLE GALADM.GAL259TBX IS
   'RMT BuildAttribute for LOT CONTROL by Product Spec Code';
COMMENT ON TABLE GALADM.GAL260TBX IS
   'RMT StationResult for QICS Station Result per process point, production date and shift';
   
COMMENT ON TABLE GALADM.GAL261TBX IS
   'RMT PartName for LOT CONTROL to manage Installed Part';
COMMENT ON TABLE GALADM.GAL263TBX IS
   'RMT ShippingStatus for LINE CONTROL to manage FRAME (Vin) shipping status';
COMMENT ON TABLE GALADM.GAL282TBX IS
   'RMT UserSecurityGroup for SECURITY to manage users to groups';
COMMENT ON TABLE GALADM.GAL283TBX IS
   'RMT AccessControlEntry for SECURITY to manage access to Terminal Applications (Screen ID)';
COMMENT ON TABLE GALADM.GAL287TBX IS
   'RMT ApplicationMenuEntry for managing Menu Items on Terminals';
COMMENT ON TABLE GALADM.GAL294TBX IS
   'RMT PrintForm for LOT CONTROL to manage forms to printers';
COMMENT ON TABLE GALADM.GAL322TBX IS 'RMT DefectDescription for QICS';
COMMENT ON TABLE GALADM.GAL324TBX IS 'RMT SecurityGroup for SECURITY';
COMMENT ON TABLE GALADM.GAL364TBX IS
   'RMT InspectionTwoPartDescription for QICS';
COMMENT ON TABLE GALADM.GAL489TBX IS
   'RMT ComponentProperty for managing application properties';
COMMENT ON TABLE GALADM.GAL600TBX IS 'RMT AdminUser for SECURITY';
COMMENT ON TABLE GALADM.GAL601TBX IS 'RMT AdminGroup for SECURITY';
COMMENT ON TABLE GALADM.GAL602TBX IS 'RMT AdminUserGroup for SECURITY';
COMMENT ON TABLE GALADM.GAL650TBX IS
   'RMT OpcConfigEntry for LOT CONTROL to manage OPC EI configuration';


COMMENT ON TABLE GALADM.MBPN_PRODUCT_TBX IS
   'RMT MbpnProduct for tracking product id of type Manufacturing Base Part Number';

COMMENT ON GALADM.MBPN_PRODUCT_TBX (
 HOLD_STATUS_ID IS 'Link to HOLD_STATUS_TBX',
 PRODUCT_STATUS_ID IS 'Link to PRODUCT_STATUS_TBX' );

COMMENT ON TABLE GALADM.MBPN_TBX IS
   'RMT Mbpn for defining Manufacturing Base Part Number product spec codes';
COMMENT ON TABLE GALADM.MEASUREMENT_SPEC_TBX IS
   'RMT MeasurementSpec for LOT CONTROL to define Installed Part Measurement Specs';

COMMENT ON TABLE GALADM.NOTIFICATION_PROVIDER_TBX IS
   'RMT NotificationProvider for managing hosts setup for asynchronous communication';
COMMENT ON TABLE GALADM.NOTIFICATION_SUBSCRIBER_TBX IS
   'RMT NotificationSubscriber for managing clients setup for asynchronous communication';

COMMENT ON TABLE GALADM.NOTIFICATION_TBX IS
   'RMT Notification class definition for asynchronous communication';
COMMENT ON TABLE GALADM.ORDER_TBX IS
   'RMT Orders for LINE to track status of Product Orders for a Plan Code';

COMMENT ON GALADM.ORDER_TBX (
 LOCATION_LEVEL_ID IS 'Location Level ID is PROCESS POINT ID or LINE ID of the station(s) that manage these orders',
 ORDER_STATUS_ID IS 'Link to ORDER_STATUS_TBX',
 HOLD_STATUS_ID IS 'Link to HOLD_STATUS_TBX' );

COMMENT ON ALIAS GALADM.WSBUILDS_TBX IS
   'RMT alias to HNA_WSBUILDS_TBX WebStartBuild for Web Start to define build numbers';
COMMENT ON ALIAS GALADM.WSCLIENTS_TBX IS
   'RMT alias to HNA_WSCLIENTS_TBX WebStartClient for Web Start to define Clients';
COMMENT ON ALIAS GALADM.WSDEFBUILDS_TBX IS
   'RMT alias to HNA_WSDEFBUILDS_TBX WebStartDefaultBuild for Web start to define default builds per environment';
