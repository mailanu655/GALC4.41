package com.honda.galc.data;

import javax.print.attribute.standard.MediaSizeName;

public enum JasperReportPageType {
	NA_LETTER	(612,  792, MediaSizeName.NA_LETTER),
	NA_LEGAL	(612,  1008, MediaSizeName.NA_LEGAL), 
	ISO_A0		(2380, 3368, MediaSizeName.ISO_A0), 
	ISO_A1		(1684, 3368, MediaSizeName.ISO_A1), 
	ISO_A2		(1190, 1684, MediaSizeName.ISO_A2), 
	ISO_A3		(842,  1190, MediaSizeName.ISO_A3),
	ISO_A4		(595,  842, MediaSizeName.ISO_A4), 
	ISO_A5		(421,  595, MediaSizeName.ISO_A5), 
	ISO_A6		(297,  421, MediaSizeName.ISO_A6), 
	ISO_A7		(210,  297, MediaSizeName.ISO_A7), 
	ISO_A8		(148,  210, MediaSizeName.ISO_A8), 
	ISO_A9		(105,  148, MediaSizeName.ISO_A9), 
	ISO_A10		(74,   105, MediaSizeName.ISO_A10), 
	ISO_B0		(2836, 4008, MediaSizeName.ISO_B0), 
	ISO_B1		(2004, 2836, MediaSizeName.ISO_B1), 
	ISO_B2		(1418, 2004, MediaSizeName.ISO_B2), 
	ISO_B3		(1002, 1418, MediaSizeName.ISO_B3), 
	ISO_B4		(709,  1002, MediaSizeName.ISO_B4), 
	ISO_B5		(501,  709, MediaSizeName.ISO_B5),
	TABLOID		(792,  1224, MediaSizeName.TABLOID); 
	
	private final int pageWidth;
	private final int pageHeight;
	private MediaSizeName mediaSizeName;

	private JasperReportPageType(int pageWidth, int pageHeight, MediaSizeName mediaSizeName) {
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.mediaSizeName = mediaSizeName;
	}

	public int getPageWidth() {
		return pageWidth;
	}
	
	public int getPageWidth(JasperReportPageOrientation orientation) {
		// if printing in Landscape mode, swap width with height
		if (orientation.equals(JasperReportPageOrientation.LANDSCAPE)) {
			return pageHeight;
		}
		return pageWidth;
	}

	public int getPageHeight() {
		return pageHeight;
	}
	
	public int getPageHeight(JasperReportPageOrientation orientation) {
		// if printing in Landscape mode, swap height with width
		if (orientation.equals(JasperReportPageOrientation.LANDSCAPE)) {
			return pageWidth;
		}
		return pageHeight;
	}

	public MediaSizeName getMediaSizeName() {
		return this.mediaSizeName;
	}

}
