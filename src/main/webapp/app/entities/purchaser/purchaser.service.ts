import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPurchaser } from 'app/shared/model/purchaser.model';

type EntityResponseType = HttpResponse<IPurchaser>;
type EntityArrayResponseType = HttpResponse<IPurchaser[]>;

@Injectable({ providedIn: 'root' })
export class PurchaserService {
  public resourceUrl = SERVER_API_URL + 'api/purchasers';

  constructor(protected http: HttpClient) {}

  create(purchaser: IPurchaser): Observable<EntityResponseType> {
    return this.http.post<IPurchaser>(this.resourceUrl, purchaser, { observe: 'response' });
  }

  update(purchaser: IPurchaser): Observable<EntityResponseType> {
    return this.http.put<IPurchaser>(this.resourceUrl, purchaser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPurchaser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPurchaser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
