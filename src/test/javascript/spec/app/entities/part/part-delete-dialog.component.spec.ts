import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IndexerTestModule } from '../../../test.module';
import { PartDeleteDialogComponent } from 'app/entities/part/part-delete-dialog.component';
import { PartService } from 'app/entities/part/part.service';

describe('Component Tests', () => {
  describe('Part Management Delete Component', () => {
    let comp: PartDeleteDialogComponent;
    let fixture: ComponentFixture<PartDeleteDialogComponent>;
    let service: PartService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [PartDeleteDialogComponent]
      })
        .overrideTemplate(PartDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PartDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PartService);
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
