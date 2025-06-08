package com.honda.galc.client;

import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SortArrow;
import com.smartgwt.client.widgets.grid.ListGrid;

public class DetailListGrid extends ListGrid {

	public DetailListGrid()
	{
	    setAlternateRecordStyles(true);
	    setShowAllRecords(true);
		setAutoFetchData(true);
		setAutoFitData(Autofit.HORIZONTAL);
		setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		setShowHeaderContextMenu(false);
		setShowHeaderMenuButton(false);
		setShowSortArrow(SortArrow.NONE);
		setCanSort(false);
		setWidth100();
	}
}
