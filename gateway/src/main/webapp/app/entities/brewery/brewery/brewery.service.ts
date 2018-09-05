import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBrewery } from 'app/shared/model/brewery/brewery.model';

type EntityResponseType = HttpResponse<IBrewery>;
type EntityArrayResponseType = HttpResponse<IBrewery[]>;

@Injectable({ providedIn: 'root' })
export class BreweryService {
    private resourceUrl = SERVER_API_URL + 'brewery/api/breweries';

    constructor(private http: HttpClient) {}

    create(brewery: IBrewery): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(brewery);
        return this.http
            .post<IBrewery>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(brewery: IBrewery): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(brewery);
        return this.http
            .put<IBrewery>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IBrewery>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBrewery[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(brewery: IBrewery): IBrewery {
        const copy: IBrewery = Object.assign({}, brewery, {
            updated: brewery.updated != null && brewery.updated.isValid() ? brewery.updated.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((brewery: IBrewery) => {
            brewery.updated = brewery.updated != null ? moment(brewery.updated) : null;
        });
        return res;
    }
}
