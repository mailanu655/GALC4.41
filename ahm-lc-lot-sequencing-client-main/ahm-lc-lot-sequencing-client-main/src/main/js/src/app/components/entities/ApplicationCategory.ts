export default class Plant {  
    id: Id;
    createTimestamp:string;
    updateTimestamp:string;
    createdBy:string;
    updatedBy:string;
}

export class Id {
    applicationId: number;
    categoryId: number; 
}
    