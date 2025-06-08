package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the PROXSCANNER_MAPPING_TBX database table.
 * 
 */
@Entity
@Table(name="PROXSCANNER_MAPPING_TBX")
public class ProxCard extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CARD_NUMBER", nullable=false)
	private Long cardNumber;

	@Column(name="USER_ID", length=11)
	private String userId;

	public ProxCard() {}
	
	public Long getId() {
		return getCardNumber();
	}

	public Long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getUserId() {
		return StringUtils.trim(userId);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		Integer result = 1;
		result = prime * result + cardNumber.intValue();
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		ProxCard other = (ProxCard) obj;
		if (cardNumber != other.cardNumber)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getCardNumber(), getUserId());
	}
}