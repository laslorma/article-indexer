/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IndexerTestModule } from '../../../test.module';
import { NlpServerConfDeleteDialogComponent } from 'app/entities/nlp-server-conf/nlp-server-conf-delete-dialog.component';
import { NlpServerConfService } from 'app/entities/nlp-server-conf/nlp-server-conf.service';

describe('Component Tests', () => {
  describe('NlpServerConf Management Delete Component', () => {
    let comp: NlpServerConfDeleteDialogComponent;
    let fixture: ComponentFixture<NlpServerConfDeleteDialogComponent>;
    let service: NlpServerConfService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NlpServerConfDeleteDialogComponent]
      })
        .overrideTemplate(NlpServerConfDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NlpServerConfDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NlpServerConfService);
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
