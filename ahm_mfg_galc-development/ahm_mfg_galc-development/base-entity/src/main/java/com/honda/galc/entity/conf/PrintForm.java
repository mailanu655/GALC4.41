package com.honda.galc.entity.conf;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL294TBX")
public class PrintForm extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PrintFormId id;

    @Column(name = "DEFAULT_FLAG")
    private int defaultFlag;

    public PrintForm() {
        super();
    }

    public PrintFormId getId() {
        return this.id;
    }

    public void setId(PrintFormId id) {
        this.id = id;
    }
    
    public int getDefaultFlag() {
        return this.defaultFlag;
    }

    public boolean isDefault() {
        return this.defaultFlag >= 1;
    }

    public void setDefaultFlag(int defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
    
    
    public String getFormId() {
    	return getId() != null ? getId().getFormId() : null;
    }

     
    public void setFormId(String formId) {
		if( getId() != null )
			getId().setFormId(formId);
	}
    
    public String getDestinationId() {
    	return getId() != null ? getId().getDestinationId() : null;
    }

     
    public void setDestinationId(String formId) {
		if( getId() != null )
			getId().setDestinationId(formId);
	}

	@Override
	public String toString() {
		return toString(id.getDestinationId(),id.getFormId());
	}
}
