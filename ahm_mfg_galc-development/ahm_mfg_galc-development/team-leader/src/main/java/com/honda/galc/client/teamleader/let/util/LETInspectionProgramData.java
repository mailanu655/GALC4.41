
package com.honda.galc.client.teamleader.let.util;

import static com.honda.galc.service.ServiceFactory.getDao;


import java.util.*;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.entity.product.LetInspectionProgram;


public class LETInspectionProgramData {

	private static LETInspectionProgramData self;

	private Hashtable htPgmData;


	private LETInspectionProgramData() {
		this.getInspectionProgramData();
	}

	public static synchronized LETInspectionProgramData getInstance()
	{
		if (self == null) {
			self = new LETInspectionProgramData();
		}
		return self;
	}

	private void getInspectionProgramData()
	{
		try {
			htPgmData = new Hashtable();         
			List<LetInspectionProgram> letProgramList = getDao(LetInspectionProgramDao.class).findAll();           
			for(LetInspectionProgram  letProgram:letProgramList)
			{
				htPgmData.put(letProgram.getInspectionPgmName(), String.valueOf(letProgram.getInspectionPgmId()));
			}
		}  catch (Exception e) {
			Logger.getLogger().error(e,"An error Occurred while processing the LET Inspection Program Data");
			e.printStackTrace();
		} 
	}

	public synchronized String getInspectionPgmId(String strName)
	{
		if (htPgmData.get(strName) == null) {
			this.insertGAL714TBX(strName);
		}

		return (String) htPgmData.get(strName);
	}

	private void insertGAL714TBX(String strName)  {
		

		try {
			LETIDCreator idCreator = new LETIDCreator();
			int itId = idCreator.createID("GAL714TBX", "INSPECTION_PGM_ID");
		   LetInspectionProgram program=new LetInspectionProgram();
			program.setInspectionPgmId(itId);
			program.setInspectionPgmName(strName);
		    getDao(LetInspectionProgramDao.class).save(program); 
			this.htPgmData.put(strName, String.valueOf(itId));

		}  catch (Exception e) {

			Logger.getLogger().error(e,"An error Occurred while processing the LET Inspection Program Data");
			e.printStackTrace();
		} 

	}

	public synchronized void discard() {
		self = null;
	}
}
