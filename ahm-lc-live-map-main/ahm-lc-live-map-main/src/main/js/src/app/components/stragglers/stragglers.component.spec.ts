import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StragglersComponent } from './stragglers.component';

describe('StragglersComponent', () => {
    let component: StragglersComponent;
    let fixture: ComponentFixture<StragglersComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [StragglersComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(StragglersComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
