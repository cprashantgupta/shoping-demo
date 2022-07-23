import { IVendor } from 'app/shared/model/vendor.model';

export interface IProduct {
  id?: number;
  productName?: string;
  vendors?: IVendor[];
  subCategoryId?: number;
}

export class Product implements IProduct {
  constructor(public id?: number, public productName?: string, public vendors?: IVendor[], public subCategoryId?: number) {}
}
