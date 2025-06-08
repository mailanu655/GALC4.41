 
update GALADM.GAL489TBX set property_key = 'ADMIN_PASSWORD' 
 where COMPONENT_ID = 'prop_LDAP' 
 and property_key = 'ADMINPASSWORD';
 
update GALADM.GAL489TBX set property_key = 'ADMIN_ID' 
 where COMPONENT_ID = 'prop_LDAP' 
 and property_key = 'ADMINID';

update GALADM.GAL489TBX set property_key = 'LDAP_URL' 
 where COMPONENT_ID = 'prop_LDAP' 
 and property_key = 'LDAPURL';