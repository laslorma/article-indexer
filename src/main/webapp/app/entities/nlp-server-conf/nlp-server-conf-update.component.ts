import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { INlpServerConf, NlpServerConf } from 'app/shared/model/nlp-server-conf.model';
import { NlpServerConfService } from './nlp-server-conf.service';

@Component({
  selector: 'jhi-nlp-server-conf-update',
  templateUrl: './nlp-server-conf-update.component.html'
})
export class NlpServerConfUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    url: [],
    port: []
  });

  constructor(protected nlpServerConfService: NlpServerConfService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ nlpServerConf }) => {
      this.updateForm(nlpServerConf);
    });
  }

  updateForm(nlpServerConf: INlpServerConf) {
    this.editForm.patchValue({
      id: nlpServerConf.id,
      url: nlpServerConf.url,
      port: nlpServerConf.port
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const nlpServerConf = this.createFromForm();
    if (nlpServerConf.id !== undefined) {
      this.subscribeToSaveResponse(this.nlpServerConfService.update(nlpServerConf));
    } else {
      this.subscribeToSaveResponse(this.nlpServerConfService.create(nlpServerConf));
    }
  }

  private createFromForm(): INlpServerConf {
    return {
      ...new NlpServerConf(),
      id: this.editForm.get(['id']).value,
      url: this.editForm.get(['url']).value,
      port: this.editForm.get(['port']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INlpServerConf>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
