import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPurchaser, Purchaser } from 'app/shared/model/purchaser.model';
import { PurchaserService } from './purchaser.service';

@Component({
  selector: 'jhi-purchaser-update',
  templateUrl: './purchaser-update.component.html',
})
export class PurchaserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    emailId: [],
    address: [],
    gstNumber: [],
  });

  constructor(protected purchaserService: PurchaserService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaser }) => {
      this.updateForm(purchaser);
    });
  }

  updateForm(purchaser: IPurchaser): void {
    this.editForm.patchValue({
      id: purchaser.id,
      name: purchaser.name,
      phone: purchaser.phone,
      emailId: purchaser.emailId,
      address: purchaser.address,
      gstNumber: purchaser.gstNumber,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaser = this.createFromForm();
    if (purchaser.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaserService.update(purchaser));
    } else {
      this.subscribeToSaveResponse(this.purchaserService.create(purchaser));
    }
  }

  private createFromForm(): IPurchaser {
    return {
      ...new Purchaser(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      emailId: this.editForm.get(['emailId'])!.value,
      address: this.editForm.get(['address'])!.value,
      gstNumber: this.editForm.get(['gstNumber'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaser>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
