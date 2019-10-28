import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIndexSession } from 'app/shared/model/index-session.model';
import { IndexSessionService } from './index-session.service';

@Component({
  selector: 'jhi-index-session-delete-dialog',
  templateUrl: './index-session-delete-dialog.component.html'
})
export class IndexSessionDeleteDialogComponent {
  indexSession: IIndexSession;

  constructor(
    protected indexSessionService: IndexSessionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.indexSessionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'indexSessionListModification',
        content: 'Deleted an indexSession'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-index-session-delete-popup',
  template: ''
})
export class IndexSessionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ indexSession }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IndexSessionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.indexSession = indexSession;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/index-session', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/index-session', { outlets: { popup: null } }]);
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
