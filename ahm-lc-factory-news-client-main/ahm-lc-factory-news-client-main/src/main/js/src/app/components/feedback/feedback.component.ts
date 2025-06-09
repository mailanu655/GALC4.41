import { HttpClient, HttpHeaders } from '@angular/common/http';
import { DatePipe } from '@angular/common'
import { RequestOptions, Headers, ResponseContentType } from '@angular/http';
import { Component, Attribute, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ConfigService } from '../../services/config.service';
import 'rxjs/add/operator/timeout';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/timeout';
import 'rxjs/add/observable/throw';
import { Observable } from 'rxjs-compat/Observable';
import { ActivatedRoute } from '@angular/router';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  }),
};

@Component({
  selector: 'feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class Feedback {
  @ViewChild('alertDialog') alertDialog: TemplateRef<any>;
  @ViewChild('successDialog') successDialog: TemplateRef<any>;
  @ViewChild('fileUploadErrorDialog') fileUploadErrorDialog: TemplateRef<any>;
  options: RequestOptions;
  responseType: { responseType: ResponseContentType; headers: Headers; };
  form: FormGroup;
  form1: FormGroup;
  reason: FormControl = new FormControl("", [Validators.required]);;
  emailGrp: string;
  uploadedFile: any;
  comment: string;
  assocFlag: boolean;
  assocNameFlag: boolean;
  assocLastNameFlag: boolean;
  reasonFlag: boolean;
  messageFlag: boolean;
  firstName: FormControl = new FormControl("");
  lastName: FormControl = new FormControl("");
  firstNm: FormControl = new FormControl("");
  lastNm: FormControl = new FormControl("");
  date: Date;
  createdDate: any;
  createdTime: any;
  time: string = '';
  feedbackEntity: any;
  statusList: any;
  emailGrps: any;
  defaultEmailGrp: string;
  defaultStatus: string;
  fileType: any;
  feedbackId: number;
  feedbackTypes: [];
  feedbackData: any = {};
  feedbackUrl: string;
  feedbackType: FormControl = new FormControl("", [Validators.required]);
  associateId: FormControl = new FormControl("", [Validators.required]);
  assocId: FormControl = new FormControl("", [Validators.required]);
  name: FormControl = new FormControl("", [Validators.required]);
  email: FormControl = new FormControl("", [Validators.required, Validators.email]);
  message: FormControl = new FormControl("", [Validators.required, Validators.maxLength(256)]);
  honeypot: FormControl = new FormControl(""); // we will use this to prevent spam
  submitted: boolean = false; // show and hide the success message
  isLoading: boolean = false; // disable the submit button if we're loading
  feedbackShow: boolean = false;
  selectedStatus: string;
  status: FormControl = new FormControl("", [Validators.required]);
  statusNumber: number;
  feedbackStatus: FormControl = new FormControl("", [Validators.required]);
  selectedStatusText: string;
  commentData: any = [];
  responseMessage: string; // the response message to show to the user
  @Attribute('format') format;


  constructor(private formBuilder: FormBuilder, private http: HttpClient,
    private config: ConfigService, private dialog: MatDialog,
    private activatedRoute: ActivatedRoute, private datePipe: DatePipe) {
    const subscription = this.activatedRoute.queryParams.subscribe(
      data => {
        try {

          this.feedbackShow = true;
          const token = atob(data['token']);
          if (token.indexOf('feedback') == 0) {
            this.feedbackId = parseInt(token.substring(8, token.length));
            this.getFeedbackInfo();
          }
        } catch (error) {

        }
      });



    this.feedbackUrl = this.config.feedbackClientUrl;
    this.http.get(this.config.feedbackBaseUrl + '/feedback/getFeedbackReasons')
      .subscribe(
        response => {
          this.feedbackTypes = JSON.parse(JSON.stringify(response));
        },
      );
    this.http.get(this.config.feedbackBaseUrl + '/feedback/getEmailGrps').subscribe(
      response => {
        this.emailGrps = JSON.parse(JSON.stringify(response));
        this.defaultEmailGrp = JSON.parse(JSON.stringify(response))[0];
      },
    );

    const headers = new Headers({ 'content-type': 'application/json' });
    this.options = new RequestOptions({ headers: headers });
    this.responseType = { responseType: ResponseContentType.Blob, headers: new Headers(headers) };
    this.time = this.getTime(new Date().getTime());
    this.date = new Date();
    this.form = this.formBuilder.group({
      associateId: this.associateId,
      firstName: this.firstName,
      lastName: this.lastName,
      name: this.associateId,
      email: this.email,
      message: this.message,
      reason: this.reason,
      honeypot: this.honeypot
    });
    this.form1 = this.formBuilder.group({});

  }
  openOtherDialog() {
    this.dialog.open(this.alertDialog);
  }
  ngOnInit(): void {
    this.getStatusesForFeedback();
  }
  onSubmit() {

  }

  getStatusesForFeedback() {
    this.http.post(this.config.feedbackBaseUrl + '/feedback/getStatusesForFeedback',
      httpOptions).subscribe(
        res => {
          this.statusList = [];
          const statusesResponse = JSON.parse(JSON.stringify(res));
          if (statusesResponse.message == 'SUCCESS') {
            if (!(statusesResponse.data == null || statusesResponse.data.length == 0)) {
              this.statusList = statusesResponse.data;
              //localStorage.setItem('assocList',JSON.stringify(this.associateList));
              console.log('this status list ', this.statusList);
            }

          }
          else {
            this.statusList = [];
          }
        }
      );
  }

  heading = 'Feedback';
  emailForm: FormGroup;

  myusername: string = '';
  emailRegex =
    '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$';

  changeReason(value) {
    console.log(value);
    this.reason = value;
  }

  changeStatus(value) {
    console.log('status value ', value);
    this.status = value;
    this.statusNumber = value;
  }

  changeEmail(value) {
    this.emailGrp = value;
  }

  onFileSelected(event) {
    console.log('file ', event.target.files[0]);
    const file: File = event.target.files[0];
    var size = Math.round(event.target.files[0].size / 10485760);
    if (size <= 1) {
      this.fileType = file.name.split('.').pop();
      console.log('file type ', this.fileType);
      const reader =new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        console.log('reader result ', reader.result);
        if(null!==reader){
        this.uploadedFile = reader?.result?.toString();
      }
      };
    }
    else {
      this.openFileUploadErrorDialog();
    }
  }

  sendMail(sendFeedbackRequestData, feedbackId) {

    const token = btoa('feedback' + feedbackId);
    sendFeedbackRequestData['feedbackId'] = feedbackId;
    this.feedbackUrl = this.config.feedbackClientUrl;
    sendFeedbackRequestData['feedbackUrl'] = this.config.feedbackClientUrl + '?token=' + token;
    this.http.post(this.config.feedbackBaseUrl + '/feedback/sendFeedbackEmail',
      JSON.stringify(sendFeedbackRequestData), httpOptions)
      .subscribe(response => {
        if ('SUCCESS' === JSON.parse(JSON.stringify(response)).message.toString()) {
          this.openSuccessDialog();
        }
      });
  }

  openSuccessDialog() {
    this.dialog.open(this.successDialog);
  }

  openFileUploadErrorDialog() {
    this.dialog.open(this.fileUploadErrorDialog);
  }

  checkFields() {
    if (this.form?.get("firstName")?.value && this.form.get("associateId")?.value && this.reason && this.form.get("message")?.value) {

      return false;
    }
    else {
      return true;
    }
  }

  checkFieldsSupport() {
    if (this.statusNumber >= 0 && this.comment) {
      return false;
    }
    else {
      return true;
    }
  }

  capitalizeString(stringText) {

    if (stringText.length > 1) {
      return stringText.charAt(0).toUpperCase() + stringText.slice(1);
    }
    else {
      return stringText.toUpperCase();
    }
  }

  saveFeedback() {
    const name = this.capitalizeString(this.form.get("firstName")?.value) + ' ' + (this.form.get("lastName")?.value ? this.capitalizeString(this.form.get("lastName")?.value) : '');
    const firstName = this.capitalizeString(this.form.get("firstName")?.value);
    const lastName = this.capitalizeString(this.form.get("lastName")?.value);

    const sendFeedbackRequestData = {
      'name': name,
      'firstName': firstName,
      'lastName': lastName,
      'associateId': this.form.get("associateId")?.value,
      'emailGrp': this.emailGrp ? this.emailGrp : this.emailGrps[0],
      'message': this.form.get("message")?.value,
      'reason': this.reason,
      'imageDefault': localStorage.getItem("captureImage"),
      'imageUploaded': this.uploadedFile ? this.uploadedFile : '',
      'fileType': this.fileType ? this.fileType : '',
      'appName': this.config.appName
    }

    this.http.post(this.config.feedbackBaseUrl + '/feedback/saveFeedback',
      JSON.stringify(sendFeedbackRequestData), httpOptions)
      .subscribe(response => {
        if ('SUCCESS' != JSON.parse(JSON.stringify(response)).message) {
          this.openOtherDialog();
        }
        this.feedbackEntity = JSON.parse(JSON.stringify(response));
        this.feedbackId = this.feedbackEntity.data[0].feedbackId;
        this.sendMail(sendFeedbackRequestData, JSON.parse(JSON.stringify(response)).data[0].feedbackId);
      });
  }

  getFeedbackInfoDetails(data) {
    return this.http.post(this.config.feedbackBaseUrl + '/feedback/getFeedbackInfoDetails', JSON.stringify(data),
      httpOptions).subscribe(res => {
        this.feedbackData = JSON.parse(JSON.stringify(res)).data[0];
        this.selectedStatus = this.feedbackData.status;

        this.commentData = this.feedbackData.feedbackMessages;
        for (let i = 0; i < this.commentData.length; i++) {
          if (this.commentData[i].createdOn != null) {
            this.commentData[i].createdDate = this.datePipe.transform(this.commentData[i].createdOn.substring(0, 10));
            this.commentData[i].createdTime = this.commentData[i].createdOn.substring(11, 16);
            this.comment = this.commentData[i].message;
            this.createdDate = this.datePipe.transform(this.commentData[i].createdOn.substring(0, 10));
            this.createdTime = this.commentData[i].createdOn.substring(11, 16);
          }
        }
        this.setFeedbackData();
        //this.feedbackShow = true;
      });
    //.map((res: Response) => res.json())
    //.timeout(1000 * 500);
  }

  openImage(index) {
    const requestData = {
      'attachmentId': index
    }

    this.http.post(this.config.feedbackBaseUrl + '/feedback/getFeedbackAttachment',
      JSON.stringify(requestData), httpOptions)
      .subscribe(
        async res => {
          const fileResp = JSON.parse(JSON.stringify(res));
          const base64Response = await fetch(fileResp.data[0].attachment);
          const blob = await base64Response.blob();
          var fileURL = window.URL.createObjectURL(blob);
          window.open(fileURL, '_blank');

        }
      );
  }

  saveFeedbackMessages() {
    this.lastName = this.form1.get("lastNm")?.value.trim();
    this.firstName = this.form1.get("firstNm")?.value.trim();
    this.associateId = this.form1.get("assocId")?.value.trim();

    for (let i = 0; i < this.statusList.length; i++) {
      if (parseInt(this.selectedStatus) == this.statusList[i].state) {
        status = this.statusList[i].state;
        this.selectedStatusText = this.statusList[i].status;
        this.defaultStatus = this.selectedStatusText;
      }
    }
    let url = window.location.href;
    if (url.search('&status') == -1) {
    } else {
      url = url.substring(0, url.indexOf('&'));
    }
    const requestData = {
      'feedbackId': this.feedbackData.feedbackId,
      'associateId': this.associateId,
      'firstName': this.firstName,
      'lastName': this.lastName,
      'message': this.form1.get("message")?.value,
      'reason': this.feedbackData.feedbackMessages[0].reason,
      'status': this.status ? this.status : this.statusNumber,
      'docId': this.feedbackData.docId,
      'docType': this.feedbackData.docType,
      'feedbackUrl': window.location.href,
      'emailGrp': this.feedbackData.emailgroups
    };
    return this.http.post(this.config.feedbackBaseUrl + '/feedback/saveFeedbackMessages',
      JSON.stringify(requestData), httpOptions).subscribe(
        res => {
          const feedbackMessageResponse = JSON.parse(JSON.stringify(res));
          if ('SUCCESS' == feedbackMessageResponse.message) {
            this.getFeedbackInfo();
          }
          else {
            this.openOtherDialog();
          }
        }
      );

  }

  getTime(data) {
    let utcSeconds: any = '';
    let date: any = '';
    utcSeconds = parseInt(data);
    date = new Date(utcSeconds);
    let minutes: string = '' + date.getMinutes();
    const hour = date.getHours();
    let formatted_time: string = '';
    if (minutes.length == 1) {
      minutes = '0' + minutes;
    }
    if (hour == 12) {
      formatted_time = hour + ':' + minutes + ' PM';
    } else if (hour > 12) {
      formatted_time = hour - 12 + ':' + minutes + ' PM';
    } else {
      formatted_time = hour + ':' + minutes + ' AM';
    }
    return formatted_time;
  }
  get validator() {
    return true;
  }
  closeWindow() {
    window.close();
  }

  close() {
    this.dialog.closeAll();
  }

  getFeedbackInfo() {
    const requestData = {
      'feedbackId': this.feedbackId
    }
    this.getFeedbackInfoDetails(requestData);
  }

  setFeedbackData() {
    this.firstName = this.feedbackData.firstName;
    this.lastName = this.feedbackData.lastName;
    this.associateId = this.feedbackData.associateId;
    this.form1 = this.formBuilder.group({
      message: this.message,
      status: this.status,
      firstNm: this.feedbackData.firstName,
      lastNm: this.feedbackData.lastName,
      assocId: this.feedbackData.associateId

    });
    for (let i = 0; i < this.statusList.length; i++) {
      if (parseInt(this.feedbackData.status) == this.statusList[i].state) {
        status = this.statusList[i].state;
        this.selectedStatusText = this.statusList[i].status;
        this.defaultStatus = this.selectedStatusText;
      }
    }
    console.log('this.selectedStatusText ', this.selectedStatusText);
    //this.defaultStatus = this.statusList[0].value;
    this.defaultStatus = this.feedbackData.status;
  }

  getDate(data) {
    const utcSeconds = parseInt(data);
    const formatted_date = new Date(utcSeconds);
    return formatted_date;
  }
  getFeedbackShow() {
    return this.feedbackShow && window.location.href.includes('token');
  }

  getStatus() {
    return this.selectedStatusText;
  }


  setAssocIdValidity() {
    if (this.form.get("associateId")?.value) {
      if (this.form.get("associateId")?.value.length < 4) {
        this.assocFlag = true;
      } else {
        this.assocFlag = false;
      }
    }
    else {
      this.assocFlag = true;
    }
  }

  setAssocFirstNameValidity() {
    if (this.form.get("firstName")?.value) {
      this.assocNameFlag = false;

    } else {
      this.assocNameFlag = true;
    }
  }

  setAssocLastName() {
    if (this.form.get("lastName")?.value) {
      this.assocLastNameFlag = false;

    } else {
      this.assocLastNameFlag = true;
    }
  }


  setFeedbackMesasageValidity() {

    if (this.form.get("message")?.value) {
      this.messageFlag = false;

    } else {
      this.messageFlag = true;
    }

  }
}