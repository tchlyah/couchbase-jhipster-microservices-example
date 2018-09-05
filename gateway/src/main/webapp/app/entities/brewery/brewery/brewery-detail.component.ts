import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBrewery } from 'app/shared/model/brewery/brewery.model';

@Component({
    selector: 'jhi-brewery-detail',
    templateUrl: './brewery-detail.component.html'
})
export class BreweryDetailComponent implements OnInit {
    brewery: IBrewery;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brewery }) => {
            this.brewery = brewery;
        });
    }

    previousState() {
        window.history.back();
    }
}
