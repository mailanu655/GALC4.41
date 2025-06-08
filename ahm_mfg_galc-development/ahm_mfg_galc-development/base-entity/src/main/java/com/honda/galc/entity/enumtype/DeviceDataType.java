package com.honda.galc.entity.enumtype;

import java.sql.Timestamp;

import com.honda.galc.device.dataformat.BitArray;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>DeviceDataType</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceDataType description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 22, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 22, 2013
 */
public enum DeviceDataType implements IdEnum<DeviceDataType> {

	BOOLEAN		(0, Boolean.class,false), 
	SHORT		(1, Short.class,0), 
	INTEGER		(2, Integer.class,0), 
	FLOAT		(3, Float.class,0.0), 
	STRING		(4, String.class,""),
	DOUBLE		(5, Double.class,0.0),
	PLC_BOOLEAN (6, PlcBoolean.class),
	BIT_ARRAY	(7, BitArray.class),
	TIMESTAMP	(8, Timestamp.class);

    private final int _id;
    private final Class<?> _clazz;
    private Object defaultValue = null;

    private DeviceDataType(int id, Class<?> clazz,Object defaultValue) {
    	this(id,clazz);
    	this.defaultValue = defaultValue;
    }
        
    private DeviceDataType(int id, Class<?> clazz) {
        _id = id;
        _clazz = clazz;
    }

    public int getId() {
        return _id;
    }
   
    public Class<?> getClazz() {
        return _clazz;
    }
    
    public Object getDefaultValue() {
		return defaultValue;
	}

	public static DeviceDataType getType(int id) {
        return EnumUtil.getType(DeviceDataType.class, id);
    }
}
