package com.honda.galc.client.teamleader;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.FilteredLabeledComboBoxCellEditor;
import com.honda.galc.client.ui.component.FilteredLabeledComboBoxCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DataCollectionImageDao;
import com.honda.galc.entity.enumtype.ParseStrategyType;
import com.honda.galc.entity.product.DataCollectionImage;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonPartUtility;

public class PartSpecTableModel extends SortableTableModel<PartSpec> {
	
	private static final long serialVersionUID = 1L;
	private List<DataCollectionImage> dcImages;
	private boolean editable = true;
	private static String MBPN_BASE5="\\w{5}.*+";
	
	public PartSpecTableModel(JTable table,List<PartSpec> items, boolean editable, boolean autoSize) {
		super(items, new String[] {"#","Part Name","Part Id", "Description","Part Mask","Max Attempts", "Measurement Count", "Comment", 
				"Part Mark", "Part #","Part Color","Parser","Parse Info","Image", "SN Scan Count"},table);
		this.setAlignment(JLabel.CENTER);
		if(!autoSize)
			this.setColumnWidths(new int[] {30,150,80,100,100,100,120,200,150,150,100,150,200,200,100});
		
		this.editable = editable;
		setParseStrategyComboCell();
		setImageIdComboBoxCell();
		table.getTableHeader().setReorderingAllowed(true);
	}
	
	
	public PartSpecTableModel(JTable table,List<PartSpec> items) {
		this(table,items,true);
	}
	
	public PartSpecTableModel(JTable table,List<PartSpec> items,boolean editable) {
		this(table, items, editable, false);
	}	
	
	private String parseString(String valueString, int length) {
	    if ( valueString != null && valueString.length() > length) {
			MessageDialog.showError(getParentComponent(),String.format("'%.10s...' value must not exceed %d characters in length",valueString, length));
			throw new IllegalArgumentException();
	    } else {
	    	return valueString;
	    }
    }
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isCellEditable (int row, int column){
		return editable && column >= 3 && column!=6;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
    	PartSpec partSpec = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return partSpec.getId().getPartName();
            case 2: return partSpec.getId().getPartId();
            case 3: return partSpec.getPartDescription();
            case 4: return getPartSerialNumberMaskDisplayName(partSpec.getPartSerialNumberMask());
            case 5: return partSpec.getPartMaxAttempts();
            case 6: return partSpec.getMeasurementCount();
            case 7: return partSpec.getComment();
            case 8: return partSpec.getPartMark();
            case 9: return partSpec.getPartNumber();
            case 10: return StringUtils.isBlank(partSpec.getPartColorCode()) ? "*" : partSpec.getPartColorCode();
            case 11: return getStrategyDisplayName(partSpec.getParseStrategy());
            case 12: return partSpec.getParserInformation();
            case 13: return getImageDisplayName(partSpec);
            case 14: return partSpec.getScanCount();
        }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		try{
			if(row < 0 || row >= getRowCount() || column <=2 || column >= getColumnCount()) return;
			super.setValueAt(value, row, column);
			PartSpec partSpec = getItem(row);
			String valueString = value == null ? null : value.toString().trim();
			switch(column) {
				case 3: partSpec.setPartDescription(parseString(valueString,128));break;
				case 4: partSpec.setPartSerialNumberMask(parseString(valueString,255));break;
				case 5: partSpec.setPartMaxAttempts(parseInt(valueString));break;
				case 6: partSpec.setMeasurementCount(parseInt(valueString));break;
				case 7: partSpec.setComment(parseString(valueString,256));break;
				case 8: partSpec.setPartMark(parseString(valueString,255));break;
				case 9:	partSpec.setPartNumber(parsePartNumber(valueString));break;
				case 10: partSpec.setPartColorCode(parseString(valueString,11));break;
				case 11: partSpec.setParseStrategy(ParseStrategyType.getName(parseString(valueString,32)));break;
				case 12: partSpec.setParserInformation(parseString(valueString,64));break;
				case 13: partSpec.setImageId(((DataCollectionImage) value).getImageId());break;
				case 14: partSpec.setScanCount(parseInt(valueString));break;
				default: return;
			}
			this.fireTableCellUpdated(row, column);
		}catch(Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + ": Error setting value " + value + " at row " + row + ", column " + column);
			return;
		}
	}    


	private String parsePartNumber(String valueString) {
		valueString = StringUtils.trimToNull(valueString);
		if ( valueString != null && (!valueString.matches(MBPN_BASE5))) {
			MessageDialog.showError(getParentComponent(),String.format("'%.10s...' invalid Part Number",valueString));				
			throw new IllegalArgumentException();
		} else {
			return valueString;
		}
	}


	private Object getStrategyDisplayName(String strategy) {
		String strategyName = StringUtils.trimToEmpty(strategy);
		if (strategyName.equals(""))
			return "";
		
		ParseStrategyType type = null;
		try {
			type = ParseStrategyType.valueOf(strategyName);
		} catch (Exception ex) {}
		
		if (type != null) return type.getDisplayName();
		else return "";
	}

	private void setParseStrategyComboCell() {
		Object[] parseStrategys = getParseStrategyTypes().toArray();
		TableColumn col = table.getColumnModel().getColumn(11);
		col.setCellEditor(new ComboBoxCellEditor(parseStrategys,false));
		col.setCellRenderer(new ComboBoxCellRender(parseStrategys));
	}

	private ArrayList<String> getParseStrategyTypes() {
		ParseStrategyType[] types = ParseStrategyType.values();
		List<String> asList = new ArrayList<String>();
		for (ParseStrategyType type : types) {
			asList.add(type.name());
		}
		return ParseStrategyType.getComboDisplayNames(asList);
	}
	
	private String getPartSerialNumberMaskDisplayName(String partSerialNumberMask){
		
		String amask = StringUtils.replace(partSerialNumberMask,"<<"+CommonPartUtility.WILD_CARD_MULTI_CHARS+">>",String.valueOf(CommonPartUtility.WILD_CARD_MULTI_CHARS));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_CHAR+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_CHAR));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_NUMBER+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_NUMBER));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ANYTHING+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ANYTHING));
		return amask;
	}	

	private String getImageDisplayName(PartSpec spec) {
		for(DataCollectionImage image : getDcImages()) {
			if(image.getImageId() == spec.getImageId()) {
				return image.toString();
			}
		}
		return "";
	}

	private void setImageIdComboBoxCell() {
		DataCollectionImage[] values = (DataCollectionImage[]) getDcImages().toArray(new DataCollectionImage[getDcImages().size()]);
		TableColumn col = table.getColumnModel().getColumn(13);
		table.putClientProperty("terminateEditOnFocusLost",true);
		col.setCellRenderer(new FilteredLabeledComboBoxCellRender(values) );
		col.setCellEditor(new FilteredLabeledComboBoxCellEditor( values, true ) );		
	}
	
	public List<DataCollectionImage> getDcImages() {
		if(dcImages == null) {
			dcImages = new ArrayList<DataCollectionImage>();
			DataCollectionImage dummyImage = new DataCollectionImage();
			dummyImage.setImageId(0);
			dummyImage.setImageName("No Image");
			dcImages.add(dummyImage);
			dcImages.addAll(ServiceFactory.getDao(DataCollectionImageDao.class).findAllActiveWithoutImageData());
		}
		return dcImages;
	}

	public void setDcImages(List<DataCollectionImage> dcImages) {
		this.dcImages = dcImages;
	}
}
