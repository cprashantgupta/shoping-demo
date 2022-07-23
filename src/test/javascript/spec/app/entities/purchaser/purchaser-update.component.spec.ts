import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { VcartTestModule } from '../../../test.module';
import { PurchaserUpdateComponent } from 'app/entities/purchaser/purchaser-update.component';
import { PurchaserService } from 'app/entities/purchaser/purchaser.service';
import { Purchaser } from 'app/shared/model/purchaser.model';

describe('Component Tests', () => {
  describe('Purchaser Management Update Component', () => {
    let comp: PurchaserUpdateComponent;
    let fixture: ComponentFixture<PurchaserUpdateComponent>;
    let service: PurchaserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VcartTestModule],
        declarations: [PurchaserUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PurchaserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Purchaser(123);
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
        const entity = new Purchaser();
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
