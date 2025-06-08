package com.honda.galc.util.check;


public class PartResultData implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	/**
	Part name
	*/
	public String part_Name = " ";
	
	/**
	Part desc
	*/
	public String part_Desc = " ";

	/**
	Part serial number
	*/
	public String part_Serial = " ";

	/**
	Part reason
	*/
	public String part_Reason = " ";

	/**
	Product ID
	*/
	public String product_ID = " ";
	/**
	 * Torque values
	 * */
	public String torques = " ";
	
	/**
	 * Process point Id
	 */
	public String procecssPointId=" ";
	/**
	 * Process point Id
	 */
	public String procecssPointName=" ";
	
	/**
 	* Initializes a newly created DefectResultData object 
 	*/
	public PartResultData() {
		super();
	}
    public String toString()
    {
        StringBuffer returnValue = new StringBuffer();
        if(part_Name == null)
        {
            returnValue.append("\t\t\t");
        }
        else
        {
            returnValue.append(part_Name);            
        }
        returnValue.append("\t\t\t\t");
        if(part_Desc == null)
        {
            returnValue.append("\t\t\t");
        }
        else
        {
            returnValue.append(part_Desc);            
        }
        returnValue.append("\t\t\t\t");
        
        
        if(part_Serial == null)
        {
            returnValue.append("\t\t\t");
        }
        else
        {
            returnValue.append(part_Serial);            
        }
        returnValue.append("\t\t\t\t");
        if(part_Reason == null)
        {
            returnValue.append("\t\t\t");
        }
        else
        {
            returnValue.append(part_Reason);            
        }
        returnValue.append("\t\t\t\t");

        if(torques == null)
        {
            returnValue.append("\t\t\t");
        }
        else
        {
            returnValue.append(torques);            
        }
        returnValue.append("\t\t\t\t");
        if(procecssPointId == null)
        {
            returnValue.append("\t\t\t");
        }
        else
        {
            returnValue.append(procecssPointId);            
        }
        if(procecssPointName == null)
        {
            returnValue.append("\t\t\t");
        }
        else
        {
            returnValue.append(procecssPointName);            
        }
        return returnValue.toString();
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((part_Desc == null) ? 0 : part_Desc.hashCode());
		result = prime * result + ((part_Name == null) ? 0 : part_Name.hashCode());
		result = prime * result + ((part_Reason == null) ? 0 : part_Reason.hashCode());
		result = prime * result + ((part_Serial == null) ? 0 : part_Serial.hashCode());
		result = prime * result + ((procecssPointId == null) ? 0 : procecssPointId.hashCode());
		result = prime * result + ((procecssPointName == null) ? 0 : procecssPointName.hashCode());
		result = prime * result + ((product_ID == null) ? 0 : product_ID.hashCode());
		result = prime * result + ((torques == null) ? 0 : torques.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartResultData other = (PartResultData) obj;
		if (part_Desc == null) {
			if (other.part_Desc != null)
				return false;
		} else if (!part_Desc.equals(other.part_Desc))
			return false;
		if (part_Name == null) {
			if (other.part_Name != null)
				return false;
		} else if (!part_Name.equals(other.part_Name))
			return false;
		if (part_Reason == null) {
			if (other.part_Reason != null)
				return false;
		} else if (!part_Reason.equals(other.part_Reason))
			return false;
		if (part_Serial == null) {
			if (other.part_Serial != null)
				return false;
		} else if (!part_Serial.equals(other.part_Serial))
			return false;
		if (procecssPointId == null) {
			if (other.procecssPointId != null)
				return false;
		} else if (!procecssPointId.equals(other.procecssPointId))
			return false;
		if (procecssPointName == null) {
			if (other.procecssPointName != null)
				return false;
		} else if (!procecssPointName.equals(other.procecssPointName))
			return false;
		if (product_ID == null) {
			if (other.product_ID != null)
				return false;
		} else if (!product_ID.equals(other.product_ID))
			return false;
		if (torques == null) {
			if (other.torques != null)
				return false;
		} else if (!torques.equals(other.torques))
			return false;
		return true;
	}
}
