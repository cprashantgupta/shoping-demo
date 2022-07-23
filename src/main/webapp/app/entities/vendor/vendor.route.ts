import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVendor, Vendor } from 'app/shared/model/vendor.model';
import { VendorService } from './vendor.service';
import { VendorComponent } from './vendor.component';
import { VendorDetailComponent } from './vendor-detail.component';
import { VendorUpdateComponent } from './vendor-update.component';

@Injectable({ providedIn: 'root' })
export class VendorResolve implements Resolve<IVendor> {
  constructor(private service: VendorService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVendor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vendor: HttpResponse<Vendor>) => {
          if (vendor.body) {
            return of(vendor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vendor());
  }
}

export const vendorRoute: Routes = [
  {
    path: '',
    component: VendorComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'vcartApp.vendor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VendorDetailComponent,
    resolve: {
      vendor: VendorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'vcartApp.vendor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VendorUpdateComponent,
    resolve: {
      vendor: VendorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'vcartApp.vendor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VendorUpdateComponent,
    resolve: {
      vendor: VendorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'vcartApp.vendor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
