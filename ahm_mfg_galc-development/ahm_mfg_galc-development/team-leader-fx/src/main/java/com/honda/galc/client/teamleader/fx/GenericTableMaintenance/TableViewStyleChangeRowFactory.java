package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/*   
 * @author Gangadhararao Gadde 
 * 
 *
 *
 */
public class TableViewStyleChangeRowFactory<T> implements
        Callback<TableView<T>, TableRow<T>> {

    private final String updateStyleClass ;
    private final  String deleteStyleClass ;
    private final  ObservableList<Integer> styledDeletedRowIndicesList ;
	private final ObservableList<Integer> styledUpdatedRowIndicesList ;
    private  final Callback<TableView<T>, TableRow<T>> baseRowFactory ;
    
   
    public TableViewStyleChangeRowFactory(String updateStyleClass,String deleteStyleClass, Callback<TableView<T>, TableRow<T>> baseFactory) {
        this.updateStyleClass = updateStyleClass ;
        this.deleteStyleClass = deleteStyleClass ;
        this.baseRowFactory = baseFactory ;
        this.styledDeletedRowIndicesList = FXCollections.observableArrayList();
        this.styledUpdatedRowIndicesList = FXCollections.observableArrayList();
    }
    
   
    public TableViewStyleChangeRowFactory(String updateStyleClass,String deleteStyleClass) {
        this(updateStyleClass,deleteStyleClass, null);
    }
    
	@Override
    public TableRow<T> call(TableView<T> tableView) {
        
        final TableRow<T> row ;
        if (baseRowFactory == null) {
            row = new TableRow();
        } else {
            row = baseRowFactory.call(tableView);
        }
        
        row.indexProperty().addListener(new ChangeListener<Number>() {
        	@Override
            public void changed(ObservableValue<? extends Number> obs,
                    Number oldValue, Number newValue) {
                updateRowStyleClass(row);
            }
        });
        
        styledDeletedRowIndicesList.addListener(new ListChangeListener<Integer>() {
        	@Override
           public void onChanged(Change<? extends Integer> change) {
                updateRowStyleClass(row);
            }
        });
        
        styledUpdatedRowIndicesList.addListener(new ListChangeListener<Integer>() {
        	@Override
           public void onChanged(Change<? extends Integer> change) {
                updateRowStyleClass(row);
            }
        });

        return row;
    }
    
   
   

    private void updateRowStyleClass(TableRow<T> row) {
        final ObservableList<String> rowStyleClasses = row.getStyleClass();
        if (styledDeletedRowIndicesList.contains(row.getIndex()) ) {
            if (! rowStyleClasses.contains(deleteStyleClass)) {
                rowStyleClasses.add(deleteStyleClass);
            }
        } else {
            rowStyleClasses.removeAll(Collections.singletonList(deleteStyleClass));
        }
        
        if (styledUpdatedRowIndicesList.contains(row.getIndex()) ) {
            if (! rowStyleClasses.contains(updateStyleClass)) {
                rowStyleClasses.add(updateStyleClass);
            }
        } else {
            rowStyleClasses.removeAll(Collections.singletonList(updateStyleClass));
        }
        
 
    }


    public String getUpdateStyleClass() {
		return updateStyleClass;
	}




	public String getDeleteStyleClass() {
		return deleteStyleClass;
	}




	public ObservableList<Integer> getStyledDeletedRowIndicesList() {
		return styledDeletedRowIndicesList;
	}




	public ObservableList<Integer> getStyledUpdatedRowIndicesList() {
		return styledUpdatedRowIndicesList;
	}



}
