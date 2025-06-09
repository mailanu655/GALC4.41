import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule, DatePipe } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTabsModule } from '@angular/material/tabs';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { MatDialogModule } from '@angular/material/dialog';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { HttpModule } from '@angular/http';
import { MatSelectModule } from '@angular/material/select';
import { MatNativeDateModule, MAT_DATE_FORMATS } from '@angular/material/core';
import { MatListModule } from '@angular/material/list';
import { PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { MomentDateModule } from '@angular/material-moment-adapter';
import { MatTreeModule } from '@angular/material/tree';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/footer/footer.component';
import { AboutComponent } from './components/about/about.component';
import { HeaderComponent } from './components/header/header.component';
import { BodyComponent } from './components/body/body.component';
import { HomeComponent } from './components/home/home.component';
import { ParentComponent } from './components/parent/parent.component';
import { Feedback } from './components/feedback/feedback.component';
import { ConfigComponent } from './components/config/config/config.component';
import { AppRoutingModule } from './app-routing.module';
import { ConfigContentComponent } from './components/config/config-content/config-content.component';
import { AddConfigComponent } from './components/config/add-config/add-config.component';
import { EditConfigComponent } from './components/config/edit-config/edit-config.component';
import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { ConfigServiceProvider, keyCloakInitializer } from './services/config.service.provider';
import { ConfigService } from './services/config.service';
import { FnConfigServiceService } from './services/fn-config-service.service';
export const MY_DATE_FORMATS = {
  parse: {
    dateInput: 'MM/DD/YYYY',
  },
  display: {
    dateInput: 'MM/DD/YYYY',
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY'
  },
};

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
  suppressScrollY: false
};

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    AboutComponent,
    HeaderComponent,
    BodyComponent,
    HomeComponent,
    ParentComponent,
    Feedback,
    ConfigComponent,
    ConfigContentComponent,
    AddConfigComponent,
    EditConfigComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    CommonModule,
    MatSlideToggleModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    ClipboardModule,
    MatDividerModule,
    MatSidenavModule,
    MatTooltipModule,
    MatTableModule,
    MatSortModule,
    MatSnackBarModule,
    MatMenuModule,
    MatTabsModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    FlexLayoutModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatDatepickerModule,
    HttpModule,
    MatSelectModule,
    MatNativeDateModule,
    MatListModule,
    PerfectScrollbarModule,
    MomentDateModule,
    MatTreeModule,
    AppRoutingModule,
    KeycloakAngularModule
  ],
  providers: [
    DatePipe,
    ConfigServiceProvider,
    FnConfigServiceService,
    KeycloakService,
    { provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMATS },
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: 'outline' },
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    },
    {
      provide: APP_INITIALIZER,
      useFactory: keyCloakInitializer,
      multi: true,
      deps: [KeycloakService, ConfigService, FnConfigServiceService]
    },
  ],
  bootstrap: [AppComponent],
  entryComponents: []
})
export class AppModule { }
