package com.honda.galc.device;

public enum SmartCardEnumFactory {
	
	OMNIKEY5427CK(Omnikey5427CK.class),
	OMNIKEY5427G2(Omnikey5427G2.class);
	
	private Class<? extends SmartCardReader> smartCardReaderClass;

	private SmartCardEnumFactory(Class<? extends SmartCardReader> clazz) {
		smartCardReaderClass = clazz;
	}

	public Class<? extends SmartCardReader> getSmartCardReaderClass() {
		return smartCardReaderClass;
	}

}
