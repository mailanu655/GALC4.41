alter table galadm.product_shipping_tbx data capture none;
ALTER TABLE GALADM.PRODUCT_SHIPPING_TBX ADD COLUMN PROCESS_POINT_ID CHAR(16) NOT NULL DEFAULT '';
ALTER TABLE GALADM.PRODUCT_SHIPPING_TBX ALTER COLUMN TRAILER_NUMBER SET NOT NULL;
ALTER TABLE GALADM.PRODUCT_SHIPPING_TBX ALTER COLUMN TRAILER_NUMBER SET DEFAULT '';
ALTER TABLE GALADM.PRODUCT_SHIPPING_TBX DROP PRIMARY KEY;
alter table galadm.product_shipping_tbx data capture changes;

reorg table galadm.product_shipping_tbx;

alter table galadm.product_shipping_tbx data capture none;
ALTER TABLE GALADM.PRODUCT_SHIPPING_TBX ADD PRIMARY KEY (PRODUCT_ID, TRAILER_NUMBER,PROCESS_POINT_ID);
alter table galadm.product_shipping_tbx data capture changes;
