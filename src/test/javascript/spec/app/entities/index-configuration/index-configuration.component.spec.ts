/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IndexerTestModule } from '../../../test.module';
import { IndexConfigurationComponent } from 'app/entities/index-configuration/index-configuration.component';
import { IndexConfigurationService } from 'app/entities/index-configuration/index-configuration.service';
import { IndexConfiguration } from 'app/shared/model/index-configuration.model';

describe('Component Tests', () => {
  describe('IndexConfiguration Management Component', () => {
    let comp: IndexConfigurationComponent;
    let fixture: ComponentFixture<IndexConfigurationComponent>;
    let service: IndexConfigurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IndexerTestModule],
        declarations: [IndexConfigurationComponent],
        providers: []
      })
        .overrideTemplate(IndexConfigurationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IndexConfigurationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IndexConfigurationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new IndexConfiguration(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.indexConfigurations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
