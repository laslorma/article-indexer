import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { NewsApiCategoryDetailComponent } from 'app/entities/news-api-category/news-api-category-detail.component';
import { NewsApiCategory } from 'app/shared/model/news-api-category.model';

describe('Component Tests', () => {
  describe('NewsApiCategory Management Detail Component', () => {
    let comp: NewsApiCategoryDetailComponent;
    let fixture: ComponentFixture<NewsApiCategoryDetailComponent>;
    const route = ({ data: of({ newsApiCategory: new NewsApiCategory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NewsApiCategoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(NewsApiCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NewsApiCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.newsApiCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
