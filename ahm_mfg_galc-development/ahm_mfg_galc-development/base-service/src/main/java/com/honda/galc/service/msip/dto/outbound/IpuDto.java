package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class IpuDto extends BaseOutboundDto implements IMsipOutboundDto {
	private static final long serialVersionUID = 1L;	
	
	String ipuData;
	String ipuLetData;
	
	private String 	productId	;
	private String 	ipuSn	;
	private String 	battery	;
	private String 	startTimestamp	;
	private String 	startUtc	;
	private String 	processEndTimestamp	;
	private String 	totalStatus	;
	private String 	cellVoltageStatus	;
	private String 	inspectionParamName	;
	private String 	inspectionParamValue	;
	private String 	vbc1	;
	private String 	vbc10	;
	private String 	vbc11	;
	private String 	vbc12	;
	private String 	vbc13	;
	private String 	vbc14	;
	private String 	vbc15	;
	private String 	vbc16	;
	private String 	vbc17	;
	private String 	vbc18	;
	private String 	vbc19	;
	private String 	vbc2	;
	private String 	vbc20	;
	private String 	vbc21	;
	private String 	vbc22	;
	private String 	vbc23	;
	private String 	vbc24	;
	private String 	vbc25	;
	private String 	vbc26	;
	private String 	vbc27	;
	private String 	vbc28	;
	private String 	vbc29	;
	private String 	vbc3	;
	private String 	vbc30	;
	private String 	vbc31	;
	private String 	vbc32	;
	private String 	vbc33	;
	private String 	vbc34	;
	private String 	vbc35	;
	private String 	vbc36	;
	private String 	vbc37	;
	private String 	vbc38	;
	private String 	vbc39	;
	private String 	vbc4	;
	private String 	vbc40	;
	private String 	vbc41	;
	private String 	vbc42	;
	private String 	vbc43	;
	private String 	vbc44	;
	private String 	vbc45	;
	private String 	vbc46	;
	private String 	vbc47	;
	private String 	vbc48	;
	private String 	vbc49	;
	private String 	vbc5	;
	private String 	vbc50	;
	private String 	vbc51	;
	private String 	vbc52	;
	private String 	vbc53	;
	private String 	vbc54	;
	private String 	vbc55	;
	private String 	vbc56	;
	private String 	vbc57	;
	private String 	vbc58	;
	private String 	vbc59	;
	private String 	vbc6	;
	private String 	vbc60	;
	private String 	vbc61	;
	private String 	vbc62	;
	private String 	vbc63	;
	private String 	vbc64	;
	private String 	vbc65	;
	private String 	vbc66	;
	private String 	vbc67	;
	private String 	vbc68	;
	private String 	vbc69	;
	private String 	vbc7	;
	private String 	vbc70	;
	private String 	vbc71	;
	private String 	vbc72	;
	private String 	vbc8	;
	private String 	vbc9	;
	private String errorMsg;
	private Boolean isError;
	
	public Boolean getIsError() {
		return isError;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setIsError(Boolean isError) {
		this.isError = isError;
		
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		
	}
	
	public String getVersion() {
		return version;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getIpuSn() {
		return ipuSn;
	}

	public void setIpuSn(String ipuSn) {
		this.ipuSn = ipuSn;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(String startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public String getStartUtc() {
		return startUtc;
	}

	public void setStartUtc(String startUtc) {
		this.startUtc = startUtc;
	}

	public String getProcessEndTimestamp() {
		return processEndTimestamp;
	}

	public void setProcessEndTimestamp(String processEndTimestamp) {
		this.processEndTimestamp = processEndTimestamp;
	}

	public String getTotalStatus() {
		return totalStatus;
	}

	public void setTotalStatus(String totalStatus) {
		this.totalStatus = totalStatus;
	}

	public String getCellVoltageStatus() {
		return cellVoltageStatus;
	}

	public void setCellVoltageStatus(String cellVoltageStatus) {
		this.cellVoltageStatus = cellVoltageStatus;
	}

	public String getInspectionParamName() {
		return inspectionParamName;
	}

	public void setInspectionParamName(String inspectionParamName) {
		this.inspectionParamName = inspectionParamName;
	}

	public String getInspectionParamValue() {
		return inspectionParamValue;
	}

	public void setInspectionParamValue(String inspectionParamValue) {
		this.inspectionParamValue = inspectionParamValue;
	}

	public String getVbc1() {
		return vbc1;
	}

	public void setVbc1(String vbc1) {
		this.vbc1 = vbc1;
	}

	public String getVbc10() {
		return vbc10;
	}

	public void setVbc10(String vbc10) {
		this.vbc10 = vbc10;
	}

	public String getVbc11() {
		return vbc11;
	}

	public void setVbc11(String vbc11) {
		this.vbc11 = vbc11;
	}

	public String getVbc12() {
		return vbc12;
	}

	public void setVbc12(String vbc12) {
		this.vbc12 = vbc12;
	}

	public String getVbc13() {
		return vbc13;
	}

	public void setVbc13(String vbc13) {
		this.vbc13 = vbc13;
	}

	public String getVbc14() {
		return vbc14;
	}

	public void setVbc14(String vbc14) {
		this.vbc14 = vbc14;
	}

	public String getVbc15() {
		return vbc15;
	}

	public void setVbc15(String vbc15) {
		this.vbc15 = vbc15;
	}

	public String getVbc16() {
		return vbc16;
	}

	public void setVbc16(String vbc16) {
		this.vbc16 = vbc16;
	}

	public String getVbc17() {
		return vbc17;
	}

	public void setVbc17(String vbc17) {
		this.vbc17 = vbc17;
	}

	public String getVbc18() {
		return vbc18;
	}

	public void setVbc18(String vbc18) {
		this.vbc18 = vbc18;
	}

	public String getVbc19() {
		return vbc19;
	}

	public void setVbc19(String vbc19) {
		this.vbc19 = vbc19;
	}

	public String getVbc2() {
		return vbc2;
	}

	public void setVbc2(String vbc2) {
		this.vbc2 = vbc2;
	}

	public String getVbc20() {
		return vbc20;
	}

	public void setVbc20(String vbc20) {
		this.vbc20 = vbc20;
	}

	public String getVbc21() {
		return vbc21;
	}

	public void setVbc21(String vbc21) {
		this.vbc21 = vbc21;
	}

	public String getVbc22() {
		return vbc22;
	}

	public void setVbc22(String vbc22) {
		this.vbc22 = vbc22;
	}

	public String getVbc23() {
		return vbc23;
	}

	public void setVbc23(String vbc23) {
		this.vbc23 = vbc23;
	}

	public String getVbc24() {
		return vbc24;
	}

	public void setVbc24(String vbc24) {
		this.vbc24 = vbc24;
	}

	public String getVbc25() {
		return vbc25;
	}

	public void setVbc25(String vbc25) {
		this.vbc25 = vbc25;
	}

	public String getVbc26() {
		return vbc26;
	}

	public void setVbc26(String vbc26) {
		this.vbc26 = vbc26;
	}

	public String getVbc27() {
		return vbc27;
	}

	public void setVbc27(String vbc27) {
		this.vbc27 = vbc27;
	}

	public String getVbc28() {
		return vbc28;
	}

	public void setVbc28(String vbc28) {
		this.vbc28 = vbc28;
	}

	public String getVbc29() {
		return vbc29;
	}

	public void setVbc29(String vbc29) {
		this.vbc29 = vbc29;
	}

	public String getVbc3() {
		return vbc3;
	}

	public void setVbc3(String vbc3) {
		this.vbc3 = vbc3;
	}

	public String getVbc30() {
		return vbc30;
	}

	public void setVbc30(String vbc30) {
		this.vbc30 = vbc30;
	}

	public String getVbc31() {
		return vbc31;
	}

	public void setVbc31(String vbc31) {
		this.vbc31 = vbc31;
	}

	public String getVbc32() {
		return vbc32;
	}

	public void setVbc32(String vbc32) {
		this.vbc32 = vbc32;
	}

	public String getVbc33() {
		return vbc33;
	}

	public void setVbc33(String vbc33) {
		this.vbc33 = vbc33;
	}

	public String getVbc34() {
		return vbc34;
	}

	public void setVbc34(String vbc34) {
		this.vbc34 = vbc34;
	}

	public String getVbc35() {
		return vbc35;
	}

	public void setVbc35(String vbc35) {
		this.vbc35 = vbc35;
	}

	public String getVbc36() {
		return vbc36;
	}

	public void setVbc36(String vbc36) {
		this.vbc36 = vbc36;
	}

	public String getVbc37() {
		return vbc37;
	}

	public void setVbc37(String vbc37) {
		this.vbc37 = vbc37;
	}

	public String getVbc38() {
		return vbc38;
	}

	public void setVbc38(String vbc38) {
		this.vbc38 = vbc38;
	}

	public String getVbc39() {
		return vbc39;
	}

	public void setVbc39(String vbc39) {
		this.vbc39 = vbc39;
	}

	public String getVbc4() {
		return vbc4;
	}

	public void setVbc4(String vbc4) {
		this.vbc4 = vbc4;
	}

	public String getVbc40() {
		return vbc40;
	}

	public void setVbc40(String vbc40) {
		this.vbc40 = vbc40;
	}

	public String getVbc41() {
		return vbc41;
	}

	public void setVbc41(String vbc41) {
		this.vbc41 = vbc41;
	}

	public String getVbc42() {
		return vbc42;
	}

	public void setVbc42(String vbc42) {
		this.vbc42 = vbc42;
	}

	public String getVbc43() {
		return vbc43;
	}

	public void setVbc43(String vbc43) {
		this.vbc43 = vbc43;
	}

	public String getVbc44() {
		return vbc44;
	}

	public void setVbc44(String vbc44) {
		this.vbc44 = vbc44;
	}

	public String getVbc45() {
		return vbc45;
	}

	public void setVbc45(String vbc45) {
		this.vbc45 = vbc45;
	}

	public String getVbc46() {
		return vbc46;
	}

	public void setVbc46(String vbc46) {
		this.vbc46 = vbc46;
	}

	public String getVbc47() {
		return vbc47;
	}

	public void setVbc47(String vbc47) {
		this.vbc47 = vbc47;
	}

	public String getVbc48() {
		return vbc48;
	}

	public void setVbc48(String vbc48) {
		this.vbc48 = vbc48;
	}

	public String getVbc49() {
		return vbc49;
	}

	public void setVbc49(String vbc49) {
		this.vbc49 = vbc49;
	}

	public String getVbc5() {
		return vbc5;
	}

	public void setVbc5(String vbc5) {
		this.vbc5 = vbc5;
	}

	public String getVbc50() {
		return vbc50;
	}

	public void setVbc50(String vbc50) {
		this.vbc50 = vbc50;
	}

	public String getVbc51() {
		return vbc51;
	}

	public void setVbc51(String vbc51) {
		this.vbc51 = vbc51;
	}

	public String getVbc52() {
		return vbc52;
	}

	public void setVbc52(String vbc52) {
		this.vbc52 = vbc52;
	}

	public String getVbc53() {
		return vbc53;
	}

	public void setVbc53(String vbc53) {
		this.vbc53 = vbc53;
	}

	public String getVbc54() {
		return vbc54;
	}

	public void setVbc54(String vbc54) {
		this.vbc54 = vbc54;
	}

	public String getVbc55() {
		return vbc55;
	}

	public void setVbc55(String vbc55) {
		this.vbc55 = vbc55;
	}

	public String getVbc56() {
		return vbc56;
	}

	public void setVbc56(String vbc56) {
		this.vbc56 = vbc56;
	}

	public String getVbc57() {
		return vbc57;
	}

	public void setVbc57(String vbc57) {
		this.vbc57 = vbc57;
	}

	public String getVbc58() {
		return vbc58;
	}

	public void setVbc58(String vbc58) {
		this.vbc58 = vbc58;
	}

	public String getVbc59() {
		return vbc59;
	}

	public void setVbc59(String vbc59) {
		this.vbc59 = vbc59;
	}

	public String getVbc6() {
		return vbc6;
	}

	public void setVbc6(String vbc6) {
		this.vbc6 = vbc6;
	}

	public String getVbc60() {
		return vbc60;
	}

	public void setVbc60(String vbc60) {
		this.vbc60 = vbc60;
	}

	public String getVbc61() {
		return vbc61;
	}

	public void setVbc61(String vbc61) {
		this.vbc61 = vbc61;
	}

	public String getVbc62() {
		return vbc62;
	}

	public void setVbc62(String vbc62) {
		this.vbc62 = vbc62;
	}

	public String getVbc63() {
		return vbc63;
	}

	public void setVbc63(String vbc63) {
		this.vbc63 = vbc63;
	}

	public String getVbc64() {
		return vbc64;
	}

	public void setVbc64(String vbc64) {
		this.vbc64 = vbc64;
	}

	public String getVbc65() {
		return vbc65;
	}

	public void setVbc65(String vbc65) {
		this.vbc65 = vbc65;
	}

	public String getVbc66() {
		return vbc66;
	}

	public void setVbc66(String vbc66) {
		this.vbc66 = vbc66;
	}

	public String getVbc67() {
		return vbc67;
	}

	public void setVbc67(String vbc67) {
		this.vbc67 = vbc67;
	}

	public String getVbc68() {
		return vbc68;
	}

	public void setVbc68(String vbc68) {
		this.vbc68 = vbc68;
	}

	public String getVbc69() {
		return vbc69;
	}

	public void setVbc69(String vbc69) {
		this.vbc69 = vbc69;
	}

	public String getVbc7() {
		return vbc7;
	}

	public void setVbc7(String vbc7) {
		this.vbc7 = vbc7;
	}

	public String getVbc70() {
		return vbc70;
	}

	public void setVbc70(String vbc70) {
		this.vbc70 = vbc70;
	}

	public String getVbc71() {
		return vbc71;
	}

	public void setVbc71(String vbc71) {
		this.vbc71 = vbc71;
	}

	public String getVbc72() {
		return vbc72;
	}

	public void setVbc72(String vbc72) {
		this.vbc72 = vbc72;
	}

	public String getVbc8() {
		return vbc8;
	}

	public void setVbc8(String vbc8) {
		this.vbc8 = vbc8;
	}

	public String getVbc9() {
		return vbc9;
	}

	public void setVbc9(String vbc9) {
		this.vbc9 = vbc9;
	}

	public String getIpuData() {
		return ipuData;
	}

	public void setIpuData(String ipuData) {
		this.ipuData = ipuData;
	}

	public String getIpuLetData() {
		return ipuLetData;
	}

	public void setIpuLetData(String ipuLetData) {
		this.ipuLetData = ipuLetData;
	}

	

	@Override
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((ipuSn == null) ? 0 : ipuSn.hashCode());
		result = prime * result + ((battery == null) ? 0 : battery.hashCode());
		result = prime * result + ((startTimestamp == null) ? 0 : startTimestamp.hashCode());
		result = prime * result + ((startUtc == null) ? 0 : startUtc.hashCode());
		result = prime * result + ((processEndTimestamp == null) ? 0 : processEndTimestamp.hashCode());
		result = prime * result + ((totalStatus == null) ? 0 : totalStatus.hashCode());
		result = prime * result + ((cellVoltageStatus == null) ? 0 : cellVoltageStatus.hashCode());
		result = prime * result + ((inspectionParamName == null) ? 0 : inspectionParamName.hashCode());
		result = prime * result + ((inspectionParamValue == null) ? 0 : inspectionParamValue.hashCode());
		result = prime * result + ((vbc1 == null) ? 0 : vbc1.hashCode());
		result = prime * result + ((vbc10 == null) ? 0 : vbc10.hashCode());
		result = prime * result + ((vbc11 == null) ? 0 : vbc11.hashCode());
		result = prime * result + ((vbc12 == null) ? 0 : vbc12.hashCode());
		result = prime * result + ((vbc13 == null) ? 0 : vbc13.hashCode());
		result = prime * result + ((vbc14 == null) ? 0 : vbc14.hashCode());
		result = prime * result + ((vbc15 == null) ? 0 : vbc15.hashCode());
		result = prime * result + ((vbc16 == null) ? 0 : vbc16.hashCode());	
		result = prime * result + ((vbc17 == null) ? 0 : vbc17.hashCode());
		result = prime * result + ((vbc18 == null) ? 0 : vbc18.hashCode());
		result = prime * result + ((vbc19 == null) ? 0 : vbc19.hashCode());
		result = prime * result + ((vbc2 == null) ? 0 : vbc2.hashCode());
		result = prime * result + ((vbc20 == null) ? 0 : vbc20.hashCode());
		result = prime * result + ((vbc21 == null) ? 0 : vbc21.hashCode());
		result = prime * result + ((vbc22 == null) ? 0 : vbc22.hashCode());
		result = prime * result + ((vbc23 == null) ? 0 : vbc23.hashCode());	
		result = prime * result + ((vbc24 == null) ? 0 : vbc24.hashCode());
		result = prime * result + ((vbc25 == null) ? 0 : vbc25.hashCode());
		result = prime * result + ((vbc26 == null) ? 0 : vbc26.hashCode());
		result = prime * result + ((vbc27 == null) ? 0 : vbc27.hashCode());
		result = prime * result + ((vbc28 == null) ? 0 : vbc28.hashCode());
		result = prime * result + ((vbc29 == null) ? 0 : vbc29.hashCode());
		result = prime * result + ((vbc3 == null) ? 0 : vbc3.hashCode());
		result = prime * result + ((vbc30 == null) ? 0 : vbc30.hashCode());	
		result = prime * result + ((vbc31 == null) ? 0 : vbc31.hashCode());
		result = prime * result + ((vbc32 == null) ? 0 : vbc32.hashCode());
		result = prime * result + ((vbc33 == null) ? 0 : vbc33.hashCode());
		result = prime * result + ((vbc34 == null) ? 0 : vbc34.hashCode());
		result = prime * result + ((vbc35 == null) ? 0 : vbc35.hashCode());
		result = prime * result + ((vbc36 == null) ? 0 : vbc36.hashCode());
		result = prime * result + ((vbc37 == null) ? 0 : vbc37.hashCode());
		result = prime * result + ((vbc38 == null) ? 0 : vbc38.hashCode());	
		result = prime * result + ((vbc39 == null) ? 0 : vbc39.hashCode());
		result = prime * result + ((vbc6 == null) ? 0 : vbc6.hashCode());
		result = prime * result + ((vbc60 == null) ? 0 : vbc60.hashCode());	
		result = prime * result + ((vbc61 == null) ? 0 : vbc61.hashCode());
		result = prime * result + ((vbc62 == null) ? 0 : vbc62.hashCode());
		result = prime * result + ((vbc63 == null) ? 0 : vbc63.hashCode());
		result = prime * result + ((vbc64 == null) ? 0 : vbc64.hashCode());
		result = prime * result + ((vbc65 == null) ? 0 : vbc65.hashCode());
		result = prime * result + ((vbc66 == null) ? 0 : vbc66.hashCode());
		result = prime * result + ((vbc67 == null) ? 0 : vbc67.hashCode());
		result = prime * result + ((vbc68 == null) ? 0 : vbc68.hashCode());	
		result = prime * result + ((vbc69 == null) ? 0 : vbc69.hashCode());
		result = prime * result + ((vbc4 == null) ? 0 : vbc4.hashCode());
		result = prime * result + ((vbc40 == null) ? 0 : vbc40.hashCode());	
		result = prime * result + ((vbc41 == null) ? 0 : vbc41.hashCode());
		result = prime * result + ((vbc42 == null) ? 0 : vbc42.hashCode());
		result = prime * result + ((vbc43 == null) ? 0 : vbc43.hashCode());
		result = prime * result + ((vbc44 == null) ? 0 : vbc44.hashCode());
		result = prime * result + ((vbc45 == null) ? 0 : vbc45.hashCode());
		result = prime * result + ((vbc46 == null) ? 0 : vbc46.hashCode());
		result = prime * result + ((vbc47 == null) ? 0 : vbc47.hashCode());
		result = prime * result + ((vbc48 == null) ? 0 : vbc48.hashCode());	
		result = prime * result + ((vbc49 == null) ? 0 : vbc49.hashCode());
		result = prime * result + ((vbc5 == null) ? 0 : vbc5.hashCode());
		result = prime * result + ((vbc50 == null) ? 0 : vbc50.hashCode());	
		result = prime * result + ((vbc51 == null) ? 0 : vbc51.hashCode());
		result = prime * result + ((vbc52 == null) ? 0 : vbc52.hashCode());
		result = prime * result + ((vbc53 == null) ? 0 : vbc53.hashCode());
		result = prime * result + ((vbc55 == null) ? 0 : vbc54.hashCode());
		result = prime * result + ((vbc55 == null) ? 0 : vbc55.hashCode());
		result = prime * result + ((vbc56 == null) ? 0 : vbc56.hashCode());
		result = prime * result + ((vbc57 == null) ? 0 : vbc57.hashCode());
		result = prime * result + ((vbc58 == null) ? 0 : vbc58.hashCode());	
		result = prime * result + ((vbc59 == null) ? 0 : vbc59.hashCode());
		result = prime * result + ((vbc7 == null) ? 0 : vbc7.hashCode());
		result = prime * result + ((vbc70 == null) ? 0 : vbc70.hashCode());	
		result = prime * result + ((vbc71 == null) ? 0 : vbc71.hashCode());
		result = prime * result + ((vbc72 == null) ? 0 : vbc72.hashCode());
		result = prime * result + ((vbc8 == null) ? 0 : vbc8.hashCode());
		result = prime * result + ((vbc9 == null) ? 0 : vbc9.hashCode());
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IpuDto other = (IpuDto) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (ipuSn == null) {
			if (other.ipuSn != null)
				return false;
		} else if (!ipuSn.equals(other.ipuSn))
			return false;
		if (battery == null) {
			if (other.battery != null)
				return false;
		} else if (!battery.equals(other.battery))
			return false;
		if (startTimestamp == null) {
			if (other.startTimestamp != null)
				return false;
		} else if (!startTimestamp.equals(other.startTimestamp))
			return false;
		if (startUtc == null) {
			if (other.startUtc != null)
				return false;
		} else if (!startUtc.equals(other.startUtc))
			return false;
		if (processEndTimestamp == null) {
			if (other.processEndTimestamp != null)
				return false;
		} else if (!processEndTimestamp.equals(other.processEndTimestamp))
			return false;
		if (totalStatus == null) {
			if (other.totalStatus != null)
				return false;
		} else if (!totalStatus.equals(other.totalStatus))
			return false;
		if (cellVoltageStatus == null) {
			if (other.cellVoltageStatus != null)
				return false;
		} else if (!cellVoltageStatus.equals(other.cellVoltageStatus))
			return false;
		if (inspectionParamName == null) {
			if (other.inspectionParamName != null)
				return false;
		} else if (!inspectionParamName.equals(other.inspectionParamName))
			return false;
		if (inspectionParamValue == null) {
			if (other.inspectionParamValue != null)
				return false;
		} else if (!inspectionParamValue.equals(other.inspectionParamValue))
			return false;
		if (vbc1 == null) {
			if (other.vbc1 != null)
				return false;
		} else if (!vbc1.equals(other.vbc1))
			return false;
		if (vbc10 == null) {
			if (other.vbc10 != null)
				return false;
		} else if (!vbc10.equals(other.vbc10))
			return false;
		if (vbc11 == null) {
			if (other.vbc11 != null)
				return false;
		} else if (!vbc11.equals(other.vbc11))
			return false;
		if (vbc12 == null) {
			if (other.vbc12 != null)
				return false;
		} else if (!vbc12.equals(other.vbc12))
			return false;
		if (vbc13 == null) {
			if (other.vbc13 != null)
				return false;
		} else if (!vbc13.equals(other.vbc13))
			return false;
		if (vbc14 == null) {
			if (other.vbc14 != null)
				return false;
		} else if (!vbc14.equals(other.vbc14))
			return false;
		if (vbc15 == null) {
			if (other.vbc15 != null)
				return false;
		} else if (!vbc15.equals(other.vbc15))
			return false;
		if (vbc16 == null) {
			if (other.vbc16 != null)
				return false;
		} else if (!vbc16.equals(other.vbc16))
			return false;
		if (vbc17 == null) {
			if (other.vbc17 != null)
				return false;
		} else if (!vbc17.equals(other.vbc17))
			return false;
		if (vbc18 == null) {
			if (other.vbc18 != null)
				return false;
		} else if (!vbc18.equals(other.vbc18))
			return false;
		if (vbc19 == null) {
			if (other.vbc19 != null)
				return false;
		} else if (!vbc19.equals(other.vbc19))
			return false;
		if (vbc2 == null) {
			if (other.vbc2 != null)
				return false;
		} else if (!vbc2.equals(other.vbc2))
			return false;
		if (vbc20 == null) {
			if (other.vbc20 != null)
				return false;
		} else if (!vbc20.equals(other.vbc20))
			return false;
		if (vbc21 == null) {
			if (other.vbc21 != null)
				return false;
		} else if (!vbc21.equals(other.vbc21))
			return false;
		if (vbc22 == null) {
			if (other.vbc22 != null)
				return false;
		} else if (!vbc22.equals(other.vbc22))
			return false;
		if (vbc23 == null) {
			if (other.vbc23 != null)
				return false;
		} else if (!vbc23.equals(other.vbc23))
			return false;
		if (vbc24 == null) {
			if (other.vbc24 != null)
				return false;
		} else if (!vbc24.equals(other.vbc24))
			return false;
		if (vbc25 == null) {
			if (other.vbc25 != null)
				return false;
		} else if (!vbc25.equals(other.vbc25))
			return false;
		if (vbc26 == null) {
			if (other.vbc26 != null)
				return false;
		} else if (!vbc26.equals(other.vbc26))
			return false;
		if (vbc27 == null) {
			if (other.vbc27 != null)
				return false;
		} else if (!vbc27.equals(other.vbc27))
			return false;
		if (vbc28 == null) {
			if (other.vbc28 != null)
				return false;
		} else if (!vbc28.equals(other.vbc28))
			return false;
		if (vbc29 == null) {
			if (other.vbc29 != null)
				return false;
		} else if (!vbc29.equals(other.vbc29))
			return false;
		if (vbc3 == null) {
			if (other.vbc3 != null)
				return false;
		} else if (!vbc3.equals(other.vbc3))
			return false;
		if (vbc30 == null) {
			if (other.vbc30 != null)
				return false;
		} else if (!vbc30.equals(other.vbc30))
			return false;
		if (vbc31 == null) {
			if (other.vbc31 != null)
				return false;
		} else if (!vbc31.equals(other.vbc31))
			return false;
		if (vbc32 == null) {
			if (other.vbc32 != null)
				return false;
		} else if (!vbc32.equals(other.vbc32))
			return false;
		if (vbc33 == null) {
			if (other.vbc33 != null)
				return false;
		} else if (!vbc33.equals(other.vbc33))
			return false;
		if (vbc34 == null) {
			if (other.vbc34 != null)
				return false;
		} else if (!vbc34.equals(other.vbc34))
			return false;
		if (vbc35 == null) {
			if (other.vbc35 != null)
				return false;
		} else if (!vbc35.equals(other.vbc35))
			return false;
		if (vbc36 == null) {
			if (other.vbc36 != null)
				return false;
		} else if (!vbc36.equals(other.vbc36))
			return false;
		if (vbc37 == null) {
			if (other.vbc37 != null)
				return false;
		} else if (!vbc37.equals(other.vbc37))
			return false;
		if (vbc38 == null) {
			if (other.vbc38 != null)
				return false;
		} else if (!vbc38.equals(other.vbc38))
			return false;
		if (vbc39 == null) {
			if (other.vbc39 != null)
				return false;
		} else if (!vbc39.equals(other.vbc39))
			return false;
		if (vbc4 == null) {
			if (other.vbc4 != null)
				return false;
		} else if (!vbc4.equals(other.vbc4))
			return false;
		if (vbc40 == null) {
			if (other.vbc40 != null)
				return false;
		} else if (!vbc40.equals(other.vbc40))
			return false;
		if (vbc41 == null) {
			if (other.vbc41 != null)
				return false;
		} else if (!vbc41.equals(other.vbc41))
			return false;
		if (vbc42 == null) {
			if (other.vbc42 != null)
				return false;
		} else if (!vbc42.equals(other.vbc42))
			return false;
		if (vbc43 == null) {
			if (other.vbc43 != null)
				return false;
		} else if (!vbc43.equals(other.vbc43))
			return false;
		if (vbc44 == null) {
			if (other.vbc44 != null)
				return false;
		} else if (!vbc44.equals(other.vbc44))
			return false;
		if (vbc45 == null) {
			if (other.vbc45 != null)
				return false;
		} else if (!vbc45.equals(other.vbc45))
			return false;
		if (vbc46 == null) {
			if (other.vbc46 != null)
				return false;
		} else if (!vbc46.equals(other.vbc46))
			return false;
		if (vbc47 == null) {
			if (other.vbc47 != null)
				return false;
		} else if (!vbc47.equals(other.vbc47))
			return false;
		if (vbc48 == null) {
			if (other.vbc48 != null)
				return false;
		} else if (!vbc48.equals(other.vbc48))
			return false;
		if (vbc49 == null) {
			if (other.vbc49 != null)
				return false;
		} else if (!vbc49.equals(other.vbc49))
			return false;
		if (vbc5 == null) {
			if (other.vbc5 != null)
				return false;
		} else if (!vbc5.equals(other.vbc5))
			return false;
		if (vbc50 == null) {
			if (other.vbc50 != null)
				return false;
		} else if (!vbc50.equals(other.vbc50))
			return false;
		if (vbc51 == null) {
			if (other.vbc51 != null)
				return false;
		} else if (!vbc51.equals(other.vbc51))
			return false;
		if (vbc52 == null) {
			if (other.vbc52 != null)
				return false;
		} else if (!vbc52.equals(other.vbc52))
			return false;
		if (vbc53 == null) {
			if (other.vbc53 != null)
				return false;
		} else if (!vbc53.equals(other.vbc53))
			return false;
		if (vbc54 == null) {
			if (other.vbc54 != null)
				return false;
		} else if (!vbc54.equals(other.vbc54))
			return false;
		if (vbc55 == null) {
			if (other.vbc55 != null)
				return false;
		} else if (!vbc55.equals(other.vbc55))
			return false;
		if (vbc56 == null) {
			if (other.vbc56 != null)
				return false;
		} else if (!vbc56.equals(other.vbc56))
			return false;
		if (vbc57 == null) {
			if (other.vbc57 != null)
				return false;
		} else if (!vbc57.equals(other.vbc57))
			return false;
		if (vbc58 == null) {
			if (other.vbc58 != null)
				return false;
		} else if (!vbc58.equals(other.vbc58))
			return false;
		if (vbc59 == null) {
			if (other.vbc59 != null)
				return false;
		} else if (!vbc59.equals(other.vbc59))
			return false;
		if (vbc6 == null) {
			if (other.vbc6 != null)
				return false;
		} else if (!vbc6.equals(other.vbc6))
			return false;
		if (vbc60 == null) {
			if (other.vbc60 != null)
				return false;
		} else if (!vbc60.equals(other.vbc60))
			return false;
		if (vbc61 == null) {
			if (other.vbc61 != null)
				return false;
		} else if (!vbc61.equals(other.vbc61))
			return false;
		if (vbc62 == null) {
			if (other.vbc62 != null)
				return false;
		} else if (!vbc62.equals(other.vbc62))
			return false;
		if (vbc63 == null) {
			if (other.vbc63 != null)
				return false;
		} else if (!vbc63.equals(other.vbc63))
			return false;
		if (vbc64 == null) {
			if (other.vbc64 != null)
				return false;
		} else if (!vbc64.equals(other.vbc64))
			return false;
		if (vbc65 == null) {
			if (other.vbc65 != null)
				return false;
		} else if (!vbc65.equals(other.vbc65))
			return false;
		if (vbc66 == null) {
			if (other.vbc66 != null)
				return false;
		} else if (!vbc66.equals(other.vbc66))
			return false;
		if (vbc67 == null) {
			if (other.vbc67 != null)
				return false;
		} else if (!vbc67.equals(other.vbc67))
			return false;
		if (vbc68 == null) {
			if (other.vbc68 != null)
				return false;
		} else if (!vbc68.equals(other.vbc68))
			return false;
		if (vbc69 == null) {
			if (other.vbc69 != null)
				return false;
		} else if (!vbc69.equals(other.vbc69))
			return false;
		if (vbc7 == null) {
			if (other.vbc7 != null)
				return false;
		} else if (!vbc7.equals(other.vbc7))
			return false;
		if (vbc70 == null) {
			if (other.vbc70 != null)
				return false;
		} else if (!vbc70.equals(other.vbc70))
			return false;
		if (vbc71 == null) {
			if (other.vbc71 != null)
				return false;
		} else if (!vbc71.equals(other.vbc71))
			return false;
		if (vbc72 == null) {
			if (other.vbc72 != null)
				return false;
		} else if (!vbc72.equals(other.vbc72))
			return false;
		if (vbc8 == null) {
			if (other.vbc8 != null)
				return false;
		} else if (!vbc8.equals(other.vbc8))
			return false;
		if (vbc9 == null) {
			if (other.vbc9 != null)
				return false;
		} else if (!vbc9.equals(other.vbc9))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
