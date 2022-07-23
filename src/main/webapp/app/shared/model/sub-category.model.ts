import { IProduct } from 'app/shared/model/product.model';

export interface ISubCategory {
  id?: number;
  subCategoryName?: string;
  products?: IProduct[];
  categoryId?: number;
}

export class SubCategory implements ISubCategory {
  constructor(public id?: number, public subCategoryName?: string, public products?: IProduct[], public categoryId?: number) {}
}
