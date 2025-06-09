import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';

import { WeldScheduleMaterialModule } from './material-module';
import { WeldScheduleComponent } from './containers';
import { ScheduleRepTbxComponent } from './components/schedule-rep-tbx/schedule-rep-tbx.component';
import { LogPublishersService, LogService, LotsService } from './services';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { SentPipe } from './pipes';
import { BasicTableComponent } from './components/basic-table/basic-table.component';
import { ExpandRowBottomSheetComponent } from './components/expand-row-bottom-sheet/expand-row-bottom-sheet.component';
import { ExpandRowDialogComponent } from './components/expand-row-dialog/expand-row-dialog.component';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { RowFormDialogComponent } from './components/row-form-dialog/row-form-dialog.component';
import { Comments } from './components/comments/comments.component';
import { SpecMaskSelectComponent } from './components/spec-mask-select/spec-mask-select.component';
import { SpecMaskComponent } from './components/spec-mask/spec-mask.component';
import { HeaderComponent } from './components/header/header.component';
import { AboutComponent } from './components/dialog/about/about.component';
import { LineChartComponent } from './components/graphs/linegraph/linegraph.component';
import { BarChartComponent } from './components/graphs/bargraph/bargraph.component';
import { FooterComponent } from './components/footer/footer.component';
import { ApplicationService } from './services/data/application.service';
import { ControlsService } from './services/controls.service';
import { SecurityService } from './services/security.service';

import { DatePipe } from '@angular/common';
import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { ConfigService } from './services/config.service';
import { ConfigServiceProvider, keyCloakInitializer } from './services/config.service.provider';
import { SelectFilterComponent } from './components/schedule-rep-tbx/select-filter/select-filter.component';
import { TableDetailsComponent } from './components/schedule-rep-tbx/table-details/table-details.component';
import { SubAssyProdComponent } from './components/sub-assy-prod/sub-assy-prod.component';
import { AppRoutes } from './app.routes';
import { ConfirmationPopupComponent } from './components/confirmation-popup/confirmation-popup.component';
import { MoveBehindConfirmationPopupComponent } from './components/move-behind-confirmation-popup/move-behind-confirmation-popup.component';

@NgModule({
  declarations: [
    AppComponent,
    WeldScheduleComponent,
    SentPipe,
    BasicTableComponent,
    ExpandRowBottomSheetComponent,
    ExpandRowDialogComponent,
    SpinnerComponent,
    RowFormDialogComponent,
    Comments,
    SpecMaskSelectComponent,
    SpecMaskComponent,
    HeaderComponent,
    AboutComponent,
    LineChartComponent,
    BarChartComponent,
    FooterComponent,
    ScheduleRepTbxComponent,
    SelectFilterComponent,
    TableDetailsComponent,
    SubAssyProdComponent,
    ConfirmationPopupComponent,
    MoveBehindConfirmationPopupComponent
  ],
  imports: [
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    BrowserModule,
    KeycloakAngularModule,
    BrowserAnimationsModule,
    HttpClientModule,
    WeldScheduleMaterialModule,
    NoopAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    KeycloakAngularModule,
    AppRoutes,
  ],
  providers: [
    LotsService,
    LogService,
    LogPublishersService,
    ApplicationService,
    ControlsService,
    SecurityService,
    DatePipe,
    KeycloakService,
    ConfigServiceProvider, 
    { provide: APP_INITIALIZER, useFactory: keyCloakInitializer, multi: true, deps: [KeycloakService, ConfigService] },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
