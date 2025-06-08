-- RGALCDEV-759
set schema galadm;
update gal489tbx set property_value = replace(property_value, 'BEARING_MATRIX_MAINTENANCE', 'BEARING_MAIN_MATRIX_MAINTENANCE,BEARING_CONROD_MATRIX_MAINTENANCE') where property_value like '%BEARING_MATRIX_MAINTENANCE%';