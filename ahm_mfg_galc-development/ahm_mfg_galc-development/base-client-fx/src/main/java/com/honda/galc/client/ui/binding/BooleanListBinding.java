package com.honda.galc.client.ui.binding;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 11, 2014
 */
public abstract class BooleanListBinding extends BooleanBinding {
	protected final ObservableList<? extends BooleanExpression> boundList;
	protected final ListChangeListener<BooleanExpression> listChangeListener = new ListChangeListener<BooleanExpression>() {
		@Override
		public void onChanged(ListChangeListener.Change<? extends BooleanExpression> change) {
			refreshBinding();
		}
	};
	protected BooleanExpression[] observedProperties = {};

	BooleanListBinding(ObservableList<? extends BooleanExpression> booleanList) {
		booleanList.addListener(listChangeListener);
		boundList = booleanList;
		refreshBinding();
	}

	@Override
	public void dispose() {
		boundList.removeListener(listChangeListener);
		super.dispose();
	}

	private void refreshBinding() {
		super.unbind(observedProperties);
		observedProperties = boundList.toArray(new BooleanExpression[0]);
		super.bind(observedProperties);
		this.invalidate();
	}
}
