import { InterchangeableId } from "./interchangeableId";

export class ModelPart {
    modelPartId: number;
    productSpecWildcard: string;
    letSystemName: string;
    dcPartNumber: string;
    active: string;
    dcClass: string;
    dcEffBegDate: string;
    dcNumber: string;    
    interchangeable: boolean;
    reflash: boolean;
    scrapParts: boolean;
}