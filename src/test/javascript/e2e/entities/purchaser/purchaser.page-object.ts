import { element, by, ElementFinder } from 'protractor';

export class PurchaserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-purchaser div table .btn-danger'));
  title = element.all(by.css('jhi-purchaser div h2#page-heading span')).first();
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

export class PurchaserUpdatePage {
  pageTitle = element(by.id('jhi-purchaser-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  phoneInput = element(by.id('field_phone'));
  emailIdInput = element(by.id('field_emailId'));
  addressInput = element(by.id('field_address'));
  gstNumberInput = element(by.id('field_gstNumber'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setPhoneInput(phone: string): Promise<void> {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput(): Promise<string> {
    return await this.phoneInput.getAttribute('value');
  }

  async setEmailIdInput(emailId: string): Promise<void> {
    await this.emailIdInput.sendKeys(emailId);
  }

  async getEmailIdInput(): Promise<string> {
    return await this.emailIdInput.getAttribute('value');
  }

  async setAddressInput(address: string): Promise<void> {
    await this.addressInput.sendKeys(address);
  }

  async getAddressInput(): Promise<string> {
    return await this.addressInput.getAttribute('value');
  }

  async setGstNumberInput(gstNumber: string): Promise<void> {
    await this.gstNumberInput.sendKeys(gstNumber);
  }

  async getGstNumberInput(): Promise<string> {
    return await this.gstNumberInput.getAttribute('value');
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

export class PurchaserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-purchaser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-purchaser'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
