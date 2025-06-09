package com.honda.mfg.stamp.conveyor.domain;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitInfo {
	private static final Logger LOG = LoggerFactory.getLogger(BitInfo.class);
    private String bit1Label;
    private String bit2Label;
    private String bit3Label;
    private String bit4Label;
    private String bit5Label;
    private String bit6Label;
    private String bit7Label;
    private String bit8Label;
    private String bit9Label;
    private String bit10Label;
    private String bit11Label;
    private String bit12Label;
    private String bit13Label;
    private String bit14Label;
    private String bit15Label;

    private Boolean maintRequired;
	private Boolean showBit1;
    
    private Boolean showBit2;
    
    private Boolean showBit3;
    
    private Boolean showBit4;
    
    private Boolean showBit5;
    
    private Boolean showBit6;
    
    private Boolean showBit7;
    
    private Boolean showBit8;
    
    private Boolean showBit9;
    
    private Boolean showBit10;
    
    private Boolean showBit11;
    
    private Boolean showBit12;
    
    private Boolean showBit13;

	
    private Boolean showBit14;
    
    private Boolean showBit15;
	public BitInfo(Boolean maintRequired,Boolean showBit1, Boolean showBit2, Boolean showBit3, Boolean showBit4, Boolean showBit5,
			Boolean showBit6, Boolean showBit7, Boolean showBit8, Boolean showBit9, Boolean showBit10,
			Boolean showBit11, Boolean showBit12, Boolean showBit13, Boolean showBit14, Boolean showBit15) {
		super();
		this.maintRequired= maintRequired;
		this.showBit1 = showBit1;
		this.showBit2 = showBit2;
		this.showBit3 = showBit3;
		this.showBit4 = showBit4;
		this.showBit5 = showBit5;
		this.showBit6 = showBit6;
		this.showBit7 = showBit7;
		this.showBit8 = showBit8;
		this.showBit9 = showBit9;
		this.showBit10 = showBit10;
		this.showBit11 = showBit11;
		this.showBit12 = showBit12;
		this.showBit13 = showBit13;
		this.showBit14 = showBit14;
		this.showBit15 = showBit15;
	}
	public BitInfo(){
		super();
	}
	public int geIntValue(){
		int i = 0;
		i=i+((maintRequired!=null&& maintRequired)?1:0);
		i=i+((showBit1!=null && showBit1)?2:0);
		i=i+((showBit2!=null && showBit2)?4:0);
		i=i+((showBit3!=null && showBit3)?8:0);
		i=i+((showBit4!=null && showBit4)?16:0);
		i=i+((showBit5!=null && showBit5)?32:0);
		i=i+((showBit6!=null && showBit6)?64:0);
		i=i+((showBit7!=null && showBit7)?128:0);
		i=i+((showBit8!=null && showBit8)?256:0);
		i=i+((showBit9!=null && showBit9)?512:0);
		i=i+((showBit10!=null && showBit10)?1024:0);
		i=i+((showBit11!=null && showBit11)?2048:0);
		i=i+((showBit12!=null && showBit12)?4096:0);
		i=i+((showBit13!=null && showBit13)?8192:0);
		i=i+((showBit14!=null && showBit14)?16384:0);
		i=i+((showBit15!=null && showBit15)?32768:0);
		return i;
	}

	public String getBit1Label() {
		return bit1Label;
	}

	public void setBit1Label(String bit1Label) {
		this.bit1Label = bit1Label;
	}

	public String getBit2Label() {
		return bit2Label;
	}

	public void setBit2Label(String bit2Label) {
		this.bit2Label = bit2Label;
	}

	public String getBit3Label() {
		return bit3Label;
	}

	public void setBit3Label(String bit3Label) {
		this.bit3Label = bit3Label;
	}

	public String getBit4Label() {
		return bit4Label;
	}

	public void setBit4Label(String bit4Label) {
		this.bit4Label = bit4Label;
	}

	public String getBit5Label() {
		return bit5Label;
	}

	public void setBit5Label(String bit5Label) {
		this.bit5Label = bit5Label;
	}

	public String getBit6Label() {
		return bit6Label;
	}

	public void setBit6Label(String bit6Label) {
		this.bit6Label = bit6Label;
	}

	public String getBit7Label() {
		return bit7Label;
	}

	public void setBit7Label(String bit7Label) {
		this.bit7Label = bit7Label;
	}

	public String getBit8Label() {
		return bit8Label;
	}

	public void setBit8Label(String bit8Label) {
		this.bit8Label = bit8Label;
	}

	public String getBit9Label() {
		return bit9Label;
	}

	public void setBit9Label(String bit9Label) {
		this.bit9Label = bit9Label;
	}

	public String getBit10Label() {
		return bit10Label;
	}

	public void setBit10Label(String bit10Label) {
		this.bit10Label = bit10Label;
	}

	public String getBit11Label() {
		return bit11Label;
	}

	public void setBit11Label(String bit11Label) {
		this.bit11Label = bit11Label;
	}

	public String getBit12Label() {
		return bit12Label;
	}

	public void setBit12Label(String bit12Label) {
		this.bit12Label = bit12Label;
	}

	public String getBit13Label() {
		return bit13Label;
	}

	public void setBit13Label(String bit13Label) {
		this.bit13Label = bit13Label;
	}

	public String getBit14Label() {
		return bit14Label;
	}

	public void setBit14Label(String bit14Label) {
		this.bit14Label = bit14Label;
	}

	public String getBit15Label() {
		return bit15Label;
	}

	public void setBit15Label(String bit15Label) {
		this.bit15Label = bit15Label;
	}

	public Boolean isShowBit1() {
		return showBit1;
	}

	public void setShowBit1(Boolean showBit1) {
		this.showBit1 = showBit1;
	}

	public Boolean isShowBit2() {
		return showBit2;
	}

	public void setShowBit2(Boolean showBit2) {
		this.showBit2 = showBit2;
	}

	public Boolean isShowBit3() {
		return showBit3;
	}

	public void setShowBit3(Boolean showBit3) {
		this.showBit3 = showBit3;
	}

	public Boolean isShowBit4() {
		return showBit4;
	}

	public void setShowBit4(Boolean showBit4) {
		this.showBit4 = showBit4;
	}

	public Boolean isShowBit5() {
		return showBit5;
	}

	public void setShowBit5(Boolean showBit5) {
		this.showBit5 = showBit5;
	}

	public Boolean isShowBit6() {
		return showBit6;
	}

	public void setShowBit6(Boolean showBit6) {
		this.showBit6 = showBit6;
	}

	public Boolean isShowBit7() {
		return showBit7;
	}

	public void setShowBit7(Boolean showBit7) {
		this.showBit7 = showBit7;
	}

	public Boolean isShowBit8() {
		return showBit8;
	}

	public void setShowBit8(Boolean showBit8) {
		this.showBit8 = showBit8;
	}

	public Boolean isShowBit9() {
		return showBit9;
	}

	public void setShowBit9(Boolean showBit9) {
		this.showBit9 = showBit9;
	}

	public Boolean isShowBit10() {
		return showBit10;
	}

	public void setShowBit10(Boolean showBit10) {
		this.showBit10 = showBit10;
	}

	public Boolean isShowBit11() {
		return showBit11;
	}

	public void setShowBit11(Boolean showBit11) {
		this.showBit11 = showBit11;
	}

	public Boolean isShowBit12() {
		return showBit12;
	}

	public void setShowBit12(Boolean showBit12) {
		this.showBit12 = showBit12;
	}

	public Boolean isShowBit13() {
		return showBit13;
	}

	public void setShowBit13(Boolean showBit13) {
		this.showBit13 = showBit13;
	}

	public Boolean isShowBit14() {
		return showBit14;
	}

	public void setShowBit14(Boolean showBit14) {
		this.showBit14 = showBit14;
	}

	public Boolean isShowBit15() {
		return showBit15;
	}

	public void setShowBit15(Boolean showBit15) {
		this.showBit15 = showBit15;
	}


	public Boolean getMaintRequired() {
		return maintRequired;
	}

	public void setMaintRequired(Boolean maintRequired) {
		this.maintRequired = maintRequired;
	}
	public Boolean getShowBit1() {
		return showBit1;
	}
	public Boolean getShowBit2() {
		return showBit2;
	}
	public Boolean getShowBit3() {
		return showBit3;
	}
	public Boolean getShowBit4() {
		return showBit4;
	}
	public Boolean getShowBit5() {
		return showBit5;
	}
	public Boolean getShowBit6() {
		return showBit6;
	}
	public Boolean getShowBit7() {
		return showBit7;
	}
	public Boolean getShowBit8() {
		return showBit8;
	}
	public Boolean getShowBit9() {
		return showBit9;
	}
	public Boolean getShowBit10() {
		return showBit10;
	}
	public Boolean getShowBit11() {
		return showBit11;
	}
	public Boolean getShowBit12() {
		return showBit12;
	}
	public Boolean getShowBit13() {
		return showBit13;
	}
	public Boolean getShowBit14() {
		return showBit14;
	}
	public Boolean getShowBit15() {
		return showBit15;
	}
	public String showBitsToString(){
		StringBuilder sb = new StringBuilder();
		sb.append((maintRequired==null)?"-|":(maintRequired)?"1|":"0|");
		sb.append((showBit1==null)?"-|":(showBit1)?"1|":"0|");
		sb.append((showBit2==null)?"-|":(showBit2)?"1|":"0|");
		sb.append((showBit3==null)?"-|":(showBit3)?"1|":"0|");
		sb.append((showBit4==null)?"-|":(showBit4)?"1|":"0|");
		sb.append((showBit5==null)?"-|":(showBit5)?"1|":"0|");
		sb.append((showBit6==null)?"-|":(showBit6)?"1|":"0|");
		sb.append((showBit7==null)?"-|":(showBit7)?"1|":"0|");
		sb.append((showBit8==null)?"-|":(showBit8)?"1|":"0|");
		sb.append((showBit9==null)?"-|":(showBit9)?"1|":"0|");
		sb.append((showBit10==null)?"-|":(showBit10)?"1|":"0|");
		sb.append((showBit11==null)?"-|":(showBit11)?"1|":"0|");
		sb.append((showBit12==null)?"-|":(showBit12)?"1|":"0|");
		sb.append((showBit13==null)?"-|":(showBit13)?"1|":"0|");
		sb.append((showBit14==null)?"-|":(showBit14)?"1|":"0|");
		sb.append((showBit15==null)?"-":(showBit15)?"1":"0");
		return sb.toString();
	}
	public void parseShowBits(String s){
		String [] fields = s.split("\\|");
		if(fields.length==16){
		maintRequired = ("-".equals(fields[0]))?null:("1".equals(fields[0]))?true:false;
		showBit1 = ("-".equals(fields[1]))?null:("1".equals(fields[1]))?true:false;
		showBit2 = ("-".equals(fields[2]))?null:("1".equals(fields[2]))?true:false;
		showBit3 = ("-".equals(fields[3]))?null:("1".equals(fields[3]))?true:false;
		showBit4 = ("-".equals(fields[4]))?null:("1".equals(fields[4]))?true:false;
		showBit5 = ("-".equals(fields[5]))?null:("1".equals(fields[5]))?true:false;
		showBit6 = ("-".equals(fields[6]))?null:("1".equals(fields[6]))?true:false;
		showBit7 = ("-".equals(fields[7]))?null:("1".equals(fields[7]))?true:false;
		showBit8 = ("-".equals(fields[8]))?null:("1".equals(fields[8]))?true:false;
		showBit9 = ("-".equals(fields[9]))?null:("1".equals(fields[9]))?true:false;
		showBit10 = ("-".equals(fields[10]))?null:("1".equals(fields[10]))?true:false;
		showBit11 = ("-".equals(fields[11]))?null:("1".equals(fields[11]))?true:false;
		showBit12 = ("-".equals(fields[12]))?null:("1".equals(fields[12]))?true:false;
		showBit13 = ("-".equals(fields[13]))?null:("1".equals(fields[13]))?true:false;
		showBit14 = ("-".equals(fields[14]))?null:("1".equals(fields[14]))?true:false;
		showBit15 = ("-".equals(fields[15]))?null:("1".equals(fields[15]))?true:false;
		}else{
			LOG.info("Illegal Length parsing Bit Info " + s + " Length " + fields.length);
		}
	}
	@Override
	public String toString() {
		return "BitInfo [bit1Label=" + bit1Label + ", bit2Label=" + bit2Label + ", bit3Label=" + bit3Label
				+ ", bit4Label=" + bit4Label + ", bit5Label=" + bit5Label + ", bit6Label=" + bit6Label + ", bit7Label="
				+ bit7Label + ", bit8Label=" + bit8Label + ", bit9Label=" + bit9Label + ", bit10Label=" + bit10Label
				+ ", bit11Label=" + bit11Label + ", bit12Label=" + bit12Label + ", bit13Label=" + bit13Label
				+ ", bit14Label=" + bit14Label + ", bit15Label=" + bit15Label + ", maintRequired=" + maintRequired
				+ ", showBit1=" + showBit1 + ", showBit2=" + showBit2 + ", showBit3=" + showBit3 + ", showBit4="
				+ showBit4 + ", showBit5=" + showBit5 + ", showBit6=" + showBit6 + ", showBit7=" + showBit7
				+ ", showBit8=" + showBit8 + ", showBit9=" + showBit9 + ", showBit10=" + showBit10 + ", showBit11="
				+ showBit11 + ", showBit12=" + showBit12 + ", showBit13=" + showBit13 + ", showBit14=" + showBit14
				+ ", showBit15=" + showBit15 + "]";
	}
}
