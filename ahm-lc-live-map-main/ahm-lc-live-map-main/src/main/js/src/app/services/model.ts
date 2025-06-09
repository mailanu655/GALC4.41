import { Entity } from "../lib/rest-read.service";

export class PartIssue extends Entity<number> {
    id: number;
    jobId: number;
    processPointId: string;
    productId: string;
    partName: string;
    statusId: number;
    dismissed: boolean;
}

export class Product {
    id: string;
    sequence: number;
    specCode: string;
    prodLot: string;
    kdLot: string;
    ein: string;
    extColor: string;
    model: string;
    afOnSequence: number;
    trackingStatus: string;
    weOnProcessPointId: string;
    weOnTs: Date;
    weOffProcessPointId: string;
    weOffTs: Date;
    paOnProcessPointId: string;
    paOnTs: Date;
    paOffProcessPointId: string;
    paOffTs: Date;
    afOnProcessPointId: string;
    afOnTs: Date;
    afOffProcessPointId: string;
    afOffTs: Date;
    locationId: number;
    locationProcessPointId: string;
    locationProcessTs: Date;
    lastProcessPointId: string;
    lastProcessTs: Date;
    afStatus: string;

    assignedLocationId: number;
    defects: Defect[];
    partIssues: PartIssue[];
    pmqaIssues: PMQAIssue[];
    holds: Hold[];
}

export class Defect {
    id: number;
    productId: string;
    partName: string;
    secondaryPartName: string;
    partLocation: string;
    defectType: string;
    defectStatus: string;
    associateNo: string;
    responsibleDept: string;
    responsibleZone: string;
    responsibleLine: string;
    writeupDept: string;
    entryDept: string;
    gdpDefect: string;
    actualTimestamp: Date;
}

export class HoldId {
    productId: string;
    holdTypeId: number;
    processTs: Date;
}

export class Hold {
    id: HoldId;
    released: number;
    releaseTs: Date;
    associateId: string;
    associateName: string;
    associatePhone: string;
    associatePager: string;
    reason: string;
    processPointId: string;
    lotHoldStatusId: number;
    qsrId: number;
    equipmentFlag: number;
    createTs: Date;
    updateTts: Date;
}

export class Lot {
    id: string;
    kdLot: string;
    startProductId: string;
    size: number;
    kdLotSize: number;
    productionDate: Date;
    lotNumber: string;
    remake: string;
    specCode: string;
    processTs: Date;
    paOnTs: Date;
    model: string;
}

export class PMQAIssue {
    productId: string;
    processPointId: string;
    partName: string;
    status: string;
}