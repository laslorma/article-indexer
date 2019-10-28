import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndexerTestModule } from '../../../test.module';
import { IndexConfigurationDetailComponent } from 'app/entities/index-configuration/index-configuration-detail.component';
import { IndexConfiguration } from 'app/shared/model/index-configuration.model';

describe('Component Tests', () => {
  describe('IndexConfiguration Management Detail Component', () => {
    let comp: IndexConfigurationDetailComponent;
    let fixture: ComponentFixture<IndexConfigurationDetailComponent>;
    const route = ({ data: of({ indexConfiguration: new IndexConfiguration(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [IndexConfigurationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IndexConfigurationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndexConfigurationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.indexConfiguration).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
