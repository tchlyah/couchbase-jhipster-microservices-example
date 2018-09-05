import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared';
import {
    BeerComponent,
    BeerDetailComponent,
    BeerUpdateComponent,
    BeerDeletePopupComponent,
    BeerDeleteDialogComponent,
    beerRoute,
    beerPopupRoute
} from './';

const ENTITY_STATES = [...beerRoute, ...beerPopupRoute];

@NgModule({
    imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [BeerComponent, BeerDetailComponent, BeerUpdateComponent, BeerDeleteDialogComponent, BeerDeletePopupComponent],
    entryComponents: [BeerComponent, BeerUpdateComponent, BeerDeleteDialogComponent, BeerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayBeerModule {}
