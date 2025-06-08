/*
RGALCDEV-4746  - OIF ProductionResult - OIF Task requires SCRAP_LINE_ID properties to be defined for frame and engine.
The following properties are required : 

For Frame :
Component | propertyKey : propertyValues 
Default_FrameLineProperties : SCRAP_LINE_ID : YOUR_SCRAP_LINE_ID

And for Engine :
Component | propertyKey : propertyValues
Default_EngineLineProperties : SCRAP_LINE_ID : YOUR_SCRAP_LINE_ID

If the property(s) does not exist You may use WebConfigurator to create property(s) or execute below script(s) to create property(s).

Please update scrap line id value if different from default 'LINE57'.
*/

--FRAME
set schema galadm;
--select * from gal489tbx where COMPONENT_ID = 'Default_FrameLineProperties' and PROPERTY_KEY = 'SCRAP_LINE_ID';

insert into gal489tbx (COMPONENT_ID, PROPERTY_KEY, PROPERTY_VALUE) 
select  'Default_FrameLineProperties', 'SCRAP_LINE_ID', 'LINE57' from sysibm.SYSDUMMY1 
where not exists (select * from gal489tbx where COMPONENT_ID = 'Default_FrameLineProperties' and PROPERTY_KEY = 'SCRAP_LINE_ID');


--ENGINE 
set schema galadm;
--select * from gal489tbx where COMPONENT_ID = 'Default_EngineLineProperties' and PROPERTY_KEY = 'SCRAP_LINE_ID';

insert into gal489tbx (COMPONENT_ID, PROPERTY_KEY, PROPERTY_VALUE) 
select  'Default_EngineLineProperties', 'SCRAP_LINE_ID', 'LINE57' from sysibm.SYSDUMMY1 
where not exists (select * from gal489tbx where COMPONENT_ID = 'Default_EngineLineProperties' and PROPERTY_KEY = 'SCRAP_LINE_ID');