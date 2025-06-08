package com.honda.galc.dto;

public class DataValidationDto implements IDto{

	@DtoTag(outputName = "URL")
	private String url;
	@DtoTag(outputName = "SITENAME")
	private String siteName;
	@DtoTag(outputName = "STATUS")
	private String status;
	@DtoTag(outputName = "COMMENT")
	private String comment;
	
	public DataValidationDto(String siteName, String url,String status,String comment){
		this.url = url;
		this.siteName = siteName;
		this.status = status;
		this.comment = comment;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	
}
