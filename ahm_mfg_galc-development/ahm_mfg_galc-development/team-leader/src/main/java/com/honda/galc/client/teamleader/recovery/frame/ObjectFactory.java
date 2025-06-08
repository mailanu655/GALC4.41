package com.honda.galc.client.teamleader.recovery.frame;

import com.honda.galc.client.teamleader.recovery.frame.PartDefinition.ElementType;
import com.honda.galc.client.teamleader.recovery.frame.PartDefinition.ValueType;

/**
 * 
 * <h3>ObjectFactory Class description</h3>
 * <p>
 * ObjectFactory description
 * </p>
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
 * @author Jeffray Huang<br>
 *         Dec 23, 2011
 * 
 * 
 */
public class ObjectFactory {

	public static PartDataPanel createProductDataPanel(ProductRecoveryConfig config, DataRecoveryController controller) {

		// === predefined === //
		if (ProductRecoveryConfig.BLOCK_4EM.equals(config)) {
			return createBlock4emDataPanel(controller);
		}
		if (ProductRecoveryConfig.HEAD_4EM.equals(config)) {
			return createHead4emDataPanel(controller);
		}

		if (ProductRecoveryConfig.BLOCK.equals(config)) {
			return createBlockDataPanel(controller);
		}
		if (ProductRecoveryConfig.HEAD.equals(config)) {
			return createHeadDataPanel(controller);
		}

		// === generic/configurable === //
		return createGenericDataPanel(config, controller);
	}

	private static PartDataPanel createBlockDataPanel(DataRecoveryController controller) {
		PartDataPanel panel = new PartDataPanel(controller);
		panel.addPartDefinition(new PartDefinition("LOWER BLOCK NUMBER", "Lower Block SN", true));
		panel.addPartDefinition(
				new PartDefinition("MC BLOCK LEAK TEST", "Leak Test Status", true, 2
					,new PartDefinition("WATER JACKET", "Water Jacket", false)
					,new PartDefinition("INTERNAL OIL HOLE", "Internal Oil Hole", false)
					,new PartDefinition("CRANK ROOM", "Crank Room", false)
					,new PartDefinition("HP OILWAY", "HP Oilway", false)
			)
		);
		panel.addPartDefinition(
				new PartDefinition("Crank Journal Ranking", 1
					,new PartDefinition("CRANK JOURNAL 1", 1, true)
					,new PartDefinition("CRANK JOURNAL 2", 1, true)
					,new PartDefinition("CRANK JOURNAL 3", 1, true)
					,new PartDefinition("CRANK JOURNAL 4", 1, true)
					,new PartDefinition("CRANK JOURNAL 5", 1, true)
				)
		);
		panel.addPartDefinition(new PartDefinition("CRANK JOURNAL MEASURE", "Crank Journal", ElementType.STATUS, true));
		return panel;
	}

	private static PartDataPanel createHeadDataPanel(DataRecoveryController controller) {
		PartDataPanel panel = new PartDataPanel(controller);
		panel.addPartDefinition(
				new PartDefinition("Furnace Count", 1 
					,new PartDefinition("SOLUTION_COUNT", "Solution", 5, ValueType.INTEGER, false)
					,new PartDefinition("AGE_FURNACE_COUNT", "Age", 5, ValueType.INTEGER, false)
				)
		);
		panel.addPartDefinition(new PartDefinition("HARDNESS", "Hardness Results", 14, ValueType.FLOAT, ElementType.STATUS_VALUE, 2, ",", true));
		panel.addPartDefinition(
				new PartDefinition("MC HEAD LEAK TEST", "Leak Test Status", true, 2
				    ,new PartDefinition("COMBUST CHMBR VTEC", "Com Chmbr VTec", false)
					,new PartDefinition("OIL GALLERY", "Oil Gallery", false)
				    ,new PartDefinition("OIL HOLE EGR", "Oil Hole EGR", false)
				    ,new PartDefinition("WATER JACKET", "Water Jacket", false)
				)
		);
		panel.addPartDefinition(new PartDefinition("CAM JOURNAL MEASURE", "Cam Journal Status", ElementType.STATUS, true));
		return panel;
	}

	
	private static PartDataPanel createBlock4emDataPanel(DataRecoveryController controller) {
		PartDataPanel panel = new PartDataPanel(controller);
		panel.addPartDefinition(
				new PartDefinition("MC BLOCK LEAK TEST", "Leak Test Status", true, 2
					,new PartDefinition("WATER JACKET BLOCK", "Water Jacket", false)
					,new PartDefinition("CRANK ROOM", "Crank Room", false)
					,new PartDefinition("OIL PATH BLOCK", "Oil Path", false)
				)
		);	
		panel.addPartDefinition(
				new PartDefinition("BCAP MEASURE", "BCAP", true, 2
					,new PartDefinition("BCAP 1", "1", ElementType.STATUS, false)
					,new PartDefinition("BCAP 3", "3", ElementType.STATUS, false)
					,new PartDefinition("BCAP 5", "5", ElementType.STATUS, false)
					,new PartDefinition("BCAP 7", "7", ElementType.STATUS, false)
					,new PartDefinition("BCAP 9", "9", ElementType.STATUS, false)
					,new PartDefinition("BCAP 2", "2", ElementType.STATUS, false)
					,new PartDefinition("BCAP 4", "4", ElementType.STATUS, false)
					,new PartDefinition("BCAP 6", "6", ElementType.STATUS, false)
					,new PartDefinition("BCAP 8", "8",  ElementType.STATUS, false)
					,new PartDefinition("BCAP 10", "10",  ElementType.STATUS, false)
				)
		);
		panel.addPartDefinition( 
				new PartDefinition("CRANK JOURNAL MEASURE", "Crank Journal", true, 1
					,new PartDefinition("CRANK JOURNAL 1", 1, true)
					,new PartDefinition("CRANK JOURNAL 2", 1, true)
					,new PartDefinition("CRANK JOURNAL 3", 1, true)
					,new PartDefinition("CRANK JOURNAL 4", 1, true)
					,new PartDefinition("CRANK JOURNAL 5", 1, true)
				)
		);
		
		panel.addPartDefinition(new PartDefinition("IMPREGNATION_COUNT", "Impregnation", 1, ValueType.INTEGER, true));
		return panel;
	}
	
	private static PartDataPanel createHead4emDataPanel(DataRecoveryController controller) {
		PartDataPanel panel = new PartDataPanel(controller);
		panel.addPartDefinition(new PartDefinition("SOLUTION_COUNT", "Solution", 5, ValueType.INTEGER, true));
		panel.addPartDefinition(new PartDefinition("HARDNESS", "Hardness Results", 14,  ValueType.FLOAT, ElementType.STATUS_VALUE, true));
		panel.addPartDefinition(
				new PartDefinition("MC HEAD LEAK TEST", "Leak Test Status", true, 2
					,new PartDefinition("WATER JACKET HEAD", "Water Jacket", false)
					,new PartDefinition("OIL PATH HEAD", "Oil Path", false)
					,new PartDefinition("CAM ROOM", "Cam Room", false)
			)
		);	
		panel.addPartDefinition(new PartDefinition("MC DI JUDGMENT", "DI Status", ElementType.STATUS, true));
		return panel;
	}

	private static PartDataPanel createGenericDataPanel(ProductRecoveryConfig config, DataRecoveryController controller) {
		PartDataPanel panel = new PartDataPanel(controller);
		if (config != null && config.getPartDefinitions() != null) {
			for (PartDefinition partDefinition : config.getPartDefinitions()) {
				panel.addPartDefinition(partDefinition);
			}
		}
		return panel;
	}
}
