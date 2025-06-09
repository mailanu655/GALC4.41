import {
  Component,
  Inject,
  Optional,
  ViewChild,
  TemplateRef,
} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialog,
} from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from 'src/app/services/config.service';
import { ApplicationService } from 'src/app/services/data/application.service';
import { CategoryService } from 'src/app/services/data/category.service';
import { LogService } from 'src/app/services/log.service';

export interface UsersData {
  name: string;
  id: number;
}

@Component({
  selector: 'edit-dialog-box',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css'],
})
export class EditComponent {
  action: string;
  type: string;
  local_data: any;
  keycloakclients: any;
  selectedFile: any = null;
  fileType: string;
  fileName: any;
  @ViewChild('svgIconAlert') svgIconAlert: TemplateRef<any>;

  constructor(
    private appService: ApplicationService,
    private catService: CategoryService,
    private http: HttpClient,
    private config: ConfigService,
    public dialogRef: MatDialogRef<EditComponent>,
    public dialog: MatDialog,
    private logService: LogService,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: UsersData
  ) {
    this.local_data = { ...data };
    this.action = this.local_data.action;
    this.type = this.local_data.type;
    this.http
      .get(this.config.baseUrl + '/feedback/getAllKeycloakClients')
      .subscribe((response) => {
        this.keycloakclients = JSON.parse(JSON.stringify(response['data']));
        this.keycloakclients.push('default');
        this.logService.info('keycloak clients -- ' + this.keycloakclients, [
          {
            method: 'constructor',
          },
        ]);
      });
  }

  doAction() {
    this.logService.info('type -- ' + this.type, [{ method: 'doAction' }]);

    if (this.type == 'Category') {
      this.catService
        .modifyCategory(
          this.local_data.name,
          this.local_data.description,
          this.local_data.categoryId
        )
        .subscribe((response: null) => {});
    }
    if (this.type == 'Application') {
      this.logService.info(
        `
      inside the application edit: 
      client -- ${this.local_data.client}
      icon--- ${this.selectedFile}
      icon name--- ${this.fileName}
      `,
        [{ method: 'doAction' }]
      );

      this.appService
        .modifyApplication(
          this.local_data.applicationId,
          this.local_data.name,
          this.local_data.description,
          this.local_data.url,
          this.local_data.client,
          this.selectedFile,
          this.fileName
        )
        .subscribe((response: null) => {});
    }
    this.dialogRef.close({ event: this.action, data: this.local_data });
    this.logService.gui('edit dialog closed', [{ method: 'doAction' }]);
  }

  closeDialog() {
    this.dialogRef.close({ event: 'Cancel' });
    this.logService.gui('edit dialog closed', [{ method: 'closeDialog' }]);
  }

  onFileSelected(event: any): void {
    this.fileName = event.target.files[0].name;
    const file: File = event.target.files[0];
    let size = Math.round(event.target.files[0].size / 10485760);
    this.fileType = file.name.split('.').pop();
    this.logService.gui(
      `
      file type -- ${this.fileType}, s
      ize -- ${size}, 
      uploaded file -- ${file}, 
      app icon -- ${this.selectedFile}`,
      [{ method: 'onFileSelected' }]
    );
    if (size <= 1 && this.fileType.toLowerCase() == 'svg') {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.logService.debug('reader read svg file', [{
          method: 'onFileSelected',
        }]);
        this.selectedFile = reader.result.toString();
      };
    } else {
      this.dialog.open(this.svgIconAlert);
    }
  }
}
