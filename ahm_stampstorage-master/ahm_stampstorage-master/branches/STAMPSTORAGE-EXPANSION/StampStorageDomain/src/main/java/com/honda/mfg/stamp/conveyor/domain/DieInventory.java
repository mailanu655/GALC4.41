package com.honda.mfg.stamp.conveyor.domain;

import com.honda.shared.domain.EntityBase;

/**
 * User: vcc30690 Date: 7/19/11
 */
public class DieInventory extends EntityBase {

	private String dieName;

	private Long quantity;

	private Long holdQuantity;

	private Long inspectionRequiredQuantity;

	public DieInventory(long id) {
		super(id);
	}

	public String getDieName() {
		return dieName;
	}

	public void setDieName(String dieName) {
		this.dieName = dieName;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getHoldQuantity() {
		return this.holdQuantity;
	}

	public void setHoldQuantity(Long quantity) {
		this.holdQuantity = quantity;
	}

	public Long getInspectionRequiredQuantity() {
		return inspectionRequiredQuantity;
	}

	public void setInspectionRequiredQuantity(Long quantity) {
		this.inspectionRequiredQuantity = quantity;
	}
}
