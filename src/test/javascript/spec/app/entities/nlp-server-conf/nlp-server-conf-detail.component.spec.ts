import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { NlpServerConfDetailComponent } from 'app/entities/nlp-server-conf/nlp-server-conf-detail.component';
import { NlpServerConf } from 'app/shared/model/nlp-server-conf.model';

describe('Component Tests', () => {
  describe('NlpServerConf Management Detail Component', () => {
    let comp: NlpServerConfDetailComponent;
    let fixture: ComponentFixture<NlpServerConfDetailComponent>;
    const route = ({ data: of({ nlpServerConf: new NlpServerConf(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [NlpServerConfDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(NlpServerConfDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NlpServerConfDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nlpServerConf).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
