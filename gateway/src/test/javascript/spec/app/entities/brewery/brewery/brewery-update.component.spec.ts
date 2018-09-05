/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { BreweryUpdateComponent } from 'app/entities/brewery/brewery/brewery-update.component';
import { BreweryService } from 'app/entities/brewery/brewery/brewery.service';
import { Brewery } from 'app/shared/model/brewery/brewery.model';

describe('Component Tests', () => {
    describe('Brewery Management Update Component', () => {
        let comp: BreweryUpdateComponent;
        let fixture: ComponentFixture<BreweryUpdateComponent>;
        let service: BreweryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GatewayTestModule],
                declarations: [BreweryUpdateComponent]
            })
                .overrideTemplate(BreweryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BreweryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BreweryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Brewery('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.brewery = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Brewery();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.brewery = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
