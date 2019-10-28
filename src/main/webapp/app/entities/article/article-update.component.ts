import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IArticle, Article } from 'app/shared/model/article.model';
import { ArticleService } from './article.service';
import { INewsApiCategory } from 'app/shared/model/news-api-category.model';
import { NewsApiCategoryService } from 'app/entities/news-api-category';
import { ISource } from 'app/shared/model/source.model';
import { SourceService } from 'app/entities/source';

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html'
})
export class ArticleUpdateComponent implements OnInit {
  isSaving: boolean;

  newsapicategories: INewsApiCategory[];

  sources: ISource[];

  editForm = this.fb.group({
    id: [],
    author: [],
    title: [null, [Validators.maxLength(1000)]],
    description: [],
    url: [null, [Validators.maxLength(2000)]],
    urlToImage: [null, [Validators.maxLength(2000)]],
    publishedAt: [],
    category: [],
    content: [],
    countryCode: [null, [Validators.minLength(2), Validators.maxLength(2)]],
    languageCode: [null, [Validators.minLength(2), Validators.maxLength(2)]],
    sentiment: [],
    textReadability: [],
    numberOfParts: [],
    newsApiCategory: [],
    source: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected articleService: ArticleService,
    protected newsApiCategoryService: NewsApiCategoryService,
    protected sourceService: SourceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ article }) => {
      this.updateForm(article);
    });
    this.newsApiCategoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<INewsApiCategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<INewsApiCategory[]>) => response.body)
      )
      .subscribe((res: INewsApiCategory[]) => (this.newsapicategories = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.sourceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISource[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISource[]>) => response.body)
      )
      .subscribe((res: ISource[]) => (this.sources = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(article: IArticle) {
    this.editForm.patchValue({
      id: article.id,
      author: article.author,
      title: article.title,
      description: article.description,
      url: article.url,
      urlToImage: article.urlToImage,
      publishedAt: article.publishedAt,
      category: article.category,
      content: article.content,
      countryCode: article.countryCode,
      languageCode: article.languageCode,
      sentiment: article.sentiment,
      textReadability: article.textReadability,
      numberOfParts: article.numberOfParts,
      newsApiCategory: article.newsApiCategory,
      source: article.source
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const article = this.createFromForm();
    if (article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  private createFromForm(): IArticle {
    return {
      ...new Article(),
      id: this.editForm.get(['id']).value,
      author: this.editForm.get(['author']).value,
      title: this.editForm.get(['title']).value,
      description: this.editForm.get(['description']).value,
      url: this.editForm.get(['url']).value,
      urlToImage: this.editForm.get(['urlToImage']).value,
      publishedAt: this.editForm.get(['publishedAt']).value,
      category: this.editForm.get(['category']).value,
      content: this.editForm.get(['content']).value,
      countryCode: this.editForm.get(['countryCode']).value,
      languageCode: this.editForm.get(['languageCode']).value,
      sentiment: this.editForm.get(['sentiment']).value,
      textReadability: this.editForm.get(['textReadability']).value,
      numberOfParts: this.editForm.get(['numberOfParts']).value,
      newsApiCategory: this.editForm.get(['newsApiCategory']).value,
      source: this.editForm.get(['source']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>) {
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

  trackNewsApiCategoryById(index: number, item: INewsApiCategory) {
    return item.id;
  }

  trackSourceById(index: number, item: ISource) {
    return item.id;
  }
}
