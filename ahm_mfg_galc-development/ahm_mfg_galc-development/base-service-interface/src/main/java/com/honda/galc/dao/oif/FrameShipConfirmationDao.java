package com.honda.galc.dao.oif;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.dto.oif.FrameShipConfirmationDTO;
import com.honda.galc.entity.oif.FrameShipConfirmation;
import com.honda.galc.entity.oif.FrameShipConfirmationId;
import com.honda.galc.service.IDaoService;

public interface FrameShipConfirmationDao extends IDaoService<FrameShipConfirmation, FrameShipConfirmationId> {
	
	/**
	 * Delete records that have an date less that the parameter
	 * @param filterDate	-	date that is used as filter for delete old records 
	 * @return
	 */
	public Integer deleteByDate (final Timestamp filterDate, final String flag);
	
	
	/**
	 * insert the records that don't exist in AEP_STAT_OUT_TBX from two days before to the current date 
	 * @param processPoints		-	this is the list of process point for get information from product history
	 * @param dateTime			-	this is the time stamp with two days before 
	 * @param plantCodeForAEP	-	this is the plant code for the AEP plant
	 */
	public void insertRecordInexistent(final String[] processPoints, final Timestamp dateTime, final String plantCodeForAEP, final String recordValue,final int frameOption);
	
	/**
	 * return all records by sent_flag
	 * @param sentFlag		-	the status of sent flag
	 * @param plantCodeAep	-	the code for aep plant
	 * @return
	 */
	public List<FrameShipConfirmationDTO> selectByFlag(final String sentFlag, final String recordType, final String plantCodeAep);

}
