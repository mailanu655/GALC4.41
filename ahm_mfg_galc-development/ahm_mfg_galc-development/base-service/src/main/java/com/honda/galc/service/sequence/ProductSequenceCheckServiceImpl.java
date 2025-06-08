package com.honda.galc.service.sequence;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.service.ProductSequenceCheckService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Get the AF On Information 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 */
/**
 * 
 *    
 * @version 1.0
 * @author Zhiqiang Wang
 * @since July 02, 2014
 */
public class ProductSequenceCheckServiceImpl implements ProductSequenceCheckService {
	
	private final static String EMPTY_CODE = "0";
	private final static String LINE_IDS = "LINE_IDS";
	private final static String IS_IN_PROCESS_PRODUCT = "IS_IN_PROCESS_PRODUCT";

	@Autowired
	protected InProcessProductDao inProcessProductDao;
	@Autowired
	protected ProductSequenceDao productSequenceDao;
	
	/**
	 * get product sequence information from GAL176TBX or PRODUCT_SEQUENCE_TBX based on the flag "isInProcessProduct".
	 * @param data
	 * @return 
	 */
	@Override
	public 	DataContainer getProductSequenceDetail(DefaultDataContainer data) {
		String productType = data.getString(TagNames.PRODUCT_TYPE.name());
		String productId = data.getString(TagNames.PRODUCT_ID.name());
		String rawlineIds = data.getString(LINE_IDS);
		String[] lineIds = StringUtils.isBlank(rawlineIds) ? new String[0] : rawlineIds.split(",");
		Boolean isInProcessProduct = Boolean.parseBoolean(data.getString(IS_IN_PROCESS_PRODUCT));
		return getProductSequenceDetail(productType, productId, lineIds, isInProcessProduct);
	}

	/**
	 * get product sequence information from GAL176TBX or PRODUCT_SEQUENCE_TBX based on the flag "isInProcessProduct".
	 * Required Tags: PRODUCT_ID
	 * @param productType product type which is used to decide where the product is stored 
	 * @param productId product ID
	 * @param LineIds The line IDs the product is allowed to belong to 
	 * @param isInProcessProduct if true, the sequence information will be in GAL176TBX, otherwise it is in PRODUCT_SEQUENCE_TBX
	 * @return 
	 */
	@Override
	public DataContainer getProductSequenceDetail(String productType, String productId, String[] lineIds, Boolean isInProcessProduct) {
		DataContainer result = new DefaultDataContainer();
		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);
		BaseProduct product = productDao.findByKey(productId);
		if (product == null) {
			setEmptyCode(result);
			return result;
		}
		
		// Check if the in process product is in the correct lines.
		if (lineIds.length > 0) {
			boolean isInvalidLine = true;
			for (String line : lineIds) {
				String checkLineId = line.trim();
				if (StringUtils.isNotBlank(checkLineId)
						&& StringUtils.equalsIgnoreCase(checkLineId, product.getTrackingStatus())) {
					isInvalidLine = false;
					break;
				}
			}
			if (isInvalidLine) {
				return setResultStatus(
						result,
						ProductSequenceCheckErrorCode.ERROR_PRODUCT_IN_INVALID_LINE);
			}
		}
		
		Object nextProduct = null;
		Object preProduct = null;
		if(isInProcessProduct) {
			nextProduct = inProcessProductDao.findByKey(productId);
			List<?> preProductsList = inProcessProductDao.findByNextProductId(productId);
			//Check if there is wrong data in the link list of the in process product table.
			if (preProductsList != null && preProductsList.size() > 1) {
				return setResultStatus(
						result,
						ProductSequenceCheckErrorCode.ERROR_MULTIPLE_PREV_IN_PROCESS_PRODUCT);
			} else {
				preProduct = (preProductsList == null || preProductsList.isEmpty()) ? null : preProductsList.get(0);
			}
		} else {
			ProductSequence productSequence = productSequenceDao.findByProductId(productId);
			if (productSequence == null) {
				setEmptyCode(result);
				return result;
			}
			nextProduct = productSequenceDao.findNextExpectedProductId(productSequence);
			preProduct = productSequenceDao.findPrevProductId(productSequence);
		}
		
		prepareOutput(result, product, nextProduct, preProduct);
		
		return result;
	}
	
	/**
	 * prepare the output based on the product, its next product and previous product
	 * @param result the returned data container
	 * @param currentProduct 
	 * @param nextProduct 
	 * @param prevProduct 
	 */
	private void prepareOutput(DataContainer result, BaseProduct currentProduct, Object nextProduct, Object prevProduct) {
		if (nextProduct == null) {
			setEmptyCode(result);
			return;
		}
		
		result.put(DataContainerTag.PRODUCT_ID, currentProduct.getId());
		result.put(DataContainerTag.PRODUCT_SPEC_CODE,
				currentProduct.getProductSpecCode());
		//Get the AF ON Sequence if the product is VIN
		if (currentProduct instanceof Frame) {
			Frame frame = (Frame) currentProduct;
			result.put(DataContainerTag.PRODUCT_SEQUENCE,
					frame.getAfOnSequenceNumber());
		}
		
		if (nextProduct instanceof InProcessProduct) {
			result.put(
					DataContainerTag.NEXT_PRODUCT_ID,
					((InProcessProduct) nextProduct).getNextProductId() == null ? EMPTY_CODE: ((InProcessProduct) nextProduct).getNextProductId());

			// Get the previous in process product id.
			if (prevProduct == null) {
				// If there is no previous in process product, put "0" in the
				// response
				result.put(DataContainerTag.PREV_PRODUCT_ID, EMPTY_CODE);
			} else {
				result.put(DataContainerTag.PREV_PRODUCT_ID,
						((InProcessProduct) prevProduct).getProductId());
			}
		} else if (nextProduct instanceof ProductSequence) {
			result.put(
					DataContainerTag.NEXT_PRODUCT_ID,
					nextProduct == null ? EMPTY_CODE: ((ProductSequence) nextProduct).getId().getProductId());

			// Get the previous in process product id.
			if (prevProduct == null) {
				// If there is no previous in process product, put "0" in the
				// response
				result.put(DataContainerTag.PREV_PRODUCT_ID, EMPTY_CODE);
			} else {
				result.put(DataContainerTag.PREV_PRODUCT_ID, ((ProductSequence) prevProduct).getId().getProductId());
			}
		}
		
		return;
	}

	/**
	 * Set error code, error message and data collection complete 
	 * @param result
	 * @param errorCode
	 * @return
	 */
	private DataContainer setResultStatus(DataContainer result,
			ProductSequenceCheckErrorCode errorCode) {
		if (errorCode != ProductSequenceCheckErrorCode.NORMAL_REPLY) {
			result.put(DataContainerTag.DATA_COLLECTION_COMPLETE,
					DataCollectionComplete.NG);
		} else {
			result.put(DataContainerTag.DATA_COLLECTION_COMPLETE,
					DataCollectionComplete.OK);
		}
		result.put(DataContainerTag.ERROR_MESSAGE, errorCode.getDescription());
		result.put(DataContainerTag.ERROR_CODE, errorCode.getCode());
		return result;
	}

	/**
	 * Set the response to all "0"
	 * @param result
	 */
	private void setEmptyCode(DataContainer result) {
		result.put(DataContainerTag.PRODUCT_ID, EMPTY_CODE);
		result.put(DataContainerTag.PRODUCT_SPEC_CODE, EMPTY_CODE);
		result.put(DataContainerTag.PRODUCT_SEQUENCE, EMPTY_CODE);
		result.put(DataContainerTag.PREV_PRODUCT_ID, EMPTY_CODE);
		result.put(DataContainerTag.NEXT_PRODUCT_ID, EMPTY_CODE);
	}

}
