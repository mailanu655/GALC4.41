/**
 * 
 */
package com.honda.galc.system.oif.svc.data;

public class TimeKey {
	private int shift;
	private java.sql.Date productionDate;
	private static final TimeKey blank = new TimeKey(null, -1);
	/**
	 * @param productionDate
	 * @param shift
	 */
	public TimeKey(java.sql.Date productionDate, int shift) {
		this.productionDate = productionDate;
		this.shift = shift;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = PRIME * result + shift;
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
		if (this.getClass() != obj.getClass())
			return false;
		final TimeKey other = (TimeKey) obj;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (shift != other.shift)
			return false;
		return true;
	}
	
	public static TimeKey getBlank() {
		return blank;
	}
	
}