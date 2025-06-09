import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { FormControl } from '@angular/forms';
import { RuleLotMaintenanceService } from 'src/app/services/rule-lot-maintenance.service';
import { ModelPartLotDto } from 'src/app/models/model-part-lot-dto';
import { RuleAssignmentService } from 'src/app/services/rule-assignment.service';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';
import { Message } from 'src/app/models/message';
import { AlertService } from 'src/app/alert/alert.service';
import { DcmsDto } from 'src/app/models/dcms-dto';
import { ModelPart } from 'src/app/models/model-part';
import { forkJoin, of } from 'rxjs';
import { LotAssignmentService } from 'src/app/services/lot-assignment.service';
import { DesignChangeRule } from 'src/app/models/design-change-rule';

export interface DialogData {
  cbuReflash: boolean;
  scrapParts: boolean;
  stragglerInterChangeable: boolean;
  selectRowData: ModelPartLotDto[];
  isChangeRuleSuccess: boolean;
}

@Component({
  selector: 'app-change-rules-dialog',
  templateUrl: './change-rules-dialog.component.html',
  styleUrls: ['./change-rules-dialog.component.css']
})
export class ChangeRulesDialogComponent implements OnInit {
  public cbuReflash: boolean;
  public scrapParts: boolean;
  public stragglerInterChangeable: boolean;
  public designChangeRuleList:DesignChangeRule[];
  public designChangeRuleMap = new Map();
  dcmsDto: DcmsDto = new DcmsDto();
  modelPart: ModelPart = new ModelPart();
  selectRowData: ModelPartLotDto[];
  modelPartLot: ModelPartLotDto;

  public message: Message = { type: null, message: null };
  messageOptionsAutoClose = {
    autoClose: false,
    keepAfterRouteChange: false
  };
  
  constructor(
    public cdk: OverlayContainer,
    private elementRef: ElementRef,
    public dialogRef: MatDialogRef<ChangeRulesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    protected alertService: AlertService,
    private confirmationDialogService: ConfirmationDialogService,
    public ruleLotMaintenanceService: RuleLotMaintenanceService,
    public ruleAssignmentService: RuleAssignmentService,
    public lotAssignmentService: LotAssignmentService
  ) {

  }

  ngOnInit(): void {
    this.selectRowData = this.data.selectRowData;
    this.populateDesignChangeRuleMap();
  }

  

  save() {
    this.data.isChangeRuleSuccess = false;
    if(this.cbuReflash === undefined || this.scrapParts === undefined || this.stragglerInterChangeable === undefined) {
      alert("Please select Rules");
      return;
    }
    let count=0;
    let invalid=0;
    let errorMap = new Map();
    let cbuReflasherrorMap = new Map();
    let stragglerInterrorMap = new Map();
    let scrapParterrorMap = new Map();
    this.selectRowData.forEach(async (eachRow, index) => {
     
      const isValid = await this.ruleAssignmentService.validateRuleSelection(eachRow.dcClass, this.cbuReflash, this.stragglerInterChangeable, this.scrapParts).toPromise();
      if (!isValid) {
          console.log('Selected Rule does not match expected rules for: ' + eachRow.dcClass);
          invalid++;
          count++;
          console.log(invalid);
          
          if(errorMap.has(eachRow.dcClass)){
            let c = errorMap.get(eachRow.dcClass);
            //c= c+1;
            if(c.indexOf(eachRow.dcNumber) !== -1 ){
              console.log(eachRow.dcNumber +"- already exists");
            }else{
              c.push(eachRow.dcNumber);
            }
           
            errorMap.set(eachRow.dcClass,c);
          
          }else{
            let d : string[]=[];
            d.push(eachRow.dcNumber);
            errorMap.set(eachRow.dcClass,d);
          }
          cbuReflasherrorMap.set(eachRow.dcNumber,this.cbuReflash ?'Y':'N');
          stragglerInterrorMap.set(eachRow.dcNumber,this.stragglerInterChangeable?'Y':'N');
          scrapParterrorMap.set(eachRow.dcNumber,this.scrapParts?'Y':'N');

          if(count > this.selectRowData.length || count === this.selectRowData.length ){
          
              console.log(index);
              this.update(count,invalid,errorMap, cbuReflasherrorMap, stragglerInterrorMap,scrapParterrorMap);
             
              //this.closeChildWindow(this.selectRowData.length, count);
          } 
        
      } else {
          console.log('Selected Rule matches expected rules for: ' + eachRow.dcClass);
          count++;
          if(count > this.selectRowData.length || count === this.selectRowData.length ){
          
            console.log(index);
            this.update(count,invalid, errorMap, cbuReflasherrorMap, stragglerInterrorMap,scrapParterrorMap);
            //this.closeChildWindow(this.selectRowData.length, count);
          }
       }
      
    });
     //this.closeChildWindow(this.selectRowData.length, this.count);
  }

  

  async update(recCount:number,errorCount:number, errorMap:Map<string,string[]>, cbuReflasherrorMap:Map<string,string>, stragglerInterrorMap:Map<string,string>,scrapParterrorMap:Map<string,string>){
    let count = 0;
    let cancelledCount = 0;
    if(errorCount > 0){ 
      if(this.designChangeRuleMap.size === 0){
        this.populateDesignChangeRuleMap();
      }
      
        console.log("invalid rules count - "+ errorCount);
        errorMap.forEach(async(values: string[], key: string) => {
          let designChangeRule:DesignChangeRule = this.designChangeRuleMap.get(key);
          let standardReflash:string = '';
          let standardInt:string ='' ;
          let standardScrap:string = '';
          let msg:string='';

          if(designChangeRule){
            standardReflash = this.getVinBomRuleRequiredValue(designChangeRule.reflash);
            standardInt = this.getVinBomRuleRequiredValue(designChangeRule.interchangable);
            standardScrap = this.getVinBomRuleRequiredValue(designChangeRule.scrapParts);
          }else{
            console.log("design change Rule not found for class -"+key);
          }
              values.forEach(async function (value) {
            //NALC-1084
                  msg = msg + 
                  '< '+value+'> ' + key + '- Class line items do not match standard rule guideline. <br>'+
                  'CBU Reflash (Selected = '+ cbuReflasherrorMap.get(value)+', Standard = '+standardReflash +' ) <br>'+
                  'Straggler-New (Selected = '+ stragglerInterrorMap.get(value)+', Standard = '+standardInt+' ) <br>'+
                  'Interchangeable (Selected = '+ scrapParterrorMap.get(value)+', Standard = '+standardScrap+' ) <br>';
                });  
               
                  console.log(msg);
                
                const isConfirmed = await this.confirmationDialogService.confirmLarge('Please confirm..  Selected Design Change Rules do not match rules for ', '<b>'+ msg+' </b> <br> Do you want to continue? ', 'Yes', 'No', 'lg');
                if(isConfirmed) {
                  console.log("User selected: Yes");

                  this.selectRowData.forEach(async(eachRow, index) => {
                    this.validateAndSave(eachRow);
                        count++;
                        
                    });
                    this.closeChildWindow(this.selectRowData.length, count);
                } else {  
                  count++;
                  cancelledCount++;
                  console.log("User selected: No");
                  this.closeChildWindow(this.selectRowData.length, count);
                  //this.showMessage(this.selection.selected.length, count, cancelledCount);
                }
         
      });
    }else{
        
        console.log(" no invalid rules count  ");
        this.selectRowData.forEach(async(eachRow, index) => {
        this.validateAndSave(eachRow);
        count++;
        
        });
        this.closeChildWindow(this.selectRowData.length, count);
      }
    
  }

  closeChildWindow(totalRows: number, processedRows: number) {
    console.log(totalRows + " - " + processedRows);
    if (totalRows === processedRows) {
      this.data.isChangeRuleSuccess = true;
      this.cancel();

    }
  }

  

  validateAndSave(modelPartLotDto: ModelPartLotDto) {
    if(modelPartLotDto.startingProductionLot === undefined || modelPartLotDto.startingProductionLot == null || modelPartLotDto.startingProductionLot.trim() == "-" || modelPartLotDto.startingProductionLot.trim() == "") {
      this.saveModelPart(modelPartLotDto);
    } else {
      this.saveModelPartApproval(modelPartLotDto);
    }
  }

  async saveModelPart(item: ModelPartLotDto) {
    this.modelPart.modelPartId = item.modelPartId;
    this.modelPart.productSpecWildcard = item.productSpecWildcard;
    this.modelPart.letSystemName = item.letSystemName;
    this.modelPart.dcPartNumber = item.dcPartNumber;
    this.modelPart.active = 'INACTIVE';
    this.modelPart.dcClass = item.dcClass;
    this.modelPart.dcEffBegDate = item.dcEffBegDate;
    this.modelPart.dcNumber = item.dcNumber;    
    this.modelPart.interchangeable = this.stragglerInterChangeable;
    this.modelPart.reflash = this.cbuReflash;
    this.modelPart.scrapParts = this.scrapParts;

    await this.ruleAssignmentService.saveModelPart(this.modelPart).toPromise();
  }

  async saveModelPartApproval(item: ModelPartLotDto) {
    await this.lotAssignmentService.saveModelPartApproval(item, item.planCode,item.startingProductionLot, this.stragglerInterChangeable, this.scrapParts, this.cbuReflash).toPromise();
  }

  toTop(id) {
    this.cdk.getContainerElement().childNodes.forEach((x: any) => {
      if (x.innerHTML.indexOf('id="' + id + '"') <= 0)
        x.style["z-index"] = 1000;
      else x.style["z-index"] = 1001;
    });
  }
  
  cancel(): void {
    //this.dialogRef.close({ });
    this.dialogRef.close({ isChangeRuleSuccess: this.data.isChangeRuleSuccess });
  }

  populateDesignChangeRuleMap(){
    let designchangeRules = this.ruleAssignmentService.getDesignChangeRules().subscribe((designChangeRuleList: DesignChangeRule[]) => {
     this.designChangeRuleList = designChangeRuleList;
     designChangeRuleList.forEach((value:DesignChangeRule,index:number)=>{
       console.log(value);
     this.designChangeRuleMap.set(value.dcClass,value);
       index++;
     });
    },(error: any) => {
         
   });
  }
 getVinBomRuleRequiredValue(value:string):string{
   console.log('value-' + value);
   return value.toUpperCase().indexOf('NOT_APPLIED')!== -1?'N':value.toUpperCase().indexOf('REQUIRED') !== -1?'Y':value.toUpperCase().indexOf('OPTIONAL')!== -1 ?'Optional':value.toString();
  
 }
  

}
