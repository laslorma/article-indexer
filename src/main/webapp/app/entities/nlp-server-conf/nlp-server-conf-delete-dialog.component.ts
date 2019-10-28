import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INlpServerConf } from 'app/shared/model/nlp-server-conf.model';
import { NlpServerConfService } from './nlp-server-conf.service';

@Component({
  selector: 'jhi-nlp-server-conf-delete-dialog',
  templateUrl: './nlp-server-conf-delete-dialog.component.html'
})
export class NlpServerConfDeleteDialogComponent {
  nlpServerConf: INlpServerConf;

  constructor(
    protected nlpServerConfService: NlpServerConfService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.nlpServerConfService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'nlpServerConfListModification',
        content: 'Deleted an nlpServerConf'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-nlp-server-conf-delete-popup',
  template: ''
})
export class NlpServerConfDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ nlpServerConf }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(NlpServerConfDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.nlpServerConf = nlpServerConf;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/nlp-server-conf', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/nlp-server-conf', { outlets: { popup: null } }]);
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
