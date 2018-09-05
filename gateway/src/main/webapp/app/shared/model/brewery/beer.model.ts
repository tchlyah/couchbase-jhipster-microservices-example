import { Moment } from 'moment';

export interface IBeer {
    id?: string;
    name?: string;
    category?: string;
    description?: string;
    style?: string;
    brewery?: string;
    abv?: number;
    ibu?: number;
    srm?: number;
    upc?: number;
    updated?: Moment;
}

export class Beer implements IBeer {
    constructor(
        public id?: string,
        public name?: string,
        public category?: string,
        public description?: string,
        public style?: string,
        public brewery?: string,
        public abv?: number,
        public ibu?: number,
        public srm?: number,
        public upc?: number,
        public updated?: Moment
    ) {}
}
