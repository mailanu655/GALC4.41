package com.honda.galc.client.teamleader.mbpn;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.entity.product.Mbpn;

/**
 * 
 * <h3>MbpnModel Class description</h3>
 * <p> MbpnModel description </p>
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
 * @author L&T Infotech<br>
 * 
 *
 */

public class MbpnModel extends AbstractModel {

	/**
	 * Finds all data from mbpn
	 * @return
	 */
	public List<Mbpn> findAllMbpn() {
		return getDao(MbpnDao.class).findAllMbpn();
	}

	/**
	 * Finds all Class No from Mbpn
	 * @return
	 */
	public List<String> findAllClassNo() {
		return getDao(MbpnDao.class).findAllClassNo();
	}

	/**
	 * Finds all Prototype Code from Mbpn
	 * @return
	 */
	public List<String> findAllProtoTypeCode() {
		return getDao(MbpnDao.class).findAllProtoTypeCode();
	}

	/**
	 * Finds all Type No from Mbpn
	 * @return
	 */
	public List<String> findAllTypeNo() {
		return getDao(MbpnDao.class).findAllTypeNo();
	}

	/**
	 * Finds all Supplementary No from Mbpn
	 * @return
	 */
	public List<String> findAllSupplementaryNo() {
		return getDao(MbpnDao.class).findAllSupplementaryNo();
	}

	/**
	 * Finds all Target No from Mbpn
	 * @return
	 */
	public List<String> findAllTargetNo() {
		return getDao(MbpnDao.class).findAllTargetNo();
	}

	/**
	 * Finds all Hes Color from Mbpn
	 * @return
	 */
	public List<String> findAllHesColor() {
		return getDao(MbpnDao.class).findAllHesColor();
	}

	/**
	 * Finds all Main No from Mbpn
	 * @return
	 */
	public List<String> findAllMainNo() {
		return getDao(MbpnDao.class).findAllMainNo();
	}

	/**
	 * this method is used to create mbpn and productSpecCode
	 * @param mbpn
	 * @return
	 */
	public Mbpn createMbpnData(Mbpn mbpn){
		return getDao(MbpnDao.class).insert(mbpn);
	}

	/**
	 * this method is used to check the duplication for productSpecCode
	 * @param productSpecCode
	 * @return
	 */
	public boolean isProductSpecCodeExists(String productSpecCode) {
		return getDao(MbpnDao.class).findByKey(productSpecCode) != null;
	}

	/**
	 * this method is used to update mbpn
	 * @param mbpn
	 * @param productSpecCode
	 */
	public void updateMbpnData(Mbpn mbpn,String productSpecCode)  {
		getDao(MbpnDao.class).updateMbpnData(mbpn, productSpecCode);
	}

	@Override
	public void reset() {
		
	}
}

