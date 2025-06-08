package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.util.CommonPartUtility;

public class PartByProductSpecTableModel extends BaseTableModel<PartByProductSpecCode> {

	
	private static final long serialVersionUID = 1L;

	public PartByProductSpecTableModel(JTable table,List<PartByProductSpecCode> items) {
		super(items, new String[] {"#","Part Name","Part Id", "Description","Part Mask","Max Attempts", "Measurement Count", "Comment", "SN Scan Count"},table);
		setColumnWidths(new int[] {30,150,80,100,100,100,120,200,100});
	}
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		PartByProductSpecCode spec = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return spec.getId().getPartName();
            case 2: return spec.getId().getPartId();
            case 3: return spec.getPartSpec().getPartDescription();
            case 4: return getPartSerialNumberMaskDisplayName(spec.getPartSpec().getPartSerialNumberMask());
            case 5: return spec.getPartSpec().getPartMaxAttempts();
            case 6: return spec.getPartSpec().getMeasurementCount();
            case 7: return spec.getPartSpec().getComment();
            case 8: return spec.getPartSpec().getScanCount();
        }
        return null;
    }
	private String getPartSerialNumberMaskDisplayName(String partSerialNumberMask){
		
		String amask = StringUtils.replace(partSerialNumberMask,"<<"+CommonPartUtility.WILD_CARD_MULTI_CHARS+">>",String.valueOf(CommonPartUtility.WILD_CARD_MULTI_CHARS));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_CHAR+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_CHAR));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_NUMBER+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_NUMBER));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ANYTHING+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ANYTHING));
		return amask;
	}	

}
