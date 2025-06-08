package com.honda.galc.qics.mobile.client;

import com.honda.galc.qics.mobile.client.widgets.VinInfoPanel;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextItem;

/**
 * The Class DefectViewerPanel shows a defect in a read-only form.  
 */
public class DefectViewerPanel extends ScrollablePanel {

	
	
    public DefectViewerPanel( VinDefectsModel vinDefectsModel ) {
		super("Defect Viewer");
		
		VinInfoPanel vinInfoPanel = new VinInfoPanel("Vin Info", vinDefectsModel );

		
		TextItem defectTypeItem = new TextItem("defectTypeName", "Defect Type");
		TextItem entryStationIdItem = new TextItem("entryStationID", "Entry Station ID");
		CheckboxItem outstandingItem = new CheckboxItem("outStanding", "Outstanding"); 
		TextItem actualProblemNameItem = new TextItem("actualProblemName", "Actual Problem Name");
		TextItem repairMethodNameItem = new TextItem("repairMethodName", "Repair Method");
		TextItem repairTimeItem = new TextItem("repairTime", "Repair Time (Minutes)");
		CheckboxItem isChangeAtRepairItem = new CheckboxItem("isChangeAtRepair", "Changed At Repair");
		TextItem twoPaitPartNameItem = new TextItem("twoPartPairPart", "Pair Part Name");
		TextItem twoPairPartLocationNameItem = new TextItem("twoPartPairLocation", "Pair Part Location");
		TextItem descriptionIdItem = new TextItem("descriptionID", "Description ID");
		TextItem secondaryPartNameItem = new TextItem("secondaryPartName", "Seconday Part Name");
		TextItem inspectionPartNameItem = new TextItem("inspectionPartName", "Inspection Part Name");
		TextItem inspectionPartLocationItem = new TextItem("inspectionPartLocationName", "Inspection Part Location");
		TextItem statusItem = new TextItem("status", "Status");

		FormItem[] formItemArray = new FormItem[] {
				 statusItem, 
				 inspectionPartNameItem,
				 inspectionPartLocationItem,
				 descriptionIdItem,
				 secondaryPartNameItem,
				 defectTypeItem,
				 entryStationIdItem,
				 outstandingItem, 
				 actualProblemNameItem,
				 repairMethodNameItem,
				 repairTimeItem,
				 isChangeAtRepairItem,
				 twoPaitPartNameItem,
				 twoPairPartLocationNameItem
		};
		
		Record record = vinDefectsModel.getDefectRecord();
		for( FormItem f : formItemArray ) {
			// make all the entries readOnly
			f.setDisabled(true);
			// set the items value
			String name = f.getName();
			Object value = record.get(name);
			if ( value != null ) {
				if ( value instanceof String ) {
					f.setValue( value );
				} else if ( value instanceof Boolean ) {
					f.setValue( value );
				} else {
					f.setValue(value.toString());
				}
			} 			
		}
		
		DynamicForm dynamicForm = new DynamicForm();
        dynamicForm.setFields(formItemArray);
        
        addMember( vinInfoPanel );
        addMember(dynamicForm);  
      }
        

}
