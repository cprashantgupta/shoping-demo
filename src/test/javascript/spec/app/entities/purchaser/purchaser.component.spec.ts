import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { VcartTestModule } from '../../../test.module';
import { PurchaserComponent } from 'app/entities/purchaser/purchaser.component';
import { PurchaserService } from 'app/entities/purchaser/purchaser.service';
import { Purchaser } from 'app/shared/model/purchaser.model';

describe('Component Tests', () => {
  describe('Purchaser Management Component', () => {
    let comp: PurchaserComponent;
    let fixture: ComponentFixture<PurchaserComponent>;
    let service: PurchaserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VcartTestModule],
        declarations: [PurchaserComponent],
      })
        .overrideTemplate(PurchaserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Purchaser(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.purchasers && comp.purchasers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
