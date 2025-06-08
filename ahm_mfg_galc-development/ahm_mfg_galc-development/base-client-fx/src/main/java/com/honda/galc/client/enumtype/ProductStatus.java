package com.honda.galc.client.enumtype;

public enum ProductStatus {
	NO_DEFECT(0, "", Category.PROCESSABLE),
	DEFECT (1, "Product has active defects", Category.PROCESSABLE),
	SCRAPPED(2, "Product is scrapped", Category.CAN_NOT_PROCESS),
	DUPLICATE(3, "Product is already waiting to be processed", Category.EXCLUDE),
	NPF(4, "Defect marks as No Problem Found", Category.PROCESSABLE),
	NOT_EXIST(5, "Product does not exist", Category.CAN_NOT_PROCESS),
	REPAIRED(6, "Defects repaired, Category.PROCESSABLE", Category.PROCESSABLE),
	INVALID(7, "Invalid Product Id", Category.CAN_NOT_PROCESS);

	public enum Category {
		PROCESSABLE,
		REPAIRED,
		CAN_NOT_PROCESS,
		EXCLUDE,
		SCRAPPED;
	}

	private int id;
	private String msg;
	private Category category;
	
	private ProductStatus(int id, String msg, Category category) {
		this.id = id;
		this.msg = msg;
		this.category = category;
	}
	
	public static ProductStatus getById(int id) {
		for(ProductStatus e : values()) {
			if(e.id == id) {
				return e;
			}
		}
		return null;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getMessage() {
		return this.msg;
	}
	
	public Category getCategory() {
		return this.category;
	}
}
