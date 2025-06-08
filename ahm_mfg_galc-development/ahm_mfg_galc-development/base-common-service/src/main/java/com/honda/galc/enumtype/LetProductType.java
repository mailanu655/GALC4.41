package com.honda.galc.enumtype;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.IpuDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ReflectionUtils;

public enum LetProductType {

	FRAME(FrameDao.class,"Frame", "VIN"),
	FRAME_JPN(FrameDao.class,"Frame_JPN", "VIN"),
	MBPN(MbpnProductDao.class,"MBPN", "MBPN"),
	IPU(IpuDao.class,"IPU", "IPU");

	private static final String PROP_PREFIX = "GET_";
	private static final String PROP_SUFFIX = "_LENGTH";

	private String productName;
	private String productLabel;
	private Class<? extends ProductDao<? extends BaseProduct>> productDaoClass;

	private LetProductType(Class<? extends ProductDao<? extends BaseProduct>> productDao, String productName, String productLabel) {
		this.productDaoClass = productDao;
		this.productName = productName;
		this.productLabel = productLabel;
	}

	public static LetProductType getType(String productName) {
		for(LetProductType productType :values()) {
			if(productType.getProductName().equalsIgnoreCase(productName) ||
					productType.name().equalsIgnoreCase(productName))	
				return productType;
		}
		return null;
	}

	public int getProductIdLength() {
		String methodName = getPropertyMethodName(PROP_PREFIX + getProductName() + PROP_SUFFIX); 
		return (Integer) ReflectionUtils.invoke(getPropertyBean(), methodName);
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductLabel() {
		return productLabel;
	}

	public void setProductLabel(String productLabel) {
		this.productLabel = productLabel;
	}

	public ProductDao<? extends BaseProduct> getProductDao() {
		return getDao(productDaoClass);
	}

	public LetPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(LetPropertyBean.class);  
	}

	private String getPropertyMethodName(String propertyName) {
		StringBuilder sb = new StringBuilder();

		String[] tokens = propertyName.split("_");
		for(int i = 0; i < tokens.length; i++ ){
			String token = tokens[i].toLowerCase().trim();
			if(i==0){
				sb.append(token);
			}
			else{
				sb.append(Character.toUpperCase(token.charAt(0)) + token.substring(1));
			}
		}

		return sb.toString();
	}

}