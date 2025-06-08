package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public class WhereClauseGenerator {
	

	
	private String inStr              = "IN";
	private String notinStr           = "NOT IN";	
	private StringBuffer whereString = new StringBuffer();
	
	public WhereClauseGenerator(Hashtable<String,GenericTableMaintFilter> hFilter){
		generateWhereClause(hFilter);	
	}
	
	/* Generate where clause values from the filter hashtable
	 * ex. prductionLot.PlanCode = 'HMI 014'
	 * Need to discuss how 'ALL' should be handled, need to define the column propertly in the config
	 */
	private void generateWhereClause(Hashtable hFilter){
		StringBuffer whereStringBuff = new StringBuffer();
		whereStringBuff.append("where");
		String values = null;
		int count = 0;
		Enumeration enumKeys = hFilter.keys();		
		try{

			while(enumKeys.hasMoreElements()){			
				String key = (String) enumKeys.nextElement();			
				GenericTableMaintFilter filter = (GenericTableMaintFilter) hFilter.get(key);	

				values = selectedValueString(filter);			
				if(values == null || values.length()==0 )
					continue;
				if(count ==0)
					whereStringBuff.append(" ");
				else
					whereStringBuff.append(" and ");

				if(filter.getFilterValueSource().equals(FilterValueSource.CUSTOMBUILDER)){
					Class obj = Class.forName(filter.getValues());
					ICustomWhereClauseBuilder objCustomWhere = (ICustomWhereClauseBuilder)obj.newInstance();
					whereStringBuff.append(objCustomWhere.getWhereClauseString(filter.getSelectedValues()));				
					count++;
					continue;
				}

				whereStringBuff.append(filter.getColumn()+" ");
				whereStringBuff.append(filter.getParamType()+" ");

				
				if(filter.getParamType().equalsIgnoreCase(inStr) || filter.getParamType().equalsIgnoreCase(notinStr)){
					values = "(" + values + ")";
				}
				whereStringBuff.append(values);	
				count++;
			}
            this.setWhereStringBuff(whereStringBuff);
			System.out.println("Generated WHERE clause string is: " + whereStringBuff.toString());

		}catch (Exception exp){
			exp.printStackTrace();
		}

	}
	
	private String selectedValueString(GenericTableMaintFilter filter ){
		List listOfVal=filter.getSelectedValues();
		boolean stringDataType=(filter.getDataType().equalsIgnoreCase(String.class.getName())|| filter.getDataType().equalsIgnoreCase(Timestamp.class.getSimpleName()));
		StringBuffer selectedValuesBuff = new StringBuffer();
		try{
			if(listOfVal == null || listOfVal.size()==0)
				return selectedValuesBuff.toString();
			
			Iterator itr = listOfVal.iterator();
			int count = 0;
			while(itr.hasNext()){
				if(count == 0){
					if(stringDataType)
					  selectedValuesBuff.append("'");					
				}else {
					if(stringDataType)
					   selectedValuesBuff.append(",'");
					else
						selectedValuesBuff.append(",");
				}
				if(stringDataType)
				   selectedValuesBuff.append(itr.next() + "'");	
				else
					selectedValuesBuff.append(itr.next() );	
				count++;
			}
		}catch (Exception exp){
			exp.printStackTrace();
		}
		if(selectedValuesBuff.length()>0 && Character.toString(selectedValuesBuff.charAt(0)).equals(","))
		{
			selectedValuesBuff=selectedValuesBuff.deleteCharAt(0);
		}
		return selectedValuesBuff.toString();
	}

	public StringBuffer getWhereStringBuff() {
		return whereString;
	}

	public void setWhereStringBuff(StringBuffer whereString) {
		this.whereString = whereString;
	}

}
