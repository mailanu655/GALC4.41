package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.Mbpn;


public interface MbpnDao extends BaseProductSpecDao<Mbpn, String> {
	
	public List<String> findAllClassNos();

	public List<Date> findAllProductionDates(String processPointId, Date date);

	public List<String> findAllClassNoByMainNo(String mainPartNo, String processPointId, String entryDept); 

	public List<Mbpn> findAllDescByMainNoAndClassNo(String mainPartNo, String classNo);
	
	public List<String> findAllMainNo();

	public List<String> findAllClassNo();

	public List<String> findAllProtoTypeCode();

	public List<String> findAllTypeNo();

	public List<String> findAllSupplementaryNo();

	public List<String> findAllTargetNo();

	public List<String> findAllHesColor();

	public List<Mbpn> findAllMbpn();

	public void updateMbpnData(Mbpn mbpn, String productSpecCode);

	public Mbpn findSpecCodeByMainNoAndClassNo(String mainPartNo, String classNo);

	Mbpn findSpecCodeByMainNoClassNoAndHesColour(String mainPartNo, String classNo, String hesColour);
	
	public List<Mbpn> findNonBlankDescByMainNoAndClassNo(String mainPartNo, String classNo);
	
	public List<Mbpn> findAllByMainNo(List<String> mainNo);
	
	public List<Mbpn>  findAllByPrefix(String prefix);
	
	public List<String> findAllProdSpecCode();
}
