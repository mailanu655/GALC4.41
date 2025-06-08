--Updating the column "SECONDARY_PART_NAME" length of gal125tbx from 20 to 32 to match with regional mapped column "INSPECTION_PART2_NAME"

ALTER TABLE GALADM.GAL125TBX
	ALTER COLUMN SECONDARY_PART_NAME SET DATA TYPE varchar(32);

--Updating the column "SECONDARY_PART_NAME" length of GAL125_HIST_TBX from 20 to 32 to match with regional mapped column "INSPECTION_PART2_NAME"

ALTER TABLE GALADM.GAL125_HIST_TBX
	ALTER COLUMN SECONDARY_PART_NAME SET DATA TYPE varchar(32);	