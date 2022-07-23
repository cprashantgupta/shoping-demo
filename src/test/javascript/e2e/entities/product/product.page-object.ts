import { element, by, ElementFinder } from 'protractor';

export class ProductComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-product div table .btn-danger'));
  title = element.all(by.css('jhi-product div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ProductUpdatePage {
  pageTitle = element(by.id('jhi-product-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  productNameInput = element(by.id('field_productName'));

  vendorSelect = element(by.id('field_vendor'));
  subCategorySelect = element(by.id('field_subCategory'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setProductNameInput(productName: string): Promise<void> {
    await this.productNameInput.sendKeys(productName);
  }

  async getProductNameInput(): Promise<string> {
    return await this.productNameInput.getAttribute('value');
  }

  async vendorSelectLastOption(): Promise<void> {
    await this.vendorSelect.all(by.tagName('option')).last().click();
  }

  async vendorSelectOption(option: string): Promise<void> {
    await this.vendorSelect.sendKeys(option);
  }

  getVendorSelect(): ElementFinder {
    return this.vendorSelect;
  }

  async getVendorSelectedOption(): Promise<string> {
    return await this.vendorSelect.element(by.css('option:checked')).getText();
  }

  async subCategorySelectLastOption(): Promise<void> {
    await this.subCategorySelect.all(by.tagName('option')).last().click();
  }

  async subCategorySelectOption(option: string): Promise<void> {
    await this.subCategorySelect.sendKeys(option);
  }

  getSubCategorySelect(): ElementFinder {
    return this.subCategorySelect;
  }

  async getSubCategorySelectedOption(): Promise<string> {
    return await this.subCategorySelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ProductDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-product-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-product'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
