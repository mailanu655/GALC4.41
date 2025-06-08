alter table GALADM.GAL246TBX add column PART_CONFIRM_FLAG INTEGER;

update galadm.GAL246TBX l set l.part_confirm_flag=(select p.part_confirm_check from galadm.gal261tbx p where p.PART_NAME = l.part_name);