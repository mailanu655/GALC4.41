import * as XLSX from 'xlsx';
import { Table2SheetOpts } from 'xlsx';
import { formatDate } from '@angular/common';

export const EXCEL_EXTENSION = '.xlsx';

export class Export {

    // === xls export === //
    public static exportTableToExcel(tableElement: any, fileName: string, sheetName?: string, options?: Table2SheetOpts): void {
        const worksheet: XLSX.WorkSheet = XLSX.utils.table_to_sheet(tableElement, options);
        Export.exportToExcel(worksheet, fileName, sheetName);
    }

    public exportJsonToExcel(json: any[], fileName: string, sheetName?: string, options?: XLSX.JSON2SheetOpts): void {
        const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(json, options);
        Export.exportToExcel(worksheet, fileName, sheetName);
    }

    public static exportToExcel(worksheet: XLSX.WorkSheet, fileName: string, sheetName?: string) {
        if (!sheetName) {
            sheetName = 'Data';

        }
        const workbook: XLSX.WorkBook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, sheetName);
        if (!fileName.endsWith(EXCEL_EXTENSION)) {
            fileName = fileName + EXCEL_EXTENSION;
        }
        XLSX.writeFile(workbook, fileName);
    }

    public static printHtml(headerLine: string, bodyContent: string, options?: string) {

        if (!options) {
            let w = 1000;
            let h = 800;
            let left = 0.5 * screen.width - 0.5 * w;
            let top = 0.5 * screen.width - 0.5 * h;
            options = 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left;
        }
        var printWindow = window.open('', 'PRINT', options);
        if (!printWindow) {
            return;
        }
        printWindow.document.write('<html>');
        printWindow.document.write('<head>');
        if (headerLine) {
            printWindow.document.write('<title>' + headerLine + '</title>');
            headerLine = document.title + "  " + headerLine;
        } else {
            printWindow.document.write('<title>' + document.title + '</title>');
        }
        printWindow.document.write('<style> ');
        printWindow.document.write('table {border: 1px solid grey; border-collapse: collapse; padding: 0px; overflow: hidden;}');
        printWindow.document.write('th, td {white-space: nowrap; border-right: 1px solid grey; border-bottom: 1px solid grey; border-collapse: collapse; padding: 5px 5px 5px 5px}');
        printWindow.document.write('a { color: rgb(54, 69, 79); }');
        printWindow.document.write('a:link { text-decoration: none; }');
        printWindow.document.write('</style>');
        printWindow.document.write('</head>');
        printWindow.document.write('<body>');
        if (headerLine) {
            printWindow.document.write('<h2>' + headerLine + '</h1>');
        }
        printWindow.document.write(bodyContent);
        printWindow.document.write('</body>');
        printWindow.document.write('</html>');
        printWindow.document.close();
        printWindow.print();
        printWindow.close();
    }

    // === timestamp str token === //
    public static getTsToken(): string {
        var date = new Date();
        var dateStr = formatDate(date, 'yyyyMMddTHHmmss', 'en-US');
        return dateStr;
    }
}