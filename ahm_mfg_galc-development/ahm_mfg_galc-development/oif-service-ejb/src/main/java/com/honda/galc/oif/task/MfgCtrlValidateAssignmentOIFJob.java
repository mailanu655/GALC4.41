package com.honda.galc.oif.task;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCMdrsManpowerAssignmentDao;
import com.honda.galc.dao.conf.MCMdrsTrainingDao;
import com.honda.galc.dao.conf.MCMdrsTrainingStatusDao;
import com.honda.galc.dao.conf.MCProcessAssignmentDao;
import com.honda.galc.dao.conf.MCTrainingDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.conf.MCMdrsManpowerAssignment;
import com.honda.galc.entity.conf.MCMdrsManpowerAssignmentId;
import com.honda.galc.entity.conf.MCMdrsTraining;
import com.honda.galc.entity.conf.MCMdrsTrainingId;
import com.honda.galc.entity.conf.MCMdrsTrainingStatus;
import com.honda.galc.entity.conf.MCMdrsTrainingStatusId;
import com.honda.galc.entity.conf.MCProcessAssignment;
import com.honda.galc.entity.conf.MCProcessAssignmentId;
import com.honda.galc.entity.conf.MCTraining;
import com.honda.galc.entity.conf.MCTrainingId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.util.LDAPService;

public class MfgCtrlValidateAssignmentOIFJob extends OifAbstractTask implements
		IEventTaskExecutable {

	private String errorMessage = "";
	private boolean errorsPresent = false;
	Map<String, String> userIdNetworkIdMap = new HashMap<String, String>();
	HashSet<String> userIdNotInLdap = new HashSet<String>();

	SimpleDateFormat processingDayTsf = new SimpleDateFormat("MM/dd/yyyy");

	private final String EMAIL_SUBJECT = "Manufacturing Control Validate Assignment OIF Job : ";
	private static final String THANKS = "\n\nMessage From";
	private static final String THANKS_MESSAGE = "\nManufacturing Control Validate Assignemnt OIF Job";
	private static final String INIT_MESSAGE = "Manufacturing Control Validate Assignment OIF Job executed for the day :";

	public MfgCtrlValidateAssignmentOIFJob(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {


		Timestamp startTs = new Timestamp(System.currentTimeMillis());

		OifServiceEMailHandler emailHandler = new OifServiceEMailHandler(
				getName());

		populateAssignmentDetails();
		String finalMessage = "";

		finalMessage = errorsPresent ? "There are no Errors"
				: " Errors Occurred. Please check logs ";

		emailHandler.delivery(EMAIL_SUBJECT + processingDayTsf.format(startTs),
				INIT_MESSAGE + finalMessage + THANKS + THANKS_MESSAGE);
	}

	public void populateAssignmentDetails() {
		Map<String, Set<String>> responseMap = new HashMap<String, Set<String>>();
		MCMdrsManpowerAssignmentDao mdrsManpowerAssignmentDao = ServiceFactory
				.getDao(MCMdrsManpowerAssignmentDao.class);

		List<Object[]> assignmentList = mdrsManpowerAssignmentDao
				.getManPowerAssignmentsFromMDRS();

		for (Object[] assignment : assignmentList) {

			if (checkIfDataComplete(assignment)) {
				insertProcessAssignmentRecord(assignment);
				insertManpowerAssignmentRecord(assignment);
			}
		}
		

	}

	private boolean checkIfDataComplete(Object[] assignment) {
		errorMessage = "";

		errorMessage += assignment[8] == null ? "Line " : "";
		errorMessage += assignment[7] == null ? " PDDA Platform Id" : "";
		errorMessage += assignment[13] == null ? " GALC Schedule Period " : "";
		errorMessage += assignment[10] == null ? " Plant Code " : "";
		errorMessage += assignment[9] == null ? " Process Location " : "";
		errorMessage += assignment[12] == null ? " Shift " : "";
		errorMessage += assignment[11] == null ? " Production Date " : "";
		errorMessage += assignment[6] == null ? " Process Point Id " : "";
		errorMessage += assignment[5] == null ? " Associate No " : "";

		if (errorMessage.length() > 0) {
			errorsPresent = true;
			Logger.getLogger().error(
					"Following columns are missing from MDRS import "
							+ errorMessage);
		}
		return errorMessage.length() == 0;

	}

	private void insertProcessAssignmentRecord(Object[] assignment) {

		MCProcessAssignmentDao mcProcessAssignmentDao = ServiceFactory
				.getDao(MCProcessAssignmentDao.class);

		MCProcessAssignment mcProcessAssignment = new MCProcessAssignment();
		MCProcessAssignmentId mcProcessAssignmentId = new MCProcessAssignmentId();
		mcProcessAssignmentId.setLineNo(assignment[8].toString());
		mcProcessAssignmentId.setPddaPlatformId(Integer.parseInt(assignment[7]
				.toString()));
		mcProcessAssignmentId.setPeriod(Integer.parseInt(assignment[13]
				.toString()));
		mcProcessAssignmentId.setPlantCode(assignment[10].toString());
		mcProcessAssignmentId.setProcessLocation(assignment[9].toString());
		mcProcessAssignmentId.setProcessPointId(assignment[6].toString());
		mcProcessAssignmentId.setProductionDate(Timestamp
				.valueOf(assignment[11].toString()));
		mcProcessAssignmentId.setShift(assignment[12].toString());
		mcProcessAssignmentId.setAssociateNo(assignment[5].toString());

		mcProcessAssignment.setId(mcProcessAssignmentId);

		mcProcessAssignment.setApproveMethod("");
		mcProcessAssignment.setApproverNo("");
		mcProcessAssignment.setComments("");
		Date date = new Date();
		Timestamp current = new Timestamp(date.getTime());
		mcProcessAssignment.setCreateTimestamp(current);

		mcProcessAssignmentDao.save(mcProcessAssignment);

	}

	private void insertManpowerAssignmentRecord(Object[] assignment) {

		MCMdrsManpowerAssignment mcMdrsManpowerAssignment = new MCMdrsManpowerAssignment();
		MCMdrsManpowerAssignmentId mcMdrsManpowerAssignmentId = new MCMdrsManpowerAssignmentId();
		mcMdrsManpowerAssignmentId.setDeptCode(assignment[2].toString());
		mcMdrsManpowerAssignmentId.setExtractDate(Timestamp
				.valueOf(assignment[4].toString()));
		mcMdrsManpowerAssignmentId.setLastUpdatedDate(Timestamp
				.valueOf(assignment[3].toString()));
		mcMdrsManpowerAssignmentId.setManpowerAssignmentId(Long
				.parseLong(assignment[0].toString()));
		mcMdrsManpowerAssignmentId.setPlantLocCode(assignment[1].toString());

		mcMdrsManpowerAssignment.setId(mcMdrsManpowerAssignmentId);

		String associateNo = assignment[5].toString();
		mcMdrsManpowerAssignment
				.setAssociateId(getMdrsUserNetworkId(associateNo));
		mcMdrsManpowerAssignment.setAssociateNo(associateNo);
		mcMdrsManpowerAssignment.setCreateTimestamp(null);
		mcMdrsManpowerAssignment.setLineNo(assignment[8].toString());
		mcMdrsManpowerAssignment.setPddaPlatformId(Integer
				.parseInt(assignment[7].toString()));
		mcMdrsManpowerAssignment.setPeriod(Integer.parseInt(assignment[13]
				.toString()));
		mcMdrsManpowerAssignment.setPlantCode(assignment[10].toString());
		mcMdrsManpowerAssignment.setProcessLocation(assignment[9].toString());
		mcMdrsManpowerAssignment.setProcessPointId(assignment[6].toString());
		mcMdrsManpowerAssignment.setProductionDate(Timestamp
				.valueOf(assignment[11].toString()));
		mcMdrsManpowerAssignment.setShift(assignment[12].toString());

		MCMdrsManpowerAssignmentDao mcMdrsManpowerAssignmentDao = ServiceFactory
				.getDao(MCMdrsManpowerAssignmentDao.class);
		mcMdrsManpowerAssignmentDao.save(mcMdrsManpowerAssignment);

	}

	private String getMdrsUserNetworkId(String mdrsUserLogonIdNo) {
		String networkId = null;

		networkId = userIdNetworkIdMap.get(mdrsUserLogonIdNo);

		if (userIdNotInLdap.contains(mdrsUserLogonIdNo))
			return null;

		if (networkId == null) {
			networkId = LDAPService.getInstance().getUseridWithPrefix(
					mdrsUserLogonIdNo);
			if (networkId != null)
				userIdNetworkIdMap.put(mdrsUserLogonIdNo, networkId);
			else {
				userIdNotInLdap.add(mdrsUserLogonIdNo);
				Logger.getLogger().warn(
						"LDAP doesn't have details for the mdrs user :: "
								+ mdrsUserLogonIdNo);
			}
		}

		return networkId;
	}

}
