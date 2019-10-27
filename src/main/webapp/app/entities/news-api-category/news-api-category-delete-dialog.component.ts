import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INewsApiCategory } from 'app/shared/model/news-api-category.model';
import { NewsApiCategoryService } from './news-api-category.service';

@Component({
  selector: 'jhi-news-api-category-delete-dialog',
  templateUrl: './news-api-category-delete-dialog.component.html'
})
export class NewsApiCategoryDeleteDialogComponent {
  newsApiCategory: INewsApiCategory;

  constructor(
    protected newsApiCategoryService: NewsApiCategoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.newsApiCategoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'newsApiCategoryListModification',
        content: 'Deleted an newsApiCategory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-news-api-category-delete-popup',
  template: ''
})
export class NewsApiCategoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ newsApiCategory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(NewsApiCategoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.newsApiCategory = newsApiCategory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/news-api-category', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/news-api-category', { outlets: { popup: null } }]);
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
