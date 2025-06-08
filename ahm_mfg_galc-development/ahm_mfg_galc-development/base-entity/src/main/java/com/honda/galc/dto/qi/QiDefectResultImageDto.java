package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @version 1.0
 * @author Dylan Yang
 * @see
 *
 */
public class QiDefectResultImageDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName ="PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName ="DEFECT_RESULT_ID")
	private long defectResultId;
	
	@DtoTag(outputName ="REPAIR_ID")
	private long repairId;
	
	@DtoTag(outputName ="PART_DEFECT_COMBINATION")
	private String partDefectCombination;
	
	@DtoTag(outputName ="APPLICATION_ID")
	private String applicationId;
	
	@DtoTag(outputName ="IMAGE_URL")
	private String imageUrl;
	
	private Object url;
	
	public QiDefectResultImageDto() {
		super();
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
	}

	public long getRepairId() {
		return repairId;
	}

	public void setRepairId(long repairId) {
		this.repairId = repairId;
	}

	public String getPartDefectCombination() {
		return StringUtils.trimToEmpty(partDefectCombination);
	}

	public void setPartDefectCombination(String partDefectCombination) {
		this.partDefectCombination = partDefectCombination;
	}

	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getImageUrl() {
		return StringUtils.trimToEmpty(imageUrl);
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
	}
	
	public String getFilename() {
		return imageUrl == null ? "" : imageUrl.substring(1 + imageUrl.lastIndexOf('/')).replace("%20", " ");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + (int) defectResultId;
		result = prime * result + (int) repairId;
		result = prime * result + ((partDefectCombination == null) ? 0 : partDefectCombination.hashCode());
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		QiDefectResultImageDto other = (QiDefectResultImageDto) obj;
		return (defectResultId == other.defectResultId) 
				&& (repairId == other.repairId) 
				&& StringUtils.equals(productId, other.getProductId()) 
				&& StringUtils.equals(partDefectCombination, other.partDefectCombination)
				&& StringUtils.equals(applicationId, other.applicationId) 
				&& StringUtils.equals(imageUrl, other.imageUrl);
	}
	
	public String toString() {
		return "QiDefectResultImageDto [defectResultId=" + defectResultId + ", imageUrl=" + imageUrl + "]";
	}
}
