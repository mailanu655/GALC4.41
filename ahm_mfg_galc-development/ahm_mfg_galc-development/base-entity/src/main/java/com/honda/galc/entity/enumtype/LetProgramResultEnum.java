package com.honda.galc.entity.enumtype;

import com.honda.galc.entity.product.LetProgramResult;
import com.honda.galc.entity.product.LetProgramResultFive;
import com.honda.galc.entity.product.LetProgramResultFour;
import com.honda.galc.entity.product.LetProgramResultOne;
import com.honda.galc.entity.product.LetProgramResultSix;
import com.honda.galc.entity.product.LetProgramResultThree;
import com.honda.galc.entity.product.LetProgramResultTwo;

public enum LetProgramResultEnum {

	GAL703TBX_01(LetProgramResultOne.class), 
	GAL703TBX_02(LetProgramResultTwo.class),
	GAL703TBX_03(LetProgramResultThree.class), 
	GAL703TBX_04(LetProgramResultFour.class), 
	GAL703TBX_05(LetProgramResultFive.class), 
	GAL703TBX_06(LetProgramResultSix.class);

	private Class<? extends LetProgramResult> letPgmResultClass;

	private LetProgramResultEnum(Class<? extends LetProgramResult> clazz) {
		letPgmResultClass = clazz;
	}

	public Class<? extends LetProgramResult> getLetPgmResultClass() {
		return letPgmResultClass;
	}
}
