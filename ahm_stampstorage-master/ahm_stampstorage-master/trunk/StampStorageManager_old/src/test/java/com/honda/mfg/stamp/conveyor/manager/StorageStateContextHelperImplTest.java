package com.honda.mfg.stamp.conveyor.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


/**
 * Michael Grecol
 * VC029913
 * 4/25/14
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class StorageStateContextHelperImplTest {


    @Test
    public void successfullyFindDieByDieNumber() {
        loadDieTable();
        StorageStateContextHelper helper = new StorageStateContextHelperImpl();

        assertNotNull(helper.findDieByNumber(166l));
        assertNull(helper.findDieByNumber(-1l));
        assertNull(helper.findDieByNumber(null));
    }

    @Test
    public void successfullyFindStopByConveyorId() {
        loadStopTable();
        StorageStateContextHelper helper = new StorageStateContextHelperImpl();

        assertNotNull(helper.findStopByConveyorId(1230l));
        assertNull(helper.findStopByConveyorId(null));
        assertNull(helper.findStopByConveyorId(-1l));
    }

	@Test
	public void successfullyFindAllCarriersInStorage() {
		loadAllStops();
		loadAllStorageRows();
		loadCarrierInEachRow();
		StorageStateContextHelper helper = new StorageStateContextHelperImpl();
		assertTrue((helper.findAllCarriersInStorage().size() == 46));
	}
    void loadDieTable() {
        Die die = new Die();
        die.setId(166L);
        die.setDescription("left Die");
        die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        die.persist();
    }
    
  @Test
  public void spaceAvailableBlocked(){
    	loadAllStops();
		loadAllStorageRows();
		Stop stop = Stop.findStop(1212L);
		StorageStateContextHelper helper = new StorageStateContextHelperImpl();
		blockAArea();
		assertFalse(helper.spaceAvailable(stop));
		unblockAArea();
		assertTrue(helper.spaceAvailable(stop)); 
 }
    
@Test
public void successfullyFindSpaceAvailableForAllRows(){

	loadAllStops();
	loadAllStorageRows();
	//set stop to row 12
	Stop stop = Stop.findStop(1212L);
	StorageStateContextHelper helper = new StorageStateContextHelperImpl();
	unblockAArea();
	//Load carriers in Queue with extra space
	List<StorageRow> rows = StorageRow.findStorageRowsByArea(StorageArea.A_AREA);
	CarrierMes c;
	for (StorageRow row : rows){
		int remaining = row.getCapacity()-row.getCurrentCarrierCount()-1;
		for (int i = 0; i<remaining; i++){
			c = new CarrierMes(); c.setId(row.getId()+i); c.setBuffer(0); c.setCarrierNumber(457+i); c.setCurrentLocation(row.getStop().getId()); c.setDestination(row.getStop().getId()); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
		}
	}
	assertTrue(helper.spaceAvailable(stop));
	//Load Stop 12 with carriers to overfill queue
		StorageRow row = StorageRow.findStorageRowsByStop(stop);
			int remaining = row.getCapacity()-row.getCurrentCarrierCount();
			for (int i = 0; i<remaining; i++){
				c = new CarrierMes(); c.setId(row.getId()+i); c.setBuffer(0); c.setCarrierNumber(457+i); c.setCurrentLocation(row.getStop().getId()); c.setDestination(row.getStop().getId()); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
			}
			assertFalse(helper.spaceAvailable(stop));	
}
    
void loadStopTable() {
	Stop stop = new Stop();
	stop.setId(1230L);
	stop.setName("ST12-30");
	stop.setStopType(StopType.NO_ACTION);
	stop.setStopArea(StopArea.findByType(0));
	stop.persist();



}

    void loadAllStops(){
    	Stop stop;
    	stop = new Stop(); stop.setId(0L); stop.setName("ST0 N/A - Maintenance Stop"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(400L); stop.setName("ST4 WE_1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(401L); stop.setName("ST4-1 MAINT / REWORK"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.C_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(403L); stop.setName("ST4-3 C_P EMPTY DEL"); stop.setStopType(StopType.EMPTY_CARRIER_DELIVERY);stop.setStopArea(StopArea.C_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(2005L); stop.setName("ST20-5 B-PR EMTY DEL"); stop.setStopType(StopType.EMPTY_CARRIER_DELIVERY);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(5200L); stop.setName("ST52 OLD_WELD_LINE"); stop.setStopType(StopType.EMPTY_CARRIER_DELIVERY);stop.setStopArea(StopArea.OLD_WELD_LINE); stop.persist();
    	stop = new Stop(); stop.setId(502L); stop.setName("ST5-2"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(506L); stop.setName("ST5-6"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(13400L); stop.setName("ST134Empty"); stop.setStopType(StopType.EMPTY_CARRIER_DELIVERY);stop.setStopArea(StopArea.EMPTY_AREA); stop.persist();
    	stop = new Stop(); stop.setId(1800L); stop.setName("ST18 WE_1"); stop.setStopType(StopType.FULL_CARRIER_CONSUMPTION);stop.setStopArea(StopArea.WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(3700L); stop.setName("ST37"); stop.setStopType(StopType.FULL_CARRIER_CONSUMPTION);stop.setStopArea(StopArea.WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(10800L); stop.setName("ST108"); stop.setStopType(StopType.FULL_CARRIER_CONSUMPTION);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(11800L); stop.setName("ST118"); stop.setStopType(StopType.FULL_CARRIER_CONSUMPTION);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(706L); stop.setName("ST7-6"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.KD_LINE); stop.persist();
    	stop = new Stop(); stop.setId(707L); stop.setName("ST7-7"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_OUT_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(708L); stop.setName("ST7-8"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(500L); stop.setName("ST5 WE1_DELIVERY"); stop.setStopType(StopType.FULL_CARRIER_DELIVERY);stop.setStopArea(StopArea.WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(803L); stop.setName("ST8-3 C_H STOR IN"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(804L); stop.setName("ST8-4 C_H STOR IN"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(805L); stop.setName("ST8-5 C_H STOR IN"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(901L); stop.setName("ST9-1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_OUT_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(902L); stop.setName("ST9-2"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_OUT_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(700L); stop.setName("ST7 KD REWORK"); stop.setStopType(StopType.FULL_CARRIER_DELIVERY);stop.setStopArea(StopArea.KD_LINE); stop.persist();
    	stop = new Stop(); stop.setId(800L); stop.setName("ST8 KD CONSUM POINT"); stop.setStopType(StopType.FULL_CARRIER_DELIVERY);stop.setStopArea(StopArea.KD_LINE); stop.persist();
    	stop = new Stop(); stop.setId(1101L); stop.setName("ST11-1 AA  STOR IN"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(1201L); stop.setName("ROW 1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1202L); stop.setName("ROW 2"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1203L); stop.setName("ROW 3"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1204L); stop.setName("ROW 4"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1205L); stop.setName("ROW 5"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1206L); stop.setName("ROW 6"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1207L); stop.setName("ROW 7"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1208L); stop.setName("ROW 8"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1209L); stop.setName("ROW 9"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1210L); stop.setName("ROW 10"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1001L); stop.setName("ST10-1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(1211L); stop.setName("ROW 11"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1212L); stop.setName("ROW 12"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1213L); stop.setName("ROW 13"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1214L); stop.setName("ROW 14"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1215L); stop.setName("ROW 15"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1216L); stop.setName("ROW 16"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1217L); stop.setName("ROW 17"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1218L); stop.setName("ROW 18"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1219L); stop.setName("ROW 19"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1220L); stop.setName("ROW 20"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1221L); stop.setName("ROW 21"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1222L); stop.setName("ROW 22"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1223L); stop.setName("ROW 23"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1224L); stop.setName("ROW 24"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.Q_WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(1225L); stop.setName("ROW 25"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.Q_WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(1226L); stop.setName("ROW 26"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.Q_WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(1227L); stop.setName("ROW 27"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1228L); stop.setName("ROW 28"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1229L); stop.setName("ROW 29"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.Q_WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(1230L); stop.setName("ROW 30"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1231L); stop.setName("ROW 31"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1232L); stop.setName("ROW 32"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1233L); stop.setName("ROW 33"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1234L); stop.setName("ROW 34"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1235L); stop.setName("ROW 35"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1236L); stop.setName("ROW 36"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1237L); stop.setName("ROW 37"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1238L); stop.setName("ROW 38"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1239L); stop.setName("ROW 39"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1240L); stop.setName("ROW 40"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1241L); stop.setName("ROW 41"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1242L); stop.setName("ROW 42"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1243L); stop.setName("ROW 43"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1244L); stop.setName("ROW 44"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1245L); stop.setName("ROW 45"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(1246L); stop.setName("ROW 46"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.ROW); stop.persist();
    	stop = new Stop(); stop.setId(10100L); stop.setName("ST101 WE_2 DELIVERY"); stop.setStopType(StopType.FULL_CARRIER_DELIVERY);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(1301L); stop.setName("ST13-1 PRE-INSPECT"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_OUT_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(1401L); stop.setName("ST14-1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_OUT_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(1600L); stop.setName("ST16 C_L STOR IN"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(4400L); stop.setName("ST44"); stop.setStopType(StopType.LEFT_CONSUMED_CARRIER_EXIT);stop.setStopArea(StopArea.WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(2001L); stop.setName("ST20-1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(2003L); stop.setName("ST20-3 EMP. STOR OUT"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.C_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(2004L); stop.setName("ST20-4"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(10900L); stop.setName("ST109"); stop.setStopType(StopType.LEFT_CONSUMED_CARRIER_EXIT);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(2007L); stop.setName("ST20-7 B PRESS ROBOT 2"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(2011L); stop.setName("ST20-11 B PRESS ROBOT 1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(2014L); stop.setName("ST20-14 B PRESS INSPECTION"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(2015L); stop.setName("ST20-15 B PRESS INSPECTION"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(404L); stop.setName("ST4-4"); stop.setStopType(StopType.MAINTENANCE);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(2016L); stop.setName("ST20-16 B PRESS INSPECTION"); stop.setStopType(StopType.PRESS_INSPECTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(704L); stop.setName("ST7-4 A_A STOR IN"); stop.setStopType(StopType.RECIRC_TO_ALL_ROWS);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(2024L); stop.setName("ST20-24"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(2027L); stop.setName("ST20-27"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(3018L); stop.setName("ST30-18"); stop.setStopType(StopType.RECIRC_TO_ALL_ROWS);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(3001L); stop.setName("ST30-1"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(905L); stop.setName("ST9-5"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(3004L); stop.setName("ST30-4"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(3008L); stop.setName("ST30-8"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(3100L); stop.setName("ST31"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(903L); stop.setName("ST9-3 C_H STOR OUT"); stop.setStopType(StopType.RELEASE_CHECK);stop.setStopArea(StopArea.STORE_OUT_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(904L); stop.setName("ST9-4 C_L STOR OUT"); stop.setStopType(StopType.RELEASE_CHECK);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(4700L); stop.setName("ST47 WELD 1 EMPTY RETURN"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.EMPTY_AREA); stop.persist();
    	stop = new Stop(); stop.setId(4900L); stop.setName("ST49"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.OLD_WELD_LINE); stop.persist();
    	stop = new Stop(); stop.setId(1300L); stop.setName("ST13 FINAL INSPECT"); stop.setStopType(StopType.RELEASE_CHECK);stop.setStopArea(StopArea.STORE_OUT_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(3027L); stop.setName("ST30-27"); stop.setStopType(StopType.RELEASE_CHECK);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(405L); stop.setName("ST4-5 C_P REWOR EXIT"); stop.setStopType(StopType.REWORK);stop.setStopArea(StopArea.C_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(2018L); stop.setName("ST20-18 B PRESS REWORK 1"); stop.setStopType(StopType.REWORK);stop.setStopArea(StopArea.B_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(11200L); stop.setName("ST112"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(2019L); stop.setName("ST20-19 B PRESS REWORK 2"); stop.setStopType(StopType.REWORK);stop.setStopArea(StopArea.B_PRESS); stop.persist();
    	stop = new Stop(); stop.setId(2400L); stop.setName("ST24"); stop.setStopType(StopType.RIGHT_CONSUMED_CARRIER_EXIT);stop.setStopArea(StopArea.WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(12200L); stop.setName("ST122"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(12400L); stop.setName("ST124"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.EMPTY_AREA); stop.persist();
    	stop = new Stop(); stop.setId(1500L); stop.setName("ST15"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(13800L); stop.setName("ST138"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.WELD_LINE_1); stop.persist();
    	stop = new Stop(); stop.setId(16100L); stop.setName("ST161"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(12800L); stop.setName("ST128"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.EMPTY_AREA); stop.persist();
    	stop = new Stop(); stop.setId(3010L); stop.setName("ST30-10"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(3015L); stop.setName("ST30-15"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(11900L); stop.setName("ST119"); stop.setStopType(StopType.RIGHT_CONSUMED_CARRIER_EXIT);stop.setStopArea(StopArea.WELD_LINE_2); stop.persist();
    	stop = new Stop(); stop.setId(508L); stop.setName("ST5-8"); stop.setStopType(StopType.STORE_IN_ALL_LANES);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(3020L); stop.setName("ST30-20"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(3021L); stop.setName("ST30-21"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(3022L); stop.setName("ST30-22"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(509L); stop.setName("ST5-9"); stop.setStopType(StopType.STORE_IN_ALL_LANES);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(512L); stop.setName("ST5-12"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(3023L); stop.setName("ST30-23"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.B_AREA); stop.persist();
    	stop = new Stop(); stop.setId(608L); stop.setName("ST6-8"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(609L); stop.setName("ST6-9"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(605L); stop.setName("ST6-5"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(513L); stop.setName("ST5-13 STORE IN"); stop.setStopType(StopType.STORE_IN_ALL_LANES);stop.setStopArea(StopArea.STORE_IN_ROUTE); stop.persist();
    	stop = new Stop(); stop.setId(511L); stop.setName("ST5-11"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(2029L); stop.setName("ST20-29"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(1100L); stop.setName("ST-11"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(1200L); stop.setName("ST-12"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(1402L); stop.setName("ST-14-2"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(2500L); stop.setName("ST-25"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(3017L); stop.setName("ST30-17"); stop.setStopType(StopType.STORE_IN_ALL_LANES);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(3003L); stop.setName("ST-30-3 Store-in"); stop.setStopType(StopType.STORE_IN_ALL_LANES);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(11000L); stop.setName("ST-110"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(11300L); stop.setName("ST-113"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(11500L); stop.setName("ST-115"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();
    	stop = new Stop(); stop.setId(15600L); stop.setName("ST-156"); stop.setStopType(StopType.NO_ACTION);stop.setStopArea(StopArea.UNDEFINED); stop.persist();

    }
    void unblockAArea(){
     	List<StorageRow> rows = StorageRow.findStorageRowsByArea(StorageArea.A_AREA);
     	for (StorageRow r : rows){
     		r.setAvailability(StopAvailability.AVAILABLE);
     		r.merge();
    	}
     	
     
    }
    void blockAArea(){
     	List<StorageRow> rows = StorageRow.findStorageRowsByArea(StorageArea.A_AREA);
     	for (StorageRow r : rows){
     		r.setAvailability(StopAvailability.BLOCKED);
     		r.merge();
     	}
    }
    void loadAllStorageRows(){
    	StorageRow r;
    	r  = new StorageRow(); 
    	r.setId(1L); 
    	r.setAvailability(StopAvailability.findByType(0));
    	r.setCapacity(12); 
    	r.setRowName("Row 1"); 
    	r.setStorageArea(StorageArea.findByType(1));
    	r.setVersion(10); 
    	r.setStop(Stop.findStop(1201L));
    	//r.setStop(null);
    	r.merge();
  
    	r  = new StorageRow(); r.setId(2L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 2"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(3); r.setStop(Stop.findStop(1202L)); r.merge(); 
    	r  = new StorageRow(); r.setId(3L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 3"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(2); r.setStop(Stop.findStop(1203L)); r.merge(); 
    	r  = new StorageRow(); r.setId(4L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 4"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(6); r.setStop(Stop.findStop(1204L)); r.merge(); 
    	r  = new StorageRow(); r.setId(5L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 5"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(5); r.setStop(Stop.findStop(1205L)); r.merge(); 
    	r  = new StorageRow(); r.setId(6L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 6"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(16); r.setStop(Stop.findStop(1206L)); r.merge(); 
    	r  = new StorageRow(); r.setId(7L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 7"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(14); r.setStop(Stop.findStop(1207L)); r.merge(); 
    	r  = new StorageRow(); r.setId(8L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 8"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(4); r.setStop(Stop.findStop(1208L)); r.merge(); 
    	r  = new StorageRow(); r.setId(9L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 9"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(4); r.setStop(Stop.findStop(1209L)); r.merge(); 
    	r  = new StorageRow(); r.setId(10L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 10"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(5); r.setStop(Stop.findStop(1210L)); r.merge(); 
    	r  = new StorageRow(); r.setId(11L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 11"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(5); r.setStop(Stop.findStop(1211L)); r.merge(); 
    	r  = new StorageRow(); r.setId(12L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 12"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(4); r.setStop(Stop.findStop(1212L)); r.merge(); 
    	r  = new StorageRow(); r.setId(13L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 13"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(5); r.setStop(Stop.findStop(1213L)); r.merge(); 
    	r  = new StorageRow(); r.setId(14L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 14"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(5); r.setStop(Stop.findStop(1214L)); r.merge(); 
    	r  = new StorageRow(); r.setId(15L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 15"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(4); r.setStop(Stop.findStop(1215L)); r.merge(); 
    	r  = new StorageRow(); r.setId(16L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 16"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(4); r.setStop(Stop.findStop(1216L)); r.merge(); 
    	r  = new StorageRow(); r.setId(17L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(12); r.setRowName("Row 17"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(4); r.setStop(Stop.findStop(1217L)); r.merge(); 
    	r  = new StorageRow(); r.setId(18L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 18"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(5); r.setStop(Stop.findStop(1218L)); r.merge(); 
    	r  = new StorageRow(); r.setId(19L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 19"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(9); r.setStop(Stop.findStop(1219L)); r.merge(); 
    	r  = new StorageRow(); r.setId(20L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(12); r.setRowName("Row 20"); r.setStorageArea(StorageArea.findByType(1)); r.setVersion(7); r.setStop(Stop.findStop(1220L)); r.merge(); 
    	r  = new StorageRow(); r.setId(21L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(21); r.setRowName("Row 21"); r.setStorageArea(StorageArea.findByType(3)); r.setVersion(6); r.setStop(Stop.findStop(1221L)); r.merge(); 
    	r  = new StorageRow(); r.setId(22L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(21); r.setRowName("Row 22"); r.setStorageArea(StorageArea.findByType(3)); r.setVersion(10); r.setStop(Stop.findStop(1222L)); r.merge(); 
    	r  = new StorageRow(); r.setId(23L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(21); r.setRowName("Row 23"); r.setStorageArea(StorageArea.findByType(3)); r.setVersion(12); r.setStop(Stop.findStop(1223L)); r.merge(); 
    	r  = new StorageRow(); r.setId(24L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(10); r.setRowName("Row 24"); r.setStorageArea(StorageArea.findByType(0)); r.setVersion(15); r.setStop(Stop.findStop(1224L)); r.merge(); 
    	r  = new StorageRow(); r.setId(25L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(21); r.setRowName("Row 25"); r.setStorageArea(StorageArea.findByType(3)); r.setVersion(9); r.setStop(Stop.findStop(1225L)); r.merge(); 
    	r  = new StorageRow(); r.setId(26L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(10); r.setRowName("Row 26"); r.setStorageArea(StorageArea.findByType(0)); r.setVersion(13); r.setStop(Stop.findStop(1226L)); r.merge(); 
    	r  = new StorageRow(); r.setId(27L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(10); r.setRowName("Row 27"); r.setStorageArea(StorageArea.findByType(0)); r.setVersion(6); r.setStop(Stop.findStop(1227L)); r.merge(); 
    	r  = new StorageRow(); r.setId(28L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(21); r.setRowName("Row 28"); r.setStorageArea(StorageArea.findByType(3)); r.setVersion(9); r.setStop(Stop.findStop(1228L)); r.merge(); 
    	r  = new StorageRow(); r.setId(29L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(10); r.setRowName("Row 29"); r.setStorageArea(StorageArea.findByType(0)); r.setVersion(8); r.setStop(Stop.findStop(1229L)); r.merge(); 
    	r  = new StorageRow(); r.setId(30L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(30); r.setRowName("Row 30"); r.setStorageArea(StorageArea.findByType(2)); r.setVersion(8); r.setStop(Stop.findStop(1230L)); r.merge(); 
    	r  = new StorageRow(); r.setId(31L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(30); r.setRowName("Row 31"); r.setStorageArea(StorageArea.findByType(2)); r.setVersion(3); r.setStop(Stop.findStop(1231L)); r.merge(); 
    	r  = new StorageRow(); r.setId(32L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(30); r.setRowName("Row 32"); r.setStorageArea(StorageArea.findByType(2)); r.setVersion(3); r.setStop(Stop.findStop(1232L)); r.merge(); 
    	r  = new StorageRow(); r.setId(33L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(30); r.setRowName("Row 33"); r.setStorageArea(StorageArea.findByType(2)); r.setVersion(7); r.setStop(Stop.findStop(1233L)); r.merge(); 
    	r  = new StorageRow(); r.setId(34L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(30); r.setRowName("Row 34"); r.setStorageArea(StorageArea.findByType(2)); r.setVersion(5); r.setStop(Stop.findStop(1234L)); r.merge(); 
    	r  = new StorageRow(); r.setId(35L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(28); r.setRowName("Row 35"); r.setStorageArea(StorageArea.findByType(2)); r.setVersion(5); r.setStop(Stop.findStop(1235L)); r.merge(); 
    	r  = new StorageRow(); r.setId(41L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(31); r.setRowName("Row 36"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(7); r.setStop(Stop.findStop(1236L)); r.merge(); 
    	r  = new StorageRow(); r.setId(42L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(31); r.setRowName("Row 37"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(9); r.setStop(Stop.findStop(1237L)); r.merge(); 
    	r  = new StorageRow(); r.setId(43L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(31); r.setRowName("Row 38"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(8); r.setStop(Stop.findStop(1238L)); r.merge(); 
    	r  = new StorageRow(); r.setId(44L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(31); r.setRowName("Row 39"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(8); r.setStop(Stop.findStop(1239L)); r.merge(); 
    	r  = new StorageRow(); r.setId(45L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(31); r.setRowName("Row 40"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(10); r.setStop(Stop.findStop(1240L)); r.merge(); 
    	r  = new StorageRow(); r.setId(46L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(31); r.setRowName("Row 41"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(11); r.setStop(Stop.findStop(1241L)); r.merge(); 
    	r  = new StorageRow(); r.setId(47L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(31); r.setRowName("Row 42"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(21); r.setStop(Stop.findStop(1242L)); r.merge(); 
    	r  = new StorageRow(); r.setId(48L); r.setAvailability(StopAvailability.findByType(1)); r.setCapacity(31); r.setRowName("Row 43"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(9); r.setStop(Stop.findStop(1243L)); r.merge(); 
    	r  = new StorageRow(); r.setId(49L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(86); r.setRowName("Row 44"); r.setStorageArea(StorageArea.findByType(4)); r.setVersion(9); r.setStop(Stop.findStop(1244L)); r.merge(); 
    	r  = new StorageRow(); r.setId(50L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(86); r.setRowName("Row 45"); r.setStorageArea(StorageArea.findByType(4)); r.setVersion(13); r.setStop(Stop.findStop(1245L)); r.merge(); 
    	r  = new StorageRow(); r.setId(51L); r.setAvailability(StopAvailability.findByType(0)); r.setCapacity(29); r.setRowName("Row 46"); r.setStorageArea(StorageArea.findByType(5)); r.setVersion(35); r.setStop(Stop.findStop(1246L)); r.merge(); 

    }
    void loadCarrierInEachRow(){
    	CarrierMes c;
    	c = new CarrierMes(); c.setId(1L); c.setBuffer(0); c.setCarrierNumber(457); c.setCurrentLocation(1201L); c.setDestination(1201L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(2L); c.setBuffer(0); c.setCarrierNumber(458); c.setCurrentLocation(1202L); c.setDestination(1202L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(3L); c.setBuffer(0); c.setCarrierNumber(459); c.setCurrentLocation(1203L); c.setDestination(1203L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(4L); c.setBuffer(0); c.setCarrierNumber(460); c.setCurrentLocation(1204L); c.setDestination(1204L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(5L); c.setBuffer(0); c.setCarrierNumber(461); c.setCurrentLocation(1205L); c.setDestination(1205L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(6L); c.setBuffer(0); c.setCarrierNumber(462); c.setCurrentLocation(1206L); c.setDestination(1206L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(7L); c.setBuffer(0); c.setCarrierNumber(463); c.setCurrentLocation(1207L); c.setDestination(1207L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(8L); c.setBuffer(0); c.setCarrierNumber(464); c.setCurrentLocation(1208L); c.setDestination(1208L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(9L); c.setBuffer(0); c.setCarrierNumber(465); c.setCurrentLocation(1209L); c.setDestination(1209L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(10L); c.setBuffer(0); c.setCarrierNumber(466); c.setCurrentLocation(1210L); c.setDestination(1210L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(11L); c.setBuffer(0); c.setCarrierNumber(467); c.setCurrentLocation(1211L); c.setDestination(1211L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(12L); c.setBuffer(0); c.setCarrierNumber(468); c.setCurrentLocation(1212L); c.setDestination(1212L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(13L); c.setBuffer(0); c.setCarrierNumber(469); c.setCurrentLocation(1213L); c.setDestination(1213L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(14L); c.setBuffer(0); c.setCarrierNumber(470); c.setCurrentLocation(1214L); c.setDestination(1214L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(15L); c.setBuffer(0); c.setCarrierNumber(471); c.setCurrentLocation(1215L); c.setDestination(1215L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(16L); c.setBuffer(0); c.setCarrierNumber(472); c.setCurrentLocation(1216L); c.setDestination(1216L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(17L); c.setBuffer(0); c.setCarrierNumber(473); c.setCurrentLocation(1217L); c.setDestination(1217L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(18L); c.setBuffer(0); c.setCarrierNumber(474); c.setCurrentLocation(1218L); c.setDestination(1218L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(19L); c.setBuffer(0); c.setCarrierNumber(475); c.setCurrentLocation(1219L); c.setDestination(1219L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(20L); c.setBuffer(0); c.setCarrierNumber(476); c.setCurrentLocation(1220L); c.setDestination(1220L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(21L); c.setBuffer(0); c.setCarrierNumber(477); c.setCurrentLocation(1221L); c.setDestination(1221L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(22L); c.setBuffer(0); c.setCarrierNumber(478); c.setCurrentLocation(1222L); c.setDestination(1222L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(23L); c.setBuffer(0); c.setCarrierNumber(479); c.setCurrentLocation(1223L); c.setDestination(1223L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(24L); c.setBuffer(0); c.setCarrierNumber(480); c.setCurrentLocation(1224L); c.setDestination(1224L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(25L); c.setBuffer(0); c.setCarrierNumber(481); c.setCurrentLocation(1225L); c.setDestination(1225L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(26L); c.setBuffer(0); c.setCarrierNumber(482); c.setCurrentLocation(1226L); c.setDestination(1226L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(27L); c.setBuffer(0); c.setCarrierNumber(483); c.setCurrentLocation(1227L); c.setDestination(1227L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(28L); c.setBuffer(0); c.setCarrierNumber(484); c.setCurrentLocation(1228L); c.setDestination(1228L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(29L); c.setBuffer(0); c.setCarrierNumber(485); c.setCurrentLocation(1229L); c.setDestination(1229L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(30L); c.setBuffer(0); c.setCarrierNumber(486); c.setCurrentLocation(1230L); c.setDestination(1230L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(31L); c.setBuffer(0); c.setCarrierNumber(487); c.setCurrentLocation(1231L); c.setDestination(1231L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(32L); c.setBuffer(0); c.setCarrierNumber(488); c.setCurrentLocation(1232L); c.setDestination(1232L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(33L); c.setBuffer(0); c.setCarrierNumber(489); c.setCurrentLocation(1233L); c.setDestination(1233L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(34L); c.setBuffer(0); c.setCarrierNumber(490); c.setCurrentLocation(1234L); c.setDestination(1234L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(35L); c.setBuffer(0); c.setCarrierNumber(491); c.setCurrentLocation(1235L); c.setDestination(1235L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(36L); c.setBuffer(0); c.setCarrierNumber(492); c.setCurrentLocation(1236L); c.setDestination(1236L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(37L); c.setBuffer(0); c.setCarrierNumber(493); c.setCurrentLocation(1237L); c.setDestination(1237L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(38L); c.setBuffer(0); c.setCarrierNumber(494); c.setCurrentLocation(1238L); c.setDestination(1238L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(39L); c.setBuffer(0); c.setCarrierNumber(495); c.setCurrentLocation(1239L); c.setDestination(1239L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(40L); c.setBuffer(0); c.setCarrierNumber(496); c.setCurrentLocation(1240L); c.setDestination(1240L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(41L); c.setBuffer(0); c.setCarrierNumber(497); c.setCurrentLocation(1241L); c.setDestination(1241L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(42L); c.setBuffer(0); c.setCarrierNumber(498); c.setCurrentLocation(1242L); c.setDestination(1242L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(43L); c.setBuffer(0); c.setCarrierNumber(499); c.setCurrentLocation(1243L); c.setDestination(1243L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(44L); c.setBuffer(0); c.setCarrierNumber(500); c.setCurrentLocation(1244L); c.setDestination(1244L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(45L); c.setBuffer(0); c.setCarrierNumber(501); c.setCurrentLocation(1245L); c.setDestination(1245L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 
    	c = new CarrierMes(); c.setId(46L); c.setBuffer(0); c.setCarrierNumber(502); c.setCurrentLocation(1246L); c.setDestination(1246L); c.setDieNumber(301); c.setOriginationLocation(2007); c.setProductionRunDate(new Date()); c.setProductionRunNumber(20144114); c.setQuantity(14); c.setStatus(0); c.setTagId(0); c.setUpdateDate(new Date()); c.setSource("Test"); c.persist(); 

    }
}
