package com.honda.galc.data;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.PrinterName;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

public class PrintRequest implements PrintJobListener {
	private String printJobStatus = null;
	private List<String> printJobEvents = null;

	public PrintRequest() {}

	public List<String> print(byte[] bytes, String printerName) {
		printJobEvents = new ArrayList<String>();
		PrintService printService = null;
		String name = "";

		try {
			DocFlavor docFlavor = DocFlavor.BYTE_ARRAY.POSTSCRIPT;
			Doc doc = new SimpleDoc(bytes, docFlavor, null);
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

			PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
					docFlavor, aset);

			if (printServices == null) {
				getLogger().info("Print Services are not found");
				return printJobEvents;
			}

			for (PrintService ps : printServices) {
				PrintServiceAttribute attr = ps.getAttribute(PrinterName.class);
				name = ((PrinterName) attr).getValue();
				if (name.contains(printerName)) {
					printService = ps;
					break;
				}
			}
			if(printService== null){
				getLogger().info("Print Services are not matched with printer name");
				return printJobEvents;
			}
			
			DocPrintJob job = printService.createPrintJob();
			job.addPrintJobListener(this);
			try {
				job.print(doc, null);
				// Wait till we know the status of our print job
				while (printJobStatus == null) {
				// Wait for 1 second before checking to see if the job is
				// complete.
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignore) {
						ignore.printStackTrace();
					}
				}
				} catch (PrintException exception) {
					exception.printStackTrace();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to create print request");
		} 
		
		return printJobEvents;

	}

	public void printDataTransferCompleted(PrintJobEvent pje) {
		printJobStatus = "printDataTransferCompleted";
		printJobEvents.add(printJobStatus);
	}

	public void printJobCanceled(PrintJobEvent pje) {
		printJobStatus = "printJobCanceled";
		printJobEvents.add(printJobStatus);
	}

	public void printJobCompleted(PrintJobEvent pje) {
		printJobStatus = "printJobCompleted";
		printJobEvents.add(printJobStatus);
	}

	public void printJobFailed(PrintJobEvent pje) {
		printJobStatus = "printJobFailed";
		printJobEvents.add(printJobStatus);
	}

	public void printJobNoMoreEvents(PrintJobEvent pje) {
		printJobStatus = "printJobNoMoreEvents";
		printJobEvents.add(printJobStatus);
	}

	public void printJobRequiresAttention(PrintJobEvent pje) {
		printJobStatus = "printJobRequiresAttention";
		printJobEvents.add(printJobStatus);
	}
}
