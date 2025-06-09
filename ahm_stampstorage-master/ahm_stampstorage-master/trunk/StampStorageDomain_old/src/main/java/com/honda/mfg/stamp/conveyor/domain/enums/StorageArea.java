package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * Areas used to store carriers.
 * User: VCC30690
 * Date: 10/20/11
 */
public enum StorageArea {

	/**  Area used to queue carriers to weld*/ Q_AREA(0) , C_HIGH(1) , C_LOW(2), A_AREA(3), S_AREA(4), B_AREA(5)  ;
	private int type;
	
	StorageArea (int type){
		this.type = type;
	}
	
    public static StorageArea findByType(int type) {
        StorageArea[] items = StorageArea.values();
        for (int i = 0; i < items.length; i++) {
            StorageArea s = items[i];
            if (type == s.type()) {
                return s;
            }
        }
        return Q_AREA;
    }
    public int type() {
        return this.type;
    }
}
