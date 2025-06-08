package com.honda.test.product;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.ManifestDataHelper;
import com.honda.galc.util.ProductManifestDataUtil;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;
/**
 * 
 * <h3>ProductManifestDataUtilTest Class description</h3>
 * <p> ProductManifestDataUtilTest description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou<br>
 * Oct.13, 2020
 *
 *
 */
public class ProductManifestDataUtilTest {

	
	@Test
	@SuppressWarnings("unchecked")
	public void testHead() {
		String clientId1 = "TEST_HEAD";
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), "5BA0HC0306B6G036G");
		dc.put(TagNames.PRODUCT_TYPE.name(), "HEAD");
		
		Device dev1 = new Device();
		dev1.setClientId(clientId1);
		
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		list.add(new DeviceFormat(clientId1, DataContainerTag.Product, null,DeviceTagType.ENTITY));
		list.add(new DeviceFormat(clientId1, "HeadBuildResult", null,DeviceTagType.ENTITY));
		list.get(1).setTagValue("(Part_Name in ('VALVE LEAK') OR Part_Name like 'CAM JOURNAL%')");
		list.add(new DeviceFormat(clientId1, "BlockBuildResult", null,DeviceTagType.ENTITY));
		list.get(2).setTagValue("(Part_Name in ('BLOCK BORE MEASURE') OR Part_Name like 'CRANK JOURNAL%')");
		dev1.setDeviceDataFormats(list);
		
		String jsonMq = ProductManifestDataUtil.getManifestDataJsonString(dc, dev1);
		
		
		// Received side 
		Map<String, String> fromMq = ProductManifestDataUtil.getGson().fromJson(jsonMq, HashMap.class);
		fromMq.remove(TagNames.PRODUCT_TYPE.name());
		
		for(String key : fromMq.keySet()) {
			if(key.equals(DataContainerTag.Product)) {
				BaseProduct fromJson = ProductManifestDataUtil.fromJson(fromMq.get(DataContainerTag.Product), ProductType.valueOf("HEAD"));
				assertTrue(fromJson.getProductId().equals("5BA0HC0306B6G036G"));
				assertTrue(fromJson.getClass().getSimpleName().equals("Head"));
				ProductManifestDataUtil.save(fromJson);
			} else {
				List<? extends AuditEntry> listFromJson = ProductManifestDataUtil.fromJson(key, fromMq.get(key));
				if(key.equals("HeadBuildResult")) {
					assertTrue(listFromJson.size() == 13);
				} else if(key.equals("BlockBuildResult")){
					assertTrue(listFromJson.size() == 0);	
				}
				ProductManifestDataUtil.saveAll(listFromJson);
			}
				
		}
		
		ProductManifestDataUtil.saveManifestDataFromJsonString(jsonMq, null);
	}


	
	@Test
	@SuppressWarnings("unchecked")
	public void testBlock() {
		String clientId1 = "TEST_BLOCK";
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), "5BA0HC0731240786F");
		dc.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		
		Device dev1 = new Device();
		dev1.setClientId(clientId1);
		
		DataContainer printAttrDc = new DefaultDataContainer();
		
		DeviceFormat df1 = new DeviceFormat(clientId1, DataContainerTag.Product, null,DeviceTagType.ENTITY);
		DeviceFormat df2 = new DeviceFormat(clientId1, "BlockBuildResult", null,DeviceTagType.ENTITY);
		df2.setTagValue("(Part_Name in ('BLOCK BORE MEASURE') OR Part_Name like 'CRANK JOURNAL%')");
		
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		list.add(df1);
		list.add(df2);
		dev1.setDeviceDataFormats(list);
		
		
		String prodType = ProductManifestDataUtil.findProductType(dc);
		
		for(DeviceFormat df : dev1.getDeviceDataFormats()) {
			String entityStr = ManifestDataHelper.getInstance().findEntity(df, dc, prodType);
			printAttrDc.put(df.getTag(), entityStr);
		}
		
		printAttrDc.put(TagNames.PRODUCT_TYPE.name(), dc.getString(TagNames.PRODUCT_TYPE.name()));
		
		String jsonMq = ProductManifestDataUtil.getGson().toJson(printAttrDc);
		
		
		// Received side 
		Map<String, String> fromMq = ProductManifestDataUtil.getGson().fromJson(jsonMq, HashMap.class);
		
		String productType = fromMq.get(TagNames.PRODUCT_TYPE.name());
		fromMq.remove(TagNames.PRODUCT_TYPE.name());
		
		for(String key : fromMq.keySet()) {
			if(key.equals(DataContainerTag.Product)) {
				BaseProduct fromJson = ProductManifestDataUtil.fromJson(fromMq.get(DataContainerTag.Product), ProductType.valueOf(productType));
				assertTrue(fromJson.getProductId().equals("5BA0HC0731240786F"));
				assertTrue(fromJson.getClass().getSimpleName().equals("Block"));
				ProductManifestDataUtil.save(fromJson);
			} else {
				List<? extends AuditEntry> listFromJson = ProductManifestDataUtil.fromJson(key, fromMq.get(key));
				assertTrue(listFromJson.size() == 7);
				ProductManifestDataUtil.saveAll(listFromJson);
			}
				
		}
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testEngine() {
		String clientId1 = "TEST_ENGINE";
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), "K20C25322981");
		dc.put(TagNames.PRODUCT_TYPE.name(), "ENGINE");
				
		Device dev1 = new Device();
		dev1.setClientId(clientId1);
		
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		list.add(new DeviceFormat(clientId1, DataContainerTag.Product, null,DeviceTagType.ENTITY));
		list.add(new DeviceFormat(clientId1, "InstalledPart", null,DeviceTagType.ENTITY));
		list.get(1).setTagValue("Part_Name ='FWDP'");
		list.add(new DeviceFormat(clientId1, "Measurement", null,DeviceTagType.ENTITY));
		list.get(2).setTagValue("Part_Name ='FWDP'");
		dev1.setDeviceDataFormats(list);
		
		String jsonMq = ProductManifestDataUtil.getManifestDataJsonString(dc, dev1);
		
		
		// Received side 
		Map<String, String> fromMq = ProductManifestDataUtil.getGson().fromJson(jsonMq, HashMap.class);
		
		String productType = fromMq.get(TagNames.PRODUCT_TYPE.name());
		fromMq.remove(TagNames.PRODUCT_TYPE.name());
		
		for(String key : fromMq.keySet()) {
			if(key.equals(DataContainerTag.Product)) {
				BaseProduct fromJson = ProductManifestDataUtil.fromJson(fromMq.get(DataContainerTag.Product), ProductType.valueOf(productType));
				assertTrue(fromJson.getProductId().equals("K20C25322981"));
				assertTrue(fromJson.getClass().getSimpleName().equals("Engine"));
				ProductManifestDataUtil.save(fromJson);
			} else {
				List<? extends AuditEntry> listFromJson = ProductManifestDataUtil.fromJson(key, fromMq.get(key));
				
				if(listFromJson.size() > 0 && listFromJson.get(0).getClass().getSimpleName().equals("InstalledPart"))
					assertTrue(listFromJson.size() == 1);
				if(listFromJson.size() > 0 && listFromJson.get(0).getClass().getSimpleName().equals("Measurement"))
					assertTrue(listFromJson.size() == 8);
				
				ProductManifestDataUtil.saveAll(listFromJson);
			}
				
		}
	}
	
	@Test
	public void testFrame() {
		String clientId1 = "TEST_FRAME";
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), "2HGFC2F86LH596856");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		
		Device dev1 = new Device();
		dev1.setClientId(clientId1);
		
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		list.add(new DeviceFormat(clientId1, DataContainerTag.Product, null,DeviceTagType.ENTITY));
		list.add(new DeviceFormat(clientId1, "ProductResult", null,DeviceTagType.ENTITY));
		list.get(1).setTagValue("PROCESS_POINT_ID ='1AF1D1001'");
		list.add(new DeviceFormat(clientId1, "DefectResult", null,DeviceTagType.ENTITY));
				
		dev1.setDeviceDataFormats(list);

		String jsonMq = ProductManifestDataUtil.getManifestDataJsonString(dc, dev1);
		
		// Received side 
		ProductManifestDataUtil.saveManifestDataFromJsonString(jsonMq, null);
	}
	
	@Test
	public void testMbpn() {
		String clientId1 = "TEST_MBPN";
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), "5BA1GK0616708826");
		dc.put(TagNames.PRODUCT_TYPE.name(), "MBPN");
		
		Device dev1 = new Device();
		dev1.setClientId(clientId1);
		
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		list.add(new DeviceFormat(clientId1, DataContainerTag.Product, null,DeviceTagType.ENTITY));
		list.add(new DeviceFormat(clientId1, "InstalledPart", null,DeviceTagType.ENTITY));
		dev1.setDeviceDataFormats(list);
		
		String jsonMq = ProductManifestDataUtil.getManifestDataJsonString(dc, dev1);
		
		ProductManifestDataUtil.saveManifestDataFromJsonString(jsonMq, null);
	}
	
	@Test
	public void testSubProduct() {
		String clientId1 = "TEST_SUBPRODUCT";
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), "5121ATBAA0K039953");
		dc.put(TagNames.PRODUCT_TYPE.name(), "KNUCKLE");
		
		Device dev1 = new Device();
		dev1.setClientId(clientId1);
		
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		list.add(new DeviceFormat(clientId1, DataContainerTag.Product, null,DeviceTagType.ENTITY));
		list.add(new DeviceFormat(clientId1, "InstalledPart", null,DeviceTagType.ENTITY));
		dev1.setDeviceDataFormats(list);
		
		String jsonMq = ProductManifestDataUtil.getManifestDataJsonString(dc, dev1);
		
		ProductManifestDataUtil.saveManifestDataFromJsonString(jsonMq, null);
	}
	
	
	@BeforeClass
	public static void loadConfig() {
		DBUtils.loadConfigDb2();
		DBUtils.readAndExecuteSqlFromFile("sql/service/product/product_manifest.sql");
		TestUtils.startWebServer();

	}
}
	
