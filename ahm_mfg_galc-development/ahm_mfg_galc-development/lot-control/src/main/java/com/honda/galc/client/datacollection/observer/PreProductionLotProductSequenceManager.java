package com.honda.galc.client.datacollection.observer;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.property.PropertyService;

public class PreProductionLotProductSequenceManager extends AbstractExpectedProductManager implements IExpectedProductManager
{
	
	private static final String MBPN_SUBLINE_NAME = "MBPN_SUBLINE_NAME";
	private static final String MAP_KEY_REGEX = "\\s*(?:(?:\\{\\s*(\\S+([\\s]\\S+)?)\\s*\\})|(?:\\[\\s*(\\S+)\\s*\\]))\\s*";
	private static final String PRE_PRODUCTION_LOT = "PRE_PRODUCTION_LOT";
	private static final String LOCK_GET_NEXT_LOT = "LOCK_GET_NEXT_LOT";
	
	private String sublineName;
	
	private List<Terminal> allTerminals = new  ArrayList<Terminal>();
	
	private Map<String,String> kdLotMap = new HashMap<String, String>();
	 
	public PreProductionLotProductSequenceManager(ClientContext context) {
		super(context);
		
		this.sublineName = getProperty(context.getProcessPointId(), MBPN_SUBLINE_NAME);
		
	}

	
	
	public  List<Terminal> getAllTerminals() {
		if(allTerminals.isEmpty()) {
			List<ComponentProperty> props = getDao(ComponentPropertyDao.class).findAllByPropertyKey(MBPN_SUBLINE_NAME);
			for(ComponentProperty prop : props) {
				List<Terminal> items = getDao(TerminalDao.class).findAllByProcessPointId(prop.getId().getComponentId());
				allTerminals.addAll(items);
			}
		}
		return allTerminals;
	}
	
	private String getProcessPointId(String terminalName) {
		for(Terminal terminal : getAllTerminals()) {
			if(terminal.getHostName().equalsIgnoreCase(terminalName)) return terminal.getLocatedProcessPointId();
		}
		return null;
	}

	@Override
	public List<String> getIncomingProducts(DataCollectionState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProductSequence(ProcessProduct state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isProductIdAheadOfExpectedProductId(String expectedProductId, String ProductId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String findPreviousProductId(String productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInSequenceProduct(String productId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void getNextExpectedProductIdForState(ProcessProduct state) {
		
		List<ComponentStatus> items = getDao(ComponentStatusDao.class).findAllByComponentId(sublineName);

		Map<String, LotData> preProdLotMap = getPreProductionMap(items);
		
		LotData currentLotData = findCurrentLot(preProdLotMap);
		
		if(currentLotData == null) return;

		if(currentLotData.status < 0) {
			confirmProcessing();
		}
		
		LotData lotData = findNextPreProductionLot();
		
		if(lotData != null && (lotData.lotPosition == 1 || !lotData.productionLot.equals(currentLotData.productionLot))) {
			boolean flag = MessageDialog.confirm(null, "This client is starting to process a new production lot " + lotData.productionLot + "\nDo you want to continue? ");
			if(!flag) {
				lotData.status = -1;
				saveExpectedLot(lotData);
				lotData = null;
				getNextExpectedProductIdForState(state);
			}
		}
		
		if(lotData != null) {
			PreProductionLot lot = getDao(PreProductionLotDao.class).findByKey(lotData.productionLot);
			context.setCurrentPreProductionLot(lot);
			state.setProductCount(lotData.lotPosition);
			state.setLotSize(lotData.lotSize);
			state.setExpectedProductId(lotData.productionLot);
		}
		
	}
	
	private void confirmProcessing() {
		String msg = "This client is currently Inactive\r\n" + 
				"Do you want to start processing the next lot?\r\n" +
				"If you click Yes, you will need to complete the lot using this client";
		JTextArea textArea = new JTextArea(msg);
		textArea.setFont(new Font("Arial", Font.BOLD, 18));
		textArea.setOpaque(false);
		textArea.setEditable(false);

		Object buttons[] = {"Yes"};

		JOptionPane pane = new JOptionPane(textArea,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION,
				null,                     			// Icon (none)
				buttons,                    		// Option buttons
				buttons[0]);                      // Button that'll have the focus);
		JDialog dialog = pane.createDialog(null, "Confirmation");

		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.setVisible(true);
	}
	
	protected LotData findNextPreProductionLot() {
		
		
		waitForLockNextLotReleased();
		
		List<ComponentStatus> items = getDao(ComponentStatusDao.class).findAllByComponentId(sublineName);
		
		LotData lotData = null;
		
		getDao(ComponentStatusDao.class).save(new ComponentStatus(sublineName, LOCK_GET_NEXT_LOT, "YES"));
		
		try {
		
			Map<String, LotData> preProdLotMap = getPreProductionMap(items);
			
			lotData = findCurrentLot(preProdLotMap);
			
			if(lotData != null && lotData.status == 0) return lotData;  // in progress
			
			if(lotData == null || lotData.status < 0) {
				lotData = findLotDataInGroup(preProdLotMap);
				if(lotData != null) {
					lotData = new LotData(lotData);
					lotData.status = -1;
				}else {
					lotData = findLotDataInOtherGroups(preProdLotMap);
					if(lotData != null ) {
						lotData = findNextLot(preProdLotMap,lotData.productionLot);
					}
					
					return lotData;
				}
			}
			
			if(lotData == null || lotData.status == 0) return lotData;  // in progress

			PreProductionLot lot = getDao(PreProductionLotDao.class).findByKey(lotData.productionLot);
			
			if(lot.getStampedCount() < lot.getLotSize()) {
				
				lotData.lotPosition = lot.getStampedCount();

				int count = findLotCount(preProdLotMap, lotData.productionLot);
				
				if(lotData.status < 0) count++;

				
				if(lotData.lotPosition + count <= lotData.lotSize) {
				
					lotData.lotPosition = lotData.lotPosition + count;
					
					lotData.status = 0;
					
					return saveExpectedLot(lotData);
				}
			}
		
			lotData = findNextLot(preProdLotMap,lotData.productionLot);

		}finally {
			getDao(ComponentStatusDao.class).save(new ComponentStatus(sublineName, LOCK_GET_NEXT_LOT, "NO"));
		}
		
		return lotData;
	}
	
	private LotData findLotDataInGroup(Map<String, LotData> preProdLotMap) {
		
		for (Entry<String,LotData> entry :preProdLotMap.entrySet()) {
			if (isCurrentProcessPointId(entry.getKey()) && entry.getValue().status >= 0) return entry.getValue();
		}
		
		return null;
		
	}
	
	private LotData findLotDataInOtherGroups(Map<String, LotData> preProdLotMap) {
		
		for (Entry<String,LotData> entry :preProdLotMap.entrySet()) {
			if (!isCurrentProcessPointId(entry.getKey()) && entry.getValue().status >= 0) return entry.getValue();
		}
		
		return null;
		
	}
	private void waitForLockNextLotReleased() {
		boolean flag = isNextLotLocked();
		if(flag) {
			Logger.getLogger().info("waiting for NEXT_LOT lock to be released");
		}
		while(isNextLotLocked()) {
			delay();
		}
		if(flag) {
			Logger.getLogger().info("NEXT_LOT lock is released");
		}
	}
	
	private boolean isNextLotLocked() {
		ComponentStatus componentStatus = getDao(ComponentStatusDao.class).findByKey(sublineName, LOCK_GET_NEXT_LOT);
		
		if(componentStatus == null ) return false;
		
		String statusValue = componentStatus.getStatusValue();
		
		boolean flag = statusValue.equalsIgnoreCase("Y") || statusValue.equalsIgnoreCase("TRUE") 
	       || statusValue.equalsIgnoreCase("YES") || statusValue.equalsIgnoreCase("1");
		
		return flag;
	}
	
	protected LotData findNextLot(Map<String, LotData> preProdLotMap,String productionLot) {
		
		PreProductionLot preProdLot = getDao(PreProductionLotDao.class).findNext(productionLot);
		if(preProdLot == null) return null;
		int flag = isProductionLotUsedByGroup(preProdLotMap,preProdLot);
		if (flag == 1) {
			// same process point id
			int count = findLotCount(preProdLotMap, preProdLot.getProductionLot());

			if(preProdLot.getStampedCount() + count < preProdLot.getLotSize()) {
				return saveExpectedLot(new LotData(preProdLot,preProdLot.getStampedCount() + count + 1));
			} 
		} else if (flag == 0){
			// not in process
			if(preProdLot.getStampedCount() >= preProdLot.getLotSize()) {
				return findNextLot(preProdLotMap,preProdLot.getProductionLot());
			} else {
				return saveExpectedLot(new LotData(preProdLot,1));
			}
		}
		// other process point id or current process point id is filled
		return findNextLot(preProdLotMap,preProdLot.getProductionLot());	
 
	}
	
	protected LotData saveExpectedLot(LotData lotData) {
		String statusKey = PRE_PRODUCTION_LOT + "{" + context.getAppContext().getHostName() + "}";
		ComponentStatus componentStatus = new ComponentStatus(sublineName, statusKey, lotData.toString());
		getDao(ComponentStatusDao.class).save(componentStatus);
		Logger.getLogger().info("Saved component status " + componentStatus);
		return lotData;
	}
	
	protected PreProductionLot getCurrentPreProductionLot() {
		String lineId = context.getAppContext().getProcessPoint().getLineId();
		InProcessProduct inProduct = getDao(InProcessProductDao.class).findLastForLine(lineId);
		if(inProduct != null) {
			return getDao(PreProductionLotDao.class).findByKey(inProduct.getProductionLot());
		} return null;
	}
	
	private int findLotCount(Map<String, LotData> preProdLotMap,String prodLot) {
		int count = 0;
		for (Entry<String,LotData> entry :preProdLotMap.entrySet()) {
			if (isCurrentProcessPointId(entry.getKey()) && entry.getValue().status >= 0 && entry.getValue().productionLot.equalsIgnoreCase(prodLot)) 
				count++;
		}
		return count;
	}
	
	private boolean isCurrentProcessPointId(String hostName) {
		String currentProcessPointId = getProcessPointId(context.getAppContext().getHostName());
		String processPointId = getProcessPointId(hostName);
		return currentProcessPointId.equalsIgnoreCase(processPointId);
	}
	
	/**
	 * return 0 - no match
	 *        1 = current process point id
	 *        2 = other process point id
	 * @param preProdLotMap
	 * @param prodLot
	 * @return
	 */
	
	private int isProductionLotUsedByGroup(Map<String, LotData> preProdLotMap,PreProductionLot prodLot) {
		for (Entry<String,LotData> entry :preProdLotMap.entrySet()) {
			LotData lotData = entry.getValue();
			if(lotData.status < 0) continue;   // inactive client
			if(isSameKdLot(lotData.productionLot, prodLot)) {
				if(isCurrentProcessPointId(entry.getKey())) return 1;
				else return 2;
			}
		}
		return 0;
	}
	
	private boolean isSameKdLot(String productionLot, PreProductionLot prodLot) {
		if(productionLot.equalsIgnoreCase(prodLot.getProductionLot())) return true;
		
		return prodLot.getKdLotNumber().equalsIgnoreCase(findKdLot(productionLot));
	}
	
	private String findKdLot(String productionLot) {
		if(!kdLotMap.containsKey(productionLot))  {
			PreProductionLot preProdLot = getDao(PreProductionLotDao.class).findByKey(productionLot);			
			if(preProdLot != null) {
				kdLotMap.put(productionLot, preProdLot.getKdLotNumber());
			}else {
				return "";
			}
		}
			
		return kdLotMap.get(productionLot);
	}
	
	
	private LotData findCurrentLot(Map<String, LotData> preProdLotMap) {
		for (Entry<String,LotData> entry :preProdLotMap.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(context.getAppContext().getHostName())) return entry.getValue();
		}
		return null;
	}
	
	public Map<String, LotData> getPreProductionMap(List<ComponentStatus> items) {
		
		Map<String, LotData> preProdLotMap = new HashMap<String, LotData>();
		
		Map<String,String> statusMap = getComponentStatusMap(items, PRE_PRODUCTION_LOT);
		
		for(Entry<String, String> item: statusMap.entrySet()) {
			preProdLotMap.put(item.getKey(), new LotData(item.getValue()));
		}
		
		return preProdLotMap;
	}
	
	public Map<String,String> getComponentStatusMap(List<ComponentStatus> items,String propertyName) {
		Map<String,String> statusMap = new LinkedHashMap<String, String>();
		
		Pattern p = Pattern.compile(propertyName + MAP_KEY_REGEX);
				
		for(ComponentStatus componentStatus : items) {
			String mapKey = null;

			Matcher m = p.matcher(componentStatus.getId().getStatusKey());
			if(m.matches()) mapKey = m.group(1);
			if(mapKey != null) statusMap.put(mapKey, componentStatus.getStatusValue());
		}
		return statusMap;
	}
	
	protected void findNextProductId() {
		String lineId = context.getAppContext().getProcessPoint().getLineId();
		InProcessProduct inProduct = getDao(InProcessProductDao.class).findLastForLine(lineId);
		if(inProduct != null) {
			PreProductionLot preProdLot = getDao(PreProductionLotDao.class).findByKey(inProduct.getProductionLot());
			if(preProdLot.getStampedCount() >= preProdLot.getLotSize()) {
				preProdLot = getDao(PreProductionLotDao.class).findByKey(preProdLot.getNextProductionLot());
			}
		}
		
	}
	
	protected void findNextAvailableLot(PreProductionLot preProdLot) {
		if(preProdLot.getStampedCount() >= preProdLot.getLotSize()) {
			preProdLot = getDao(PreProductionLotDao.class).findByKey(preProdLot.getNextProductionLot());
		}
	}
	
	protected String getProperty(String componentId, String propertyName) {
		return PropertyService.getProperty(componentId, propertyName);
	}
	
	
	public void saveNextExpectedProduct(ProcessProduct state) {
		List<ComponentStatus> items = getDao(ComponentStatusDao.class).findAllByComponentId(sublineName);
		Map<String, LotData> preProdLotMap = getPreProductionMap(items);
		LotData lotData = findCurrentLot(preProdLotMap);
		if(lotData != null) {
			lotData.status = 2;
			saveExpectedLot(lotData);
		}
	}

	private void delay() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class LotData {
		public String productionLot = "";
		public int lotSize;
		public int lotPosition;
		public int status; // -1 = inactive, 0 = IN_PROGRESS, 1 = DONE
		
		public LotData(LotData lotData) {
		  this.productionLot = lotData.productionLot;
		  this.lotSize = lotData.lotSize;
		  this.lotPosition = lotData.lotPosition;
		  this.status = lotData.status;
		}
		
		public LotData(String lotStr) {
			String [] items = lotStr.split(Delimiter.COMMA);
			this.productionLot = items[0];
			this.lotSize = Integer.parseInt(items[1]);
			this.lotPosition = Integer.parseInt(items[2]);
			this.status = Integer.parseInt(items[3]);
		}
		
		public LotData(PreProductionLot prodLot, int count) {
			this.productionLot = prodLot.getProductionLot();
			this.lotSize = prodLot.getLotSize();
			this.lotPosition = count;
			this.status = 0;
		}
		
		public String toString() {
			return productionLot + "," + lotSize + "," + lotPosition + "," + status;
		}
	}

}
