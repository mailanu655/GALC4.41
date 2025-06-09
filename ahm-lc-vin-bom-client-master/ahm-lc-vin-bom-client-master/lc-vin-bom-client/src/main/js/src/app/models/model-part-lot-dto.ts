import { InterchangeableId } from "./interchangeableId";

export class ModelPartLotDto {
    modelPartId: number;
    model: string;
    modelType: string;
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
    planCode: string;
    startingProductionLot: string ='-';
}
