package com.honda.galc.webstart;

import java.util.Date;

public class WebStartDefaultBuild {
	
	private String cell;
	
	private String buildId;
	
	private String description;
	
	private Date modified;
	
	/**
	 * @param cell
	 * @param buildId
	 * @param description
	 */
	public WebStartDefaultBuild(String cell, String buildId, String description) {
		this.cell = cell;
		this.buildId = buildId;
		this.description = description;
	}
	
	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}
	
	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}
	
	/**
	 * @return the buildId
	 */
	public String getBuildId() {
		return buildId;
	}
	
	/**
	 * @return the cell
	 */
	public String getCell() {
		return cell;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		
		sb.append(": cell: ").append(this.cell);
		sb.append(", description: ").append(this.description);
		sb.append(", buildId: ").append(this.buildId);
		
		return sb.toString();
	}

}
