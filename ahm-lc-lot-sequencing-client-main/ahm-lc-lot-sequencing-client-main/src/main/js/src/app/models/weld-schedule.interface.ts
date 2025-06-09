export interface WeldScheduleModel {
  productionLot?: string;
  lotSize?: number;
  startProductId?: string;
  holdStatus?: number;
  lotNumber?: string;
  planCode?: string;
  processLocation?: string;
  modelCode?: string;
  isRemake?: string;
  extColor?: string;
  modelType?: string;
  productSpecCode?: string;
  plantCode?: string;
  kdLotNumber?: string;
  seqNumber?: number;
  remakeFlag?: string;
  stampingFlag?: string;
  carryInOutFlag?: string;
  carryInOutUnits?: number;
  buildSeqNotFixedFlag?: string;
  buildSequenceNumber?: string;
  mbpn?: string;
  hesColor?: string;
  demandType?: string;
  notes?: string;
  comments?: string;
  partNumber?: string;
  partMark?: string;
  productionDate?: string;
  createTimestamp?: string;
  updateTimestamp?: string;
  expanded?: boolean;
  subData?: LotProductModel[];
  isEdit?: boolean;
  commentsOptions?: string[];
}

export interface ProductIdModel {
  productionLot: string;
  productID: string;
}

export interface LotProductModel {
  productId: string;
  buildSequence?: number;
  stampedSequence?: number;
  sendStatus?: string;
}

export interface Message{
  message:string;
}
