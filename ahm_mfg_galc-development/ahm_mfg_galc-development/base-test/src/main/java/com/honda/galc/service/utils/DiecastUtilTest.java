package com.honda.galc.service.utils;

import java.sql.Date;

import org.junit.Assert;
import org.junit.Test;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.HoldParm;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DiecastUtilTest</code> is ... .
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
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Aug 21, 2014
 */
public class DiecastUtilTest {

	private static int qsrId;

	private String dch = "R1A0AE2203230014R";
	// R1A 2 3 0

	private String dcb = "RNA0HC2217140478S";
	// RNA 1 4 0

	private Date startDate = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
	private Date stopDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

	public static void main(String[] args) {
		DiecastUtilTest test = new DiecastUtilTest();
		test.testHeadMatch();
		test.testHeadModelNoMatch();
		test.testHeadMachineNoMatch();
		test.testHeadDieNoMatch();
		test.testHeadCoreNoMatch();
		test.testHeadDieCoreNoMatch();

		test.testBlockMatch();
		test.testBlockModelNoMatch();
		test.testBlockMachineNoMatch();
		test.testBlockDieNoMatch();
		test.testBlockCoreIgnored();
	}

	@Test
	public void testHeadMatch() {
		test(ProductType.HEAD, createHoldParm("R1A", "2", "[2,3]", "[0,A]", startDate, stopDate), dch, true);
		test(ProductType.HEAD, createHoldParm("R1A", "2", "[3]", "[0]", startDate, stopDate), dch, true);
		test(ProductType.HEAD, createHoldParm(null, "2", "[3]", "[0]", startDate, stopDate), dch, true);
		test(ProductType.HEAD, createHoldParm("  ", "2", "[3]", "[0]", startDate, stopDate), dch, true);
		test(ProductType.HEAD, createHoldParm("  ", "2", null, "[0]", startDate, stopDate), dch, true);
		test(ProductType.HEAD, createHoldParm("  ", "2", "[3]", null, startDate, stopDate), dch, true);
	}

	@Test
	public void testHeadModelNoMatch() {
		test(ProductType.HEAD, createHoldParm("RNA", "2", "[2,3]", "[0,A]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("RNA", "2", "[3]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("RNA", "2", "[3]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("RNA", "2", "[3]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("RNA", "2", null, "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("RNA", "2", "[3]", null, startDate, stopDate), dch, false);
	}

	@Test
	public void testHeadMachineNoMatch() {
		test(ProductType.HEAD, createHoldParm("R1A", "3", "[2,3]", "[0,A]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("R1A", "3", "[3]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm(null, "3", "[3]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("  ", "3", "[3]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("  ", "3", null, "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("  ", "3", "[3]", null, startDate, stopDate), dch, false);
	}

	@Test
	public void testHeadDieNoMatch() {
		test(ProductType.HEAD, createHoldParm("R1A", "2", "[4]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm(null, "2", "[4]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("  ", "2", "[4]", "[0]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("  ", "2", "[4]", null, startDate, stopDate), dch, false);
	}

	@Test
	public void testHeadCoreNoMatch() {
		test(ProductType.HEAD, createHoldParm("R1A", "2", "[2, 3]", "[A]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("R1A", "2", "[3]", "[A]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm(null, "2", "[3]", "[A]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("  ", "2", "[3]", "[A]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("  ", "2", null, "[A]", startDate, stopDate), dch, false);
	}

	@Test
	public void testHeadDieCoreNoMatch() {
		test(ProductType.HEAD, createHoldParm("R1A", "2", "[2, 3]", "[A]", startDate, stopDate), dch, false);
		test(ProductType.HEAD, createHoldParm("R1A", "2", "[4]", "[0]", startDate, stopDate), dch, false);
	}

	@Test
	public void testBlockMatch() {
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[4,5]", null, startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[4]", null, startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm(null, "1", "[4]", null, startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm("  ", "1", "[4]", null, startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm("  ", "1", null, null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "1", "[4]", null, startDate, stopDate), dcb, true);
	}

	@Test
	public void testBlockModelNoMatch() {
		test(ProductType.BLOCK, createHoldParm("R1A", "1", "[4,5]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("R1A", "1", "[4]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("R1A", "1", "[4]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("R1A", "1", "[4]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("R1A", "1", null, null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("R1A", "1", "[4]", null, startDate, stopDate), dcb, false);

	}

	@Test
	public void testBlockMachineNoMatch() {
		test(ProductType.BLOCK, createHoldParm("RNA", "2", "[4,5]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("RNA", "2", "[4]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm(null, "2", "[4]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "2", "[4]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "2", null, null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "2", "[4]", null, startDate, stopDate), dcb, false);
	}

	@Test
	public void testBlockDieNoMatch() {
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[3,5]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[3]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm(null, "1", "[3]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "1", "[3]", null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "1", null, null, startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "1", "[3]", null, startDate, stopDate), dcb, false);
	}

	@Test
	public void testBlockCoreIgnored() {
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[4,5]", "[0]", startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[4]", "[2]", startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm(null, "1", "[4]", "[3]", startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm("  ", "1", "[4]", "[4]", startDate, stopDate), dcb, true);
		test(ProductType.BLOCK, createHoldParm("  ", "1", "[4]", "[A]", startDate, stopDate), dcb, true);

		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[5]", "[0]", startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[3]", "[0]", startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm(null, "1", "[3]", "[0]", startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("RNA", "1", "[2]", "[0]", startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "1", null, "[0]", startDate, stopDate), dcb, false);
		test(ProductType.BLOCK, createHoldParm("  ", "1", "[6]", null, startDate, stopDate), dcb, false);
	}

	// === //
	protected void test(ProductType productType, HoldParm holdParm, String productId, Boolean expected) {
		boolean isHold = DiecastUtil.isProcessHold(holdParm, productId, productType);
		String patern = "%s{%s, model:%s, machine:%s, die:%s, core:%s, hold:%s, expected:%s}";
		String msg = String.format(patern, productType, productId, holdParm.getModelCode(), holdParm.getMachineNumber(), holdParm.getDieNumber(), holdParm.getCoreNumber(), isHold, expected);
		System.out.println(msg);
		Assert.assertEquals(String.valueOf(holdParm.getHoldId()), expected, isHold);
	}

	protected static HoldParm createHoldParm(String modelCode, String machineNumber, String dieNumber, String coreNumber, Date startDate, Date stopDate) {
		HoldParm hp = new HoldParm();
		hp.setHoldId(++qsrId);
		hp.setModelCode(modelCode);
		hp.setMachineNumber(machineNumber);
		hp.setDieNumber(dieNumber);
		hp.setCoreNumber(coreNumber);
		hp.setStartDate(startDate);
		hp.setStopDate(stopDate);
		return hp;
	}
}
