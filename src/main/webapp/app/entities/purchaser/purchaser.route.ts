import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPurchaser, Purchaser } from 'app/shared/model/purchaser.model';
import { PurchaserService } from './purchaser.service';
import { PurchaserComponent } from './purchaser.component';
import { PurchaserDetailComponent } from './purchaser-detail.component';
import { PurchaserUpdateComponent } from './purchaser-update.component';

@Injectable({ providedIn: 'root' })
export class PurchaserResolve implements Resolve<IPurchaser> {
  constructor(private service: PurchaserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchaser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((purchaser: HttpResponse<Purchaser>) => {
          if (purchaser.body) {
            return of(purchaser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Purchaser());
  }
}

export const purchaserRoute: Routes = [
  {
    path: '',
    component: PurchaserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'vcartApp.purchaser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PurchaserDetailComponent,
    resolve: {
      purchaser: PurchaserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'vcartApp.purchaser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PurchaserUpdateComponent,
    resolve: {
      purchaser: PurchaserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'vcartApp.purchaser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PurchaserUpdateComponent,
    resolve: {
      purchaser: PurchaserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'vcartApp.purchaser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
