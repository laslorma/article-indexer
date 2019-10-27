/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IndexerTestModule } from '../../../test.module';
import { IndexSessionDeleteDialogComponent } from 'app/entities/index-session/index-session-delete-dialog.component';
import { IndexSessionService } from 'app/entities/index-session/index-session.service';

describe('Component Tests', () => {
  describe('IndexSession Management Delete Component', () => {
    let comp: IndexSessionDeleteDialogComponent;
    let fixture: ComponentFixture<IndexSessionDeleteDialogComponent>;
    let service: IndexSessionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [IndexSessionDeleteDialogComponent]
      })
        .overrideTemplate(IndexSessionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndexSessionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IndexSessionService);
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
