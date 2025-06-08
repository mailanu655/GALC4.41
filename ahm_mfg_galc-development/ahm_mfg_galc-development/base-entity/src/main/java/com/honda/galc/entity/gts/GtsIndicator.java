package com.honda.galc.entity.gts;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsIndicatorType;

/**
 * 
 * 
 * <h3>GtsIndicator Class description</h3>
 * <p> GtsIndicator description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 16, 2014
 *
 *
 */
@Entity
@Table(name="GTS_INDICATOR_TBX")
public class GtsIndicator extends AuditEntry {
	@EmbeddedId
	private GtsIndicatorId id;

	@Column(name="INDICATOR_VALUE")
	private String indicatorValue;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@OneToMany(mappedBy="indicator")
	private Set<GtsMoveCondition> moveConditions;

	@OneToMany(mappedBy="indicator")
	private Set<GtsDecisionPointCondition> decisionPointConditions;

	private static final long serialVersionUID = 1L;

	public GtsIndicator() {
		super();
	}
	
	public GtsIndicator(String area, String indicatorName, String value) {
		id = new GtsIndicatorId();
		id.setTrackingArea(area);
		id.setIndicatorName(indicatorName);
		this.indicatorValue = value;
	}

	public GtsIndicatorId getId() {
		return this.id;
	}

	public void setId(GtsIndicatorId id) {
		this.id = id;
	}

	public String getIndicatorValue() {
		return StringUtils.trim(this.indicatorValue);
	}
	
	/**
	 * integer status for MIP, CP etc
	 * @return
	 */
	public int getStatus() {
		if(StringUtils.equalsIgnoreCase(getIndicatorValue(), "Yes") || 
		   StringUtils.equalsIgnoreCase(getIndicatorValue(), "Y") ||
		   StringUtils.equalsIgnoreCase(getIndicatorValue(), "True")) return 1;
		return NumberUtils.toInt(getIndicatorValue(),0);
	}
	
	public boolean isStatusOn() {
		return getStatus() >= 1;
	}

	public void setIndicatorValue(String indicatorValue) {
		this.indicatorValue = indicatorValue;
	}
	
	public String getIndicatorName() {
		return id.getIndicatorName();
	}
	
	public String getIndicatorTypeValue() {
		String[] tokens = id.getIndicatorName().split(Delimiter.HYPHEN);
		if(tokens == null) return "";
		else return tokens[0];
	}
	
	public String getNoTypeIndicatorName(){ 
		String[] tokens = id.getIndicatorName().split(Delimiter.HYPHEN);
		if(tokens == null || tokens.length <= 0) return "";
		else return tokens[1];
	}
	
	public GtsIndicatorType getIndicatorType() {
		return GtsIndicatorType.getIndicatorType(getIndicatorTypeValue());
	}
	
	public String getSourceLaneName() {
		String[] tokens = id.getIndicatorName().split(Delimiter.HYPHEN);
		if(tokens != null && tokens.length > 1) return tokens[1];
        return null;
	}
	
	public String getLaneName() {
		return getSourceLaneName();
	}
	
	public String getBodyPositionIoPointName() {
		return "BP" + Delimiter.HYPHEN + getLaneName();
	}
	
	public String getBodyCountIoPointName() {
		return "BC" + Delimiter.HYPHEN + getLaneName();
	}
	
	public String getDestLaneName() {
		String[] tokens = id.getIndicatorName().split(Delimiter.HYPHEN);
		if(tokens != null && tokens.length > 2) return tokens[2];
        return null;
	}
	
	
	/**
     * parse the indicator name to get the gate name
     * @return
     */
    
    public String getGateName(){
        
    	String[] tokens = id.getIndicatorName().split(Delimiter.HYPHEN);
		
        if(tokens != null && tokens.length == 3){
            if(!tokens[0].equalsIgnoreCase(GtsIndicatorType.GATA_STATUS.getType())) return null;
            return tokens[1] + Delimiter.HYPHEN + tokens[2];
        }else return null;
    }
    
	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	public boolean isMoveInProgress() {
		return getIndicatorType().equals(GtsIndicatorType.MOVE_IN_PROGRESS);
	}
	
	public boolean isMoveStatus() {
		return getIndicatorType().equals(GtsIndicatorType.MOVE_STATUS);
	}

	public Set<GtsMoveCondition> getMoveConditions() {
		return this.moveConditions;
	}

	public void setMoveConditions(Set<GtsMoveCondition> conditions) {
		this.moveConditions = conditions;
	}

	public Set<GtsDecisionPointCondition> getDecisionPointConditions() {
		return this.decisionPointConditions;
	}

	public void setDecisionPointConditions(Set<GtsDecisionPointCondition> conditions) {
		this.decisionPointConditions = conditions;
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getIndicatorType(),getId().getIndicatorName(),getIndicatorValue());
	}
	
	private static final class DataPointComparator implements Comparator<String> {

		public int compare(String object1, String object2) {
			if(StringUtils.isEmpty(object1) && StringUtils.isEmpty(object2)) return 0;
			if(StringUtils.isEmpty(object2)) return 1;
			if(StringUtils.isEmpty(object1)) return -1;
			
			int index1 = object1.lastIndexOf(Delimiter.HYPHEN);
			int index2 = object2.lastIndexOf(Delimiter.HYPHEN);
			
			String firstString1 = index1 == -1? object1: object1.substring(0, index1);
			String firstString2 = index1 == -1? object1: object1.substring(0, index1);
			
			int result = firstString1.compareTo(firstString2);
			if(result != 0) return result;
			
			String lastString1 = index1 == -1 ? "" : object1.substring(index1 + 1);
			String lastString2 = index2 == -1 ? "" : object2.substring(index2 + 1);
			
			Integer num1 = NumberUtils.toInt(lastString1, -1);
			Integer num2 = NumberUtils.toInt(lastString2, -1);
			
			return num1.compareTo(num2);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GtsIndicator other = (GtsIndicator) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public static Comparator<String> DATA_POINT_COMPARATOR = new DataPointComparator();

}
