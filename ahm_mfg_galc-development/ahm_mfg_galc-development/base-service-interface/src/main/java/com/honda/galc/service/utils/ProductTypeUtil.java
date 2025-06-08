package com.honda.galc.service.utils;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BlockHistoryDao;
import com.honda.galc.dao.product.BumperDao;
import com.honda.galc.dao.product.ConrodBuildResultDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.ConrodHistoryDao;
import com.honda.galc.dao.product.CrankshaftBuildResultDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.CrankshaftHistoryDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.HeadHistoryDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.IpuDao;
import com.honda.galc.dao.product.KnuckleDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.MissionCaseDao;
import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.dao.product.MissionSpecDao;
import com.honda.galc.dao.product.MovablePulleyDriveDao;
import com.honda.galc.dao.product.MovablePulleyDrivenDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.dao.product.PulleyShaftDriveDao;
import com.honda.galc.dao.product.PulleyShaftDrivenDao;
import com.honda.galc.dao.product.TorqueConverterCaseDao;
import com.honda.galc.dao.product.WeldDao;
import com.honda.galc.dao.product.FrontIpuCaseDao;
import com.honda.galc.dao.product.RearIpuCaseDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockHistory;
import com.honda.galc.entity.product.Bumper;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.ConrodHistory;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.CrankshaftHistory;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadHistory;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Ipu;
import com.honda.galc.entity.product.Knuckle;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Mission;
import com.honda.galc.entity.product.MissionCase;
import com.honda.galc.entity.product.MovablePulleyDrive;
import com.honda.galc.entity.product.MovablePulleyDriven;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.PulleyShaftDrive;
import com.honda.galc.entity.product.PulleyShaftDriven;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.TorqueConverterCase;
import com.honda.galc.entity.product.Weld;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.entity.product.FrontIpuCase;
import com.honda.galc.entity.product.RearIpuCase;

/**
 * 
 * <h3>ProductTypeEx Class description</h3>
 * <p> ProductTypeEx description </p>
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
 * Apr 13, 2012
 *
 *
 */
public enum ProductTypeUtil{
	ENGINE(EngineDao.class,EngineSpecDao.class,InstalledPartDao.class,ProductResultDao.class,
			Engine.class,InstalledPart.class,ProductResult.class),
	FRAME(FrameDao.class,FrameSpecDao.class,InstalledPartDao.class,ProductResultDao.class,
			Frame.class,InstalledPart.class,ProductResult.class),
	BLOCK(BlockDao.class,ProductSpecCodeDao.class,BlockBuildResultDao.class,BlockHistoryDao.class,
			Block.class,BlockBuildResult.class,BlockHistory.class),
	HEAD(HeadDao.class,ProductSpecCodeDao.class,HeadBuildResultDao.class,HeadHistoryDao.class,
			Head.class,HeadBuildResult.class,HeadHistory.class),
	MISSION(MissionDao.class,MissionSpecDao.class,InstalledPartDao.class,ProductResultDao.class,
			Mission.class,InstalledPart.class,ProductResult.class),
	MCASE(MissionCaseDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			MissionCase.class,InstalledPart.class,ProductResult.class),
	TCCASE(TorqueConverterCaseDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			TorqueConverterCase.class,InstalledPart.class,ProductResult.class),
	FIPUCASE(FrontIpuCaseDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			FrontIpuCase.class,InstalledPart.class,ProductResult.class),
	RIPUCASE(RearIpuCaseDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			RearIpuCase.class,InstalledPart.class,ProductResult.class),
	MPDR(MovablePulleyDriveDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			MovablePulleyDrive.class,InstalledPart.class,ProductResult.class),
	MPDN(MovablePulleyDrivenDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			MovablePulleyDriven.class,InstalledPart.class,ProductResult.class),
	PSDR(PulleyShaftDriveDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			PulleyShaftDrive.class,InstalledPart.class,ProductResult.class),
	PSDN(PulleyShaftDrivenDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			PulleyShaftDriven.class,InstalledPart.class,ProductResult.class),
	IPU(IpuDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			Ipu.class,InstalledPart.class, ProductResult.class),
	KNUCKLE(KnuckleDao.class,FrameSpecDao.class,InstalledPartDao.class,ProductResultDao.class,
			Knuckle.class,InstalledPart.class, ProductResult.class),
	PLASTICS(MbpnProductDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class,
			 MbpnProduct.class,InstalledPart.class, ProductResult.class),
	MBPN(MbpnProductDao.class,MbpnDao.class,InstalledPartDao.class,ProductResultDao.class,
			 MbpnProduct.class,InstalledPart.class, ProductResult.class),
	CRANKSHAFT(CrankshaftDao.class,ProductSpecCodeDao.class,CrankshaftBuildResultDao.class,CrankshaftHistoryDao.class,
			Crankshaft.class,CrankshaftBuildResult.class,CrankshaftHistory.class),
	CONROD(ConrodDao.class,ProductSpecCodeDao.class,ConrodBuildResultDao.class,ConrodHistoryDao.class,
			Conrod.class,ConrodBuildResult.class,ConrodHistory.class),
	BUMPER(BumperDao.class,FrameSpecDao.class,InstalledPartDao.class,ProductResultDao.class,
			Bumper.class,InstalledPart.class, ProductResult.class),
	WELD(WeldDao.class,ProductSpecCodeDao.class,InstalledPartDao.class,ProductResultDao.class, 
			Weld.class,InstalledPart.class, ProductResult.class),
	IPU_MBPN(MbpnProductDao.class,MbpnDao.class,InstalledPartDao.class,ProductResultDao.class,
			 MbpnProduct.class,InstalledPart.class, ProductResult.class),
	TDU(MbpnProductDao.class,MbpnDao.class,InstalledPartDao.class,ProductResultDao.class,
			 MbpnProduct.class,InstalledPart.class, ProductResult.class),
	BMP_MBPN(MbpnProductDao.class,MbpnDao.class,InstalledPartDao.class,ProductResultDao.class,
			 MbpnProduct.class,InstalledPart.class, ProductResult.class),
	SUBFRAME(MbpnProductDao.class,MbpnDao.class,InstalledPartDao.class,ProductResultDao.class,
			 MbpnProduct.class,InstalledPart.class, ProductResult.class),
	KNU_MBPN(MbpnProductDao.class,MbpnDao.class,InstalledPartDao.class,ProductResultDao.class,
			 MbpnProduct.class,InstalledPart.class, ProductResult.class),
	MBPN_PART(MbpnProductDao.class,MbpnDao.class,InstalledPartDao.class,ProductResultDao.class,
			MbpnProduct.class,InstalledPart.class, ProductResult.class),
	FRAME_JPN(FrameDao.class,FrameSpecDao.class,InstalledPartDao.class,ProductResultDao.class,
			Frame.class,InstalledPart.class,ProductResult.class);


	
	private Class<? extends ProductDao<? extends BaseProduct>> productDaoClass;
	private Class<? extends BaseProductSpecDao<? extends BaseProductSpec,?>> productSpecDaoClass;
	private Class<? extends ProductBuildResultDao<? extends ProductBuildResult, ?>> productBuildResultDaoClass;
	private Class<? extends ProductHistoryDao<? extends ProductHistory,?>> productHistoryDaoClass;
	private Class<? extends BaseProduct> productClass;
	private Class<? extends ProductBuildResult> productBuildResultClass;
	private Class<? extends ProductHistory> productHistoryClass;
	
	private ProductTypeUtil(
			Class<? extends ProductDao<? extends BaseProduct>> productDao,
			Class<? extends BaseProductSpecDao<? extends BaseProductSpec,?>> productSpecDao,		
			Class<? extends ProductBuildResultDao<? extends ProductBuildResult, ?>> productBuildResultDao,
			Class<? extends ProductHistoryDao<? extends ProductHistory,?>> productHistoryDao,
			Class<? extends BaseProduct> productClass,
			Class<? extends ProductBuildResult> productBuildResultClass,
			Class<? extends ProductHistory> productHistoryClass) {

		this.productDaoClass = productDao;
		this.productSpecDaoClass = productSpecDao;
		this.productBuildResultDaoClass = productBuildResultDao;
		this.productHistoryDaoClass = productHistoryDao;
		this.productClass = productClass;
		this.productBuildResultClass =productBuildResultClass;
		this.productHistoryClass = productHistoryClass;
		
		
	}
	
	public Class<? extends ProductDao<? extends BaseProduct>> getProductDaoClass() {
		return productDaoClass;
	}

	public void setProductDaoClass(
			Class<? extends ProductDao<? extends BaseProduct>> productDaoClass) {
		this.productDaoClass = productDaoClass;
	}

	public Class<? extends BaseProductSpecDao<? extends BaseProductSpec, ?>> getProductSpecDaoClass() {
		return productSpecDaoClass;
	}

	public void setProductSpecDaoClass(
			Class<? extends BaseProductSpecDao<? extends BaseProductSpec, ?>> productSpecDaoClass) {
		this.productSpecDaoClass = productSpecDaoClass;
	}

	public Class<? extends ProductBuildResultDao<? extends ProductBuildResult, ?>> getProductBuildResultDaoClass() {
		return productBuildResultDaoClass;
	}

	public void setProductBuildResultDaoClass(
			Class<? extends ProductBuildResultDao<? extends ProductBuildResult, ?>> productBuildResultDaoClass) {
		this.productBuildResultDaoClass = productBuildResultDaoClass;
	}

	public Class<? extends ProductHistoryDao<? extends ProductHistory, ?>> getProductHistoryDaoClass() {
		return productHistoryDaoClass;
	}

	public void setProductHistoryDaoClass(
			Class<? extends ProductHistoryDao<? extends ProductHistory, ?>> productHistoryDaoClass) {
		this.productHistoryDaoClass = productHistoryDaoClass;
	}
	
	public ProductDao<? extends BaseProduct> getProductDao() {
		return getDao(productDaoClass);
	}
	
	public BaseProductSpecDao<? extends BaseProductSpec, ?> getProductSpecDao() {
		return getDao(productSpecDaoClass);
	}
	
	public ProductBuildResultDao<? extends ProductBuildResult, ?> getProductBuildResultDao() {
		return getDao(productBuildResultDaoClass);
	}

	public ProductHistoryDao<? extends ProductHistory, ?> getProductHistoryDao() {
		return getDao(productHistoryDaoClass);
	}

	public BaseProduct createProduct(String productId) {
		BaseProduct product = ReflectionUtils.createInstance(productClass, productId);
		return product;
	}

	public BaseProduct findProduct(String productId) {
		BaseProduct product = getProductDao().findBySn(productId);
		return product;
	}
	
	public ProductBuildResult createBuildResult() {
		return ReflectionUtils.createInstance(productBuildResultClass);
	}
	
	public ProductBuildResult createBuildResult(String productId, String partName) {
		return ReflectionUtils.createInstance(productBuildResultClass, productId, partName);
	}
	
	public ProductHistory createProductHistory(String productId,String processPointId) {
		return ReflectionUtils.createInstance(productHistoryClass, new Object[]{findProduct(productId), processPointId});
	}
	
	public ProductHistory createProductHistoryEntity(BaseProduct product,String processPointId) {
		return ReflectionUtils.createInstance(productHistoryClass, new Object[]{product, processPointId});
	}

	public static List<InventoryCount> findAllInventoryCounts(String productType) {
		ProductDao<? extends BaseProduct> productDao = getProductDao(productType);
		return productDao.findAllInventoryCounts();
	}
	
	public static ProductDao<? extends BaseProduct> getProductDao(String productType) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.getProductDao();
	}
	
	public static BaseProductSpecDao<? extends BaseProductSpec, ?> getProductSpecDao(String productType) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.getProductSpecDao();
	}
	
	public static ProductBuildResultDao<? extends ProductBuildResult, ?> getProductBuildResultDao(String productType) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.getProductBuildResultDao();
	}
	
	public static ProductHistoryDao<? extends ProductHistory, ?> getProductHistoryDao(String productType) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.getProductHistoryDao();
	}
	
	public static ProductDao<? extends BaseProduct> getProductDao(ProductType productType) {
		return getProductDao(productType.toString());
	}
	
	public static BaseProductSpecDao<? extends BaseProductSpec, ?> getProductSpecDao(ProductType productType) {
		return getProductSpecDao(productType.toString());
	}
	
	public static ProductBuildResultDao<? extends ProductBuildResult, ?> getProductBuildResultDao(ProductType productType) {
		return getProductBuildResultDao(productType.toString());
	}
	
	public static ProductHistoryDao<? extends ProductHistory, ?> getProductHistoryDao(ProductType productType) {
		return getProductHistoryDao(productType.toString());
	}

	public static BaseProduct createProduct(String productType,String productId) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.createProduct(productId);
	}

	public static SubProduct createSubProduct(String productType,String productId) {
		BaseProduct product = createProduct(productType, productId);
		if (product instanceof SubProduct) {
			return (SubProduct) product;
		}
		return null;
	}
	
	public static SubProduct createSubProduct(String productType,String productId, String subId) {
		BaseProduct product = createProduct(productType, productId);
		if (product instanceof SubProduct) {
			product.setSubId(subId);
			return (SubProduct) product;
		}
		return null;
	}	
	
	public static BaseProduct findProduct(String productType,String productId) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.findProduct(productId);
	}

	public static ProductBuildResult createBuildResult(String productType) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.createBuildResult();
	}
	
	public static ProductBuildResult createBuildResult(String productType,String productId, String partName) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.createBuildResult(productId,partName);
	}
	
	public static ProductHistory createProductHistory(String productId,String processPointId, ProductType productType) {
		return createProductHistory(productId, processPointId, productType.toString());
	}
	
	public static ProductHistory createProductHistory(String productId,String processPointId,String productType) {
		ProductTypeUtil productTypeEx = getTypeUtil(productType);
		return productTypeEx == null ? null : productTypeEx.createProductHistory(productId,processPointId);
	}
	
	public static ProductHistory createProductHistory(BaseProduct product,String processPointId) {
		ProductTypeUtil productTypeEx = getTypeUtil(product.getProductType());
		return productTypeEx == null ? null : productTypeEx.createProductHistoryEntity(product,processPointId);
	}
	
	public static ProductTypeUtil getTypeUtil(ProductType type) {
		for(ProductTypeUtil item :ProductTypeUtil.values()) {
			if(item.toString().equalsIgnoreCase(type.toString())) {
				return item;
			}
		}
		return null;
	}
	
	public static ProductTypeUtil getTypeUtil(String productType) {
		productType = ProductTypeCatalog.getProductType(productType).name();
		for(ProductTypeUtil item :ProductTypeUtil.values()) {
			if(item.toString().equalsIgnoreCase(StringUtils.trimToEmpty(productType))) {
				return item;
			}
		}
		return null;
	}
	
	public static boolean isInstanceOf(ProductType productType, Class<?> clz) {
		if (productType == null || clz == null) {
			return false;
		}
		ProductTypeUtil util = ProductTypeUtil.getTypeUtil(productType); 
		if (util == null || util.productClass == null) {
			return false;
		}
		if (clz.isAssignableFrom(util.productClass)) {
			return true;
		}
		return false;
	}
	
	public static boolean isDunnagable(ProductType productType) {
		if (productType == null) {
			return false;
		}
		ProductTypeUtil util = ProductTypeUtil.getTypeUtil(productType);
		if (util == null || util.productClass == null) {
			return false;
		}
		return isDunnagable(util.productClass);
	}
	
	public static boolean isDunnagable(Class<?> productClass) {
		if (productClass == null) {
			return false;
		}
		Class<?>[] dunnagable = { DieCast.class, SubProduct.class, MbpnProduct.class };
		for (Class<?> cls : dunnagable) {
			if (cls.isAssignableFrom(productClass)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDieCast(ProductType productType) {
		if (productType == null) {
			return false;
		}
		ProductTypeUtil util = ProductTypeUtil.getTypeUtil(productType);
		if (util == null || util.productClass == null) {
			return false;
		}
		
		return DieCast.class.isAssignableFrom(util.productClass);
	}
	
	public static boolean isFrameProduct(ProductType productType) {
		if (productType == null) {
			return false;
		}
		ProductTypeUtil util = ProductTypeUtil.getTypeUtil(productType);
		if (util == null || util.productClass == null) {
			return false;
		}
		
		return Frame.class.isAssignableFrom(util.productClass);
	}
	
	public Class<? extends ProductBuildResult> getProductBuildResultClass() {
		return productBuildResultClass;
	}

	public Class<? extends BaseProduct> getProductClass() {
		return productClass;
	}

	public Class<? extends ProductHistory> getProductHistoryClass() {
		return productHistoryClass;
	}

	public static boolean isMbpnProduct(String productTypeStr){
		if(StringUtils.isEmpty(productTypeStr)) return false;
		ProductType productType = ProductTypeCatalog.getProductType(productTypeStr);
		Class<? extends BaseProductSpecDao<? extends BaseProductSpec, ?>> specClass = ProductTypeUtil.getTypeUtil(productType).getProductSpecDaoClass();
		return specClass == MbpnDao.class;
	}

	public static boolean isMbpnProduct(ProductType productType){
		Class<? extends BaseProductSpecDao<? extends BaseProductSpec, ?>> specClass = ProductTypeUtil.getTypeUtil(productType).getProductSpecDaoClass();
		return specClass == MbpnDao.class;
	}

	public static boolean isSubProduct(ProductType  productType) {
		Class<? extends BaseProduct> productClass = ProductTypeUtil.getTypeUtil(productType).getProductClass();
		return SubProduct.class.isAssignableFrom(productClass);
	}
}
