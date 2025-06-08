package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.qi.QiImageSectionPointDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectEntryModelTest</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Nov 1, 2018
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceFactory.class)
public class DefectEntryModelTest {

	public static final String FRAME = "FRAME";
	public static final String AUTOMOBILE = "AUTOMOBILE";
	public static final String PROCESS_POINT_ID = "TEST_PP_ID";
	public static final String MTOC1 = "MTOC";
	public static final String ENTRY_SCREEN_NAME = "ENTRY_SCREEN1";
	public static final String IMAGE_NAME = "IMAGE_NAME1";
	public static final String ENTRY_MODEL_NAME = "ENTRY_MODEL1";

	@Mock
	public ProductPropertyBean property = Mockito.mock(ProductPropertyBean.class);

	@Mock
	public ProcessPointDao processPointDao = Mockito.mock(ProcessPointDao.class);

	@Mock
	QiLocalDefectCombinationDao qiLocalDefectCombinationDao = Mockito.mock(QiLocalDefectCombinationDao.class);

	@Mock
	QiImageSectionPointDao qiImageSectionPointDao = Mockito.mock(QiImageSectionPointDao.class);

	public DefectEntryModel model;

	@BeforeClass
	public static void setUpClass() {
		ClientMainFx client = new ClientMainFx();
		client.currentApplicationId = PROCESS_POINT_ID;
	}

	@Before
	public void setUp() {
		// === mock properties === //
		Mockito.when(getProperty().getCacheSizeMem()).thenReturn(5);
		Mockito.when(getProperty().getCacheSizeDisk()).thenReturn(10);
		Mockito.when(getProperty().getCacheTimeToLive()).thenReturn(0);
		Mockito.when(getProperty().getCacheTimeToIdle()).thenReturn(21600);

		// === mock static calls === //
		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.when(ServiceFactory.getDao(ProcessPointDao.class)).thenReturn(processPointDao);
		PowerMockito.when(ServiceFactory.getDao(QiLocalDefectCombinationDao.class)).thenReturn(qiLocalDefectCombinationDao);
		PowerMockito.when(ServiceFactory.getDao(QiImageSectionPointDao.class)).thenReturn(qiImageSectionPointDao);

		// === mock dao api === //
		Mockito.when(qiLocalDefectCombinationDao.findAllPartDefectCombByDefectEntryTextFilter(ENTRY_SCREEN_NAME, ENTRY_MODEL_NAME, null, null)).thenReturn(getPdcList1());

		Mockito.when(qiLocalDefectCombinationDao.findAllPartDefectCombByDefectEntryImageFilter(IMAGE_NAME, ENTRY_SCREEN_NAME, ENTRY_MODEL_NAME, null, null)).thenReturn(getPdcList1());

		Mockito.when(qiImageSectionPointDao.findAllByDefectFilter(AUTOMOBILE, ENTRY_SCREEN_NAME, IMAGE_NAME, MTOC1, PROCESS_POINT_ID, FRAME, null)).thenReturn(getImageSectionPoints1());

		model = new DefectEntryModel() {
			public String getMtcModel() {
				return MTOC1;
			}

			public String getInspectionPartName() {
				return null;
			}

			public String getProductType() {
				return FRAME;
			}

			public String getCurrentWorkingProcessPointId() {
				return PROCESS_POINT_ID;
			}

			public String getProductKind() {
				return AUTOMOBILE;
			}

			public ProductModel getProductModel() {
				return new ProductModel();
			}
		};
	}

	/**
	 * @author VCC56462, HNA
	 * date Nov 1, 2018
	 * Tests accessing PDC Text data from empty cache 
	 */
	@Test
	public void testGeneratePartDefectCombCache_textEmptyCache() {

		QiDefectEntryDto entryScreen = new QiDefectEntryDto();
		entryScreen.setEntryScreen(ENTRY_SCREEN_NAME);
		entryScreen.setImageName(IMAGE_NAME);
		entryScreen.setIsImage((short) 0);

		getModel().getDefectEntryCacheUtil().clearCache();

		getModel().generatePartDefectCombCache(entryScreen, entryScreen.isImage(), false, false, ENTRY_MODEL_NAME);
			
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getSize());
		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getImageSectionCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getSize());
		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getImageSectionClientCache().getSize());

		getModel().getDefectEntryCacheUtil().clearCache();
	}

	/**
	 * @author VCC56462, HNA
	 * date Nov 1, 2018
	 * Tests accessing PDC Text data from cache
	 */
	@Test
	public void testGeneratePartDefectCombCache_textDataOnCache() {

		QiDefectEntryDto entryScreen = new QiDefectEntryDto();
		entryScreen.setEntryScreen(ENTRY_SCREEN_NAME);
		entryScreen.setImageName(IMAGE_NAME);
		entryScreen.setIsImage((short) 0);

		getModel().getDefectEntryCacheUtil().clearCache();
		String pdcKey = getModel().getCachePdcKey(entryScreen.getEntryScreen(), null);
		getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().put(pdcKey, getPdcList1());
		Mockito.when(qiLocalDefectCombinationDao.findAllPartDefectCombByDefectEntryTextFilter(ENTRY_SCREEN_NAME, ENTRY_MODEL_NAME, null, null)).thenReturn(getPdcList0());

		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getSize());
		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getImageSectionCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getSize());
		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getImageSectionClientCache().getSize());

		getModel().generatePartDefectCombCache(entryScreen, entryScreen.isImage(), false, false, ENTRY_MODEL_NAME);

		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getSize());
		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getImageSectionCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getSize());
		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getImageSectionClientCache().getSize());

		List<QiDefectResultDto> clientList = getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getList(pdcKey, QiDefectResultDto.class);
		List<QiDefectResultDto> productList = getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getList(entryScreen.getEntryScreen(), QiDefectResultDto.class);

		Assert.assertEquals(1, clientList.size());
		Assert.assertEquals(1, productList.size());

		getModel().getDefectEntryCacheUtil().clearCache();
	}

	/**
	 * @author VCC56462, HNA
	 * date Nov 1, 2018
	 * Tests accessing PDC Img data from empty cache.
	 */
	@Test
	public void testGeneratePartDefectCombCache_imgEmptyCache() {

		QiDefectEntryDto entryScreen = new QiDefectEntryDto();
		entryScreen.setEntryScreen(ENTRY_SCREEN_NAME);
		entryScreen.setImageName(IMAGE_NAME);
		entryScreen.setIsImage((short) 1);

		getModel().getDefectEntryCacheUtil().clearCache();

		getModel().generatePartDefectCombCache(entryScreen, entryScreen.isImage(), false, false, ENTRY_MODEL_NAME);

		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getImageSectionCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getImageSectionClientCache().getSize());

		getModel().getDefectEntryCacheUtil().clearProductCache();

		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getSize());
		Assert.assertEquals(0, getModel().getDefectEntryCacheUtil().getImageSectionCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getImageSectionClientCache().getSize());

		getModel().getDefectEntryCacheUtil().clearCache();
	}

	/**
	 * @author VCC56462, HNA
	 * date Nov 1, 2018
	 * Tests accessing PDC Img data from cache.
	 */
	@Test
	public void testGeneratePartDefectCombCache_imgDataOnCache() {

		QiDefectEntryDto entryScreen = new QiDefectEntryDto();
		entryScreen.setEntryScreen(ENTRY_SCREEN_NAME);
		entryScreen.setImageName(IMAGE_NAME);
		entryScreen.setIsImage((short) 1);

		getModel().getDefectEntryCacheUtil().clearCache();

		String pdcKey = getModel().getCachePdcKey(entryScreen.getEntryScreen(), null);
		String imgKey = getModel().getCacheImageSectionKey(entryScreen.getEntryScreen(), entryScreen.getImageName(), null);

		getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().put(pdcKey, getPdcList1());
		getModel().getDefectEntryCacheUtil().getImageSectionClientCache().put(imgKey, getImageSectionPoints1());

		Mockito.when(qiLocalDefectCombinationDao.findAllPartDefectCombByDefectEntryImageFilter(IMAGE_NAME, ENTRY_SCREEN_NAME, ENTRY_MODEL_NAME, null, null)).thenReturn(getPdcList0());

		Mockito.when(qiImageSectionPointDao.findAllByDefectFilter(AUTOMOBILE, ENTRY_SCREEN_NAME, IMAGE_NAME, MTOC1, PROCESS_POINT_ID, FRAME, null)).thenReturn(getImageSectionPoints0());

		getModel().generatePartDefectCombCache(entryScreen, entryScreen.isImage(), false, false, ENTRY_MODEL_NAME);

		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getImageSectionCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getSize());
		Assert.assertEquals(1, getModel().getDefectEntryCacheUtil().getImageSectionClientCache().getSize());

		List<QiDefectResultDto> pdcClientList = getModel().getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getList(pdcKey, QiDefectResultDto.class);
		List<QiDefectResultDto> pdcProductList = getModel().getDefectEntryCacheUtil().getPartDefectCombCache().getList(entryScreen.getEntryScreen(), QiDefectResultDto.class);

		List<QiImageSectionPoint> sectionClientList = getModel().getDefectEntryCacheUtil().getImageSectionClientCache().getList(imgKey, QiImageSectionPoint.class);
		List<QiImageSectionPoint> sectionProductList = getModel().getDefectEntryCacheUtil().getImageSectionCache().getList(entryScreen.getEntryScreen(), QiImageSectionPoint.class);

		Assert.assertEquals(1, pdcClientList.size());
		Assert.assertEquals(1, pdcProductList.size());
		Assert.assertEquals(1, sectionClientList.size());
		Assert.assertEquals(1, sectionProductList.size());

		getModel().getDefectEntryCacheUtil().clearCache();
	}

	protected ProductPropertyBean getProperty() {
		return property;
	}

	protected DefectEntryModel getModel() {
		return model;
	}

	// === data providers === //
	public static List<QiDefectResultDto> getPdcList0() {
		List<QiDefectResultDto> list = new ArrayList<QiDefectResultDto>();
		return list;
	}

	public static List<QiDefectResultDto> getPdcList1() {
		List<QiDefectResultDto> list = new ArrayList<QiDefectResultDto>();
		list.add(new QiDefectResultDto());
		return list;
	}

	public static List<QiImageSectionPoint> getImageSectionPoints0() {
		List<QiImageSectionPoint> list = new ArrayList<QiImageSectionPoint>();
		return list;
	}

	public static List<QiImageSectionPoint> getImageSectionPoints1() {
		List<QiImageSectionPoint> list = new ArrayList<QiImageSectionPoint>();
		list.add(null);
		return list;
	}
}
