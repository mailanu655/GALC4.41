package com.honda.galc.client.product.process.engine.bearing.pick.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.process.engine.bearing.model.BearingModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.bearing.BearingMatrixDao;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingMatrixId;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPickModel</code> is ... .
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
 */
public class BearingPickModel extends BearingModel {

	public enum BearingPartType {
		MAIN_UPPER, MAIN_LOWER, CONROD_UPPER, CONROD_LOWER
	};

	private List<BearingPartType> bearingPartTypes;
	private Map<String, Color> colors;
	private BearingMatrix bearingMatrix;

	public BearingPickModel(ApplicationContext applicationContext) {
		super(applicationContext);
		this.colors = new TreeMap<String, Color>();
		this.bearingPartTypes = new ArrayList<BearingPartType>();
		setProperty(PropertyService.getPropertyBean(BearingPickPropertyBean.class, getApplicationContext().getProcessPointId()));
		init();
	}

	// === prepare model === //
	protected void init() {
		initColors();
		initBearingPartTypes();
	}

	protected void initColors() {
		getColors().put("BLACK", Color.BLACK);
		getColors().put("RED", Color.RED);
		getColors().put("GREEN", Color.GREEN);
		getColors().put("BLUE", Color.BLUE);
		getColors().put("WHITE", Color.WHITE);

		getColors().put("YELLOW", Color.YELLOW);
		getColors().put("PINK", Color.PINK);
		getColors().put("BROWN", new Color(204, 102, 0));
		
		getColors().put("PURPLE", new Color(128, 0, 128));
		getColors().put("CYAN", Color.CYAN);
		getColors().put("ORANGE", Color.ORANGE);
		getColors().put("GRAY", Color.GRAY);  

		getColors().put("NO_DATA", Color.LIGHT_GRAY);

		// TODO consider loading it from DB
		// empty field - see getIdleColor();
	}

	protected void initBearingPartTypes() {
		String propertyString = getProperty().getBearingPartTypes();
		if (!StringUtils.isBlank(propertyString)) {
			String[] ar = propertyString.split(",");
			for (String str : ar) {
				if (StringUtils.isBlank(str)) {
					continue;
				}
				str = str.trim();
				try {
					BearingPartType type = BearingPartType.valueOf(str);
					getBearingPartTypes().add(type);
				} catch (Exception e) {
					String msg = String.format("Could not parse BearingPartType for %s");
					Logger.getLogger(getClass().getName()).warn(msg);
				}
			}
		}
		if (getBearingPartTypes().isEmpty()) {
			getBearingPartTypes().addAll(Arrays.asList(BearingPartType.values()));
		}
	}

	// === business api === //
	public BearingSelectResult selectMeasurements(BaseProduct product) {
		BearingSelectResult bsResult = ServiceFactory.getDao(BearingSelectResultDao.class).findByProductId(product.getProductId());
		return bsResult;
	}

	public Map<Integer, BearingPart> selectMainUpperBearings(BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart main01 = result.getJournalUpperBearing01() == null ? null : dao.findByKey(result.getJournalUpperBearing01());
		BearingPart main02 = result.getJournalUpperBearing02() == null ? null : dao.findByKey(result.getJournalUpperBearing02());
		BearingPart main03 = result.getJournalUpperBearing03() == null ? null : dao.findByKey(result.getJournalUpperBearing03());
		BearingPart main04 = result.getJournalUpperBearing04() == null ? null : dao.findByKey(result.getJournalUpperBearing04());
		BearingPart main05 = result.getJournalUpperBearing05() == null ? null : dao.findByKey(result.getJournalUpperBearing05());
		BearingPart main06 = result.getJournalUpperBearing06() == null ? null : dao.findByKey(result.getJournalUpperBearing06());

		map.put(1, main01);
		map.put(2, main02);
		map.put(3, main03);
		map.put(4, main04);
		map.put(5, main05);
		map.put(6, main06);

		return map;
	}

	public Map<Integer, BearingPart> selectMainLowerBearings(BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart main01 = result.getJournalLowerBearing01() == null ? null : dao.findByKey(result.getJournalLowerBearing01());
		BearingPart main02 = result.getJournalLowerBearing02() == null ? null : dao.findByKey(result.getJournalLowerBearing02());
		BearingPart main03 = result.getJournalLowerBearing03() == null ? null : dao.findByKey(result.getJournalLowerBearing03());
		BearingPart main04 = result.getJournalLowerBearing04() == null ? null : dao.findByKey(result.getJournalLowerBearing04());
		BearingPart main05 = result.getJournalLowerBearing05() == null ? null : dao.findByKey(result.getJournalLowerBearing05());
		BearingPart main06 = result.getJournalLowerBearing06() == null ? null : dao.findByKey(result.getJournalLowerBearing06());

		map.put(1, main01);
		map.put(2, main02);
		map.put(3, main03);
		map.put(4, main04);
		map.put(5, main05);
		map.put(6, main06);

		return map;
	}

	public Map<Integer, BearingPart> selectConrodUpperBearings(BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart conrod01 = result.getConrodUpperBearing01() == null ? null : dao.findByKey(result.getConrodUpperBearing01());
		BearingPart conrod02 = result.getConrodUpperBearing02() == null ? null : dao.findByKey(result.getConrodUpperBearing02());
		BearingPart conrod03 = result.getConrodUpperBearing03() == null ? null : dao.findByKey(result.getConrodUpperBearing03());
		BearingPart conrod04 = result.getConrodUpperBearing04() == null ? null : dao.findByKey(result.getConrodUpperBearing04());
		BearingPart conrod05 = result.getConrodUpperBearing05() == null ? null : dao.findByKey(result.getConrodUpperBearing05());
		BearingPart conrod06 = result.getConrodUpperBearing06() == null ? null : dao.findByKey(result.getConrodUpperBearing06());

		map.put(1, conrod01);
		map.put(2, conrod02);
		map.put(3, conrod03);
		map.put(4, conrod04);
		map.put(5, conrod05);
		map.put(6, conrod06);

		return map;
	}

	public Map<Integer, BearingPart> selectConrodLowerBearings(BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart conrod01 = result.getConrodLowerBearing01() == null ? null : dao.findByKey(result.getConrodLowerBearing01());
		BearingPart conrod02 = result.getConrodLowerBearing02() == null ? null : dao.findByKey(result.getConrodLowerBearing02());
		BearingPart conrod03 = result.getConrodLowerBearing03() == null ? null : dao.findByKey(result.getConrodLowerBearing03());
		BearingPart conrod04 = result.getConrodLowerBearing04() == null ? null : dao.findByKey(result.getConrodLowerBearing04());
		BearingPart conrod05 = result.getConrodLowerBearing05() == null ? null : dao.findByKey(result.getConrodLowerBearing05());
		BearingPart conrod06 = result.getConrodLowerBearing06() == null ? null : dao.findByKey(result.getConrodLowerBearing06());

		map.put(1, conrod01);
		map.put(2, conrod02);
		map.put(3, conrod03);
		map.put(4, conrod04);
		map.put(5, conrod05);
		map.put(6, conrod06);

		return map;
	}

	public Color getColor(String colorName) {
		Color color = getColors().get(colorName);
		if (color == null) {
			return Color.GRAY;
		}
		return color;
	}

	public List<BearingPartType> getBearingPartTypes() {
		return bearingPartTypes;
	}

	// === derived config === //
	public List<Integer> getMainBearingIxDisplaySequence(BearingMatrix bearingMatrix) {
		List<Integer> list = getMainBearingIxSequence(bearingMatrix);
		if (getProperty().isBearingDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	public List<Integer> getConrodIxDisplaySequence(BearingMatrix bearingMatrix) {
		List<Integer> list = getConrodIxSequence(bearingMatrix);
		if (getProperty().isBearingDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	public Map<String, Color> getColors() {
		return colors;
	}

	public BearingPickPropertyBean getProperty() {
		return (BearingPickPropertyBean) super.getProperty();
	}
	
	public int getMainBearingCount(BearingMatrix bearingMatrix) {
		int defaultCount = getProperty().getMainBearingCount();
		int count = defaultCount;
		if(bearingMatrix != null && getProperty().getUseBearingMatrixForBearingPick()){
			count = bearingMatrix.getNumberOfMainBearings();
		}
		
		return count;
	}

	public int getConrodCount(BearingMatrix bearingMatrix) {
		int defaultCount = getProperty().getConrodCount();
		int count = defaultCount;
		if(bearingMatrix != null && getProperty().getUseBearingMatrixForBearingPick()){
			count = bearingMatrix.getNumberOfConrods();
		}
		return count;
	}

	public List<Integer> getMainBearingIxSequence(BearingMatrix bearingMatrix) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < getMainBearingCount(bearingMatrix); i++) {
			list.add(i + 1);
		}
		return list;
	}

	public List<Integer> getConrodIxSequence(BearingMatrix bearingMatrix) {
		List<Integer> list = new ArrayList<Integer>();

		if (getConrodCount(bearingMatrix) == 6) {
			list.addAll(Arrays.asList(new Integer[] { 1, 4, 2, 5, 3, 6 }));
			return list;
		}

		if (getConrodCount(bearingMatrix) == 8) {
			list.addAll(Arrays.asList(new Integer[] { 1, 5, 2, 6, 3, 7, 4, 8 }));
			return list;
		}

		for (int i = 0; i < getConrodCount(bearingMatrix); i++) {
			list.add(i + 1);
		}
		return list;
	}

	public BearingMatrix selectBearingMatrix(BearingSelectResult result) {
		BearingMatrixDao bearingMatrixDao = ServiceFactory.getDao(BearingMatrixDao.class);
		BearingMatrixId id = new BearingMatrixId();
		id.setModelCode(result.getModelCode());
		id.setModelYearCode(result.getModelYearCode());
		this.bearingMatrix = bearingMatrixDao.findByKey(id);
		return bearingMatrix;
	}
}
