import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVendor } from 'app/shared/model/vendor.model';

type EntityResponseType = HttpResponse<IVendor>;
type EntityArrayResponseType = HttpResponse<IVendor[]>;

@Injectable({ providedIn: 'root' })
export class VendorService {
  public resourceUrl = SERVER_API_URL + 'api/vendors';

  constructor(protected http: HttpClient) {}

  create(vendor: IVendor): Observable<EntityResponseType> {
    return this.http.post<IVendor>(this.resourceUrl, vendor, { observe: 'response' });
  }

  update(vendor: IVendor): Observable<EntityResponseType> {
    return this.http.put<IVendor>(this.resourceUrl, vendor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVendor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVendor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
