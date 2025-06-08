/**
 * 
 */
package com.honda.galc.web;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;

/**
 * @author VCC44349
 *
 */
public class ProductSequenceDtoTest {

	Timestamp now = null;
	ProductSequenceId id = null;
	ProductSequence prodSeq = null;
	ProductSequenceDto dto = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		now = new Timestamp(System.currentTimeMillis());
		id = new ProductSequenceId(" 1HGCV1650JA511147 ", " RFID_009 ");
		prodSeq = new ProductSequence(id);
		prodSeq.setAssociateNo("  vcc0123456  ");
		prodSeq.setProductType("   FRAME");
		prodSeq.setReferenceTimestamp(now);
		prodSeq.setSequenceNumber(7);
		prodSeq.setSourceSystemId(" 1HGCV1650JA511147 ");
		dto = ProductSequenceDto.createMeFromEntity(prodSeq);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#getProductId()}.
	 */
	@Test
	public void testGetProductId() {
		assertNotNull(dto);
		assertEquals("1HGCV1650JA511147", dto.getProductId());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#getStationId()}.
	 */
	@Test
	public void testGetStationId() {
		assertEquals("RFID_009", dto.getStationId());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#getReferenceTimestamp()}.
	 */
	@Test
	public void testGetReferenceTimestamp() {
		assertEquals(now, dto.getReferenceTimestamp());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#getAssociateNo()}.
	 */
	@Test
	public void testGetAssociateNo() {
		assertEquals("vcc0123456", dto.getAssociateNo());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#getSequenceNumber()}.
	 */
	@Test
	public void testGetSequenceNumber() {
		assertEquals(7, dto.getSequenceNumber().intValue());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#getSourceSystemId()}.
	 */
	@Test
	public void testGetSourceSystemId() {
		assertEquals("1HGCV1650JA511147", dto.getSourceSystemId());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#getProductType()}.
	 */
	@Test
	public void testGetProductType() {
		assertEquals("FRAME", dto.getProductType());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductSequenceDto#createProductSequence()}.
	 */
	@Test
	public void testCreateProductSequence() {
		ProductSequence newEntity = dto.createProductSequence();
		assertNotNull(newEntity);
		assertEquals(prodSeq, newEntity);
		assertEquals(prodSeq.hashCode(), newEntity.hashCode());
	}

}
