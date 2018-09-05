import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBeer } from 'app/shared/model/brewery/beer.model';

@Component({
    selector: 'jhi-beer-detail',
    templateUrl: './beer-detail.component.html'
})
export class BeerDetailComponent implements OnInit {
    beer: IBeer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ beer }) => {
            this.beer = beer;
        });
    }

    previousState() {
        window.history.back();
    }
}
