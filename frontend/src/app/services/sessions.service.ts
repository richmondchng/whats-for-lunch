import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { ResponseSessions, SessionBody, ResponseDeleteSession, ResponseAddRestaurant, ResponseSelectRestaurant } from '../interfaces/ResponseDetails';
import { Restaruant } from '../interfaces/Restaurant';
import { environment } from 'src/environments/environment';
import { DatePipe } from '@angular/common';
import { Session } from '../interfaces/Session';

@Injectable({
  providedIn: 'root'
})
export class SessionsService {

  constructor(private http: HttpClient) { }

  getSessionForUser() : Observable<SessionBody[]> {

      const url = `${environment.apiUrl}/sessions`;
      const headers = new HttpHeaders({
        'Authorization' : 'Bearer ' + localStorage.getItem("token"),
        'Content-Type' : 'application/json'
      });
      return this.http.get<ResponseSessions>(url, {headers: headers})
      .pipe(
        map((response:ResponseSessions) => {
          return response.data;
        }),
        catchError(this.handleError)
      );
  }

  getSession(id: number) : Observable<SessionBody> {

    const url = `${environment.apiUrl}/sessions/${id}`;
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    return this.http.get<ResponseSessions>(url, {headers: headers})
    .pipe(
      map((response:ResponseSessions) => {
        return response.data[0];
      }),
      catchError(this.handleError)
    );
  }

  deleteSession(session: SessionBody): Observable<ResponseDeleteSession> {
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    const url = `${environment.apiUrl}/sessions/${session.id}`;
    return this.http.delete<ResponseDeleteSession>(url, {headers: headers}).pipe(
      catchError(this.handleError));
  }

  addRestaurantToSession(sessionId:number, restaurant: Restaruant) : Observable<ResponseAddRestaurant> {
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    const url = `${environment.apiUrl}/sessions/${sessionId}/restaurants`;
    return this.http.post<ResponseAddRestaurant>(url, 
      { restaurant: restaurant.name, description: restaurant.description},
      { headers: headers }).pipe(
        catchError(this.handleError));
  }

  selectRestaurantForSession(sessionId: number) {
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    const url = `${environment.apiUrl}/sessions/${sessionId}`;
    return this.http.patch<ResponseSelectRestaurant>(url, 
      { strategy: "RANDOM" }, { headers: headers }).pipe(
        catchError(this.handleError));
  }

  createSession(session: Session) : Observable<ResponseSessions> {
    const datepipe = new DatePipe('en-UK');
    const formattedDate = datepipe.transform(session.date, 'yyyy-MM-dd');

    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    const url = `${environment.apiUrl}/sessions`;
    return this.http.post<any>(url, 
      {
        date: formattedDate,
        participants: session.participants
      },
      { headers: headers }).pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    let msg = 'Something bad happened; please try again later.';
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(`Backend returned code ${error.status}, body was: `, error.error);
      if(error.error && error.error.message) {
        msg = "Error: " + error.error.message;
      }
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error(msg));
  }
}
