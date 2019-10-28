import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIndexConfiguration } from 'app/shared/model/index-configuration.model';

@Component({
  selector: 'jhi-index-configuration-detail',
  templateUrl: './index-configuration-detail.component.html'
})
export class IndexConfigurationDetailComponent implements OnInit {
  indexConfiguration: IIndexConfiguration;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ indexConfiguration }) => {
      this.indexConfiguration = indexConfiguration;
    });
  }

  previousState() {
    window.history.back();
  }
}
