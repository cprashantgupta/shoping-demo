import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { VcartSharedModule } from 'app/shared/shared.module';
import { PurchaserComponent } from './purchaser.component';
import { PurchaserDetailComponent } from './purchaser-detail.component';
import { PurchaserUpdateComponent } from './purchaser-update.component';
import { PurchaserDeleteDialogComponent } from './purchaser-delete-dialog.component';
import { purchaserRoute } from './purchaser.route';

@NgModule({
  imports: [VcartSharedModule, RouterModule.forChild(purchaserRoute)],
  declarations: [PurchaserComponent, PurchaserDetailComponent, PurchaserUpdateComponent, PurchaserDeleteDialogComponent],
  entryComponents: [PurchaserDeleteDialogComponent],
})
export class VcartPurchaserModule {}
