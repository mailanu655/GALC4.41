package com.honda.global.galc.bl.common.data.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.honda.global.galc.bl.common.data.model.Column;
import com.honda.global.galc.bl.common.data.model.IModel;
import com.honda.global.galc.bl.common.data.model.Column.ColumnType;
import com.honda.global.galc.bl.common.data.shipping.ShippingQuorumDetail.QuorumDetailStatus;
import com.honda.global.galc.bl.common.data.shipping.ShippingTrailerInfo.TrailerInfoStatus;

public class ShippingQuorum  implements IModel,Comparator{
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int LENGTH_KD_LOT = 18;
	public static final int LENGTH_PREFIX_KD_LOT = 6;
	
	private java.sql.Date quorumDate;
	private long kdLotNumber;
	private int quorumID;
	private String trailerNumber;
	private int trailerID;
    private String palletType = "";
	private int row;
    private List<ShippingQuorumDetail> details = new ArrayList<ShippingQuorumDetail>();
	private int size;
	private QuorumStatus status;
	private TrailerInfoStatus trailerStatus;
	
	public static final int QUORUM_DEFAULT_SIZE = 6;
	private static Map<Integer, String> dummyQuorumMap = new HashMap<Integer, String>();
	public static final int TRAILER_ID_REPAIR = -1;
	public static final int TRAILER_ID_EXCEPT = -2;
	public static final int SHIFT_OFFSET = 100;
	
	public java.sql.Date getQuorumDate() {
		return quorumDate;
	}


	public void setQuorumDate(java.sql.Date quorumDate) {
		this.quorumDate = quorumDate;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getSize() {
		return size;
	}
	
	public int getMaxSequence() {
		return details.get(details.size() -1).getQuorumSeq();
	}
	
	public String getProductSpec(int seq) {
		for(ShippingQuorumDetail detail : details) {
			if(detail.getQuorumSeq() == seq) return detail.getProductSpec();
		}
		return "";
	}


	public void setSize(int size) {
		this.size = size;
	}


	public QuorumStatus getStatus() {
		return status;
	}
	
	public int getStatusValue() {
		return status.ordinal();
	}

	public void setStatusValue(int status) {
		this.status = QuorumStatus.convert(status);
        if(this.status == null) this.status = QuorumStatus.Waiting;
	}
    
    public void setStatus(QuorumStatus status) {
        this.status = status;
    }
    
    public boolean isWaiting(){
        return status == QuorumStatus.Waiting;
    }
    
    public boolean isLoading(){
        return status == QuorumStatus.Loading;
    }

    public boolean isAllocating(){
        return status == QuorumStatus.Allocating;
    }
    
    public boolean isAllocated(){
        return status == QuorumStatus.Allocated;
    }
    
	public int getTrailerID() {
		return trailerID;
	}


	public void setTrailerID(int trailerID) {
		this.trailerID = trailerID;
	}
    
    public String getPalletType() {
        return palletType;
    }

    public void setPalletType(String palletType) {
        this.palletType = palletType;
    }

	public List<ShippingQuorumDetail> getQuorumDetails(){
     return details;   
    }
	
	public ShippingQuorumDetail getQuorumDetailToLoadEngine(){
		for(ShippingQuorumDetail detail: details){
			if (detail.getEngineNumber()==null || detail.getEngineNumber().trim().length()==0)
				return detail;
		}
		return null;
	}
    
    /**
     * load an engine into the quorum
     * @param engineNumber
     * @param ymto
     * @return
     */
    
	public ShippingQuorumDetail loadEngine(String engineNumber,String ymto) {
        
        ShippingQuorumDetail detail = getQuorumDetailToLoadEngine();
        if (detail == null) return null;
        if(detail.getProductSpec()!=null && !detail.getProductSpec().equalsIgnoreCase(ymto)){
            detail.setStatus(QuorumDetailStatus.misLoading);
        }
        detail.setEngineNumber(engineNumber);
        if (isQuorumComplete()){
            //when the quorum is finished, may be it is the true complete (full size or it is the repair or exceptional quorum) OR
            // not true complete (resized to smaller size)
            if(getSize() == ShippingQuorum.QUORUM_DEFAULT_SIZE || 
                    isExceptionalQuorum() ||
                    isRepairQuorum() &&
                    !isQuorumMisloaded())
                setStatus(QuorumStatus.Completed);
            else 
                setStatus(QuorumStatus.Incompleted);
        }else{
            setStatus(QuorumStatus.Loading);
        }
        return detail;
    }
	
	public boolean isQuorumComplete(){
		return this.getSize() == getLoadedEngineNumber();
	}
	
	public int getLoadedEngineNumber(){
		int count = 0;
		for(ShippingQuorumDetail detail: details){
			if (detail.getEngineNumber()!=null)
				count++;
		}
		return count;
	}
	
	public int getAssignedQuorumSize(){
		int count = 0;
		for(ShippingQuorumDetail detail: details){
			if (detail.getProductSpec()!=null && detail.getProductSpec().trim().length()>0)
				count++;
		}
		return count;
	}
	
	public boolean isQuorumMisloaded(){
		for(ShippingQuorumDetail detail: details){
			if(detail.getStatus().equals(QuorumDetailStatus.misLoading)){
    			return true;
    		}
		}
		return false;
	}
	
	public boolean isRepairQuorum(){
		return trailerID == TRAILER_ID_REPAIR;
	}
	
	public boolean isExceptionalQuorum(){
		return trailerID == TRAILER_ID_EXCEPT;
	}
    
    /**
     *  get the quorum type 
     * @return quorum type N-Normal, R-Repair E-Exceptional
     */
    
    public String getQuorumType(){
        if(isRepairQuorum()) return "R";
        else if(isExceptionalQuorum()) return "E";
        return "N";
    }
    
    public void setQuorumDetails(List<ShippingQuorumDetail> details){
        this.details = details;
        this.setKdLotNumber();
    }
    
    public void addQuorumDetails(ShippingQuorumDetail detail){
        details.add(detail);
        this.setKdLotNumber();
        Collections.sort(details, new ShippingQuorumDetail()); 
    }
    
	public String getTrailerNumber() {
		if(trailerNumber==null || trailerNumber.trim().length()==0){
			return getDummyQuorumTrailerNumber(getTrailerID());
		} else{
			return trailerNumber;
		}
	}
	
	public void updateQuorumSeq(java.sql.Date date, int seq){
		this.setQuorumDate(date);
		this.setQuorumID(seq);
		for(ShippingQuorumDetail detailItem : details){
			detailItem.setQuorumID(seq);
			detailItem.setQuorumDate(date);
		}
	}


	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}


	public int getQuorumID() {
		return quorumID;
	}


	public void setQuorumID(int quorumID) {
		this.quorumID = quorumID;
	}
	
	public void shiftQuorumID() {
		this.quorumID += ShippingQuorum.SHIFT_OFFSET;
	}
    
    public int getNextQuorumSeq(){
        int seq = 0;
        for(ShippingQuorumDetail detail:this.details){
            if(detail.getEngineNumber() != null) seq = detail.getQuorumSeq();
        }
        return seq + 1;
    }


	/**
	 * This method is to compare the two passed objects, always compare the production date first, followed by quorum ID
	 */
	public int compare(Object o1, Object o2) {
		int result = 0;
		ShippingQuorum quorum1 = (ShippingQuorum)o1;
		ShippingQuorum quorum2 = (ShippingQuorum)o2;
		if (quorum1.getQuorumDate().before(quorum2.getQuorumDate())){
			result = -1;
		}else if (quorum1.getQuorumDate().after(quorum2.getQuorumDate())){
			result = 1;
		} else if (quorum1.getQuorumID() < quorum2.getQuorumID()){
			result = -1;
		} else if (quorum1.getQuorumID() > quorum2.getQuorumID()){
			result = 1;
		}
		return result;
	}
	
	public int compare(ShippingQuorum quorum) {
		return compare(this,quorum);
	}
	
	public static enum QuorumStatus{
        Waiting,Allocating,Loading,Delayed, Completed, Incompleted,Allocated;
        public static QuorumStatus convert(int num){
            if(num <0 || num >=values().length) return null;
            return values()[num];
       }
    }
	
	private long getKDLotNumber(String kdLot){
		if (kdLot!=null && kdLot.length()==LENGTH_KD_LOT){
			return (Long.parseLong(kdLot.substring(LENGTH_PREFIX_KD_LOT)));
		}else{
			return 0;
		}
	}
    
	public long getKDLotNumber(){
		return this.kdLotNumber;
	}

	public void setKdLotNumber() {
		long kdLotNumber = 0;
		for (ShippingQuorumDetail detail:details){
			long newNumber = getKDLotNumber(detail.getKdLot());
			if ( kdLotNumber==0 || (kdLotNumber >0 && newNumber < kdLotNumber) )
				kdLotNumber = newNumber;
		}
		this.kdLotNumber = kdLotNumber;
	}
    
    public String toString(){
        StringBuilder buf = new StringBuilder();
        
        buf.append("Quorum(");
        buf.append("Trailer #=");
        buf.append(this.getTrailerNumber());
        buf.append(",Trailer ID=");
        buf.append(this.getTrailerID());
        buf.append(",Quorum Date=");
        buf.append(this.getQuorumDate());
        buf.append(",Quorum ID=");
        buf.append(this.getQuorumID());
        buf.append(",Row #=");
        buf.append(this.getRow());
        buf.append(",Size=");
        buf.append(this.getSize());
        if(this.getStatus()!=null){
	        buf.append(",Status=");
	        buf.append(this.getStatus().name());
        }
        if(this.getTrailerStatus()!=null){
	        buf.append(",TrailerStatus=");
	        buf.append(this.getTrailerStatus().name());
        }
        buf.append(",Pallet type=");
        buf.append(this.getPalletType());
        buf.append(",Quorum type=");
        buf.append(this.getQuorumType());
        buf.append(")");
        
        return buf.toString();
    }


   public Vector<Column> defineColumns() {
        Vector<Column> columns = new Vector<Column>();
        columns.add(new Column("QUORUM_DATE",ColumnType.Date,"QuorumDate",true));
        columns.add(new Column("QUORUM_ID",ColumnType.SmallInt,"QuorumID",true));
        columns.add(new Column("TRAILER_ID",ColumnType.SmallInt,"TrailerID"));
        columns.add(new Column("TRAILER_ROW",ColumnType.SmallInt,"Row"));
        columns.add(new Column("QUORUM_SIZE",ColumnType.SmallInt,"Size"));
        columns.add(new Column("PALLET_TYPE",ColumnType.String,"PalletType",5));
        columns.add(new Column("STATUS",ColumnType.SmallInt,"StatusValue"));
        return columns;
    }

    public String defineTableName() {
        return "GALADM.HCM_QUORUM_TBX";
    }   
    
    private static String getDummyQuorumTrailerNumber(int trailerID) {
        String trailerNumber = dummyQuorumMap.get(trailerID);
        if (trailerNumber == null) {
        	trailerNumber = initDummyQuorumMap(trailerID);
        }
        return trailerNumber;
    }
    
    private static synchronized String initDummyQuorumMap(int trailerID) {
    	String trailerNumber = dummyQuorumMap.get(trailerID);
        if (trailerNumber == null) {
        	dummyQuorumMap.put(TRAILER_ID_REPAIR, "REPAIR");
        	dummyQuorumMap.put(TRAILER_ID_EXCEPT, "EXCEPT");
        }
        return dummyQuorumMap.get(trailerID);
    }


	public TrailerInfoStatus getTrailerStatus() {
		return trailerStatus;
	}


	public void setTrailerStatus(TrailerInfoStatus trailerStatus) {
		this.trailerStatus = trailerStatus;
	}
	
	public int getTrailerStatusValue() {
		return trailerStatus.ordinal();
	}

	public void setTrailerStatusValue(int status) {
		this.trailerStatus = TrailerInfoStatus.convert(status);
        if(this.trailerStatus == null) this.trailerStatus = TrailerInfoStatus.Waiting;
	}
}
