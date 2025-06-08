package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;
/**
 * 
 * <h3>AFOffMonthlyPassingDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> POJO class to hold the AF Off Monthly Passing statistics data </p>
 * <p> The record's format is like the following: </p>
 * <p> 05  ACCOUNT-BUDGET       PIC  X(32).      1  - 32    'PRODUCTION_UNITS'   </p>
 * <p> 05  EVENT                PIC  X(32).     33  - 64    'ACTUAL'          </p>   
 * <p> 05  TIME                 PIC  X(32).     65  - 96     YYYY.MMM format'    </p>        
 * <p> 05  COMPANY              PIC  X(32).     97  - 128   '204P'            </p> 
 * <p> 05  COST-CENTER          PIC  X(32).    129  - 160   'NO_COSTCENTER'    </p>          
 * <p> 05  PROFIT-CENTER        PIC  X(32).    161  - 192    spaces             </p> 
 * <p> 05  PRODUCT              PIC  X(32).    193  - 224    MODEL / TYPE       </p>       
 * <p> 05  SLD-TO-DESTINATION   PIC  X(32).    225  - 256   'NO_SLDTODESTINATION'  </p>            
 * <p> 05  INTER-COMPANY        PIC  X(32).    257  - 288   'NO_INTERCO'         </p>     
 * <p> 05  VARIANCE-DETAIL      PIC  X(32).    289  - 320   'NO_VARIANCEDETAIL'   </p>           
 * <p> 05  AUDIT-TRAIL          PIC  X(32).    321  - 352   'LINE_CONTROL'     </p>         
 * <p> 05  RPT-CURRENCY         PIC  X(32).    353  - 384   'LC'        </p>      
 * <p> 05  MEASURES             PIC  X(32).    385  - 416   'PERIODIC'     </p>         
 * <p> 05  QUANTITY             PIC  9(11).    417  - 427               </p>
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
 * <TD>YX</TD>
 * <TD>Aug 05, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author YX
 * @created Aug 05, 2014
 */
public class AFOffMonthlyPassingDTO implements IOutputFormat {
	@OutputData(value="ACCOUNT_BUDGET")
	private String accountBudget;
	@OutputData(value="EVENT")
	private String event;
	@OutputData(value="TIME")
	private String time;
	@OutputData(value="COMPANY")
	private String company;
	@OutputData(value="COST_CENTER")
	private String costCenter;
	@OutputData(value="PROFIT_CENTER")
	private String profitCenter;
	@OutputData(value="PRODUCT")
	private String product;
	@OutputData(value="SLD_TO_DESTINATION")
	private String sldToDestination;
	@OutputData(value="INTER_COMPANY")
	private String interCompany;
	@OutputData(value="VARIANCE_DETAIL")
	private String varianceDetail;
	@OutputData(value="AUDIT_TRAIL")
	private String auditTrail;
	@OutputData(value="RPT_CURRENCY")
	private String rptCurrency;
	@OutputData(value="MEASURES")
	private String measures;
	@OutputData(value="QUANTITY")
	private int quantity = 0;
	@OutputData(value="ENDMARK")
	private String endMark;
	
	public AFOffMonthlyPassingDTO() {
		super();
	}
	
	public String getAccountBudget() {
		return accountBudget;
	}
	public void setAccountBudget(String accountBudget) {
		this.accountBudget = accountBudget;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public String getProfitCenter() {
		return profitCenter;
	}
	public void setProfitCenter(String profitCenter) {
		this.profitCenter = profitCenter;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getSldToDestination() {
		return sldToDestination;
	}
	public void setSldToDestination(String sldToDestination) {
		this.sldToDestination = sldToDestination;
	}
	public String getInterCompany() {
		return interCompany;
	}
	public void setInterCompany(String interCompany) {
		this.interCompany = interCompany;
	}
	public String getVarianceDetail() {
		return varianceDetail;
	}
	public void setVarianceDetail(String varianceDetail) {
		this.varianceDetail = varianceDetail;
	}
	public String getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(String auditTrail) {
		this.auditTrail = auditTrail;
	}
	public String getRptCurrency() {
		return rptCurrency;
	}
	public void setRptCurrency(String rptCurrency) {
		this.rptCurrency = rptCurrency;
	}
	public String getMeasures() {
		return measures;
	}
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getEndMark() {
		return endMark;
	}

	public void setEndMark(String endMark) {
		this.endMark = endMark;
	}
}
