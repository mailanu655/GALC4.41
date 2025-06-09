declare function require(path: string);
import { Component, OnInit, Output, EventEmitter, ViewChild, TemplateRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormControl, Validators } from '@angular/forms';
import Application from '../../../entities/Application';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationService } from 'src/app/services/data/application.service';
import { ConfigService } from 'src/app/services/config.service';
import { LogService } from 'src/app/services';


@Component({
  selector: 'app-new-application-form',
  templateUrl: './new-application-form.component.html',
  styleUrls: ['./new-application-form.component.css']
})
export class NewApplicationFormComponent implements OnInit  {
  @Output() applicationSaved = new EventEmitter();
  @ViewChild('addApplicationDialog') addApplicationDialog: TemplateRef<any>;
  @ViewChild('svgIconAlert') svgIconAlert: TemplateRef<any>;
  imgname= require('../../../../assets/img/favicon.svg');
  submitted = false;
  model = new Application();
  keycloakclients: any;
  selectedFile: any = null;
  nameFormControl = new FormControl('', [Validators.required]);
  descrFormControl = new FormControl('');
  urlFormControl = new FormControl('', [Validators.required]);
  clientFormControl = new FormControl('', [Validators.required]);
  fileType: string;
  fileName: string;
  defaultIcon: string;

  constructor(private appService: ApplicationService, private config: ConfigService, private logService: LogService,
    private dialog: MatDialog,
     private http: HttpClient) {     
    this.http.get(this.config.baseUrl + '/feedback/getAllKeycloakClients')
      .subscribe(
        response => {
          this.keycloakclients = JSON.parse(JSON.stringify(response['data']));
          const defaultValue='default';
          this.keycloakclients=[defaultValue].concat(this.keycloakclients);
          this.logService.info('keycloak clients -- ' + this.keycloakclients, [{ method: 'constructor' }]);
        },
      );
  }

  ngOnInit(): void {
    this.http.get('assets/img/favicon.svg',{responseType:'blob'}).subscribe(
      (res:Blob)=>{
        const reader = new FileReader();
        reader.readAsDataURL(res);
        reader.onload = () => {
          this.defaultIcon = reader.result.toString();
        };
      }
    )
    this.newApplication();
  }

  onSubmit() { this.submitted = true; }

  newApplication(){
    this.model = new Application();
    this.model.client = this.config.default;
  };

  saveApplication(model: Application){
    const startTime: any = new Date();

    if (model.description == null) model.description = "";
    if (model.client==null) model.client='default';
    if(undefined ==this.fileType || null ==this.fileType){
      this.selectedFile=this.defaultIcon;
      this.fileName='favicon.svg';
      this.fileType='svg';
    }
    if(this.fileType.toLowerCase()=='svg'){
    this.appService.insertApplication(model.name, model.description,model.url,model.client,this.selectedFile, this.fileName).subscribe((response:null) => {
      const endTime: any = new Date();

      this.applicationSaved.emit();
      this.newApplication();
      this.logService.debug('Application saved successfully', [{ method: 'saveApplication', duration: endTime - startTime }]);
    });
    this.dialog.closeAll();    
  }else {
    this.dialog.open(this.svgIconAlert);
    this.logService.error('Invalid file type', [{ method: 'saveApplication' }]);
  }
    
  }

  addApplication(){
    this.dialog.open(this.addApplicationDialog);
    this.logService.gui('add application dialog opened', [{ method: 'addApplication' }]);
  }

  onFileSelected(event: any): void {
    this.logService.gui('on file selected', [{ method: 'onFileSelected' }])
    this.fileName=event.target.files[0].name;
    const file: File = event.target.files[0];
    let size = Math.round(event.target.files[0].size / 10485760);
    this.fileType = file.name.split('.').pop();
    if (size <= 1 && this.fileType.toLowerCase()=='svg') {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.selectedFile = reader.result.toString();
        this.logService.debug('reader read svg file', [{ method: 'onFileSelected' }]);
      };
    }
    else{
      this.dialog.open(this.svgIconAlert);
      this.logService.error('Invalid file type', [{ method: 'onFileSelected' }]);
    }
  }

}
