import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { IndexSessionDetailComponent } from 'app/entities/index-session/index-session-detail.component';
import { IndexSession } from 'app/shared/model/index-session.model';

describe('Component Tests', () => {
  describe('IndexSession Management Detail Component', () => {
    let comp: IndexSessionDetailComponent;
    let fixture: ComponentFixture<IndexSessionDetailComponent>;
    const route = ({ data: of({ indexSession: new IndexSession(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [IndexSessionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IndexSessionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndexSessionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.indexSession).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
