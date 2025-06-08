package com.honda.galc.device.bbscale;

public enum Corner {
	BBSCALES_LFRT(1),
    BBSCALES_LRR(2),
    BBSCALES_RFRT(3),
    BBSCALES_RRR(4),
    BBSCALES_LCR(5),
    BBSCALES_RCR(6),
    BBSCALES_TOTAL(7),
    BBSCALES_CRDIFF(8);
    
    private String altName;
    private int measurementSequence;
    
    private Corner(int measurementSequence) {
		this.measurementSequence = measurementSequence;
		altName = name();
	}
	
	public int getMeasurementSequence() {
		return measurementSequence;
	}
   
    public String getAltName() {
		return altName;
	}

	public void setAltName(String altName) {
		this.altName = altName;
	}

	public static Corner getCorner(int i)  {
        Corner whichCorner = null;
        for (Corner c: Corner.values())  {
            if(i == c.ordinal())  {
                whichCorner = c;
            }
        }
        return whichCorner;
    }
}
