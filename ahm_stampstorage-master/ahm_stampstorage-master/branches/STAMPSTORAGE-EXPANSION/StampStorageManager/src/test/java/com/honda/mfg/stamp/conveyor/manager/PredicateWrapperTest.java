package com.honda.mfg.stamp.conveyor.manager;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.StorageRow;

/**
 * User: vcc30690 Date: 3/8/11
 */
public class PredicateWrapperTest {

	@Test
	public void predicateWrapperEmptyLaneMatcherTest() {

		PredicateWrapper<StorageRow> wrapper = new PredicateWrapper<StorageRow>(RowMatchers.isCurrentCapacityEmpty());
		StorageRow lane = new StorageRow(1l, "lane1", 10, 1);
		assertTrue(wrapper.apply(lane));
	}
}
