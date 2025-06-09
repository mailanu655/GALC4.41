import { NgModule, APP_INITIALIZER } from '@angular/core';
import { NgbModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { NgxLoadingModule } from 'ngx-loading';
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';
import { Feedback } from './components/feedback/feedback.component';
import { AppRoutingModule } from './app-routing.module';
import { AppRoutes } from './app.routes';
import { TokenInterceptor } from './interceptors/token-interceptor';

import { MatAutocompleteModule} from '@angular/material/autocomplete';
import { AlertModule } from './alert/alert.module';
import { HeaderComponent } from './components/header/header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PartAssignmentComponent } from './components/part-assignment/part-assignment.component';
import { LotAssignmentComponent } from './components/lot-assignment/lot-assignment.component';
import { AddPartComponent } from './components/add-part/add-part.component';
import { UtilityService } from './services/utility-service';
import { UtilityComponent } from './components/header/utility.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DATE_LOCALE } from '@angular/material/core'
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule} from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { InterchangeableComponent } from './components/interchangeable/interchangeable.component';
//import { AddBomPartDialog } from './components/part-assignment/add-bom-part-dialog';
import { ManualOverrideComponent } from './components/manual-override/manual-override.component';
import { ChangeDcPartNumberDialogComponent } from './components/manual-override/change-dc-part-number-dialog/change-dc-part-number-dialog.component';
import { ManualOverrideApprovalComponent } from './components/manual-override-approval/manual-override-approval.component';
import { CategoryMaintComponent } from './components/category-maint/category-maint.component';
import { AddCategoryDialog } from './components/category-maint/add-category-dialog';
import { RuleAssignmentComponent } from './components/rule-assignment/rule-assignment.component';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { ConfirmationDialogService } from './confirmation-dialog/confirmation-dialog.service';
import { RuleLotApprovalComponent } from './components/rule-lot-approval/rule-lot-approval.component';
import { RuleLotMaintenanceComponent } from './components/rule-lot-maintenance/rule-lot-maintenance.component';
import { AddBomPartComponent } from './components/part-assignment/add-bom-part/add-bom-part.component';
import { ChangeRulesDialogComponent } from './components/rule-lot-maintenance/change-rules-dialog/change-rules-dialog.component';
import { SelectProdLotDialogComponent } from './components/select-prod-lot-dialog/select-prod-lot-dialog.component';

import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { ConfigService } from './services/config.service';
import { ConfigServiceProvider, keyCloakInitializer } from './services/config.service.provider';
import { SecurityService } from './services/security.service';
import { AboutComponent } from './components/about/about.component';
import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    AppComponent,
    UtilityComponent,
    HeaderComponent,
    DashboardComponent,
    PartAssignmentComponent,
  //  AddBomPartDialog,
    AddPartComponent,
    LotAssignmentComponent,
    InterchangeableComponent,
    ManualOverrideComponent,
    ChangeDcPartNumberDialogComponent,
    ManualOverrideApprovalComponent,
    CategoryMaintComponent ,
    AddCategoryDialog,
	RuleAssignmentComponent,
    ConfirmationDialogComponent,
    RuleLotApprovalComponent,
    RuleLotMaintenanceComponent,
    AddBomPartComponent,
    ChangeRulesDialogComponent,
    SelectProdLotDialogComponent,
    AboutComponent,
    Feedback

],
  imports: [
    NgbModule,
    NgbAlertModule,
    KeycloakAngularModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserModule,
    AppRoutingModule,
    AppRoutes,
    AlertModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatSelectModule,
    MatSortModule,
    MatGridListModule,
    MatPaginatorModule,
    MatRadioModule,
    MatSnackBarModule,
    MatListModule,
    MatTooltipModule,
    MatSidenavModule,
    MatExpansionModule,
    MatTabsModule,
    MatDialogModule,
    MatProgressBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatToolbarModule,
    MatMenuModule,
    NgxLoadingModule,
    DragDropModule
],
entryComponents: [PartAssignmentComponent,
  //AddBomPartDialog
],
providers: [
  UtilityService,
  ConfirmationDialogService,
  DatePipe,
  SecurityService,
  ConfigServiceProvider,
  { provide: APP_INITIALIZER, useFactory: keyCloakInitializer, multi: true, deps: [KeycloakService, ConfigService] },
  { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }
],

  bootstrap: [AppComponent]
})
export class AppModule { }
