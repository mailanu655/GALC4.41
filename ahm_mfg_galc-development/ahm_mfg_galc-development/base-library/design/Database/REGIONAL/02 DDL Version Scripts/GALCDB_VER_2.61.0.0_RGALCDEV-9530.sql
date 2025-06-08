DROP INDEX GALADM.BOM_PK;

CREATE UNIQUE INDEX "GALADM"."BOM_PK" ON "GALADM  "."BOM_TBX" 
                                ("PART_NO" ASC,
                                "PLANT_LOC_CODE" ASC,
                                "PART_COLOR_CODE" ASC,
                                "SUPPLIER_NO" ASC,
                                "TGT_SHIP_TO_CODE" ASC,
                                "MTC_MODEL" ASC,
                                "MTC_COLOR" ASC,
                                "MTC_OPTION" ASC,
                                "MTC_TYPE" ASC,
                                "INT_COLOR_CODE" ASC,
                                "EFF_BEG_DATE" ASC,
                                "PART_BLOCK_CODE" ASC,
                                "PART_SECTION_CODE" ASC,
                                "PART_ITEM_NO" ASC,
                                "DC_PART_NO" ASC 
                                )
                                COMPRESS NO 
                                INCLUDE NULL KEYS ALLOW REVERSE SCANS;

