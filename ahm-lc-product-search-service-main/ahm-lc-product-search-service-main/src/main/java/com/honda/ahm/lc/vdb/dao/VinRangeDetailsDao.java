package com.honda.ahm.lc.vdb.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.honda.ahm.lc.vdb.entity.VinRangeProductDetails;

@Repository
public interface VinRangeDetailsDao extends JpaRepository<VinRangeProductDetails, String> {


	@Query(value = " SELECT * \r\n" + "FROM (\r\n" + "    SELECT DISTINCT \r\n" + "        frame.product_id, pp.line_id, "
			+ "        frame.product_spec_code,\r\n" + "        frame.kd_lot_number,\r\n"
			+ "        frame.production_lot,\r\n" + "        frame.engine_serial_no,\r\n"
			+ "        frame.tracking_status,\r\n" + "        frame.AF_ON_SEQUENCE_NUMBER,\r\n"
			+ "        seq.division_name,\r\n" + "        seq.line_name,\r\n" + "        pp.PROCESS_POINT_NAME,\r\n"
			+ "        pp.PROCESS_POINT_ID,\r\n" + "        b.ENGINE_MTO,\r\n" + "        b.SALES_MODEL_TYPE_CODE\r\n"
			+ "    FROM (\r\n" + "        SELECT \r\n" + "            l.line_Id AS tracking_status,\r\n"
			+ "            l.line_name AS LINE_Name,\r\n" + "            D.DIVISION_NAME,\r\n"
			+ "            (COALESCE(d.SEQUENCE_NUMBER, 0) + 1) * 1000000 + \r\n"
			+ "            (COALESCE(l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000 AS sequence_num\r\n"
			+ "        FROM gal195tbx l\r\n" + "        LEFT JOIN GAL128TBX D ON l.DIVISION_ID = D.DIVISION_ID\r\n"
			+ "        WHERE d.DIVISION_NAME IN ('WE', 'PA', 'AF', 'VQ')\r\n" + "    ) AS seq\r\n"
			+ "    JOIN gal143tbx frame ON frame.tracking_status = seq.tracking_status\r\n"
			+ "    LEFT JOIN gal131tbx engine ON frame.engine_serial_no = engine.PRODUCT_ID\r\n"
			+ "    LEFT JOIN GAL214TBX pp ON frame.LAST_PASSING_PROCESS_POINT_ID = pp.PROCESS_POINT_ID\r\n"
			+ "    JOIN GAL144TBX b ON frame.PRODUCT_SPEC_CODE = b.PRODUCT_SPEC_CODE\r\n" + "    WHERE \r\n"
			+ "        ( ?1 IS NULL OR seq.sequence_num BETWEEN \r\n"
			+ "            COALESCE((SELECT sequence_num FROM (\r\n" + "                SELECT \r\n"
			+ "                    l.line_Id AS tracking_status,\r\n"
			+ "                    l.line_name AS LINE_Name,\r\n" + "                    D.DIVISION_NAME,\r\n"
			+ "                    (COALESCE(d.SEQUENCE_NUMBER, 0) + 1) * 1000000 + \r\n"
			+ "                    (COALESCE(l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000 AS sequence_num\r\n"
			+ "                FROM gal195tbx l\r\n"
			+ "                LEFT JOIN GAL128TBX D ON l.DIVISION_ID = D.DIVISION_ID\r\n"
			+ "                WHERE d.DIVISION_NAME IN ('WE', 'PA', 'AF', 'VQ')\r\n"
			+ "            ) AS seq WHERE LINE_Name = ?2), 0) AND \r\n"
			+ "            COALESCE((SELECT sequence_num FROM (\r\n" + "                SELECT \r\n"
			+ "                    l.line_Id AS tracking_status,\r\n"
			+ "                    l.line_name AS LINE_Name,\r\n" + "                    D.DIVISION_NAME,\r\n"
			+ "                    (COALESCE(d.SEQUENCE_NUMBER, 0) + 1) * 1000000 + \r\n"
			+ "                    (COALESCE(l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000 AS sequence_num\r\n"
			+ "                FROM gal195tbx l\r\n"
			+ "                LEFT JOIN GAL128TBX D ON l.DIVISION_ID = D.DIVISION_ID\r\n"
			+ "                WHERE d.DIVISION_NAME IN ('WE', 'PA', 'AF', 'VQ')\r\n"
			+ "            ) AS seq WHERE LINE_Name = ?3), 999999999))\r\n"
			+ "        AND (?4 IS NULL OR  frame.product_spec_code IN ?5)\r\n"
			+ "        AND (?6 IS NULL OR CAST(RIGHT(frame.product_id, 6) AS INT) BETWEEN ?7 AND ?8)\r\n"
			+ "        AND (?9 IS NULL OR frame.product_id LIKE '%' || ?9 || '%' OR  frame.product_spec_code LIKE '%' || ?9 || '%' OR frame.kd_lot_number LIKE '%' || ?9 || '%'"
			+ " OR frame.engine_serial_no LIKE '%' || ?9 || '%' OR frame.production_lot LIKE '%' || ?9 || '%' OR frame.AF_ON_SEQUENCE_NUMBER LIKE '%' || ?9 || '%'"
			+ " OR frame.tracking_status LIKE '%' || ?9 || '%' OR pp.PROCESS_POINT_NAME LIKE '%' || ?9 || '%' OR b.SALES_MODEL_TYPE_CODE LIKE '%' || ?9 || '%' "
			+ " OR seq.line_name LIKE '%' || ?9 || '%')) \\r\\n "


			, countQuery = " SELECT * \r\n" + "FROM (\r\n" + "    SELECT count( DISTINCT frame.product_id) \r\n"
					+ "    FROM (\r\n" + "        SELECT \r\n" + "            l.line_Id AS tracking_status,\r\n"
					+ "            l.line_name AS LINE_Name,\r\n" + "            D.DIVISION_NAME,\r\n"
					+ "            (COALESCE(d.SEQUENCE_NUMBER, 0) + 1) * 1000000 + \r\n"
					+ "            (COALESCE(l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000 AS sequence_num\r\n"
					+ "        FROM gal195tbx l\r\n"
					+ "        LEFT JOIN GAL128TBX D ON l.DIVISION_ID = D.DIVISION_ID\r\n"
					+ "        WHERE d.DIVISION_NAME IN ('WE', 'PA', 'AF', 'VQ')\r\n" + "    ) AS seq\r\n"
					+ "    JOIN gal143tbx frame ON frame.tracking_status = seq.tracking_status\r\n"
					+ "    LEFT JOIN gal131tbx engine ON frame.engine_serial_no = engine.PRODUCT_ID\r\n"
					+ "    LEFT JOIN GAL214TBX pp ON frame.LAST_PASSING_PROCESS_POINT_ID = pp.PROCESS_POINT_ID\r\n"
					+ "    JOIN GAL144TBX b ON frame.PRODUCT_SPEC_CODE = b.PRODUCT_SPEC_CODE\r\n" + "    WHERE \r\n"
					+ "        ( ?1 IS NULL OR seq.sequence_num BETWEEN \r\n"
					+ "            COALESCE((SELECT sequence_num FROM (\r\n" + "                SELECT \r\n"
					+ "                    l.line_Id AS tracking_status,\r\n"
					+ "                    l.line_name AS LINE_Name,\r\n" + "                    D.DIVISION_NAME,\r\n"
					+ "                    (COALESCE(d.SEQUENCE_NUMBER, 0) + 1) * 1000000 + \r\n"
					+ "                    (COALESCE(l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000 AS sequence_num\r\n"
					+ "                FROM gal195tbx l\r\n"
					+ "                LEFT JOIN GAL128TBX D ON l.DIVISION_ID = D.DIVISION_ID\r\n"
					+ "                WHERE d.DIVISION_NAME IN ('WE', 'PA', 'AF', 'VQ')\r\n"
					+ "            ) AS seq WHERE LINE_Name = ?2), 0) AND \r\n"
					+ "            COALESCE((SELECT sequence_num FROM (\r\n" + "                SELECT \r\n"
					+ "                    l.line_Id AS tracking_status,\r\n"
					+ "                    l.line_name AS LINE_Name,\r\n" + "                    D.DIVISION_NAME,\r\n"
					+ "                    (COALESCE(d.SEQUENCE_NUMBER, 0) + 1) * 1000000 + \r\n"
					+ "                    (COALESCE(l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000 AS sequence_num\r\n"
					+ "                FROM gal195tbx l\r\n"
					+ "                LEFT JOIN GAL128TBX D ON l.DIVISION_ID = D.DIVISION_ID\r\n"
					+ "                WHERE d.DIVISION_NAME IN ('WE', 'PA', 'AF', 'VQ')\r\n"
					+ "            ) AS seq WHERE LINE_Name = ?3), 999999999))\r\n"
					+ "        AND (?4 IS NULL OR frame.product_spec_code IN ?5)\r\n"
					+ "        AND (?6 IS NULL OR CAST(RIGHT(frame.product_id, 6) AS INT) BETWEEN ?7 AND ?8) \r\n"
					+ "        AND (?9 IS NULL OR frame.product_id LIKE '%' || ?9 || '%' OR  frame.product_spec_code LIKE '%' || ?9 || '%' OR frame.kd_lot_number LIKE '%' || ?9 || '%'"
					+ " OR frame.engine_serial_no LIKE '%' || ?9 || '%' OR frame.production_lot LIKE '%' || ?9 || '%' OR frame.AF_ON_SEQUENCE_NUMBER LIKE '%' || ?9 || '%'"
					+ " OR frame.tracking_status LIKE '%' || ?9 || '%' OR pp.PROCESS_POINT_NAME LIKE '%' || ?9 || '%' OR b.SALES_MODEL_TYPE_CODE LIKE '%' || ?9 || '%' "
					+ " OR seq.line_name LIKE '%' || ?9 || '%')) \\r\\n "
					
					

			, nativeQuery = true)
	public Page<VinRangeProductDetails> findProductDetailsByVinRange(String trackingStatusFlag,
			String fromTrackingStatus, String toTrackingStatus, String specCodeFlag, List<String> specCode, String serailFlag,
			Integer fromSerial, Integer toSerial,String searchBy, Pageable pageable);
	
}
