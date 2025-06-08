package com.honda.galc.service.schedule.backout;

import com.honda.galc.data.DataContainer;
import com.honda.galc.service.IoService;

public interface ProductionLotBackoutService extends IoService {

	public enum InfoCodes {
		NG(-1),
		OK(0),
		INVALID_LOT_PREFIX(1),
		INVALID_LOT_DATE(2),
		INVALID_LOT_DATE_NOT_LAST_DATE(3),
		INVALID_LOT_DATE_ACTIVE_PRODUCTS_EXIST(4),
		INVALID_LOT_DATE_ACTIVE_PRODUCT_RESULTS_EXIST(5),
		INVALID_LOT_DATE_ACTIVE_INSTALLED_PARTS_EXIST(6),
		INVALID_LOT_DATE_ACTIVE_MEASUREMENTS_EXIST(7),
		CANNOT_BACKOUT(8);
		private int id;
		private InfoCodes(int id) {
			this.id = id;
		}
		public int getId() {
			return this.id;
		}
	};

	/**
	 * Backs out the given production lots.
	 * @param input - <br>
	 * LOT_PREFIX : the lot prefix,<br>
	 * LOT_DATE : the lot date<br>
	 * @return INFO_CODE : the info code id,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer backoutProductionLot(DataContainer input);

	/**
	 * Backs out the given production lots.
	 * @param lotPrefix : the lot prefix,<br>
	 * @param lotDate : the lot date<br>
	 * @return INFO_CODE : the info code id,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer backoutProductionLot(String lotPrefix, String lotDate);

}
