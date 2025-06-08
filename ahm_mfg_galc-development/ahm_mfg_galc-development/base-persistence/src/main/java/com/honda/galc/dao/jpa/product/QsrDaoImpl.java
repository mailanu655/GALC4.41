package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.HoldParmDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.ExceptionalOutId;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrDaoImpl</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class QsrDaoImpl extends BaseDaoImpl<Qsr, Integer> implements QsrDao {

	private static final long serialVersionUID = 1L;

	@Autowired
	private HoldResultDao holdResultDao;
	@Autowired
	private BlockDao blockDao;
	@Autowired
	private HeadDao headDao;
	@Autowired
	private DailyDepartmentScheduleDao dailyDepartmentScheduleDao;
	@Autowired
	private ExceptionalOutDao exceptionalOutDao;
	@Autowired
	private HoldParmDao holdParamDao;
	
	private final static char COMMENT_SEPARATOR = ',';

	@Transactional
	public Qsr insert(Qsr entity) {
		if (entity.getId() == null) {
			Integer max = max("id", Integer.class);
			int maxId = max == null ? 1 : max + 1;
			entity.setId(maxId);
		}
		return super.insert(entity);
	}

	@Transactional
	public Qsr insert(Qsr qsr, HoldParm holdParam) {
		qsr = insert(qsr);
		holdParam.setQsrId(qsr.getId());
		getHoldParamDao().insert(holdParam);
		return qsr;
	}

	public List<Qsr> findAll(String processLocation, int qsrStatus) {
		return findAll(Parameters.with("processLocation", processLocation).put("status", qsrStatus));
	}

	public List<Qsr> findAll(String processLocation, String productType, int qsrStatus) {
		return findAll(Parameters.with("processLocation", processLocation).put("productType", productType).put("status", qsrStatus));
	}

	@Transactional
	public Qsr holdProducts(ProductType productType, List<HoldResult> holdResults, Qsr qsr) {
		if (qsr.getId() == null) {
			qsr = insert(qsr);
		}
		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);
		for (HoldResult hr : holdResults) {
			hr.setQsrId(qsr.getId());
			productDao.updateHoldStatus(hr.getId().getProductId(), HoldStatus.ON_HOLD.getId());
		}
		getHoldResultDao().insertAll(holdResults);
		return findByKey(qsr.getId());
	}

	@Transactional
	public void updateHoldResults(ProductType productType, List<HoldResult> holdResults, Qsr qsr) {
		if (holdResults == null) {
			return;
		}
		getHoldResultDao().updateAll(holdResults);
		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);
		for (HoldResult item : holdResults) {
			if (item.getReleaseFlag() == 1) {
				productDao.releaseHoldWithCheck(item.getId().getProductId());
			} else {
				BaseProduct product = productDao.findByKey(item.getId().getProductId());
				if (HoldStatus.NOT_ON_HOLD.getId() == product.getHoldStatus()) {
					productDao.updateHoldStatus(item.getId().getProductId(), HoldStatus.ON_HOLD.getId());
				}
			}
		}
		if (qsr != null) {
			qsr.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			update(qsr);
		}
	}

	@Transactional
	public void scrapHoldProducts(ProductType productType, Qsr qsr, List<HoldResult> holdResults, HoldResult releaseInfo, DefectDescription defectDescription, ProcessPoint processPoint) {

		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);

		DailyDepartmentSchedule schedule = getDailyDepartmentScheduleDao().find(processPoint.getDivisionId(), new Timestamp(System.currentTimeMillis()));
		for (HoldResult hr : holdResults) {
			if (hr == null) {
				continue;
			}
			getHoldResultDao().releaseProductHolds(releaseInfo.getReleaseAssociateNo(), releaseInfo.getReleaseAssociateName(), releaseInfo.getReleaseAssociatePager(), releaseInfo.getReleaseAssociatePhone(),
					releaseInfo.getReleaseReason(), hr.getId().getProductId());

			productDao.updateHoldStatus(hr.getId().getProductId(), HoldStatus.NOT_ON_HOLD.getId());

			ExceptionalOutId id = new ExceptionalOutId();
			id.setProductId(hr.getId().getProductId());
			id.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			ExceptionalOut exceptionalOut = new ExceptionalOut();
			exceptionalOut.setId(id);

			exceptionalOut.setProcessPointId(processPoint.getProcessPointId());
			exceptionalOut.setAssociateNo(releaseInfo.getReleaseAssociateNo());
			exceptionalOut.setLocation(defectDescription.getId().getInspectionPartLocationName());
			exceptionalOut.setExceptionalOutReasonString(defectDescription.getDefectTypeName());
			if (schedule != null) {
				exceptionalOut.setProductionDate(schedule.getId().getProductionDate());
			}
			exceptionalOut.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
			if (!StringUtils.isBlank(defectDescription.getId().getInspectionPartName()) && !StringUtils.isBlank(defectDescription.getId().getSecondaryPartName())){
				exceptionalOut.setExceptionalOutComment(qsr.getName()  + COMMENT_SEPARATOR + defectDescription.getId().getInspectionPartName() + COMMENT_SEPARATOR + defectDescription.getId().getSecondaryPartName());
			}else{
				exceptionalOut.setExceptionalOutComment(qsr.getName());
			}

			getExceptionalOutDao().save(exceptionalOut);
			productDao.updateDefectStatus(hr.getId().getProductId(), DefectStatus.SCRAP);
		}

		if (QsrStatus.COMPLETED.getIntValue() == qsr.getStatus() || StringUtils.isNotBlank(qsr.getComment())) {
			qsr.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			update(qsr);
		}
	}

	@Transactional
	public void massScrapProducts(ProductType productType, Qsr qsr, List<HoldResult> holdResults, DefectDescription defectDescription, ProcessPoint processPoint) {

		if (qsr.getId() == null) {
			qsr = insert(qsr);
		}

		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);

		DailyDepartmentSchedule schedule = getDailyDepartmentScheduleDao().find(processPoint.getDivisionId(), new Timestamp(System.currentTimeMillis()));
		for (HoldResult hr : holdResults) {
			if (hr == null) {
				continue;
			}
			hr.setQsrId(qsr.getId());
			getHoldResultDao().insert(hr);

			productDao.updateHoldStatus(hr.getId().getProductId(), HoldStatus.NOT_ON_HOLD.getId());

			ExceptionalOutId id = new ExceptionalOutId();
			id.setProductId(hr.getId().getProductId());
			id.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			ExceptionalOut exceptionalOut = new ExceptionalOut();
			exceptionalOut.setId(id);

			exceptionalOut.setProcessPointId(processPoint.getProcessPointId());
			exceptionalOut.setAssociateNo(hr.getReleaseAssociateNo());
			exceptionalOut.setLocation(defectDescription.getId().getInspectionPartLocationName());
			exceptionalOut.setExceptionalOutReasonString(defectDescription.getDefectTypeName());
			if (schedule != null) {
				exceptionalOut.setProductionDate(schedule.getId().getProductionDate());
			}
			exceptionalOut.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
			exceptionalOut.setExceptionalOutComment(qsr.getName());

			getExceptionalOutDao().save(exceptionalOut);
			productDao.updateDefectStatus(hr.getId().getProductId(), DefectStatus.SCRAP);
		}

		if (QsrStatus.COMPLETED.getIntValue() == qsr.getStatus() || StringUtils.isNotBlank(qsr.getComment())) {
			qsr.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			update(qsr);
		}
	}

	public HoldResultDao getHoldResultDao() {
		return holdResultDao;
	}

	public void setHoldResultDao(HoldResultDao holdResultDao) {
		this.holdResultDao = holdResultDao;
	}

	public BlockDao getBlockDao() {
		return blockDao;
	}

	public void setBlockDao(BlockDao blockDao) {
		this.blockDao = blockDao;
	}

	public HeadDao getHeadDao() {
		return headDao;
	}

	public void setHeadDao(HeadDao headDao) {
		this.headDao = headDao;
	}

	public DailyDepartmentScheduleDao getDailyDepartmentScheduleDao() {
		return dailyDepartmentScheduleDao;
	}

	public void setDailyDepartmentScheduleDao(DailyDepartmentScheduleDao dailyDepartmentScheduleDao) {
		this.dailyDepartmentScheduleDao = dailyDepartmentScheduleDao;
	}

	public ExceptionalOutDao getExceptionalOutDao() {
		return exceptionalOutDao;
	}

	public void setExceptionalOutDao(ExceptionalOutDao exceptionalOutDao) {
		this.exceptionalOutDao = exceptionalOutDao;
	}

	protected HoldParmDao getHoldParamDao() {
		return holdParamDao;
	}

	protected void setHoldParamDao(HoldParmDao holdParamDao) {
		this.holdParamDao = holdParamDao;
	}

	@Override
	public List<Qsr> findAll(String processLocation, String productType, int qsrStatus, String holdAccessType) {
		return findAll(Parameters.with("processLocation", processLocation).put("productType", productType).put("status", qsrStatus).put("holdAccessType", holdAccessType));
	}
}
