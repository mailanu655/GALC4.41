delete from MC_REV_TBX;

insert into MC_REV_TBX(REV_ID, REV_DESC, ASSOCIATE_NO, REV_TYPE, REV_STATUS, PDDA_FORM_ID, CREATE_TIMESTAMP, UPDATE_TIMESTAMP) VALUES
(1, 'Initial config for NSX Weld', '27766', 'OPERATION', 'DEVELOPING', NULL, NULL, NULL);

select * from MC_REV_TBX;

delete from MC_OP_REV_TBX;

insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('RF Sub Frame Damper House       ',1,1,'Create unique SN and stamp it as a 2D barcode','MADE_FROM','','com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL3',1,'PLC_WE_01                       ','RF_ASSY                         ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('Abiation Right Front Lower      ',2,1,'Install Abiation Right Front Lower','INSTALLED_PART',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL1',2,'PLC_WE_01                       ','RF_ASSY                         ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('Abiation Right Front Upper      ',3,1,'Install Abiation Right Front Upper','INSTALLED_PART',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL1',3,'PLC_WE_01                       ','RF_ASSY                         ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('RF Assy                         ',4,1,'Install Right Front Assembly','MADE_FROM',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL1',1,'PLC_WE_01                       ','FRONT_COMP                      ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('LF Assy                         ',5,1,'Install Left Front Assembly','MADE_FROM',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL2',2,'PLC_WE_01                       ','FRONT_COMP                      ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('Front Comp                      ',6,1,'Install Front Comp','MADE_FROM',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL4',1,'PLC_WE_01                       ','FRAME_COMP                      ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('RR Frame Damper House           ',7,1,'Install Right Rear Damper House','MADE_FROM',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL3',2,'PLC_WE_01                       ','FRAME_COMP                      ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('LR Frame Damper House           ',8,1,'Install Left Rear Frame Damper House','MADE_FROM',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL3',3,'PLC_WE_01                       ','FRAME_COMP                      ',null);
insert into "GALADM"."MC_OP_REV_TBX"("OPERATION_NAME","OP_REV","REV_ID","OP_DESC","OP_TYPE","OP_VIEW","OP_PROCESSOR","OP_TIME","OP_CHECK","PROCESS_POINT_ID","OP_SEQ_NUM","DEVICE_ID","DEVICE_MSG","PDDA_UNIT_ID") values ('B-Pillar Comp                   ',9,1,'Install B-Pillar Comp','MADE_FROM',null,'com.honda.galc.service.datacollection.DataCollectionServiceImpl',null,1,'PP_CELL3',4,'PLC_WE_01                       ','FRAME_COMP                      ',null);

select * from GALADM.MC_OP_REV_TBX;

delete from MC_OP_PART_REV_TBX;

insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (1,1,'RF Sub Frame Damper House       ','A0001','MADE_FROM',null,null,null,null,'11000NSX*                     ','RF Sub Frame Damper Hous','11000NSX*',null,null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (2,1,'Abiation Right Front Lower      ','A0001','INSTALLED_PART',null,null,null,null,null,'Abiation Right Front Lower',null,'L1',null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (3,1,'Abiation Right Front Upper      ','A0001','INSTALLED_PART',null,null,null,null,null,'Abiation Right Front Upper',null,'U1',null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (4,1,'RF Assy                         ','A0001','MADE_FROM',null,null,null,null,'12000NSX*                     ','RF Assy','12000NSX*',null,null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (5,1,'LF Assy                         ','A0001','MADE_FROM',null,null,null,null,'13000NSX*                     ','LF Assy','13000NSX*',null,null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (6,1,'Front Comp                      ','A0001','MADE_FROM',null,null,null,null,'14000NSX*                     ','Front Comp','14000NSX*',null,null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (7,1,'RR Frame Damper House           ','A0001','MADE_FROM',null,null,null,null,'15000NSX*                     ','RR Frame Damper House','15000NSX*',null,null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (8,1,'LR Frame Damper House           ','A0001','MADE_FROM',null,null,null,null,'16000NSX*                     ','LR Frame Damper House','16000NSX*',null,null,0,null,null);
insert into "GALADM"."MC_OP_PART_REV_TBX"("PART_REV","REV_ID","OPERATION_NAME","PART_ID","PART_TYPE","PART_VIEW","PART_PROCESSOR","DEVICE_ID","DEVICE_MSG","PART_NO","PART_DESC","PART_MASK","PART_MARK","PART_MAX_ATTEMPTS","MEASUREMENT_COUNT","PART_TIME","PART_CHECK") values (9,1,'B-Pillar Comp                   ','A0001','MADE_FROM',null,null,null,null,'17000NSX*                     ','B-Pillar Comp','17000NSX*',null,null,0,null,null);

SELECT * FROM GALADM.MC_OP_PART_REV_TBX;


SELECT * FROM GALADM.MC_OP_MEAS_TBX;

delete from MC_STRUCTURE_TBX;

insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('12000NSX C000                 ',1,'PP_CELL1        ','RF Sub Frame Damper House',1,'A0001',1);
insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('12000NSX C000                 ',1,'PP_CELL1        ','Abiation Right Front Lower',2,'A0001',2);
insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('12000NSX C000                 ',1,'PP_CELL1        ','Abiation Right Front Upper',3,'A0001',3);
insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('14000NSX C000                 ',1,'PP_CELL4        ','RF Assy',4,'A0001',4);
insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('14000NSX C000                 ',1,'PP_CELL4        ','LF Assy',5,'A0001',5);
--insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('15000NSX C000                 ',1,'PP_CELL4        ','Front Comp',6,'A0001',6);
--insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('15000NSX C000                 ',1,'PP_CELL3        ','RR Frame Damper House',7,'A0001',7);
--insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('15000NSX C000                 ',1,'PP_CELL3        ','LR Frame Damper House',8,'A0001',8);
--insert into "GALADM"."MC_STRUCTURE_TBX"("PRODUCT_SPEC_CODE","STRUCTURE_REV","PROCESS_POINT_ID","OPERATION_NAME","OP_REV","PART_ID","PART_REV") values ('15000NSX C000                 ',1,'PP_CELL3        ','B-Pillar Comp',9,'A0001',9);

select * from MC_STRUCTURE_TBX;

delete from MC_ORDER_STRUCTURE_TBX;

INSERT INTO MC_ORDER_STRUCTURE_TBX(ORDER_NO, PRODUCT_SPEC_CODE, STRUCTURE_REV) VALUES('TESTORDER1', '11000NSX C000', 1);
INSERT INTO MC_ORDER_STRUCTURE_TBX(ORDER_NO, PRODUCT_SPEC_CODE, STRUCTURE_REV) VALUES('TESTORDER2', '12000NSX C000', 1);
INSERT INTO MC_ORDER_STRUCTURE_TBX(ORDER_NO, PRODUCT_SPEC_CODE, STRUCTURE_REV) VALUES('TESTORDER3', '13000NSX C000', 1);
INSERT INTO MC_ORDER_STRUCTURE_TBX(ORDER_NO, PRODUCT_SPEC_CODE, STRUCTURE_REV) VALUES('TESTORDER4', '14000NSX C000', 1);
INSERT INTO MC_ORDER_STRUCTURE_TBX(ORDER_NO, PRODUCT_SPEC_CODE, STRUCTURE_REV) VALUES('TESTORDER5', '15000NSX C000', 1);
INSERT INTO MC_ORDER_STRUCTURE_TBX(ORDER_NO, PRODUCT_SPEC_CODE, STRUCTURE_REV) VALUES('TESTORDER6', '16000NSX C000', 1);
INSERT INTO MC_ORDER_STRUCTURE_TBX(ORDER_NO, PRODUCT_SPEC_CODE, STRUCTURE_REV) VALUES('TESTORDER7', '17000NSX C000', 1);

select * from MC_ORDER_STRUCTURE_TBX;

delete from ORDER_TBX;

INSERT INTO ORDER_TBX(ORDER_NO, PLAN_CODE, PRODUCT_SPEC_CODE, PROD_ORDER_QTY) VALUES('TESTORDER1', 'CELL3', '11000NSX C000', 30);
INSERT INTO ORDER_TBX(ORDER_NO, PLAN_CODE, PRODUCT_SPEC_CODE, PROD_ORDER_QTY) VALUES('TESTORDER2', 'CELL1', '12000NSX C000', 30);
INSERT INTO ORDER_TBX(ORDER_NO, PLAN_CODE, PRODUCT_SPEC_CODE, PROD_ORDER_QTY) VALUES('TESTORDER3', 'CELL2', '13000NSX C000', 30);
INSERT INTO ORDER_TBX(ORDER_NO, PLAN_CODE, PRODUCT_SPEC_CODE, PROD_ORDER_QTY) VALUES('TESTORDER4', 'CELL4', '14000NSX C000', 30);
INSERT INTO ORDER_TBX(ORDER_NO, PLAN_CODE, PRODUCT_SPEC_CODE, PROD_ORDER_QTY) VALUES('TESTORDER5', 'CELL3', '15000NSX C000', 30);
INSERT INTO ORDER_TBX(ORDER_NO, PLAN_CODE, PRODUCT_SPEC_CODE, PROD_ORDER_QTY) VALUES('TESTORDER6', 'CELL3', '16000NSX C000', 30);
INSERT INTO ORDER_TBX(ORDER_NO, PLAN_CODE, PRODUCT_SPEC_CODE, PROD_ORDER_QTY) VALUES('TESTORDER7', 'CELL3', '17000NSX C000', 30);

select * from ORDER_TBX;

select * from MBPN_PRODUCT_TBX;
select * from MBPN_PRODUCT_TBX where product_id like 'YW%'; 

insert into MBPN_PRODUCT_TBX (PRODUCT_ID,CURRENT_PRODUCT_SPEC_CODE,CURRENT_ORDER_NO,TRACKING_STATUS,LAST_PASSING_PROCESS_POINT) values ('YW20130313001', '12000NSX C000', 'TESTORDER1', 'CELL3', 'PP_CELL3');

delete from MBPN_PRODUCT_TBX where product_id like 'YW%';


--findMadeFrombyOrderAndProcessPointId
SELECT OrdStr.ORDER_NO,
OrdStr.PRODUCT_SPEC_CODE,
OrdStr.STRUCTURE_REV,
Struct.PROCESS_POINT_ID,
Struct.OPERATION_NAME,
Struct.OP_REV,
Op.OP_TYPE,
Op.OP_SEQ_NUM,
Struct.PART_ID,
Struct.PART_REV,
OpPart.PART_NO,
OpPart.PART_SECTION_CODE,
OpPart.PART_ITEM_NO,
OpPart.PART_MASK,
OpPart.PART_MARK
FROM GALADM.MC_ORDER_STRUCTURE_TBX OrdStr
left JOIN GALADM.MC_STRUCTURE_TBX Struct
ON OrdStr.PRODUCT_SPEC_CODE = Struct.PRODUCT_SPEC_CODE
AND OrdStr.STRUCTURE_REV = Struct.STRUCTURE_REV
left JOIN GALADM.MC_OP_REV_TBX Op
ON Struct.OPERATION_NAME = Op.OPERATION_NAME
AND Struct.OP_REV = Op.OP_REV
LEFT JOIN GALADM.MC_OP_PART_REV_TBX OpPart
ON Op.OPERATION_NAME = OpPart.OPERATION_NAME
AND Struct.PART_ID = OpPart.PART_ID
AND Struct.PART_REV = OpPart.PART_REV
WHERE OrdStr.ORDER_NO = @ORDER_NO
AND Struct.PROCESS_POINT_ID = @PROCESS_POINT_ID
and Op.OP_TYPE='MADE_FROM'
ORDER BY Op.PROCESS_SEQ_NUM, Op.OP_SEQ_NUM;