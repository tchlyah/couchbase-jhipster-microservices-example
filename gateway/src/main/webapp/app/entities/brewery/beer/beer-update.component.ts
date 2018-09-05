import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IBeer } from 'app/shared/model/brewery/beer.model';
import { BeerService } from './beer.service';

@Component({
    selector: 'jhi-beer-update',
    templateUrl: './beer-update.component.html'
})
export class BeerUpdateComponent implements OnInit {
    private _beer: IBeer;
    isSaving: boolean;
    updatedDp: any;

    constructor(private beerService: BeerService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ beer }) => {
            this.beer = beer;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.beer.id !== undefined) {
            this.subscribeToSaveResponse(this.beerService.update(this.beer));
        } else {
            this.subscribeToSaveResponse(this.beerService.create(this.beer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBeer>>) {
        result.subscribe((res: HttpResponse<IBeer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get beer() {
        return this._beer;
    }

    set beer(beer: IBeer) {
        this._beer = beer;
    }
}
