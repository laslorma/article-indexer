/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { ParagraphUpdateComponent } from 'app/entities/paragraph/paragraph-update.component';
import { ParagraphService } from 'app/entities/paragraph/paragraph.service';
import { Paragraph } from 'app/shared/model/paragraph.model';

describe('Component Tests', () => {
  describe('Paragraph Management Update Component', () => {
    let comp: ParagraphUpdateComponent;
    let fixture: ComponentFixture<ParagraphUpdateComponent>;
    let service: ParagraphService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [ParagraphUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ParagraphUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParagraphUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParagraphService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Paragraph(123);
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
        const entity = new Paragraph();
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
