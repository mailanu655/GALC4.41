package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author hcm_adm_015809
 *
 */

@Embeddable
public class ProductShippingId implements Serializable {
	    @Column(name = "PRODUCT_ID")
	    private String productId;
	    
		@Column(name="TRAILER_NUMBER")
		private String trailerNumber;

	    @Column(name = "PROCESS_POINT_ID")
	    private String processPointId = "";

	   
	    private static final long serialVersionUID = 1L;

	    public ProductShippingId() {
	        super();
	    }

		public String getProductId() {
			return StringUtils.trim(productId);
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getTrailerNumber() {
			return StringUtils.trim(trailerNumber);
		}

		public void setTrailerNumber(String trailerNumber) {
			this.trailerNumber = trailerNumber;
		}

		public String getProcessPointId() {
			return StringUtils.trim(processPointId);
		}

		public void setProcessPointId(String processPointId) {
			this.processPointId = processPointId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
			result = prime * result + ((productId == null) ? 0 : productId.hashCode());
			result = prime * result + ((trailerNumber == null) ? 0 : trailerNumber.hashCode());
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
			ProductShippingId other = (ProductShippingId) obj;
			if (processPointId == null) {
				if (other.processPointId != null)
					return false;
			} else if (!processPointId.equals(other.processPointId))
				return false;
			if (productId == null) {
				if (other.productId != null)
					return false;
			} else if (!productId.equals(other.productId))
				return false;
			if (trailerNumber == null) {
				if (other.trailerNumber != null)
					return false;
			} else if (!trailerNumber.equals(other.trailerNumber))
				return false;
			return true;
		}
	
	    
}
