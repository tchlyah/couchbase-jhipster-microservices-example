/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { BreweryDetailComponent } from 'app/entities/brewery/brewery/brewery-detail.component';
import { Brewery } from 'app/shared/model/brewery/brewery.model';

describe('Component Tests', () => {
    describe('Brewery Management Detail Component', () => {
        let comp: BreweryDetailComponent;
        let fixture: ComponentFixture<BreweryDetailComponent>;
        const route = ({ data: of({ brewery: new Brewery('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GatewayTestModule],
                declarations: [BreweryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BreweryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BreweryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.brewery).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
