package com.honda.galc.dao.product;

import java.util.List;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.service.IDaoService;

/**
 * Interface to define the implementations for all the shipping transaction
 * @author Anuar Vasquez
 *
 */
public interface ShippingTransactionDao extends IDaoService<ShippingTransaction, String>
	{
	
	public List<ShippingTransaction> get50ATransactionVin( final Integer status
			, final Integer effectiveDate
			, final Character sendFlag
			, final String cccPartName			
			);
	
	/**
	 * find all 
	 * @param timeFrame60A
	 */
	List<ShippingTransaction> findAllNotConfirmedByCreateTimePassed(Integer timeFrame60A);
	}
