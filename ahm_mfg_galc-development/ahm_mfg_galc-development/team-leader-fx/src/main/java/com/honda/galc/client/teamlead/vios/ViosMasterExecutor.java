package com.honda.galc.client.teamlead.vios;

public interface ViosMasterExecutor<E> {
	public String doValidation(E entity);
	
	public void uploadEntity (E entity, String viosPlatform, String userId)throws Exception;
}
