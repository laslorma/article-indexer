import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILingoToken } from 'app/shared/model/lingo-token.model';

@Component({
  selector: 'jhi-lingo-token-detail',
  templateUrl: './lingo-token-detail.component.html'
})
export class LingoTokenDetailComponent implements OnInit {
  lingoToken: ILingoToken;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ lingoToken }) => {
      this.lingoToken = lingoToken;
    });
  }

  previousState() {
    window.history.back();
  }
}
