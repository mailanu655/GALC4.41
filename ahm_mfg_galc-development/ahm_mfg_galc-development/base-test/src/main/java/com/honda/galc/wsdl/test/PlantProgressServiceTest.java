package com.honda.galc.wsdl.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.honda.galc.test.dao.AbstractBaseTest;
import com.honda.galc.wsdl.client.FrameInfoDto;
import com.honda.galc.wsdl.client.OpEfficiencyDto;
import com.honda.galc.wsdl.client.PlantProgressService_Service;
import com.honda.galc.wsdl.client.PushTimerStatusDto;

/**
 * @author Subu Kathiresan
 * @date Sep 21, 2015
 */
public class PlantProgressServiceTest extends AbstractBaseTest {

	static PlantProgressService_Service service;
	
	@BeforeClass
	public static void beforeClass() {
		service = new PlantProgressService_Service();
	}
	
	@Test
	public void getProgressTest() { 
		List<PushTimerStatusDto> pushTimerStatuses = service.getPlantProgressServicePort().getProgress("Frame");
		System.out.println(pushTimerStatuses);
		int count = 0;
		for (PushTimerStatusDto dto: pushTimerStatuses) {
			if (dto.getProcessPointId().startsWith("SIMWSDL")) {
				count++;
			}
		}
		assertEquals(2, count);
	}
	
	@Test
	public void getTopLaggingProcessPointsTest() { 
		List<PushTimerStatusDto> pushTimerStatuses = service.getPlantProgressServicePort().getTopLaggingProcessPoints("Frame", "DIV3", 12);
		System.out.println(pushTimerStatuses);
		int count = 0;
		for (PushTimerStatusDto dto: pushTimerStatuses) {
			if (dto.getProcessPointId().startsWith("SIMWSDL")) {
				count++;
			}
		}
		assertEquals(2, count);
	}
	
	@Test
	public void getCurrentInventoryTest() { 
		int inventory = service.getPlantProgressServicePort().getCurrentInventory("SIMWSDLLINE1", "Frame");
		assertEquals(5, inventory);
	}
	
	@Test
	public void getAgedInventoryTest() { 
		int inventory = service.getPlantProgressServicePort().getAgedInventory("SIMWSDLLINE1", 100);
		assertEquals(5, inventory);
	}
	
	@Test
	public void getFrameInfoTest() { 
		FrameInfoDto frameInfoDto = service.getPlantProgressServicePort().getFrameInfo("SIMWSDLPP1", "TEST_TERMINAL");
		System.out.println(frameInfoDto.toString());
		assertEquals("SIMWSDL34AE000002", frameInfoDto.getProductId());
		assertEquals("ASVKAA600 NH578     B", frameInfoDto.getProductSpecCode().trim());
		assertEquals("GBNBTBRBDBBBWBSBABHB", frameInfoDto.getFifCodes().trim());
	}
	
	@Test
	public void getDeptUnitEfficiency() { 
		List<OpEfficiencyDto> opEfficiencyDtoList = service.getPlantProgressServicePort().getDeptUnitEfficiency("DIV3", 2015, 10);
		int count = 0;
		for (OpEfficiencyDto dto: opEfficiencyDtoList) {
			if (dto.getProcessPointId().startsWith("SIMWSDL")) {
				count++;
			}
		}
		assertEquals(20, count);
	}
	
	@Test
	public void getPPUnitEfficiency() { 
		List<OpEfficiencyDto> opEfficiencyDtoList = service.getPlantProgressServicePort().getPPUnitEfficiency("SIMWSDLPP1", 2015, 10);
		int count = 0;
		for (OpEfficiencyDto dto: opEfficiencyDtoList) {
			if (dto.getProcessPointId().startsWith("SIMWSDL")) {
				count++;
			}
		}
		assertEquals(10, count);
	}
}
