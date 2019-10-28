/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { IndexSessionUpdateComponent } from 'app/entities/index-session/index-session-update.component';
import { IndexSessionService } from 'app/entities/index-session/index-session.service';
import { IndexSession } from 'app/shared/model/index-session.model';

describe('Component Tests', () => {
  describe('IndexSession Management Update Component', () => {
    let comp: IndexSessionUpdateComponent;
    let fixture: ComponentFixture<IndexSessionUpdateComponent>;
    let service: IndexSessionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [IndexSessionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IndexSessionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IndexSessionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IndexSessionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IndexSession(123);
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
        const entity = new IndexSession();
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
