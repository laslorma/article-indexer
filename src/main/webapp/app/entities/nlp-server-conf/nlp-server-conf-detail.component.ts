import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INlpServerConf } from 'app/shared/model/nlp-server-conf.model';

@Component({
  selector: 'jhi-nlp-server-conf-detail',
  templateUrl: './nlp-server-conf-detail.component.html'
})
export class NlpServerConfDetailComponent implements OnInit {
  nlpServerConf: INlpServerConf;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ nlpServerConf }) => {
      this.nlpServerConf = nlpServerConf;
    });
  }

  previousState() {
    window.history.back();
  }
}
