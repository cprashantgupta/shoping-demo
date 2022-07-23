export interface IPurchaser {
  id?: number;
  name?: string;
  phone?: number;
  emailId?: string;
  address?: string;
  gstNumber?: number;
}

export class Purchaser implements IPurchaser {
  constructor(
    public id?: number,
    public name?: string,
    public phone?: number,
    public emailId?: string,
    public address?: string,
    public gstNumber?: number
  ) {}
}
