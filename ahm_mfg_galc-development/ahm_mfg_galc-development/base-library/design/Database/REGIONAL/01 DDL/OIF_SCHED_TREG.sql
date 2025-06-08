
------------------------------------------------
-- DDL Statements for Table "GALADM  "."OIF_SCHED_TREG"
------------------------------------------------
 
DROP TABLE "GALADM  "."OIF_SCHED_TREG";


CREATE TABLE "GALADM  "."OIF_SCHED_TREG"  (
		  "REGKEY" VARCHAR(254 OCTETS) NOT NULL , 
		  "REGVALUE" VARCHAR(254 OCTETS) )   
		 IN "GALTBSREF11" INDEX IN "GALTBSIDX1"  
		 ORGANIZE BY ROW; 

COMMENT ON TABLE "GALADM  "."OIF_SCHED_TREG" IS 'RMT OIF WAS Scheduler Info';











