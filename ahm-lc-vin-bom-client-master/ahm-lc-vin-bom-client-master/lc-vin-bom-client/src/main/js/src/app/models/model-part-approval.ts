import { InterchangeableId } from "./interchangeableId";
import { ModelPart } from "./model-part";

export class ModelPartApproval {
    model: string;
    modelType: string;
    modelPartApprovalId: number;
    approveAssociateNumber: string;
    approveStatus: string;
    approveTimestamp: string;
    currentInterchangable: boolean;
    currentReflash: boolean;
    currentScrapParts: boolean;
    currentStartingProductionLot: string;
    currentStartingProductionDate: string;
    modelPartId: number;
    newInterchangable: boolean;
    newReflash: boolean;
    newScrapParts: boolean;
    newStartingProductionLot: string;
    newStartingProductionDate: string;
    requestAssociateNumber: string;
    requestAssociateName: string;
    requestTimestamp: string;
    modelPart: ModelPart;

}