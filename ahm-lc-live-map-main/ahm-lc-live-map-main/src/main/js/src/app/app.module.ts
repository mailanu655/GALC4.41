import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule } from '@angular/material/snack-bar'
import { MatSlideToggleModule } from '@angular/material/slide-toggle'
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTabsModule } from '@angular/material/tabs'
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { AngularSplitModule } from 'angular-split';
import { MatDialogModule } from '@angular/material/dialog';
//import { ChartsModule } from 'ng2-charts';
//import { DatePipe } from '@angular/common'
import { AppComponent } from './app.component';
import { MapComponent } from './components/map/map.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { DefectsComponent } from './components/defects/defects.component';
import { ProductsComponent } from './components/products/products.component';
import { LineComponent } from './components/line/line.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LegendComponent } from './components/legend/legend.component';
import { SettingsComponent } from './components/settings/settings.component';
import { ConfirmComponent } from './components/confirm/confirm.component';
import { LcTableComponent } from './components/lc-table/lc-table.component';
import { PartIssuesComponent } from './components/part-issues/part-issues.component';
import { ProductComponent } from './components/product/product.component';
import { HoldsComponent } from './components/holds/holds.component';
import { StragglersComponent } from './components/stragglers/stragglers.component';
import { SimpleViewComponent } from './components/simple-view/simple-view.component';
import { CompositeViewComponent } from './components/composite-view/composite-view.component';
import { LineViewComponent } from './components/line-view/line-view.component';
import { MetricsComponent } from './components/metrics/metrics.component';
import { UpcomingLotsComponent } from './components/upcoming-lots/upcoming-lots.component';
import { AboutComponent } from './components/about/about.component';
import { PMQAIssuesComponent } from './components/pmqa-issues/pmqa-issues.component';

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        FooterComponent,
        DashboardComponent,
        MapComponent,
        LineComponent,
        DefectsComponent,
        ProductsComponent,
        LegendComponent,
        SettingsComponent,
        ConfirmComponent,
        LcTableComponent,
        PartIssuesComponent,
        ProductComponent,
        HoldsComponent,
        StragglersComponent,
        SimpleViewComponent,
        CompositeViewComponent,
        LineViewComponent,
        MetricsComponent,
        UpcomingLotsComponent,
        AboutComponent,
        PMQAIssuesComponent
    ],
    imports: [
        AngularSplitModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        BrowserModule,
        ClipboardModule,
        CommonModule,
        FormsModule,
        HttpClientModule,
        MatDialogModule,
        MatSlideToggleModule,
        MatButtonModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatDividerModule,
        MatMenuModule,
        MatSidenavModule,
        MatSortModule,        
        MatTableModule,
        MatTooltipModule,
        MatSnackBarModule,
        MatTabsModule
        //ChartsModule
    ],
    providers: [/*DatePipe*/],
    bootstrap: [AppComponent]
})
export class AppModule { }
