package com.honda.galc.vios.dto;

import com.honda.galc.common.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;

public class ProcessChange {
	
	protected static final String RUN_DLL_CMD = "rundll32 url.dll,FileProtocolHandler";

	private SimpleStringProperty processNo = null;
	
	private SimpleStringProperty associateNo = null;
	
	private SimpleStringProperty date = null;
	
	private SimpleStringProperty changeDesc = null;
	
	private SimpleStringProperty unitNo = null;
	
	private BooleanProperty process ;
	
	private Hyperlink trngPdfLink;

	private String reportUrl = "";
	
	
	
	public ProcessChange(String url) {
		processNo =  new SimpleStringProperty();
		associateNo =  new SimpleStringProperty();
		date =  new SimpleStringProperty();
		changeDesc =  new SimpleStringProperty();
		unitNo =  new SimpleStringProperty();
		this.process =  new SimpleBooleanProperty(false);
		this.reportUrl = url;
		trngPdfLink = new Hyperlink("Process Change Form");
		trngPdfLink.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try{
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+reportUrl);
				}
				catch(Exception e1){
					e1.printStackTrace();
					Logger.getLogger().error("Error generating the Crystal report URL "+e1.getMessage());
				}
			}
		});
		
	}
	
	public void setProcessNo(String processNo) {
		this.processNo.set(processNo);
	}

	public String getProcessNo() {
		return processNo.get();
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo.set(associateNo);
	}

	public String getAssociateNo() {
		return associateNo.get();
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public String getDate() {
		return date.get();
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc.set(changeDesc);
	}

	public String getChangeDesc() {
		return changeDesc.get();
	}

	public String getUnitNo() {
		return unitNo.get();
	}

	public void setUnitNo(String unitNo) {
		this.unitNo.set(unitNo);
	}

	
	public Hyperlink getHyperlink() {
        return trngPdfLink;
    }
 
    public void setHyperlink(String pdfUrl) {
        this.trngPdfLink = new Hyperlink(pdfUrl);
    }
    
    public StringProperty processNoProperty() {
    	return processNo;
    }

    public boolean isProcess() {
		return process.get();
	}

	
    public BooleanProperty processProperty() {
    	return process;
    }
	 
	public void setProcess(boolean process) {
		this.process.set(process);
	}

}
