package com.honda.mfg.stamp.conveyor.domain.enums;

public enum DEFECT_TYPE {

	BAD_HANDWORK(0), BURR(1), DIE_CRACK(2), DOUBLE_HIGH_LINE(3), LASER_BLOWOUT(4), LASER_CRACK(5), LASER_PINHOLE(6),
	MATERIAL(7), MATERIAL_CRACK(8), MINUS_DEFORM(9), OIL_LINES(10), OTHER(18), OVERLAP(11), PLUS_DEFORM(12),
	SCRAP_IN_DIE(13), SCRATCH(14), TWIST(15), WAVES(16), WRINKLES(17);

	private int type;

	DEFECT_TYPE(int type) {
		this.type = type;
	}

	public int type() {
		return this.type;
	}

	public static DEFECT_TYPE findByType(int type) {
		DEFECT_TYPE[] items = DEFECT_TYPE.values();
		for (int i = 0; i < items.length; i++) {
			DEFECT_TYPE s = items[i];
			if (type == s.type()) {
				return s;
			}
		}
		return BAD_HANDWORK;
	}
}
