import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IndexSessionService } from 'app/entities/index-session/index-session.service';
import { IIndexSession, IndexSession } from 'app/shared/model/index-session.model';

describe('Service Tests', () => {
  describe('IndexSession Service', () => {
    let injector: TestBed;
    let service: IndexSessionService;
    let httpMock: HttpTestingController;
    let elemDefault: IIndexSession;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(IndexSessionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new IndexSession(0, 0, 0, currentDate, currentDate, 0, 0, false, 0, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            started: currentDate.format(DATE_TIME_FORMAT),
            ended: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a IndexSession', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            started: currentDate.format(DATE_TIME_FORMAT),
            ended: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            started: currentDate,
            ended: currentDate
          },
          returnedFromService
        );
        service
          .create(new IndexSession(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a IndexSession', () => {
        const returnedFromService = Object.assign(
          {
            newsApiCalls: 1,
            fiveFilterApiCalls: 1,
            started: currentDate.format(DATE_TIME_FORMAT),
            ended: currentDate.format(DATE_TIME_FORMAT),
            duration: 1,
            totalArticles: 1,
            indexing: true,
            articlesSaved: 1,
            errorMessage: 'BBBBBB',
            hadError: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            started: currentDate,
            ended: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of IndexSession', () => {
        const returnedFromService = Object.assign(
          {
            newsApiCalls: 1,
            fiveFilterApiCalls: 1,
            started: currentDate.format(DATE_TIME_FORMAT),
            ended: currentDate.format(DATE_TIME_FORMAT),
            duration: 1,
            totalArticles: 1,
            indexing: true,
            articlesSaved: 1,
            errorMessage: 'BBBBBB',
            hadError: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            started: currentDate,
            ended: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a IndexSession', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
