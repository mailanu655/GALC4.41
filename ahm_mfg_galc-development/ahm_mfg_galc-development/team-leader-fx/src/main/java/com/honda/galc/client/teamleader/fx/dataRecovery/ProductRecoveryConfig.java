package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductRecoveryConfig</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4> a <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD> L&T Infotech</TD>
 * <TD>Jul 17, 20178</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class ProductRecoveryConfig {

	private static Map<String, ProductRecoveryConfig> instances = new HashMap<String, ProductRecoveryConfig>();

	public static final ProductRecoveryConfig BLOCK = new ProductRecoveryConfig("BLOCK", "Block DC/MC Data Maintenance", ProductType.BLOCK, KeyEvent.VK_B);
	public static final ProductRecoveryConfig HEAD = new ProductRecoveryConfig("HEAD", "Head DC/MC Data Maintenance", ProductType.HEAD, KeyEvent.VK_H);
	public static final ProductRecoveryConfig BLOCK_4EM = new ProductRecoveryConfig("BLOCK_4EM", "Block DC/MC 4EM", ProductType.BLOCK, KeyEvent.VK_B);
	public static final ProductRecoveryConfig HEAD_4EM = new ProductRecoveryConfig("HEAD_4EM", "Head DC/MC 4EM", ProductType.HEAD, KeyEvent.VK_H);

	private String configId;
	private String title;
	private ProductType productType;
	private int keyEvent;
	private int sequence;
	private List<PartDefinition> partDefinitions = new ArrayList<PartDefinition>();

	public ProductRecoveryConfig(String configId, String title, ProductType productType, int keyEvent) {
		this.configId = configId;
		this.title = title;
		this.productType = productType;
		this.keyEvent = keyEvent;
		getInstances().put(getConfigId(), this);
	}

	// === get/set === //
	public static ProductRecoveryConfig getConfig(String configId) {
		return getInstances().get(configId);
	}

	public String getScreenName() {
		return getConfigId().replaceAll("_", " ");
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public int getKeyEvent() {
		return keyEvent;
	}

	public void setKeyEvent(char keyEvent) {
		this.keyEvent = keyEvent;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private static Map<String, ProductRecoveryConfig> getInstances() {
		return instances;
	}

	@Override
	public String toString() {
		return getConfigId();
	}

	public List<PartDefinition> getPartDefinitions() {
		return partDefinitions;
	}

	public void setPartDefinitions(List<PartDefinition> partDefinitions) {
		this.partDefinitions = partDefinitions;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}
