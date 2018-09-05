import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared';
import {
    BreweryComponent,
    BreweryDetailComponent,
    BreweryUpdateComponent,
    BreweryDeletePopupComponent,
    BreweryDeleteDialogComponent,
    breweryRoute,
    breweryPopupRoute
} from './';

const ENTITY_STATES = [...breweryRoute, ...breweryPopupRoute];

@NgModule({
    imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BreweryComponent,
        BreweryDetailComponent,
        BreweryUpdateComponent,
        BreweryDeleteDialogComponent,
        BreweryDeletePopupComponent
    ],
    entryComponents: [BreweryComponent, BreweryUpdateComponent, BreweryDeleteDialogComponent, BreweryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayBreweryModule {}
