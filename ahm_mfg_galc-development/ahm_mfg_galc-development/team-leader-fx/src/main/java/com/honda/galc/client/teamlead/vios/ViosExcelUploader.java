package com.honda.galc.client.teamlead.vios;

public class ViosExcelUploader<E> extends DefaultExcelUploader<E> {
	private String viosPlatform;
	private String userId;
	
	public ViosExcelUploader(String viosPlatform, String userId) {
		super();
		this.viosPlatform = viosPlatform;
		this.userId = userId;
	}

	public String getViosPlatform() {
		return viosPlatform;
	}

	public void setViosPlatform(String viosPlatform) {
		this.viosPlatform = viosPlatform;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String doValidation(E entity) {
		if(ViosExcelUploadUtility.getInstance().get(entity)!=null) {
			return ViosExcelUploadUtility.getInstance().get(entity).doValidation(entity);
		}
		return "No Excel Uploader Executor found!";
	}
	
	@Override
	public void uploadEntity(E entity) throws Exception {
			ViosExcelUploadUtility.getInstance().get(entity).uploadEntity(entity, getViosPlatform(), getUserId());
		}
	}

