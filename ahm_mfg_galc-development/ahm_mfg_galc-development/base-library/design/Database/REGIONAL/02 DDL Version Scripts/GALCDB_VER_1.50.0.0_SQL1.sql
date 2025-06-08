/***************************************************************
Data migration script for JIRA RGALCDEV-3808/RGALCPROD-1934
Script will create new properties from old properties.
SHIPPING_PROCESS_POINT_IDS -> PRODUCT_SHIPPED_PROCESS_POINT_IDS
DO_NOT_PROCESS_IF_HISTORY_EXISTS -> PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS
-> PRODUCT_NOT_PROCESSABLE_CHECK_TYPES
****************************************************************/
/***************************************************************
REMARK !!! - if this query does not return any values, you do not need to run this script as there is no data to be migrated
--select old config
select * from gal489tbx where property_key in ( 'SHIPPING_PROCESS_POINT_IDS', 'DO_NOT_PROCESS_IF_HISTORY_EXISTS');

REMARK !!!
--if you want to rerun this script please use below delete statements to delete properties that were created in the previous run
--delete from gal489tbx where property_key in ('PRODUCT_SHIPPED_PROCESS_POINT_IDS', 'PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS', 'PRODUCT_NOT_PROCESSABLE_CHECK_TYPES'); 
--delete from gal489tbx where COMPONENT_ID = 'DEFAULT_PRODUCT_CHECK'; 
****************************************************************/

set schema galadm;
--****************************************************************************************************************************
--select old config (to verify before migration)
select * from gal489tbx where property_key in ( 'SHIPPING_PROCESS_POINT_IDS', 'DO_NOT_PROCESS_IF_HISTORY_EXISTS');
--****************************************************************************************************************************
--select new config ( to verify after migration)
select * from gal489tbx where property_key in ('PRODUCT_SHIPPED_PROCESS_POINT_IDS', 'PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS', 'PRODUCT_NOT_PROCESSABLE_CHECK_TYPES'); 
select * from gal489tbx where component_id = 'DEFAULT_PRODUCT_CHECK'; 


-- migrate properties 
--***********************************************************************************************************
--SHIPPING_PROCESS_POINT_IDS -> PRODUCT_SHIPPED_PROCESS_POINT_IDS
--DO_NOT_PROCESS_IF_HISTORY_EXISTS -> PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS
insert into gal489tbx (component_id, property_key, property_value) 
select case when component_id = 'DEFAULT_QICS' then 'DEFAULT_PRODUCT_CHECK' else component_id end,
case when property_key = 'SHIPPING_PROCESS_POINT_IDS' then 'PRODUCT_SHIPPED_PROCESS_POINT_IDS'  when property_key = 'DO_NOT_PROCESS_IF_HISTORY_EXISTS' then 'PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS' end, 
property_value 
from gal489tbx where property_key in ( 'SHIPPING_PROCESS_POINT_IDS', 'DO_NOT_PROCESS_IF_HISTORY_EXISTS')
;

--***********************************************************************************************************
--create PRODUCT_NOT_PROCESSABLE_CHECK_TYPES properties
insert into gal489tbx (component_id, property_key, property_value) 
select 
coalesce(p.component_id, s.component_id) as component_id, 'PRODUCT_NOT_PROCESSABLE_CHECK_TYPES' as property_key,
case 
when p.property_value is not null and s.property_value is not null then 'PRODUCT_RESULT_EXIST_CHECK,PRODUCT_SHIPPED_CHECK' 
when p.property_value is not null then 'PRODUCT_RESULT_EXIST_CHECK' 
when s.property_value is not null then 'PRODUCT_SHIPPED_CHECK' 
end as property_value
from gal489tbx s 
full join gal489tbx p on p.component_id = s.component_id and  p.PROPERTY_KEY = 'PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS' and s.property_key = 'PRODUCT_SHIPPED_PROCESS_POINT_IDS'
where p.property_key = 'PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS' or s.property_key = 'PRODUCT_SHIPPED_PROCESS_POINT_IDS'
;

--deprecate old properties
update gal489tbx set description = 'DEPREC in 1.50, rpl by PRODUCT_SHIPPED_PROCESS_POINT_IDS'  where property_key = 'SHIPPING_PROCESS_POINT_IDS';
update gal489tbx set description = 'DEPREC in 1.50, rpl by PRODUCT_RESULT_EXIST_PROCESS_POINT_IDS'  where property_key = 'DO_NOT_PROCESS_IF_HISTORY_EXISTS';
