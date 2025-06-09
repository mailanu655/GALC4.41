import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { SecurityService } from './services/security.service';
import { ScheduleRepTbxComponent } from './components/schedule-rep-tbx/schedule-rep-tbx.component';
import { WeldScheduleComponent } from './containers';

export const homeUrl = "home";
export const homeRedirectUrl = "/#/home";

const routes: Routes = [
    { path: '', component: DashboardComponent, canActivate: [SecurityService]},
    { path: 'home', component: DashboardComponent, canActivate: [SecurityService]},
    { path: 'schedule-rep-tbx', component: ScheduleRepTbxComponent, canActivate: [SecurityService], data: { roles: ['schedule_replication_admin'] }},
    { path: 'containers/weld-schedule', component: WeldScheduleComponent, canActivate: [SecurityService], data: { roles: ['lot_sequence_admin', 'lot_sequence_sendtoweon'] }}
    
]

export const AppRoutes = RouterModule.forRoot(routes, { useHash: true });
