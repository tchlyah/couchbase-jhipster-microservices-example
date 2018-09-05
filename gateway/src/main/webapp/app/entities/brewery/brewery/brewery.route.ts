import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Brewery } from 'app/shared/model/brewery/brewery.model';
import { BreweryService } from './brewery.service';
import { BreweryComponent } from './brewery.component';
import { BreweryDetailComponent } from './brewery-detail.component';
import { BreweryUpdateComponent } from './brewery-update.component';
import { BreweryDeletePopupComponent } from './brewery-delete-dialog.component';
import { IBrewery } from 'app/shared/model/brewery/brewery.model';

@Injectable({ providedIn: 'root' })
export class BreweryResolve implements Resolve<IBrewery> {
    constructor(private service: BreweryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((brewery: HttpResponse<Brewery>) => brewery.body));
        }
        return of(new Brewery());
    }
}

export const breweryRoute: Routes = [
    {
        path: 'brewery',
        component: BreweryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBrewery.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brewery/:id/view',
        component: BreweryDetailComponent,
        resolve: {
            brewery: BreweryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBrewery.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brewery/new',
        component: BreweryUpdateComponent,
        resolve: {
            brewery: BreweryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBrewery.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brewery/:id/edit',
        component: BreweryUpdateComponent,
        resolve: {
            brewery: BreweryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBrewery.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const breweryPopupRoute: Routes = [
    {
        path: 'brewery/:id/delete',
        component: BreweryDeletePopupComponent,
        resolve: {
            brewery: BreweryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBrewery.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
