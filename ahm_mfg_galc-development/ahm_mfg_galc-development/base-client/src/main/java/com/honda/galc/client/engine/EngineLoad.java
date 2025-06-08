/**
 * 
 */
package com.honda.galc.client.engine;

import com.honda.galc.client.device.plc.memorymap.AbstractPlcMemoryMap;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * @date Dec 21, 2011
 */
public class EngineLoad extends AbstractPlcMemoryMap {

	/** plc registers **/


	/** galc registers **/

	@XStreamAlias("mto1")
	private String _mto1 = "";
	
	@XStreamAlias("seq1")
	private String _seq1 = "";
	
	@XStreamAlias("vin1")
	private String _vin1 = "";
	
	@XStreamAlias("afb1")
	private StringBuilder _afb1 = new StringBuilder();

	@XStreamAlias("checksum1")
	private String _checksum1 = "";
	
	@XStreamAlias("mto2")
	private String _mto2 = "";
	
	@XStreamAlias("seq2")
	private String _seq2 = "";
	
	@XStreamAlias("vin2")
	private String _vin2 = "";
	
	@XStreamAlias("afb2")
	private StringBuilder _afb2 = new StringBuilder();

	@XStreamAlias("checksum2")
	private String _checksum2 = "";
	
	@XStreamAlias("mto3")
	private String _mto3 = "";
	
	@XStreamAlias("seq3")
	private String _seq3 = "";
	
	@XStreamAlias("vin3")
	private String _vin3 = "";
	
	@XStreamAlias("afb3")
	private StringBuilder _afb3 = new StringBuilder();

	@XStreamAlias("checksum3")
	private String _checksum3 = "";
	
	@XStreamAlias("mto4")
	private String _mto4 = "";
	
	@XStreamAlias("seq4")
	private String _seq4 = "";
	
	@XStreamAlias("vin4")
	private String _vin4 = "";
	
	@XStreamAlias("afb4")
	private StringBuilder _afb4 = new StringBuilder();

	@XStreamAlias("checksum4")
	private String _checksum4 = "";
	
	@XStreamAlias("galcDataReady")
	private String _galcDataReady = "";
	
	@XStreamAlias("eqDataReady")
	private String _eqDataReady = "";
	
	public EngineLoad() {
	}
	
	public String getMto1() {
		return _mto1;
	}
	
	public void setMto1(String mto1) {
		_mto1 = mto1;
	}
	
	public String getSeq1() {
		return _seq1;
	}
	
	public void setSeq1(String seq1) {
		_seq1 = seq1;
	}
	
	public String getVin1() {
		return _vin1;
	}
	
	public void setVin1(String vin1) {
		_vin1 = vin1;
	}
	
	public StringBuilder getAfb1() {
		return _afb1;
	}
	
	public void setAfb1(StringBuilder afb1) {
		_afb1 = afb1;
	}
	
	public String getChecksum1() {
		return _checksum1;
	}
	
	public void setChecksum1(String checksum1) {
		_checksum1 = checksum1;
	}
	
	public String getMto2() {
		return _mto2;
	}
	
	public void setMto2(String mto2) {
		_mto2 = mto2;
	}
	
	public String getSeq2() {
		return _seq2;
	}
	
	public void setSeq2(String seq2) {
		_seq2 = seq2;
	}
	
	public String getVin2() {
		return _vin2;
	}
	
	public void setVin2(String vin2) {
		_vin2 = vin2;
	}
	
	public StringBuilder getAfb2() {
		return _afb2;
	}
	
	public void setAfb2(StringBuilder afb2) {
		_afb2 = afb2;
	}
	
	public String getChecksum2() {
		return _checksum2;
	}
	
	public void setChecksum2(String checksum2) {
		_checksum2 = checksum2;
	}
	
	public String getMto3() {
		return _mto3;
	}
	
	public void setMto3(String mto3) {
		_mto3 = mto3;
	}
	
	public String getSeq3() {
		return _seq3;
	}
	
	public void setSeq3(String seq3) {
		_seq3 = seq3;
	}
	
	public String getVin3() {
		return _vin3;
	}
	
	public void setVin3(String vin3) {
		_vin3 = vin3;
	}
	
	public StringBuilder getAfb3() {
		return _afb3;
	}
	
	public void setAfb3(StringBuilder afb3) {
		_afb3 = afb3;
	}
	
	public String getChecksum3() {
		return _checksum3;
	}
	
	public void setChecksum3(String checksum3) {
		_checksum3 = checksum3;
	}

	public String getMto4() {
		return _mto4;
	}
	
	public void setMto4(String mto4) {
		_mto4 = mto4;
	}
	
	public String getSeq4() {
		return _seq4;
	}
	
	public void setSeq4(String seq4) {
		_seq4 = seq4;
	}
	
	public String getVin4() {
		return _vin4;
	}
	
	public void setVin4(String vin4) {
		_vin4 = vin4;
	}
	
	public StringBuilder getAfb4() {
		return _afb4;
	}
	
	public void setAfb4(StringBuilder afb4) {
		_afb4 = afb4;
	}
	
	public String getChecksum4() {
		return _checksum4;
	}
	
	public void setChecksum4(String checksum4) {
		_checksum4 = checksum4;
	}
	
	public String getEqDataReady() {
		return _eqDataReady;
	}
	
	public void setEqDataReady(String eqDataReady) {
		_eqDataReady = eqDataReady;
	}
	public String getGalcDataReady() {
		return _galcDataReady;
	}
	
	public void setGalcDataReady(String galcDataReady) {
		_galcDataReady = galcDataReady;
	}
}