package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartLot;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProductLot;
/* *
 * 
 * <h3>ILotControlDbManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ILotControlDbManager description </p>
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
 * @author Paul Chou
 * Apr 9, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public interface ILotControlDbManager {

	public IExpectedProductManager getExpectedProductManger();
	
	/**
	 * Retrieve All Load Lot Control Rules from database for a process point
	 * @param processPointId
	 * @return
	 */
	public List<LotControlRule> loadLotControlRules(String processPointId);

	/**
	 * Retrieve all Engine Specs from database
	 * @return
	 */
	public List<EngineSpec> loadEngineSpecs();
	
	/**
	 * Retrieve all Frame Specs from database
	 * @return
	 */
	public List<FrameSpec> loadFrameSpecs(String modelYearCode);
	
	public BaseProduct findProductBySn(String productId);
	
	public Frame findFrameByAfOnSequenceNumber(int afOnSequenceNumber);

	/**
	 * Retrieve Load All Production Lots from database
	 * @return
	 */
	public List<ProductionLot> loadProductionLots();
	
	public ProductionLot getProductionLot(String productId);
	
	public ProductionLot getProductionLotByKey(String productId);
	
	public InProcessProduct findInProcessProductByKey(String productId);
	
	public InProcessProduct findInProcessProductByNextProductId(String nextProductId);
	
	public List<InProcessProduct> findPreviousProducts(String productId);
	
	/**
	 * Get a list of Installed part with same part name and part number
	 * @param currentPartName
	 * @param partNumber
	 * @return
	 */
	public List<InstalledPart> findDuplicatePartsByPartName(String currentPartName, String partNumber);
	
	/**
	 * Get a list of Installed part with same Product Id and Part number
	 * @param productId
	 * @param partNumber
	 * @return
	 */
	public List<InstalledPart> findDuplicatePartsByProductId(String productId, String partNumber);

	/**
	 * Check if the product has already been processed
	 * @param productId
	 * @param partNamesString
	 * @return
	 */
	public boolean isProcessed(String productId, List<String> partNamesList);
	
	/**
	 * Check product Id exist in database product table
	 * @param productId
	 * @return
	 */
	public BaseProduct confirmProductOnServer(String productId);
	
	/**
	 * Merger Property into property table 
	 * @param property
	 */
	public void saveProperty(ComponentProperty property);
	
	/**
	 * Merger properties into property table
	 * @param properties
	 */
	public void saveProperties(List<ComponentProperty> properties);
	
	/**
	 * Delete the specified property from property table
	 * @param property
	 */
	public void deleteProperty(ComponentProperty property);

	/**
	 * Retrieve a list of all the incoming expected product from database  
	 * @return
	 */
	public List<String> getIncomingProducts(DataCollectionState state);
	
	/**
	 * Save next expected Product to database
	 * @param nextProductId
	 */
	public void saveExpectedProductId(String nextProductId);
	
	public void saveExpectedProductId(String nextProductId, String lastProcessedProductId);
	/**
	 * Retrieve next Expected product Id
	 * @param state
	 * @return
	 */
	public String getNextExpectedProductId(String productId);
	
	/** refresh next expected product id into the ProcessProduct from cache or server 
	 * @param state
	 */
	public void updateNextExpectedProductId(ProcessProduct state);
	
	
	/** Set Product Count and Lot Size to State
	 * @param state and Product
	 */
	public void setCountLotSize(DataCollectionState state, BaseProduct product);
	
	
	
	public boolean isProductIdAheadOfExpectedProductId(String expectedProductId,String productId);
	/**
	 * Retrieve part names that are required but not yet installed
	 * @param subId 
	 * 
	 * @return
	 */
	public List<String> getMissingRequiredPart(DataCollectionState state, String productId, String subId);

	/**
	 * Retrieve sub product lot 
	 * @return
	 */
	public List<SubProductLot> loadSubProductLots();

	/**
	 * Retrieve the current preproduction lot
	 * @param processLocation
	 * @return
	 */
	public PreProductionLot findCurrentPreProductionLot(String processLocation);

	/**
	 * Retrieve all expected product of a process point from server 
	 * @return
	 */
	public List<ExpectedProduct> findAllExpectedProduct();

	public List<PreProductionLot> getUnSentPreproductionLot();
	
	public List<? extends BaseProduct> findProductOnServer(String SN);
	
	public Product createProduct(Product product);
	
	public PartLot findCurrentPartLot(String partName);
	
	public List<? extends Product> findProductByPartName(String lineId, int prePrintQty, int maxCycle, String ppid, String partName);
	
	public ProductSequence findProductSequenceByProductId(String productId);
	
	public ProductSequence findPreviousProductSequenceByProductId(String productId);
	
	/**
	 * find duplicated installed part serial number on a list of part names
	 * @param partNames
	 * @param partnumber
	 * @return
	 */
	public List<InstalledPart> findDuplicatePartsByPartNames(List<String> partNames, String partnumber);
}
