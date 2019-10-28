import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ILingoToken, LingoToken } from 'app/shared/model/lingo-token.model';
import { LingoTokenService } from './lingo-token.service';

@Component({
  selector: 'jhi-lingo-token-update',
  templateUrl: './lingo-token-update.component.html'
})
export class LingoTokenUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    text: [],
    blankText: [],
    lingoOrder: [],
    posTag: [],
    lemma: [],
    nerTag: []
  });

  constructor(protected lingoTokenService: LingoTokenService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ lingoToken }) => {
      this.updateForm(lingoToken);
    });
  }

  updateForm(lingoToken: ILingoToken) {
    this.editForm.patchValue({
      id: lingoToken.id,
      text: lingoToken.text,
      blankText: lingoToken.blankText,
      lingoOrder: lingoToken.lingoOrder,
      posTag: lingoToken.posTag,
      lemma: lingoToken.lemma,
      nerTag: lingoToken.nerTag
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const lingoToken = this.createFromForm();
    if (lingoToken.id !== undefined) {
      this.subscribeToSaveResponse(this.lingoTokenService.update(lingoToken));
    } else {
      this.subscribeToSaveResponse(this.lingoTokenService.create(lingoToken));
    }
  }

  private createFromForm(): ILingoToken {
    return {
      ...new LingoToken(),
      id: this.editForm.get(['id']).value,
      text: this.editForm.get(['text']).value,
      blankText: this.editForm.get(['blankText']).value,
      lingoOrder: this.editForm.get(['lingoOrder']).value,
      posTag: this.editForm.get(['posTag']).value,
      lemma: this.editForm.get(['lemma']).value,
      nerTag: this.editForm.get(['nerTag']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILingoToken>>) {
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
