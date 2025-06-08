-- RGALCDEV-550 
-- This script will migrate dunnage config property from
-- COMPONENT    : PROPERTY_KEY     : PROPERTY_VALUE
-------------------------------------------------------------------------------------------------------
-- DEFAULT_QICS : DUNNAGE_STATIONS : PROCESS_POINT_ID[,PROCESS_POINT_ID2[,PROCESS_POINT_ID3...]]   or
-- PROCESS_POINT_ID : DUNNAGE_STATIONS : PROCESS_POINT_ID
-- to
-- PROCESS_POINT_ID : DUNNAGE : true
-------------------------------------------------------------------------------------------------------
set schema galadm;
insert into galadm.gal489tbx (component_id, property_key, property_value, description)
select distinct prop.process_point_id, 'DUNNAGE', 'true', 'Dunnage Station indicator'
from 
(
select r.component_id,  r.property_value, r.process_point_id, locate(trim(r.process_point_id), trim(r.property_value))
from (
select pr.component_id, pr.property_value, pp.process_point_id
from galadm.gal214tbx pp
join galadm.gal489tbx pr on pr.component_id = 'DEFAULT_QICS' and pr.property_key = 'DUNNAGE_STATIONS'
) as r
where locate(trim(r.process_point_id), trim(r.property_value)) > 0
union all
select r.component_id, r.property_value, r.process_point_id , locate(trim(r.process_point_id), trim(r.property_value))
from (
select pr.component_id, pr.property_value, pp.process_point_id
from galadm.gal214tbx pp
join galadm.gal489tbx pr on pr.component_id = pp.process_point_id and pr.property_key = 'DUNNAGE_STATIONS'
) as r
where locate(trim(r.process_point_id), trim(r.property_value)) > 0
) as prop;

-------------------------------------------------------------------------------------------------------
-- check scripts
-------------------------------------------------------------------------------------------------------
-- select * from galadm.gal489tbx where property_key = 'DUNNAGE_STATIONS';
-- select * from galadm.gal489tbx where property_key = 'DUNNAGE';
-------------------------------------------------------------------------------------------------------
-- clean up scripts
-------------------------------------------------------------------------------------------------------
-- REMARK : after 1.7 build is succesfully deployed and succesfully running it is recommended to clean up old 'DUNNAGE_STATIONS' property  with below script
-------------------------------------------------------------------------------------------------------
-- delete from galadm.gal489tbx where property_key = 'DUNNAGE_STATIONS';
