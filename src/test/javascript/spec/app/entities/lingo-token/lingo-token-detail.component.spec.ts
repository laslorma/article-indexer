import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { LingoTokenDetailComponent } from 'app/entities/lingo-token/lingo-token-detail.component';
import { LingoToken } from 'app/shared/model/lingo-token.model';

describe('Component Tests', () => {
  describe('LingoToken Management Detail Component', () => {
    let comp: LingoTokenDetailComponent;
    let fixture: ComponentFixture<LingoTokenDetailComponent>;
    const route = ({ data: of({ lingoToken: new LingoToken(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [LingoTokenDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LingoTokenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LingoTokenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lingoToken).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
