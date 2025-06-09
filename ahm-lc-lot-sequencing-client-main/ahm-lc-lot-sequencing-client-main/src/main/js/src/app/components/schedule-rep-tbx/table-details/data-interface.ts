export interface User {
    id: number;
    sourceProcLoc: string;
    destProcLoc: string;
    destSpecCode: string;
    subAssyProdIdFormat: string;
    prodDateOffset: string;
    isEdit: boolean;
    status?: '';
}

export const UserColumns = [
    {
        key: 'sourceProcLoc',
        type: 'appUpperCase',
        label: 'SOURCE_PROC_LOC',
        required: true,
        disabledEdit: true
    },
    {
        key: 'destProcLoc',
        type: 'appUpperCase',
        label: 'DEST_PROC_LOC',
        required: true,
        disabledEdit: true
    },
    {
        key: 'destSpecCode',
        type: 'destSpecCode',
        label: 'DEST_SPEC_CODE',
        required: true,
        disabledEdit: true
    },
    {
        key: 'subAssyProdIdFormat',
        type: 'subAssyProdIdFormat',
        label: 'SUB_ASSY_PROD_ID_FORMAT',
    },
    {
        key: 'prodDateOffset',
        type: 'text',
        label: 'PROD_DATE_OFFSET',
    },
    {
        key: 'isEdit',
        type: 'isEdit',
        label: '',
    },
];