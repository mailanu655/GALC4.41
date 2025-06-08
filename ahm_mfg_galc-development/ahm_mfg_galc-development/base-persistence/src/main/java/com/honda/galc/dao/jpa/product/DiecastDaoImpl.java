package com.honda.galc.dao.jpa.product;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>DiecastDaoImpl Class description</h3>
 * <p> DiecastDaoImpl description </p>
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
 * Jun 28, 2011
 *
 *
 */
public abstract class DiecastDaoImpl<T extends DieCast> extends ProductDaoImpl<T> implements DiecastDao<T> {

	private static final String FIND_ALL_BY_MCDC_NUMBER_NATIVE = "SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER() AS rn FROM %s E WHERE TRIM(E.DC_SERIAL_NUMBER) LIKE ?1 OR TRIM(E.MC_SERIAL_NUMBER) LIKE ?1) AS temp WHERE rn BETWEEN %d AND %d";
	
	protected abstract String getProductIdName();
	
	@Override
	public T findBySn(String sn) {
		T product=null;
		try{
			product = super.findBySn(sn);
			if (product != null) {
				return product;
		}
		} catch (Exception e) {
		}
		try{
			product = findByMCSerialNumber(sn);
			if (product != null) {
				return product;
			}
		} catch (Exception e) {
		}
			try{
				product = findByDCSerialNumber(sn); 
			if (product != null) {
				return product;
			}
		} catch (Exception ex) {
		}		
		return product;
	}
	
   	@Override
	public List<T> findAllBySN(String sn) {
   		return findAllByMCDCNumber(sn);
	}

   	@Override
   	public List<T> findPageBySN(String sn, int pageNumber, int pageSize) {
   		return findPageByMCDCNumber(sn, pageNumber, pageSize);
   	}

	public T findBySn(String sn, NumberType numberType) {
		if (NumberType.IN.equals(numberType)) {
			return findByKey(sn);
		}
		if (NumberType.MC.equals(numberType)) {
			return findByMCSerialNumber(sn);
		}
		if (NumberType.DC.equals(numberType)) {
			return findByDCSerialNumber(sn);
		}
		return null;
	}	
	
	public T findByDCSerialNumber(String dcNumber) {
		
		return findFirst(Parameters.with("dcSerialNumber", dcNumber));
		
	}

	public T findByMCSerialNumber(String mcNumber) {
		
		return findFirst(Parameters.with("mcSerialNumber", mcNumber));
		
	}

	public List<T> findAllByEngineSerialNumber(String engineId) {
		
		return findAll(Parameters.with("engineSerialNumber", engineId));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
 	public List<T> findProducts(List<String> numbers, int startPos, int pageSize) {
		List<T> list = new ArrayList<T>();
		if (numbers == null || numbers.isEmpty()) {
			return list;
		}
		
		StringBuilder sb = new StringBuilder(getFindAllSql());
		sb.append(" where");
		sb.append(" e."+getProductIdName()+" in ("+ StringUtil.toSqlInString(numbers) + ")");
		sb.append(" or e.dcSerialNumber in ("+ StringUtil.toSqlInString(numbers) + ")");
		sb.append(" or e.mcSerialNumber in ("+ StringUtil.toSqlInString(numbers) + ")");
		StringUtil.toSqlInString(numbers);
		Parameters params = new Parameters();
		params.put("id", numbers);
		params.put("dcSerialNumbers", numbers);
		params.put("mcSerialNumbers", numbers);
		return entityManager.createQuery(sb.toString()).setFirstResult(startPos).setMaxResults(pageSize).getResultList();
	}
	
	public List<T> findAllByMCDCNumber(List<String> numbers) {
		List<T> list = new ArrayList<T>();
		if (numbers == null || numbers.isEmpty()) {
			return list;
		}
		StringBuilder sb = new StringBuilder(getFindAllSql());
		sb.append(" where");
		sb.append(" e.dcSerialNumber in (:dcSerialNumbers)");
		sb.append(" or e.mcSerialNumber in (:mcSerialNumbers)");
		Parameters params = new Parameters();
		params.put("dcSerialNumbers", numbers);
		params.put("mcSerialNumbers", numbers);
		list = findAllByQuery(sb.toString(), params);
		return list;
	}
	
	public List<T> findAllByMCDCNumber(String serialNumber) {
		List<T> list = new ArrayList<T>();
		StringBuilder sb = new StringBuilder(getFindAllSql());
		sb.append(getMatchingSNClause(serialNumber));
		Parameters params = new Parameters();
		params.put("serialNumber", "%" + serialNumber);
		list = findAllByQuery(sb.toString(), params);
		return list;
	}
	
	public List<T> findPageByMCDCNumber(String serialNumber, int pageNumber, int pageSize) {
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		if (tableAnnotation == null) {
			List<T> list = new ArrayList<T>();
			StringBuilder sb = new StringBuilder(getFindAllSql());
			sb.append(getMatchingSNClause(serialNumber));
			Parameters params = new Parameters();
			params.put("serialNumber", "%" + serialNumber);
			list = findAllByQuery(sb.toString(), params, pageNumber * pageSize, pageSize);
			return list;
		}
		Parameters params = Parameters.with("1", "%" + serialNumber);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(FIND_ALL_BY_MCDC_NUMBER_NATIVE, tableAnnotation.name(), (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);
	}
	
	private String getMatchingSNClause(String serialNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append(" where");
		sb.append(" TRIM(e."+getProductIdName()+") LIKE :serialNumber");
		sb.append(" or TRIM(e.dcSerialNumber) LIKE :serialNumber");
		sb.append(" or TRIM(e.mcSerialNumber) LIKE :serialNumber");
		return sb.toString();
	}
	
	public long countByMatchingSN(String sn) {
		StringBuilder sb = new StringBuilder(prepareCountClause());
		sb.append(getMatchingSNClause(sn));
		return count(sb.toString(), Parameters.with("serialNumber", "%" + sn));
	}

	public long countByProductionLot(String productionLot) {
		return 0;
	}

	public List<T> findAllByDunnage(String dunnage) {
		return findAll(Parameters.with("dunnage", dunnage));
	}
	
	/**
	 * Method for updating when capacity constraint is not being applied
	 * @param productId
	 * @param dunnage
	 * @return
	 */
	@Transactional
	public int updateDunnage(String productId,String dunnageId) {
		return update(Parameters.with("dunnage", dunnageId), Parameters.with("productId", productId));
	}
	
	
	@Override
	@Transactional
	public int removeDunnage(String productId) {
		return update(Parameters.with("dunnage", null), Parameters.with("productId", productId));
	}
	
	public T findByMCDCNumber(String productId) {
		return findBySn(productId);
	}

	@Transactional
	public void updateEngineSerialNumber(String productId, String esn) {
		update(Parameters.with("engineSerialNumber", esn),Parameters.with("productId", productId));
	}
	
	public List<T> findAllLastProcessed(String processPointId, int count){
    	return new ArrayList<T>();
    }

	public List<Map<String, Object>> selectDunnageInfo(String criteria, int resultsetSize) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jpq = "select e.dunnage, count(e) from %s e where e.dunnage like :param  group by e.dunnage order by e.dunnage desc";
		jpq = String.format(jpq, entityClass.getName());
		Query query = entityManager.createQuery(jpq);
		query.setParameter("param", criteria);
		query.setMaxResults(resultsetSize);
		List<?> resultList = query.getResultList();
		if (resultList != null) {
			for (Object item : resultList) {
				Object[] row = (Object[]) item;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dunnage", row[0]);
				map.put("count", row[1]);
				list.add(map);
			}
		}
		return list;
	}
	
	public long countByDunnage(String dunnage) {
		return count(Parameters.with("dunnage", dunnage));
	}
	
	@Transactional
    public int createProducts(ProductionLot prodLot,String productType,String lineId,String ppId) {
		savePreProductionLot(prodLot);
		return 0;
	}

	@Transactional
	protected void savePreProductionLot(ProductionLot prodLot) {
		PreProductionLotDao preProductionLotDao = getDao(PreProductionLotDao.class);
		PreProductionLot preProdLot = preProductionLotDao.findByKey(prodLot.getProductionLot());
		
		if(preProdLot == null) {
			preProdLot = prodLot.derivePreProductionLot();
			double maxSeq = preProductionLotDao.findMaxSequence(preProdLot.getPlanCode());
			int interval = PropertyService.getPropertyBean(ProductOnHlPropertyBean.class).getSequenceInterval();
			preProdLot.setSequence(maxSeq + interval);
		}
		preProductionLotDao.save(preProdLot);
	}
}