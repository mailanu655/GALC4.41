-------------------------------------------------------------------------------------------------------------------------------------------------
-- RGALCDEV-1054 - QSR Support for all products
-- REMARK:
-- If qsr_tbx table is empty then there is no need to update existing qsr data.
-- If qsr_tbx table has data, then we need to pupulate new column(product_type) with data(product type) before we set constraint 'NOT NULL' on product_type column.
-- To update product type data we may use generic script or adjusted template scripts.
-- In template scripts update division ids to match block and head product types with appropriate divisions. 
-- After data update review qsr table and verify that updated product types column correspond to appropriate process locations (division), you may use below script.
-- select qsr.process_location, qsr.product_type, d.division_id, d.division_name, qsr.* from qsr_tbx qsr left outer join gal128tbx d on d.division_id = qsr.process_location;
-------------------------------------------------------------------------------------------------------------------------------------------------

set schema galadm;

DROP TRIGGER GALADM.QSR_TR2;
DROP TRIGGER GALADM.QSR_TR1;


ALTER TABLE GALADM.QSR_TBX DATA CAPTURE NONE; 
ALTER TABLE GALADM.QSR_TBX ADD COLUMN PRODUCT_TYPE VARCHAR(10);
ALTER TABLE GALADM.QSR_TBX DATA CAPTURE CHANGES;  

REORG TABLE  GALADM.QSR_TBX;
REORG INDEXES  ALL FOR TABLE  GALADM.QSR_TBX;

------------------------------------------------------------------------------------------------
------------- UPDATE EXISTING DATA
------------- GENERIC SCRIPT
update qsr_tbx qsr 
set qsr.product_type = 
coalesce(
(select pr.property_value from gal489tbx pr where pr.property_key = 'PRODUCT_TYPE' and pr.COMPONENT_ID in ( select pp.process_point_id from gal214tbx pp where pp.division_id = qsr.process_location ) group by pr.property_value order by count(pr.property_value) desc fetch first 1 rows only)
,(select pr.property_value from gal489tbx pr where pr.COMPONENT_ID = 'System_Info' and pr.property_key = 'PRODUCT_TYPE')
)
where qsr.product_type is null; 
------------- CUSTOM TEMPLATES
--update qsr_tbx set product_type = 'BLOCK' where process_location in ('DIV_ID_DIECAST_BLOCK', 'DIV_ID_MACHINING_BLOCK');
--update qsr_tbx set product_type = 'HEAD' where process_location in ('DIV_ID_DIECAST_HEAD', 'DIV_ID_MACHINING_HEAD');
------------- SAMPLE CUSTOM SCRIPTS FOR HCM
--update qsr_tbx set product_type = 'BLOCK' where process_location in ('HP', 'MB');
--update qsr_tbx set product_type = 'HEAD' where process_location in ('LP', 'MH');

ALTER TABLE GALADM.QSR_TBX DATA CAPTURE NONE; 
ALTER TABLE GALADM.QSR_TBX ALTER COLUMN PRODUCT_TYPE SET NOT NULL;  
ALTER TABLE GALADM.QSR_TBX DATA CAPTURE CHANGES;  

CREATE TRIGGER GALADM.QSR_TR1 NO CASCADE BEFORE INSERT ON GALADM.QSR_TBX REFERENCING NEW AS A FOR EACH ROW SET CREATE_TIMESTAMP = CURRENT_TIMESTAMP;
CREATE TRIGGER GALADM.QSR_TR2 NO CASCADE BEFORE UPDATE ON GALADM.QSR_TBX REFERENCING NEW AS A FOR EACH ROW SET UPDATE_TIMESTAMP = CURRENT_TIMESTAMP;

REORG TABLE  GALADM.QSR_TBX;
REORG INDEXES  ALL FOR TABLE  GALADM.QSR_TBX;