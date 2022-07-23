import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VcartTestModule } from '../../../test.module';
import { VendorDetailComponent } from 'app/entities/vendor/vendor-detail.component';
import { Vendor } from 'app/shared/model/vendor.model';

describe('Component Tests', () => {
  describe('Vendor Management Detail Component', () => {
    let comp: VendorDetailComponent;
    let fixture: ComponentFixture<VendorDetailComponent>;
    const route = ({ data: of({ vendor: new Vendor(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VcartTestModule],
        declarations: [VendorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(VendorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VendorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vendor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vendor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
