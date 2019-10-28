import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { IndexConfigurationUpdateComponent } from 'app/entities/index-configuration/index-configuration-update.component';
import { IndexConfigurationService } from 'app/entities/index-configuration/index-configuration.service';
import { IndexConfiguration } from 'app/shared/model/index-configuration.model';

describe('Component Tests', () => {
  describe('IndexConfiguration Management Update Component', () => {
    let comp: IndexConfigurationUpdateComponent;
    let fixture: ComponentFixture<IndexConfigurationUpdateComponent>;
    let service: IndexConfigurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [IndexConfigurationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IndexConfigurationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IndexConfigurationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IndexConfigurationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IndexConfiguration(123);
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
        const entity = new IndexConfiguration();
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
