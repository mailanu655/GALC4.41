import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BodyComponent } from './components/body/body.component';
import { HomeComponent } from './components/home/home.component';
import { ConfigContentComponent } from './components/config/config-content/config-content.component';
import { SecurityService } from './services/security.service';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: ':site', component: BodyComponent },
  { path: 'dashboard', component: BodyComponent },
  {
    path: 'config', component: ConfigContentComponent, canActivate: [SecurityService],
    data: { roles: ['factorynews_admin'] }
  },
  {
    path: 'config', component: ConfigContentComponent, canActivate: [SecurityService],
    data: { roles: ['factorynews_user'] }
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }

