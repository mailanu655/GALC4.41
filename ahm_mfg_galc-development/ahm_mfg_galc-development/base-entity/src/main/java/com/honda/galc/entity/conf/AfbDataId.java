/**
 * 
 */
package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * @date Jan 3, 2012
 */
@Embeddable
public class AfbDataId implements Serializable {
	
	private static final long serialVersionUID = 8136354798481478450L;

	@Column(name = "MTC_MODEL")
	private String model;

	@Column(name = "MTC_TYPE")
	private String type;

	@Column(name = "MTC_OPTION")
	private String option;

	public AfbDataId() {
		super();
	}

	public String getModel() {
		return StringUtils.trim(this.model);
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getType() {
		return StringUtils.trim(this.type);
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getOption() {
		return StringUtils.trim(this.option);
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof AfbDataId)) {
			return false;
		}
		AfbDataId other = (AfbDataId) o;
		return this.model.equals(other.model)
			&& this.type.equals(other.type)
			&& this.option.equals(other.option);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.model.hashCode();
		hash = hash * prime + this.type.hashCode();
		hash = hash * prime + this.option.hashCode();
		return hash;
	}
}

