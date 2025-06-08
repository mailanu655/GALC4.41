package com.honda.galc.client.loader.dto;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.pdda.ChangeFormUnit;

public class PlatformDto {
	private String asm_proc_num; 
	private String process_pt_id;
	private int proc_seq_num;
	private List<ChangeFormUnit> cfUnits = new ArrayList<ChangeFormUnit>();
	
	public PlatformDto(String asm_proc_num, String process_pt_id,
			int proc_seq_num) {
		super();
		this.asm_proc_num = asm_proc_num;
		this.process_pt_id = process_pt_id;
		this.proc_seq_num = proc_seq_num;
	}
	
	public String getAsm_proc_num() {
		return asm_proc_num;
	}
	public void setAsm_proc_num(String asm_proc_num) {
		this.asm_proc_num = asm_proc_num;
	}
	public String getProcess_pt_id() {
		return process_pt_id;
	}
	public void setProcess_pt_id(String process_pt_id) {
		this.process_pt_id = process_pt_id;
	}
	public int getProc_seq_num() {
		return proc_seq_num;
	}
	public void setProc_seq_num(int proc_seq_num) {
		this.proc_seq_num = proc_seq_num;
	}
	public void addChgFrmUnit(ChangeFormUnit cfUnit) {
		cfUnits.add(cfUnit);
	}
	public List<ChangeFormUnit> getCfUnits() {
		return cfUnits;
	}
}
