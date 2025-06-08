/**
 * 
 */
package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import org.apache.commons.lang.StringUtils;

/**
 * @author VF031824
 *
 */
@Entity
@Table(name = "EXT_REQUIRED_PART_SPEC_TBX")
public class ExtRequiredPartSpec extends AuditEntry{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PartId id;

	@Column(name = "PARSE_STRATEGY")
	private String parseStrategy;

	@Column(name = "PARSE_INFORMATION")
	private String parserInformation;
	
	@Column(name = "PART_GROUP")
	private String partGroup;

	public ExtRequiredPartSpec() {
		super();
	}
	
    public ExtRequiredPartSpec(PartId id) {
		this();
		this.id = id;
	}

	public PartId getId() {
		return this.id;
	}

	public void setId(PartId id) {
		this.id = id;
	}

	public String getParseStrategy() {
		return StringUtils.trim(this.parseStrategy);
	}

	public void setParseStrategy(String parseStragety) {
		this.parseStrategy = parseStragety;
	}

	public String getParserInformation() {
		return StringUtils.trim(this.parserInformation);
	}	

	public void setParserInformation(String parserInformation) {
		this.parserInformation = parserInformation;
	}
	
	public String getPartGroup() {
		return StringUtils.trim(this.partGroup);
	}
	
	public void setPartGroup(String partGroup) {
		this.partGroup = partGroup;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((parseStrategy == null) ? 0 : parseStrategy.hashCode());
		result = prime
				* result
				+ ((parserInformation == null) ? 0 : parserInformation.hashCode());
		result = prime
				* result
				+ ((partGroup == null) ? 0 : partGroup.hashCode());
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
		ExtRequiredPartSpec other = (ExtRequiredPartSpec) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (parseStrategy == null) {
			if (other.parseStrategy != null)
				return false;
		} else if (!parseStrategy.equals(other.parseStrategy))
			return false;
		if (parserInformation == null) {
			if (other.parserInformation != null)
				return false;
		} else if (!parserInformation.equals(other.parserInformation))
			return false;
		if (partGroup == null) {
			if (other.partGroup != null)
				return false;
		} else if (!partGroup.equals(other.partGroup))
			return false;
	return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(",");
		builder.append(StringUtils.trim(parseStrategy)).append(",");
		builder.append(parserInformation).append(",");
		builder.append(partGroup);
		
		return builder.toString();
	}
}