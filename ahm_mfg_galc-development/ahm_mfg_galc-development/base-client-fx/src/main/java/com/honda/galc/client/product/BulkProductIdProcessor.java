package com.honda.galc.client.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.utils.ProductTypeUtil;

public class BulkProductIdProcessor extends AbstractProductIdProcessor {
	protected List<ProductSearchResult> productSearchResults;

	private static final int SINGLE_BATCH = 1;
	private static final int SMALL_BATCH = 4;
	private static final int MEDIUM_BATCH = 11;
	private static final int LARGE_BATCH = 51;
	private static final int LARGEST_BATCH = 101;


	public BulkProductIdProcessor(BulkProductController productController) {
		super(productController);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processInputNumber(ProductEvent event) {
		if (null != event) {
			if(!isAssociateIdSelected()) return;
			QiProgressBar qiProgressBar = null;
			try {
				productSearchResults = new ArrayList<ProductSearchResult>();
				List<BaseProduct> products = new ArrayList<BaseProduct>();
				getProductController().getLogger().info("Processing product: " + event.getTargetObject());
				qiProgressBar = QiProgressBar.getInstance("Please wait. Number of products: " + products.size(), " Validating.","Validating", getProductController().getView().getMainWindow().getStage(),true);	
				qiProgressBar.showMe();		
				if(event.getTargetObject() instanceof String) {
					setProductIdInputNumber(event);
					List<String> productIds = new ArrayList<String>();
					productIds.add((String) event.getTargetObject());
					products = (validateAndCreateBaseProduct(productIds));
					BaseProduct product = products.get(0);
					ProductSearchResult thisResult = new ProductSearchResult(product);
					if(product != null && product instanceof Frame)  {
						Frame f = (Frame)product;
						if(f != null && f.getAfOnSequenceNumber() != null)  {
							thisResult.setAfOnSeqNum(((Frame)product).getAfOnSequenceNumber());
						}
						thisResult.setProductionLot(((Frame)product).getProductionLot());
					}
					productSearchResults.add(thisResult);
				}else if(event.getTargetObject() instanceof ArrayList){
					List<String> productIds = (List<String>) event.getTargetObject();
					products = validateAndCreateBaseProduct(productIds);
					boolean isFrame = false;
					if(products != null && !products.isEmpty() && products.get(0) != null && products.get(0) instanceof Frame)  {
						isFrame = true;
					}
					for(BaseProduct product : products) {
						ProductSearchResult thisResult = new ProductSearchResult(product);
						if(isFrame)  {
							Frame f = (Frame)product;
							if(f != null && f.getAfOnSequenceNumber() != null)  {
								thisResult.setAfOnSeqNum(((Frame)product).getAfOnSequenceNumber());
							}
							thisResult.setProductionLot(((Frame)product).getProductionLot());
						}
						productSearchResults.add(thisResult);
					}
				}else { return; }

				if (products == null) {
					return;
				}
				validateProduct();
				
			} catch (Exception e) {
				getProductController().getLogger().error("Exception: Unable to process input numbers: " + e.getMessage());
			} finally {
				if(qiProgressBar != null)  {
					qiProgressBar.closeMe();
				}
			}
		}
	}

	protected List<BaseProduct> validateAndCreateBaseProduct(List<String> productIds) {
		List<BaseProduct> products = new ArrayList<BaseProduct>();
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		int productsToCreate = productIds.size();
		int batchSize = 0; 
		int currentLocation = 0;

		while(productsToCreate > 0) {
			batchSize = getBatchSize(productsToCreate);

			List<String> productIdList = removeLeadingVinChars(getProdcutListForQuery(productIds, batchSize, currentLocation));
			productList.addAll(getProductController().getModel().findProducts(productIdList, 0, batchSize));

			if(getProductController().isDcProduct()) {
				if(getProductController().isDcStation()) {
					//get products by DC serial number
					if(productList.size() != batchSize) {
						Map<String, BaseProduct> map = productList.stream().collect(Collectors.toMap(BaseProduct::getProductId, c -> c));
						Map<String, BaseProduct> mcmap = new HashMap<String, BaseProduct>();
						for(BaseProduct product : productList) {
							mcmap.put(((DieCast) product).getMcSerialNumber(),product);
						}

						for(String productId : productIdList) {
							if(!map.containsKey(productId) && !mcmap.containsKey(productId)) {
								DieCast product = (DieCast) createBaseProduct(productId);
								product.setDcSerialNumber(productId);
								map.put(productId, product);
							}
						}
						productList.clear();
						productList.addAll(map.values());
					}
				} else {
					//get products by MC number
					if(productList.size() != batchSize) {
						Map<String, BaseProduct> map = productList.stream().collect(Collectors.toMap(BaseProduct::getProductId, c -> c));
						Map<String, BaseProduct> mcmap = new HashMap<String, BaseProduct>();
						for(BaseProduct product : productList) {
							mcmap.put(((DieCast) product).getMcSerialNumber(),product);
						}
						
						for(String productId : productIdList) {
							if(!map.containsKey(productId) && !mcmap.containsKey(productId)) {
								DieCast product = (DieCast) createBaseProduct(productId);
								product.setMcSerialNumber(productId);
								map.put(productId, product);
							}
						}
						productList.clear();
						productList.addAll(map.values());
					}
				}
			} else {
				//get products by product Id
				if(productList.size() != batchSize) {
					Map<String, BaseProduct> map = productList.stream().collect(Collectors.toMap(BaseProduct::getProductId, c -> c));

					for(String productId : productIdList) {
						if(!map.containsKey(productId)) {
							map.put(productId, createBaseProduct(productId));
						}
					}
					productList.clear();
					productList.addAll(map.values());
				}
			}

			products.addAll(productList);
			productList.clear();
			productsToCreate -= batchSize;
			currentLocation += batchSize;
		}
		return products;
	}

	public List<String> removeLeadingVinChars(List<String> productIds) {
		getProductController().getLogger().info("Start processing InputNumbers");
		if(getProductController().getModel().getProperty().isRemoveIEnabled() && ProductType.FRAME.name().equals(getProductController().getModel().getProductType())) {
			String leadingVinChars = getProductController().getView().getApplicationPropertyBean().getLeadingVinCharsToRemove();
			if(StringUtils.isNotEmpty(leadingVinChars)){
				String[] vinChars = leadingVinChars.trim().split(",");
				List<String> newIds = new ArrayList<String>();
				
				for(String prodId : productIds) {
					for(String c : vinChars) {
						newIds.add(prodId.toUpperCase().startsWith(c) ? prodId.substring(c.length()) : prodId);
					}
				}
				return newIds;
			}
		}
		return productIds;
	}
	
	private BaseProduct createBaseProduct(String productId) {
		return ProductTypeUtil.createProduct( 
				getProductController().getProductTypeData().getProductType().getProductName(), productId);
	}

	private List<String> getProdcutListForQuery(List<String> productIds, int batchSize, int currentLocation) {
		List<String> productIdList = new ArrayList<String>();
		for(int y = currentLocation, x = batchSize; x > 0; x--, y++) {
			if(productIds.get(y) != null) {
				productIdList.add(productIds.get(y));
			}
		}
		return productIdList;
	}

	public boolean validateInputNumber(String inputNumber) {
		messages = validate(inputNumber);
		return (messages.isEmpty());
	}

	protected int getBatchSize(int resultSize) {
		int batchSize = SINGLE_BATCH;
		if (resultSize >= LARGEST_BATCH) {
			batchSize = LARGEST_BATCH;
		} else if (resultSize >= LARGE_BATCH) {
			batchSize = LARGE_BATCH;
		} else if (resultSize >= MEDIUM_BATCH) {
			batchSize = MEDIUM_BATCH;
		} else if (resultSize >= SMALL_BATCH) {
			batchSize = SMALL_BATCH;
		}
		return batchSize;
	}

	protected void setProductIdInputNumber(ProductEvent event) {
		if (null!=event) {
			getProductController().getView().getInputPane().getProductIdField().setText((String)event.getTargetObject());
		}
	}

	public void validateProduct(){
	} 
	
	public boolean isAssociateIdSelected() {
		return true;
	}
}