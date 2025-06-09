import { OverlayContainer } from '@angular/cdk/overlay';
import { AfterViewInit, Component, Inject, ElementRef, OnInit, ViewChild, Input } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';

import { VinBomPartDto } from 'src/app/models/vin-bom-part-dto';
import { FrameSpec } from 'src/app/models/frame-spec';
import { ModelYearCode } from 'src/app/models/model-year-code';
import { FrameSpecService } from 'src/app/services/frame-spec.service';
import { PartAssignmentService } from 'src/app/services/part-assignment.service';
import { VinBomPart } from 'src/app/models/vin-bom-part';

export interface DialogData {
    isAddedNewBomPart: boolean;
}

  @Component({
    selector: "add-bom-part",
    templateUrl: "add-bom-part.component.html",
    styleUrls: ['../part-assignment.component.css']
  })
  export class AddBomPartComponent implements OnInit {
    addPartForm: FormGroup;
    submitted = false;
    public modelYearList: string[];
    public modelCodeList: string[];
    public modelTypeList: string[];
    public selecteModelYearCodeList: string[];
    public vinBomPart: VinBomPart = new VinBomPart();
    public vinBomPartDto: VinBomPartDto = new VinBomPartDto();
    public modelYearCodeList: ModelYearCode[];
    public modelYearCodeString: string[];
    public frameSpecList: FrameSpec[]
    public modelYearInDialog: string;
    public modelCodeInDialog: string;
    public modelTypeInDialog: string;
    public dcPartNoInDialog: string;
    public systemNameInDialog: string;

    constructor(
      private formBuilder: FormBuilder,
      public cdk: OverlayContainer,
      public dialogRef: MatDialogRef<AddBomPartComponent>,
      @Inject(MAT_DIALOG_DATA) public data: DialogData,
      public partAssignmentService: PartAssignmentService,
      public frameSpecService: FrameSpecService
    ) {}

    ngOnInit(): void {

        this.addPartForm = this.formBuilder.group({
            dcPartNoInDialog:  ['', [Validators.required, Validators.minLength(6)]],
            modelYearInDialog: ['', [Validators.required]],
            modelCodeInDialog: ['', [Validators.required]],
            modelTypeInDialog: ['', [Validators.required]],
            systemNameInDialog:['', [Validators.required, Validators.minLength(6)]]
        });

        this.modelYearInDialog = '';
        this.modelCodeInDialog = '';
        this.modelTypeInDialog = '';
        this.populateModelYear();
        //this.populateAllModelCode();
        this.populateAllModelType();
    }

    cancel(): void {
        this.dialogRef.close({ isAddedNewBomPart: this.data.isAddedNewBomPart });
    }

    save(): void {
      this.submitted = true;
      
      if (this.addPartForm.invalid) {
        return;
      }
      
      this.partAssignmentService.addBomPart(this.modelYearInDialog, this.modelCodeInDialog, this.modelTypeInDialog, this.dcPartNoInDialog, this.systemNameInDialog)
        .subscribe((vinBomPartDto: any) => {
          console.log("Part Added")
          this.data.isAddedNewBomPart = true;
          this.cancel();
        },(error: any) => {
          alert("Error Occured while adding Part");
          this.data.isAddedNewBomPart = false;
          console.log(error);
          return;
        });
    }

    toTop(id) {
        this.cdk.getContainerElement().childNodes.forEach((x: any) => {
            if (x.innerHTML.indexOf('id="' + id + '"') <= 0)
            x.style["z-index"] = 1000;
            else x.style["z-index"] = 1001;
        });
    }

    populateAllModelType() {
        this.frameSpecService.findAllModelTypebyProductType('FRAME')
            .subscribe((modelTypeList: string[]) => {
    
            this.modelTypeList = modelTypeList;
            },(error: any) => {
            
            });
      
    }
  
    // populateModelYear() {
    //   this.frameSpecService.findAllModelYearCodes()
    //     .subscribe((modelYearList: string[]) => {
  
    //       this.modelYearList = modelYearList;
    //     },(error: any) => {
          
    //     });
    // }

    populateModelYear() {
      this.frameSpecService.findAllModelYearCodes()
        .subscribe((modelYearList: string[]) => {
          this.modelYearList = modelYearList;
          this.populateAllModelCode();
          this.populateModelTypebyYMTOC(this.modelYearInDialog, this.modelCodeInDialog);
        },(error: any) => {
          
        });
    }
    
    populateModelTypebyYMTOC(modelYearCode: string, modelCode: string) {
      this.frameSpecService.findModelTypebyYMTOC(modelYearCode, modelCode, '', '', '', '')
        .subscribe((modelTypeList: string[]) => {
          this.modelTypeList = modelTypeList;
        },(error: any) => {
          
        });
    }
    populateModelCodeByYear(modelYear: string) {
      this.selecteModelYearCodeList = [];
      this.selecteModelYearCodeList.push(modelYear);
      this.frameSpecService.findModelCodeByYear(this.selecteModelYearCodeList)
        .subscribe((modelCodeList: string[]) => {
          this.modelCodeList = modelCodeList;
          console.log(this.modelCodeList);
        },(error: any) => {
        
      });
      this.populateModelTypebyYMTOC(modelYear, '');
      // this.modelYearCodeList = [];
      // this.frameSpecService.findAllByModelYearCode(this.addPartForm.value.modelYearInDialog)
      //   .subscribe((frameSpecList: FrameSpec[]) => {
      //     this.frameSpecList = frameSpecList;
      //     for (const frameSpec of this.frameSpecList) {
      //       let p = new ModelYearCode();
      //       p.modelCode = frameSpec.modelCode;
      //       p.year = frameSpec.modelYearCode;
      //       if(this.checkModelCodeExistence(p.modelCode) == true) {
  
      //       } else {
      //         this.modelYearCodeList.push(p);
      //       }
      //     }
      //   },(error: any) => {
      //     console.log(error);
      //   });
    }
  
    populateAllModelCode() {
      this.frameSpecService.findModelCodeByYear(this.modelYearList)
      .subscribe((modelCodeList: string[]) => {

        this.modelCodeList = modelCodeList;
        console.log(this.modelCodeList);
      },(error: any) => {
        
      });
      /*  this.frameSpecService.findAllModelCodeYear()
        .subscribe((response) => {
          this.modelYearCodeList = [];
          for(let key in response){
              let p = new ModelYearCode();
              p.modelCode = key.split('-')[0];
              p.year = response[key];
              this.modelYearCodeList.push(p);
              if(this.checkModelCodeExistence(p.modelCode) == true) {

              } else {
                this.modelYearCodeList.push(p);
              }
          } 
          //console.log(this.modelYearCodeList);
      });*/
    }

    populateModelTypeByYearAndCode(modelYear: string, modelCode: string) {
      if(modelYear == '' && modelCode == '') {
        this.populateAllModelType();
      } else {
        this.modelTypeList = [];
        modelCode = modelCode;
        this.frameSpecService.findAllByYMTOCWildCard(modelYear, modelCode)
        .subscribe((frameSpecList: FrameSpec[]) => {
          this.frameSpecList = frameSpecList;
          for (const frameSpec of this.frameSpecList) {
            let mt = frameSpec.modelTypeCode;
            if(this.modelTypeList.includes(mt)) {
  
            } else {
              this.modelTypeList.push(mt);
            }
          }
        },(error: any) => {
          console.log(error);
        });
      }
    }

  checkModelCodeExistence(modelCode: string): boolean {
    return this.modelYearCodeList.some(r => r.modelCode === modelCode);
  }

  get f() { return this.addPartForm.controls; }

}
