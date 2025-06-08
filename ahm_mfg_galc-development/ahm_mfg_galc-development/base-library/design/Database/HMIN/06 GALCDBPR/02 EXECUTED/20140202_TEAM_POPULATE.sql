select * from GALADM.TEAM_ROTATION_TBX;

--The following is for specific date ranges since the rotation changes with each shutdown period
INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,'A' as TEAM
FROM GALADM.GAL226TBX SCHED
WHERE SCHED.PROCESS_LOCATION='AF'
and SCHED.PRODUCTION_DATE between '01/01/2007' and '10/22/2011'
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (0,1)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM GALADM.GAL226TBX SCHED
WHERE SCHED.PROCESS_LOCATION='AF'
and SCHED.PRODUCTION_DATE between '10/23/2011' and '12/31/2011'
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (0,3)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM GALADM.GAL226TBX SCHED
WHERE SCHED.PROCESS_LOCATION='AF'
and SCHED.PRODUCTION_DATE between '1/1/2012' and '7/7/2012'
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (0,1)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM GALADM.GAL226TBX SCHED
WHERE SCHED.PROCESS_LOCATION='AF'
and SCHED.PRODUCTION_DATE between '7/8/2012' and '12/29/2012'
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (1,2)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM GALADM.GAL226TBX SCHED
WHERE SCHED.PROCESS_LOCATION='AF'
and SCHED.PRODUCTION_DATE between '12/30/2012' and '7/6/2013'
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (2,3)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM GALADM.GAL226TBX SCHED
WHERE SCHED.PROCESS_LOCATION='AF'
and SCHED.PRODUCTION_DATE between '7/7/2013' and '1/4/2014'
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (0,3)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM GALADM.GAL226TBX SCHED
WHERE SCHED.PROCESS_LOCATION='AF'
and SCHED.PRODUCTION_DATE between '1/5/2014' and '7/4/2014'
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

--Need to create a query that populates future dates for when we don't have GAL226TBX records
INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (0,3)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM 
(SELECT *
FROM
(SELECT 
       LINE_NO,
       PROCESS_LOCATION,
       PLANT_CODE,
       CAST('1/5/2014' AS DATE) -1 DAY + (ROW_NUMBER () OVER ()) DAYS AS PRODUCTION_DATE
  FROM GALADM.GAL226TBX SCHED
 WHERE   SCHED.LINE_NO='01' AND  SCHED.PROCESS_LOCATION = 'AF' AND SCHED.PLANT_CODE='HMI'
GROUP BY LINE_NO,
         PROCESS_LOCATION,
         PLANT_CODE,
         PRODUCTION_DATE
FETCH FIRST 1000 ROWS ONLY) PROD_DATES
,
  (SELECT '01' AS SHIFT FROM SYSIBM.SYSDUMMY1
         UNION ALL
         SELECT '02' AS SHIFT FROM SYSIBM.SYSDUMMY1
         UNION ALL
         SELECT '03' AS SHIFT FROM SYSIBM.SYSDUMMY1) SHIFTS
WHERE PRODUCTION_DATE BETWEEN '1/5/2014' and '7/5/2014') SCHED_FUTURE
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;

INSERT INTO GALADM.TEAM_ROTATION_TBX (LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT, TEAM)(
Select teamcalc.*
from (
SELECT 
LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
,case when mod(cast(year(production_date)||right('00'||week(production_date),2) as integer),4) in (0,1)
  then CASE WHEN shift in ('01','03')
   THEN 'A'
   ELSE 'B'
   END
  else CASE WHEN shift in ('01','03')
   THEN 'B'
   ELSE 'A'
   END
 END as TEAM
FROM 
(SELECT *
FROM
(SELECT 
       LINE_NO,
       PROCESS_LOCATION,
       PLANT_CODE,
       CAST('7/6/2014' AS DATE) -1 DAY + (ROW_NUMBER () OVER ()) DAYS AS PRODUCTION_DATE
  FROM GALADM.GAL226TBX SCHED
 WHERE   SCHED.LINE_NO='01' AND  SCHED.PROCESS_LOCATION = 'AF' AND SCHED.PLANT_CODE='HMI'
GROUP BY LINE_NO,
         PROCESS_LOCATION,
         PLANT_CODE,
         PRODUCTION_DATE
FETCH FIRST 1000 ROWS ONLY) PROD_DATES
,
  (SELECT '01' AS SHIFT FROM SYSIBM.SYSDUMMY1
         UNION ALL
         SELECT '02' AS SHIFT FROM SYSIBM.SYSDUMMY1
         UNION ALL
         SELECT '03' AS SHIFT FROM SYSIBM.SYSDUMMY1) SHIFTS
WHERE PRODUCTION_DATE BETWEEN '7/6/2014' and '1/3/2015') SCHED_FUTURE
GROUP BY LINE_NO, PROCESS_LOCATION, PLANT_CODE, PRODUCTION_DATE, SHIFT
) as teamcalc
left join GALADM.TEAM_ROTATION_TBX teamreal
on teamcalc.LINE_NO = teamreal.LINE_NO and teamcalc.PLANT_CODE = teamreal.PLANT_CODE and teamcalc.PROCESS_LOCATION = teamreal.PROCESS_LOCATION and teamcalc.PRODUCTION_DATE = teamreal.PRODUCTION_DATE and teamcalc.SHIFT = teamreal.SHIFT
where teamreal.TEAM is null
)
;