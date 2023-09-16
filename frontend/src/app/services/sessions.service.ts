import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ResponseSessions, SessionBody, ResponseDeleteSession, ResponseAddRestaurant } from '../interfaces/ResponseDetails';
import { Restaruant } from '../interfaces/Restaurant';
import { environment } from 'src/environments/environment';

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
        })
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
      })
    );
  }

  deleteSession(session: SessionBody): Observable<ResponseDeleteSession> {
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    const url = `${environment.apiUrl}/sessions/${session.id}`;
    return this.http.delete<ResponseDeleteSession>(url, {headers: headers});
  }

  addRestaurantToSession(sessionId:number, restaurant: Restaruant) : Observable<ResponseAddRestaurant> {
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    const url = `${environment.apiUrl}/sessions/${sessionId}/restaurants`;
    return this.http.post<ResponseAddRestaurant>(url, 
      { restaurant: restaurant.name, description: restaurant.description},
      { headers: headers });
  }
}
