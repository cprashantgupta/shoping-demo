import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { VcartSharedModule } from 'app/shared/shared.module';
import { VcartCoreModule } from 'app/core/core.module';
import { VcartAppRoutingModule } from './app-routing.module';
import { VcartHomeModule } from './home/home.module';
import { VcartEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    VcartSharedModule,
    VcartCoreModule,
    VcartHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    VcartEntityModule,
    VcartAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class VcartAppModule {}
