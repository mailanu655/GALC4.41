package com.honda.galc.client.teamlead.vios;

import java.util.List;

public interface IExcelUploader<E> {
	public String doUpload(List<E> entityList);
	public String doValidation(E entity);
	public void uploadEntity(E entity) throws Exception;
}
