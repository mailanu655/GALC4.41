package com.honda.galc.client.glassload;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.SortedArrayList;

public class GlassLoadModel {
	
	private static String GLASS_LOAD = "GLASS_LOAD";
	
	private static String FRONT_GLASS_TYPE = "FRONT_GLASS_TYPE";
	private static String REAR_GLASS_TYPE = "REAR_GLASS_TYPE";
	
	private static String SQL = "SELECT * FROM GTS_CARRIER_TBX WHERE TRACKING_AREA = ?1 order by STATUS ASC";
		
	private GlassLoadView glassLoadView;
	
	private BuildAttributeCache buildAttributeCache;
	
	private List<LotControlRule> rules;
	
	private List<PreProductionLot> preProductionLotList = new ArrayList<PreProductionLot>();
	
	private ExpectedProduct expectedProduct= null;
	
	private Map<String, List<Frame>> frameLotMap = new HashMap<String, List<Frame>>();
	
	private Map<String, List<FrameSpec>> frameSpecMap = new HashMap<String, List<FrameSpec>>();

	
	public GlassLoadModel(GlassLoadView glassLoadView) {
		
		this.glassLoadView = glassLoadView;
		
		buildAttributeCache = new BuildAttributeCache();
		buildAttributeCache.loadAttribute(FRONT_GLASS_TYPE);
		buildAttributeCache.loadAttribute(REAR_GLASS_TYPE);

	}
	
	private List<FrameSpec> fetchFrameSpec(String modelYear) {
		
		if(!frameSpecMap.containsKey(modelYear)) {
			List<FrameSpec> frameSpecs = getDao(FrameSpecDao.class).findAllByModelYearCode(modelYear);
			frameSpecMap.put(modelYear, frameSpecs);
		}
		
		return frameSpecMap.get(modelYear);
	}
	
	private FrameSpec getProductSpec(String productSpecCode) {
		String modelYear = ProductSpec.extractModelYearCode(productSpecCode);
		List<FrameSpec> frameSpecs = fetchFrameSpec(modelYear);
		
		for(FrameSpec spec : frameSpecs) {
			if(spec.getProductSpecCode().equalsIgnoreCase(productSpecCode)) return spec;
		}
		return null;
	}
	
	public List<LotControlRule>	getLotControlRules(String productSpecCode) {
		FrameSpec spec = getProductSpec(productSpecCode);
		return LotControlPartUtil.getLotControlRuleByProductSpec(spec, getLotControlRules());
	}
	
	
	public List<GtsCarrier> fetchGlassCarriers() {
		
		List<GtsCarrier> carriers = getService(GenericDaoService.class).findAll(SQL, Parameters.with("1", GLASS_LOAD), GtsCarrier.class);
		
		return carriers;
	}
	
	public List<LotControlRule> getLotControlRules() {
		if(this.rules == null) {
			this.rules = getDao(LotControlRuleDao.class).findAllByProcessPoint(getProcessPointId());
		};
		return rules;
	}
	
	public GtsCarrier findNextGtsCarrier(int seq) {
		List<GtsCarrier> carriers = fetchGlassCarriers();
		int size = carriers.size();
		for(int i = 0; i< size; i++) {
			if(seq == carriers.get(i).getStatusValue()) {
				int nextIndex = (i < size -1) ? i+1 : 0;
				return carriers.get(nextIndex);
			}
		}
		
		return null;
	}
	
	public void addCarrier(int seq, String label) {
		GtsCarrier carrier = new GtsCarrier(GLASS_LOAD,label);
		carrier.setStatusValue(seq);
		
		getDao(GtsCarrierDao.class).save(carrier);
		
	}
	
	public void updateCarrier(int seq, String label, String vin) {
		GtsCarrier carrier = new GtsCarrier(GLASS_LOAD,label);
		carrier.setProductId(vin);
		carrier.setStatusValue(seq);
		
		getDao(GtsCarrierDao.class).save(carrier);
		
	}
	
	public void deleteCarrier(GtsCarrier carrier) {
		getDao(GtsCarrierDao.class).remove(carrier);
	}
	
	public void updateCarrier(GtsCarrier carrier) {
		getDao(GtsCarrierDao.class).save(carrier);
	}
	
	public void updateCarriers(List<GtsCarrier> carriers) {
		getDao(GtsCarrierDao.class).saveAll(carriers);
	}
	
	public boolean isStraggler(String productId) {
		List<Straggler> currentProductstragglerList = ServiceFactory.getDao(StragglerDao.class).findStragglerProductList(productId, getAFOnProcessPointId());	
		if(currentProductstragglerList != null && currentProductstragglerList.size() > 0){
			if(PropertyService.getPropertyBoolean(getAFOnProcessPointId(),"STRAGGLER_GROUPING",false)) {
				for (Straggler straggler : currentProductstragglerList) {
					if(StringUtils.equals(StringUtils.trimToEmpty(straggler.getId().getStragglerType()), "MTOCI")) {
						return true;
					}
				}
				return false;
			}
			
			return true;
		}
		
		return false;	
	}
	
	public List<Frame> fetchAFonVinList() {
		List<Frame> frames = getDao(FrameDao.class).findByTrackingStatus(getPropertyBean().getAfOnTrackingStatus());
		
		List<Frame> filteredFrames = new ArrayList<Frame>();
		
		for(Frame frame : frames) {
			if(frame.getAfOnSequenceNumber() != null) filteredFrames.add(frame);
			else {
			 System.out.println(" frame " + frame);
			}
		}
		
		List<Frame> items = null ;
		try {
			items =  new SortedArrayList<Frame>(filteredFrames, "getAfOnSequenceNumber");
			
		}catch(Throwable ex) {
			ex.printStackTrace();
		}
		
		return items;
	}
	
	public ExpectedProduct getExpectedProduct() {
		if(expectedProduct == null) {
			expectedProduct = getDao(ExpectedProductDao.class).findByKey(getProcessPointId());
		}
	    return expectedProduct;
	}
	
	public void updateExpectedProduct(String vin) {
		ExpectedProduct expectedProduct = new ExpectedProduct(vin, getProcessPointId());
		this.expectedProduct = getDao(ExpectedProductDao.class).save(expectedProduct);
	}
	
	public List<MultiValueObject<Frame>> loadVinList() {
		
		this.frameLotMap = new HashMap<String, List<Frame>>();
		
	 	List<GtsCarrier> carriers = fetchGlassCarriers();
    	List<Frame> afonFrames = fetchAFonVinList();
   
		List<MultiValueObject<Frame>> list = new ArrayList<MultiValueObject<Frame>>();
		
		int index = findIndex(afonFrames, getExpectedProduct().getProductId());
		
		 int startIndex = index - getPropertyBean().getProcessedProductNumber();
		 int size = afonFrames.size();
		
		for(int i = startIndex; i < size ; i++) {

			if( i < 0 ) continue;
			Frame frame = afonFrames.get(i);
			boolean processed = i <= startIndex + getPropertyBean().getProcessedProductNumber();
			GtsCarrier carrier = getCarrier(carriers, afonFrames.get(i).getProductId());
			List<Object> values = prepareValues(frame, carrier,processed);

			if(carrier != null) frame.setQuantity(""+carrier.getStatusValue());
			MultiValueObject<Frame> valueObject = new MultiValueObject<Frame>(frame, values);
			list.add(valueObject);

		}
		
		return list;

	}
	
	public MultiValueObject<Frame> createFrameItem(String productId) {
		Frame frame = getDao(FrameDao.class).findByKey(productId);
		if(frame == null) return null;
		
		List<Object> values = prepareValues(frame, null,false);
		MultiValueObject<Frame> valueObject = new MultiValueObject<Frame>(frame, values);
		
		return valueObject;
		
	}
	
	public String getAFOnProcessPointId() {
		FrameLinePropertyBean frameLinePropertyBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class,getProcessPointId());
		return frameLinePropertyBean.getAfOnProcessPointId();
	}
	
	private int findIndex(List<Frame> afonFrames, String vin) {
		int size = afonFrames.size();
		for (int i = 0; i< size ; i++) {
			if(afonFrames.get(i).getProductId().equals(vin)) return i;
		}
		
		return -1;
	}
	
	private List<Object> prepareValues(Frame frame, GtsCarrier carrier,boolean processed) {
		List<Object> values = new ArrayList<Object>();
		values.add(processed);
		values.add(carrier == null ? "" : carrier.getCarrierNumber());
		values.add(StringUtils.leftPad("" + frame.getAfOnSequenceNumber() % 10000, 4,'0'));
		values.add(frame.getProductId());
		values.add(frame.getProductSpecCode());
		String attributeValue1 = getFrontGlassType(frame.getProductSpecCode());
		String attributeValue2 = getRearGlassType(frame.getProductSpecCode());

		values.add(attributeValue1 + "," + attributeValue2);
		values.add(frame.getKdLotNumber());
		values.add(getLotPosition(frame));
		values.add(getLotSize(frame.getProductionLot()));
		values.add(carrier == null ? -1: carrier.getStatusValue());
		values.add(isStraggler(frame.getProductId()));
		
		return values;
	}
	
	private int getLotPosition(Frame frame) {
		if(!frameLotMap.containsKey(frame.getProductionLot())) {
			List<Frame> frames = getDao(FrameDao.class).findAllByProductionLot(frame.getProductionLot());
			
			List<Frame> filteredFrames = new SortedArrayList<Frame>("getAfOnSequenceNumber");
			
			for(Frame item : frames) {
				if(item.getAfOnSequenceNumber() != null) filteredFrames.add(item);
			}
			frameLotMap.put(frame.getProductionLot(),filteredFrames);
		}
		
		List<Frame> frameList = frameLotMap.get(frame.getProductionLot());
		
		int i = 1;
		for(Frame item : frameList) {
			if(frame.getProductId().equals(item.getProductId())) return i;
			else i++;
		}
		
		frameList.add(frame);
		
		return i;
	}
	
	public String getFrontGlassType(String productSpecCode) {
		
		List<LotControlRule> currentRules = getLotControlRules(productSpecCode);
		
		LotControlRule rule = currentRules.get(0);
		String glassType = rule.getPartMasks();
		return glassType;
	}

	public String getRearGlassType(String productSpecCode) {
		List<LotControlRule> currentRules = getLotControlRules(productSpecCode);
		
		LotControlRule rule = currentRules.get(1);
		String glassType = rule.getPartMasks();
		return glassType;

	}

	private GtsCarrier getCarrier(List<GtsCarrier> carriers, String productId) {
		for (GtsCarrier carrier : carriers) {
			if(productId.equals(carrier.getProductId())) return carrier;
		}
		
		return null;
	}
	
	protected void saveBuildResults(List<InstalledPart> installedParts) {
		getDao(InstalledPartDao.class).saveAll(installedParts);
	}
	

	
	private PreProductionLot getPreProductionLot(String productionLot) {
		PreProductionLot lot = findPreProductionLot(productionLot);
		if(lot == null) {
			lot = getDao(PreProductionLotDao.class).findByKey(productionLot);
			if(lot != null) preProductionLotList.add(lot);
		}
		
		return lot;
	}
	
	private int getLotSize(String productionLot) {
		PreProductionLot lot = getPreProductionLot(productionLot);
		return lot == null ? 0 : lot.getLotSize();
	}
	
	private PreProductionLot findPreProductionLot(String productionLot) {
		for (PreProductionLot lot : preProductionLotList) {
			if(lot.getProductionLot().equals(productionLot)) return lot;
		}
		return null;
	}
	
	public String getProcessPointId() {
		return this.glassLoadView.getProcessPointId();
	}
	
	public GlassLoadPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(GlassLoadPropertyBean.class, getProcessPointId());
	}


}
