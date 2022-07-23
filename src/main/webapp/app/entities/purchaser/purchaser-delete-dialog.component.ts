import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaser } from 'app/shared/model/purchaser.model';
import { PurchaserService } from './purchaser.service';

@Component({
  templateUrl: './purchaser-delete-dialog.component.html',
})
export class PurchaserDeleteDialogComponent {
  purchaser?: IPurchaser;

  constructor(protected purchaserService: PurchaserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.purchaserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('purchaserListModification');
      this.activeModal.close();
    });
  }
}
