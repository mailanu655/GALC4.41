package com.honda.galc.entity.product;

import java.util.Date;

import javax.persistence.*;
import com.honda.galc.entity.AuditEntry;


/**
 * 
 * <h3>AnnualCalendar.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AnnualCalendar.java description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>KM</TD>
 * <TD>Feb 11, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Kamlesh Maharjan
 * @created Feb 11, 2014
 */

/**
 * The persistent class for the GAL279TBX database table.
 * 
 */
@Entity
@Table(name="GAL279TBX")
public class AnnualCalendar extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	@Column(name="DAY_OF_WEEK")
	private String dayOfWeek;

	@Column(name="WEEK_NO")
	private String weekNo;

    public AnnualCalendar() {
    }
    
    public Date getId() {
    	return getProductionDate();
    }
	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getDayOfWeek() {
		return this.dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getWeekNo() {
		return this.weekNo;
	}

	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}

}