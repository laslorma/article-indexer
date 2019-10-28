import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { NewsApiCategoryUpdateComponent } from 'app/entities/news-api-category/news-api-category-update.component';
import { NewsApiCategoryService } from 'app/entities/news-api-category/news-api-category.service';
import { NewsApiCategory } from 'app/shared/model/news-api-category.model';

describe('Component Tests', () => {
  describe('NewsApiCategory Management Update Component', () => {
    let comp: NewsApiCategoryUpdateComponent;
    let fixture: ComponentFixture<NewsApiCategoryUpdateComponent>;
    let service: NewsApiCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NewsApiCategoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(NewsApiCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NewsApiCategoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NewsApiCategoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new NewsApiCategory(123);
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
        const entity = new NewsApiCategory();
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
