------------------------------------------------
-- DML Statements for Table "QI_STATION_CONFIG_TBX". This SQL script should be run for every plant. 
------------------------------------------------

-- Below Statement will update Property value to default value.

UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='Yes' where PROPERTY_KEY='Highlight';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='Yes' where PROPERTY_KEY='Defects';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='8' where PROPERTY_KEY='Range';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='Yes' where PROPERTY_KEY='Repair';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='Yes' where PROPERTY_KEY='Repair Comment';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='' where PROPERTY_KEY='Default Qty';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='10' where PROPERTY_KEY='Most frequently used list size';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='0' where PROPERTY_KEY='In Repair Entry Prompt for"Is this caused during Repair"';
UPDATE "GALADM"."QI_STATION_CONFIG_TBX" SET PROPERTY_VALUE='Yes' where PROPERTY_KEY='Multi-Select Repairs';
