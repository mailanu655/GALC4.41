import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpcomingLotsComponent } from './upcoming-lots.component';

describe('UpcomingLotsComponent', () => {
    let component: UpcomingLotsComponent;
    let fixture: ComponentFixture<UpcomingLotsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [UpcomingLotsComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(UpcomingLotsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
