package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */

@Entity
@Table(name="GAL730TBX")
public class LetProgramCategory extends AuditEntry {
	
	@EmbeddedId
	private LetProgramCategoryId id;

	@Column(name="PGM_CATEGORY_NAME")
	private String pgmCategoryName;

	@Column(name="BG_COLOR")
	private String bgColor;


	private static final long serialVersionUID = 1L;

	public LetProgramCategory() {
		super();
	}
	

	public LetProgramCategory(LetProgramCategoryId id) {
		super();
		this.id = id;
	}


	public LetProgramCategory(LetProgramCategoryId id, String pgmCategoryName,
			String bgColor) {
		super();
		this.id = id;
		this.pgmCategoryName = pgmCategoryName;
		this.bgColor = bgColor;
	}


	public LetProgramCategoryId getId() {
		return id;
	}

	public void setId(LetProgramCategoryId id) {
		this.id = id;
	}

	public String getPgmCategoryName() {
		return StringUtils.trim(pgmCategoryName);
	}

	public void setPgmCategoryName(String pgmCategoryName) {
		this.pgmCategoryName = pgmCategoryName;
	}

	public String getBgColor() {
		return StringUtils.trim(bgColor);
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	
	@Override
	public String toString() {
		return toString(getId().getInspectionDeviceType(),getId().getCriteriaPgmAttr());
}

}
