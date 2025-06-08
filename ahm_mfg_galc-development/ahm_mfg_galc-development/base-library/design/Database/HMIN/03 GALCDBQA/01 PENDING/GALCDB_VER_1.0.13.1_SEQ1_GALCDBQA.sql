DROP TRIGGER GALADM.GAL198_TR3@

CREATE TRIGGER GALADM.GAL198_TR3
   AFTER INSERT
   ON GALADM.GAL198TBX
   REFERENCING NEW AS A
   FOR EACH ROW
BEGIN
   DECLARE ATTEMPT   INTEGER;--
   SET ATTEMPT =
          (SELECT COALESCE (COUNT (*), 0) + 1
             FROM GALADM.HMIN_MEASUREMENT_ATTEMPTTBX
            WHERE     PRODUCT_ID = A.PRODUCT_ID
                  AND PART_NAME = A.PART_NAME
                  AND MEASUREMENT_SEQUENCE_NUMBER =
                         A.MEASUREMENT_SEQUENCE_NUMBER);--

   UPDATE GALADM.HMIN_MEASUREMENT_ATTEMPTTBX
      SET LAST_ATTEMPT = 0
    WHERE     PRODUCT_ID = A.PRODUCT_ID
          AND PART_NAME = A.PART_NAME
          AND MEASUREMENT_SEQUENCE_NUMBER = A.MEASUREMENT_SEQUENCE_NUMBER
          AND LAST_ATTEMPT = 1;--

   INSERT
     INTO GALADM.HMIN_MEASUREMENT_ATTEMPTTBX (PRODUCT_ID,
                                              PART_NAME,
                                              MEASUREMENT_SEQUENCE_NUMBER,
                                              MEASUREMENT_ATTEMPT,
                                              MEASUREMENT_VALUE,
                                              MEASUREMENT_ANGLE,
                                              MEASUREMENT_STATUS,
                                              PART_SERIAL_NUMBER,
                                              LAST_ATTEMPT,
                                              ACTUAL_TIMESTAMP)
   VALUES (A.PRODUCT_ID,
           A.PART_NAME,
           A.MEASUREMENT_SEQUENCE_NUMBER,
           ATTEMPT,
           A.MEASUREMENT_VALUE,
           A.MEASUREMENT_ANGLE,
           A.MEASUREMENT_STATUS,
           A.PART_SERIAL_NUMBER,
           1,
           A.ACTUAL_TIMESTAMP);--
END@

DROP TRIGGER GALADM.GAL198_TR4@

CREATE TRIGGER GALADM.GAL198_TR4
   AFTER UPDATE
   ON GALADM.GAL198TBX
   REFERENCING NEW AS A
   FOR EACH ROW
BEGIN
   DECLARE ATTEMPT   INTEGER;--
   SET ATTEMPT =
          (SELECT COALESCE (COUNT (*), 0) + 1
             FROM GALADM.HMIN_MEASUREMENT_ATTEMPTTBX
            WHERE     PRODUCT_ID = A.PRODUCT_ID
                  AND PART_NAME = A.PART_NAME
                  AND MEASUREMENT_SEQUENCE_NUMBER =
                         A.MEASUREMENT_SEQUENCE_NUMBER);--

   UPDATE GALADM.HMIN_MEASUREMENT_ATTEMPTTBX
      SET LAST_ATTEMPT = 0
    WHERE     PRODUCT_ID = A.PRODUCT_ID
          AND PART_NAME = A.PART_NAME
          AND MEASUREMENT_SEQUENCE_NUMBER = A.MEASUREMENT_SEQUENCE_NUMBER
          AND LAST_ATTEMPT = 1;--

   INSERT
     INTO GALADM.HMIN_MEASUREMENT_ATTEMPTTBX (PRODUCT_ID,
                                              PART_NAME,
                                              MEASUREMENT_SEQUENCE_NUMBER,
                                              MEASUREMENT_ATTEMPT,
                                              MEASUREMENT_VALUE,
                                              MEASUREMENT_ANGLE,
                                              MEASUREMENT_STATUS,
                                              PART_SERIAL_NUMBER,
                                              LAST_ATTEMPT,
                                              ACTUAL_TIMESTAMP)
   VALUES (A.PRODUCT_ID,
           A.PART_NAME,
           A.MEASUREMENT_SEQUENCE_NUMBER,
           ATTEMPT,
           A.MEASUREMENT_VALUE,
           A.MEASUREMENT_ANGLE,
           A.MEASUREMENT_STATUS,
           A.PART_SERIAL_NUMBER,
           1,
           A.ACTUAL_TIMESTAMP);--
END@