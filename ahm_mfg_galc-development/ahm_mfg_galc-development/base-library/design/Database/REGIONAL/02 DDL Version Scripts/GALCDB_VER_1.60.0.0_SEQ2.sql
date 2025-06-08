-- RGALCDEV-4459 - increase column length to accomodate longer attribute values for sql like attributes

set schema galadm;
alter table gal258tbx alter column attribute_value set data type varchar(3850);
 

