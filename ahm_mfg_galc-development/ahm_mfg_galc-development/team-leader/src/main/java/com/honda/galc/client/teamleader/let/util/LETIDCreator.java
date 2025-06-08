
package com.honda.galc.client.teamleader.let.util;



import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.entity.product.IdCreate;


public class LETIDCreator {

	public LETIDCreator() {

	}

	public int createID(String tableName, String columnName)
	{
		int currentId=0;
		try {     	
			int idCreate = getDao(IdCreateDao.class).incrementAndGetStatus(tableName, columnName);  
			if (idCreate == 0) {
				Logger.getLogger().error("Could not update the current id in GAL226TBX");

			}
			IdCreate idCreateEntity = getDao(IdCreateDao.class).getIdCreate(tableName, columnName);
			if (idCreateEntity!=null)
				currentId= (idCreateEntity.getCurrentId()-1);
			else
			{
				Logger.getLogger().error("Could not find the table name in GAL226TBX"); 
			}

		} catch (Exception e) {
			Logger.getLogger().error(e,"An error Occurred while processing the LET Inspection Program Data");
			e.printStackTrace();
		}
		return currentId;
	}
}
