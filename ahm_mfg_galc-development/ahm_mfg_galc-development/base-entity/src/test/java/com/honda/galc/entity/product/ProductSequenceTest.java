/**
 * 
 */
package com.honda.galc.entity.product;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author VCC44349
 *
 */
public class ProductSequenceTest {

	Timestamp now = null;
	ProductSequenceId id = null;
	ProductSequence prodSeq = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		now = new Timestamp(System.currentTimeMillis());
		id = new ProductSequenceId("1HGCV1650JA511147", "RFID_009");
		prodSeq = new ProductSequence(id);
		prodSeq.setAssociateNo("vcc0123456");
		prodSeq.setProductType("   FRAME");
		prodSeq.setReferenceTimestamp(now);
		prodSeq.setSequenceNumber(7);
		prodSeq.setSourceSystemId(" 1HGCV1650JA511147 ");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		ProductSequenceId myId = new ProductSequenceId();
		myId.setProcessPointId("RFID_009");
		myId.setProductId("1HGCV1650JA511147");
		ProductSequence mySeq = new ProductSequence(myId);
		assertEquals(prodSeq.hashCode(), mySeq.hashCode());
		
		myId.setProcessPointId("RFID_002");
		assertEquals(prodSeq.hashCode() == mySeq.hashCode(), false);
		
		myId.setProcessPointId("RFID_009");
		myId.setProductId("1HGCV1661JA607943");
		assertEquals(prodSeq.hashCode() == mySeq.hashCode(), false);
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#toString()}.
	 */
	@Test
	public void testToString() {
		assertNotNull(prodSeq.toString());
		assertEquals("".equals(prodSeq.toString().trim()), false);
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		ProductSequenceId myId = new ProductSequenceId();
		myId.setProcessPointId("RFID_009");
		myId.setProductId("1HGCV1650JA511147");
		ProductSequence mySeq = new ProductSequence(myId);
		assertEquals(true, prodSeq.equals(mySeq));
		
		myId.setProcessPointId("RFID_002");
		assertEquals(false, prodSeq.equals(mySeq));
		
		myId.setProcessPointId("RFID_009");
		myId.setProductId("1HGCV1661JA607943");
		assertEquals(false, prodSeq.equals(mySeq));
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#getId()}.
	 */
	@Test
	public void testGetId() {
		assertNotNull(prodSeq.getId());
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#getReferenceTimestamp()}.
	 */
	@Test
	public void testGetReferenceTimestamp() {
		assertEquals(now, prodSeq.getReferenceTimestamp());
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#getAssociateNo()}.
	 */
	@Test
	public void testGetAssociateNo() {
		assertEquals("vcc0123456", prodSeq.getAssociateNo());
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#getSequenceNumber()}.
	 */
	@Test
	public void testGetSequenceNumber() {
		assertEquals(new Integer(7), prodSeq.getSequenceNumber());
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#getSourceSystemId()}.
	 */
	@Test
	public void testGetSourceSystemId() {
		assertEquals("1HGCV1650JA511147", prodSeq.getSourceSystemId());
	}

	/**
	 * Test method for {@link com.honda.galc.entity.product.ProductSequence#getProductType()}.
	 */
	@Test
	public void testGetProductType() {
		assertEquals("FRAME", prodSeq.getProductType());
	}

}
