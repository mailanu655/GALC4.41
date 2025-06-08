package com.honda.galc.qics.mobile.client;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.qics.mobile.client.utils.DoneCallback;
import com.honda.galc.qics.mobile.client.widgets.IntegerEntryPanel;
import com.honda.galc.qics.mobile.client.widgets.MessageList;
import com.honda.galc.qics.mobile.client.widgets.StringAreaEntryPanel;
import com.honda.galc.qics.mobile.client.widgets.StringEntryPanel;
import com.honda.galc.qics.mobile.client.widgets.StringSelectPanel;
import com.honda.galc.qics.mobile.client.widgets.form.AlphaKeyboard;
import com.honda.galc.qics.mobile.client.widgets.form.IntegerItem;
import com.honda.galc.qics.mobile.client.widgets.form.IntegerMaxValueVerifier;
import com.honda.galc.qics.mobile.client.widgets.form.IntegerMinValueVerifier;
import com.honda.galc.qics.mobile.client.widgets.form.MaxLengthVerifier;
import com.honda.galc.qics.mobile.client.widgets.form.RegexVerifier;
import com.honda.galc.qics.mobile.client.widgets.form.StringAreaItem;
import com.honda.galc.qics.mobile.client.widgets.form.StringItem;
import com.honda.galc.qics.mobile.shared.entity.AddNewDefectRepairResultRequest;
import com.honda.galc.qics.mobile.shared.entity.DefectRepairResult;
import com.honda.galc.qics.mobile.shared.entity.DefectRepairResultId;
import com.honda.galc.qics.mobile.shared.entity.DefectStatus;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.layout.NavStack;

/**
 * The RepairWizard allows the user to enter repair info in a wizard.  The wizard
 * gets:
 * 1. Repaired or Outstanding
 * 2. Repair Minutes
 * 3. Actual Problem
 * 4. Repair Method
 * 5. Repair Associate
 * 
 * Results are returned via a callback.  Saving is the responsibility of the caller.
 */
public class RepairWizard  {

	private NavStack navStack;
	
	String repairChoice = null;
	Integer repairTime = null;
	String actualProblem = null;
	String repairMethod = null;
	String repairAssociate = null;
	
	VinDefectsModel vinDefectsModel = null;
	DoneCallback<AddNewDefectRepairResultRequest> callback = null;

	


	public RepairWizard( NavStack navStack, 
			VinDefectsModel vinDefectsModel, 
			final DoneCallback<AddNewDefectRepairResultRequest> callback  ) {
		this.navStack = navStack;
		this.vinDefectsModel = vinDefectsModel;
		this.callback = callback;
	}
	
	public void start() {
		navStack.push( buildRepairChoicePanel());
	}

	/**
	 * Enter Outstanding or Repaird
	 * 
	 * @return Panel to make Outstanding or Repaird choice
	 */
	private Panel buildRepairChoicePanel() {
	      List<String> stringList = new ArrayList<String>();
	      stringList.add(DefectStatus.REPAIRED.getName());
	      stringList.add(DefectStatus.OUTSTANDING.getName());
	      StringSelectPanel sp = new StringSelectPanel( "Repaired?", stringList, new DoneCallback<String>() {

			@Override
			public void onDone(String selectedValue) {
				repairChoice = selectedValue;
				navStack.push( buildRepairMinutesPanel());
			}});
	      return sp;
	}
	
	
	/**
	 * Enter repair minutes 
	 * 
	 * @return Panel for entering repair minutes
	 */
	private Panel buildRepairMinutesPanel() {
		MessageList messageList = new MessageList();
		IntegerItem repairTimeItem = new IntegerItem("repair_Time", "Repair Time", "Minutes needed to repair");
		repairTimeItem.setRequired(false);
		repairTimeItem.setAutoFocus();
		repairTimeItem.addVerifiers(
				new IntegerMinValueVerifier( messageList, 0 ),
				new IntegerMaxValueVerifier( messageList, 999 )
				);
		IntegerEntryPanel repairTimePanel = new IntegerEntryPanel( "Enter repair time", 
				messageList, 
				repairTimeItem,
				new DoneCallback<Integer>(){

					@Override
					public void onDone(Integer result) {
						repairTime = result;
						navStack.push( buildActualProblemPanel());
					}});
		return repairTimePanel;
	}
	
	
	/**
	 * Enter actual problem 
	 * 
	 * @return Panel for entering repair minutes
	 */
	private Panel buildActualProblemPanel() {
		MessageList messageList = new MessageList();
		StringAreaItem stringItem = new StringAreaItem("actualProblem", "Actual Problem", "What really was wrong?");
		stringItem.setRequired(false);
		stringItem.setAutoFocus();
		stringItem.addVerifiers(
				new MaxLengthVerifier( messageList, 64 ),
				new RegexVerifier( messageList,  RegexVerifier.CONSTRAINED_ENTRY_PATTERN, "No exotic characters are allowed" )
				);
		StringAreaEntryPanel panel = new StringAreaEntryPanel( "Enter Actual Problem", 
				messageList, 
				stringItem,
				new DoneCallback<String>(){

					@Override
					public void onDone(String result) {
						actualProblem = result;
						navStack.push( buildRepairMethodPanel());
					}},
					new AlphaKeyboard());
		return panel;
	}
	
	/**
	 * Enter repair method panel
	 * 
	 * @return Panel for entering repair method
	 */
	private Panel buildRepairMethodPanel() {
		MessageList messageList = new MessageList();
		StringAreaItem stringAreaItem = new StringAreaItem("user", "Repair Method", "How was it repaired?");
		stringAreaItem.setRequired(false);
		stringAreaItem.setAutoFocus();
		stringAreaItem.addVerifiers(
				new MaxLengthVerifier( messageList, 64 ),
				new RegexVerifier( messageList,  RegexVerifier.CONSTRAINED_ENTRY_PATTERN, "is not a valid associate number" )
				);
		StringAreaEntryPanel panel = new StringAreaEntryPanel( "Enter Repair Method", 
				messageList, 
				stringAreaItem,
				new DoneCallback<String>(){

					@Override
					public void onDone(String result) {
						repairMethod= result;
						navStack.push( buildRepairAssociatePanel());
					}},
					new AlphaKeyboard());
		return panel;
	}
	
	/**
	 * Enter repair method panel
	 * 
	 * @return Panel for entering repair method
	 */
	private Panel buildRepairAssociatePanel() {
		MessageList messageList = new MessageList();
		StringItem stringItem = new StringItem("user", "Repair Associate", "Who fixed it?");
		stringItem.setRequired(false);
		stringItem.setAutoFocus();
		stringItem.addVerifiers(
				new MaxLengthVerifier( messageList, 11 ),
				new RegexVerifier( messageList,  RegexVerifier.ALPHANUMERIC_PATTERN, "is not a valid associate number" )
				);
		stringItem.setValue( Settings.getUser());
		StringEntryPanel panel = new StringEntryPanel( "Enter Repair Associate", 
				messageList, 
				stringItem,
				new DoneCallback<String>(){

					@Override
					public void onDone(String result) {
						Settings.setUser(result);
						repairAssociate = result;
						finish();
					}},
					new AlphaKeyboard());
		return panel;
	}
	
	private void finish() {
   		DefectRepairResult rr = new DefectRepairResult();
		DefectRepairResultId id = new DefectRepairResultId();
		rr.setId( id );
		
		id.setDefectResultId(vinDefectsModel.getSelectedDefectResultId());
		id.setProductId(vinDefectsModel.getVin());
		id.setRepairId(0);
		
		rr.setRepairAssociateNo(repairAssociate);
		rr.setActualProblemName(actualProblem);
    	rr.setRepairMethodName(repairMethod);
    	rr.setRepairProcessPointId(vinDefectsModel.getProcessPoint());
    	rr.setRepairDept(Settings.getRepairDepartment());
    	
    	AddNewDefectRepairResultRequest req = new AddNewDefectRepairResultRequest();
    	req.setDefectResultId(vinDefectsModel.getSelectedDefectResultId());
    	req.setProcessPointId(vinDefectsModel.getProcessPoint());
    	req.setProductId(vinDefectsModel.getVin());
    	req.setDefectStatusId(DefectStatus.getByName(repairChoice).getId());
    	req.setRepairTimePlan( repairTime);
    	if ( req.getRepairTimePlan() == null ) {
    		req.setRepairTimePlan(0);
    	}
    	
    	req.setNewDefectRepairResult(rr);
    	callback.onDone(req);
   	
	}


}
