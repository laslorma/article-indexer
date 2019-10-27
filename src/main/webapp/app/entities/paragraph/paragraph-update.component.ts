import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IParagraph, Paragraph } from 'app/shared/model/paragraph.model';
import { ParagraphService } from './paragraph.service';
import { IArticle } from 'app/shared/model/article.model';
import { ArticleService } from 'app/entities/article';

@Component({
  selector: 'jhi-paragraph-update',
  templateUrl: './paragraph-update.component.html'
})
export class ParagraphUpdateComponent implements OnInit {
  isSaving: boolean;

  articles: IArticle[];

  editForm = this.fb.group({
    id: [],
    content: [],
    originalCleanedContent: [],
    totalWords: [],
    header: [],
    readability: [],
    article: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected paragraphService: ParagraphService,
    protected articleService: ArticleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ paragraph }) => {
      this.updateForm(paragraph);
    });
    this.articleService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IArticle[]>) => mayBeOk.ok),
        map((response: HttpResponse<IArticle[]>) => response.body)
      )
      .subscribe((res: IArticle[]) => (this.articles = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(paragraph: IParagraph) {
    this.editForm.patchValue({
      id: paragraph.id,
      content: paragraph.content,
      originalCleanedContent: paragraph.originalCleanedContent,
      totalWords: paragraph.totalWords,
      header: paragraph.header,
      readability: paragraph.readability,
      article: paragraph.article
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
    const paragraph = this.createFromForm();
    if (paragraph.id !== undefined) {
      this.subscribeToSaveResponse(this.paragraphService.update(paragraph));
    } else {
      this.subscribeToSaveResponse(this.paragraphService.create(paragraph));
    }
  }

  private createFromForm(): IParagraph {
    return {
      ...new Paragraph(),
      id: this.editForm.get(['id']).value,
      content: this.editForm.get(['content']).value,
      originalCleanedContent: this.editForm.get(['originalCleanedContent']).value,
      totalWords: this.editForm.get(['totalWords']).value,
      header: this.editForm.get(['header']).value,
      readability: this.editForm.get(['readability']).value,
      article: this.editForm.get(['article']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParagraph>>) {
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
