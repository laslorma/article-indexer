import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIndexConfiguration } from 'app/shared/model/index-configuration.model';
import { IndexConfigurationService } from './index-configuration.service';

@Component({
  selector: 'jhi-index-configuration-delete-dialog',
  templateUrl: './index-configuration-delete-dialog.component.html'
})
export class IndexConfigurationDeleteDialogComponent {
  indexConfiguration: IIndexConfiguration;

  constructor(
    protected indexConfigurationService: IndexConfigurationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.indexConfigurationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'indexConfigurationListModification',
        content: 'Deleted an indexConfiguration'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-index-configuration-delete-popup',
  template: ''
})
export class IndexConfigurationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ indexConfiguration }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IndexConfigurationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.indexConfiguration = indexConfiguration;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/index-configuration', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/index-configuration', { outlets: { popup: null } }]);
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
