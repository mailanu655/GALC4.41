package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;


/*
 * 
 * @author Gangadhararao Gadde 
 * @since Feb 06, 2014
 */
@Entity
@Table(name="GAL183TBX")
public class InspectionSampling extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InspectionSamplingId id;

	@Column(name="EMISSION_DENOMINATOR")
	private Integer emissionDenominator;

	@Column(name="EMISSION_MOLECULE")
	private Integer emissionMolecule;

	@Column(name="DIMENSION_DENOMINATOR")
	private Integer dimensionDenominator;

	@Column(name="DIMENSION_MOLECULE")
	private Integer dimensionMolecule;

	@Column(name="EMISSION_DENOMINATOR_MASTER")
	private Integer emissionDenominatorMaster;

	@Column(name="EMISSION_MOLECULE_MASTER")
	private Integer emissionMoleculeMaster;

	@Column(name="DIMENSION_DENOMINATOR_MASTER")
	private Integer dimensionDenominatorMaster;

	@Column(name="DIMENSION_MOLECULE_MASTER")
	private Integer dimensionMoleculeMaster;

	public InspectionSampling() {
		super();
	}

	public InspectionSampling(InspectionSamplingId id,
			Integer emissionDenominator, Integer emissionMolecule,
			Integer dimensionDenominator, Integer dimensionMolecule,
			Integer emissionDenominatorMaster, Integer emissionMoleculeMaster,
			Integer dimensionDenominatorMaster, Integer dimensionMoleculeMaster) {
		super();
		this.id = id;
		this.emissionDenominator = emissionDenominator;
		this.emissionMolecule = emissionMolecule;
		this.dimensionDenominator = dimensionDenominator;
		this.dimensionMolecule = dimensionMolecule;
		this.emissionDenominatorMaster = emissionDenominatorMaster;
		this.emissionMoleculeMaster = emissionMoleculeMaster;
		this.dimensionDenominatorMaster = dimensionDenominatorMaster;
		this.dimensionMoleculeMaster = dimensionMoleculeMaster;
	}

	public Integer getEmissionDenominator() {
		return emissionDenominator;
	}

	public void setEmissionDenominator(Integer emissionDenominator) {
		this.emissionDenominator = emissionDenominator;
	}

	public Integer getEmissionMolecule() {
		return emissionMolecule;
	}

	public void setEmissionMolecule(Integer emissionMolecule) {
		this.emissionMolecule = emissionMolecule;
	}

	public Integer getDimensionDenominator() {
		return dimensionDenominator;
	}

	public void setDimensionDenominator(Integer dimensionDenominator) {
		this.dimensionDenominator = dimensionDenominator;
	}

	public Integer getDimensionMolecule() {
		return dimensionMolecule;
	}

	public void setDimensionMolecule(Integer dimensionMolecule) {
		this.dimensionMolecule = dimensionMolecule;
	}

	public Integer getEmissionDenominatorMaster() {
		return emissionDenominatorMaster;
	}

	public void setEmissionDenominatorMaster(Integer emissionDenominatorMaster) {
		this.emissionDenominatorMaster = emissionDenominatorMaster;
	}

	public Integer getEmissionMoleculeMaster() {
		return emissionMoleculeMaster;
	}

	public void setEmissionMoleculeMaster(Integer emissionMoleculeMaster) {
		this.emissionMoleculeMaster = emissionMoleculeMaster;
	}

	public Integer getDimensionDenominatorMaster() {
		return dimensionDenominatorMaster;
	}

	public void setDimensionDenominatorMaster(Integer dimensionDenominatorMaster) {
		this.dimensionDenominatorMaster = dimensionDenominatorMaster;
	}

	public Integer getDimensionMoleculeMaster() {
		return dimensionMoleculeMaster;
	}

	public void setDimensionMoleculeMaster(Integer dimensionMoleculeMaster) {
		this.dimensionMoleculeMaster = dimensionMoleculeMaster;
	}

	public InspectionSamplingId getId() {
		return this.id;
	}

	public void setId(InspectionSamplingId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return toString(getId().getModelCode(),getId().getModelTypeCode(),getDimensionDenominator(),getDimensionDenominatorMaster(),getDimensionMolecule(),getDimensionMoleculeMaster(),getEmissionDenominator(),getEmissionDenominatorMaster(),getEmissionMolecule(),getEmissionMoleculeMaster());
	}

}
