package com.honda.global.galc.bl.common.data.shipping;

import java.util.Comparator;
import java.util.Vector;

import com.honda.global.galc.bl.common.data.model.Column;
import com.honda.global.galc.bl.common.data.model.IModel;
import com.honda.global.galc.bl.common.data.model.Column.ColumnType;


public class ShippingQuorumDetail implements IModel, Comparator{
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
 	private java.sql.Date quorumDate;
	private int quorumID;
	private int quorumSeq;
	private String kdLot;
	private String productSpec;
	private String engineNumber;
	private QuorumDetailStatus status = QuorumDetailStatus.autoLoading;
	
	private int LESS_THAN = -1;
	private int GREATER_THAN = 1;
	
	public String getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	public String getKdLot() {
		return kdLot;
	}
	
	public String getPlant() {
		return kdLot == null ? ""  : kdLot.substring(0,6);
	}
	
	public void setKdLot(String kdLot) {
		this.kdLot = kdLot;
	}
	public String getProductSpec() {
		return productSpec;
	}
	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
	}
	public java.sql.Date getQuorumDate() {
		return quorumDate;
	}
	public void setQuorumDate(java.sql.Date quorumDate) {
		this.quorumDate = quorumDate;
	}
	public int getQuorumID() {
		return quorumID;
	}
	public void setQuorumID(int quorumID) {
		this.quorumID = quorumID;
	}
	public int getQuorumSeq() {
		return quorumSeq;
	}
	public void setQuorumSeq(int quorumSeq) {
		this.quorumSeq = quorumSeq;
	}
	
	public QuorumDetailStatus getStatus() {
		return status;
	}
	
	public int getStatusValue() {
		return status.ordinal();
	}
	
	public void setStatusValue(int status) {
		this.status = QuorumDetailStatus.convert(status);
        if(this.status == null) this.status = QuorumDetailStatus.autoLoading;
	}
	
	public void setStatus(QuorumDetailStatus status) {
		this.status = status;
	}
	
	/**
	 * This method is to compare the two passed objects, always compare the production date first, followed by quorum ID
	 * and quorum sequence.
	 */
	public int compare(Object o1, Object o2) {
		int result = 0;
		ShippingQuorumDetail detail1 = (ShippingQuorumDetail)o1;
		ShippingQuorumDetail detail2 = (ShippingQuorumDetail)o2;
		if (detail1.getQuorumDate().before(detail2.getQuorumDate())){
			result = LESS_THAN;
		}else if (detail1.getQuorumDate().after(detail2.getQuorumDate())){
			result = GREATER_THAN;
		} else if (detail1.getQuorumID() < detail2.getQuorumID()){
			result = LESS_THAN;
		} else if (detail1.getQuorumID() > detail2.getQuorumID()){
			result = GREATER_THAN;
		} else if (detail1.getQuorumSeq() < detail2.getQuorumSeq()){
			result = LESS_THAN;
		} else if (detail1.getQuorumSeq() > detail2.getQuorumSeq()){
			result = GREATER_THAN;
		}
		return result;
	}

    public Vector<Column> defineColumns() {
        Vector<Column> columns = new Vector<Column>();
        columns.add(new Column("QUORUM_DATE",ColumnType.Date,"QuorumDate",true));
        columns.add(new Column("QUORUM_ID",ColumnType.SmallInt,"QuorumID",true));
        columns.add(new Column("QUORUM_SEQ",ColumnType.SmallInt,"QuorumSeq", true));
        columns.add(new Column("KD_LOT",ColumnType.Char,"KdLot", 18));
        columns.add(new Column("YMTO",ColumnType.Char,"ProductSpec", 9));
        columns.add(new Column("ENGINE_NUMBER",ColumnType.Char,"EngineNumber", 12, false, true));
        columns.add(new Column("STATUS",ColumnType.SmallInt,"StatusValue"));
        return columns;
    }

    public String defineTableName() {
        return "GALADM.HCM_QUORUM_DETAIL_TBX";
    }
    
    public static enum QuorumDetailStatus{
        autoLoading, manualLoading, misLoading;
        public static QuorumDetailStatus convert(int num){
            if(num <0 || num >=values().length) return null;
            return values()[num];
       }
    }
    
    public String toString(){
        StringBuilder buf = new StringBuilder();
        
        buf.append("ShippingQuorumDetail(");
        buf.append("quorum Date =");
        buf.append(this.getQuorumDate());
        buf.append(",quorum ID =");
        buf.append(this.getQuorumID());
        buf.append(",quorum Seq =");
        buf.append(this.getQuorumSeq());
        buf.append(",KD Lot =");
        buf.append(this.getKdLot());
        buf.append(",YMTO =");
        buf.append(this.getProductSpec());
        buf.append(",engine Number =");
        buf.append(this.getEngineNumber());
        buf.append(",Quorum Detail Status =");
        buf.append(this.getStatus().name());
        buf.append(")");
        
        return buf.toString();
    }
	
}
