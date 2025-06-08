package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL242TBX")
public class ApplicationByTerminal extends AuditEntry {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private ApplicationByTerminalId id;

    @Column(name = "DEFAULT_APPLICATION_FLAG")
    private short defaultApplicationFlag;

    @Column(name = "SEQ_NUM")
    private int sequenceNumber;
    
    @OneToOne(targetEntity = Application.class,fetch = FetchType.EAGER)
    @JoinColumn(name="APPLICATION_ID", referencedColumnName="APPLICATION_ID")
    private Application application;

    public ApplicationByTerminal() {
        super();
    }
    
    public ApplicationByTerminal(String applicationId, String hostName) {
    	this.id = new ApplicationByTerminalId(applicationId,hostName);
    }

    public ApplicationByTerminalId getId() {
        return this.id;
    }

    public void setId(ApplicationByTerminalId id) {
        this.id = id;
    }

    public short getDefaultApplicationFlag() {
        return this.defaultApplicationFlag;
    }

    public void setDefaultApplicationFlag(short defaultApplicationFlag) {
        this.defaultApplicationFlag = defaultApplicationFlag;
    }
    
    public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

    public boolean isDefaultApplication() {
        return defaultApplicationFlag >= 1;
    }
    
    public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
	public String getHostName() {
		return getId().getHostName();
	}
	
	public String getApplicationId() {
		return getId().getApplicationId();
	}

	@Override
	public String toString() {
		return toString(getApplicationId(),getHostName(), 
				getDefaultApplicationFlag(), getSequenceNumber());
	}
}
