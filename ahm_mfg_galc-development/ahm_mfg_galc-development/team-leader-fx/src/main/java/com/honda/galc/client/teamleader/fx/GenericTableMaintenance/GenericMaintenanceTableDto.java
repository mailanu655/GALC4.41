package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */

public  class GenericMaintenanceTableDto {
        private  String columnName;
       

        public String getColumnName() {
			return columnName;
		}


		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}


		public GenericMaintenanceTableDto(String columnName) {
            this.columnName = new String(columnName);
         
        }
       
    }
