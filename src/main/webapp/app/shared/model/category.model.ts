import { ISubCategory } from 'app/shared/model/sub-category.model';

export interface ICategory {
  id?: number;
  categoryName?: string;
  subCategories?: ISubCategory[];
}

export class Category implements ICategory {
  constructor(public id?: number, public categoryName?: string, public subCategories?: ISubCategory[]) {}
}
