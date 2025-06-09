import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CompositeViewComponent } from './components/composite-view/composite-view.component';
import { LineViewComponent } from './components/line-view/line-view.component';
import { SimpleViewComponent } from './components/simple-view/simple-view.component';
import { ProductComponent } from './components/product/product.component';

const routes: Routes = [
    { path: '', component: DashboardComponent },
    { path: 'it', component: DashboardComponent },
    { path: ':site', component: CompositeViewComponent },
    { path: ':site/line', component: LineViewComponent },
    { path: ':site/:view', component: SimpleViewComponent },
    { path: ':site/products/:id', component: ProductComponent },
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, { useHash: true })
    ],
    exports: [RouterModule]
})
export class AppRoutingModule { }