import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IIndexSession, IndexSession } from 'app/shared/model/index-session.model';
import { IndexSessionService } from './index-session.service';

@Component({
  selector: 'jhi-index-session-update',
  templateUrl: './index-session-update.component.html'
})
export class IndexSessionUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    newsApiCalls: [],
    fiveFilterApiCalls: [],
    started: [],
    ended: [],
    duration: [],
    totalArticles: [],
    indexing: [],
    articlesSaved: [],
    errorMessage: [],
    hadError: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected indexSessionService: IndexSessionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ indexSession }) => {
      this.updateForm(indexSession);
    });
  }

  updateForm(indexSession: IIndexSession) {
    this.editForm.patchValue({
      id: indexSession.id,
      newsApiCalls: indexSession.newsApiCalls,
      fiveFilterApiCalls: indexSession.fiveFilterApiCalls,
      started: indexSession.started != null ? indexSession.started.format(DATE_TIME_FORMAT) : null,
      ended: indexSession.ended != null ? indexSession.ended.format(DATE_TIME_FORMAT) : null,
      duration: indexSession.duration,
      totalArticles: indexSession.totalArticles,
      indexing: indexSession.indexing,
      articlesSaved: indexSession.articlesSaved,
      errorMessage: indexSession.errorMessage,
      hadError: indexSession.hadError
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
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
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
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const indexSession = this.createFromForm();
    if (indexSession.id !== undefined) {
      this.subscribeToSaveResponse(this.indexSessionService.update(indexSession));
    } else {
      this.subscribeToSaveResponse(this.indexSessionService.create(indexSession));
    }
  }

  private createFromForm(): IIndexSession {
    return {
      ...new IndexSession(),
      id: this.editForm.get(['id']).value,
      newsApiCalls: this.editForm.get(['newsApiCalls']).value,
      fiveFilterApiCalls: this.editForm.get(['fiveFilterApiCalls']).value,
      started: this.editForm.get(['started']).value != null ? moment(this.editForm.get(['started']).value, DATE_TIME_FORMAT) : undefined,
      ended: this.editForm.get(['ended']).value != null ? moment(this.editForm.get(['ended']).value, DATE_TIME_FORMAT) : undefined,
      duration: this.editForm.get(['duration']).value,
      totalArticles: this.editForm.get(['totalArticles']).value,
      indexing: this.editForm.get(['indexing']).value,
      articlesSaved: this.editForm.get(['articlesSaved']).value,
      errorMessage: this.editForm.get(['errorMessage']).value,
      hadError: this.editForm.get(['hadError']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndexSession>>) {
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
}
