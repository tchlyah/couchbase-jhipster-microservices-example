import { Moment } from 'moment';

export interface IBrewery {
    id?: string;
    name?: string;
    description?: string;
    address?: string;
    city?: string;
    code?: string;
    country?: string;
    phone?: string;
    state?: string;
    website?: string;
    updated?: Moment;
}

export class Brewery implements IBrewery {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public address?: string,
        public city?: string,
        public code?: string,
        public country?: string,
        public phone?: string,
        public state?: string,
        public website?: string,
        public updated?: Moment
    ) {}
}
