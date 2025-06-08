package com.honda.galc.client.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>PreProduction Lot Seq Utils</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PreProductionLotSeqUtils description </p>
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
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 *
 */
public class PreProductionLotSeqUtils extends PreProductionLotUtils{
	protected ScheduleMainPanel scheduleMainPanel;
	
	public PreProductionLotSeqUtils(
			List<MultiValueObject<PreProductionLot>> productionLots, boolean isMoveByKdLot, boolean isLockFirstLot) {
		super(productionLots, isMoveByKdLot, isLockFirstLot);
	}
	
	public PreProductionLotSeqUtils(List<MultiValueObject<PreProductionLot>> items,
			boolean moveByKdLot, boolean lockFirstLot,
			ScheduleMainPanel scheduleMainPanel) {
		this(items, moveByKdLot, lockFirstLot);
		this.scheduleMainPanel = scheduleMainPanel;
		
	}

	public List<PreProductionLot> getSelectedRows(int startRow, int endRow) {
		List<PreProductionLot> selectedLots = new ArrayList<PreProductionLot>();

		int firstSelectedRow = isMoveByKdLot ? getFirstRowWithSameKdLot(startRow) : startRow;
		if(firstSelectedRow < 0) return selectedLots;

		int lastSelectedRow = isMoveByKdLot ? getLastRowWithSameKdLot(endRow) : endRow;
		
		for(int i =firstSelectedRow; i <= lastSelectedRow; i++ )
			selectedLots.add(productionLots.get(i).getKeyObject());
		
		return selectedLots;
	}
	
	public List<PreProductionLot> getPreviousRows(int startRow) {
		int firstSelectedRow = isMoveByKdLot ? getFirstRowWithSameKdLot(startRow) : startRow;
		List<PreProductionLot> previousLots = new ArrayList<PreProductionLot>();
		int firstRow = isMoveByKdLot ? getFirstRowWithSameKdLot(firstSelectedRow -1 ): firstSelectedRow -1;
	    int lastRow = isMoveByKdLot ? getLastRowWithSameKdLot(firstSelectedRow -1) : firstSelectedRow -1;

	    for(int i = firstRow; i <= lastRow; i++)
	    	previousLots.add(productionLots.get(i).getKeyObject());
	    
	    return previousLots;
					
	}

	public List<PreProductionLot> getNextLotRows(int endRow) {
		List<PreProductionLot> nextLots = new ArrayList<PreProductionLot>();
		int lastSelectedRow = isMoveByKdLot ? getLastRowWithSameKdLot(endRow) : endRow;
		if(lastSelectedRow + 1 >=getRowCount()) return nextLots;

		int firstRow = isMoveByKdLot? getFirstRowWithSameKdLot(lastSelectedRow +1 ) : lastSelectedRow +1;
		int lastRow = isMoveByKdLot?getLastRowWithSameKdLot(lastSelectedRow +1): lastSelectedRow +1;
		
		 for(int i = firstRow; i <= lastRow; i++)
			 nextLots.add(productionLots.get(i).getKeyObject());
		 
		return nextLots;
	}
	
	public List<PreProductionLot> completeLots() {
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();

		for( MultiValueObject<PreProductionLot> lotObj: productionLots){
			PreProductionLot lot = lotObj.getKeyObject();
			lot.setSendStatus(PreProductionLotSendStatus.DONE);
			lots.add(lot);
		}
		return lots;
	}
	
	public List<PreProductionLot> setCurrentLot(int startRow, int endRow) {
		return  getUnProcessedRowsBeforeSelectedRow(startRow);
	}
	
	private List<PreProductionLot> getUnProcessedRowsBeforeSelectedRow(int selectedRow) {
		List<PreProductionLot> updateLots = new ArrayList<PreProductionLot>();
		
		if(scheduleMainPanel.getCurrentLotPanel() != null)
			updateLots.addAll(scheduleMainPanel.getCurrentLotPanel().getCurrentLots());
		
		if(selectedRow > 0){
			int firstSelectedRow = isMoveByKdLot ? getFirstRowWithSameKdLot(selectedRow) : selectedRow;
			for(MultiValueObject<PreProductionLot> lot : scheduleMainPanel.getUpcomingLotTblPane().getTable().getItems().subList(0, (firstSelectedRow)))
				updateLots.add(lot.getKeyObject());
		}
		
		return updateLots;
	}

	public List<MultiValueObject<PreProductionLot>> getDataList(List<MultiValueObject<PreProductionLot>> items, List<PreProductionLot> selectedRowsList) {
		List<MultiValueObject<PreProductionLot>> list = new ArrayList<MultiValueObject<PreProductionLot>>();
		
		for(PreProductionLot lot : selectedRowsList)
			for(MultiValueObject<PreProductionLot> item: items)
				if(lot.getProductionLot().equals(item.getKeyObject().getProductionLot()))
					list.add(item);
		
		return list;
		
	}

	
	public List<PreProductionLot> switchLotsSeq(List<PreProductionLot> lots1, List<PreProductionLot> lots2, String[] methods, boolean moveUp) {
		List<PreProductionLot> list = new ArrayList<PreProductionLot>();//list need to be updated
		Double firstSeq1 = lots1.get(0).getSequence();
		Double lastSeq1 = lots1.get(lots1.size() -1).getSequence();
		
		Double firstSeq2 = lots2.get(0).getSequence();
		Double lastSeq2 = lots2.get(lots2.size() -1).getSequence();
		
		assignSeq(lots1, firstSeq2, lastSeq2, moveUp);
		assignSeq(lots2, firstSeq1, lastSeq1, !moveUp);
		
		list.addAll(lots1);
		list.addAll(lots2);
		
		updateSequence(list, methods);
		sortBySequence(productionLots);
		
		return list;
		
	}
	
	

	private void updateSequence(List<PreProductionLot> list, String[] methods) {
		int seqPosition = getSequencePosition(methods);
		for(MultiValueObject<PreProductionLot> mvoLot : productionLots)
			for(PreProductionLot lot : list)
				if(lot.getProductionLot().equals(mvoLot.getKeyObject().getProductionLot()))
					mvoLot.setValue(seqPosition, ReflectionUtils.invoke(lot, "getSequence", new Object[0]));

	}
	
	private int getSequencePosition(String[] methods) {
		for(int i = 0; i < methods.length; i++){
			if(methods[i].equals("getSequence"))
				return i;
		}
		 return -1;
		
	}

	private void assignSeq(List<PreProductionLot> lots, double first, double last, boolean moveUp) {
		
		if(first == last){
			if(lots.size() == 1) {
				lots.get(0).setSequence(first);
				return;
			}else{
				//get second last lot
				double seqToUse = moveUp? lots.get(lots.size() -2).getSequence() : lots.get(1).getSequence();
				assignLotsSeq(lots, first, seqToUse);
				return;
			}	
		} else if(lots.size() == 1) {
			if(moveUp) lots.get(0).setSequence(first);
			else lots.get(0).setSequence(last);
			return;
		}
		
		assignLotsSeq(lots, first, last);
		
	}

	private void assignLotsSeq(List<PreProductionLot> lots, Double seq1, Double seq2) {
		double first = seq1 <= seq2 ? seq1 : seq2;
		double last = first == seq1 ? seq2 : seq1;
		
		double interval = (last - first)/(lots.size() -1);
		
		for(int i = 0; i < lots.size(); i++)
			lots.get(i).setSequence(first + interval*i);
	}
	
	public void sortBySequence(List<MultiValueObject<PreProductionLot>> items){
		Collections.sort(items, new MvoPreProductionLotComparator());
		
	}
	
	public List<PreProductionLot> moveDown(int startRow,int endRow, String[] methods){
		List<PreProductionLot> selectedRowsList = getSelectedRows(startRow, endRow);
		List<PreProductionLot> nextLotRows = getNextLotRows(endRow);
		return switchLotsSeq(selectedRowsList, nextLotRows, methods, false);
	}
	
	public List<PreProductionLot> moveUp(int startRow,int endRow, String[] methods){
		List<PreProductionLot> selectedRowsList = getSelectedRows(startRow, endRow);
		List<PreProductionLot> previousRowsList = getPreviousRows(startRow);
		return switchLotsSeq(selectedRowsList, previousRowsList, methods, true);
	}


}
class MvoPreProductionLotComparator implements Comparator<MultiValueObject<PreProductionLot>>{
	public int compare(MultiValueObject<PreProductionLot> o1, MultiValueObject<PreProductionLot> o2) {
		return Double.compare(o1.getKeyObject().getSequence(), o2.getKeyObject().getSequence());
	}
	
}

