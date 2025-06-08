package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/** 
* @author Subu Kathiresan 
* @since Sep 09, 2022
*/
@Entity
@Table(name = "TERMINAL_TYPE_TBX")
public class TerminalType extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TERMINAL_FLAG")
    private int terminalFlag;
    
    @Column(name = "NAME")
    private String name;

    public TerminalType() {
        super();
    }

    public Integer getId() {
    	return getTerminalFlag();
    }

    public int getTerminalFlag() {
        return this.terminalFlag;
    }

    public void setTerminalFlag(int terminalFlag) {
        this.terminalFlag = terminalFlag;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}