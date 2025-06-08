package com.honda.galc.client.teamleader.frame;

import static com.honda.galc.client.teamleader.frame.EngineMissionAssignmentPanel.ASSIGNMENT_ACTION_COMMAND;
import static com.honda.galc.client.teamleader.frame.EngineMissionAssignmentPanel.ASSIGN_ENGINE_LABEL;
import static com.honda.galc.client.teamleader.frame.EngineMissionAssignmentPanel.ASSIGN_LABEL;
import static com.honda.galc.client.teamleader.frame.EngineMissionAssignmentPanel.ASSIGN_MISSION_LABEL;
import static com.honda.galc.client.teamleader.frame.EngineMissionAssignmentPanel.DEASSIGNMENT_ACTION_COMMAND;
import static com.honda.galc.client.teamleader.frame.EngineMissionAssignmentPanel.DEASSIGN_LABEL;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.honda.galc.client.common.util.EngineLoadUtility;
import com.honda.galc.client.datacollection.property.EngineLoadPropertyBean;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.ComboBoxState;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameEngineModelMapDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>EngineMissionAssignmentController</code> is ....
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
 * @created Nov 13, 2013
 * 
 * @author Gangadhararao Gadde
 * @date May 04, 2016
 * 
 * @author K Maharjan	
 * @date May 31, 2017
 * 
 */
// TODO - in the future we may want to :
// 1. implement frame/engine assignment logic in stateless service
// so it could be reused from TeamLeader clients or from Headed/Headless line
// clients. See also EngineLoadSnProcessor,
// LotControlEngineLoadPersistenceManager,
// ManualLotControlRepairEngineController.
// 2. add product checker to run additional validation for engine (defect
// status, holds, etc ...)
// 

/*
 * Functionality moved to com.honda.galc.client.frame.engine.assignment.EngineMissionAssignmentController
 */
@Deprecated
public class EngineMissionAssignmentController extends BaseListener<EngineMissionAssignmentPanel> implements ActionListener, FocusListener {

	private static final String NO_ENGINE_DEASSIGN_STATUSES = "NO_ENGINE_DEASSIGN_STATUSES";
	private static final String LCR_SOURCE = "LCR";
	
	// === business model === //
	private Frame frame;
	private FrameSpec frameSpec;
	private Engine engine;
	private EngineSpec engineSpec;
	private ProcessPoint lastProcessPoint;
	private ProductResult productResult;
	
	private String[] noEngineDeassignStatuses;

	// === ui support === //
	private Component lastFocusedComponent;
	private DateFormat dateTimeFormat;
	private ProductCheckUtil checkUtil = null;
	private boolean  useAltEngineMto =false;
	private boolean updateEngineMtoCheck = false;
	private boolean isUsingAltMtoFlag = false;
	private boolean isMissionTypeMismatched = false;

	public EngineMissionAssignmentController(EngineMissionAssignmentPanel view) {
		super(view);
		this.dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		checkUtil = new ProductCheckUtil();
	    useAltEngineMto=PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getView().getApplicationId()).isUseAltEngineMto();
	    updateEngineMtoCheck = PropertyService.getPropertyBean(EngineLoadPropertyBean.class, getView().getApplicationId()).isEngineMtoUpdateEnabled();
	}

	// === event dispatching === //
	// === action === //
	@Override
	protected void executeActionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(getView().getProductIdTextField())) {
			processVin();
		} else if (ae.getSource().equals(getView().getResetButton())) {
			nextProduct();
		} else if (ae.getSource().equals(getView().getEinTextField())) {
			if (TextFieldState.READ_ONLY.isInState(getView().getProductIdTextField())) processEin();
			else processVinByEin();
		} else if (ae.getSource().equals(getView().getEinResetButton())) {
			resetEin();
		} else if (ae.getSource().equals(getView().getEngineAssignButton())) {
			processEngineAssignment();
		} else if (ae.getSource().equals(getView().getMissionSnTextField())) {
			processMissionSn();
		} else if (ae.getSource().equals(getView().getMissionTypeTextField())) {
			processMissionType();
		} else if (ae.getSource().equals(getView().getMissionSnResetButton())) {
			resetMissionInput();
		} else if (ae.getSource().equals(getView().getMissionAssignButton())) {
			processMissionAssignment();
		}
	}

	// === focus === //
	public void focusGained(FocusEvent fe) {
	}

	public void focusLost(FocusEvent fe) {
		if (fe.getComponent() != null && fe.getComponent().isEnabled()) {
			setLastFocusedComponent(fe.getComponent());
		}
	}

	// === action handlers === //
	protected void processVin() {
		getView().getEinTextField().setText(null);
		selectData();
		if (getFrame() == null) {
			return;
		}
		if (StringUtils.isNotBlank(getFrame().getEngineSerialNo())) {
			setEngine(getEngineDao().findBySn(getFrame().getEngineSerialNo()));
		}
		selectProcessLocationData();
		locationToProcessing();
		missionToProcessing();
		engineToProcessing();
		frameToProcessing();
		toProcessing();
		validateExistingAssignmentIntegrity();
	}

	// === action handlers === /
		protected void processVinByEin() {
			String ein = getView().getEinTextField().getText();
			ein = StringUtils.trim(ein);
			Engine engine = getEngineDao().findBySn(ein); 
			if (engine == null) getMainWindow().setErrorMessage("EIN does not exist for "+ein);
			else if (engine.getVin() == null) getMainWindow().setErrorMessage("VIN not Assigned for EIN "+ein);
			else {
				getView().getProductIdTextField().setText(engine.getVin());
				processVin();
			}
		}
	
	protected void nextProduct() {
		if (getView().getEinResetButton().isEnabled() || getView().getMissionSnResetButton().isEnabled()) {
			int retCode = JOptionPane.showConfirmDialog(getView(), "You may have unsaved data on the screen. \nAre you sure you want to go to the next Product ?", "Next Product", JOptionPane.YES_NO_OPTION);
			if (retCode != JOptionPane.YES_OPTION) {
				if (getLastFocusedComponent() != null && getLastFocusedComponent().isEnabled()) {
					getLastFocusedComponent().requestFocus();
				}
				return;
			}
		}
		resetModel();
		toIdle();
	}

	protected void processEin() {
		String ein = getView().getEinTextField().getText();
		ein = StringUtils.trim(ein);
		getView().getEinTextField().setText(ein);
		if (!UiUtils.isValid(getView().getEinTextField(), getMainWindow())) {
			getView().getEinTextField().selectAll();
			sectionToError(getView().getEinTextField(), getView().getEngineTypeTextField(), getView().getEngineAssignButton(), getView().getEinResetButton());
			return;
		}
		processEin(ein);
	}

	protected void resetEin() {
		setEngine(null);

		getView().getMissionSnTextField().setText("");
		getView().getMissionTypeTextField().setText("");
		sectionToDisabled(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());

		getView().getEinTextField().setText("");
		getView().getEngineTypeTextField().setText("");
		sectionToEdit(getView().getEinTextField(), getView().getEngineRequiredTypeComboBox(), getView().getEngineAssignButton(), getView().getEinResetButton());
		
	}

	protected void processEngineAssignment() {
		if (ASSIGNMENT_ACTION_COMMAND.equals(getView().getEngineAssignButton().getActionCommand())) {
			int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Engine Assignment", JOptionPane.YES_NO_OPTION);
			if (retCode == JOptionPane.YES_OPTION) {
				assignEngine();
			}
			return;
		}
		if (DEASSIGNMENT_ACTION_COMMAND.equals(getView().getEngineAssignButton().getActionCommand())) {
			int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Engine Deassignment", JOptionPane.YES_NO_OPTION);
			if (retCode == JOptionPane.YES_OPTION) {
				deassignEngine();
			}
			return;
		}
	}

	protected void processMissionSn() {
		String msn = getView().getMissionSnTextField().getText();
		msn = StringUtils.trim(msn);
		getView().getMissionSnTextField().setText(msn);

		if (!UiUtils.isValid(getView().getMissionSnTextField(), getMainWindow())) {
			getView().getMissionSnTextField().requestFocus();
			return;
		}

		String msg = validateMissionSn(msn);
		if (msg != null) {
			getMainWindow().setErrorMessage(msg);
			getView().getMissionSnTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getMissionSnTextField());
			return;
		}

		TextFieldState.READ_ONLY.setState(getView().getMissionSnTextField());
		getView().getMissionSnResetButton().setEnabled(true);

		if (!TextFieldState.READ_ONLY.isInState(getView().getMissionTypeTextField())) {
			getView().getMissionTypeTextField().requestFocus();
			return;
		}

		sectionToValidated(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
	}

	protected void processMissionType() {
		String ymt = getView().getMissionTypeTextField().getText();
		ymt = StringUtils.trim(ymt);
		getView().getMissionTypeTextField().setText(ymt);

		if (!UiUtils.isValid(getView().getMissionTypeTextField(), getMainWindow())) {
			getView().getMissionTypeTextField().requestFocus();
			return;
		}

		String msg = validateMissionType(ymt);
		if (msg != null) {
			getMainWindow().setErrorMessage(msg);
			getView().getMissionTypeTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getMissionTypeTextField());
			return;
		}

		TextFieldState.READ_ONLY.setState(getView().getMissionTypeTextField());
		getView().getMissionSnResetButton().setEnabled(true);

		if (!TextFieldState.READ_ONLY.isInState(getView().getMissionSnTextField())) {
			getView().getMissionSnTextField().requestFocus();
			return;
		}
		sectionToValidated(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
	}

	protected void resetMissionInput() {
		getView().getMissionTypeTextField().setText("");
		getView().getMissionSnTextField().setText("");
		msnToEdit(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
	}

	protected void processMissionAssignment() {
		if (ASSIGNMENT_ACTION_COMMAND.equals(getView().getMissionAssignButton().getActionCommand())) {
			int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Mission Assignment", JOptionPane.YES_NO_OPTION);
			if (retCode == JOptionPane.YES_OPTION) {
				assignMission();
			}
			return;
		}
		if (DEASSIGNMENT_ACTION_COMMAND.equals(getView().getMissionAssignButton().getActionCommand())) {
			int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Mission Deassignment", JOptionPane.YES_NO_OPTION);
			if (retCode == JOptionPane.YES_OPTION) {
				deassignMission();
			}
			return;
		}
	}

	// === validation api === //
	protected void validateExistingAssignmentIntegrity() {

		String einFr = getView().getEinTextField().getText();
		Engine engine = getEngine();
		Frame frame = getFrame();
		String missionSn = getView().getMissionSnTextField().getText();

		if (StringUtils.isNotBlank(einFr)) {
			String msg = null;
			if (StringUtils.isNotBlank(einFr) && engine == null) {
				msg = String.format("Invalid EIN %s, engine does not exist", einFr);
			} else if (StringUtils.isBlank(engine.getVin())) {
				msg = "Invalid EIN assignment, Engine assigned in frame table, does not have Frame assigned in engine table";
			} else if (!frame.getProductId().equals(engine.getVin())) {
				msg = String.format("Invalid EIN assignment, EIN assigned in frame table points to the Engine that is assigned to different VIN %s in engine table", engine.getVin());
			} else if (!checkUtil.checkEngineTypeForEngineAssignment(frame, engine, useAltEngineMto)) {
				msg = String.format("Engine SpecCode does not match Required Type");
			}
			if (msg != null) {
				if (StringUtils.isNotBlank(missionSn)) {
					sectionToErrorReadOnly(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
					getView().getMissionAssignButton().setEnabled(false);
				} else {
					sectionToDisabled(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
				}
				sectionToErrorReadOnly(getView().getEinTextField(), getView().getEngineTypeTextField(), getView().getEngineAssignButton(), getView().getEinResetButton());
				getMainWindow().setErrorMessage(msg);
				getMainWindow().getLogger().warn(msg);
				return;
			}
		}
		validateMissionExistingAssignmentIntegrity();
	}

	protected void validateMissionExistingAssignmentIntegrity() {

		String missionYmtRequired = getView().getMissionRequiredTypeTextField().getText();
		String missionSnUi = getView().getMissionSnTextField().getText();
		String missionSnEng = getEngine() != null ? getEngine().getMissionSerialNo() : null;
		String missionYmtUi = getView().getMissionTypeTextField().getText();
		String missionYmtEng = getEngine() != null ? getEngine().getActualMissionType() : null;

		if (StringUtils.isBlank(missionSnUi) && (getEngine() == null || StringUtils.isBlank(missionSnEng))) {
			return;
		}

		String msg = null;
		if (getEngine() == null) {
			msg = "MissionSN is assigned to Frame but Engine is not assigned";
		} else if (StringUtils.isBlank(missionSnUi) && StringUtils.isNotBlank(missionSnEng)) {
			msg = String.format("MissionSN %s is assigned to Engine but it is not assigned to Frame", missionSnEng);
		} else if (StringUtils.isNotBlank(missionSnUi) && StringUtils.isBlank(missionSnEng)) {
			msg = String.format("MissionSN %s is assigned to Frame but it is not assigned to Engine", missionSnUi);
		} else if (StringUtils.isNotBlank(missionSnUi) && !StringUtils.equals(missionSnUi, missionSnEng)) {
			msg = String.format("MissionSN %s assigned to Frame is different from MissionSN %s assigned to Engine", missionSnUi, missionSnEng);
		} else if (getView().getPropertyBean().isMissionTypeRequired() && StringUtils.isBlank(missionYmtUi)) {
			msg = "Mission Type can not be empty";
		} else if (getView().getPropertyBean().isMissionTypeRequired() && StringUtils.isBlank(missionYmtEng)) {
			msg = "Mission Type assigned to Engine is empty";
		} else if (getView().getPropertyBean().isMissionTypeRequired() && !StringUtils.equals(missionYmtUi, missionYmtEng)) {
			msg = String.format("Missiong Type assigned to Frame is different from Mission Type assigned to Engine %s", missionYmtEng);
		} else if (getView().getPropertyBean().isMissionTypeRequired() && !(validMissionType(engine.getActualMissionType(),getView().getMissionRequiredTypeTextField().getText()))) {
			msg = "Mission Type is different from Required Mission Type";
		}

		if (msg != null) {
			sectionToErrorReadOnly(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
			getMainWindow().setErrorMessage(msg);
			getMainWindow().getLogger().warn(msg);
			return;
		}
	}

	protected String validateEngine(Engine engine) {
		String msg = null;
		List<Frame> frames = null;
		if (engine == null) {
			msg = String.format("Engine does not exist for the EIN");
		} else if (!checkUtil.checkEngineTypeForEngineAssignment(getFrame(), engine, useAltEngineMto)) {
			msg = String.format("Engine SpecCode %s does not match to Engine Required Type", engine.getProductSpecCode());
		} else if (StringUtils.isNotBlank(engine.getVin()) && ObjectUtils.notEqual(getFrame().getProductId(), engine.getVin())) {
			msg = String.format("Engine is already assigned to VIN : %s ", engine.getVin());
		} else if ((frames = getFrameDao().findByEin(engine.getProductId())) != null && frames.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Frame f : frames) {
				if (!StringUtils.equals(getFrame().getProductId(), f.getProductId())) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(f.getProductId());
				}
			}
			if (sb.length() > 0) {
				msg = String.format("Engine is already assigned to VIN : %s ", sb);
			}
		}
		return msg;
	}

	protected String validateMissionSn(String missionSn) {
		String msg = null;
		List<Engine> engines = null;
		List<Frame> frames = null;

		if ((frames = getFrameDao().findAllByMissionSn(missionSn)) != null && frames.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Frame e : frames) {
				if (!StringUtils.equals(getFrame().getProductId(), e.getProductId())) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(e.getProductId());
				}
			}
			if (sb.length() > 0) {
				msg = String.format("Mission is already assigned to VIN : %s ", sb);
			}
		} else if ((engines = getEngineDao().findAllByMissionSn(missionSn)) != null && engines.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Engine e : engines) {
				if (!StringUtils.equals(getEngine().getProductId(), e.getProductId())) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(e.getProductId());
				}
			}
			if (sb.length() > 0) {
				msg = String.format("Mission is already assigned to EIN : %s ", sb);
			}
		}
		return msg;
	}

	protected String validateMissionType(Engine engine) {
		String msg = null;
		if(getView().getPropertyBean().isMissionTypeRequired() && updateEngineMtoCheck){
			msg = validateInstalledMission(frame, engine);
		}
		else if (getView().getPropertyBean().isMissionTypeRequired() && !validMissionType(engine.getActualMissionType(), getView().getMissionRequiredTypeTextField().getText())) {
			msg = String.format("Mission Type %s assigned to engine does not match Required Type, MissionSN : %s", engine.getActualMissionType(), engine.getMissionSerialNo());
		}
		return msg;
	}

	/**
	 * Description
	 * 
	 * @param frame2
	 * @param engine2
	 * void
	 */
	protected String validateInstalledMission(Frame frame, Engine engine) {
		FrameSpec frameSpec = getFrameSpecDao().findByKey(frame.getProductSpecCode());
		String vinEngineMTO = StringUtils.trimToEmpty(frameSpec.getEngineMto());
		String altEngineMto=StringUtils.trimToEmpty(frameSpec.getAltEngineMto());
        String engineProductSpecCode=StringUtils.trimToEmpty(engine.getProductSpecCode());
		String msg = null;
		isUsingAltMtoFlag = false;
		if (altEngineMto != null && altEngineMto.equals(engineProductSpecCode) && !altEngineMto.equals(vinEngineMTO)) {
			 String actualMissionTypeInstalled = engine.getActualMissionType();			 
			 String expectedMissomTypeMask = getMissionTypeFromLotControlRule(getView().getPropertyBean().getMissionTypePartName(), getView().getPropertyBean().getMissionInstalledPartProductType());
			 
	         if(!validMissionType(actualMissionTypeInstalled,expectedMissomTypeMask)){  
	        	 isMissionTypeMismatched = true;
	         }
	         isUsingAltMtoFlag = true;
	         
		}
        return msg; 
	}
	
	protected boolean isEngineMtoUpdateRequired(Frame frame, Engine engine){
		FrameSpec frameSpec = getFrameSpecDao().findByKey(frame.getProductSpecCode());
		String vinEngineMTO = StringUtils.trimToEmpty(frameSpec.getEngineMto());
		String altEngineMto=StringUtils.trimToEmpty(frameSpec.getAltEngineMto());
        String engineProductSpecCode=StringUtils.trimToEmpty(engine.getProductSpecCode());
		if (altEngineMto != null && altEngineMto.equals(engineProductSpecCode) && !altEngineMto.equals(vinEngineMTO)) {
	         isUsingAltMtoFlag = true;
		}
		else  isUsingAltMtoFlag = false;
        return isUsingAltMtoFlag;
	}

	protected boolean isAltMtoUsed(){
		FrameSpec frameSpec = getFrameSpecDao().findByKey(frame.getProductSpecCode());
		String altEngineMto=StringUtils.trimToEmpty(frameSpec.getAltEngineMto());
        String engineProductSpecCode=StringUtils.trimToEmpty(engine.getProductSpecCode());
		if (altEngineMto != null && altEngineMto.equals(engineProductSpecCode)) {
			return true;
		}
		return false;
	}
	
	protected String validateMissionType(String missionType) {
		String msg = null;
		if (getView().getPropertyBean().isMissionTypeRequired() && !(validMissionType(missionType, getView().getMissionRequiredTypeTextField().getText())) ) {
			msg = String.format("Mission Type %s does not match Required Type", missionType);
		}
		return msg;
	}

	// === handlers supporting api === //
	protected Engine processEin(String ein) {
		isMissionTypeMismatched = false;
		Engine engine = getEngineDao().findBySn(ein);
		String msg = validateEngine(engine);
		if (msg != null) {

			getView().getEinTextField().selectAll();
			getView().getEngineTypeTextField().setText("");
			sectionToError(getView().getEinTextField(), getView().getEngineTypeTextField(), getView().getEngineAssignButton(), getView().getEinResetButton());
			getMainWindow().setErrorMessage(msg);
			return null;
		}

		if (StringUtils.isNotBlank(engine.getMissionSerialNo())) {
			msg = validateMissionType(engine);
			if (msg != null) {
				getMainWindow().setErrorMessage(msg);
				sectionToDisabled(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
				sectionToError(getView().getEinTextField(), getView().getEngineTypeTextField(), getView().getEngineAssignButton(), getView().getEinResetButton());
				return null;
			} else {
				getView().getMissionSnTextField().setText(engine.getMissionSerialNo());
				getView().getMissionTypeTextField().setText(engine.getActualMissionType());
				sectionToValidated(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), null, null);
				if(isMissionTypeMismatched){
					sectionToError(getView().getMissionTypeTextField(),getView().getMissionSnTextField(),  getView().getMissionAssignButton(), getView().getMissionSnResetButton());
					getMainWindow().setMessage("Assign engine has Mission installed with wrong type");
				}
			}
		} else {
			if(getView().getPropertyBean().isMissionTypeRequired() && updateEngineMtoCheck){
				isEngineMtoUpdateRequired(frame,engine);
			}
			sectionToDisabled(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
		}
		getView().getEngineRequiredTypeComboBox().setModel(new ComboBoxModel<String>(new SortedArrayList<String>()));
		getView().getEngineRequiredTypeComboBox().getEditor().setItem(engine.getProductSpecCode());
		sectionToValidated(getView().getEinTextField(), getView().getEngineRequiredTypeComboBox(), getView().getEngineAssignButton(), getView().getEinResetButton());
		return engine;
	}

	protected void assignEngine() {

		String ein = getView().getEinTextField().getText();
		Engine engine = processEin(ein);
		if (engine == null) {
			return;
		}
		if(isMissionTypeMismatched){
			int retCode = JOptionPane.showConfirmDialog(getView(), "Mission will be deassigned, correct Mission and type need to be inserted.","Mission Deassign", JOptionPane.YES_NO_OPTION);
			if (retCode == JOptionPane.YES_OPTION) {
				setEngine(engine);
				deassignMission();
				}
			else return;

		}
		
		engine.setVin(getFrame().getProductId());
		setEngine(engine);
		getFrame().setEngineSerialNo(getEngine().getProductId());
		getFrame().setEngineStatus(true);
		if (StringUtils.isNotBlank(engine.getMissionSerialNo())) {
			getFrame().setActualMissionType(engine.getActualMissionType());
			getFrame().setMissionSerialNo(engine.getMissionSerialNo());
		} else {
			getFrame().setActualMissionType(null);
			getFrame().setMissionSerialNo(null);
		}

		setEngine(getEngineDao().save(engine));
		logUserAction(SAVED, engine);
		setFrame(getFrameDao().save(getFrame()));
		logUserAction(SAVED, getFrame());
		
		try {
			InstalledPart enginePart = getEngineOrMissionFromLotControlRule(getView().getPropertyBean().getEnginePartName(), ein, getView().getPropertyBean().getEngineInstalledPartProductType());
			InstalledPart missionPart = getEngineOrMissionFromLotControlRule(getView().getPropertyBean().getMissionPartName(), engine.getMissionSerialNo(), getView().getPropertyBean().getMissionInstalledPartProductType());
			List<InstalledPart> installedPartList = new ArrayList<InstalledPart>();
			if(null != enginePart) 
				installedPartList.add(enginePart);
			if(null != missionPart) 
				installedPartList.add(missionPart);
			
			if(installedPartList != null && installedPartList.size() > 0) {
				ServiceFactory.getDao(InstalledPartDao.class).saveAll(installedPartList, true);
				logUserAction(SAVED, installedPartList);
			}
			
			String logMsg = String.format("User: %s,  - assigned Engine: %s, to VIN: %s", getView().getMainWindow().getUserId(), enginePart.getPartSerialNumber(), enginePart.getProductId());
			getMainWindow().getLogger().info(logMsg);
			
		} catch(Exception e) {
			String logMsg = String.format("User: %s,  - Exception while assigning Engine in Installed Part table :%s", getView().getMainWindow().getUserId(), ExceptionUtils.getStackTrace(e));
			getMainWindow().getLogger().error(logMsg);
		}
		
		sectionToAssigned(getView().getEinTextField(), getView().getEngineRequiredTypeComboBox(), getView().getEngineAssignButton(), getView().getEinResetButton());
		if (StringUtils.isNotBlank(engine.getMissionSerialNo())) {
			sectionToAssigned(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
			getView().getResetButton().requestFocus();
		} else {
			msnToEdit(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
		}

		String vin = getFrame().getId();
		String engineType = getEngine().getProductSpecCode();
		String msn = getFrame().getMissionSerialNo();
		String missionType = getFrame().getActualMissionType();
		String logMsg = String.format("Assigned Engine/Mission to Frame - VIN:%s, EIN:%s, EngineType:%s, MissionSn:%s, MissionType:%s", vin, ein, engineType, msn, missionType);
		getMainWindow().getLogger().info(logMsg);
		
		//update product spec for alt engine
		if(isUsingAltMtoFlag){
			EngineLoadUtility utility = new EngineLoadUtility();
			utility.updateEngineSpec(getFrame().getProductId(), getEngine());
		}
	}

	protected void deassignEngine() {

		String vin = getFrame().getId();
		String ein = getEngine() != null ? getEngine().getProductId() : getFrame().getEngineSerialNo();
		String logMsg = String.format("Deassigned Engine/Mission from Frame - VIN:%s, EIN:%s", vin, ein);

		getFrame().setEngineSerialNo(null);
		getFrame().setEngineStatus(false);
		getFrame().setActualMissionType(null);
		getFrame().setMissionSerialNo(null);
		this.expandComboBox(this.getView().getEngineRequiredTypeComboBox());

		setFrame(getFrameDao().save(getFrame()));
		logUserAction(SAVED, getFrame());
		if (getEngine() != null) {
			getEngine().setVin(null);
			getEngineDao().save(getEngine());
			logUserAction(SAVED, getEngine());
			if (this.getView().getPropertyBean().isAutoMissionDeassignEnabled())
				deassignMission();
		}
		
		try {
			InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
			List<String> partNames = new ArrayList<String>();
			partNames.add(getView().getPropertyBean().getEnginePartName());			
			List<InstalledPart> installedPart = installedPartDao.findAllByProductIdAndPartNames(vin, partNames);
			for(InstalledPart part : installedPart) {
				installedPartDao.remove(part);
				logUserAction(REMOVED, part);
			}
			String logMsg1 = String.format("User: %s,  - De-Assigned Engine: %s, from VIN: %s", getView().getMainWindow().getUserId(), ein, vin);
			getMainWindow().getLogger().info(logMsg1);
		} catch(Exception e) {
			String logMsg2 = String.format("User: %s,  - Exception while De-Assigning Engine: %s, VIN: %s, from Installed Part table :%s", getView().getMainWindow().getUserId(), ein, vin, ExceptionUtils.getStackTrace(e));
			getMainWindow().getLogger().error(logMsg2);

		}

		sectionToDisabled(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
		resetEin();
		getMainWindow().getLogger().info(logMsg);
	}

	public boolean isEngineDeassignAllowed() {
		ShippingStatus shippingStatus = ServiceFactory.getDao(ShippingStatusDao.class).findByKey(frame.getProductId());
		if(shippingStatus != null) {
			String statusName = ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus().intValue()).getName();
			for(int i = 0; i < getNoEngineDeassignStatuses().length; i++) {
				if(statusName.equals(getNoEngineDeassignStatuses()[i])) {
					// Show warning to user for better visual reference
					String msg = String.format("The VIN %s is Shipped", getFrame().getId());
					getMainWindow().setWarningMessage(msg);
					getMainWindow().getLogger().warn(msg);
					return false; 
				}
			}
		}
		return true;
	}
	
	protected void assignMission() {
		String msn = getView().getMissionSnTextField().getText();
		String missionType = getView().getMissionTypeTextField().getText();

		String msg = validateMissionSn(msn);
		if (msg != null) {
			getMainWindow().setErrorMessage(msg);
			getView().getMissionSnTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getMissionSnTextField());
			return;
		}

		getEngine().setMissionSerialNo(msn);
		getEngine().setActualMissionType(missionType);
		getEngine().setMissionStatus(true);
		getFrame().setActualMissionType(missionType);
		getFrame().setMissionSerialNo(msn);

		getEngineDao().save(getEngine());
		logUserAction(SAVED, getEngine());
		getFrameDao().save(getFrame());
		logUserAction(SAVED, getFrame());

		try {
			InstalledPart missionPart = getEngineOrMissionFromLotControlRule(getView().getPropertyBean().getMissionPartName(), msn, getView().getPropertyBean().getMissionInstalledPartProductType());
			InstalledPart missionTypePart=null;
			if(getView().getPropertyBean().isMissionTypeRequired())
			    missionTypePart = getEngineOrMissionFromLotControlRule(getView().getPropertyBean().getMissionTypePartName(), missionType, getView().getPropertyBean().getMissionInstalledPartProductType());
			List<InstalledPart> installedPartList = new ArrayList<InstalledPart>();
			if(null != missionPart) 
				installedPartList.add(missionPart);
			if(getView().getPropertyBean().isMissionTypeRequired() && null != missionTypePart) 
				installedPartList.add(missionTypePart);
			
			if(installedPartList != null && installedPartList.size() > 0) {
				ServiceFactory.getDao(InstalledPartDao.class).saveAll(installedPartList, true);
				logUserAction(SAVED, installedPartList);
			}
			
			String logMsg = String.format("User: %s,  - assigned Engine: %s, to VIN: %s", getView().getMainWindow().getUserId(), missionPart.getPartSerialNumber(), missionPart.getProductId());
			getMainWindow().getLogger().info(logMsg);
			
		} catch(Exception e) {
			String logMsg = String.format("User: %s,  - Exception while assigning Engine in Installed Part table :%s", getView().getMainWindow().getUserId(), ExceptionUtils.getStackTrace(e));
			getMainWindow().getLogger().error(logMsg);

		}

		sectionToAssigned(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
		getView().getResetButton().requestFocus();

		String vin = getFrame().getId();
		String ein = getEngine().getId();
		String engineType = getEngine().getProductSpecCode();

		String logMsg = String.format("Assigned Mission to Frame/Engine - VIN:%s, EIN:%s, EngineType:%s, MissionSn:%s, MissionType:%s", vin, ein, engineType, msn, missionType);
		getMainWindow().getLogger().info(logMsg);
	}

	protected void deassignMission() {
		getFrame().setMissionSerialNo(null);
		getFrame().setActualMissionType(null);
		setFrame(getFrameDao().update(getFrame()));
		logUserAction(UPDATED, getFrame());
		if (getEngine() != null) {
			getEngine().setMissionSerialNo(null);
			getEngine().setActualMissionType(null);
			getEngine().setMissionStatus(false);
			setEngine(getEngineDao().update(getEngine()));
			logUserAction(UPDATED, getEngine());

			try {
				InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
				List<String> partNames = new ArrayList<String>();
				partNames.add(getView().getPropertyBean().getMissionPartName());
				String productId=null;
				if(getView().getPropertyBean().isMissionTypeRequired())
				  partNames.add(getView().getPropertyBean().getMissionTypePartName());
				if(ProductType.FRAME.toString().equalsIgnoreCase(getView().getPropertyBean().getMissionInstalledPartProductType())) {
					productId = getFrame().getProductId();
				} else {
					productId = getEngine().getProductId();
				}
				List<InstalledPart> installedPart = installedPartDao.findAllByProductIdAndPartNames(productId, partNames);
				for(InstalledPart part : installedPart) {
					installedPartDao.remove(part);
					logUserAction(REMOVED, part);
				}
				String logMsg1 = String.format("User: %s,  - De-Assigned Engine: %s, from VIN: %s,  Mission: %s, MissionType: %s", getView().getMainWindow().getUserId(), getEngine().getProductId(), getFrame().getProductId(), getEngine().getMissionSerialNo(), getEngine().getActualMissionType());
				getMainWindow().getLogger().info(logMsg1);
			} catch(Exception e) {
				String logMsg2 = String.format("User: %s,  - Exception while De-Assigning Mission: %s, Mission_Type: %s, VIN: %s, from Installed Part table :%s", getView().getMainWindow().getUserId(), getEngine().getMissionSerialNo(), getEngine().getActualMissionType(), getFrame().getProductId(), ExceptionUtils.getStackTrace(e));
				getMainWindow().getLogger().error(logMsg2);

			}
		}
		resetMissionInput();

		String vin = getFrame().getId();
		String ein = getEngine() != null ? getEngine().getProductId() : getFrame().getEngineSerialNo();
		String msn = getFrame().getMissionSerialNo();
		if (msn == null && getEngine() != null) {
			msn = getEngine().getMissionSerialNo();
		}
		String logMsg = String.format("Deassigned Mission from Frame/Engine - VIN:%s, EIN:%s", vin, ein, msn);
		getMainWindow().getLogger().info(logMsg);
	}

	// === model api === //
	protected void selectData() {
		String productId = getView().getProductIdTextField().getText();
		productId = StringUtils.trim(productId);
		getView().getProductIdTextField().setText(productId);

		if (!UiUtils.isValid(getView().getProductIdTextField(), getMainWindow())) {
			getView().getProductIdTextField().requestFocus();
			return;
		}
		Frame frame = getFrameDao().findBySn(productId);
		if (frame == null) {
			getView().getProductIdTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getProductIdTextField());
			getMainWindow().setErrorMessage(String.format("Product does not exist for %s", productId));
			return;
		}

		if (StringUtils.isBlank(frame.getProductSpecCode())) {
			getView().getProductIdTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getProductIdTextField());
			getMainWindow().setErrorMessage("Frame SpecCode is empty");
			return;
		}

		FrameSpec frameSpec = getFrameSpecDao().findByKey(frame.getProductSpecCode());

		if (frameSpec == null) {
			getView().getProductIdTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getProductIdTextField());
			getMainWindow().setErrorMessage(String.format("Frame SpecCode does not exist for %s", frame.getProductSpecCode()));
			return;
		}

		if (StringUtils.isBlank(frameSpec.getEngineMto())) {
			getView().getProductIdTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getProductIdTextField());
			getMainWindow().setErrorMessage(String.format("EngineMto is empty for Frame ProductSpec %s", frameSpec.getEngineMto()));
		}

		EngineSpec engineSpec = getEngineSpecDao().findByKey(frameSpec.getEngineMto());

		setFrame(frame);
		setFrameSpec(frameSpec);
		setEngineSpec(engineSpec);
	}

	protected void selectProcessLocationData() {

		String processPointId = getFrame().getLastPassingProcessPointId();
		if (StringUtils.isBlank(processPointId)) {
			getMainWindow().setErrorMessage("Last Passing Process Point is empty");
			return;
		}

		ProcessPoint porcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		if (porcessPoint == null) {
			String msg = String.format("Invalid Last Passing ProcessPointId : %s", processPointId);
			getMainWindow().setErrorMessage(msg);
			getView().getProcessPointTextField().setText(processPointId);
			return;
		}
		setLastProcessPoint(porcessPoint);

		List<ProductResult> history = ServiceFactory.getDao(ProductResultDao.class).findAllByProductAndProcessPoint(getFrame().getProductId(), processPointId);
		if (history != null && !history.isEmpty()) {
			ProductResult result = history.get(0);
			setProductResult(result);
		}
	}

	protected void resetModel() {
		setFrame(null);
		setFrameSpec(null);
		setEngine(null);
		setEngineSpec(null);
		setProductResult(null);
		setLastProcessPoint(null);
	}

	// === transition api === //
	protected void toIdle() {
		UiUtils.setText(UiUtils.getComponents(getView(), JTextField.class), "");
		UiUtils.setState(UiUtils.getComponents(getView(), JTextField.class), TextFieldState.DISABLED);
		UiUtils.setEnabled(UiUtils.getComponents(getView(), JButton.class), false);
		getView().getEngineAssignButton().setText(ASSIGN_ENGINE_LABEL);
		getView().getMissionAssignButton().setText(ASSIGN_MISSION_LABEL);
		// === product === //
		TextFieldState.EDIT.setState(getView().getEinTextField());
		TextFieldState.EDIT.setState(getView().getProductIdTextField());
		getView().getProductIdTextField().requestFocus();
	}

	protected void toProcessing() {
		TextFieldState.READ_ONLY.setState(getView().getProductIdTextField());
		getView().getResetButton().setEnabled(true);
		JTextField field = null;
		if ((field = UiUtils.getFirstTextField(getView(), TextFieldState.ERROR)) != null) {
			field.requestFocus();
		} else if ((field = UiUtils.getFirstTextField(getView(), TextFieldState.EDIT)) != null) {
			field.requestFocus();
		} else {
			getView().getResetButton().requestFocus();
		}
	}
	
	// pull Mission model from Lot Control Rules of Mission Install process point
	// Property needs to be created to decide to use 133 table or LCR
	// if property is set to LCR, code needs to be updated to be able to evaluate LCR
	protected String getRequiredMissionType(){
		if (getView().getPropertyBean().getMissionModelSource().equalsIgnoreCase(LCR_SOURCE)) {
			return getMissionTypeFromLotControlRule(getView().getPropertyBean().getMissionTypePartName(), getView().getPropertyBean().getMissionInstalledPartProductType());
		}else {
				return getEngineSpec() != null ?
						String.format("%s%s%s", getEngineSpec().getMissionModelYearCode(), getEngineSpec()
								.getMissionModelCode(), getEngineSpec().getMissionModelTypeCode()):"";
		}		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void frameToProcessing() {
		JComboBox reqTypeComboBox = getView().getEngineRequiredTypeComboBox();
		getView().getProductionLotTextField().setText(getFrame().getProductionLot());
		if (getFrameSpec() != null) {
			getView().getEnginePlantTextField().setText(getFrameSpec().getEnginePlantCode());
			getView().getProdSpecCodeTextField().setText(getFrameSpec().getId());
			if (StringUtils.isBlank(getFrame().getEngineSerialNo()) || getEngine() == null) {
				this.sectionToEdit(getView().getEinTextField(), reqTypeComboBox, getView().getEngineAssignButton(), getView().getEinResetButton());
				this.expandComboBox(reqTypeComboBox);
			} else {
				Set<String> requiredTypes = getEngineYmtoList(getFrameSpec().getId());
				reqTypeComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>(requiredTypes)));
				if (getEngineSpec() != null && requiredTypes.contains(getEngine().getProductSpecCode())){
					reqTypeComboBox.setSelectedItem(getEngine().getProductSpecCode());
				} else {
					reqTypeComboBox.setSelectedItem(getFrameSpec().getEngineMto());
				}
				this.sectionToAssigned(getView().getEinTextField(), reqTypeComboBox, getView().getEngineAssignButton(), getView().getEinResetButton());
			}
		} else {
			reqTypeComboBox.setModel(null);
			ComboBoxState.DISABLED.setState(reqTypeComboBox);
		}
		String missionYmt = getRequiredMissionType();
		getView().getMissionRequiredTypeTextField().setText(missionYmt);

		TextFieldState.READ_ONLY.setState(getView().getProductionLotTextField());
		TextFieldState.READ_ONLY.setState(getView().getProdSpecCodeTextField());
		TextFieldState.READ_ONLY.setState(getView().getEnginePlantTextField());
		TextFieldState.READ_ONLY.setState(getView().getMissionRequiredTypeTextField());
	}
	
	protected TreeSet<String> getEngineYmtoList(String frameYmtoc){
		TreeSet<String> engYmtoList = new TreeSet<String>();
		String engYmto = this.getFrameSpec().getEngineMto();
		String altEngYmto = this.getFrameSpec().getAltEngineMto();
		SortedArrayList<String> comEngYmtoList = this.getAltEngineYmtosForVIN(frameYmtoc);
		if (!StringUtils.isBlank(engYmto)) engYmtoList.add(engYmto);
		if (!StringUtils.isBlank(altEngYmto)) engYmtoList.add(altEngYmto);
		if (comEngYmtoList != null && !comEngYmtoList.isEmpty()) engYmtoList.addAll(comEngYmtoList);
		return engYmtoList;
	}
	
	private SortedArrayList<String> getAltEngineYmtosForVIN(String frmYmto){
		SortedArrayList<String> engineYmtos = new SortedArrayList<String>();
		List<FrameEngineModelMap> records = ServiceFactory.getDao(FrameEngineModelMapDao.class).findAllByFrameYmto(frmYmto);
		for (FrameEngineModelMap record : records) {
			engineYmtos.add(record.getEngineYmto());
		}
		return engineYmtos;
	}
	
	public void expandComboBox(JComboBox comboBox) {
		ArrayList<String> engYmtoList = new ArrayList<String>();
		engYmtoList.addAll(getEngineYmtoList(getFrameSpec().getId()));
		ComboBoxModel<String> model = new ComboBoxModel<String>(engYmtoList);
		comboBox.setModel(model);
		ComboBoxState.EXPANDED.setState(comboBox);
		comboBox.getEditor().setItem(getFrameSpec().getEngineMto());
		comboBox.setSelectedItem(getFrameSpec().getEngineMto());
		comboBox.showPopup();
		comboBox.setPopupVisible(true);
	}

	protected void engineToProcessing() {
		if (StringUtils.isBlank(getFrame().getEngineSerialNo())) {
			sectionToEdit(getView().getEinTextField(), getView().getEngineRequiredTypeComboBox(), getView().getEngineAssignButton(), getView().getEinResetButton());
		} else {
			getView().getEinTextField().setText(getFrame().getEngineSerialNo());
			if (getEngine() != null) {
				getView().getEngineTypeTextField().setText(getEngine().getProductSpecCode());
				sectionToAssigned(getView().getEinTextField(), getView().getEngineRequiredTypeComboBox(), getView().getEngineAssignButton(), getView().getEinResetButton());
			}
		}
	}

	protected void missionToProcessing() {
		if (StringUtils.isBlank(getFrame().getMissionSerialNo())) {
			if (getEngine() == null) {
				sectionToDisabled(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
			} else {
				msnToEdit(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
			}
		} else {
			getView().getMissionSnTextField().setText(getFrame().getMissionSerialNo());
			getView().getMissionTypeTextField().setText(getFrame().getActualMissionType());
			sectionToAssigned(getView().getMissionSnTextField(), getView().getMissionTypeTextField(), getView().getMissionAssignButton(), getView().getMissionSnResetButton());
		}
	}

	protected void locationToProcessing() {

		if (getLastProcessPoint() == null) {
			UiUtils.setState(getView().getLocationTextFields(), TextFieldState.ERROR_READ_ONLY);
			return;
		}

		String division = String.format("%s(%s)", getLastProcessPoint().getDivisionName(), getLastProcessPoint().getDivisionId());
		String line = String.format("%s(%s)", getLastProcessPoint().getLineName(), getLastProcessPoint().getLineId());

		getView().getPlantTextField().setText(getLastProcessPoint().getPlantName());
		getView().getDivisionTextField().setText(division);
		getView().getLineTextField().setText(line);
		getView().getProcessPointTextField().setText(getLastProcessPoint().getDisplayName());

		if (getProductResult() != null) {
			String time = getDateTimeFormat().format(getProductResult().getActualTimestamp());
			getView().getTimestampTextField().setText(time);
		}
		UiUtils.setState(getView().getLocationTextFields(), TextFieldState.READ_ONLY);
	}

	protected void sectionToEdit(JTextField snField, JTextField specField, JButton assignButton, JButton resetButton) {
		TextFieldState.EDIT.setState(snField);
		TextFieldState.DISABLED.setState(specField);
		assignButton.setText(assignButton.getText().replace(DEASSIGN_LABEL, ASSIGN_LABEL));
		assignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
		assignButton.setEnabled(false);
		resetButton.setEnabled(false);
		snField.requestFocus();
	}
	
	protected void sectionToEdit(JTextField snField, JComboBox specComboBox, JButton assignButton, JButton resetButton) {
		TextFieldState.EDIT.setState(snField);
		this.expandComboBox(specComboBox);
		assignButton.setText(assignButton.getText().replace(DEASSIGN_LABEL, ASSIGN_LABEL));
		assignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
		assignButton.setEnabled(false);
		resetButton.setEnabled(false);
		snField.requestFocus();
	}

	protected void msnToEdit(JTextField snField, JTextField specField, JButton assignButton, JButton resetButton) {
		sectionToEdit(snField, specField, assignButton, resetButton);
		TextFieldState.EDIT.setState(specField);
	}

	protected void sectionToValidated(JTextField snField, JTextField specField, JButton assignButton, JButton resetButton) {
		TextFieldState.READ_ONLY.setState(snField);
		TextFieldState.READ_ONLY.setState(specField);
		if (assignButton != null) {
			assignButton.setText(assignButton.getText().replace(DEASSIGN_LABEL, ASSIGN_LABEL));
			assignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
			assignButton.setEnabled(true);
			assignButton.requestFocus();
		}
		if (resetButton != null) {
			resetButton.setEnabled(true);
		}
	}
	
	protected void sectionToValidated(JTextField snField, JComboBox comboBox, JButton assignButton, JButton resetButton) {
		TextFieldState.READ_ONLY.setState(snField);
		ComboBoxState.COLLAPSED.setState(comboBox);
		if (assignButton != null) {
			assignButton.setText(assignButton.getText().replace(DEASSIGN_LABEL, ASSIGN_LABEL));
			assignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
			assignButton.setEnabled(true);
			assignButton.requestFocus();
		}
		if (resetButton != null) {
			resetButton.setEnabled(true);
		}
	}

	protected void sectionToAssigned(JTextField snField, JTextField specField, JButton assignButton, JButton resetButton) {
		assignButton.setText(assignButton.getText().replace(ASSIGN_LABEL, DEASSIGN_LABEL));
		assignButton.setActionCommand(DEASSIGNMENT_ACTION_COMMAND);
		TextFieldState.READ_ONLY.setState(snField);
		TextFieldState.READ_ONLY.setState(specField);
		assignButton.setEnabled(isEngineDeassignAllowed());
		resetButton.setEnabled(false);
		assignButton.requestFocus();
	}
	
	protected void sectionToAssigned(JTextField snField, JComboBox comboBox, JButton assignButton, JButton resetButton) {
		assignButton.setText(assignButton.getText().replace(ASSIGN_LABEL, DEASSIGN_LABEL));
		assignButton.setActionCommand(DEASSIGNMENT_ACTION_COMMAND);
		TextFieldState.READ_ONLY.setState(snField);
		ComboBoxState.COLLAPSED.setState(comboBox);
		assignButton.setEnabled(isEngineDeassignAllowed());
		resetButton.setEnabled(false);
		assignButton.requestFocus();
	}

	protected void sectionToErrorReadOnly(JTextField snField, JTextField specField, JButton assignButton, JButton resetButton) {
		TextFieldState.ERROR_READ_ONLY.setState(snField);
		TextFieldState.ERROR_READ_ONLY.setState(specField);
		if (resetButton != null) {
			resetButton.setEnabled(false);
		}
		if (assignButton != null) {
			assignButton.setText(assignButton.getText().replace(ASSIGN_LABEL, DEASSIGN_LABEL));
			assignButton.setActionCommand(DEASSIGNMENT_ACTION_COMMAND);
			assignButton.setEnabled(true);
			assignButton.requestFocus();
		}
	}

	protected void sectionToError(JTextField snField, JTextField specField, JButton assignButton, JButton resetButton) {
		TextFieldState.ERROR.setState(snField);
		TextFieldState.DISABLED.setState(specField);
		if (assignButton != null) {
			assignButton.setText(assignButton.getText().replace(DEASSIGN_LABEL, ASSIGN_LABEL));
			assignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
			assignButton.setEnabled(false);
		}
		if (resetButton != null) {
			resetButton.setEnabled(false);
		}
		snField.requestFocus();
	}

	protected void sectionToDisabled(JTextField snField, JTextField specField, JButton assignButton, JButton resetButton) {
		assignButton.setText(assignButton.getText().replace(DEASSIGN_LABEL, ASSIGN_LABEL));
		assignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
		TextFieldState.DISABLED.setState(snField);
		TextFieldState.DISABLED.setState(specField);
		assignButton.setEnabled(false);
		resetButton.setEnabled(false);
	}

	protected InstalledPart createInstalledPart(String productId, String partName, String partId, String partSN) {
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setProductId(productId);
		id.setPartName(partName);
		installedPart.setId(id);
		installedPart.setPartId(partId);
		installedPart.setAssociateNo(getView().getMainWindow().getUserId());
		installedPart.setInstalledPartReason("Repaired: ASSIGN_DEASSIGN");
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setPartSerialNumber(partSN);
		return installedPart;
	}
	
	private InstalledPart getEngineOrMissionFromLotControlRule(String partName, String partSerialNumber, String productType) {
		String mtoc = "";
		String productId = "";
		if(ProductType.FRAME.toString().equalsIgnoreCase(productType)) {
			mtoc = getFrame().getProductSpecCode();
			productId = getFrame().getProductId();
		} else {
			mtoc = getEngine().getProductSpecCode();
			productId = getEngine().getProductId();
		}
		String processPointId = ServiceFactory.getDao(LotControlRuleDao.class).findProcessPointIdByMtocAndPartName(mtoc, partName);
		BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao(productType);
		ProductSpec productSpec =  (ProductSpec)productSpecDao.findByProductSpecCode(mtoc, productType);
		List<LotControlRule> lotControlRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPointAndProductSpec(processPointId, productSpec);
		InstalledPart installedPart = null;
		for(LotControlRule rule : lotControlRules){
			PartSpec partSpec = rule.getParts().get(0);
            if(rule.getPartName().getPartName().equals(partName)) {
            	installedPart = createInstalledPart(productId, rule.getPartName().getPartName(), partSpec.getId().getPartId(), partSerialNumber);
            	break;
            }
		}
		
		return installedPart;
	}
	
	private String getMissionTypeFromLotControlRule(String partName, String productType) {
		String missionType = "";
		LotControlRule currentRule = findLotControlRuleForPart(partName, productType);
		// null check
		if (currentRule == null) {
			getMainWindow().setErrorMessage("Lot Control Rules not set for Mission Type");
			
			TextFieldState.ERROR.setState(getView().getMissionRequiredTypeTextField());
			getView().getMissionRequiredTypeTextField().selectAll();
			
		}else {
			List<PartSpec> parts = currentRule.getParts();
			missionType = parts.get(0).getPartSerialNumberMask();
		}
		return missionType;
		
	}
    private LotControlRule findLotControlRuleForPart(String partName, String productType) {
    	String mtoc = getFrameSpec().getEngineMto();
		String processPointId = ServiceFactory.getDao(LotControlRuleDao.class).findProcessPointIdByMtocAndPartName(mtoc, partName);
		@SuppressWarnings("rawtypes")
		BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao(productType);
		ProductSpec productSpec =  (ProductSpec)productSpecDao.findByProductSpecCode(mtoc, productType);
		List<LotControlRule> lotControlRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPointAndProductSpec(processPointId, productSpec);

        for(LotControlRule rule : lotControlRules){
            if(partName.trim().equals(rule.getPartName().getPartName()))
                return rule;
        }
        return null;
    }
	// === get/set === //
	protected Frame getFrame() {
		return frame;
	}

	protected void setFrame(Frame frame) {
		this.frame = frame;
	}

	protected FrameSpec getFrameSpec() {
		return frameSpec;
	}

	protected void setFrameSpec(FrameSpec frameSpec) {
		this.frameSpec = frameSpec;
	}

	protected Engine getEngine() {
		return engine;
	}

	protected void setEngine(Engine engine) {
		this.engine = engine;
	}

	protected EngineSpec getEngineSpec() {
		return engineSpec;
	}

	protected void setEngineSpec(EngineSpec engineSpec) {
		this.engineSpec = engineSpec;
	}

	protected DateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}

	protected ProductResult getProductResult() {
		return productResult;
	}

	protected void setProductResult(ProductResult productResult) {
		this.productResult = productResult;
	}

	protected ProcessPoint getLastProcessPoint() {
		return lastProcessPoint;
	}

	protected void setLastProcessPoint(ProcessPoint lastProcessPoint) {
		this.lastProcessPoint = lastProcessPoint;
	}

	protected FrameDao getFrameDao() {
		return ServiceFactory.getDao(FrameDao.class);
	}

	protected EngineDao getEngineDao() {
		return ServiceFactory.getDao(EngineDao.class);
	}

	protected EngineSpecDao getEngineSpecDao() {
		return ServiceFactory.getDao(EngineSpecDao.class);
	}

	protected FrameSpecDao getFrameSpecDao() {
		return ServiceFactory.getDao(FrameSpecDao.class);
	}

	protected Component getLastFocusedComponent() {
		return lastFocusedComponent;
	}

	protected void setLastFocusedComponent(Component lastFocusedComponent) {
		this.lastFocusedComponent = lastFocusedComponent;
	}

	public String[] getNoEngineDeassignStatuses() {
		if(noEngineDeassignStatuses == null) {
			String property = getMainWindow().getApplicationProperty(NO_ENGINE_DEASSIGN_STATUSES);
			noEngineDeassignStatuses = property != null ? property.split(",") : new String[]{};
		}
		return noEngineDeassignStatuses;
	}
	private boolean validMissionType(String actualMissionType, String requiredMissionType){
		boolean validMissionType = false;
		if (getView().getPropertyBean().getMissionModelSource().equalsIgnoreCase(LCR_SOURCE)) {
			validMissionType = CommonPartUtility.verification(actualMissionType, requiredMissionType, PropertyService.getPartMaskWildcardFormat());
		} else {
			validMissionType = StringUtils.equals(requiredMissionType, actualMissionType);
		}
		return validMissionType;
	}
}

