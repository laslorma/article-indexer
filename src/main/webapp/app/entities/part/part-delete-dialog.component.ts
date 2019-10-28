import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPart } from 'app/shared/model/part.model';
import { PartService } from './part.service';

@Component({
  selector: 'jhi-part-delete-dialog',
  templateUrl: './part-delete-dialog.component.html'
})
export class PartDeleteDialogComponent {
  part: IPart;

  constructor(protected partService: PartService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.partService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'partListModification',
        content: 'Deleted an part'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-part-delete-popup',
  template: ''
})
export class PartDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ part }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PartDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.part = part;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/part', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/part', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
