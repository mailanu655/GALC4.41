package com.honda.galc.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>ManifestDataHelper Class description</h3>
 * <p> ManifestDataHelper description </p>
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
 * Oct.5, 2020
 *
 *
 */
public class ManifestDataHelper {
private static ManifestDataHelper _manifestDataHelper = null;
	
	private Map<String, String> sqlMap = new HashMap<String, String>();
	private Map<String, String> prodIdNameMap = new HashMap<String, String>();
	private ProductTypeUtil util = null;
	Class<?> currentClz = null;
	DataContainer dc = null;
	String[] packages = new String[] {"com.honda.galc.entity.product","com.honda.galc.entity.bearing","com.honda.galc.entity.qics"};
	
	/**
	 * static method that returns the singleton instance
	 * 
	 * @return
	 */
	public static ManifestDataHelper getInstance()
	{
		if (_manifestDataHelper == null)
			_manifestDataHelper = new ManifestDataHelper();
		
		return _manifestDataHelper; 
	}
	
	public String findEntity(DeviceFormat df, DataContainer dc, String productType) {
		try {
			util = ProductTypeUtil.getTypeUtil(productType);
			currentClz = null;
			this.dc = dc;
			if (DataContainerTag.Product.equals(df.getTag())) {
				String sql = DataContainerUtil.makeSQL(getProductSqlString(productType, df), dc);
				BaseProduct entity = ServiceFactory.getService(GenericDaoService.class).find(sql,
						util.getProductClass());
				return ProductManifestDataUtil.toJson(entity);

			} else {
				try {
					String sql = DataContainerUtil.makeSQL(getSqlString(productType, df), dc);
					if(StringUtils.isEmpty(sql) || currentClz == null) {
						return StringUtils.EMPTY;
					}
					List<?> entityList = ServiceFactory.getService(GenericDaoService.class).findAll(sql, null,
							currentClz);
					return ProductManifestDataUtil.toJson(entityList);
				} catch (Exception e) {
					/*
					 * sometime head Id is passed but BlockBuildResult is configured,
					 * so exception is ok here.
					 */
				}
			} 
		} catch (Exception e) {
			Logger.getLogger().error(e, "failed to find entity.");
		}
		return StringUtils.EMPTY;
				
	}

	
	
	public String getProductSqlString(String productType, DeviceFormat df) {
		if(StringUtils.isEmpty(df.getTagValue()))
			return getProductSqlString(productType);
		else 
			return buildSqlWithFilter(getProductSqlString(productType), df.getTagValue());
	}
	
	public String getSqlString(String productType, DeviceFormat df) {
		if(StringUtils.isEmpty(df.getTagValue()))
			return generateEntitySqlStr(productType, df.getTag());
		else 
			return buildSqlWithFilter(generateEntitySqlStr(productType, df.getTag()), df.getTagValue());
		
	}
	
	private String generateEntitySqlStr(String productType, String clzName) {


		getCurrentClz(clzName);

		return generateSqlStringWithProductId(currentClz);
	}

	private void getCurrentClz(String clzName) {
		if (DefectResult.class.getSimpleName().equals(clzName))
			currentClz = DefectResult.class;
		else if(Measurement.class.getSimpleName().equals(clzName))
			currentClz = Measurement.class;
		else if(util.getProductBuildResultClass().getSimpleName().equals(clzName))
				currentClz = util.getProductBuildResultClass();
		else if(util.getProductHistoryClass().getSimpleName().equals(clzName))
				currentClz = util.getProductHistoryClass();
		else {
			/**
			 * it's does not make sense to retrieve class that is not in the product Util 
			 *  and not PRODUCT_ID 
			 */
			if(!TagNames.PRODUCT_ID.name().equals(getProductIdName()))
				return;
			currentClz = searchClass(clzName);
		}
		
		
	}

	private Class<?> searchClass(String clzName) {
		for(int i = 0; i < packages.length; i++) {
			try {
				return Class.forName(packages[i] + "." + clzName);
			} catch (Exception e) {
				;
			}
		}
		return null;
	}

	private String buildSqlWithFilter(String productSqlString, String tagValue) {
		if(StringUtils.isEmpty(productSqlString)) return StringUtils.EMPTY;
		StringBuilder sb = new StringBuilder(productSqlString);
		if(productSqlString.contains("where"))
			sb.append(" and ").append(tagValue);
		else
			sb.append(" where ").append(tagValue);
		return sb.toString();
		
	}

	public String getProductSqlString(String productType) {
		currentClz =util.getProductClass();
		if(!sqlMap.keySet().contains(productType)) {
			sqlMap.put(productType, generateSqlStringWithProductId(currentClz));
		} 
		
		return sqlMap.get(productType);
	}
	
	public String generateSqlStringWithProductId(Class<?> clz) {
		Table table = getTable(clz);
		String productIdName = getProductIdName();

		StringBuilder sb = new StringBuilder();
		sb.append("select * from ").append("galadm.").append(table.name());
		
		if(!StringUtils.isEmpty(productIdName) && 
				dc.keySet().contains(TagNames.PRODUCT_ID.name()))
			sb.append(" where ").append(productIdName).append(" = '{PRODUCT_ID}' ");
		
		return sb.toString();
	}

	private Table getTable(Class<?> clz) {
		Table annotation = clz.getAnnotation(Table.class);
		if(annotation != null)
			return annotation;
		else 
			return getTable(clz.getSuperclass());
			
	}

	private String getProductIdName() {
		if(!prodIdNameMap.keySet().contains(util.name())) {
			getProductIdName1(util.getProductClass());
		}
		
		return prodIdNameMap.get(util.name());
	}

	private void getProductIdName1(Class<?> class1) {
		
		if(class1.equals(Object.class))
			prodIdNameMap.put(util.name(), StringUtils.EMPTY);
		
		Field[] declaredFields = class1.getDeclaredFields();

		for(Field f : declaredFields) {
			if( f.getAnnotation(Id.class) != null) {
				prodIdNameMap.put(util.name(), f.getAnnotation(Column.class).name());
				return;
			}

		}
		
		getProductIdName1(class1.getSuperclass());
	}
}