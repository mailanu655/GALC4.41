package com.honda.galc.entity.mdrs;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 * 
 * @version 2 Fredrick Yessaian
 * Sep 10, 2014
 */
@Entity
@Table(name="TBLKNOWLEDGESTATUS", schema="VIOS")
public class KnowledgeStatus extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private KnowledgeStatusId id;

	@Column(name="BODY_LOC_NO", nullable=false, length=4)
	private String bodyLocNo;

	@Column(name="DAYSFLAG" , nullable=false, length=1)
	private String daysflag;

	@Column(name="ENTEREDBY_UID", nullable=false, length=7)
	private String enteredbyUid;

	@Column(name="KNOWLEDGE_NOTE_TEXT" , length=250)
	private String knowledgeNoteText;

	@Column(name="KNOWLEDGESTATUS_NO" , nullable=false)
	private int knowledgeStatusNo;

	@Column(name="PROCDESC", nullable=false, length=35)
	private String processDesc;

	@Column(name="PROCESS_NO", nullable=false, length=5)
	private String processNo;

	@Column(name="PROCLINENO", nullable=false, length=1)
	private String processLineNo;

	@Column(name="PROCTEAM", nullable=false, length=4)
	private String processTeam;

	@Column(name="PRODRATE")
	private int prodrate;

    public KnowledgeStatus() {
    }

	public KnowledgeStatusId getId() {
		return this.id;
	}

	public void setId(KnowledgeStatusId id) {
		this.id = id;
	}

	public String getBodyLocNo() {
		return StringUtils.trim(bodyLocNo);
	}

	public void setBodyLocNo(String bodyLocNo) {
		this.bodyLocNo = bodyLocNo;
	}

	public String getDaysflag() {
		return StringUtils.trim(daysflag);
	}

	public void setDaysflag(String daysflag) {
		this.daysflag = daysflag;
	}

	public String getEnteredbyUid() {
		return StringUtils.trim(enteredbyUid);
	}

	public void setEnteredbyUid(String enteredbyUid) {
		this.enteredbyUid = enteredbyUid;
	}

	public String getKnowledgeNoteText() {
		return StringUtils.trim(knowledgeNoteText);
	}

	public void setKnowledgeNoteText(String knowledgeNoteText) {
		this.knowledgeNoteText = knowledgeNoteText;
	}

	public int getKnowledgeStatusNo() {
		return knowledgeStatusNo;
	}

	public void setKnowledgeStatusNo(int knowledgeStatusNo) {
		this.knowledgeStatusNo = knowledgeStatusNo;
	}

	public String getProcessDesc() {
		return StringUtils.trim(processDesc);
	}

	public void setProcessDesc(String processDesc) {
		this.processDesc = processDesc;
	}

	public String getProcessNo() {
		return StringUtils.trim(processNo);
	}

	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}

	public String getProcessLineNo() {
		return StringUtils.trim(processLineNo);
	}

	public void setProcessLineNo(String procrocessLineNo) {
		this.processLineNo = procrocessLineNo;
	}

	public String getProcessTeam() {
		return StringUtils.trim(processTeam);
	}

	public void setProcessTeam(String processTeam) {
		this.processTeam = processTeam;
	}

	public int getProdrate() {
		return prodrate;
	}

	public void setProdrate(int prodrate) {
		this.prodrate = prodrate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bodyLocNo == null) ? 0 : bodyLocNo.hashCode());
		result = prime * result
				+ ((daysflag == null) ? 0 : daysflag.hashCode());
		result = prime * result
				+ ((enteredbyUid == null) ? 0 : enteredbyUid.hashCode());
		result = prime
				* result
				+ ((knowledgeNoteText == null) ? 0 : knowledgeNoteText
						.hashCode());
		result = prime * result + knowledgeStatusNo;
		result = prime * result
				+ ((processDesc == null) ? 0 : processDesc.hashCode());
		result = prime * result
				+ ((processNo == null) ? 0 : processNo.hashCode());
		result = prime * result
				+ ((processTeam == null) ? 0 : processTeam.hashCode());
		result = prime
				* result
				+ ((processLineNo == null) ? 0 : processLineNo.hashCode());
		result = prime * result + prodrate;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof KnowledgeStatus)) {
			return false;
		}
		KnowledgeStatus other = (KnowledgeStatus) obj;
		if (bodyLocNo == null) {
			if (other.bodyLocNo != null) {
				return false;
			}
		} else if (!bodyLocNo.equals(other.bodyLocNo)) {
			return false;
		}
		if (daysflag == null) {
			if (other.daysflag != null) {
				return false;
			}
		} else if (!daysflag.equals(other.daysflag)) {
			return false;
		}
		if (enteredbyUid == null) {
			if (other.enteredbyUid != null) {
				return false;
			}
		} else if (!enteredbyUid.equals(other.enteredbyUid)) {
			return false;
		}
		if (knowledgeNoteText == null) {
			if (other.knowledgeNoteText != null) {
				return false;
			}
		} else if (!knowledgeNoteText.equals(other.knowledgeNoteText)) {
			return false;
		}
		if (knowledgeStatusNo != other.knowledgeStatusNo) {
			return false;
		}
		if (processDesc == null) {
			if (other.processDesc != null) {
				return false;
			}
		} else if (!processDesc.equals(other.processDesc)) {
			return false;
		}
		if (processNo == null) {
			if (other.processNo != null) {
				return false;
			}
		} else if (!processNo.equals(other.processNo)) {
			return false;
		}
		if (processTeam == null) {
			if (other.processTeam != null) {
				return false;
			}
		} else if (!processTeam.equals(other.processTeam)) {
			return false;
		}
		if (processLineNo == null) {
			if (other.processLineNo != null) {
				return false;
			}
		} else if (!processLineNo.equals(other.processLineNo)) {
			return false;
		}
		if (prodrate != other.prodrate) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getUserLogonIdNo(), getId().getProcessId(), getId().getModelYearDate(), 
				getId().getMtcModel(), getId().getExtractDate(),getId().getPlantLocCode(),  getId().getDeptCode(), 
				getId().getEnteredTstp(), getEnteredbyUid(), getKnowledgeNoteText(), getKnowledgeStatusNo(), 
				getProcessNo(), getProcessLineNo(), 
				getProcessTeam(), getProdrate(), getBodyLocNo(), getDaysflag());
	}
	

}