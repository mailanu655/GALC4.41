package com.honda.galc.client.qics.data;

import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.ImageSection;

/**
 * <h3>Class description</h3>
 *  <h4>Description</h4>
 *  It is a polygon with ID.
 *  <P>
 *  <h4>Change History</h4>
 *  <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 *  <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 *  <TH>Updated by</TH>
 *  <TH>Update date</TH>
 *  <TH>Version</TH>
 *  <TH>Mark of Update</TH>
 *  <TH>Reason</TH>
 *  </TR>
 *  <TR>
 *  <TD>K.Ishibe</TD>
 *  <TD>(2001/03/18 20:00:00)</TD>
 *  <TD>0.1</TD><TD>(none)</TD>
 *  <TD>Initial Release</TD>
 *  </TR>
 *  </TABLE>
 *  @see 
 *  @ver 0.1
 *  @author K.Ishibe
 */
public	class XPolygon extends java.awt.Polygon {
	
	private static final long serialVersionUID = 1L;
	
	/**
	It is ID for discriminating a polygon.
	*/
	private	String	polygon_Id		=	"0";
	/**
	It is the part King flag of a polygon.
	*/
	public	boolean	part_Kind_Flag	=	false;
	/**
	It is the defect ID of a polygon.
	*/
	public  int defect_Id = 0;
	
	private ImageSection imageSection;
	
	private DefectDescription defectDescription;
	
/**
 * XPolygon constructor comment.
 */
public XPolygon() {
	super();
}
/**
 * XPolygon constructor comment.
 * @param xpoints int[]
 * @param ypoints int[]
 * @param npoints int
 */
public XPolygon(int[] xpoints, int[] ypoints, int npoints) {
	super(xpoints, ypoints, npoints);
}
/**
 * The part_Kind_Flag currently held is returned.
 * @return java.lang.String
 */
public boolean getPart_Kind_Flag() {
	return part_Kind_Flag;
}
/**
 * The polygon_id currently held is returned.
 * @return java.lang.String
 */
public String getPolygonId() {
	return polygon_Id;
}
/**
 * A new part_Kind_Flag is set.
 * @param parts java.lang.String
 */
public void setPart_Kind_Flag(boolean part_Kind_Flag) {
	this.part_Kind_Flag	=	part_Kind_Flag;	
}
/**
 * A new polygon_id is set.
 * @param parts java.lang.String
 */
public void setPolygonId(String parts) {
	polygon_Id	=	parts;	
}
public ImageSection getImageSection() {
	return imageSection;
}
public void setImageSection(ImageSection imageSection) {
	this.imageSection = imageSection;
}

public DefectDescription getDefectDescription() {
	return defectDescription;
}

public void setDefectDescription(DefectDescription defectDescription) {
	this.defectDescription = defectDescription;
}


}
