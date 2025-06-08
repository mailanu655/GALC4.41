package com.honda.global.galc.bl.common.data.model;

import java.io.Serializable;
import java.util.List;

public interface IModel extends Serializable{
     abstract List<Column> defineColumns();
     abstract String defineTableName();
}
