package com.honda.galc.client.ui.binding;

import javafx.beans.binding.BooleanExpression;
import javafx.collections.ObservableList;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 11, 2014
 */
public class AnyFalseBinding extends BooleanListBinding {

	public AnyFalseBinding(ObservableList<? extends BooleanExpression> booleanList) {
		super(booleanList);
	}

	@Override
	protected boolean computeValue() {
		for (BooleanExpression bp : observedProperties) {
			if (!bp.get()) {
				return true;
			}
		}
		return false;
	}

}

