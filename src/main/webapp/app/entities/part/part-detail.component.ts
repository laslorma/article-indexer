import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPart } from 'app/shared/model/part.model';

@Component({
  selector: 'jhi-part-detail',
  templateUrl: './part-detail.component.html'
})
export class PartDetailComponent implements OnInit {
  part: IPart;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ part }) => {
      this.part = part;
    });
  }

  previousState() {
    window.history.back();
  }
}
