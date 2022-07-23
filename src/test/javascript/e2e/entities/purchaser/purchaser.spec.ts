import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PurchaserComponentsPage, PurchaserDeleteDialog, PurchaserUpdatePage } from './purchaser.page-object';

const expect = chai.expect;

describe('Purchaser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let purchaserComponentsPage: PurchaserComponentsPage;
  let purchaserUpdatePage: PurchaserUpdatePage;
  let purchaserDeleteDialog: PurchaserDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Purchasers', async () => {
    await navBarPage.goToEntity('purchaser');
    purchaserComponentsPage = new PurchaserComponentsPage();
    await browser.wait(ec.visibilityOf(purchaserComponentsPage.title), 5000);
    expect(await purchaserComponentsPage.getTitle()).to.eq('vcartApp.purchaser.home.title');
    await browser.wait(ec.or(ec.visibilityOf(purchaserComponentsPage.entities), ec.visibilityOf(purchaserComponentsPage.noResult)), 1000);
  });

  it('should load create Purchaser page', async () => {
    await purchaserComponentsPage.clickOnCreateButton();
    purchaserUpdatePage = new PurchaserUpdatePage();
    expect(await purchaserUpdatePage.getPageTitle()).to.eq('vcartApp.purchaser.home.createOrEditLabel');
    await purchaserUpdatePage.cancel();
  });

  it('should create and save Purchasers', async () => {
    const nbButtonsBeforeCreate = await purchaserComponentsPage.countDeleteButtons();

    await purchaserComponentsPage.clickOnCreateButton();

    await promise.all([
      purchaserUpdatePage.setNameInput('name'),
      purchaserUpdatePage.setPhoneInput('5'),
      purchaserUpdatePage.setEmailIdInput('emailId'),
      purchaserUpdatePage.setAddressInput('address'),
      purchaserUpdatePage.setGstNumberInput('5'),
    ]);

    expect(await purchaserUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await purchaserUpdatePage.getPhoneInput()).to.eq('5', 'Expected phone value to be equals to 5');
    expect(await purchaserUpdatePage.getEmailIdInput()).to.eq('emailId', 'Expected EmailId value to be equals to emailId');
    expect(await purchaserUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');
    expect(await purchaserUpdatePage.getGstNumberInput()).to.eq('5', 'Expected gstNumber value to be equals to 5');

    await purchaserUpdatePage.save();
    expect(await purchaserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await purchaserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Purchaser', async () => {
    const nbButtonsBeforeDelete = await purchaserComponentsPage.countDeleteButtons();
    await purchaserComponentsPage.clickOnLastDeleteButton();

    purchaserDeleteDialog = new PurchaserDeleteDialog();
    expect(await purchaserDeleteDialog.getDialogTitle()).to.eq('vcartApp.purchaser.delete.question');
    await purchaserDeleteDialog.clickOnConfirmButton();

    expect(await purchaserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
