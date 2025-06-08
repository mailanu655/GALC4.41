
package com.honda.galc.service.qics;

import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiExternalSystemDataDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectIdMapDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dto.qi.QiCreateDefectDto;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.qi.constant.QiConstant;
import com.honda.galc.service.HeadlessNaqService;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.common.logging.Logger;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

public class QiHeadlessDefectServiceImpl implements QiHeadlessDefectService {
	/**
	 * Default constructor.
	 */
	public QiHeadlessDefectServiceImpl() {
	}

	@Override
	public Boolean[] createDefects(List<QiCreateDefectDto> dtoList) {
		List<Boolean> statuses = new ArrayList<Boolean>();
		if (dtoList == null || dtoList.isEmpty()) {
			Boolean[] statusArray = new Boolean[1];
			statuses.add(false);
			return statuses.toArray(statusArray);
		}
		Boolean[] statusArray = new Boolean[dtoList.size()];
		for (QiCreateDefectDto dto : dtoList) {
			if (dto == null) {
				statuses.add(false);
				continue;
			}
			try {
				Boolean status = createDefect(dto);
				statuses.add(status);
			} catch (Exception ex) {
				Logger.getLogger().error(ex, "Exception processing dto");
				statuses.add(false);
			}
		}
		return statuses.toArray(statusArray);
	}

	@Transactional
	@Override
	public boolean createDefect(QiCreateDefectDto dto) {
		boolean resp = false;
		try {
			// if validation fails, create external system error record
			if (!QiExternalDefectDataHelper.validateKeyFields(dto)
					|| !QiExternalDefectDataHelper.validateDefectData(dto)) {
				QiExternalSystemData extSysData = dto.getExternalDataEntity();
				if (!QiExternalDefectDataHelper.isExternalSystemDataExist(extSysData)) {
					getDao(QiExternalSystemDataDao.class).save(extSysData);
				}
			}
			// else call headless NAQ service to create defect
			else {
				int status = ServiceFactory.getService(HeadlessNaqService.class).saveDefectData(dto.getDefectMapDto());
				if (status == QiConstant.SC_CREATED) {
					resp = true;
					getLogger().info("QiHeadlessDefectServiceImpl: create defect successful: " + resp);
				} else {
					// else response defaults to not accepted, as initialized
					getLogger().info("Headless NAQService returned status: " + status);
					getLogger().info("QiHeadlessDefectServiceImpl: create defect failed");
				}
			}
		} catch (Exception e) { // exception => reset response just in case it got set to null
			getLogger().error(e, "createDefect");
			resp = false;
		}
		return resp;
	}

	@Override
	public Boolean[] repairDefects(List<QiRepairDefectDto> dtoList) {
		return repairDefects(dtoList, false);
	}

	private class MyList<E extends QiDefectResult> extends ArrayList<E> {
		// QiDefectResult already has an equals method, so this is a custom list to
		// override contains() to compare defectResultId
		public boolean contains(E thisDefect) {
			if (thisDefect == null || this.isEmpty())
				return false;
			for (E qiD : this) {
				if (qiD != null && qiD.getDefectResultId() == thisDefect.getDefectResultId())
					return true;
			}
			return false;
		}

		public boolean contains(long id) {
			for (E qiD : this) {
				if (qiD != null && qiD.getDefectResultId() == id)
					return true;
			}
			return false;
		}
	}

	@Override
	public Boolean[] repairDefects(List<QiRepairDefectDto> dtoList, boolean isUpdateDefectStatus) {
		List<Boolean> statuses = new ArrayList<Boolean>();
		if (dtoList == null || dtoList.isEmpty()) {
			Boolean[] statusArray = new Boolean[1];
			statuses.add(false);
			return statuses.toArray(statusArray);
		}
		Boolean[] statusArray = new Boolean[dtoList.size()];
		MyList<QiDefectResult> fixedDefects = new MyList<QiDefectResult>();

		for (QiRepairDefectDto dto : dtoList) {
			if (dto == null) {
				statuses.add(false);
				continue;
			}
			// first look up the defect result id by external key
			QiExternalSystemDefectIdMap defectMap = getDao(QiExternalSystemDefectIdMapDao.class)
					.findByExternalSystemKey(dto.getExternalSystemName(), dto.getExternalSystemKey());
			// if a mapping was found and if headless repair is permitted, and the defect id
			// is not in the already-fixed list
			if (defectMap != null && defectMap.getIsQicsRepairReqd() == 0
					&& !fixedDefects.contains(defectMap.getId().getDefectResultId())) {
				try {
					Boolean status = repairDefect(dto, isUpdateDefectStatus, fixedDefects);
					statuses.add(status);
				} catch (Exception ex) {
					Logger.getLogger().error(ex, "Exception processing dto");
					statuses.add(false);
				}
			}
		}
		return statuses.toArray(statusArray);
	}

	/*
	 * @see
	 * com.honda.galc.service.QiHeadlessDefectService#repairDefect(com.honda.galc.
	 * dto.qi.QiRepairDefectDto)
	 * 
	 * @see
	 * com.honda.galc.service.QiHeadlessDefectService#repairDefect(com.honda.galc.
	 * dto.qi.QiRepairDefectDto, boolean)
	 * 
	 * @see
	 * com.honda.galc.service.QiHeadlessDefectService#repairDefects(java.util.List)
	 * wrapper method sets update flag to false => product defect status will not be
	 * updated called from headless data collection
	 */
	@Override
	public boolean repairDefect(QiRepairDefectDto dto) {
		return repairDefect(dto, false);
	}

	@Override
	public boolean repairDefect(QiRepairDefectDto dto, boolean isUpdateDefectStatus) {
		MyList<QiDefectResult> fixedDefects = new MyList<QiDefectResult>();
		return repairDefect(dto, isUpdateDefectStatus, fixedDefects);
	}

	/*
	 * @see
	 * com.honda.galc.service.QiHeadlessDefectService#repairDefect(com.honda.galc.
	 * dto.qi.QiRepairDefectDto, boolean)
	 * 
	 * @see
	 * com.honda.galc.service.QiHeadlessDefectService#repairDefects(java.util.List,
	 * boolean) product defect status will be updated based on parameter, if true =>
	 * defect status is updated called from Qics repair
	 */
	@Transactional
	public boolean repairDefect(QiRepairDefectDto dto, boolean isUpdateDefectStatus,
			MyList<QiDefectResult> fixedDefects) {
		List<QiDefectResult> allDefects = new ArrayList<QiDefectResult>();
		boolean resp = false;

		try {// try to find and repair defects
			QiDefectResult qiDefect = null;
			// first look up the defect result id by external key
			if (dto.isMapExtKey()) {
				QiExternalSystemDefectIdMap defectMap = getDao(QiExternalSystemDefectIdMapDao.class)
						.findByExternalSystemKey(dto.getExternalSystemName(), dto.getExternalSystemKey());
				// if a mapping was found and if headless repair is permitted, then look up the
				// defect
				if (defectMap != null && defectMap.getIsQicsRepairReqd() == 0) {
					qiDefect = getDao(QiDefectResultDao.class).findByKey(defectMap.getId().getDefectResultId());
				}
			} else if (dto.getDefectResultId() > 0) {
				qiDefect = getDao(QiDefectResultDao.class).findByKey(dto.getDefectResultId());
			}

			// how this works
			// 1. look up the defect based on external key, if it's deleted, un-delete it -
			// let's call this current defect D1
			// 2. get a list of any existing not-fixed duplicate defects, those should be
			// fixed too, let's call it D2, D3...
			// 3. if a duplicate defect is in deleted state, ignore it
			// 4. For D1, D2...get list of existing not fixed actual problems.
			// 4a. If there are existing not-fixed actual problems, fix them and add an
			// inline repair method to each. In this case, do not add another actual problem
			// 5. If there are no existing not-fixed actual problems, create a new actual
			// problem and add an inline repair method.
			if (qiDefect != null && !fixedDefects.contains(qiDefect)) {
				if (!qiDefect.isDefectFixed()) {
					qiDefect.setDeleted((short) 0);
					allDefects.add(qiDefect);
				}
				if (dto.isFixDuplicates()) {
					// find and fix all duplicate part defects
					List<QiDefectResult> existingDefects = getDao(QiDefectResultDao.class)
							.findMatchingDefectsByNotFixed(qiDefect);
					if (existingDefects != null && !existingDefects.isEmpty()) {
						allDefects.addAll(existingDefects);
					}
				}
				for (QiDefectResult thisDefect : allDefects) { // for each duplicate defect
					thisDefect.setCurrentDefectStatus((short) dto.getCurrentDefectStatus());
					thisDefect.setDeleted((short) 0);
					getDao(QiDefectResultDao.class).save(thisDefect); // save the current defect status (FIXED)
					List<QiRepairResult> repairResultList = getDao(QiRepairResultDao.class)
							.findNotFixedByDefectResultId(thisDefect.getDefectResultId());
					if (repairResultList == null || repairResultList.isEmpty()) { // no existing actual problem
						QiRepairResult qiRepairResult = QiExternalDefectDataHelper
								.createRepairResultFromDefect(thisDefect, dto);
						qiRepairResult = getDao(QiRepairResultDao.class).createRepairResult(qiRepairResult, null); // insert
																													// repair
																													// result
						// apply a generic repair method to new repair result
						QiAppliedRepairMethod repairMethod = QiExternalDefectDataHelper
								.createRepairMethod(qiRepairResult, dto);
						getDao(QiAppliedRepairMethodDao.class).save(repairMethod);
					} else { // if there are existing repair methods/actual problems, add a repair method
						for (QiRepairResult prevRepairResult : repairResultList) {
							// set the current defect status of each repair result entry to current defect
							// status (FIXED)
							prevRepairResult.setDeleted((short) 0);
							prevRepairResult.setCurrentDefectStatus((short) dto.getCurrentDefectStatus());
							if (!StringUtils.isEmpty(dto.getProcessPointId()))
								prevRepairResult.setApplicationId(dto.getProcessPointId());
							getDao(QiRepairResultDao.class).save(prevRepairResult);
							QiAppliedRepairMethod repairMethod = QiExternalDefectDataHelper
									.createRepairMethod(prevRepairResult, dto);
							getDao(QiAppliedRepairMethodDao.class).save(repairMethod);
						}
					}
					fixedDefects.add(thisDefect);
				}
				// if it came here, defect was found and repaired
				// update defect status
				if (isUpdateDefectStatus) {
					DefectStatusHelper.updateDefectStatus(qiDefect.getDefectResultId());
				}
				resp = true;
			} else {// defect not found or qics repair required
				resp = false;
			}
		} catch (Exception e) {
			getLogger().error(e, "headless repair");
			resp = false;
		}

		return resp;
	}

	@Transactional
	@Override
	public boolean deleteDefect(String extSysName, Long extSysKey) {
		boolean resp = false;
		try {
			// first look up the defect result id by external key
			QiExternalSystemDefectIdMap defectMap = getDao(QiExternalSystemDefectIdMapDao.class)
					.findByExternalSystemKey(extSysName, extSysKey);
			QiDefectResult qiDefect = null;
			if (defectMap != null && defectMap.getIsQicsRepairReqd() == 0) {
				qiDefect = getDao(QiDefectResultDao.class).findByKey(defectMap.getId().getDefectResultId());
			}
			// if defect was found, delete
			if (qiDefect != null) {
				qiDefect.setDeleted((short) 1);
				getDao(QiDefectResultDao.class).save(qiDefect);
				getDao(QiRepairResultDao.class).deleteRepairResultByDefectResultId(qiDefect.getDefectResultId());
				resp = true;
			} else { // defect not found
				resp = false;
			}
		} catch (Exception e) {
			getLogger().error(e, "delete defect");
			resp = false;
		}

		return resp;
	}

	@Transactional
	@Override
	public boolean repairActualProblem(QiRepairDefectDto dto) {
		boolean resp = false;

		if (dto == null || dto.getRepairId() == 0)
			return resp;
		try {// try to find and repair defects
			QiRepairResult qiRepair = getDao(QiRepairResultDao.class).findByKey(dto.getRepairId());
			QiDefectResult qiDefect = getDao(QiDefectResultDao.class).findByKey(dto.getDefectResultId());

			if (qiRepair != null && qiRepair.getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId()) {
				qiRepair.setDeleted((short) 0);
				qiRepair.setCurrentDefectStatus((short) dto.getCurrentDefectStatus());
				if (!StringUtils.isEmpty(dto.getProcessPointId()))
					qiRepair.setApplicationId(dto.getProcessPointId());
				getDao(QiRepairResultDao.class).save(qiRepair); // save the current defect status (FIXED)
				QiAppliedRepairMethod repairMethod = QiExternalDefectDataHelper.createRepairMethod(qiRepair, dto);
				getDao(QiAppliedRepairMethodDao.class).save(repairMethod);
				// if it came here, act prob was found and repaired
				// update defect status
				if (dto.isUpdateDefectStatus()) {
					DefectStatusHelper.updateDefectStatus(qiRepair.getDefectResultId());
				}
				resp = true;
				List<QiRepairResult> notfixedAP = getDao(QiRepairResultDao.class)
						.findAllByDefectIdAndCurrentStatus(dto.getDefectResultId(), DefectStatus.NOT_FIXED);
				if (notfixedAP == null || notfixedAP.isEmpty()) {
					qiDefect.setCurrentDefectStatus((short) dto.getCurrentDefectStatus());
					qiDefect.setDeleted((short) 0);
					getDao(QiDefectResultDao.class).save(qiDefect); // save the current defect status (FIXED)

				}

			} else {// defect not found
				resp = false;
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append(e).append("\n").append("repairing actual problem-");
			if (dto != null) {
				sb.append("Repair id:").append(dto.getRepairId()).append("DefectResultId")
						.append(dto.getDefectResultId());
			}
			getLogger().error(sb.toString());
			resp = false;
		}

		return resp;
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());
	}

	@Transactional
	@Override
	public DefectStatus getDefectStatus(String productId) {
		DefectStatus resp = DefectStatus.NOT_FIXED;

		long notFixed = getDao(QiDefectResultDao.class).findNotFixedDefectCountByProductId(productId);
		if (notFixed == 0)
			resp = DefectStatus.FIXED;
		return resp;
	}

	@Transactional
	@Override
	public List<QiDefectResult> getDefectsForProduct(String productId) {
		List<QiDefectResult> defectList = null;
		if (!StringUtils.isBlank(productId))
			defectList = getDao(QiDefectResultDao.class).findAllByProductId(productId);
		return defectList;
	}

}