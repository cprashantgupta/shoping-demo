import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SubCategoryComponentsPage, SubCategoryDeleteDialog, SubCategoryUpdatePage } from './sub-category.page-object';

const expect = chai.expect;

describe('SubCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let subCategoryComponentsPage: SubCategoryComponentsPage;
  let subCategoryUpdatePage: SubCategoryUpdatePage;
  let subCategoryDeleteDialog: SubCategoryDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SubCategories', async () => {
    await navBarPage.goToEntity('sub-category');
    subCategoryComponentsPage = new SubCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(subCategoryComponentsPage.title), 5000);
    expect(await subCategoryComponentsPage.getTitle()).to.eq('vcartApp.subCategory.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(subCategoryComponentsPage.entities), ec.visibilityOf(subCategoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SubCategory page', async () => {
    await subCategoryComponentsPage.clickOnCreateButton();
    subCategoryUpdatePage = new SubCategoryUpdatePage();
    expect(await subCategoryUpdatePage.getPageTitle()).to.eq('vcartApp.subCategory.home.createOrEditLabel');
    await subCategoryUpdatePage.cancel();
  });

  it('should create and save SubCategories', async () => {
    const nbButtonsBeforeCreate = await subCategoryComponentsPage.countDeleteButtons();

    await subCategoryComponentsPage.clickOnCreateButton();

    await promise.all([subCategoryUpdatePage.setSubCategoryNameInput('subCategoryName'), subCategoryUpdatePage.categorySelectLastOption()]);

    expect(await subCategoryUpdatePage.getSubCategoryNameInput()).to.eq(
      'subCategoryName',
      'Expected SubCategoryName value to be equals to subCategoryName'
    );

    await subCategoryUpdatePage.save();
    expect(await subCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await subCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SubCategory', async () => {
    const nbButtonsBeforeDelete = await subCategoryComponentsPage.countDeleteButtons();
    await subCategoryComponentsPage.clickOnLastDeleteButton();

    subCategoryDeleteDialog = new SubCategoryDeleteDialog();
    expect(await subCategoryDeleteDialog.getDialogTitle()).to.eq('vcartApp.subCategory.delete.question');
    await subCategoryDeleteDialog.clickOnConfirmButton();

    expect(await subCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
