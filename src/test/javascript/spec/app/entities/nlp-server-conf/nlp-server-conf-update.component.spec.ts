import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { NlpServerConfUpdateComponent } from 'app/entities/nlp-server-conf/nlp-server-conf-update.component';
import { NlpServerConfService } from 'app/entities/nlp-server-conf/nlp-server-conf.service';
import { NlpServerConf } from 'app/shared/model/nlp-server-conf.model';

describe('Component Tests', () => {
  describe('NlpServerConf Management Update Component', () => {
    let comp: NlpServerConfUpdateComponent;
    let fixture: ComponentFixture<NlpServerConfUpdateComponent>;
    let service: NlpServerConfService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NlpServerConfUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(NlpServerConfUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NlpServerConfUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NlpServerConfService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new NlpServerConf(123);
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
        const entity = new NlpServerConf();
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
