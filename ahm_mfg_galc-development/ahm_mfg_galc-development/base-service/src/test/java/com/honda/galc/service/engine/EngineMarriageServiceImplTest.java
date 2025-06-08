package com.honda.galc.service.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.ProductAttributeDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductAttributeId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.common.ProductMarriageImpl;
import com.honda.galc.service.engine.EngineMarriageServiceImpl;
import com.honda.galc.service.utils.EngineUtil;
import com.honda.galc.service.utils.ServiceUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceFactory.class)
public class EngineMarriageServiceImplTest {

	private static final String MISSION = "MISSION";
	private static final String MISSION_TYPE = "MISSION_TYPE";
	private static final String ENGINE = "ENGINE";
	private static final String FRAME = "FRAME";

	public Engine engine = new Engine();
	public Frame frame = new Frame();

	public EngineMarriageServiceTestSetup setupTest = new EngineMarriageServiceTestSetup();
	public InstalledPart installedPart = new InstalledPart();
	public List<InstalledPart> installedPartList = new ArrayList<InstalledPart>();

	@Mock
	public ProductMarriageImpl productMarriageMock = Mockito.mock(ProductMarriageImpl.class);

	@Mock
	public EngineUtil engineUtilMock = Mockito.mock(EngineUtil.class);
	
	@Mock
	public ServiceUtil serviceUtil = Mockito.mock(ServiceUtil.class);
	
	@Mock
	PartNameDao partNameDaoMock;			


	@InjectMocks
	public EngineMarriageServiceImpl engineMarriageService = new EngineMarriageServiceImpl();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);	
		PowerMockito.mockStatic(ServiceFactory.class);  
		partNameDaoMock =  PowerMockito.mock(PartNameDao.class);
		when(ServiceFactory.getDao(PartNameDao.class)).thenReturn(partNameDaoMock);
		when(partNameDaoMock.findByKey((String)Matchers.any())).thenReturn(setupTest.initMissionPart());

		Mockito.when(productMarriageMock.getProduct("J35Y71056715", ENGINE)).thenReturn(engine);
		Mockito.when(productMarriageMock.getProduct("5FNRL6H20JB057874", FRAME)).thenReturn(frame);

		Mockito.when(engineUtilMock.getEnginePartName()).thenReturn(ENGINE);

		installedPartList = new ArrayList<InstalledPart>();
	}

	//*************************************//
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission type to an engine
	 * when the engine is not associated with a mission or frame.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION} 
	 */
	@Test()
	public void init_marryMissionTypeNoMissionNoFrame() {
		engine = setupTest.initEngine(new Engine());
		installedPartList.add(setupTest.setInstalledPartWithMissionTypeGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION_TYPE);
		Mockito.verify(productMarriageMock, Mockito.times(1)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission type to an engine 
	 * when the engine is not associated with a mission but is associate to a frame.
	 * The Mission Type status is NG.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}{Frame=5FNRL6H20JB057874} 
	 */
	@Test()
	public void init_marryMissionTypeNoMissionWithFrameNG() {
		engine = setupTest.setEngineWithFrame(engine);
		frame = setupTest.setFrameWithEngine(frame);
		installedPartList.add(setupTest.setInstalledPartWithMissionTypeNG(new InstalledPart()));

		engineMarriageService.init(installedPartList, MISSION_TYPE);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission type to an engine 
	 * when the engine is not associated with a mission but is associate to a frame.
	 * THe mission type has a good status.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}{Frame=5FNRL6H20JB057874} 
	 */
	@Test()
	public void init_marryMissionTypeNoMissionWithFrameGood() {
		engine = setupTest.setEngineWithFrame(engine);
		frame = setupTest.setFrameWithEngine(frame);
		installedPartList.add(setupTest.setInstalledPartWithMissionTypeGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION_TYPE);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission type to an engine
	 * when the engine is associated with a mission but not associate to a frame.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION} 
	 */
	@Test()
	public void init_marryMissionTypeWithMissionNoFrame() {
		engine = setupTest.setEngineWithMission(engine);
		installedPartList.add(setupTest.setInstalledPartWithMissionTypeGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION_TYPE);
		Mockito.verify(productMarriageMock, Mockito.times(1)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission type to an engine
	 * when the engine is associated with a mission and frame.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}{Frame=5FNRL6H20JB057874}(Mission=Q5NZ0263739}
	 */
	@Test()
	public void init_marryMissionTypeWithMissionAndFrameGood() {
		engine = setupTest.setEngineWithFrameMission(engine);
		frame = setupTest.setFrameWithMission(frame);
		installedPartList.add(setupTest.setInstalledPartWithMissionTypeGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION_TYPE);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission to an engine
	 * when the engine is not associated to a mission or frame.
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739}
	 */
	@Test()
	public void init_marryMissionNoMissionTypeNoFrame() {
		engine = setupTest.initEngine(engine);
		installedPartList.add(setupTest.setInstalledPartWithMissionGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION);
		Mockito.verify(productMarriageMock, Mockito.times(1)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission to an engine
	 * when the engine is not associated to a mission and is  associated to a frame.
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739}{Frame=5FNRL6H20JB057874}
	 */
	@Test()
	public void init_marryMissionNoMissionTypeWithFrame() {
		engine = setupTest.setEngineWithFrame(engine);
		frame = setupTest.setFrameWithEngine(frame);
		installedPartList.add(setupTest.setInstalledPartWithMissionGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission to an engine
	 * when the engine is associated to a mission and is not associated to a frame.
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test()
	public void init_marryMissionWithMissionTypeNoFrame() {
		engine = setupTest.setEngineWithMissionType(engine);
		installedPartList.add(setupTest.setInstalledPartWithMissionGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION);
		Mockito.verify(productMarriageMock, Mockito.times(1)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the mission to an engine
	 * when the engine is associated to a mission and associated to a frame.
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739}{Mission type SN=Q5NZMISSION}{Frame=5FNRL6H20JB057874}
	 */
	@Test()
	public void init_marryMissionWithMissionTypeWithFrame() {
		engine = setupTest.setEngineWithFrameMissionTypeGood(engine);
		frame = setupTest.setFrameWithMissionType(frame);
		installedPartList.add(setupTest.setInstalledPartWithMissionGood(installedPart));

		engineMarriageService.init(installedPartList, MISSION);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the frame to an engine
	 * when the engine is not associated to a mission type and not associated to a mission.
	 * {Engine SN=J35Y71056715}{Frame=5FNRL6H20JB057874}
	 */
	@Test()
	public void init_marryFrameNoMissionTypeNoMission() {
		engine = setupTest.setEngineWithFrame(engine);
		frame = setupTest.setFrameWithEngine(frame);
		installedPartList.add(setupTest.setInstalledPartWithEngineGood(installedPart));

		engineMarriageService.init(installedPartList, ENGINE);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the frame to an engine
	 * when the engine is associated to a mission type and not associated to a mission.
	 * {Engine SN=J35Y71056715}{Frame=5FNRL6H20JB057874}{Mission type SN=Q5NZMISSION}
	 */
	@Test()
	public void init_marryFrameWithMissionTypeNoMission() {
		engine = setupTest.setEngineWithFrameMissionTypeGood(engine);
		frame = setupTest.setFrameWithMissionType(frame);
		installedPartList.add(setupTest.setInstalledPartWithEngineGood(installedPart));

		engineMarriageService.init(installedPartList, ENGINE);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jul 29, 2018
	 * Test the init method when marrying the frame to an engine
	 * when the engine is not associated to a mission type and is associated to a mission.
	 * {Engine SN=J35Y71056715}{Frame=5FNRL6H20JB057874}{Mission type SN=Q5NZMISSION}{Mission SN=Q5NZ0263739}
	 */
	@Test()
	public void init_marryFrameNoMissionTypeWithMission() {
		engine = setupTest.setEngineWithFrameMission(engine);
		frame = setupTest.setFrameWithMission(frame);
		installedPartList.add(setupTest.setInstalledPartWithEngineGood(installedPart));

		engineMarriageService.init(installedPartList, ENGINE);
		Mockito.verify(productMarriageMock, Mockito.times(2)).getProduct(any(String.class), any(String.class));
	}
	
	//*************************************//

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMissionType method when marrying the mission type to an engine
	 * when the engine is not associated with a mission, engine and frame.
	 * {Engine SN = J35Y71056715}{Mission type SN = Q5NZMISSION} 
	 */
	@Test()
	public void assignMissionType_marryMissionTypeNoMissionNoFrame() throws Exception {
		installedPart = setupTest.setInstalledPartWithMissionTypeNG(new InstalledPart());
		engine = setupTest.initEngine(engine);
		installedPartList.add(installedPart);
		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);

		engineMarriageService.assignMissionType(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZMISSION", engine.getActualMissionType());
		assertEquals(0, engine.getMissionStatus());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMissionType method when marrying the mission type to an engine 
	 * when the engine is not associated with a mission but is associate to a frame.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}{Frame=5FNRL6H20JB057874} 
	 */
	@Test()
	public void assignMissionType_marryMissionTypeNoMissionWithFrame() throws Exception {
		installedPart = setupTest.setInstalledPartWithMissionTypeNG(new InstalledPart());
		frame = setupTest.setFrameWithEngine(frame);
		engine = setupTest.setEngineWithFrame(engine);
		installedPartList.add(installedPart);
		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);

		engineMarriageService.assignMissionType(installedPartList, "PPID", "APPID");

		assertEquals("Q5NZMISSION", engine.getActualMissionType());
		assertEquals("Q5NZMISSION", frame.getActualMissionType());
		assertTrue(engine.getMissionStatus() == 0);
		assertTrue(frame.getEngineStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMissionType method when marrying the mission type to an engine
	 * when the engine is associated with a mission but not associate to a frame.
	 *{Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION} 
	 */
	@Test()
	public void assignMissionType_marryMissionTypeWithMissionNoFrame() throws Exception {
		engine = setupTest.setEngineWithMission(engine);
		installedPart = setupTest.setInstalledPartWithMissionTypeNG(new InstalledPart());
		installedPartList.add(installedPart);
		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(true);

		engineMarriageService.assignMissionType(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZMISSION",engine.getActualMissionType());
		assertTrue(engine.getMissionStatus() == 1);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMissionType method when marrying the mission type to an engine
	 * when the engine is associated with a mission and frame where mission type status in NG.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}{Frame=5FNRL6H20JB057874}(Mission=Q5NZ0263739}
	 */
	@Test()
	public void assignMissionType_marryMissionTypeWithMissionAndFrameNG() throws Exception {
		engine = setupTest.setEngineWithFrameMission(engine);
		frame = setupTest.setFrameWithMission(frame);
		installedPart = setupTest.setInstalledPartWithMissionTypeNG(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		
		engineMarriageService.assignMissionType(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZMISSION",engine.getActualMissionType());
		assertEquals("Q5NZMISSION",frame.getActualMissionType());
		assertTrue(engine.getMissionStatus() == 0);
		assertTrue(frame.getEngineStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMissionType method when marrying the mission type to an engine
	 * when the engine is associated with a mission and frame where mission type status in good.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}{Frame=5FNRL6H20JB057874}(Mission=Q5NZ0263739}
	 */
	@Test()
	public void assignMissionType_marryMissionTypeWithMissionAndFrameGood() throws Exception {
		engine = setupTest.setEngineWithFrameMission(engine);
		frame = setupTest.setFrameWithMission(frame);
		installedPart = setupTest.setInstalledPartWithMissionTypeGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(true);
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(true);

		engineMarriageService.assignMissionType(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZMISSION",engine.getActualMissionType());
		assertEquals("Q5NZMISSION",frame.getActualMissionType());
		assertTrue(engine.getMissionStatus() == 1);
		assertTrue(frame.getEngineStatus() == 1);
	}
	
	//*************************************//

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMission method when marrying the mission to an engine
	 * when the engine is not associated with a mission type and engine is not associated with a frame.
	 * {Engine SN = J35Y71056715}{Mission SN =Q5NZ0263739} 
	 */
	@Test()
	public void assignMission_marryMissionNoMissionNoFrame() throws Exception {
		engine = setupTest.initEngine(engine);
		installedPart = setupTest.setInstalledPartWithMissionGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);

		engineMarriageService.assignMission(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZ0263739",engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMission method when marrying the mission to an engine 
	 * when the engine is not associated with a mission type but is associate to a frame.
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739}{Frame=5FNRL6H20JB057874} 
	 */
	@Test()
	public void assigneMission_marryMissionNoMissionTypeWithFrame() throws Exception {
		engine = setupTest.setEngineWithFrame(engine);
		frame = setupTest.setFrameWithEngine(frame);
		installedPart = setupTest.setInstalledPartWithMissionGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);

		
		engineMarriageService.assignMission(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZ0263739",engine.getMissionSerialNo());
		assertEquals("Q5NZ0263739",frame.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
		assertTrue(frame.getEngineStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMission method when marrying the mission to an engine
	 * when the engine is associated with the mission but not associate to a frame.
	 * THe mission has a NG status
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739} 
	 */
	@Test()
	public void assigneMission_marryMissionWithMissionTypeNoFrameNG() throws Exception {
		engine = setupTest.setEngineWithMissionType(engine);
		installedPart = setupTest.setInstalledPartWithMissionNG(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		
		engineMarriageService.assignMission(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZ0263739",engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMission method when marrying the mission to an engine
	 * when the engine is associated with the mission but not associate to a frame.
	 * THe mission has a Good status
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739} 
	 */
	@Test()
	public void assignMission_marryMissionWithMissionTypeNoFrameGood() throws Exception {
		engine = setupTest.setEngineWithMissionType(engine);
		installedPart = setupTest.setInstalledPartWithMissionGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(true);
		
		engineMarriageService.assignMission(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZ0263739",engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 1);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMission method when marrying the mission to an engine
	 * when the engine is associated with a mission type and frame. With NG mission.
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739}{Frame=5FNRL6H20JB057874}(Mission Type =Q5NZMISSION}
	 */
	@Test()
	public void assignMission_marryMissionWithMissionTypeAndFrameNG() throws Exception {
		engine = setupTest.setEngineWithFrameMission(engine);
		frame = setupTest.setFrameWithMissionType(frame);
		installedPart = setupTest.setInstalledPartWithMissionNG(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		
		engineMarriageService.assignMission(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZ0263739",engine.getMissionSerialNo());
		assertEquals("Q5NZ0263739",frame.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
		assertTrue(frame.getEngineStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateMission method when marrying the mission to an engine
	 * when the engine is associated with a mission type and frame. With Good mission.
	 * {Engine SN=J35Y71056715}{Mission SN=Q5NZ0263739}{Frame=5FNRL6H20JB057874}(Mission Type=Q5NZMISSION}
	 */
	@Test()
	public void assignMission_marryMissionWithMissionTypeAndFrameGood() throws Exception {
		engine = setupTest.setEngineWithFrameMissionTypeGood(engine);
		frame = setupTest.setFrameWithMissionType(frame);
		installedPart = setupTest.setInstalledPartWithMissionGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);
		Mockito.when(engineUtilMock.determineMissionStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(true);
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(true);

		engineMarriageService.assignMission(installedPartList, "PPID", "APPID");
		assertEquals("Q5NZ0263739",engine.getMissionSerialNo());
		assertEquals("Q5NZ0263739",frame.getMissionSerialNo());

		assertTrue(engine.getMissionStatus() == 1);
		assertTrue(frame.getEngineStatus() == 1);
	}
	
	//*************************************//

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateMission method when deassigning the mission type from an engine
	 * when the engine is not associated with a mission and engine is not associated with a frame.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION} 
	 */
	@Test()
	public void deassigneMissionType_deassociateMissionTypeNoMissionNoFrame() throws Exception {
		engine = setupTest.setEngineWithMissionType(engine);
		installedPart = setupTest.removeInstalledPartWithMissionType(installedPart);
		installedPart.setPartName(MISSION_TYPE);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);

		engineMarriageService.deassignMissionType(installedPartList, "PPID", "APPID");
		assertNull(engine.getActualMissionType());
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateMission method when deassigning the mission from an engine
	 * when the engine is not associated with a mission type and engine is not associated with a frame.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION} 
	 */
	@Test()
	public void deassignMission_deassociateMissionMissionNoMissionTypeNoFrame() throws Exception {
		engine = setupTest.setEngineWithMission(engine);
		installedPart = setupTest.removeInstalledPartWithMission(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);
		
		engineMarriageService.deassignMission(installedPartList, "PPID", "APPID");	
		
		assertNull(engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateMission method when deassigning the mission type and the mission from an engine
	 * when the engine is not associated with a frame.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION}{Mission SN=Q5NZ0263739}
	 */
	@Test()
	public void deassignMissionType_deassociateMissionTypeWithMissionNoFrame() throws Exception {
		engine = setupTest.setEngineWithMissionTypeGood(engine);
		installedPart = setupTest.removeInstalledPartWithMissionType(installedPart);
		installedPartList.add(installedPart);
		installedPart = new InstalledPart();
		installedPart = setupTest.removeInstalledPartWithMissionType(installedPart);
		installedPart.setPartName(MISSION_TYPE);
		installedPartList.add(installedPart);

		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);

		engineMarriageService.deassignMissionType(installedPartList, "PPID", "APPID");
		
		assertNull(engine.getActualMissionType());
		assertNull(engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateMission method when deassigning the mission  and the mission type from an engine
	 * when the engine is not associated with a frame.
	 * Currently this is the same as deassigning a mission type since both the mission and 
	 * mission type are removed together.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION}{Mission SN=Q5NZ0263739}
	 */
	@Test()
	public void deassigneMission_deassociateMissionWithMissionTypeNoFrame() throws Exception {
		engine = setupTest.setEngineWithMissionTypeGood(engine);
		installedPart = setupTest.removeInstalledPartWithMissionType(installedPart);
		installedPartList.add(installedPart);
		installedPart = new InstalledPart();
		installedPart = setupTest.removeInstalledPartWithMission(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);

		engineMarriageService.deassignMission(installedPartList, "PPID", "APPID");
		
		assertNull(engine.getActualMissionType());
		assertNull(engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateMission method when deassigning the mission type and the mission from an engine
	 * when the engine is associated with a frame.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION}{Mission SN=Q5NZ0263739}{Frame=5FNRL6H20JB057874} 
	 */
	@Test()
	public void deasignMissionType_deassociateMissionTypeWithMissionWithFrame() throws Exception {
		engine = setupTest.setEngineWithFrameMissionTypeGood(engine);
		frame = setupTest.setFrameGoodEngine(frame);
		installedPart = setupTest.removeInstalledPartWithMission(installedPart);
		installedPartList.add(installedPart);
		installedPart = new InstalledPart();
		installedPart = setupTest.removeInstalledPartWithMissionType(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.getMissionTypePartName()).thenReturn(MISSION_TYPE);

		engineMarriageService.deassignMissionType(installedPartList, "PPID", "APPID");
		assertNull(engine.getActualMissionType());
		assertNull(engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
		assertTrue(engine.getMissionStatus() == 0);
		assertNull(frame.getActualMissionType());
		assertNull(frame.getMissionSerialNo());
		assertTrue(frame.getEngineStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateMission method when deassigning the mission type and the mission from an engine
	 * when the engine is associated with a frame.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION}{Mission SN=Q5NZ0263739}{Frame=5FNRL6H20JB057874} 
	 */
	@Test
	public void deassignMission_deassociateMissionWithMissionWithTypeFrame() throws Exception {
		engine = setupTest.setEngineWithFrameMissionTypeGood(engine);
		frame = setupTest.setFrameGoodEngine(frame);
		installedPart = setupTest.removeInstalledPartWithMissionType(installedPart);
		installedPartList.add(installedPart);
		installedPart = new InstalledPart();
		installedPart = setupTest.removeInstalledPartWithMission(installedPart);
		installedPartList.add(installedPart);

		Mockito.when(engineUtilMock.getMissionPartName()).thenReturn(MISSION);

		engineMarriageService.deassignMission(installedPartList, "PPID", "APPID");
		assertNull(engine.getActualMissionType());
		assertNull(engine.getMissionSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
		assertTrue(engine.getMissionStatus() == 0);
		assertNull(frame.getActualMissionType());
		assertNull(frame.getMissionSerialNo());
		assertTrue(frame.getEngineStatus() == 0);
		assertTrue(frame.getEngineStatus() == 0);
	}
	
	//*************************************//


	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateEngineAndFrame method when assigning the engine to the frame
	 * when the engine is not associated with a mission type and engine is not associated with a mission.
	 * {Engine SN = J35Y71056715}{Frame=5FNRL6H20JB057874}
	 */
	@Test
	public void assignEngineAndFrame_associateEngentNoMissionTypeNoMission() throws Exception {
		when( partNameDaoMock.findByKey((String)Matchers.any())).thenReturn(setupTest.initEnginePart());
		engine = setupTest.initEngine(engine);
		frame = setupTest.initFrame(frame);
		installedPart = setupTest.setInstalledPartWithFrameGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);
		Mockito.when(engineUtilMock.updateEngineSpec(any(Frame.class),any(Engine.class))).thenReturn(engine);

		engineMarriageService.assignEngineAndFrame(installedPartList);
		assertEquals("5FNRL6H20JB057874", engine.getVin());
		assertEquals("J35Y71056715", frame.getEngineSerialNo());
		assertTrue(frame.getEngineStatus() == 0);
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateEngineAndFrame method when assigning the engine to the frame
	 * when the engine is associated with a mission type and engine is not associated with a mission.
	 * {Engine SN = J35Y71056715}{Frame=5FNRL6H20JB057874}{Mission type SN =Q5NZMISSION}
	 */
	@Test
	public void assignEngineAndFrame_associateEngentWithMissionTypeWithMission() throws Exception {
		when( partNameDaoMock.findByKey((String)Matchers.any())).thenReturn(setupTest.initEnginePart());

		engine = setupTest.setEngineWithFrameMissionTypeGood(engine);
		frame = setupTest.initFrame(frame);
		installedPart = setupTest.setInstalledPartWithFrameGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(true);
		Mockito.when(engineUtilMock.updateEngineSpec(any(Frame.class),any(Engine.class))).thenReturn(engine);

		engineMarriageService.assignEngineAndFrame(installedPartList);
		assertEquals("5FNRL6H20JB057874", engine.getVin());
		assertEquals("J35Y71056715", frame.getEngineSerialNo());
		assertEquals("Q5NZMISSION", frame.getActualMissionType());
		assertEquals("Q5NZ0263739", frame.getMissionSerialNo());
		assertTrue(frame.getEngineStatus() == 1);
		assertTrue(engine.getMissionStatus() == 1);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the associateEngineAndFrame method when assigning the engine to the frame
	 * when the engine is associated with a mission type and engine is associated with a mission.
	 * {Engine SN = J35Y71056715}{Frame=5FNRL6H20JB057874}{Mission type SN =Q5NZMISSION}
	 */
	@Test
	public void assignEngineAndFrame_associateEngentWithMissionTypeNoMission() throws Exception {
		when(partNameDaoMock.findByKey((String)Matchers.any())).thenReturn(setupTest.initEnginePart());

		engine = setupTest.setEngineWithFrameMissionType(engine);
		frame = setupTest.initFrame(frame);
		installedPart = setupTest.setInstalledPartWithFrameGood(installedPart);
		installedPartList.add(installedPart);
		
		Mockito.when(engineUtilMock.updateEngineSpec(any(Frame.class),any(Engine.class))).thenReturn(engine);
		Mockito.when(engineUtilMock.determineEngineStatus(any(Engine.class), any(String.class), any(InstalledPartStatus.class))).thenReturn(false);

		engineMarriageService.assignEngineAndFrame(installedPartList);
		assertEquals("5FNRL6H20JB057874", engine.getVin());
		assertEquals("J35Y71056715", frame.getEngineSerialNo());
		assertEquals("Q5NZMISSION", frame.getActualMissionType());
		assertTrue(frame.getEngineStatus() == 0);
		assertTrue(engine.getMissionStatus() == 0);
	}
	
	//*************************************//

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateEngineAndFrame method when deassigning the engine from the frame
	 * when the engine is not associated with a mission type and engine is not associated with a mission.
	 * {Engine SN = J35Y71056715}{Frame=5FNRL6H20JB057874}
	 */
	@Test
	public void deassignEngineAndFrame_deassociateEngentNoMissionTypeNoMission() throws Exception {
		when( partNameDaoMock.findByKey((String)Matchers.any())).thenReturn(setupTest.initEnginePart());

		engine = setupTest.setEngineWithFrame(engine);
		frame = setupTest.setFrameWithEngine(frame);
		installedPart = setupTest.removeEngineFromFrame(installedPart);
		installedPartList.add(installedPart);

		engineMarriageService.deassignEngineAndFrame(installedPartList, "PPID", "APPID");
		assertNull(engine.getVin());
		assertNull(frame.getEngineSerialNo());
		assertTrue(engine.getMissionStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateEngineAndFrame method when deassigning the engine from the frame
	 * when the engine is not associated with a mission type and engine is not associated with a mission.
	 * {Engine SN = J35Y71056715}{Frame=5FNRL6H20JB057874}{Mission type SN =Q5NZMISSION}
	 */
	@Test
	public void deassignEngineAndFrame_deassociateEngentWithMissionTypeNoMission() throws Exception {
		when( partNameDaoMock.findByKey((String)Matchers.any())).thenReturn(setupTest.initEnginePart());

		engine = setupTest.setEngineWithFrameMissionTypeNG(engine);
		frame = setupTest.setFrameWithMissionType(frame);
		installedPart = setupTest.removeEngineFromFrame(installedPart);
		installedPartList.add(installedPart);

		engineMarriageService.deassignEngineAndFrame(installedPartList, "PPID", "APPID");
		assertNull(engine.getVin());
		assertNull(frame.getEngineSerialNo());
		assertNull(frame.getActualMissionType());
		assertTrue(frame.getEngineStatus() == 0);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @throws Exception 
	 * @date Jul 29, 2018
	 * Test the deassociateEngineAndFrame method when deassigning the engine from the frame
	 * when the engine is associated with a mission type and engine is not associated with a mission.
	 * {Engine SN = J35Y71056715}{Mission type SN =Q5NZMISSION}{Mission SN=Q5NZ0263739}{Frame=5FNRL6H20JB057874} 
	 */
	@Test
	public void deassignEngineAndFrame_deassociateEngentWithMissionTypeWithMission() throws Exception {
		when( partNameDaoMock.findByKey((String)Matchers.any())).thenReturn(setupTest.initEnginePart());

		engine = setupTest.setEngineWithFrameMissionTypeGood(engine);
		frame = setupTest.setFrameWithMissionType(frame);
		installedPart = setupTest.removeEngineFromFrame(installedPart);
		installedPartList.add(installedPart);

		engineMarriageService.deassignEngineAndFrame(installedPartList, "PPID", "APPID");
		assertNull(engine.getVin());
		assertNull(frame.getEngineSerialNo());
		assertNull(frame.getActualMissionType());
		assertTrue(engine.getMissionStatus() == 1);
		assertTrue(frame.getEngineStatus() == 0);
	}
}