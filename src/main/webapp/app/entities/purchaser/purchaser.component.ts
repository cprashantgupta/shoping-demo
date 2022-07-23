import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaser } from 'app/shared/model/purchaser.model';
import { PurchaserService } from './purchaser.service';
import { PurchaserDeleteDialogComponent } from './purchaser-delete-dialog.component';

@Component({
  selector: 'jhi-purchaser',
  templateUrl: './purchaser.component.html',
})
export class PurchaserComponent implements OnInit, OnDestroy {
  purchasers?: IPurchaser[];
  eventSubscriber?: Subscription;

  constructor(protected purchaserService: PurchaserService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.purchaserService.query().subscribe((res: HttpResponse<IPurchaser[]>) => (this.purchasers = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPurchasers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPurchaser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPurchasers(): void {
    this.eventSubscriber = this.eventManager.subscribe('purchaserListModification', () => this.loadAll());
  }

  delete(purchaser: IPurchaser): void {
    const modalRef = this.modalService.open(PurchaserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.purchaser = purchaser;
  }
}
