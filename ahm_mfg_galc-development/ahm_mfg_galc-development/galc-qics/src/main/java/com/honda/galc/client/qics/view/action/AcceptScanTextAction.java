package com.honda.galc.client.qics.view.action;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import com.honda.galc.client.qics.view.screen.DefectScanTextPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.InspectionPartDescription;





public class AcceptScanTextAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;
	
	private String inputText = "";
	String defectGroup = "";
	String partGroup = "";
	


	public AcceptScanTextAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Accept");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
	}
	
	@Override
	public void execute(ActionEvent e) {

		try {
			getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			getQicsFrame().clearMessage();
			
			inputText = getQicsPanel().getDefectScanInputPane().getEntryTextField().getText();

			if (inputText.equalsIgnoreCase(DefectStatus.OUTSTANDING.name())|| (inputText.equalsIgnoreCase(DefectStatus.REPAIRED.name()))){
				setDefectStatus(inputText);
				return;
			}

			List<InspectionModel> inspectionModelList = getQicsController().getProductModel().getInspectionModels();
			if (inspectionModelList.size() != 1){
				getQicsFrame().getLogger().info("Process Points implementing Scan Defect station must have only one Defect and Part Group");
			} else {
				defectGroup = inspectionModelList.get(0).getDefectGroupName();
				getQicsFrame().getLogger().info("Defect Group Name:"+defectGroup);
				partGroup = inspectionModelList.get(0).getPartGroupName();
				getQicsFrame().getLogger().info("Part Group Name:"+partGroup);
			}
			
			getQicsController().selectDefectTypes(defectGroup);
			getQicsController().selectPartLocations(partGroup);
			
			if (isValidDefect(inputText)){
				getQicsFrame().getLogger().info("Defect Type:"+inputText+" is valid");
				setDefect(inputText);
				return;
			} else if (!getQicsPanel().getDefectScanInputPane().getDefectTextField().getText().equalsIgnoreCase("") && isValidLocation(inputText)) {
				DefectResult defect = getDefectResult();
				processDefectResult(defect);
				getQicsPanel().getDefectScanInputPane().getEntryTextField().setFocusable(true);
				resetForNextInput();
				
			} else {
				setErrorMessage("Invalid scan value");
				getQicsFrame().getLogger().info("Invalid scan value");
			}
		} finally {
			getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	protected boolean processDefectType(){
		return true;
	}
	
	protected boolean processDefectLocation(){
		return true;
	}

	@Override
	protected DefectScanTextPanel getQicsPanel() {
		return (DefectScanTextPanel) super.getQicsPanel();
	}

	public boolean isValidDefect(String DefectTypeValue) {
		List<DefectTypeDescription> defectTypes = getQicsController().getProductModel().getDistinctDefectTypes();
		for (int i=0; i < defectTypes.size();i++){
			DefectTypeDescription defectTypeDesc =  defectTypes.get(i);
			if (defectTypeDesc.getId().getDefectTypeName().equals(DefectTypeValue)){
				return true;
			}
		}
		return false;
	}

	public boolean isValidLocation(String locationValue) {
		Iterator<InspectionPartDescription> iter = getQicsController().getProductModel().getParts().iterator();
		while(iter.hasNext()) {
			InspectionPartDescription partDesc=(InspectionPartDescription)iter.next();
			String location = partDesc.getInspectionPartLocationName();
			if (location.equals(locationValue))
			{
				getQicsFrame().getLogger().info("Valid scan location:"+locationValue);
				return true;
			}
		}
		getQicsFrame().getLogger().info("InValid scan location:"+locationValue);
		return false;
	}

	private void setDefectStatus(String inputText) {
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setText(inputText);
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setBackground(Color.green);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().grabFocus();
	}


	private void setDefect(String inputText) {
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setText(inputText);
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setBackground(Color.green);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().grabFocus();
	}

	
	protected DefectResult getDefectResult() {
		DefectResult defectResult = new DefectResult();		
		DefectResultId id = new DefectResultId();		
		id.setDefectTypeName(getQicsPanel().getDefectScanInputPane().getDefectTextField().getText().trim());
		id.setInspectionPartName("");
		id.setInspectionPartLocationName(getQicsPanel().getDefectScanInputPane().getEntryTextField().getText().trim());
		id.setProductId(getQicsController().getProductModel().getProduct().getProductId());
		id.setApplicationId(getQicsController().getProcessPointId());
		id.setTwoPartPairPart("");
		id.setTwoPartPairLocation("");		
		defectResult.setId(id);
		defectResult.setRepairAssociateNo(getQicsFrame().getUserId());
		defectResult.setEntryStation(getQicsPanel().getClientModel().getProcessPoint().getProcessPointName());
		defectResult.setEntryDept(getQicsController().getClientModel().getProcessPoint().getDivisionId());
		defectResult.setNewDefect(true);
		defectResult.setNewDefect(true);
		defectResult.setWriteUpDepartment("");
		defectResult.setRepairAssociateNo(null);
		defectResult.setDefectStatus(DefectStatus.valueOf(getQicsPanel().getDefectScanInputPane().getStatusTextField().getText()));
        if (defectResult.getDefectStatus() == DefectStatus.OUTSTANDING) {
			 defectResult.setOutstandingFlag((short)1);
		} else {
			 defectResult.setOutstandingFlag((short)0);
		}
		if (getQicsController().getClientModel().getCurrentSchedule() != null) {
			defectResult.setShift(getQicsController().getClientModel().getCurrentShiftCode());
			defectResult.setDate(getQicsController().getClientModel().getCurrentSchedule().getId().getProductionDate());
		}
		id.setSecondaryPartName("");		
		defectResult.setIqsCategoryName("");
		defectResult.setIqsItemName("");
		defectResult.setRegressionCode("");		
		return defectResult;
	}

	
	protected void processDefectResult(DefectResult defect) {	
           getQicsController().getProductModel().addNewDefect(defect);
	}

	protected void setErrorMessage(String errorMessageId) {
		getQicsFrame().setErrorMessage(errorMessageId);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setColor(Color.red);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setBackground(Color.red);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().selectAll();
	}

	private void resetForNextInput() {
		getQicsPanel().resetDefectTable();
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setText("");
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setText("");
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setBackground(Color.blue);
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setText("OUTSTANDING");
		getQicsPanel().getDefectScanInputPane().getEntryTextField().grabFocus();
	}
	
	
}
