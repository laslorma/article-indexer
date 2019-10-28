import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPart, Part } from 'app/shared/model/part.model';
import { PartService } from './part.service';
import { IArticle } from 'app/shared/model/article.model';
import { ArticleService } from 'app/entities/article/article.service';

@Component({
  selector: 'jhi-part-update',
  templateUrl: './part-update.component.html'
})
export class PartUpdateComponent implements OnInit {
  isSaving: boolean;

  articles: IArticle[];

  editForm = this.fb.group({
    id: [],
    text: [null, [Validators.maxLength(500)]],
    posibleOptions: [],
    article: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected partService: PartService,
    protected articleService: ArticleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ part }) => {
      this.updateForm(part);
    });
    this.articleService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IArticle[]>) => mayBeOk.ok),
        map((response: HttpResponse<IArticle[]>) => response.body)
      )
      .subscribe((res: IArticle[]) => (this.articles = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(part: IPart) {
    this.editForm.patchValue({
      id: part.id,
      text: part.text,
      posibleOptions: part.posibleOptions,
      article: part.article
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const part = this.createFromForm();
    if (part.id !== undefined) {
      this.subscribeToSaveResponse(this.partService.update(part));
    } else {
      this.subscribeToSaveResponse(this.partService.create(part));
    }
  }

  private createFromForm(): IPart {
    return {
      ...new Part(),
      id: this.editForm.get(['id']).value,
      text: this.editForm.get(['text']).value,
      posibleOptions: this.editForm.get(['posibleOptions']).value,
      article: this.editForm.get(['article']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPart>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackArticleById(index: number, item: IArticle) {
    return item.id;
  }
}
