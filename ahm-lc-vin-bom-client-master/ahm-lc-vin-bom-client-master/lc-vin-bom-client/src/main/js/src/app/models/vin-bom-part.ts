import { VinBomPartId } from "./vin-bom-part-id";

export class VinBomPart {
    modelYear: string;
    modelCode: string;
    modelType: string;
    dcPartNo: string;
    systemName: string;

    id: VinBomPartId;
    basePartNumber: string;
    description: string;
    effectiveBeginDate: string;
    effectiveEndDate: string;
    effectiveBeginDateString: string;
    effectiveEndDateString: string;
}