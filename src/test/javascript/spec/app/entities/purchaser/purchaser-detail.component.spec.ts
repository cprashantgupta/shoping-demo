import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VcartTestModule } from '../../../test.module';
import { PurchaserDetailComponent } from 'app/entities/purchaser/purchaser-detail.component';
import { Purchaser } from 'app/shared/model/purchaser.model';

describe('Component Tests', () => {
  describe('Purchaser Management Detail Component', () => {
    let comp: PurchaserDetailComponent;
    let fixture: ComponentFixture<PurchaserDetailComponent>;
    const route = ({ data: of({ purchaser: new Purchaser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VcartTestModule],
        declarations: [PurchaserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PurchaserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load purchaser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.purchaser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
