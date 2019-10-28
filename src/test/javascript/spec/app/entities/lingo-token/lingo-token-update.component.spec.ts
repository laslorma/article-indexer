import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { LingoTokenUpdateComponent } from 'app/entities/lingo-token/lingo-token-update.component';
import { LingoTokenService } from 'app/entities/lingo-token/lingo-token.service';
import { LingoToken } from 'app/shared/model/lingo-token.model';

describe('Component Tests', () => {
  describe('LingoToken Management Update Component', () => {
    let comp: LingoTokenUpdateComponent;
    let fixture: ComponentFixture<LingoTokenUpdateComponent>;
    let service: LingoTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [LingoTokenUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LingoTokenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LingoTokenUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LingoTokenService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LingoToken(123);
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
        const entity = new LingoToken();
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
