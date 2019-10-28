import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { PartDetailComponent } from 'app/entities/part/part-detail.component';
import { Part } from 'app/shared/model/part.model';

describe('Component Tests', () => {
  describe('Part Management Detail Component', () => {
    let comp: PartDetailComponent;
    let fixture: ComponentFixture<PartDetailComponent>;
    const route = ({ data: of({ part: new Part(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [PartDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PartDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PartDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.part).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
