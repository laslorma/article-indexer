import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IndexerTestModule } from '../../../test.module';
import { NewsApiCategoryDeleteDialogComponent } from 'app/entities/news-api-category/news-api-category-delete-dialog.component';
import { NewsApiCategoryService } from 'app/entities/news-api-category/news-api-category.service';

describe('Component Tests', () => {
  describe('NewsApiCategory Management Delete Component', () => {
    let comp: NewsApiCategoryDeleteDialogComponent;
    let fixture: ComponentFixture<NewsApiCategoryDeleteDialogComponent>;
    let service: NewsApiCategoryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NewsApiCategoryDeleteDialogComponent]
      })
        .overrideTemplate(NewsApiCategoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NewsApiCategoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NewsApiCategoryService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
