package com.honda.galc.dto;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Sep 21, 2015
 */
public class FrameInfoDto implements IDto {
	
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String productId = null;
	
	@DtoTag()
	private String productSpecCode = null;
	
	@DtoTag()
	private String fifCodes = null;
	
	@DtoTag()
	private String currentLine = null;
	
	@DtoTag()
	private long minsOnLine = -1L;
	
	public FrameInfoDto() {}
	
	public FrameInfoDto(String productId, String productSpecCode) {
		this.productId = productId;
		this.productSpecCode = productSpecCode;
	}
	
	public FrameInfoDto(String productId, String productSpecCode, String fifCodes) {
		this(productId, productSpecCode);
		this.fifCodes = fifCodes;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getFifCodes() {
		return fifCodes;
	}

	public void setFifCodes(String fifCodes) {
		this.fifCodes = fifCodes;
	}

	public String getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(String currentLine) {
		this.currentLine = currentLine;
	}

	public long getMinsOnLine() {
		return minsOnLine;
	}

	public void setMinsOnLine(long minsOnLine) {
		this.minsOnLine = minsOnLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentLine == null) ? 0 : currentLine.hashCode());
		result = prime * result
				+ ((fifCodes == null) ? 0 : fifCodes.hashCode());
		result = prime * result + (int) (minsOnLine ^ (minsOnLine >>> 32));
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
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
		FrameInfoDto other = (FrameInfoDto) obj;
		if (currentLine == null) {
			if (other.currentLine != null)
				return false;
		} else if (!currentLine.equals(other.currentLine))
			return false;
		if (fifCodes == null) {
			if (other.fifCodes != null)
				return false;
		} else if (!fifCodes.equals(other.fifCodes))
			return false;
		if (minsOnLine != other.minsOnLine)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateToString(this);
	}
}
