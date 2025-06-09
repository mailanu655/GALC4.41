package com.honda.mfg.stamp.conveyor.domain.dialects;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class DB2Dialect extends org.hibernate.dialect.DB2Dialect{
	

		   public DB2Dialect() {
		       super();
		       
		       registerFunction( "bitwise_and", new SQLFunctionTemplate( StandardBasicTypes.INTEGER, " BITAND( ?1, ?2) " ) );

		   }
}


