package com.honda.shared.domain;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * User: vcc30690 Date: 3/8/11
 */
public class EntityBaseTest {

	@Test
	public void equalityCheckForTwoEntitiesWithSameIds() {

		EntityBase entity1 = new EntityBase(1);
		EntityBase entity2 = new EntityBase(1);
		EntityBase entity3 = new EntityBase(3);

		assertEquals(entity1, entity2);
		assertTrue(entity1.sameIdentityAs(entity2));
		assertNotSame(entity1, entity3);
		assertFalse(entity1.equals(new Object()));

	}

	@Test
	public void equalityCheckForTwoEntitiesWithoutSameIds() {

		EntityBase entity1 = new EntityBase(1);
		EntityBase entity2 = new EntityBase(12);

		assertNotSame(entity1, entity2);
		assertTrue(!entity1.sameIdentityAs(entity2));
	}

	@Test
	public void equalityCheckForTwoEntitiesWithNull() {

		EntityBase entity1 = new EntityBase(1);
		assertTrue(!entity1.equals(null));
	}
}
