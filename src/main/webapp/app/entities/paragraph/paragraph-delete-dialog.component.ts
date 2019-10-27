import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParagraph } from 'app/shared/model/paragraph.model';
import { ParagraphService } from './paragraph.service';

@Component({
  selector: 'jhi-paragraph-delete-dialog',
  templateUrl: './paragraph-delete-dialog.component.html'
})
export class ParagraphDeleteDialogComponent {
  paragraph: IParagraph;

  constructor(protected paragraphService: ParagraphService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.paragraphService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'paragraphListModification',
        content: 'Deleted an paragraph'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-paragraph-delete-popup',
  template: ''
})
export class ParagraphDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ paragraph }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ParagraphDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.paragraph = paragraph;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/paragraph', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/paragraph', { outlets: { popup: null } }]);
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
