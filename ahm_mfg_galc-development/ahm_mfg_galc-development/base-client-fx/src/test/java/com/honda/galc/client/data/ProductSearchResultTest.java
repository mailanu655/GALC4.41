/**
 * 
 */
package com.honda.galc.client.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationPreviousDefectId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author vf031824
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class})
public class ProductSearchResultTest {

	@Mock
	private QiRepairResultDao qiRepairResultDaoMock;
	
	@Mock
	private QiStationPreviousDefectDao qiStationPreviousDefectDaoMock;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(ServiceFactory.class);
	}

	@After
	public void tearDown() {
		ProductSearchResult.clearDefectResultMap();
		ProductSearchResult.clearDefectsProcessingMap();
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#initCommonDefects(java.util.List)}
	 * Test to verify that the defect processing map is cleared before the defects from the
	 * defect result map are populated.
	 * 
	 *  productId = LG93291019T, LG93291019L,LG93291010P
	 *  defectTypeName  DEFECT TYPE NAME, DEFECT TYPE NAME2, DEFECT TYPE NAME3
	 */
	@Test
	public void initCommonDefects_clearDefectResultMap() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAM2");
		defectResult.setDefectResultId(121);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME1");
		defectResult2.setDefectResultId(122);
		QiRepairResultDto defectResult4 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult3.setDefectResultId(123);
		QiRepairResultDto defectResult5 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(124);
		QiRepairResultDto defectResult6 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult3.setDefectResultId(125);
		QiRepairResultDto defectResult7 = createQiRepairResultDto("LG93291010P", "DEFECT TYPE NAME2");
		defectResult3.setDefectResultId(126);
		
		List<QiRepairResultDto> defectList = new ArrayList<QiRepairResultDto>();
		defectList.add(defectResult);
		defectList.add(defectResult2);
		defectList.add(defectResult3);
		defectList.add(defectResult4);
		defectList.add(defectResult4);
		defectList.add(defectResult5);
		defectList.add(defectResult6);
		defectList.add(defectResult7);

		ProductSearchResult.setDefectResults(defectList);
		
		BaseProduct product1 = createProduct("111");

		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		productList.add(product1);

		when(ServiceFactory.getDao(QiRepairResultDao.class)).thenReturn(qiRepairResultDaoMock);
		when(qiRepairResultDaoMock.findAllDefectsByProductIds(Arrays.asList("111"))).thenReturn(new ArrayList<QiRepairResultDto>());
		
		ProductSearchResult.setCurrentWorkingProcessPointId("PP10001");
		QiStationPreviousDefectId qiStationPreviousDefectId = new QiStationPreviousDefectId();
		qiStationPreviousDefectId.setProcessPointId("PP1001");
		qiStationPreviousDefectId.setEntryDivisionId("AF");
		QiStationPreviousDefect qiStationPreviousDefect = new QiStationPreviousDefect();
		qiStationPreviousDefect.setId(qiStationPreviousDefectId);
		List<QiStationPreviousDefect> qiStationPreviousDefectList = new ArrayList<QiStationPreviousDefect>();
		qiStationPreviousDefectList.add(qiStationPreviousDefect);
		when(ServiceFactory.getDao(QiStationPreviousDefectDao.class)).thenReturn(qiStationPreviousDefectDaoMock);
		when(qiStationPreviousDefectDaoMock.findAllByProcessPoint("PP10001")).thenReturn(qiStationPreviousDefectList);

		ProductSearchResult.initCommonDefects(productList);
		
		assertEquals(0, ProductSearchResult.getDefectsProcessing().size());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#initCommonDefects(java.util.List)}
	 * Test for adding all common defect to the defect processing map.
	 * 
	 *  productId = LG93291019T, LG93291019L,LG93291010P
	 *  defectTypeName  DEFECT TYPE NAME, DEFECT TYPE NAME2, DEFECT TYPE NAME3, DEFECT TYPE NAME4
	 */
	@Test
	public void initCommonDefects_addCommonDefectsForFirstProduct() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(121);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME4");
		defectResult3.setDefectResultId(122);
		QiRepairResultDto defectResult4 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult4.setDefectResultId(123);
		QiRepairResultDto defectResult5 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult5.setDefectResultId(124);
		QiRepairResultDto defectResult6 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult6.setDefectResultId(125);
		QiRepairResultDto defectResult7 = createQiRepairResultDto("LG93291010P", "DEFECT TYPE NAME2");
		defectResult7.setDefectResultId(126);
		
		List<QiRepairResultDto> defectList = new ArrayList<QiRepairResultDto>();
		defectList.add(defectResult);
		defectList.add(defectResult2);
		defectList.add(defectResult3);
		defectList.add(defectResult4);
		defectList.add(defectResult4);
		defectList.add(defectResult5);
		defectList.add(defectResult6);
		defectList.add(defectResult7);
		
		ProductSearchResult.setDefectResults(defectList);
		
		BaseProduct product1 = createProduct("LG93291019T");
		BaseProduct product2 = createProduct("LG93291019L");
		BaseProduct product3 = createProduct("LG93291010P");
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		
		ProductSearchResult.setCurrentWorkingProcessPointId("PP10001");
		QiStationPreviousDefectId qiStationPreviousDefectId = new QiStationPreviousDefectId();
		qiStationPreviousDefectId.setProcessPointId("PP1001");
		qiStationPreviousDefectId.setEntryDivisionId("AF");
		QiStationPreviousDefect qiStationPreviousDefect = new QiStationPreviousDefect();
		qiStationPreviousDefect.setId(qiStationPreviousDefectId);
		List<QiStationPreviousDefect> qiStationPreviousDefectList = new ArrayList<QiStationPreviousDefect>();
		qiStationPreviousDefectList.add(qiStationPreviousDefect);
		when(ServiceFactory.getDao(QiStationPreviousDefectDao.class)).thenReturn(qiStationPreviousDefectDaoMock);
		when(qiStationPreviousDefectDaoMock.findAllByProcessPoint("PP10001")).thenReturn(qiStationPreviousDefectList);
		
		List<QiRepairResultDto> commonMainDefects = ProductSearchResult.initCommonDefects(productList);
		
		assertEquals(3, commonMainDefects.size());
		assertTrue(commonMainDefects.contains(defectResult2));
		assertTrue(commonMainDefects.contains(defectResult4));
		assertTrue(commonMainDefects.contains(defectResult7));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#initCommonDefects(java.util.List)}
	 *
	 * 
	 *  productId = LG93291019T, LG93291019L,LG93291010P
	 *  defectTypeName  DEFECT TYPE NAME, DEFECT TYPE NAME2, DEFECT TYPE NAME3, DEFECT TYPE NAME4
	 */
	@Test
	public void initCommonDefects_removeUncommonDefects() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(121);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME1");
		defectResult3.setDefectResultId(122);
		QiRepairResultDto defectResult4 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult4.setDefectResultId(123);
		QiRepairResultDto defectResult5 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult5.setDefectResultId(124);
		QiRepairResultDto defectResult6 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult6.setDefectResultId(125);
		QiRepairResultDto defectResult7 = createQiRepairResultDto("LG93291010P", "DEFECT TYPE NAME2");
		defectResult7.setDefectResultId(126);
		
		List<QiRepairResultDto> defectList = new ArrayList<QiRepairResultDto>();
		defectList.add(defectResult);
		defectList.add(defectResult2);
		defectList.add(defectResult3);
		defectList.add(defectResult4);
		defectList.add(defectResult4);
		defectList.add(defectResult5);
		defectList.add(defectResult6);
		defectList.add(defectResult7);
		
		ProductSearchResult.setDefectResults(defectList);
		
		BaseProduct product1 = createProduct("LG93291019T");
		BaseProduct product2 = createProduct("LG93291019L");
		BaseProduct product3 = createProduct("LG93291010P");
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		
		ProductSearchResult.setCurrentWorkingProcessPointId("PP10001");
		QiStationPreviousDefectId qiStationPreviousDefectId = new QiStationPreviousDefectId();
		qiStationPreviousDefectId.setProcessPointId("PP1001");
		qiStationPreviousDefectId.setEntryDivisionId("AF");
		QiStationPreviousDefect qiStationPreviousDefect = new QiStationPreviousDefect();
		qiStationPreviousDefect.setId(qiStationPreviousDefectId);
		List<QiStationPreviousDefect> qiStationPreviousDefectList = new ArrayList<QiStationPreviousDefect>();
		qiStationPreviousDefectList.add(qiStationPreviousDefect);
		when(ServiceFactory.getDao(QiStationPreviousDefectDao.class)).thenReturn(qiStationPreviousDefectDaoMock);
		when(qiStationPreviousDefectDaoMock.findAllByProcessPoint("PP10001")).thenReturn(qiStationPreviousDefectList);
		
		List<QiRepairResultDto> commonMainDefects = ProductSearchResult.initCommonDefects(productList);
		
		assertEquals(3, commonMainDefects.size());
		assertTrue(commonMainDefects.contains(defectResult2));
		assertTrue(commonMainDefects.contains(defectResult4));
		assertTrue(commonMainDefects.contains(defectResult7));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(java.util.List)}
	 * Test for adding a new defect to the defect Processing map, the defect is not already in the map
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void addNewDefect_newDefect() {
		QiDefectResult defectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME");
		ProductSearchResult.addNewDefect(defectResult);
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 1);
		QiRepairResultDto resultDto = new QiRepairResultDto(defectResult, 0);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(resultDto));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(java.util.List)}
	 * Test for adding a new defect to the defect Processing map, the defect is already in the map.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void addNewDefect_DuplicateDefect() {
		QiDefectResult defectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME");
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult);
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 1);
	}
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(java.util.List)}
	 * Test for adding a new defect to the defect Processing map for multiple products.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void addNewDefect_multipleProducts() {
		QiDefectResult defectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME");
		QiDefectResult defectResult2 = createQiDefectResult("LG93291017R", "DEFECT TYPE NAME");
		
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 2);
		QiRepairResultDto resultDto = new QiRepairResultDto(defectResult, 0);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(resultDto));
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(resultDto));
		resultDto = new QiRepairResultDto(defectResult2, 0);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(resultDto));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(java.util.List)}
	 * Test for adding multiple new defects to the defect Processing map.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void addNewDefect_multipleDefects() {
		QiDefectResult defectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME");

		ProductSearchResult.addNewDefect(defectResult);
		QiDefectResult defectResult2 = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME2");
		ProductSearchResult.addNewDefect(defectResult2);
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 2);
		QiRepairResultDto resultDto = new QiRepairResultDto(defectResult, 0);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(resultDto));
		resultDto = new QiRepairResultDto(defectResult2, 0);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(resultDto));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * Test for adding a new defect to the defect Processing map, the defect is not already in the map.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void addNewDefect_newRepairResultDto() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		ProductSearchResult.addNewDefect(defectResult);
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 1);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(defectResult));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * Test for adding a new defect to the defect Processing map, the defect is already in the map.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void addNewDefect_DuplicateRepairResultDto() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult);

		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 1);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(defectResult));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * Test for adding a new defect to the defect Processing map for multiple products.
	 * 
	 *  productId = LG93291019T, LG93291017R
	 *  defectTypeName  DEFECT TYPE NAME, DEFECT TYPE NAME2
	 */
	@Test
	public void addNewDefect_multipleProductsRepairResult() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		ProductSearchResult.addNewDefect(defectResult);

		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291017R", "DEFECT TYPE NAME");
		ProductSearchResult.addNewDefect(defectResult2);
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 2);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(defectResult));
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(defectResult2));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#addNewDefect(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * Test for adding multiple defects to the defect Processing map for a single product id.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME, DEFECT TYPE NAME2
	 */
	@Test
	public void addNewDefect_multipleRepairs() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME2");

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 2);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(defectResult));
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(defectResult));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#clearDefectResultMap()}.
	 * Test for removing all defects from the defect result map.
	 * 
	 *  productId = LG93291019T LG93291019L LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void clearDefectResultMap_clearAllDefects() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);

		List<QiRepairResultDto> defectResults = new ArrayList<QiRepairResultDto>();
		defectResults.add(defectResult);
		defectResults.add(defectResult2);
		defectResults.add(defectResult3);
		ProductSearchResult.setDefectResults(defectResults);
		assertEquals(3, ProductSearchResult.getAllDefectResults().size());
		ProductSearchResult.clearDefectResultMap();
		assertEquals(0, ProductSearchResult.getAllDefectResults().size());

	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#clearDefectsProcessingMap()}.
	 * Test for removing all defects from the defect processing map.
	 * 
	 *  productId = LG93291019T LG93291019L LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void clearDefectsProcessingMap_clearAllDefects() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);
		assertEquals(3, ProductSearchResult.getDefectsProcessing().size());
		ProductSearchResult.clearDefectsProcessingMap();
		assertEquals(0, ProductSearchResult.getDefectsProcessing().size());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectsProcessingMap(java.util.List)}.
	 * Test for updating the defects processing map when no products are updated. Three new defects are inserted.
	 * 
	 * product ids = LG93291019T, LG93291019L,
	 * updated product id  LG93291019T, LG93291019L, LG93291010P
	 * defect type names = DEFECT TYPE NAME, DEFECT TYPE NAME3
	 * updated defect type name = DEFECT TYPE NAME4
	 */
	@Test
	public void updateDefectsProcessingMap_noDefectsUpdated() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		List<QiDefectResult> updateDefects =  new ArrayList<QiDefectResult>();
		QiDefectResult updateDefectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME4");
		updateDefectResult.setDefectResultId(126);
		QiDefectResult updateDefectResult2 = createQiDefectResult("LG93291019L", "DEFECT TYPE NAME4");
		updateDefectResult2.setDefectResultId(127);
		QiDefectResult updateDefectResult3 = createQiDefectResult("LG93291010P", "DEFECT TYPE NAME4");
		updateDefectResult3.setDefectResultId(128);
		updateDefects.add(updateDefectResult);
		updateDefects.add(updateDefectResult2);
		updateDefects.add(updateDefectResult3);

		QiRepairResultDto defectResult4 = new QiRepairResultDto(updateDefectResult, 0);
		defectResult4.setDefectResultId(126);
		QiRepairResultDto defectResult5 = new QiRepairResultDto(updateDefectResult2, 0);
		defectResult5.setDefectResultId(127);
		QiRepairResultDto defectResult6 = new QiRepairResultDto(updateDefectResult3, 0);
		defectResult6.setDefectResultId(128);

		ProductSearchResult.updateDefectsProcessingMap(updateDefects);
		List<QiRepairResultDto> defects = ProductSearchResult.getDefectsProcessing();

		assertEquals(6, defects.size());
		assertTrue(defects.contains(defectResult));
		assertTrue(defects.contains(defectResult2));
		assertTrue(defects.contains(defectResult3));
		assertTrue(defects.contains(defectResult4));
		assertTrue(defects.contains(defectResult5));
		assertTrue(defects.contains(defectResult6));		
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectsProcessingMap(java.util.List)}.
	 * Test for updating the defects processing map when one products is updated.
	 * 
	 * product ids = LG93291019T, LG93291019L,
	 * updated product id = LG93291019L
	 * defect type names = DEFECT TYPE NAME, DEFECT TYPE NAME3
	 * updated defect type name = DEFECT TYPE NAME4
	 */
	@Test
	public void updateDefectsProcessingMap_defectsUpdated() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(1);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(2);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(3);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		List<QiDefectResult> updateDefects =  new ArrayList<QiDefectResult>();
		QiDefectResult updateDefectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME");
		
		updateDefectResult.setDefectResultId(1);
		updateDefectResult.setCurrentDefectStatus((short) 4);
		
		updateDefects.add(updateDefectResult);
		
		QiRepairResultDto defectResult4 = new QiRepairResultDto(updateDefectResult, 0);
		defectResult4.setDefectResultId(1);
		defectResult4.setCurrentDefectStatus((short) 4);

		ProductSearchResult.updateDefectsProcessingMap(updateDefects);
		List<QiRepairResultDto> defects = ProductSearchResult.getDefectsProcessing();

		assertEquals(3, defects.size());
		assertTrue(defects.contains(defectResult2));
		assertTrue(defects.contains(defectResult3));
		assertTrue(defects.contains(defectResult4));		
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectsProcessingMap(java.util.List)}.
	 * Test for updating the defects processing map when no products are updated. One new defect is inserted.
	 * 
	 * product ids = LG93291019T, LG93291019L,
	 * updated product id  LG93291019T, LG93291019L
	 * defect type names = DEFECT TYPE NAME, DEFECT TYPE NAME3
	 * updated defect type name = DEFECT TYPE NAME4
	 */
	@Test
	public void updateDefectsProcessingMap_noDefectUpdated() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		QiDefectResult updateDefectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME4");
		updateDefectResult.setDefectResultId(126);

		QiRepairResultDto defectResult4 = new QiRepairResultDto(updateDefectResult, 0);
		defectResult4.setDefectResultId(126);

		ProductSearchResult.updateDefectsProcessingMap(defectResult4);
		List<QiRepairResultDto> defects = ProductSearchResult.getDefectsProcessing();

		assertEquals(4, defects.size());
		assertTrue(defects.contains(defectResult));
		assertTrue(defects.contains(defectResult2));
		assertTrue(defects.contains(defectResult3));
		assertTrue(defects.contains(defectResult4));	
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectsProcessingMap(java.util.List)}.
	 * Test for updating the defects processing map when one defect is updated
	 * 
	 * product ids = LG93291019T, LG93291019L,
	 * updated product id  LG93291019T, LG93291019L, LG93291010P
	 * defect type names = DEFECT TYPE NAME, DEFECT TYPE NAME3
	 * updated defect type name = DEFECT TYPE NAME4
	 */
	@Test
	public void updateDefectsProcessingMap_defectUpdated() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		QiDefectResult updateDefectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME");
		updateDefectResult.setDefectResultId(120);
		updateDefectResult.setCurrentDefectStatus((short) 8);

		QiRepairResultDto defectResult4 = new QiRepairResultDto(updateDefectResult, 0);
		defectResult4.setDefectResultId(120);

		ProductSearchResult.updateDefectsProcessingMap(defectResult4);
		List<QiRepairResultDto> defects = ProductSearchResult.getDefectsProcessing();

		assertEquals(3, defects.size());
		assertTrue(defects.contains(defectResult2));
		assertTrue(defects.contains(defectResult3));
		assertTrue(defects.contains(defectResult4));	
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectsProcessingMap(java.util.List)}.
	 * Test for updating the defects processing map when one defect is updated. The update includes a child defect
	 * 
	 * product ids = LG93291019T, LG93291019L,
	 * updated product id  LG93291019T, LG93291019L
	 * defect type names = DEFECT TYPE NAME, DEFECT TYPE NAME3, DEFECT TYPE NAME 4
	 * updated defect type name = CHILD DEFECT TYPE
	 */
	@Test
	public void updateDefectsProcessingMap_defectUpdatedWithChildDefect() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
	
		List<QiRepairResultDto> childDefectList = new ArrayList<QiRepairResultDto>();
		QiRepairResultDto childDefect = createQiRepairResultDto("LG93291019T", "CHILD DEFECT TYPE");
		childDefect.setRepairId(10);
		childDefectList.add(childDefect);
		defectResult.setChildRepairResultList(childDefectList);
		
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		QiRepairResultDto updateDefectResult = defectResult;
		updateDefectResult.setCurrentDefectStatus((short) 7);
		updateDefectResult.setDefectResultId(120);

		ProductSearchResult.updateDefectsProcessingMap(updateDefectResult);
		List<QiRepairResultDto> defects = ProductSearchResult.getDefectsProcessing();

		assertEquals(3, defects.size());
		assertTrue(defects.contains(defectResult2));
		assertTrue(defects.contains(defectResult3));
		assertTrue(defects.contains(updateDefectResult));	
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectResult(com.honda.galc.entity.qi.QiDefectResult, long)}.
	 * Test for updating the defect repair id when there is no child defect.
	 * productId = LG93291019T 
	 * defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void updateDefectResult_updateDefectResultId() {
		QiDefectResult defectResult = createQiDefectResult("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		defectResult.setActualTimestamp(new Date());
		
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.updateDefectResult(defectResult, 10);
		assertEquals(1, ProductSearchResult.getDefectsProcessing().size());
		assertEquals(0, ProductSearchResult.getDefectsProcessing().get(0).getRepairId());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectResultCache()}.
	 * Test for updating the defect result map by removing all the defects for the products that where processed.
	 * 
	 * productId = LG93291019T LG93291019L
	 * defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void updateDefectResultCache_updateCache() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);
		
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);
		
		ProductSearchResult.updateDefectResultCache();
		assertEquals(0, ProductSearchResult.getDefectsProcessing().size());
		assertEquals(0, ProductSearchResult.getAllDefectResults().size());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date June 02, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectResultCache()}.
	 * Test for updating the defect result map by removing all the defects for the products what where processed.
	 * The defect result map should still have a defect for a product that was not processed.
	 * 
	 * productId = LG93291019T LG93291019L
	 * defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void updateDefectResultCache_updateCacheWithProductNotProcessed() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);
		
		List<QiRepairResultDto> defectResults = new ArrayList<QiRepairResultDto>();
		defectResults.add(defectResult);
		
		ProductSearchResult.setDefectResults(defectResults);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);
		
		ProductSearchResult.updateDefectResultCache();
		assertEquals(0, ProductSearchResult.getDefectsProcessing().size());
		assertEquals(1, ProductSearchResult.getAllDefectResults().size());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#removeChildDefect(long)}.
	 * Test for removing a child defect when the product id does not have any defects assigned.
	 * 
	 */
	@Test(expected = Test.None.class)
	public void removeChildDefect_noDefectsForProductId() {
		ProductSearchResult.removeChildDefect(10);
		assertEquals(0, ProductSearchResult.getDefectsProcessing().size());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#removeChildDefect(long)}.
	 * Test for removing a child defect when the defect has no child defects.
	 * 
	 * productId = LG93291019T
	 * defectTypeName = DEFECT TYPE NAME
	 */
	@Test
	public void removeChildDefect_noChildDefect() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.removeChildDefect(10);
		assertEquals(1, ProductSearchResult.getDefectsProcessing().size());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#removeChildDefect(long)}.
	 * Test for removing a child defect when the defect has child defects.
	 * 
	 * productId = LG93291019T
	 * defectTypeName = DEFECT TYPE NAME
	 * child defectTypeNam = CHILD DEFECT TYPE
	 */
	@Test
	public void removeChildDefect_withChildDefect() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		defectResult.setRepairId(10);
		List<QiRepairResultDto> childDefectList = new ArrayList<QiRepairResultDto>();
		childDefectList.add(createQiRepairResultDto("LG93291019T", "CHILD DEFECT TYPE"));
		defectResult.setChildRepairResultList(childDefectList);
		childDefectList.get(0).setRepairId(10);
		
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.removeChildDefect(10);
		assertEquals(1, ProductSearchResult.getDefectsProcessing().size());
		assertEquals(0, ProductSearchResult.getDefectsProcessing().get(0).getChildRepairResultList().size());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#removeProcessedProducts()}.
	 * test for removing defects when no defects exist in the defects processing map.
	 * productId = LG93291019L
	 */
	@Test(expected = Test.None.class)
	public void removeProcessedProducts_noDefectExist() {
		ProductSearchResult.removeProcessedProducts();
		assertEquals(0, ProductSearchResult.getDefectsProcessing().size());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#removeProcessedProducts()}.
	 * test for removing defects in the defects processing map.
	 *
	 * productId = LG93291019T LG93291019L
	 * defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test(expected = Test.None.class)
	public void removeProcessedProducts_removeDefects() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(124);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(125);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);
		ProductSearchResult.removeProcessedProducts();
		assertEquals(0, ProductSearchResult.getDefectsProcessing().size());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#setDefectResults(java.util.List)}.
	 * Test for adding a new defect to a product
	 * 
	 * productId = LG93291019T
	 * defectTypeName = DEFECT TYPE NAME
	 */
	@Test
	public void setDefectResults_singleProductOneDefect() {
		List<QiRepairResultDto> defectResults = new ArrayList<QiRepairResultDto>();
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResults.add(defectResult);
		ProductSearchResult.setDefectResults(defectResults);
		assertTrue(ProductSearchResult.getAllDefectResults().size() == 1);
		assertTrue(ProductSearchResult.getAllDefectResults().contains(defectResult));	
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#setDefectResults(java.util.List)}.
	 * Test for adding two new defects to a product
	 * 
	 * productId = LG93291019T
	 * defectTypeName = DEFECT TYPE NAME, DEFECT TYPE NAME2
	 */
	@Test
	public void setDefectResults_singleProductMultipleDefect() {
		List<QiRepairResultDto> defectResults = new ArrayList<QiRepairResultDto>();
		QiRepairResultDto defectResult = createQiRepairResultDto(1001,"LG93291019T", "DEFECT TYPE NAME");
		defectResults.add(defectResult);
		QiRepairResultDto defectResult2 = createQiRepairResultDto(1002,"LG93291019T", "DEFECT TYPE NAME2");
		defectResults.add(defectResult2);
		ProductSearchResult.setDefectResults(defectResults);
		assertTrue(ProductSearchResult.getAllDefectResults().size() == 2);
		assertTrue(ProductSearchResult.getAllDefectResults().contains(defectResult));
		assertTrue(ProductSearchResult.getAllDefectResults().contains(defectResult2));	
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#setDefectResults(java.util.List)}.
	 * Test for adding a new defect to a product when the defect is already added.
	 * 
	 * productId = LG93291019T
	 * defectTypeName = DEFECT TYPE NAME
	 */
	@Test
	public void setDefectResults_singleProductSameCommonDefect() {
		List<QiRepairResultDto> defectResults = new ArrayList<QiRepairResultDto>();
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResults.add(defectResult);
		defectResults.add(defectResult);
		ProductSearchResult.setDefectResults(defectResults);
		assertTrue(ProductSearchResult.getAllDefectResults().size() == 1);
		assertTrue(ProductSearchResult.getAllDefectResults().contains(defectResult));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#updateDefectRepairStatusFixed(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * Test for updating the defect status to fixed.
	 * productId = LG93291019T
	 * defectTypeName = DEFECT TYPE NAME
	 */
	@Test
	public void setDefectRepairStatusFixed_updateStatusToFixed() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setCurrentDefectStatus((short)DefectStatus.NOT_REPAIRED.getId());

		ProductSearchResult.updateDefectRepairStatusFixed(defectResult);
		defectResult.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());

		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 1);
		assertTrue(ProductSearchResult.getDefectsProcessing().contains(defectResult));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getDefectsProcessing()}.
	 * Test to get all the defects in the defect processing map when the map is empty
	 */
	@Test
	public void getDefectsProcessing_noDefects() {
		assertTrue(ProductSearchResult.getDefectsProcessing().size() == 0);

	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getAllDefectResults()}.
	 * The for when no defects exist in the defect result map
	 */
	@Test
	public void getAllDefectResults_noDefectExist() {
		assertTrue(ProductSearchResult.getAllDefectResults().size() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getAllDefectResults()}.
	 * The for when no defects exist in the defect result map
	 */
	@Test
	public void getAllDefectResults_defectExist() {
		assertTrue(ProductSearchResult.getAllDefectResults().size() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getCommonDefects(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * The for when no defects exist in the defects processing map
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void getCommonDefects_noDefectsExist() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		List<QiRepairResultDto> defectList = ProductSearchResult.getCommonDefects(defectResult);

		assertTrue(defectList.size() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getCommonDefects(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * The for when parent defects exist in the defects processing map
	 * 
	 *  productId = LG93291019T LG93291019L 
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2
	 */
	@Test
	public void getCommonDefects_defectsExist() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(123);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult3.setDefectResultId(125);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		List<QiRepairResultDto> defectList = ProductSearchResult.getCommonDefects(defectResult3);

		assertTrue(defectList.size() == 2);
		assertTrue(defectList.contains(defectResult));
		assertTrue(defectList.contains(defectResult3));

	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getCommonDefects(com.honda.galc.dto.qi.QiRepairResultDto)}.
	 * Test for when child defects exist in the defects processing map
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2
	 */
	@Test
	public void getCommonDefects_childDefectsExist() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult2.setDefectResultId(124);
		defectResult2.setRepairId(10);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult3.setDefectResultId(125);

		QiRepairResultDto childDefectResult = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		childDefectResult.setDefectResultId(124);
		childDefectResult.setRepairId(10);

		List<QiRepairResultDto> childDefectList = new ArrayList<QiRepairResultDto>();
		childDefectList.add(childDefectResult);
		defectResult3.setChildRepairResultList(childDefectList);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		List<QiRepairResultDto> defectList = ProductSearchResult.getCommonDefects(defectResult);

		assertTrue("Actual : " + defectList.size(), defectList.size() == 2);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getChildDefect(java.lang.String, long)}.
	 * Test for getting child defect for product id and no defects for product id are found.
	 * 
	 * productId = LG93291019T
	 */
	@Test
	public void getChildDefect_noDefectForProduct() {
		QiRepairResultDto childDefect = ProductSearchResult.getChildDefect("LG93291019T", 10);
		assertNull(childDefect);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getChildDefect(java.lang.String, long)}.
	 * Test for getting child defect for product id and no child defects exist for product
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void getChildDefect_noChildDefectForProduct() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult2.setDefectResultId(123);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult2.setDefectResultId(124);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		QiRepairResultDto childDefect = ProductSearchResult.getChildDefect("LG93291019T", 10);
		assertNull(childDefect);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getChildDefect(java.lang.String, long)}.
	 * Test for getting child defect for product id and no child defects exist for product
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2
	 */
	@Test
	public void getChildDefect_childDefectForProduct() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult2.setRepairId(10);
		defectResult2.setDefectResultId(123);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult3.setDefectResultId(124);
		defectResult2.setRepairId(10);
		QiRepairResultDto childDefectResult = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		childDefectResult.setDefectResultId(123);
		childDefectResult.setRepairId(10);

		List<QiRepairResultDto> childDefectList = new ArrayList<QiRepairResultDto>();
		childDefectList.add(childDefectResult);
		defectResult3.setChildRepairResultList(childDefectList);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		QiRepairResultDto childDefect = ProductSearchResult.getChildDefect("LG93291019L", 10);

		assertEquals(childDefect,  childDefectResult);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getDefectResultId(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for when getting the defect result id for a common defect and product id when defects exist in cache but, none for the product id.
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void getDefectResultId_noDefectForProduct() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);

		long defectResultId = ProductSearchResult.getDefectResultId(defectResult, "LG93291019L");
		assertEquals(0,  defectResultId);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getDefectResultId(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for when getting the defect result id for a common defect and product id when 2 defects are assigned to product id and the first defect
	 * is returned
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void getDefectResultId_defectIdReturned() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(123);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(123);
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		long defectResultId = ProductSearchResult.getDefectResultId(defectResult2, "LG93291019L");
		assertEquals(123,  defectResultId);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getDefectResultId(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for when getting the defect result id for a common defect and product id when no defects are applied to product id
	 * 
	 *  productId = LG93291019T LG93291019L LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void getDefectResultId_defectNotAppliedToProduct() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(123);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(123);
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		long defectResultId = ProductSearchResult.getDefectResultId(defectResult, "LG93291019L");
		assertEquals(0,  defectResultId);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 12, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getRepairResultId(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for getting the repair id for a defect, but no defects are assigned to product id.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2
	 */
	@Test
	public void getRepairResultId_noDefectsForProduct() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(120);
		defectResult2.setRepairId(10);
		
		long repairId = ProductSearchResult.getRepairResultId(defectResult, defectResult2, "LG93291019T");
		assertEquals(0, repairId);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 12, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getRepairResultId(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for getting the repair id for a defect, but the child defect does not exist for the product.
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  CHILD DEFECT NAME
	 */
	@Test
	public void getRepairResultId_noCommonDefectFound() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(121);
		
		QiRepairResultDto childDefectResult = createQiRepairResultDto("LG93291019T", "CHILD DEFECT NAME");
		childDefectResult.setDefectResultId(120);
		childDefectResult.setRepairId(10);
		List<QiRepairResultDto> childRepairResultList = new ArrayList<QiRepairResultDto>();
		childRepairResultList.add(childDefectResult);
		defectResult.setChildRepairResultList(childRepairResultList);
		
		ProductSearchResult.addNewDefect(defectResult);
		
		long repairId = ProductSearchResult.getRepairResultId(defectResult2, childDefectResult, "LG93291019T");
		assertEquals(0, repairId);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 12, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getRepairResultId(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for getting the repair id for a defect
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  CHILD DEFECT NAME
	 */
	@Test
	public void getRepairResultId_commonDefectFound() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(121);
		
		QiRepairResultDto childDefectResult = createQiRepairResultDto("LG93291019T", "CHILD DEFECT NAME");
		childDefectResult.setDefectResultId(120);
		childDefectResult.setRepairId(10);
		List<QiRepairResultDto> childRepairResultList = new ArrayList<QiRepairResultDto>();
		childRepairResultList.add(childDefectResult);
		defectResult.setChildRepairResultList(childRepairResultList);
		
		ProductSearchResult.addNewDefect(defectResult);
		
		long repairId = ProductSearchResult.getRepairResultId(defectResult, childDefectResult, "LG93291019T");
		assertEquals(10, repairId);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getParentProductDefect(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for getting parent defect for a product with no defects assigned
	 * 
	 *  productId = LG93291019T
	 *  defectTypeName  DEFECT TYPE NAME
	 */
	@Test
	public void getParentProductDefect_noCommonDefectFound() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto result = ProductSearchResult.getParentProductDefect(defectResult, "LG93291019T");
		assertNull(result);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getParentProductDefect(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for getting parent defect for a product with two defects assigned but no common defect
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void getParentProductDefect_commonDefectNoChildDefect() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setDefectResultId(123);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME3");
		defectResult3.setDefectResultId(123);
		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);
		QiRepairResultDto result = ProductSearchResult.getParentProductDefect(defectResult3, "LG93291019L");

		assertNull(result);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getParentProductDefect(com.honda.galc.dto.qi.QiRepairResultDto, java.lang.String)}.
	 * Test for getting parent defect for a product with two defects assigned
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2  DEFECT TYPE NAME3
	 */
	@Test
	public void getParentProductDefect_commonDefectWithChildDefect() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult2.setRepairId(10);
		defectResult2.setDefectResultId(123);
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		defectResult3.setDefectResultId(124);
		defectResult2.setRepairId(10);
		QiRepairResultDto childDefectResult = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME");
		childDefectResult.setDefectResultId(123);
		childDefectResult.setRepairId(10);

		List<QiRepairResultDto> childDefectList = new ArrayList<QiRepairResultDto>();
		childDefectList.add(childDefectResult);
		defectResult3.setChildRepairResultList(childDefectList);

		ProductSearchResult.addNewDefect(defectResult);
		ProductSearchResult.addNewDefect(defectResult2);
		ProductSearchResult.addNewDefect(defectResult3);

		QiRepairResultDto parentDefect = ProductSearchResult.getParentProductDefect(childDefectResult, "LG93291019L");

		assertEquals(defectResult3, parentDefect);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getDefectResults(java.lang.String)}.
	 * Get a List of all defects in the defect result when no defects exist for the product id;
	 * 
	 * productId = LG93291019L
	 */
	@Test
	public void getDefectResults_noDefectsForProduct() {

		List<QiRepairResultDto> resultList = ProductSearchResult.getDefectResults("LG93291019L");
		assertEquals(new ArrayList<QiRepairResultDto>(), resultList);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#getDefectResults(java.lang.String)}.
	 * Get a List of all defects in the defect result when a defect exist for the product id;
	 * 
	 *  productId = LG93291019T LG93291019L
	 *  defectTypeName  DEFECT TYPE NAME  DEFECT TYPE NAME2
	 */
	@Test
	public void getDefectResults_defectForProduct() {
		QiRepairResultDto defectResult = createQiRepairResultDto("LG93291019T", "DEFECT TYPE NAME");
		defectResult.setDefectResultId(120);
		QiRepairResultDto defectResult2 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult2.setRepairId(121);

		List<QiRepairResultDto> defects = new ArrayList();
		defects.add(defectResult);
		defects.add(defectResult2);
		ProductSearchResult.setDefectResults(defects);


		List<QiRepairResultDto> resultList = ProductSearchResult.getDefectResults("LG93291019L");
		assertTrue(resultList.size() == 1);
		assertTrue(resultList.contains(defectResult2));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 04, 2019
	 * 
	 * Test method for {@link com.honda.galc.client.data.ProductSearchResult#initCommonDefects(java.util.List)}
	 * Test to verify that the defect processing map is cleared before the defects from the
	 * defect result map are populated.
	 * 
	 *  productId = LG93291019T, LG93291019L,LG93291010P
	 *  defectTypeName  DEFECT TYPE NAME, DEFECT TYPE NAME2, DEFECT TYPE NAME3
	 */
	@Test
	public void initCommonDefects_DefectsForSingleProductSearch() {
		QiRepairResultDto defectResult3 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME1");
		defectResult3.setDefectResultId(122);
		QiRepairResultDto defectResult4 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME2");
		defectResult4.setDefectResultId(123);
		QiRepairResultDto defectResult5 = createQiRepairResultDto("LG93291019L", "DEFECT TYPE NAME1");
		defectResult5.setDefectResultId(124);
		
		List<QiRepairResultDto> defectList = new ArrayList<QiRepairResultDto>();

		defectList.add(defectResult3);
		defectList.add(defectResult4);
		defectList.add(defectResult5);


		ProductSearchResult.setDefectResults(defectList);
		
		BaseProduct product1 = createProduct("LG93291019L");

		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		productList.add(product1);

		when(ServiceFactory.getDao(QiRepairResultDao.class)).thenReturn(qiRepairResultDaoMock);
		when(qiRepairResultDaoMock.findAllDefectsByProductIds(Arrays.asList("LG93291019L"))).thenReturn(defectList);
		
		ProductSearchResult.setCurrentWorkingProcessPointId("PP10001");
		QiStationPreviousDefectId qiStationPreviousDefectId = new QiStationPreviousDefectId();
		qiStationPreviousDefectId.setProcessPointId("PP1001");
		qiStationPreviousDefectId.setEntryDivisionId("AF");
		QiStationPreviousDefect qiStationPreviousDefect = new QiStationPreviousDefect();
		qiStationPreviousDefect.setId(qiStationPreviousDefectId);
		List<QiStationPreviousDefect> qiStationPreviousDefectList = new ArrayList<QiStationPreviousDefect>();
		qiStationPreviousDefectList.add(qiStationPreviousDefect);
		when(ServiceFactory.getDao(QiStationPreviousDefectDao.class)).thenReturn(qiStationPreviousDefectDaoMock);
		when(qiStationPreviousDefectDaoMock.findAllByProcessPoint("PP10001")).thenReturn(qiStationPreviousDefectList);
		
		List<QiRepairResultDto> commonMainDefects = ProductSearchResult.initCommonDefects(productList);
		
		assertEquals(3, commonMainDefects.size());
	}

	private QiDefectResult createQiDefectResult(String productId,String defectTypeName) {
		QiDefectResult defect = new QiDefectResult();
		defect.setProductId(productId);
		defect.setDefectTypeName(defectTypeName);

		return defect;
	}

	private QiRepairResultDto createQiRepairResultDto(String productId,String defectTypeName) {
		QiRepairResultDto defect = new QiRepairResultDto();
		defect.setProductId(productId);
		defect.setDefectTypeName(defectTypeName);
		defect.setEntryDept("AF");

		return defect;
	}
	
	private QiRepairResultDto createQiRepairResultDto(long defectResultId, String productId,String defectTypeName) {
		QiRepairResultDto defect = new QiRepairResultDto();
		defect.setDefectResultId(defectResultId);
		defect.setProductId(productId);
		defect.setDefectTypeName(defectTypeName);
		defect.setEntryDept("AF");

		return defect;
	}
	
	private BaseProduct createProduct(String productId) {
		return ProductTypeUtil.createProduct("BLOCK", productId);
	}
}