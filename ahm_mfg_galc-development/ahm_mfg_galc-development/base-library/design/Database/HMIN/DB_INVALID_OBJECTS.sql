SELECT * FROM SYSCAT.INVALIDOBJECTS;

SELECT 
'DROP TRIGGER '||TRIM(TR.TRIGSCHEMA)||'.'||TR.TRIGNAME||';'
FROM SYSCAT.TRIGGERS TR
WHERE TR.VALID='X';

SELECT *
FROM SYSCAT.TABLES
WHERE STATUS='X';

--Use this to compare tables between databases
select tabname, colname,typename, length, scale, default, nulls 
from syscat.columns   
where tabschema = 'GALADM' 
order by tabname, colname;

--Use this to compare triggers between databases
select tr.TRIGNAME,tr.TRIGTIME,tr.TRIGEVENT,tr.TEXT
from SYSCAT.TRIGGERS tr
where tr.TRIGSCHEMA='GALADM'
order by tr.TRIGNAME;