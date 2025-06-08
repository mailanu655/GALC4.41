DROP VIEW GALADM.HMIN_AHM_SHIP_STATUS_VW;
DROP VIEW GALADM.HMIN_VIN_VW;
DROP VIEW GALADM.HMIN_CBU_INV_VW;
DROP VIEW GALADM.HMIN_CBU_HISTORY_VW;
DROP VIEW GALADM.HMIN_PP_METRICS_SHIFT_VW;
DROP VIEW GALADM.HMIN_PP_METRICS_DATE_VW;
DROP VIEW GALADM.HMIN_TRACKING_INVENTORY_VW;
DROP VIEW GALADM.HMIN_TRACKING_INVENTORY_REF_VW;
DROP VIEW GALADM.HMIN_PLAN_SHIFT_VW;
DROP VIEW GALADM.HMIN_PLAN_DATE_VW;
DROP VIEW GALADM.HMIN_PLAN_VW;
DROP VIEW GALADM.HMIN_OFF_PP_DIV_VW;

DROP FUNCTION GALADM.HMIN_CALC_PLAN_NOW_FUNC;
DROP FUNCTION GALADM.HMIN_CALC_PLAN_FUNC;
DROP FUNCTION GALADM.HMIN_GET_QUALITY_PP_LINE_FUNC;
DROP FUNCTION GALADM.HMIN_GET_QUALITY_PP_DIV_FUNC;
DROP FUNCTION GALADM.HMIN_GET_OFF_PP_DIV_FUNC;
DROP FUNCTION GALADM.HMIN_GET_TRACKING_PP_BY_SEQ_TIME_FUNC;
DROP FUNCTION GALADM.HMIN_GET_NEXT_TRACKING_PP_FUNC;
DROP FUNCTION GALADM.HMIN_GET_NEXT_TRACKING_LINE_FUNC;
DROP FUNCTION GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC;
DROP FUNCTION GALADM.HMIN_GET_SHIFT_END_FUNC;
DROP FUNCTION GALADM.HMIN_GET_SHIFT_START_FUNC;
DROP FUNCTION GALADM.HMIN_GET_PP_FIRST_TIMESTAMP_FUNC;
DROP FUNCTION GALADM.HMIN_GET_WORKING_DAYS_FUNC;
DROP FUNCTION GALADM.HMIN_GET_AF_OFF_VQ_SHIP_INV_FUNC;
DROP FUNCTION GALADM.HMIN_GET_VQ_OFF_TIMESTAMP_FUNC;
DROP FUNCTION GALADM.HMIN_GET_VQ_OFF_FUNC;

CREATE FUNCTION GALADM.HMIN_GET_VQ_OFF_FUNC ()
   RETURNS VARCHAR (16)
   SPECIFIC HMIN_GET_VQ_OFF_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      RETURN 'PP10090';                                                     --
   END;

CREATE FUNCTION GALADM.HMIN_GET_VQ_OFF_TIMESTAMP_FUNC (
   PRODUCT_ID_IN VARCHAR (17))
   RETURNS TIMESTAMP
   SPECIFIC HMIN_GET_VQ_OFF_TIMESTAMP_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      RETURN SELECT MIN (T.ACTUAL_TIMESTAMP)
               FROM GALADM.GAL215TBX T
              WHERE     T.PRODUCT_ID = PRODUCT_ID_IN
                    AND T.PROCESS_POINT_ID = GALADM.HMIN_GET_VQ_OFF_FUNC ()
             GROUP BY T.PRODUCT_ID, T.PROCESS_POINT_ID;                     --
   END;

CREATE FUNCTION GALADM.HMIN_GET_AF_OFF_VQ_SHIP_INV_FUNC (END_IN TIMESTAMP)
   RETURNS BIGINT
   SPECIFIC HMIN_GET_AF_OFF_VQ_SHIP_INV_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      DECLARE INV   BIGINT;

      SET INV =
             (SELECT count (DISTINCT Product.product_id)
                        AS AF_OFF_VQ_SHIP_INV
                FROM GALADM.gal215tbx ProduCt
                     INNER JOIN GALADM.gal143tbx Frame
                        ON Product.product_id = Frame.product_id
                     LEFT JOIN GALADM.GAL177TBX Repair
                        ON Frame.PRODUCT_ID = Repair.PRODUCT_ID
               WHERE     Frame.tracking_status != 'LINE57'
                     AND Frame.tracking_status != 'LINE58'
                     AND Product.process_point_id = 'PP10088'
                     AND Product.product_id IN
                            (SELECT AF_OFF.product_id
                               FROM GALADM.gal215tbx AF_OFF
                              WHERE     AF_OFF.process_point_id = 'PP10088'
                                    AND AF_OFF.ACTUAL_TIMESTAMP <= END_IN
                             EXCEPT
                             SELECT VQ_SHIP.product_id
                               FROM GALADM.gal215tbx VQ_SHIP
                              WHERE     process_point_id = 'PP10096'
                                    AND VQ_SHIP.ACTUAL_TIMESTAMP <= END_IN));
      RETURN INV;
   END;

CREATE FUNCTION GALADM.HMIN_GET_WORKING_DAYS_FUNC (
   DIVISION_IN    VARCHAR (16),
   START_IN       TIMESTAMP,
   END_IN         TIMESTAMP)
   RETURNS BIGINT
   SPECIFIC HMIN_GET_WORKING_DAYS_FUNC
   LANGUAGE SQL
   DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      DECLARE DAYS_DIFF          BIGINT;
      DECLARE NON_WORKING_DAYS   BIGINT;

      SET DAYS_DIFF =
             (  (  (DAYS (END_IN) - DAYS (START_IN)) * 86400
                 + (MIDNIGHT_SECONDS (END_IN) - MIDNIGHT_SECONDS (START_IN)))
              / 86400);
      SET NON_WORKING_DAYS =
             (SELECT count (DISTINCT S.production_date)
                FROM    GALADM.gal226tbx S
                     LEFT JOIN
                        GALADM.gal238tbx D
                     ON     D.GPCS_LINE_NO = S.LINE_NO
                        AND D.GPCS_PLANT_CODE = S.PLANT_CODE
                        AND D.GPCS_PROCESS_LOCATION = S.PROCESS_LOCATION
               WHERE     D.division_id = DIVISION_IN
                     AND (S.ISWORK IS NULL OR S.ISWORK <> 'Y')
                     AND S.START_TIMESTAMP >= START_IN
                     AND S.END_TIMESTAMP <= END_IN);
      RETURN (DAYS_DIFF - NON_WORKING_DAYS);
   END;

CREATE FUNCTION GALADM.HMIN_GET_PP_FIRST_TIMESTAMP_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   PRODUCT_ID_IN          VARCHAR (17))
   RETURNS TIMESTAMP
   SPECIFIC HMIN_GET_PP_FIRST_TIMESTAMP_FUNC
   LANGUAGE SQL
   DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      RETURN SELECT MIN (T.ACTUAL_TIMESTAMP)
               FROM GALADM.GAL215TBX T
              WHERE     T.PRODUCT_ID = PRODUCT_ID_IN
                    AND T.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
             GROUP BY T.PRODUCT_ID, T.PROCESS_POINT_ID;
   END;

COMMENT ON FUNCTION GALADM.HMIN_GET_PP_FIRST_TIMESTAMP_FUNC( VARCHAR(16), VARCHAR(17) ) IS 'Determines the First Timestamp of when the product went through the process point using the GAL215TBX';

CREATE FUNCTION GALADM.HMIN_GET_SHIFT_START_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   PRODUCTION_DATE_IN     DATE,
   SHIFT_IN               VARCHAR (2))
   RETURNS TIMESTAMP
   SPECIFIC HMIN_GET_SHIFT_START_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      RETURN SELECT MIN (S.START_TIMESTAMP) SHIFT_START
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.GAL238TBX G
                       ON     S.PLANT_CODE = G.GPCS_PLANT_CODE
                          AND S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL128TBX D
                       ON G.DIVISION_ID = D.DIVISION_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON D.DIVISION_ID = P.DIVISION_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.PRODUCTION_DATE = PRODUCTION_DATE_IN
                    AND S.SHIFT = SHIFT_IN;                                 --
   END;

CREATE FUNCTION GALADM.HMIN_GET_SHIFT_END_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   PRODUCTION_DATE_IN     DATE,
   SHIFT_IN               VARCHAR (2))
   RETURNS TIMESTAMP
   SPECIFIC HMIN_GET_SHIFT_END_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      RETURN SELECT MAX (S.END_TIMESTAMP) SHIFT_END
               FROM GALADM.GAL226TBX S
                    INNER JOIN GALADM.GAL238TBX G
                       ON     S.PLANT_CODE = G.GPCS_PLANT_CODE
                          AND S.LINE_NO = G.GPCS_LINE_NO
                          AND S.PROCESS_LOCATION = G.GPCS_PROCESS_LOCATION
                    INNER JOIN GALADM.GAL128TBX D
                       ON G.DIVISION_ID = D.DIVISION_ID
                    INNER JOIN GALADM.GAL214TBX P
                       ON D.DIVISION_ID = P.DIVISION_ID
              WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                    AND S.PRODUCTION_DATE = PRODUCTION_DATE_IN
                    AND S.SHIFT = SHIFT_IN;                                 --
   END;

CREATE FUNCTION GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
   PROCESS_POINT_ID_IN VARCHAR (16))
   RETURNS DOUBLE
   SPECIFIC HMIN_GET_PP_ALL_SEQ_NUM_FUNC
   LANGUAGE SQL
   DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
  F1:
   BEGIN ATOMIC
      RETURN SELECT   (coalesce (d.SEQUENCE_NUMBER, 0) + 1) * 1000000
                    + (coalesce (l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000
                    + (coalesce (p.SEQUENCE_NUMBER, 0) + 1)
                       ALL_SEQ_NUM
               FROM galadm.gal214tbx p
                    LEFT JOIN galadm.gal195tbx l
                       ON p.LINE_ID = l.LINE_ID
                    LEFT JOIN GALADM.GAL128TBX D
                       ON L.DIVISION_ID = D.DIVISION_ID
                    LEFT JOIN GALADM.GAL117TBX S
                       ON D.SITE_NAME = S.SITE_NAME
              WHERE P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN;               --
   END;

CREATE FUNCTION GALADM.HMIN_GET_NEXT_TRACKING_LINE_FUNC (
   LINE_ID_IN VARCHAR (16))
   RETURNS VARCHAR (16)
   SPECIFIC HMIN_GET_NEXT_TRACKING_LINE_FUNC
   LANGUAGE SQL
   DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      RETURN (SELECT L.LINE_ID
                FROM GALADM.GAL195TBX L
               WHERE     GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                            L.ENTRY_PROCESS_POINT_ID) >
                            (SELECT GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                                       P.ENTRY_PROCESS_POINT_ID)
                               FROM GALADM.GAL195TBX P
                              WHERE P.LINE_ID = LINE_ID_IN)
                     AND (    L.ENTRY_PROCESS_POINT_ID <> ''
                          AND L.ENTRY_PROCESS_POINT_ID IS NOT NULL)
              ORDER BY GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                          L.ENTRY_PROCESS_POINT_ID)
              FETCH FIRST 1 ROWS ONLY);                                     --
   END;

CREATE FUNCTION GALADM.HMIN_GET_NEXT_TRACKING_PP_FUNC (
   DIVISION_ID_IN    VARCHAR (16),
   PP_ID_IN          VARCHAR (16))
   RETURNS VARCHAR (16)
   SPECIFIC HMIN_GET_NEXT_TRACKING_PP_FUNC
   LANGUAGE SQL
   DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      DECLARE NEXT_PP_V   VARCHAR (16);
      SET NEXT_PP_V =
             (SELECT p.PROCESS_POINT_ID
                FROM galadm.gal214tbx p
               WHERE     p.tracking_point_flag = 1
                     AND p.DIVISION_ID = DIVISION_ID_IN
                     AND GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                            p.PROCESS_POINT_ID) >
                            (SELECT GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                                       p.PROCESS_POINT_ID)
                               FROM galadm.gal214tbx p
                              WHERE p.PROCESS_POINT_ID = PP_ID_IN)
              ORDER BY GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                          p.PROCESS_POINT_ID)
              FETCH FIRST 1 ROWS ONLY);

      IF (NEXT_PP_V IS NULL)
      THEN
         SET NEXT_PP_V =
                (SELECT p.PROCESS_POINT_ID
                   FROM galadm.gal214tbx p
                  WHERE     p.tracking_point_flag = 1
                        AND p.DIVISION_ID = DIVISION_ID_IN
                 ORDER BY GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                             p.PROCESS_POINT_ID)
                 FETCH FIRST 1 ROWS ONLY);
      END IF;

      RETURN NEXT_PP_V;
   END;

CREATE FUNCTION GALADM.HMIN_GET_TRACKING_PP_BY_SEQ_TIME_FUNC (
   LOCATION_LEVEL_ID_IN    VARCHAR (16),
   SECONDS_HOLD_IN         INTEGER)
   RETURNS VARCHAR (16)
   SPECIFIC HMIN_GET_TRACKING_PP_BY_SEQ_TIME_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      RETURN (SELECT t.PROCESS_POINT_ID
                FROM (SELECT row_number ()
                             OVER (
                                ORDER BY
                                   galadm.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                                      m.PROCESS_POINT_ID),
                                   M.PROCESS_POINT_ID)
                                rownum,
                             m.PROCESS_POINT_ID
                        FROM GALADM.GAL214TBX m
                       WHERE     m.tracking_point_flag = 1
                             AND m.DIVISION_ID =
                                    CAST (
                                       LOCATION_LEVEL_ID_IN AS VARCHAR (16))
                      ORDER BY galadm.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
                                  m.PROCESS_POINT_ID),
                               M.PROCESS_POINT_ID) t
               WHERE t.rownum =
                        (  1
                         + mod (
                              int (
                                   MIDNIGHT_SECONDS (CURRENT_TIMESTAMP)
                                 / SECONDS_HOLD_IN),
                              (SELECT count (*)
                                 FROM galadm.gal214tbx p
                                WHERE     p.tracking_point_flag = 1
                                      AND p.DIVISION_ID =
                                             CAST (
                                                LOCATION_LEVEL_ID_IN AS VARCHAR (16))))));
   END;

COMMENT ON FUNCTION GALADM.HMIN_GET_TRACKING_PP_BY_SEQ_TIME_FUNC( VARCHAR(16), INTEGER ) IS 'Return a Process Point ID for SECONDS_HOLD for every tracking point in the LOCATION_LEVEL_ID (It varies over time)';

CREATE FUNCTION GALADM.HMIN_GET_OFF_PP_DIV_FUNC (DIVISION_ID_IN VARCHAR (16))
   RETURNS VARCHAR (16)
   SPECIFIC HMIN_GET_OFF_PP_DIV_FUNC
   LANGUAGE SQL
   DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      DECLARE COMPONENT_ID_V       VARCHAR (40);
      DECLARE PROPERTY_KEY_V       VARCHAR (40);
      DECLARE PROPERTY_VALUE_V     VARCHAR (256);
      DECLARE PROCESS_POINT_ID_V   VARCHAR (16);

      SET PROPERTY_KEY_V = 'OFF_PP_' || DIVISION_ID_IN;
      SET PROPERTY_VALUE_V = 'TRUE';
      --Get the First Record that matches in the property table
      SET COMPONENT_ID_V =
             (SELECT P.COMPONENT_ID
                FROM GALADM.GAL489TBX P
               WHERE     P.PROPERTY_KEY = PROPERTY_KEY_V
                     AND UPPER (P.PROPERTY_VALUE) = UPPER (PROPERTY_VALUE_V)
              ORDER BY P.COMPONENT_ID
              FETCH FIRST 1 ROWS ONLY);

      IF (LENGTH (COMPONENT_ID_V) > 5)
      THEN
         --Some process points have properties that start with prop_ so remove it
         IF (SUBSTR (COMPONENT_ID_V, 1, 5) = 'prop_')
         THEN
            SET COMPONENT_ID_V =
                   RIGHT (COMPONENT_ID_V, LENGTH (COMPONENT_ID_V) - 5);
         END IF;
      END IF;

      --Only return if it is a valid Process Point
      SET PROCESS_POINT_ID_V =
             (SELECT P.PROCESS_POINT_ID
                FROM GALADM.GAL214TBX P
               WHERE P.PROCESS_POINT_ID = COMPONENT_ID_V);

      RETURN PROCESS_POINT_ID_V;
   END;

CREATE FUNCTION GALADM.HMIN_GET_QUALITY_PP_DIV_FUNC (
   DIVISION_ID_IN VARCHAR (16))
   RETURNS VARCHAR (16)
   SPECIFIC HMIN_GET_QUALITY_PP_DIV_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      DECLARE COMPONENT_ID_V       VARCHAR (40);                            --
      DECLARE PROPERTY_KEY_V       VARCHAR (40);                            --
      DECLARE PROPERTY_VALUE_V     VARCHAR (256);                           --
      DECLARE PROCESS_POINT_ID_V   VARCHAR (16);                            --

      SET PROPERTY_KEY_V = 'QUALITY_PP_' || DIVISION_ID_IN;                 --
      SET PROPERTY_VALUE_V = 'TRUE';                                        --
      SET COMPONENT_ID_V =
             (SELECT P.COMPONENT_ID
                FROM GALADM.GAL489TBX P
               WHERE     P.PROPERTY_KEY = PROPERTY_KEY_V
                     AND UPPER (P.PROPERTY_VALUE) = UPPER (PROPERTY_VALUE_V)
              ORDER BY P.COMPONENT_ID
              FETCH FIRST 1 ROWS ONLY);                                     --

      IF (LENGTH (COMPONENT_ID_V) > 5)
      THEN
         IF (SUBSTR (COMPONENT_ID_V, 1, 5) = 'prop_')
         THEN
            SET COMPONENT_ID_V =
                   RIGHT (COMPONENT_ID_V, LENGTH (COMPONENT_ID_V) - 5);     --
         END IF;                                                            --
      END IF;                                                               --

      SET PROCESS_POINT_ID_V =
             (SELECT P.PROCESS_POINT_ID
                FROM GALADM.GAL214TBX P
               WHERE P.PROCESS_POINT_ID = COMPONENT_ID_V);                  --

      RETURN PROCESS_POINT_ID_V;                                            --
   END;

CREATE FUNCTION GALADM.HMIN_GET_QUALITY_PP_LINE_FUNC (
   LINE_ID_IN VARCHAR (16))
   RETURNS VARCHAR (16)
   SPECIFIC HMIN_GET_QUALITY_PP_LINE_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
   BEGIN ATOMIC
      DECLARE COMPONENT_ID_V       VARCHAR (40);                            --
      DECLARE PROPERTY_KEY_V       VARCHAR (40);                            --
      DECLARE PROPERTY_VALUE_V     VARCHAR (256);                           --
      DECLARE PROCESS_POINT_ID_V   VARCHAR (16);                            --

      SET PROPERTY_KEY_V = 'QUALITY_PP_' || LINE_ID_IN;                     --
      SET PROPERTY_VALUE_V = 'TRUE';                                        --
      SET COMPONENT_ID_V =
             (SELECT P.COMPONENT_ID
                FROM GALADM.GAL489TBX P
               WHERE     P.PROPERTY_KEY = PROPERTY_KEY_V
                     AND UPPER (P.PROPERTY_VALUE) = UPPER (PROPERTY_VALUE_V)
              ORDER BY P.COMPONENT_ID
              FETCH FIRST 1 ROWS ONLY);                                     --

      IF (LENGTH (COMPONENT_ID_V) > 5)
      THEN
         IF (SUBSTR (COMPONENT_ID_V, 1, 5) = 'prop_')
         THEN
            SET COMPONENT_ID_V =
                   RIGHT (COMPONENT_ID_V, LENGTH (COMPONENT_ID_V) - 5);     --
         END IF;                                                            --
      END IF;                                                               --

      SET PROCESS_POINT_ID_V =
             (SELECT P.PROCESS_POINT_ID
                FROM GALADM.GAL214TBX P
               WHERE P.PROCESS_POINT_ID = COMPONENT_ID_V);                  --

      RETURN PROCESS_POINT_ID_V;                                            --
   END;

CREATE FUNCTION GALADM.HMIN_CALC_PLAN_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   START_IN               TIMESTAMP,
   END_IN                 TIMESTAMP)
   RETURNS DOUBLE
   SPECIFIC HMIN_CALC_PLAN_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
  F1:
   BEGIN ATOMIC
      DECLARE PROCESS_POINT_TYPE_V   INTEGER;                               --
      DECLARE PLAN_V                 DOUBLE;                                --

      SET PROCESS_POINT_TYPE_V =
             (SELECT PROCESS_POINT_TYPE
                FROM GALADM.GAL214TBX P
               WHERE P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN);             --


      IF (PROCESS_POINT_TYPE_V = 1 OR PROCESS_POINT_TYPE_V = 9)
      THEN
         SET PLAN_V =
                (SELECT sum (s.CAPACITY_ON) AS PLAN_QTY
                   FROM galadm.GAL214TBX p
                        LEFT JOIN galadm.GAL128TBX d
                           ON p.DIVISION_ID = d.DIVISION_ID
                        LEFT JOIN galadm.gal238tbx g
                           ON d.DIVISION_ID = g.DIVISION_ID
                        LEFT JOIN galadm.GAL226TBX s
                           ON     g.GPCS_LINE_NO = s.LINE_NO
                              AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                              AND g.GPCS_PROCESS_LOCATION =
                                     s.PROCESS_LOCATION
                  WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                        AND S.START_TIMESTAMP >= START_IN
                        AND S.END_TIMESTAMP <= END_IN);                     --
      END IF;                                                               --

      IF (PROCESS_POINT_TYPE_V = 2 OR PROCESS_POINT_TYPE_V = 8)
      THEN
         SET PLAN_V =
                (SELECT sum (s.CAPACITY) AS PLAN_QTY
                   FROM galadm.GAL214TBX p
                        LEFT JOIN galadm.GAL128TBX d
                           ON p.DIVISION_ID = d.DIVISION_ID
                        LEFT JOIN galadm.gal238tbx g
                           ON d.DIVISION_ID = g.DIVISION_ID
                        LEFT JOIN galadm.GAL226TBX s
                           ON     g.GPCS_LINE_NO = s.LINE_NO
                              AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                              AND g.GPCS_PROCESS_LOCATION =
                                     s.PROCESS_LOCATION
                  WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                        AND S.START_TIMESTAMP >= START_IN
                        AND S.END_TIMESTAMP <= END_IN);                     --
      END IF;                                                               --

      SET PLAN_V = COALESCE (PLAN_V, 0);                                    --
      RETURN PLAN_V;                                                        --
   END;

COMMENT ON FUNCTION GALADM.HMIN_CALC_PLAN_FUNC( VARCHAR(16), TIMESTAMP, TIMESTAMP ) IS 'Given a Process Point ID and start and end timestamp return the Plan from the GAL226TBX';

CREATE FUNCTION GALADM.HMIN_CALC_PLAN_NOW_FUNC (
   PROCESS_POINT_ID_IN    VARCHAR (16),
   START_IN               TIMESTAMP,
   END_IN                 TIMESTAMP)
   RETURNS DOUBLE
   SPECIFIC HMIN_CALC_PLAN_NOW_FUNC
   LANGUAGE SQL
   NOT DETERMINISTIC
   NO EXTERNAL ACTION
   READS SQL DATA
   INHERIT SPECIAL REGISTERS
  F1:
   BEGIN ATOMIC
      DECLARE PROCESS_POINT_TYPE_V   INTEGER;                               --
      DECLARE PLAN_V                 DOUBLE;                                --

      SET PROCESS_POINT_TYPE_V =
             (SELECT PROCESS_POINT_TYPE
                FROM GALADM.GAL214TBX P
               WHERE P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN);             --


      IF (PROCESS_POINT_TYPE_V = 1 OR PROCESS_POINT_TYPE_V = 9)
      THEN
         SET PLAN_V =
                (SELECT sum (
                           CASE
                              WHEN     S.PLAN = 'Y'
                                   AND S.START_TIMESTAMP <= CURRENT_TIMESTAMP
                                   AND CURRENT_TIMESTAMP >= S.END_TIMESTAMP
                              THEN
                                 S.CAPACITY_ON
                              WHEN     S.PLAN = 'Y'
                                   AND CURRENT TIMESTAMP >= S.START_TIMESTAMP
                                   AND CURRENT TIMESTAMP <= S.END_TIMESTAMP
                              THEN
                                   timestampdiff (
                                      2,
                                      char (
                                           CURRENT TIMESTAMP
                                         - S.START_TIMESTAMP))
                                 * (  decimal (S.CAPACITY_ON, 20, 15)
                                    / (TIMESTAMPDIFF (
                                          2,
                                          char (
                                               S.END_TIMESTAMP
                                             - S.START_TIMESTAMP))))
                              ELSE
                                 0
                           END)
                           AS PLAN_QTY_NOW
                   FROM galadm.GAL214TBX p
                        LEFT JOIN galadm.GAL128TBX d
                           ON p.DIVISION_ID = d.DIVISION_ID
                        LEFT JOIN galadm.gal238tbx g
                           ON d.DIVISION_ID = g.DIVISION_ID
                        LEFT JOIN galadm.GAL226TBX s
                           ON     g.GPCS_LINE_NO = s.LINE_NO
                              AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                              AND g.GPCS_PROCESS_LOCATION =
                                     s.PROCESS_LOCATION
                  WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                        AND S.START_TIMESTAMP >= START_IN
                        AND S.END_TIMESTAMP <= END_IN);                     --
      END IF;                                                               --

      IF (PROCESS_POINT_TYPE_V = 2 OR PROCESS_POINT_TYPE_V = 8)
      THEN
         SET PLAN_V =
                (SELECT sum (
                           CASE
                              WHEN     S.PLAN = 'Y'
                                   AND S.START_TIMESTAMP <= CURRENT_TIMESTAMP
                                   AND CURRENT_TIMESTAMP >= S.END_TIMESTAMP
                              THEN
                                 S.CAPACITY
                              WHEN     S.PLAN = 'Y'
                                   AND CURRENT TIMESTAMP >= S.START_TIMESTAMP
                                   AND CURRENT TIMESTAMP <= S.END_TIMESTAMP
                              THEN
                                   timestampdiff (
                                      2,
                                      char (
                                           CURRENT TIMESTAMP
                                         - S.START_TIMESTAMP))
                                 * (  decimal (S.CAPACITY, 20, 15)
                                    / (TIMESTAMPDIFF (
                                          2,
                                          char (
                                               S.END_TIMESTAMP
                                             - S.START_TIMESTAMP))))
                              ELSE
                                 0
                           END)
                           AS PLAN_QTY_NOW
                   FROM galadm.GAL214TBX p
                        LEFT JOIN galadm.GAL128TBX d
                           ON p.DIVISION_ID = d.DIVISION_ID
                        LEFT JOIN galadm.gal238tbx g
                           ON d.DIVISION_ID = g.DIVISION_ID
                        LEFT JOIN galadm.GAL226TBX s
                           ON     g.GPCS_LINE_NO = s.LINE_NO
                              AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                              AND g.GPCS_PROCESS_LOCATION =
                                     s.PROCESS_LOCATION
                  WHERE     P.PROCESS_POINT_ID = PROCESS_POINT_ID_IN
                        AND S.START_TIMESTAMP >= START_IN
                        AND S.END_TIMESTAMP <= END_IN);                     --
      END IF;                                                               --

      SET PLAN_V = COALESCE (PLAN_V, 0);                                    --
      RETURN PLAN_V;                                                        --
   END;

COMMENT ON FUNCTION GALADM.HMIN_CALC_PLAN_NOW_FUNC( VARCHAR(16), TIMESTAMP, TIMESTAMP ) IS 'Given a Process Point ID and start and end timestamp return the Plan as of the current time from the GAL226TBX';


CREATE VIEW GALADM.HMIN_OFF_PP_DIV_VW
(
   SITE_NAME,
   PLANT_NAME,
   DIV_SEQ_NUM,
   DIVISION_ID,
   DIVISION_NAME,
   DIVISION_DESCRIPTION,
   PROCESS_POINT_ID,
   PP_SEQ_NUM
)
AS
   SELECT trim (T.SITE_NAME) AS SITE_NAME,
          trim (T.PLANT_NAME) AS PLANT_NAME,
          T.SEQUENCE_NUMBER AS DIV_SEQ_NUM,
          trim (T.DIVISION_ID) AS DIVISION_ID,
          trim (T.DIVISION_NAME) AS DIVISION_NAME,
          trim (T.DIVISION_DESCRIPTION) AS DIVISION_DESCRIPTION,
          GALADM.HMIN_GET_QUALITY_PP_DIV_FUNC (T.DIVISION_ID)
             AS PROCESS_POINT_ID,
          GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (
             GALADM.HMIN_GET_QUALITY_PP_DIV_FUNC (T.DIVISION_ID))
             AS PP_SEQ_NUM
     FROM GALADM.GAL128TBX T
    WHERE GALADM.HMIN_GET_OFF_PP_DIV_FUNC (T.DIVISION_ID) IS NOT NULL
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_OFF_PP_DIV_VW IS
   'The Off Process Points for the Division using the HMIN_GET_OFF_PP_DIV_FUNC';

CREATE VIEW GALADM.HMIN_PLAN_VW
(
   PROCESS_POINT_ID,
   TRACKING_POINT_FLAG,
   SCHEDULE_ID,
   PLAN_QTY,
   PLAN_QTY_NOW
)
AS
   SELECT P.PROCESS_POINT_ID,
          P.TRACKING_POINT_FLAG,
          S.SCHEDULE_ID,
          GALADM.HMIN_CALC_PLAN_FUNC (P.PROCESS_POINT_ID,
                                      S.START_TIMESTAMP,
                                      S.END_TIMESTAMP)
             AS PLAN_QTY,
          GALADM.HMIN_CALC_PLAN_NOW_FUNC (P.PROCESS_POINT_ID,
                                          S.START_TIMESTAMP,
                                          S.END_TIMESTAMP)
             AS PLAN_QTY_NOW
     FROM galadm.GAL214TBX p
          LEFT JOIN galadm.GAL128TBX d
             ON p.DIVISION_ID = d.DIVISION_ID
          LEFT JOIN galadm.gal238tbx g
             ON d.DIVISION_ID = g.DIVISION_ID
          LEFT JOIN galadm.GAL226TBX s
             ON     g.GPCS_LINE_NO = s.LINE_NO
                AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                AND g.GPCS_PROCESS_LOCATION = s.PROCESS_LOCATION
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_PLAN_VW IS 'Plan_View';

CREATE VIEW GALADM.HMIN_PLAN_DATE_VW
(
   SITE_NAME,
   PLANT_NAME,
   DIV_SEQ_NUM,
   DIVISION_ID,
   DIVISION_NAME,
   DIVISION_DESCRIPTION,
   PP_SEQ_NUM,
   PROCESS_POINT_ID,
   PROCESS_POINT_NAME,
   PROCESS_POINT_DESCRIPTION,
   TRACKING_POINT_FLAG,
   PROCESS_POINT_TYPE,
   PRODUCTION_DATE,
   DATE_START_TIMESTAMP,
   DATE_END_TIMESTAMP,
   PLAN_QTY,
   PLAN_QTY_NOW
)
AS
   SELECT d.SITE_NAME,
          d.PLANT_NAME,
          d.SEQUENCE_NUMBER AS DIV_SEQ_NUM,
          d.DIVISION_ID,
          d.DIVISION_NAME,
          d.DIVISION_DESCRIPTION,
          GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (P.PROCESS_POINT_ID)
             AS PP_SEQ_NUM,
          P.PROCESS_POINT_ID,
          P.PROCESS_POINT_NAME,
          P.PROCESS_POINT_DESCRIPTION,
          P.TRACKING_POINT_FLAG,
          P.PROCESS_POINT_TYPE,
          S.PRODUCTION_DATE,
          MIN (S.START_TIMESTAMP) AS DATE_START_TIMESTAMP,
          MAX (S.END_TIMESTAMP) AS DATE_END_TIMESTAMP,
          GALADM.HMIN_CALC_PLAN_FUNC (P.PROCESS_POINT_ID,
                                      MIN (S.START_TIMESTAMP),
                                      MAX (S.END_TIMESTAMP))
             AS PLAN_QTY,
          GALADM.HMIN_CALC_PLAN_NOW_FUNC (P.PROCESS_POINT_ID,
                                          MIN (S.START_TIMESTAMP),
                                          MAX (S.END_TIMESTAMP))
             AS PLAN_QTY_NOW
     FROM galadm.GAL214TBX p
          LEFT JOIN galadm.GAL128TBX d
             ON p.DIVISION_ID = d.DIVISION_ID
          LEFT JOIN galadm.gal238tbx g
             ON d.DIVISION_ID = g.DIVISION_ID
          LEFT JOIN galadm.GAL226TBX s
             ON     g.GPCS_LINE_NO = s.LINE_NO
                AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                AND g.GPCS_PROCESS_LOCATION = s.PROCESS_LOCATION
   GROUP BY d.SITE_NAME,
            d.PLANT_NAME,
            d.SEQUENCE_NUMBER,
            d.DIVISION_ID,
            d.DIVISION_NAME,
            d.DIVISION_DESCRIPTION,
            P.PROCESS_POINT_ID,
            P.PROCESS_POINT_NAME,
            P.PROCESS_POINT_DESCRIPTION,
            P.TRACKING_POINT_FLAG,
            P.PROCESS_POINT_TYPE,
            S.PRODUCTION_DATE
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_PLAN_DATE_VW IS
   'All Process Points from GAL214TBX joined to all Production Dates in GAL226TBX with Plan and Plan Now calculated using function';

CREATE VIEW GALADM.HMIN_PLAN_SHIFT_VW
(
   SITE_NAME,
   PLANT_NAME,
   DIV_SEQ_NUM,
   DIVISION_ID,
   DIVISION_NAME,
   DIVISION_DESCRIPTION,
   PP_SEQ_NUM,
   PROCESS_POINT_ID,
   PROCESS_POINT_NAME,
   PROCESS_POINT_DESCRIPTION,
   TRACKING_POINT_FLAG,
   PROCESS_POINT_TYPE,
   PRODUCTION_DATE,
   SHIFT,
   SHIFT_START_TIMESTAMP,
   SHIFT_END_TIMESTAMP,
   PLAN_QTY,
   PLAN_QTY_NOW
)
AS
   SELECT d.SITE_NAME,
          d.PLANT_NAME,
          d.SEQUENCE_NUMBER AS DIV_SEQ_NUM,
          d.DIVISION_ID,
          d.DIVISION_NAME,
          d.DIVISION_DESCRIPTION,
          GALADM.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (P.PROCESS_POINT_ID)
             AS PP_SEQ_NUM,
          P.PROCESS_POINT_ID,
          P.PROCESS_POINT_NAME,
          P.PROCESS_POINT_DESCRIPTION,
          P.TRACKING_POINT_FLAG,
          P.PROCESS_POINT_TYPE,
          S.PRODUCTION_DATE,
          S.SHIFT,
          MIN (S.START_TIMESTAMP) AS SHIFT_START_TIMESTAMP,
          MAX (S.END_TIMESTAMP) AS SHIFT_END_TIMESTAMP,
          GALADM.HMIN_CALC_PLAN_FUNC (P.PROCESS_POINT_ID,
                                      MIN (S.START_TIMESTAMP),
                                      MAX (S.END_TIMESTAMP))
             AS PLAN_QTY,
          GALADM.HMIN_CALC_PLAN_NOW_FUNC (P.PROCESS_POINT_ID,
                                          MIN (S.START_TIMESTAMP),
                                          MAX (S.END_TIMESTAMP))
             AS PLAN_QTY_NOW
     FROM galadm.GAL214TBX p
          LEFT JOIN galadm.GAL128TBX d
             ON p.DIVISION_ID = d.DIVISION_ID
          LEFT JOIN galadm.gal238tbx g
             ON d.DIVISION_ID = g.DIVISION_ID
          LEFT JOIN galadm.GAL226TBX s
             ON     g.GPCS_LINE_NO = s.LINE_NO
                AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                AND g.GPCS_PROCESS_LOCATION = s.PROCESS_LOCATION
   GROUP BY d.SITE_NAME,
            d.PLANT_NAME,
            d.SEQUENCE_NUMBER,
            d.DIVISION_ID,
            d.DIVISION_NAME,
            d.DIVISION_DESCRIPTION,
            P.PROCESS_POINT_ID,
            P.PROCESS_POINT_NAME,
            P.PROCESS_POINT_DESCRIPTION,
            P.TRACKING_POINT_FLAG,
            P.PROCESS_POINT_TYPE,
            S.PRODUCTION_DATE,
            S.SHIFT
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_PLAN_SHIFT_VW IS
   'All Process Points from GAL214TBX joined to all Production Date and Shifts in GAL226TBX with Plan and Plan Now calculated using function';

CREATE VIEW GALADM.HMIN_TRACKING_INVENTORY_REF_VW
(
   TRACKING_SITE_NAME,
   TRACKING_SITE_DESC,
   TRACKING_DIVISION_ID,
   TRACKING_DIVISION_NAME,
   TRACKING_DIVISION_STD_INV,
   TRACKING_DIVISION_DESC,
   TRACKING_PLANT_NAME,
   TRACKING_LINE_ID,
   TRACKING_LINE_NAME,
   TRACKING_LINE_DESC,
   TRACKING_PROCESS_POINT_ID,
   TRACKING_PROCESS_POINT_NAME,
   TRACKING_PROCESS_POINT_DESC,
   TRACKING_SEQ,
   STD_INVENTORY,
   MINIMUM_INVENTORY,
   MAXIMUM_INVENTORY,
   CURRENT_INVENTORY,
   NEXT_TRACKING_LINE_ID,
   NEXT_TRACKING_LINE_NAME,
   NEXT_TRACKING_PP_NAME
)
AS
   SELECT trim (S.SITE_NAME),
          trim (S.SITE_DESCRIPTION),
          trim (D.DIVISION_ID),
          trim (D.DIVISION_NAME),
          (SELECT sum (LINE.std_inventory)
             FROM galadm.gal195tbx line
            WHERE     line.DIVISION_ID = d.DIVISION_ID
                  AND LINE.ENTRY_PROCESS_POINT_ID IS NOT NULL
                  AND LINE.ENTRY_PROCESS_POINT_ID <> ''),
          trim (D.DIVISION_DESCRIPTION),
          trim (D.PLANT_NAME),
          TRIM (L.LINE_ID),
          TRIM (L.LINE_NAME),
          TRIM (L.LINE_DESCRIPTION),
          TRIM (P.PROCESS_POINT_ID),
          TRIM (P.PROCESS_POINT_NAME),
          TRIM (P.PROCESS_POINT_DESCRIPTION),
          galadm.HMIN_GET_PP_ALL_SEQ_NUM_FUNC (p.PROCESS_POINT_ID),
          L.STD_INVENTORY,
          L.MINIMUM_INVENTORY,
          L.MAXIMUM_INVENTORY,
          COALESCE ( (SELECT COUNT (DISTINCT F.PRODUCT_ID)
                        FROM GALADM.GAL143TBX F
                       WHERE F.TRACKING_STATUS = L.LINE_ID
                      GROUP BY F.TRACKING_STATUS),
                    0),
          GALADM.HMIN_GET_NEXT_TRACKING_LINE_FUNC (L.LINE_ID),
          (SELECT N.LINE_NAME
             FROM GALADM.GAL195TBX N
            WHERE N.LINE_ID =
                     GALADM.HMIN_GET_NEXT_TRACKING_LINE_FUNC (L.LINE_ID)),
          (SELECT P.PROCESS_POINT_NAME
             FROM    GALADM.GAL195TBX N
                  LEFT JOIN
                     GALADM.GAL214TBX P
                  ON N.ENTRY_PROCESS_POINT_ID = P.PROCESS_POINT_ID
            WHERE N.LINE_ID =
                     GALADM.HMIN_GET_NEXT_TRACKING_LINE_FUNC (L.LINE_ID))
     FROM GALADM.GAL117TBX S
          LEFT JOIN GALADM.GAL128TBX D
             ON S.SITE_NAME = D.SITE_NAME
          LEFT JOIN GALADM.GAL195TBX L
             ON D.DIVISION_ID = L.DIVISION_ID
          LEFT JOIN GALADM.GAL214TBX P
             ON L.LINE_ID = P.LINE_ID
    WHERE P.TRACKING_POINT_FLAG = 1
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_TRACKING_INVENTORY_REF_VW IS
   'Tracking Inventory REF';

CREATE VIEW GALADM.HMIN_TRACKING_INVENTORY_VW
(
   TRACKING_SITE_NAME,
   TRACKING_SITE_DESC,
   TRACKING_DIVISION_ID,
   TRACKING_DIVISION_NAME,
   TRACKING_DIVISION_STD_INV,
   TRACKING_DIVISION_DESC,
   TRACKING_PLANT_NAME,
   TRACKING_LINE_ID,
   TRACKING_LINE_NAME,
   TRACKING_LINE_DESC,
   TRACKING_PROCESS_POINT_ID,
   TRACKING_PROCESS_POINT_NAME,
   TRACKING_PROCESS_POINT_DESC,
   TRACKING_SEQ,
   STD_INVENTORY,
   MINIMUM_INVENTORY,
   MAXIMUM_INVENTORY,
   CURRENT_INVENTORY,
   DIFF,
   CURRENT_INV_STATUS,
   NEXT_TRACKING_LINE_ID,
   NEXT_TRACKING_LINE_NAME,
   NEXT_TRACKING_PP_NAME
)
AS
   SELECT TRACKING_SITE_NAME,
          TRACKING_SITE_DESC,
          TRACKING_DIVISION_ID,
          TRACKING_DIVISION_NAME,
          TRACKING_DIVISION_STD_INV,
          TRACKING_DIVISION_DESC,
          TRACKING_PLANT_NAME,
          TRACKING_LINE_ID,
          TRACKING_LINE_NAME,
          TRACKING_LINE_DESC,
          TRACKING_PROCESS_POINT_ID,
          TRACKING_PROCESS_POINT_NAME,
          TRACKING_PROCESS_POINT_DESC,
          TRACKING_SEQ,
          STD_INVENTORY,
          MINIMUM_INVENTORY,
          MAXIMUM_INVENTORY,
          CURRENT_INVENTORY,
          (CURRENT_INVENTORY - STD_INVENTORY) AS DIFF,
          CASE
             WHEN CURRENT_INVENTORY < MINIMUM_INVENTORY THEN -1
             WHEN CURRENT_INVENTORY > MAXIMUM_INVENTORY THEN 1
             ELSE 0
          END
             AS CURRENT_INV_STATUS,
          NEXT_TRACKING_LINE_ID,
          NEXT_TRACKING_LINE_NAME,
          NEXT_TRACKING_PP_NAME
     FROM GALADM.HMIN_TRACKING_INVENTORY_REF_VW
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_TRACKING_INVENTORY_VW IS 'Tracking Inventory';

CREATE VIEW GALADM.HMIN_PP_METRICS_DATE_VW
(
   SITE_NAME,
   PLANT_NAME,
   DIV_SEQ_NUM,
   DIVISION_ID,
   DIVISION_NAME,
   DIVISION_DESCRIPTION,
   PP_SEQ_NUM,
   PROCESS_POINT_ID,
   PROCESS_POINT_NAME,
   PROCESS_POINT_DESCRIPTION,
   TRACKING_POINT_FLAG,
   PROCESS_POINT_TYPE,
   PRODUCTION_DATE,
   DATE_START_TIMESTAMP,
   DATE_END_TIMESTAMP,
   PLAN_QTY,
   PLAN_QTY_NOW,
   INSPECTED,
   PLAN_ACTUAL_DIFF,
   DEFECTS_PRODUCT_ENTRY,
   DEFECTS_PRODUCT,
   DEFECTS_ENTRY,
   DEFECTS,
   OUTSTANDING_PRODUCT_ENTRY,
   OUTSTANDING_PRODUCT,
   REPAIRS_PRODUCT_ENTRY,
   SCRAP,
   DIRECT_PASSED,
   RPU_ENTRY,
   RPU,
   RSS_ENTRY,
   RSS,
   SS_ENTRY,
   SS
)
AS
   SELECT PLAN.SITE_NAME,
          PLAN.PLANT_NAME,
          PLAN.DIV_SEQ_NUM,
          PLAN.DIVISION_ID,
          PLAN.DIVISION_NAME,
          PLAN.DIVISION_DESCRIPTION,
          PLAN.PP_SEQ_NUM,
          PLAN.PROCESS_POINT_ID,
          PLAN.PROCESS_POINT_NAME,
          PLAN.PROCESS_POINT_DESCRIPTION,
          PLAN.TRACKING_POINT_FLAG,
          PLAN.PROCESS_POINT_TYPE,
          PLAN.PRODUCTION_DATE,
          PLAN.DATE_START_TIMESTAMP,
          PLAN.DATE_END_TIMESTAMP,
          PLAN.PLAN_QTY,
          PLAN.PLAN_QTY_NOW,
          SUM (COALESCE (METRICS.INSPECTED, 0)) AS INSPECTED,
          (SUM (COALESCE (METRICS.INSPECTED, 0)) - PLAN.PLAN_QTY_NOW)
             AS PLAN_ACTUAL_DIFF,
          SUM (COALESCE (METRICS.DEFECTS_PRODUCT_ENTRY, 0))
             AS DEFECTS_PRODUCT_ENTRY,
          SUM (COALESCE (METRICS.DEFECTS_PRODUCT, 0)) AS DEFECTS_PRODUCT,
          SUM (COALESCE (METRICS.DEFECTS_ENTRY, 0)) AS DEFECTS_ENTRY,
          SUM (COALESCE (METRICS.DEFECTS, 0)) AS DEFECTS,
          SUM (COALESCE (METRICS.OUTSTANDING_PRODUCT_ENTRY, 0))
             AS OUTSTANDING_PRODUCT_ENTRY,
          SUM (COALESCE (METRICS.OUTSTANDING_PRODUCT, 0))
             AS OUTSTANDING_PRODUCT,
          SUM (COALESCE (METRICS.REPAIRS_PRODUCT_ENTRY, 0))
             AS REPAIRS_PRODUCT_ENTRY,
          SUM (COALESCE (METRICS.SCRAP, 0)) AS SCRAP,
          SUM (
               COALESCE (METRICS.INSPECTED, 0)
             - COALESCE (METRICS.DEFECTS_PRODUCT_ENTRY, 0))
             AS DIRECT_PASSED,
          CASE
             WHEN SUM (METRICS.INSPECTED) <> 0
             THEN
                (  SUM (COALESCE (METRICS.DEFECTS_ENTRY, 0))
                 / SUM (METRICS.INSPECTED))
             ELSE
                0
          END
             AS RPU_ENTRY,
          CASE
             WHEN SUM (METRICS.INSPECTED) <> 0
             THEN
                (  SUM (COALESCE (METRICS.DEFECTS, 0))
                 / SUM (METRICS.INSPECTED))
             ELSE
                0
          END
             AS RPU,
          CASE
             WHEN SUM (METRICS.INSPECTED) <> 0
             THEN
                (  (  SUM (COALESCE (METRICS.INSPECTED, 0))
                    - SUM (COALESCE (METRICS.DEFECTS_PRODUCT_ENTRY, 0)))
                 / SUM (METRICS.INSPECTED))
             ELSE
                0
          END
             AS RSS_ENTRY,
          CASE
             WHEN SUM (METRICS.INSPECTED) <> 0
             THEN
                (  (  SUM (COALESCE (METRICS.INSPECTED, 0))
                    - SUM (COALESCE (METRICS.DEFECTS_PRODUCT, 0)))
                 / SUM (METRICS.INSPECTED))
             ELSE
                0
          END
             AS RSS,
          CASE
             WHEN SUM (METRICS.INSPECTED) <> 0
             THEN
                (  (  SUM (COALESCE (METRICS.INSPECTED, 0))
                    - SUM (COALESCE (METRICS.OUTSTANDING_PRODUCT_ENTRY, 0)))
                 / SUM (METRICS.INSPECTED))
             ELSE
                0
          END
             AS SS_ENTRY,
          CASE
             WHEN SUM (METRICS.INSPECTED) <> 0
             THEN
                (  (  SUM (COALESCE (METRICS.INSPECTED, 0))
                    - SUM (COALESCE (METRICS.OUTSTANDING_PRODUCT, 0)))
                 / SUM (METRICS.INSPECTED))
             ELSE
                0
          END
             AS SS
     FROM    GALADM.HMIN_PLAN_DATE_VW PLAN
          LEFT JOIN
             GALADM.HMIN_PP_METRICS_TBX METRICS
          ON     PLAN.PROCESS_POINT_ID = METRICS.PROCESS_POINT_ID
             AND PLAN.PRODUCTION_DATE = METRICS.PRODUCTION_DATE
   GROUP BY PLAN.SITE_NAME,
            PLAN.PLANT_NAME,
            PLAN.DIV_SEQ_NUM,
            PLAN.DIVISION_ID,
            PLAN.DIVISION_NAME,
            PLAN.DIVISION_DESCRIPTION,
            PLAN.PP_SEQ_NUM,
            PLAN.PROCESS_POINT_ID,
            PLAN.PROCESS_POINT_NAME,
            PLAN.PROCESS_POINT_DESCRIPTION,
            PLAN.TRACKING_POINT_FLAG,
            PLAN.PROCESS_POINT_TYPE,
            PLAN.PRODUCTION_DATE,
            PLAN.DATE_START_TIMESTAMP,
            PLAN.DATE_END_TIMESTAMP,
            PLAN.PLAN_QTY,
            PLAN.PLAN_QTY_NOW
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_PP_METRICS_DATE_VW IS
   'Plan, Actual and Quality Metrics calculated for all Process Points in GAL214TBX for all production dates in GAL226TBX';

CREATE VIEW GALADM.HMIN_PP_METRICS_SHIFT_VW
(
   SITE_NAME,
   PLANT_NAME,
   DIV_SEQ_NUM,
   DIVISION_ID,
   DIVISION_NAME,
   DIVISION_DESCRIPTION,
   PP_SEQ_NUM,
   PROCESS_POINT_ID,
   PROCESS_POINT_NAME,
   PROCESS_POINT_DESCRIPTION,
   TRACKING_POINT_FLAG,
   PROCESS_POINT_TYPE,
   PRODUCTION_DATE,
   SHIFT,
   SHIFT_START_TIMESTAMP,
   SHIFT_END_TIMESTAMP,
   PLAN_QTY,
   PLAN_QTY_NOW,
   PP_METRICS_ID,
   FIRST_PRODUCT_ID,
   LAST_PRODUCT_ID,
   INSPECTED,
   PLAN_ACTUAL_DIFF,
   DEFECTS_PRODUCT_ENTRY,
   DEFECTS_PRODUCT,
   DEFECTS_ENTRY,
   DEFECTS,
   OUTSTANDING_PRODUCT_ENTRY,
   OUTSTANDING_PRODUCT,
   REPAIRS_PRODUCT_ENTRY,
   SCRAP,
   DIRECT_PASSED,
   RPU_ENTRY,
   RPU,
   RSS_ENTRY,
   RSS,
   SS_ENTRY,
   SS
)
AS
   SELECT PLAN.SITE_NAME,
          PLAN.PLANT_NAME,
          PLAN.DIV_SEQ_NUM,
          PLAN.DIVISION_ID,
          PLAN.DIVISION_NAME,
          PLAN.DIVISION_DESCRIPTION,
          PLAN.PP_SEQ_NUM,
          PLAN.PROCESS_POINT_ID,
          PLAN.PROCESS_POINT_NAME,
          PLAN.PROCESS_POINT_DESCRIPTION,
          PLAN.TRACKING_POINT_FLAG,
          PLAN.PROCESS_POINT_TYPE,
          PLAN.PRODUCTION_DATE,
          PLAN.SHIFT,
          PLAN.SHIFT_START_TIMESTAMP,
          PLAN.SHIFT_END_TIMESTAMP,
          PLAN.PLAN_QTY,
          PLAN.PLAN_QTY_NOW,
          COALESCE (METRICS.PP_METRICS_ID, 0),
          COALESCE (METRICS.FIRST_PRODUCT_ID, ''),
          COALESCE (METRICS.LAST_PRODUCT_ID, ''),
          COALESCE (METRICS.INSPECTED, 0),
          (COALESCE (METRICS.INSPECTED, 0) - PLAN.PLAN_QTY_NOW)
             AS PLAN_ACTUAL_DIFF,
          COALESCE (METRICS.DEFECTS_PRODUCT_ENTRY, 0),
          COALESCE (METRICS.DEFECTS_PRODUCT, 0),
          COALESCE (METRICS.DEFECTS_ENTRY, 0),
          COALESCE (METRICS.DEFECTS, 0),
          COALESCE (METRICS.OUTSTANDING_PRODUCT_ENTRY, 0),
          COALESCE (METRICS.OUTSTANDING_PRODUCT, 0),
          COALESCE (METRICS.REPAIRS_PRODUCT_ENTRY, 0),
          COALESCE (METRICS.SCRAP, 0),
          (  COALESCE (METRICS.INSPECTED, 0)
           - COALESCE (METRICS.DEFECTS_PRODUCT_ENTRY, 0))
             AS DIRECT_PASSED,
          CASE
             WHEN METRICS.INSPECTED <> 0
             THEN
                (COALESCE (METRICS.DEFECTS_ENTRY, 0) / METRICS.INSPECTED)
             ELSE
                0
          END
             AS RPU_ENTRY,
          CASE
             WHEN METRICS.INSPECTED <> 0
             THEN
                (COALESCE (METRICS.DEFECTS, 0) / METRICS.INSPECTED)
             ELSE
                0
          END
             AS RPU,
          CASE
             WHEN METRICS.INSPECTED <> 0
             THEN
                (  (  COALESCE (METRICS.INSPECTED, 0)
                    - COALESCE (METRICS.DEFECTS_PRODUCT_ENTRY, 0))
                 / METRICS.INSPECTED)
             ELSE
                0
          END
             AS RSS_ENTRY,
          CASE
             WHEN METRICS.INSPECTED <> 0
             THEN
                (  (  COALESCE (METRICS.INSPECTED, 0)
                    - COALESCE (METRICS.DEFECTS_PRODUCT, 0))
                 / METRICS.INSPECTED)
             ELSE
                0
          END
             AS RSS,
          CASE
             WHEN METRICS.INSPECTED <> 0
             THEN
                (  (  COALESCE (METRICS.INSPECTED, 0)
                    - COALESCE (METRICS.OUTSTANDING_PRODUCT_ENTRY, 0))
                 / METRICS.INSPECTED)
             ELSE
                0
          END
             AS SS_ENTRY,
          CASE
             WHEN METRICS.INSPECTED <> 0
             THEN
                (  (  COALESCE (METRICS.INSPECTED, 0)
                    - COALESCE (METRICS.OUTSTANDING_PRODUCT, 0))
                 / METRICS.INSPECTED)
             ELSE
                0
          END
             AS SS
     FROM    GALADM.HMIN_PLAN_SHIFT_VW PLAN
          LEFT JOIN
             GALADM.HMIN_PP_METRICS_TBX METRICS
          ON     PLAN.PROCESS_POINT_ID = METRICS.PROCESS_POINT_ID
             AND PLAN.PRODUCTION_DATE = METRICS.PRODUCTION_DATE
             AND PLAN.SHIFT = METRICS.SHIFT
   WITH NO ROW MOVEMENT;

COMMENT ON TABLE GALADM.HMIN_PP_METRICS_SHIFT_VW IS
   'Plan, Actual and Quality Metrics calculated for all Process Points in GAL214TBX for all production dates and shifts in GAL226TBX';

CREATE VIEW GALADM.HMIN_CBU_HISTORY_VW
(
   PRODUCTION_DATE,
   SHIFT,
   SHIFT_END_TIMESTAMP,
   PRODUCT_ID,
   AF_OFF_TIMESTAMP,
   AF_OFF_DELAY_NOW,
   AF_OFF_DELAY_BUCKET,
   CBU_UNDER_24_HOURS,
   CBU_OVER_24_HOURS
)
AS
   (SELECT d.PRODUCTION_DATE,
           d.SHIFT,
           d.SHIFT_END_TIMESTAMP,
           D.PRODUCT_ID,
           D.AF_OFF_TIMESTAMP,
           D.AF_OFF_DELAY_NOW,
           CASE
              WHEN D.AF_OFF_DELAY_NOW <= 0 THEN 'IN PROC'
              WHEN D.AF_OFF_DELAY_NOW = 1 THEN '1 DAY'
              WHEN D.AF_OFF_DELAY_NOW = 2 THEN '2 DAYS'
              ELSE '3+ DAYS'
           END
              AS AF_OFF_DELAY_BUCKET,
           CASE WHEN D.AF_OFF_DELAY_NOW < 1 THEN 1 ELSE 0 END
              AS CBU_UNDER_24_HOURS,
           CASE WHEN D.AF_OFF_DELAY_NOW >= 1 THEN 1 ELSE 0 END
              AS CBU_OVER_24_HOURS
      FROM (SELECT s.PRODUCTION_DATE,
                   s.SHIFT,
                   S.SHIFT_END_TIMESTAMP,
                   Frame.product_id,
                   GALADM.HMIN_GET_PP_FIRST_TIMESTAMP_FUNC ('PP10088',
                                                            Frame.PRODUCT_ID)
                      AS AF_OFF_TIMESTAMP,
                   galadm.hmin_get_working_days_func (
                      'DIV3',
                      GALADM.HMIN_GET_PP_FIRST_TIMESTAMP_FUNC (
                         'PP10088',
                         Frame.PRODUCT_ID),
                      S.SHIFT_END_TIMESTAMP)
                      AS AF_OFF_DELAY_NOW
              FROM (SELECT s.PRODUCTION_DATE,
                           s.SHIFT,
                           max (s.END_TIMESTAMP) AS SHIFT_END_TIMESTAMP
                      FROM galadm.GAL128TBX d
                           LEFT JOIN galadm.gal238tbx g
                              ON d.DIVISION_ID = g.DIVISION_ID
                           LEFT JOIN galadm.GAL226TBX s
                              ON     g.GPCS_LINE_NO = s.LINE_NO
                                 AND g.GPCS_PLANT_CODE = s.PLANT_CODE
                                 AND g.GPCS_PROCESS_LOCATION =
                                        s.PROCESS_LOCATION
                     WHERE D.DIVISION_ID = 'DIV3'
                    GROUP BY s.PRODUCTION_DATE, s.SHIFT) S
                   LEFT JOIN GALADM.gal215tbx P
                      ON P.ACTUAL_TIMESTAMP < S.SHIFT_END_TIMESTAMP
                   LEFT JOIN GALADM.gal143tbx Frame
                      ON P.product_id = Frame.product_id
                   LEFT JOIN GALADM.GAL217TBX L
                      ON Frame.PRODUCTION_LOT = L.PRODUCTION_LOT
             WHERE     l.PLANT_CODE <> 'TEST'
                   AND Frame.tracking_status != 'LINE57'
                   AND Frame.tracking_status != 'LINE58'
                   AND Frame.PRODUCT_ID NOT IN
                          (SELECT VQ_SHIP.product_id
                             FROM GALADM.gal215tbx VQ_SHIP
                            WHERE     VQ_SHIP.process_point_id = 'PP10096'
                                  AND VQ_SHIP.ACTUAL_TIMESTAMP <
                                         S.SHIFT_END_TIMESTAMP)
                   AND P.process_point_id = 'PP10088') AS D);

COMMENT ON TABLE GALADM.HMIN_CBU_HISTORY_VW IS
   'Completed Body Units as of the Production Shift End Timestamp between AF OFF PP10088 and VQ SHIP PP10096 excluding production lot plant_code=TEST, tracking status LINE57 scrap or LINE58 exceptional';

CREATE VIEW GALADM.HMIN_CBU_INV_VW
(
   PRODUCT_ID,
   CBU_RESP_DEPT,
   AF_OFF_TIMESTAMP,
   AF_OFF_DELAY_NOW,
   AF_OFF_DELAY_BUCKET,
   CBU_UNDER_24_HOURS,
   CBU_OVER_24_HOURS
)
AS
   (SELECT D.PRODUCT_ID,
           D.CBU_RESP_DEPT,
           D.AF_OFF_TIMESTAMP,
           D.AF_OFF_DELAY_NOW,
           CASE
              WHEN D.AF_OFF_DELAY_NOW <= 0 THEN 'IN PROC'
              WHEN D.AF_OFF_DELAY_NOW = 1 THEN '1 DAY'
              WHEN D.AF_OFF_DELAY_NOW = 2 THEN '2 DAYS'
              ELSE '3+ DAYS'
           END
              AS AF_OFF_DELAY_BUCKET,
           CASE WHEN D.AF_OFF_DELAY_NOW < 1 THEN 1 ELSE 0 END
              AS CBU_UNDER_24_HOURS,
           CASE WHEN D.AF_OFF_DELAY_NOW >= 1 THEN 1 ELSE 0 END
              AS CBU_OVER_24_HOURS
      FROM (SELECT Frame.product_id,
                   COALESCE (R.RESPONSIBLE_DEPT, 'IN PROC') AS CBU_RESP_DEPT,
                   GALADM.HMIN_GET_PP_FIRST_TIMESTAMP_FUNC ('PP10088',
                                                            Frame.PRODUCT_ID)
                      AS AF_OFF_TIMESTAMP,
                   galadm.hmin_get_working_days_func (
                      'DIV3',
                      GALADM.HMIN_GET_PP_FIRST_TIMESTAMP_FUNC (
                         'PP10088',
                         Frame.PRODUCT_ID),
                      CURRENT_TIMESTAMP)
                      AS AF_OFF_DELAY_NOW
              FROM GALADM.gal143tbx Frame
                   LEFT JOIN GALADM.gal215tbx P
                      ON P.product_id = Frame.product_id
                   LEFT JOIN GALADM.GAL217TBX L
                      ON Frame.PRODUCTION_LOT = L.PRODUCTION_LOT
                   LEFT JOIN GALADM.GAL177TBX R
                      ON P.PRODUCT_ID = R.PRODUCT_ID
             WHERE     l.PLANT_CODE <> 'TEST'
                   AND Frame.tracking_status != 'LINE57'
                   AND Frame.tracking_status != 'LINE58'
                   AND Frame.PRODUCT_ID NOT IN
                          (SELECT VQ_SHIP.product_id
                             FROM GALADM.gal215tbx VQ_SHIP
                            WHERE process_point_id = 'PP10096')
                   AND P.process_point_id = 'PP10088') AS D);

COMMENT ON TABLE GALADM.HMIN_CBU_INV_VW IS
   'Current Completed Body Units between AF OFF PP10088 and VQ SHIP PP10096 excluding production lot plant_code=TEST, tracking status LINE57 scrap or LINE58 exceptional';

CREATE VIEW GALADM.HMIN_VIN_VW
(
   VIN,
   ENGINE_SERIAL_NO,
   MISSION_SERIAL_NO,
   KEY_NO,
   SHORT_VIN,
   PRODUCTION_LOT,
   KD_LOT_NUMBER,
   PRODUCT_SPEC_CODE,
   PLAN_OFF_DATE,
   TRACKING_STATUS,
   PRODUCT_START_DATE,
   ACTUAL_OFF_DATE,
   AUTO_HOLD_STATUS,
   PRODUCTION_DATE,
   LAST_PASSING_PROCESS_POINT_ID,
   ENGINE_STATUS,
   AF_ON_SEQUENCE_NUMBER,
   ACTUAL_MISSION_TYPE
)
AS
   SELECT trim (t.PRODUCT_ID) AS VIN,
          trim (t.ENGINE_SERIAL_NO) AS ENGINE_SERIAL_NO,
          trim (t.MISSION_SERIAL_NO) AS MISSION_SERIAL_NO,
          trim (t.KEY_NO) AS KEY_NO,
          trim (t.SHORT_VIN) AS SHORT_VIN,
          trim (t.PRODUCTION_LOT) AS PRODUCTION_LOT,
          trim (t.KD_LOT_NUMBER) AS KD_LOT_NUMBER,
          trim (t.PRODUCT_SPEC_CODE) AS PRODUCT_SPEC_CODE,
          t.PLAN_OFF_DATE,
          trim (coalesce (t.TRACKING_STATUS, 'LINE146')) AS TRACKING_STATUS,
          t.PRODUCT_START_DATE,
          t.ACTUAL_OFF_DATE,
          t.AUTO_HOLD_STATUS,
          t.PRODUCTION_DATE,
          trim (t.LAST_PASSING_PROCESS_POINT_ID)
             AS LAST_PASSING_PROCESS_POINT_ID,
          t.ENGINE_STATUS,
          t.AF_ON_SEQUENCE_NUMBER,
          trim (t.ACTUAL_MISSION_TYPE) AS ACTUAL_MISSION_TYPE
     FROM    GALADM.gal143tbx t
          LEFT JOIN
             galadm.gal217tbx l
          ON t.PRODUCTION_LOT = l.PRODUCTION_LOT
    WHERE l.PLANT_CODE <> 'TEST';

COMMENT ON TABLE "GALADM"."HMIN_VIN_VW" IS
   'GAL143TBX with vins filtered from GAL217TBX Production Lot with Plant_Code<>TEST';

CREATE VIEW GALADM.HMIN_AHM_SHIP_STATUS_VW
(
   VIN,
   SHIP_STATUS_TIMESTAMP,
   SHIP_STATUS,
   SHIP_STATUS_NAME,
   SHIP_STATUS_DESC,
   ON_TIME_SHIPPING,
   NOT_ON_TIME_SHIPPING
)
AS
   SELECT S.VIN,
          S.ACTUAL_TIMESTAMP AS SHIP_STATUS_TIMESTAMP,
          S.STATUS AS SHIP_STATUS,
          T.SHIP_STATUS_NAME,
          T.SHIP_STATUS_DESC,
          S.ON_TIME_SHIPPING,
          CASE WHEN S.ON_TIME_SHIPPING = 0 THEN 1 ELSE 0 END
             AS NOT_ON_TIME_SHIPPING
     FROM GALADM.GAL263TBX S
          LEFT JOIN GALADM.HMIN_AH_SHIP_STATUS_TBX T
             ON S.STATUS = T.STATUS
          LEFT JOIN GALADM.GAL143TBX F
             ON s.VIN = f.PRODUCT_ID
          LEFT JOIN GALADM.GAL217TBX L
             ON F.PRODUCTION_LOT = L.PRODUCTION_LOT
    WHERE l.PLAN_CODE <> 'TEST';

COMMENT ON TABLE "GALADM"."HMIN_AHM_SHIP_STATUS_VW" IS
   'American Honda Status by VIN';