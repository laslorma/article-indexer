/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IndexerTestModule } from '../../../test.module';
import { ParagraphDeleteDialogComponent } from 'app/entities/paragraph/paragraph-delete-dialog.component';
import { ParagraphService } from 'app/entities/paragraph/paragraph.service';

describe('Component Tests', () => {
  describe('Paragraph Management Delete Component', () => {
    let comp: ParagraphDeleteDialogComponent;
    let fixture: ComponentFixture<ParagraphDeleteDialogComponent>;
    let service: ParagraphService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [ParagraphDeleteDialogComponent]
      })
        .overrideTemplate(ParagraphDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParagraphDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParagraphService);
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
