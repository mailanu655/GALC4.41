import { VinPartApprovalStatus } from "./vin-part-approval-status";

export class VinPartApproval {
    vinPartApprovalId: number;
    approveAssociateNumber: string;
    //approveStatus: VinPartApprovalStatus;
    approveStatus: string;
    approveTimestamp: string;
    currentDcPartNumber: string;
    currentShipStatus: boolean;
    letSystemName: string;
    newDcPartNumber: string;
    newShipStatus: boolean;
    productId: string;
    requestAssociateNumber: string;
    requestAssociateName: string;
    requestTimestamp: string;
    interchangeable: boolean;
    productSpecCode:string;
    productionLot:string;
}