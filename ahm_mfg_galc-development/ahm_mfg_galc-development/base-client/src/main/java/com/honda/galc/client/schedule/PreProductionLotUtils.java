package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.PreProductionLot;
/**
 * <h3>Class description</h3>
 * Utility class to manage PreProductionLots. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jan 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130122</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date March 8 , 2017
 * 
 */

public class PreProductionLotUtils {
	
	protected List<MultiValueObject<PreProductionLot>> productionLots;
	protected boolean isMoveByKdLot;
	protected boolean isLockFirstLot;
	
	
	public PreProductionLotUtils(List<MultiValueObject<PreProductionLot>> productionLots,boolean isMoveByKdLot,boolean isLockFirstLot) {
		this.productionLots = productionLots;
		this.isMoveByKdLot = isMoveByKdLot;
		this.isLockFirstLot = isLockFirstLot;
		
	}

	public int[] parseSelections(int startRow,int endRow) {
		int firstSelectedRow = 
			isMoveByKdLot ? getFirstRowWithSameKdLot(startRow) : startRow;
		int lastSelectedRow = 
			isMoveByKdLot ? getLastRowWithSameKdLot(endRow) : endRow;
		return new int[] {firstSelectedRow, lastSelectedRow};	
	}
	
	public List<PreProductionLot> moveUp(int startRow,int endRow) {
		
		List<PreProductionLot> changedProdLots = new ArrayList<PreProductionLot>();
		
		int firstSelectedRow = 
			isMoveByKdLot ? getFirstRowWithSameKdLot(startRow) : startRow;
		
		if(firstSelectedRow <= 0) return changedProdLots;
		
		int lastSelectedRow = 
			isMoveByKdLot ? getLastRowWithSameKdLot(endRow) : endRow;
		
		int firstRow = 
			isMoveByKdLot ? getFirstRowWithSameKdLot(firstSelectedRow -1 ): firstSelectedRow -1;
		int lastRow = 
			isMoveByKdLot ? getLastRowWithSameKdLot(firstSelectedRow -1) : firstSelectedRow -1;
		
		return exchange(firstRow,lastRow,firstSelectedRow, lastSelectedRow);
		
	}
	
public List<PreProductionLot> moveTop(int selectedRowNum) {
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();			    
		MultiValueObject<PreProductionLot> firstLot = getItem(0 );
		PreProductionLot firstProdLotPrevLot = getDao(PreProductionLotDao.class).findByNextProdLot(getItem(0).getKeyObject().getProductionLot());
		MultiValueObject<PreProductionLot> selectedProdLot = getItem(selectedRowNum );
		MultiValueObject<PreProductionLot> selectedProdLotPrevLot = getItem(selectedRowNum-1 );
		MultiValueObject<PreProductionLot> selectedProdLotNextLot = getItem(selectedRowNum+1);
		if(selectedProdLotNextLot!=null)
			selectedProdLotPrevLot.getKeyObject().setNextProductionLot(selectedProdLotNextLot.getKeyObject().getProductionLot());
		else
			selectedProdLotPrevLot.getKeyObject().setNextProductionLot(null);
		changedLots.add(selectedProdLotPrevLot.getKeyObject());
		if(firstProdLotPrevLot != null) {
			firstProdLotPrevLot.setNextProductionLot(selectedProdLot.getKeyObject().getProductionLot());
			getDao(PreProductionLotDao.class).save(firstProdLotPrevLot);
		}
		selectedProdLot.getKeyObject().setNextProductionLot(firstLot.getKeyObject().getProductionLot());
		changedLots.add(selectedProdLot.getKeyObject());		
		productionLots.remove(selectedProdLot);
		productionLots.add(0,selectedProdLot);
		return changedLots;
	}
	
	public List<PreProductionLot> moveDown(int startRow,int endRow) {
		
		List<PreProductionLot> changedProdLots = new ArrayList<PreProductionLot>();
		
		int firstSelectedRow = 
			isMoveByKdLot ? getFirstRowWithSameKdLot(startRow) : startRow;
		int lastSelectedRow = 
			isMoveByKdLot ? getLastRowWithSameKdLot(endRow) : endRow;

		if(lastSelectedRow + 1 >=getRowCount()) return changedProdLots;
		
		int firstRow = 
			isMoveByKdLot? getFirstRowWithSameKdLot(lastSelectedRow +1 ) : lastSelectedRow +1;
		int lastRow = 
			isMoveByKdLot?getLastRowWithSameKdLot(lastSelectedRow +1): lastSelectedRow +1;
		
		return exchange(firstSelectedRow, lastSelectedRow, firstRow, lastRow);
		
	}
	
	private List<PreProductionLot> exchange(int first1,int last1,int first2,int last2) {
		
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		
		MultiValueObject<PreProductionLot> prevLot = getItem(first1-1);

		MultiValueObject<PreProductionLot> firstLot1 = getItem(first1 );
		MultiValueObject<PreProductionLot> lastLot1 = getItem(last1 );
		MultiValueObject<PreProductionLot> firstLot2 = getItem(first2 );
		MultiValueObject<PreProductionLot> lastLot2 = getItem(last2 );
		
		MultiValueObject<PreProductionLot> lastLot = getItem(last2 + 1 );
		
		if(prevLot != null) {
			prevLot.getKeyObject().setNextProductionLot(firstLot2.getKeyObject().getProductionLot());
			changedLots.add(prevLot.getKeyObject());
		}
		
		lastLot1.getKeyObject().setNextProductionLot(lastLot == null ? null :lastLot.getKeyObject().getProductionLot());
		changedLots.add(lastLot1.getKeyObject());
		
		
		lastLot2.getKeyObject().setNextProductionLot(firstLot1.getKeyObject().getProductionLot());
		changedLots.add(lastLot2.getKeyObject());
		
		List<MultiValueObject<PreProductionLot>> lots = getSelectedProductionLots(first2, last2);
		productionLots.removeAll(lots);
		productionLots.addAll(first1,lots);
		
		return changedLots;
		
	}
	
	protected int getFirstRowWithSameKdLot(int row) {
		
		int  firstRow = row;
		MultiValueObject<PreProductionLot> selectedProdLot = getItem(row);
		
		for(int i = row -1 ; i >= 0; i--) {
			
			if(selectedProdLot.getKeyObject().isSameKdLot(getItem(i).getKeyObject())) firstRow = i;
			else break;
		}	
		return firstRow;
	}
	
	protected int getLastRowWithSameKdLot(int row) {
		
		int  lastRow = row;
		MultiValueObject<PreProductionLot> selectedProdLot = getItem(row);
		
		for(int i = row +1 ; i < getRowCount(); i++) {
			
			if(selectedProdLot.getKeyObject().isSameKdLot(getItem(i).getKeyObject())) lastRow = i;
			else break;
		}	
		return lastRow;
	}
	
	public List<MultiValueObject<PreProductionLot>> getSelectedProductionLots(int start, int end) {
		List<MultiValueObject<PreProductionLot>> preProdLots = new ArrayList<MultiValueObject<PreProductionLot>>();
		for(int i = start; i<end + 1; i++) {
			preProdLots.add(getItem(i));
		}
		return preProdLots;
	}
	
	public MultiValueObject<PreProductionLot> getItem(int index) {
		 if(index < 0 || productionLots == null || index >= productionLots.size()) return null;
	      return productionLots.get(index);
	}
	
	protected int getRowCount(){
		return productionLots.size();
	}
	
	
	public static List<PreProductionLot> sortPreProductionLot(List<PreProductionLot> items) {
		if(items.size() == 0) {
			return items;
		}
		Map<String, PreProductionLot> preProductionLotMap = new HashMap<String, PreProductionLot>();
		for(PreProductionLot lot : items) {
			preProductionLotMap.put(lot.getProductionLot(), lot);
		}
		
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();
		PreProductionLot aLot = findFirst(items);
		PreProductionLot nextLot;
		lots.add(aLot);
		while((nextLot = preProductionLotMap.get(aLot.getNextProductionLot())) != null) {
			preProductionLotMap.remove(aLot.getProductionLot());
			lots.add(nextLot);
			aLot = nextLot;
		}
		return lots;
	}
	
	public static PreProductionLot findFirst(List<PreProductionLot> items) {
		PreProductionLot lot = items.get(0);
		PreProductionLot previousLot;
		List<PreProductionLot> list = new ArrayList<PreProductionLot>();
		for(PreProductionLot anItem : items) {
			list.add(anItem);
		}
		
		while((previousLot = findPrevious(lot, list)) != null) {
			list.remove(lot);
			lot = previousLot;
		}
		return lot;
	}

	public static PreProductionLot findPrevious(PreProductionLot lot, List<PreProductionLot> items) {
		for(PreProductionLot ppLot : items) {
			if(ppLot.getNextProductionLot() != null && ppLot.getNextProductionLot().equals(lot.getProductionLot())) {
				return ppLot;
			}
		}
		return null;
	}
	
	public static PreProductionLot findPreProductionLot(String productionLotNumber, List<PreProductionLot> items) {
		for(PreProductionLot lot : items) {
			if(lot.getProductionLot().equals(productionLotNumber)) {
				return lot;
			}
		}
		return null;
	}

}
