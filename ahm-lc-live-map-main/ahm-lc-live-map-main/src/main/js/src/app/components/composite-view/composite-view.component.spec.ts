import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { CompositeViewComponent } from './composite-view.component';

describe('ComplexViewComponent', () => {
    let component: CompositeViewComponent;
    let fixture: ComponentFixture<CompositeViewComponent>;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [CompositeViewComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(CompositeViewComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
