package com.honda.galc.entity.enumtype;

import com.honda.galc.entity.product.LetProgramResultValue;
import com.honda.galc.entity.product.LetProgramResultValueFive;
import com.honda.galc.entity.product.LetProgramResultValueFour;
import com.honda.galc.entity.product.LetProgramResultValueOne;
import com.honda.galc.entity.product.LetProgramResultValueSix;
import com.honda.galc.entity.product.LetProgramResultValueThree;
import com.honda.galc.entity.product.LetProgramResultValueTwo;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 25, 2013
 */

public enum LetProgramResultValueEnum {

	GAL704TBX_01(LetProgramResultValueOne.class), 
	GAL704TBX_02(LetProgramResultValueTwo.class), 
	GAL704TBX_03(LetProgramResultValueThree.class), 
	GAL704TBX_04(LetProgramResultValueFour.class), 
	GAL704TBX_05(LetProgramResultValueFive.class), 
	GAL704TBX_06(LetProgramResultValueSix.class);

	private Class<? extends LetProgramResultValue> letPgmResultValueClass;

	private LetProgramResultValueEnum(Class<? extends LetProgramResultValue> clazz) {
		letPgmResultValueClass = clazz;
	}

	public Class<? extends LetProgramResultValue> getLetPgmResultValueClass() {
		return letPgmResultValueClass;
	}
}
