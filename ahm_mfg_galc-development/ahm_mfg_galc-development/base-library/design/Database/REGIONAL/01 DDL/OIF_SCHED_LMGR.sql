
------------------------------------------------
-- DDL Statements for Table "GALADM  "."OIF_SCHED_LMGR"
------------------------------------------------
 
DROP TABLE "GALADM  "."OIF_SCHED_LMGR";


CREATE TABLE "GALADM  "."OIF_SCHED_LMGR"  (
		  "LEASENAME" VARCHAR(254 OCTETS) NOT NULL , 
		  "LEASEOWNER" VARCHAR(254 OCTETS) NOT NULL , 
		  "LEASE_EXPIRE_TIME" BIGINT , 
		  "DISABLED" VARCHAR(5 OCTETS) )   
		 IN "GALTBSREF11" INDEX IN "GALTBSIDX1"  
		 ORGANIZE BY ROW; 

COMMENT ON TABLE "GALADM  "."OIF_SCHED_LMGR" IS 'RMT OIF WAS Redundant Scheduler Leases';











