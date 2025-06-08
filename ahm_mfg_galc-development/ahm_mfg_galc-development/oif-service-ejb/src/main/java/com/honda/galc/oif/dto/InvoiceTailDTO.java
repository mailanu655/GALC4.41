package com.honda.galc.oif.dto;

import java.lang.reflect.Field;
import java.util.Map;

import com.honda.galc.util.OutputData;

//Additional fields not in DailyDepartmentSchedule
public class InvoiceTailDTO   implements IOutputFormat { 


	@OutputData("TRL_RECORD_ID")
	private String trlRecordId;

	@OutputData("TRL_TRANS_CODE")
	private String trlTransCode;

	@OutputData("TRL_HASH_TOTAL")
	private String trlHashTotal;

	@OutputData("SPACE7")
	private String space7;

	public String getTrlRecordId()  {
		return trlRecordId;
	}

	public void setTrlRecordId(String newTrlRecordId)  {
		trlRecordId=newTrlRecordId;
	}

	public String getTrlTransCode()  {
		return trlTransCode;
	}

	public void setTrlTransCode(String newTrlTransCode)  {
		trlTransCode=newTrlTransCode;
	}

	public String getTrlHashTotal()  {
		return trlHashTotal;
	}

	public void setTrlHashTotal(String newTrlHashTotal)  {
		trlHashTotal=newTrlHashTotal;
	}

	public String getSpace7()  {
		return space7;
	}
	
	public void setSpace7(String newSpace7)  {
		space7=newSpace7;
	}
	
	public void initialize(Map<String,String>  inputValues)  {
		if(inputValues == null || inputValues.isEmpty())  return;
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields)  {
			OutputData a1 = f.getAnnotation(OutputData.class);
			if(!inputValues.containsKey(a1.value()))  continue;
			if(f.getType().isAssignableFrom(String.class))  {
				try {
					f.set(this, inputValues.get(a1.value()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceTailDTO [trlRecordId=" + trlRecordId + ", trlTransCode=" + trlTransCode + ", trlHashTotal="
				+ trlHashTotal + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trlHashTotal == null) ? 0 : trlHashTotal.hashCode());
		result = prime * result + ((trlRecordId == null) ? 0 : trlRecordId.hashCode());
		result = prime * result + ((trlTransCode == null) ? 0 : trlTransCode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceTailDTO other = (InvoiceTailDTO) obj;
		if (trlHashTotal == null) {
			if (other.trlHashTotal != null)
				return false;
		} else if (!trlHashTotal.equals(other.trlHashTotal))
			return false;
		if (trlRecordId == null) {
			if (other.trlRecordId != null)
				return false;
		} else if (!trlRecordId.equals(other.trlRecordId))
			return false;
		if (trlTransCode == null) {
			if (other.trlTransCode != null)
				return false;
		} else if (!trlTransCode.equals(other.trlTransCode))
			return false;
		return true;
	}
	

}
