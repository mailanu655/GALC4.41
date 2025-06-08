package com.honda.galc.client.teamleader;

import org.junit.Test;

import org.powermock.core.classloader.annotations.PrepareForTest;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartByProductSpecCodeId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.property.PropertyService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.Matchers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


@PrepareForTest({PropertyService.class,FrameDao.class, ProductResultDao.class,ProcessPointDao.class, ApplicationMainPanel.class})

public class StopShipMassUpdatePanelTest extends TestCase{

	private StopShipMassUpdatePanel testPanel;

	
	/**
	 * @author Ambica Gawarla
	 * @date Sep 26,2018
	 * 
	 * Tests refresh ProductUpdateList when no products to update
	 */
	@Test	
	public void testRefreshProductUpdatelistWhenNoProductsToUpdate( ){

		CommonTlPropertyBean commonTlPropertyBeanMock = mock(CommonTlPropertyBean.class);
		FrameDao frameDaoMock =mock(FrameDao.class);
		ProductResultDao productResultDaoMock = mock(ProductResultDao.class);
		ProcessPointDao  processPointDaoMock = mock(ProcessPointDao.class);
	
		when(commonTlPropertyBeanMock.getOnPp()).thenReturn("TAF3ON1P00102,TAF3ON1P00101");
		when(commonTlPropertyBeanMock.getShippedTrackingStatuses()).thenReturn("TAH3DC1,TPC3EX1,TPC3SC1");
		when(processPointDaoMock.findById(Matchers.anyString())).thenReturn(getProcessPoint());
		when(frameDaoMock.findAllProcessedProductsForProcessPointForTimeRange("", null, null,null)).thenReturn(null);
		when(frameDaoMock.findAllProcessedProductsForProcessPointBeforeTime("", null, null)).thenReturn(null);
		when(productResultDaoMock.findAllByProductAndProcessPoint(null, null)).thenReturn(null);
		
		/*testPanel  = new StopShipMassUpdatePanel();
		testPanel.loadProperty(commonTlPropertyBeanMock);
		testPanel.setProcessPointDao(processPointDaoMock);
		testPanel.refreshProductUpdateList();
		
		assertTrue(testPanel.getStopShipProductTableModel().getItems().isEmpty());
		assertFalse(testPanel.getCreateInstalledPartsAndMeasuremntsButton().isEnabled());
		assertEquals(testPanel.getUpdateCountLabel().getText(),"0");*/
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Sep 26,2018
	 * 
	 * Tests Add and Remove button clicks
	 */	
	@Test	
	public void testAddAndRemovePartsButtons( ){
		CommonTlPropertyBean commonTlPropertyBeanMock = mock(CommonTlPropertyBean.class);
		PartNameDao partNameDaoMock =mock(PartNameDao.class);
		ProcessPointDao  processPointDaoMock = mock(ProcessPointDao.class);
		
		when(commonTlPropertyBeanMock.getOnPp()).thenReturn("TAF3ON1P00102,TAF3ON1P00101");
		when(commonTlPropertyBeanMock.getShippedTrackingStatuses()).thenReturn("TAH3DC1,TPC3EX1,TPC3SC1");
		when(partNameDaoMock.findAllByProductType(Matchers.anyString())).thenReturn( getParts());
		when(processPointDaoMock.findById(Matchers.anyString())).thenReturn(getProcessPoint());
		
		/*testPanel  = new StopShipMassUpdatePanel();
		
		//set mocks
		testPanel.loadProperty(commonTlPropertyBeanMock);
		testPanel.setProductType(ProductType.FRAME.getProductName());
		testPanel.setPartNameDao(partNameDaoMock);
		testPanel.setProcessPointDao(processPointDaoMock);
		testPanel.onTabSelected();
	
		//initial setUp
		assertFalse(testPanel.getAddPartButton().isEnabled());
		assertFalse(testPanel.getRemovePartButton().isEnabled());
		assertEquals(testPanel.getPartNameSelectionPanel().getPartNameTableModel().getItems().size(),2);
		
		//select Part to add
		PartName selectedPartName = getParts().get(0);
		testPanel.getPartNameSelectionPanel().getPartNameTableModel().selectItem(selectedPartName);
		
		assertTrue(testPanel.getAddPartButton().isEnabled());
		
		//click add button
		testPanel.getAddPartButton().doClick();
		
		
		
		assertEquals(testPanel.getAssignedPartNameSelectionPanel().getPartNameTableModel().getItems().size(),1);
		assertEquals(testPanel.getPartNameSelectionPanel().getPartNameTableModel().getItems().size(),1);
		assertFalse(testPanel.getRemovePartButton().isEnabled());
		
		//select part to Remove
		testPanel.getAssignedPartNameSelectionPanel().getPartNameTableModel().selectItem(selectedPartName);
		
		assertTrue(testPanel.getRemovePartButton().isEnabled());
		
		//click remove button
		testPanel.getRemovePartButton().doClick();
		
		assertEquals(testPanel.getAssignedPartNameSelectionPanel().getPartNameTableModel().getItems().size(),0);
		assertEquals(testPanel.getPartNameSelectionPanel().getPartNameTableModel().getItems().size(),2);*/
			
	}

	/**
	 * @author Ambica Gawarla
	 * @date Sep 26,2018
	 * 
	 * Tests refresh ProductUpdateList Table
	 */
	@Test	
	public void testRefreshProductUpdatelist( ){

		CommonTlPropertyBean commonTlPropertyBeanMock = mock(CommonTlPropertyBean.class);
		FrameDao frameDaoMock =mock(FrameDao.class);
		PartNameDao partNameDaoMock =mock(PartNameDao.class);
		ProductResultDao productResultDaoMock = mock(ProductResultDao.class);
		ProcessPointDao  processPointDaoMock = mock(ProcessPointDao.class);
		
		when(partNameDaoMock.findAllByProductType(Matchers.anyString())).thenReturn( getParts());
		when(commonTlPropertyBeanMock.getOnPp()).thenReturn("TAF3ON1P00102,TAF3ON1P00101");
		when(commonTlPropertyBeanMock.getShippedTrackingStatuses()).thenReturn("TAH3DC1,TPC3EX1,TPC3SC1");
		when(frameDaoMock.findAllProcessedProductsForProcessPointBeforeTime(Matchers.anyString(), Matchers.anyString(), Matchers.anyList())).thenReturn(getProducts()).thenReturn(null);
		when(frameDaoMock.findAllProcessedProductsForProcessPointForTimeRange(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyList())).thenReturn(getProducts()).thenReturn(null);
		when(productResultDaoMock.findByProductAndProcessPoint((ProductResult)Matchers.anyObject())).thenReturn(getProductResults()).thenReturn(null);
		when(processPointDaoMock.findById(Matchers.anyString())).thenReturn(getProcessPoint());
		
		/*testPanel  = new StopShipMassUpdatePanel();
		
		//set Mocks
		testPanel.loadProperty(commonTlPropertyBeanMock);
		testPanel.setProductType(ProductType.FRAME.getProductName());
		testPanel.setFrameDao(frameDaoMock);
		testPanel.setProductResultDao(productResultDaoMock);
		testPanel.setPartNameDao(partNameDaoMock);
		testPanel.setProcessPointDao(processPointDaoMock);
		
		//initialize
		testPanel.onTabSelected();
		
		//parts selected
		PartName selectedPartName = getParts().get(0);
		testPanel.getPartNameSelectionPanel().getPartNameTableModel().selectItem(selectedPartName);
		testPanel.getAddPartButton().doClick();
		
		//OnprocessPointSelected
		testPanel.getProcessPointComboBox().setSelectedIndex(0);
		
		//StartProductId and endProductId Specified
		testPanel.getStartProductIdText().setText("Product1");
		testPanel.getEndProductIdText().setText("Product5");
		
		//refreshProductUpdateList
		testPanel.refreshProductUpdateList();
		
		assertFalse(testPanel.getStopShipProductTableModel().getItems().isEmpty());
		assertTrue(testPanel.getCreateInstalledPartsAndMeasuremntsButton().isEnabled());
		assertEquals(testPanel.getUpdateCountLabel().getText(),"1");
		
		//when No Products found in time range
		testPanel.refreshProductUpdateList();
		
		assertTrue(testPanel.getStopShipProductTableModel().getItems().isEmpty());
		assertFalse(testPanel.getCreateInstalledPartsAndMeasuremntsButton().isEnabled());
		assertNotSame(testPanel.getUpdateCountLabel().getText(),"1");*/
		
		
	}

	/**
	 * @author Ambica Gawarla
	 * @date Sep 26,2018
	 * 
	 * Tests createInstalledPartsAndMeasurements button click
	 */
	@Test	
	public void testCreateInstalledPartsAndMeasuremnts(){

		CommonTlPropertyBean commonTlPropertyBeanMock = mock(CommonTlPropertyBean.class);
		FrameDao frameDaoMock =mock(FrameDao.class);
		LotControlRuleDao lotControlRuleDaoMock = mock(LotControlRuleDao.class);
		InstalledPartDao installedPartDaoMock = mock(InstalledPartDao.class);
		PartNameDao partNameDaoMock =mock(PartNameDao.class);
		ProductResultDao productResultDaoMock = mock(ProductResultDao.class);
		ProcessPointDao  processPointDaoMock = mock(ProcessPointDao.class);
		
		when(partNameDaoMock.findAllByProductType(Matchers.anyString())).thenReturn( getParts());
		when(commonTlPropertyBeanMock.getOnPp()).thenReturn("TAF3ON1P00102,TAF3ON1P00101");
		when(commonTlPropertyBeanMock.getShippedTrackingStatuses()).thenReturn("TAH3DC1,TPC3EX1,TPC3SC1");
		when(frameDaoMock.findAllProcessedProductsForProcessPointForTimeRange(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),Matchers.anyList())).thenReturn(getProducts());
		when(frameDaoMock.findAllProcessedProductsForProcessPointBeforeTime(Matchers.anyString(), Matchers.anyString(), Matchers.anyList())).thenReturn(getProducts());
		when(productResultDaoMock.findByProductAndProcessPoint((ProductResult)Matchers.anyObject())).thenReturn(getProductResults());
		when(lotControlRuleDaoMock.findAllByPartName(Matchers.anyString())).thenReturn(getLotControlRules());
		when(installedPartDaoMock.isPartInstalled(Matchers.anyString(), Matchers.anyString())).thenReturn(false);
		when(processPointDaoMock.findById(Matchers.anyString())).thenReturn(getProcessPoint());
		
		/*testPanel  = new StopShipMassUpdatePanel();
		
		//set Mocks
		testPanel.loadProperty(commonTlPropertyBeanMock);
		testPanel.setProductType(ProductType.FRAME.getProductName());
		testPanel.setFrameDao(frameDaoMock);
		testPanel.setProductResultDao(productResultDaoMock);
		testPanel.setPartNameDao(partNameDaoMock);
		testPanel.setLotControlRuleDao(lotControlRuleDaoMock);
		testPanel.setInstalledPartDao(installedPartDaoMock);
		testPanel.setProcessPointDao(processPointDaoMock);
		
		//initialize
		testPanel.onTabSelected();
		
		//parts selected
		PartName selectedPartName = getParts().get(0);
		testPanel.getPartNameSelectionPanel().getPartNameTableModel().selectItem(selectedPartName);
		testPanel.getAddPartButton().doClick();
		
		//OnprocessPointSelected
		testPanel.getProcessPointComboBox().setSelectedIndex(0);
		
		//StartProductId and endProductId Specified
		testPanel.getStartProductIdText().setText("Product1");
		testPanel.getEndProductIdText().setText("Product5");
		
		//refreshProductUpdate
		testPanel.refreshProductUpdateList();
		
		//CreateInstalledPartsAndMeasuremntsButton Click
		testPanel.setContinue(true);
		testPanel.refreshProductStatus(null);
		testPanel.createInstalledPartsAndMeasuremnts();
		
		assertFalse(testPanel.getProductUpdateStatusTableModel().getItems().isEmpty());
		assertEquals(testPanel.getProductUpdateStatusTableModel().getItems().size(),1);
		assertTrue(testPanel.getProductUpdateStatusTableModel().getItem(0).isCreated());*/
		
	}
	

	private List<PartName> getParts() {
		List<PartName> partNames = new ArrayList<PartName>();
		PartName part1 = new PartName();
		part1.setPartName("partName1");
		partNames.add(part1);
		
		PartName part2 = new PartName();
		part2.setPartName("partName2");
		partNames.add(part2);
		
		return partNames;
	}
	
	private List<LotControlRule> getLotControlRules() {
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		LotControlRule rule1 = new LotControlRule();
		LotControlRuleId ruleId1 = new LotControlRuleId();
		ruleId1.setPartName("partName1");
		ruleId1.setProcessPointId("TAH3RE1P00101");
		ruleId1.setProductSpecCode("HTLAAE700 NH603P    A");
		
		rule1.setId(ruleId1);
		rule1.setSerialNumberScanFlag(1);
		
		List<PartByProductSpecCode> partByProductSpecs1 = new ArrayList<PartByProductSpecCode>();
		
		PartByProductSpecCode partByProductSpecCode1 = new PartByProductSpecCode();
		PartByProductSpecCodeId partByProductSpecCodeId1 = new PartByProductSpecCodeId();
		partByProductSpecCodeId1.setPartId("A001");
		partByProductSpecCodeId1.setPartName("partName1");
		partByProductSpecCodeId1.setProductSpecCode("HTLAAE700 NH603P    A");
		partByProductSpecCode1.setId(partByProductSpecCodeId1);
		
		partByProductSpecs1.add(partByProductSpecCode1);
		
		rule1.setPartByProductSpecs(partByProductSpecs1);
		
		
		rules.add(rule1);
		
		LotControlRule rule2 = new LotControlRule();
		LotControlRuleId ruleId2 = new LotControlRuleId();
		ruleId2.setPartName("partName2");
		ruleId2.setProcessPointId("TAH3RE1P00101");
		ruleId2.setProductSpecCode("HTLAAE700 NH603P    A");
		
		rule2.setId(ruleId2);
		
		rule2.setSerialNumberScanFlag(1);
		
		List<PartByProductSpecCode> partByProductSpecs2 = new ArrayList<PartByProductSpecCode>();
		
		PartByProductSpecCode partByProductSpecCode2 = new PartByProductSpecCode();
		PartByProductSpecCodeId partByProductSpecCodeId2 = new PartByProductSpecCodeId();
		partByProductSpecCodeId2.setPartId("A001");
		partByProductSpecCodeId2.setPartName("partName2");
		partByProductSpecCodeId2.setProductSpecCode("HTLAAE700 NH603P    A");
		partByProductSpecCode2.setId(partByProductSpecCodeId1);
		
		partByProductSpecs2.add(partByProductSpecCode2);
		
		rule2.setPartByProductSpecs(partByProductSpecs2);
		
		rules.add(rule2);
		
		return rules;
	}

	private List<ProductResult> getProductResults(){
		List<ProductResult> prodResults = new ArrayList<ProductResult>();
		ProductResult productResult1 = new ProductResult();
		productResult1.setProductId("5J6RW2H50HL064769");
		productResult1.setProcessPointId("TAF3ON1P00102");
		productResult1.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		prodResults.add(productResult1);
		
		return prodResults;
	}
	
	private List<Frame> getProducts(){
		List<Frame> productList = new ArrayList<Frame>();
		
		Frame frame1= new Frame();
		frame1.setProductId("5J6RW2H50HL064769");
		frame1.setProductSpecCode("HTLAAE700 NH603P    A ");
		frame1.setTrackingStatus("Passed Afon");
		frame1.setAfOnSequenceNumber(68088);
		
		productList.add(frame1);
		
		
		return productList;
	}
	
	private ProcessPoint getProcessPoint(){
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("processPointId");
		processPoint.setProcessPointName("processPointName");
		
		return processPoint;
	}
}
