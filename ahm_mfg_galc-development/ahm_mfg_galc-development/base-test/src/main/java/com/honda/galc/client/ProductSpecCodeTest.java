package com.honda.galc.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.util.ProductSpecUtil;

public class ProductSpecCodeTest {
	
	
	
	@Test
	public void test1() {
		String[] specCodes = new String[] {
				"GT6NAB600 N478M",
				"GT6N",
				
				"GT6N      N478M     ",
				"GT6N      N589M     IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatched(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test1 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[0],result.getProductSpecCode());
	}
	
	@Test
	public void test2() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N      N589M     IN",
				"GT6NAB6",
				"GT6N      N478M     ",
				"GT6NAB600           IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatched(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test2 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[4],result.getProductSpecCode());
	}
	
	@Test
	public void test3() {
		String[] specCodes = new String[] {
				"GT6NAB6   N478N     I",
				"GT6N",
				"GT6NAB6",
				"GT6N      N478M1    IN",
				"GT6N      N589M     IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test3 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[2],result.getProductSpecCode());
	}
	
	/*
	 * existing lot control rule cannot tell the difference between "GT6NAB6               ",
		and	"GT6N                IN" because they have same hit counts = 1
	 */
	@Test
	public void test4() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N      N589M     IN",
				"GT6NAB6   N478M     IN",
				"GT6N      N478M     ",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test4 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[2],result.getProductSpecCode());
	}
	
	@Test
	public void test5() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N      N589M     IN",
				"GT6NAB6             IN",
				"GT6N      N478M     ",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test5 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[2],result.getProductSpecCode());
	}
	
	@Test
	public void test6() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N      N589M     IN",
				"GT6NAB6",
				"GT6N      N478M     ",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test6 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[2],result.getProductSpecCode());
	}
	
	@Test
	public void test7() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N   00 N478M     IN",
				"GT6NAB6X6S",
				"GT6N      N478M     ",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test7 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[1],result.getProductSpecCode());
	}
	
	@Test
	public void test8() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N   00 N478M",
				"GT6NAB6X6S",
				"GT6N      N478M     ",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test8 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[1],result.getProductSpecCode());
	}
	
	@Test
	public void test9() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N   00",
				"GT6NAB6X6S",
				"GT6N      N478M     ",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test9 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[1],result.getProductSpecCode());
	}
	
	@Test
	public void test10() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N      N478M     IN",
				"GT6NAB6X6S",
				"GT6N      N478M     ",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test10 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[1],result.getProductSpecCode());
	}
	
	@Test
	public void test11() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6N      N478M",
				"GT6NAB6X6S",
				"GT6N      N478M     IM",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test11 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[1],result.getProductSpecCode());
	}
	
	@Test
	public void test12() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6NAN6   N478M",
				"GT6NAB6X6S",
				"GT6N      N478M     IM",
				"GT6N                IN"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test12 result = " + result);
		assertNotNull(result);
		assertEquals( specCodes[4],result.getProductSpecCode());
	}
	
	@Test
	public void test13() {
		String[] specCodes = new String[] {
				"GT6N",
				"GT6NAN6   N478M",
				"GT6NAB6X6S",
				"GT6N      N478M     IM",
				"GT6N                I"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes), BuildAttribute.class);
		System.out.println("test13 result = " + result);
		assertNotNull(result);
		assertEquals(specCodes[0],result.getProductSpecCode());
	}
	
	@Test
	public void test14() {
		String[] specCodes1 = new String[] {
				"GT6NAN6   N478M",
				"GT6NAB6X6S",
				"GT6N      N478M     IM",
				"GT6N                I ",
				"G*"
		};
		
		String testSpecCode = "GT6NAB600 N478M     IN";
		BuildAttribute result1 = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes1), BuildAttribute.class);
		System.out.println("SpecCode - "+testSpecCode +" test14 result = " + result1);
		assertNotNull(result1);
		assertEquals(specCodes1[4],result1.getProductSpecCode());
		
		String[] specCodes2 = new String[] {
				"GT6NAN6   N478M",
				"GT6NAB6X6S",
				"GT6N      N478M     IM",
				"GT6N                I ",
				"*"
		};
		
		BuildAttribute result2 = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes2), BuildAttribute.class);
		System.out.println("SpecCode - "+testSpecCode +" test14 result = " + result2);
		assertNotNull(result2);
		assertEquals(specCodes2[4],result2.getProductSpecCode());
		String[] specCodes3 = new String[] {
				"GT6NAN6   N478M",
				"GT6NAB6X6S",
				"GT6N      N478M     IM",
				"GT6N                IN",
				"GT6NAB600",
				"*T6N",
				"G",
				"GT6N",
				"*"
		};
		
		BuildAttribute result3 = ProductSpecUtil.getMatchedItem(testSpecCode, createList(specCodes3), BuildAttribute.class);
		System.out.println("SpecCode - "+testSpecCode +" test14 result = " + result3);
		assertNotNull(result3);
		assertEquals(specCodes3[4],result3.getProductSpecCode());
		
		List<BuildAttribute> attributeList = ProductSpecUtil.getMatchedList(testSpecCode, createList(specCodes3), BuildAttribute.class);
		System.out.println("AttributeListSize-"+attributeList.size());
		int i=0;
		for(BuildAttribute attribute: attributeList){
			System.out.println(i+") "+attribute.getProductSpecCode());
			i++;
		}
	}
	
	private List<BuildAttribute> createList(String[] specCodes) {
		List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();
		for(String specCode : specCodes) {
			buildAttributes.add(new BuildAttribute(specCode,"TestAttr",specCode));
		}
		return buildAttributes;
	}
}
