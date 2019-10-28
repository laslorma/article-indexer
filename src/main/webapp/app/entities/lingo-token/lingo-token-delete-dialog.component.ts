import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILingoToken } from 'app/shared/model/lingo-token.model';
import { LingoTokenService } from './lingo-token.service';

@Component({
  selector: 'jhi-lingo-token-delete-dialog',
  templateUrl: './lingo-token-delete-dialog.component.html'
})
export class LingoTokenDeleteDialogComponent {
  lingoToken: ILingoToken;

  constructor(
    protected lingoTokenService: LingoTokenService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.lingoTokenService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'lingoTokenListModification',
        content: 'Deleted an lingoToken'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-lingo-token-delete-popup',
  template: ''
})
export class LingoTokenDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ lingoToken }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LingoTokenDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.lingoToken = lingoToken;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/lingo-token', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/lingo-token', { outlets: { popup: null } }]);
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
