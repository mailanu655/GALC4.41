package com.honda.galc.entity.conf;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Line represents plant floor line section<br>
 * It belongs to Division and consists of ProcessPoints
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Aug 31, 2010</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL236TBX")
public class PreviousLine extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PreviousLineId id;


    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "LINE_ID", referencedColumnName = "LINE_ID",
                    unique = true, nullable = false, insertable = false, updatable = false)
    })

    private Line line;
    
	public PreviousLine() {
        super();
    }
    
	public PreviousLine(String lineId, String previousLineId) {
		this.id = new PreviousLineId();
		id.setLineId(lineId);
		id.setPreviousLineId(previousLineId);
	}


	public PreviousLineId getId() {
		return id;
	}



	public void setId(PreviousLineId id) {
		this.id = id;
	}
	
	public Line getLine() {
		return line;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PreviousLine)) {
			return false;
		}
		PreviousLine e = (PreviousLine) obj;
		return getId() == null ? e.getId() == null : getId().equals(e.getId());
	}    	
	
	@Override
	public String toString() {
		return toString(id.getLineId(),id.getPreviousLineId());
	}
}
