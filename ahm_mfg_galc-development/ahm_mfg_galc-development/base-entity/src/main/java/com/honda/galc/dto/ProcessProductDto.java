package com.honda.galc.dto;

public class ProcessProductDto implements IDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DtoTag(name = "SEQUENCE_NUMBER")
	private Object sequence;
	
	@DtoTag(name = "PRODUCT_ID")
	private String vin;
	
	@DtoTag(name = "PRODUCT_SPEC_CODE")
	private String mtoci;
	
	@DtoTag(name = "KD_LOT_NUMBER")
	private String kdLot;
	
	public Object getSequence() {
		return sequence;
	}
	public String getVin() {
		return vin;
	}
	public String getMtoci() {
		return mtoci;
	}
	public String getKdLot() {
		return kdLot;
	}
	public void setSequence(Object sequence) {
		this.sequence = sequence;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public void setMtoci(String mtoci) {
		this.mtoci = mtoci;
	}
	public void setKdLot(String kdLot) {
		this.kdLot = kdLot;
	}

}
