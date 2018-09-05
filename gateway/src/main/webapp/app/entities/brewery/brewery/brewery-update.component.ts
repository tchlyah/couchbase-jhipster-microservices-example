import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IBrewery } from 'app/shared/model/brewery/brewery.model';
import { BreweryService } from './brewery.service';

@Component({
    selector: 'jhi-brewery-update',
    templateUrl: './brewery-update.component.html'
})
export class BreweryUpdateComponent implements OnInit {
    private _brewery: IBrewery;
    isSaving: boolean;
    updatedDp: any;

    constructor(private breweryService: BreweryService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ brewery }) => {
            this.brewery = brewery;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.brewery.id !== undefined) {
            this.subscribeToSaveResponse(this.breweryService.update(this.brewery));
        } else {
            this.subscribeToSaveResponse(this.breweryService.create(this.brewery));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBrewery>>) {
        result.subscribe((res: HttpResponse<IBrewery>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get brewery() {
        return this._brewery;
    }

    set brewery(brewery: IBrewery) {
        this._brewery = brewery;
    }
}
