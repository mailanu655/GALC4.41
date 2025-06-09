import jsPDF from 'jspdf';
import 'jspdf-autotable';

// Extend jsPDF with autoTable plugin
declare module 'jspdf' {
    interface jsPDF {
        autoTable: (options: any) => jsPDF;
    }
}

export default jsPDF;