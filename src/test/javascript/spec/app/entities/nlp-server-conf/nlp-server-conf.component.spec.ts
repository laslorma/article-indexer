import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IndexerTestModule } from '../../../test.module';
import { NlpServerConfComponent } from 'app/entities/nlp-server-conf/nlp-server-conf.component';
import { NlpServerConfService } from 'app/entities/nlp-server-conf/nlp-server-conf.service';
import { NlpServerConf } from 'app/shared/model/nlp-server-conf.model';

describe('Component Tests', () => {
  describe('NlpServerConf Management Component', () => {
    let comp: NlpServerConfComponent;
    let fixture: ComponentFixture<NlpServerConfComponent>;
    let service: NlpServerConfService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NlpServerConfComponent],
        providers: []
      })
        .overrideTemplate(NlpServerConfComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NlpServerConfComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NlpServerConfService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new NlpServerConf(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.nlpServerConfs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
