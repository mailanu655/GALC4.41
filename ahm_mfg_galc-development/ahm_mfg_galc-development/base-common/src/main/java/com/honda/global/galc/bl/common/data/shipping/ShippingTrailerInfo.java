package com.honda.global.galc.bl.common.data.shipping;

import java.util.Vector;

import com.honda.global.galc.bl.common.data.model.Column;
import com.honda.global.galc.bl.common.data.model.IModel;
import com.honda.global.galc.bl.common.data.model.Column.ColumnType;

public class ShippingTrailerInfo implements IModel{
	
	
    private static final long serialVersionUID = 1L;
    
    private int trailerID;
	private String trailerNumber;
	private int scheduledQty;
	private int actualQty;
	private TrailerInfoStatus trailerInfoStatus;
	private java.sql.Date createTimeStamp;
	
	public ShippingTrailerInfo(){}
	
	public ShippingTrailerInfo(int trailerId){
	    this.trailerID = trailerId;
    }
    
    public ShippingTrailerInfo(ShippingTrailerInfo sti){
		this();
        this.setActualQty(sti.getActualQty());
        this.setScheduledQty(sti.getScheduledQty());
        this.setTrailerID(sti.getTrailerID());
        this.setTrailerNumber(sti.getTrailerNumber());
        this.setTrailerStatus(sti.getTrailerStatus());
    }
	
	public int getActualQty() {
		return actualQty;
	}
	public void setActualQty(int actualQty) {
		this.actualQty = actualQty;
	}
	public int getScheduledQty() {
		return scheduledQty;
	}
	public void setScheduledQty(int scheduledQty) {
		this.scheduledQty = scheduledQty;
	}
	public int getTrailerID() {
		return trailerID;
	}
	public void setTrailerID(int trailerID) {
		this.trailerID = trailerID;
	}
	public String getTrailerNumber() {
		return trailerNumber;
	}
	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}
	public TrailerInfoStatus getTrailerStatus() {
		return trailerInfoStatus;
	}
	
	public int getTrailerStatusValue() {
		return trailerInfoStatus.ordinal();
	}
	
	public void setTrailerStatusValue(int trailerStatus) {
		this.trailerInfoStatus = TrailerInfoStatus.convert(trailerStatus);
        if(this.trailerInfoStatus == null) this.trailerInfoStatus = TrailerInfoStatus.Waiting;
	}
    
	public void setTrailerStatus(TrailerInfoStatus trailerStatus){
		this.trailerInfoStatus = trailerStatus;
	}
    
    /**
     * Load one engine into the trailer
     *
     */
    
    public void loadEngine(){
        
        this.actualQty += 1;
        
        if(getScheduledQty() == getActualQty()) 
            setTrailerStatus(TrailerInfoStatus.PCMP);
        else if(getTrailerStatus() != TrailerInfoStatus.Loading)
                setTrailerStatus(TrailerInfoStatus.Loading);
        
    }
    
	public ShippingTrailerInfo clone(){
        return new ShippingTrailerInfo(this);
    }
	
	public static enum TrailerInfoStatus{
        Waiting,Loading,HOLD,PCMP, Completed, Devanned;
        public static TrailerInfoStatus convert(int num){
            if(num <0 || num >=values().length) return null;
            return values()[num];
       }
    }
	
  public Vector<Column> defineColumns() {
        Vector<Column> columns = new Vector<Column>();
        columns.add(new Column("TRAILER_ID",ColumnType.SmallInt,"TrailerID",true, true));
        columns.add(new Column("TRAILER_NUMBER",ColumnType.Char,"TrailerNumber",10));
        columns.add(new Column("SCH_QTY",ColumnType.SmallInt,"ScheduledQty"));
        columns.add(new Column("ACT_QTY",ColumnType.SmallInt,"ActualQty"));
        columns.add(new Column("STATUS",ColumnType.SmallInt,"TrailerStatusValue"));
        columns.add(new Column("CREATE_TIMESTAMP",ColumnType.Date,"CreateTimeStamp"));
        return columns;
    }

    public String defineTableName() {
        return "GALADM.HCM_TRAILER_INFO_TBX";
    }

	public java.sql.Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(java.sql.Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}   
	
	public String toString(){
        StringBuilder buf = new StringBuilder();
        
        buf.append("ShippingTrailerInfo(");
        buf.append("Trailer #=");
        buf.append(this.getTrailerNumber());
        buf.append(",Trailer ID=");
        buf.append(this.getTrailerID());
        buf.append(",Scheduled Qty=");
        buf.append(this.getScheduledQty());
        buf.append(",Actual Qty=");
        buf.append(this.getActualQty());
        buf.append(",TrailerInfoStatus =");
        buf.append(this.getTrailerStatus().name());
        buf.append(")");
        
        return buf.toString();
    }
}
