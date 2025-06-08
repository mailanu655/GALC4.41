-- Please follow below steps to recreate new FK constraint

------------------------------------------------------------------------------------------------------------------------------
-- 1. Drop current FK constraint

set schema galadm;
ALTER TABLE GALADM.LET_CONNECTED_PROGRAM_TBX DROP CONSTRAINT LET_CONNECTED_PROGRAM_FK2;

------------------------------------------------------------------------------------------------------------------------------
-- 2. Verify if there is data in LET_CONNECTED_PROGRAM_TBX ,
--    If there is no data then please go to the last step and create new FK constraint 

    select * from LET_CONNECTED_PROGRAM_TBX; 

------------------------------------------------------------------------------------------------------------------------------
-- 2. Delete connected programs that has triggering programs that have no corresponding inspeciton programs in gal714tbx

    delete 
    from LET_CONNECTED_PROGRAM_TBX 
    where TRIGGERING_PGM_ID in (
       select crp.CRITERIA_PGM_ID
       from LET_CONNECTED_PROGRAM_TBX lcp 
       join gal718tbx crp on crp.CRITERIA_PGM_ID = lcp.TRIGGERING_PGM_ID
       and CRITERIA_PGM_NAME not in (select INSPECTION_PGM_NAME from gal714tbx)
    );

------------------------------------------------------------------------------------------------------------------------------
-- 3. Update LET_CONNECTED_PROGRAM_TBX.triggering_pgm_id - replace gal718tbx.criteria_pgm_id  with gal714.inspection_pgm_id

    update LET_CONNECTED_PROGRAM_TBX lcp set lcp.TRIGGERING_PGM_ID = 
        (select max(ip.INSPECTION_PGM_ID) 
        from LET_CONNECTED_PROGRAM_TBX lcp 
        join gal718tbx crp on crp.CRITERIA_PGM_ID = lcp.TRIGGERING_PGM_ID
        join gal714tbx ip on ip.INSPECTION_PGM_NAME = crp.CRITERIA_PGM_NAME
        group by ip.INSPECTION_PGM_NAME);

------------------------------------------------------------------------------------------------------------------------------
-- 4. Create new FK

ALTER TABLE GALADM.LET_CONNECTED_PROGRAM_TBX ADD FOREIGN KEY LET_CONNECTED_PROGRAM_FK2 (TRIGGERING_PGM_ID) REFERENCES GALADM.GAL714TBX (INSPECTION_PGM_ID) ON DELETE NO ACTION ON UPDATE NO ACTION;