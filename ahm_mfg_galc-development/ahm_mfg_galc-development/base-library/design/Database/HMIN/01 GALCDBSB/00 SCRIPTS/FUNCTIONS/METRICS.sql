--Drop all database objects that are used to calculate metrics in the correct order
DROP TRIGGER GALADM.GAL215TR7;
DROP TRIGGER GALADM.GAL215TR6;
DROP TRIGGER GALADM.GAL215TR5;
DROP TRIGGER GALADM.GAL215TR4;
DROP TRIGGER GALADM.GAL215TR3;
DROP PROCEDURE GALADM.HMIN_PP_METRICS_U_PROC;
DROP PROCEDURE GALADM.HMIN_PP_METRICS_I_PROC;


DROP FUNCTION GALADM.HMIN_GET_PRODUCTION_DATE_FUNC;
DROP FUNCTION GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC;
DROP FUNCTION GALADM.HMIN_GET_SCHEDULE_ID_FUNC;


CREATE FUNCTION GALADM.HMIN_GET_SCHEDULE_ID_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   ACTUAL_TIMESTAMP_IN    TIMESTAMP)
   RETURNS BIGINT
   DETERMINISTIC
   NO EXTERNAL ACTION
   ------------------------------------------------------------------------
   -- SQL Function GALADM.HMIN_GET_SCHEDULE_ID_FUNC
   -- PROCESS_POINT_ID_IN
   -- ACTUAL_TIMESTAMP_IN

   -- This function is to return  the schedule id
   -- given the process point id and the actual timestamp

   -- Sriram Shanmugavel
   -- 20090610
   -- Initial Creation

   ------------------------------------------------------------------------
   BEGIN ATOMIC
      RETURN SELECT MIN (S.SCHEDULE_ID)
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.HMIN_GPCS_GALC_MAP_TBX G
                       ON     S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL128TBX D
                       ON G.LOCATION_LEVEL_ID = D.DIVISION_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON D.DIVISION_ID = P.DIVISION_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.START_TIMESTAMP <= ACTUAL_TIMESTAMP_IN
                    AND ACTUAL_TIMESTAMP_IN <= S.END_TIMESTAMP
             HAVING MIN (S.SCHEDULE_ID) IS NOT NULL
             UNION
             SELECT MIN (S.SCHEDULE_ID)
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.HMIN_GPCS_GALC_MAP_TBX G
                       ON     S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL195TBX L
                       ON G.LOCATION_LEVEL_ID = L.LINE_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON L.LINE_ID = P.LINE_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.START_TIMESTAMP <= ACTUAL_TIMESTAMP_IN
                    AND ACTUAL_TIMESTAMP_IN <= S.END_TIMESTAMP
             HAVING MIN (S.SCHEDULE_ID) IS NOT NULL
             FETCH FIRST 1 ROW ONLY;
   END;

COMMENT ON FUNCTION GALADM.HMIN_GET_SCHEDULE_ID_FUNC IS
   'Given a Process Point ID and actual timestamp return the schedule id from the schedule table';

CREATE FUNCTION GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   ACTUAL_TIMESTAMP_IN    TIMESTAMP)
   RETURNS VARCHAR (2)
   DETERMINISTIC
   NO EXTERNAL ACTION
   ------------------------------------------------------------------------
   -- SQL Function GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC
   -- PROCESS_POINT_ID_IN
   -- ACTUAL_TIMESTAMP_IN

   -- This function is to return  the produdction shift
   -- given the process point id and the actual timestamp

   -- Sriram Shanmugavel
   -- 20090610
   -- Initial Creation

   ------------------------------------------------------------------------

   BEGIN ATOMIC
      RETURN SELECT MIN (S.SHIFT)
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.HMIN_GPCS_GALC_MAP_TBX G
                       ON     S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL128TBX D
                       ON G.LOCATION_LEVEL_ID = D.DIVISION_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON D.DIVISION_ID = P.DIVISION_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.START_TIMESTAMP <= ACTUAL_TIMESTAMP_IN
                    AND ACTUAL_TIMESTAMP_IN <= S.END_TIMESTAMP
             HAVING MIN (S.SCHEDULE_ID) IS NOT NULL
             UNION
             SELECT MIN (S.SHIFT)
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.HMIN_GPCS_GALC_MAP_TBX G
                       ON     S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL195TBX L
                       ON G.LOCATION_LEVEL_ID = L.LINE_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON L.LINE_ID = P.LINE_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.START_TIMESTAMP <= ACTUAL_TIMESTAMP_IN
                    AND ACTUAL_TIMESTAMP_IN <= S.END_TIMESTAMP
             HAVING MIN (S.SCHEDULE_ID) IS NOT NULL
             FETCH FIRST 1 ROW ONLY;
   END;

COMMENT ON FUNCTION GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC IS
   'Given a Process Point ID and actual timestamp return the production shift from the schedule table';

CREATE FUNCTION GALADM.HMIN_GET_PRODUCTION_DATE_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   ACTUAL_TIMESTAMP_IN    TIMESTAMP)
   RETURNS DATE
   DETERMINISTIC
   NO EXTERNAL ACTION
   ------------------------------------------------------------------------
   -- SQL Function GALADM.HMIN_GET_PRODUCTION_DATE_FUNC
   -- PROCESS_POINT_ID_IN
   -- ACTUAL_TIMESTAMP_IN

   -- This function is to return  the produdction date
   -- given the process point id and the actual timestamp

   -- Sriram Shanmugavel
   -- 20090610
   -- Initial Creation

   ------------------------------------------------------------------------

   BEGIN ATOMIC
      RETURN SELECT MIN (S.PRODUCTION_DATE)
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.HMIN_GPCS_GALC_MAP_TBX G
                       ON     S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL128TBX D
                       ON G.LOCATION_LEVEL_ID = D.DIVISION_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON D.DIVISION_ID = P.DIVISION_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.START_TIMESTAMP <= ACTUAL_TIMESTAMP_IN
                    AND ACTUAL_TIMESTAMP_IN <= S.END_TIMESTAMP
             HAVING MIN (S.SCHEDULE_ID) IS NOT NULL
             UNION
             SELECT MIN (S.PRODUCTION_DATE)
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.HMIN_GPCS_GALC_MAP_TBX G
                       ON     S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL195TBX L
                       ON G.LOCATION_LEVEL_ID = L.LINE_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON L.LINE_ID = P.LINE_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.START_TIMESTAMP <= ACTUAL_TIMESTAMP_IN
                    AND ACTUAL_TIMESTAMP_IN <= S.END_TIMESTAMP
             HAVING MIN (S.SCHEDULE_ID) IS NOT NULL
             FETCH FIRST 1 ROW ONLY;
   END;

COMMENT ON FUNCTION GALADM.HMIN_GET_PRODUCTION_DATE_FUNC IS
   'Given a Process Point ID and actual timestamp return the production date from the schedule table';

CREATE PROCEDURE GALADM.HMIN_PP_METRICS_I_PROC (
   IN  PROCESS_POINT_ID_IN   VARCHAR (16),
   IN  ACTUAL_TIMESTAMP_IN   TIMESTAMP,
   OUT PP_METRICS_ID_OUT     BIGINT)
   SPECIFIC HMIN_PP_METRICS_I_PROC
   LANGUAGE SQL
   NOT DETERMINISTIC
   EXTERNAL ACTION
   MODIFIES SQL DATA
   CALLED ON NULL INPUT
   INHERIT SPECIAL REGISTERS
   ------------------------------------------------------------------------
   -- SQL Stored Procedure HMIN_PP_METRICS_I_PROC
   -- PROCESS_POINT_ID_IN
   -- ACTUAL_TIMESTAMP_IN
   -- PP_METRICS_ID_OUT

   -- This function is to make sure that a record exists in the HMIN_PP_METRICS_TBX
   -- For the given Process Point and timestamp and returns the ID of that record

   -- Alan Study
   -- 20080412
   -- Initial Creation
   -- 20120123 Add WITH RR USE AND KEEP UPDATE LOCKS to fix deadlock when tracking same PP with multiple history records at same time
   ------------------------------------------------------------------------
   BEGIN
      DECLARE PRODUCTION_DATE_V   DATE;
      DECLARE SHIFT_V             VARCHAR (2);
      DECLARE SHIFT_START_V       TIMESTAMP;
      DECLARE SHIFT_END_V         TIMESTAMP;

      SET PRODUCTION_DATE_V =
             GALADM.HMIN_GET_PRODUCTION_DATE_FUNC (PROCESS_POINT_ID_IN,
                                                   ACTUAL_TIMESTAMP_IN);
      SET SHIFT_V =
             GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC (PROCESS_POINT_ID_IN,
                                                    ACTUAL_TIMESTAMP_IN);

      SELECT T.PP_METRICS_ID
        INTO PP_METRICS_ID_OUT
        FROM GALADM.HMIN_PP_METRICS_TBX T
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND T.PRODUCTION_DATE = PRODUCTION_DATE_V
             AND T.SHIFT = SHIFT_V
        WITH RR USE AND KEEP UPDATE LOCKS;

      IF PP_METRICS_ID_OUT IS NULL
      THEN
         SET PP_METRICS_ID_OUT = GALADM.HMIN_ID_FUNC ();
         SET SHIFT_START_V =
                GALADM.HMIN_GET_SHIFT_START_FUNC (PROCESS_POINT_ID_IN,
                                                  PRODUCTION_DATE_V,
                                                  SHIFT_V);                 --
         SET SHIFT_END_V =
                GALADM.HMIN_GET_SHIFT_END_FUNC (PROCESS_POINT_ID_IN,
                                                PRODUCTION_DATE_V,
                                                SHIFT_V);                   --

         IF (   PRODUCTION_DATE_V IS NULL
             OR SHIFT_V IS NULL
             OR SHIFT_START_V IS NULL
             OR SHIFT_END_V IS NULL)
         THEN
            --Exit if the data is not setup correctly
            RETURN;
         END IF;

         INSERT INTO GALADM.HMIN_PP_METRICS_TBX (PP_METRICS_ID,
                                                 PROCESS_POINT_ID,
                                                 SHIFT,
                                                 PRODUCTION_DATE,
                                                 SHIFT_START,
                                                 SHIFT_END)
         VALUES (PP_METRICS_ID_OUT,
                 PROCESS_POINT_ID_IN,
                 SHIFT_V,
                 PRODUCTION_DATE_V,
                 SHIFT_START_V,
                 SHIFT_END_V);
      END IF;
   END;

COMMENT ON SPECIFIC PROCEDURE GALADM.HMIN_PP_METRICS_I_PROC IS
   'Makes sure a record exists in the HMIN_PP_METRICS_TBX for the given Process Point and timestamp and returns the ID of that record';

CREATE PROCEDURE GALADM.HMIN_PP_METRICS_U_PROC (
   IN PROCESS_POINT_ID_IN   VARCHAR (16),
   IN ACTUAL_TIMESTAMP_IN   TIMESTAMP)
   SPECIFIC HMIN_PP_METRICS_U_PROC
   LANGUAGE SQL
   NOT DETERMINISTIC
   EXTERNAL ACTION
   MODIFIES SQL DATA
   CALLED ON NULL INPUT
   INHERIT SPECIAL REGISTERS
  ------------------------------------------------------------------------
  -- SQL Stored Procedure HMIN_PP_METRICS_U_PROC
  -- PROCESS_POINT_ID_IN
  -- ACTUAL_TIMESTAMP_IN

  -- This function is to calculate the metrics for the given process point
  -- over the production date and shift that the actual timestamp falls within
  -- The calculations will be stored/updated in the summary tables
  -- HMIN_PP_METRICS_TBX
  -- Overall Metrics summarized by process point, production date, shift

  -- Alan Study
  -- 20080412
  -- Initial Creation
  -- Remove updates to HMIN_PP_METRICS_DETAILS
  ------------------------------------------------------------------------
  P1:
   BEGIN
      DECLARE PP_METRICS_ID_V               BIGINT;
      DECLARE PRODUCTION_DATE_V             DATE;
      DECLARE SHIFT_V                       VARCHAR (2);
      DECLARE SHIFT_START_V                 TIMESTAMP;
      DECLARE SHIFT_END_V                   TIMESTAMP;
      DECLARE INSPECTED_V                   DOUBLE;
      DECLARE OUTSTANDING_PRODUCT_ENTRY_V   DOUBLE;
      DECLARE OUTSTANDING_PRODUCT_V         DOUBLE;
      DECLARE DEFECTS_PRODUCT_V             DOUBLE;
      DECLARE DEFECTS_PRODUCT_ENTRY_V       DOUBLE;
      DECLARE DEFECTS_ENTRY_V               DOUBLE;
      DECLARE DEFECTS_V                     DOUBLE;
      DECLARE REPAIRS_PRODUCT_ENTRY_V       DOUBLE;
      DECLARE FIRST_PRODUCT_V               VARCHAR (17);
      DECLARE LAST_PRODUCT_V                VARCHAR (17);

      SET PRODUCTION_DATE_V =
             GALADM.HMIN_GET_PRODUCTION_DATE_FUNC (PROCESS_POINT_ID_IN,
                                                   ACTUAL_TIMESTAMP_IN);
      SET SHIFT_V =
             GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC (PROCESS_POINT_ID_IN,
                                                    ACTUAL_TIMESTAMP_IN);
      SET SHIFT_START_V =
             GALADM.HMIN_GET_SHIFT_START_FUNC (PROCESS_POINT_ID_IN,
                                               PRODUCTION_DATE_V,
                                               SHIFT_V);
      SET SHIFT_END_V =
             GALADM.HMIN_GET_SHIFT_END_FUNC (PROCESS_POINT_ID_IN,
                                             PRODUCTION_DATE_V,
                                             SHIFT_V);

      IF (   PRODUCTION_DATE_V IS NULL
          OR SHIFT_V IS NULL
          OR SHIFT_START_V IS NULL
          OR SHIFT_END_V IS NULL)
      THEN
         --Exit if the data is not setup correctly
         RETURN;
      END IF;

      CALL GALADM.HMIN_PP_METRICS_I_PROC (PROCESS_POINT_ID_IN,
                                          ACTUAL_TIMESTAMP_IN,
                                          PP_METRICS_ID_V);

      IF (PP_METRICS_ID_V IS NULL)
      THEN
         RETURN;
      END IF;


      -- Calculate the metrics for the summary table
      -- Added WITH UR so that concurent
      SELECT T.PRODUCT_ID FIRST_PRODUCT_ID
        INTO FIRST_PRODUCT_V
        FROM GALADM.GAL215TBX T
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      ORDER BY T.ACTUAL_TIMESTAMP
      FETCH FIRST 1 ROW ONLY
        WITH UR;

      SELECT T.PRODUCT_ID LAST_PRODUCT_ID
        INTO LAST_PRODUCT_V
        FROM GALADM.GAL215TBX T
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      ORDER BY T.ACTUAL_TIMESTAMP DESC
      FETCH FIRST 1 ROW ONLY
        WITH UR;

      SELECT COUNT (DISTINCT T.PRODUCT_ID) INSPECTED
        INTO INSPECTED_V
        FROM GALADM.GAL215TBX T
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      SELECT COUNT (DISTINCT T.PRODUCT_ID) OUTSTANDING_PRODUCT_ENTRY
        INTO OUTSTANDING_PRODUCT_ENTRY_V
        FROM    GALADM.GAL215TBX T
             INNER JOIN
                GALADM.GAL125TBX D
             ON     T.PRODUCT_ID = D.PRODUCT_ID
                AND D.DEFECT_STATUS <> 2
                AND T.PROCESS_POINT_ID = D.APPLICATION_ID
                AND (   T.ACTUAL_TIMESTAMP < D.REPAIR_TIMESTAMP
                     OR D.REPAIR_TIMESTAMP IS NULL)
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      SELECT COUNT (DISTINCT T.PRODUCT_ID) OUTSTANDING_PRODUCT
        INTO OUTSTANDING_PRODUCT_V
        FROM    GALADM.GAL215TBX T
             INNER JOIN
                GALADM.GAL125TBX D
             ON     T.PRODUCT_ID = D.PRODUCT_ID
                AND D.DEFECT_STATUS <> 2
                AND (   T.PROCESS_POINT_ID = D.APPLICATION_ID
                     OR T.ACTUAL_TIMESTAMP >= D.ACTUAL_TIMESTAMP)
                AND (   T.ACTUAL_TIMESTAMP < D.REPAIR_TIMESTAMP
                     OR D.REPAIR_TIMESTAMP IS NULL)
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      SELECT COUNT (DISTINCT T.PRODUCT_ID) DEFECTS_PRODUCT
        INTO DEFECTS_PRODUCT_V
        FROM    GALADM.GAL215TBX T
             INNER JOIN
                GALADM.GAL125TBX D
             ON     T.PRODUCT_ID = D.PRODUCT_ID
                AND D.DEFECT_STATUS <> 2
                AND (   T.PROCESS_POINT_ID = D.APPLICATION_ID
                     OR T.ACTUAL_TIMESTAMP >= D.ACTUAL_TIMESTAMP)
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      SELECT COUNT (DISTINCT T.PRODUCT_ID) DEFECTS_PRODUCT_ENTRY
        INTO DEFECTS_PRODUCT_ENTRY_V
        FROM    GALADM.GAL215TBX T
             INNER JOIN
                GALADM.GAL125TBX D
             ON     T.PRODUCT_ID = D.PRODUCT_ID
                AND D.DEFECT_STATUS <> 2
                AND T.PROCESS_POINT_ID = D.APPLICATION_ID
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      SELECT COUNT (
                DISTINCT    D.INSPECTION_PART_NAME
                         || D.INSPECTION_PART_LOCATION_NAME
                         || D.DEFECT_TYPE_NAME
                         || D.SECONDARY_PART_NAME
                         || CHAR (D.DEFECTRESULTID)
                         || D.APPLICATION_ID
                         || D.PRODUCT_ID
                         || D.TWO_PART_PAIR_PART
                         || D.TWO_PART_PAIR_LOCATION)
        INTO DEFECTS_ENTRY_V
        FROM    GALADM.GAL215TBX T
             INNER JOIN
                GALADM.GAL125TBX D
             ON     T.PRODUCT_ID = D.PRODUCT_ID
                AND D.DEFECT_STATUS <> 2
                AND T.PROCESS_POINT_ID = D.APPLICATION_ID
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      SELECT COUNT (
                DISTINCT    D.INSPECTION_PART_NAME
                         || D.INSPECTION_PART_LOCATION_NAME
                         || D.DEFECT_TYPE_NAME
                         || D.SECONDARY_PART_NAME
                         || CHAR (D.DEFECTRESULTID)
                         || D.APPLICATION_ID
                         || D.PRODUCT_ID
                         || D.TWO_PART_PAIR_PART
                         || D.TWO_PART_PAIR_LOCATION)
                DEFECTS
        INTO DEFECTS_V
        FROM    GALADM.GAL215TBX T
             INNER JOIN
                GALADM.GAL125TBX D
             ON     T.PRODUCT_ID = D.PRODUCT_ID
                AND D.DEFECT_STATUS <> 2
                AND (   T.PROCESS_POINT_ID = D.APPLICATION_ID
                     OR T.ACTUAL_TIMESTAMP >= D.ACTUAL_TIMESTAMP)
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      SELECT COUNT (DISTINCT T.PRODUCT_ID) REPAIRS_PRODUCT_ENTRY
        INTO REPAIRS_PRODUCT_ENTRY_V
        FROM    GALADM.GAL215TBX T
             INNER JOIN
                GALADM.GAL125TBX D
             ON     T.PRODUCT_ID = D.PRODUCT_ID
                AND D.DEFECT_STATUS <> 2
                AND T.PROCESS_POINT_ID = D.APPLICATION_ID
                AND T.ACTUAL_TIMESTAMP >= D.REPAIR_TIMESTAMP
       WHERE     T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             AND SHIFT_START_V <= T.ACTUAL_TIMESTAMP
             AND T.ACTUAL_TIMESTAMP <= SHIFT_END_V
      GROUP BY T.PROCESS_POINT_ID
        WITH UR;

      -- Update the summary table for all of the metrics
      UPDATE GALADM.HMIN_PP_METRICS_TBX T
         SET T.FIRST_PRODUCT_ID = FIRST_PRODUCT_V,
             T.LAST_PRODUCT_ID = LAST_PRODUCT_V,
             T.INSPECTED = COALESCE (INSPECTED_V, 0),
             T.OUTSTANDING_PRODUCT_ENTRY =
                COALESCE (OUTSTANDING_PRODUCT_ENTRY_V, 0),
             T.OUTSTANDING_PRODUCT = COALESCE (OUTSTANDING_PRODUCT_V, 0),
             T.DEFECTS_PRODUCT = COALESCE (DEFECTS_PRODUCT_V, 0),
             T.DEFECTS_PRODUCT_ENTRY = COALESCE (DEFECTS_PRODUCT_ENTRY_V, 0),
             T.DEFECTS_ENTRY = COALESCE (DEFECTS_ENTRY_V, 0),
             T.DEFECTS = COALESCE (DEFECTS_V, 0),
             T.REPAIRS_PRODUCT_ENTRY = COALESCE (REPAIRS_PRODUCT_ENTRY_V, 0)
       WHERE T.PP_METRICS_ID = PP_METRICS_ID_V;
   END P1;

COMMENT ON SPECIFIC PROCEDURE GALADM.HMIN_PP_METRICS_U_PROC IS
   'Calculates the metrics for the given process point over the production date and shift that the actual timestamp falls within and updates HMIN_PP_METRICS_TBX';

CREATE TRIGGER GALADM.GAL215TR3
   AFTER INSERT
   ON GALADM.GAL215TBX
   REFERENCING NEW AS N
   FOR EACH ROW
BEGIN ATOMIC
   CALL GALADM.HMIN_PP_METRICS_U_PROC (N.PROCESS_POINT_ID,
                                       N.ACTUAL_TIMESTAMP);
END;

CREATE TRIGGER GALADM.GAL215TR4
   AFTER UPDATE
   ON GALADM.GAL215TBX
   REFERENCING NEW AS N
   FOR EACH ROW
BEGIN ATOMIC
   CALL GALADM.HMIN_PP_METRICS_U_PROC (N.PROCESS_POINT_ID,
                                       N.ACTUAL_TIMESTAMP);
END;

CREATE TRIGGER GALADM.GAL215TR5
   AFTER DELETE
   ON GALADM.GAL215TBX
   REFERENCING OLD AS O
   FOR EACH ROW
BEGIN ATOMIC
   DECLARE PP_METRICS_ID_V   BIGINT;
   --If this is the only product in a PP_METRICS_DETAILS row then the record will never get deleted
   --So remove the rows from PP_METRICS_DETAILS
   CALL GALADM.HMIN_PP_METRICS_I_PROC (O.PROCESS_POINT_ID,
                                       O.ACTUAL_TIMESTAMP,
                                       PP_METRICS_ID_V);
   CALL GALADM.HMIN_PP_METRICS_U_PROC (O.PROCESS_POINT_ID,
                                       O.ACTUAL_TIMESTAMP);
END;


CREATE TRIGGER GALADM.GAL215TR6
   BEFORE INSERT
   ON GALADM.GAL215TBX
   REFERENCING NEW AS N
   FOR EACH ROW
BEGIN ATOMIC
   SET N.ACTUAL_PRODUCTION_DATE =
          GALADM.HMIN_GET_PRODUCTION_DATE_FUNC (N.PROCESS_POINT_ID,
                                                N.ACTUAL_TIMESTAMP);        --
   SET N.PRODUCTION_SHIFT =
          GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC (N.PROCESS_POINT_ID,
                                                 N.ACTUAL_TIMESTAMP);       --
   SET N.SCHEDULE_ID =
          GALADM.HMIN_GET_SCHEDULE_ID_FUNC (N.PROCESS_POINT_ID,
                                            N.ACTUAL_TIMESTAMP);            --
END;


CREATE TRIGGER GALADM.GAL215TR7
   BEFORE UPDATE
   ON GALADM.GAL215TBX
   REFERENCING NEW AS N
   FOR EACH ROW
BEGIN ATOMIC
   SET N.ACTUAL_PRODUCTION_DATE =
          GALADM.HMIN_GET_PRODUCTION_DATE_FUNC (N.PROCESS_POINT_ID,
                                                N.ACTUAL_TIMESTAMP);        --
   SET N.PRODUCTION_SHIFT =
          GALADM.HMIN_GET_PRODUCTION_SHIFT_FUNC (N.PROCESS_POINT_ID,
                                                 N.ACTUAL_TIMESTAMP);       --
   SET N.SCHEDULE_ID =
          GALADM.HMIN_GET_SCHEDULE_ID_FUNC (N.PROCESS_POINT_ID,
                                            N.ACTUAL_TIMESTAMP);            --
END;