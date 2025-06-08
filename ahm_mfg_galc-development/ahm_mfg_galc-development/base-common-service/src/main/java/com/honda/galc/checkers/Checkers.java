package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Subu Kathiresan
 * @date Jul 24, 2015
 */
public enum Checkers {
	PASSED_PROCESS_POINT_CHECKER		(CheckerType.Application, PassedProcessPointChecker.class),
	PRODUCT_MADE_FROM_CHECKER			(CheckerType.Application, ProductMadeFromChecker.class),
	PRODUCT_ORDER_ASSIGN_CHECKER		(CheckerType.Application, ProductOrderAssignChecker.class),
	PRODUCT_STOP_SHIP_CHECKER			(CheckerType.Application, ProductStopShipChecker.class),
	LOT_CONTROL_RULES_CHECKER			(CheckerType.Application, LotControlRulesChecker.class),
	OUTSTANDING_DEFECTS_CHECKER			(CheckerType.Application, OutstandingDefectsChecker.class),
	NOT_FIXED_DEFECTS_CHECKER			(CheckerType.Application, NotFixedDefectsChecker.class),
	OUTSTANDING_MCOPERATION_CHECKER		(CheckerType.Application, OutstandingMCOperationChecker.class),
	REQUIRED_PARTS_CHECKER				(CheckerType.Application, RequiredPartsChecker.class),
	DUPLICATE_ENGINE_CHECKER			(CheckerType.Application, DuplicateEngineChecker.class),
	LET_CHECKER							(CheckerType.Application, LetChecker.class),
	ENGINE_DOCKING_CHECKER				(CheckerType.Application, EngineDockingChecker.class),
	ENGINE_ON_HOLD_CHECKER				(CheckerType.Application, EngineOnHoldChecker.class),
	PRODUCT_ON_HOLD_CHECKER				(CheckerType.Application, ProductOnHoldChecker.class),
	NGLC_CHECKER						(CheckerType.Application, NglcChecker.class),
	IPU_CHECKER							(CheckerType.Application, IPUChecker.class),
	DUPLICATE_MBPN_PRODUCT_ID_CHECKER	(CheckerType.Application, DuplicateMbpnProductIdChecker.class),
	MBPN_PRODUCT_ID_FORMAT_CHECKER		(CheckerType.Application, MbpnProductIdFormatChecker.class),
	MBPN_ASSIGNED_CHECKER				(CheckerType.Application, MbpnProductIdAssignedChecker.class),
	VIOS_PMQA_RESULT_CHECKER			(CheckerType.Application, ViosPmqaResultChecker.class),
	VIN_BOM_SHIP_STATUS_CHECKER			(CheckerType.Application, VinBomShipStatusChecker.class),
	
	PART_SERIAL_MASK_CHECKER			(CheckerType.Part, PartMaskChecker.class),
	DUPLICATE_PART_CHECKER				(CheckerType.Part, DuplicatePartChecker.class),
	MBPN_STOP_SHIP_CHECKER				(CheckerType.Part, MbpnStopShipChecker.class),
	MBPN_SPEC_CODE_MATCH_CHECKER		(CheckerType.Part, MbpnSpecCodeMatchChecker.class),
	MBPN_MODEL_YEAR_MATCH_CHECKER		(CheckerType.Part, MbpnModelYearMatchChecker.class),
	MBPN_CHILD_PART_CHECKER				(CheckerType.Part, MbpnChildPartChecker.class),
	MBPN_TO_FRAME_CHECKER				(CheckerType.Part, MbpnToFrameChecker.class),
	ENGINE_TYPE_TO_FRAME_CHECKER        (CheckerType.Part, EngineTypetoFrameChecker.class),
	ENGINE_ALREADY_ASSIGNED_CHECKER     (CheckerType.Part, EngineAlreadyAssignedChecker.class),
	VERIFY_ALLOWED_PART_TYPE_CHECKER     (CheckerType.Part, VerifyAllowedChildPartTypeChecker.class),
	PART_ALREADY_ASSIGNED_CHECKER       (CheckerType.Part, PartAlreadyAssignedChecker.class),
	ENGINE_TO_FRAME_CHECKER 			(CheckerType.Part, EngineToFrameChecker.class),
	PART_SN_EXPIRATION_DATE_CHECKER 	(CheckerType.Part, PartSnExpirationDateChecker.class),
	ENGINE_TO_TRANSMISSION_TYPE_CHECKER	(CheckerType.Part, EngineToTransmissionTypeChecker.class),
	ENGINE_TO_TRANSMISSION_CHECKER		(CheckerType.Part, EngineToTransmissionChecker.class),
	PART_ON_HOLD_CHECKER				(CheckerType.Part, PartOnHoldChecker.class),
	FRAME_MANUFAC_DATE_MATCH_CHECKER	(CheckerType.Part, FrameManufacturedDateMatchChecker.class),
	SEAT_SENSOR_TEMPARATURE_CHECKER		(CheckerType.Part, SeatSensorTemperatureChecker.class),
	SEQUENCE_NUMBER_PLATE_CHECKER		(CheckerType.Part, SequenceNumberPlateChecker.class),
	BEARING_SERIAL_NUMBER_CHECKER		(CheckerType.Part, BearingSerialNumberChecker.class),
	BEARING_TRASH_SERIAL_NUMBER_CHECKER	(CheckerType.Part, BearingTrashSerialNumberChecker.class),
	IPU_LABEL_DATE_MATCH_CHECKER		(CheckerType.Part, IpuLabelDateMatchChecker.class),
	
	MEASUREMENT_CHECKER					(CheckerType.Measurement, MeasurementChecker.class),
	TORQUE_MEASUREMENT_CHECKER			(CheckerType.Measurement, TorqueMeasurementChecker.class);

	private CheckerType checkerType;
	private Class<? extends IChecker<?>> checkerClass;

	private <T extends IChecker<?>> Checkers(CheckerType checkerType, Class<T> checkerClass) {
		this.checkerType = checkerType;
		this.checkerClass = checkerClass;
	}
	
	public CheckerType getCheckerType() {
		return checkerType;
	}
	
	public Class<? extends IChecker<?>> getCheckerClass() {
		return checkerClass;
	}
	
	public static Checkers getChecker(String checkerId) {
		return Checkers.valueOf(checkerId);
	}
	
	public static List<Checkers> getCheckers(CheckerType type) {
		List<Checkers> checkerIds = new ArrayList<Checkers>();
		
		for(Checkers checkerId : Checkers.values()) {
			if(checkerId.getCheckerType().equals(type)) {
				checkerIds.add(checkerId);
			}
		}
		return checkerIds;
	}
	
	public static Checkers getCheckerByClassName(String checkerClassName) {
		for(Checkers checkerId : Checkers.values()) {
			if(checkerId.getCheckerClass().getName().equals(checkerClassName)) {
				return checkerId;
			}
		}
		return null;
	}
}
