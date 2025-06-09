package com.honda.ahm.lc.vdb.entity;

import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import com.honda.ahm.lc.vdb.util.Constants;

@Entity
@Immutable
@Subselect(value="select  a.ACTUAL_TIMESTAMP as actual_timestamp, a.product_id as product_id,b.line_id as line_id "
		+ "         from GAL215TBX a \r\n"
		+ "         join  gal214tbx b on a.process_point_id = b.PROCESS_POINT_ID")
public class ProductLastStatusDetails  implements IProductDetails {

	@Id
	@Column(name="product_id")
	private String productId;
	
	@Column(name="actual_timestamp")
	private Timestamp actualTimestamp;
	
	@Column(name="line_id")
	private String lineId;
	
	


	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	

	

	

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actualTimestamp, productId, lineId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductLastStatusDetails other = (ProductLastStatusDetails) obj;
		return Objects.equals(actualTimestamp, other.actualTimestamp) && Objects.equals(productId, other.productId)
				&& Objects.equals(lineId, other.lineId);
	}

	@Override
	public String toString() {
		return "ProductLastStatusDetails [id=" + productId + ", actualTimestamp=" + actualTimestamp + ", processPointId="
				+ productId + "]";
	}

	@Override
	public String getUniqueKey() {
		// TODO Auto-generated method stub
		 return getProductId() + "" + Constants.SEPARATOR + "" + getLineId();
	}

	
	
	
	

	
	
	
	
	
	
	
	
	

}
