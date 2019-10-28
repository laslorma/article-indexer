import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ISource, Source } from 'app/shared/model/source.model';
import { SourceService } from './source.service';

@Component({
  selector: 'jhi-source-update',
  templateUrl: './source-update.component.html'
})
export class SourceUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    sourceId: [],
    name: [],
    description: [],
    url: [],
    category: [],
    language: [],
    country: [],
    active: []
  });

  constructor(protected sourceService: SourceService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ source }) => {
      this.updateForm(source);
    });
  }

  updateForm(source: ISource) {
    this.editForm.patchValue({
      id: source.id,
      sourceId: source.sourceId,
      name: source.name,
      description: source.description,
      url: source.url,
      category: source.category,
      language: source.language,
      country: source.country,
      active: source.active
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const source = this.createFromForm();
    if (source.id !== undefined) {
      this.subscribeToSaveResponse(this.sourceService.update(source));
    } else {
      this.subscribeToSaveResponse(this.sourceService.create(source));
    }
  }

  private createFromForm(): ISource {
    return {
      ...new Source(),
      id: this.editForm.get(['id']).value,
      sourceId: this.editForm.get(['sourceId']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      url: this.editForm.get(['url']).value,
      category: this.editForm.get(['category']).value,
      language: this.editForm.get(['language']).value,
      country: this.editForm.get(['country']).value,
      active: this.editForm.get(['active']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISource>>) {
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
