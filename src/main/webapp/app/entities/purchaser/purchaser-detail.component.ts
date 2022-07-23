import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaser } from 'app/shared/model/purchaser.model';

@Component({
  selector: 'jhi-purchaser-detail',
  templateUrl: './purchaser-detail.component.html',
})
export class PurchaserDetailComponent implements OnInit {
  purchaser: IPurchaser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaser }) => (this.purchaser = purchaser));
  }

  previousState(): void {
    window.history.back();
  }
}
