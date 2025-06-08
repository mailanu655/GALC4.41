
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public class FiltersBySequenceNumber implements java.util.Comparator {
	
	 public int compare(Object a, Object b) {
		 if (((GenericTableMaintFilter)a).getSequenceNumber() < ((GenericTableMaintFilter)b).getSequenceNumber())
			 return -1;
		 else if(((GenericTableMaintFilter)a).getSequenceNumber() == ((GenericTableMaintFilter)b).getSequenceNumber())
			 return 0 ;
		 else
			 return 1;
	 }
}
