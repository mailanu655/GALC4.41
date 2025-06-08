package com.honda.galc.client.teamleader.mcshipping;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.product.ProductShippingDao;
import com.honda.galc.entity.product.ProductShipping;

/**
 * <h3>ProductShippingManifestModel Class description</h3>
 * <p> ProductShippingManifestModel description </p>
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
 * @author Paul Chou<br>
 * Jul. 27, 2022
 */
public class ProductShippingManifestModel extends AbstractModel {
	private List<String> destinations = new ArrayList<String>();
	private List<String> trailers = new ArrayList<String>();
	private List<ProductShipping> dunnages = new ArrayList<ProductShipping>();
	private List<ProductShipping> productList = new ArrayList<ProductShipping>();

	public List<ProductShipping> getProductList() {
		return productList;
	}

	public List<ProductShipping> getDunnages() {
		return dunnages;
	}

	public List<String> getDestinations() {
		return destinations;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void findShippingDestinations() {
		destinations = getDao(ProductShippingDao.class).findAllShippingDestinations();
		
	}

	public void loadTrillers(String destination, String dateStr) {
		trailers = getDao(ProductShippingDao.class).getTrailers(destination,dateStr);
		
		//clean dunnage and products
		dunnages.clear();
		productList.clear();
		
	}

	public List<String> getTrailers() {
		return trailers;
	}

	public void getAllDunnages(String trailerNumber, String shipDate) {
		dunnages = getDao(ProductShippingDao.class).findAllDunnages(trailerNumber, shipDate);
		
		productList.clear();
		
	}

	public List<ProductShipping> getAllByDunnage(String trailernumber, String dunnage,java.sql.Date shipDate) {
		productList = getDao(ProductShippingDao.class).getAllByDunnage(trailernumber, dunnage, shipDate);
		return productList;
		
	}
	
	

}
