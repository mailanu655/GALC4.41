package com.honda.galc.oif.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * <h3>TableDefinitions Class description</h3>
 * <p> TableDefinitions description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jun 4, 2012
 *
 *
 */
public class TableDefinitions {

	private static TableDefinitions baseProductTables = new TableDefinitions();
	private static TableDefinitions baseResultTables = new TableDefinitions();
	public static TableDefinitions productTables = new TableDefinitions();
	public static TableDefinitions engineTables = new TableDefinitions();
	public static TableDefinitions productionDateTables = new TableDefinitions();
	public static TableDefinitions headTables = new TableDefinitions();
	public static TableDefinitions blockTables = new TableDefinitions();
	public static TableDefinitions knuckleTables = new TableDefinitions();
	public static TableDefinitions mbpnTables = new TableDefinitions();
	public static TableDefinitions subProductTables = new TableDefinitions();
	static {
		baseProductTables.put("GAL198_HIST_TBX", "Measurement");
		baseProductTables.put("GAL198TBX", "Measurement");
		baseProductTables.put("GAL176TBX", "In Progress Product");
		baseProductTables.put("GAL147TBX", "Hold Result");
		baseProductTables.put("GAL136TBX", "Exceptional Out");
		baseProductTables.put("PRODUCT_SEQUENCE_TBX", "Product Sequence");
		
		baseResultTables.put("GAL125TBX", "Defect Result");
		baseResultTables.put("GAL224TBX", "Repari Result");
		baseResultTables.put("GAL222TBX", "Reuse Product Result");
		baseResultTables.put("GAL191TBX", "IPP Tag");
		
		productTables.put("GAL215TBX", "Product History");
		productTables.put("GAL185_HIST_TBX", "Installed Part");
		productTables.put("GAL185TBX", "Installed Part");
		productTables.add(baseProductTables);
		productTables.add(baseResultTables);
		
		headTables.put("HEAD_BUILD_RESULTS_TBX", "Head Build Result","HEAD_ID");
		headTables.put("HEAD_HISTORY_TBX", "Head History","HEAD_ID");
		headTables.add(baseProductTables);
		headTables.add(baseResultTables);
		headTables.put("MC_SHIPPING_TBX", "AEP Shipping Head");
		headTables.put("HEAD_TBX", "Head","HEAD_ID");
		
		blockTables.put("BLOCK_BUILD_RESULTS_TBX", "Block Build Result","BLOCK_ID");
		blockTables.put("BLOCK_HISTORY_TBX", "Block History","BLOCK_ID");
		blockTables.add(baseProductTables);
		blockTables.add(baseResultTables);
		blockTables.put("MC_SHIPPING_TBX", "AEP Shipping Block");
		blockTables.put("BLOCK_TBX", "Block","BLOCK_ID");
		
		engineTables.put("PRODUCT_CARRIER_TBX", "Product Carrier");
		engineTables.put("GAL141TBX", "Engine Firing Result","ENGINE_SERIAL_NO");
		engineTables.put("GAL108TBX", "Bearing Select Result");
		engineTables.put("GTS_PRODUCT_TBX", "GTS Product");
		engineTables.put("ENGINE_MANIFEST_TBX", "Engine Manifest","ENGINE_NO");
		engineTables.put("ENGINE_MANIFEST_HIST_TBX", "Engine Manifest History","ENGINE_NO");
		
		knuckleTables.put("GAL215TBX", "Product History");
		knuckleTables.put("GAL185TBX", "Installed Part");
		knuckleTables.put("GAL185_HIST_TBX", "Installed Part");
		knuckleTables.add(baseProductTables);
		knuckleTables.put("SKIPPED_PRODUCT_TBX", "Skipped Product");
		knuckleTables.put("SUB_PRODUCT_SHIPPING_DETAIL_TBX", "Sub Product Shipping Detail");
		
		productionDateTables.put("GAL118TBX", "Counter By Model Group");
		productionDateTables.put("GAL119TBX", "Counter By Product Spec");
		productionDateTables.put("GAL120TBX", "Counter By Production Lot");
		productionDateTables.put("GAL226TBX", "Daily Department Schedule");
		
		mbpnTables.add(productTables);
		mbpnTables.remove("GAL215TBX");
		
		subProductTables.add(productTables);
		subProductTables.put("SKIPPED_PRODUCT_TBX", "Skipped Product");
		subProductTables.put("SUB_PRODUCT_SHIPPING_DETAIL_TBX", "Sub Product Shipping Detail");
	}
	
	public TableDefinitions put(String tableName,String tableDescription) {
		tables.add(new TableDefinition(tableName,tableDescription));
		return this;
	}
	
	private void remove(String tableName) {
		Iterator<TableDefinition> iterator = tables.iterator();
		while(iterator.hasNext()){
			TableDefinition next = iterator.next();
			if(tableName.equals(next.getTableName()))
				iterator.remove();
		}
		
	}

	public void remove(String tableName,String tableDescription) {
		tables.remove(new TableDefinition(tableName,tableDescription));
	}
	
	public TableDefinitions put(String tableName,String tableDescription,String productIdName) {
		tables.add(new TableDefinition(tableName,tableDescription,productIdName));
		return this;
	}
	
	public TableDefinitions add(TableDefinitions tableDefinitions) {
		this.tables.addAll(tableDefinitions.getTables());
		return this;
	}
	
	private List<TableDefinition> tables = new ArrayList<TableDefinition>();
	
	
	public List<TableDefinition> getTables() {
		return tables;
	}

	public void setTables(List<TableDefinition> tables) {
		this.tables = tables;
	}


	public class TableDefinition {
	
		private String tableName;
		private String tableDescription;
		private String productIdName = "PRODUCT_ID";
		
		public TableDefinition(String tableName,String tableDescription) {
			this.tableName = tableName;
			this.tableDescription = tableDescription;
		}
		public TableDefinition(String tableName,String tableDescription,String productIdName) {
			this.tableName = tableName;
			this.tableDescription = tableDescription;
			this.productIdName = productIdName;
		}
		
		public String getTableName() {
			return this.tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public String getTableDescription() {
			return tableDescription;
		}
		public void setTableDescription(String tableDescription) {
			this.tableDescription = tableDescription;
		}
		public String getProductIdName() {
			return productIdName;
		}
		public void setProductIdName(String productIdName) {
			this.productIdName = productIdName;
		}
		private TableDefinitions getOuterType() {
			return TableDefinitions.this;
		}
	
	}
}
