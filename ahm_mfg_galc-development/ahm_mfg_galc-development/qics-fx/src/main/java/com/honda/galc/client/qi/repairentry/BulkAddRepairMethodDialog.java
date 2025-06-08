package com.honda.galc.client.qi.repairentry;

import java.util.Date;
import java.util.List;

import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.dto.qi.QiRepairResultDto;

public class BulkAddRepairMethodDialog extends AddRepairMethodDialog {
	
	public BulkAddRepairMethodDialog(String title, RepairEntryModel model, QiRepairResultDto qiRepairResultDto,
			String applicationId, boolean isFixedDefect,boolean noProblemFound, List<QiRepairResultDto> allSelectedDefects, Date sessionTimestamp, 
			List<Long> repairIds, AbstractRepairEntryController repairEntryController) {
		super(title, model, qiRepairResultDto, applicationId, isFixedDefect, noProblemFound, allSelectedDefects, sessionTimestamp, repairIds, repairEntryController);
	}
	
	@Override
	protected void initController(RepairEntryModel model, Date sessionTimestamp,
			AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?, ?>> repairEntryController) {
		setAddRepairMethodController(new BulkAddRepairMethodDialogController(model, this, getQiRepairResultDto(), getAllSelectedDefects(), sessionTimestamp, repairEntryController));		
	}
	
	@Override
	public void initHistoryRepairMethodTable() {
		getHistoryRepairMethodDataPane().getTable().getItems().clear();
		if(getRepairIds() != null && !getRepairIds().isEmpty())  {
			getHistoryRepairMethodDataPane().setData(getModel().getAppliedRepairMethodHistoryData(getRepairIds(), getQiRepairResultDto()));
		}
	}
}
