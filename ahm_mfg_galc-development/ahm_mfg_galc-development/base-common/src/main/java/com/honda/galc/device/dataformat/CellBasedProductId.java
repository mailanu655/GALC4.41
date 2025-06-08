package com.honda.galc.device.dataformat;


public class CellBasedProductId extends ProductId {
	public CellBasedProductId() {
		super();
	}

	private static final long serialVersionUID = 1L;
	protected static final String CELL_TYPE_OUT = "OUT";
	protected static final String CELL_TYPE_IN = "IN";
	protected String cellType = null;
	private String productId;

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	private String getCellTypeString() {
		return cellType == null
				? ""
				: cellType;
	}

	public boolean isCellInProduct() {
		return isInputData() && getCellTypeString().equals(CellBasedProductId.CELL_TYPE_IN);
	}

	public boolean isCellOutProduct() {
		return isInputData() && getCellTypeString().equals(CellBasedProductId.CELL_TYPE_OUT);
	}
}
