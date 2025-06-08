
------------------------------------------------
-- DDL Statements for Table "GALADM"."TERMINAL_TYPE_TBX"
------------------------------------------------
 
DROP TABLE "GALADM"."TERMINAL_TYPE_TBX";


CREATE TABLE GALADM.TERMINAL_TYPE_TBX (
  TERMINAL_FLAG SMALLINT NOT NULL DEFAULT,
  NAME	VARCHAR(255),
  CREATE_TIMESTAMP	TIMESTAMP,
  UPDATE_TIMESTAMP	TIMESTAMP
  ) 
  IN GALTBSREF11
  INDEX IN GALTBSIDX4
  ORGANIZE BY ROW;

ALTER TABLE GALADM.TERMINAL_TYPE_TBX
  DATA CAPTURE CHANGES INCLUDE LONGVAR COLUMNS
  PCTFREE 0
  LOCKSIZE ROW
  APPEND OFF
  NOT VOLATILE;

COMMENT ON TABLE GALADM.TERMINAL_TYPE_TBX IS 'RMT Terminal Type';

SET SCHEMA = QGALINST;

-- Step 2. Restoring constraints and indexes
ALTER TABLE GALADM.TERMINAL_TYPE_TBX
  ADD PRIMARY KEY
    (TERMINAL_FLAG)
    ENFORCED;

COMMIT;

-- Step 3. Runstats
RUNSTATS ON TABLE GALADM.TERMINAL_TYPE_TBX
	ALLOW WRITE ACCESS;

-------------------------------
-- DDL Statements for Triggers
-------------------------------

SET CURRENT SCHEMA = "GALINST ";
SET CURRENT PATH = "SYSIBM","SYSFUN","SYSPROC","SYSIBMADM","GALINST";

DROP TRIGGER "GALADM"."TERMINAL_TYPE_TR1";

CREATE TRIGGER GALADM.TERMINAL_TYPE_TR1 NO CASCADE BEFORE INSERT ON GALADM.TERMINAL_TYPE_TBX
REFERENCING NEW AS A FOR EACH ROW SET CREATE_TIMESTAMP = CURRENT_TIMESTAMP;


SET CURRENT SCHEMA = "GALINST ";
SET CURRENT PATH = "SYSIBM","SYSFUN","SYSPROC","SYSIBMADM","GALINST";

DROP TRIGGER "GALADM"."TERMINAL_TYPE_TR2";

CREATE TRIGGER GALADM.TERMINAL_TYPE_TR2 NO CASCADE BEFORE UPDATE ON GALADM.TERMINAL_TYPE_TBX
REFERENCING NEW AS A FOR EACH ROW SET UPDATE_TIMESTAMP = CURRENT_TIMESTAMP;

-- TERMINAL_TYPE_TBX
INSERT INTO "GALADM"."TERMINAL_TYPE_TBX" ("TERMINAL_FLAG", "NAME") VALUES (0, 'LEGACY');
INSERT INTO "GALADM"."TERMINAL_TYPE_TBX" ("TERMINAL_FLAG", "NAME") VALUES (1, 'GALC_SWING');
INSERT INTO "GALADM"."TERMINAL_TYPE_TBX" ("TERMINAL_FLAG", "NAME") VALUES (2, 'GALC_FX');
INSERT INTO "GALADM"."TERMINAL_TYPE_TBX" ("TERMINAL_FLAG", "NAME") VALUES (3, 'GTS');
INSERT INTO "GALADM"."TERMINAL_TYPE_TBX" ("TERMINAL_FLAG", "NAME") VALUES (4, 'NALC_SWING');
INSERT INTO "GALADM"."TERMINAL_TYPE_TBX" ("TERMINAL_FLAG", "NAME") VALUES (5, 'NALC_FX');
INSERT INTO "GALADM"."TERMINAL_TYPE_TBX" ("TERMINAL_FLAG", "NAME") VALUES (6, 'NAQ');

--LEGACY
insert into galadm.gal489tbx
select component_id, 'LEGACY_' || PROPERTY_KEY, property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'DISPATCHER_URL';

insert into galadm.gal489tbx
select component_id, 'LEGACY_' || PROPERTY_KEY, property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'MAIN_CLASS';

insert into galadm.gal489tbx
select component_id, 'LEGACY_' || PROPERTY_KEY, property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key like 'JARS%';

--GALC_SWING
insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'GALC_SWING'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'NEW_DISPATCHER_URL';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'GALC_SWING'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'NEW_MAIN_CLASS';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'GALC_SWING'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key like 'NEW_JARS%';

--GALC FX
insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'GALC_FX'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'NEW_DISPATCHER_URL';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'V2', 'GALC_FX'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'V2_MAIN_CLASS';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'V2', 'GALC_FX'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key like 'V2_JARS%';

--NALC_SWING
insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'NALC_SWING'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'NEW_DISPATCHER_URL';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'NALC_SWING'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'NEW_MAIN_CLASS';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'NALC_SWING'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key like 'NEW_JARS%';

--NALC FX
insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'NALC_FX'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'NEW_DISPATCHER_URL';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'V2', 'NALC_FX'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'V2_MAIN_CLASS';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'V2', 'NALC_FX'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key like 'V2_JARS%';

--NAQ
insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'NEW', 'NAQ'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'NEW_DISPATCHER_URL';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'V2', 'NAQ'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key = 'V2_MAIN_CLASS';

insert into galadm.gal489tbx
select component_id, replace(PROPERTY_KEY, 'V2', 'NAQ'), property_value, DESCRIPTION, current_user, null, null
from galadm.gal489tbx where component_id = 'ClientLoaderServlet' and property_key like 'V2_JARS%';