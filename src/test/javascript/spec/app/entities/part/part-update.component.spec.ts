/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { PartUpdateComponent } from 'app/entities/part/part-update.component';
import { PartService } from 'app/entities/part/part.service';
import { Part } from 'app/shared/model/part.model';

describe('Component Tests', () => {
  describe('Part Management Update Component', () => {
    let comp: PartUpdateComponent;
    let fixture: ComponentFixture<PartUpdateComponent>;
    let service: PartService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [PartUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PartUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PartService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Part(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Part();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
