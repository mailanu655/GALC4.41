package com.honda.galc.client.enumtype;

public enum RfidErrorCodes {
	
	NO_ERRORS("No Errors",0),
	VIN_MISSING("VIN missing ",1),
	VIN_NOT_VALID("VIN not valid",2),
	MTOC_MISSING("MTOC missing ",3),
	MTOC_NOT_VALID("MTOC not valid",4),
	PROD_LOT_MISSING("PRODUCTION_LOT missing ",5),
	PROD_LOT_NOT_VALID("PRODUCTION_LOT not valid",6),
	PROD_LOT_QTY_MISSING("PROD_LOT_QTY missing ",7),
	PROD_LOT_QTY_NOT_VALID("PROD_LOT_QTY not valid",8),
	SUNROOF_MISSING("PA_SUNROOF missing ",9),
	SUNROOF_NOT_VALID("PA_SUNROOF not valid",10),
	TRUNK_LINER_MISSING("PA_TRUNK_LID_LING missing ",11),
	TRUNK_LINER_NOT_VALID("PA_TRUNK_LID_LING not valid",12),
	HOODINSULATOR_MISSING("PA_HOODINSULATOR missing ",13),
	HOODINSULATOR_NOT_VALID("PA_HOODINSULATOR not valid",14),
	UBC_MISSING("UBC is missing ",15),
	UBC_NOT_VALID("UBC not valid",16),	
	LASD_MISSING("LASD is missing ",17),
	LASD_NOT_VALID("LASD not valid",18),	
	SEALER_MISSING("SEALER is missing ",19),
	SEALER_NOT_VALID("SEALER not valid",20),	
	SURFACER_EXTERIOR_MISSING("SURFACER_EXTERIOR is missing ",21),
	SURFACER_EXTERIOR_NOT_VALID("SURFACER_EXTERIOR not valid",22),	
	EXPECTED_VIN_MISSING("EXPECTED_VIN is missing ",23),
	EXPECTED_VIN_DOES_NOT_MATCH("EXPECTED_VIN does not match ",24),
	BODY_SEQ_NUMBER_NOT_VALID("BODY_SEQ_NUMBER not valid",25),	
	BODY_SEQ_NUMBER_MISSING("BODY_SEQ_NUMBER missing ",26),
	AFB_NOT_VALID("AFB not valid",27),
	AFB_MISSING("AFB missing ",28),
	MODEL_MISSING("MODEL missing ",29),
	OPTION_MISSING("OPTION missing ",30),
	TYPE_MISSING("TYPE missing ",31),
	YEAR_MISSING("YEAR missing ",32),
	SURFACER_INTERIOR_NOT_VALID("SURFACER_INTERIOR not valid",33),
	SURFACER_INTERIOR_MISSING("SURFACER_INTERIOR is missing ",34),
	TOPCOAT_EXTERIOR_NOT_VALID("TOPCOAT_EXTERIOR not valid",35),
	TOPCOAT_EXTERIOR_MISSING("TOPCOAT_EXTERIOR is missing ",36),
	TOPCOAT_INTERIOR_NOT_VALID("TOPCOAT_INTERIOR not valid",37),
	TOPCOAT_INTERIOR_MISSING("TOPCOAT_INTERIOR is missing ",38),
	WAX_NOT_VALID("WAX is not valid",39),
	WAX_MISSING("WAX is missing ",40),
	TESTFIRE_NOT_VALID("TESTFIRE is not valid ",41),
	TESTFIRE_MISSING("TESTFIRE is missing ",42),
	ENGINE_SERIALNUM_NOT_VALID("ENGINE_SERIAL_NUM is not valid ",43),
	ENGINE_SERIALNUM_MISSING("ENGINE_SERIAL_NUM is missing ",44),
	DUPLICATE_VIN("DUPLICATE_VIN ",45);
		
	private String errorMsg;
	
	private int value;
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String getErrorMessage(){
		return(errorMsg);
	}

	RfidErrorCodes(String errorMsg, int value)
	{
		this.errorMsg=errorMsg;
		this.value=value;
	}

}
