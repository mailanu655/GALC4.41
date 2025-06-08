package com.honda.galc.service.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.property.EngineMissionAssignPropertyBean;
import com.honda.galc.service.engine.EngineMarriageServiceTestSetup;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyService.class, ProductCheckUtil.class})
public class EngineUtilTest extends EngineMarriageServiceTestSetup {
	private static final String MISSION = "MISSION";
	private static final String MISSION_TYPE = "MISSION_TYPE";
	private static final String ENGINE = "ENGINE";
	private static final String processPointId = "PP12345";

	private static final String MISSION_MESSAGE = "Incorrect result for MISSION_STATUS, actual was OK, expected NG";

	public InstalledPartStatus status;
	public Engine engine = new Engine();;
	
	@Mock
	EngineMissionAssignPropertyBean assignmentPropertyMock = PowerMockito.mock(EngineMissionAssignPropertyBean.class);
	
	@Mock
	ProductCheckUtil productCheckUtilMock = PowerMockito.mock(ProductCheckUtil.class);
	
	@InjectMocks
	public EngineUtil engineUtil = new EngineUtil(processPointId);

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when(PropertyService.getPropertyBean(EngineMissionAssignPropertyBean.class)).
		thenReturn(assignmentPropertyMock);
		PowerMockito.whenNew(ProductCheckUtil.class).withAnyArguments().thenReturn(productCheckUtilMock);
		PowerMockito.when(assignmentPropertyMock.getMissionPartName()).thenReturn("MISSION");
		PowerMockito.when(assignmentPropertyMock.getMissionTypePartName()).thenReturn("MISSION_TYPE");
		PowerMockito.when(assignmentPropertyMock.getEnginePartName()).thenReturn("ENGINE");
		PowerMockito.when(assignmentPropertyMock.isMissionTypeRequired()).thenReturn(true);
		
		engine = initEngine(engine);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissionStatus when the Mission is NG and the mission type
	 * is not Required
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}
	 */
	@Test
	public void determineMissionStatus_InstallMissionNGMissionTypeNR() {
		PowerMockito.when(assignmentPropertyMock.isMissionTypeRequired()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissionStatus when the Mission is Good and the mission type
	 * is not Required
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}
	 */
	@Test
	public void determineMissionStatus_InstallMissionGoodMissionTypeNR() {
		PowerMockito.when(assignmentPropertyMock.isMissionTypeRequired()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		assertTrue(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION, InstalledPartStatus.OK));
	}
	
	//--------------------------------//
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissioTypenStatus when the mission type  is NG and 
	 * the Mission is not associated
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineMissionStatus_InstallMissionTypeMissionTypeNGNoMission() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION_TYPE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissioTypenStatus when the Mission Type is Good and 
	 * the Mission is not associated.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineMissionStatus_InstallMissionTypeMissionTypeGoodNoMission() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION_TYPE, InstalledPartStatus.OK));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissioTypenStatus when the Mission Type is NG and 
	 * the Mission is NG.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineMissionStatus_InstallMissionTypeMissionTypeNGWithMissionNG() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION_TYPE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissioTypenStatus when the Mission Type is NG and 
	 * the Mission is Good.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineMissionStatus_InstallMissionTypeMissionTypeNGWithMissionGood() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(true);
		assertFalse(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION_TYPE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissioTypenStatus when the Mission Type is Good and 
	 * the Mission is NG.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineMissionStatus_InstallMissionTypeMissionTypeGoodWithMissionNG() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION_TYPE, InstalledPartStatus.OK));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineMissioTypenStatus when the Mission Type is Good and 
	 * the Mission is Good.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineMissionStatus_InstallMissionTypeMissionTypeGoodWithMissionGood() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(true);
		assertTrue(MISSION_MESSAGE, engineUtil.determineMissionStatus(engine, MISSION_TYPE, InstalledPartStatus.OK));
	}
	
	//--------------------------------//
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is not required and 
	 * the Mission is not associated.
	 * {Engine SN=J35Y71056715}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeNRNoMission() {
		PowerMockito.when(assignmentPropertyMock.isMissionTypeRequired()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is not required and 
	 * the Mission is NG.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeNRMissionNG() {
		PowerMockito.when(assignmentPropertyMock.isMissionTypeRequired()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is not required and 
	 * the Mission is Good.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeNRMissionGood() {
		PowerMockito.when(assignmentPropertyMock.isMissionTypeRequired()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(true);
		assertTrue(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.OK));
	}
	
	//--------------------------------//
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is NG and 
	 * the Mission is not installed.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeNGNoMission() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is NG and 
	 * the Mission is NG.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeNGMissionNG() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is NG and 
	 * the Mission is Good.
	 * {Engine SN=J35Y71056715}(Mission=Q5NZ0263739}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeNGMissionGood() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(true);
		assertFalse(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.NG));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is Good and 
	 * the Mission is not installed.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeGoodNoMission() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.OK));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is Good and 
	 * the Mission is NG.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}(Mission=Q5NZ0263739}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeGoodNoMissionNG() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(false);
		assertFalse(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.OK));
	}
	
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Aug 10, 2018
	 * The determineEngineStatus when the Mission Type is Good and 
	 * the Mission is Good.
	 * {Engine SN=J35Y71056715}{Mission type SN=Q5NZMISSION}(Mission=Q5NZ0263739}
	 */
	@Test
	public void determineEngineStatus_InstallEngineMissionTypeGoodNoMissionGood() {
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION_TYPE)).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.isInstalledPartStatusCheck(engine.getProductId(), MISSION)).thenReturn(true);
		assertTrue(MISSION_MESSAGE, engineUtil.determineEngineStatus(engine, ENGINE, InstalledPartStatus.OK));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date NOV 09, 2018
	 * Verify that checkValidPreviousLine returns true when the tracking statues are not 
	 * set with the property VALID_ENGINE_LOAD_PREVIOUS_LINES
	 * {Engine SN=J35Y71056715}{Tracking Status=LINE27}
	 */
	@Test
	public void checkValidPreviousEngineLine_noLineSet() {
		PowerMockito.when(assignmentPropertyMock.getValidEngineLoadPreviousLines()).thenReturn(null);
		assertTrue(engineUtil.checkValidPreviousEngineLine(engine));
	}	
	
	/**
	 * @author Bradley Brown, HMA
	 * @date NOV 09, 2018
	 * Verify that checkValidPreviousLine returns false when the current engine's tracking
	 * status is not listed in the property VALID_ENGINE_LOAD_PREVIOUS_LINES
	 * {Engine SN=J35Y71056715}{Tracking Status=LINE27}
	 */
	@Test
	public void checkValidPreviousEngineLine_invalidLine() {
		PowerMockito.when(assignmentPropertyMock.getValidEngineLoadPreviousLines()).thenReturn("LINE1,LINE2,LINE3");
		assertFalse(engineUtil.checkValidPreviousEngineLine(engine));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date NOV 09, 2018
	 * Verify that checkValidPreviousLine returns true when the current engine's tracking
	 * status is listed in the property VALID_ENGINE_LOAD_PREVIOUS_LINES
	 * {Engine SN=J35Y71056715}{Tracking Status=LINE27}
	 */
	@Test
	public void checkValidPreviousEngineLine_validLine() {
		PowerMockito.when(assignmentPropertyMock.getValidEngineLoadPreviousLines()).thenReturn("LINE1,LINE2,LINE27,LINE3");
		assertTrue(engineUtil.checkValidPreviousEngineLine(engine));
	}
	
	
}
