package com.honda.galc.client.datacollection.processor;


import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.KnuckleBarMeasurementDao;
import com.honda.galc.dao.product.MeasurementAttemptDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.KnuckleBarMeasurement;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementAttempt;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.DeleteEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.InsertEntity;
import com.honda.galc.entitypersister.UpdateEntity;
import com.honda.galc.property.PartSerialMatchCheckPairPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/** * * 
* @version  
* @author Gangadhararao Gadde 
* @since Sept  02, 2016
* Knuckle Vin Marriage - Confirm Part SN Matches previously installed part
*/
public class KnuckleBarVinMarriageProcessor extends PartSerialNumberProcessor {



	private String ENTITIES_FOR = "KNUCKLEBARVINMARRIAGE";
	
	public KnuckleBarVinMarriageProcessor(ClientContext context) {
		super(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		checkPartSerialNumber(partnumber);

		if(isCheckDuplicatePart())
			checkDuplicatePart(partnumber.getPartSn());

		installedPart.setValidPartSerialNumber(true);
		EntityList<AbstractEntity> entityList = loadEntityList(partnumber);
		
		if(entityList.size()>0){
			getController().getState().getProduct().setMasterEntityList(entityList);
		}else{
			handleException("No Knuckle Bar Measurement(s) found for the Part Serial Number : " + partnumber.getPartSn());
		}
		
		entityList = null;
		return true;
		
	}

	/**
	 * Check part serial number against lot control rule part mask
	 * @param partnumber 
	 * 
	 * @return part with matching mask 
	 *         if no matching mask then return null
	 */
	@Override
	protected void checkPartSerialNumber(PartSerialNumber partnumber) {
		
		checkPartSerialNumberIsNotNull(partnumber);
		installedPart.setPartSerialNumber(partnumber.getPartSn());
		
		checkPartSerialNumberMask();
	}

	private void checkPartSerialNumberIsNotNull(PartSerialNumber partnumber) {
		if(partnumber == null || partnumber.getPartSn() == null)
			handleException("Received part serial number is null!");
		
	}
	
	/*
	 * loadEntityList in KnuckleBarVinMarriageProcessor does the following...
	 * 
	 * 1. Inserts a new row in GAL198_HIST_TBX if isSaveMeasurementHistory is true
	 * 2. If record is already present in GAL198TBX for Product id, Part Name & Measurement Seq No combination then update 
	 * if not insert new part name for new product id , Measurement Seq No
	 * 3. Delete records from GAL328TBX (KnuckleBarMeasurement) 
	 * 4. Insert or update GAL185TBX (Installed parts)
	 * */
	@SuppressWarnings("unchecked")
	private EntityList loadEntityList(PartSerialNumber partnumber){
		
		String productId = getController().getState().getProductId().trim();

		EntityList<AbstractEntity> entityList = new EntityList<AbstractEntity>(ENTITIES_FOR, productId, partnumber.getPartSn());
		
		KnuckleBarMeasurementDao knuckleBarMeasurementsDao = ServiceFactory.getDao(KnuckleBarMeasurementDao.class);
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		List distinctPartNameIdList = knuckleBarMeasurementsDao.findDistPartNameId(partnumber.getPartSn());
		Iterator distinctPartNameIdItr = (Iterator)distinctPartNameIdList.iterator();
		
 		while (distinctPartNameIdItr.hasNext()){
 			Object[] distinctPartNameId = (Object[])distinctPartNameIdItr.next();

 			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
 			int partStatus = 0;
 			int count = 0;
 			int measurementStatusCount = 0;
 			
 			List<KnuckleBarMeasurement> knuckleBarMeasurementsLst = knuckleBarMeasurementsDao.getMeasurementForNameSlNo((String)distinctPartNameId[0], partnumber.getPartSn());
 			Iterator<KnuckleBarMeasurement> knuckleBarMeasurementsItr = (Iterator<KnuckleBarMeasurement>) knuckleBarMeasurementsLst.iterator();
 			
	 			while(knuckleBarMeasurementsItr.hasNext()){
	 				
	 				count++;
	 				Map<String, String> partSerialMatchCheckPair =PropertyService.getPropertyBean(PartSerialMatchCheckPairPropertyBean.class, context.getProcessPointId()).getPartSerialMatchCheckPair();
	 				if(partSerialMatchCheckPair!=null && partSerialMatchCheckPair.get((String)distinctPartNameId[0])!=null)
	 				{
	 					InstalledPart previousSerialNumScanPart=installedPartDao.findById(productId, partSerialMatchCheckPair.get((String)distinctPartNameId[0]).trim());
	 					if(previousSerialNumScanPart==null|| previousSerialNumScanPart.getPartSerialNumber()==null || !(previousSerialNumScanPart.getPartSerialNumber().trim().equals(partnumber.getPartSn().trim())))
	 					{
	 						handleException("SerialNumberScan for Part:"+partSerialMatchCheckPair.get((String)distinctPartNameId[0]).trim()+" is missing or does not match");
	 					}
	 				}
	 				
	 				KnuckleBarMeasurement knuckleBarMeasurement =(KnuckleBarMeasurement)knuckleBarMeasurementsItr.next();
	 				MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);

	 				measurementStatusCount += knuckleBarMeasurement.getMeasurementStatus();
	 				
	 				//Check whether the part name for product id is exist in GAL198TBX
	 				MeasurementId measurementId = new MeasurementId(productId, knuckleBarMeasurement.getId().getPartName(), knuckleBarMeasurement.getId().getMeasurementSequenceNumber());
	 				Measurement measurement = measurementDao.findByKey(measurementId);
	 				
	 				if(measurement != null){
	 					//update existing row in GAL198TBX
	 					entityList.addEntity(new UpdateEntity(constructMeasurement(measurement, knuckleBarMeasurement, timeStamp, partnumber, false), measurement.toString(),measurementDao));
	 				}else{
		 				//Add new record in GAL198TBX
		 				Measurement newMeasurement = new Measurement(productId, knuckleBarMeasurement.getId().getPartName().trim(),knuckleBarMeasurement.getId().getMeasurementSequenceNumber());
		 				entityList.addEntity(new InsertEntity(constructMeasurement(newMeasurement, knuckleBarMeasurement, timeStamp, partnumber, true), newMeasurement.toString(),measurementDao));
		 			}	 					

	 				// insert record in MeasurementAttempt (GAL198_HIST_TBX) if isSaveMeasurementHistory is true
	 				if(getController().getProperty().isSaveMeasurementHistory())
	 					insertMeasurementAttempt(entityList, productId, knuckleBarMeasurement, timeStamp, partnumber);
	 				
	 				// remove GAL328TBX record 
	 				entityList.addEntity(new DeleteEntity((Object)knuckleBarMeasurement, knuckleBarMeasurement.toString(),knuckleBarMeasurementsDao));
	 				 				
	 				partStatus = (count == measurementStatusCount)? 1 : 0;
	 				knuckleBarMeasurement = null;
	 			}
 				
 				//Create or update Installed part (GAL185TBX)
 				
 				InstalledPartId installedPartId = new InstalledPartId(productId, (String)distinctPartNameId[0]);
 				InstalledPart installedPart = installedPartDao.findByKey(installedPartId);
 				
 				if(installedPart != null){
 					// update the existing record in GAL185TBX
 					entityList.addEntity(new UpdateEntity(constructInstalledPart(installedPart, timeStamp, partnumber, false, partStatus, distinctPartNameId), installedPart.toString(),installedPartDao));
 				}else{
 					// Create new record in GAL185TBX
 					InstalledPart  newInstalledPart = new InstalledPart(installedPartId);
 					entityList.addEntity(new InsertEntity(constructInstalledPart(newInstalledPart, timeStamp, partnumber, true, partStatus, distinctPartNameId), newInstalledPart.toString(),installedPartDao));
 					newInstalledPart = null;
 				}
			installedPart = null;
			distinctPartNameId = null;
			installedPartId = null;
 		}
 		distinctPartNameIdList = null;
 		distinctPartNameIdItr = null;
 		
 		return entityList;
	}
	
	
	private void insertMeasurementAttempt(EntityList<AbstractEntity> entityList , String productId, KnuckleBarMeasurement knuckleBarMeasurements, Timestamp timeStamp, PartSerialNumber partnumber){
			MeasurementAttemptDao attemptDao = ServiceFactory.getDao(MeasurementAttemptDao.class);
				int maxCount = attemptDao.getMaxAttemptForMeasurement(productId, knuckleBarMeasurements.getId().getPartName().trim(), knuckleBarMeasurements.getId().getMeasurementSequenceNumber());

			MeasurementAttempt newMeasurementAttempt = new MeasurementAttempt(productId, knuckleBarMeasurements.getId().getPartName(), knuckleBarMeasurements.getId().getMeasurementSequenceNumber(), maxCount);
			newMeasurementAttempt.setMeasurementValue(knuckleBarMeasurements.getMeasurementValue());
			newMeasurementAttempt.setMeasurementStatus(MeasurementStatus.getType(knuckleBarMeasurements.getMeasurementStatus()));
			newMeasurementAttempt.setMeasurementAngle(knuckleBarMeasurements.getMeasurementAngle());
			newMeasurementAttempt.setPartSerialNumber(partnumber.getPartSn());
			entityList.addEntity(new InsertEntity((Object)newMeasurementAttempt, newMeasurementAttempt.toString(),attemptDao));
			attemptDao = null;
	}
	
	private Object constructMeasurement(Measurement measurement, KnuckleBarMeasurement knuckleBarMeasurements, Timestamp timeStamp, PartSerialNumber partnumber, boolean isNew){
		
			measurement.setMeasurementValue(knuckleBarMeasurements.getMeasurementValue());
			measurement.setMeasurementStatus(MeasurementStatus.getType(knuckleBarMeasurements.getMeasurementStatus()));
			measurement.setMeasurementAngle(knuckleBarMeasurements.getMeasurementAngle());
			measurement.setPartSerialNumber(partnumber.getPartSn());
			measurement.setActualTimestamp(timeStamp);
			
			if(!isNew)
				measurement.setUpdateTimestamp(timeStamp);
			
			return (Object)measurement;
	}
	
	private Object constructInstalledPart(InstalledPart  installedPart, Timestamp timeStamp, PartSerialNumber partnumber, boolean isNewPart, int partStatus, Object[] distinctPartNameId){
		
			installedPart.setPartSerialNumber(partnumber.getPartSn());
			installedPart.setActualTimestamp(timeStamp);
			installedPart.setPartId((String)distinctPartNameId[1]);
			installedPart.setInstalledPartStatus(InstalledPartStatus.getType(partStatus));

			if(isNewPart){
				installedPart.setPassTime(0);
				installedPart.setInstalledPartReason(" ");
				installedPart.setAssociateNo(null);
				installedPart.setFirstAlarm(0);
				installedPart.setSecondAlarm(0);				
			}
		
			return (Object)installedPart;
	}
}
