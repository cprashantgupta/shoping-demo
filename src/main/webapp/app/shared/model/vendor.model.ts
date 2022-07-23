import { IProduct } from 'app/shared/model/product.model';

export interface IVendor {
  id?: number;
  name?: string;
  phone?: number;
  emailId?: string;
  address?: string;
  gstNumber?: number;
  products?: IProduct[];
}

export class Vendor implements IVendor {
  constructor(
    public id?: number,
    public name?: string,
    public phone?: number,
    public emailId?: string,
    public address?: string,
    public gstNumber?: number,
    public products?: IProduct[]
  ) {}
}
