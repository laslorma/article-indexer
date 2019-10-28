/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IndexerTestModule } from '../../../test.module';
import { LingoTokenComponent } from 'app/entities/lingo-token/lingo-token.component';
import { LingoTokenService } from 'app/entities/lingo-token/lingo-token.service';
import { LingoToken } from 'app/shared/model/lingo-token.model';

describe('Component Tests', () => {
  describe('LingoToken Management Component', () => {
    let comp: LingoTokenComponent;
    let fixture: ComponentFixture<LingoTokenComponent>;
    let service: LingoTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [LingoTokenComponent],
        providers: []
      })
        .overrideTemplate(LingoTokenComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LingoTokenComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LingoTokenService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LingoToken(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lingoTokens[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
