import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LcTableComponent } from './lc-table.component';

describe('LcTableComponent', () => {
    let component: LcTableComponent<any, any, any>;
    let fixture: ComponentFixture<LcTableComponent<any, any, any>>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [LcTableComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(LcTableComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
