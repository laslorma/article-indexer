/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IndexerTestModule } from '../../../test.module';
import { IndexConfigurationDeleteDialogComponent } from 'app/entities/index-configuration/index-configuration-delete-dialog.component';
import { IndexConfigurationService } from 'app/entities/index-configuration/index-configuration.service';

describe('Component Tests', () => {
  describe('IndexConfiguration Management Delete Component', () => {
    let comp: IndexConfigurationDeleteDialogComponent;
    let fixture: ComponentFixture<IndexConfigurationDeleteDialogComponent>;
    let service: IndexConfigurationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [IndexConfigurationDeleteDialogComponent]
      })
        .overrideTemplate(IndexConfigurationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndexConfigurationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IndexConfigurationService);
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
