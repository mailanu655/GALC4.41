/*

 RGALCDEV RGALCDEV-4284 :: ApplicationType/ProcessPointType 

 With release containing implementation for RGALCDEV RGALCDEV-4284 JIRA, QICS ProcessPoints(Applications) to be visible in QICS Maintenance client have to have updated application type in gal241tbx table in the following way :

 1. If ProcessPointType in gal214tbx is : (3, "QICS INSPECTION STATION") or (8, "OFF QICS") or (9, "ON QICS") 
     Then ApplicationType in 241tbx for corresponding application should be updated from PROD(1,"Process Point") to QICS(4,"QICS")
 
 2. If ProcessPointType in gal214tbx is : (4, "QICS REPAIR STATION")
     Then ApplicationType in 241tbx for corresponding application should be updated from PROD(1,"Process Point") to QICS_REPAIR(104, "QICS Repair")

ApplicationType update can be accomplished in two ways :

1. Use GALC WebConfigurator ProcesPoint/Application Form (Application Tab) to update to new Application Types
or
2. Use two below sql scripts to perform batch updates
*/

set schema galadm;

update gal241tbx set application_type = 4 where application_id in (select process_point_id from gal214tbx where process_point_type in (3, 8, 9)) 
and screen_class in ('com.honda.galc.client.qics.view.frame.QicsFrame', 'com.honda.galc.client.qics.view.frame.QicsLotControlFrame');

update gal241tbx set application_type = 104 where application_id in (select process_point_id from gal214tbx where process_point_type in (4))
and screen_class in ('com.honda.galc.client.qics.view.frame.QicsFrame', 'com.honda.galc.client.qics.view.frame.QicsLotControlFrame');

--Rollback
--update gal241tbx set application_type = 1 where application_type in (4, 104)