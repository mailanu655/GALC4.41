import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PartAssignmentComponent } from './components/part-assignment/part-assignment.component';
import { LotAssignmentComponent } from './components/lot-assignment/lot-assignment.component';

import { Resource } from './services/constants';
import { InterchangeableComponent } from './components/interchangeable/interchangeable.component';
import { ManualOverrideComponent } from './components/manual-override/manual-override.component';
import { ManualOverrideApprovalComponent } from './components/manual-override-approval/manual-override-approval.component';
import { CategoryMaintComponent } from './components/category-maint/category-maint.component';
import { RuleAssignmentComponent } from './components/rule-assignment/rule-assignment.component';
import { RuleLotApprovalComponent } from './components/rule-lot-approval/rule-lot-approval.component';
import { RuleLotMaintenanceComponent } from './components/rule-lot-maintenance/rule-lot-maintenance.component';

import { SecurityService } from './services/security.service';

export const homeUrl = "home";
export const homeRedirectUrl = "/#/home";

const routes: Routes = [
    { path: '', component: DashboardComponent, canActivate: [SecurityService], data: { roles: ['public'] }},
    { path: 'part-assignment', component: PartAssignmentComponent, canActivate: [SecurityService], data: { roles: ['cat_assign','system_assign'] }},
    { path: 'lot-assignment', component: LotAssignmentComponent, canActivate: [SecurityService], data: { roles: ['lot_assign','rule_assign'] }},
    { path: 'interchangeable', component: InterchangeableComponent, canActivate: [SecurityService], data: { roles: ['lot_assign','rule_assign'] }},
    { path: 'manualoverride', component: ManualOverrideComponent, canActivate: [SecurityService], data: { roles: ['override_approval','rule_assign','system_assign'] }},
    { path: 'manualoverrideapproval', component: ManualOverrideApprovalComponent, canActivate: [SecurityService], data: { roles: ['override_approval'] }},
    { path: 'ruleassignment', component: RuleAssignmentComponent, canActivate: [SecurityService], data: { roles: ['rule_assign'] }},
    { path: 'rulelotapproval', component: RuleLotApprovalComponent, canActivate: [SecurityService], data: { roles: ['override_approval'] }},
    { path: 'rulelotmaintenance', component: RuleLotMaintenanceComponent, canActivate: [SecurityService], data: { roles: ['lot_assign','rule_assign'] }},
    { path: 'category-maint', component: CategoryMaintComponent, canActivate: [SecurityService], data: { roles: ['cat_assign'] } },
    
]

export const AppRoutes = RouterModule.forRoot(routes, { useHash: true });
