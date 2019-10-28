import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { ParagraphDetailComponent } from 'app/entities/paragraph/paragraph-detail.component';
import { Paragraph } from 'app/shared/model/paragraph.model';

describe('Component Tests', () => {
  describe('Paragraph Management Detail Component', () => {
    let comp: ParagraphDetailComponent;
    let fixture: ComponentFixture<ParagraphDetailComponent>;
    const route = ({ data: of({ paragraph: new Paragraph(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [ParagraphDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ParagraphDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParagraphDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paragraph).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
