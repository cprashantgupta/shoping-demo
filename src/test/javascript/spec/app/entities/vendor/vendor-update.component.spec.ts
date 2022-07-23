import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { VcartTestModule } from '../../../test.module';
import { VendorUpdateComponent } from 'app/entities/vendor/vendor-update.component';
import { VendorService } from 'app/entities/vendor/vendor.service';
import { Vendor } from 'app/shared/model/vendor.model';

describe('Component Tests', () => {
  describe('Vendor Management Update Component', () => {
    let comp: VendorUpdateComponent;
    let fixture: ComponentFixture<VendorUpdateComponent>;
    let service: VendorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VcartTestModule],
        declarations: [VendorUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(VendorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VendorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VendorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vendor(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vendor();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
