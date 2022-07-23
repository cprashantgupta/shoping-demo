import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VendorComponentsPage, VendorDeleteDialog, VendorUpdatePage } from './vendor.page-object';

const expect = chai.expect;

describe('Vendor e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let vendorComponentsPage: VendorComponentsPage;
  let vendorUpdatePage: VendorUpdatePage;
  let vendorDeleteDialog: VendorDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Vendors', async () => {
    await navBarPage.goToEntity('vendor');
    vendorComponentsPage = new VendorComponentsPage();
    await browser.wait(ec.visibilityOf(vendorComponentsPage.title), 5000);
    expect(await vendorComponentsPage.getTitle()).to.eq('vcartApp.vendor.home.title');
    await browser.wait(ec.or(ec.visibilityOf(vendorComponentsPage.entities), ec.visibilityOf(vendorComponentsPage.noResult)), 1000);
  });

  it('should load create Vendor page', async () => {
    await vendorComponentsPage.clickOnCreateButton();
    vendorUpdatePage = new VendorUpdatePage();
    expect(await vendorUpdatePage.getPageTitle()).to.eq('vcartApp.vendor.home.createOrEditLabel');
    await vendorUpdatePage.cancel();
  });

  it('should create and save Vendors', async () => {
    const nbButtonsBeforeCreate = await vendorComponentsPage.countDeleteButtons();

    await vendorComponentsPage.clickOnCreateButton();

    await promise.all([
      vendorUpdatePage.setNameInput('name'),
      vendorUpdatePage.setPhoneInput('5'),
      vendorUpdatePage.setEmailIdInput('emailId'),
      vendorUpdatePage.setAddressInput('address'),
      vendorUpdatePage.setGstNumberInput('5'),
    ]);

    expect(await vendorUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await vendorUpdatePage.getPhoneInput()).to.eq('5', 'Expected phone value to be equals to 5');
    expect(await vendorUpdatePage.getEmailIdInput()).to.eq('emailId', 'Expected EmailId value to be equals to emailId');
    expect(await vendorUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');
    expect(await vendorUpdatePage.getGstNumberInput()).to.eq('5', 'Expected gstNumber value to be equals to 5');

    await vendorUpdatePage.save();
    expect(await vendorUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await vendorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Vendor', async () => {
    const nbButtonsBeforeDelete = await vendorComponentsPage.countDeleteButtons();
    await vendorComponentsPage.clickOnLastDeleteButton();

    vendorDeleteDialog = new VendorDeleteDialog();
    expect(await vendorDeleteDialog.getDialogTitle()).to.eq('vcartApp.vendor.delete.question');
    await vendorDeleteDialog.clickOnConfirmButton();

    expect(await vendorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
