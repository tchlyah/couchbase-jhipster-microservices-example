import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Beer } from 'app/shared/model/brewery/beer.model';
import { BeerService } from './beer.service';
import { BeerComponent } from './beer.component';
import { BeerDetailComponent } from './beer-detail.component';
import { BeerUpdateComponent } from './beer-update.component';
import { BeerDeletePopupComponent } from './beer-delete-dialog.component';
import { IBeer } from 'app/shared/model/brewery/beer.model';

@Injectable({ providedIn: 'root' })
export class BeerResolve implements Resolve<IBeer> {
    constructor(private service: BeerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((beer: HttpResponse<Beer>) => beer.body));
        }
        return of(new Beer());
    }
}

export const beerRoute: Routes = [
    {
        path: 'beer',
        component: BeerComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'gatewayApp.breweryBeer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'beer/:id/view',
        component: BeerDetailComponent,
        resolve: {
            beer: BeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBeer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'beer/new',
        component: BeerUpdateComponent,
        resolve: {
            beer: BeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBeer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'beer/:id/edit',
        component: BeerUpdateComponent,
        resolve: {
            beer: BeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBeer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const beerPopupRoute: Routes = [
    {
        path: 'beer/:id/delete',
        component: BeerDeletePopupComponent,
        resolve: {
            beer: BeerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.breweryBeer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
