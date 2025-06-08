package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.entity.product.FrameMTOCMasterSpec;
import com.honda.galc.entity.product.FrameMTOCMasterSpecId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 21, 2017
 */
public class Gmm111Dto implements IMsipInboundDto {

	private static final long serialVersionUID = 1L;
	
	private String plantCodeFrame;
	private String modelYearCode;
	private String modelCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private String engineModelCode;
	private String engineModelTypeCode;
	private String engineModelYearCode;
	private String engineOptionCode;
	private String fE;
	private String modelYearDescription;
	private String plantCodeEngine;
	private String salesModelOptionCode;
	private String extColorCode;
	private String intColorCode;
	private String extColorDescription;
	private String intColorDescription;
	private String salesModelCode;
	private String salesModelTypeCode;
	private String salesExtColorCode;
	private String salesIntColorCode;
	private String prototypeCode;
	private String frameNoPrefix;
	private String seriesCode;
	private String seriesDescription;
	private String gradeCode;
	private String bodyAndTransTypeCode;
	private String bodyAndTransTypeDesc;
	private String boundaryMarkRequired;
	private String altSalesModelCode;
	private String altSalesModelTypeCode;
	private String productSpecCode;
	private String engineMTO;

	public Gmm111Dto() {}

	public String getPlantCodeFrame() {
		return plantCodeFrame;
	}

	public void setPlantCodeFrame(String plantCodeFrame) {
		this.plantCodeFrame = plantCodeFrame;
	}

	public String getModelYearCode() {
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOptionCode() {
		return modelOptionCode;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getEngineModelCode() {
		return engineModelCode;
	}

	public void setEngineModelCode(String engineModelCode) {
		this.engineModelCode = engineModelCode;
	}

	public String getEngineModelTypeCode() {
		return engineModelTypeCode;
	}

	public void setEngineModelTypeCode(String engineModelTypeCode) {
		this.engineModelTypeCode = engineModelTypeCode;
	}

	public String getEngineModelYearCode() {
		return engineModelYearCode;
	}

	public void setEngineModelYearCode(String engineModelYearCode) {
		this.engineModelYearCode = engineModelYearCode;
	}

	public String getEngineOptionCode() {
		return engineOptionCode;
	}

	public void setEngineOptionCode(String engineOptionCode) {
		this.engineOptionCode = engineOptionCode;
	}

	public String getFE() {
		return fE;
	}

	public void setFE(String fE) {
		this.fE = fE;
	}

	public String getModelYearDescription() {
		return modelYearDescription;
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}

	public String getPlantCodeEngine() {
		return plantCodeEngine;
	}

	public void setPlantCodeEngine(String plantCodeEngine) {
		this.plantCodeEngine = plantCodeEngine;
	}

	public String getSalesModelOptionCode() {
		return salesModelOptionCode;
	}

	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getExtColorCode() {
		return extColorCode;
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	public String getIntColorCode() {
		return intColorCode;
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	public String getExtColorDescription() {
		return extColorDescription;
	}

	public void setExtColorDescription(String extColorDescription) {
		this.extColorDescription = extColorDescription;
	}

	public String getIntColorDescription() {
		return intColorDescription;
	}

	public void setIntColorDescription(String intColorDescription) {
		this.intColorDescription = intColorDescription;
	}

	public String getSalesModelCode() {
		return salesModelCode;
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelTypeCode() {
		return salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	public String getSalesExtColorCode() {
		return salesExtColorCode;
	}

	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	public String getSalesIntColorCode() {
		return salesIntColorCode;
	}

	public void setSalesIntColorCode(String salesIntColorCode) {
		this.salesIntColorCode = salesIntColorCode;
	}

	public String getPrototypeCode() {
		return prototypeCode;
	}

	public void setPrototypeCode(String prototypeCode) {
		this.prototypeCode = prototypeCode;
	}

	public String getFrameNoPrefix() {
		return frameNoPrefix;
	}

	public void setFrameNoPrefix(String frameNoPrefix) {
		this.frameNoPrefix = frameNoPrefix;
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getBodyAndTransTypeCode() {
		return bodyAndTransTypeCode;
	}

	public void setBodyAndTransTypeCode(String bodyAndTransTypeCode) {
		this.bodyAndTransTypeCode = bodyAndTransTypeCode;
	}

	public String getBodyAndTransTypeDesc() {
		return bodyAndTransTypeDesc;
	}

	public void setBodyAndTransTypeDesc(String bodyAndTransTypeDesc) {
		this.bodyAndTransTypeDesc = bodyAndTransTypeDesc;
	}

	public String getBoundaryMarkRequired() {
		return boundaryMarkRequired;
	}

	public void setBoundaryMarkRequired(String boundaryMarkRequired) {
		this.boundaryMarkRequired = boundaryMarkRequired;
	}

	public void setEngineMTO(String engineMTO) {
		this.engineMTO = engineMTO;
	}

	public String getEngineMTO() {
		return engineMTO;
	}

	public void setAltSalesModelCode(String altSalesModelCode) {
		this.altSalesModelCode = altSalesModelCode;
	}

	public String getAltSalesModelCode() {
		return altSalesModelCode;
	}

	public void setAltSalesModelTypeCode(String altSalesModelTypeCode) {
		this.altSalesModelTypeCode = altSalesModelTypeCode;
	}

	public String getAltSalesModelTypeCode() {
		return altSalesModelTypeCode;
	}

	// Copy Constructor
	public Gmm111Dto(Gmm111Dto mtoSpec) {
		this.bodyAndTransTypeCode = mtoSpec.getBodyAndTransTypeCode();
		this.bodyAndTransTypeDesc = mtoSpec.getBodyAndTransTypeDesc();
		this.boundaryMarkRequired = mtoSpec.getBoundaryMarkRequired();
		this.engineModelCode = mtoSpec.getEngineModelCode();
		this.engineModelTypeCode = mtoSpec.getEngineModelTypeCode();
		this.engineModelYearCode = mtoSpec.getEngineModelYearCode();
		this.engineMTO = mtoSpec.getEngineMTO();
		this.engineOptionCode = mtoSpec.getEngineOptionCode();
		this.extColorCode = mtoSpec.getExtColorCode();
		this.extColorDescription = mtoSpec.getExtColorDescription();
		this.fE = mtoSpec.getFE();
		this.frameNoPrefix = mtoSpec.getFrameNoPrefix();
		this.gradeCode = mtoSpec.getGradeCode();
		this.intColorCode = mtoSpec.getIntColorCode();
		this.intColorDescription = mtoSpec.getIntColorDescription();
		this.modelCode = mtoSpec.getModelCode();
		this.modelOptionCode = mtoSpec.getModelOptionCode();
		this.modelTypeCode = mtoSpec.getModelTypeCode();
		this.modelYearCode = mtoSpec.getModelYearCode();
		this.modelYearDescription = mtoSpec.getModelYearDescription();
		this.plantCodeEngine = mtoSpec.getPlantCodeEngine();
		this.plantCodeFrame = mtoSpec.getPlantCodeFrame();
		this.productSpecCode = mtoSpec.getProductSpecCode();
		this.prototypeCode = mtoSpec.getPrototypeCode();
		this.salesExtColorCode = mtoSpec.getSalesExtColorCode();
		this.salesIntColorCode = mtoSpec.getIntColorCode();
		this.salesModelCode = mtoSpec.getSalesModelCode();
		this.salesModelOptionCode = mtoSpec.getSalesModelOptionCode();
		this.salesModelTypeCode = mtoSpec.getSalesModelTypeCode();
		this.seriesCode = mtoSpec.getSeriesCode();
		this.seriesDescription = mtoSpec.getSeriesDescription();		
	}

	public FrameSpec deriveFrameSpec(){
		FrameSpec frameSpec = new FrameSpec();
		frameSpec.setBodyAndTransTypeCode(bodyAndTransTypeCode);
		frameSpec.setBodyAndTransTypeDesc(bodyAndTransTypeDesc);
		frameSpec.setBoundaryMarkRequired(boundaryMarkRequired);
		frameSpec.setEngineMto(engineMTO);
		frameSpec.setEnginePlantCode(plantCodeEngine);
		frameSpec.setExtColorCode(extColorCode);
		frameSpec.setExtColorDescription(extColorDescription);
		frameSpec.setFrameNoPrefix(frameNoPrefix);
		frameSpec.setGradeCode(gradeCode);
		frameSpec.setIntColorCode(intColorCode);
		frameSpec.setIntColorDescription(intColorDescription);
		frameSpec.setModelCode(modelCode);
		frameSpec.setModelOptionCode(modelOptionCode);
		frameSpec.setModelTypeCode(modelTypeCode);
		frameSpec.setModelYearCode(modelYearCode);
		frameSpec.setModelYearDescription(modelYearDescription);
		frameSpec.setPlantCodeGpcs(plantCodeFrame);
		frameSpec.setProductSpecCode(productSpecCode);
		frameSpec.setPrototypeCode(prototypeCode);
		frameSpec.setSalesExtColorCode(salesExtColorCode);
		frameSpec.setSalesIntColorCode(salesIntColorCode);
		frameSpec.setSalesModelCode((altSalesModelCode != null && altSalesModelCode.trim().length() > 0) ? altSalesModelCode : salesModelCode);
		frameSpec.setSalesModelTypeCode((altSalesModelTypeCode != null && altSalesModelTypeCode.trim().length() > 0) ? altSalesModelTypeCode : salesModelTypeCode);
		frameSpec.setSeriesCode(seriesCode);
		frameSpec.setSeriesDescription(seriesDescription);

		return frameSpec;
	}

	public FrameMTOCMasterSpec deriveframeMtocMasterSpec() {
		FrameMTOCMasterSpec frameMtocMasterSpec = new FrameMTOCMasterSpec();
		frameMtocMasterSpec.setId(deriveId());
		frameMtocMasterSpec.setBodyAndTransTypeCode(bodyAndTransTypeCode);
		frameMtocMasterSpec.setBodyAndTransTypeDesc(bodyAndTransTypeDesc);
		frameMtocMasterSpec.setEngineModelCode(engineModelCode);
		frameMtocMasterSpec.setEngineModelTypeCode(engineModelTypeCode);
		frameMtocMasterSpec.setEngineModelYearCode(engineModelYearCode);
		frameMtocMasterSpec.setEngineOptionCode(engineOptionCode);
		frameMtocMasterSpec.setExtColorDescription(extColorDescription);
		frameMtocMasterSpec.setFE(fE);
		frameMtocMasterSpec.setFrameNoPrefix(frameNoPrefix);
		frameMtocMasterSpec.setGradeCode(gradeCode);
		frameMtocMasterSpec.setIntColorDescription(intColorDescription);
		frameMtocMasterSpec.setModelYearDescription(modelYearDescription);
		frameMtocMasterSpec.setPlantCodeEngine(plantCodeEngine);
		frameMtocMasterSpec.setPrototypeCode(prototypeCode);
		frameMtocMasterSpec.setSalesExtColorCode(salesExtColorCode);
		frameMtocMasterSpec.setSalesIntColorCode(salesIntColorCode);
		frameMtocMasterSpec.setSalesModelCode(salesModelCode);
		frameMtocMasterSpec.setSalesModelOptionCode(salesModelOptionCode);
		frameMtocMasterSpec.setSalesModelTypeCode(salesModelTypeCode);
		frameMtocMasterSpec.setSeriesCode(seriesCode);
		frameMtocMasterSpec.setSeriesDescription(seriesDescription);
		return frameMtocMasterSpec;
	}

	private FrameMTOCMasterSpecId deriveId() {
		FrameMTOCMasterSpecId frameMtocMasterSpecId = new FrameMTOCMasterSpecId();
		frameMtocMasterSpecId.setExtColorCode(extColorCode);
		frameMtocMasterSpecId.setIntColorCode(intColorCode);
		frameMtocMasterSpecId.setModelCode(modelCode);
		frameMtocMasterSpecId.setModelTypeCode(modelTypeCode);
		frameMtocMasterSpecId.setModelYearCode(modelYearCode);
		frameMtocMasterSpecId.setModelOptionCode(modelOptionCode);
		frameMtocMasterSpecId.setPlantCodeFrame(plantCodeFrame);
		return frameMtocMasterSpecId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plantCodeFrame == null) ? 0 : plantCodeFrame.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((engineModelCode == null) ? 0 : engineModelCode.hashCode());
		result = prime * result + ((engineModelTypeCode == null) ? 0 : engineModelTypeCode.hashCode());
		result = prime * result + ((engineModelYearCode == null) ? 0 : engineModelYearCode.hashCode());
		result = prime * result + ((engineOptionCode == null) ? 0 : engineOptionCode.hashCode());
		result = prime * result + ((fE == null) ? 0 : fE.hashCode());
		result = prime * result + ((modelYearDescription == null) ? 0 : modelYearDescription.hashCode());
		result = prime * result + ((plantCodeEngine == null) ? 0 : plantCodeEngine.hashCode());
		result = prime * result + ((salesModelOptionCode == null) ? 0 : salesModelOptionCode.hashCode());
		result = prime * result + ((extColorCode == null) ? 0 : extColorCode.hashCode());
		result = prime * result + ((intColorCode == null) ? 0 : intColorCode.hashCode());
		result = prime * result + ((extColorDescription == null) ? 0 : extColorDescription.hashCode());
		result = prime * result + ((intColorDescription == null) ? 0 : intColorDescription.hashCode());
		result = prime * result + ((salesModelCode == null) ? 0 : salesModelCode.hashCode());
		result = prime * result + ((salesModelTypeCode == null) ? 0 : salesModelTypeCode.hashCode());
		result = prime * result + ((salesExtColorCode == null) ? 0 : salesExtColorCode.hashCode());
		result = prime * result + ((salesIntColorCode == null) ? 0 : salesIntColorCode.hashCode());
		result = prime * result + ((prototypeCode == null) ? 0 : prototypeCode.hashCode());
		result = prime * result + ((frameNoPrefix == null) ? 0 : frameNoPrefix.hashCode());
		result = prime * result + ((seriesCode == null) ? 0 : seriesCode.hashCode());
		result = prime * result + ((seriesDescription == null) ? 0 : seriesDescription.hashCode());
		result = prime * result + ((gradeCode == null) ? 0 : gradeCode.hashCode());
		result = prime * result + ((bodyAndTransTypeCode == null) ? 0 : bodyAndTransTypeCode.hashCode());
		result = prime * result + ((bodyAndTransTypeDesc == null) ? 0 : bodyAndTransTypeDesc.hashCode());
		result = prime * result + ((boundaryMarkRequired == null) ? 0 : boundaryMarkRequired.hashCode());
		result = prime * result + ((altSalesModelCode == null) ? 0 : altSalesModelCode.hashCode());
		result = prime * result + ((altSalesModelTypeCode == null) ? 0 : altSalesModelTypeCode.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((engineMTO == null) ? 0 : engineMTO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gmm111Dto other = (Gmm111Dto) obj;
		if (plantCodeFrame == null) {
			if (other.plantCodeFrame != null)
				return false;
		} else if (!plantCodeFrame.equals(other.plantCodeFrame))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
			return false;
		if (engineModelCode == null) {
			if (other.engineModelCode != null)
				return false;
		} else if (!engineModelCode.equals(other.engineModelCode))
			return false;
		if (engineModelTypeCode == null) {
			if (other.engineModelTypeCode != null)
				return false;
		} else if (!engineModelTypeCode.equals(other.engineModelTypeCode))
			return false;
		if (engineModelYearCode == null) {
			if (other.engineModelYearCode != null)
				return false;
		} else if (!engineModelYearCode.equals(other.engineModelYearCode))
			return false;
		if (engineOptionCode == null) {
			if (other.engineOptionCode != null)
				return false;
		} else if (!engineOptionCode.equals(other.engineOptionCode))
			return false;
		if (fE == null) {
			if (other.fE != null)
				return false;
		} else if (!fE.equals(other.fE))
			return false;
		if (modelYearDescription == null) {
			if (other.modelYearDescription != null)
				return false;
		} else if (!modelYearDescription.equals(other.modelYearDescription))
			return false;
		if (plantCodeEngine == null) {
			if (other.plantCodeEngine != null)
				return false;
		} else if (!plantCodeEngine.equals(other.plantCodeEngine))
			return false;
		if (salesModelOptionCode == null) {
			if (other.salesModelOptionCode != null)
				return false;
		} else if (!salesModelOptionCode.equals(other.salesModelOptionCode))
			return false;
		if (extColorCode == null) {
			if (other.extColorCode != null)
				return false;
		} else if (!extColorCode.equals(other.extColorCode))
			return false;
		if (intColorCode == null) {
			if (other.intColorCode != null)
				return false;
		} else if (!intColorCode.equals(other.intColorCode))
			return false;
		if (extColorDescription == null) {
			if (other.extColorDescription != null)
				return false;
		} else if (!extColorDescription.equals(other.extColorDescription))
			return false;
		if (intColorDescription == null) {
			if (other.intColorDescription != null)
				return false;
		} else if (!intColorDescription.equals(other.intColorDescription))
			return false;
		if (salesModelCode == null) {
			if (other.salesModelCode != null)
				return false;
		} else if (!salesModelCode.equals(other.salesModelCode))
			return false;
		if (salesModelTypeCode == null) {
			if (other.salesModelTypeCode != null)
				return false;
		} else if (!salesModelTypeCode.equals(other.salesModelTypeCode))
			return false;
		if (salesExtColorCode == null) {
			if (other.salesExtColorCode != null)
				return false;
		} else if (!salesExtColorCode.equals(other.salesExtColorCode))
			return false;
		if (salesIntColorCode == null) {
			if (other.salesIntColorCode != null)
				return false;
		} else if (!salesIntColorCode.equals(other.salesIntColorCode))
			return false;
		if (prototypeCode == null) {
			if (other.prototypeCode != null)
				return false;
		} else if (!prototypeCode.equals(other.prototypeCode))
			return false;
		if (frameNoPrefix == null) {
			if (other.frameNoPrefix != null)
				return false;
		} else if (!frameNoPrefix.equals(other.frameNoPrefix))
			return false;
		if (seriesCode == null) {
			if (other.seriesCode != null)
				return false;
		} else if (!seriesCode.equals(other.seriesCode))
			return false;
		if (seriesDescription == null) {
			if (other.seriesDescription != null)
				return false;
		} else if (!seriesDescription.equals(other.seriesDescription))
			return false;
		if (gradeCode == null) {
			if (other.gradeCode != null)
				return false;
		} else if (!gradeCode.equals(other.gradeCode))
			return false;
		if (bodyAndTransTypeCode == null) {
			if (other.bodyAndTransTypeCode != null)
				return false;
		} else if (!bodyAndTransTypeCode.equals(other.bodyAndTransTypeCode))
			return false;
		if (bodyAndTransTypeDesc == null) {
			if (other.bodyAndTransTypeDesc != null)
				return false;
		} else if (!bodyAndTransTypeDesc.equals(other.bodyAndTransTypeDesc))
			return false;
		if (boundaryMarkRequired == null) {
			if (other.boundaryMarkRequired != null)
				return false;
		} else if (!boundaryMarkRequired.equals(other.boundaryMarkRequired))
			return false;
		if (altSalesModelCode == null) {
			if (other.altSalesModelCode != null)
				return false;
		} else if (!altSalesModelCode.equals(other.altSalesModelCode))
			return false;
		if (altSalesModelTypeCode == null) {
			if (other.altSalesModelTypeCode != null)
				return false;
		} else if (!altSalesModelTypeCode.equals(other.altSalesModelTypeCode))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (engineMTO == null) {
			if (other.engineMTO != null)
				return false;
		} else if (!engineMTO.equals(other.engineMTO))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}



