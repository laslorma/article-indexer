import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IndexerTestModule } from '../../../test.module';
import { LingoTokenDeleteDialogComponent } from 'app/entities/lingo-token/lingo-token-delete-dialog.component';
import { LingoTokenService } from 'app/entities/lingo-token/lingo-token.service';

describe('Component Tests', () => {
  describe('LingoToken Management Delete Component', () => {
    let comp: LingoTokenDeleteDialogComponent;
    let fixture: ComponentFixture<LingoTokenDeleteDialogComponent>;
    let service: LingoTokenService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [LingoTokenDeleteDialogComponent]
      })
        .overrideTemplate(LingoTokenDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LingoTokenDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LingoTokenService);
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
