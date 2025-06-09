import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PartIssuesComponent } from './part-issues.component';

describe('PartIssuesComponent', () => {
    let component: PartIssuesComponent;
    let fixture: ComponentFixture<PartIssuesComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [PartIssuesComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(PartIssuesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
