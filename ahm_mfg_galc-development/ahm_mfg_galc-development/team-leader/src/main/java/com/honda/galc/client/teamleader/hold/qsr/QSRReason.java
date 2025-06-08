package com.honda.galc.client.teamleader.hold.qsr;

import java.io.Serializable;
import java.util.List;

import javax.swing.JComboBox;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.dto.HoldReasonMappingDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.enumtype.QCAction;

public class QSRReason implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String USER_DEFINED = "USER_DEFINED";
	public static final String APPROVED_TO_SHIP = "Approved To Ship";


	private QSRDialog component;

	private QCAction qcAction;

	public QSRReason(QSRDialog component) {
		this.component = component;
	}

	public void populateReasonsByDiv(QCAction qcAction)  {

		this.qcAction = qcAction;
		Division division = (Division) component.getDepartmentComboBoxComponent().getSelectedItem();
		if(division == null) {
			return;
		}
		component.getReasonInput().removeAllItems();
		addDefaultReasons(component.getReasonInput(), qcAction);
		String[] holdReasons = getReasonsFromProperties();
		if (holdReasons != null && holdReasons.length > 0) { 
			if(isReasonsFromProperties()) {
				List<HoldReasonMappingDto> holdReasonList;

				holdReasonList = Config.getReasonsByQcAction(division, qcAction);
				for(HoldReasonMappingDto reason : holdReasonList) {
					component.getReasonInput().addItem(reason.getHoldReason());
				}
				if(holdReasonList == null || holdReasonList.isEmpty()) {
					component.getLogger().info("No reasons exist for " + division.getDivisionId());
				}
			}else {
				for(String holdReason : holdReasons) {
					component.getReasonInput().addItem(holdReason);
				}
			}
		}else {
			component.getLogger().info("No reasons exist for " + division.getDivisionId());
		}
		component.getReasonInput().setSelectedIndex(-1);
	}

	public void populateReasonsByDivAndLine(QCAction qcAction) {
		this.qcAction = qcAction;
		if(component.getLineComboBoxComponent().getSelectedItem() == null) {
			return;
		}
		component.getReasonInput().removeAllItems();
		addDefaultReasons(component.getReasonInput(), qcAction);
		String[] reasons = getReasonsFromProperties();
		if (reasons != null && reasons.length > 0) {
			if(isReasonsFromProperties()) { 
				List<HoldReasonMappingDto> reasonList;
				reasonList = Config.getReasonsByLineAndQcAction((Division) component.getDepartmentComboBoxComponent().getSelectedItem(),
						(Line) component.getLineComboBoxComponent().getSelectedItem(),  qcAction);

				for(HoldReasonMappingDto reason : reasonList) {
					component.getReasonInput().addItem(reason.getHoldReason());
				}
				if(reasonList == null ||  reasonList.isEmpty()) {
					component.getLogger().info("No reasons exist for department " + component.getDepartmentComboBoxComponent().getSelectedItem().toString() + 
							" and line " + component.getLineComboBoxComponent().getSelectedItem().toString());
				}
			}else {
				for(String reason : reasons) {
					component.getReasonInput().addItem(reason);
				}
			}
		}else {
			component.getLogger().info("No reasons exist for department " + component.getDepartmentComboBoxComponent().getSelectedItem().toString() + 
					" and line " + component.getLineComboBoxComponent().getSelectedItem().toString());
		}
		component.getReasonInput().setSelectedIndex(-1);
	}

	public void addDefaultReasons(JComboBox element, QCAction qcAction) {
		if (qcAction.getQcActionId().equals(QCAction.QCRELEASE.getQcActionId())) {
			String[] defaultReasons = {"Confirmed In Spec", "Repaired In Spec", APPROVED_TO_SHIP};
			for (String reason : defaultReasons) {
				element.addItem(reason);
			}
		}
	}

	public boolean isReasonsFromProperties() {
		if (getQCAction().getQcActionId().equals(QCAction.QCHOLD.getQcActionId()))
			return component.getProperty().getHoldReasons()[0].equalsIgnoreCase(USER_DEFINED);
		else if (getQCAction().getQcActionId().equals(QCAction.KICKOUT.getQcActionId()))
			return component.getProperty().getKickoutReasons()[0].equalsIgnoreCase(USER_DEFINED);
		else if (getQCAction().getQcActionId().equals(QCAction.SCRAP.getQcActionId()))
			return component.getProperty().getMassScrapReasons()[0].equalsIgnoreCase(USER_DEFINED);
		else if (getQCAction().getQcActionId().equals(QCAction.QCRELEASE.getQcActionId())) 
			return component.getProperty().getReleaseReasons()[0].equalsIgnoreCase(USER_DEFINED);
		return component.getProperty().getHoldReasons()[0].equalsIgnoreCase(USER_DEFINED);

	}

	public String[] getReasonsFromProperties() {
		if (getQCAction().getQcActionId().equals(QCAction.QCHOLD.getQcActionId()))
			return component.getProperty().getHoldReasons();
		else if (getQCAction().getQcActionId().equals(QCAction.KICKOUT.getQcActionId()))
			return component.getProperty().getKickoutReasons();
		else if (getQCAction().getQcActionId().equals(QCAction.SCRAP.getQcActionId()))
			return component.getProperty().getMassScrapReasons();
		else if (getQCAction().getQcActionId().equals(QCAction.QCRELEASE.getQcActionId())) 
			return component.getProperty().getReleaseReasons();
		else return component.getProperty().getHoldReasons();
	}

	public QCAction getQCAction() {
		return qcAction;
	}
}
