package com.honda.mfg.stamp.storage.web;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ModelControllerTest {
	@Test
	public void successfullyTestModelController() {
		loadTestData();
		ModelController controller = new ModelController();
		assertNotNull(controller.populateDies());
		assertNotNull(controller.populateModels());
	}

	public void loadTestData() {
		Die die, emptyDie;

		die = new Die();
		die.setId(1L);
		die.setBpmPartNumber("100A");
		die.setDescription("die");
		die.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		die.persist();

		emptyDie = new Die();
		emptyDie.setId(999L);
		emptyDie.setBpmPartNumber("100B");
		emptyDie.setDescription("emptydie");
		emptyDie.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		emptyDie.persist();
	}
}
