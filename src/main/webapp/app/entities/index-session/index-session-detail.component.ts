import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IIndexSession } from 'app/shared/model/index-session.model';

@Component({
  selector: 'jhi-index-session-detail',
  templateUrl: './index-session-detail.component.html'
})
export class IndexSessionDetailComponent implements OnInit {
  indexSession: IIndexSession;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ indexSession }) => {
      this.indexSession = indexSession;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
