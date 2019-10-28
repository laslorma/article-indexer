import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IParagraph } from 'app/shared/model/paragraph.model';

@Component({
  selector: 'jhi-paragraph-detail',
  templateUrl: './paragraph-detail.component.html'
})
export class ParagraphDetailComponent implements OnInit {
  paragraph: IParagraph;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ paragraph }) => {
      this.paragraph = paragraph;
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
