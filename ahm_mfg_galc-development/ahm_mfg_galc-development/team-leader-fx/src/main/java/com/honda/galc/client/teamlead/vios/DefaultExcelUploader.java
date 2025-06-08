package com.honda.galc.client.teamlead.vios;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;

public abstract class DefaultExcelUploader<E> implements IExcelUploader<E> {

	@Override
	public String doUpload(List<E> entityList) {
		StringBuffer infoLog = new StringBuffer();
		int recordSaved = 0;
		int recordSkipped = 0;
		for (E entity : entityList) {
			try {
				
				String validationStatus = doValidation(entity);
				if(StringUtils.isNotBlank(validationStatus)) {
					infoLog.append(validationStatus+"\n");
					recordSkipped++;
				} else {
					try {
						uploadEntity(entity);
						recordSaved++;
					} catch (Exception e) {
						recordSkipped++;
					}
				}
				
			} catch (Exception e) {
				infoLog.append("Record skipped: " + entity.toString() + ": " + e.getMessage() + "\n");
				recordSkipped++;
				Logger.getLogger().error(e, new LogRecord("Record skipped: " + entity.toString()));
			}
		}
		infoLog.append("\n");
		infoLog.append("No of Records Saved: " + recordSaved + "\n");
		infoLog.append("No of Records Skipped: " + recordSkipped + "\n");
		return infoLog.toString();
	}

	@Override
	public abstract String doValidation(E entity);

	@Override
	public abstract void uploadEntity(E entity) throws Exception;

}
