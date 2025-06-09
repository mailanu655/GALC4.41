import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PMQAIssuesComponent } from './pmqa-issues.component';

describe('PMQAIssuesComponent', () => {
    let component: PMQAIssuesComponent;
    let fixture: ComponentFixture<PMQAIssuesComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [PMQAIssuesComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(PMQAIssuesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
