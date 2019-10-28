import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IIndexConfiguration, IndexConfiguration } from 'app/shared/model/index-configuration.model';
import { IndexConfigurationService } from './index-configuration.service';

@Component({
  selector: 'jhi-index-configuration-update',
  templateUrl: './index-configuration-update.component.html'
})
export class IndexConfigurationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    generateCorpuses: [],
    corpusesOutputPath: [],
    newsApiKey: [],
    activateAllCategoriesAndCountries: []
  });

  constructor(
    protected indexConfigurationService: IndexConfigurationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ indexConfiguration }) => {
      this.updateForm(indexConfiguration);
    });
  }

  updateForm(indexConfiguration: IIndexConfiguration) {
    this.editForm.patchValue({
      id: indexConfiguration.id,
      generateCorpuses: indexConfiguration.generateCorpuses,
      corpusesOutputPath: indexConfiguration.corpusesOutputPath,
      newsApiKey: indexConfiguration.newsApiKey,
      activateAllCategoriesAndCountries: indexConfiguration.activateAllCategoriesAndCountries
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const indexConfiguration = this.createFromForm();
    if (indexConfiguration.id !== undefined) {
      this.subscribeToSaveResponse(this.indexConfigurationService.update(indexConfiguration));
    } else {
      this.subscribeToSaveResponse(this.indexConfigurationService.create(indexConfiguration));
    }
  }

  private createFromForm(): IIndexConfiguration {
    return {
      ...new IndexConfiguration(),
      id: this.editForm.get(['id']).value,
      generateCorpuses: this.editForm.get(['generateCorpuses']).value,
      corpusesOutputPath: this.editForm.get(['corpusesOutputPath']).value,
      newsApiKey: this.editForm.get(['newsApiKey']).value,
      activateAllCategoriesAndCountries: this.editForm.get(['activateAllCategoriesAndCountries']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndexConfiguration>>) {
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
