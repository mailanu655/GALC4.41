--To correct existing PRODUCT_SPEC_CODE with non space at 18th position in MBPN_TBX

--get duplicated MBPN and HES_COLOR but different PRODUCT_SPEC_CODE, one has space at 18th position and the other does not
select * from mbpn_tbx where (mbpn, hes_color) in (select MBPN, HES_COLOR from mbpn_tbx group by MBPN, HES_COLOR having count(*) > 1);

--delete PRODUCT_SPEC_CODE with 18th char is not space
delete from mbpn_tbx where (mbpn, hes_color) in (select MBPN, HES_COLOR from mbpn_tbx group by MBPN, HES_COLOR having count(*) > 1) and substr(product_spec_code, 18, 1) != ' ';

--add additional space to 18th position in PRODUCT_SPEC_CODE
update mbpn_tbx set product_spec_code = substr(product_spec_code, 1, 17) concat ' ' concat substr(product_spec_code, 18, 11) where substr(product_spec_code, 18, 1) != ' ';
