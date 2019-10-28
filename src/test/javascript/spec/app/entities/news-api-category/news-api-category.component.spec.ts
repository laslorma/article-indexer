import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IndexerTestModule } from '../../../test.module';
import { NewsApiCategoryComponent } from 'app/entities/news-api-category/news-api-category.component';
import { NewsApiCategoryService } from 'app/entities/news-api-category/news-api-category.service';
import { NewsApiCategory } from 'app/shared/model/news-api-category.model';

describe('Component Tests', () => {
  describe('NewsApiCategory Management Component', () => {
    let comp: NewsApiCategoryComponent;
    let fixture: ComponentFixture<NewsApiCategoryComponent>;
    let service: NewsApiCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NewsApiCategoryComponent],
        providers: []
      })
        .overrideTemplate(NewsApiCategoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NewsApiCategoryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NewsApiCategoryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new NewsApiCategory(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.newsApiCategories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
