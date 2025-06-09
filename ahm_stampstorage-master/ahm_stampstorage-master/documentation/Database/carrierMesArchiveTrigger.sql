CREATE TRIGGER Carrier_Mes_Archive_Trg AFTER UPDATE ON CARRIER_MES_TBX 
REFERENCING OLD as OLDROW NEW as NEWROW 
 FOR EACH ROW MODE DB2SQL
INSERT INTO CARRIER_MES_ARCHIVE_TBX 
(buffer, carriernumber, currentlocation, destination, originationlocation, dienumber, quantity, tagID, status, productionrundate, productionrunnumber, updatedate)  
values( OLDROW.buffer,OLDROW.carriernumber, OLDROW.currentlocation, OLDROW.destination, OLDROW.originationlocation,OLDROW.dienumber,OLDROW. quantity, OLDROW.tagID, 
OLDROW.status, OLDROW.productionrundate, OLDROW.productionrunnumber, OLDROW.updatedate);