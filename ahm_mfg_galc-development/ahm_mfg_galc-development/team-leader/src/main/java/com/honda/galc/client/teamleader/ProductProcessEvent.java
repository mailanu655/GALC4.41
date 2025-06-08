package com.honda.galc.client.teamleader;

import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.InstalledPart;

public class ProductProcessEvent {
	private Object source;
	private String productId;
	private ProductType productType;
	private List<InstalledPart> productBuildResult;
	private State state;
	
	public enum State {
		LOAD, COMPLETE, ERROR, VALID_PRODUCT
	};
	
	
	public ProductProcessEvent(State state, Object source) {
		super();
		this.state = state;
		this.source = source;
	}

	public ProductProcessEvent(ProductType productType,
			List<InstalledPart> productBuildResult, State state) {
		super();
		this.productType = productType;
		this.productBuildResult = productBuildResult;
		this.state = state;
	}

	public ProductProcessEvent(String productId, ProductType productType, State state) {
		super();
		this.productId = productId;
		this.productType = productType;
		this.state = state;
	}

	public ProductProcessEvent(String productId, ProductType productType,
			List<InstalledPart> productBuildResult, State state) {
		super();
		this.productId = productId;
		this.productType = productType;
		this.productBuildResult = productBuildResult;
		this.state = state;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public List<InstalledPart> getProductBuildResult() {
		return productBuildResult;
	}

	public void setProductBuildResult(
			List<InstalledPart> productBuildResult) {
		this.productBuildResult = productBuildResult;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
	
	public boolean isSource(Object object) {
		return source == object;
	}
	
	public boolean isState(State state) {
		return this.state == state;
	}
	
	public boolean isStateFromSource(State state,Object object) {
		return isState(state) && isSource(object);
	}

}
